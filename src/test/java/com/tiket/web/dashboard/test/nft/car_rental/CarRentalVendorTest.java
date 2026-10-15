package com.tiket.web.dashboard.test.nft.car_rental;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.nft.car_rental.CarRentalPage;
import com.tiket.web.dashboard.page.nft.car_rental.CarRentalVendorPage;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.core.Is.is;

public class CarRentalVendorTest extends BaseTest {
    @Author(names = {"Okta", "Eka"})
    @Module(name = "Account")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testAddAccount() {
        Map<String, String> param = getParam("testAddAccount");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        CarRentalPage<?> adminPage = new CarRentalPage<>(session);
        CarRentalVendorPage<?> vendorPage = new CarRentalVendorPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        String businessName = param.get("businessName");
        String name = param.get("name");
        String email = param.get("email");
        String phone = param.get("phone");

        step("User open car rental dashboard");
        adminPage.openCarRentalPage(username, password);

        step("User open business profile sub menu");
        adminPage.openBusinessProfileSubMenu();

        step("User search business profile");
        Assertion.assertThat("The result for business profile is displayed according to input", adminPage.searchBusinessProfile(businessName), is(true));
        String businessProfileId = adminPage.getBusinessProfileId();

        step("User open manage account sub menu");
        vendorPage.openManageAccountSubMenu(businessProfileId);

        step("User add account");
        vendorPage.addAccount(name, email, phone);
        Assertion.assertThat("Success alert is displayed for successful add account", adminPage.verifySuccessAlertIsDisplayed(), is(true));

    }

    @Author(names = "Okta")
    @Module(name = "Account")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testAddAccount")
    public void testEditAccount() {
        Map<String, String> param = getParam("testEditAccount");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        CarRentalPage<?> adminPage = new CarRentalPage<>(session);
        CarRentalVendorPage<?> vendorPage = new CarRentalVendorPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String businessName = param.get("businessName");
        String name = param.get("name");

        step("User open car rental dashboard");
        adminPage.openCarRentalPage(username, password);

        step("User open business profile sub menu");
        adminPage.openBusinessProfileSubMenu();

        step("User search business profile");
        Assertion.assertThat("The result for business profile is displayed according to input", adminPage.searchBusinessProfile(businessName), is(true));
        String businessProfileId = adminPage.getBusinessProfileId();

        step("User open manage account sub menu");
        vendorPage.openManageAccountSubMenu(businessProfileId);

        step("User update account");
        Assertion.assertThat("Updated account is displayed on the list", vendorPage.editAccount(name), is(true));
        Assertion.assertThat("Success alert is displayed for successful edit account", adminPage.verifySuccessAlertIsDisplayed(), is(true));
    }

    @Author(names = "Okta")
    @Module(name = "PaymentList")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testExportPaymentListVendor() {
        Map<String, String> param = getParam("testExportPaymentListVendor");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        CarRentalPage<?> adminPage = new CarRentalPage<>(session);
        CarRentalVendorPage<?> vendorPage = new CarRentalVendorPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String businessName = param.get("businessName");
        String fileName = param.get("fileName");
        String timeOut = param.get("timeOut");

        step("User open car rental dashboard");
        adminPage.openCarRentalPage(username, password);

        step("User open business profile sub menu");
        adminPage.openBusinessProfileSubMenu();

        step("User search business profile");
        Assertion.assertThat("The result for business profile is displayed according to input", adminPage.searchBusinessProfile(businessName), is(true));
        String businessProfileId = adminPage.getBusinessProfileId();

        step("User open payment list sub menu");
        vendorPage.openPaymentListSubMenu(businessProfileId);

        step("User export payment list");
        vendorPage.exportPaymentList();
        Assertion.assertThat("Verify file has been downloaded", adminPage.verifyDownloadedExport(fileName, timeOut), is(true));
    }

    @Author(names = "Okta")
    @Module(name = "CarPool")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testAddCarPool() {
        Map<String, String> param = getParam("testAddCarPool");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        CarRentalPage<?> adminPage = new CarRentalPage<>(session);
        CarRentalVendorPage<?> vendorPage = new CarRentalVendorPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String businessName = param.get("businessName");
        String region = param.get("region");
        String name = param.get("name");

        step("User open car rental dashboard");
        adminPage.openCarRentalPage(username, password);

        step("User open business profile sub menu");
        adminPage.openBusinessProfileSubMenu();

        step("User search business profile");
        Assertion.assertThat("The result for business profile is displayed according to input", adminPage.searchBusinessProfile(businessName), is(true));
        String businessProfileId = adminPage.getBusinessProfileId();

        step("User open manage car pool sub menu");
        vendorPage.openManageCarPoolSubMenu(businessProfileId);

        step("User add new car pool");
        Assertion.assertThat("New car pool is displayed on the list", vendorPage.addCarPool(region, name), is(true));
        Assertion.assertThat("Snackbar is displayed for successful add car pool", vendorPage.verifySuccessSnackbar(), is(true));
    }

    @Author(names = "Okta")
    @Module(name = "CarPool")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testEditCarPool() {
        Map<String, String> param = getParam("testEditCarPool");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        CarRentalPage<?> adminPage = new CarRentalPage<>(session);
        CarRentalVendorPage<?> vendorPage = new CarRentalVendorPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String businessName = param.get("businessName");
        String region = param.get("region");

        step("User open car rental dashboard");
        adminPage.openCarRentalPage(username, password);

        step("User open business profile sub menu");
        adminPage.openBusinessProfileSubMenu();

        step("User search business profile");
        Assertion.assertThat("The result for business profile is displayed according to input", adminPage.searchBusinessProfile(businessName), is(true));
        String businessProfileId = adminPage.getBusinessProfileId();

        step("User open manage car pool sub menu");
        vendorPage.openManageCarPoolSubMenu(businessProfileId);

        step("User edit car pool");
        Assertion.assertThat("Success snackbar is displayed, and updated car pool is displayed on the list", vendorPage.editCarPool(region), is(true));
    }

    @Author(names = "Okta")
    @Module(name = "CarPool")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testDeleteCarPool() {
        Map<String, String> param = getParam("testDeleteCarPool");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        CarRentalPage<?> adminPage = new CarRentalPage<>(session);
        CarRentalVendorPage<?> vendorPage = new CarRentalVendorPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String businessName = param.get("businessName");

        step("User open car rental dashboard");
        adminPage.openCarRentalPage(username, password);

        step("User open business profile sub menu");
        adminPage.openBusinessProfileSubMenu();

        step("User search business profile");
        Assertion.assertThat("The result for business profile is displayed according to input", adminPage.searchBusinessProfile(businessName), is(true));
        String businessProfileId = adminPage.getBusinessProfileId();

        step("User open manage car pool sub menu");
        vendorPage.openManageCarPoolSubMenu(businessProfileId);

        step("User delete car pool");
        Assertion.assertThat("Success snackbar is displayed, and car pool is deleted from the list", vendorPage.deleteCarPool(), is(true));
    }

    @Author(names = "Okta")
    @Module(name = "CarPool")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testToggleCarPool() {
        Map<String, String> param = getParam("testToggleCarPool");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        CarRentalPage<?> adminPage = new CarRentalPage<>(session);
        CarRentalVendorPage<?> vendorPage = new CarRentalVendorPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String businessName = param.get("businessName");

        step("User open car rental dashboard");
        adminPage.openCarRentalPage(username, password);

        step("User open business profile sub menu");
        adminPage.openBusinessProfileSubMenu();

        step("User search business profile");
        Assertion.assertThat("The result for business profile is displayed according to input", adminPage.searchBusinessProfile(businessName), is(true));
        String businessProfileId = adminPage.getBusinessProfileId();

        step("User open manage car pool sub menu");
        vendorPage.openManageCarPoolSubMenu(businessProfileId);

        step("User deactivate car pool status");
        Assertion.assertThat("Success snackbar is displayed, and car pool status is deactivated", vendorPage.deactivateCarPoolStatus(), is(true));

        step("User activate car pool status");
        Assertion.assertThat("Success snackbar is displayed, and car pool status is activated", vendorPage.activateCarPoolStatus(), is(true));
    }

    @Author(names = "Okta")
    @Module(name = "Regional")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testAddRegional() {
        Map<String, String> param = getParam("testAddRegional");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        CarRentalPage<?> adminPage = new CarRentalPage<>(session);
        CarRentalVendorPage<?> vendorPage = new CarRentalVendorPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String businessName = param.get("businessName");
        String province = param.get("province");

        step("User open car rental dashboard");
        adminPage.openCarRentalPage(username, password);

        step("User open business profile sub menu");
        adminPage.openBusinessProfileSubMenu();

        step("User search business profile");
        Assertion.assertThat("The result for business profile is displayed according to input", adminPage.searchBusinessProfile(businessName), is(true));
        String businessProfileId = adminPage.getBusinessProfileId();

        step("User open manage regional sub menu");
        vendorPage.openManageRegionalSubMenu(businessProfileId);

        step("User add regional");
        vendorPage.addRegional(province);
        Assertion.assertThat("Success alert is displayed for successful add regional", adminPage.verifySuccessAlertIsDisplayed(), is(true));

        step("User open manage regional sub menu");
        vendorPage.openManageRegionalSubMenu(businessProfileId);
        Assertion.assertThat("Newly added regional is displayed on the list", vendorPage.verifyResultIsDisplayed(province), is(true));
    }

    @Author(names = "Okta")
    @Module(name = "Regional")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testToggleRegional() {
        Map<String, String> param = getParam("testToggleRegional");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        CarRentalPage<?> adminPage = new CarRentalPage<>(session);
        CarRentalVendorPage<?> vendorPage = new CarRentalVendorPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String businessName = param.get("businessName");

        step("User open car rental dashboard");
        adminPage.openCarRentalPage(username, password);

        step("User open business profile sub menu");
        adminPage.openBusinessProfileSubMenu();

        step("User search business profile");
        Assertion.assertThat("The result for business profile is displayed according to input", adminPage.searchBusinessProfile(businessName), is(true));
        String businessProfileId = adminPage.getBusinessProfileId();

        step("User open manage regional sub menu");
        vendorPage.openManageRegionalSubMenu(businessProfileId);

        step("User deactivate regional status");
        Assertion.assertThat("Regional status is deactivated", vendorPage.deactivateRegionalStatus(), is(true));
        Assertion.assertThat("Success alert is displayed for successful deactivate status", adminPage.verifySuccessAlertIsDisplayed(), is(true));

        step("User activate regional status");
        Assertion.assertThat("Regional status is activated", vendorPage.activateRegionalStatus(), is(true));
        Assertion.assertThat("Success alert is displayed for successful activate status", adminPage.verifySuccessAlertIsDisplayed(), is(true));
    }

    @Author(names = "Okta")
    @Module(name = "CarAllotment")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testSetAllotmentWithDriver() {
        Map<String, String> param = getParam("testSetAllotmentWithDriver");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        CarRentalPage<?> adminPage = new CarRentalPage<>(session);
        CarRentalVendorPage<?> vendorPage = new CarRentalVendorPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String businessName = param.get("businessName");
        String price = param.get("price");
        String allotment = param.get("allotment");

        step("User open car rental dashboard");
        adminPage.openCarRentalPage(username, password);

        step("User open business profile sub menu");
        adminPage.openBusinessProfileSubMenu();

        step("User search business profile");
        Assertion.assertThat("The result for business profile is displayed according to input", adminPage.searchBusinessProfile(businessName), is(true));
        String businessProfileId = adminPage.getBusinessProfileId();

        step("User open car allotment sub menu");
        vendorPage.openCarAllotmentSubMenu("withDriver", businessProfileId);

        step("User search car allotment");
        vendorPage.searchCarAllotment();

        step("User set car allotment");
        vendorPage.setCarAllotmentWithDriver(price, allotment);
        Assertion.assertThat("Success alert is displayed for successful set allotment", adminPage.verifySuccessAlertIsDisplayed(), is(true));
        Assertion.assertThat("Price and allotment are displayed correctly", vendorPage.verifyPriceAndAllotmentAreDisplayed(price, allotment), is(true));
    }

    @Author(names = "Okta")
    @Module(name = "CarAllotment")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testSetAllotmentWithoutDriver() {
        Map<String, String> param = getParam("testSetAllotmentWithoutDriver");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        CarRentalPage<?> adminPage = new CarRentalPage<>(session);
        CarRentalVendorPage<?> vendorPage = new CarRentalVendorPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String businessName = param.get("businessName");
        String price = param.get("price");
        String allotment = param.get("allotment");

        step("User open car rental dashboard");
        adminPage.openCarRentalPage(username, password);

        step("User open business profile sub menu");
        adminPage.openBusinessProfileSubMenu();

        step("User search business profile");
        Assertion.assertThat("The result for business profile is displayed according to input", adminPage.searchBusinessProfile(businessName), is(true));
        String businessProfileId = adminPage.getBusinessProfileId();

        step("User open car allotment sub menu");
        vendorPage.openCarAllotmentSubMenu("withoutDriver", businessProfileId);

        step("User search car allotment");
        vendorPage.searchCarAllotment();

        step("User set car allotment");
        vendorPage.setCarAllotmentWithoutDriver(price, allotment);
        Assertion.assertThat("Success alert is displayed for successful set allotment", adminPage.verifySuccessAlertIsDisplayed(), is(true));
        Assertion.assertThat("Price and allotment are displayed correctly", vendorPage.verifyPriceAndAllotmentAreDisplayed(price, allotment), is(true));
    }

    @Author(names = "Okta")
    @Module(name = "ManageCar")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testAddCarWithDriver() {
        Map<String, String> param = getParam("testAddCarWithDriver");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        CarRentalPage<?> adminPage = new CarRentalPage<>(session);
        CarRentalVendorPage<?> vendorPage = new CarRentalVendorPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String businessName = param.get("businessName");
        String carBrand = param.get("carBrand");
        String carModel = param.get("carModel");

        step("User open car rental dashboard");
        adminPage.openCarRentalPage(username, password);

        step("User open business profile sub menu");
        adminPage.openBusinessProfileSubMenu();

        step("User search business profile");
        Assertion.assertThat("The result for business profile is displayed according to input", adminPage.searchBusinessProfile(businessName), is(true));
        String businessProfileId = adminPage.getBusinessProfileId();

        step("User open manage car sub menu");
        vendorPage.openManageCarSubMenu(businessProfileId);

        step("User add new car for vendor");
        vendorPage.addCar(carBrand, carModel);

        step("User open with driver tab");
        vendorPage.openWithDriverTab();

        step("User open manage car sub menu");
        vendorPage.openManageCarSubMenu(businessProfileId);

        step("User search newly added car");
        Assertion.assertThat("The result for car is displayed according to input", vendorPage.searchCarBrand(carBrand), is(true));
    }

    @Author(names = "Okta")
    @Module(name = "ManageCar")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testAddCarPriceWithDriver() {
        Map<String, String> param = getParam("testAddCarPriceWithDriver");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        CarRentalPage<?> adminPage = new CarRentalPage<>(session);
        CarRentalVendorPage<?> vendorPage = new CarRentalVendorPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        String businessName = param.get("businessName");
        String carBrand = param.get("carBrand");
        String carModel = param.get("carModel");
        String price = param.get("price");

        step("User open car rental dashboard");
        adminPage.openCarRentalPage(username, password);

        step("User open business profile sub menu");
        adminPage.openBusinessProfileSubMenu();

        step("User search business profile");
        Assertion.assertThat("The result for business profile is displayed according to input", adminPage.searchBusinessProfile(businessName), is(true));
        String businessProfileId = adminPage.getBusinessProfileId();

        step("User open manage car sub menu");
        vendorPage.openManageCarSubMenu(businessProfileId);

        step("User add new car for vendor");
        vendorPage.addCar(carBrand, carModel);

        step("User open with driver tab");
        vendorPage.openWithDriverTab();

        step("User add price for newly added car");
        vendorPage.addOrEditQuantity("with-driver", "price", price);
        Assertion.assertThat("Success alert is displayed for successful add price", adminPage.verifySuccessAlertIsDisplayed(), is(true));
    }

    @Author(names = "Okta")
    @Module(name = "ManageCar")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testAddCarAllotmentWithDriver() {
        Map<String, String> param = getParam("testAddCarAllotmentWithDriver");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        CarRentalPage<?> adminPage = new CarRentalPage<>(session);
        CarRentalVendorPage<?> vendorPage = new CarRentalVendorPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        String businessName = param.get("businessName");
        String carBrand = param.get("carBrand");
        String carModel = param.get("carModel");
        String allotment = param.get("allotment");

        step("User open car rental dashboard");
        adminPage.openCarRentalPage(username, password);

        step("User open business profile sub menu");
        adminPage.openBusinessProfileSubMenu();

        step("User search business profile");
        Assertion.assertThat("The result for business profile is displayed according to input", adminPage.searchBusinessProfile(businessName), is(true));
        String businessProfileId = adminPage.getBusinessProfileId();

        step("User open manage car sub menu");
        vendorPage.openManageCarSubMenu(businessProfileId);

        step("User add new car for vendor");
        vendorPage.addCar(carBrand, carModel);

        step("User open with driver tab");
        vendorPage.openWithDriverTab();

        step("User add allotment for newly added car");
        vendorPage.addOrEditQuantity("with-driver", "allotment", allotment);
        Assertion.assertThat("Success alert is displayed for successful add allotment", adminPage.verifySuccessAlertIsDisplayed(), is(true));
    }

    @Author(names = "Okta")
    @Module(name = "ManageCar")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testToggleCarWithDriver() {
        Map<String, String> param = getParam("testToggleCarWithDriver");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        CarRentalPage<?> adminPage = new CarRentalPage<>(session);
        CarRentalVendorPage<?> vendorPage = new CarRentalVendorPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String businessName = param.get("businessName");
        String carBrand = param.get("carBrand");
        String carModel = param.get("carModel");

        step("User open car rental dashboard");
        adminPage.openCarRentalPage(username, password);

        step("User open business profile sub menu");
        adminPage.openBusinessProfileSubMenu();

        step("User search business profile");
        Assertion.assertThat("The result for business profile is displayed according to input", adminPage.searchBusinessProfile(businessName), is(true));
        String businessProfileId = adminPage.getBusinessProfileId();

        step("User open manage car sub menu");
        vendorPage.openManageCarSubMenu(businessProfileId);

        step("User add new car for vendor");
        vendorPage.searchCarBrand(carBrand);

        step("User open without driver tab");
        vendorPage.openDriverTabMenu();

        step("User update car status");
        vendorPage.toggleCar("With Driver");
        Assertion.assertThat("Success alert is displayed for successful update car status", adminPage.verifySuccessAlertIsDisplayed(), is(true));
    }

    @Author(names = "Okta")
    @Module(name = "ManageCar")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testEditCarPriceWithDriver() {
        Map<String, String> param = getParam("testEditCarPriceWithDriver");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        CarRentalPage<?> adminPage = new CarRentalPage<>(session);
        CarRentalVendorPage<?> vendorPage = new CarRentalVendorPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String businessName = param.get("businessName");
        String carBrand = param.get("carBrand");
        String price = param.get("price");

        step("User open car rental dashboard");
        adminPage.openCarRentalPage(username, password);

        step("User open business profile sub menu");
        adminPage.openBusinessProfileSubMenu();

        step("User search business profile");
        Assertion.assertThat("The result for business profile is displayed according to input", adminPage.searchBusinessProfile(businessName), is(true));
        String businessProfileId = adminPage.getBusinessProfileId();

        step("User open manage car sub menu");
        vendorPage.openManageCarSubMenu(businessProfileId);

        step("User search car");
        Assertion.assertThat("The result for car is displayed according to input", vendorPage.searchCarBrand(carBrand), is(true));

        step("User click edit button");
        vendorPage.clickCarEditButton();

        step("User open with driver tab");
        vendorPage.openWithDriverTab();

        step("User edit price");
        vendorPage.addOrEditQuantity("with-driver", "price", price);
        Assertion.assertThat("Success alert is displayed for successful edit price", adminPage.verifySuccessAlertIsDisplayed(), is(true));
    }

    @Author(names = "Okta")
    @Module(name = "ManageCar")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testEditCarAllotmentWithDriver() {
        Map<String, String> param = getParam("testEditCarAllotmentWithDriver");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        CarRentalPage<?> adminPage = new CarRentalPage<>(session);
        CarRentalVendorPage<?> vendorPage = new CarRentalVendorPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String businessName = param.get("businessName");
        String carBrand = param.get("carBrand");
        String allotment = param.get("allotment");

        step("User open car rental dashboard");
        adminPage.openCarRentalPage(username, password);

        step("User open business profile sub menu");
        adminPage.openBusinessProfileSubMenu();

        step("User search business profile");
        Assertion.assertThat("The result for business profile is displayed according to input", adminPage.searchBusinessProfile(businessName), is(true));
        String businessProfileId = adminPage.getBusinessProfileId();

        step("User open manage car sub menu");
        vendorPage.openManageCarSubMenu(businessProfileId);

        step("User search car");
        Assertion.assertThat("The result for car is displayed according to input", vendorPage.searchCarBrand(carBrand), is(true));

        step("User click edit button");
        vendorPage.clickCarEditButton();

        step("User open with driver tab");
        vendorPage.openWithDriverTab();

        step("User edit allotment");
        vendorPage.addOrEditQuantity("with-driver", "allotment", allotment);
        Assertion.assertThat("Success alert is displayed for successful edit allotment", adminPage.verifySuccessAlertIsDisplayed(), is(true));
    }

    @Author(names = "Okta")
    @Module(name = "ManageCar")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testAddCarPriceWithoutDriver() {
        Map<String, String> param = getParam("testAddCarPriceWithoutDriver");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        CarRentalPage<?> adminPage = new CarRentalPage<>(session);
        CarRentalVendorPage<?> vendorPage = new CarRentalVendorPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        String businessName = param.get("businessName");
        String carBrand = param.get("carBrand");
        String carModel = param.get("carModel");
        String price = param.get("price");

        step("User open car rental dashboard");
        adminPage.openCarRentalPage(username, password);

        step("User open business profile sub menu");
        adminPage.openBusinessProfileSubMenu();

        step("User search business profile");
        Assertion.assertThat("The result for business profile is displayed according to input", adminPage.searchBusinessProfile(businessName), is(true));
        String businessProfileId = adminPage.getBusinessProfileId();

        step("User open manage car sub menu");
        vendorPage.openManageCarSubMenu(businessProfileId);

        step("User add new car for vendor");
        vendorPage.addCar(carBrand, carModel);

        step("User open without driver tab");
        vendorPage.openWithoutDriverTab();

        step("User add price for newly added car");
        vendorPage.addOrEditQuantity("without-driver", "price", price);
        Assertion.assertThat("Success alert is displayed for successful add price", adminPage.verifySuccessAlertIsDisplayed(), is(true));
    }

    @Author(names = "Okta")
    @Module(name = "ManageCar")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testAddCarAllotmentWithoutDriver() {
        Map<String, String> param = getParam("testAddCarAllotmentWithoutDriver");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        CarRentalPage<?> adminPage = new CarRentalPage<>(session);
        CarRentalVendorPage<?> vendorPage = new CarRentalVendorPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        String businessName = param.get("businessName");
        String carBrand = param.get("carBrand");
        String carModel = param.get("carModel");
        String allotment = param.get("allotment");

        step("User open car rental dashboard");
        adminPage.openCarRentalPage(username, password);

        step("User open business profile sub menu");
        adminPage.openBusinessProfileSubMenu();

        step("User search business profile");
        Assertion.assertThat("The result for business profile is displayed according to input", adminPage.searchBusinessProfile(businessName), is(true));
        String businessProfileId = adminPage.getBusinessProfileId();

        step("User open manage car sub menu");
        vendorPage.openManageCarSubMenu(businessProfileId);

        step("User add new car for vendor");
        vendorPage.addCar(carBrand, carModel);

        step("User open without driver tab");
        vendorPage.openWithoutDriverTab();

        step("User add allotment for newly added car");
        vendorPage.addOrEditQuantity("without-driver", "allotment", allotment);
        Assertion.assertThat("Success alert is displayed for successful add allotment", adminPage.verifySuccessAlertIsDisplayed(), is(true));
    }

    @Author(names = "Okta")
    @Module(name = "ManageCar")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testToggleCarWithoutDriver() {
        Map<String, String> param = getParam("testToggleCarWithoutDriver");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        CarRentalPage<?> adminPage = new CarRentalPage<>(session);
        CarRentalVendorPage<?> vendorPage = new CarRentalVendorPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String businessName = param.get("businessName");
        String carBrand = param.get("carBrand");
        String carModel = param.get("carModel");

        step("User open car rental dashboard");
        adminPage.openCarRentalPage(username, password);

        step("User open business profile sub menu");
        adminPage.openBusinessProfileSubMenu();

        step("User search business profile");
        Assertion.assertThat("The result for business profile is displayed according to input", adminPage.searchBusinessProfile(businessName), is(true));
        String businessProfileId = adminPage.getBusinessProfileId();

        step("User open manage car sub menu");
        vendorPage.openManageCarSubMenu(businessProfileId);

        step("User add new car for vendor");
        vendorPage.searchCarBrand(carBrand);

        step("User open without driver tab");
        vendorPage.openDriverTabMenu();

        step("User update car status");
        vendorPage.toggleCar("Without Driver");
        Assertion.assertThat("Success alert is displayed for successful update car status", adminPage.verifySuccessAlertIsDisplayed(), is(true));
    }

    @Author(names = "Okta")
    @Module(name = "ManageCar")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testEditCarPriceWithoutDriver() {
        Map<String, String> param = getParam("testEditCarPriceWithoutDriver");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        CarRentalPage<?> adminPage = new CarRentalPage<>(session);
        CarRentalVendorPage<?> vendorPage = new CarRentalVendorPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String businessName = param.get("businessName");
        String carBrand = param.get("carBrand");
        String price = param.get("price");

        step("User open car rental dashboard");
        adminPage.openCarRentalPage(username, password);

        step("User open business profile sub menu");
        adminPage.openBusinessProfileSubMenu();

        step("User search business profile");
        Assertion.assertThat("The result for business profile is displayed according to input", adminPage.searchBusinessProfile(businessName), is(true));
        String businessProfileId = adminPage.getBusinessProfileId();

        step("User open manage car sub menu");
        vendorPage.openManageCarSubMenu(businessProfileId);

        step("User search car");
        Assertion.assertThat("The result for car is displayed according to input", vendorPage.searchCarBrand(carBrand), is(true));

        step("User click edit button");
        vendorPage.clickCarEditButton();

        step("User open without driver tab");
        vendorPage.openWithoutDriverTab();

        step("User edit price");
        vendorPage.addOrEditQuantity("without-driver", "price", price);
        Assertion.assertThat("Success alert is displayed for successful edit price", adminPage.verifySuccessAlertIsDisplayed(), is(true));
    }

    @Author(names = "Okta")
    @Module(name = "ManageCar")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testEditCarAllotmentWithoutDriver() {
        Map<String, String> param = getParam("testEditCarAllotmentWithoutDriver");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        CarRentalPage<?> adminPage = new CarRentalPage<>(session);
        CarRentalVendorPage<?> vendorPage = new CarRentalVendorPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String businessName = param.get("businessName");
        String carBrand = param.get("carBrand");
        String allotment = param.get("allotment");

        step("User open car rental dashboard");
        adminPage.openCarRentalPage(username, password);

        step("User open business profile sub menu");
        adminPage.openBusinessProfileSubMenu();

        step("User search business profile");
        Assertion.assertThat("The result for business profile is displayed according to input", adminPage.searchBusinessProfile(businessName), is(true));
        String businessProfileId = adminPage.getBusinessProfileId();

        step("User open manage car sub menu");
        vendorPage.openManageCarSubMenu(businessProfileId);

        step("User search for car");
        Assertion.assertThat("The result for car is displayed according to input", vendorPage.searchCarBrand(carBrand), is(true));

        step("User click edit button");
        vendorPage.clickCarEditButton();

        step("User open without driver tab");
        vendorPage.openWithoutDriverTab();

        step("User edit allotment");
        vendorPage.addOrEditQuantity("without-driver", "allotment", allotment);
        Assertion.assertThat("Success alert is displayed for successful edit allotment", adminPage.verifySuccessAlertIsDisplayed(), is(true));
    }

    @Author(names = "Okta")
    @Module(name = "Orders")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testConfirmPickupOrderAll() {
        Map<String, String> param = getParam("testConfirmPickupOrderAll");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        CarRentalPage<?> adminPage = new CarRentalPage<>(session);
        CarRentalVendorPage<?> vendorPage = new CarRentalVendorPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String businessName = param.get("businessName");
        String orderId = param.get("orderId");

        step("User open car rental dashboard");
        adminPage.openCarRentalPage(username, password);

        step("User open business profile sub menu");
        adminPage.openBusinessProfileSubMenu();

        step("User search business profile");
        Assertion.assertThat("The result for business profile is displayed according to input", adminPage.searchBusinessProfile(businessName), is(true));
        String businessProfileId = adminPage.getBusinessProfileId();

        step("User open orders sub menu");
        vendorPage.openOrdersSubMenu(businessProfileId);

        step("User open all orders tab");
        vendorPage.openOrdersTab("Semua Pesanan");

        step("User search pickup order");
        vendorPage.searchOrder(orderId);

        step("User confirm pickup order");
        vendorPage.confirmPickupOrder();
        vendorPage.verifySuccessConfirmPopupIsDisplayed();

        step("User open completed orders tab");
        vendorPage.openOrdersTab("Pesanan Selesai");

        step("User search confirmed pickup order");
        Assertion.assertThat("Pickup order is moved to completed orders tab", vendorPage.searchOrder(orderId), is(true));
    }

    @Author(names = "Okta")
    @Module(name = "Orders")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testConfirmPickupOrderReady() {
        Map<String, String> param = getParam("testConfirmPickupOrderReady");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        CarRentalPage<?> adminPage = new CarRentalPage<>(session);
        CarRentalVendorPage<?> vendorPage = new CarRentalVendorPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String businessName = param.get("businessName");
        String orderId = param.get("orderId");

        step("User open car rental dashboard");
        adminPage.openCarRentalPage(username, password);

        step("User open business profile sub menu");
        adminPage.openBusinessProfileSubMenu();

        step("User search business profile");
        Assertion.assertThat("The result for business profile is displayed according to input", adminPage.searchBusinessProfile(businessName), is(true));
        String businessProfileId = adminPage.getBusinessProfileId();

        step("User open orders sub menu");
        vendorPage.openOrdersSubMenu(businessProfileId);

        step("User open ready pickup orders tab");
        vendorPage.openOrdersTab("Siap Berangkat");

        step("User search pickup order");
        vendorPage.searchOrder(orderId);

        step("User confirm pickup order");
        vendorPage.confirmPickupOrder();
        vendorPage.verifySuccessConfirmPopupIsDisplayed();

        step("User open completed orders tab");
        vendorPage.openOrdersTab("Pesanan Selesai");

        step("User search confirmed pickup order");
        Assertion.assertThat("Pickup order is moved to completed orders tab", vendorPage.searchOrder(orderId), is(true));
    }

    @Author(names = "Okta")
    @Module(name = "Orders")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testConfirmOrderAll() {
        Map<String, String> param = getParam("testConfirmOrderAll");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        CarRentalPage<?> adminPage = new CarRentalPage<>(session);
        CarRentalVendorPage<?> vendorPage = new CarRentalVendorPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String businessName = param.get("businessName");
        String orderId = param.get("orderId");

        step("User open car rental dashboard");
        adminPage.openCarRentalPage(username, password);

        step("User open business profile sub menu");
        adminPage.openBusinessProfileSubMenu();

        step("User search business profile");
        Assertion.assertThat("The result for business profile is displayed according to input", adminPage.searchBusinessProfile(businessName), is(true));
        String businessProfileId = adminPage.getBusinessProfileId();

        step("User open orders sub menu");
        vendorPage.openOrdersSubMenu(businessProfileId);

        step("User open all orders tab");
        vendorPage.openOrdersTab("Semua Pesanan");

        step("User search order");
        vendorPage.searchOrder(orderId);

        step("User confirm order");
        vendorPage.confirmOrder();
        vendorPage.verifySuccessConfirmPopupIsDisplayed();

        step("User open ready pickup orders tab");
        vendorPage.openOrdersTab("Siap Berangkat");

        step("User search confirmed order");
        Assertion.assertThat("Order is moved to ready pickup orders tab", vendorPage.searchOrder(orderId), is(true));
    }

    @Author(names = "Okta")
    @Module(name = "Orders")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testConfirmOrderIncoming() {
        Map<String, String> param = getParam("testConfirmOrderIncoming");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        CarRentalPage<?> adminPage = new CarRentalPage<>(session);
        CarRentalVendorPage<?> vendorPage = new CarRentalVendorPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String businessName = param.get("businessName");
        String orderId = param.get("orderId");

        step("User open car rental dashboard");
        adminPage.openCarRentalPage(username, password);

        step("User open business profile sub menu");
        adminPage.openBusinessProfileSubMenu();

        step("User search business profile");
        Assertion.assertThat("The result for business profile is displayed according to input", adminPage.searchBusinessProfile(businessName), is(true));
        String businessProfileId = adminPage.getBusinessProfileId();

        step("User open orders sub menu");
        vendorPage.openOrdersSubMenu(businessProfileId);

        step("User open incoming orders tab");
        vendorPage.openOrdersTab("Pesanan Baru");

        step("User search order");
        vendorPage.searchOrder(orderId);

        step("User confirm order");
        vendorPage.confirmOrder();
        vendorPage.verifySuccessConfirmPopupIsDisplayed();

        step("User open ready pickup orders tab");
        vendorPage.openOrdersTab("Siap Berangkat");

        step("User search confirmed order");
        Assertion.assertThat("Order is moved to ready pickup orders tab", vendorPage.searchOrder(orderId), is(true));
    }

    @Author(names = "Okta")
    @Module(name = "Account")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testEditAccount")
    public void testDeleteAccount() {
        Map<String, String> param = getParam("testDeleteAccount");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        CarRentalPage<?> adminPage = new CarRentalPage<>(session);
        CarRentalVendorPage<?> vendorPage = new CarRentalVendorPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String businessName = param.get("businessName");
        String name = param.get("name");

        step("User open car rental dashboard");
        adminPage.openCarRentalPage(username, password);

        step("User open business profile sub menu");
        adminPage.openBusinessProfileSubMenu();

        step("User search business profile");
        Assertion.assertThat("The result for business profile is displayed according to input", adminPage.searchBusinessProfile(businessName), is(true));
        String businessProfileId = adminPage.getBusinessProfileId();

        step("User open manage account sub menu");
        vendorPage.openManageAccountSubMenu(businessProfileId);

        step("User delete account");
        vendorPage.deleteAccount();
        Assertion.assertThat("Success alert is displayed for successful delete account", adminPage.verifySuccessAlertIsDisplayed(), is(true));
        Assertion.assertThat("Account has been deleted from the list", vendorPage.verifyResultIsDisplayed(name), is(false));
    }

    @Author(names = "Okta")
    @Module(name = "Orders")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testDownloadEvoucherAll() {
        Map<String, String> param = getParam("testDownloadEvoucherAll");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        CarRentalPage<?> adminPage = new CarRentalPage<>(session);
        CarRentalVendorPage<?> vendorPage = new CarRentalVendorPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String businessName = param.get("businessName");

        String orderId = param.get("orderId");
        String fileName = param.get("fileName");
        String timeOut = param.get("timeOut");

        step("User open car rental dashboard");
        adminPage.openCarRentalPage(username, password);

        step("User open business profile sub menu");
        adminPage.openBusinessProfileSubMenu();

        step("User search business profile");
        Assertion.assertThat("The result for business profile is displayed according to input", adminPage.searchBusinessProfile(businessName), is(true));
        String businessProfileId = adminPage.getBusinessProfileId();

        step("User open orders sub menu");
        vendorPage.openOrdersSubMenu(businessProfileId);

        step("User open all orders tab");
        vendorPage.openOrdersTab("Semua Pesanan");

        step("User search order");
        vendorPage.searchOrder(orderId);

        step("User download e-voucher");
        vendorPage.downloadEvoucher();
        Assertion.assertThat("E-voucher has been downloaded successfully", adminPage.verifyDownloadedExport(fileName, timeOut), is(true));
    }

    @Author(names = "Okta")
    @Module(name = "Orders")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testDownloadEvoucherIncoming() {
        Map<String, String> param = getParam("testDownloadEvoucherIncoming");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        CarRentalPage<?> adminPage = new CarRentalPage<>(session);
        CarRentalVendorPage<?> vendorPage = new CarRentalVendorPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String businessName = param.get("businessName");

        String orderId = param.get("orderId");
        String fileName = param.get("fileName");
        String timeOut = param.get("timeOut");

        step("User open car rental dashboard");
        adminPage.openCarRentalPage(username, password);

        step("User open business profile sub menu");
        adminPage.openBusinessProfileSubMenu();

        step("User search business profile");
        Assertion.assertThat("The result for business profile is displayed according to input", adminPage.searchBusinessProfile(businessName), is(true));
        String businessProfileId = adminPage.getBusinessProfileId();

        step("User open orders sub menu");
        vendorPage.openOrdersSubMenu(businessProfileId);

        step("User open incoming orders tab");
        vendorPage.openOrdersTab("Pesanan Baru");

        step("User search order");
        vendorPage.searchOrder(orderId);

        step("User download e-voucher");
        vendorPage.downloadEvoucher();
        Assertion.assertThat("E-voucher has been downloaded successfully", adminPage.verifyDownloadedExport(fileName, timeOut), is(true));
    }

    @Author(names = "Okta")
    @Module(name = "Orders")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testDownloadEvoucherReady() {
        Map<String, String> param = getParam("testDownloadEvoucherReady");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        CarRentalPage<?> adminPage = new CarRentalPage<>(session);
        CarRentalVendorPage<?> vendorPage = new CarRentalVendorPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String businessName = param.get("businessName");

        String orderId = param.get("orderId");
        String fileName = param.get("fileName");
        String timeOut = param.get("timeOut");

        step("User open car rental dashboard");
        adminPage.openCarRentalPage(username, password);

        step("User open business profile sub menu");
        adminPage.openBusinessProfileSubMenu();

        step("User search business profile");
        Assertion.assertThat("The result for business profile is displayed according to input", adminPage.searchBusinessProfile(businessName), is(true));
        String businessProfileId = adminPage.getBusinessProfileId();

        step("User open orders sub menu");
        vendorPage.openOrdersSubMenu(businessProfileId);

        step("User open ready orders tab");
        vendorPage.openOrdersTab("Siap Berangkat");

        step("User search order");
        vendorPage.searchOrder(orderId);

        step("User download e-voucher");
        vendorPage.downloadEvoucher();
        Assertion.assertThat("E-voucher has been downloaded successfully", adminPage.verifyDownloadedExport(fileName, timeOut), is(true));
    }
}