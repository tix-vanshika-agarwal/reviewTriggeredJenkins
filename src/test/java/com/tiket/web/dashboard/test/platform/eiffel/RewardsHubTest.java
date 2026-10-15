package com.tiket.web.dashboard.test.platform.eiffel;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;
import com.tiket.web.WebTest;
import com.tiket.web.dashboard.page.platform.eiffel.loyalty.RewardsHubPage;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static com.tiket.testdata.TestData.getParam;

public class RewardsHubTest extends WebTest {

    private static final String TEST_CAMPAIGN_PREFIX = "Automation ";

    @Author(names = { "Yanuar" })
    @Module(name = "RewardsHub")
    @Tribe(name = "Eiffel")
    @Vertical(name = "Platform")
    @Test
    public void testRewardsHubAddNewCampaignTriggerTypeAPI() {
        Session<?> session = WebTest.sessionThreadLocal.get();
        Map<String, String> testData = getParam("testRewardsHubAddNewCampaignTriggerTypeAPI");
        WebTest.paramsThreadLocal.set(testData);

        String email = testData.get("email");
        String password = testData.get("password");
        String campaignName = TEST_CAMPAIGN_PREFIX + CoreFunctions.getCurrentDate("ddMMyyyy HHmmss");
        String triggerType = testData.get("triggerType");
        String pointType = testData.get("pointType");
        String amount = testData.get("amount");
        String rewardQuota = testData.get("rewardQuota");
        String emailTemplateCode = testData.get("emailTemplateCode");
        String successMessage = testData.get("successMessage");

        RewardsHubPage<?> rewardsHubPage = new RewardsHubPage<>(session);

        step("Given user logs in to the Rewards Hub dashboard");
        rewardsHubPage.goToRewardsHubPageViaUI(email, password);

        step("When user clicks the Create New button");
        rewardsHubPage.clickCreateNew();

        step("And user selects the trigger type");
        rewardsHubPage.selectTriggerType(triggerType);

        step("And user fills in the campaign details");
        rewardsHubPage.inputCampaignName(campaignName);
        rewardsHubPage.clickSetAsActive();
        rewardsHubPage.setEndDateTomorrow();

        step("And user configures the point reward settings");
        rewardsHubPage.selectPointReward();
        rewardsHubPage.selectPointType(pointType);
        rewardsHubPage.inputAmount(amount);
        rewardsHubPage.inputRewardQuota(rewardQuota);

        step("And user configures the CRM email settings");
        rewardsHubPage.clickEmailToggle();
        rewardsHubPage.inputEmailTemplateCode(emailTemplateCode);

        step("And user saves and activates the campaign");
        rewardsHubPage.clickSave();
        rewardsHubPage.confirmActivateCampaign();

        step("Then user should see a success notification");
        rewardsHubPage.verifySuccessSnackbarDisplayed(successMessage);

        step("And the campaign details should be saved correctly");
        rewardsHubPage.verifyCampaignDetails(campaignName, triggerType, pointType, amount, emailTemplateCode);
    }

    @Author(names = { "Yanuar" })
    @Module(name = "RewardsHub")
    @Tribe(name = "Eiffel")
    @Vertical(name = "Platform")
    @Test
    public void testRewardsHubAddNewCampaignTriggerTypeBulkUpload() throws IOException {
        Session<?> session = WebTest.sessionThreadLocal.get();
        Map<String, String> testData = getParam("testRewardsHubAddNewCampaignTriggerTypeBulkUpload");
        WebTest.paramsThreadLocal.set(testData);

        String email = testData.get("email");
        String password = testData.get("password");
        String campaignName = TEST_CAMPAIGN_PREFIX + CoreFunctions.getCurrentDate("ddMMyyyy HHmmss");
        String triggerType = testData.get("triggerType");
        String pointType = testData.get("pointType");
        String amount = testData.get("amount");
        String rewardQuota = testData.get("rewardQuota");
        String emailTemplateCode = testData.get("emailTemplateCode");
        String successMessage = testData.get("successMessage");
        String accountIdsRaw = testData.get("accountIds");

        RewardsHubPage<?> rewardsHubPage = new RewardsHubPage<>(session);
        File csvFile = null;

        try {
            step("Given user logs in to the Rewards Hub dashboard");
            rewardsHubPage.goToRewardsHubPage(email, password);

            step("When user clicks the Create New button");
            rewardsHubPage.clickCreateNew();

            step("And user selects the trigger type using Bulk Upload");
            rewardsHubPage.selectTriggerType(triggerType);

            step("And user uploads the CSV file");
            csvFile = rewardsHubPage.createBulkUploadCSV(accountIdsRaw);
            rewardsHubPage.uploadCSV(csvFile.getAbsolutePath());

            step("And user fills in the campaign details");
            rewardsHubPage.inputCampaignName(campaignName);
            rewardsHubPage.clickSetAsActive();

            step("And user configures the point reward settings");
            rewardsHubPage.selectPointReward();
            rewardsHubPage.selectPointType(pointType);
            rewardsHubPage.inputAmount(amount);
            rewardsHubPage.inputRewardQuota(rewardQuota);

            step("And user configures the CRM email settings");
            rewardsHubPage.clickEmailToggle();
            rewardsHubPage.inputEmailTemplateCode(emailTemplateCode);

            step("And user saves and activates the campaign");
            rewardsHubPage.clickSave();
            rewardsHubPage.confirmActivateCampaign();

            step("Then user should see a success Snackbar notification");
            rewardsHubPage.verifySuccessSnackbarDisplayed(successMessage);

            step("And the campaign details should be saved correctly");
            rewardsHubPage.verifyCampaignDetails(campaignName, triggerType, pointType, amount, emailTemplateCode);
        } finally {
            if (csvFile != null) {
                CoreFunctions.deleteFile(csvFile.getAbsolutePath());
            }
        }
    }

    @Author(names = { "Yanuar" })
    @Module(name = "RewardsHub")
    @Tribe(name = "Eiffel")
    @Vertical(name = "Platform")
    @Test
    public void testRewardsHubAddNewCampaignTriggerTypeSegment() {
        Session<?> session = WebTest.sessionThreadLocal.get();
        Map<String, String> testData = getParam("testRewardsHubAddNewCampaignTriggerTypeSegment");
        WebTest.paramsThreadLocal.set(testData);

        String email = testData.get("email");
        String password = testData.get("password");
        String campaignName = TEST_CAMPAIGN_PREFIX + CoreFunctions.getCurrentDate("ddMMyyyy HHmmss");
        String triggerType = testData.get("triggerType");
        String userSegment = testData.get("userSegment");
        String pointType = testData.get("pointType");
        String amount = testData.get("amount");
        String rewardQuota = testData.get("rewardQuota");
        String emailTemplateCode = testData.get("emailTemplateCode");
        String successMessage = testData.get("successMessage");

        RewardsHubPage<?> rewardsHubPage = new RewardsHubPage<>(session);

        step("Given user logs in to the Rewards Hub dashboard");
        rewardsHubPage.goToRewardsHubPage(email, password);

        step("When user clicks the Create New button");
        rewardsHubPage.clickCreateNew();

        step("And user selects trigger type using User segment");
        rewardsHubPage.selectTriggerType(triggerType);

        step("And user input user segment");
        rewardsHubPage.inputUserSegment(userSegment);

        step("And user fills in the campaign details");
        rewardsHubPage.inputCampaignName(campaignName);
        rewardsHubPage.clickSetAsActive();
        rewardsHubPage.setEndDateTomorrow();

        step("And user configures the point reward settings");
        rewardsHubPage.selectPointReward();
        rewardsHubPage.selectPointType(pointType);
        rewardsHubPage.inputAmount(amount);
        rewardsHubPage.inputRewardQuota(rewardQuota);

        step("And user configures the CRM email settings");
        rewardsHubPage.clickEmailToggle();
        rewardsHubPage.inputEmailTemplateCode(emailTemplateCode);

        step("And user saves and activates the campaign");
        rewardsHubPage.clickSave();
        rewardsHubPage.confirmActivateCampaign();

        step("Then user should see success snackbar");
        rewardsHubPage.verifySuccessSnackbarDisplayed(successMessage);

        step("And the campaign details should be saved correctly");
        rewardsHubPage.verifyCampaignDetails(campaignName, triggerType, pointType, amount, emailTemplateCode);
    }

    @Author(names = { "Yanuar" })
    @Module(name = "RewardsHub")
    @Tribe(name = "Eiffel")
    @Vertical(name = "Platform")
    @Test
    public void testRewardsHubEditCampaign() {
        Session<?> session = WebTest.sessionThreadLocal.get();
        Map<String, String> testData = getParam("testRewardsHubEditCampaign");
        WebTest.paramsThreadLocal.set(testData);

        String email = testData.get("email");
        String password = testData.get("password");
        String initialCampaignName = TEST_CAMPAIGN_PREFIX + CoreFunctions.getCurrentDate("ddMMyyyy HHmmss");
        String triggerType = testData.get("triggerType");
        String pointType = testData.get("pointType");
        String initialAmount = testData.get("amount");
        String initialRewardQuota = testData.get("rewardQuota");
        String emailTemplateCode = testData.get("emailTemplateCode");
        String successMessage = testData.get("successMessage");

        // Updated data
        String updatedCampaignName = initialCampaignName + " Edited";
        String updatedAmount = String.valueOf(CoreFunctions.generateRandomInt(100000, 500000));
        String updatedRewardQuota = String.valueOf(CoreFunctions.generateRandomInt(500, 1000));

        RewardsHubPage<?> rewardsHubPage = new RewardsHubPage<>(session);

        step("Given user login to the rewards HUB dashboard");
        rewardsHubPage.goToRewardsHubPage(email, password);

        step("And user have created one campaign");
        rewardsHubPage.clickCreateNew();
        rewardsHubPage.selectTriggerType(triggerType);
        rewardsHubPage.inputCampaignName(initialCampaignName);
        rewardsHubPage.clickSetAsActive();
        rewardsHubPage.setEndDateTomorrow();
        rewardsHubPage.selectPointReward();
        rewardsHubPage.selectPointType(pointType);
        rewardsHubPage.inputAmount(initialAmount);
        rewardsHubPage.inputRewardQuota(initialRewardQuota);
        rewardsHubPage.clickEmailToggle();
        rewardsHubPage.inputEmailTemplateCode(emailTemplateCode);
        rewardsHubPage.clickSave();
        rewardsHubPage.confirmActivateCampaign();
        rewardsHubPage.verifySuccessSnackbarDisplayed(successMessage);

        step("When user click on Edit after creating campaign");
        rewardsHubPage.clickEditCampaign();

        step("And user inputs the changes");
        rewardsHubPage.inputCampaignName(updatedCampaignName);
        rewardsHubPage.setEndDateAfterTomorrow();
        rewardsHubPage.inputAmount(updatedAmount);
        rewardsHubPage.inputRewardQuota(updatedRewardQuota);

        step("And user clicks on button Update");
        rewardsHubPage.clickUpdate();
        rewardsHubPage.confirmActivateCampaign();

        step("Then user can verify all the details are correct");
        rewardsHubPage.verifyCampaignDetails(updatedCampaignName, triggerType, pointType, updatedAmount, updatedRewardQuota, emailTemplateCode);
    }
}
