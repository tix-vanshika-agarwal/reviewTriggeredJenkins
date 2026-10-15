package com.tiket.web.dashboard.test.nft.car_rental;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.core.CoreFunctions;
import com.tiket.logging.ILogger;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.nft.car_rental.CarRentalPage;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;
import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.core.Is.is;

public class CarRentalTest extends BaseTest {
    @Author(names = "Okta")
    @Module(name = "CarBrand")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testAddCarBrand() {
        Map<String, String> param = getParam("testAddCarBrand");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        CarRentalPage<?> carPage = new CarRentalPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String brand = param.get("brand");

        step("User open car rental dashboard");
        carPage.openCarRentalPageViaUI(username, password);

        step("User open car brand sub menu");
        carPage.openCarBrandSubMenu();

        step("User add car brand");
        carPage.addCarBrand(brand);
        Assertion.assertThat("Success alert is displayed for successful add car brand", carPage.verifySuccessAlertIsDisplayed(), is(true));

        step("User search newly added car brand");
        Assertion.assertThat("The result is displayed according to car brand input", carPage.searchCarBrand(brand), is(true));
    }

    @Author(names = "Okta")
    @Module(name = "CarBrand")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testAddCarBrand")
    public void testEditCarBrand() {
        Map<String, String> param = getParam("testEditCarBrand");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        CarRentalPage<?> carPage = new CarRentalPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String brand = param.get("brand");
        String newBrand = param.get("newBrand");

        step("User open car rental dashboard");
        carPage.openCarRentalPage(username, password);

        step("User open car brand sub menu");
        carPage.openCarBrandSubMenu();

        step("User search car brand");
        carPage.searchCarBrand(brand);

        step("User edit car brand");
        carPage.editCarBrand(newBrand);
        Assertion.assertThat("Success alert is displayed for successful edit car brand", carPage.verifySuccessAlertIsDisplayed(), is(true));

        step("User search updated car brand");
        Assertion.assertThat("The result is displayed according to car brand input", carPage.searchCarBrand(newBrand), is(true));
    }

    @Author(names = "Okta")
    @Module(name = "CarBrand")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testToggleCarBrand() {
        Map<String, String> param = getParam("testToggleCarBrand");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        CarRentalPage<?> carPage = new CarRentalPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String brand = param.get("brand");

        step("User open car rental dashboard");
        carPage.openCarRentalPage(username, password);

        step("User open car brand sub menu");
        carPage.openCarBrandSubMenu();

        step("User search car brand");
        Assertion.assertThat("The result is displayed according to car brand input", carPage.searchCarBrand(brand), is(true));

        step("User deactivate car brand toggle");
        Assertion.assertThat("Car brand toggle is deactivated", carPage.deactivateToggle(), is(true));

        step("User activate car brand toggle");
        Assertion.assertThat("Car brand toggle is activated", carPage.activateToggle(), is(true));
    }

    @Author(names = "Okta")
    @Module(name = "CarModel")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testAddCarModel() {
        Map<String, String> param = getParam("testAddCarModel");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        CarRentalPage<?> carPage = new CarRentalPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        String model = param.get("model");
        String maxPassenger = param.get("maxPassenger");
        String maxBaggage = param.get("maxBaggage");
        String factory = param.get("factory");
        String priorityScore = param.get("priorityScore");
        String filePath = param.get("filePath");

        step("User open car rental dashboard");
        carPage.openCarRentalPage(username, password);

        step("User open car model sub menu");
        carPage.openCarModelSubMenu();

        step("User add car model");
        carPage.addCarModel(model, maxPassenger, maxBaggage, factory, priorityScore, filePath);
        Assertion.assertThat("Success alert is displayed for successful add car model", carPage.verifySuccessAlertIsDisplayed(), is(true));

        step("User search newly added car model");
        Assertion.assertThat("The result is displayed according to car model input", carPage.searchCarModel(model), is(true));
    }

    @Author(names = "Okta")
    @Module(name = "CarModel")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testAddCarModel")
    public void testEditCarModel() {
        Map<String, String> param = getParam("testEditCarModel");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        CarRentalPage<?> carPage = new CarRentalPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String model = param.get("model");
        String newModel = param.get("newModel");

        step("User open car rental dashboard");
        carPage.openCarRentalPage(username, password);

        step("User open car model sub menu");
        carPage.openCarModelSubMenu();

        step("User search car model");
        Assertion.assertThat("The result is displayed according to car model input", carPage.searchCarModel(model), is(true));

        step("User edit car model");
        carPage.editCarModel(newModel);
        Assertion.assertThat("Success alert is displayed for successful edit car model", carPage.verifySuccessAlertIsDisplayed(), is(true));

        step("User search updated car model");
        Assertion.assertThat("The result is displayed according to car model input", carPage.searchCarModel(newModel), is(true));
    }

    @Author(names = "Okta")
    @Module(name = "CarModel")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testToggleCarModel() {
        Map<String, String> param = getParam("testToggleCarModel");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        CarRentalPage<?> carPage = new CarRentalPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String model = param.get("model");

        step("User open car rental dashboard");
        carPage.openCarRentalPage(username, password);

        step("User open car model sub menu");
        carPage.openCarModelSubMenu();

        step("User search car model");
        Assertion.assertThat("The result is displayed according to car model input", carPage.searchCarModel(model), is(true));

        step("User deactivate car model toggle");
        Assertion.assertThat("Car model toggle is deactivated", carPage.deactivateToggle(), is(true));

        step("User activate car model toggle");
        Assertion.assertThat("Car brand toggle is activated", carPage.activateToggle(), is(true));
    }

    @Author(names = "Okta")
    @Module(name = "BusinessProfile")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testAddBusinessProfileCarRental() {
        Map<String, String> param = getParam("testAddBusinessProfileCarRental");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        CarRentalPage<?> carPage = new CarRentalPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String businessName = param.get("businessName");
        String phoneNumber = param.get("phoneNumber");
        String email = param.get("email");

        step("User open car rental dashboard");
        carPage.openCarRentalPage(username, password);

        step("User open business profile sub menu");
        carPage.openBusinessProfileSubMenu();

        step("User add business profile");
        carPage.addBusinessProfile("CarRental", businessName, phoneNumber, email);
        Assertion.assertThat("Snackbar is displayed for successful add business profile", carPage.verifySuccessSnackbar(), is(true));

        step("User search newly added business profile");
        Assertion.assertThat("The result for business profile is displayed according to input", carPage.searchBusinessProfile(businessName), is(true));
    }

    @Author(names = "Okta")
    @Module(name = "BusinessProfile")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testAddBusinessProfileAirportTransfer() {
        Map<String, String> param = getParam("testAddBusinessProfileAirportTransfer");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        CarRentalPage<?> carPage = new CarRentalPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String businessName = CoreFunctions.randomName();
        String phoneNumber = CoreFunctions.randomPhone();
        String email = CoreFunctions.randomEmail();

        step("User open car rental dashboard");
        carPage.openCarRentalPage(username, password);

        step("User open business profile sub menu");
        carPage.openBusinessProfileSubMenu();

        step("User add business profile");
        carPage.addBusinessProfile("AirportTransfer", businessName, phoneNumber, email);
        Assertion.assertThat("Snackbar is displayed for successful add business profile", carPage.verifySuccessSnackbar(), is(true));

        step("User search newly added business profile");
        Assertion.assertThat("The result for business profile is displayed according to input", carPage.searchBusinessProfile(businessName), is(true));
    }

    @Author(names = "Okta")
    @Module(name = "BusinessProfile")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testAddBusinessProfileCarRental")
    public void testEditBusinessProfile() {
        Map<String, String> param = getParam("testEditBusinessProfile");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        CarRentalPage<?> carPage = new CarRentalPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String businessName = param.get("businessName");
        String newBusinessName = param.get("newBusinessName");

        step("User open car rental dashboard");
        carPage.openCarRentalPage(username, password);

        step("User open business profile sub menu");
        carPage.openBusinessProfileSubMenu();

        step("User search business profile");
        carPage.searchBusinessProfile(businessName);

        step("User edit business profile");
        carPage.editBusinessProfile(newBusinessName);
        Assertion.assertThat("Snackbar is displayed for successful edit business profile", carPage.verifySuccessSnackbar(), is(true));

        step("User search updated business profile");
        Assertion.assertThat("The result for business profile is displayed according to input", carPage.searchBusinessProfile(newBusinessName), is(true));
    }

    @Author(names = "Okta")
    @Module(name = "Facility")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testAddFreeFacility() {
        Map<String, String> param = getParam("testAddFreeFacility");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        CarRentalPage<?> carPage = new CarRentalPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String facilityNameId = param.get("facilityNameId");
        String facilityNameEn = param.get("facilityNameEn");
        String filePath = param.get("filePath");

        step("User open car rental dashboard");
        carPage.openCarRentalPage(username, password);

        step("User open facility sub menu");
        carPage.openFacilitySubMenu("free");

        step("User click add facility button");
        carPage.clickAddFacilityButton();

        step("User add facility");
        carPage.addOrEditFacility(facilityNameId, facilityNameEn, filePath);
        Assertion.assertThat("Success alert is displayed for successful add facility", carPage.verifySuccessAlertIsDisplayed(), is(true));

        step("User search newly added facility");
        Assertion.assertThat("The result for facility is displayed according to input", carPage.searchFacility(facilityNameId), is(true));
    }

    @Author(names = "Okta")
    @Module(name = "Facility")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testAddPaidFacility() {
        Map<String, String> param = getParam("testAddPaidFacility");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        CarRentalPage<?> carPage = new CarRentalPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String facilityNameId = param.get("facilityNameId") + CoreFunctions.randomName();
        String facilityNameEn = param.get("facilityNameEn") + CoreFunctions.randomName();
        String filePath = param.get("filePath");

        step("User open car rental dashboard");
        carPage.openCarRentalPage(username, password);

        step("User open facility sub menu");
        carPage.openFacilitySubMenu("paid");

        step("User click add facility button");
        carPage.clickAddFacilityButton();

        step("User add facility");
        carPage.addOrEditFacility(facilityNameId, facilityNameEn, filePath);
        Assertion.assertThat("Success alert is displayed for successful add facility", carPage.verifySuccessAlertIsDisplayed(), is(true));

        step("User search newly added facility");
        Assertion.assertThat("The result for facility is displayed according to input", carPage.searchFacility(facilityNameId), is(true));
    }

    @Author(names = "Okta")
    @Module(name = "Facility")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testAddFreeFacility")
    public void testEditFreeFacility() {
        Map<String, String> param = getParam("testEditFreeFacility");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        CarRentalPage<?> carPage = new CarRentalPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        String facilityNameId = param.get("facilityNameId");
        String newFacilityNameId = param.get("newFacilityNameId");
        String newFacilityNameEn = param.get("newFacilityNameEn");
        String filePath = param.get("filePath");

        step("User open car rental dashboard");
        carPage.openCarRentalPage(username, password);

        step("User open facility sub menu");
        carPage.openFacilitySubMenu("free");

        step("User search facility");
        Assertion.assertThat("The result for facility is displayed according to input", carPage.searchFacility(facilityNameId), is(true));

        step("User click edit facility button");
        carPage.clickEditFacilityButton();

        step("User edit facility");
        carPage.addOrEditFacility(newFacilityNameId, newFacilityNameEn, filePath);
        Assertion.assertThat("Success alert is displayed for successful edit facility", carPage.verifySuccessAlertIsDisplayed(), is(true));

        step("User search updated facility");
        Assertion.assertThat("The result for facility is displayed according to input", carPage.searchFacility(newFacilityNameId), is(true));
    }

    @Author(names = "Okta")
    @Module(name = "Facility")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testAddPaidFacility")
    public void testEditPaidFacility() {
        Map<String, String> param = getParam("testEditPaidFacility");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        CarRentalPage<?> carPage = new CarRentalPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        String facilityNameId = param.get("facilityNameId");
        String newFacilityNameId = param.get("newFacilityNameId");
        String newFacilityNameEn = param.get("newFacilityNameEn");
        String filePath = param.get("filePath");

        step("User open car rental dashboard");
        carPage.openCarRentalPage(username, password);

        step("User open facility sub menu");
        carPage.openFacilitySubMenu("paid");

        step("User search facility");
        Assertion.assertThat("The result for facility is displayed according to input", carPage.searchFacility(facilityNameId), is(true));

        step("User click edit facility button");
        carPage.clickEditFacilityButton();

        step("User edit facility");
        carPage.addOrEditFacility(newFacilityNameId, newFacilityNameEn, filePath);
        Assertion.assertThat("Success alert is displayed for successful edit facility", carPage.verifySuccessAlertIsDisplayed(), is(true));

        step("User search updated facility");
        Assertion.assertThat("The result for facility is displayed according to input", carPage.searchFacility(newFacilityNameId), is(true));
    }

    @Author(names = "Okta")
    @Module(name = "Facility")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testToggleFreeFacility() {
        Map<String, String> param = getParam("testToggleFreeFacility");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        CarRentalPage<?> carPage = new CarRentalPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String facility = param.get("facility");

        step("User open car rental dashboard");
        carPage.openCarRentalPage(username, password);

        step("User open facility sub menu");
        carPage.openFacilitySubMenu("free");

        step("User search facility");
        Assertion.assertThat("The result for facility is displayed according to input", carPage.searchFacility(facility), is(true));

        step("User deactivate facility toggle");
        Assertion.assertThat("Facility toggle is deactivated", carPage.deactivateToggle(), is(true));

        step("User activate facility toggle");
        Assertion.assertThat("Facility toggle is activated", carPage.activateToggle(), is(true));
    }

    @Author(names = "Okta")
    @Module(name = "Facility")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testTogglePaidFacility() {
        Map<String, String> param = getParam("testTogglePaidFacility");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        CarRentalPage<?> carPage = new CarRentalPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String facility = param.get("facility");

        step("User open car rental dashboard");
        carPage.openCarRentalPage(username, password);

        step("User open facility sub menu");
        carPage.openFacilitySubMenu("paid");

        step("User search facility");
        Assertion.assertThat("The result for facility is displayed according to input", carPage.searchFacility(facility), is(true));

        step("User deactivate facility toggle");
        Assertion.assertThat("Facility toggle is deactivated", carPage.deactivateToggle(), is(true));

        step("User activate facility toggle");
        Assertion.assertThat("Facility toggle is activated", carPage.activateToggle(), is(true));
    }

    @Author(names = "Okta")
    @Module(name = "CampaignList")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testCreateCampaignListWithCardLabelGimmick() {
        Map<String, String> param = getParam("testCreateCampaignListWithCardLabelGimmick");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        CarRentalPage<?> carPage = new CarRentalPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        String campaignNameId = param.get("campaignNameId");
        String campaignNameEn = param.get("campaignNameEn");
        String regional = param.get("regional");
        String carType = param.get("carType");
        String vendorName = param.get("vendorName");
        String textColor = param.get("textColor");
        String labelColor = param.get("labelColor");

        step("User open car rental dashboard");
        carPage.openCarRentalPage(username, password);

        step("User open campaign list sub menu");
        carPage.openCampaignListSubMenu();

        step("User add campaign list");
        carPage.addCampaignList(campaignNameId, campaignNameEn, regional, carType, vendorName);

        step("User select card label gimmick for campaign type");
        carPage.addCardLabelGimmickCampaign(campaignNameId, campaignNameEn, textColor, labelColor);

        step("User submit campaign list");
        carPage.submitCampaignList();

        step("User check newly added campaign list");
        Assertion.assertThat("Verify campaign list is displayed", carPage.verifyResultIsDisplayed(campaignNameId), is(true));
    }

    @Author(names = "Okta")
    @Module(name = "CampaignList")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testCreateCampaignListWithStrikeoutPrice() {
        Map<String, String> param = getParam("testCreateCampaignListWithStrikeoutPrice");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        CarRentalPage<?> carPage = new CarRentalPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        String campaignNameId = param.get("campaignNameId");
        String campaignNameEn = param.get("campaignNameEn");
        String regional = param.get("regional");
        String carType = param.get("carType");
        String vendorName = param.get("vendorName");
        String markupPrice = param.get("markupPrice");
        String discountPrice = param.get("discountPrice");

        step("User open car rental dashboard");
        carPage.openCarRentalPage(username, password);

        step("User open campaign list sub menu");
        carPage.openCampaignListSubMenu();

        step("User add campaign list");
        carPage.addCampaignList(campaignNameId, campaignNameEn, regional, carType, vendorName);

        step("User select strikeout price for campaign type");
        carPage.addStrikeoutPriceCampaign(markupPrice, discountPrice);

        step("User submit campaign list");
        carPage.submitCampaignList();

        step("User check newly added campaign list");
        Assertion.assertThat("Verify campaign list is displayed", carPage.verifyResultIsDisplayed(campaignNameId), is(true));
    }

    @Author(names = "Okta")
    @Module(name = "CampaignList")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testCreateCampaignListWithSpecialSection() {
        Map<String, String> param = getParam("testCreateCampaignListWithSpecialSection");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        CarRentalPage<?> carPage = new CarRentalPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        String campaignNameId = param.get("campaignNameId");
        String campaignNameEn = param.get("campaignNameEn");
        String regional = param.get("regional");
        String carType = param.get("carType");
        String vendorName = param.get("vendorName");
        String bgColor = param.get("bgColor");
        String textColor = param.get("textColor");

        step("User open car rental dashboard");
        carPage.openCarRentalPage(username, password);

        step("User open campaign list sub menu");
        carPage.openCampaignListSubMenu();

        step("User add campaign list");
        carPage.addCampaignList(campaignNameId, campaignNameEn, regional, carType, vendorName);

        step("User select special section for campaign type");
        carPage.addSpecialSectionCampaign(campaignNameId, campaignNameEn, bgColor, textColor);

        step("User submit campaign list");
        carPage.submitCampaignList();

        step("User check newly added campaign list");
        Assertion.assertThat("Verify campaign list is displayed", carPage.verifyResultIsDisplayed(campaignNameId), is(true));
    }

    @Author(names = "Okta")
    @Module(name = "CampaignList")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testCreateCampaignListWithAllCombination() {
        Map<String, String> param = getParam("testCreateCampaignListWithAllCombination");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        CarRentalPage<?> carPage = new CarRentalPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        String campaignNameId = param.get("campaignNameId");
        String campaignNameEn = param.get("campaignNameEn");
        String regional = param.get("regional");
        String carType = param.get("carType");
        String vendorName = param.get("vendorName");

        String bgColor = param.get("bgColor");
        String textColor = param.get("textColor");
        String labelColor = param.get("labelColor");
        String markupPrice = param.get("markupPrice");
        String discountPrice = param.get("discountPrice");

        step("User open car rental dashboard");
        carPage.openCarRentalPage(username, password);

        step("User open campaign list sub menu");
        carPage.openCampaignListSubMenu();

        step("User add campaign list");
        carPage.addCampaignList(campaignNameId, campaignNameEn, regional, carType, vendorName);

        step("User select card label gimmick for campaign type");
        carPage.addCardLabelGimmickCampaign(campaignNameId, campaignNameEn, textColor, labelColor);

        step("User select strikeout price for campaign type");
        carPage.addStrikeoutPriceCampaign(markupPrice, discountPrice);

        step("User select special section for campaign list type");
        carPage.addSpecialSectionCampaign(campaignNameId, campaignNameEn, bgColor, textColor);

        step("User submit campaign list");
        carPage.submitCampaignList();

        step("User check newly added campaign list");
        Assertion.assertThat("Verify campaign list is displayed", carPage.verifyResultIsDisplayed(campaignNameId), is(true));
    }

    @Author(names = "Okta")
    @Module(name = "CampaignList")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testActivateCampaignListStatus() {
        Map<String, String> param = getParam("testActivateCampaignListStatus");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        CarRentalPage<?> carPage = new CarRentalPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String campaignList = param.get("campaignList");

        step("User open car rental dashboard");
        carPage.openCarRentalPage(username, password);

        step("User open campaign list sub menu");
        carPage.openCampaignListSubMenu();

        step("User search campaign list");
        Assertion.assertThat("Verify campaign list is displayed", carPage.searchCampaignList(campaignList), is(true));

        step("User activate campaign list status");
        Assertion.assertThat("Verify campaign list status is activated", carPage.activateToggleCampaignList(), is(true));
        Assertion.assertThat("Verify success snackbar is displayed", carPage.verifyUploadSuccessSnackbar(), is(true));
    }

    @Author(names = "Okta")
    @Module(name = "PaymentList")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testExportPaymentList() {
        Map<String, String> param = getParam("testExportPaymentList");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        CarRentalPage<?> carPage = new CarRentalPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String vendorName = param.get("vendorName");
        String fileName = param.get("fileName");
        String timeOut = param.get("timeOut");

        step("User open car rental dashboard");
        carPage.openCarRentalPage(username, password);

        step("User open payment list sub menu");
        carPage.openPaymentListSubMenu();

        step("User export payment list");
        carPage.exportPaymentList(vendorName);
        Assertion.assertThat("Verify file has been downloaded", carPage.verifyDownloadedExport(fileName, timeOut), is(true));
    }

    @Author(names = "Okta")
    @Module(name = "CarModel")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testEditCarModel")
    public void testDeleteCarModel() {
        Map<String, String> param = getParam("testDeleteCarModel");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        CarRentalPage<?> carPage = new CarRentalPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String model = param.get("model");

        step("User open car rental dashboard");
        carPage.openCarRentalPage(username, password);

        step("User open car model sub menu");
        carPage.openCarModelSubMenu();

        step("User search car model");
        Assertion.assertThat("The result is displayed according to car model input", carPage.searchCarModel(model), is(true));

        step("User delete car model");
        carPage.deleteCarBrandModelOrFacility();

        step("User open car model sub menu");
        carPage.openCarModelSubMenu();

        step("User search deleted car model");
        Assertion.assertThat("Car model has been deleted from the list", carPage.searchCarModel(model), is(false));
    }

    @Author(names = "Okta")
    @Module(name = "BusinessProfile")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testEditBusinessProfile")
    public void testDeleteBusinessProfile() {
        Map<String, String> param = getParam("testDeleteBusinessProfile");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        CarRentalPage<?> carPage = new CarRentalPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String businessName = param.get("businessName");

        step("User open car rental dashboard");
        carPage.openCarRentalPage(username, password);

        step("User open business profile sub menu");
        carPage.openBusinessProfileSubMenu();

        step("User search business profile");
        carPage.searchBusinessProfile(businessName);

        step("User delete business profile");
        carPage.deleteBusinessProfile();
        Assertion.assertThat("Snackbar is displayed for successful delete business profile", carPage.verifySuccessSnackbar(), is(true));

        step("User open business profile sub menu");
        carPage.openBusinessProfileSubMenu();

        step("User search deleted business profile");
        Assertion.assertThat("Business profile has been deleted from the list", carPage.searchBusinessProfile(businessName), is(false));
    }

    @Author(names = "Okta")
    @Module(name = "Facility")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testEditFreeFacility")
    public void testDeleteFreeFacility() {
        Map<String, String> param = getParam("testDeleteFreeFacility");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        CarRentalPage<?> carPage = new CarRentalPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String facilityName = param.get("facilityName");

        step("User open car rental dashboard");
        carPage.openCarRentalPage(username, password);

        step("User open facility sub menu");
        carPage.openFacilitySubMenu("free");

        step("User search facility");
        Assertion.assertThat("The result for facility is displayed according to input", carPage.searchFacility(facilityName), is(true));

        step("User delete facility");
        carPage.deleteCarBrandModelOrFacility();

        step("User search deleted facility");
        Assertion.assertThat("Facility has been deleted from the list", carPage.searchFacility(facilityName), is(false));
    }

    @Author(names = "Okta")
    @Module(name = "Facility")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testEditPaidFacility")
    public void testDeletePaidFacility() {
        Map<String, String> param = getParam("testDeletePaidFacility");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        CarRentalPage<?> carPage = new CarRentalPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String facilityName = param.get("facilityName");

        step("User open car rental dashboard");
        carPage.openCarRentalPage(username, password);

        step("User open facility sub menu");
        carPage.openFacilitySubMenu("paid");

        step("User search facility");
        Assertion.assertThat("The result for facility is displayed according to input", carPage.searchFacility(facilityName), is(true));

        step("User delete facility");
        carPage.deleteCarBrandModelOrFacility();

        step("User search deleted facility");
        Assertion.assertThat("Facility has been deleted from the list", carPage.searchFacility(facilityName), is(false));
    }
    @Author(names = "Sylvia")
    @Module(name = "CarBrand")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testDeleteCarBrand() {
        Map<String, String> param = getParam("testDeleteCarBrand");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        CarRentalPage<?> carPage = new CarRentalPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String brand = param.get("brand");

        step("User open car rental dashboard");
        carPage.openCarRentalPage(username, password);

        step("User open car brand sub menu");
        carPage.openCarBrandSubMenu();

        step("User add car brand");
        carPage.addCarBrand(brand);

        step("User search newly added car brand");
        carPage.searchCarBrand(brand);

        step("User delete car brand");
        carPage.deleteCarBrand();
        Assertion.assertThat("The result is displayed data not exist", carPage.searchCarBrand(brand), is(false));

    }

    @Author(names = "Adit")
    @Module(name = "PaymentList")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test()
    public void testUploadDataPaymentListUsingFiltering() throws FileNotFoundException, InterruptedException {
        Map<String, String> param = getParam("testUploadDataPaymentListUsingFiltering");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        CarRentalPage<?> carPage = new CarRentalPage<>(session);
        ILogger logger = BaseTest.mainLogger.get();

        String username = param.get("username");
        String password = param.get("password");
        String orderId =  param.get("orderId");
        String fileId =  param.get("fileId");
        String downloadDirectory = param.get("downloadDirectory");
        String orderStatus = param.get("orderStatus");
        String paymentStatus = param.get("paymentStatus");

        step("User open car rental dashboard");
        carPage.openCarRentalPage(username, password);

        step("User open payment list sub menu");
        carPage.openPaymentListSubMenuViaSidebar();

        step("User search orderId");
        carPage.searchOrderId(orderId, orderStatus, paymentStatus);

        step("Download payment data from drive");
        String filepath = carPage.downloadPaymentListFromDrive(fileId, downloadDirectory);
        logger.log("Filepath: " + filepath);

        step("Upload data");
        carPage.uploadPaymentList(filepath);
        Assertion.assertThat("Verify the success snackbar is shown", carPage.verifyUploadSuccessSnackbar(), is(true));

        step("User open payment list sub menu");
        carPage.openPaymentListSubMenuViaSidebar();

        step("User search orderId");
        carPage.searchOrderId(orderId, orderStatus, paymentStatus);
        Assertion.assertThat("Verify table is empty", carPage.isTableEmpty(), is(true));
        Assertion.assertThat("Verify orderId is not shown", carPage.isOrderIdFound(orderId), is(false));

        carPage.searchAndDeleteFile(downloadDirectory);
    }

    @Author(names = "Pankaj Singh Kushwaha")
    @Module(name = "Finance")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testUploadXSLButtonNotVisibleOnTheCRFinancePage() throws InterruptedException {
        Map<String, String> param = getParam("testUploadXSLButtonNotVisibleOnTheCRFinancePage");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        CarRentalPage<?> carPage = new CarRentalPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("Login to NFT Admin Dashboard and open the Car Rental homepage card");
        carPage.openCarRentalPageViaHomepageCard(username, password);

        step("Click on the Finance menu from the sidebar and open Payment List");
        carPage.openPaymentListSubMenuViaSidebar();

        step("Verify that there is NO 'Upload XSL' button present on the Payment List page");
        carPage.verifyUploadXlsButtonNotPresent();
    }
}