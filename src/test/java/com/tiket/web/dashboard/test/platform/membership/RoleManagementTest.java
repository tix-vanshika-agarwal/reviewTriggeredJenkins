package com.tiket.web.dashboard.test.platform.membership;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;

import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.platform.membership.RoleManagementPage;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalToIgnoringCase;

public class RoleManagementTest extends BaseTest {
    @Author(names = {"Yanuar"})
    @Module(name = "RoleManagement")
    @Tribe(name = "Membership")
    @Vertical(name = "Platform")
    @Test
    public void testAdminCreateRole() {
        Map<String, String> sheetData = getParam("testAdminCreateRole");
        Session<?> session = BaseTest.sessionThreadLocal.get();
        RoleManagementPage<?> roleManagementPage = new RoleManagementPage<>(session);
        roleManagementPage.setSheetData(sheetData);

        step("Given admin go to role management page");
        String username = sheetData.get("username");
        String password = sheetData.get("password");
        roleManagementPage.goToRoleManagementPage(username, password);

        step("When admin click Add New Role Button");
        roleManagementPage.clickAddNewRoleButton();

        step("And input all the mandatory forms and click Add Role");
        roleManagementPage.inputAllFormsAndSubmit();

        step("Then admin should successfully add new role");
        sheetData = roleManagementPage.getSheetData();
        Map<?, ?> snackbarData = roleManagementPage.getSnackbarData();
        Assertion.assertThat("Verify Snackbar success is appeared", snackbarData.get("visibility"), is(true));
        Assertion.assertThat("Verify Snackbar text is correct", snackbarData.get("snackbarText"), is(sheetData.get("successMsg")));

        step("And the new role can be verified");
        Map<String, String> roleData = roleManagementPage.getCreatedRoleData();

        Assertion.assertThat("Verify ID is correct", roleData.get("id"), equalToIgnoringCase(sheetData.get("id")));
        Assertion.assertThat("Verify Name is correct", roleData.get("name"), equalToIgnoringCase(sheetData.get("name")));
        Assertion.assertThat("Verify Description is correct", roleData.get("description"), equalToIgnoringCase(sheetData.get("description")));

        step("Delete created role");
        roleManagementPage.deleteRole();
    }
}
