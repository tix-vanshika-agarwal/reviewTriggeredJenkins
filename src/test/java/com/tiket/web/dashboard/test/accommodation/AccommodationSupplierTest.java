package com.tiket.web.dashboard.test.accommodation;

import com.tiket.web.dashboard.page.accommodation.internal_supplier.user_management.InternalSupplierUserManagementPage;
import com.tiket.web.dashboard.page.accommodation.external_supplier.dashboard.LoginPage;
import com.tiket.web.dashboard.page.accommodation.external_supplier.dashboard.OverviewPage;
import com.tiket.web.dashboard.page.accommodation.external_supplier.dashboard.SidebarPage;
import com.tiket.web.dashboard.page.accommodation.external_supplier.dashboard.UserListPage;
import com.tiket.web.dashboard.page.member_dashboard.MemberLoginPage;
import com.tiket.web.dashboard.page.accommodation.internal_supplier.dashboard.InternalSupplierDashboardPage;
import com.tiket.web.dashboard.page.accommodation.external_supplier.properties_mapping.ExternalSupplierPropertiesMappingPage;
import com.tiket.web.dashboard.page.accommodation.external_supplier.rooms_mapping.ExternalSupplierRoomsMappingPage;
import com.tiket.web.dashboard.page.accommodation.internal_supplier.properties_mapping.InternalSupplierPropertiesMappingPage;
import com.tiket.web.dashboard.page.common.LoginMemberAdmin;
import com.tiket.apiPage.HotelApi;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import com.tiket.annotation.*;
import com.tiket.annotation.Module;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.WebTest;

import static com.tiket.core.CoreFunctions.randomNameWithoutTest;
import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.Matchers.equalToIgnoringCase;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AccommodationSupplierTest extends WebTest {
    
    @Author(names = {"Nasha","Riya Bajpai"})
    @Module(name = "Rooms Mapping")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyFilterAndSortMapRoomsFilterMappedUnmappedRoomsAndRoomMappingLog() {
        Map<String, String> param = getParam("verifyFilterAndSortMapRoomsFilterMappedUnmappedRoomsAndRoomMappingLog");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        InternalSupplierDashboardPage<?> internalSupplierDashboardPage = InternalSupplierDashboardPage.getInstance(session);
        String email = param.get("email");
        String password = param.get("password");
        String vendorName = param.get("vendorName");
        String vendorPropertyId = param.get("vendorPropertyId");
        String mappedRoomName = param.get("mappedRoomName");
        String mappedRoomId = param.get("mappedRoomId");
        String expectedSuccessMessageUnmap = param.get("expectedSuccessMessageUnmap");
        String mappedRoomGroupId = param.get("mappedRoomGroupId");
        // true while the test room is unmapped; the finally block maps it back if the test dies in between
        boolean roomLeftUnmapped = false;

        try {

           step("User Log in to Panel dashboard");
            memberLoginPage.gotoMemberLoginPage();
            memberLoginPage.adminLogin(email, password);
            loginMemberAdmin.gotoHome();

            step("Go to Internal Supplier Dashboard");
            loginMemberAdmin.goToAccommodationSupplierDashboard();
            internalSupplierDashboardPage.goToMapRoomsPage();

            step("[Rooms Mapping][Map Rooms] Verify internal user is able to see and filter property list on map rooms page by vendor and vendor property id.");
            internalSupplierDashboardPage.clickVendorButton();
            internalSupplierDashboardPage.filterByVendorName(vendorName);
            internalSupplierDashboardPage.inputVendorPropertyId(vendorPropertyId);
            internalSupplierDashboardPage.clickSearch();
            Assertion.assertThat("Filtered Property ID should be visible in the table", internalSupplierDashboardPage.isTextPresentInTable(vendorPropertyId), is(true));

            step("[Rooms Mapping][Map Rooms] Verify internal user is able to sort property list by room mapping status percentage ascendingly/descendingly");
            internalSupplierDashboardPage.clickLocationButton();
            double beforeSort = internalSupplierDashboardPage.getFirstMappingStatusValue();
            internalSupplierDashboardPage.sortMappingStatus();
            double afterSort = internalSupplierDashboardPage.getFirstMappingStatusValue();
            Assertion.assertThat("Property list should be sorted by mapping percentage status", internalSupplierDashboardPage.isSorted(beforeSort, afterSort, false), is(true));
        
            step("[Rooms Mapping][Map Rooms] Verify internal user is able to filter mapped room list by room name");
            internalSupplierDashboardPage.clickVendorButton();
            internalSupplierDashboardPage.inputVendorPropertyId(vendorPropertyId);
            internalSupplierDashboardPage.clickSearch();
            internalSupplierDashboardPage.clickMapButtonByPropertyId(vendorPropertyId);
            internalSupplierDashboardPage.searchByVendorRoomName("Mapped List", mappedRoomName);
            Assertion.assertThat("Filtered mapped room name should be visible", internalSupplierDashboardPage.isTextPresentInTableMap("Mapped List", mappedRoomName), is(true));

            step("[Rooms Mapping][Map Rooms] Verify internal user is able to filter mapped room list by vendor room id");
            internalSupplierDashboardPage.clickSegmentedButton("Mapped List", "ID");
            internalSupplierDashboardPage.searchByVendorRoomId("Mapped List", mappedRoomId);
            Assertion.assertThat("Filtered mapped room ID should be visible in Mapped table", internalSupplierDashboardPage.isTextPresentInTableMap("Mapped List", mappedRoomId), is(true));

            step("[Rooms Mapping][Map Rooms] Verify internal user is able to unmap room from existing room group");
            roomLeftUnmapped = true;
            internalSupplierDashboardPage.clickUnmapByRoomId(mappedRoomId);
            internalSupplierDashboardPage.clickContinueOnPopup();
            Assertion.assertThat("Snackbar message should confirm success unmapping", internalSupplierDashboardPage.verifySnackbarMessage(expectedSuccessMessageUnmap), is(true));

            step("[Post condition] Map the unmapped room back to room group");
            internalSupplierDashboardPage.searchByVendorRoomName("Unmapped Rooms", mappedRoomName);
            internalSupplierDashboardPage.selectRoomById(mappedRoomId);
            internalSupplierDashboardPage.clickMapButtonByRoomGroupId(mappedRoomGroupId);
            internalSupplierDashboardPage.clickContinueOnPopup();
            roomLeftUnmapped = false;
        
            step("[Rooms Mapping][Rooms Mapping Log] Verify internal user is able to see and filter room mapping log by vendor, vendor property id, and vendor room id");
            internalSupplierDashboardPage.goToRoomsMappingLogPage();
            internalSupplierDashboardPage.filterByVendorName(vendorName);
            internalSupplierDashboardPage.inputVendorPropertyIdInLog(vendorPropertyId);
            internalSupplierDashboardPage.inputVendorRoomIdInLog(mappedRoomId);
            internalSupplierDashboardPage.clickSearch();
            Assertion.assertThat("Filtered logs matching criteria should appear (vendor property id)", internalSupplierDashboardPage.isTextPresentInTable(vendorPropertyId), is(true));
            Assertion.assertThat("Filtered logs matching criteria should appear (vendor room id)", internalSupplierDashboardPage.isTextPresentInTable(mappedRoomId), is(true));

            step("[Rooms Mapping][Rooms Mapping Log] Verify internal user is able to see and filter room mapping log by vendor and start & end date");
            internalSupplierDashboardPage.clickResetButton();
            internalSupplierDashboardPage.filterByVendorName(vendorName);
            internalSupplierDashboardPage.selectDateRangeInLog(-1, 0);
            internalSupplierDashboardPage.clickSearch();
            Assertion.assertThat("Filtered logs matching criteria should appear (vendor)", internalSupplierDashboardPage.isTextPresentInTableHeader(vendorName), is(true));
            Assertion.assertThat("Filtered logs matching criteria should appear (start or end date)", internalSupplierDashboardPage.isDatePresentInTable(-1) || internalSupplierDashboardPage.isDatePresentInTable(0), is(true));
    
        } finally {
            if (roomLeftUnmapped) {
                // Cleanup: test failed while the room was unmapped - map it back so the next run starts clean
                try {
                    step("[Cleanup] Test failed with the room unmapped - map it back to its room group");
                    internalSupplierDashboardPage.goToMapRoomsPage();
                    internalSupplierDashboardPage.clickVendorButton();
                    internalSupplierDashboardPage.filterByVendorName(vendorName);
                    internalSupplierDashboardPage.inputVendorPropertyId(vendorPropertyId);
                    internalSupplierDashboardPage.clickSearch();
                    internalSupplierDashboardPage.clickMapButtonByPropertyId(vendorPropertyId);
                    internalSupplierDashboardPage.searchByVendorRoomName("Unmapped Rooms", mappedRoomName);
                    if (internalSupplierDashboardPage.isTextPresentInTableMap("Unmapped Rooms", mappedRoomId)) {
                        internalSupplierDashboardPage.selectRoomById(mappedRoomId);
                        internalSupplierDashboardPage.clickMapButtonByRoomGroupId(mappedRoomGroupId);
                        internalSupplierDashboardPage.clickContinueOnPopup();
                    }
                } catch (Exception e) {
                    System.err.println("[Cleanup] Could not map the room back, please check it manually: " + e);
                }
            }
        }
    }

    @Author(names = {"Yazid"})
    @Module(name = "Properties Mapping")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testVerifyInternalMapPropertyByUrl() {
        Map<String, String> param = getParam("testVerifyInternalMapPropertyByUrl");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        InternalSupplierDashboardPage<?> internalSupplierDashboardPage = InternalSupplierDashboardPage.getInstance(session);
        InternalSupplierPropertiesMappingPage<?> internalSupplierPropertiesMappingPage = new InternalSupplierPropertiesMappingPage<>(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String vendorName = param.get("vendorName");
        String expectedSuccessMessage = param.get("expectedSuccessMessage");
        String accommodationType = param.getOrDefault("accommodationType", "HOTEL");

        step("Login to Internal Supplier Dashboard overview");
        memberLoginPage.gotoMemberLoginPage();
        memberLoginPage.adminLogin(email, password);
        loginMemberAdmin.gotoHome();
        loginMemberAdmin.goToAccommodationSupplierDashboard();
        internalSupplierDashboardPage.goToOverviewPage();
        Assertion.assertThat("Overview page is loaded", internalSupplierDashboardPage.isOverviewPageLoaded(), is(true));

        step("Go to Properties Mapping > Map Properties");
        internalSupplierPropertiesMappingPage.goToMapPropertiesPage();
        Assertion.assertThat("Map Properties page is loaded", internalSupplierPropertiesMappingPage.isMapPropertiesPageLoaded(), is(true));

        step("Resolve vendor property from unmapped hotels API via hotel autocomplete");
        HotelApi.ResolvedUnmappedProperty resolvedProperty = hotelApi.resolveVendorPropertyFromVendorNames(
                vendorName, "", "", accommodationType);
        String resolvedVendorName = resolvedProperty.vendorName();
        String vendorPropertyId = resolvedProperty.vendorPropertyId();
        String mappedPropertyName = resolvedProperty.propertyName();
        String existingPdpUrl = resolvedProperty.pdpUrl();

        step("Filter property list by vendor name resolved from API before searching vendor property id");
        internalSupplierPropertiesMappingPage.filterByVendorName(resolvedVendorName);

        step("Search vendor property id and verify property name in table");
        internalSupplierPropertiesMappingPage.searchByVendorPropertyId(vendorPropertyId);
        String propertyNameInTable = internalSupplierPropertiesMappingPage.getPropertyNameByVendorPropertyId(vendorPropertyId);
        Assertion.assertThat(
                "Property name in table should match API property name",
                propertyNameInTable,
                equalToIgnoringCase(mappedPropertyName));

        step("[Properties Mapping][Map Properties] Verify internal user is able to map property by proposing existing property's PDP URL");
        internalSupplierPropertiesMappingPage.clickSelectActionMapByVendorPropertyId(vendorPropertyId);
        internalSupplierPropertiesMappingPage.clickProposeAnotherProperty();
        internalSupplierPropertiesMappingPage.inputPropertyUrl(existingPdpUrl);
        internalSupplierPropertiesMappingPage.confirmMerging();
        Assertion.assertThat("Property mapped successfully by PDP URL", internalSupplierDashboardPage.verifySnackbarMessage(expectedSuccessMessage), is(true));
        internalSupplierPropertiesMappingPage.switchToSupplierDashboardTab();

        step("Go to Properties Mapping > Properties Mapping Log");
        internalSupplierPropertiesMappingPage.goToPropertiesMappingLogPage();
        Assertion.assertThat("Properties Mapping Log page is loaded", internalSupplierPropertiesMappingPage.isPropertiesMappingLogPageLoaded(), is(true));

        step("[Properties Mapping][Properties Mapping Log] Verify internal user is able to see and filter property mapping log by vendor and start & end date");
        internalSupplierPropertiesMappingPage.filterByVendorName(resolvedVendorName);
        internalSupplierDashboardPage.selectDateRangeInLog(-1, 0);
        internalSupplierPropertiesMappingPage.inputVendorPropertyIdInLog(vendorPropertyId);
        internalSupplierDashboardPage.clickSearch();
        Assertion.assertThat("Filtered logs show today's date in results", internalSupplierPropertiesMappingPage.isDatePresentInTable(0), is(true));

        step("[Properties Mapping][Properties Mapping Log] Verify property mapping log is presented correctly after internal user map property by proposing existing property's PDP URL");
        Assertion.assertThat("Property mapping log contains vendor property id", internalSupplierDashboardPage.isTextPresentInTable(vendorPropertyId), is(true));
        Assertion.assertThat("Property mapping log contains mapped property name", internalSupplierDashboardPage.isTextPresentInTable(mappedPropertyName), is(true));
        Assertion.assertThat(
                "Property mapping log shows Map to existing property as action type for filtered property",
                internalSupplierPropertiesMappingPage.getActionTypeByVendorPropertyIdAndPropertyName(vendorPropertyId, mappedPropertyName),
                equalToIgnoringCase("Map to existing property"));
    }

    @Author(names = {"Yazid"})
    @Module(name = "Properties Mapping")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testVerifyExternalMapPropertyByUrl() {
        Map<String, String> param = getParam("testVerifyExternalMapPropertyByUrl");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        InternalSupplierDashboardPage<?> internalSupplierDashboardPage = InternalSupplierDashboardPage.getInstance(session);
        ExternalSupplierPropertiesMappingPage<?> externalSupplierPropertiesMappingPage = new ExternalSupplierPropertiesMappingPage<>(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);

        String emails = param.get("email");
        String password = param.get("password");
        String otp = param.getOrDefault("otp", "1234");
        String vendorName = param.get("vendorName");
        String expectedSuccessMessage = param.get("expectedSuccessMessage");
        String accommodationType = param.getOrDefault("accommodationType", "HOTEL");

        step("Resolve vendor property and login email from unmapped hotels API via hotel autocomplete");
        HotelApi.ResolvedUnmappedPropertyWithLogin resolved = hotelApi.resolveVendorPropertyAndEmail(
                vendorName, emails, "", "", accommodationType);
        String email = resolved.email();
        String vendorPropertyId = resolved.vendorPropertyId();
        String mappedPropertyName = resolved.propertyName();
        String existingPdpUrl = resolved.pdpUrl();

        step("Login to External Supplier Dashboard");
        loginPage.login(email, password, otp);

        step("Go to Properties Mapping > Map Properties");
        externalSupplierPropertiesMappingPage.goToMapPropertiesPage();
        step("Dismiss continue popup if present");
        externalSupplierPropertiesMappingPage.clickContinueIfPresent();
        Assertion.assertThat("Map Properties page is loaded", externalSupplierPropertiesMappingPage.isMapPropertiesPageLoaded(), is(true));

        step("Search vendor property id and verify property name in table");
        externalSupplierPropertiesMappingPage.searchByVendorPropertyId(vendorPropertyId);
        String propertyNameInTable = externalSupplierPropertiesMappingPage.getPropertyNameByVendorPropertyId(vendorPropertyId);
        Assertion.assertThat(
                "Property name in table should match API property name",
                propertyNameInTable,
                equalToIgnoringCase(mappedPropertyName));

        step("[Properties Mapping][Map Properties] Verify external user is able to map property by proposing existing property's PDP URL");
        externalSupplierPropertiesMappingPage.clickMapByVendorPropertyId(vendorPropertyId);
        externalSupplierPropertiesMappingPage.clickProposeAnotherProperty();
        externalSupplierPropertiesMappingPage.inputPropertyUrl(existingPdpUrl);
        externalSupplierPropertiesMappingPage.confirmMerging();
        Assertion.assertThat("Property mapped successfully by PDP URL", internalSupplierDashboardPage.verifySnackbarMessage(expectedSuccessMessage), is(true));
        externalSupplierPropertiesMappingPage.switchToSupplyDashboardTab();

        step("Go to Properties Mapping > Properties Mapping Log");
        externalSupplierPropertiesMappingPage.goToPropertiesMappingLogPage();
        Assertion.assertThat("Properties Mapping Log page is loaded", externalSupplierPropertiesMappingPage.isPropertiesMappingLogPageLoaded(), is(true));

        step("[Properties Mapping][Properties Mapping Log] Verify external user is able to see and filter property mapping log by date and vendor property id");
        internalSupplierDashboardPage.selectDateRangeInLog(-1, 0);
        externalSupplierPropertiesMappingPage.inputVendorPropertyIdInLog(vendorPropertyId);
        internalSupplierDashboardPage.clickSearch();
        Assertion.assertThat("Filtered logs show today's date in results", externalSupplierPropertiesMappingPage.isDatePresentInTable(0), is(true));

        step("[Properties Mapping][Properties Mapping Log] Verify property mapping log is presented correctly after property is mapped by proposing existing property's PDP URL");
        Assertion.assertThat("Property mapping log contains vendor property id", internalSupplierDashboardPage.isTextPresentInTable(vendorPropertyId), is(true));
        Assertion.assertThat("Property mapping log contains mapped property name", internalSupplierDashboardPage.isTextPresentInTable(mappedPropertyName), is(true));
        Assertion.assertThat(
                "Property mapping log shows Map to existing property as action type for filtered property",
                externalSupplierPropertiesMappingPage.getActionTypeByVendorPropertyIdAndPropertyName(vendorPropertyId, mappedPropertyName),
                equalToIgnoringCase("Map to existing property"));
    }

    @Author(names = {"Yazid"})
    @Module(name = "Properties Mapping")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testVerifyMapPropertyAsNewStandalone() {
        Map<String, String> param = getParam("testVerifyMapPropertyAsNewStandalone");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        InternalSupplierDashboardPage<?> internalSupplierDashboardPage = InternalSupplierDashboardPage.getInstance(session);
        InternalSupplierPropertiesMappingPage<?> internalSupplierPropertiesMappingPage = new InternalSupplierPropertiesMappingPage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String vendorName = param.get("vendorName");
        String expectedSuccessMessage = param.get("expectedSuccessMessage");

        step("Login to Internal Supplier Dashboard overview");
        memberLoginPage.gotoMemberLoginPage();
        memberLoginPage.adminLogin(email, password);
        loginMemberAdmin.gotoHome();
        loginMemberAdmin.goToAccommodationSupplierDashboard();
        internalSupplierDashboardPage.goToOverviewPage();
        Assertion.assertThat("Overview page is loaded", internalSupplierDashboardPage.isOverviewPageLoaded(), is(true));

        step("Go to Properties Mapping > Map Properties");
        internalSupplierPropertiesMappingPage.goToMapPropertiesPage();
        Assertion.assertThat("Map Properties page is loaded", internalSupplierPropertiesMappingPage.isMapPropertiesPageLoaded(), is(true));

        step("Filter property list by vendor and store property name with vendor property id");
        internalSupplierPropertiesMappingPage.filterByVendorName(vendorName);
        List<String> propertyNames = internalSupplierPropertiesMappingPage.getPropertyNamesFromTable(1);
        Assertion.assertThat("At least one property name should be available in the table", !propertyNames.isEmpty(), is(true));
        String propertyName = propertyNames.get(0);        
        String vendorPropertyId = internalSupplierPropertiesMappingPage.getVendorPropertyIdByPropertyName(propertyName);
        Assertion.assertThat("Property to map should be visible in property list", internalSupplierDashboardPage.isTextPresentInTable(propertyName), is(true));

        step("[Properties Mapping][Map Properties] Verify internal user is able to map property to be new standalone property");
        internalSupplierPropertiesMappingPage.clickSelectActionAddAsNewByPropertyName(propertyName);
        internalSupplierDashboardPage.clickContinueOnPopup();
        Assertion.assertThat("Property mapped successfully as new standalone", internalSupplierDashboardPage.verifySnackbarMessage(expectedSuccessMessage), is(true));

        step("Go to Properties Mapping > Properties Mapping Log");
        internalSupplierPropertiesMappingPage.goToPropertiesMappingLogPage();
        Assertion.assertThat("Properties Mapping Log page is loaded", internalSupplierPropertiesMappingPage.isPropertiesMappingLogPageLoaded(), is(true));

        step("[Properties Mapping][Properties Mapping Log] Verify internal user is able to see and filter property mapping log by vendor and vendor property id");
        internalSupplierPropertiesMappingPage.filterByVendorName(vendorName);
        internalSupplierPropertiesMappingPage.inputVendorPropertyIdInLog(vendorPropertyId);
        internalSupplierDashboardPage.clickSearch();

        step("[Properties Mapping][Properties Mapping Log] Verify property mapping log is presented correctly after internal user map property as new standalone property");
        Assertion.assertThat("Property mapping log contains vendor property id", internalSupplierDashboardPage.isTextPresentInTable(vendorPropertyId), is(true));
        Assertion.assertThat("Property mapping log contains mapped property name", internalSupplierDashboardPage.isTextPresentInTable(propertyName), is(true));
        Assertion.assertThat(
                "Property mapping log shows Add new property as action type for filtered property",
                internalSupplierPropertiesMappingPage.getActionTypeByVendorPropertyIdAndPropertyName(vendorPropertyId, propertyName),
                equalToIgnoringCase("Add new property"));
    }

    @Author(names = {"Nasha","Riya Bajpai"})
    @Module(name = "Rooms Mapping")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyMapRoomsPageFiltersMapSingleMultipleRoomsAndRoomMappingLog(){
        Map<String, String> param = getParam("verifyMapRoomsPageFiltersMapSingleMultipleRoomsAndRoomMappingLog");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        InternalSupplierDashboardPage<?> internalSupplierDashboardPage = InternalSupplierDashboardPage.getInstance(session);
        String email = param.get("email");
        String password = param.get("password");
        String vendorName = param.get("vendorName");
        String country = param.get("country");
        String region = param.get("region");
        String city = param.get("city");
        String vendorPropertyId = param.get("vendorPropertyId");
        String groupRoomId = param.get("groupRoomId");
        String groupRoomId2 = param.get("groupRoomId2");
        String unmappedRoomName = param.get("unmappedRoomName");
        String room1 = param.get("room1");
        String room2 = param.get("room2");
        String room3 = param.get("room3");
        String expectedSuccessMessageMultiple = param.get("expectedSuccessMessageMultiple");
        String expectedSuccessMessageSingle = param.get("expectedSuccessMessageSingle");

        step("User Log in to Panel dashboard");
        memberLoginPage.gotoMemberLoginPage();
        memberLoginPage.adminLogin(email, password);
        loginMemberAdmin.gotoHome();

        step("Go to Internal Supplier Dashboard");
        loginMemberAdmin.goToAccommodationSupplierDashboard();
        internalSupplierDashboardPage.goToMapRoomsPage();

        step("[Rooms Mapping][Map Rooms] Verify internal user is able to see and filter property list on map rooms page by vendor and locations.");
        internalSupplierDashboardPage.filterByVendorName(vendorName);
        internalSupplierDashboardPage.clickLocationButton();
        internalSupplierDashboardPage.selectDropdownOption("Country", "country", country);
        internalSupplierDashboardPage.selectDropdownOption("Region", "region", region);
        internalSupplierDashboardPage.selectDropdownOption("City", "city", city);
        internalSupplierDashboardPage.clickSearch();
        
        Assertion.assertThat("Filtered mapped room ID should be visible in Mapped table (Country)", internalSupplierDashboardPage.isTextPresentInTable(country), is(true));
        Assertion.assertThat("Filtered mapped room ID should be visible in Mapped table (Region)", internalSupplierDashboardPage.isTextPresentInTable(region), is(true));
        Assertion.assertThat("Filtered mapped room ID should be visible in Mapped table (City)", internalSupplierDashboardPage.isTextPresentInTable(city), is(true));
        
        // rooms that are currently unmapped; the finally block maps them back to their group if the test dies in between
        Set<String> roomsToRestore = new LinkedHashSet<>();

        try {
            step("[Precondition] Unmap rooms");
            internalSupplierDashboardPage.clickVendorButton();
            internalSupplierDashboardPage.inputVendorPropertyId(vendorPropertyId);
            internalSupplierDashboardPage.clickSearch();
            internalSupplierDashboardPage.clickMapButtonByPropertyId(vendorPropertyId);
            internalSupplierDashboardPage.clickSegmentedButton("Mapped List", "ID");
            internalSupplierDashboardPage.searchByVendorRoomId("Mapped List", room1);
            roomsToRestore.add(room1);
            internalSupplierDashboardPage.clickUnmapByRoomId(room1);
            internalSupplierDashboardPage.clickContinueOnPopup();
            internalSupplierDashboardPage.searchByVendorRoomId("Mapped List", room2);
            roomsToRestore.add(room2);
            internalSupplierDashboardPage.clickUnmapByRoomId(room2);
            internalSupplierDashboardPage.clickContinueOnPopup();
            internalSupplierDashboardPage.searchByVendorRoomId("Mapped List", room3);
            roomsToRestore.add(room3);
            internalSupplierDashboardPage.clickUnmapByRoomId(room3);
            internalSupplierDashboardPage.clickContinueOnPopup();

            step("[Rooms Mapping][Map Rooms] Verify internal user is able to filter unmapped room list by room name");
            internalSupplierDashboardPage.clickSegmentedButton("Unmapped Rooms", "Name");
            internalSupplierDashboardPage.searchByVendorRoomName("Unmapped Rooms", unmappedRoomName);
            Assertion.assertThat("Filtered unmapped room name should be displayed", internalSupplierDashboardPage.isTextPresentInTableMap("Unmapped Rooms", unmappedRoomName), is(true));

            step("[Rooms Mapping][Map Rooms] Verify internal user is able to filter unmapped room list by vendor room id");
            internalSupplierDashboardPage.clickSegmentedButton("Unmapped Rooms", "ID");
            internalSupplierDashboardPage.searchByVendorRoomId("Unmapped Rooms", room1);
            Assertion.assertThat("Filtered unmapped room ID should be displayed", internalSupplierDashboardPage.isTextPresentInTableMap("Unmapped Rooms", room1), is(true));
        
            step("[Rooms Mapping][Map Rooms] Verify internal user is able to map single room to existing room group");
            internalSupplierDashboardPage.searchByVendorRoomId("Unmapped Rooms", room3);
            internalSupplierDashboardPage.selectRoomById(room3);
            internalSupplierDashboardPage.clickMapButtonByRoomGroupId(groupRoomId2);
            internalSupplierDashboardPage.clickContinueOnPopup();
            roomsToRestore.remove(room3);
            Assertion.assertThat("Snackbar message should confirm success single mapping", internalSupplierDashboardPage.verifySnackbarMessage(expectedSuccessMessageSingle), is(true));

            step("[Rooms Mapping][Map Rooms] Verify internal user is able to map multiple rooms to existing room group");
            internalSupplierDashboardPage.clearSearchVendorRoomId("Unmapped Rooms");
            internalSupplierDashboardPage.selectRooms(List.of(room1, room2));
            internalSupplierDashboardPage.clickMapButtonByRoomGroupId(groupRoomId);
            internalSupplierDashboardPage.clickContinueOnPopup();
            roomsToRestore.remove(room1);
            roomsToRestore.remove(room2);
            Assertion.assertThat("Snackbar message should confirm success multiple mapping", internalSupplierDashboardPage.verifySnackbarMessage(expectedSuccessMessageMultiple), is(true));

            step("[Rooms Mapping][Rooms Mapping Log] Verify internal user is able to see and filter room mapping log by vendor and vendor property id");
            internalSupplierDashboardPage.goToRoomsMappingLogPage();
            internalSupplierDashboardPage.inputVendorPropertyIdInLog(vendorPropertyId);
            internalSupplierDashboardPage.clickSearch();
            Assertion.assertThat("Filtered log details should match property ID", internalSupplierDashboardPage.isTextPresentInTable(vendorPropertyId), is(true));

            step("[Rooms Mapping][Rooms Mapping Log] Verify room mapping log is presented correctly after internal user map room through map rooms page");
            internalSupplierDashboardPage.clickResetButton();
            internalSupplierDashboardPage.inputVendorPropertyIdInLog(vendorPropertyId);
            internalSupplierDashboardPage.inputVendorRoomIdInLog(room1);
            internalSupplierDashboardPage.clickSearch();
            Assertion.assertThat("Filtered log details should match property ID", internalSupplierDashboardPage.isTextPresentInTable(vendorPropertyId), is(true));
            Assertion.assertThat("Newly created map log should exist in results for " + room1, internalSupplierDashboardPage.isTextPresentInTable(room1), is(true));
        
            internalSupplierDashboardPage.clickResetButton();
            internalSupplierDashboardPage.inputVendorRoomIdInLog(room2);
            internalSupplierDashboardPage.clickSearch();
            Assertion.assertThat("Filtered log details should match property ID", internalSupplierDashboardPage.isTextPresentInTable(vendorPropertyId), is(true));
            Assertion.assertThat("Newly created map log should exist in results for " + room2, internalSupplierDashboardPage.isTextPresentInTable(room2), is(true));
        
            internalSupplierDashboardPage.clickResetButton();
            internalSupplierDashboardPage.inputVendorRoomIdInLog(room3);
            internalSupplierDashboardPage.clickSearch();
            Assertion.assertThat("Filtered log details should match property ID", internalSupplierDashboardPage.isTextPresentInTable(vendorPropertyId), is(true));
            Assertion.assertThat("Newly created map log should exist in results for " + room3, internalSupplierDashboardPage.isTextPresentInTable(room3), is(true));
        } finally {
            if (!roomsToRestore.isEmpty()) {
                // Cleanup: test failed while rooms were unmapped - map them back so the next run starts clean
                try {
                    step("[Cleanup] Test failed with rooms unmapped - map them back to their room groups");
                    internalSupplierDashboardPage.goToMapRoomsPage();
                    internalSupplierDashboardPage.clickVendorButton();
                    internalSupplierDashboardPage.filterByVendorName(vendorName);
                    internalSupplierDashboardPage.inputVendorPropertyId(vendorPropertyId);
                    internalSupplierDashboardPage.clickSearch();
                    internalSupplierDashboardPage.clickMapButtonByPropertyId(vendorPropertyId);
                    internalSupplierDashboardPage.clickSegmentedButton("Unmapped Rooms", "ID");
                    for (String room : new LinkedHashSet<>(roomsToRestore)) {
                        try {
                            String group = room.equals(room3) ? groupRoomId2 : groupRoomId;
                            internalSupplierDashboardPage.searchByVendorRoomId("Unmapped Rooms", room);
                            if (internalSupplierDashboardPage.isTextPresentInTableMap("Unmapped Rooms", room)) {
                                internalSupplierDashboardPage.selectRoomById(room);
                                internalSupplierDashboardPage.clickMapButtonByRoomGroupId(group);
                                internalSupplierDashboardPage.clickContinueOnPopup();
                            }
                        } catch (Exception e) {
                            System.err.println("[Cleanup] Could not map room " + room + " back, please check it manually: " + e);
                        }
                    }
                } catch (Exception e) {
                    System.err.println("[Cleanup] Could not restore unmapped rooms, please check them manually: " + e);
                }
            }
        }
    }

    @Author(names = {"Aditi"})
    @Module(name = "Overview")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testVerifyInternalUserAbleToSeeAndFilterPropertyAndRoomOverviewByVendor() {
        Map<String, String> param = getParam("testVerifyInternalUserAbleToSeeAndFilterPropertyAndRoomOverviewByVendor");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        InternalSupplierDashboardPage<?> internalSupplierDashboardPage = InternalSupplierDashboardPage.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String vendorName = param.get("vendorName");

        step("Login to member admin dashboard");
        memberLoginPage.gotoMemberLoginPage();
        memberLoginPage.adminLogin(username, password);
        loginMemberAdmin.gotoHome();

        step("Select Accommodation Supplier Dashboard");
        loginMemberAdmin.goToAccommodationSupplierDashboard();
        internalSupplierDashboardPage.goToOverviewPage();
        Assertion.assertThat("Overview page is loaded", internalSupplierDashboardPage.isOverviewPageLoaded(), is(true));

        step("Validate Property Overview and Room Overview are displayed before filtering");
        Assertion.assertThat("Property Overview section is displayed", internalSupplierDashboardPage.isPropertyOverviewDisplayed(), is(true));
        Assertion.assertThat("Room Overview section is displayed", internalSupplierDashboardPage.isRoomOverviewDisplayed(), is(true));

        step(String.format("Filter overview by vendor name: %s", vendorName));
        internalSupplierDashboardPage.filterByVendorName(vendorName);
        Assertion.assertThat(String.format("Vendor name selected: %s", vendorName), internalSupplierDashboardPage.isVendorNameSelected(vendorName), is(true));

        step("Validate Property Overview and Room Overview are displayed after filtering by vendor");
        Assertion.assertThat("Property Overview section is displayed after vendor filter", internalSupplierDashboardPage.isPropertyOverviewDisplayed(), is(true));
        Assertion.assertThat("Room Overview section is displayed after vendor filter", internalSupplierDashboardPage.isRoomOverviewDisplayed(), is(true));
    }

    @Author(names = {"Aditi","Riya Bajpai"})
    @Module(name = "User Management")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testVerifyInternalSupplierUserManagementSeeFilterAddEditRemoveAndResendInvitation() {
        Map<String, String> param = getParam("testVerifyInternalSupplierUserManagementSeeFilterAddEditRemoveAndResendInvitation");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        InternalSupplierUserManagementPage<?> userManagementPage = InternalSupplierUserManagementPage.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String accessType = param.get("accessType");
        String pageAccess = param.get("pageAccess");
        String expectedInvitationSuccessMessage = param.get("expectedInvitationSuccessMessage");
        String[] roles = {param.get("adminRole"), param.get("financeRole"), param.get("opsRole")};

        step("Login to member admin dashboard");
        memberLoginPage.gotoMemberLoginPage();
        memberLoginPage.adminLogin(username, password);
        loginMemberAdmin.gotoHome();

        step("Select Accommodation Supplier Dashboard and open User Management");
        loginMemberAdmin.goToAccommodationSupplierDashboard();
        userManagementPage.goToUserManagementPage();
        Assertion.assertThat("User Management page is loaded", userManagementPage.isUserManagementPageLoaded(), is(true));

        step("Verify internal user is able to see and filter user list by access type and role.");
        Assertion.assertThat("User list table is displayed", userManagementPage.isUserListTableDisplayed(), is(true));
        for (String role : roles) {
            userManagementPage.filterUserListByAccessTypeAndRole(accessType, role);
        }

        for (String role : roles) {
            String firstName = randomNameWithoutTest();
            String lastName = role + "User";
            String email = (firstName + lastName).toLowerCase() + System.currentTimeMillis() + "@tiket.com";
            String updatedFirstName = randomNameWithoutTest();

            step(String.format("Verify internal user is able to add new user with role %s successfully", role));
            userManagementPage.clickInviteUsers();
            userManagementPage.fillInviteUserForm(firstName, lastName, email, pageAccess, role);
            userManagementPage.submitInviteUserForm();
            Assertion.assertThat(String.format("Invite success snackbar is displayed for %s user", role),
                    userManagementPage.verifySnackbarMessage(expectedInvitationSuccessMessage), is(true));
            userManagementPage.filterUserListByAccessTypeAndRole(accessType, role);
            Assertion.assertThat(String.format("Invited %s user is displayed in user list", role),
                    userManagementPage.isUserPresentInTable(email), is(true));

            step(String.format("Verify internal user is able to edit existing user with role %s successfully", role));
            userManagementPage.filterUserListByAccessTypeAndRole(accessType, role);
            userManagementPage.clickEditUserFromTableRow(email);
            userManagementPage.updateEditUserForm(updatedFirstName, lastName, role);
            userManagementPage.submitEditUserForm();
            userManagementPage.filterUserListByAccessTypeAndRole(accessType, role);
            Assertion.assertThat(String.format("Updated %s user is displayed in user list after edit", role),
                    userManagementPage.isUserPresentInTable(updatedFirstName), is(true));

            step(String.format("Verify internal user is able to resend invitation for user with role %s successfully", role));
            userManagementPage.filterUserListByAccessTypeAndRole(accessType, role);
            userManagementPage.clickResendInvitationFromTableRow(email);
            userManagementPage.confirmActionPopup();
            Assertion.assertThat(String.format("Resend invitation success snackbar is displayed for %s user", role),
                    userManagementPage.verifySnackbarMessage(expectedInvitationSuccessMessage), is(true));

            step(String.format("Verify internal user is able to remove user with role %s successfully", role));
            userManagementPage.filterUserListByAccessTypeAndRole(accessType, role);
            userManagementPage.clickRemoveUserFromTableRow(email);
            userManagementPage.confirmActionPopup();
            userManagementPage.refreshPage(5);
            userManagementPage.filterUserListByAccessTypeAndRole(accessType, role);
            Assertion.assertThat(String.format("Removed %s user is not displayed in user list", role),
                    userManagementPage.isUserNotPresentInTable(email), is(true));
        }
    }

    @Author(names = {"Manan Kumar","Riya Bajpai"})
    @Module(name = "UserManagement")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testVerifyExternalUserAbleToSeeUserList() {
        Map<String, String> param = getParam("testVerifyExternalUserAbleToSeeUserList");
        BaseTest.paramsThreadLocal.set(param);
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("Start a fresh browser session so no cookies from earlier tests are reused");
        Session<WebDriver> session = BaseTest.renewSessionForFreshInstall();
        LoginPage<?> loginPage = new LoginPage<>(session);
        UserListPage<?> userListPage = new UserListPage<>(session);

        step("Login to External Supplier Dashboard as Admin with OTP");
        loginPage.login(email, password, otp);

        step("Go to User List");
        userListPage.goToUserList();

        step("Validate user list is showing");
        Assertion.assertThat("User list is displayed", userListPage.isUserListDisplayed(), is(true));

        step("Validate pagination is working");
        Assertion.assertThat("Pagination control is displayed", userListPage.isPaginationDisplayed(), is(true));
        boolean hasNextPage = userListPage.goToNextPageAndVerifyChange();
        BaseTest.mainLogger.get().log("[Pagination] Next page navigation attempted — result: " + (hasNextPage ? "navigated to next page" : "only one page exists, skipped"));

        step("Logout");
        loginPage.logout();
        Assertion.assertThat("Ops user is logged out", loginPage.isLoggedOut(), is(true));
    }

    @Author(names = {"Manan Kumar", "Riya Bajpai"})
    @Module(name = "Overview")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testVerifyExternalUserAbleToSeePropertyAndRoomOverview() {
        Map<String, String> param = getParam("testVerifyExternalUserAbleToSeePropertyAndRoomOverview");
        BaseTest.paramsThreadLocal.set(param);
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("Start a fresh browser session so no cookies from earlier tests are reused");
        Session<WebDriver> session = BaseTest.renewSessionForFreshInstall();
        LoginPage<?> loginPage = new LoginPage<>(session);
        OverviewPage<?> overviewPage = new OverviewPage<>(session);

        step("Login to External Supplier Dashboard as Admin with OTP");
        loginPage.login(email, password, otp);

        step("Navigate to Overview");
        overviewPage.goToOverview();

        step("Validate Overview page is loaded");
        Assertion.assertThat("Overview page is loaded", overviewPage.isOverviewPageLoaded(), is(true));

        step("Validate Property Overview section is displayed");
        Assertion.assertThat("Property Overview section is displayed", overviewPage.isPropertyOverviewDisplayed(), is(true));

        step("Validate Room Overview section is displayed");
        Assertion.assertThat("Room Overview section is displayed", overviewPage.isRoomOverviewDisplayed(), is(true));

        step("Logout");
        loginPage.logout();
        Assertion.assertThat("Ops user is logged out", loginPage.isLoggedOut(), is(true));
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "Login")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testVerifyExternalUserLoginAccessAndPasswordReset() {
        Map<String, String> param = getParam("testVerifyExternalUserLoginAccessAndPasswordReset");
        BaseTest.paramsThreadLocal.set(param);
        String adminEmail = param.get("adminEmail");
        String adminPassword = param.get("adminPassword");
        String financeEmail = param.get("financeEmail");
        String financePassword = param.get("financePassword");
        String opsEmail = param.get("opsEmail");
        String opsPassword = param.get("opsPassword");
        String otp = param.get("otp");
        String newPassword = param.get("newPassword");
        String resetEmail = param.get("resetEmail");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        SidebarPage<?> sidebarPage = new SidebarPage<>(session);
        OverviewPage<?> overviewPage = new OverviewPage<>(session);

        step("Login as Admin ");
        loginPage.login(adminEmail, adminPassword, otp);

        step("Validate sidebar is displayed for Admin");
        Assertion.assertThat("Sidebar is displayed for Admin", sidebarPage.isSidebarDisplayed(), is(true));

        step("Verify user can Navigate to Overview");
        overviewPage.goToOverview();

        step("Logout from Admin account");
        loginPage.logout();
        Assertion.assertThat("Admin user is logged out", loginPage.isLoggedOut(), is(true));

        step("Login as Finance and validate sidebar menu");
        loginPage.login(financeEmail, financePassword, otp);

        step("Validate sidebar is displayed for Finance");
        Assertion.assertThat("Sidebar is displayed for Finance", sidebarPage.isSidebarDisplayed(), is(true));

        step("Verify user can Navigate to Overview");
        overviewPage.goToOverview();

        step("Logout from Finance account");
        loginPage.logout();
        Assertion.assertThat("Finance user is logged out", loginPage.isLoggedOut(), is(true));

        step("Login as Ops and validate sidebar menu");
        loginPage.login(opsEmail, opsPassword, otp);

        step("Validate sidebar is displayed for Ops");
        Assertion.assertThat("Sidebar is displayed for Ops", sidebarPage.isSidebarDisplayed(), is(true));

        step("Verify user can Navigate to Overview");
        overviewPage.goToOverview();

        step("Logout from Ops account");
        loginPage.logout();
        Assertion.assertThat("Ops user is logged out", loginPage.isLoggedOut(), is(true));

        step("Click Forgot Password and reset to new password");
        loginPage.goToLoginPage();
        loginPage.resetPassword(resetEmail, otp, newPassword);
        Assertion.assertThat("Returned to login page after password reset", loginPage.isBackOnLoginPage(), is(true));

        step("Login with new password");
        loginPage.login(resetEmail, newPassword, otp);
        Assertion.assertThat("Login successful with new password", loginPage.isLoginSuccessful(), is(true));

        step("Revert password back to original");
        loginPage.goToLoginPage();
        loginPage.resetPassword(resetEmail, otp, adminPassword);
        Assertion.assertThat("Returned to login page after reverting password", loginPage.isBackOnLoginPage(), is(true));

        step("Verify login with original password still works");
        loginPage.login(resetEmail, adminPassword, otp);
        Assertion.assertThat("Login successful with original password after revert", loginPage.isLoginSuccessful(), is(true));

        step("Logout");
        loginPage.logout();
        Assertion.assertThat("Ops user is logged out", loginPage.isLoggedOut(), is(true));
    }

    @Author(names = {"Mridul","Riya Bajpai"})
    @Module(name = "Rooms Mapping")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyExternalMapRoomsSortFilterMapSingleMultipleRoomsAndRoomMappingLog() {
        Map<String, String> param = getParam("verifyExternalMapRoomsSortFilterMapSingleMultipleRoomsAndRoomMappingLog");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.getOrDefault("otp", "1234");
        String vendorPropertyId = param.get("vendorPropertyId");
        String unmappedRoomName = param.get("unmappedRoomName");
        String room1 = param.get("room1");
        String room2 = param.get("room2");
        String room3 = param.get("room3");
        String groupRoomId = param.get("groupRoomId");
        String groupRoomId2 = param.get("groupRoomId2");
        String expectedSuccessMessageSingle = param.get("expectedSuccessMessageSingle");
        String expectedSuccessMessageMultiple = param.get("expectedSuccessMessageMultiple");

        step("Start a fresh browser session so no cookies from earlier tests are reused");
        Session<WebDriver> session = BaseTest.renewSessionForFreshInstall();
        LoginPage<?> loginPage = new LoginPage<>(session);
        ExternalSupplierRoomsMappingPage<?> roomsMappingPage = new ExternalSupplierRoomsMappingPage<>(session);

        step("Login to External Supplier Dashboard as Admin with OTP");
        loginPage.login(email, password, otp);

        step("Go to Rooms Mapping > Map Rooms");
        roomsMappingPage.goToMapRoomsPage();
        roomsMappingPage.clickContinueOnPopup();

        step("[External Supplier Dashboard][Rooms Mapping][Map Rooms] Verify external user is able to sort property list by room mapping status percentage descendingly");
        double beforeSortDesc = roomsMappingPage.getFirstMappingStatusValue();
        roomsMappingPage.sortMappingStatus();
        double afterSortDesc = roomsMappingPage.getFirstMappingStatusValue();
        Assertion.assertThat("Property list should be sorted descending by room mapping status percentage", roomsMappingPage.isSorted(beforeSortDesc, afterSortDesc, false), is(true));

        step("[External Supplier Dashboard][Rooms Mapping][Map Rooms] Verify external user is able to sort property list by room mapping status percentage ascendingly");
        double beforeSortAsc = roomsMappingPage.getFirstMappingStatusValue();
        roomsMappingPage.sortMappingStatus();
        double afterSortAsc = roomsMappingPage.getFirstMappingStatusValue();
        Assertion.assertThat("Property list should be sorted ascending by room mapping status percentage", roomsMappingPage.isSorted(beforeSortAsc, afterSortAsc, true), is(true));

        step("Search the hotel by vendor property id and click Map on the particular hotel");
        roomsMappingPage.clickVendorButton();
        roomsMappingPage.inputVendorPropertyId(vendorPropertyId);
        roomsMappingPage.clickSearch();
        roomsMappingPage.clickMapButtonByPropertyId(vendorPropertyId);

        step("[Precondition] Unmap rooms so they are available in the Unmapped Rooms list");
        roomsMappingPage.clickUnmapButton("Mapped List","ID");
        roomsMappingPage.searchByVendorRoomId("Mapped List", room1);
        if(roomsMappingPage.isTextPresentInTableMap("Mapped List", room1)) {
            roomsMappingPage.clickUnmapByRoomId(room1);
            roomsMappingPage.clickContinueOnPopup();
        }

        roomsMappingPage.searchByVendorRoomId("Mapped List", room2);
        if(roomsMappingPage.isTextPresentInTableMap("Mapped List", room2)) {
            roomsMappingPage.clickUnmapByRoomId(room2);
            roomsMappingPage.clickContinueOnPopup();
        }

        roomsMappingPage.searchByVendorRoomId("Mapped List", room3);
        if(roomsMappingPage.isTextPresentInTableMap("Mapped List", room3)) {
            roomsMappingPage.clickUnmapByRoomId(room3);
            roomsMappingPage.clickContinueOnPopup();
        }

        step("[External Supplier Dashboard][Rooms Mapping][Map Rooms] Verify external user is able to filter unmapped room list by room name");
        roomsMappingPage.clickUnmapButton("","Name");
        roomsMappingPage.searchByVendorRoomName("Unmapped Rooms", unmappedRoomName);
        Assertion.assertThat("Filtered unmapped room name should be displayed", roomsMappingPage.isTextPresentInTableMap("Unmapped Rooms", unmappedRoomName), is(true));

        step("[External Supplier Dashboard][Rooms Mapping][Map Rooms] Verify external user is able to filter unmapped room list by vendor room id");
        roomsMappingPage.clickUnmapButton("","ID");
        roomsMappingPage.searchByVendorRoomId("Unmapped Rooms", room1);
        Assertion.assertThat("Filtered unmapped room ID should be displayed", roomsMappingPage.isTextPresentInTableMap("Unmapped Rooms", room1), is(true));

        step("[External Supplier Dashboard][Rooms Mapping][Map Rooms] Verify external user is able to map single room to existing room group");
        roomsMappingPage.searchByVendorRoomId("Unmapped Rooms", room3);
        roomsMappingPage.selectRoomById(room3);
        roomsMappingPage.clickMapButtonByRoomGroupId(groupRoomId2);
        roomsMappingPage.clickContinueOnPopup();
        Assertion.assertThat("Snackbar message should confirm successful single room mapping", roomsMappingPage.verifySnackbarMessage(expectedSuccessMessageSingle), is(true));

        step("[External Supplier Dashboard][Rooms Mapping][Map Rooms] Verify external user is able to map multiple rooms to existing room group");
        roomsMappingPage.clearSearchVendorRoomId("Unmapped Rooms");
        roomsMappingPage.selectRooms(List.of(room1, room2));
        roomsMappingPage.clickMapButtonByRoomGroupId(groupRoomId);
        roomsMappingPage.clickContinueOnPopup();
        Assertion.assertThat("Snackbar message should confirm successful multiple room mapping", roomsMappingPage.verifySnackbarMessage(expectedSuccessMessageMultiple), is(true));

        step("Validate the rooms are moving to the Mapped List section");
        roomsMappingPage.clickUnmapButton("Mapped List","ID");
        roomsMappingPage.searchByVendorRoomId("Mapped List", room1);
        Assertion.assertThat("Mapped room1 should be present in Mapped List section", roomsMappingPage.isTextPresentInTableMap("Mapped List", room1), is(true));
        roomsMappingPage.searchByVendorRoomId("Mapped List", room2);
        Assertion.assertThat("Mapped room2 should be present in Mapped List section", roomsMappingPage.isTextPresentInTableMap("Mapped List", room2), is(true));
        roomsMappingPage.searchByVendorRoomId("Mapped List", room3);
        Assertion.assertThat("Mapped room3 should be present in Mapped List section", roomsMappingPage.isTextPresentInTableMap("Mapped List", room3), is(true));

        step("Go to Rooms Mapping > Rooms Mapping Log");
        roomsMappingPage.goToRoomsMappingLogPage();

        step("[External Supplier Dashboard][Rooms Mapping][Room Mapping Log] Verify external user is able to see and filter room mapping log by vendor property id");
        roomsMappingPage.inputVendorPropertyIdInLog(vendorPropertyId);
        roomsMappingPage.clickSearch();
        Assertion.assertThat("Filtered room mapping log should contain the vendor property id", roomsMappingPage.isTextPresentInTable(vendorPropertyId), is(true));

        step("[External Supplier Dashboard][Rooms Mapping][Room Mapping Log] Verify room mapping log is presented correctly after external user map room through map rooms page");
        roomsMappingPage.clickResetButton();
        roomsMappingPage.inputVendorPropertyIdInLog(vendorPropertyId);
        roomsMappingPage.inputVendorRoomIdInLog(room1);
        roomsMappingPage.clickSearch();
        Assertion.assertThat("Room mapping log should contain the vendor property id for room1", roomsMappingPage.isTextPresentInTable(vendorPropertyId), is(true));
        Assertion.assertThat("Room mapping log should contain newly mapped room1", roomsMappingPage.isTextPresentInTable(room1), is(true));

        roomsMappingPage.clickResetButton();
        roomsMappingPage.inputVendorRoomIdInLog(room2);
        roomsMappingPage.clickSearch();
        Assertion.assertThat("Room mapping log should contain newly mapped room2", roomsMappingPage.isTextPresentInTable(room2), is(true));

        roomsMappingPage.clickResetButton();
        roomsMappingPage.inputVendorRoomIdInLog(room3);
        roomsMappingPage.clickSearch();
        Assertion.assertThat("Room mapping log should contain newly mapped room3", roomsMappingPage.isTextPresentInTable(room3), is(true));
    }

    @Author(names = {"Mridul","Riya Bajpai"})
    @Module(name = "Rooms Mapping")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyExternalMapRoomsFilterMappedRoomsUnmapSingleRoomAndRoomMappingLog() {
        Map<String, String> param = getParam("verifyExternalMapRoomsFilterMappedRoomsUnmapSingleRoomAndRoomMappingLog");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.getOrDefault("otp", "1234");
        String vendorPropertyId = param.get("vendorPropertyId");
        String mappedRoomName = param.get("mappedRoomName");
        String mappedRoomId = param.get("mappedRoomId");
        String roomGroupName = param.get("roomGroupName");
        String expectedSuccessMessageUnmap = param.get("expectedSuccessMessageUnmap");

        step("Start a fresh browser session so no cookies from earlier tests are reused");
        Session<WebDriver> session = BaseTest.renewSessionForFreshInstall();
        LoginPage<?> loginPage = new LoginPage<>(session);
        ExternalSupplierRoomsMappingPage<?> roomsMappingPage = new ExternalSupplierRoomsMappingPage<>(session);

        step("Login to External Supplier Dashboard as Admin with OTP");
        loginPage.login(email, password, otp);

        step("Go to Rooms Mapping > Map Rooms");
        roomsMappingPage.goToMapRoomsPage();
        roomsMappingPage.clickContinueOnPopup();

        step("Search the hotel by vendor property id and click Map on the particular hotel");
        roomsMappingPage.clickVendorButton();
        roomsMappingPage.inputVendorPropertyId(vendorPropertyId);
        roomsMappingPage.clickSearch();
        roomsMappingPage.clickMapButtonByPropertyId(vendorPropertyId);

        step("[Precondition] Ensure the room is mapped to the room group so it can be unmapped");
        roomsMappingPage.ensureRoomMappedToGroup(mappedRoomId, roomGroupName);

        step("[External Supplier Dashboard][Rooms Mapping][Map Rooms] Verify external user is able to filter mapped room list by room name");
        roomsMappingPage.clickSegmentedButton("Mapped List", "Name");
        roomsMappingPage.searchByVendorRoomName("Mapped List", mappedRoomName);
        Assertion.assertThat("Filtered mapped room name should be displayed", roomsMappingPage.isTextPresentInTableMap("Mapped List", mappedRoomName), is(true));

        step("[External Supplier Dashboard][Rooms Mapping][Map Rooms] Verify external user is able to filter mapped room list by vendor room id");
        roomsMappingPage.clickSegmentedButton("Mapped List", "ID");
        roomsMappingPage.searchByVendorRoomId("Mapped List", mappedRoomId);
        Assertion.assertThat("Filtered mapped room ID should be displayed", roomsMappingPage.isTextPresentInTableMap("Mapped List", mappedRoomId), is(true));

        step("[External Supplier Dashboard][Rooms Mapping][Map Rooms] Verify external user is able to unmap single room from existing room group");
        roomsMappingPage.clickUnmapByRoomId(mappedRoomId);
        roomsMappingPage.clickContinueOnPopup();
        Assertion.assertThat("Snackbar message should confirm successful unmapping", roomsMappingPage.verifySnackbarMessage(expectedSuccessMessageUnmap), is(true));

        step("Validate the room is moving to the Unmapped Rooms section");
        roomsMappingPage.clickSegmentedButton("Unmapped Rooms", "ID");
        roomsMappingPage.searchByVendorRoomId("Unmapped Rooms", mappedRoomId);
        Assertion.assertThat("Unmapped room should be present in Unmapped Rooms section", roomsMappingPage.isTextPresentInTableMap("Unmapped Rooms", mappedRoomId), is(true));

        step("Go to Rooms Mapping > Rooms Mapping Log");
        roomsMappingPage.goToRoomsMappingLogPage();

        step("[External Supplier Dashboard][Rooms Mapping][Room Mapping Log] Verify external user is able to see and filter room mapping log by vendor property id and vendor room id");
        roomsMappingPage.inputVendorPropertyIdInLog(vendorPropertyId);
        roomsMappingPage.inputVendorRoomIdInLog(mappedRoomId);
        roomsMappingPage.clickSearch();
        Assertion.assertThat("Filtered room mapping log should contain the vendor property id", roomsMappingPage.isTextPresentInTable(vendorPropertyId), is(true));
        Assertion.assertThat("Filtered room mapping log should contain the vendor room id", roomsMappingPage.isTextPresentInTable(mappedRoomId), is(true));

        step("[External Supplier Dashboard][Rooms Mapping][Room Mapping Log] Verify external user is able to see and filter room mapping log by start & end date");
        roomsMappingPage.clickResetButton();
        roomsMappingPage.inputVendorPropertyIdInLog(vendorPropertyId);
        roomsMappingPage.inputVendorRoomIdInLog(mappedRoomId);
        roomsMappingPage.selectDateRangeInLog(-1, 0);
        roomsMappingPage.clickSearch();
        Assertion.assertThat("Filtered room mapping log should show the unmapping date within selected range", roomsMappingPage.isDatePresentInTable(0) || roomsMappingPage.isDatePresentInTable(-1), is(true));
    }
}
