package com.tiket.web.dashboard.test.platform.membership;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.platform.membership.ForgotPasswordPage;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.testdata.TestData.getParam;

public class ForgotPasswordTest extends BaseTest {
    @Author(names = {"Fandy"})
    @Module(name = "LoginPageWeb")
    @Tribe(name = "Liberty")
    @Vertical(name = "Platform")
    @Test
    public void testAdminForgotPassword() throws InterruptedException{
        Map<String, String> sheetData = getParam("testAdminForgotPassword");
        ForgotPasswordPage<?> forgotPasswordPage = new ForgotPasswordPage<>(BaseTest.sessionThreadLocal.get());
        forgotPasswordPage.setSheetData(sheetData);

        String username = sheetData.get("username");
        int OTP = Integer.parseInt(sheetData.get("OTP"));
        String generatedPassword = forgotPasswordPage.getNewPassword();

        step("Open login page & click forgot password");
        forgotPasswordPage.clickForgotPasswordButtonPage();
        forgotPasswordPage.isForgotTitleExist();

        step("Submit email & input otp");
        forgotPasswordPage.inputEmailOTP(username, OTP);

        step("Input new password");
        forgotPasswordPage.isSetNewPasswordTitleExist();
        forgotPasswordPage.inputNewPassword();

        step("Login email & input new password");
        forgotPasswordPage.goToLoginPage();
        forgotPasswordPage.inputUsernameNewPassword(username, generatedPassword);

        step("Verify logged in");
        sheetData = forgotPasswordPage.getSheetData();
        forgotPasswordPage.verifyLoggedIn();
    }
}
