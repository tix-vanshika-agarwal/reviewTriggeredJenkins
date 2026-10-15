package com.tiket.web.dashboard.test.platform.eiffel;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.apiPage.LoyaltyApi;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.platform.eiffel.loyalty.ApprovalPointPage;
import com.tiket.web.dashboard.page.platform.eiffel.loyalty.DeletePointPage;
import com.tiket.web.dashboard.page.platform.eiffel.loyalty.InjectPointPage;
import com.tiket.web.dashboard.page.platform.eiffel.loyalty.LoyaltyPointsPage;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.core.Is.is;

public class DeletePointTest extends BaseTest {

    @Author(names = { "Saurabh" })
    @Module(name = "Loyalty")
    @Tribe(name = "Eiffel")
    @Vertical(name = "Platform")
    @Test
    public void checkDeletePoints() throws Exception {
        Map<String, String> param = getParam("checkDeletePoints");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoyaltyPointsPage<?> loyaltyPointsPage = new LoyaltyPointsPage<>(session);
        InjectPointPage<?> injectPointPage = new InjectPointPage<>(session);
        ApprovalPointPage<?> approvalPointPage = new ApprovalPointPage<>(session);
        DeletePointPage<?> deletePointPage = new DeletePointPage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String account_id = param.get("account_id");
        String process_type = param.get("process_type");
        String points = param.get("points");
        String taskType = param.get("taskType");
        String taskStatus = param.get("taskStatus");

        step("Open the Admin Care Page");
        loyaltyPointsPage.goToUserManagementPage(username, password);

        step("Open the Loyalty Page");
        loyaltyPointsPage.clickLoyaltyIcon();

        step("Open the Inject Point Page");
        injectPointPage.injectpointsdashboard();

        Thread.sleep(1000);
        step("Open the Inject Point Page");
        injectPointPage.addAccountId(account_id);

        step("Select the Category");
        injectPointPage.selectCategory(process_type);

        step("Add the Points");
        injectPointPage.addPoint(points);

        step("Click on the Submit Icon");
        injectPointPage.submitIcon();

        step("Click on the Submit Icon");
        injectPointPage.clickOnOk();

        step("Check the success message");
        Assertion.assertThat("check the success message",
                injectPointPage.successMessage("Successfully Inject Point to Approval Page"), is(true));

        step("Proceed to the Approval Page");
        approvalPointPage.openApprovalPage();

        step("Approve the Points Page");
        approvalPointPage.clickApprovalButton();

        step("Get the DB Data");
        Map<String,Object> map = approvalPointPage.getDataDB(account_id);
        String accountId = String.valueOf(map.get("account_id"));
        String type =  String.valueOf(map.get("type"));
        String processType =  String.valueOf(map.get("process_type"));
        String status =  String.valueOf(map.get("status"));
        String amount =  String.valueOf(map.get("amount"));

        step("Check the DB Data");
        Assertion.assertThat("account id should be same", approvalPointPage.verifyContains(accountId, account_id), is(true));
        Assertion.assertThat("task type should be same", approvalPointPage.verifyContains(type, taskType), is(true));
        Assertion.assertThat("task status should be same", approvalPointPage.verifyContains(status, taskStatus), is(true));
        Assertion.assertThat("process type should be same", approvalPointPage.verifyContains(processType, process_type), is(true));

        step("Open Delete Point Page");
        deletePointPage.openDeletePointsPage();

        step("Select UserName & Point Type");
        deletePointPage.selectUserName();

        step("Enter the User Name");
        String userEmail = param.get("useremail");
        deletePointPage.enterUserName(userEmail);

        step("Select Reward Point Type");
        deletePointPage.selectPointType();

        step("Click On Search");
        deletePointPage.clickOnSearch();

        Assertion.assertThat("process type should be same", deletePointPage.getProcessType(process_type), is(true));
        Assertion.assertThat("Amount should be same", deletePointPage.Amount(points), is(true));
    }

    @Author(names = {"Yanuar"})
    @Module(name = "Loyalty")
    @Tribe(name = "Eiffel")
    @Vertical(name = "Platform")
    @Test
    public void testVerifyDeletePoint() throws Exception {
        Map<String, String> param = getParam("testVerifyDeletePoint");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoyaltyPointsPage<?> loyaltyPointsPage = new LoyaltyPointsPage<>(session);
        DeletePointPage<?> deletePointPage = new DeletePointPage<>(session);
        ApprovalPointPage<?> approvalPointPage = new ApprovalPointPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String targetEmail = param.get("targetEmail");
        String targetAccountId = param.get("targetAccountId");
        String processType = param.get("processType");
        String points = param.get("points");
        String apiUsername = param.get("apiUsername");

        step("Precondition: Inject REWARD points via API");
        loyaltyPointsPage.injectPointApi(targetAccountId, points, processType, apiUsername);

        step("Open the Admin Care Page");
        loyaltyPointsPage.goToUserManagementPage(username, password);

        step("Open the Loyalty Page");
        loyaltyPointsPage.clickLoyaltyIcon();

        step("Open Delete Point Page");
        deletePointPage.openDeletePointsPage();

        step("Select Username & Point Type search criteria");
        deletePointPage.selectUserName();

        step("Enter the target user email");
        deletePointPage.enterUserName(targetEmail);

        step("Select REWARD Point Type");
        deletePointPage.selectPointType();

        step("Click Search");
        deletePointPage.clickOnSearch();

        step("Verify REWARD points are displayed for target user");
        Assertion.assertThat("REWARD point type should be displayed",
                deletePointPage.getProcessType(processType), is(true));

        step("Click Delete on the last item in the list");
        deletePointPage.clickOnDeleteIcon();

        step("Add note for deletion");
        deletePointPage.addNote("Automation test - delete point");

        step("Click Submit");
        deletePointPage.clickOnSubmitIcon();

        step("Verify success snackbar is shown");
        Assertion.assertThat("Success snackbar should be displayed",
                deletePointPage.isSuccessSnackbarDisplayed(), is(true));

        step("Navigate to Approval List");
        approvalPointPage.openApprovalPage();

        step("Verify the DELETE entry for account " + targetAccountId + " is in the approval list");
        Assertion.assertThat("DELETE entry for account " + targetAccountId + " should appear in approval list",
                approvalPointPage.isDeleteEntryPresentForAccountId(targetAccountId), is(true));

        int countBefore = approvalPointPage.countDeleteEntriesForAccountId(targetAccountId);

        step("Click Approval for DELETE entry of account " + targetAccountId);
        approvalPointPage.clickApprovalButtonForDeleteEntry(targetAccountId);

        step("Verify success snackbar after approval");
        Assertion.assertThat("Approval success message should be displayed",
                approvalPointPage.successMessage("REQUEST SUCCESSFULLY APPROVED"), is(true));

        step("Wait for snackbar to disappear and reload the page");
        approvalPointPage.waitForSnackbarToDisappearAndReload();

        step("Verify the approved DELETE entry has been removed from the list");
        int countAfter = approvalPointPage.countDeleteEntriesForAccountId(targetAccountId);
        Assertion.assertThat("DELETE entries count should decrease by 1 after approval",
                countAfter, is(countBefore - 1));
    }
}
