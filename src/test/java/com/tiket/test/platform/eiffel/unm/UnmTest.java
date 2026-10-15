package com.tiket.test.platform.eiffel.unm;

import com.tiket.apiPage.FlightApi;
import com.tiket.apiPage.Unm;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Environment;
import com.tiket.model.Session;
import com.tiket.page.account.AccountPage;
import com.tiket.page.accountcenter.AccountCenterPage;
import com.tiket.page.flight.FlightPage;
import com.tiket.page.flight.searchpage.FlightSearchPage;
import com.tiket.page.flight.searchresultpage.FlightSearchResultPage;
import com.tiket.page.home.HomePage;
import com.tiket.page.login.LoginPage;
import com.tiket.page.setting.SettingPage;
import com.tiket.page.train.bookingform.TrainBookingPage;
import com.tiket.page.train.searchpage.TrainSearchPage;
import com.tiket.page.train.searchresultpage.TrainSearchResultPage;
import com.tiket.pojo.flight.FlightDynamicData;
import com.tiket.pojo.member.RegisterNewAccountData;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.testbase.Configs;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.tiket.core.CoreFunctions.randomInt;
import static com.tiket.core.CoreFunctions.randomLastName;
import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;

public class UnmTest extends BaseTest {

    @Author(names = {"Yazid"})
    @Module(name = "UNM")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test
    public void verifyDeleteAccount() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("verifyDeleteAccount");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountCenterPage<WebDriver> accountCenterPage = AccountCenterPage.getInstance(session);
        Unm<WebDriver> unm = new Unm<>(session);

        RegisterNewAccountData accountData = unm.registerNewAccountViaApiUNM();
        String email = accountData.email;
        String password = accountData.password;
        boolean accountDeleted = false;

        try {

            step("Go to home");
            homePage.gotoHome();

            step("Login as " + email + " via deeplink");
            loginPage.loginWith("email", email, password);

            step("user click Account center UNM");
            homePage.openAccountCenter(email);

            step("user click on Edit Profile");
            accountCenterPage.openEditProfile();

            step("user click on Delete Account");
            accountCenterPage.deleteAccount();

            step("Confirm delete account");
            accountCenterPage.confirmDeleteAccount();
            Assertion.assertThat("user should see \"See you again\" bottomsheet", accountCenterPage.isSeeYouAgain(), is(true));
            accountDeleted = true;

            step("upon clicking \"See you!\", user should redirected to login page");
            accountCenterPage.clickSeeYouAgain();
            Assertion.assertThat("User is on Login page", loginPage.isRedirectLoginPage(), is(true));
        } finally {
            if(!accountDeleted){
                step("Delete account after test finished");
                unm.deleteAccountViaApi(accountData.unmData);
            }
        }
    }

    @Author(names = {"Zia"})
    @Module(name = "UNM")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test
    public void loginWithSuspendedAccount(){
        Map<String, String> params = getParam("loginWithSuspendedAccount");
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(params);

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);

        String email = params.get("email");

        step("Wait for app to launch");
        homePage.gotoHome();
        loginPage.loginAsGuest();

        step("Get language");
        String lang = homePage.getLanguageApp();

        step("Input credential on login page "+email);
        loginPage.openLoginPage();
        loginPage.loginWithSuspendedEmail(email);

        step("Verify suspended popup in lang: "+lang);
        String[] expectedTexts = {"There's a problem with your account","Maybe later", "Contact Customer Care"};
        if (lang.equals("id")) {
            expectedTexts = new String[]{"Ada masalah dengan akunmu", "Nanti aja", "Hubungi Customer Care"};
        }

        Assertion.assertThat("Account suspension popup appear", loginPage.verifySuspendedAccountPopup(expectedTexts),is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "Login")
    @Tribe(name = "UNM")
    @Vertical(name = "Platform")
    @Test
    public void testLoginUsingEMV(){
        Map<String, String> params = getParam("testLoginUsingEMV");
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(params);

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);

        String loginType = "email";
        String username = params.get("username");
        String password = params.get("password");

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);

        step("Verify user login succesfully");
        Assertion.assertThat("verify user login succesfully", homePage.isUserLogin(), is(true));

        step("User go to Account page");
        homePage.openAccount();

        accountPage.clickEditProfileDetail();
        Assertion.assertThat("User's email should be correct", accountPage.checkAccountEntity(username), is(true));
    }

    @Author(names = {"Kartik"})
    @Module(name = "UNM")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test
    public void registerWithSuspendedAccount(){
        Map<String, String> params = getParam("registerWithSuspendedAccount");
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(params);

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);

        String email = params.get("email");

        step("Wait for app to launch");
        homePage.gotoHome();
        loginPage.loginAsGuest();

        step("Get language");
        String lang = homePage.getLanguageApp();

        step("Register with suspended email "+email);
        loginPage.openLoginPage();
        loginPage.registerWithSuspendedEmail(email);

        step("Verify suspended popup in lang: "+lang);
        String[] expectedString = {"There's a problem with your account","Maybe later", "Contact Customer Care"};
        if (lang.equals("id")) {
            expectedString = new String[]{"Ada masalah dengan akunmu", "Nanti aja", "Hubungi Customer Care"};
        }
        Assertion.assertThat("Account suspension popup appears", loginPage.verifySuspendedAccountPopup(expectedString),is(true));
    }

    @Author(names = {"Kartik"})
    @Module(name = "UNM")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test
    public void registerWithNewEmailPhoneNumber(){
        Map<String, String> params = getParam("registerWithNewEmailPhoneNumber");
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(params);

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        Unm<WebDriver> unm = new Unm<>(session);

        String password = "Password123";
        String email = CoreFunctions.randomEmail().toLowerCase();
        String fullName = CoreFunctions.randomName() + " " + CoreFunctions.randomName();
        String phoneNumber = "+62777" + CoreFunctions.randomInt(7);

        step("Wait for app to launch");
        homePage.gotoHome();
        loginPage.loginAsGuest();

        step("open login page");
        loginPage.openLoginPage();

        try {
            step("Register with " + email);
            loginPage.registerWithNewPhoneNumberOrEmail(phoneNumber);

            step("Input details on register page");
            loginPage.inputDetailsRegisterPage(fullName, email, password);

            step("Verify account details");
            homePage.openAccount();
            Assertion.assertThat("User's name should be displayed correctly after login", accountPage.getAccountName(), containsString(fullName));
            accountPage.clickAccountDetail();
            Assertion.assertThat("User's email and phone should be displayed correctly in account details", accountPage.checkAccountEmailAndPhone(email, phoneNumber), is(true));
        } finally {
            if (Configs.ENVIRONMENT != Environment.PROD) {
                //using api to delete account
                step("Delete account after test finished");
                unm.deleteAccountViaApi(email);
            }
        }

    }

    @Author(names = {"Prerna"})
    @Module(name = "ChangeAccountDetails")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test
    public void testChangeAccountName() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("testChangeAccountName");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountCenterPage<WebDriver> accountCenterPage = AccountCenterPage.getInstance(session);
        Unm<WebDriver> unm = new Unm<>(session);

        step("User already logged in");
        RegisterNewAccountData accountData = unm.registerNewAccountViaApiUNM();
        String email = accountData.email;
        String password = accountData.password;
        String loginType = "email";

        try {
            homePage.gotoHome();
            loginPage.loginWith(loginType, email, password);

            String name = "Automation " + randomLastName();

            step("open account center");
            homePage.openAccountCenter(email);

            step("Click edit profile details");
            accountCenterPage.openEditProfile();

            step("Get Name before changing ");
            String initialName = accountCenterPage.getNameText();

            step("Change Name");
            accountCenterPage.changeName(name);

            step("Get Name After changing ");
            String finalName = accountCenterPage.getNameText();

            Assertion.assertThat("Name Changed Successfully", initialName, not(finalName));

        } finally {
            step("Delete account after test finished");
            unm.deleteAccountViaApi(email);
        }
    }

    @Author(names = {"Prerna"})
    @Module(name = "ChangeAccountDetails")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test
    public void testChangeDOBOnAccountCenter() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("testChangeDOBOnAccountCenter");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountCenterPage<WebDriver> accountCenterPage = AccountCenterPage.getInstance(session);
        Unm<WebDriver> unm = new Unm<>(session);

        step("User already logged in");
        RegisterNewAccountData accountData = unm.registerNewAccountViaApiUNM();
        String email = accountData.email;
        String password = accountData.password;

        String loginType = "email";
        homePage.gotoHome();
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, email, password);

        step("open account center");
        homePage.openAccountCenter(email);

        step("Click edit profile details");
        accountCenterPage.openEditProfile();

        step("Get DOB before changing ");
        String initialDOB = accountCenterPage.getDOBText();

        step("change DOB");
        accountCenterPage.changeDOB();

        step("Get DOB After changing ");
        String finalDOB = accountCenterPage.getDOBText();

        Assertion.assertThat("DOB changed successfull", initialDOB, not(finalDOB));

        step("user click on Delete Account");
        accountCenterPage.deleteAccount();

        step("Confirm delete account");
        accountCenterPage.confirmDeleteAccount();
        Assertion.assertThat("user should see \"See you again\" bottomsheet", accountCenterPage.isSeeYouAgain(), is(true));
    }

    @Author(names = {"Prerna"})
    @Module(name = "ChangeAccountDetails")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test
    public void testChangeGender() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("testChangeGender");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountCenterPage<WebDriver> accountCenterPage = AccountCenterPage.getInstance(session);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        step("Goto home");
        homePage.gotoHome();

        step("login with" + username);
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);

        step("open account center");
        homePage.openAccountCenter(username);

        step("Click edit profile details");
        accountCenterPage.openEditProfile();

        step("Get Gender before changing ");
        String initialGender = accountCenterPage.getGenderText();

        step("Change Gender");
        accountCenterPage.changeGender(initialGender);

        step("Get Gender After changing ");
        String finalGender = accountCenterPage.getGenderText();
        Assertion.assertThat("Gender changed successfull", initialGender, not(finalGender));
    }

    @Author(names = {"Prerna"})
    @Module(name = "ChangeAccountDetails")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test
    public void testChangePNUsingNewPN() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("testChangePNUsingNewPN");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountCenterPage<WebDriver> accountCenterPage = AccountCenterPage.getInstance(session);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String phoneNumber = map.get("phoneNumber")+ randomInt(5);

        step("Goto home");
        homePage.gotoHome();

        step("login with " + username);
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);

        step("open account center");
        homePage.openAccountCenter(username);

        step("Click edit profile details");
        accountCenterPage.openEditProfile();

        step("Click change phone number btn");
        accountCenterPage.clickChangePhoneNumberBtn();

        step("Enter Otp");
        accountCenterPage.inputOtp(1);

        step("Enter new PN " + phoneNumber);
        accountCenterPage.changePhoneNumber(phoneNumber);

        step("Enter Otp");
        accountCenterPage.inputOtp(1);

        step("Verify phone number changed");
        String accountPageText = accountCenterPage.getValueOnAccountCenter(phoneNumber);
        Assertion.assertThat("Is new PN shown on account center", accountPageText, containsString(phoneNumber));
    }

    @Author(names = {"Prerna"})
    @Module(name = "ChangeAccountDetails")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test
    public void verifyClaimedPhoneNumber() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("verifyClaimedPhoneNumber");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountCenterPage<WebDriver> accountCenterPage = AccountCenterPage.getInstance(session);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String phoneNumber = map.get("phoneNumber");//phone number not associated with this account but already in use.

        step("Goto home");
        homePage.gotoHome();

        step("login with" + username);
        loginPage.loginWith(loginType, username, password);

        step("open account center");
        homePage.openAccountCenter(username);

        step("Click edit profile details");
        accountCenterPage.openEditProfile();

        step("Click change phone number btn");
        accountCenterPage.clickChangePhoneNumberBtn();

        step("Enter Otp");
        accountCenterPage.inputOtp(1);

        step("Enter already owned phone number and submit");
        accountCenterPage.changePhoneNumber(phoneNumber);

        step("Verify error message");
        Assertion.assertThat("Verify error message", accountCenterPage.isPhoneNumberClaimedErrorDisplayed(), is(true));
    }

    @Author(names = {"Prerna","Ashish"})
    @Module(name = "ChangeAccountDetails")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test
    public void testChangeEmailWithNewEmail() throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("testChangeEmailWithNewEmail");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountCenterPage<WebDriver> accountCenterPage = AccountCenterPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        Unm<?> unm = new Unm<>(session);

        String phoneNumber = map.get("phoneNumber");
        String email = "", password = "";
        RegisterNewAccountData accountData = new RegisterNewAccountData();
        if(Configs.ENVIRONMENT!=Environment.PROD) {
            //api create account with email
            step("Register new account");
            accountData = unm.registerNewAccountViaApiUNM();
            email = accountData.email;
            phoneNumber = accountData.phoneCountryCode + accountData.phoneNationalNumber;
            password = accountData.password;
        }

        step("Wait for app to launch");
        homePage.gotoHome();

        step("user open app as guest");
        loginPage.loginAsGuest();

        if(Configs.ENVIRONMENT!=Environment.PROD) {
            step("Check in homepage that { " + email + " } already login");
            loginPage.loginWith("email", email, password);

            step("open account center");
            homePage.openAccountCenter(email);
        }else {
            step("Check in homepage that { " + phoneNumber + " } already login");
            loginPage.loginWith("phonenumber", phoneNumber, password);

            step("open account center");
            email = homePage.openAccountCenter();
        }

        step("change email");
        String newEmail = accountCenterPage.changeEmail(email);
        Assertion.assertThat("The new email { "+newEmail+" } is different from the one with which we logged in { " + email + " }", email, not(newEmail));

        if(Configs.ENVIRONMENT!=Environment.PROD){
            //using api to verify email changed
            String emailAfterChanging = accountPage.getEmailFromAccountDetails("phone", phoneNumber);
            Assertion.assertThat("The new email is same as we entered in change email field", emailAfterChanging, is(newEmail));

            //using api to delete account
            step("delete account");
            unm.deleteAccountViaApi(accountData.unmData);
        }
    }

    @Author(names = {"Prerna"})
    @Module(name = "ChangeAccountDetails")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test
    public void testChangeCountryFromAccountDetails() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("testChangeCountryFromAccountDetails");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountCenterPage<WebDriver> accountCenterPage = AccountCenterPage.getInstance(session);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String country = map.get("country");

        step("Goto home");
        homePage.gotoHome();

        step("login with" + username);
        loginPage.loginWith(loginType, username, password);

        step("open account center");
        homePage.openAccountCenter(username);

        step("get initial country");
        String initialCountry = accountCenterPage.getCountryName();

        step("change country");
        accountCenterPage.changeCountry(country, initialCountry);

        step("verify country changed");
        String finalCountry = accountCenterPage.getCountryName();
        Assertion.assertThat("Verify country change", initialCountry, not(finalCountry));
    }

    @Author(names = {"Kartik"})
    @Module(name = "Passenger")
    @Tribe(name = "Forgot Password")
    @Vertical(name = "Platform")
    @Test
    public void testChangePassword() throws Exception {
        Map<String, String> params = getParam("testChangePassword");
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(params);

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        AccountCenterPage<WebDriver> accountCenterPage = AccountCenterPage.getInstance(session);

        String username = params.get("username");
        String oldPassword = params.get("password");
        String newPassword = params.get("newPassword");
        String loginType = params.get("loginType");

        loginPage.resetPasswordViaApi(username, oldPassword);

        step("Goto home");
        homePage.gotoHome();

        step("User Login");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, oldPassword);

        step("Open Account Center");
        homePage.openAccountCenter(username);

        step("Click Change Password");
        accountCenterPage.clickChangePasswordButton();

        step("Change password");
        accountCenterPage.changePassword(oldPassword, newPassword);

        step("Verify user is logged out");
        Assertion.assertThat(
                "User is redirected to login page",
                loginPage.isOnLoginPage(),
                is(true));

        step("Login with new password");
        loginPage.loginFromDifferentPages(username, newPassword);

        step("Verify login successful");
        Assertion.assertThat(
                "User is logged in successfully",
                homePage.isUserLogin(),
                is(true));

        // Cleanup
        step("Revert password back to original");
        loginPage.resetPasswordViaApi(username, oldPassword);
    }

    @Author(names = {"Kartik"})
    @Module(name = "NewRegister")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test()
    public void testRegisterPhoneNoFromBookingForm(){
        Map<String, String> param = getParam("testRegisterPhoneNoFromBookingForm");
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        TrainSearchPage<WebDriver> trainSearchPage = TrainSearchPage.getInstance(session);
        TrainSearchResultPage<WebDriver> trainSearchResultPage = TrainSearchResultPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TrainBookingPage<WebDriver> trainBookingPage = TrainBookingPage.getInstance(session);
        Unm<WebDriver> unm = new Unm<>(session);

        String password = "Password123";
        String email = CoreFunctions.randomEmail().toLowerCase();
        String phoneNumber = "812"+CoreFunctions.randomInt(8);
        String fullName = CoreFunctions.randomName() + " " + CoreFunctions.randomName();
        String tripType = param.get("tripType");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("User user app as guest");
        loginPage.loginAsGuest();

        step("Change currency");
        homePage.changeCurrency("IDR","");

        step("Open train page");
        homePage.openVerticalPageDeeplink("Trains","","IDR");

        step("Skip Popup");
        trainSearchPage.skipPopup();

        step("Select origin station");
        trainSearchPage.selectOrigin(originStation);

        step("Select destination station");
        trainSearchPage.selectDestination(destinationStation);

        step("Toggle roundtrip");
        trainSearchPage.toggleTripType(tripType);

        step("Select date");
        trainSearchPage.selectDate(departureDate);

        step("Search train");
        trainSearchPage.searchTrain();

        step("skip popup");
        trainSearchPage.skipPopup();

        step("Select departure train");
        trainSearchResultPage.selectTrain();

        try {
            step("Register booking page");
            trainBookingPage.openLoginPage();
            loginPage.registerWithNewPhoneNumberOrEmail(phoneNumber);
            loginPage.inputDetailsRegisterPage(fullName, email, password);

            step("Verify contact detail have same data with registered");
            Assertion.assertThat("Verify contact is the same as registered", trainBookingPage.verifyContactDetailData(email, phoneNumber, fullName), is(true));
        } finally {
            step("delete account");
            unm.deleteAccountViaApi(email);
        }
    }

    @Author(names = {"Prerna","Kartik"})
    @Module(name = "NewRegister")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test
    public void testRegisterEmailFromSearchResult() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testRegisterEmailFromSearchResult");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        Unm<WebDriver> unm = new Unm<>(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String airline = param.get("airline");
        String password = "Password123";
        String email = CoreFunctions.randomEmail().toLowerCase();
        String fullName = CoreFunctions.randomName() + " " + CoreFunctions.randomName();
        String phoneNumber = "812" + CoreFunctions.randomInt(8);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDate(
                "","",
                originAirport,destinationAirport,daysToDepartureDate,
                adultPassengers,childPassengers,infantPassengers,
                tripType,airline,"","","","IDR"
        );
        departureDate =CoreFunctions.convertToLongDate(flightDynamicData.departureDate);

        step("Login as Guest");
        loginPage.loginAsGuest();

        step("Change currency");
        homePage.changeCurrency("IDR", "");

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","","IDR");
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(flightDynamicData, tripType);

        step("Toggle trip type");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select departure date");
        flightSearchPage.selectDate(departureDate);

        step("Search flight");
        flightSearchPage.searchFlight();

        try {
            step("Register From Search Page");
            flightSearchResultPage.clickLoginBanner();
            loginPage.registerWithNewPhoneNumberOrEmail(email);
            loginPage.inputDetailsRegisterPageEmail(fullName, phoneNumber, password);

            Assertion.assertThat("User is on SRP", flightSearchResultPage.isOnSearchResultPage(), is(true));
            Assertion.assertThat("Banner to register from SRP should not be there", flightSearchResultPage.isRegisterBannerDisappear(), is(true));
        } finally {
            step("delete account");
            unm.deleteAccountViaApi(email);
        }
    }

    @Author(names = {"Ashish"})
    @Module(name = "Passenger")
    @Tribe(name = "Forgot Password")
    @Vertical(name = "Platform")
    @Test
    public void testForgetPasswordWithEmail(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> params = getParam("testForgetPasswordWithEmail");
        BaseTest.paramsThreadLocal.set(params);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);

        String username = params.get("username");
        String password = params.get("password");

        step("Goto home");
        homePage.gotoHome();

        step("Login as guest");
        loginPage.loginAsGuest();

        step("open login page");
        loginPage.openLoginPage(username);

        step("Forget Password");
        String updatedPassword = loginPage.forgetPassword(username, password);

        step("Verify login with updated password for :  " + username);
        loginPage.loginAfterForgotPasswordWithEmail(username, updatedPassword);

        step("Verify Step");
        Assertion.assertThat("User is on login state", homePage.isUserLogin(), is(true));

        step("User go to Account page");
        homePage.openAccount();

        accountPage.clickEditProfileDetail();
        Assertion.assertThat("User's email should be correct", accountPage.checkAccountEntity(username), is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "Passenger")
    @Tribe(name = "Forgot Password")
    @Vertical(name = "Platform")
    @Test
    public void testForgetPasswordWithPhoneNo(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> params = getParam("testForgetPasswordWithPhoneNo");
        BaseTest.paramsThreadLocal.set(params);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);

        String username = params.get("username");
        String password = params.get("password");
        String phoneNumber = params.get("phoneNumber");

        step("Goto home");
        homePage.gotoHome();

        step("Login as guest");
        loginPage.loginAsGuest();

        step("open login page");
        loginPage.openLoginPage(username);

        step("Forget Password");
        String updatedPassword = loginPage.forgetPasswordPhoneNumber(username,phoneNumber, password);

        step("Verify login with updated password for :  " + username);
        loginPage.loginAfterForgotPasswordWithEmail(username, updatedPassword);

        step("Verify Step");
        Assertion.assertThat("User is on login state", homePage.isUserLogin(), is(true));

        step("User go to Account page");
        homePage.openAccount();

        accountPage.clickEditProfileDetail();
        Assertion.assertThat("User's phone number should be correct", accountPage.checkAccountEntity(phoneNumber), is(true));
    }
}
