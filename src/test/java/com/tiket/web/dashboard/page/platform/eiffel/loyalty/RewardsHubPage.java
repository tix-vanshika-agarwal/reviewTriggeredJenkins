package com.tiket.web.dashboard.page.platform.eiffel.loyalty;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.web.WebPage;
import com.tiket.web.dashboard.page.member_dashboard.MemberLoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.Is.is;

public class RewardsHubPage<T extends WebDriver> extends WebPage<T> {

    private static final String REWARDS_HUB_URL = "/admin/care/loyalty/rewards-hub";
    private static final String CSV_DIRECTORY_PATH = System.getProperty("user.dir") + "/src/test/resources/dashboard/eiffel/";
    private static final String BULK_UPLOAD_TRIGGER_TYPE = "Bulk upload";
    private static final String USER_SEGMENT_TRIGGER_TYPE = "User segment";
    private static final String ACCOUNT_ID_DELIMITER = "#";
    private static final int DEFAULT_WAIT_TIMEOUT = 10;
    private static final int LOADING_WAIT_TIMEOUT = 2;

    private static final Locator CREATE_NEW_BUTTON = new Locator("CREATE_NEW_BUTTON", By.xpath("//button[contains(., 'Create new')]"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", By.cssSelector("[data-testid='save-submit-btn']"));
    private static final Locator UPDATE_BUTTON = new Locator("UPDATE_BUTTON", By.cssSelector("[data-testid='update-submit-btn']"));
    private static final Locator ACTIVATE_CAMPAIGN_BUTTON = new Locator("ACTIVATE_CAMPAIGN_BUTTON", By.xpath("//button[contains(., 'Activate campaign')]"));
    private static final Locator LOADING_CONTAINER = new Locator("LOADING_CONTAINER", By.xpath("//div[contains(@class, 'LoadingWithIllustration_container')]"));

    private static final Locator CAMPAIGN_NAME_INPUT = new Locator("CAMPAIGN_NAME_INPUT", By.id("campaign-name"));
    private static final Locator TRIGGER_TYPE_INPUT = new Locator("TRIGGER_TYPE_INPUT", By.id("trigger-type"));
    private static final Locator POINT_TYPE_INPUT = new Locator("POINT_TYPE_INPUT", By.id("point-type"));
    private static final Locator AMOUNT_INPUT = new Locator("AMOUNT_INPUT", By.id("amount"));
    private static final Locator END_DATE_INPUT = new Locator("END_DATE_INPUT", By.id("end-date"));
    private static final Locator EMAIL_TEMPLATE_INPUT = new Locator("EMAIL_TEMPLATE_INPUT", By.xpath("//input[@data-testid='ut-rewars-hub-detail-crm-email-template-code']"));
    private static final Locator REWARD_QUOTA_INPUT = new Locator("REWARD_QUOTA_INPUT", By.xpath("//input[@data-testid='ut-rewards-hub-detail-reward-quota']"));
    private static final Locator BULK_UPLOAD_INPUT = new Locator("BULK_UPLOAD_INPUT", By.xpath("//input[@type='file']"));
    private static final Locator USER_SEGMENT_INPUT = new Locator("USER_SEGMENT_INPUT", By.id("user-segment"));

    private static final Locator SET_AS_ACTIVE_TOGGLE = new Locator("SET_AS_ACTIVE_TOGGLE", By.xpath("//span[text()='Set as active']/following-sibling::div//label"));
    private static final Locator SET_AS_ACTIVE_CONDITION = new Locator("SET_AS_ACTIVE_CONDITION", By.xpath("//span[text()='Set as active']/parent::div/following-sibling::div"));
    private static final Locator POINT_TOGGLE = new Locator("POINT_TOGGLE", By.xpath("//span[text()='Point']/following-sibling::div//label"));
    private static final Locator EMAIL_TOGGLE = new Locator("EMAIL_TOGGLE", By.xpath("//span[text()='Email']/following-sibling::div//label"));
    private static final Locator PN_TOGGLE = new Locator("PN_TOGGLE", By.xpath("//span[text()='Push Notification']/parent::div/following-sibling::div"));
    private static final Locator PN_TOGGLE_ON_OFF = new Locator("PN_TOGGLE_ON_OFF", By.xpath("//input[@data-testid='ut-rewars-hub-detail-crm-pn-active']/following-sibling::div"));

    private static final Locator POINT_INPUT = new Locator("POINT_INPUT", By.xpath("//span[text()='Point']/following-sibling::div//input"));
    private static final Locator EMAIL_INPUT = new Locator("EMAIL_INPUT", By.xpath("//span[text()='Email']/following-sibling::div//input"));
    private static final Locator SUCCESS_SNACKBAR = new Locator("SUCCESS_SNACKBAR", By.xpath("//div[contains(@class, 'snackbar') and contains(@class, 'type_success')]"));
    private static final Locator UPLOADED_FILENAME = new Locator("UPLOADED_FILENAME", By.xpath("//div[contains(@class, 'SingleFileUpload_filename_wrapper')]//span[contains(@class, 'SingleFileUpload_filename')]"));
    private static final Locator EDIT_BUTTON = new Locator("EDIT_BUTTON", By.xpath("//button[contains(., 'Edit')]"));

    private String uploadedFileName;
    private String userSegment;

    private static Locator getDropdownOptionLocator(String optionText) {
        return new Locator("DROPDOWN_OPTION_" + optionText.toUpperCase().replace(" ", "_"), By.xpath("//p[text()='" + optionText + "']"));
    }

    private static Locator getDatePickerOptionLocator(String dateLabel) {
        return new Locator("DATE_OPTION_" + dateLabel.replace(" ", "_"), By.xpath("//span[contains(@title, '" + dateLabel + "') or contains(@aria-label, '" + dateLabel + "')]"));
    }

    public RewardsHubPage(Session<T> session) {
        super(session);
    }

    public void goToRewardsHubPage(String userName, String password) {
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        memberLoginPage.adminLoginViaCookie(userName, password);
        memberLoginPage.goToPage(REWARDS_HUB_URL);
    }

    public void goToRewardsHubPageViaUI(String userName, String password) {
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        memberLoginPage.goToPage(REWARDS_HUB_URL);

        if (!memberLoginPage.isLoginUsingExpectedEmail(userName)) {
            memberLoginPage.logoutAdmin();
            memberLoginPage.adminLogin(userName, password);
            memberLoginPage.goToPage(REWARDS_HUB_URL);
        }
    }

    public void clickCreateNew() {
        waitForLoadingToDisappear();
        driverTools.click(CREATE_NEW_BUTTON);
    }

    public void inputCampaignName(String campaignName) {
        driverTools.click(CAMPAIGN_NAME_INPUT);
        driverTools.clearInput(CAMPAIGN_NAME_INPUT);
        driverTools.sendKeys(CAMPAIGN_NAME_INPUT, campaignName);
    }

    public void selectTriggerType(String type) {
        selectDropdownOption(TRIGGER_TYPE_INPUT, type);
    }

    public File createBulkUploadCSV(String accountIdsRaw) throws IOException {
        this.uploadedFileName = "bulk_upload_" + CoreFunctions.randomInt(5) + ".csv";
        List<String> fileContent = new ArrayList<>();
        fileContent.add("accountId");
        fileContent.addAll(Arrays.asList(accountIdsRaw.split(ACCOUNT_ID_DELIMITER)));
        return CoreFunctions.createCSV(CSV_DIRECTORY_PATH, uploadedFileName, fileContent);
    }

    public void uploadCSV(String filePath) {
        driverTools.sendKeys(BULK_UPLOAD_INPUT, filePath);
    }

    public void inputUserSegment(String segment) {
        this.userSegment = segment;
        driverTools.sendKeys(USER_SEGMENT_INPUT, segment);
    }

    public void selectPointType(String type) {
        selectDropdownOption(POINT_TYPE_INPUT, type);
    }

    public void clickSetAsActive() {
        driverTools.waitForElementToAppear(SET_AS_ACTIVE_CONDITION);
        String ariaHidden = driverTools.getAttribute(SET_AS_ACTIVE_CONDITION.by(), "aria-hidden");
        if ("true".equals(ariaHidden)) {
            logger.log("Set as active condition is off, clicking to turn it on");
            driverTools.click(SET_AS_ACTIVE_TOGGLE);
        }
    }

    public void setEndDateTomorrow() {
        driverTools.scrollElementToCenter(END_DATE_INPUT);
        driverTools.click(END_DATE_INPUT);
        String dateLabel = CoreFunctions.addDaysToLongCurrentDate(1);
        driverTools.click(getDatePickerOptionLocator(dateLabel));
    }

    public void setEndDateAfterTomorrow() {
        driverTools.scrollElementToCenter(END_DATE_INPUT);
        driverTools.click(END_DATE_INPUT);
        String dateLabel = CoreFunctions.addDaysToLongCurrentDate(2);
        driverTools.click(getDatePickerOptionLocator(dateLabel));
    }

    public void selectPointReward() {
        boolean isAmountInputDisplayed = driverTools.waitForElementToAppear(AMOUNT_INPUT).isPresent();
        if (!isAmountInputDisplayed) {
            driverTools.scrollElementToCenter(POINT_TOGGLE);
            driverTools.click(POINT_TOGGLE);
        }
    }

    public void inputAmount(String amount) {
        driverTools.scrollElementToCenter(AMOUNT_INPUT);
        driverTools.click(AMOUNT_INPUT);
        driverTools.clearInput(AMOUNT_INPUT);
        driverTools.sendKeys(AMOUNT_INPUT, amount);
    }

    public void inputRewardQuota(String quota) {
        driverTools.click(REWARD_QUOTA_INPUT);
        driverTools.clearInput(REWARD_QUOTA_INPUT);
        driverTools.sendKeys(REWARD_QUOTA_INPUT, quota);
    }

    public void clickEmailToggle() {
        driverTools.waitForElementToAppear(PN_TOGGLE);
        String ariaHidden = driverTools.getAttribute(PN_TOGGLE.by(), "aria-hidden");
        if ("false".equals(ariaHidden)) {
            logger.log("PN Toggle is on, clicking to turn it off");
            driverTools.scrollElementToCenter(PN_TOGGLE_ON_OFF);
            driverTools.clickWithJavascriptExecutor(PN_TOGGLE_ON_OFF);
        }
        driverTools.scrollElementToCenter(EMAIL_TOGGLE);
        driverTools.click(EMAIL_TOGGLE);
    }

    public void inputEmailTemplateCode(String code) {
        driverTools.clickWithJavascriptExecutor(EMAIL_TEMPLATE_INPUT);
        driverTools.clearInput(EMAIL_TEMPLATE_INPUT);
        driverTools.sendKeys(EMAIL_TEMPLATE_INPUT, code);
    }

    public void clickEditCampaign() {
        driverTools.click(EDIT_BUTTON);
    }

    public void clearCampaignName() {
        driverTools.clearInput(CAMPAIGN_NAME_INPUT);
    }

    public void clearAmount() {
        driverTools.clearInput(AMOUNT_INPUT);
    }

    public void clearRewardQuota() {
        driverTools.clearInput(REWARD_QUOTA_INPUT);
    }

    public void clickSave() {
        driverTools.scrollElementToCenter(SAVE_BUTTON);
        driverTools.click(SAVE_BUTTON);
    }

    public void clickUpdate() {
        driverTools.scrollElementToCenter(UPDATE_BUTTON);
        driverTools.click(UPDATE_BUTTON);
    }

    public void confirmActivateCampaign() {
        driverTools.click(ACTIVATE_CAMPAIGN_BUTTON);
    }

    public String getCampaignNameValue() {
        driverTools.waitForElementToAppear(CAMPAIGN_NAME_INPUT, DEFAULT_WAIT_TIMEOUT);
        return driverTools.getAttribute(CAMPAIGN_NAME_INPUT.by(), "value");
    }

    public String getTriggerTypeValue() {
        driverTools.waitForElementToAppear(TRIGGER_TYPE_INPUT, DEFAULT_WAIT_TIMEOUT);
        return driverTools.getAttribute(TRIGGER_TYPE_INPUT.by(), "value");
    }

    public String getPointTypeValue() {
        driverTools.waitForElementToAppear(POINT_TYPE_INPUT, DEFAULT_WAIT_TIMEOUT);
        return driverTools.getAttribute(POINT_TYPE_INPUT.by(), "value");
    }

    public String getAmountValue() {
        driverTools.waitForElementToAppear(AMOUNT_INPUT, DEFAULT_WAIT_TIMEOUT);
        return driverTools.getAttribute(AMOUNT_INPUT.by(), "value");
    }

    public String getEmailTemplateCodeValue() {
        driverTools.waitForElementToAppear(EMAIL_TEMPLATE_INPUT, DEFAULT_WAIT_TIMEOUT);
        return driverTools.getAttribute(EMAIL_TEMPLATE_INPUT.by(), "value");
    }

    public String getUserSegmentValue() {
        driverTools.waitForElementToAppear(USER_SEGMENT_INPUT, DEFAULT_WAIT_TIMEOUT);
        return driverTools.getAttribute(USER_SEGMENT_INPUT.by(), "value");
    }

    public boolean isPointChecked() {
        return driverTools.isElementSelected(POINT_INPUT);
    }

    public boolean isEmailChecked() {
        return driverTools.isElementSelected(EMAIL_INPUT);
    }

    public void verifySuccessSnackbarDisplayed(String expectedMessage) {
        boolean isDisplayed = driverTools.waitForElementToAppear(SUCCESS_SNACKBAR, DEFAULT_WAIT_TIMEOUT).isPresent();
        String actualMessage = driverTools.getText(SUCCESS_SNACKBAR).orElse("");
        driverTools.waitForElementToDisappear(SUCCESS_SNACKBAR);

        Assertion.assertThat("Success snackbar should be displayed", isDisplayed, is(true));
        Assertion.assertThat("Success snackbar text should match expected message", actualMessage, is(expectedMessage));
    }

    public void verifyCampaignDetails(String campaignName, String triggerType, String pointType, String amount, String emailTemplateCode) {
        driverTools.dummyWait(2);
        waitForLoadingToDisappear();
        Assertion.assertThat("Campaign Name should match", getCampaignNameValue(), is(campaignName));
        Assertion.assertThat("Trigger Type should match", getTriggerTypeValue(), is(triggerType));
        Assertion.assertThat("Point Type should match", getPointTypeValue(), is(pointType));
        Assertion.assertThat("Amount should match", normalizeAmount(getAmountValue()), is(amount));
        Assertion.assertThat("Email Template Code should match", getEmailTemplateCodeValue(), is(emailTemplateCode));

        if (BULK_UPLOAD_TRIGGER_TYPE.equalsIgnoreCase(triggerType)) {
            verifyUploadedFileName();
        } else if (USER_SEGMENT_TRIGGER_TYPE.equalsIgnoreCase(triggerType)) {
            Assertion.assertThat("User Segment should match", getUserSegmentValue(), is(userSegment));
        }
    }

    public void verifyCampaignDetails(String campaignName, String triggerType, String pointType, String amount, String rewardQuota, String emailTemplateCode) {
        verifyCampaignDetails(campaignName, triggerType, pointType, amount, emailTemplateCode);
        Assertion.assertThat("Reward Quota should match", normalizeAmount(getRewardQuotaValue()), is(rewardQuota));
    }

    public String getRewardQuotaValue() {
        driverTools.waitForElementToAppear(REWARD_QUOTA_INPUT, DEFAULT_WAIT_TIMEOUT);
        return driverTools.getAttribute(REWARD_QUOTA_INPUT.by(), "value");
    }

    private void verifyUploadedFileName() {
        String actualFileName = driverTools.getText(UPLOADED_FILENAME).orElse("");
        Assertion.assertThat("Uploaded file name should match", actualFileName, containsString(uploadedFileName));
    }

    private void waitForLoadingToDisappear() {
        driverTools.waitForElementToAppear(LOADING_CONTAINER, LOADING_WAIT_TIMEOUT).ifPresent(e -> driverTools.waitForElementToDisappear(e));
    }

    private void selectDropdownOption(Locator dropdownLocator, String optionText) {
        driverTools.scrollElementToCenter(dropdownLocator);
        driverTools.click(dropdownLocator);
        driverTools.click(getDropdownOptionLocator(optionText));
    }

    private String normalizeAmount(String amount) {
        return amount != null ? amount.replace(",", "") : "";
    }
}
