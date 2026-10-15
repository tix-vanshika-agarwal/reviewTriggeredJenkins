package com.tiket.web.dashboard.test.platform.membership;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Session;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.platform.membership.TableMemberPage;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.testdata.TestData.getParam;

public class TableMemberTest extends BaseTest {
    @Author(names = {"Yanuar"})
    @Module(name = "TableMember")
    @Tribe(name = "Membership")
    @Vertical(name = "Platform")
    @Test
    public void testAdminDeactivateUser() {
        Map<String, String> sheetData = getParam("testAdminDeactivateUser");
        Session<?> session = BaseTest.sessionThreadLocal.get();
        TableMemberPage<?> tableMemberPage = new TableMemberPage<>(session);
        tableMemberPage.setSheetData(sheetData);

        step("Given admin go to Table Member Dashboard");
        final String username = sheetData.get("username");
        final String password = sheetData.get("password");
        tableMemberPage.goToTableMember(username, password);
        tableMemberPage.verifyTableMemberLoaded();

        step("And admin search user by email");
        tableMemberPage.searchUserByEmail(sheetData.get("userEmail"));

        step("When admin click on Deactivate");
        tableMemberPage.deactivateUser();

        step("Then admin should deactivate the user successfully");
        tableMemberPage.verifySuccessSnackbar(sheetData.get("successMsg"));
        tableMemberPage.verifyUserData();
    }
}
