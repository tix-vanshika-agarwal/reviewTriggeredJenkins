package com.tiket.web.dashboard.test.platform.membership;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.platform.membership.UserManagementPage;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.testdata.TestData.getParam;

public class UserManagementTest extends BaseTest {
    @Author(names = {"Yanuar"})
    @Module(name = "UserManagement")
    @Tribe(name = "Membership")
    @Vertical(name = "Platform")
    @Test
    public void testCreateAdmin() throws InterruptedException {
        Map<String, String> sheetData = getParam("testCreateAdmin");
        UserManagementPage<?> userManagementPage = new UserManagementPage<>(BaseTest.sessionThreadLocal.get());
        userManagementPage.setSheetData(sheetData);

        step("Given admin go to User Management Page");
        userManagementPage.goToUserManagementPage();

        step("When admin click on Add new user");
        userManagementPage.clickAddNewUser();

        step("And admin input all the fields and submit");
        userManagementPage.inputAllAdminFields();

        step("Then admin should see success snackbar");
        userManagementPage.verifySuccessSnackBar();

        step("And newly created admin data are correct");
        userManagementPage.verifyNewAdminData();
        userManagementPage.tryToDeleteAdmin(sheetData.get("adminEmail"));
    }

    @Author(names = {"Yanuar"})
    @Module(name = "UserManagement")
    @Tribe(name = "Membership")
    @Vertical(name = "Platform")
    @Test
    public void testSearchAdmin() throws InterruptedException {
        Map<String, String> sheetData = getParam("testSearchAdmin");
        UserManagementPage<?> userManagementPage = new UserManagementPage<>(BaseTest.sessionThreadLocal.get());
        userManagementPage.setSheetData(sheetData);

        step("Given admin go to User Management Page");
        userManagementPage.goToUserManagementPage();

        step("When admin search user by email");
        userManagementPage.searchUserByType("email", sheetData.get("email"));

        step("Then search result should be correct");
        userManagementPage.verifyAfterSearchUser();

        step("When admin search user by User Id");
        userManagementPage.searchUserByType("userid", sheetData.get("userId"));

        step("Then search result should be correct as well");
        userManagementPage.verifyAfterSearchUser();
    }

    @Author(names = {"Yanuar"})
    @Module(name = "UserManagement")
    @Tribe(name = "Membership")
    @Vertical(name = "Platform")
    @Test
    public void testAdminForgotPasswordAutomatic() throws InterruptedException {
        Map<String, String> sheetData = getParam("testAdminForgotPasswordAutomatic");
        UserManagementPage<?> userManagementPage = new UserManagementPage<>(BaseTest.sessionThreadLocal.get());
        userManagementPage.setSheetData(sheetData);

        step("Given admin go to User Management Page");
        userManagementPage.goToUserManagementPage();

        step("And admin search user by email");
        userManagementPage.searchUserByType("email", sheetData.get("email"));
        userManagementPage.verifyAfterSearchUser();

        step("When admin click change password");
        userManagementPage.clickChangePassword();

        step("And admin choose automatic");
        userManagementPage.chooseChangePasswordBy(sheetData.get("changeBy"));

        step("Then Super Admin should succeed forgot password admin");
        userManagementPage.verifySuccessChangePassword();
    }

    @Author(names = {"Yanuar"})
    @Module(name = "UserManagement")
    @Tribe(name = "Membership")
    @Vertical(name = "Platform")
    @Test
    public void testAdminSuspendUser() {
        Map<String, String> sheetData = getParam("testAdminSuspendUser");
        UserManagementPage<?> userManagementPage = new UserManagementPage<>(BaseTest.sessionThreadLocal.get());
        userManagementPage.setSheetData(sheetData);

        step("Given admin go to User Management Page");
        userManagementPage.goToUserManagementPage();

        step("And admin search user by email");
        userManagementPage.searchUserByType("email", sheetData.get("email"));
        userManagementPage.verifyAfterSearchUser();

        step("When super admin click suspend and click CTA yes on confirmation pop up");
        userManagementPage.adminSuspendUser();

        step("Then successfully suspend admin");
        userManagementPage.verifySuccessSuspend();
    }

    @Author(names = {"Yanuar"})
    @Module(name = "UserManagement")
    @Tribe(name = "Membership")
    @Vertical(name = "Platform")
    @Test
    public void testAdminUpdateRolePriv() throws InterruptedException {
        Map<String, String> sheetData = getParam("testAdminUpdateRolePriv");
        UserManagementPage<?> userManagementPage = new UserManagementPage<>(BaseTest.sessionThreadLocal.get());
        userManagementPage.setSheetData(sheetData);

        step("Given admin go to User Management Page");
        userManagementPage.goToUserManagementPage();

        step("And admin search user by email");
        userManagementPage.searchUserByType("userid", sheetData.get("userId"));
        userManagementPage.verifyAfterSearchUser();

        step("When admin click on manage role");
        userManagementPage.clickManageRole();

        step("And admin update the role and privilege");
        String[] roles = sheetData.get("roles").split("#");
        String[] privs = sheetData.get("privs").split("#");
        userManagementPage.updateRolePriv(roles, privs);

        step("Then admin should successfully update them");
        userManagementPage.verifySuccessUpdateRolePriv();
    }
}
