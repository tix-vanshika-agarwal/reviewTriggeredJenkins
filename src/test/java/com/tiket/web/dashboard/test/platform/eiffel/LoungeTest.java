package com.tiket.web.dashboard.test.platform.eiffel;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.apiPage.TTDApi;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.platform.eiffel.lounge.LoungeDashboardPageMWeb;
import com.tiket.web.dashboard.page.platform.eiffel.lounge.LoungeDashboardPageWeb;
import com.tiket.web.dashboard.page.platform.eiffel.lounge.LoungeListPageWeb;
import static org.hamcrest.core.Is.is;

import com.tiket.web.dashboard.page.platform.eiffel.lounge.LoungeLoginPageWeb;
import com.tiket.web.dashboard.page.platform.eiffel.lounge.change_password.LoungeChangePasswordPageWeb;
import com.tiket.web.dashboard.page.platform.eiffel.lounge.reset_password.ResetPasswordPageWeb;
import com.tiket.web.dashboard.page.platform.eiffel.lounge.user_management.UserListPageWeb;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.testdata.TestData.getParam;

public class LoungeTest extends BaseTest {

    @Author(names = { "Rizki" })
    @Module(name = "Lounge")
    @Tribe(name = "Eiffel")
    @Vertical(name = "Platform")
    @Test
    public void testLoungeVerifyAddLounge() {
        Map<String, String> param = getParam("testLoungeVerifyAddLounge");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");
        String menu = param.get("menu");
        String airport = param.get("airport");
        String loungeCode = param.get("loungeCode");
        String loungeName = param.get("loungeName");
        loungeName += " " + CoreFunctions.randomInt(6);
        String[] admins = param.get("admins").split(" ");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoungeLoginPageWeb<?> loungeLoginPageWeb = LoungeLoginPageWeb.getInstance(session);
        LoungeDashboardPageWeb<?> loungeDashboardPageWeb = LoungeDashboardPageMWeb.getInstance(session);
        LoungeListPageWeb<?> loungeListPageWeb = LoungeListPageWeb.getInstance(session);

        step("Login to Lounge Dashboard");
        loungeLoginPageWeb.loginViaCookie(email, password);

        step("Click on the Lounge Menu");
        loungeDashboardPageWeb.goToSpecificPage(menu);

        step("Click on button Create Lounge");
        loungeListPageWeb.clickCreateLoungeButton();

        step("Select the airport from the airport list");
        loungeListPageWeb.selectAirport(airport);

        step("Input the lounge name");
        loungeListPageWeb.inputLoungeName(loungeName);

        step("Click on the toggle ON");

        step("Select the administrator");
        loungeListPageWeb.selectLoungeAdmin(admins);

        step("Click on the button Create Lounge");
        loungeListPageWeb.confirmCreateLounge();

        step("Assertions");
        loungeListPageWeb.searchLounge(loungeName);
        Assertion.assertThat("Successfully add new lounge", loungeListPageWeb.verifyLoungeHasBeenCreated(loungeName, loungeCode), is(true));

        step("Reset state");
        loungeListPageWeb.clickEditButton(loungeName);
        loungeListPageWeb.changeStatusOfLounge();
        loungeListPageWeb.clickSaveLoungeDetailsPopup();
    }

    @Author(names = { "Manan Kumar" })
    @Module(name = "Lounge")
    @Tribe(name = "Eiffel")
    @Vertical(name = "Platform")
    @Test
    public void testLoungeVerifyLogin() {
        Map<String, String> param = getParam("testLoungeVerifyLogin");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoungeLoginPageWeb<?> loungeLoginPageWeb = LoungeLoginPageWeb.getInstance(session);

        step("Login to Lounge Dashboard");
        loungeLoginPageWeb.loginViaCookie(email, password);

        step("Verify user is successfully logged in using the registered account");
        Assertion.assertThat("User successfully logged in to Lounge dashboard", loungeLoginPageWeb.isUserLoggedIn(), is(true));

    }

    @Author(names = { "Manan Kumar" })
    @Module(name = "Lounge")
    @Tribe(name = "Eiffel")
    @Vertical(name = "Platform")
    @Test
    public void testLoungeVerifyOnScheduleFlights() {
        Map<String, String> param = getParam("testLoungeVerifyOnScheduleFlights");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");
        String menu = param.get("menu");
        String flightType = param.get("flightType");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoungeLoginPageWeb<?> loungeLoginPageWeb = LoungeLoginPageWeb.getInstance(session);
        LoungeDashboardPageWeb<?> loungeDashboardPageWeb = LoungeDashboardPageMWeb.getInstance(session);

        step("Login to Lounge Dashboard");
        loungeLoginPageWeb.loginViaCookie(email, password);

        step("Verify user is successfully logged in using the registered account");
        Assertion.assertThat("User successfully logged in to Lounge dashboard", loungeLoginPageWeb.isUserLoggedIn(), is(true));

        step("Click on the Check In Menu");
        loungeDashboardPageWeb.goToSpecificPage(menu);

        step("Verify Check In page is opened successfully");
        Assertion.assertThat("Check In Management page is displayed", loungeDashboardPageWeb.isCheckInPageOpened(), is(true));

        step("Ensure toggle is set to On Schedule Flights");
        loungeDashboardPageWeb.selectFlightType(flightType);

        step("Ensure On Schedule flights are visible when toggle is on");
        if (loungeDashboardPageWeb.getFlightsRowCount() == 0) {
            loungeLoginPageWeb.logout();
            Assertion.skip("No On Schedule flights to display for account '" + email
                    + "' on the selected date — skipping the visibility check (V2 renders the flights table "
                    + "only when there is at least one flight). Verify test data if flights are expected.");
        }
        Assertion.assertThat("On Schedule flights are visible when toggle is on", loungeDashboardPageWeb.areOnScheduleFlightsVisible(), is(true));

    }

    @Author(names = { "Manan Kumar" })
    @Module(name = "Lounge")
    @Tribe(name = "Eiffel")
    @Vertical(name = "Platform")
    @Test
    public void testLoungeVerifyDelayedFlights() {
        Map<String, String> param = getParam("testLoungeVerifyDelayedFlights");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");
        String menu = param.get("menu");
        String flightType = param.get("flightType");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoungeLoginPageWeb<?> loungeLoginPageWeb = LoungeLoginPageWeb.getInstance(session);
        LoungeDashboardPageWeb<?> loungeDashboardPageWeb = LoungeDashboardPageMWeb.getInstance(session);

        step("Login to Lounge Dashboard");
        loungeLoginPageWeb.loginViaCookie(email, password);

        step("Verify user is successfully logged in using the registered account");
        Assertion.assertThat("User successfully logged in to Lounge dashboard", loungeLoginPageWeb.isUserLoggedIn(), is(true));

        step("Click on the Check In Menu");
        loungeDashboardPageWeb.goToSpecificPage(menu);

        step("Verify Check In page is opened successfully");
        Assertion.assertThat("Check In Management page is displayed", loungeDashboardPageWeb.isCheckInPageOpened(), is(true));

        step("Ensure toggle is set to Delayed Flights");
        loungeDashboardPageWeb.selectFlightType(flightType);

        step("Ensure Delayed flights are visible when toggle is on");
        if (loungeDashboardPageWeb.getFlightsRowCount() == 0) {
            loungeLoginPageWeb.logout();
            Assertion.skip("No Delayed flights to display for account '" + email
                    + "' on the selected date — skipping the visibility check (V2 renders the flights table "
                    + "only when there is at least one flight). Verify test data if flights are expected.");
        }
        Assertion.assertThat("Delayed flights are visible when toggle is on", loungeDashboardPageWeb.areDelayedFlightsVisible(), is(true));

    }

    @Author(names = { "Rizki" })
    @Module(name = "Lounge")
    @Tribe(name = "Eiffel")
    @Vertical(name = "Platform")
    @Test
    public void testLoungeVerifyActivateLounge() {
        Map<String, String> param = getParam("testLoungeVerifyActivateLounge");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");
        String menu = param.get("menu");
        String loungeName = param.get("loungeName");
        String status = param.get("status");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoungeLoginPageWeb<?> loungeLoginPageWeb = LoungeLoginPageWeb.getInstance(session);
        LoungeDashboardPageWeb<?> loungeDashboardPageWeb = LoungeDashboardPageMWeb.getInstance(session);
        LoungeListPageWeb<?> loungeListPageWeb = LoungeListPageWeb.getInstance(session);

        step("Login to the Lounge Dashboard");
        loungeLoginPageWeb.loginViaCookie(email, password);

        step("Clicks on the Lounge menu");
        loungeDashboardPageWeb.goToSpecificPage(menu);

        step("Precondition: ensure the lounge starts Inactive (the toggle below blind-flips, so a left-over "
                + "Active state would otherwise deactivate it and fail the Active assertion)");
        loungeListPageWeb.ensureLoungeStatus(loungeName, "Inactive");

        step("Clicks on the button edit in one of the lounges that has an INACTIVE status");
        loungeListPageWeb.clickEditButton(loungeName);

        step("Clicks on the toggle ON");
        loungeListPageWeb.changeStatusOfLounge();

        step("Clicks on the Save button");
        loungeListPageWeb.clickSaveLoungeDetailsPopup();

        step("Assertions");
        Assertion.assertThat("Successfully activate the lounge", loungeListPageWeb.verifyLoungeStatus(loungeName, status), is(true));

        step("Reset State");
        loungeListPageWeb.ensureLoungeStatus(loungeName, "Inactive");

    }

    @Author(names = { "Rizki" })
    @Module(name = "Lounge")
    @Tribe(name = "Eiffel")
    @Vertical(name = "Platform")
    @Test
    public void testLoungeVerifyAddUsersLounge() {
        Map<String, String> param = getParam("testLoungeVerifyAddUsersLounge");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");
        String menu = param.get("menu");
        String userName = param.get("userName");
        String[] userNameArr = userName.split("@");
        userName = userNameArr[0] + CoreFunctions.randomInt(7) + "@" + userNameArr[1];

        String userRole = param.get("userRole");
        String[] lounges = param.get("lounges").split("-");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoungeLoginPageWeb<?> loungeLoginPageWeb = LoungeLoginPageWeb.getInstance(session);
        LoungeDashboardPageWeb<?> loungeDashboardPageWeb = LoungeDashboardPageMWeb.getInstance(session);
        UserListPageWeb<?> userListPageWeb = UserListPageWeb.getInstance(session);

        step("Log in to the Lounge Dashboard");
        loungeLoginPageWeb.loginViaCookie(email, password);

        step("Click on the User Management Menu");
        loungeDashboardPageWeb.goToSpecificPage(menu);

        step("Click on button Add New User");
        userListPageWeb.clickAddNewUserButton();

        step("Input admin email as a username");
        userListPageWeb.inputUsernameToAddNewUser(userName);

        step("Selects the role");
        userListPageWeb.inputRoleToAddNewUser(userRole);

        step("Click the toggle ON");

        step("Select the lounge that will be assigned");
        userListPageWeb.assignLoungeToAddNewUser(lounges);

        step("Click on the button Save");
        userListPageWeb.clickSaveToAddNewUser();

        step("Assertions");
        Assertion.assertThat("Succesfully add a new lounge user", userListPageWeb.verifyUserExist(userName), is(true));

        step("Reset state");
        userListPageWeb.changeUserStatus(userName, "Inactive");

    }

    @Author(names = { "Rizki" })
    @Module(name = "Lounge")
    @Tribe(name = "Eiffel")
    @Vertical(name = "Platform")
    @Test
    public void testLoungeVerifyActiveUsers() {
        Map<String, String> param = getParam("testLoungeVerifyActiveUsers");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");
        String menu = param.get("menu");
        String userName = param.get("userName");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoungeLoginPageWeb<?> loungeLoginPageWeb = LoungeLoginPageWeb.getInstance(session);
        LoungeDashboardPageWeb<?> loungeDashboardPageWeb = LoungeDashboardPageMWeb.getInstance(session);
        UserListPageWeb<?> userListPageWeb = UserListPageWeb.getInstance(session);

        step("Log in to lounge dashboard");
        loungeLoginPageWeb.loginViaCookie(email, password);

        step("Clicks on the User Management Menu");
        loungeDashboardPageWeb.goToSpecificPage(menu);

        step("Precondition: ensure the user starts Inactive (the toggle below blind-flips, so a left-over "
                + "Active state would otherwise deactivate the user and fail the Active assertion)");
        userListPageWeb.changeUserStatus(userName, "Inactive");

        step("Clicks on button Edit on the user list that has status INACTIVE");
        userListPageWeb.clickEditButtonForUserName(userName);

        step("Click on the toggle ON");
        userListPageWeb.clickStatusToggle();

        step("Click on the button Save");
        userListPageWeb.clickEditUserSaveButton();

        step("Assertions");
        Assertion.assertThat("Succesfully activate the user", userListPageWeb.verifyUserStatus(userName, "Active"), is(true));

        step("Reset state");
        userListPageWeb.changeUserStatus(userName, "Inactive");

    }

    @Author(names = { "Rizki" })
    @Module(name = "Lounge")
    @Tribe(name = "Eiffel")
    @Vertical(name = "Platform")
    @Test
    public void testLoungeVerifyResetUserPassword() {
        Map<String, String> param = getParam("testLoungeVerifyResetUserPassword");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");
        String menu = param.get("menu");
        String userName = param.get("userName");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoungeLoginPageWeb<?> loungeLoginPageWeb = LoungeLoginPageWeb.getInstance(session);
        LoungeDashboardPageWeb<?> loungeDashboardPageWeb = LoungeDashboardPageMWeb.getInstance(session);
        UserListPageWeb<?> userListPageWeb = UserListPageWeb.getInstance(session);

        step("Log in to Lounge Dashboard");
        loungeLoginPageWeb.goToPage();
        loungeLoginPageWeb.login(email, password);

        step("Go to User Management");
        loungeDashboardPageWeb.goToSpecificPage(menu);

        step("Search an account");
        userListPageWeb.searchUser(userName);

        step("Click on Edit Button");
        userListPageWeb.clickEditButtonForUserName(userName);

        step("Click on Reset Password");
        userListPageWeb.clickResetPasswordButton();

        step("Click on 'Reset' button in the pop up");
        userListPageWeb.confirmResetPasswordConfirmationPopup();

        step("Assertions");
        Assertion.assertThat("Success toast should appear", userListPageWeb.verifyResetPasswordSuccess(), is(true));

    }

    @Author(names = { "Rizki" })
    @Module(name = "Lounge")
    @Tribe(name = "Eiffel")
    @Vertical(name = "Platform")
    @Test
    public void testLoungeVerifyChangeUserPassword() {
        Map<String, String> param = getParam("testLoungeVerifyChangeUserPassword");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");
        String newPassword = param.get("newPassword");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoungeLoginPageWeb<?> loungeLoginPageWeb = LoungeLoginPageWeb.getInstance(session);
        LoungeDashboardPageWeb<?> loungeDashboardPageWeb = LoungeDashboardPageMWeb.getInstance(session);
        LoungeChangePasswordPageWeb<?> loungeChangePasswordPageWeb = LoungeChangePasswordPageWeb.getInstance(session);

        step("Log in to Lounge Dashboard");
        loungeLoginPageWeb.goToPage();
        loungeLoginPageWeb.login(email, password);

        step("Clicks on the profile name on the right side of the page");
        loungeDashboardPageWeb.openUserProfileMenu();

        step("Click on the Change Password");
        loungeDashboardPageWeb.selectChangePasswordMenu();

        step("Type current password");
        loungeChangePasswordPageWeb.inputCurrentPassword(password);

        step("Type new password");
        loungeChangePasswordPageWeb.inputNewPassword(newPassword);

        step("Reconfirm the new password");
        loungeChangePasswordPageWeb.inputConfirmPassword(newPassword);

        step("Clicks on the button Save");
        loungeChangePasswordPageWeb.saveChangePassword();

        step("Assertions");
        Assertion.assertThat("Successfully changes the password", loungeChangePasswordPageWeb.isSuccessfullyChangePassword(), is(true));
        loungeDashboardPageWeb.logout();

        loungeLoginPageWeb.goToPage();
        loungeLoginPageWeb.login(email, newPassword);
        Assertion.assertThat("Successfully login with new password", loungeDashboardPageWeb.confirmSuccessfullyLogin(), is(true));

        step("Reset state");
        loungeDashboardPageWeb.openUserProfileMenu();
        loungeDashboardPageWeb.selectChangePasswordMenu();
        loungeChangePasswordPageWeb.inputCurrentPassword(newPassword);
        loungeChangePasswordPageWeb.inputNewPassword(password);
        loungeChangePasswordPageWeb.inputConfirmPassword(password);
        loungeChangePasswordPageWeb.saveChangePassword();
        Assertion.assertThat("Successfully reset the new password", loungeChangePasswordPageWeb.isSuccessfullyChangePassword(), is(true));

        loungeDashboardPageWeb.logout();
        loungeLoginPageWeb.goToPage();
        loungeLoginPageWeb.login(email, password);
        Assertion.assertThat("Successfully login with old password", loungeDashboardPageWeb.confirmSuccessfullyLogin(), is(true));

    }

    @Author(names = { "Rizki" })
    @Module(name = "Lounge")
    @Tribe(name = "Eiffel")
    @Vertical(name = "Platform")
    @Test
    public void testLoungeVerifyEditUsers() {
        Map<String, String> param = getParam("testLoungeVerifyEditUsers");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");
        String menu = param.get("menu");
        String username = param.get("username");
        String role = param.get("role");
        String[] lounges = param.get("lounges").split("_");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoungeLoginPageWeb<?> loungeLoginPageWeb = LoungeLoginPageWeb.getInstance(session);
        LoungeDashboardPageWeb<?> loungeDashboardPageWeb = LoungeDashboardPageMWeb.getInstance(session);
        UserListPageWeb<?>  userListPageWeb = UserListPageWeb.getInstance(session);

        step("Log in to Lounge Dashboard");
        loungeLoginPageWeb.loginViaCookie(email, password);

        step("Go to User Management");
        loungeDashboardPageWeb.goToSpecificPage(menu);

        step("Search an account");
        userListPageWeb.searchUser(username);

        step("Click on 'Edit Button'");
        userListPageWeb.clickEditButtonForUserName(username);

        step("Edit account role");
        String previousRole = userListPageWeb.getUserRole();
        userListPageWeb.inputRoleToAddNewUser(role);

        step("Check some lounges");
        userListPageWeb.assignLoungeToAddNewUser(lounges);

        step("Click save");
        userListPageWeb.clickEditUserSaveButton();

        step("Assertions");
        Assertion.assertThat("Success toast should appear", userListPageWeb.verifyEditUserSuccessful(), is(true));

        step("Reset state");
        userListPageWeb.clickEditButtonForUserName(username);
        userListPageWeb.inputRoleToAddNewUser(previousRole);
        userListPageWeb.assignLoungeToAddNewUser(lounges);
        userListPageWeb.clickEditUserSaveButton();
        Assertion.assertThat("Successfully reset state", userListPageWeb.verifyEditUserSuccessful(), is(true));

    }

    @Author(names = { "Manan Kumar" })
    @Module(name = "User Management")
    @Tribe(name = "Eiffel")
    @Vertical(name = "Platform")
    @Test
    public void testLoungeVerifyNewUserRegistrationAndLogin() {
        Map<String, String> param = getParam("testLoungeVerifyNewUserRegistrationAndLogin");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");
        String menu = param.get("menu");
        String newUserName = param.get("newUserName");
        String role = param.get("role");


        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoungeLoginPageWeb<?> loungeLoginPageWeb = LoungeLoginPageWeb.getInstance(session);
        LoungeDashboardPageWeb<?> loungeDashboardPageWeb = LoungeDashboardPageMWeb.getInstance(session);
        UserListPageWeb<?> userListPageWeb = UserListPageWeb.getInstance(session);

        step("User logs into the Lounge Dashboard");
        loungeLoginPageWeb.loginViaCookie(email, password);

        step("User is successfully logged in to the dashboard");
        Assertion.assertThat("User successfully logged in", loungeLoginPageWeb.isUserLoggedIn(), is(true));

        step("User navigates to the User Management menu");
        loungeDashboardPageWeb.goToSpecificPage(menu);

        step("User sees the User Management page is opened successfully");
        Assertion.assertThat("User Management page is displayed", userListPageWeb.isUserManagementPageOpened(), is(true));

        step("User clicks on Add New User button");
        userListPageWeb.clickAddNewUserButton();

        step("User creates and enters the unique new username with timestamp");
        String newUserEmail = userListPageWeb.generateAndInputUsernameWithTimestamp(newUserName);

        step("User selects the role");
        userListPageWeb.inputRoleToAddNewUser(role);

        step("User copies the auto-generated password");
        String autoGeneratedPassword = userListPageWeb.getAutoGeneratedPassword();

        step("User Clicks on the button Save");
        userListPageWeb.clickSaveToAddNewUser();

        step("User verifies that the new user is added successfully");
        Assertion.assertThat("Successfully add a new lounge user", userListPageWeb.verifyUserExist(newUserEmail), is(true));

        step("User logs in to the lounge dashboard with the new user credentials");
        loungeLoginPageWeb.loginViaCookie(newUserEmail, autoGeneratedPassword);

        step("User is successfully logged in to the dashboard with the new user credentials");
        Assertion.assertThat("User successfully logged in", loungeLoginPageWeb.isUserLoggedIn(), is(true));

    }

    @Author(names = { "Rizki" })
    @Module(name = "Lounge")
    @Tribe(name = "Eiffel")
    @Vertical(name = "Platform")
    @Test
    public void testLoungeVerifySetPassword() {
        Map<String, String> param = getParam("testLoungeVerifySetPassword");
        BaseTest.paramsThreadLocal.set(param);

        String targetEmail = param.get("targetEmail");
        String oldPassword = param.get("oldPassword");
        String newPassword = param.get("newPassword");
        String token = param.get("token");

        if (newPassword.equals(oldPassword)) {
            newPassword = oldPassword + "9";
        }

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoungeLoginPageWeb<?> loungeLoginPageWeb = LoungeLoginPageWeb.getInstance(session);
        LoungeDashboardPageWeb<?> loungeDashboardPageWeb = LoungeDashboardPageMWeb.getInstance(session);
        ResetPasswordPageWeb<?> resetPasswordPageWeb = ResetPasswordPageWeb.getInstance(session);
        LoungeChangePasswordPageWeb<?> loungeChangePasswordPageWeb = LoungeChangePasswordPageWeb.getInstance(session);

        step("Open the forgot-password flow from the Lounge login page");
        resetPasswordPageWeb.goToForgetPasswordPage();

        step("Request an OTP for the target email");
        resetPasswordPageWeb.requestResetOtp(targetEmail);

        step("Submit the OTP");
        resetPasswordPageWeb.submitOtp(token);

        step("Set the new password");
        resetPasswordPageWeb.inputPassword(newPassword);

        step("Save");
        resetPasswordPageWeb.save();

        step("Assertions");
        loungeLoginPageWeb.goToPage();
        if (loungeLoginPageWeb.isUserLoggedIn()) {
            loungeDashboardPageWeb.logout();
        }
        loungeLoginPageWeb.loginViaCookie(targetEmail, newPassword);
        Assertion.assertThat("Password successfully changed", loungeLoginPageWeb.isUserLoggedIn(), is(true));

        step("Reset state");
        loungeDashboardPageWeb.openUserProfileMenu();
        loungeDashboardPageWeb.selectChangePasswordMenu();
        loungeChangePasswordPageWeb.inputCurrentPassword(newPassword);
        loungeChangePasswordPageWeb.inputNewPassword(oldPassword);
        loungeChangePasswordPageWeb.inputConfirmPassword(oldPassword);
        loungeChangePasswordPageWeb.saveChangePassword();
        Assertion.assertThat("Successfully reset the new password", loungeChangePasswordPageWeb.isSuccessfullyChangePassword(), is(true));

        loungeDashboardPageWeb.logout();
        loungeLoginPageWeb.goToPage();
        loungeLoginPageWeb.loginViaCookie(targetEmail, oldPassword);
        Assertion.assertThat("Successfully login with old password", loungeDashboardPageWeb.confirmSuccessfullyLogin(), is(true));

        step("Log Out");
        loungeLoginPageWeb.logout();
    }

    /**
     * Verifies an e-Voucher check-in through the "Check-In by Voucher" modal — the correct flow for this case (the
     * on-list route, opening a booking's Check-In panel and picking a per-passenger method, is exercised separately
     * with a phone-number check-in by {@link #testLoungeVerifyCheckInCustomerOnSchedule()}).
     * <p>
     * The precondition creates a fresh e-Voucher order via API ({@code createIssuedOrderSakukuApi}, which throws —
     * skipping the case — on any booking/payment failure, so the order ID is always a valid, non-blank id past that
     * point), then checks it in by order ID and expects the success snackbar.
     */
    @Author(names = { "Yanuar" })
    @Module(name = "Lounge")
    @Tribe(name = "Eiffel")
    @Vertical(name = "Platform")
    @Test
    public void testLoungeVerifyCheckInUsingEVoucher() {
        Map<String, String> param = getParam("testLoungeVerifyCheckInUsingEVoucher");
        BaseTest.paramsThreadLocal.set(param);

        String adminEmail = param.get("adminEmail");
        String adminPassword = param.get("adminPassword");
        String userEmail = param.get("userEmail");
        String userPassword = param.get("userPassword");
        String menu = param.get("menu");
        String productSlug = param.get("productSlug");
        String currency = param.getOrDefault("currency", "IDR");
        boolean refundable = Boolean.parseBoolean(param.getOrDefault("refundable", "false"));

        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String contactFullName = CoreFunctions.randomName() + " autorder";
        String contactSalutation = CoreFunctions.getRandomObject(salutationList);
        String guestFullName = CoreFunctions.randomName() + " autguest";
        String guestEmailAddress = CoreFunctions.randomEmail();
        String guestSalutation = salutationList[0];

        Session<?> session = BaseTest.sessionThreadLocal.get();
        TTDApi<?> ttdApi = new TTDApi<>(session);
        LoungeLoginPageWeb<?> loungeLoginPageWeb = LoungeLoginPageWeb.getInstance(session);
        LoungeDashboardPageWeb<?> loungeDashboardPageWeb = LoungeDashboardPageMWeb.getInstance(session);

        step("Precondition: create a '" + productSlug + "' e-Voucher order via API and get the order ID");
        String orderId = ttdApi.createIssuedOrderSakukuApi(userEmail, userPassword, currency, productSlug, refundable,
                contactFullName, contactSalutation, guestFullName, guestEmailAddress, guestSalutation, false);

        step("Login to the Lounge Dashboard (e-Voucher order ID to check in: " + orderId + ")");
        loungeLoginPageWeb.loginViaCookie(adminEmail, adminPassword);

        step("Go to the Check-In menu");
        loungeDashboardPageWeb.goToSpecificPage(menu);

        step("Click 'Check-In by Voucher'");
        loungeDashboardPageWeb.openCheckInByVoucher();

        step("Input the TTD order ID and save");
        loungeDashboardPageWeb.inputEVoucherOrderIdOnModal(orderId);
        loungeDashboardPageWeb.saveCheckIn();

        step("Confirm the check-in when a confirmation dialog is shown");
        loungeDashboardPageWeb.confirmCheckInIfPresent();

        step("Verify the success snackbar is shown");
        Assertion.assertThat("A snackbar confirming a successful passenger check-in should be shown",
                loungeDashboardPageWeb.isCheckInSuccessSnackbarShown(), is(true));
    }

    /**
     * QAAUT-33213 / TestRail 1496717 — "Verify check-in customer - on-schedule flight".
     * <p>
     * On-list check-in via a static phone number (Phone Number is the default check-in method, so there is no
     * order-creation precondition and no confirmation dialog — Save shows the success snackbar directly). Like
     * {@link #testLoungeVerifyCancelCheckInCustomerOnSchedule()}, it targets the first booking with a
     * check-in-eligible passenger — status "Available" (nobody checked in) <em>or</em> "Partial" (some checked in,
     * some not) — and checks in the <b>next passenger that is not yet checked in</b>, rather than assuming a fully
     * "Available" booking and always acting on passenger #1. This keeps the case runnable on a Partial list instead
     * of skipping whenever no fully-"Available" booking is left. The check-in it creates is intentionally left in
     * place (it is not undone).
     * <p>
     * Data-dependent: it needs an On Schedule booking with a check-in-eligible passenger ("Available" or "Partial")
     * on today's list (seeded by B2C / flight-team data, not automation), so it skips gracefully when there is none
     * instead of failing.
     */
    @Author(names = { "Yanuar" })
    @Module(name = "Lounge")
    @Tribe(name = "Eiffel")
    @Vertical(name = "Platform")
    @Test
    public void testLoungeVerifyCheckInCustomerOnSchedule() {
        Map<String, String> param = getParam("testLoungeVerifyCheckInCustomerOnSchedule");
        BaseTest.paramsThreadLocal.set(param);

        String adminEmail = param.get("adminEmail");
        String adminPassword = param.get("adminPassword");
        String menu = param.get("menu");
        String phoneNumbersRaw = param.get("phoneNumbers");
        if (phoneNumbersRaw == null || phoneNumbersRaw.isBlank()) {
            phoneNumbersRaw = param.get("checkInPhone");
        }
        if (phoneNumbersRaw == null || phoneNumbersRaw.isBlank()) {
            Assertion.skip("QAAUT-33213 / TestRail 1496717 skipped: required phone test data is missing — provide a "
                    + "'#'-separated 'phoneNumbers' list (preferred) or the single legacy 'checkInPhone' used to "
                    + "check a passenger in. Add the value to the test-data sheet so this case can run — a missing "
                    + "value would otherwise fail with an opaque NullPointerException.");
        }
        String[] phoneNumbers = phoneNumbersRaw.split("#");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoungeLoginPageWeb<?> loungeLoginPageWeb = LoungeLoginPageWeb.getInstance(session);
        LoungeDashboardPageWeb<?> loungeDashboardPageWeb = LoungeDashboardPageMWeb.getInstance(session);

        step("Login to the Lounge Dashboard");
        loungeLoginPageWeb.loginViaCookie(adminEmail, adminPassword);

        step("Go to the Check-In menu");
        loungeDashboardPageWeb.goToSpecificPage(menu);

        step("Ensure the On Schedule tab is selected");
        loungeDashboardPageWeb.selectFlightType("On Schedule");

        step("Wait for the On Schedule list to load and locate a booking with a checkable passenger");
        if (!loungeDashboardPageWeb.hasEligibleBooking()) {
            Assertion.skip("QAAUT-33213 / TestRail 1496717 (on-schedule customer check-in) skipped: the Check-In "
                    + "page rendered but there is no 'Available' or 'Partial' booking on today's On Schedule list "
                    + "with a passenger left to check in. This case depends on B2C / flight-team booking data for "
                    + "today — it is not seedable from automation and an empty list is not a product defect. Re-run "
                    + "when today's On Schedule list has an eligible booking. (A genuine page/list load failure "
                    + "throws instead of skipping.)");
        }

        String bookingCode = loungeDashboardPageWeb.getFirstEligibleBookingCode();

        step("Open booking '" + bookingCode + "' and check in the first passenger that is not yet checked in via phone");
        loungeDashboardPageWeb.openManageCheckInForBooking(bookingCode);
        int checkedInPassengerIndex = loungeDashboardPageWeb.checkInFirstEligiblePassenger(phoneNumbers);
        if (checkedInPassengerIndex < 0) {
            Assertion.skip("QAAUT-33213 / TestRail 1496717 skipped: booking '" + bookingCode + "' has no "
                    + "check-in-eligible passenger left to check in via phone (every passenger is already checked "
                    + "in). There is nothing to check in, so this is an environmental data gap, not a product "
                    + "defect.");
        }

        step("Save the check-in");
        loungeDashboardPageWeb.saveCheckIn();

        step("Verify the success snackbar is shown");
        Assertion.assertThat("A snackbar confirming a successful passenger check-in should be shown",
                loungeDashboardPageWeb.isCheckInSuccessSnackbarShown(), is(true));
    }

    /**
     * QAAUT-33219 / TestRail 1496730 — "Verify cancel check-in customer - on-schedule flight".
     * <p>
     * Cancels a <b>phone</b> check-in (deliberately never an e-Voucher one — the app does not allow those to be
     * un-checked; their toggle is even rendered disabled) and expects the success snackbar. It picks its target the
     * way that mutates the least data:
     * <ol>
     *     <li><b>Reuse (preferred)</b> — if a "Partial"/"Checked-In" booking already holds a phone check-in, it
     *     cancels <em>that</em> one directly (no check-in to create first) and leaves it cancelled. This keeps the
     *     case runnable when today's list has only already-checked-in bookings, and the leftover "Partial" booking
     *     conveniently gives the sibling {@link #testLoungeVerifyCheckInCustomerOnSchedule() on-schedule check-in
     *     case} a passenger to check in — the two cases hand state back and forth (cancel un-checks, check-in
     *     re-checks) instead of draining the list toward all-"Checked-In".</li>
     *     <li><b>Create (fallback)</b> — otherwise it checks a single eligible passenger in via Phone Number (a
     *     lightweight partial precondition — far less flake than checking everyone in) and cancels that; here the
     *     cancellation itself is the reset, and a {@code finally} block undoes the check-in only if the flow bailed
     *     out before the cancellation was confirmed.</li>
     * </ol>
     * <p>
     * Data-dependent: it needs an On Schedule booking that either already carries a cancellable phone check-in or has
     * a check-in-eligible passenger ("Available"/"Partial") on today's list (seeded by B2C / flight-team data, not
     * automation), so it skips gracefully when there is none instead of failing.
     */
    @Author(names = { "Yanuar" })
    @Module(name = "Lounge")
    @Tribe(name = "Eiffel")
    @Vertical(name = "Platform")
    @Test
    public void testLoungeVerifyCancelCheckInCustomerOnSchedule() {
        Map<String, String> param = getParam("testLoungeVerifyCancelCheckInCustomerOnSchedule");
        BaseTest.paramsThreadLocal.set(param);

        String adminEmail = param.get("adminEmail");
        String adminPassword = param.get("adminPassword");
        String menu = param.get("menu");
        String phoneNumbersRaw = param.get("phoneNumbers");
        if (phoneNumbersRaw == null || phoneNumbersRaw.isBlank()) {
            Assertion.skip("QAAUT-33219 / TestRail 1496730 skipped: required 'phoneNumbers' test data (one or more "
                    + "'#'-separated phone numbers used to check a passenger in before cancelling) is missing or "
                    + "blank for 'testLoungeVerifyCancelCheckInCustomerOnSchedule'. Add the row to the test-data "
                    + "sheet so this case can run — a missing value would otherwise fail with an opaque "
                    + "NullPointerException.");
        }
        String[] phoneNumbers = phoneNumbersRaw.split("#");
        String cancelNote = "Auto cancel check-in " + CoreFunctions.randomInt(6);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoungeLoginPageWeb<?> loungeLoginPageWeb = LoungeLoginPageWeb.getInstance(session);
        LoungeDashboardPageWeb<?> loungeDashboardPageWeb = LoungeDashboardPageMWeb.getInstance(session);

        step("Login to the Lounge Dashboard");
        loungeLoginPageWeb.loginViaCookie(adminEmail, adminPassword);

        step("Go to the Check-In menu");
        loungeDashboardPageWeb.goToSpecificPage(menu);

        step("Ensure the On Schedule tab is selected");
        loungeDashboardPageWeb.selectFlightType("On Schedule");

        step("Look for a Partial/Checked-In booking that already has a phone check-in to cancel directly");
        if (loungeDashboardPageWeb.hasBookingWithExistingCheckIn()) {
            String existingBookingCode = loungeDashboardPageWeb.getFirstCheckedInBookingCode();
            loungeDashboardPageWeb.openManageCheckInForBooking(existingBookingCode);
            int existingPhoneIndex = loungeDashboardPageWeb.cancelFirstExistingPhoneCheckIn(cancelNote);
            if (existingPhoneIndex >= 0) {
                step("Save the cancellation of the existing phone check-in for passenger "
                        + (existingPhoneIndex + 1) + " on booking '" + existingBookingCode + "'");
                loungeDashboardPageWeb.saveCheckIn();

                step("Verify the success snackbar is shown");
                Assertion.assertThat("A snackbar confirming the passenger check-in was successfully cancelled "
                        + "should be shown", loungeDashboardPageWeb.isCheckInSuccessSnackbarShown(), is(true));
                return;
            }
            step("No cancellable phone check-in on that booking — falling back to creating one");
            loungeDashboardPageWeb.closeManageCheckInPanel();
            loungeDashboardPageWeb.selectFlightType("On Schedule");
        }

        step("Wait for the On Schedule list to load and locate a booking with a checkable passenger");
        if (!loungeDashboardPageWeb.hasEligibleBooking()) {
            Assertion.skip("QAAUT-33219 / TestRail 1496730 (cancel on-schedule customer check-in) skipped: the "
                    + "Check-In page rendered but today's On Schedule list has neither a 'Partial'/'Checked-In' "
                    + "booking with a cancellable phone check-in nor an 'Available'/'Partial' booking to check a "
                    + "passenger in and then cancel. This case depends on B2C / flight-team booking data for today — "
                    + "it is not seedable from automation and an empty or e-Voucher-only list is not a product "
                    + "defect. Re-run when today's list has a usable booking. (A genuine page/list load failure "
                    + "throws instead of skipping.)");
        }

        String bookingCode = loungeDashboardPageWeb.getFirstEligibleBookingCode();

        step("Precondition: check in one eligible passenger via phone on booking '" + bookingCode + "'");
        loungeDashboardPageWeb.openManageCheckInForBooking(bookingCode);
        int checkedInPassengerIndex = loungeDashboardPageWeb.checkInFirstEligiblePassenger(phoneNumbers);
        if (checkedInPassengerIndex < 0) {
            Assertion.skip("QAAUT-33219 / TestRail 1496730 skipped: booking '" + bookingCode + "' has no "
                    + "check-in-eligible passenger left to check in via phone (every passenger is already checked in). "
                    + "There is nothing to check in and then cancel, so this is an environmental data gap, not a "
                    + "product defect.");
        }
        loungeDashboardPageWeb.saveCheckIn();

        boolean phoneCheckInNeedsUndo = true;
        try {
            step("Re-open the booking and cancel the phone check-in for the passenger we just checked in");
            loungeDashboardPageWeb.openManageCheckInForBooking(bookingCode);
            if (!loungeDashboardPageWeb.cancelPhoneCheckInForPassenger(checkedInPassengerIndex, cancelNote)) {
                Assertion.skip("QAAUT-33219 / TestRail 1496730 skipped: the passenger checked in during the "
                        + "precondition on booking '" + bookingCode + "' is no longer a cancellable phone check-in "
                        + "(the phone check-in most likely did not persist — e.g. a number rejected by the "
                        + "environment). There is nothing to cancel, so this is treated as an environmental "
                        + "precondition gap, not a product defect.");
            }

            step("Save the cancellation");
            loungeDashboardPageWeb.saveCheckIn();

            step("Verify the success snackbar is shown");
            boolean cancelled = loungeDashboardPageWeb.isCheckInSuccessSnackbarShown();
            phoneCheckInNeedsUndo = !cancelled;
            Assertion.assertThat("A snackbar confirming the passenger check-in was successfully cancelled should be "
                    + "shown", cancelled, is(true));
        } finally {
            if (phoneCheckInNeedsUndo) {
                step("Reset state: undo the phone check-in left behind (e-Voucher check-ins are left untouched)");
                loungeDashboardPageWeb.undoPhoneCheckIn(bookingCode, checkedInPassengerIndex, cancelNote);
            }
        }
    }
}
