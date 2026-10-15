package com.tiket.web.dashboard.test.nft.bus;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.nft.bus.BusPage;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.core.Is.is;

public class BusTest extends BaseTest {
    @Author(names = { "Glend" })
    @Module(name = "ActiveInactive")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test
    public void testBusToggle() throws InterruptedException {
        Map<String, String> param = getParam("testBusToggle");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        BusPage<?> busPage = new BusPage<>(session);
        String username = param.get("username");
        String password = param.get("password");

        step("Login to Bus");
        busPage.goToBusPage(username, password);

        step("Open Bus Page");
        busPage.openBusPage();

        step("Activate and Inactivate the bus location status");
        Assertion.assertThat("The bus operator status is successfully activated/deactivated", busPage.verifyLocationBusActivateOrInactivateStatus(), is(true));
        Assertion.assertThat("The bus operator status is successfully activated/deactivated", busPage.verifyLocationBusActivateOrInactivateStatus(), is(true));

    }
    @Author(names = { "Glend" })
    @Module(name = "View")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test
    public void testBusView() throws InterruptedException{
        Map<String, String> param = getParam("testBusToggle");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        BusPage<?> busPage = new BusPage<>(session);
        String username = param.get("username");
        String password = param.get("password");

        step("Login to Bus");
        busPage.goToBusPage(username, password);

        step("Open Bus Page");
        busPage.openBusPage();

        step("View Bus Location");
        busPage.locationBusView();
        Assertion.assertThat("The bus location is shown", busPage.verifyLocationBusView(), is(true));
    }
    @Author(names = { "Glend", "Clement" })
    @Module(name = "Update")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testBusLocationCreate")
    public void testBusLocationUpdate() throws InterruptedException{
        Map<String, String> param = getParam("testBusToggle");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        BusPage<?> busPage = new BusPage<>(session);
        String username = param.get("username");
        String password = param.get("password");

        step("Login to Bus");
        busPage.goToBusPage(username, password);

        step("Open Bus Page");
        busPage.openBusPage();

        busPage.locationBusUpdate();
        Assertion.assertThat("The bus location is updated", busPage.verifyLocationBusUpdate(), is(true));
    }
    @Author(names = { "Glend" })
    @Module(name = "Search")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test
    public void testBusSearch() throws InterruptedException{
        Map<String, String> param = getParam("testBusToggle");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        BusPage<?> busPage = new BusPage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String search = param.get("search");

        step("The user logins to the Bus dashboard.");
        busPage.goToBusPage(username, password);

        step("Open Bus Page");
        busPage.openBusPage();

        step("The user goes to the Location menu.");
        busPage.locationBusView();

        step("The user searches a location.");
        busPage.locationBusSearch(search);
        Assertion.assertThat("The user is able to see the searched location", busPage.verifyLocationBusSearch(), is(true));
    }
    @Author(names = { "Glend" })
    @Module(name = "BusVehicle")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test
    public void testBusVehicleView() throws InterruptedException{
        Map<String, String> param = getParam("testBusVehicleView");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        BusPage<?> busPage = new BusPage<>(session);
        String username = param.get("username");
        String password = param.get("password");

        step("Login to Bus");
        busPage.goToBusPage(username, password);

        step("Open Bus Page");
        busPage.openBusPage();

        step("View Bus Vehicle");
        busPage.vehicleBusView();
        Assertion.assertThat("The bus vehicle is shown", busPage.verifyVehicleBusView(), is(true));
    }

    @Author(names = { "Glend", "Clement"})
    @Module(name = "BusVehicle")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test
    public void testBusVehicleCreate() throws InterruptedException {
        Map<String, String> param = getParam("testBusVehicleCreate");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        BusPage<?> busPage = new BusPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String label = param.get("label");
        String searchType = param.get("searchType");
        String classNameID = param.get("classNameID");
        String classNameEN = param.get("classNameEN");

        step("The user logins to Bus dashboard.");
        busPage.goToBusPage(username, password);

        step("Open Bus Page");
        busPage.openBusPage();

        step("The user goes to Vehicle menu.");
        busPage.vehicleBusView();

        step("The user creates the vehicle.");
        busPage.createVehicle(label, searchType, classNameID, classNameEN);
        Assertion.assertThat("The vehicle has been successfully created", busPage.verifySuccessSnackbarIsDisplayed(), is(true));

        step("The user searches the created vehicle to make sure the vehicle is available on the dashboard");
        busPage.searchVehicle(label);
        Assertion.assertThat("The created vehicle is displayed.", busPage.verifySearch(), is(true));
    }

    @Author(names = { "Glend" })
    @Module(name = "BusVehicle")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testBusVehicleCreate")
    public void testBusVehicleActivateInactivateStatus() throws InterruptedException{
        Map<String, String> param = getParam("testBusVehicleActivateInactivateStatus");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        BusPage<?> busPage = new BusPage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String vehicle = param.get("vehicle");

        step("The user logins to Bus dashboard.");
        busPage.goToBusPage(username, password);

        step("Open Bus Page");
        busPage.openBusPage();

        step("The user searches for the desired vehicle that needs to be activated and/or deactivated.");
        busPage.searchVehicle(vehicle);

        step("The user activates and/or inactivates the vehicle status");
        Assertion.assertThat("The bus vehicle is successfully activated or inactivated", busPage.verifyVehicleBusActivateInactivate(), is(true));
        Assertion.assertThat("The bus vehicle is successfully activated or inactivated", busPage.verifyVehicleBusActivateInactivate(), is(true));
    }

    @Author(names = { "Glend" })
    @Module(name = "BusVehicle")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testBusVehicleCreate")
    public void testBusVehicleUpdate() throws InterruptedException{
        Map<String, String> param = getParam("testBusVehicleUpdate");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        BusPage<?> busPage = new BusPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String vehicle = param.get("vehicle");
        String newIDDescription = param.get("newIDDescription");
        String newENDescription = param.get("newENDescription");

        step("The user login to Bus dashboard.");
        busPage.goToBusPage(username, password);

        step("Open Bus Page");
        busPage.openBusPage();

        step("The user goes to Vehicle menu.");
        busPage.vehicleBusView();

        step("The user searches for the desired vehicle that needs to be updated.");
        busPage.searchVehicle(vehicle);

        step("The user fills the fields and update the vehicle");
        busPage.vehicleBusUpdate(newIDDescription, newENDescription);
        Assertion.assertThat("The bus vehicle has been successfully updated.", busPage.verifySuccessSnackbarIsDisplayed(), is(true));
    }
    @Author(names = { "Glend" })
    @Module(name = "BusVehicle")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testBusVehicleUpdate")
    public void testBusVehicleDelete() throws InterruptedException{
        Map<String, String> param = getParam("testBusVehicleDelete");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        BusPage<?> busPage = new BusPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String createdVehicle = param.get("createdVehicle");

        step("The user login to Bus dashboard.");
        busPage.goToBusPage(username, password);

        step("Open Bus Page");
        busPage.openBusPage();

        step("The user goes to Vehicle menu.");
        busPage.vehicleBusView();

        step("The user searches for the created vehicle from the previous test case.");
        busPage.searchVehicle(createdVehicle);

        step("The user deletes the created vehicle from the previous test case.");
        busPage.deleteVehicle();
        Assertion.assertThat("The bus vehicle has been successfully deleted.", busPage.verifySuccessSnackbarIsDisplayed(), is(true));

        step("The user searches for the deleted vehicle to make sure the vehicle is not available the dashboard.");
        busPage.searchVehicle(createdVehicle);
        Assertion.assertThat("The deleted vehicle is not available when searched", busPage.verifyVehicleSearchAfterDeletion(), is(true));
    }
    @Author(names = { "Glend", "Clement", "Eka"})
    @Module(name = "BusFacilities")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test
    public void testBusFacilitiesView() throws InterruptedException{
        Map<String, String> param = getParam("testBusFacilitiesView");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        BusPage<?> busPage = new BusPage<>(session);
        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to Bus dashboard.");
        busPage.goToBusPage(username, password);

        step("Open Bus Page");
        busPage.openBusPage();

        step("The user views the Facilities menu.");
        busPage.facilitiesBusView();
        Assertion.assertThat("The bus facility is displayed.", busPage.verifyFacilitiesBusView(), is(true));
    }

    @Author(names = { "Glend", "Clement", "Eka"})
    @Module(name = "BusFacilities")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test
    public void testBusFacilitiesCreate() throws InterruptedException {
        Map<String, String> param = getParam("testBusFacilitiesCreate");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        BusPage<?> busPage = new BusPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String label = param.get("label");
        Integer priorityScoring = Integer.valueOf(param.get("priorityScoring"));
        String facilityNameID = param.get("facilityNameID");
        String facilityNameEN = param.get("facilityNameEN");

        step("The user logins to Bus dashboard.");
        busPage.goToBusPage(username, password);

        step("Open Bus Page");
        busPage.openBusPage();

        step("The user goes to the Facilities menu.");
        busPage.facilitiesBusView();

        step("The user creates a facility.");
        busPage.createFacility(label, priorityScoring, facilityNameID, facilityNameEN);
        Assertion.assertThat("The facility has been successfully created", busPage.verifySuccessSnackbarIsDisplayed(), is(true));

        step("The user searches the created facility to make sure the facility is available on the dashboard");
        busPage.searchFacility(label);
        Assertion.assertThat("The created vehicle is displayed.", busPage.verifySearch(), is(true));
    }

    @Author(names = { "Glend", "Clement", "Eka"})
    @Module(name = "BusFacilities")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testBusFacilitiesCreate")
    public void testBusFacilitiesActivateInactivateStatus() throws InterruptedException{
        Map<String, String> param = getParam("testBusFacilitiesActivateInactivateStatus");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        BusPage<?> busPage = new BusPage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String facility = param.get("facility");

        step("The user logins to Bus dashboard.");
        busPage.goToBusPage(username, password);

        step("Open Bus Page");
        busPage.openBusPage();

        step("The user goes to Facility menu");
        busPage.facilitiesBusView();

        step("The user searches for the desired facility that needs to be activated and/or deactivated.");
        busPage.searchFacility(facility);

        step("The user activates and/or inactivates the facility status");
        busPage.activateOrInactivateFacility();
        Assertion.assertThat("The bus vehicle is successfully activated or inactivated", busPage.verifySuccessActivationOrInactivationFacility(), is(true));
        busPage.activateOrInactivateFacility();
        Assertion.assertThat("The bus vehicle is successfully activated or inactivated", busPage.verifySuccessActivationOrInactivationFacility(), is(true));
    }

    @Author(names = { "Glend", "Clement", "Eka"})
    @Module(name = "BusFacilities")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testBusFacilitiesCreate")
    public void testBusFacilitiesUpdate() throws InterruptedException{
        Map<String, String> param = getParam("testBusFacilitiesUpdate");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        BusPage<?> busPage = new BusPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String facility = param.get("facility");

        step("The user login to Bus dashboard.");
        busPage.goToBusPage(username, password);

        step("Open Bus Page");
        busPage.openBusPage();

        step("The user goes to the Facility menu.");
        busPage.facilitiesBusView();

        step("The user searches for the desired facility that needs to be updated.");
        busPage.searchFacility(facility);

        step("The user fills the fields and update the facility");
        busPage.facilityBusUpdate();
        busPage.deleteFacility();
        Assertion.assertThat("The bus facility has been successfully updated.", busPage.verifySuccessSnackbarIsDisplayed(), is(true));
    }

    @Author(names = { "Glend", "Clement", "Eka"})
    @Module(name = "BusFacilities")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testBusFacilitiesUpdate")
    public void testBusFacilitiesDelete() throws InterruptedException {
        Map<String, String> param = getParam("testBusFacilitiesDelete");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        BusPage<?> busPage = new BusPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String createdFacility = param.get("createdFacility");

        step("The user login to Bus dashboard.");
        busPage.goToBusPage(username, password);

        step("Open Bus Page");
        busPage.openBusPage();

        step("The user goes to the Facility menu.");
        busPage.facilitiesBusView();

        step("The user searches for the created facility from the previous test case.");
        busPage.searchFacility(createdFacility);

        step("The user deletes the created facility from the previous test case.");
        busPage.deleteFacility();
        Assertion.assertThat("The facility has been successfully deleted.", busPage.verifySuccessSnackbarIsDisplayed(), is(true));

        step("The user searches for the deleted facility to make sure the facility is not available the dashboard.");
        busPage.searchVehicle(createdFacility);
        Assertion.assertThat("The deleted facility is not available anymore and can not be searched.", busPage.verifyFacilitySearchAfterDeletion(), is(true));
    }

    @Author(names = { "Glend" })
    @Module(name = "Vendor Operator")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test
    public void testBusVendorOperatorSearch() throws InterruptedException{
        Map<String, String> param = getParam("testBusVendorOperatorSearch");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        BusPage<?> busPage = new BusPage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String search = param.get("search");

        step("The user logins to the Bus dashboard.");
        busPage.goToBusPage(username, password);

        step("Open Bus Page");
        busPage.openBusPage();

        step("The user goes to the Vendor Operator menu");
        busPage.vendorOperatorBusView();

        step("The user searches the Operator name");
        busPage.dataMappingBusSearch(search);
        Assertion.assertThat("The operator name can be searched and is available", busPage.verifyDataMappingBusSearch(search), is(true));
    }

    @Author(names = { "Glend", "Clement" })
    @Module(name = "Vendor Operator")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test
    public void testBusVendorOperatorMapUnmap() throws InterruptedException{
        Map<String, String> param = getParam("testBusVendorOperatorMapUnmap");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        BusPage<?> busPage = new BusPage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String searchOperatorName = param.get("searchOperatorName");
        String searchTiketsVehicle = param.get("searchTiketsVehicle");
        String searchVendorsVendor = param.get("searchVendorsVendor");
        String searchVendorsOperator =  param.get("searchVendorsOperator");
        String searchVendorsVehicle =  param.get("searchVendorsVehicle");

        step("The user logins to the Bus dashboard.");
        busPage.goToBusPage(username, password);

        step("Open Bus Page");
        busPage.openBusPage();

        step("The user goes to the Vendor Operator menu");
        busPage.vendorOperatorBusView();

        step("The user maps the operator");
        busPage.vendorOperatorBusMap(searchTiketsVehicle, searchVendorsVendor,
                searchVendorsOperator, searchVendorsVehicle);
        Assertion.assertThat("The operator has been successfully mapped", busPage.verifyDataMappingBusMap(), is(true));

        step("The user searches the Operator name to unmap");
        busPage.dataMappingBusSearch(searchOperatorName);

        step("The user unmaps the Operator name");
        busPage.dataMappingBusUnmap();
        Assertion.assertThat("The operator has been successfully unmapped", busPage.verifyDataMappingBusUnmap(), is(true));
    }

    @Author(names = { "Glend" })
    @Module(name = "Vendor Operator")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test
    public void testBusVendorOperatorPagination() throws InterruptedException{
        Map<String, String> param = getParam("testBusVendorOperatorPagination");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        BusPage<?> busPage = new BusPage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        Integer nextPageCount = Integer.valueOf(param.get("nextPageCount"));
        Integer previousPageCount = Integer.valueOf(param.get("previousPageCount"));

        step("The user logins to the Bus dashboard.");
        busPage.goToBusPage(username, password);

        step("Open Bus Page");
        busPage.openBusPage();

        step("The user goes to the Vendor Operator menu");
        busPage.vendorOperatorBusView();

        step("The user clicks the > icon to be directed to the next page");
        busPage.dataMappingBusNextPage(nextPageCount);
        Assertion.assertThat("The user sees the next page", busPage.verifyDataMappingBusNextPage(nextPageCount), is(true));

        step("The user clicks the < icon to be directed to the previous page");
        busPage.dataMappingBusPreviousPage(previousPageCount);
        Assertion.assertThat("The user sees the previous page", busPage.verifyDataMappingBusPreviousPage(nextPageCount, previousPageCount), is(true));
    }

    @Author(names = { "Glend" })
    @Module(name = "Vendor Operator")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test
    public void testBusVendorOperatorDeleteMapping() throws InterruptedException{
        Map<String, String> param = getParam("testBusVendorOperatorDeleteMapping");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        BusPage<?> busPage = new BusPage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        Integer addAnotherMappingCount = Integer.valueOf(param.get("addAnotherMappingCount"));
        Integer deleteMappingCount = Integer.valueOf(param.get("deleteMappingCount"));

        step("The user logins to the Bus dashboard.");
        busPage.goToBusPage(username, password);

        step("Open Bus Page");
        busPage.openBusPage();

        step("The user goes to the Vendor Operator menu");
        busPage.vendorOperatorBusView();

        step("The user deletes the mapping");
        busPage.dataMappingBusDeleteMapping(addAnotherMappingCount, deleteMappingCount);
        Assertion.assertThat("The tiket's data and the vendor's data still appears", busPage.verifyDataMappingBusDeleteMapping(), is(true));
    }

    @Author(names = { "Glend" })
    @Module(name = "Vendor Location")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test
    public void testBusVendorLocationSearch() throws InterruptedException{
        Map<String, String> param = getParam("testBusVendorLocationSearch");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        BusPage<?> busPage = new BusPage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String search = param.get("search");

        step("The user logins to the Bus dashboard.");
        busPage.goToBusPage(username, password);

        step("Open Bus Page");
        busPage.openBusPage();

        step("The user goes to the Vendor Location menu");
        busPage.vendorLocationBusView();

        step("The user searches the Location name");
        busPage.dataMappingBusSearch(search);
        Assertion.assertThat("The location can be searched and is available", busPage.verifyDataMappingBusSearch(search), is(true));
    }

    @Author(names = { "Glend" })
    @Module(name = "Vendor Location")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test
    public void testBusVendorLocationMapUnmap() throws InterruptedException{
        Map<String, String> param = getParam("testBusVendorLocationMapUnmap");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        BusPage<?> busPage = new BusPage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String searchTiketsLocation = param.get("searchTiketsLocation");
        String searchVendorsVendor = param.get("searchVendorsVendor");
        String searchVendorsLocation =  param.get("searchVendorsLocation");

        step("The user logins to the Bus dashboard.");
        busPage.goToBusPage(username, password);

        step("Open Bus Page");
        busPage.openBusPage();

        step("The user goes to the Vendor Location menu");
        busPage.vendorLocationBusView();

        step("The user maps the location");
        busPage.vendorLocationBusMap(searchTiketsLocation, searchVendorsVendor,
                searchVendorsLocation);
        Assertion.assertThat("The location has been successfully mapped", busPage.verifyDataMappingBusMap(), is(true));

        step("The user searches the location name to unmap");
        busPage.dataMappingBusSearch(searchTiketsLocation);

        step("The user unmaps the location name");
        busPage.dataMappingBusUnmap();
        Assertion.assertThat("The location has been successfully unmapped", busPage.verifyDataMappingBusUnmap(), is(true));
    }

    @Author(names = { "Glend", "Clement"})
    @Module(name = "Vendor Location")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test
    public void testBusVendorLocationPagination() throws InterruptedException{
        Map<String, String> param = getParam("testBusVendorLocationPagination");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        BusPage<?> busPage = new BusPage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        Integer nextPageCount = Integer.valueOf(param.get("nextPageCount"));
        Integer previousPageCount = Integer.valueOf(param.get("previousPageCount"));

        step("The user logins to the Bus dashboard.");
        busPage.goToBusPage(username, password);

        step("Open Bus Page");
        busPage.openBusPage();

        step("The user goes to the Vendor Location menu");
        busPage.vendorLocationBusView();

        step("The user clicks the > icon to be directed to the next page");
        busPage.dataMappingBusNextPage(nextPageCount);
        Assertion.assertThat("The user sees the next page", busPage.verifyDataMappingBusNextPage(nextPageCount), is(true));

        step("The user clicks the < icon to be directed to the previous page");
        busPage.dataMappingBusPreviousPage(previousPageCount);
        Assertion.assertThat("The user sees the previous page", busPage.verifyDataMappingBusPreviousPage(nextPageCount, previousPageCount), is(true));
    }

    @Author(names = { "Glend" })
    @Module(name = "Vendor Location")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test
    public void testBusVendorLocationDeleteMapping() throws InterruptedException{
        Map<String, String> param = getParam("testBusVendorLocationDeleteMapping");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        BusPage<?> busPage = new BusPage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        Integer addAnotherMappingCount = Integer.valueOf(param.get("addAnotherMappingCount"));
        Integer deleteMappingCount = Integer.valueOf(param.get("deleteMappingCount"));

        step("The user logins to the Bus dashboard.");
        busPage.goToBusPage(username, password);

        step("Open Bus Page");
        busPage.openBusPage();

        step("The user goes to the Vendor Location menu");
        busPage.vendorLocationBusView();

        step("The user deletes the mapping");
        busPage.dataMappingBusDeleteMapping(addAnotherMappingCount, deleteMappingCount);
        Assertion.assertThat("The tiket's data and the vendor's data still appears", busPage.verifyDataMappingBusDeleteMapping(), is(true));
    }

    @Author(names = { "Glend" })
    @Module(name = "Vendor Location")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test
    public void testBusVendorLocationSearchDuringMap() throws InterruptedException{
        Map<String, String> param = getParam("testBusVendorLocationSearchDuringMap");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        BusPage<?> busPage = new BusPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String searchTiketsLocation = param.get("searchTiketsLocation");
        String searchVendorsVendor = param.get("searchVendorsVendor");
        String searchVendorsLocation = param.get("searchVendorsLocation");

        step("The user logins to the Bus dashboard.");
        busPage.goToBusPage(username, password);

        step("Open Bus Page");
        busPage.openBusPage();

        step("The user goes to the Vendor Location menu.");
        busPage.vendorLocationBusView();

        step("The user sees the tiket's location, vendor's vendor, and vendor's location data in dropdowns after clicking the +Add Another Route button.");
        busPage.vendorLocationBusSearchDuringMap(searchTiketsLocation, searchVendorsVendor, searchVendorsLocation);
        Assertion.assertThat("The tiket's location, vendor's vendor, and vendor's location data is displayed", busPage.verifyVendorLocationBusSearchDuringMap(searchTiketsLocation, searchVendorsVendor, searchVendorsLocation), is(true));
    }

    @Author(names = { "Glend", "Clement" })
    @Module(name = "BusOperator")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testBusOperatorCreate")
    public void testBusOperatorSearch() throws InterruptedException{
        Map<String, String> param = getParam("testBusOperatorSearch");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        BusPage<?> busPage = new BusPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String search = param.get("search");

        step("The user logins to the Bus dashboard.");
        busPage.goToBusPage(username, password);

        step("Open Bus Page");
        busPage.openBusPage();

        step("The user goes to the operator menu.");
        busPage.operatorBusView();

        step("The user searches the operator.");
        busPage.operatorBusSearch(search);
        Assertion.assertThat("The searched operator is displayed.", busPage.verifyOperatorBusSearch(search), is(true));
    }

    @Author(names = { "Glend", "Clement" })
    @Module(name = "BusOperator")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test
    public void testBusOperatorCreate() throws InterruptedException{
        Map<String, String> param = getParam("testBusOperatorCreate");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        BusPage<?> busPage = new BusPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String operatorName = param.get("operatorName");

        step("The user logins to the Bus dashboard.");
        busPage.goToBusPage(username, password);

        step("Open Bus Page");
        busPage.openBusPage();

        step("The user goes to the operator menu.");
        busPage.operatorBusView();

        step("The user creates the operator.");
        busPage.operatorBusCreate(operatorName);
        Assertion.assertThat("The bus operator has been created successfully.", busPage.verifyOperatorBusCreate(), is(true));

        step("The user searches the newly created operator.");
        busPage.operatorBusSearch(operatorName);
        Assertion.assertThat("The newly created operator is displayed.", busPage.verifyOperatorBusSearch(operatorName), is(true));
    }

    @Author(names = { "Glend" })
    @Module(name = "Route Management")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test
    public void testBusRouteManagementAdd() throws InterruptedException{
        Map<String, String> param = getParam("testBusRouteManagementAdd");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        BusPage<?> busPage = new BusPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String origin = param.get("origin");
        String destination = param.get("destination");

        step("The user logins to the Bus dashboard.");
        busPage.goToBusPage(username, password);

        step("Open Bus Page");
        busPage.openBusPage();

        step("The user goes to the Route Management menu.");
        busPage.routeManagementBusView();

        step("The user adds a route.");
        busPage.routeManagementBusAdd(origin, destination);
        Assertion.assertThat("The route has been successfully added", busPage.verifyRouteManagementBusAdd(), is(true));

        step("The user searches the newly created route.");
        busPage.routeManagementBusSearchOrigin(origin);
        busPage.routeManagementBusSearchDestination(destination);
        Assertion.assertThat("The newly added route that contains the searched origin & destination is displayed.", busPage.verifyRouteManagementBusSearch(origin) && busPage.verifyRouteManagementBusSearch(destination), is(true));
    }

    @Author(names = { "Glend" })
    @Module(name = "Route Management")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testBusRouteManagementAdd")
    public void testBusRouteManagementDelete() throws InterruptedException{
        Map<String, String> param = getParam("testBusRouteManagementDelete");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        BusPage<?> busPage = new BusPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String origin = param.get("origin");
        String destination = param.get("destination");

        step("The user logins to the Bus dashboard.");
        busPage.goToBusPage(username, password);

        step("Open Bus Page");
        busPage.openBusPage();

        step("The user goes to the Route Management menu.");
        busPage.routeManagementBusView();

        step("The user searches the origin.");
        busPage.routeManagementBusSearchOrigin(origin);
        Assertion.assertThat("The route(s) that contain(s) the searched origin is/are displayed.", busPage.verifyRouteManagementBusSearch(origin), is(true));

        step("The user searches the destination.");
        busPage.routeManagementBusSearchDestination(destination);
        Assertion.assertThat("The route(s) that contain(s) the searched origin is/are displayed.", busPage.verifyRouteManagementBusSearch(destination), is(true));

        step("The user retrieves the route and deletes it.");
        busPage.routeManagementBusDelete();
        Assertion.assertThat("The route has been successfully deleted.", busPage.verifyRouteManagementDelete(), is(true));

        step("The user searches the deleted route.");
        busPage.routeManagementBusSearchOrigin(origin);
        busPage.routeManagementBusSearchDestination(destination);
        Assertion.assertThat("The deleted route is not available.", busPage.verifyBusEmptyList(), is(true));
    }

    @Author(names = { "Glend" })
    @Module(name = "Route Management")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test
    public void testBusRouteManagementSearch() throws InterruptedException{
        Map<String, String> param = getParam("testBusRouteManagementSearch");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        BusPage<?> busPage = new BusPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String origin = param.get("origin");
        String destination = param.get("destination");

        step("The user logins to the Bus dashboard.");
        busPage.goToBusPage(username, password);

        step("Open Bus Page");
        busPage.openBusPage();

        step("The user goes to the Route Management menu.");
        busPage.routeManagementBusView();

        step("The user searches the origin.");
        busPage.routeManagementBusSearchOrigin(origin);
        Assertion.assertThat("The route(s) that contain(s) the searched origin is/are displayed.", busPage.verifyRouteManagementBusSearch(origin), is(true));

        step("The user searches the destination.");
        busPage.routeManagementBusSearchDestination(destination);
        Assertion.assertThat("The route(s) that contain(s) the searched origin is/are displayed.", busPage.verifyRouteManagementBusSearch(destination), is(true));
    }

    @Author(names = { "Glend" })
    @Module(name = "Route Management")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test
    public void testBusRouteManagementPagination() throws InterruptedException{
        Map<String, String> param = getParam("testBusRouteManagementPagination");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        BusPage<?> busPage = new BusPage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        Integer nextPageCount = Integer.valueOf(param.get("nextPageCount"));
        Integer previousPageCount = Integer.valueOf(param.get("previousPageCount"));

        step("The user logins to the Bus dashboard.");
        busPage.goToBusPage(username, password);

        step("Open Bus Page");
        busPage.openBusPage();

        step("The user goes to the Route Management menu.");
        busPage.routeManagementBusView();

        step("The user clicks the > icon to be directed to the next page");
        busPage.routeManagementBusNextPage(nextPageCount);
        Assertion.assertThat("The user sees the next page", busPage.verifyRouteManagementBusNextPage(nextPageCount), is(true));

        step("The user clicks the < icon to be directed to the previous page");
        busPage.routeManagementBusPreviousPage(previousPageCount);
        Assertion.assertThat("The user sees the previous page", busPage.verifyRouteManagementBusPreviousPage(nextPageCount, previousPageCount), is(true));
    }

    @Author(names = { "Glend" })
    @Module(name = "Route Management")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test
    public void testBusRouteManagementSearchDuringAdd() throws InterruptedException{
        Map<String, String> param = getParam("testBusRouteManagementSearchDuringAdd");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        BusPage<?> busPage = new BusPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String origin = param.get("origin");
        String destination = param.get("destination");
        Integer addAnotherRouteCount = Integer.valueOf(param.get("addAnotherRouteCount"));
        Integer deleteAddAnotherRouteCount = Integer.valueOf(param.get("deleteAddAnotherRouteCount"));

        step("The user logins to the Bus dashboard.");
        busPage.goToBusPage(username, password);

        step("Open Bus Page");
        busPage.openBusPage();

        step("The user goes to the Route Management menu.");
        busPage.routeManagementBusView();

        step("The user sees the origin & destination data in dropdowns after clicking the +Add Another Route button.");
        busPage.routeManagementBusSearchDuringAdd(origin, destination, addAnotherRouteCount, deleteAddAnotherRouteCount);
        Assertion.assertThat("The origin & destination data is displayed", busPage.verifyRouteManagementBusSearchDuringAdd(origin, destination), is(true));
    }

    @Author(names = { "Glend" })
    @Module(name = "Location")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test
    public void testBusLocationCreate() throws InterruptedException {
        Map<String, String> param = getParam("testBusLocationCreate");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        BusPage<?> busPage = new BusPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String name = param.get("name");
        String locationType = param.get("locationType");
        Integer latitude = Integer.valueOf(param.get("latitude"));
        Integer longitude = Integer.valueOf(param.get("longitude"));
        String city = param.get("city");
        String locationAddress = param.get("locationAddress");

        step("The user logins to the Bus dashboard.");
        busPage.goToBusPage(username, password);

        step("Open Bus Page");
        busPage.openBusPage();

        step("The user goes to the Location menu.");
        busPage.locationBusView();

        step("The user adds a location.");
        busPage.locationBusAdd(name, locationType, latitude, longitude, city, locationAddress);
        Assertion.assertThat("The location has been successfully added", busPage.verifyLocationBusAdd(), is(true));

        step("The user searches the newly created location.");
        busPage.locationBusSearch(name);
        Assertion.assertThat("The newly created location can be searched", busPage.verifyLocationBusSearch(), is(true));
    }

    @Author(names = { "Glend" })
    @Module(name = "BusLocationPoint")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test
    public void testBusLocationPointView() throws InterruptedException{
        Map<String, String> param = getParam("testBusLocationPointView");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        BusPage<?> busPage = new BusPage<>(session);
        String username = param.get("username");
        String password = param.get("password");

        step("Login to Bus");
        busPage.goToBusPage(username, password);

        step("Open Bus Page");
        busPage.openBusPage();

        step("View Bus Location Point");
        busPage.locationPointBusView();
        Assertion.assertThat("The bus location point is shown", busPage.verifyLocationPointBusView(), is(true));
    }

    @Author(names = { "Glend", "Clement"})
    @Module(name = "BusLocationPoint")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test
    public void testBusLocationPointActivateInactivateStatus() throws InterruptedException {
        Map<String, String> param = getParam("testBusLocationPointActivateInactivateStatus");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        BusPage<?> busPage = new BusPage<>(session);
        String username = param.get("username");
        String password = param.get("password");

        step("Login to Bus");
        busPage.goToBusPage(username, password);

        step("Open Bus Page");
        busPage.openBusPage();

        step("Activate and Inactivate the Bus Location Point");
        busPage.locationPointBusActivateInactivate();
        Assertion.assertThat("The bus location point is successfully activated and inactivated", busPage.verifyLocationPointBusActivateInactivate(), is(true));
    }

    @Author(names = { "Glend" })
    @Module(name = "BusLocationPoint")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test
    public void testBusLocationPointUpdate() throws InterruptedException{
        Map<String, String> param = getParam("testBusLocationPointUpdate");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        BusPage<?> busPage = new BusPage<>(session);
        String username = param.get("username");
        String password = param.get("password");

        step("Login to Bus");
        busPage.goToBusPage(username, password);

        step("Open Bus Page");
        busPage.openBusPage();

        step("Update the location point");
        busPage.locationPointBusUpdate();
        Assertion.assertThat("The bus location point is updated", busPage.verifyLocationPointBusUpdate(), is(true));
    }

    @Author(names = { "Glend", "Clement" })
    @Module(name = "BusLocationPoint")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test
    public void testBusLocationPointSearch() throws InterruptedException{
        Map<String, String> param = getParam("testBusLocationPointSearch");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        BusPage<?> busPage = new BusPage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String locationPoint = param.get("locationPoint");

        step("Login to Bus");
        busPage.goToBusPage(username, password);

        step("Open Bus Page");
        busPage.openBusPage();

        step("Search the location point");
        busPage.locationPointBusSearch(locationPoint);
        Assertion.assertThat("The user is able to see the searched location point", busPage.verifyLocationPointBusSearch(), is(true));
    }

    @Author(names = { "Clement" })
    @Module(name = "Location")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testBusLocationCreate")
    public void testBusLocationDelete() throws InterruptedException {
        Map<String, String> param = getParam("testBusLocationDelete");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        BusPage<?> busPage = new BusPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String createdLocation = param.get("createdLocation");

        step("The user logins to the Bus dashboard.");
        busPage.goToBusPage(username, password);

        step("Open Bus Page");
        busPage.openBusPage();

        step("The user goes to the Location menu.");
        busPage.locationBusView();

        step("The user searches for the created location from the previous test case.");
        busPage.searchLocation(createdLocation);

        step("The user deactivates the created location from the previous test case.");
        busPage.clickToggleInactive();

        step("The user deletes the created location from the previous test case.");
        busPage.deleteLocation();
        Assertion.assertThat("The location has been successfully deleted.", busPage.verifySuccessSnackbarIsDisplayed(), is(true));

        step("The user searches for the deleted location to make sure the location is not available the dashboard.");
        busPage.searchLocation(createdLocation);
        Assertion.assertThat("The location has been successfully deleted. (currently using snackbar since there are a lot of duplicates)", busPage.verifySuccessSnackbarIsDisplayed(), is(true));
    }

    @Author(names = { "Glend" })
    @Module(name = "BusOperator")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test
    public void testBusOperatorView() throws InterruptedException{
        Map<String, String> param = getParam("testBusOperatorView");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        BusPage<?> busPage = new BusPage<>(session);
        String username = param.get("username");
        String password = param.get("password");

        step("Login to Bus");
        busPage.goToBusPage(username, password);

        step("Open Bus Page");
        busPage.openBusPage();

        step("View Bus Operator");
        busPage.operatorBusView();
        Assertion.assertThat("The bus operator is shown", busPage.verifyOperatorBusView(), is(true));
    }

    @Author(names = { "Glend" })
    @Module(name = "BusOperator")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testBusOperatorCreate")
    public void testBusOperatorUpdate() throws InterruptedException{
        Map<String, String> param = getParam("testBusOperatorUpdate");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        BusPage<?> busPage = new BusPage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String operatorName = param.get("operatorName");

        step("Login to Bus");
        busPage.goToBusPage(username, password);

        step("Open Bus Page");
        busPage.openBusPage();

        step("The user searches the operator.");
        busPage.searchOperator(operatorName);

        step("Update the operator");
        busPage.operatorBusUpdate();
        Assertion.assertThat("The bus operator is updated", busPage.verifyOperatorBusUpdate(), is(true));
    }

    @Author(names = { "Glend", "Clement" })
    @Module(name = "BusOperator")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testBusOperatorCreate")
    public void testBusOperatorActivateInactivateStatus() throws InterruptedException {
        Map<String, String> param = getParam("testBusOperatorActivateInactivateStatus");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        BusPage<?> busPage = new BusPage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String operatorName = param.get("operatorName");

        step("Login to Bus");
        busPage.goToBusPage(username, password);

        step("Open Bus Page");
        busPage.openBusPage();

        step("The user search the operator.");
        busPage.searchOperator(operatorName);

        step("Activate and Inactivate the bus operator status");
        Assertion.assertThat("The bus operator status is successfully activated/deactivated", busPage.verifyOperatorBusActivateOrInactivateStatus(), is(true));
        Assertion.assertThat("The bus operator status is successfully activated/deactivated", busPage.verifyOperatorBusActivateOrInactivateStatus(), is(true));
        step("Activate and Inactivate the bus operator reschedule");
        Assertion.assertThat("The bus operator reschedule is successfully activated/inactivated", busPage.verifyOperatorBusActivateOrInactivateReschedule(), is(true));
        Assertion.assertThat("The bus operator reschedule is successfully activated/inactivated", busPage.verifyOperatorBusActivateOrInactivateReschedule(), is(true));
        step("Activate and Inactivate the bus operator refund");
        Assertion.assertThat("The bus operator refund is successfully activated/inactivated", busPage.verifyOperatorBusActivateOrInactivateRefund(), is(true));
        Assertion.assertThat("The bus operator refund is successfully activated/inactivated", busPage.verifyOperatorBusActivateOrInactivateRefund(), is(true));
    }

    @Author(names = { "Clement" })
    @Module(name = "BusOperator")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testBusOperatorCreate")
    public void testBusOperatorDelete() throws InterruptedException{
        Map<String, String> param = getParam("testBusOperatorDelete");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        BusPage<?> busPage = new BusPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String operatorName = param.get("operatorName");

        step("The user logins to the Bus dashboard.");
        busPage.goToBusPage(username, password);

        step("Open Bus Page");
        busPage.openBusPage();

        step("The user goes to the operator menu.");
        busPage.operatorBusView();

        step("The user deletes the operator.");
        busPage.operatorBusSearch(operatorName);
        busPage.operatorBusDelete();
        Assertion.assertThat("The operator has been successfully deleted.", busPage.verifySuccessSnackbarIsDisplayed(), is(true));

        step("The user searches the deleted operator.");
        busPage.operatorBusSearch(operatorName);
        Assertion.assertThat("The operator has been successfully deleted and can't be searched.", busPage.verifyOperatorBusSearch(operatorName), is(false));
    }

}