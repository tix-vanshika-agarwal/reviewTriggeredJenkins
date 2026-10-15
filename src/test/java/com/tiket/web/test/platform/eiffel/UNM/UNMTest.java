package com.tiket.web.test.platform.eiffel.UNM;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.apiPage.LoyaltyApi;
import com.tiket.apiPage.MemberApi;
import com.tiket.core.CoreFunctions;
import com.tiket.apiPage.Unm;
import com.tiket.model.Session;
import com.tiket.pojo.member.RegisterNewAccountData;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.page.accommodation.hotels.SRP.HotelSearchResultPage;
import com.tiket.web.page.accommodation.hotels.SRP.SRPPageWeb;
import com.tiket.web.page.accommodation.hotels.hotelLandingPage.HotelLandingPageWeb;
import com.tiket.web.page.common.home.HomePageWeb;
import com.tiket.web.page.common.home.LoginPageWeb;
import com.tiket.web.page.common.home.Setting_Page.SettingsPageWeb;
import com.tiket.web.page.nft.train.bookingForm_Page.TrainBookingFormWeb;
import com.tiket.web.page.nft.train.searchResult_Page.TrainSearchResultPageWeb;
import com.tiket.web.page.nft.train.search_Page.TrainSearchPageWeb;
import com.tiket.web.page.platform.eiffel.UNM.AccountCentrePageWeb;
import com.tiket.web.page.platform.eiffel.UNM.AccountProfilePageWeb;
import com.tiket.web.page.platform.eiffel.UNM.UNMPageWeb;
import com.tiket.web.page.platform.eiffel.loyalty.LoyaltyDependencyPageWeb;
import com.tiket.web.page.platform.eiffel.loyalty.LoyaltyPointPageWeb;
import com.tiket.web.page.platform.eiffel.membership.AccountPageWeb;
import com.tiket.web.page.platform.payment.PaymentPageWeb;
import org.eclipse.jgit.util.StringUtils;
import org.hamcrest.core.Is;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import javax.security.auth.login.FailedLoginException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.tiket.core.CoreFunctions.generateRandomDate;
import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public class UNMTest extends BaseTest {

    @Author(names = { "Ishika" })
    @Module(name = "Homepage")
    @Tribe(name = "UNM")
    @Vertical(name = "Platform")
    @Test
    public void loginChangeAccountNameWeb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("loginChangeAccountNameWeb");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        SettingsPageWeb<?> settingsPage = SettingsPageWeb.getInstance(session);
        AccountProfilePageWeb<?> accountProfilePage = AccountProfilePageWeb.getInstance(session);
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("User open home page");
        homePage.goToHomePage();

        step("Go to account page");
        loginPage.goToAccountPage();

        step("login using { " + email + " }");
        loginPage.loginIfNotWithSameEmail(email, password, otp, 1);

        step("go to tiket home page");
        homePage.goToHomePage();

        step("Go to account page");
        loginPage.goToAccountPage();

        step("Click on \"To Account Centre\"");
        settingsPage.openAccountCenter();

        step("Click Edit Icon");
        settingsPage.clickEditIcon();

        step("Edit Name");
        accountProfilePage.editName();

        step("Verify Name");
        Assertion.assertThat("Profile with name updated", accountProfilePage.verifyName(), is(true));

    }

    @Author(names = { "Ishika" })
    @Module(name = "Homepage")
    @Tribe(name = "UNM")
    @Vertical(name = "Platform")
    @Test
    public void testChangePasswordLogin() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testChangePasswordLogin");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        SettingsPageWeb<?> settingsPage = SettingsPageWeb.getInstance(session);
        AccountCentrePageWeb<?> accountCentrePage = AccountCentrePageWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        String oldPassword = param.get("oldPassword");
        String newPassword = param.get("newPassword");
        String success_message = param.get("success_message");

        step("User open home page");
        homePage.goToHomePage();

        step("Go to account page");
        loginPage.goToAccountPage();

        step("login in using { " + username + " }");
        loginPage.loginIfNotWithSameEmail(username, password, otp, 1);

        step("go to tiket home page");
        homePage.goToHomePage();

        step("Go to account page");
        loginPage.goToAccountPage();

        step("Click on \"To Account Centre\"");
        settingsPage.openAccountCenter();

        step("Open Change Password Page");
        accountCentrePage.openChangePasswordPage();

        step("Change Password");
        accountCentrePage.changePassword(oldPassword, newPassword);

        Assertion.assertThat("Redirected to staging site after password changed sucessfully",
                accountCentrePage.verifyRedirection(), is("https://account.preprod.bliblitiket.dev/bridge"));

        step("User open home page");
        homePage.goToHomePage();

        step("Go to account page");
        loginPage.goToAccountPage();

        step("login in using { " + username + " }");
        Boolean loginValue = loginPage.loginIntoWeb(username, newPassword, otp, 1);

        Assertion.assertThat("Login successfull", loginValue, is(true));

    }

    @Author(names = { "Ishika" })
    @Module(name = "Homepage")
    @Tribe(name = "UNM")
    @Vertical(name = "Platform")
    @Test
    public void testLoginChangeNotificationLanguageWeb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginChangeNotificationLanguageWeb");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        SettingsPageWeb<?> settingsPage = SettingsPageWeb.getInstance(session);
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("User open home page");
        homePage.goToHomePage();

        step("Go to account page");
        loginPage.goToAccountPage();

        step("login using { " + email + " }");
        loginPage.loginIfNotWithSameEmail(email, password, otp, 1);

        step("go to tiket home page");
        homePage.goToHomePage();

        step("Go to account page");
        loginPage.goToAccountPage();

        step("Click on \"To Account Centre\"");
        settingsPage.openAccountCenter();

        step("User Change Language");
        String oldLanguage = settingsPage.changeLanguage();

        step("Verify Name");
        Assertion.assertThat("Profile with Notification Language updated", settingsPage.verifyLanguage(oldLanguage),
                is(true));

    }

    @Author(names = { "Ishika" })
    @Module(name = "LoginPage")
    @Tribe(name = "UNM")
    @Vertical(name = "Platform")
    @Test
    public void testForgotPasswordUsingPNWeb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testForgotPasswordUsingPNWeb");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        String email = param.get("email");
        String otp = param.get("otp");
        String newPassword = param.get("password");
        String pn = param.get("pn");

        step("User open home page");
        homePage.goToHomePage();

        step("Go to account page");
        loginPage.goToAccountPage();

        step("Log Out From Account Page if logged in");
        loginPage.logOutFromAccountPage();

        step("enter { " + email + " } and click Forgot Password");
        loginPage.enterEmailAndClickForgotPassword(email);

        step("Input New Password");
        loginPage.enterNewPassword(newPassword, pn, otp);

        Assertion.assertThat("New Password Updated", loginPage.isPasswordUpdated(), is(true));

        step("able to login using { " + email + " } with new password");
        homePage.goToHomePage();
        Boolean loginValue = loginPage.loginIntoWeb(email, newPassword, otp, 1);

        Assertion.assertThat("Login successfull", loginValue, is(true));

    }

    @Author(names = { "Ishika" })
    @Module(name = "Homepage")
    @Tribe(name = "UNM")
    @Vertical(name = "Platform")
    @Test
    public void testChangeIntoNewEmail() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testChangeIntoNewEmail");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        SettingsPageWeb<?> settingsPage = SettingsPageWeb.getInstance(session);
        AccountProfilePageWeb<?> accountProfilePage = AccountProfilePageWeb.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        String randomEmail = CoreFunctions.randomEmail();

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Go to account page");
        loginPage.goToAccountPage();

        step("Click on \"To Account Centre\"");
        settingsPage.openAccountCenter();
        String accessToken = accountProfilePage.getCurrentUNMAccessToken();

        step("Click Edit Icon");
        settingsPage.clickEditIcon();

        step("Change Email");
        accountProfilePage.changeEmail(randomEmail, otp);

        step("Verify email");
        Boolean value = accountProfilePage.verifyEmail(randomEmail);

        Assertion.assertThat("Email Changed To New Email", value, is(true));

        step("user change email via API");
        accountProfilePage.changeEmailAPI(email, randomEmail, accessToken);
    }

    @Author(names = { "Mridul" })
    @Module(name = "Member")
    @Tribe(name = "UNM")
    @Vertical(name = "Platform")
    @Test
    public void testVerifyUnverifiedEmailLoggedInWeb() throws FailedLoginException, InterruptedException
    {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testVerifyUnverifiedEmailLoggedInWeb");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        SettingsPageWeb<?> settingsPage = SettingsPageWeb.getInstance(session);
        AccountProfilePageWeb<?> accountProfilePage = AccountProfilePageWeb.getInstance(session);
        Unm<?> unm = new Unm<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("User open home page");
        homePage.goToHomePage();

        step("Logout via URL");
        loginPage.logoutViaUrl();

        step("Login first using { " + email + " } (mandatory before unverifying the email)");
        loginPage.loginIntoWeb(email, password,otp,3);

        step("Given user email is not verified (unverify via admin API)");
        String unverifiedEmail = unm.unverifyEmailViaAdminApi(email, password);
        Assertion.assertThat("Account email is now unverified: " + unverifiedEmail,unverifiedEmail,is(email));

        step("When user go to UNM account center");
        loginPage.goToAccountPage();
        settingsPage.openAccountCenter();

        step("And click on \"Edit\"");
        settingsPage.clickEditIcon();

        step("And click on \"Verify\" on the email section");
        accountProfilePage.clickVerifyEmailOnEmailSection();

        step("And follow the email verification flow");
        accountProfilePage.followEmailVerificationFlow(otp);

        step("Then after success, user should see the email is verified");
        Assertion.assertThat("Email should be verified after the verification flow", accountProfilePage.isEmailVerified(), is(true));
    }

    @Author(names = { "Ishika" })
    @Module(name = "Homepage")
    @Tribe(name = "UNM")
    @Vertical(name = "Platform")
    @Test
    public void testChangeIntoNewPNLoggedIn() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testChangeIntoNewPNLoggedIn");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        SettingsPageWeb<?> settingsPage = SettingsPageWeb.getInstance(session);
        AccountCentrePageWeb<?> accountCentrePage = AccountCentrePageWeb.getInstance(session);
        AccountProfilePageWeb<?> accountProfilePage = AccountProfilePageWeb.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        String randomPN = "89" + CoreFunctions.randomInt(8);

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Go to account page");
        loginPage.goToAccountPage();

        step("Click on \"To Account Centre\"");
        settingsPage.openAccountCenter();

        step("Click Edit Icon");
        settingsPage.clickEditIcon();

        step("Change PN");
        accountProfilePage.changePN(randomPN, otp);

        step("Verify PN");
        Boolean value = accountProfilePage.verifyPN(randomPN);

        Assertion.assertThat("PN Changed To New PN", value, is(true));
    }

    @Author(names = { "Ishika" })
    @Module(name = "Homepage")
    @Tribe(name = "UNM")
    @Vertical(name = "Platform")
    @Test
    public void testloginUsingSuspendedAccountNonLogIn() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testloginUsingSuspendedAccountNonLogIn");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        SettingsPageWeb<?> settingsPage = SettingsPageWeb.getInstance(session);
        AccountCentrePageWeb<?> accountCentrePage = AccountCentrePageWeb.getInstance(session);
        AccountProfilePageWeb<?> accountProfilePage = AccountProfilePageWeb.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("User open home page");
        homePage.goToHomePage();

        step("Go to account page");
        loginPage.goToAccountPage();

        step("login in using { " + email + " } for suspended account");
        Boolean value = loginPage.loginIntoSuspendedAccount(email);

        Assertion.assertThat("Suspended User Can Not Login", value, is(true));

    }

    @Author(names = { "Ishika" })
    @Module(name = "Member")
    @Tribe(name = "UNM")
    @Vertical(name = "Platform")
    @Test
    public void testChangeGenderLoggedIn() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testChangeGenderLoggedIn");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        SettingsPageWeb<?> settingsPage = SettingsPageWeb.getInstance(session);
        AccountCentrePageWeb<?> accountCentrePage = AccountCentrePageWeb.getInstance(session);
        AccountProfilePageWeb<?> accountProfilePage = AccountProfilePageWeb.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("User open home page");
        homePage.goToHomePage();

        step("Go to account page");
        loginPage.goToAccountPage();

        step("login in using { " + email + " }");
        loginPage.loginIfNotWithSameEmail(email, password, otp, 1);

        step("go to tiket home page");
        homePage.goToHomePage();

        step("Go to account page");
        loginPage.goToAccountPage();

        step("Click on \"To Account Centre\"");
        settingsPage.openAccountCenter();

        step("Click Edit Icon");
        settingsPage.clickEditIcon();

        step("Get Gender before changing ");
        String initialGender = accountProfilePage.getGenderText();

        step("Change Gender");
        accountProfilePage.changeGender(initialGender);

        step("Get Gender After changing ");
        String finalGender = accountProfilePage.getGenderText();
        Assertion.assertThat("Gender changed successfully", initialGender, not(finalGender));
    }

    @Author(names = { "Ishika" })
    @Module(name = "Member")
    @Tribe(name = "UNM")
    @Vertical(name = "Platform")
    @Test
    public void testChangeDOBLoggedIn() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testChangeDOBLoggedIn");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        SettingsPageWeb<?> settingsPage = SettingsPageWeb.getInstance(session);
        AccountProfilePageWeb<?> accountProfilePage = AccountProfilePageWeb.getInstance(session);
        Unm<?> unm = new Unm<>(session);

        step("Register new user");
        RegisterNewAccountData accountData = unm.registerNewAccountViaApiUNM();
        String email = accountData.email;
        String password = accountData.password;
        String otp = param.get("otp");
        String initialDOB = param.get("initialDOB");

        step("User open home page");
        homePage.goToHomePage();

        step("Go to account page");
        loginPage.goToAccountPage();

        step("login in using { " + email + " }");
        loginPage.loginIfNotWithSameEmail(email, password, otp, 1);

        step("go to tiket home page");
        homePage.goToHomePage();

        step("Go to account page");
        loginPage.goToAccountPage();

        step("Click on \"To Account Centre\"");
        settingsPage.openAccountCenter();

        step("Click Edit Icon");
        settingsPage.clickEditIcon();

        step("change DOB");
        accountProfilePage.changeDOB();

        step("Get DOB After changing ");
        String finalDOB = accountProfilePage.getDOBText();

        Assertion.assertThat("DOB changed successfully", initialDOB, not(finalDOB));

        step("user click on Delete Account");
        accountProfilePage.deleteAccount();

        step("Confirm delete account");
        accountProfilePage.confirmDeleteAccount();
        Assertion.assertThat("user should see \"See you again\" bottomsheet", accountProfilePage.isSeeYouAgain(),
                is(true));
    }

    @Author(names = { "Ishika" })
    @Module(name = "Member")
    @Tribe(name = "UNM")
    @Vertical(name = "Platform")
    @Test
    public void testDeleteAccountLoggedIn() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testDeleteAccountLoggedIn");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        SettingsPageWeb<?> settingsPage = SettingsPageWeb.getInstance(session);
        AccountProfilePageWeb<?> accountProfilePage = AccountProfilePageWeb.getInstance(session);
        Unm<?> unm = new Unm<>(session);

        step("Register new user");
        RegisterNewAccountData accountData = unm.registerNewAccountViaApiUNM();
        String email = accountData.email;
        String password = accountData.password;
        String otp = param.get("otp");

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Go to account page");
        loginPage.goToAccountPage();

        step("Click on \"To Account Centre\"");
        settingsPage.openAccountCenter();

        step("Click Edit Icon");
        settingsPage.clickEditIcon();

        step("user click on Delete Account");
        accountProfilePage.deleteAccount();

        step("Confirm delete account");
        accountProfilePage.confirmDeleteAccount();
        Assertion.assertThat("user should see \"See you again\" bottomsheet", accountProfilePage.isSeeYouAgain(),
                is(true));
    }

    @Author(names = { "Ishika" })
    @Module(name = "GuestBooking")
    @Tribe(name = "UNM")
    @Vertical(name = "Platform")
    @Test
    public void testRegisterPhoneNoFromBookingFormNonLogin() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testRegisterPhoneNoFromBookingFormNonLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String tripType = param.get("tripType");
        int daysToDepartureDate;
        try {
            String daysStr = param.get("daysToDepartureDate");
            daysToDepartureDate = Integer.parseInt(daysStr);
        } catch (NumberFormatException e) {
            throw new AssertionError("Invalid value for 'daysToDepartureDate': " + param.get("daysToDepartureDate"), e);
        }
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        String otp = param.get("otp");
        int retryCounter;
        try {
            String retryStr = param.get("retryCounter");
            retryCounter = Integer.parseInt(retryStr);
        } catch (NumberFormatException e) {
            throw new AssertionError("Invalid value for 'retryCounter': " + param.get("retryCounter"), e);
        }
        String password = "Password123";
        String email = CoreFunctions.randomEmail().toLowerCase();
        String phoneNumber = "812" + CoreFunctions.randomInt(8);
        String fullName = CoreFunctions.randomName() + " " + CoreFunctions.randomName();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        TrainSearchPageWeb<?> trainSearchPage = TrainSearchPageWeb.getInstance(session);
        TrainSearchResultPageWeb<?> trainSearchResultPage = TrainSearchResultPageWeb.getInstance(session);
        TrainBookingFormWeb<?> trainBookingFormWeb = TrainBookingFormWeb.getInstance(session);
        AccountPageWeb<?> accountPageWeb = AccountPageWeb.getInstance(session);

        step("Given user open home page");
        homePage.goToHomePage();

        step("And user login with curreny IDR");
        loginPage.loginIfNotWithSameEmail(email, password, otp, 1);
        String curr = accountPageWeb.getCurrentCurrency();
        if (!curr.equals("IDR")) {
            accountPageWeb.changeCurrency("IDR");
        }

        step("User open train vertical");
        homePage.openVerticalPageViaURL("Train");

        step("User skip pop-up");
        trainSearchPage.clickSkipPopup();

        step("User select origin station");
        trainSearchPage.selectOrigin(originStation);

        step("User select destination station");
        trainSearchPage.selectDestination(destinationStation);

        step("User toggle one way");
        trainSearchPage.toggleTripType(tripType);

        step("User select departure date");
        trainSearchPage.selectDate(departureDate);

        step("User search train");
        trainSearchPage.searchTrain();

        step("User skip pop-up");
        trainSearchPage.clickSkipPopup();

        step("Select train");
        trainSearchResultPage.selectTrain();

        step("Continue to Booking Form Page");
        trainBookingFormWeb.continueToBookingForm2();

        step("Click Log In On Booking Form Page");
        trainBookingFormWeb.clickLogInNowButton();

        step("Click Register Now Button");
        trainBookingFormWeb.clickRegisterNowButton();

        step("Register on booking form page");
        loginPage.registerWithNewPhoneNumberOrEmail(phoneNumber);
        loginPage.inputDetailsRegisterPage(fullName, email, password, otp, retryCounter);

        step("Assert that newly registered user is able to Continue To booking Form After Registration");
        Assertion.assertThat("newly registered user is able to Continue To booking Form After Registration",
                trainBookingFormWeb.isuserAbleToContinue(), is(true));
    }

    @Author(names = { "Ishika" })
    @Module(name = "GuestBooking")
    @Tribe(name = "UNM")
    @Vertical(name = "Platform")
    @Test
    public void testRegisterPhoneNoNonLogin() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testRegisterPhoneNoNonLogin");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        String otp = param.get("otp");
        int retryCounter;
        try {
            retryCounter = Integer.parseInt(param.get("retryCounter"));
        } catch (NumberFormatException e) {
            throw new AssertionError("Invalid value for 'retryCounter': " + param.get("retryCounter"), e);
        }
        String password = "Password123";
        String email = CoreFunctions.randomEmail().toLowerCase();
        String phoneNumber = "812" + CoreFunctions.randomInt(8);
        String fullName = "automation" + " " + CoreFunctions.generateRandomString(10);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        step("User open home page and ensure logged out");
        homePage.goToHomePage();
        loginPage.loginAsGuest();

        step("Click Login Button");
        homePage.clickOnLoginButton();

        step("Click 'Continue with Phone or Email' and input phone number");
        loginPage.clickContinueWithPhoneOrEmail();
        loginPage.inputPhoneAndContinue(phoneNumber);

        step("Fill registration form and register");
        loginPage.fillAndSubmitRegistration(fullName, email, password);

        step("Enter OTP and complete registration");
        loginPage.completeRegistration(otp, retryCounter);

        step("Assert that newly registered user is successfully logged in after registration");
        Assertion.assertThat("newly registered user is successfully logged in after registration",
                loginPage.checkIfLoggedIn(), is(true));
    }

    @Author(names = { "Ishika" })
    @Module(name = "LoginPage")
    @Tribe(name = "UNM")
    @Vertical(name = "Platform")
    @Test
    public void testloginForEMVAccountNonLogin() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testloginForEMVAccountNonLogin");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("User open home page");
        homePage.goToHomePage();

        step("Go to account page");
        loginPage.goToAccountPage();

        step("login in using { " + email + " }");
        loginPage.loginIfNotWithSameEmail(email, password, otp, 1);

        step("go to tiket home page");
        homePage.goToHomePage();

        step("Assert that user is successfully logged in");
        Assertion.assertThat("user is successfully logged in", loginPage.checkIfLoggedIn(), is(true));
    }

    @Author(names = { "Ishika" })
    @Module(name = "GuestBooking")
    @Tribe(name = "UNM")
    @Vertical(name = "Platform")
    @Test
    public void testRegisterPhoneNoFromSRPNonLogin() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testRegisterPhoneNoFromSRPNonLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String currency = param.get("currency");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String password = "Password123";
        String email = CoreFunctions.randomEmail().toLowerCase();
        String phoneNumber = "812" + CoreFunctions.randomInt(8);
        String fullName = CoreFunctions.randomName() + " " + CoreFunctions.randomName();
        String location = param.get("location");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelSearchResultPage<?> searchResultPage = new HotelSearchResultPage<>(session);
        SRPPageWeb<?> srpPage = SRPPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("Log out the user");
        loginPage.logoutNeeded();

        step("User select IDR currency");
        homePage.selectCurrency(currency);

        step("User Open NHA Landing page");
        homePage.openVerticalPageViaURL("Homes");

        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), Is.is(true));

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("submit search Button");
        landingPage.clickSubmit();

        step("close Pop up");
        searchResultPage.clickPopUp();

        step("Click Log In On Booking Form Page");
        srpPage.clickLogInButton();

        step("Click Register Now Button");
        loginPage.clickRegisterNowButton();

        step("Register on booking form page");
        loginPage.registerWithNewPhoneNumberOrEmail(phoneNumber);
        loginPage.inputDetailsRegisterPage(fullName, email, password, otp, retryCounter);

        step("Assert that newly registered user is Registered and logged in successfully");
        Assertion.assertThat("newly registered user is Registered and logged in successfully",
                srpPage.logInSuccessfullOnSRP(), is(true));
    }

    @Author(names = { "Yanuar" })
    @Module(name = "UNM")
    @Tribe(name = "UNM")
    @Vertical(name = "Platform")
    @Test
    public void testAutoLoginUNMWeb() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> sheetData = getParam("testAutoLoginUNMWeb");
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        UNMPageWeb<?> unmPage = UNMPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        String email = sheetData.get("email");
        String password = sheetData.get("password");
        String otp = sheetData.get("otp");
        step("Logout from tiket if login");
        loginPage.logoutNeeded();

        step("Given user login from blibli web");
        unmPage.loginFromBlibli(email, password, otp);

        step("When user open tiket web");
        homePage.goToHomePage();
        unmPage.goToUNMAccountPageViaURL();

        step("Then user should already logged in with the correct profile data");
        Map<String, String> profileData = unmPage.getProfileData();

        Assertion.assertThat("Verify profile name matches", profileData.get("fullName"),
                equalToIgnoringCase(sheetData.get("fullName")));
        Assertion.assertThat("Verify profile phone number matches", profileData.get("phoneNumber"),
                is(sheetData.get("phoneNumber")));
        Assertion.assertThat("Verify profile email matches", profileData.get("email"),
                equalToIgnoringCase(sheetData.get("email")));
    }

    @Author(names = { "Yanuar" })
    @Module(name = "UNM")
    @Tribe(name = "UNM")
    @Vertical(name = "Platform")
    @Test
    public void testLogoutDeviceLevelWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testLogoutDeviceLevelWeb");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        UNMPageWeb<?> unmPage = UNMPageWeb.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("Given user already login on tiket web ");
        homePage.goToHomePage();
        loginPage.loginIfNotWithSameEmail(email, password, otp, 1);

        step("And user should already login automatically in Blibli");
        unmPage.goToBlibliWeb();
        unmPage.ensureUserIsLoggedInOnBlibli();
        Assertion.assertThat("user is logged in on blibli web", unmPage.isLoginOnBlibli(), is(true));

        step("When user clicks logout from tiket web");
        loginPage.logoutNeeded();
        Assertion.assertThat("user is logged out from tiket web", loginPage.checkIfLoggedIn(), is(false));

        step("And user goes to Blibli web");
        unmPage.goToBlibliWeb();

        step("Then user should be logged out from both entities");
        Assertion.assertThat("user is logged out from blibli web", unmPage.isLoginOnBlibli(), is(false));
    }

}
