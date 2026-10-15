package com.tiket.web.dashboard.test.nft.airport_transfer;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.nft.airport_transfer.*;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.is;

public class AirportTransferTest extends BaseTest {
    @Author(names = { "Okta" , "Clement" })
    @Module(name = "Facility")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test
    public void testSearchFacilityByNameID() {
        Map<String, String> param = getParam("testSearchFacilityByNameID");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        AirportTransferPage<?> atPage = new AirportTransferPage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String facility = param.get("facility");

        step("User open airport transfer admin dashboard");
        atPage.goToATPageViaUI(username, password);

        step("User open facility tab");
        atPage.goToFacilityTab();

        step("User search facility by name ID");
        String facilityNameValue = atPage.searchFacilityByLanguage(facility, "ID");
        Assertion.assertThat("Verify facility name is correct", facilityNameValue.contains(facility), is(true));
    }

    @Author(names = { "Okta" , "Clement" })
    @Module(name = "Facility")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test
    public void testSearchFacilityByNameEN() {
        Map<String, String> param = getParam("testSearchFacilityByNameEN");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        AirportTransferPage<?> atPage = new AirportTransferPage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String facility = param.get("facility");

        step("User open airport transfer admin dashboard");
        atPage.goToATPage(username, password);

        step("User open facility tab");
        atPage.goToFacilityTab();

        step("User search facility by name EN");
        String facilityNameValue = atPage.searchFacilityByLanguage(facility, "EN");
        Assertion.assertThat("Verify facility name is correct", facilityNameValue.contains(facility), is(true));
    }

    @Author(names = { "Adit" , "Clement" })
    @Module(name = "Facility")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test
    public void testAddNewFacility() throws InterruptedException {
        Map<String, String> param = getParam("testAddNewFacility");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        AirportTransferFacilityPage<?> atPage = new AirportTransferFacilityPage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String label = param.get("label") + System.currentTimeMillis();
        String idName = param.get("idName");
        String enName = param.get("enName");
        String imageLink = param.get("imageLink");
        String priorityScore = param.get("priorityScore");
        String successMessage = param.get("successMessage");

        step("Login to Airport Transfer");
        atPage.goToATPage(username, password);

        step("Open Facilities Tab");
        atPage.goToFacilityTab();

        step("Add New Facility");
        atPage.addNewFacility(label, idName, enName, imageLink, priorityScore);

        Assertion.assertThat("Verify the success snackbar is displayed with correct description", atPage.verifySuccessSnackbarDisplayed(String.format(successMessage, label)), is(true));
    }

    @Author(names = { "Adit" , "Clement" })
    @Module(name = "Facility")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testAddNewFacility")
    public void testSearchForEditFacility() throws InterruptedException {
        Map<String, String> param = getParam("testSearchForEditFacility");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        AirportTransferFacilityPage<?> atPage = new AirportTransferFacilityPage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String label = param.get("label");
        String idName = param.get("idName");
        String enName = param.get("enName");
        String successMessage = param.get("successMessage");
        String imageLink = param.get("imageLink");
        String priorityScore = param.get("priorityScore");

        step("Login to Airport Transfer");
        atPage.goToATPage(username, password);

        step("Open Facilities Tab");
        atPage.goToFacilityTab();

        step("Search The Facility");
        atPage.searchFacilityByName(label);

        step("Edit Facility");
        atPage.editFacility(idName, enName, imageLink, priorityScore);

        Assertion.assertThat("Verify the success snackbar is displayed with correct description", atPage.verifySuccessSnackbarDisplayed(label), is(true));
    }

    @Author(names = { "Adit" , "Clement" })
    @Module(name = "Facility")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testSearchForEditFacility")
    public void testDeleteFacility() throws InterruptedException {
        Map<String, String> param = getParam("testDeleteFacility");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        AirportTransferFacilityPage<?> atPage = new AirportTransferFacilityPage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String idName = param.get("idName");
        String label = param.get("label");
        String emptyMessage = param.get("emptyMessage");
        String successMessage = param.get("successMessage");

        step("Login to Airport Transfer");
        atPage.goToATPage(username, password);

        step("Open Facilities Tab");
        atPage.goToFacilityTab();

        step("Search The Facility");
        atPage.searchFacilityByName(idName);

        step("Delete Facility");
        atPage.deleteFacility();

        Assertion.assertThat("Verify the success snackbar is displayed with correct description", atPage.verifySuccessSnackbarDisplayed(String.format(successMessage, label)), is(true));
        Assertion.assertThat("Verify the facility does not exist", atPage.verifySRPIsEmpty(emptyMessage), is(true));
    }

    @Author(names = { "Adit" , "Clement" })
    @Module(name = "CarType")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test
    public void testAddNewCarType() throws InterruptedException {
        Map<String, String> param = getParam("testAddNewCarType");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        AirportTransferCarTypePage<?> atPage = new AirportTransferCarTypePage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String label = param.get("label") +  System.currentTimeMillis();
        String idName = param.get("idName");
        String enName = param.get("enName");
        String maxPax = param.get("maxPax");
        String maxBaggage = param.get("maxBaggage");
        String uspID = param.get("uspID");
        String uspEN = param.get("uspEN");
        String carModelId = param.get("carModelId");
        String carModelEn = param.get("carModelEn");
        String filePath = param.get("filePath");
        String successMessage = param.get("successMessage");

        step("Login to Airport Transfer");
        atPage.goToATPage(username, password);

        step("Open Car Type Tab");
        atPage.goToCarTypeTab();

        step("Add New Car Type");
        atPage.createNewCarType(label, idName, enName, maxPax, maxBaggage, uspID, uspEN, carModelId, carModelEn, filePath);
        Assertion.assertThat("Verify the success snackbar is displayed with correct description", atPage.verifySuccessSnackbarDisplayed(String.format(successMessage, label)), is(true));

        step("Verify The New Car Type Exists");
        atPage.searchCarTypeByName(idName);
        Assertion.assertThat("Verify the new car type exists on srp", atPage.verifySRPContainsCorrectName(idName), is(true));
    }

    @Author(names = { "Adit" , "Clement"})
    @Module(name = "CarType")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testAddNewCarType")
    public void testSearchCarTypeByNameID() throws InterruptedException {
        Map<String, String> param = getParam("testSearchCarTypeByNameID");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        AirportTransferCarTypePage<?> atPage = new AirportTransferCarTypePage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String idName = param.get("idName");

        step("Login to Airport Transfer");
        atPage.goToATPage(username, password);

        step("Open Car Type Tab");
        atPage.goToCarTypeTab();

        step("Search The Car Type");
        atPage.searchCarTypeByName(idName);
        Assertion.assertThat("Verify the car type exists on srp", atPage.verifySRPContainsCorrectName(idName), is(true));
    }

    @Author(names = {"Adit"})
    @Module(name = "CarType")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testAddNewCarType")
    public void testSearchCarTypeByNameEN() throws InterruptedException {
        Map<String, String> param = getParam("testSearchCarTypeByNameEN");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        AirportTransferCarTypePage<?> atPage = new AirportTransferCarTypePage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String enName = param.get("enName");

        step("Login to Airport Transfer");
        atPage.goToATPage(username, password);

        step("Open Car Type Tab");
        atPage.goToCarTypeTab();

        step("Search The Car Type");
        atPage.searchCarTypeByName(enName);
        Assertion.assertThat("Verify the car type exists on srp", atPage.verifySRPContainsCorrectName(enName), is(true));
    }

    @Author(names = { "Adit" , "Clement"})
    @Module(name = "CarType")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testAddNewCarType")
    public void testSearchForEditCarType() throws InterruptedException {
        Map<String, String> param = getParam("testSearchForEditCarType");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        AirportTransferCarTypePage<?> atPage = new AirportTransferCarTypePage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String label = param.get("label");
        String idName = param.get("idName");
        String enName = param.get("enName");
        String maxPax = param.get("maxPax");
        String maxBaggage = param.get("maxBaggage");
        String uspID = param.get("uspID");
        String uspEN = param.get("uspEN");
        String carModelId = param.get("carModelId");
        String carModelEn = param.get("carModelEn");
        String filePath = param.get("filePath");
        String expectedName = param.get("expectedName");
        String successMessage = param.get("successMessage");

        step("Login to Airport Transfer");
        atPage.goToATPage(username, password);

        step("Open Car Type Tab");
        atPage.goToCarTypeTab();

        step("Edit Car Type");
        atPage.editCarType(idName, enName, maxPax, maxBaggage, uspID, uspEN, carModelId, carModelEn, filePath);
        Assertion.assertThat("Verify the success snackbar is displayed with correct description", atPage.verifySuccessSnackbarDisplayed(label), is(true));

        step("Verify The Edited Car Type Exists");
        atPage.searchCarTypeByName(expectedName);
        Assertion.assertThat("Verify the car type exists on srp", atPage.verifySRPContainsCorrectName(expectedName), is(true));
    }

    @Author(names = { "Adit" })
    @Module(name = "CarType")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testToggleActivateCarType")
    public void testDeleteCarType() throws InterruptedException {
        Map<String, String> param = getParam("testDeleteCarType");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        AirportTransferCarTypePage<?> atPage = new AirportTransferCarTypePage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String label = param.get("label");
        String successMessage = param.get("successMessage");
        String emptyMessage = param.get("emptyMessage");

        step("Login to Airport Transfer");
        atPage.goToATPage(username, password);

        step("Open Car Type Tab");
        atPage.goToCarTypeTab();

        step("Search The Car Type");
        atPage.searchCarTypeByName(label);

        step("Delete The Car Type");
        atPage.deleteCarType();
        Assertion.assertThat("Verify the success snackbar is displayed with correct description", atPage.verifySuccessSnackbarDisplayed(String.format(successMessage, label)), is(true));
        Assertion.assertThat("Verify the car type does not exist", atPage.verifySRPIsEmpty(emptyMessage), is(true));
    }

    @Author(names = { "Adit" , "Clement"})
    @Module(name = "CarType")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testSearchForEditCarType")
    public void testToggleActivateCarType() throws InterruptedException {
        Map<String, String> param = getParam("testToggleActivateCarType");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        AirportTransferCarTypePage<?> atPage = new AirportTransferCarTypePage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String label = param.get("label");
        String status = param.get("status");
        String activateMessage = param.get("activateMessage");
        String deactivateMessage = param.get("deactivateMessage");

        step("Login to Airport Transfer");
        atPage.goToATPage(username, password);

        step("Open Car Type Tab");
        atPage.goToCarTypeTab();

        step("Search The Car Type");
        atPage.searchCarTypeByName(label);

        step("Scroll Table");
        atPage.scrollTable();

        step("Deactivate Car Type");
        atPage.toggleActiveCarType();
        Assertion.assertThat("Verify the success snackbar is displayed with correct description", atPage.verifySuccessSnackbarDisplayed(deactivateMessage), is(true));
        Assertion.assertThat("Verify the status is Non-Aktif", atPage.verifyStatusIsChanged(status), is(true));

        step("Activate Car Type");
        atPage.toggleActiveCarType();
        Assertion.assertThat("Verify the success snackbar is displayed with correct description", atPage.verifySuccessSnackbarDisplayed(activateMessage), is(true));
        Assertion.assertThat("Verify the status is Aktif", atPage.verifyStatusIsChanged(status), is(false));
    }

    @Author(names = { "Adit" , "Clement"})
    @Module(name = "CampaignLabel")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test
    public void testAddNewCampaignLabel() throws InterruptedException {
        Map<String, String> param = getParam("testAddNewCampaignLabel");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        AirportTransferCampaignLabelPage<?> atPage = new AirportTransferCampaignLabelPage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String title = param.get("title");
        String labelId = param.get("labelId");
        String labelEn = param.get("labelEn");
        String filePath = param.get("filePath");
        String successMessage = param.get("successMessage");

        step("Login to Airport Transfer");
        atPage.goToATPage(username, password);

        step("Open Campaign Label Tab");
        atPage.goToCampaignLabelTab();

        step("Add New Campaign Label");
        atPage.createNewCampaignLabel(title, labelId, labelEn, filePath);
        Assertion.assertThat("Verify the success snackbar is displayed with correct description", atPage.verifySuccessSnackbarDisplayed(successMessage), is(true));

        step("Verify The New Campaign Label Exists");
        atPage.searchCampaignLabelByName(labelId);
        Assertion.assertThat("Verify the new car type exists on srp", atPage.verifySRPContainsCorrectName(labelEn), is(true));
    }

    @Author(names = { "Adit" , "Clement" })
    @Module(name = "CampaignLabel")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testAddNewCampaignLabel")
    public void testSearchCampaignLabelByNameID() throws InterruptedException {
        Map<String, String> param = getParam("testSearchCampaignLabelByNameID");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        AirportTransferCampaignLabelPage<?> atPage = new AirportTransferCampaignLabelPage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String labelId = param.get("labelId");

        step("Login to Airport Transfer");
        atPage.goToATPage(username, password);

        step("Open Campaign Label Tab");
        atPage.goToCampaignLabelTab();

        step("Search The Campaign Label");
        atPage.searchCampaignLabelByName(labelId);

        Assertion.assertThat("Verify the campaign label exists on srp", atPage.verifySRPContainsCorrectName(labelId), is(true));
    }

    @Author(names = { "Adit" , "Clement" })
    @Module(name = "CampaignLabel")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testAddNewCampaignLabel")
    public void testSearchCampaignLabelByNameEN() throws InterruptedException {
        Map<String, String> param = getParam("testSearchCampaignLabelByNameEN");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        AirportTransferCampaignLabelPage<?> atPage = new AirportTransferCampaignLabelPage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String labelEn = param.get("labelEn");

        step("Login to Airport Transfer");
        atPage.goToATPage(username, password);

        step("Open Campaign Label Tab");
        atPage.goToCampaignLabelTab();

        step("Search The Campaign Label");
        atPage.searchCampaignLabelByName(labelEn);
        Assertion.assertThat("Verify the campaign label exists on srp", atPage.verifySRPContainsCorrectName(labelEn), is(true));
    }

    @Author(names = { "Adit", "Clement" })
    @Module(name = "CampaignLabel")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testSearchCampaignLabelByNameEN")
    public void testSearchForEditCampaignLabel() throws InterruptedException {
        Map<String, String> param = getParam("testSearchForEditCampaignLabel");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        AirportTransferCampaignLabelPage<?> atPage = new AirportTransferCampaignLabelPage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String title = param.get("title");
        String labelId = param.get("labelId");
        String labelEn = param.get("labelEn");
        String filePath = param.get("filePath");
        String initialLabel = param.get("initialLabel");
        String successMessage = param.get("successMessage");

        step("Login to Airport Transfer");
        atPage.goToATPage(username, password);

        step("Open Campaign Label Tab");
        atPage.goToCampaignLabelTab();

        step("Search The Campaign Label");
        atPage.searchCampaignLabelByName(initialLabel);

        step("Edit The Campaign Label");
        atPage.editCampaignLabel(labelId, labelEn, filePath);
        Assertion.assertThat("Verify the success snackbar is displayed with correct description", atPage.verifySuccessSnackbarDisplayed(String.format(successMessage, title)), is(true));
    }

    @Author(names = {"Adit"})
    @Module(name = "CampaignLabel")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testToggleActivateCampaignLabel")
    public void testDeleteCampaignLabel() throws InterruptedException {
        Map<String, String> param = getParam("testDeleteCampaignLabel");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        AirportTransferCampaignLabelPage<?> atPage = new AirportTransferCampaignLabelPage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String title = param.get("title");
        String labelEn = param.get("labelEn");
        String emptyMessage = param.get("emptyMessage");
        String successMessage = param.get("successMessage");

        step("Login to Airport Transfer");
        atPage.goToATPage(username, password);

        step("Open Campaign Label Tab");
        atPage.goToCampaignLabelTab();

        step("Search The Campaign Label");
        atPage.searchCampaignLabelByName(labelEn);

        step("Delete The Campaign Label");
        atPage.deleteCampaignLabel();
        Assertion.assertThat("Verify the success snackbar is displayed with correct description", atPage.verifySuccessSnackbarDisplayed(String.format(successMessage, title)), is(true));
        Assertion.assertThat("Verify the campaign label does not exist", atPage.verifySRPIsEmpty(emptyMessage), is(true));
    }

    @Author(names = {"Adit"})
    @Module(name = "CampaignLabel")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testSearchForEditCampaignLabel")
    public void testToggleActivateCampaignLabel() throws InterruptedException {
        Map<String, String> param = getParam("testToggleActivateCampaignLabel");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        AirportTransferCampaignLabelPage<?> atPage = new AirportTransferCampaignLabelPage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String title = param.get("title");
        String status = param.get("status");
        String activateMessage = param.get("activateMessage");
        String deactivateMessage = param.get("deactivateMessage");

        step("Login to Airport Transfer");
        atPage.goToATPage(username, password);

        step("Open Campaign Label Tab");
        atPage.goToCampaignLabelTab();

        step("Search The Campaign Label");
        atPage.searchCampaignLabelByName(title);

        step("Scroll Table");
        atPage.scrollTable();

        step("Deactivate Campaign Label");
        atPage.toggleActiveCampaignLabel();
        Assertion.assertThat("Verify the success snackbar is displayed with correct description", atPage.verifySuccessSnackbarDisplayed(deactivateMessage), is(true));
        Assertion.assertThat("Verify the status is Non-Aktif", atPage.verifyStatusIsChanged(status), is(true));

        step("Activate Campaign Label");
        atPage.toggleActiveCampaignLabel();
        Assertion.assertThat("Verify the success snackbar is displayed with correct description", atPage.verifySuccessSnackbarDisplayed(activateMessage), is(true));
        Assertion.assertThat("Verify the status is Aktif", atPage.verifyStatusIsChanged(status), is(false));
    }

    @Author(names = {"Adit"})
    @Module(name = "Fleet")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test
    public void testAddFleetWithEmptyFields() throws InterruptedException {
        Map<String, String> param = getParam("testAddFleetWithEmptyFields");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        AirportTransferFleetPage<?> atPage = new AirportTransferFleetPage<>(session);
        String username = param.get("username");
        String password = param.get("password");

        step("Login to Airport Transfer");
        atPage.goToATPage(username, password);

        step("Open Fleet Tab");
        atPage.goToFleetTab();

        step("Add Fleet with Empty Fields");
        atPage.addFleet();
        Assertion.assertThat("Verify the success snackbar is not displayed", atPage.verifySuccessSnackbarIsDisplayed(), is(false));
    }

    @Author(names = { "Adit" , "Clement"})
    @Module(name = "Fleet")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testAddFleetWithEmptyFields")
    public void testAddFleet() throws InterruptedException {
        Map<String, String> param = getParam("testAddFleet");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        AirportTransferFleetPage<?> atPage = new AirportTransferFleetPage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String business = param.get("business");
        String model = param.get("model");
        String successMessage = param.get("successMessage");

        step("Login to Airport Transfer");
        atPage.goToATPage(username, password);

        step("Open Fleet Tab");
        atPage.goToFleetTab();

        step("Add Fleet");
        atPage.addFleet(business, model);
        Assertion.assertThat("Verify the success snackbar is displayed with correct description", atPage.verifySuccessSnackbarContainsText(successMessage), is(true));
        Assertion.assertThat("Verify the new fleet is shown on result table", atPage.verifySRPContainsFleet(business), is(true));
    }

    @Author(names = { "Adit" , "Clement" })
    @Module(name = "Fleet")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testAddFleet")
    public void testSearchForEditFleet() throws InterruptedException {
        Map<String, String> param = getParam("testSearchForEditFleet");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        AirportTransferFleetPage<?> atPage = new AirportTransferFleetPage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String business = param.get("business");
        String model = param.get("model");
        String successMessage = param.get("successMessage");

        step("Login to Airport Transfer");
        atPage.goToATPage(username, password);

        step("Open Fleet Tab");
        atPage.goToFleetTab();

        step("Search Fleet");
        atPage.searchFleetByBusinessName(business);

        step("Edit Fleet");
        atPage.editFleet(model);
        Assertion.assertThat("Verify the success snackbar is displayed with correct description", atPage.verifySuccessSnackbarContainsText(successMessage), is(true));
        Assertion.assertThat("Verify the edited fleet is shown on result table", atPage.verifySRPContainsFleet(business), is(true));
    }

    @Author(names = { "Adit", "Clement" })
    @Module(name = "Fleet")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testSearchForEditFleet")
    public void testEditFleetPrice() throws InterruptedException {
        Map<String, String> param = getParam("testEditFleetPrice");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        AirportTransferFleetPage<?> atPage = new AirportTransferFleetPage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String business = param.get("business");
        String price = param.get("price");
        String successMessage = param.get("successMessage");

        step("Login to Airport Transfer");
        atPage.goToATPage(username, password);

        step("Open Fleet Tab");
        atPage.goToFleetTab();

        step("Search Fleet");
        atPage.searchFleetByBusinessName(business);

        step("Edit Fleet Price");
        atPage.editPrice(price);
        Assertion.assertThat("Verify the success snackbar is displayed with correct description", atPage.verifySuccessSnackbarContainsText(successMessage), is(true));
        Assertion.assertThat("Verify the edited fleet is shown on result table", atPage.verifySRPContainsFleet(business), is(true));
    }

    @Author(names = { "Adit" , "Clement" })
    @Module(name = "Fleet")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testEditFleetPrice")
    public void testToggleActivateFleet() throws InterruptedException{
        Map<String, String> param = getParam("testToggleActivateFleet");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        AirportTransferFleetPage<?> atPage = new AirportTransferFleetPage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String activateMessage = param.get("activateMessage");
        String deactivateMessage = param.get("deactivateMessage");
        String status = param.get("status");
        String business = param.get("business");

        step("Login to Airport Transfer");
        atPage.goToATPage(username, password);

        step("Open Fleet Tab");
        atPage.goToFleetTab();

        step("Search Fleet");
        atPage.searchFleetByBusinessName(business);

        step("Scroll Table");
        atPage.scrollTable();

        step("Activate Fleet");
        atPage.toggleActiveFleet();
        Assertion.assertThat("Verify the success snackbar is displayed with correct description", atPage.verifySuccessSnackbarDisplayed(String.format(activateMessage)), is(true));
        Assertion.assertThat("Verify the status is Aktif", atPage.verifyStatusIsChanged(status), is(false));

        step("Deactivate Fleet");
        atPage.toggleActiveFleet();
        Assertion.assertThat("Verify the success snackbar is displayed with correct description", atPage.verifySuccessSnackbarDisplayed(String.format(deactivateMessage)), is(true));
        Assertion.assertThat("Verify the status is Non-Aktif", atPage.verifyStatusIsChanged(status), is(true));
    }

    @Author(names = { "Adit" , "Clement" })
    @Module(name = "Fleet")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testToggleActivateFleet")
    public void testDeleteFleet() throws InterruptedException {
        Map<String, String> param = getParam("testDeleteFleet");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        AirportTransferFleetPage<?> atPage = new AirportTransferFleetPage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String business = param.get("business");
        String successMessage = param.get("successMessage");

        step("Login to Airport Transfer");
        atPage.goToATPage(username, password);

        step("Open Fleet Tab");
        atPage.goToFleetTab();

        step("Search Fleet");
        atPage.searchFleetByBusinessName(business);

        step("Delete Fleet");
        atPage.deleteFleet();
        Assertion.assertThat("Verify the success snackbar is displayed with correct description", atPage.verifySuccessSnackbarContainsText(successMessage), is(true));
    }

    @Author(names = { "Adit" , "Clement" })
    @Module(name = "ZoneManagement")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test
    public void testAddNewZone() throws InterruptedException {
        Map<String, String> param = getParam("testAddNewZone");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        AirportTransferZoneManagementPage<?> atPage = new AirportTransferZoneManagementPage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String zoneName = param.get("zoneName");
        String successMessage = param.get("successMessage");
        String filePath = param.get("filePath");

        step("Login to Airport Transfer");
        atPage.goToATPage(username, password);

        step("Open Zone Management Tab");
        atPage.goToZoneManagementTab();

        step("Add The New Zone");
        atPage.addNewZone(zoneName, filePath);
        Assertion.assertThat("Verify the success snackbar is displayed with correct description", atPage.verifySuccessSnackbarDisplayed(successMessage), is(true));
    }

    @Author(names = {"Adit"})
    @Module(name = "ZoneManagement")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testAddNewZone")
    public void testToggleActivateZone() throws InterruptedException {
        Map<String, String> param = getParam("testToggleActivateZone");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        AirportTransferZoneManagementPage<?> atPage = new AirportTransferZoneManagementPage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String successMessage = param.get("successMessage");
        String status = param.get("status");

        step("Login to Airport Transfer");
        atPage.goToATPage(username, password);

        step("Open Zone Tab");
        atPage.goToZoneManagementTab();

        step("Activate Zone");
        atPage.toggleActiveZone();
        Assertion.assertThat("Verify the success snackbar is displayed with correct description", atPage.verifySuccessSnackbarDisplayed(String.format(successMessage)), is(true));
        Assertion.assertThat("Verify the status is Aktif", atPage.verifyStatusIsChanged(status), is(false));

        step("Deactivate Zone");
        atPage.toggleActiveZone();
        Assertion.assertThat("Verify the success snackbar is displayed with correct description", atPage.verifySuccessSnackbarDisplayed(String.format(successMessage)), is(true));
        Assertion.assertThat("Verify the status is Non-Aktif", atPage.verifyStatusIsChanged(status), is(true));
    }

    @Author(names = {"Adit"})
    @Module(name = "ZoneManagement")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test
    public void testAddNewZoneWithEmptyFields() throws InterruptedException {
        Map<String, String> param = getParam("testAddNewZoneWithEmptyFields");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        AirportTransferZoneManagementPage<?> atPage = new AirportTransferZoneManagementPage<>(session);
        String username = param.get("username");
        String password = param.get("password");

        step("Login to Airport Transfer");
        atPage.goToATPage(username, password);

        step("Open Zone Management Tab");
        atPage.goToZoneManagementTab();

        step("Add The New Zone");
        atPage.addNewZone();
        Assertion.assertThat("Verify the success snackbar is not displayed", atPage.verifySuccessSnackbarIsDisplayed(), is(false));
    }

    @Author(names = {"Adit"})
    @Module(name = "ZoneManagement")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testAddNewZone")
    public void testEditZone() throws InterruptedException {
        Map<String, String> param = getParam("testEditZone");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        AirportTransferZoneManagementPage<?> atPage = new AirportTransferZoneManagementPage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String zoneName = param.get("zoneName");
        String successMessage = param.get("successMessage");

        step("Login to Airport Transfer");
        atPage.goToATPage(username, password);

        step("Open Zone Management Tab");
        atPage.goToZoneManagementTab();

        step("Edit The Zone");
        atPage.editZone(zoneName);
        Assertion.assertThat("Verify the success snackbar is displayed with correct description", atPage.verifySuccessSnackbarDisplayed(successMessage), is(true));
    }

    @Author(names = {"Adit"})
    @Module(name = "BusinessProfile")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test
    public void testSearchBusinessProfile() throws InterruptedException {
        Map<String, String> param = getParam("testSearchBusinessProfile");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        AirportTransferBusinessProfilePage<?> atPage = new AirportTransferBusinessProfilePage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String name = param.get("name");

        step("Login to Airport Transfer");
        atPage.goToATPage(username, password);

        step("Open Business Profile Tab");
        atPage.goToBusinessProfileTab();

        step("Filter By Transfer IN");
        atPage.filterTransferIn();

        step("Search The Business Profile");
        atPage.searchBusinessProfileByName(name);
        Assertion.assertThat("Verify the business profile exists on srp", atPage.verifySRPContainsCorrectName(name), is(true));
    }

    @Author(names = { "Adit" , "Clement"})
    @Module(name = "BusinessProfile")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testSearchBusinessProfile")
    public void testAddAccountTransferInBusinessProfile() throws InterruptedException {
        Map<String, String> param = getParam("testAddAccountTransferInBusinessProfile");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        AirportTransferBusinessProfilePage<?> atPage = new AirportTransferBusinessProfilePage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String firstName = param.get("firstName");
        String lastName = param.get("lastName");
        String phoneNumber = param.get("phoneNumber");
        String successMessage = param.get("successMessage");

        step("Login to Airport Transfer");
        atPage.goToATPage(username, password);

        step("Open Business Profile Tab");
        atPage.goToBusinessProfileTab();

        step("Filter By Transfer IN");
        atPage.filterTransferIn();

        step("Add New Account");
        atPage.addNewAccount(firstName, lastName, phoneNumber);
        Assertion.assertThat("Verify the success snackbar is displayed with correct description", atPage.verifySuccessSnackbarDisplayed(successMessage), is(true));
        Assertion.assertThat("Verify the new account is shown on result table", atPage.verifyAccountTableContainsCorrectName(firstName), is(true));
    }

    @Author(names = { "Adit" , "Clement"})
    @Module(name = "BusinessProfile")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testAddAccountTransferInBusinessProfile")
    public void testEditAccountTransferInBusinessProfile() throws InterruptedException {
        Map<String, String> param = getParam("testEditAccountTransferInBusinessProfile");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        AirportTransferBusinessProfilePage<?> atPage = new AirportTransferBusinessProfilePage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String firstName = param.get("firstName");
        String lastName = param.get("lastName");
        String phoneNumber = param.get("phoneNumber");
        String initialFirstName = param.get("initialFirstName");
        String successMessage = param.get("successMessage");

        step("Login to Airport Transfer");
        atPage.goToATPage(username, password);

        step("Open Business Profile Tab");
        atPage.goToBusinessProfileTab();

        step("Filter By Transfer IN");
        atPage.filterTransferIn();

        step("Edit Account");
        atPage.editAccount(firstName, lastName, phoneNumber);
        Assertion.assertThat("Verify the success snackbar is displayed with correct description", atPage.verifySuccessSnackbarDisplayed(successMessage), is(true));
        Assertion.assertThat("Verify the edited account is shown on result table", atPage.verifyAccountTableContainsCorrectName(initialFirstName + firstName), is(true));
    }

    @Author(names = { "Adit" , "Clement"})
    @Module(name = "BusinessProfile")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testEditAccountTransferInBusinessProfile")
    public void testDeleteAccountTransferInBusinessProfile() throws InterruptedException {
        Map<String, String> param = getParam("testDeleteAccountTransferInBusinessProfile");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        AirportTransferBusinessProfilePage<?> atPage = new AirportTransferBusinessProfilePage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String successMessage = param.get("successMessage");

        step("Login to Airport Transfer");
        atPage.goToATPage(username, password);

        step("Open Business Profile Tab");
        atPage.goToBusinessProfileTab();

        step("Filter By Transfer IN");
        atPage.filterTransferIn();

        step("Delete Account");
        atPage.deleteAccount();
        Assertion.assertThat("Verify the success snackbar is displayed with correct description", atPage.verifySuccessSnackbarDisplayed(successMessage), is(true));
    }

    @Author(names = { "Adit" , "Clement"})
    @Module(name = "BusinessProfile")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test
    public void testAddAccountTransferOutBusinessProfile() throws InterruptedException {
        Map<String, String> param = getParam("testAddAccountTransferOutBusinessProfile");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        AirportTransferBusinessProfilePage<?> atPage = new AirportTransferBusinessProfilePage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String firstName = param.get("firstName");
        String lastName = param.get("lastName");
        String phoneNumber = param.get("phoneNumber");
        String successMessage = param.get("successMessage");

        step("Login to Airport Transfer");
        atPage.goToATPage(username, password);

        step("Open Business Profile Tab");
        atPage.goToBusinessProfileTab();

        step("Filter By Transfer Out");
        atPage.filterTransferOut();

        step("Add New Account");
        atPage.addNewAccount(firstName, lastName, phoneNumber);
        Assertion.assertThat("Verify the success snackbar is displayed with correct description", atPage.verifySuccessSnackbarDisplayed(successMessage), is(true));
        Assertion.assertThat("Verify the new account is shown on result table", atPage.verifyAccountTableContainsCorrectName(firstName), is(true));
    }

    @Author(names = { "Adit" , "Clement"})
    @Module(name = "BusinessProfile")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testAddAccountTransferOutBusinessProfile")
    public void testEditAccountTransferOutBusinessProfile() throws InterruptedException {
        Map<String, String> param = getParam("testEditAccountTransferOutBusinessProfile");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        AirportTransferBusinessProfilePage<?> atPage = new AirportTransferBusinessProfilePage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String firstName = param.get("firstName");
        String lastName = param.get("lastName");
        String phoneNumber = param.get("phoneNumber");
        String initialFirstName = param.get("initialFirstName");
        String successMessage = param.get("successMessage");

        step("Login to Airport Transfer");
        atPage.goToATPage(username, password);

        step("Open Business Profile Tab");
        atPage.goToBusinessProfileTab();

        step("Filter By Transfer Out");
        atPage.filterTransferOut();

        step("Edit Account");
        atPage.editAccount(firstName, lastName, phoneNumber);
        Assertion.assertThat("Verify the success snackbar is displayed with correct description", atPage.verifySuccessSnackbarDisplayed(successMessage), is(true));
        Assertion.assertThat("Verify the edited account is shown on result table", atPage.verifyAccountTableContainsCorrectName(initialFirstName + firstName), is(true));
    }

    @Author(names = { "Adit" , "Clement"})
    @Module(name = "BusinessProfile")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testEditAccountTransferOutBusinessProfile")
    public void testDeleteAccountTransferOutBusinessProfile() throws InterruptedException {
        Map<String, String> param = getParam("testDeleteAccountTransferOutBusinessProfile");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        AirportTransferBusinessProfilePage<?> atPage = new AirportTransferBusinessProfilePage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String successMessage = param.get("successMessage");

        step("Login to Airport Transfer");
        atPage.goToATPage(username, password);

        step("Open Business Profile Tab");
        atPage.goToBusinessProfileTab();

        step("Filter By Transfer OUT");
        atPage.filterTransferOut();

        step("Delete Account");
        atPage.deleteAccount();
        Assertion.assertThat("Verify the success snackbar is displayed with correct description", atPage.verifySuccessSnackbarDisplayed(successMessage), is(true));
    }

    @Author(names = { "Adit" , "Clement", "Manan Kumar"})
    @Module(name = "Airport")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test
    public void testAddNewAirport() throws InterruptedException {
        Map<String, String> param = getParam("testAddNewAirport");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        AirportTransferAirportPage<?> atPage = new AirportTransferAirportPage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String airportName = param.get("airportName") + " " + CoreFunctions.randomName();
        String cityName = param.get("cityName") + " " + CoreFunctions.randomName();
        String stationCode = param.get("stationCode") + " " + CoreFunctions.randomName();
        String latitude = param.get("latitude");
        String longitude = param.get("longitude");
        String description = param.get("description") + " " + CoreFunctions.randomName();
        String successBoothMessage = param.get("successBoothMessage");
        String successAddMessage = param.get("successAddMessage");
        int minTransferOut = Integer.parseInt(param.get("minTransferOut"));
        int minTransferIn = Integer.parseInt(param.get("minTransferIn"));
        int autoCompleteRadius = Integer.parseInt(param.get("autoCompleteRadius"));
        String successMessage = param.get("successMessage");


        step("Login to Airport Transfer");
        atPage.goToATPage(username, password);

        step("Open Airport Tab");
        atPage.goToAirportTab();

        step("Add The New Airport");
        atPage.addNewAirport(airportName, cityName, stationCode, latitude, longitude, description, minTransferOut, minTransferIn, autoCompleteRadius);

        step("Verify that new Airport is created");
        Assertion.assertThat("Verify the success snackbar is displayed with correct description", atPage.verifySuccessSnackbarDisplayed(String.format(successAddMessage, airportName)), is(true));

        step("Search The Airport");
        atPage.searchAirportByName(airportName);

        step("Edit Airport Booth");
        atPage.editBooth("Booth 1");
        Assertion.assertThat("Verify the success snackbar is displayed with correct description", atPage.verifySuccessSnackbarDisplayed(String.format(successBoothMessage, airportName)), is(true));

        step("Search The Airport");
        atPage.searchAirportByName(airportName);

        step("Delete The Airport");
        atPage.deleteAirport();
        Assertion.assertThat("Verify the success snackbar is displayed with correct description", atPage.verifySuccessSnackbarDisplayed(successMessage), is(true));
    }

    @Author(names = { "Adit" , "Clement" ,"Manan Kumar"})
    @Module(name = "Airport")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test
    public void testSearchForEditAirport() throws InterruptedException {
        Map<String, String> param = getParam("testSearchForEditAirport");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        AirportTransferAirportPage<?> atPage = new AirportTransferAirportPage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String airportName = param.get("airportName") + " " + CoreFunctions.randomName();
        String cityName = param.get("cityName") + " " + CoreFunctions.randomName();
        String stationCode = param.get("stationCode") + " " + CoreFunctions.randomName();
        String latitude = param.get("latitude");
        String longitude = param.get("longitude");
        String description = param.get("description") + " " + CoreFunctions.randomName();
        String successAddMessage = param.get("successAddMessage");
        int minTransferOut = Integer.parseInt(param.get("minTransferOut"));
        int minTransferIn = Integer.parseInt(param.get("minTransferIn"));
        int autoCompleteRadius = Integer.parseInt(param.get("autoCompleteRadius"));
        String successMessage = param.get("successMessage");

        step("Login to Airport Transfer");
        atPage.goToATPage(username, password);

        step("Open Airport Tab");
        atPage.goToAirportTab();

        step("Add The New Airport");
        atPage.addNewAirport(airportName, cityName, stationCode, latitude, longitude, description, minTransferOut, minTransferIn, autoCompleteRadius);

        step("Verify that new Airport is created");
        Assertion.assertThat("Verify the success snackbar is displayed with correct description", atPage.verifySuccessSnackbarDisplayed(String.format(successAddMessage, airportName)), is(true));

        step("Search The Airport");
        atPage.searchAirportByName(airportName);

        step("Edit The Airport");
        atPage.editAirport(longitude, latitude, description, minTransferOut, minTransferIn, autoCompleteRadius);
        Assertion.assertThat("Verify the success snackbar is displayed with correct description", atPage.verifySuccessSnackbarDisplayed(String.format(successMessage, airportName)), is(true));

        step("Search The Airport");
        atPage.searchAirportByName(airportName);

        step("Delete The Airport");
        atPage.deleteAirport();
        Assertion.assertThat("Verify the success snackbar is displayed with correct description", atPage.verifySuccessSnackbarDisplayed(successMessage), is(true));
    }

    @Author(names = { "Adit" , "Clement","Manan Kumar" })
    @Module(name = "Airport")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test
    public void testToggleActivateAirport() throws InterruptedException {
        Map<String, String> param = getParam("testToggleActivateAirport");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        AirportTransferAirportPage<?> atPage = new AirportTransferAirportPage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String airportName = param.get("airportName") + " " + CoreFunctions.randomName();
        String cityName = param.get("cityName") + " " + CoreFunctions.randomName();
        String stationCode = param.get("stationCode") + " " + CoreFunctions.randomName();
        String latitude = param.get("latitude");
        String longitude = param.get("longitude");
        String description = param.get("description") + " " + CoreFunctions.randomName();
        String successAddMessage = param.get("successAddMessage");
        int minTransferOut = Integer.parseInt(param.get("minTransferOut"));
        int minTransferIn = Integer.parseInt(param.get("minTransferIn"));
        int autoCompleteRadius = Integer.parseInt(param.get("autoCompleteRadius"));
        String successMessage = param.get("successMessage");
        String status = param.get("status");
        String activateMessage = param.get("activateMessage");
        String deactivateMessage = param.get("deactivateMessage");
        String successBoothMessage = param.get("successBoothMessage");

        step("Login to Airport Transfer");
        atPage.goToATPage(username, password);

        step("Open Airport Tab");
        atPage.goToAirportTab();

        step("Add The New Airport");
        atPage.addNewAirport(airportName, cityName, stationCode, latitude, longitude, description, minTransferOut, minTransferIn, autoCompleteRadius);

        step("Verify that new Airport is created");
        Assertion.assertThat("Verify the success snackbar is displayed with correct description", atPage.verifySuccessSnackbarDisplayed(String.format(successAddMessage, airportName)), is(true));

        step("Search The Airport");
        atPage.searchAirportByName(airportName);

        step("Edit Airport Booth");
        atPage.editBooth("Booth 1");
        Assertion.assertThat("Verify the success snackbar is displayed with correct description", atPage.verifySuccessSnackbarDisplayed(String.format(successBoothMessage, airportName)), is(true));

        step("Search The Airport");
        atPage.searchAirportByName(airportName);

        step("Scroll Table");
        atPage.scrollTable();

        step("Deactivate Airport");
        atPage.toggleActiveAirport();
        Assertion.assertThat("Verify the success snackbar is displayed with correct description", atPage.verifySuccessSnackbarDisplayed(deactivateMessage), is(true));
        Assertion.assertThat("Verify the status is Non-Aktif", atPage.verifyStatusIsChanged(status), is(true));

        step("Activate Airport");
        atPage.toggleActiveAirport();
        Assertion.assertThat("Verify the success snackbar is displayed with correct description", atPage.verifySuccessSnackbarDisplayed(activateMessage), is(true));
        Assertion.assertThat("Verify the status is Aktif", atPage.verifyStatusIsChanged(status), is(false));


        step("Search The Airport");
        atPage.searchAirportByName(airportName);

        step("Delete The Airport");
        atPage.deleteAirport();
        Assertion.assertThat("Verify the success snackbar is displayed with correct description", atPage.verifySuccessSnackbarDisplayed(successMessage), is(true));
    }

    @Author(names = { "Adit" , "Clement","Manan Kumar" })
    @Module(name = "Airport")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test
    public void testDeleteAirport() throws InterruptedException {
        Map<String, String> param = getParam("testDeleteAirport");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        AirportTransferAirportPage<?> atPage = new AirportTransferAirportPage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String successMessage = param.get("successMessage");
        String airportName = param.get("airportName") + " " + CoreFunctions.randomName();
        String cityName = param.get("cityName") + " " + CoreFunctions.randomName();
        String stationCode = param.get("stationCode") + " " + CoreFunctions.randomName();
        String latitude = param.get("latitude");
        String longitude = param.get("longitude");
        String description = param.get("description") + " " + CoreFunctions.randomName();
        String successBoothMessage = param.get("successBoothMessage");
        String successAddMessage = param.get("successAddMessage");
        int minTransferOut = Integer.parseInt(param.get("minTransferOut"));
        int minTransferIn = Integer.parseInt(param.get("minTransferIn"));
        int autoCompleteRadius = Integer.parseInt(param.get("autoCompleteRadius"));

        step("Login to Airport Transfer");
        atPage.goToATPage(username, password);

        step("Open Airport Tab");
        atPage.goToAirportTab();

        step("Add The New Airport");
        atPage.addNewAirport(airportName, cityName, stationCode, latitude, longitude, description, minTransferOut, minTransferIn, autoCompleteRadius);

        step("Verify that new Airport is created");
        Assertion.assertThat("Verify the success snackbar is displayed with correct description", atPage.verifySuccessSnackbarDisplayed(String.format(successAddMessage, airportName)), is(true));

        step("Search The Airport");
        atPage.searchAirportByName(airportName);

        step("Edit Airport Booth");
        atPage.editBooth("Booth 1");
        Assertion.assertThat("Verify the success snackbar is displayed with correct description", atPage.verifySuccessSnackbarDisplayed(String.format(successBoothMessage, airportName)), is(true));

        step("Search The Airport");
        atPage.searchAirportByName(airportName);

        step("Delete The Airport");
        atPage.deleteAirport();
        Assertion.assertThat("Verify the success snackbar is displayed with correct description", atPage.verifySuccessSnackbarDisplayed(successMessage), is(true));
    }

    @Author(names = {"Eka"})
    @Module(name = "BusinessProfile")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test
    public void testSearchVendorOut() throws InterruptedException {
        Map<String, String> param = getParam("testSearchVendorOut");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        AirportTransferBusinessProfilePage<?> atPage = new AirportTransferBusinessProfilePage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String name = param.get("name");

        step("Login to Airport Transfer");
        atPage.goToATPage(username, password);

        step("Open Business Profile Tab");
        atPage.goToBusinessProfileTab();

        step("Filter By Transfer Out");
        atPage.filterTransferOut();

        step("Search The Business Profile");
        atPage.searchBusinessProfileByName(name);
        Assertion.assertThat("Verify the business profile exists on srp", atPage.verifySRPContainsCorrectName(name), is(true));

    }
    @Author(names = {"Eka"})
    @Module(name = "BusinessProfile")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testSearchVendorOut")
    public void testEditVendorOut() throws InterruptedException {
        Map<String, String> param = getParam("testEditVendorOut");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        AirportTransferBusinessProfilePage<?> atPage = new AirportTransferBusinessProfilePage<>(session);
        String commission = param.get("commission");
        String name = param.get("name");

        step("Click Commission");
        atPage.clickCommission();

        step("Input Commission");
        atPage.inputCommission(commission);
        Assertion.assertThat("Verify the business profile exists on srp", atPage.verifySRPContainsCorrectName(name), is(true));
    }
    @Author(names = {"Eka"})
    @Module(name = "BusinessProfile")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test
    public void testEditVendorIn() throws InterruptedException {
        Map<String, String> param = getParam("testEditVendorIn");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        AirportTransferBusinessProfilePage<?> atPage = new AirportTransferBusinessProfilePage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String name = param.get("name");
        String commission = param.get("commission");
        String successMessage = param.get("successMessage");

        step("Login to Airport Transfer");
        atPage.goToATPage(username, password);

        step("Open Business Profile Tab");
        atPage.goToBusinessProfileTab();

        step("Filter By Transfer IN");
        atPage.filterTransferIn();

        step("Search The Business Profile");
        atPage.searchBusinessProfileByName(name);
        Assertion.assertThat("Verify the business profile exists on srp", atPage.verifySRPContainsCorrectName(name), is(true));

        step("Click Commission");
        atPage.clickCommission();

        step("Input Commission");
        atPage.inputCommission(commission);
        Assertion.assertThat("Verify the success snackbar is displayed with correct description", atPage.verifySuccessSnackbarDisplayed(successMessage), is(true));
    }

    @Author(names = {"Pankaj Singh Kushwaha"})
    @Module(name = "Finance")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test
    public void testUploadXSLButtonNotVisibleOnTheATFinancePage() throws InterruptedException {
        Map<String, String> param = getParam("testUploadXSLButtonNotVisibleOnTheATFinancePage");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        AirportTransferBusinessProfilePage<?> atPage = new AirportTransferBusinessProfilePage<>(session);
        String username = param.get("username");
        String password = param.get("password");

        step("Login to NFT Admin Dashboard");
        atPage.goToNFTDashboardPage(username, password);

        step("Click the Airport Transfer menu");
        atPage.clickAirportTransferMenu();

        step("Click on the Finance menu from the sidebar");
        atPage.clickFinanceMenu();

        step("Verify that the Finance page is successfully opened");
        atPage.verifyFinancePageOpened();

        step("Verify that there is NO 'Upload XLS' button present on the Finance page");
        atPage.verifyUploadXlsButtonNotPresent();
    }

    @Author(names = { "Prachi Upadhyay" })
    @Module(name = "Airport")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test
    public void testSearchForEditAirportDetails() throws InterruptedException {
        Map<String, String> param = getParam("testSearchForEditAirportDetails");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        AirportTransferAirportPage<?> atPage = new AirportTransferAirportPage<>(session);
        String username = param.get("username");
        String password = param.get("password");

        String airportName = param.get("airportName");
        String latitude = param.get("latitude");
        String longitude = param.get("longitude");

        String descriptionIdnParam = param.get("descriptionIDN");
        String descriptionEngParam = param.get("descriptionENG");
        String randomSuffix = CoreFunctions.randomName();
        String descriptionIdn = (descriptionIdnParam != null && !descriptionIdnParam.isEmpty()) ? descriptionIdnParam + " " + randomSuffix : null;
        String descriptionEng = (descriptionEngParam != null && !descriptionEngParam.isEmpty()) ? descriptionEngParam + " " + randomSuffix : null;

        String minTransferOut = param.get("minTransferOut");
        String minTransferIn = param.get("minTransferIn");
        String autoCompleteRadius = param.get("autoCompleteRadius");

        String successMessage = param.get("successMessage");

        step("Login to Dashboard");
        atPage.goToNFTDashboard(username, password);

        step("Navigate to Airport & Station menu via sidebar");
        atPage.navigateToAirportAndStationMenu();

        step("Search The Airport");
        atPage.searchAirportName(airportName);

        step("Edit The Airport");
        atPage.editAirportDetails(longitude, latitude, descriptionIdn, descriptionEng, minTransferOut, minTransferIn, autoCompleteRadius);
        Assertion.assertThat("Verify the new success snackbar is displayed", atPage.verifyNewSuccessSnackbarDisplayed(successMessage), is(true));

        step("Verify new updated Airport/Station Details");
        atPage.searchAirportName(airportName);
        atPage.verifyUpdatedAirportDetails(longitude, latitude, descriptionIdn, descriptionEng, minTransferOut, minTransferIn, autoCompleteRadius);
        Assertion.assertThat("Verify the success snackbar is displayed after second save", atPage.verifyNewSuccessSnackbarDisplayed(successMessage), is(true));
    }

    @Author(names = { "Prachi Upadhyay" })
    @Module(name = "Airport")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test
    public void testEditBoothDetails() throws InterruptedException {
        Map<String, String> param = getParam("testEditBoothDetails");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        AirportTransferAirportPage<?> atPage = new AirportTransferAirportPage<>(session);
        String username = param.get("username");
        String password = param.get("password");

        String airportName = param.get("airportName");
        String boothName = param.get("boothName");
        String successMessage = param.get("successMessage");

        step("Login to Dashboard");
        atPage.goToNFTDashboard(username, password);

        step("Navigate to Airport & Station menu via sidebar");
        atPage.navigateToAirportAndStationMenu();

        step("Search The Airport");
        atPage.searchAirportName(airportName);

        step("Click Edit Booth, Fill Details, Toggle Status and Save");
        atPage.editBoothDetails(boothName);

        step("Verify new Booth is successfully added");
        Assertion.assertThat("Verify the success snackbar is displayed", atPage.verifyNewSuccessSnackbarDisplayed(successMessage), is(true));
        Assertion.assertThat("Verify booth name and toggle status are persisted after reopening Edit Booth", atPage.verifyBoothUpdatedAfterSave(boothName), is(true));
    }

    @Author(names = { "Prachi Upadhyay" })
    @Module(name = "Airport")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test
    public void AirportTransferActiveDeactiveTest() throws InterruptedException {
        Map<String, String> param = getParam("AirportTransferActiveDeactiveTest");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        AirportTransferAirportPage<?> atPage = new AirportTransferAirportPage<>(session);
        String username = param.get("username");
        String password = param.get("password");

        String airportName = param.get("airportName");

        String successMessage = param.get("successMessage");

        step("Login to Dashboard");
        atPage.goToNFTDashboard(username, password);

        step("Navigate to Airport & Station menu via sidebar");
        atPage.navigateToAirportAndStationMenu();

        step("Search The Airport");
        atPage.searchAirportName(airportName);

        step("Verify current/default state of the Airport/Station toggle");
        boolean initialStateActive = atPage.isAirportStatusActive();

        step("Toggle Airport/Station status to the opposite state");
        atPage.toggleAirportStatus();
        Assertion.assertThat("Verify the success snackbar is displayed", atPage.verifyNewSuccessSnackbarDisplayed(successMessage), is(true));
        Assertion.assertThat("Verify toggle state changed to the opposite of the initial state", atPage.isAirportStatusActive(), is(!initialStateActive));

        step("Toggle Airport/Station status again to restore the original state");
        atPage.toggleAirportStatus();
        Assertion.assertThat("Verify the success snackbar is displayed", atPage.verifyNewSuccessSnackbarDisplayed(successMessage), is(true));
        Assertion.assertThat("Verify the original toggle state is restored", atPage.isAirportStatusActive(), is(initialStateActive));
    }
}