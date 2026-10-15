package com.tiket.web.dashboard.test.platform.eiffel;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.platform.eiffel.loyalty.ApprovalPointPage;
import com.tiket.web.dashboard.page.platform.eiffel.loyalty.InjectPointPage;
import com.tiket.web.dashboard.page.platform.eiffel.loyalty.LoyaltyPointsPage;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;

import static com.tiket.core.CoreFunctions.*;
import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.core.Is.is;

public class InjectPointTest extends BaseTest {

    @Author(names = { "Saurabh" })
    @Module(name = "Loyalty")
    @Tribe(name = "Eiffel")
    @Vertical(name = "Platform")
    @Test
    public void injectPointsReward() throws Exception {
        Map<String, String> param = getParam("injectPointsReward");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoyaltyPointsPage<?> loyaltyPointsPage = new LoyaltyPointsPage<>(session);
        InjectPointPage<?> injectPointPage = new InjectPointPage<>(session);
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
                injectPointPage.successMessage("Successfully Inject Point to Approval Page"),is(true));

        Map<String,Object> map = injectPointPage.getDataDB(account_id);
        String accountId = String.valueOf(map.get("account_id"));
        String task_type =  String.valueOf(map.get("task_type"));
        String processType =  String.valueOf(map.get("process_type"));
        String task_status =  String.valueOf(map.get("task_status"));
        String amount =  String.valueOf(map.get("amount"));

        Assertion.assertThat("account id should be same", injectPointPage.verifyContains(accountId, account_id), is(true));
        Assertion.assertThat("task type should be same", injectPointPage.verifyContains(task_type, taskType), is(true));
        Assertion.assertThat("task status should be same", injectPointPage.verifyContains(task_status, taskStatus), is(true));
        Assertion.assertThat("process type should be same", injectPointPage.verifyContains(processType, process_type), is(true));
        Assertion.assertThat("amount should be same", injectPointPage.verifyContains(amount, points), is(true));
    }

    @Author(names = { "Saurabh" })
    @Module(name = "Loyalty")
    @Tribe(name = "Eiffel")
    @Vertical(name = "Platform")
    @Test
    public void injectPointsRefund() throws Exception {
        Map<String, String> param = getParam("injectPointsRefund");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoyaltyPointsPage<?> loyaltyPointsPage = new LoyaltyPointsPage<>(session);
        InjectPointPage<?> injectPointPage = new InjectPointPage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String account_id = param.get("account_id");
        String process_type = param.get("process_type");
        String orderId = String.valueOf(CoreFunctions.generateRandomInt(12));
        String referenceId = String.valueOf(CoreFunctions.generateRandomInt(10));
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

        step("Add Order Id");
        injectPointPage.addOrderId(orderId);

        step("Add Reference ID");
        injectPointPage.addReferenceId(referenceId);

        step("Add the Points");
        injectPointPage.addPoint(points);

        step("Click on the Submit Icon");
        injectPointPage.submitIcon();

        step("Click on the Submit Icon");
        injectPointPage.clickOnOk();

        step("Check the success message");
        Assertion.assertThat("check the success message",
                injectPointPage.successMessage("Successfully Inject Point to Approval Page"),is(true));

        step("Get the DB Data");
        Map<String,Object> map = injectPointPage.getDataDB(account_id);
        String accountId = String.valueOf(map.get("account_id"));
        String task_type =  String.valueOf(map.get("task_type"));
        String processType =  String.valueOf(map.get("process_type"));
        String task_status =  String.valueOf(map.get("task_status"));
        String amount =  String.valueOf(map.get("amount"));

        step("Check the DB Data");
        Assertion.assertThat("account id should be same", injectPointPage.verifyContains(accountId, account_id), is(true));
        Assertion.assertThat("task type should be same", injectPointPage.verifyContains(task_type, taskType), is(true));
        Assertion.assertThat("task status should be same", injectPointPage.verifyContains(task_status, taskStatus), is(true));
        Assertion.assertThat("process type should be same", injectPointPage.verifyContains(processType, process_type), is(true));
        Assertion.assertThat("amount should be same", injectPointPage.verifyContains(amount, points), is(true));
    }

    @Author(names = { "Saurabh" })
    @Module(name = "Loyalty")
    @Tribe(name = "Eiffel")
    @Vertical(name = "Platform")
    @Test
    public void injectPointsBPG() throws Exception {
        Map<String, String> param = getParam("injectPointsBPG");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoyaltyPointsPage<?> loyaltyPointsPage = new LoyaltyPointsPage<>(session);
        InjectPointPage<?> injectPointPage = new InjectPointPage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String account_id = param.get("account_id");
        String process_type = param.get("process_type");
        String orderId = param.get("orderId");
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

        step("Add Order Id");
        injectPointPage.addOrderId(orderId);

        step("Add the Points");
        injectPointPage.addPoint(points);

        step("Click on the Submit Icon");
        injectPointPage.submitIcon();

        step("Click on the Submit Icon");
        injectPointPage.clickOnOk();

        step("Check the success message");
        Assertion.assertThat("check the success message",
                injectPointPage.successMessage("Successfully Inject Point to Approval Page"),is(true));

        step("Get Data from Db");
        Map<String,Object> map = injectPointPage.getDataDB(account_id);
        String accountId = String.valueOf(map.get("account_id"));
        String task_type =  String.valueOf(map.get("task_type"));
        String processType =  String.valueOf(map.get("process_type"));
        String task_status =  String.valueOf(map.get("task_status"));
        String amount =  String.valueOf(map.get("amount"));

        step("Check the DB Data");
        Assertion.assertThat("account id should be same", injectPointPage.verifyContains(accountId, account_id), is(true));
        Assertion.assertThat("task type should be same", injectPointPage.verifyContains(task_type, taskType), is(true));
        Assertion.assertThat("task status should be same", injectPointPage.verifyContains(task_status, taskStatus), is(true));
        Assertion.assertThat("process type should be same", injectPointPage.verifyContains(processType, process_type), is(true));
        Assertion.assertThat("amount should be same", injectPointPage.verifyContains(amount, points), is(true));
    }

    @Author(names = { "Ram Pratap Singh" })
    @Module(name = "Loyalty")
    @Tribe(name = "Eiffel")
    @Vertical(name = "Platform")
    @Test
    public void testInjectPointPartner() throws Exception {
        Map<String, String> param = getParam("testInjectPointPartner");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoyaltyPointsPage<?> loyaltyPointsPage = new LoyaltyPointsPage<>(session);
        InjectPointPage<?> injectPointPage = new InjectPointPage<>(session);
        ApprovalPointPage<?> approvalPointPage = new ApprovalPointPage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String filePath = param.get("filePath");
        String accountId = param.get("accountId");

        step("Open the Admin Care Page");
        loyaltyPointsPage.goToUserManagementPage(username, password);

        step("Open the Loyalty Page");
        loyaltyPointsPage.clickLoyaltyIcon();

        step("Open the Inject Point Partner Page");
        injectPointPage.goTOInjectPointPartner();

        step("Upload File");
        injectPointPage.uploadFile(filePath);

        step("Click on the Submit Icon");
        injectPointPage.submitIcon();

        step("User verify file uploaded successfully");
        Assertion.assertThat("check the success message", injectPointPage.isFileUploaded(),is(true));

        step("Proceed to the Approval Page");
        approvalPointPage.openApprovalPage();

        step("User verify account id is present in the approval page");
        boolean isContain = approvalPointPage.checkUpload(accountId);
        Assertion.assertThat("account id should be present", isContain, is(true));

        step("User reject the injected point");
        approvalPointPage.clickRejectIcon();
    }

    @Author(names = { "Ram Pratap Singh" })
    @Module(name = "Loyalty")
    @Tribe(name = "Eiffel")
    @Vertical(name = "Platform")
    @Test
    public void testVerifyHistoryList() throws Exception {
        Map<String, String> param = getParam("testVerifyHistoryList");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoyaltyPointsPage<?> loyaltyPointsPage = new LoyaltyPointsPage<>(session);
        InjectPointPage<?> injectPointPage = new InjectPointPage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        
        String endDate = getCurrentDateTime("dd MMM yyyy");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -30);
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        String startDate = sdf.format(cal.getTime());

        step("Given user admin log in to admin/care");
        loyaltyPointsPage.goToUserManagementPage(username, password);

        step("And user go to the menu Loyalty");
        loyaltyPointsPage.clickLoyaltyIcon();

        step("And user clicks on the Approval Points");
        injectPointPage.clickApprovalPointMenu();

        step("And user clicks on the tab History List");
        injectPointPage.clickHistoryListTab();

        step("And user selects the dynamic date range (30 days ago to today)");
        injectPointPage.selectStartDate(startDate);
        injectPointPage.selectEndDate(endDate);

        step("When user clicks on button Search");
        injectPointPage.clickSearchButtonHistory();

        step("Then user should be able to see the history of point injection/rejection based on the selected date range");
        Assertion.assertThat("History table should be displayed",
                injectPointPage.isHistoryTableDisplayed(), is(true));

        step("Verify pagination information is displayed");
        String paginationInfo = injectPointPage.getPaginationInfo();
        Assertion.assertThat("Pagination info should contain 'Showing'",
                paginationInfo.contains("Showing"), is(true));

        step("Verify first row data is accessible");
        String submittedDate = injectPointPage.getHistorySubmittedDate();
        String accountId = injectPointPage.getHistoryAccountId();

        Assertion.assertThat("Submitted date should not be empty",
                !submittedDate.isEmpty(), is(true));
        Assertion.assertThat("Account ID should not be empty",
                !accountId.isEmpty(), is(true));
    }

    @Author(names = { "Ram Pratap Singh", "Yanuar" })
    @Module(name = "Loyalty")
    @Tribe(name = "Eiffel")
    @Vertical(name = "Platform")
    @Test
    public void testInjectPointBulk() throws Exception {
        Map<String, String> param = getParam("testInjectPointBulk");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoyaltyPointsPage<?> loyaltyPointsPage = new LoyaltyPointsPage<>(session);
        InjectPointPage<?> injectPointPage = new InjectPointPage<>(session);
        ApprovalPointPage<?> approvalPointPage = new ApprovalPointPage<>(session);
        String username = param.get("username");
        String password = param.get("password");

        String[] accountIds = param.get("accountIds").split("#");
        String processType = param.get("processType");
        
        String templateFileName =  param.get("templateFileName");
        String uniqueFilePath = null;

        step("Create unique test file with custom data");
        uniqueFilePath = injectPointPage.createUniqueTestFileWithCustomData(templateFileName, accountIds);
        log("Using account IDs for bulk test: " + Arrays.toString(accountIds));
        log("Generated unique bulk test file: " + uniqueFilePath);

        try {
            step("Open the Admin Care Page");
            loyaltyPointsPage.goToUserManagementPage(username, password);

            step("Open the Loyalty Page");
            loyaltyPointsPage.clickLoyaltyIcon();

            step("Open the Inject Point Partner Page");
            injectPointPage.injectpointsdashboard();

            step("Click on the Bulk Inject Tab");
            injectPointPage.clickBulkTab();

            step("User select the Process Type");
            injectPointPage.clickProcessTypeDropdown();
            injectPointPage.selectSubCategory(processType);

            step("Upload unique test file");
            injectPointPage.uploadFile(uniqueFilePath);

            step("Click on the Submit Icon");
            injectPointPage.submitFileIcon();

            step("User verify file uploaded successfully");
            Assertion.assertThat("check the success message", injectPointPage.isFileUploaded(),is(true));

            step("Proceed to the Approval Page for bulk list");
            approvalPointPage.openApprovalPage();
            approvalPointPage.openPointApprovalBulkList();

            step("Verify the uploaded file is present and correct");
            String currentDate = CoreFunctions.getCurrentDate("dd/MM/yyyy");
            String fileName = injectPointPage.getFileName().replace(".csv", "");
            String[] expectedData = {currentDate, fileName, processType, String.valueOf(accountIds.length), username};
            approvalPointPage.checkUploadedFileInfo(expectedData);

            step("Verify uploaded file data details");
            String[][] expectedDetails = injectPointPage.getExpectedData();
            approvalPointPage.checkUploadedFileDetails(expectedDetails);

            step("User reject the injected point");
            approvalPointPage.clickRejectIconBulk();
        } finally {
            step("Cleanup temporary test file");
            if (uniqueFilePath != null) {
                injectPointPage.cleanupTestFile(uniqueFilePath);
                log("Cleaned up temporary test file: " + uniqueFilePath);
            }
        }
    }
}
