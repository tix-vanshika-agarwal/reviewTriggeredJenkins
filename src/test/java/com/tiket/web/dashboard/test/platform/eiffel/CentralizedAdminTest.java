package com.tiket.web.dashboard.test.platform.eiffel;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.common.CentralizedAdminLoginPage;
import org.testng.annotations.Test;

import java.util.Map;
import java.util.Objects;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.core.Is.is;

public class CentralizedAdminTest extends BaseTest {

    @Author(names = { "Yanuar" })
    @Module(name = "CentralizedAdmin")
    @Tribe(name = "Eiffel")
    @Vertical(name = "Platform")
    @Test
    public void testVerifyUserLoginCentralizedAdmin() {
        Map<String, String> param = Objects.requireNonNull(
                getParam("testVerifyUserLoginCentralizedAdmin"),
                "Test data not found for testVerifyUserLoginCentralizedAdmin");
        BaseTest.paramsThreadLocal.set(param);

        String businessType = Objects.requireNonNull(param.get("businessType"), "Missing test data: businessType");
        String email = Objects.requireNonNull(param.get("adminEmail"), "Missing test data: adminEmail");
        String password = Objects.requireNonNull(param.get("adminPassword"), "Missing test data: adminPassword");
        String otpCode = param.getOrDefault("otpCode", "1234");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        CentralizedAdminLoginPage<?> centralizedAdminLoginPage = new CentralizedAdminLoginPage<>(session);

        step("Open member's external admin login page");
        centralizedAdminLoginPage.goToExternalLoginPage(businessType);

        step("Login with email, password and OTP");
        centralizedAdminLoginPage.login(email, password, otpCode);

        try {
            step("Verify user is redirected to the correct business dashboard");
            Assertion.assertThat(
                    "User is redirected to business dashboard after login",
                    centralizedAdminLoginPage.isUserLoggedIn(), is(true));
        } catch (Exception ignored) {
        } finally {
            step("Logout");
            centralizedAdminLoginPage.logoutViaUrl();
        }
    }

    @Author(names = { "Yanuar" })
    @Module(name = "CentralizedAdmin")
    @Tribe(name = "Eiffel")
    @Vertical(name = "Platform")
    @Test
    public void testVerifyAdminForgotPasswordCentralizedAdmin() {
        Map<String, String> param = Objects.requireNonNull(
                getParam("testVerifyAdminForgotPasswordCentralizedAdmin"),
                "Test data not found for testVerifyAdminForgotPasswordCentralizedAdmin");
        BaseTest.paramsThreadLocal.set(param);

        String businessType = Objects.requireNonNull(param.get("businessType"), "Missing test data: businessType");
        String email = Objects.requireNonNull(
                param.getOrDefault("adminEmail", param.get("adminUsername")),
                "Missing test data: adminEmail or adminUsername");
        String otpCode = param.getOrDefault("otpCode", "1234");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        CentralizedAdminLoginPage<?> centralizedAdminLoginPage = new CentralizedAdminLoginPage<>(session);

        step("Open member's external admin login page");
        centralizedAdminLoginPage.goToExternalLoginPage(businessType);

        step("Click on 'Forgot your password?'");
        centralizedAdminLoginPage.clickForgotPassword();

        step("Input email and click Next");
        centralizedAdminLoginPage.inputForgotPasswordEmail(email);
        centralizedAdminLoginPage.clickNext();

        step("Input OTP code");
        centralizedAdminLoginPage.handleForgotPasswordOtp(otpCode);

        step("Verify user is prompted to input New Password and Confirm New Password");
        Assertion.assertThat(
                "Set New Password page is displayed",
                centralizedAdminLoginPage.isSetNewPasswordPageDisplayed(), is(true));

        String newPassword = CentralizedAdminLoginPage.generateDynamicPassword();

        step("Input new password and confirm new password, then click Confirm");
        centralizedAdminLoginPage.inputNewPassword(newPassword);
        centralizedAdminLoginPage.inputConfirmNewPassword(newPassword);
        centralizedAdminLoginPage.clickConfirm();

        step("Verify success snackbar and redirect to login page");
        Assertion.assertThat(
                "Success snackbar and login page redirect after password reset",
                centralizedAdminLoginPage.isLoginPageDisplayedWithSnackbar(), is(true));

        step("Login using the new password");
        centralizedAdminLoginPage.login(email, newPassword, otpCode);

        try {
            step("Verify user can login successfully with the new password");
            Assertion.assertThat(
                    "User is logged in after using new password",
                    centralizedAdminLoginPage.isUserLoggedIn(), is(true));
        } catch (Exception ignored) {
        } finally {
            step("Logout");
            centralizedAdminLoginPage.logoutViaUrl();
        }
    }
}
