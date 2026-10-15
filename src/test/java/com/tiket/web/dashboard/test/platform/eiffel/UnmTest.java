package com.tiket.web.dashboard.test.platform.eiffel;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;
import com.tiket.pojo.member.RegisterNewAccountData;
import com.tiket.testbase.Assertion;
import com.tiket.apiPage.Unm;
import com.tiket.apiPage.Unm.BcaCobrandCardholder;
import com.tiket.web.WebTest;
import com.tiket.web.dashboard.page.platform.eiffel.unm.UnmBulkLogoutPage;
import com.tiket.web.dashboard.page.platform.eiffel.unm.UnmDashboardLoginPage;
import com.tiket.web.dashboard.page.platform.eiffel.unm.UnmEmailBlacklistPage;
import com.tiket.web.dashboard.page.platform.eiffel.unm.UnmDashboardUserPage;
import com.tiket.web.dashboard.page.platform.eiffel.unm.UnmMemberDetailPage;
import com.tiket.web.dashboard.page.platform.eiffel.unm.UnmRbacPage;
import com.tiket.web.dashboard.page.platform.eiffel.unm.UnmSearchMemberPage;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.tiket.core.CoreFunctions.getCurrentDate;
import static com.tiket.core.CoreFunctions.shiftDateByDaysWithFormat;
import static com.tiket.testdata.TestData.getParam;
import static com.tiket.web.dashboard.page.platform.eiffel.unm.UnmMemberDetailPage.BCA_COBRAND_CARD_CONNECTED;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.not;

public class UnmTest extends WebTest {

    @Author(names = { "Yanuar" })
    @Module(name = "UNM")
    @Tribe(name = "Eiffel")
    @Vertical(name = "Platform")
    @Test
    public void testUNMSearchMemberEmail() {
        Map<String, String> param = getParam("testUNMSearchMemberEmail");
        paramsThreadLocal.set(param);

        Session<?> session = sessionThreadLocal.get();
        UnmSearchMemberPage<?> unmSearchMemberPage = new UnmSearchMemberPage<>(session);
        UnmDashboardLoginPage<?> unmDashboardLoginPage = new UnmDashboardLoginPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String userEmail = param.get("userEmail");

        step("Given user is already logged in on UNM Dashboard");
        unmDashboardLoginPage.goToUnmLoginPage();
        unmDashboardLoginPage.loginUnmAdmin(username, password);

        step("When user is in the Search Member menu and user selects Search by Email and user inputs a valid registered email");
        unmSearchMemberPage.searchMember("email", userEmail);

        step("Then user should see the user details in the table");
        unmSearchMemberPage.verifyFirstResultRow(userEmail);
    }

    @Author(names = { "Yanuar" })
    @Module(name = "UNM")
    @Tribe(name = "Eiffel")
    @Vertical(name = "Platform")
    @Test
    public void testUNMSuspendMember() {
        Map<String, String> param = getParam("testUNMSuspendMember");
        paramsThreadLocal.set(param);

        Session<?> session = sessionThreadLocal.get();
        UnmSearchMemberPage<?> unmSearchMemberPage = new UnmSearchMemberPage<>(session);
        UnmDashboardLoginPage<?> unmDashboardLoginPage = new UnmDashboardLoginPage<>(session);
        UnmMemberDetailPage<?> unmMemberDetailPage = new UnmMemberDetailPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String userEmail = param.get("userEmail");
        String reason = param.get("reason") + "_" + CoreFunctions.generateRandomString(10);

        step("Given user is already logged in on UNM Dashboard");
        unmDashboardLoginPage.goToUnmLoginPage();
        unmDashboardLoginPage.loginUnmAdmin(username, password);

        step("When user is in the Search Member menu and user selects Search by Email and user inputs a valid registered email");
        unmSearchMemberPage.searchMember("email", userEmail);

        step("And user clicks on button View");
        unmSearchMemberPage.clickViewFirstResult();

        step("And user clicks on button Action");
        unmMemberDetailPage.clickAction();

        step("And user clicks on Suspend");
        unmMemberDetailPage.clickSuspend();

        step("And user inputs notes reason of account suspension");
        unmMemberDetailPage.inputReason(reason);

        step("And user clicks on button Continue");
        unmMemberDetailPage.clickContinue();

        step("And user inputs admin password");
        unmMemberDetailPage.inputPassword(password);

        step("And user clicks on button Continue");
        unmMemberDetailPage.clickContinue();

        step("Then user should successfully suspend the selected account and user should see the user status changed to Suspended");
        unmMemberDetailPage.verifyStatus("Suspended");

        step("And user should see the logs activity in the member logs");
        unmMemberDetailPage.verifyLogsActivity(reason);

        step("Unsuspend user for future automation.");
        unmMemberDetailPage.unSuspendAccount("Unsuspend account for testing", password);
        unmMemberDetailPage.verifyStatus("Active");
    }

    @Author(names = { "Yanuar" })
    @Module(name = "UNM")
    @Tribe(name = "Eiffel")
    @Vertical(name = "Platform")
    @Test
    public void testUNMAddEmailBlacklist() throws IOException {
        Map<String, String> param = getParam("testUNMAddEmailBlacklist");
        paramsThreadLocal.set(param);

        Session<?> session = sessionThreadLocal.get();
        UnmDashboardLoginPage<?> unmDashboardLoginPage = new UnmDashboardLoginPage<>(session);
        UnmEmailBlacklistPage<?> unmEmailBlacklistPage = new UnmEmailBlacklistPage<>(session);
        final String ONE_DAY_IN_SECONDS = "86400";
        final String DATE_FORMAT = "dd MMM yyyy";
        final String EMAIL_PREFIX = "automation-";
        final String EMAIL_DOMAIN = "@mail.com";

        String username = param.get("username");
        String password = param.get("password");
        String smtp = param.getOrDefault("smtp", "mailgun");
        String successMessage = param.get("successMessage");

        long currentTime = System.currentTimeMillis();
        String randomEmail = EMAIL_PREFIX + currentTime + EMAIL_DOMAIN;
        String reason = "Blacklist via automation test on " + currentTime;
        String ttl = ONE_DAY_IN_SECONDS;
        String tsvFilePath = null;

        try {
            step("Given user is already logged in on UNM Dashboard");
            unmDashboardLoginPage.goToUnmLoginPage();
            unmDashboardLoginPage.loginUnmAdmin(username, password);

            step("And user click on Email > Email blacklist menu");
            unmEmailBlacklistPage.goToEmailBlacklistMenu();

            step("When user click on the Add Email Blacklist button");
            unmEmailBlacklistPage.clickAddEmailBlacklist();

            step("And user select the SMTP (" + smtp + ")");
            unmEmailBlacklistPage.selectSmtp(smtp);

            step("And user upload a valid file");
            tsvFilePath = unmEmailBlacklistPage.createTsvFile(randomEmail, reason, ttl);
            unmEmailBlacklistPage.uploadBlacklistFile(tsvFilePath);

            step("And user click on button Submit");
            unmEmailBlacklistPage.clickSubmit();

            step("Then a snackbar telling that added the blacklist email is successful should be visible");
            unmEmailBlacklistPage.verifySuccessSnackbar(successMessage);

            step("And user can search the email");
            unmEmailBlacklistPage.searchEmail(randomEmail);

            step("And user can verify the email is blacklisted as configured in the tsv file");
            String today = getCurrentDate(DATE_FORMAT);
            String tomorrow = shiftDateByDaysWithFormat(today, 1, DATE_FORMAT, 24);

            unmEmailBlacklistPage.verifyEmailInList(randomEmail, reason, today, tomorrow, smtp);
        } finally {
            unmEmailBlacklistPage.deleteFile(tsvFilePath);
        }
    }

    @Author(names = { "Yanuar" })
    @Module(name = "UNM")
    @Tribe(name = "Eiffel")
    @Vertical(name = "Platform")
    @Test
    public void testUNMBulkLogoutUser() {
        Map<String, String> param = getParam("testUNMBulkLogoutUser");
        paramsThreadLocal.set(param);

        Session<?> session = sessionThreadLocal.get();
        UnmDashboardLoginPage<?> unmDashboardLoginPage = new UnmDashboardLoginPage<>(session);
        UnmBulkLogoutPage<?> unmBulkLogoutPage = new UnmBulkLogoutPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String accountIds = param.get("accountIds");
        String successMessage = param.get("successMsg");

        String[] accountIdArray = accountIds.split("#");

        String tsvFilePath = null;

        try {
            step("Precondition: Generate TSV file");
            tsvFilePath = unmBulkLogoutPage.generateTsvFile(accountIdArray);

            step("Given user is already logged in on UNM Dashboard");
            unmDashboardLoginPage.goToUnmLoginPage();
            unmDashboardLoginPage.loginUnmAdmin(username, password);

            step("When user click on bulk action > force logout menu");
            unmBulkLogoutPage.goToForceLogoutMenu();

            step("And user click on Force Logout button");
            unmBulkLogoutPage.clickForceLogoutButton();

            step("And user upload a valid TSV");
            unmBulkLogoutPage.uploadTsvFile(tsvFilePath);

            step("And user click on the button Force Logout");
            unmBulkLogoutPage.clickSubmitButtonInModal();

            step("And user inputs a valid password");
            unmBulkLogoutPage.enterPassword(password);
            unmBulkLogoutPage.clickContinueButton();

            step("Then user should successfully execute the bulk logout file");
            unmBulkLogoutPage.verifyBulkLogoutSuccess(successMessage);

            step("And user go to History List tab");
            unmBulkLogoutPage.goToHistoryListTab();

            step("And user can see that the file is uploaded successfully with status 'completed' in the list");
            boolean fileVerified = unmBulkLogoutPage.verifyFileInHistoryList();
            Assertion.assertThat("Verify file appears in History List with Completed status", fileVerified, is(true));

        } finally {
            if (tsvFilePath != null) {
                step("Postcondition: Delete TSV file");
                unmBulkLogoutPage.deleteTsvFile(tsvFilePath);
            }
        }
    }

    @Author(names = { "Yanuar" })
    @Module(name = "UNM")
    @Tribe(name = "Eiffel")
    @Vertical(name = "Platform")
    @Test
    public void testUNMDashboardForgotPassword() {
        Map<String, String> param = getParam("testUNMDashboardForgotPassword");
        paramsThreadLocal.set(param);

        Session<?> session = sessionThreadLocal.get();
        UnmDashboardLoginPage<?> loginPage = new UnmDashboardLoginPage<>(session);

        String email = param.get("email");
        String newPassword = param.get("newPassword");

        step("Given user is in UNM Dashboard Login Page");
        loginPage.goToUnmLoginPage();
        loginPage.logoutUNMAdmin();
        loginPage.goToUnmLoginPage();

        step("When user clicks on Forgot Password");
        loginPage.clickForgotPassword();

        step("And user inputs a valid email address on the forgot password page");
        loginPage.inputEmail(email);

        step("And user clicks on button Send Reset Password Link");
        loginPage.clickSendResetPasswordLink();

        step("Then user should receive the Reset Password Link on email");
        String token = loginPage.getResetPasswordToken(email);

        step("When user clicks on the received link");
        loginPage.goToSetPasswordPage(token);

        step("And user is directed to Reset Password Page");
        loginPage.verifySetPasswordPageDisplayed();

        step("When user input new password and confirm password and user click on Set Password & Login button");
        loginPage.setPasswordAndSubmit(newPassword);

        step("Then user should see the success reset password snackbar");
        String expectedSuccessMessage = param.get("successMessage");
        loginPage.verifyResetPasswordSuccessSnackbar(expectedSuccessMessage);

        step("And user should be able to login using the new password");
        loginPage.loginUnmAdmin(email, newPassword);
    }

    @Author(names = { "Yanuar" })
    @Module(name = "UNM")
    @Tribe(name = "Eiffel")
    @Vertical(name = "Platform")
    @Test
    public void testUNMSearchMemberPhone() {
        Map<String, String> param = getParam("testUNMSearchMemberPhone");
        paramsThreadLocal.set(param);

        Session<?> session = sessionThreadLocal.get();
        UnmSearchMemberPage<?> unmSearchMemberPage = new UnmSearchMemberPage<>(session);
        UnmDashboardLoginPage<?> unmDashboardLoginPage = new UnmDashboardLoginPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String userPhone = param.get("userPhone");
        String fullPhone = param.get("phoneCode") + userPhone;

        step("Given user is already logged in on UNM Dashboard");
        unmDashboardLoginPage.goToUnmLoginPage();
        unmDashboardLoginPage.loginUnmAdmin(username, password);

        step("When user is in the Search Member menu and user selects Search by Phone and user inputs a valid registered phone");
        unmSearchMemberPage.searchMember("phone", userPhone);

        step("Then user should see the user details in the table");
        unmSearchMemberPage.verifyFirstResultRow(fullPhone);
    }

    @Author(names = { "Yanuar" })
    @Module(name = "UNM")
    @Tribe(name = "Eiffel")
    @Vertical(name = "Platform")
    @Test
    public void testUNMAddDashboardUser() {
        Map<String, String> param = getParam("testUNMAddDashboardUser");
        WebTest.paramsThreadLocal.set(param);

        Session<?> session = WebTest.sessionThreadLocal.get();
        UnmDashboardLoginPage<?> unmDashboardLoginPage = new UnmDashboardLoginPage<>(session);
        UnmDashboardUserPage<?> unmDashboardUserPage = new UnmDashboardUserPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String fullName = param.get("fullName");
        String userEmail = "automation-" + CoreFunctions.getCurrentTime() + "@tiket.com";
        String role = param.get("role");
        String successMessage = param.get("successMessage");
        String deleteMessage = param.get("deleteMessage").replace("#", userEmail);

        step("Given user is already logged in on UNM Dashboard");
        unmDashboardLoginPage.goToUnmLoginPage();
        unmDashboardLoginPage.loginUnmAdmin(username, password);

        step("When user clicks on the menu Dashboard Users");
        unmDashboardUserPage.goToDashboardUserMenu();

        step("And user clicks on the button Add User");
        unmDashboardUserPage.clickAddUser();

        step("And user input Mandatory details");
        unmDashboardUserPage.inputUserDetails(fullName, userEmail, role);

        step("And user click on button Add");
        unmDashboardUserPage.clickAdd();

        step("Then user should see success snackbar with correct message");
        unmDashboardUserPage.verifySuccessSnackbar(successMessage);

        step("Post-condition: Delete the newly created admin");
        unmDashboardUserPage.searchUser(userEmail);
        unmDashboardUserPage.deleteUser(password);
        unmDashboardUserPage.verifySuccessSnackbar(deleteMessage);
    }

    @Author(names = { "Yanuar" })
    @Module(name = "UNM")
    @Tribe(name = "Eiffel")
    @Vertical(name = "Platform")
    @Test
    public void testUNMUpdateName() {
        Map<String, String> param = getParam("testUNMUpdateName");
        paramsThreadLocal.set(param);

        Session<?> session = sessionThreadLocal.get();
        UnmSearchMemberPage<?> unmSearchMemberPage = new UnmSearchMemberPage<>(session);
        UnmDashboardLoginPage<?> unmDashboardLoginPage = new UnmDashboardLoginPage<>(session);
        UnmMemberDetailPage<?> unmMemberDetailPage = new UnmMemberDetailPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String userEmail = param.get("userEmail");
        String successMessage = param.get("successMessage");

        String newNamePrefix = param.getOrDefault("newNamePrefix", "Automation ");
        String reasonPrefix = param.getOrDefault("reasonPrefix", "Update name via automation test ");

        String newName = newNamePrefix + CoreFunctions.generateRandomString(8);
        String reason = reasonPrefix + CoreFunctions.generateRandomString(5);

        step("Given user is already logged in on UNM Dashboard");
        unmDashboardLoginPage.goToUnmLoginPage();
        unmDashboardLoginPage.loginUnmAdmin(username, password);

        step("When user search member by email and user inputs a valid registered email");
        unmSearchMemberPage.searchMember("email", userEmail);

        step("And user click on button View");
        unmSearchMemberPage.clickViewFirstResult();

        step("And user click on button Action");
        unmMemberDetailPage.clickAction();

        step("And user click on Update Name menu");
        unmMemberDetailPage.clickUpdateName();

        step("And user inputs a note reason for updating name");
        unmMemberDetailPage.inputReason(reason);

        step("And user inputs a new name");
        unmMemberDetailPage.inputNewName(newName);

        step("And user clicks on button Continue");
        unmMemberDetailPage.clickContinue();

        step("And user inputs a valid password");
        unmMemberDetailPage.inputPassword(password);

        step("And user clicks on button Continue");
        unmMemberDetailPage.clickContinue();

        step("Then user should successfully update the customer name and user should see the customer name is changed");
        unmMemberDetailPage.verifySuccessSnackbar(successMessage);
        unmMemberDetailPage.verifyName(newName);

        step("And user should see the logs activity in the member logs");
        unmMemberDetailPage.verifyLogsActivity(reason);
    }

    @Author(names = { "Yanuar" })
    @Module(name = "UNM")
    @Tribe(name = "Eiffel")
    @Vertical(name = "Platform")
    @Test
    public void testUNMUpdateDOB() {
        Map<String, String> param = getParam("testUNMUpdateDOB");
        paramsThreadLocal.set(param);

        Session<?> session = sessionThreadLocal.get();
        UnmSearchMemberPage<?> unmSearchMemberPage = new UnmSearchMemberPage<>(session);
        UnmDashboardLoginPage<?> unmDashboardLoginPage = new UnmDashboardLoginPage<>(session);
        UnmMemberDetailPage<?> unmMemberDetailPage = new UnmMemberDetailPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String userEmail = param.get("userEmail");
        String successMessage = param.get("successMessage");
        String todayFormatted = getCurrentDate("dd MMM yyyy");

        step("Given user is already logged in on UNM Dashboard");
        unmDashboardLoginPage.goToUnmLoginPage();
        unmDashboardLoginPage.loginUnmAdmin(username, password);

        step("When user search member by email and user inputs a valid registered email");
        unmSearchMemberPage.searchMember("email", userEmail);

        step("And user clicks on button View");
        unmSearchMemberPage.clickViewFirstResult();

        step("And user clicks on button Action");
        unmMemberDetailPage.clickAction();

        step("And user clicks on Update Date of Birth");
        unmMemberDetailPage.clickUpdateDOB();

        step("And user selects today as the new Date of Birth");
        unmMemberDetailPage.selectTodayDateOfBirth();

        step("And user clicks on button Continue");
        unmMemberDetailPage.clickContinue();

        step("And user inputs a valid password and clicks Continue");
        unmMemberDetailPage.confirmWithPassword(password);

        step("Then user should see a success snackbar");
        unmMemberDetailPage.verifySuccessSnackbar(successMessage);

        step("And user should see the customer DOB is changed");
        unmMemberDetailPage.verifyDOB(todayFormatted);

        step("And user should see the logs activity in the member logs");
        unmMemberDetailPage.verifyLogsActivity("Change Date of Birth");
    }

    @Author(names = { "Yanuar" })
    @Module(name = "UNM")
    @Tribe(name = "Eiffel")
    @Vertical(name = "Platform")
    @Test
    public void testUNMUpdateEmail() {
        Map<String, String> param = getParam("testUNMUpdateEmail");
        paramsThreadLocal.set(param);

        Session<?> session = sessionThreadLocal.get();
        UnmSearchMemberPage<?> unmSearchMemberPage = new UnmSearchMemberPage<>(session);
        UnmDashboardLoginPage<?> unmDashboardLoginPage = new UnmDashboardLoginPage<>(session);
        UnmMemberDetailPage<?> unmMemberDetailPage = new UnmMemberDetailPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String userEmail = param.get("userEmail");
        String targetEmail = param.get("targetEmail");
        String successMessage = param.getOrDefault("successMessage", "success");
        String reason = "Update email via automation test " + CoreFunctions.generateRandomString(5);

        boolean isRecovery = false;

        step("Given user is already logged in on UNM Dashboard");
        unmDashboardLoginPage.goToUnmLoginPage();
        unmDashboardLoginPage.loginUnmAdmin(username, password);

        step("When user searches member by email using userEmail");
        unmSearchMemberPage.searchMember("email", userEmail);

        if (!unmSearchMemberPage.isDataFound()) {
            step("Recovery: userEmail not found, searching by targetEmail instead (previous run may have failed before reverting)");
            unmSearchMemberPage.searchMember("email", targetEmail);
            isRecovery = true;

            if (!unmSearchMemberPage.isDataFound()) {
                Assertion.fail("Neither userEmail (" + userEmail + ") nor targetEmail (" + targetEmail + ") found. Test data may be corrupted.");
            }
        }

        step("And user clicks on button View");
        unmSearchMemberPage.clickViewFirstResult();

        String toEmail = isRecovery ? userEmail : targetEmail;
        boolean emailUpdated = false;

        try {
            step("And user clicks on button Action");
            unmMemberDetailPage.clickAction();

            step("And user clicks on Update Email menu");
            unmMemberDetailPage.clickUpdateEmail();

            step("And user inputs a note reason for updating email");
            unmMemberDetailPage.inputReason(reason);

            step("And user inputs a new email");
            unmMemberDetailPage.inputNewEmail(toEmail);

            step("And user clicks on button Continue");
            unmMemberDetailPage.clickContinue();

            step("And user inputs a valid password");
            unmMemberDetailPage.inputPassword(password);

            step("And user clicks on button Continue");
            unmMemberDetailPage.clickContinue();

            step("Then user should see the success snackbar");
            unmMemberDetailPage.verifySuccessSnackbar(successMessage);
            emailUpdated = true;

            step("And user should see the customer email is changed");
            unmMemberDetailPage.verifyEmail(toEmail);

            step("And user should see the logs activity in the member logs");
            unmMemberDetailPage.verifyLogsActivity(reason);
        } finally {
            if (emailUpdated && !isRecovery) {
                step("Post-condition: Revert email back to " + userEmail);
                unmMemberDetailPage.updateEmail(userEmail, "Revert email via automation", password);
                unmMemberDetailPage.verifyEmail(userEmail);
            }
        }
    }

    @Author(names = { "Yanuar" })
    @Module(name = "UNM")
    @Tribe(name = "Eiffel")
    @Vertical(name = "Platform")
    @Test
    public void testUNMRemovePhoneNumber() {
        Map<String, String> param = getParam("testUNMRemovePhoneNumber");
        paramsThreadLocal.set(param);

        Session<?> session = sessionThreadLocal.get();
        UnmSearchMemberPage<?> unmSearchMemberPage = new UnmSearchMemberPage<>(session);
        UnmDashboardLoginPage<?> unmDashboardLoginPage = new UnmDashboardLoginPage<>(session);
        UnmMemberDetailPage<?> unmMemberDetailPage = new UnmMemberDetailPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String userEmail = param.get("userEmail");
        String phoneNumber = "+62777" + CoreFunctions.randomInt(7);
        long unmId = Long.parseLong(param.get("unmId"));
        String reason = "Remove phone automation_" + CoreFunctions.generateRandomString(10);
        Unm<?> unmApi = new Unm<>(session);

        step("Precondition: Ensure account has a phone number");
        var profile = unmApi.getProfileDetail(userEmail);
        boolean phoneMissing = profile == null
                || profile.data == null
                || profile.data.phoneNationalNumber == null
                || profile.data.phoneNationalNumber.isEmpty();
        if (phoneMissing) {
            log("Phone number not found on account, adding via API with dynamic number: " + phoneNumber);
            unmApi.addPhoneNumberViaApi(phoneNumber, unmId);
        }

        try {
            step("Given user is already logged in on UNM Dashboard");
            unmDashboardLoginPage.goToUnmLoginPage();
            unmDashboardLoginPage.loginUnmAdmin(username, password);

            step("When user is in the Search Member menu and user selects Search by Email and user inputs a valid registered email and Search");
            unmSearchMemberPage.searchMember("email", userEmail);

            step("And user clicks on button View");
            unmSearchMemberPage.clickViewFirstResult();

            step("And user clicks on button Action");
            unmMemberDetailPage.clickAction();

            step("And user clicks on Remove Phone Number");
            unmMemberDetailPage.clickRemovePhoneNumber();

            step("And user inputs a note reason for removing phone number");
            unmMemberDetailPage.inputReason(reason);

            step("And user clicks on button Continue");
            unmMemberDetailPage.clickContinue();

            step("And user inputs a valid password");
            unmMemberDetailPage.inputPassword(password);

            step("And user clicks on button Continue");
            unmMemberDetailPage.clickContinue();

            step("Then user should successfully remove customer's phone number");
            unmMemberDetailPage.verifySuccessSnackbar(param.get("successMessage"));

            step("And user should see the customer phone number empty");
            unmMemberDetailPage.verifyPhoneNumberEmpty();

            step("And user should see the remove phone number & update email option no longer available");
            unmMemberDetailPage.verifyRemovePhoneAndUpdateEmailNotAvailable();

            step("And user should see the logs activity in the member logs");
            unmMemberDetailPage.verifyLogsActivity(reason);
        } finally {
            try {
                step("Postcondition: Add phone number back to the account via API");
                String restorePhone = "+62777" + CoreFunctions.randomInt(7);
                log("Restoring phone with dynamic number: " + restorePhone);
                unmApi.addPhoneNumberViaApi(restorePhone, unmId);
            } catch (Exception e) {
                log("WARNING: Postcondition failed to restore phone number: " + e.getMessage());
            }
        }
    }

    @Author(names = { "Pankaj Singh Kushwaha" })
    @Module(name = "UNM")
    @Tribe(name = "Eiffel")
    @Vertical(name = "Platform")
    @Test
    public void testUNMDeleteAccount() {
        Map<String, String> param = getParam("testUNMDeleteAccount");
        paramsThreadLocal.set(param);

        Session<?> session = sessionThreadLocal.get();
        Unm<?> unm = new Unm<>(session);
        UnmSearchMemberPage<?> unmSearchMemberPage = new UnmSearchMemberPage<>(session);
        UnmDashboardLoginPage<?> unmDashboardLoginPage = new UnmDashboardLoginPage<>(session);
        UnmMemberDetailPage<?> unmMemberDetailPage = new UnmMemberDetailPage<>(session);

        String adminUsername = param.get("adminEmail");
        String adminPassword = param.get("adminPassword");
        String successMessage = param.get("successMessage");

        step("Register New Account via API");
        RegisterNewAccountData newAccount = unm.registerNewAccountViaApiUNM();
        String newAccountEmail = newAccount.email;

        step("Given user is already logged in on UNM Dashboard");
        unmDashboardLoginPage.goToUnmLoginPage();
        unmDashboardLoginPage.loginUnmAdmin(adminUsername, adminPassword);

        step("Select Search by Email and search the Newly created Account: "+newAccountEmail);
        unmSearchMemberPage.searchMember("email", newAccountEmail);
        unmSearchMemberPage.verifyAccountStatus(newAccountEmail,"Active");

        step("Click on the View button");
        unmSearchMemberPage.clickViewFirstResult();

        step("Click on Action Menu and Select Delete Account");
        unmMemberDetailPage.clickAction();
        unmMemberDetailPage.clickDeleteAccountOption();

        step("Verify Delete Account modal appears, fill notes, and click Continue");
        unmMemberDetailPage.verifyModalAndConfirmDeletion("Automated test account deletion");

        step("Fill the password in the confirmation modal and click Continue");
        unmMemberDetailPage.fillPasswordAndConfirm(adminPassword);

        step("Verify success snackbar appears, validates message, and dismisses");
        unmMemberDetailPage.verifySnackbarMessageAndDismissal(successMessage);

        step("Search for the deleted user email");
        unmSearchMemberPage.searchMember("email", newAccountEmail);

        step("Verify the status in the search results table is 'deleted'");
        unmSearchMemberPage.verifyAccountStatus(newAccountEmail,"Deleted");
    }

    @Author(names = { "Yanuar" })
    @Module(name = "UNM")
    @Tribe(name = "Eiffel")
    @Vertical(name = "Platform")
    @Test
    public void testUNMAddRoleAccess() {
        Map<String, String> param = getParam("testUNMAddRoleAccess");
        paramsThreadLocal.set(param);

        Session<?> session = sessionThreadLocal.get();
        UnmDashboardLoginPage<?> unmDashboardLoginPage = new UnmDashboardLoginPage<>(session);
        UnmRbacPage<?> unmRbacPage = new UnmRbacPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String roleDesc = param.get("roleDesc");
        String successMessage = param.get("successMessage");
        int accessCount = Integer.parseInt(param.getOrDefault("accessCount", "3"));
        String roleName = "auto_ui_" + CoreFunctions.generateRandomString(5);

        step("Given user is already logged in on UNM Dashboard");
        unmDashboardLoginPage.goToUnmLoginPage();
        unmDashboardLoginPage.loginUnmAdmin(username, password);

        step("When user navigates to Settings > Role Based Access Control");
        unmRbacPage.goToRbacMenu();

        step("And user clicks on the button Add Role");
        unmRbacPage.clickAddRole();

        step("And user inputs the role name and description");
        unmRbacPage.inputRoleDetails(roleName, roleDesc);

        step("And user selects " + accessCount + " random access");
        List<String> selectedAccess = unmRbacPage.selectRandomAccess(accessCount);
        log("Selected access for role " + roleName + ": " + selectedAccess);

        step("And user clicks on button Add");
        unmRbacPage.clickAdd();

        step("Then user should see the success snackbar");
        unmRbacPage.verifySuccessSnackbar(successMessage);

        step("When user navigates to the last page of the role list");
        unmRbacPage.goToLastPage();

        step("And user opens Action > View Access for the newly created role");
        unmRbacPage.openViewAccess(roleName);

        step("Then user should see the selected access assigned to the role");
        unmRbacPage.verifyRoleAccess(roleName, selectedAccess);
    }

    @Author(names = { "Yanuar" })
    @Module(name = "UNM")
    @Tribe(name = "Eiffel")
    @Vertical(name = "Platform")
    @Test
    public void testUNMBcaCobrandNewCardholderRegistration() {
        Map<String, String> param = getParam("testUNMBcaCobrandNewCardholderRegistration");
        paramsThreadLocal.set(param);

        Session<?> session = sessionThreadLocal.get();
        Unm<?> unm = new Unm<>(session);
        UnmDashboardLoginPage<?> unmDashboardLoginPage = new UnmDashboardLoginPage<>(session);
        UnmSearchMemberPage<?> unmSearchMemberPage = new UnmSearchMemberPage<>(session);
        UnmMemberDetailPage<?> unmMemberDetailPage = new UnmMemberDetailPage<>(session);

        try {
            step("BCA hits UNM Registration API for a new cobrand cardholder");
            BcaCobrandCardholder cardholder = unm.registerBcaCobrandCardholder(
                    param.get("name"),
                    param.get("emailDomain"),
                    param.get("phoneCountryCode"),
                    param.get("phoneNationalNumberPrefix"),
                    param.get("type"));

            step("Account is created and name is derived");
            Assertion.assertThat("A new UNM account was created", cardholder.isNewUser(), is(true));
            Assertion.assertThat("UNM derived a member name from the registration",
                    cardholder.derivedName(), not(emptyOrNullString()));

            step("Given admin is logged in on the UNM Member dashboard");
            unmDashboardLoginPage.goToUnmLoginPage();
            unmDashboardLoginPage.loginUnmAdmin(param.get("adminEmail"), param.get("adminPassword"));

            step("Search the newly created member by email");
            unmSearchMemberPage.searchMember("email", cardholder.email());
            unmSearchMemberPage.verifyAccountStatus(cardholder.email(), "Active");

            step("Open member details and verify BCA cobrand connection");
            unmSearchMemberPage.clickViewFirstResult();
            unmMemberDetailPage.verifyName(cardholder.derivedName());
            unmMemberDetailPage.verifyBcaCobrandCardStatus(BCA_COBRAND_CARD_CONNECTED);

            step("Verify member log shows Account Registration Successful and Registration source: BCA_COBRAND - API");
            unmMemberDetailPage.verifyBcaCobrandRegistrationLog(cardholder.email());
        } finally {
            unm.deleteCreatedAccountQuietly();
        }
    }

    @Author(names = { "Yanuar" })
    @Module(name = "UNM")
    @Tribe(name = "Eiffel")
    @Vertical(name = "Platform")
    @Test
    public void testUNMBcaCobrandExistingUserMapping() {
        Map<String, String> param = getParam("testUNMBcaCobrandExistingUserMapping");
        paramsThreadLocal.set(param);

        Session<?> session = sessionThreadLocal.get();
        Unm<?> unm = new Unm<>(session);
        UnmDashboardLoginPage<?> unmDashboardLoginPage = new UnmDashboardLoginPage<>(session);
        UnmSearchMemberPage<?> unmSearchMemberPage = new UnmSearchMemberPage<>(session);
        UnmMemberDetailPage<?> unmMemberDetailPage = new UnmMemberDetailPage<>(session);

        try {
            step("Register a normal UNM user via API");
            RegisterNewAccountData account = unm.registerNormalUnmUser();

            step("Given admin is logged in on the UNM Member dashboard");
            unmDashboardLoginPage.goToUnmLoginPage();
            unmDashboardLoginPage.loginUnmAdmin(param.get("adminEmail"), param.get("adminPassword"));

            step("Search the newly created member by email");
            unmSearchMemberPage.searchMember("email", account.email);
            unmSearchMemberPage.verifyAccountStatus(account.email, "Active");

            step("Open member details and verify the account is not a BCA cobrand cardholder");
            unmSearchMemberPage.clickViewFirstResult();
            unmMemberDetailPage.verifyName(account.fullName);
            unmMemberDetailPage.verifyRegistrationSourceIsNotBcaCobrand();
            unmMemberDetailPage.verifyBcaCobrandCardIsNotConnected();

            step("BCA hits UNM Registration API for the existing user with a random name");
            unm.mapExistingAccountAsBcaCobrand(
                    account.email,
                    account.phoneCountryCode,
                    account.phoneNationalNumber,
                    param.get("type"));

            step("Refresh member details and verify BCA cobrand mapping");
            unmMemberDetailPage.verifyBcaCobrandMappingLog();
            unmMemberDetailPage.verifyBcaCobrandCardStatus(BCA_COBRAND_CARD_CONNECTED);
            unmMemberDetailPage.verifyName(account.fullName);
        } finally {
            unm.deleteCreatedAccountQuietly();
        }
    }

    @Author(names = { "Yanuar" })
    @Module(name = "UNM")
    @Tribe(name = "Eiffel")
    @Vertical(name = "Platform")
    @Test
    public void testUNMBcaCobrandAccountDeletion() {
        Map<String, String> param = getParam("testUNMBcaCobrandAccountDeletion");
        paramsThreadLocal.set(param);

        Session<?> session = sessionThreadLocal.get();
        Unm<?> unm = new Unm<>(session);
        UnmDashboardLoginPage<?> unmDashboardLoginPage = new UnmDashboardLoginPage<>(session);
        UnmSearchMemberPage<?> unmSearchMemberPage = new UnmSearchMemberPage<>(session);
        UnmMemberDetailPage<?> unmMemberDetailPage = new UnmMemberDetailPage<>(session);

        try {
            step("BCA hits UNM Registration API for a new cobrand cardholder");
            BcaCobrandCardholder cardholder = unm.registerBcaCobrandCardholder(
                    param.get("name"),
                    param.get("emailDomain"),
                    param.get("phoneCountryCode"),
                    param.get("phoneNationalNumberPrefix"),
                    param.get("type"));

            step("Given admin is logged in on the UNM Member dashboard");
            unmDashboardLoginPage.goToUnmLoginPage();
            unmDashboardLoginPage.loginUnmAdmin(param.get("adminEmail"), param.get("adminPassword"));

            step("Search the newly created cobrand member by email");
            unmSearchMemberPage.searchMember("email", cardholder.email());
            unmSearchMemberPage.verifyAccountStatus(cardholder.email(), "Active");

            step("Open member details and delete the BCA cobrand account");
            unmSearchMemberPage.clickViewFirstResult();
            unmMemberDetailPage.verifyBcaCobrandCardStatus(BCA_COBRAND_CARD_CONNECTED);
            unmMemberDetailPage.clickAction();
            unmMemberDetailPage.clickDeleteAccountOption();
            unmMemberDetailPage.verifyModalAndConfirmDeletion(param.get("deletionNotes"));
            unmMemberDetailPage.fillPasswordAndConfirm(param.get("adminPassword"));
            unmMemberDetailPage.verifySnackbarMessageAndDismissal(param.get("successMessage"));

            step("Search the deleted member and verify status is Deleted");
            unmSearchMemberPage.searchMember("email", cardholder.email());
            unmSearchMemberPage.verifyAccountStatus(cardholder.email(), "Deleted");
            unm.forgetCreatedAccount();
        } finally {
            unm.deleteCreatedAccountQuietly();
        }
    }
}
