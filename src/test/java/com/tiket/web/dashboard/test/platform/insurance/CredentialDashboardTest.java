package com.tiket.web.dashboard.test.platform.insurance;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.platform.insurance.CredentialDashboardPage;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.CoreMatchers.is;

public class CredentialDashboardTest extends BaseTest {
    @Author(names = {"Yanuar"})
    @Module(name = "CredentialDashboard")
    @Tribe(name = "Insurance")
    @Vertical(name = "Platform")
    @Test
    public void testSearchByCredentialName() {
        Map<String, String> sheetData = getParam("testSearchByCredentialName");
        Session<?> session = BaseTest.sessionThreadLocal.get();
        CredentialDashboardPage<?> credentialDashboardPage = new CredentialDashboardPage<>(session);
        credentialDashboardPage.setSheetData(sheetData);

        step("Given admin go to Credential ");
        credentialDashboardPage.goToCredentialDashboardViaUI();
        credentialDashboardPage.verifyCredentialDashboardPage();

        step("When admin input Credential name and click Search");
        credentialDashboardPage.inputCredential(sheetData.get("credential"));

        step("Then admin should see the search result");
        credentialDashboardPage.getSearchResult();

        step("And admin can click details on one of the result and verify data");
        credentialDashboardPage.clickDetailsOnAResult();
    }

    @Author(names = {"Yanuar"})
    @Module(name = "CredentialDashboard")
    @Tribe(name = "Insurance")
    @Vertical(name = "Platform")
    @Test
    public void testAdminCreateCredential() {
        Map<String, String> sheetData = getParam("testAdminCreateCredential");
        Session<?> session = BaseTest.sessionThreadLocal.get();
        CredentialDashboardPage<?> credentialDashboardPage = new CredentialDashboardPage<>(session);
        credentialDashboardPage.setSheetData(sheetData);

        step("Given admin go to Credential ");
        credentialDashboardPage.goToCredentialDashboard();
        credentialDashboardPage.verifyCredentialDashboardPage();

        step("And admin search an insurance group");
        String insuranceGroupName = sheetData.get("insuranceGroupName");
        credentialDashboardPage.searchInsuranceGroup(insuranceGroupName);

        step("When admin click create credential");
        credentialDashboardPage.clickOnAddCredentialButton();

        step("And input all mandatory fields");
        credentialDashboardPage.inputMandatoryFields();

        step("Then admin should see the success message");
        Map<String, ?> toastData = credentialDashboardPage.getSuccessToastData();
        sheetData = credentialDashboardPage.getSheetData();

        Assertion.assertThat("Verify success toast is shown", toastData.get("isShown"), is(true));
        Assertion.assertThat("Verify success message is correct", toastData.get("successMsg"), is(sheetData.get("successMsg")));

        step("And admin could verify the credential created");
        credentialDashboardPage.verifyCredentialCreated();
        Map<String, String> actualData = credentialDashboardPage.getActualCredData();

        Assertion.assertThat("Verify Credential Name is correct", actualData.get("credentialName"), is(sheetData.get("credentialName")));
        Assertion.assertThat("Verify Partner Name is correct", actualData.get("partnerName"), is(sheetData.get("partnerName")));
        Assertion.assertThat("Verify Insurance Type is correct", actualData.get("insuranceType"), is(sheetData.get("insuranceType")));
        Assertion.assertThat("Verify Client ID is correct", actualData.get("clientId"), is(sheetData.get("clientId")));
        Assertion.assertThat("Verify Client Secret is correct", actualData.get("clientSecret"), is(sheetData.get("clientSecret")));
        Assertion.assertThat("Verify Client Code is correct", actualData.get("clientCode"), is(sheetData.get("clientCode")));
        Assertion.assertThat("Verify Variable Name is correct", actualData.get("variableName"), is(sheetData.get("variableName")));
        Assertion.assertThat("Verify Operator is correct", actualData.get("operator"), is(sheetData.get("operator")));
        Assertion.assertThat("Verify Value is correct", actualData.get("value"), is(sheetData.get("variableValue")));

        step("Lastly admin delete the credential to only leave '" + sheetData.get("totalInsurance") + "' left.");
        credentialDashboardPage.normalizingTotalInsurance(Integer.parseInt(sheetData.get("totalInsurance")));
    }
}
