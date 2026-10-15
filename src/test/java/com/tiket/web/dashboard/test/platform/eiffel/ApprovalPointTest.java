package com.tiket.web.dashboard.test.platform.eiffel;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
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

public class ApprovalPointTest extends BaseTest {

    @Author(names = { "Saurabh" })
    @Module(name = "Loyalty")
    @Tribe(name = "Eiffel")
    @Vertical(name = "Platform")
    @Test
    public void approveInjectPoints() throws Exception {
        Map<String, String> param = getParam("approveInjectPoints");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoyaltyPointsPage<?> loyaltyPointsPage = new LoyaltyPointsPage<>(session);
        InjectPointPage<?> injectPointPage = new InjectPointPage<>(session);
        ApprovalPointPage<?> approvalPointPage = new ApprovalPointPage<>(session);
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

        step("Check the DB Data");
        Assertion.assertThat("account id should be same", approvalPointPage.verifyContains(accountId, account_id), is(true));
        Assertion.assertThat("task type should be same", approvalPointPage.verifyContains(type, taskType), is(true));
        Assertion.assertThat("task status should be same", approvalPointPage.verifyContains(status, taskStatus), is(true));
        Assertion.assertThat("process type should be same", approvalPointPage.verifyContains(processType, process_type), is(true));
    }

    @Author(names = { "Saurabh" })
    @Module(name = "Loyalty")
    @Tribe(name = "Eiffel")
    @Vertical(name = "Platform")
    @Test
    public void rejectInjectPoints() throws Exception {
        Map<String, String> param = getParam("rejectInjectPoints");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoyaltyPointsPage<?> loyaltyPointsPage = new LoyaltyPointsPage<>(session);
        InjectPointPage<?> injectPointPage = new InjectPointPage<>(session);
        ApprovalPointPage<?> approvalPointPage = new ApprovalPointPage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String account_id = param.get("account_id");
        String process_type = param.get("process_type");
        String points = param.get("points");
        String taskType = param.get("taskType");

        step("Open the Admin Care Page");
        loyaltyPointsPage.goToUserManagementPage(username, password);

        step("Open the Loyalty Page");
        loyaltyPointsPage.clickLoyaltyIcon();

        step("Open the Inject Point Page");
        injectPointPage.injectpointsdashboard();

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
        approvalPointPage.clickRejectIcon();

        step("Get the DB Data");
        Map<String,Object> map = injectPointPage.getDataDB(account_id);
        String accountId = String.valueOf(map.get("account_id"));
        String task_type =  String.valueOf(map.get("task_type"));
        String processType =  String.valueOf(map.get("process_type"));
        String amount =  String.valueOf(map.get("amount"));

        step("Check the DB Data");
        Assertion.assertThat("account id should be same", injectPointPage.verifyContains(accountId, account_id), is(true));
        Assertion.assertThat("task type should be same", injectPointPage.verifyContains(task_type, taskType), is(true));
        Assertion.assertThat("process type should be same", injectPointPage.verifyContains(processType, process_type), is(true));
        Assertion.assertThat("amount should be same", injectPointPage.verifyContains(amount, points), is(true));
    }

    @Author(names = { "Saurabh" })
    @Module(name = "Loyalty")
    @Tribe(name = "Eiffel")
    @Vertical(name = "Platform")
    @Test
    public void approveDeletedPoints() throws Exception {
        Map<String, String> param = getParam("approveDeletedPoints");
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
        Assertion.assertThat("amount should be same", approvalPointPage.verifyContains(amount, points), is(true));

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

        step("Click On Delete Icon");
        deletePointPage.clickOnDeleteIcon();

        step("Click On Submit Icon");
        deletePointPage.clickOnSubmitIcon();

        step("Proceed to the Approval Page");
        approvalPointPage.openApprovalPage();

        step("Approve the Points Page");
        approvalPointPage.clickApprovalButton();

        step("Check the success message");
        Assertion.assertThat("check the success message",
                approvalPointPage.successMessage("REQUEST SUCCESSFULLY APPROVED"), is(true));
    }

    @Author(names = { "Saurabh" })
    @Module(name = "Loyalty")
    @Tribe(name = "Eiffel")
    @Vertical(name = "Platform")
    @Test
    public void rejectDeletedPoints() throws Exception {
        Map<String, String> param = getParam("rejectDeletedPoints");
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

        step("Click On Delete Icon");
        deletePointPage.clickOnDeleteIcon();

        step("Click On Submit Icon");
        deletePointPage.clickOnSubmitIcon();

        step("Proceed to the Approval Page");
        approvalPointPage.openApprovalPage();

        step("Approve the Points Page");
        approvalPointPage.clickRejectIcon();

        step("Check the success message");
        Assertion.assertThat("check the success message",
                approvalPointPage.rejectedMessage("REQUEST SUCCESSFULLY REJECTED"), is(true));
    }
}
