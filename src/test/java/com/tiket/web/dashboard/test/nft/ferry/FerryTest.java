package com.tiket.web.dashboard.test.nft.ferry;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.nft.ferry.FerryClassPage;
import com.tiket.web.dashboard.page.nft.ferry.FerryCovidReasonPage;
import com.tiket.web.dashboard.page.nft.ferry.FerryFacilityPage;
import com.tiket.web.dashboard.page.nft.ferry.FerryRoutePage;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.is;

public class FerryTest extends BaseTest {
    @Author(names = { "Adit" })
    @Module(name = "Class")
    @Tribe(name = "Ferry")
    @Vertical(name = "NFT")
    @Test
    public void testViewFerryClass() {
        Map<String, String> param = getParam("testViewFerryClass");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        FerryClassPage<?> ferryPage = new FerryClassPage<>(session);
        String username = param.get("username");
        String password = param.get("password");

        step("Login to Ferry Page");
        ferryPage.goToFerryPage(username, password);

        step("Open Class Tab");
        ferryPage.goToClassTab();

        step("Check the result page");
        Assertion.assertThat("Verify that the srp is not empty", ferryPage.verifyTheSRPIsNotEmpty(), is(true));
    }

    @Author(names = { "Adit" , "Clement" })
    @Module(name = "Class")
    @Tribe(name = "Ferry")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testViewFerryClass")
    public void testAddNewFerryClass() {
        Map<String, String> param = getParam("testAddNewFerryClass");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        FerryClassPage<?> ferryPage = new FerryClassPage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String label = param.get("label");
        String idName = param.get("idName");
        String enName = param.get("enName");
        String vendorId = param.get("vendorId");
        int index = Integer.parseInt(param.get("index"));
        String successUploadImageMessage = param.get("successUploadImageMessage");
        String successCreateClassMessage = param.get("successCreateClassMessage");
        String filepath = param.get("filePath");

        step("Login to Ferry Page");
        ferryPage.goToFerryPage(username, password);

        step("Open Class Tab");
        ferryPage.goToClassTab();

        step("Click Add Class Button");
        ferryPage.goToAddClassPage();

        step("Add Image");
        ferryPage.addImage(filepath);
        Assertion.assertThat("Verify the success uploaded image snackbar is displayed", ferryPage.isSuccessSnackbarDisplayedWithCorrectMessage(successUploadImageMessage), is(true));

        step("Fill All Mandatory Fields");
        ferryPage.addNewClass(label, idName, enName, vendorId, index);

        step("Save The New Class");
        ferryPage.saveForm();
        Assertion.assertThat("Verify the success creation class data snackbar is displayed", ferryPage.isSuccessSnackbarDisplayedWithCorrectMessage(String.format(successCreateClassMessage, label)), is(true));

        step("Check The First Item in SRP");
        Assertion.assertThat("Verify the created class data is at the top of the Class menu list", ferryPage.verifyClassIsAtTheTop(label), is(true));

        step("Search The Class Data");
        ferryPage.searchFerryClass(label);
        Assertion.assertThat("Verify the created class data can be searched", ferryPage.verifySRPContainsClassLabel(label), is(true));
    }

    @Author(names = { "Adit" })
    @Module(name = "Class")
    @Tribe(name = "Ferry")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testAddNewFerryClass")
    public void testDuplicateFerryClass() {
        Map<String, String> param = getParam("testDuplicateFerryClass");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        FerryClassPage<?> ferryPage = new FerryClassPage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String successDuplicateClassMessage = param.get("successDuplicateClassMessage");

        step("Login to Ferry Page");
        ferryPage.goToFerryPage(username, password);

        step("Open Class Tab");
        ferryPage.goToClassTab();

        step("Get The First Item");
        String label = ferryPage.getFirstSRPItemLabel().getText();
        String copiedLabel = "[Copied] " + label;

        step("Click Duplicate Class Button");
        ferryPage.duplicateClass();
        Assertion.assertThat("Verify the success duplication class snackbar is displayed", ferryPage.isSuccessSnackbarDisplayedWithCorrectMessage(String.format(successDuplicateClassMessage, copiedLabel)), is(true));

        step("Check The First Item in SRP");
        ferryPage.refreshPage();
        Assertion.assertThat("Verify the duplicated class data is at the top of the Class menu list", ferryPage.verifyClassIsAtTheTop(copiedLabel), is(true));

        step("Search The Class Data");
        ferryPage.searchFerryClass(copiedLabel);
        Assertion.assertThat("Verify the duplicated class data can be searched", ferryPage.verifySRPContainsClassLabel(copiedLabel), is(true));
    }

    @Author(names = { "Adit" })
    @Module(name = "Class")
    @Tribe(name = "Ferry")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testDuplicateFerryClass")
    public void testSearchFerryClass() {
        Map<String, String> param = getParam("testSearchFerryClass");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        FerryClassPage<?> ferryPage = new FerryClassPage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String label = param.get("label");
        String idName = param.get("idName");
        String enName = param.get("enName");
        step("Login to Ferry Page");
        ferryPage.goToFerryPage(username, password);

        step("Open Class Tab");
        ferryPage.goToClassTab();

        step("Search The Class Data by Label");
        ferryPage.searchFerryClass(label);
        Assertion.assertThat("Verify the searched label matches the label in the list", ferryPage.verifySRPContainsClassLabel(label), is(true));

        step("Search The Class Data by ID Name");
        ferryPage.searchFerryClass(idName);
        Assertion.assertThat("Verify the searched name (ID) matches the name (ID) in the list", ferryPage.verifySRPContainsClassIdName(idName), is(true));

        step("Search The Class Data by EN Name");
        ferryPage.searchFerryClass(enName);
        Assertion.assertThat("Verify the searched name (EN) matches the name (EN) in the list", ferryPage.verifySRPContainsClassEnName(enName), is(true));
    }

    @Author(names = { "Adit" })
    @Module(name = "Class")
    @Tribe(name = "Ferry")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testSearchFerryClass")
    public void testEditFerryClass() {
        Map<String, String> param = getParam("testEditFerryClass");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        FerryClassPage<?> ferryPage = new FerryClassPage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String idName = param.get("idName");
        String enName = param.get("enName");
        String successEditClassMessage = param.get("successEditClassMessage");

        step("Login to Ferry Page");
        ferryPage.goToFerryPage(username, password);

        step("Open Class Tab");
        ferryPage.goToClassTab();

        step("Get The First Item");
        String label = ferryPage.getFirstSRPItemLabel().getText();

        step("Click Edit Class Button");
        ferryPage.goToEditClassPage();

        step("Edit The Fields");
        ferryPage.editFerryClass(idName, enName);
        Assertion.assertThat("Verify the success edit snackbar is displayed", ferryPage.isSuccessSnackbarDisplayedWithCorrectMessage(String.format(successEditClassMessage, label)), is(true));

        step("Search The Class Data by ID Name");
        ferryPage.searchFerryClass(idName);
        Assertion.assertThat("Verify the Name (ID) have been changed and matched the edited version", ferryPage.verifySRPContainsClassIdName(idName), is(true));

        step("Search The Class Data by EN Name");
        ferryPage.searchFerryClass(enName);
        Assertion.assertThat("Verify the Name (EN) have been changed and matched the edited version", ferryPage.verifySRPContainsClassEnName(enName), is(true));
    }

    @Author(names = { "Adit" })
    @Module(name = "Class")
    @Tribe(name = "Ferry")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testEditFerryClass")
    public void testToggleActivateFerryClass() {
        Map<String, String> param = getParam("testToggleActivateFerryClass");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        FerryClassPage<?> ferryPage = new FerryClassPage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String activateMessage = param.get("activateMessage");
        String deactivateMessage = param.get("deactivateMessage");

        step("Login to Ferry Page");
        ferryPage.goToFerryPage(username, password);

        step("Open Class Tab");
        ferryPage.goToClassTab();

        step("Get The First Item");
        String label = ferryPage.getFirstSRPItemLabel().getText();

        step("Activate Class");
        ferryPage.toggleActiveFerry();
        Assertion.assertThat("Verify the success snackbar is displayed with correct description", ferryPage.isSuccessSnackbarDisplayedWithCorrectMessage(String.format(activateMessage, label)), is(true));

        step("Refresh Page");
        ferryPage.refreshPage();

        step("Deactivate Class");
        ferryPage.toggleActiveFerry();
        Assertion.assertThat("Verify the success snackbar is displayed with correct description", ferryPage.isSuccessSnackbarDisplayedWithCorrectMessage(String.format(deactivateMessage, label)), is(true));
    }

    @Author(names = { "Adit" , "Clement" })
    @Module(name = "Class")
    @Tribe(name = "Ferry")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testToggleActivateFerryClass")
    public void testDeleteFerryClass() {
        Map<String, String> param = getParam("testDeleteFerryClass");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        FerryClassPage<?> ferryPage = new FerryClassPage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String label = param.get("label");
        String duplicatedLabel = "[Copied] " + label;
        String successDeleteClassMessage = param.get("successDeleteClassMessage");

        step("Login to Ferry Page");
        ferryPage.goToFerryPage(username, password);

        step("Open Class Tab");
        ferryPage.goToClassTab();

        step("Search The Class Data");
        ferryPage.searchFerryClass(label);

        step("Click Delete Class Button for the Added Duplicated Class");
        ferryPage.deleteClass();
        Assertion.assertThat("Verify the success deletion snackbar is displayed", ferryPage.isSuccessSnackbarDisplayedWithCorrectMessage(String.format(successDeleteClassMessage, duplicatedLabel)), is(true));

        ferryPage.waitForSnackbarToDisappear();
        
        step("Click Delete Class Button for the Added Class");
        ferryPage.deleteClass();
        Assertion.assertThat("Verify the success deletion snackbar is displayed", ferryPage.isSuccessSnackbarDisplayedWithCorrectMessage(String.format(successDeleteClassMessage, label)), is(true));

        step("Search The Class Data");
        ferryPage.searchFerryClass(label);
        Assertion.assertThat("Verify the deleted class can not be searched", ferryPage.verifyTheSRPIsNotEmpty(), is(false));
    }

    @Author(names = { "Adit" })
    @Module(name = "CovidReason")
    @Tribe(name = "Ferry")
    @Vertical(name = "NFT")
    @Test
    public void testViewFerryCovidReason() {
        Map<String, String> param = getParam("testViewFerryCovidReason");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        FerryCovidReasonPage<?> ferryPage = new FerryCovidReasonPage<>(session);
        String username = param.get("username");
        String password = param.get("password");

        step("Login to Ferry Page");
        ferryPage.goToFerryPage(username, password);

        step("Open Covid Reason Tab");
        ferryPage.goToCovidReasonTab();

        step("Check the result page");
        Assertion.assertThat("Verify that the srp is not empty", ferryPage.verifyTheSRPIsNotEmpty(), is(true));
    }

    @Author(names = { "Adit" })
    @Module(name = "CovidReason")
    @Tribe(name = "Ferry")
    @Vertical(name = "NFT")
    @Test
    public void testSearchFerryCovidReason() {
        Map<String, String> param = getParam("testSearchFerryCovidReason");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        FerryCovidReasonPage<?> ferryPage = new FerryCovidReasonPage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String code = param.get("code");
        String vendorReason = param.get("vendorReason");
        String idReason = param.get("idReason");
        String enReason = param.get("enReason");
        step("Login to Ferry Page");
        ferryPage.goToFerryPage(username, password);

        step("Open Covid Reason Tab");
        ferryPage.goToCovidReasonTab();

        step("Search The Covid Reason Data by Code");
        ferryPage.searchFerryCovidReason(code);
        Assertion.assertThat("Verify the searched code matches the code in the list", ferryPage.verifySRPContainsCode(code), is(true));

        step("Search The Covid Reason Data by Vendor Reason");
        ferryPage.searchFerryCovidReason(vendorReason);
        Assertion.assertThat("Verify the searched code matches the code in the list", ferryPage.verifySRPContainsVendorReason(vendorReason), is(true));

        step("Search The Covid Reason Data by Reason ID");
        ferryPage.searchFerryCovidReason(idReason);
        Assertion.assertThat("Verify the searched code matches the code in the list", ferryPage.verifySRPContainsReasonId(idReason), is(true));

        step("Search The Covid Reason Data by Reason EN");
        ferryPage.searchFerryCovidReason(enReason);
        Assertion.assertThat("Verify the searched code matches the code in the list", ferryPage.verifySRPContainsReasonEn(enReason), is(true));
    }

    @Author(names = { "Adit" })
    @Module(name = "CovidReason")
    @Tribe(name = "Ferry")
    @Vertical(name = "NFT")
    @Test
    public void testToggleActivateFerryCovidReason() {
        Map<String, String> param = getParam("testToggleActivateFerryCovidReason");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        FerryCovidReasonPage<?> ferryPage = new FerryCovidReasonPage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String activateMessage = param.get("activateMessage");
        String deactivateMessage = param.get("deactivateMessage");

        step("Login to Ferry Page");
        ferryPage.goToFerryPage(username, password);

        step("Open Covid Reason Tab");
        ferryPage.goToCovidReasonTab();

        step("Activate Covid Reason");
        ferryPage.toggleActiveCovidReason();
        Assertion.assertThat("Verify the success snackbar is displayed with correct description", ferryPage.isSuccessSnackbarDisplayedWithCorrectMessage(String.format(activateMessage)), is(true));

        step("Refresh The Page");
        ferryPage.refreshPage();

        step("Deactivate Covid Reason");
        ferryPage.toggleActiveCovidReason();
        Assertion.assertThat("Verify the success snackbar is displayed with correct description", ferryPage.isSuccessSnackbarDisplayedWithCorrectMessage(String.format(deactivateMessage)), is(true));
    }

    @Author(names = { "Adit" })
    @Module(name = "CovidReason")
    @Tribe(name = "Ferry")
    @Vertical(name = "NFT")
    @Test
    public void testEditFerryCovidReason() {
        Map<String, String> param = getParam("testEditFerryCovidReason");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        FerryCovidReasonPage<?> ferryPage = new FerryCovidReasonPage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String idReason = param.get("idReason");
        String enReason = param.get("enReason");
        String successMessage = param.get("successMessage");

        step("Login to Ferry Page");
        ferryPage.goToFerryPage(username, password);

        step("Open Covid Reason Tab");
        ferryPage.goToCovidReasonTab();

        step("Go To Edit Reason Page");
        ferryPage.goToEditTab();

        step("Edit Covid Reason");
        ferryPage.editCovidReason(idReason, enReason);
        Assertion.assertThat("Verify the success snackbar is displayed with correct description", ferryPage.isSuccessSnackbarDisplayedWithCorrectMessage(String.format(successMessage)), is(true));

        step("Search The Covid Reason Data by Reason ID");
        ferryPage.searchFerryCovidReason(idReason);
        Assertion.assertThat("Verify the searched Reason (ID) matches the Reason (ID) in the list", ferryPage.verifySRPContainsReasonId(idReason), is(true));

        step("Search The Covid Reason Data by Reason EN");
        ferryPage.searchFerryCovidReason(enReason);
        Assertion.assertThat("Verify the searched Reason (EN) matches the Reason (EN) in the list", ferryPage.verifySRPContainsReasonEn(enReason), is(true));
    }

    @Author(names = { "Adit" })
    @Module(name = "Route")
    @Tribe(name = "Ferry")
    @Vertical(name = "NFT")
    @Test
    public void testViewFerryRoute() {
        Map<String, String> param = getParam("testViewFerryRoute");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        FerryRoutePage<?> ferryPage = new FerryRoutePage<>(session);
        String username = param.get("username");
        String password = param.get("password");

        step("Login to Ferry Page");
        ferryPage.goToFerryPage(username, password);

        step("Open Route Tab");
        ferryPage.goToRouteTab();

        step("Check the result page");
        Assertion.assertThat("Verify that the srp is not empty", ferryPage.verifyTheSRPIsNotEmpty(), is(true));
    }

    @Author(names = { "Adit" , "Clement"})
    @Module(name = "Route")
    @Tribe(name = "Ferry")
    @Vertical(name = "NFT")
    @Test
    public void testAddNewFerryRoute() {
        Map<String, String> param = getParam("testAddNewFerryRoute");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        FerryRoutePage<?> ferryPage = new FerryRoutePage<>(session);
        String username = param.get("username");
        String password = param.get("password");

        String successCreateRouteMessage = param.get("successCreateRouteMessage");
        int index = Integer.parseInt(param.get("index"));
        int altIndex = Integer.parseInt(param.get("altIndex"));
        String duration = param.get("duration");

        step("Login to Ferry Page");
        ferryPage.goToFerryPage(username, password);

        step("Open Route Tab");
        ferryPage.goToRouteTab();

        step("Click Add Route Button");
        ferryPage.goToAddRoutePage();

        step("Fill All Mandatory Fields");
        ferryPage.addNewRoute(index, altIndex, duration);

        step("Get The Route Name");
        String origin = ferryPage.getSelectedOrigin();

        step("Save The New Route");
        ferryPage.saveForm();
        Assertion.assertThat("Verify the success creation class data snackbar is displayed", ferryPage.isSuccessSnackbarDisplayedWithCorrectMessage(String.format(successCreateRouteMessage)), is(true));

        step("Check The First Item in SRP");
        Assertion.assertThat("Verify the created class data is at the top of the Route menu list", ferryPage.verifyRouteIsAtTheTop(origin), is(true));

        step("Search The Route Data");
        ferryPage.searchFerryRoute(origin);
        Assertion.assertThat("Verify the created class data can be searched", ferryPage.verifySRPContainsRouteOrigin(origin), is(true));
    }

    @Author(names = { "Adit" , "Clement" })
    @Module(name = "Route")
    @Tribe(name = "Ferry")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testAddNewFerryRoute")
    public void testSearchFerryRoute() {
        Map<String, String> param = getParam("testSearchFerryRoute");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        FerryRoutePage<?> ferryPage = new FerryRoutePage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String origin = param.get("origin");
        String destination = param.get("destination");
        step("Login to Ferry Page");
        ferryPage.goToFerryPage(username, password);

        step("Open Route Tab");
        ferryPage.goToRouteTab();

        step("Search The Route Data by Origin");
        ferryPage.searchFerryRoute(origin);
        Assertion.assertThat("Verify the searched origin matches the origin in the list", ferryPage.verifySRPContainsRouteOrigin(origin), is(true));

        step("Search The Route Data by Destination");
        ferryPage.searchFerryRoute(destination);
        Assertion.assertThat("Verify the searched destination matches the destination in the list", ferryPage.verifySRPContainsRouteDestination(destination), is(true));
    }

    @Author(names = { "Adit" , "Clement" })
    @Module(name = "Route")
    @Tribe(name = "Ferry")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testSearchFerryRoute")
    public void testEditFerryRoute() {
        Map<String, String> param = getParam("testEditFerryRoute");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        FerryRoutePage<?> ferryPage = new FerryRoutePage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        int index = Integer.parseInt(param.get("index"));
        int altIndex = Integer.parseInt(param.get("altIndex"));
        String duration = param.get("duration");
        String successEditRouteMessage = param.get("successEditRouteMessage");

        step("Login to Ferry Page");
        ferryPage.goToFerryPage(username, password);

        step("Open Route Tab");
        ferryPage.goToRouteTab();

        step("Click Edit Route Button");
        ferryPage.goToEditRoutePage();

        step("Edit The Fields");
        ferryPage.editRoute(index, altIndex, duration);

        step("Get The Route Names");
        String origin = ferryPage.getSelectedOrigin();
        String destination = ferryPage.getSelectedDestination();

        step("Save The Form");
        ferryPage.saveForm();
        Assertion.assertThat("Verify the success edit snackbar is displayed", ferryPage.isSuccessSnackbarDisplayedWithCorrectMessage(String.format(successEditRouteMessage)), is(true));

        step("Search The Route Data by Origin");
        ferryPage.searchFerryRoute(origin);
        Assertion.assertThat("Verify the Origin have been changed and matched the edited version", ferryPage.verifySRPContainsRouteOrigin(origin), is(true));

        step("Search The Route Data by Destination");
        ferryPage.searchFerryRoute(destination);
        Assertion.assertThat("Verify the Destination have been changed and matched the edited version", ferryPage.verifySRPContainsRouteDestination(destination), is(true));
    }

    @Author(names = { "Adit" })
    @Module(name = "Route")
    @Tribe(name = "Ferry")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testEditFerryRoute")
    public void testToggleActivateFerryRoute() {
        Map<String, String> param = getParam("testToggleActivateFerryRoute");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        FerryRoutePage<?> ferryPage = new FerryRoutePage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String origin = param.get("origin");
        String activateMessage = param.get("activateMessage");
        String deactivateMessage = param.get("deactivateMessage");

        step("Login to Ferry Page");
        ferryPage.goToFerryPage(username, password);

        step("Open Route Tab");
        ferryPage.goToRouteTab();

        step("Search The Route Data by Origin");
        ferryPage.searchFerryRoute(origin);

        step("Activate Route");
        ferryPage.toggleActiveRoute();
        Assertion.assertThat("Verify the success snackbar is displayed with correct description", ferryPage.isSuccessSnackbarDisplayedWithCorrectMessage(String.format(activateMessage)), is(true));

        step("Deactivate Route");
        ferryPage.toggleActiveRoute();
        Assertion.assertThat("Verify the success snackbar is displayed with correct description", ferryPage.isSuccessSnackbarDisplayedWithCorrectMessage(String.format(deactivateMessage)), is(true));
    }

    @Author(names = { "Adit" })
    @Module(name = "Route")
    @Tribe(name = "Ferry")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testToggleActivateFerryRoute")
    public void testDeleteFerryRoute() {
        Map<String, String> param = getParam("testDeleteFerryRoute");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        FerryRoutePage<?> ferryPage = new FerryRoutePage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String origin = param.get("origin");
        String successDeleteRouteMessage = param.get("successDeleteRouteMessage");

        step("Login to Ferry Page");
        ferryPage.goToFerryPage(username, password);

        step("Open Route Tab");
        ferryPage.goToRouteTab();

        step("Search The Route Data by Origin");
        ferryPage.searchFerryRoute(origin);

        step("Get The Route ID");
        String objectId = ferryPage.getRouteObjectId();

        step("Click Delete Route Button");
        ferryPage.deleteRoute();
        Assertion.assertThat("Verify the success deletion snackbar is displayed", ferryPage.isSuccessSnackbarDisplayedWithCorrectMessage(String.format(successDeleteRouteMessage, origin)), is(true));

        step("Search The Route Data");
        ferryPage.searchFerryRoute(origin);
        Assertion.assertThat("Verify the deleted route can not be searched", ferryPage.verifyTheSRPIsNotEmpty(), is(false));
    }

    @Author(names = { "Adit" })
    @Module(name = "Facility")
    @Tribe(name = "Ferry")
    @Vertical(name = "NFT")
    @Test
    public void testViewFerryFacility() {
        Map<String, String> param = getParam("testViewFerryFacility");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        FerryFacilityPage<?> ferryPage = new FerryFacilityPage<>(session);
        String username = param.get("username");
        String password = param.get("password");

        step("Login to Ferry Page");
        ferryPage.goToFerryPage(username, password);

        step("Open Facility Tab");
        ferryPage.goToFacilityTab();

        step("Check the result page");
        Assertion.assertThat("Verify that the srp is not empty", ferryPage.verifyTheSRPIsNotEmpty(), is(true));
    }

    @Author(names = { "Adit" , "Clement" })
    @Module(name = "Facility")
    @Tribe(name = "Ferry")
    @Vertical(name = "NFT")
    @Test
    public void testAddFerryFacility() {
        Map<String, String> param = getParam("testAddFerryFacility");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        FerryFacilityPage<?> ferryPage = new FerryFacilityPage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String label = param.get("label");
        String idName = param.get("idName");
        String enName = param.get("enName");
        String priorityScore = param.get("priorityScore");
        String filePath = param.get("filePath");

        step("Login to Ferry Page");
        ferryPage.goToFerryPage(username, password);

        step("Open Facility Tab");
        ferryPage.goToFacilityTab();

        step("Go To Add Facility Page");
        ferryPage.goToAddFacilityPage();

        step("Add Images");
        ferryPage.addFacilityImages(filePath);
        Assertion.assertThat("Verify the success uploaded image snackbar is displayed", ferryPage.isSuccessSnackbarDisplayed(), is(true));

        step("Close Previous Snackbar");
        ferryPage.waitForSnackbarToDisappear();

        step("Fill The Mandatory Fields");
        ferryPage.addNewFacility(label, priorityScore, idName, enName);

        step("Save The New Facility");
        ferryPage.saveForm();
        Assertion.assertThat("Verify the success snackbar is displayed", ferryPage.isSuccessSnackbarDisplayed(), is(true));

        step("Close Previous Snackbar");
        ferryPage.waitForSnackbarToDisappear();

        step("Search The Facility Data");
        ferryPage.searchFerryFacility(idName);
        Assertion.assertThat("Verify the created facility data can be searched", ferryPage.verifySRPContainsFacilityLabel(label), is(true));
    }

    @Author(names = { "Adit" })
    @Module(name = "Facility")
    @Tribe(name = "Ferry")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testAddFerryFacility")
    public void testSearchFerryFacility() {
        Map<String, String> param = getParam("testSearchFerryFacility");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        FerryFacilityPage<?> ferryPage = new FerryFacilityPage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String label = param.get("label");
        String idName = param.get("idName");
        String enName = param.get("enName");
        step("Login to Ferry Page");
        ferryPage.goToFerryPage(username, password);

        step("Open Facility Tab");
        ferryPage.goToFacilityTab();

        step("Search The Facility Data by Label");
        ferryPage.searchFerryFacility(label);
        Assertion.assertThat("Verify the searched label matches the label in the list", ferryPage.verifySRPContainsFacilityLabel(label), is(true));

        step("Search The Facility Data by ID Name");
        ferryPage.searchFerryFacility(idName);
        Assertion.assertThat("Verify the searched name (ID) matches the name (ID) in the list", ferryPage.verifySRPContainsFacilityIdName(idName), is(true));

        step("Search The Facility Data by EN Name");
        ferryPage.searchFerryFacility(enName);
        Assertion.assertThat("Verify the searched name (EN) matches the name (EN) in the list", ferryPage.verifySRPContainsFacilityEnName(enName), is(true));
    }

    @Author(names = { "Adit" })
    @Module(name = "Facility")
    @Tribe(name = "Ferry")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testSearchFerryFacility")
    public void testEditFerryFacility() {
        Map<String, String> param = getParam("testEditFerryFacility");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        FerryFacilityPage<?> ferryPage = new FerryFacilityPage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String label = param.get("label");
        String idName = param.get("idName");
        String enName = param.get("enName");

        step("Login to Ferry Page");
        ferryPage.goToFerryPage(username, password);

        step("Open Facility Tab");
        ferryPage.goToFacilityTab();

        step("Search The Facility Data by Label");
        ferryPage.searchFerryFacility(label);

        step("Click Edit Facility Button");
        ferryPage.goToEditFacilityPage();

        step("Edit The Fields");
        ferryPage.editFerryFacility(idName, enName);
        Assertion.assertThat("Verify the success edit snackbar is displayed", ferryPage.isSuccessSnackbarDisplayed(), is(true));

        step("Search The Facility Data by ID Name");
        ferryPage.searchFerryFacility(idName);
        Assertion.assertThat("Verify the Name (ID) have been changed and matched the edited version", ferryPage.verifySRPContainsFacilityIdName(idName), is(true));

        step("Search The Facility Data by EN Name");
        ferryPage.searchFerryFacility(enName);
        Assertion.assertThat("Verify the Name (EN) have been changed and matched the edited version", ferryPage.verifySRPContainsFacilityEnName(enName), is(true));
    }

    @Author(names = { "Adit" })
    @Module(name = "Facility")
    @Tribe(name = "Ferry")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testEditFerryFacility")
    public void testToggleActivateFerryFacility() {
        Map<String, String> param = getParam("testToggleActivateFerryFacility");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        FerryFacilityPage<?> ferryPage = new FerryFacilityPage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String label = param.get("label");

        step("Login to Ferry Page");
        ferryPage.goToFerryPage(username, password);

        step("Open Facility Tab");
        ferryPage.goToFacilityTab();

        step("Search The Facility Data by Label");
        ferryPage.searchFerryFacility(label);

        step("Activate Facility");
        ferryPage.toggleActiveFacility();
        Assertion.assertThat("Verify the success snackbar is displayed with correct description", ferryPage.isSuccessSnackbarDisplayed(), is(true));

        step("Deactivate Facility");
        ferryPage.toggleActiveFacility();
        Assertion.assertThat("Verify the success snackbar is displayed with correct description", ferryPage.isSuccessSnackbarDisplayed(), is(true));
    }

    @Author(names = { "Adit" })
    @Module(name = "Facility")
    @Tribe(name = "Ferry")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testToggleActivateFerryFacility")
    public void testDeleteFerryFacility() {
        Map<String, String> param = getParam("testDeleteFerryFacility");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        FerryFacilityPage<?> ferryPage = new FerryFacilityPage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String label = param.get("label");

        step("Login to Ferry Page");
        ferryPage.goToFerryPage(username, password);

        step("Open Facility Tab");
        ferryPage.goToFacilityTab();

        step("Search The Facility Data by Label");
        ferryPage.searchFerryFacility(label);

        step("Get The Facility ID");
        String id = ferryPage.getFacilityId();

        step("Click Delete Facility Button");
        ferryPage.deleteFacility();
        Assertion.assertThat("Verify the success deletion snackbar is displayed", ferryPage.isSuccessSnackbarDisplayed(), is(true));

        step("Search The Facility Data");
        ferryPage.searchFerryFacility(label);
        Assertion.assertThat("Verify the deleted class can not be searched", ferryPage.verifyTheSRPIsNotEmpty(), is(false));
    }
}