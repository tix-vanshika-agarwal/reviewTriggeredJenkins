package com.tiket.web.dashboard.test.platform.membership;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.platform.membership.PrivilegeManagementPage;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.equalToIgnoringCase;

public class PrivilegeManagementTest extends BaseTest {
    @Author(names = {"Yanuar"})
    @Module(name = "PrivilegeManagement")
    @Tribe(name = "Membership")
    @Vertical(name = "Platform")
    @Test
    public void testAdminCreatePrivilege() {
        Map<String, String> sheetData = getParam("testAdminCreatePrivilege");
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PrivilegeManagementPage<?> privilegeManagementPage = new PrivilegeManagementPage<>(session);
        privilegeManagementPage.setSheetData(sheetData);

        step("Given admin go to privilege management page");
        String username = sheetData.get("username");
        String password = sheetData.get("password");
        privilegeManagementPage.goToPrivilegeManagement(username, password);
        privilegeManagementPage.verifyPrivilegeManagementPageLoaded();

        step("When admin click Add New Privilege Button");
        privilegeManagementPage.clickPrivilegeButton();

        step("And input all the mandatory forms and click Add Privilege");
        privilegeManagementPage.inputAllFormsAndSubmit();

        step("Then admin should successfully add new privilege");
        privilegeManagementPage.verifySuccessSnackbar();
        privilegeManagementPage.verifyPrivDataCreated();
        Map<String, String> privData = privilegeManagementPage.getPrivData();

        Assertion.assertThat("Verify ID is correct", privData.get("id"), equalToIgnoringCase(sheetData.get("id")));
        Assertion.assertThat("Verify Name is correct", privData.get("name"), equalToIgnoringCase(sheetData.get("name")));
        Assertion.assertThat("Verify Description is correct", privData.get("description"), equalToIgnoringCase(sheetData.get("description")));
        Assertion.assertThat("Verify Url is correct", privData.get("url"), equalToIgnoringCase(sheetData.get("url").replace("#", "/")));
        Assertion.assertThat("Verify Sequence is 0", privData.get("sequence"), equalToIgnoringCase("0"));

        step("Delete created privilege");
        privilegeManagementPage.deletePriv();
    }
}
