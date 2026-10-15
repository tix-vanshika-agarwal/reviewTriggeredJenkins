package com.tiket.test.platform.eiffel.member;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.apiPage.FlightApi;
import com.tiket.apiPage.Unm;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.page.accommodation.hotel.bookingformpage.BookingFormPage;
import com.tiket.page.accommodation.hotel.detailpage.DetailPage;
import com.tiket.page.accommodation.hotel.landingpage.LandingPage;
import com.tiket.page.accommodation.hotel.roomlistpage.RoomListPage;
import com.tiket.page.account.AccountPage;
import com.tiket.page.account.referral.ReferralPage;
import com.tiket.page.account.reviewpage.ReviewPage;
import com.tiket.page.accountcenter.AccountCenterPage;
import com.tiket.page.cartransfer.CarTransferPage;
import com.tiket.page.cartransfer.bookingform.CarTransferBookingPage;
import com.tiket.page.cartransfer.searchpage.CarTransferSearchPage;
import com.tiket.page.cartransfer.searchresultpage.CarTransferSearchResultPage;
import com.tiket.page.common.webview.WebViewPage;
import com.tiket.page.flight.FlightPage;
import com.tiket.page.flight.bookingform.FlightBookingFormPage;
import com.tiket.page.flight.prp.FlightProductReviewPage;
import com.tiket.page.flight.searchpage.FlightSearchPage;
import com.tiket.page.flight.searchresultpage.FlightSearchResultPage;
import com.tiket.page.home.HomePage;
import com.tiket.page.login.LoginPage;
import com.tiket.page.loyalty.partnerPage.LoyaltyPartnerPage;
import com.tiket.page.member.smart_profile.SmartProfilePage;
import com.tiket.page.onboarding.OnboardingPage;
import com.tiket.page.payment.PaymentPage;
import com.tiket.page.setting.SettingPage;
import com.tiket.page.train.bookingform.TrainBookingPage;
import com.tiket.page.train.searchpage.TrainSearchPage;
import com.tiket.page.train.searchresultpage.TrainSearchResultPage;
import com.tiket.page.ttd.landingpage.TTDLandingPage;
import com.tiket.page.ttd.searchpage.TTDSearchPage;
import com.tiket.page.utilities.message.help_center.HelpCenterPage;
import com.tiket.pojo.flight.FlightDynamicData;
import com.tiket.pojo.member.RegisterNewAccountData;
import com.tiket.pojo.unm.GetProfileDetailApiResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.testbase.Configs;
import com.tiket.testdata.TestData;
import com.tiket.web.page.ttd.TtdBookingFormPage.TtdBookingFormPageWeb;
import com.tiket.web.page.ttd.TtdProductDetailPage.TtdProductDetailPageWeb;
import com.tiket.web.page.ttd.TtdSelectedPackageDetailPage.TtdSelectedPackageDetailPageWeb;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.tiket.core.CoreFunctions.*;
import static com.tiket.testdata.TestData.getObjectParams;
import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.core.Is.is;

public class MemberTest extends BaseTest {

    @Author(names = {"Yazid"})
    @Module(name = "Login")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test
    public void loginFromBlibliTiketLoyaltyProgramBanner() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("loginFromBlibliTiketLoyaltyProgramBanner");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        SettingPage<WebDriver> settingPage = SettingPage.getInstance(session);
        AccountCenterPage<WebDriver> accountCenterPage = AccountCenterPage.getInstance(session);
        Unm<?> unm = new Unm<>(session);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        step("get profile data from unm api");
        GetProfileDetailApiResponse profileDetailApiResponse = unm.getProfileDetail(username);
        String firstName = profileDetailApiResponse.data.name.split(" ")[0];
        String lastName = profileDetailApiResponse.data.name.split(" ")[1];
        String phoneNumber = profileDetailApiResponse.data.phoneNationalNumber;

        step("Goto home");
        homePage.gotoHome();

        step("Set the session as non-login");
        loginPage.loginAsGuest();

        step("Change currency to the IDR");
        homePage.changeCurrency("IDR", "");

        step("Open the account page");
        homePage.openAccount();

        step("Open the login page from the Blibli Tiket Loyalty Program Banner");
        accountPage.clickLoyaltyBanner();
        Assertion.assertThat("able to click tiket loyalty program banner", accountPage.canClickTiketLoyaltyBanner(), is(true));
        accountPage.clickLoginRegisterButtonLoyaltyPopup();

        step("Submit login with the valid account and login type: " + loginType);
        loginPage.loginFromDifferentPages(username, password);

        Assertion.assertThat("User should be redirected to My Account page after login", accountPage.isLoggedIn(), is(true));
        Assertion.assertThat("User's name should be displayed correctly after login", accountPage.getAccountName(), containsString(firstName + " " + lastName));

        step("open account detail page");
        accountPage.clickAccountDetail();
        Assertion.assertThat("User's email and phone should be displayed correctly in account details", accountPage.checkAccountEmailAndPhone(username, phoneNumber), is(true));
        settingPage.clickAccountCenter();
        Assertion.assertThat("User's account details should match the provided information", accountCenterPage.verifyAccountDetail(profileDetailApiResponse), is(true));
    }

    @Author(names = {"Dendi"})
    @Module(name = "Login")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test(dataProvider="testLoginProvider")
    public void testLogin(Map<String, String> params){

        Session<WebDriver> session = sessionThreadLocal.get();
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);

        String loginType = params.get("loginType");
        String username = params.get("username");
        String password = params.get("password");

        step("Goto home");
        homePage.gotoHome();
        step("Input credential on login page");

        loginPage.loginWith(loginType, username, password);
        step("Verify user login succesfully");

        Assertion.assertThat("verify user login succesfully", homePage.isUserLogin(), is(true));
        
        step("Log out");
        loginPage.loginAsGuest();
    }

    @Author(names = {"Dendi"})
    @Module(name = "Login")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test(dataProvider="testGuestProvider")
    public void testGuest(Map<String, String> params){

        Session<WebDriver> session = sessionThreadLocal.get();
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("use app as guest");
        loginPage.loginAsGuest();

        step("Verify user guest succesfully");
        Assertion.assertThat("verify user is guest", homePage.isUserLogin(), is(false));
    }

    @Author(names = {"Dendi"})
    @Module(name = "AccountDetail")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test
    public void testAccountDetail(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> testProperties = getParam("testAccountDetail");
        BaseTest.paramsThreadLocal.set(testProperties);

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);

        String username = testProperties.get("username");
        String password = testProperties.get("password");
        String loginType = testProperties.get("loginType");

        String firstName = testProperties.get("firstName");
        String lastName = testProperties.get("lastName");
        String dateOfBirth = testProperties.get("dob");
        String salutation = testProperties.get("salutation");

        step("User goto home");
        homePage.gotoHome();

        step("use app as guest");
        loginPage.loginAsGuest();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("User open account page to check account detail");
        homePage.openAccount();

        step("User open account detail on account page");
        accountPage.clickAccountDetail();

        step("User verify account detail");
        Assertion.assertThat("Verify account detail", accountPage.verifyAccountDetail(firstName,lastName,dateOfBirth,salutation), is(true));
    }

    @Author(names = {"Dendi","Akbar"})
    @Module(name = "ChangeLanguage")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test(dataProvider = "changeLanguageIDDataProvider")
    public void testShouldChangeLanguageID(Map<String, String> param) {

        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        String language = "IDN";

        step("Wait for app to launch");
        homePage.gotoHome();

        step("login as guest user");
        loginPage.loginAsGuest();

        step("Open account page");
        homePage.openAccount();

        step("Select " + language + " language");
        accountPage.selectLanguage(language);

        Assertion.assertThat(""+language+" is selected", accountPage.isLanguageSelected(language), is(true));
    }

    @Author(names = {"Dendi","Akbar"})
    @Module(name = "ChangeLanguage")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test(dataProvider = "changeLanguageENDataProvider")
    public void testShouldChangeLanguageEN(Map<String, String> param) {

        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        String language = "ENG";

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();

        step("Open account page");
        homePage.openAccount();

        step("Select " + language + " language");
        accountPage.selectLanguage(language);

        Assertion.assertThat(" "+language+" is selected", accountPage.isLanguageSelected(language), is(true));
    }

     @Author(names = {"Budi","Zia"})
    @Module(name = "NewRegister")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test(dataProvider = "testRegisterNewEmailNewPhoneDataProvider")
    public void testRegisterNewEmailNewPhone(Map<String, String> param) {

        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        Unm<WebDriver> unm = new Unm<>(session);

        String password = param.get("password");
        String email = CoreFunctions.randomEmail();
        String phoneNumber = "812"+CoreFunctions.randomInt(8);
        String userFullName = CoreFunctions.randomName() + " " + CoreFunctions.randomName();

        step("Wait for app to launch");
        homePage.gotoHome();

        step("user open app as guest");
        loginPage.loginAsGuest();

        step("Register new account");
        //TODO fix this step

        step("Check in homepage that user already login");
        //TODO fix this step
        Assertion.assertThat("Check logged in homepage",homePage.isUserLogin(), is(true));

        step("Check user detail is the same as registered");
        homePage.openAccount();
        Assertion.assertThat("Verify account name is the same as registered", accountPage.checkAccountName(userFullName), is(true));
        accountPage.clickAccountDetail();
        //by default it should be ID phone number
        Assertion.assertThat("Verify user email and phone number is the same as registered",  accountPage.checkAccountEmailAndPhone(email, "+62" + phoneNumber), is(true));

        step("Delete account after test finished");
        homePage.gotoHome();
        loginPage.loginAsGuest();
        unm.deleteAccountViaApi(email);
    }

    @Author(names = {"Zia"})
    @Module(name = "NewRegister")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test(dataProvider = "testRegisterNewPhoneNewEmailDataProvider")
    public void testRegisterNewPhoneNewEmail(Map<String, String> param) {

        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        Unm<WebDriver> unm = new Unm<>(session);

        String password = param.get("password");
        String phone = "812"+CoreFunctions.randomInt(8);
        String email = CoreFunctions.randomEmail();
        String fullName = CoreFunctions.randomName() + " " + CoreFunctions.randomName();

        step("Wait for app to launch");
        homePage.gotoHome();

        step("user open app as guest");
        loginPage.loginAsGuest();

        step("Register new account");
        //TODO fix this step

        step("Check user detail is the same as registered");
        homePage.openAccount();
        Assertion.assertThat("Verify account name is the same as registered", accountPage.checkAccountName(fullName), is(true));
        accountPage.clickAccountDetail();
        Assertion.assertThat("Verify user email and phone number is the same as registered",  accountPage.checkAccountEmailAndPhone(email, "+62" + phone), is(true));

        step("Delete account after test finished");
        homePage.gotoHome();
        loginPage.loginAsGuest();
        unm.deleteAccountViaApi(email);
    }

    @Author(names = {"Budi","Ashish","Zia"})
    @Module(name = "NewRegister")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test(dataProvider = "testRegisterEmailFromBookingFormDataProvider")
    public void testRegisterEmailFromBookingForm(Map<String, String> param) throws InterruptedException {

        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        TrainSearchPage<WebDriver> trainSearchPage = TrainSearchPage.getInstance(session);
        TrainSearchResultPage<WebDriver> trainSearchResultPage = TrainSearchResultPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TrainBookingPage<WebDriver> trainBookingPage = TrainBookingPage.getInstance(session);
        Unm<WebDriver> unm = new Unm<>(session);

        String password = param.get("password");
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

        step("Register booking page");
        //TODO fix this step

        step("Verify contact detail have same data with registered");
        Assertion.assertThat("Verify contact is the same as registered", trainBookingPage.verifyContactDetailData(email, phoneNumber, fullName), is(true));

        step("Delete account after test finished");
        homePage.gotoHome();
        loginPage.loginAsGuest();
        unm.deleteAccountViaApi(email);
    }

    @Author(names = {"Zia"})
    @Module(name = "NewRegister")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test(dataProvider = "testRegisterWithGuestEmailNewPhoneDataProvider")
    public void testRegisterWithGuestEmailNewPhone(Map<String, String> param) {

        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CarTransferPage<WebDriver> carTransferPage= CarTransferPage.getInstance(session);
        Unm<WebDriver> unm = new Unm<>(session);

        String guestEmail=CoreFunctions.randomEmail();
        String password = param.get("password");
        String guestPhoneNumber="812"+CoreFunctions.randomInt(8);
        String firstName=CoreFunctions.randomName();
        String lastName=CoreFunctions.randomName();
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String userFullName = firstName+" "+lastName;

        step("create guest order via API");
        //TODO fix this step

        step("Wait for app to launch");
        homePage.gotoHome();

        step("User user app as guest");
        loginPage.loginAsGuest();

        step("register with guest email new phone number");
        //TODO fix this step    

        step("Check user detail is the same as registered");
        //TODO fix this step

        step("Delete account after test finished");
        homePage.gotoHome();
        loginPage.loginAsGuest();
        unm.deleteAccountViaApi(guestEmail);
    }

    @Author(names = {"Zia"})
    @Module(name = "NewRegister")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test(dataProvider = "testRegisterWithGuestPhoneNewEmailDataProvider")
    public void testRegisterWithGuestPhoneNewEmail(Map<String, String> param) {

        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CarTransferPage<WebDriver> carTransferPage= CarTransferPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);

        String guestEmail=CoreFunctions.randomEmail();
        String password = param.get("password");
        String guestPhoneNumber="812"+CoreFunctions.randomInt(8);
        String firstName=CoreFunctions.randomName();
        String lastName=CoreFunctions.randomName();
        String userFullName =  firstName+" "+lastName;
        String startDate = generateRandomDate(param.get("rangeAddDate"));

        step("create guest order via API");
        //TODO fix this step

        step("Wait for app to launch");
        homePage.gotoHome();

        step("User user app as guest");
        loginPage.loginAsGuest();

        step("register with guest phone number new email");
        //TODO fix this step

        step("Check user detail is the same as registered");
        //TODO fix this step

        step("Delete account after test finished");
        //TODO fix this step
    }

    @Author(names = {"Zia"})
    @Module(name = "NewRegister")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test(dataProvider = "testRegisterWithGuestPhoneGuestEmailDataProvider")
    public void testRegisterWithGuestPhoneGuestEmail(Map<String, String> param) {

        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CarTransferPage<WebDriver> carTransferPage= CarTransferPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        Unm<WebDriver> unm = new Unm<>(session);

        String guestEmail=CoreFunctions.randomEmail();
        String password = param.get("password");
        String guestPhoneNumber="812"+CoreFunctions.randomInt(8);
        String firstName=CoreFunctions.randomName();
        String lastName=CoreFunctions.randomName();
        String fullName = firstName + " " + lastName;
        String startDate = generateRandomDate(param.get("rangeAddDate"));

        step("create guest order via API");
        //TODO fix this step

        step("Wait for app to launch");
        homePage.gotoHome();

        step("User user app as guest");
        loginPage.loginAsGuest();

        step("register with guest phone guest email");
        //TODO fix this step

        step("Check user detail is the same as registered");
        homePage.openAccount();
        Assertion.assertThat("Verify account name is the same as registered", accountPage.checkAccountName(fullName), is(true));
        accountPage.clickAccountDetail();
        Assertion.assertThat("Verify user email and phone number is the same as registered",  accountPage.checkAccountEmailAndPhone(guestEmail, "+62" + guestPhoneNumber), is(true));

        step("Delete account after test finished");
        homePage.gotoHome();
        loginPage.loginAsGuest();
        unm.deleteAccountViaApi(guestEmail);
    }

    @Author(names = {"Zia"})
    @Module(name = "NewRegister")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test(dataProvider = "testRegisterWithNewPhoneGuestEmailDataProvider")
    public void testRegisterWithNewPhoneGuestEmail(Map<String, String> param) {

        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CarTransferPage<WebDriver> carTransferPage= CarTransferPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        Unm<WebDriver> unm = new Unm<>(session);

        String guestEmail=CoreFunctions.randomEmail();
        String password = param.get("password");
        String phoneNumber="812"+CoreFunctions.randomInt(8);
        String firstName=CoreFunctions.randomName();
        String lastName=CoreFunctions.randomName();
        String fullName = firstName + " " + lastName;
        String startDate = generateRandomDate(param.get("rangeAddDate"));

        step("create guest order via API");
        //TODO fix this step

        step("Wait for app to launch");
        homePage.gotoHome();

        step("User user app as guest");
        loginPage.loginAsGuest();

        step("register with new phone guest email");
        //TODO fix this step

        step("Check user detail is the same as registered");
        //TODO fix this step

        step("Delete account after test finished");
        homePage.gotoHome();
        loginPage.loginAsGuest();
        unm.deleteAccountViaApi(guestEmail);
    }

    @Author(names = {"Zia"})
    @Module(name = "NewRegister")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test(dataProvider = "testRegisterWithNewEmailGuestPhoneDataProvider")
    public void testRegisterWithNewEmailGuestPhone(Map<String, String> param) {

        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CarTransferPage<WebDriver> carTransferPage= CarTransferPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);

        String guestEmail=CoreFunctions.randomEmail();
        String password = param.get("password");
        String guestPhoneNumber="812"+CoreFunctions.randomInt(8);
        String firstName=CoreFunctions.randomName();
        String lastName=CoreFunctions.randomName();
        String fullName = firstName + " " + lastName;
        String startDate = generateRandomDate(param.get("rangeAddDate"));

        step("create guest order via API");
        //TODO fix this step

        step("Wait for app to launch");
        homePage.gotoHome();

        step("User user app as guest");
        loginPage.loginAsGuest();

        step("register with new email guest phone");
        //TODO fix this step

        step("Check user detail is the same as registered");
        //TODO fix this step

        step("Delete account after test finished");
        //TODO fix this step
    }

    @Author(names = {"Zia"})
    @Module(name = "NewRegister")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test(dataProvider = "testRegisterWithGuestEmailGuestPhoneDataProvider")
    public void testRegisterWithGuestEmailGuestPhone(Map<String, String> param) {

        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CarTransferPage<WebDriver> carTransferPage= CarTransferPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        Unm<WebDriver> unm = new Unm<>(session);

        String guestEmail=CoreFunctions.randomEmail();
        String password = param.get("password");
        String guestPhoneNumber="812"+CoreFunctions.randomInt(8);
        String firstName=CoreFunctions.randomName();
        String lastName=CoreFunctions.randomName();
        String fullName = firstName + " " + lastName;
        String startDate = generateRandomDate(param.get("rangeAddDate"));

        step("create guest order via API");
        //TODO fix this step

        step("Wait for app to launch");
        homePage.gotoHome();

        step("User user app as guest");
        loginPage.loginAsGuest();

        step("register with guest email guest phone");
        //TODO fix this step

        step("Check user detail is the same as registered");
        homePage.openAccount();
        Assertion.assertThat("Verify account name is the same as registered", accountPage.checkAccountName(fullName), is(true));
        accountPage.clickAccountDetail();
        Assertion.assertThat("Verify user email and phone number is the same as registered",  accountPage.checkAccountEmailAndPhone(guestEmail, "+62"+guestPhoneNumber), is(true));

        step("Delete account after test finished");
        homePage.gotoHome();
        loginPage.loginAsGuest();
        unm.deleteAccountViaApi(guestEmail);
    }

    @Author(names = {"Prerna"})
    @Module(name = "ChangeLanguage")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test(dataProvider = "changeLanguageIDLoginDataProvider")
    public void testShouldChangeLanguageIDLogin(Map<String, String> param) {

        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String language = "IDN";

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed)");
        homePage.loginIfLoggedOutViaDeeplink(username, password);

        step("Open account page");
        homePage.openAccount();

        step("Select " + language + " language");
        accountPage.selectLanguage(language);

        Assertion.assertThat(language+" is selected", accountPage.isLanguageSelected(language), is(true));
    }

    @Author(names = {"Prerna"})
    @Module(name = "ChangeLanguage")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test(dataProvider = "changeLanguageENLoginDataProvider")
    public void testShouldChangeLanguageENLogin(Map<String, String> param) {

        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String language = "EN";

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed)");
        homePage.loginIfLoggedOutViaDeeplink(username, password);

        step("Open account page");
        homePage.openAccount();

        step("Select " + language + " language");
        accountPage.selectLanguage(language);

        Assertion.assertThat(" "+language+" is selected", accountPage.isLanguageSelected(language), is(true));
    }

    @Author(names = {"Prerna"})
    @Module(name = "Passenger")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test(dataProvider="testAddSmartProfileDataProvider")
    public void testAddSmartProfile(Map<String, String> params){
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(params);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        SmartProfilePage<WebDriver> smartProfilePage = SmartProfilePage.getInstance(session);
        SettingPage<WebDriver> settingPage = SettingPage.getInstance(session);

        String loginType = params.get("loginType");
        String username = params.get("username");
        String password = params.get("password");

        step("Goto home");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink");
        homePage.loginWithSpecificAccountViaDeeplinkOnly(username, password);

        step("Go to Account");
        homePage.openAccount();

        step("Click Save Passenger Data");
        settingPage.openSavePassengersData();

        step("Add Passenger Data");
        String profileName = smartProfilePage.addPassengerData();

        step("Verify smart profile successfully added");
        Assertion.assertThat("profile add snackBar appears", smartProfilePage.getSnackbarText(), anyOf(containsString("A new profile successfully added."),containsString("Profil baru berhasil ditambahkan.")));
        Assertion.assertThat("smart profile exist from search", smartProfilePage.findProfile(profileName), is(true));
    }

    @Author(names = {"Prerna","Zia"})
    @Module(name = "Passenger")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test(dataProvider="testDeleteSmartProfileDataProvider")
    public void testDeleteSmartProfile(Map<String, String> params){
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(params);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        SmartProfilePage<WebDriver> smartProfilePage = SmartProfilePage.getInstance(session);
        SettingPage<WebDriver> settingPage = SettingPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);

        String loginType = params.get("loginType");
        String username = params.get("username");
        String password = params.get("password");
        int delay = Integer.parseInt(params.get("delay"));
        int limit = Integer.parseInt(params.get("limit"));

        step("Delete existing smart profile");
        smartProfilePage.deleteProfileAPI(username,password,delay,limit);

        step("Add smart profile");
        String firstName = CoreFunctions.randomName();
        String lastName = CoreFunctions.randomName();
        String salutation = CoreFunctions.randomSalutation();
        smartProfilePage.addProfileAPI(username,password,firstName,lastName,salutation);

        step("Goto home");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink");
        homePage.loginWithSpecificAccountViaDeeplinkOnly(username, password);

        step("Go to Account");
        homePage.openAccount();

        step("Click Save Passenger Data");
        settingPage.openSavePassengersData();

        step("Open Smart Profile");
        Assertion.assertThat("smart profile exist from search", smartProfilePage.findProfile(firstName + " " + lastName), is(true));
        smartProfilePage.openSmartProfile(firstName + " " + lastName);

        step("Delete Smart Profile");
        smartProfilePage.deleteSmartProfile();

        step("Assertions");
        Assertion.assertThat("profile delete snackBar appears", smartProfilePage.getSnackbarText(), anyOf(containsString("The profile successfully removed."),containsString("Profil berhasil dihapus.")));
        Assertion.assertThat("smart profile exist from search", smartProfilePage.findProfile(firstName + " " + lastName), is(false));
    }

    @Author(names = {"Akbar"}) //TODO fix this
    @Module(name = "Settings")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test
    public void testUserSetNewPIN() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("testUserSetNewPIN");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        SettingPage<WebDriver> settingPage = SettingPage.getInstance(session);

        String password = map.get("password");
        String newPIN = map.get("newPIN");

        step("Open app");
        homePage.gotoHome();

        step("user open app as guest");
        loginPage.loginAsGuest();

        step("Register new account");
        //TODO fix this step

        step("Go to security settings page");
        homePage.openAccount();
        accountPage.openSettings();
        settingPage.openSecuritySettingPage();

        step("Go to PIN section");
        settingPage.setNewPin(newPIN);

        step("Verify step");
        Assertion.assertThat("Verify if the new pin is already set", settingPage.isNewPINSet(), is(true));

        step("Delete account after test finished");
        //TODO fix this step
    }

    @Author(names = {"Yazid"})
    @Module(name = "Settings")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test
    public void testTurnOffTrustedDevice() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("testTurnOffTrustedDevice");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        SettingPage<WebDriver> settingPage = SettingPage.getInstance(session);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        step("Open app");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Go to security settings page");
        homePage.openAccount();
        accountPage.openSettings();
        settingPage.openSecuritySettingPage();

        step("TURN OFF the trusted device");
        settingPage.switchDeviceTrust(false);
        settingPage.setTrustDevicePopupYes();

        Assertion.assertThat("Trusted device notification should be visible", settingPage.verifyNotifDeviceTrust(false), is(true));
        Assertion.assertThat("Trusted device toggle button should be INACTIVE", settingPage.verifyStatusDeviceTrust(false), is(false));
    }

    @Author(names = {"Yazid"})
    @Module(name = "Settings")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test
    public void testTurnOnTrustedDevice() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("testTurnOnTrustedDevice");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        SettingPage<WebDriver> settingPage = SettingPage.getInstance(session);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        step("Open app");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Go to security settings page");
        homePage.openAccount();

        accountPage.openSettings();
        settingPage.openSecuritySettingPage();

        step("TURN ON the trusted device");
        settingPage.switchDeviceTrust(true);
        settingPage.setVerificationStep();

        Assertion.assertThat("Trusted device notification should be visible", settingPage.verifyNotifDeviceTrust(true), is(true));
        Assertion.assertThat("Trusted device toggle button should be ACTIVE", settingPage.verifyStatusDeviceTrust(true), is(true));
    }

    @Author(names = {"Prerna","Zia"})
    @Module(name = "Passenger")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test(dataProvider="testEditSmartProfileDataProvider")
    public void testEditSmartProfile(Map<String, String> params){
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(params);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        SmartProfilePage<WebDriver> smartProfilePage = SmartProfilePage.getInstance(session);
        SettingPage<WebDriver> settingPage = SettingPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);

        String loginType = params.get("loginType");
        String username = params.get("username");
        String password = params.get("password");

        int delay = Integer.parseInt(params.get("delay"));
        int limit = Integer.parseInt(params.get("limit"));

        step("Delete existing smart profile");
        smartProfilePage.deleteProfileAPI(username,password,delay,limit);

        step("Add smart profile");
        String firstName = CoreFunctions.randomName();
        String lastName = CoreFunctions.randomName();
        String salutation = CoreFunctions.randomSalutation();
        smartProfilePage.addProfileAPI(username,password,firstName,lastName,salutation);

        step("Goto home");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink");
        homePage.loginWithSpecificAccountViaDeeplinkOnly(username, password);

        step("Go to Account");
        homePage.openAccount();

        step("Click Save Passenger Data");
        settingPage.openSavePassengersData();

        step("Open Smart Profile");
        Assertion.assertThat("smart profile exist from search", smartProfilePage.findProfile(firstName + " " + lastName), is(true));
        smartProfilePage.openSmartProfile(firstName + " " + lastName);

        step("Edit Smart Profile");
        String newFirstName = CoreFunctions.randomName();
        smartProfilePage.editSmartProfile(newFirstName);

        step("Verify smart profile edited");
        Assertion.assertThat("profile Edit snackBar appears", smartProfilePage.getSnackbarText(), anyOf(equalToIgnoringCase("Yay! You’ve updated your profile."),equalToIgnoringCase("Hore! Profil kamu berhasil diperbarui.")));
        Assertion.assertThat("smart profile exist from search", smartProfilePage.findProfile(newFirstName + " " + lastName), is(true));
    }

    @Author(names = {"Kartik"})
    @Module(name = "Passenger")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test(dataProvider="testEditAccountDetailDataProvider")
    public void testEditAccountDetail(Map<String, String> params){
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(params);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);

        String loginType = params.get("loginType");
        String username = params.get("username");
        String password = params.get("password");

        step("Goto home");
        homePage.gotoHome();

        step("Login as guest");
        loginPage.loginAsGuest();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Go to Account");
        homePage.openAccount();

        step("User open account detail on account page");
        accountPage.clickAccountDetail();

        step("User makes some changes in account detail page");
        String newLastName = accountPage.userEditAccountDetails();
        accountPage.refresh();

        Assertion.assertThat("Verify edited Details ",accountPage.verifyEditedAccountDetail(newLastName),is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "loginFromBookingForm")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test(dataProvider = "testLoginFromBookingFormDataProvider")
    public void testLoginFromBookingForm(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TrainSearchPage<WebDriver> trainSearchPage = TrainSearchPage.getInstance(session);
        TrainSearchResultPage<WebDriver> trainSearchResultPage = TrainSearchResultPage.getInstance(session);
        TrainBookingPage<WebDriver> trainBookingPage = TrainBookingPage.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String name = param.get("name");
        String phoneNumber = param.get("phoneNumber");
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

        step("change currency");
        homePage.changeCurrency("IDR","");

        step("Open train page");
        homePage.openVerticalPageDeeplink("Trains","","IDR");

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
        trainSearchPage.skipPopup();

        step("Skip popup");
        trainSearchPage.skipPopup();

        step("Select departure train");
        trainSearchResultPage.selectTrain();

        step("open booking page");
        trainBookingPage.openLoginPage();

        step("login from booking page");
        loginPage.loginFromDifferentPages(username,password);

        step("verify we are on booking page");
        Assertion.assertThat("In train page with login condition",trainBookingPage.isLoggedIn(), is(true));
        Assertion.assertThat("Verify contact is the same as log in", trainBookingPage.verifyContactDetailData(username, phoneNumber, name), is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "Settings")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test
    public void testLoginDeviceManagement() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("testLoginDeviceManagement");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        SettingPage<WebDriver> settingPage = SettingPage.getInstance(session);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        step("Open app");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Go to security settings page");
        homePage.openAccount();
        accountPage.openSettings();
        settingPage.openSecuritySettingPage();

        step("Go to device management");
        settingPage.openDeviceManagement();

        step("Verify step");
        Assertion.assertThat("Verify if device management page is valid", settingPage.verifyDeviceManagementPage(), is(true));
    }

    @Author(names = {"Kartik"})
    @Module(name = "Settings")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test(dataProvider="testUserTurnOnThreeDsDataProvider")
    public void testUserTurnOnThreeDs(Map<String, String> params) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(params);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        SettingPage<WebDriver> settingPage = SettingPage.getInstance(session);

        String loginType = params.get("loginType");
        String username = params.get("username");
        String password = params.get("password");

        step("Open app");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Go to security settings page");
        homePage.openAccount();
        accountPage.openSettings();
        settingPage.openSecuritySettingPage();

        step("User turn ON double verification;");
        settingPage.userTurnOnDoubleVerification();

        step("Assertion");
        Assertion.assertThat("is double verification is turned ON",settingPage.isDoubleVerificationON(),is(true));
    }

    @Author(names = {"Kartik"})
    @Module(name = "Settings")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test(dataProvider="testUserTurnOffThreeDsVerifiedDataProvider")
    public void testUserTurnOffThreeDsVerified(Map<String, String> params) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(params);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        SettingPage<WebDriver> settingPage = SettingPage.getInstance(session);

        String loginType = params.get("loginType");
        String username = params.get("username");
        String password = params.get("password");

        step("Open app");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Go to security settings page");
        homePage.openAccount();
        accountPage.openSettings();
        settingPage.openSecuritySettingPage();

        step("User turn ON double verification;");
        settingPage.userTurnOnSingleVerification();

        step("Assertion");
        Assertion.assertThat("is Single verification is turned ON",settingPage.isSingleVerificationON(),is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "loginFromPaymentPage")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test(dataProvider = "testLoginFromPaymentPageDataProvider")
    public void testLoginFromPaymentPage(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CarTransferSearchPage<WebDriver> carTransferSearchPage = CarTransferSearchPage.getInstance(session);
        CarTransferSearchResultPage<WebDriver> carTransferSearchResultPage = CarTransferSearchResultPage.getInstance(session);
        CarTransferBookingPage<WebDriver> carTransferBookingPage = CarTransferBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String phoneNumber = param.get("phoneNumber");
        String airport = param.get("airport");
        String destination = param.get("destination");
        String currency = param.get("currency");

        step("User open app");
        homePage.gotoHome();

        step("User input credential");
        loginPage.loginAsGuest();

        step("Change currency");
        homePage.changeCurrency(currency,"");

        step("User open car transfer vertical page");
        homePage.openVerticalPageDeeplink("Car Transfers","",currency);
        webViewPage.switchToWebViewUrl("airport-transfer");
        carTransferSearchPage.closePopups();

        step("User choose car transfer out");
        carTransferSearchPage.chooseTransferType("transfer_out");

        step("user input origin and destination");
        carTransferSearchPage.choosePickup(airport, destination);

        step("user search for car");
        carTransferSearchPage.searchCar();

        if(Configs.PLATFORM == Platform.IOS){
            webViewPage.switchToNative();
            carTransferSearchPage.allowLocationAccess();
            webViewPage.switchToWebViewUrl("airport-transfer");
        }

        step("user select car and vendor");
        carTransferSearchResultPage.selectCarAndVendor();

        step("user continue to booking page");
        carTransferSearchResultPage.continueToBookingPage();

        step("user fill contact detail in booking page");
        carTransferBookingPage.fillDetailOrder(username,phoneNumber);

        step("user continue to payment list page");
        carTransferBookingPage.continueToPayment();
        webViewPage.switchToNative();
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isEmpty(),is(true));

        step("login in the payment page");
        paymentPage.openLoginPage();
        loginPage.loginFromDifferentPages(username,password);
        Assertion.assertThat("No login label shown",!paymentPage.isLoginContainerDisplayed(),is(true));
    }

    @Author(names = {"Kartik","Zia","Dendi"})
    @Module(name = "Settings")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test(dataProvider="testUserLoginWithPINDataProvider")
    public void testUserLoginWithPIN(Map<String, String> params) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(params);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        SettingPage<WebDriver> settingPage = SettingPage.getInstance(session);

        String username = params.get("username");
        String loginType = params.get("loginType");
        String pin = params.get("pin");

        step("Open app");
        homePage.gotoHome();

        step("user open app as guest");
        loginPage.loginAsGuest();

        step("user login using phone");
        boolean isLoggedUsingPin = loginPage.loginWithPin(username,pin);

        if(!isLoggedUsingPin){
            step("Go to security settings page");
            homePage.openAccount();
            accountPage.openSettings();
            settingPage.openSecuritySettingPage();
            settingPage.switchDeviceTrust(true);
            settingPage.setVerificationStep();

            step("User log out from account");
            settingPage.closeSecuritySettingPage();
            settingPage.logOut();

            step("User login with pin");
            loginPage.loginWithPin(username,pin);
        }
        Assertion.assertThat("verify user login successfully", homePage.isUserLogin(), is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "loginUsingPhoneNumber")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test(dataProvider="testLoginUsingPhoneNumberDataProvider")
    public void testLoginUsingPhoneNumber(Map<String, String> params){
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(params);

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);

        String loginType = params.get("loginType");
        String phoneNumber = params.get("phoneNumber");
        String password = params.get("password");

        step("Goto home");
        homePage.gotoHome();
        step("Input credential on login page");
        loginPage.loginWith(loginType, phoneNumber, password);
        step("Verify user login successfully");
        Assertion.assertThat("verify user login successfully", homePage.isUserLogin(), is(true));
    }

    @Author(names = {"Prerna"})
    @Module(name = "ChangeCurrency")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test
    public void verifyChangeInCurrency() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("verifyChangeInCurrency");
        BaseTest.paramsThreadLocal.set(map);

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        SettingPage<WebDriver> settingPage = SettingPage.getInstance(session);

        String currency = map.get("currency");

        step("Goto home");
        homePage.gotoHome();

        step("User Login as Guest");
        loginPage.loginAsGuest();

        step("open Account");
        homePage.openAccount();

        step("check current currency before changing");
        String initialCurrency = settingPage.checkCurrentCurrency();

        step("User change currency");
        String currencyOptionOne = currency.split("/")[0].toUpperCase();
        String currencyOptionTwo = currency.split("/")[1].toUpperCase();
        if(initialCurrency.equals(currencyOptionOne)){
            currency=currencyOptionTwo;
        }else{
            currency=currencyOptionOne;
        }
        settingPage.changeCurrencyIfOtherCurrencyIsSelected(currency);

        step("check currency after currency change");
        String finalCurrency = settingPage.checkCurrentCurrency();

        Assertion.assertThat("Currency changed success fully", !initialCurrency.equals(finalCurrency), is(true));
    }

    @Author(names = {"Prerna"})
    @Module(name = "ChangeCurrency")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test
    public void verifyChangeInCurrencyLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("verifyChangeInCurrencyLogin");
        BaseTest.paramsThreadLocal.set(map);

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        SettingPage<WebDriver> settingPage = SettingPage.getInstance(session);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String currency = map.get("currency");

        step("Open app");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed)");
        homePage.loginIfLoggedOutViaDeeplink(username, password);

        step("open Account");
        homePage.openAccount();

        step("check current currency before changing");
        String initialCurrency = settingPage.checkCurrentCurrency();

        step("User change currency");
        String currencyOptionOne = currency.split("/")[0].toUpperCase();
        String currencyOptionTwo = currency.split("/")[1].toUpperCase();
        if(initialCurrency.equals(currencyOptionOne)){
            currency=currencyOptionTwo;
        }else{
            currency=currencyOptionOne;
        }
        settingPage.changeCurrencyIfOtherCurrencyIsSelected(currency);

        step("check currency after currency change");
        String finalCurrency = settingPage.checkCurrentCurrency();

        Assertion.assertThat("Currency changed success fully", !initialCurrency.equals(finalCurrency), is(true));
    }

    @Author(names = {"Dendi"})
    @Module(name = "HelpCenter")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test
    public void testOpenHelpCenterGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("testOpenHelpCenterGuest");
        BaseTest.paramsThreadLocal.set(map);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HelpCenterPage<WebDriver> helpCenterPage = HelpCenterPage.getInstance(session);

        step("Open app");
        homePage.gotoHome();
        
        step("User Login as Guest");
        loginPage.loginAsGuest();

        step("Open Account");
        homePage.openAccount();

        step("Open help center");
        accountPage.openHelpCenterPage();

        step("Verify help center page open");
        Assertion.assertThat("Verify that help center page is open", helpCenterPage.isHelpCenterOpen(), is(true));
    }

    @Author(names = {"Dendi"})
    @Module(name = "HelpCenter")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test
    public void testOpenHelpCenterLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("testOpenHelpCenterLogin");
        BaseTest.paramsThreadLocal.set(map);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HelpCenterPage<WebDriver> helpCenterPage = HelpCenterPage.getInstance(session);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        step("Open app");
        homePage.gotoHome();
        
        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Open Account");
        homePage.openAccount();

        step("Open help center");
        accountPage.openHelpCenterPage();

        step("Verify help center page open");
        Assertion.assertThat("Verify that help center page is open", helpCenterPage.isHelpCenterOpen(), is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "Logout")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test
    public void testUserClickLogout() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("testUserClickLogout");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        step("Open app");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Logout to change user as guest");
        loginPage.loginAsGuest();
        boolean isLoggedIn = homePage.isUserLogin();

        step("Go to account page");
        homePage.openAccount();

        step("Veirfy step");
        Assertion.assertThat("Verify if homepage is on non-login state", isLoggedIn, is(false));
        Assertion.assertThat("Verify if account page is showing login section", accountPage.isLoggedIn(), is(false));
    }

    @Author(names = {"Prerna"})
    @Module(name = "VoucherBox")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test
    public void testOpenVoucherBox() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("testOpenVoucherBox");
        BaseTest.paramsThreadLocal.set(map);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        step("Open app");
        homePage.gotoHome();

        step("Input credential on login page");
        homePage.loginViaDeeplink(username, password);

        step("Go to account page");
        homePage.openAccount();

        step("Open voucher box");
        accountPage.openVoucherPage();

        step("Verify voucher box open");
        Assertion.assertThat("Verify voucher page open", accountPage.isVoucherPageOpen(), is(true));
    }

    @Author(names = {"Dendi"})        
    @Module(name = "DeviceInfo")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test
    public void testDeviceInfoGuest(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("testDeviceInfoGuest");
        BaseTest.paramsThreadLocal.set(map);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);

        step("Open app");
        homePage.gotoHome();

        step("Logout to change user as guest");
        loginPage.loginAsGuest();

        step("Go to account page");
        homePage.openAccount();

        step("Open About Tiket.com");
        accountPage.openAboutTiketDotCom();

        step("Open device info page");
        accountPage.openDeviceInfoMenu();

        step("Verify device info open");
        accountPage.verifyDeviceInfoShown();

        step("Verify device info copy button");
        accountPage.verifyCopyDeviceInfoButton();

    }

    @Author(names = {"Dendi"})
    @Module(name = "DeviceInfo")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test
    public void testDeviceInfoLogin(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("testDeviceInfoLogin");
        BaseTest.paramsThreadLocal.set(map);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        step("Open app");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Go to account page");
        homePage.openAccount();

        step("Open About Tiket.com");
        accountPage.openAboutTiketDotCom();

        step("Open device info page");
        accountPage.openDeviceInfoMenu();

        step("Verify device info open");
        accountPage.verifyDeviceInfoShown();

        step("Verify device info copy button");
        accountPage.verifyCopyDeviceInfoButton();
    }

    @Author(names = {"Ashish"})
    @Module(name = "MEMBERSHIP›Settings")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test(dataProvider = "testUserTurnOnLocationLoginDataProvider")
    public void testUserTurnOnLocationLogin(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        SettingPage<WebDriver> settingPage = SettingPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        step("Wait for app to launch");
        homePage.gotoHome();

        step("User login into their account");
        loginPage.loginWith(loginType, username, password);

        step("Open account page");
        homePage.openAccount();

        step("turn on location");
        settingPage.setLocationSwitch("ON");
        Assertion.assertThat("is location on", settingPage.isLocationOn(), is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "MEMBERSHIP›Settings")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test(dataProvider = "testUserTurnOnLocationGuestDataProvider")
    public void testUserTurnOnLocationGuest(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        SettingPage<WebDriver> settingPage = SettingPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("User login into their account");
        loginPage.loginAsGuest();

        step("Open account page");
        homePage.openAccount();

        step("turn on location");
        settingPage.setLocationSwitch("ON");

        step("verify if location is on");
        Assertion.assertThat("is location on", settingPage.isLocationOn(), is(true));
    }

    @Author(names = {"Kartik", "Prerna"})
    @Module(name = "Others")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test
    public void testCheckTermAndConditionLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("testCheckTermAndConditionLogin");
        BaseTest.paramsThreadLocal.set(map);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        step("Open app");
        homePage.gotoHome();

        step("User login into their account");
        loginPage.loginWith(loginType, username, password);

        step("Go to account page");
        homePage.openAccount();

        step("Open About tiket.com option");
        accountPage.openAboutTiketDotCom();

        step("Open Terms and condition");
        accountPage.openTermAndCondition();

        step("Assertion");
        Assertion.assertThat("Verify term and condition text is shown", accountPage.verifyTermAndConditionPage(), is(true));
    }

    @DataProvider(name = "changeLanguageENDataProvider")
    public Object[][] changeLanguageENDataProvider() {
        return getObjectParams("testShouldChangeLanguageEN");
    }

    @DataProvider(name = "changeLanguageIDDataProvider")
    public Object[][] changeLanguageDataIDProvider() {
        return getObjectParams("testShouldChangeLanguageID");
    }

    @DataProvider(name = "changeLanguageENLoginDataProvider")
    public Object[][] changeLanguageLoginENDataProvider() {
        return getObjectParams("testShouldChangeLanguageENLogin");
    }

    @DataProvider(name = "changeLanguageIDLoginDataProvider")
    public Object[][] changeLanguageIDLoginDataProvider() {
        return getObjectParams("testShouldChangeLanguageIDLogin");
    }

    @DataProvider(name = "testRegisterEmailFromBookingFormDataProvider")
    public Object[][] testRegisterEmailFromBookingFormDataProvider() {
        return getObjectParams("testRegisterEmailFromBookingForm");
    }

    @DataProvider(name = "testAccountDetailDataProvider")
    public Object[][] testAccountDetailDataProvider() {
        return TestData.getObjectParams("testLogin");
    }

    @DataProvider(name = "testLoginProvider")
    public Object[][] shouldChangeLanguageDataProvider() {
        return TestData.getObjectParams("testLogin");
    }

    @DataProvider(name = "testGuestProvider")
    public Object[][] testGuestProvider() {
        return TestData.getObjectParams("testGuest");
    }

    @DataProvider(name = "testRegisterNewEmailNewPhoneDataProvider")
    public Object[][] testRegisterNewEmailNewPhoneDataProvider() {
        return getObjectParams("testRegisterNewEmailNewPhone");
    }

    @DataProvider(name = "testRegisterNewPhoneNewEmailDataProvider")
    public Object[][] testRegisterNewPhoneNewEmailDataProvider() {
        return getObjectParams("testRegisterNewPhoneNewEmail");
    }

    @DataProvider(name = "testVerifiedPhoneDataProvider")
    public Object[][] testVerifiedPhoneDataProvider() {
        return getObjectParams("testVerifiedPhone");
    }

    @DataProvider(name = "testRegisterWithGuestEmailNewPhoneDataProvider")
    public Object[][] testRegisterWithGuestEmailNewPhoneDataProvider() {
        return getObjectParams("testRegisterWithGuestEmailNewPhone");
    }

    @DataProvider(name = "testRegisterWithGuestPhoneGuestEmailDataProvider")
    public Object[][] testRegisterWithGuestPhoneGuestEmailDataProvider() {
        return getObjectParams("testRegisterWithGuestPhoneGuestEmail");
    }

    @DataProvider(name = "testRegisterWithGuestPhoneNewEmailDataProvider")
    public Object[][] testRegisterWithGuestPhoneNewEmailDataProvider() {
        return getObjectParams("testRegisterWithGuestPhoneNewEmail");
    }

    @DataProvider(name = "testRegisterWithNewPhoneGuestEmailDataProvider")
    public Object[][] testRegisterWithNewPhoneGuestEmailDataProvider() {
        return getObjectParams("testRegisterWithNewPhoneGuestEmail");
    }

    @DataProvider(name = "testRegisterWithNewEmailGuestPhoneDataProvider")
    public Object[][] testRegisterWithNewEmailGuestPhoneDataProvider() {
        return getObjectParams("testRegisterWithNewEmailGuestPhone");
    }

    @DataProvider(name = "testRegisterWithGuestEmailGuestPhoneDataProvider")
    public Object[][] testRegisterWithGuestEmailGuestPhoneDataProvider() {
        return getObjectParams("testRegisterWithGuestEmailGuestPhone");
    }

    @DataProvider(name = "testAddSmartProfileDataProvider")
    public Object[][] testAddSmartProfileDataProvider() {
        return TestData.getObjectParams("testAddSmartProfile");
    }

    @DataProvider(name = "testDeleteSmartProfileDataProvider")
    public Object[][] testDeleteSmartProfileDataProvider() {
        return TestData.getObjectParams("testDeleteSmartProfile");
    }

    @DataProvider(name = "testEditSmartProfileDataProvider")
    public Object[][] testEditSmartProfileDataProvider() {
        return TestData.getObjectParams("testEditSmartProfile");
    }

    @DataProvider(name = "testEditAccountDetailDataProvider")
    public Object[][] testEditAccountDetailDataProvider() {
        return TestData.getObjectParams("testEditAccountDetail");
    }

    @DataProvider(name = "testLoginFromBookingFormDataProvider")
    public Object[][] testLoginFromBookingFormDataProvider() {
        return TestData.getObjectParams("testLoginFromBookingForm");
    }

    @DataProvider(name = "testForgetPasswordWithPhoneNoDataProvider")
    public Object[][] testForgetPasswordWithPhoneNoDataProvider() {
        return getObjectParams("testForgetPasswordWithPhoneNo");
    }

    @DataProvider(name = "testUserTurnOnThreeDsDataProvider")
    public Object[][] testUserTurnOnThreeDsDataProvider() {
        return TestData.getObjectParams("testUserTurnOnThreeDs");
    }

    @DataProvider(name = "testUserTurnOffThreeDsVerifiedDataProvider")
    public Object[][] testUserTurnOffThreeDsVerifiedDataProvider() {
        return TestData.getObjectParams("testUserTurnOffThreeDsVerified");
    }

    @DataProvider(name = "testLoginFromPaymentPageDataProvider")
    public Object[][] testLoginFromPaymentPageDataProvider() {
        return TestData.getObjectParams("testLoginFromPaymentPage");
    }

    @DataProvider(name = "testLoginUsingPhoneNumberDataProvider")
    public Object[][] testLoginUsingPhoneNumberDataProvider() {
        return TestData.getObjectParams("testLoginUsingPhoneNumber");
    }

    @DataProvider(name = "testUserLoginWithPINDataProvider")
    public Object[][] testUserLoginWithPINDataProvider() {
        return getObjectParams("testUserLoginWithPIN");
    }

    @DataProvider(name = "testUserTurnOnLocationLoginDataProvider")
    public Object[][] testUserTurnOnLocationLoginDataProvider() {
        return getObjectParams("testUserTurnOnLocationLogin");
    }

    @DataProvider(name = "testUserTurnOnLocationGuestDataProvider")
    public Object[][] testUserTurnOnLocationGuestDataProvider() {
        return getObjectParams("testUserTurnOnLocationGuest");
    }

    @Author(names = {"Prerna"})
    @Module(name = "Other")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test
    public void testPrivacyPolicyMenuLogin(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("testPrivacyPolicyMenuLogin");
        BaseTest.paramsThreadLocal.set(map);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        step("Open app");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed)");
        homePage.loginIfLoggedOutViaDeeplink(username, password);

        step("Go to account page");
        homePage.openAccount();

        step("Open other page");
        accountPage.openAboutTiketDotCom();

        step("Open Privacy Policy Menu");
        accountPage.openPrivacyPolicyMenu();

        step("Verify Privacy Policy open");
        Assertion.assertThat("Privacy Policy is Shown", accountPage.verifyPrivacyPolicy(), is(true));
    }

    @Author(names = {"Prerna"})
    @Module(name = "Other")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test
    public void testPrivacyPolicyMenuGuest(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("testPrivacyPolicyMenuGuest");
        BaseTest.paramsThreadLocal.set(map);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);

        step("Open app");
        homePage.gotoHome();

        step("Login as Guest");
        loginPage.loginAsGuest();

        step("Go to account page");
        homePage.openAccount();

        step("Open other page");
        accountPage.openAboutTiketDotCom();

        step("Open Privacy Policy Menu");
        accountPage.openPrivacyPolicyMenu();

        step("Verify Privacy Policy open");
        Assertion.assertThat("Privacy Policy is Shown", accountPage.verifyPrivacyPolicy(), is(true));
    }

    @Author(names = {"Zia"})
    @Module(name = "Settings")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test
    public void testRequestDeleteAccount() throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("testRequestDeleteAccount");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        SettingPage<WebDriver> settingPage = SettingPage.getInstance(session);
        Unm<WebDriver> unm = new Unm<>(session);

        step("Open app");
        RegisterNewAccountData accountData = unm.registerNewAccountViaApiUNM();
        String username = accountData.email;
        String password = accountData.password;
        String loginType = "email";
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);

        step("Go to security settings page");
        homePage.openAccount();
        accountPage.openSettings();
        settingPage.openSecuritySettingPage();

        step("Click delete account tab");
        settingPage.clickDeleteAccount();

        step("Fill delete account form");
        settingPage.fillDeleteAccountForm();

        step("Submit delete account request");
        settingPage.clickSubmitDelete();

        step("Verify account deleted");
        Assertion.assertThat("Session Expired", settingPage.isSessionExpired(), is(true));
        Assertion.assertThat("In non login condition", homePage.isUserLogin(), is(false));
    }

    @Author(names = {"Kartik", "Prerna"})
    @Module(name = "others")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test
    public void testCheckTermAndConditionGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("testCheckTermAndConditionGuest");
        BaseTest.paramsThreadLocal.set(map);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);

        step("Open app");
        homePage.gotoHome();

        step("User login into their account");
        loginPage.loginAsGuest();

        step("Go to account page");
        homePage.openAccount();

        step("Open About tiket.com option");
        accountPage.openAboutTiketDotCom();

        step("Open Terms and condition");
        accountPage.openTermAndCondition();

        step("Assertion");
        Assertion.assertThat("Verify term and condition text is shown", accountPage.verifyTermAndConditionPage(), is(true));
    }

    @Author(names = {"Kartik", "Prerna"})
    @Module(name = "Other")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test
    public void testCheckAboutTiketDotComGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("testCheckAboutTiketDotComGuest");
        BaseTest.paramsThreadLocal.set(map);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);

        step("Open app");
        homePage.gotoHome();

        step("Login as Guest");
        loginPage.loginAsGuest();

        step("Go to account page");
        homePage.openAccount();

        step("Open About tiket.com option");
        accountPage.openAboutTiketDotCom();

        step("Verify about tiket.com page open");
        Assertion.assertThat("about page is Shown", accountPage.verifyAboutTiketDotCom(), is(true));
    }

    @Author(names = {"Kartik", "Prerna"})
    @Module(name = "Other")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test
    public void testCheckAboutTiketDotComLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("testCheckAboutTiketDotComLogin");
        BaseTest.paramsThreadLocal.set(map);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        step("Open app");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Go to account page");
        homePage.openAccount();

        step("Open About tiket.com option");
        accountPage.openAboutTiketDotCom();

        step("Verify about tiket.com page open");
        Assertion.assertThat("about page is Shown", accountPage.verifyAboutTiketDotCom(), is(true));
    }


    @Author(names = {"Zia"}) //TODO fix this
    @Module(name = "NewRegister")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test
    public void testRegisterPhoneFromPaymentList() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("testRegisterPhoneFromPaymentList");
        BaseTest.paramsThreadLocal.set(map);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CarTransferSearchPage<WebDriver> carTransferSearchPage = CarTransferSearchPage.getInstance(session);
        CarTransferSearchResultPage<WebDriver> carTransferSearchResultPage = CarTransferSearchResultPage.getInstance(session);
        CarTransferBookingPage<WebDriver> carTransferBookingPage = CarTransferBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        Unm<WebDriver> unm = new Unm<>(session);

        String airport = map.get("airport");
        String destination = map.get("destination");
        String currency = map.get("currency");

        String password = map.get("password");
        String phone = "812"+CoreFunctions.randomInt(8);
        String email = CoreFunctions.randomEmail();
        String fullName = CoreFunctions.randomName() + " " + CoreFunctions.randomName();

        step("User open app");
        homePage.gotoHome();

        step("User input credential");
        loginPage.loginAsGuest();

        step("Change currency");
        homePage.changeCurrency(currency,"");

        step("User open car transfer vertical page");
        homePage.openVerticalPageDeeplink("Car Transfers", "", currency);
        webViewPage.switchToWebViewUrl("airport-transfer");
        carTransferSearchPage.closePopups();

        step("User choose car transfer out");
        carTransferSearchPage.chooseTransferType("transfer_out");

        step("user input origin and destination");
        carTransferSearchPage.choosePickup(airport, destination);

        step("user search for car");
        carTransferSearchPage.searchCar();

        if(Configs.PLATFORM == Platform.IOS){
            webViewPage.switchToNative();
            carTransferSearchPage.allowLocationAccess();
            webViewPage.switchToWebViewUrl("airport-transfer");
        }

        step("user select car and vendor");
        carTransferSearchResultPage.selectCarAndVendor();

        step("user continue to booking page");
        carTransferSearchResultPage.continueToBookingPage();

        step("user fill contact detail in booking page");
        carTransferBookingPage.fillDetailOrder(CoreFunctions.randomEmail(),"812"+CoreFunctions.randomInt(8));

        step("user continue to payment list page");
        carTransferBookingPage.continueToPayment();
        webViewPage.switchToNative();
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isEmpty(),is(true));

        step("register from payment list page");
        //TODO fix this step

        step("Delete account after test finished");
        homePage.gotoHome();
        loginPage.loginAsGuest();
        unm.deleteAccountViaApi(email);
    }


    @Author(names = {"Zia"})
    @Module(name = "User login")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test
    public void testLoginDifferentCurrencyFromPaymentPage() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginDifferentCurrencyFromPaymentPage");
        BaseTest.paramsThreadLocal.set(param);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CarTransferSearchPage<WebDriver> carTransferSearchPage = CarTransferSearchPage.getInstance(session);
        CarTransferSearchResultPage<WebDriver> carTransferSearchResultPage = CarTransferSearchResultPage.getInstance(session);
        CarTransferBookingPage<WebDriver> carTransferBookingPage = CarTransferBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String phoneNumber = "8" + randomInt(10);
        String airport = param.get("airport");
        String destination = param.get("destination");
        String currency1 = param.get("currency1");
        String currency2 = param.get("currency2");

        String loginType = param.get("loginType");

        step("Open app");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Change currency");
        homePage.changeCurrency(currency1,username);

        step("User log out");
        loginPage.loginAsGuest();

        step("Change currency");
        homePage.changeCurrency(currency2,"");

        step("User open car transfer vertical page");
        homePage.openVerticalPage("Car Transfers");
        webViewPage.switchToWebViewUrl("airport-transfer");
        carTransferSearchPage.closePopups();

        step("User choose car transfer out");
        carTransferSearchPage.chooseTransferType("transfer_out");

        step("user input origin and destination");
        carTransferSearchPage.choosePickup(airport, destination);

        step("user search for car");
        carTransferSearchPage.searchCar();

        if(Configs.PLATFORM == Platform.IOS){
            webViewPage.switchToNative();
            carTransferSearchPage.allowLocationAccess();
            webViewPage.switchToWebViewUrl("airport-transfer");
        }

        step("user select car and vendor");
        carTransferSearchResultPage.selectCarAndVendor();

        step("user continue to booking page");
        carTransferSearchResultPage.continueToBookingPage();

        step("user fill contact detail in booking page");
        carTransferBookingPage.fillDetailOrder(username, phoneNumber);

        step("user continue to payment list page");
        carTransferBookingPage.continueToPayment();
        webViewPage.switchToNative();
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id", !orderID.isEmpty(), is(true));

        step("login in the payment page");
        paymentPage.openLoginPage();
        loginPage.loginFromDifferentPages(username, password);
        Assertion.assertThat("No login label shown", !paymentPage.isLoginContainerDisplayed(), is(true));
        Assertion.assertThat("Currency in payment page still equals currency after changing", paymentPage.getTotalPrice().contains(currency2), is(true));
    }

    @Author(names = {"Zia"})
    @Module(name = "User login")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test
    public void testLoginDifferentCurrencyFromSRP() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginDifferentCurrencyFromSRP");
        BaseTest.paramsThreadLocal.set(param);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightApi<?> flightApi = new  FlightApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String airline = param.get("airline");

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        String currency1 = param.get("currency1");
        String currency2 = param.get("currency2");

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDate(
                username,password,
                originAirport,destinationAirport,daysToDepartureDate,
                adultPassengers,childPassengers,infantPassengers,
                tripType,"","","","",currency1
        );
        String departureDate =CoreFunctions.convertToLongDate(flightDynamicData.departureDate);

        step("Login with account");
        loginPage.loginWith(loginType,username,password);

        step("Change account currency to "+currency1);
        homePage.changeCurrency(currency1,username);

        step("Logout and login as Guest");
        loginPage.loginAsGuest();

        step("Change currency to "+currency2);
        homePage.changeCurrency(currency2,"");

        step("Open flights page");
        homePage.openVerticalPage("Flights");
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(flightDynamicData, tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("login From Search Page");
        flightSearchResultPage.clickLoginBanner();
        loginPage.loginFromDifferentPages(username,password);

        step("verify login success");
        Assertion.assertThat("Banner to register/login from SRP should not be there", flightSearchResultPage.isRegisterBannerDisappear(), is(true));

        step("verify currency still "+currency2);
        Assertion.assertThat("Currency still "+currency2, flightSearchResultPage.getPrices().stream().allMatch(price -> (price.contains(currency2))), is(true));
    }

    @Author(names = {"Zia"})
    @Module(name = "Payment Methods")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test
    public void testLoginClickOVO() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginClickOVO");
        BaseTest.paramsThreadLocal.set(param);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String loginType = param.get("loginType");

        step("Open app");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("change to IDR");
        homePage.changeCurrency("IDR",username);

        step("User open account page to check account detail");
        homePage.openAccount();

        step("Click see all payment methods");
        accountPage.clickSeeAllPayment();

        step("Click OVO");
        accountPage.selectPaymentMethod("ovo");

        step("Verify OVO alert");
        Assertion.assertThat("Got ovo menu", accountPage.isOvoPaymentMenuOpen(), is(true));

        step("Click OVO menu");
        accountPage.clickOvoLinkageButton();

        step("Verify OVO phone number is registered");
        Assertion.assertThat("Got popup phone has not been registered", accountPage.isOvoPhoneNotRegistered(), is(true));
    }

    @Author(names = {"Dendi"})
    @Module(name = "Account")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test
    public void testChangeVerifiedPhoneNumber() throws ExecutionException, InterruptedException{
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testChangeVerifiedPhoneNumber");
        BaseTest.paramsThreadLocal.set(param);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        Unm<WebDriver> unm = new Unm<>(session);

        step("Open app");
        homePage.gotoHome();

        step("Create new account");
        RegisterNewAccountData accountData = unm.registerNewAccountViaApiUNM();
        String username = accountData.email;
        String password = accountData.password;
        String newPhoeNumber = "8" + randomInt(10);
        String loginType = "email";

        step("Input credential on login page");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);

        step("Open account page");
        homePage.openAccount();

        step("Open account detail page");
        accountPage.clickAccountDetail();

        step("Open edit phone number");
        //TODO fix this step

        step("Verify phone number edited");
        String newPhoneNumberValueAfterEdit = accountPage.getPhoneNumberInChangeEmailOrPhonePage();
        Assertion.assertThat("Verify phone number in change account email/phone page should be new phone number", newPhoneNumberValueAfterEdit, equalTo(newPhoeNumber));
        accountPage.closeChangeEmailOrPhonePage();
        String accountDetailPhoneNumber = accountPage.getAccountDetailPhoneNumber();
        Assertion.assertThat("Verify phone number should be new phone number", accountDetailPhoneNumber, equalTo(newPhoeNumber));

        step("delete account after test finished");
        homePage.gotoHome();
        loginPage.loginAsGuest();

        step("delete account");
        unm.deleteAccountViaApi(accountData.unmData);
    }

    @Author(names = {"Ashish"})
    @Module(name = "Voucher & Review")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test
    public void testUserClickReviewMenuLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testUserClickReviewMenuLogin");
        BaseTest.paramsThreadLocal.set(param);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        ReviewPage<WebDriver> reviewPage = ReviewPage.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String loginType = param.get("loginType");

        step("Open app");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("User open account page to check account detail");
        homePage.openAccount();

        step("open review page");
        accountPage.openMyReviewPage();
        Assertion.assertThat("is review page open",reviewPage.isReviewPageOpen(),is(true));
    }

    @Author(names = {"Zia"})
    @Module(name = "Payment Methods")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test
    public void testLoginClickBCA() {
        //obsolete
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginClickBCA");
        BaseTest.paramsThreadLocal.set(param);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String loginType = param.get("loginType");

        step("Open app");
        homePage.gotoHome();

        step("use logout");
        loginPage.loginAsGuest();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("change to IDR");
        homePage.changeCurrency("IDR",username);

        step("User open account page to check account detail");
        homePage.openAccount();

        step("Click see all payment methods");
        accountPage.clickSeeAllPayment();

        step("Click OneKlik");
        accountPage.selectPaymentMethod("oneklik");

        step("Click Linkage button");
        accountPage.clickAddBcaCard();

        step("Verify BCA Card Form");
        Assertion.assertThat("Verify BCA card detail form fields", accountPage.verifyBCAForm(username), is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "Referrals")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test
    public void testUserClickShareReferralCodeNonPNVLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testUserClickShareReferralCodeNonPNVLogin");
        BaseTest.paramsThreadLocal.set(param);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        ReferralPage<WebDriver> referralPage = ReferralPage.getInstance(session);
        SettingPage<WebDriver> settingPage = SettingPage.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String loginType = param.get("loginType");
        String userAccountId = param.get("userAccountId");

        step("Open app");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginAsGuest();
        loginPage.loginWithUnverifiedPhoneNumber(loginType, username, password);

        step("remove verification from phone number");
        boolean isRemovePNVSuccess = homePage.addOrRemovePhoneNumberVerification(
                userAccountId, "Test unverify from automation", false
        );
        Assertion.assertThat("is remove pnv success",isRemovePNVSuccess,is(true));

        step("Refresh homepage");
        homePage.refreshHomePage(1);

        step("Change currency");
        homePage.changeCurrency("IDR",username);

        step("User open account page to check account detail");
        homePage.openAccount();
        accountPage.refresh();

        step("open referral page");
        accountPage.openReferralPage();
        Assertion.assertThat("is verify phone number popup displayed",referralPage.verificationPopUpDisplayed(),is(true));

        step("dismiss the phone number verification pop up");
        referralPage.clickCancelBtn();

        step("click on share referral invitation");
        referralPage.shareReferralInvitation();
        Assertion.assertThat("is verify phone number popup displayed",referralPage.verificationPopUpDisplayed(),is(true));

        step("log out");
        referralPage.clickCancelBtn();
        referralPage.clickBackBtn();
        accountPage.openSettings();
        settingPage.logOut();
    }

    @Author(names = {"Ashish"})
    @Module(name = "Referrals")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test
    public void testUserClickReferralMenuPNVLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testUserClickReferralMenuPNVLogin");
        BaseTest.paramsThreadLocal.set(param);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        ReferralPage<WebDriver> referralPage = ReferralPage.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String loginType = param.get("loginType");

        step("Open app");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Change currency");
        homePage.changeCurrency("IDR",username);

        step("User open account page to check account detail");
        homePage.openAccount();

        step("open referral page");
        accountPage.openReferralPage();
        referralPage.removeReferralPopup();
        Assertion.assertThat("is referral page open",referralPage.isReferralPageOpen(),is(true));
    }

    @Author(names = {"Dendi"})
    @Module(name = "Referrals")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test
    public void testUserClickReferralMenuNonPNV() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testUserClickReferralMenuNonPNV");
        BaseTest.paramsThreadLocal.set(param);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        ReferralPage<WebDriver> referralPage = ReferralPage.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String loginType = param.get("loginType");
        String expectedTitleEn = param.get("expectedTitleEn");
        String expectedTitleId = param.get("expectedTitleId");
        String userAccountId = param.get("userAccountId");

        step("Open app");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginAsGuest();
        loginPage.loginWithUnverifiedPhoneNumber(loginType, username, password);
        String appLang = homePage.getLanguageApp();

        step("remove verification from phone number");
        boolean isRemovePNVSuccess = homePage.addOrRemovePhoneNumberVerification(
                userAccountId, "Test unverify from automation", false
        );
        Assertion.assertThat("is remove pnv success",isRemovePNVSuccess,is(true));

        step("Change currency");
        homePage.changeCurrency("IDR",username);

        step("User open account page to check account detail");
        homePage.openAccount();
        accountPage.refresh();

        step("open referral page");
        accountPage.openReferralPage();

        step("verify referral page verification chalange appear");
        referralPage.verifyPhoneVerificationChallengePopUp(appLang,expectedTitleEn, expectedTitleId);
    }

    @Author(names = {"Dendi"})
    @Module(name = "Referrals")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test
    public void testUserClickShareReferralCodePNVLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testUserClickShareReferralCodePNVLogin");
        BaseTest.paramsThreadLocal.set(param);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        ReferralPage<WebDriver> referralPage = ReferralPage.getInstance(session);
        SettingPage<WebDriver> settingPage = SettingPage.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String loginType = param.get("loginType");
        String userFullName = param.get("userFullName");
        String referralMessageId = param.get("referralMessageId");
        String referralMessageEn = param.get("referralMessageEn");

        step("Open app");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);

        step("User open account page to check account detail");
        homePage.openAccount();

        accountPage.refresh();
        String appLang = homePage.getLanguageApp();

        step("Open account");
        homePage.openAccount();

        step("open settings");
        accountPage.openSettings();

        step("change currency");
        settingPage.changeCurrencyIfOtherCurrencyIsSelected("IDR");
        
        step("back from settings");
        settingPage.clickBackBtn();

        step("open referral page");
        accountPage.openReferralPage();

        step("dismiss information overlay popup");
        referralPage.removeReferralPopup();

        step("click on share referral invitation");
        referralPage.shareReferralInvitation();

        step("Verify list of contact present");
        Assertion.assertThat("verify contact list present", referralPage.verifyListOfContactPresent(), is(true));

        step("Verify native app share dialog present");
        Assertion.assertThat("verify native app share dialog present", referralPage.verifyNativeAppShareDialogPresent(userFullName,appLang,referralMessageId,referralMessageEn), is(true));
    }

    @Author(names = {"Zia"})
    @Module(name = "Account")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test
    public void testChangeEmailOtpToPhone() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testChangeEmailOtpToPhone");
        BaseTest.paramsThreadLocal.set(param);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        SettingPage<WebDriver> settingPage = SettingPage.getInstance(session);
        Unm<WebDriver> unm = new Unm<>(session);

        step("Open app");
        homePage.gotoHome();

        step("register new email via api");
        RegisterNewAccountData accountData = unm.registerNewAccountViaApiUNM();
        String username = accountData.email;
        String password = accountData.password;
        String loginType = "email";

        step("Input credential on login page");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);

        step("User open my account");
        homePage.openAccount();

        step("Open account settings");
        accountPage.openSettings();
        settingPage.openAccountSetting();

        step("Change email with otp to phone");
        //TODO fix this step

        step("Verify email changed and snackbar appeared");
        //TODO fix this step

        step("delete account after test finished");
        //TODO fix this step
    }

    @Author(names = {"Zia"})
    @Module(name = "Account")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test
    public void testChangeEmailOtpToEmail() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testChangeEmailOtpToEmail");
        BaseTest.paramsThreadLocal.set(param);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        SettingPage<WebDriver> settingPage = SettingPage.getInstance(session);
        Unm<WebDriver> unm = new Unm<>(session);

        step("Open app");
        RegisterNewAccountData accountData = unm.registerNewAccountViaApiUNM();
        String username = accountData.email;
        String password = accountData.password;
        String loginType = "email";
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);

        step("User open my account");
        homePage.openAccount();

        step("Open account settings");
        accountPage.openSettings();
        settingPage.openAccountSetting();

        step("Change email with otp to email");
        //TODO fix this step

        step("Verify email changed and snackbar appeared");
        //TODO fix this step

        step("delete account after test finished");
        //TODO fix this step
    }

    @Author(names = {"Zia"})
    @Module(name = "LoyaltyProgram")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test
    public void testClickLoyaltyProgram() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testClickLoyaltyProgram");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        LoyaltyPartnerPage<WebDriver> loyaltyPartnerPage = LoyaltyPartnerPage.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String loginType = param.get("loginType");

        step("Open app");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);
        homePage.changeCurrency("IDR","");

        step("User open account page to check account detail");
        homePage.openAccount();

        step("open loyalty partners page");
        accountPage.openLoyaltyPartnerPage();
        Assertion.assertThat("is loyalty partner page open", loyaltyPartnerPage.isLoyaltyPartnerPageOpen(), is(true));
    }

    @Author(names = {"Zia"})
    @Module(name = "Account")
    @Tribe(name = "Member")
    @Vertical(name ="Platform")
    @Test
    public void testChangeEmailWithEmailUnverified () {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testChangeEmailWithEmailUnverified");
        BaseTest.paramsThreadLocal.set(param);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        SettingPage<WebDriver> settingPage = SettingPage.getInstance(session);

        step("Open app");
        homePage.gotoHome();

        step("Create new account via api");
//        Map<String, String> userData = accountPage.registerNewAccountEmailUnverifiedViaApi();
        String username = "";//userData.get("email");
        String password = "";//userData.get("password");
        String loginType = "email";

        step("Input credential on login page");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);

        step("User open my account");
        homePage.openAccount();

        step("Open account settings");
        accountPage.openSettings();
        settingPage.openAccountSetting();

        step("Change email with otp to phone");
        //TODO fix this step

        step("Verify email changed and snackbar appeared");
        //TODO fix this step

        step("delete account after test finished");
        //TODO fix this step
    }

    @Author(names = {"Dendi"})
    @Module(name = "Payment Methods")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test
    public void testRedirectToPaylaterPage () {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testRedirectToPaylaterPage");
        BaseTest.paramsThreadLocal.set(param);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);

        String username = param.get("email");
        String password = param.get("password");
        String loginType = param.get("loginType");

        step("Open app");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed) and change currency to IDR");
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, "IDR");

        step("User open account page");
        homePage.openAccount();

        step("Open pay later page");
        accountPage.openPaylaterPage();

        step("Verify paylater page open");
        Assertion.assertThat("Verify paylater page open", accountPage.verifyPaylaterPageOpen(), is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "Booking Form")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test
    public void testBookingProductsWithFreshGuestEmail() throws Exception {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> params = getParam("testBookingProductsWithFreshGuestEmail");
        BaseTest.paramsThreadLocal.set(params);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdSelectedPackageDetailPageWeb<?> ttdSelectedPackageDetailPageWeb = TtdSelectedPackageDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        LoginPage <WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);

        String product = params.get("product");
        int ticketQty = Integer.parseInt(params.get("ticket_qty"));
        String email = CoreFunctions.randomEmail();
        String phoneNumber = "8123" + CoreFunctions.randomInt(6);
        String currency = params.get("currency");

        step("Go to home page");
        homePage.gotoHome();

        step("User input credential on login page");
        loginPage.loginAsGuest();

        step("Change currency to \"" + currency + "\"");
        homePage.changeCurrency(currency, "");

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", "", currency);

        step("Search the product");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(product);

        step("select the activity");
        ttdSearchPage.selectRecommendedActivity(product);

        step("Select ticket");
        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.goToPackagesList();
        ttdProductDetailPageWeb.chooseTicket();

        step("Set total ticket");
        ttdSelectedPackageDetailPageWeb.fillAvailableSection();
        ttdSelectedPackageDetailPageWeb.setTicketQty(ticketQty, 0);

        step("Submit package detail");
        ttdSelectedPackageDetailPageWeb.clickBookButton();
        webViewPage.switchToNative();

        step("Completing booking form data");
        webViewPage.switchToWebViewUrl("to-do");
        ttdBookingFormPageWeb.fillContactOrderDetail(false, email, phoneNumber);
        ttdBookingFormPageWeb.fillGuestDetail();

        step("Submit booking");
        ttdBookingFormPageWeb.continueToPaymentPage();
        ttdBookingFormPageWeb.checkPriceNotifChange();
        webViewPage.switchToNative();

        step("Successfully redirected to the payment page");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id", !orderID.isEmpty(), is(true));

        step("check email recorded in member DB");
        Assertion.assertThat("is user email recorded in member DB",accountPage.verifyEmailPresentInDB(email),is(true) );
    }

    @Author(names = {"Prerna"})
    @Module(name = "UserLogin")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test
    public void testVerifyPNOnLoginPopUp() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testVerifyPNOnLoginPopUp");
        BaseTest.paramsThreadLocal.set(param);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String loginType = param.get("loginType");
        String userAccountId = param.get("userAccountId");

        step("Open app");
        homePage.gotoHome();

        step("remove verification from phone number from API ");
        boolean isRemovePNVSuccess = homePage.addOrRemovePhoneNumberVerification(
                userAccountId, "Test unverify from automation", false
        );
        Assertion.assertThat("is remove pnv success",isRemovePNVSuccess,is(true));

        step("Change currency");
        homePage.changeCurrency("IDR","");

        step("Input credential on login page");
        loginPage.loginAsGuest();
        boolean isPNVerified = loginPage.checkLoginWithUnverifiedPhoneNumber(loginType, username, password);

        Assertion.assertThat("Is Phone Number Verified", isPNVerified, is(true));
    }

    @Author(names = {"Prerna"})
    @Module(name = "AccountPage")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test
    public void testReferAndEarnMenu() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testReferAndEarnMenu");
        BaseTest.paramsThreadLocal.set(param);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);

        step("Open app");
        homePage.gotoHome();

        step("User open account page");
        homePage.openAccount();

        step("Open the login page from the Blibli Tiket Loyalty Program Banner");
        accountPage.clickLoyaltyBanner();

        Assertion.assertThat("Bottom Sheet of Loyality Program Shown", accountPage.canClickTiketLoyaltyBanner(), is(true));

        step("click CTA latter");
        accountPage.clickLoginRegisterButtonLoyaltyPopup();

        Assertion.assertThat("Bottom Sheet Dissappears", accountPage.canClickTiketLoyaltyBanner(), is(false));
    }

    @Author(names = {"Ashish"})
    @Module(name = "AccountPage")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test
    public void testClickBlibliPayLaterMenuAndLoginWithPhoneNumber(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> params = getParam("testClickBlibliPayLaterMenuAndLoginWithPhoneNumber");
        BaseTest.paramsThreadLocal.set(params);

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);

        String phoneNumber = params.get("phoneNumber");

        step("Goto home");
        homePage.gotoHome();

        step("Login as guest");
        loginPage.loginAsGuest();

        step("change currency");
        homePage.changeCurrency("IDR","");

        step("open account page");
        homePage.openAccount();

        step("click Blibli paylater menu");
        accountPage.openPaylaterPage();

        step("Input credential on login page");
        loginPage.loginFromDifferentPagesUsingPhoneNumber(phoneNumber);

        step("Verify user login successfully and is on account page");
        Assertion.assertThat("verify user login successfully and is on account page", accountPage.verifyIsOnAccountPage(), is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "AccountPage")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test
    public void testClickSavePassengersDataMenuGuest(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> params = getParam("testClickSavePassengersDataMenuGuest");
        BaseTest.paramsThreadLocal.set(params);

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Login as guest");
        loginPage.loginAsGuest();

        step("open account page");
        homePage.openAccount();

        step("click save passenger data menu");
        accountPage.clickSavePassengerData();

        step("verify show bottom sheet introducing the feature is for member only");
        Assertion.assertThat("verify show bottom sheet introducing the feature is for member only", accountPage.verifyBottomSheetIntroducingFeatureForMemberOnly(), is(true));

        step("click CTA latter");
        accountPage.clickLaterCTABtn();

        step("verify bottom sheet disappear");
        Assertion.assertThat("verify bottom sheet disappear", accountPage.verifyBottomSheetDisappear(), is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "SmartProfile")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test
    public void addSmartProfileFromBookingForm(){
        Map<String, String> params = getParam("addSmartProfileFromBookingForm");
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(params);

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        SmartProfilePage<WebDriver> smartProfilePage = SmartProfilePage.getInstance(session);
        SettingPage<WebDriver> settingPage = SettingPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String loginType = params.get("loginType");
        String username = params.get("username");
        String password = params.get("password");

        String tripType = params.get("tripType");
        String originAirport = params.get("originAirport");
        String destinationAirport = params.get("destinationAirport");

        int daysToDepartureDate = Integer.parseInt(params.get("daysToDepartureDate"));
        int adultPassengers = 1;
        int childPassengers = 0;
        int infantPassengers = 0;
        String airline = params.get("airline");

        String currency = params.get("currency");

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDate(
                username,password,
                originAirport,destinationAirport,daysToDepartureDate,
                adultPassengers,childPassengers,infantPassengers,
                tripType,airline,"","","",currency
        );
        String departureDate =CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        airline = flightDynamicData.airlineDepart;

        step("Login as " + username + " via deeplink and change currency to " + currency);
        homePage.loginWithSpecificAccountViaDeeplink(username, password, currency);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(flightDynamicData, tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("Open filter");
        flightSearchResultPage.openFilter();

        step("Filter airline");
        flightSearchResultPage.filterAirline(airline);

        step("Save filter");
        flightSearchResultPage.saveFilter();

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill passenger detail");
        List<String> passengerName = flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers, "domestic",airline);

        step("Continue to second half booking form");
        flightBookingFormPage.continueToSecondHalf();

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();

        step("Go to Homepage");
        homePage.goToHomeDeeplink();

        step("Go to Account");
        homePage.openAccount();

        step("Click Save Passenger Data");
        settingPage.openSavePassengersData();

        step("Verify smart profile successfully added");
        String targetName = passengerName.get(0);
        Assertion.assertThat("smart profile exist from search", smartProfilePage.findProfile(targetName), is(true));

        step("Delete smart profile");
        try {
            smartProfilePage.openSmartProfile(targetName);
            smartProfilePage.deleteSmartProfile();
        } catch (WebDriverException e) {
            log("Test passed but delete smart profile failed");
        }
    }

    @Author(names = {"Prerna"})
    @Module(name = "loginUsingFaceBook")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test(dataProvider = "testLoginFromPaymentPageUsingFacebookDataProvider")
    public void testLoginFromPaymentPageUsingFacebook(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);

        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");
        String location = param.get("location");

        step("User open app");
        homePage.gotoHome();

        step("User user app as guest");
        loginPage.loginAsGuest();

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", "IDR");

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Go to the room list");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select room from the room list");
        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.clickBookRoom();

        step("Completing booking form data");
        bookingFormPage.fillContactOrderDetail();
        bookingFormPage.fillGuestDetail(false);

        step("Submit booking");
        bookingFormPage.continueToPaymentPage();
        bookingFormPage.PriceChangesPopUp();

        step("login in the payment page");
        paymentPage.openLoginPage();

        step("Login using facebook");
        loginPage.loginUsingFacebook();
        loginPage.loginOnFacebookPage(email, password);
        Assertion.assertThat("No login label shown",!paymentPage.isLoginContainerDisplayed(),is(true));
    }

    @DataProvider(name = "testLoginFromPaymentPageUsingFacebookDataProvider")
    public Object[][] testLoginFromPaymentPageUsingFacebookDataProvider() {
        return TestData.getObjectParams("testLoginFromPaymentPageUsingFacebook");
    }

    @Author(names = {"Prerna"})
    @Module(name = "loginUsingGoogle")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test(dataProvider = "testLoginFromPDPUsingGoogleDataProvider")
    public void testLoginFromPDPUsingGoogle(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);

        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");
        String location = param.get("location");

        step("User open app");
        homePage.gotoHome();

        step("User user app as guest");
        loginPage.loginAsGuest();

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", "IDR");

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Click login button on PDP");
        detailPage.clickLoginFromPDP();

        step("Login using google");
        loginPage.loginUsingGoogle();
        loginPage.loginOnGooglePage(email, password);
        Assertion.assertThat("No login label shown",!detailPage.isLoginButtonDisplayed(),is(true));
    }

    @DataProvider(name = "testLoginFromPDPUsingGoogleDataProvider")
    public Object[][] testLoginFromPDPUsingGoogleDataProvider() {
        return TestData.getObjectParams("testLoginFromPDPUsingGoogle");
    }

    @Author(names = {"Aditi"})
    @Module(name = "Rewards")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test
    public void testOpenRewardsPageAsLoginUser() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testOpenRewardsPageAsLoginUser");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        step("Given user is already logged in");
        homePage.gotoHome();
        loginPage.loginWith(loginType, username, password);
        Assertion.assertThat("User should be logged in", homePage.isUserLogin(), is(true));

        step("When user clicks on rewards page");
        homePage.clickOnRewardButton();

        step("Then user should be redirected to ULP page");
        Assertion.assertThat("User should be on the ULP (rewards) page", homePage.isOnRewardsPage(), is(true));

        step("And user can see their points");
        Assertion.assertThat("User points balance should be visible", homePage.isRewardsPointsBalanceVisible(), is(true));

        step("And user can scroll to the bottom of the page");
        homePage.scrollToBottomOfRewardsPage();
        Assertion.assertThat("User should be scrolled to the bottom of the rewards page", homePage.isRewardsPageScrolledToBottom(), is(true));
    }

    @Author(names = {"Aditi"})
    @Module(name = "Membership")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test
    public void testCountryCurrencyPreferenceUpdatedViaAccountDeeplink() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testCountryCurrencyPreferenceUpdatedViaAccountDeeplink");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);

        String preferenceCountry = param.get("preferenceCountry");
        String preferenceCurrency = param.get("preferenceCurrency");
        String deeplinkCountryCode = param.get("deeplinkCountryCode");
        String deeplinkCurrency = param.get("deeplinkCurrency");
        String expectedCountry = param.get("expectedCountry");
        String expectedCurrency = param.get("expectedCurrency");

        step("Given user already saved country and currency on their preference");
        homePage.gotoHome();
        loginPage.loginAsGuest();
        accountPage.chooseCountry(preferenceCountry);
        homePage.changeCurrency(preferenceCurrency, "");

        step("When user open url/deeplink to my account page with different country and currency");
        accountPage.openAccountPageUsingDeeplink(deeplinkCountryCode, deeplinkCurrency);

        step("Then user should see the country & currency pop up");
        Assertion.assertThat("Country and currency popup should be displayed",
                homePage.isCurrencyCountryPopupDisplayed(), is(true));

        step("When user click on primary button");
        homePage.clickCurrencyCountryPopupPrimaryButton();

        step("Then user will be redirected to my account page with the new country and currency from the link");
        Assertion.assertThat("User should be on My Account page", accountPage.isAccountPageDisplayed(), is(true));

        step("And user can see their currency menu show correct currency");
        Assertion.assertThat("Currency menu should show the currency from the deeplink",
                accountPage.getSelectedCurrency(), equalToIgnoringCase(expectedCurrency));

        step("And user can see their country menu show correct country");
        Assertion.assertThat("Country menu should show the country from the deeplink",
                accountPage.getSelectedCountry(), equalToIgnoringCase(expectedCountry));
    }

    @Author(names = {"Ashish"})
    @Module(name = "WelcomeRewards")
    @Tribe(name = "Member")
    @Vertical(name = "Eiffel")
    @Test
    public void verifyWelcomeRewardsAfterOnboarding() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("verifyWelcomeRewardsAfterOnboarding");
        BaseTest.paramsThreadLocal.set(map);

        String currency = map.get("currency");

        OnboardingPage<WebDriver> onboardingPage = OnboardingPage.getInstance(session);

        // This test requires a genuine fresh install so the onboarding page appears. If the pooled
        // session was already used by an earlier test (onboarding not present), renew it once to get
        // a fresh install. Guarded on isOnboardingPageDisplayed() so an already-fresh session is NOT
        // renewed (avoids discarding a good session / looping).
        step("Ensure fresh install (renew session only if onboarding is not already showing)");
        // Use a short probe (not the default 20s / 40s-on-PREPROD wait) for the renewal decision so an
        // already-onboarded pooled driver (the common case) does not block for the full onboarding timeout.
        if (!onboardingPage.isOnboardingPageDisplayed(3)) {
            log("Onboarding not detected - current session is not a fresh install; renewing session once");
            session = renewSessionForFreshInstall();
            onboardingPage = OnboardingPage.getInstance(session);
        }

        HomePage<WebDriver> homePage = HomePage.getInstance(session);

        step("Verify onboarding page is displayed on fresh install");
        Assertion.assertThat("Onboarding page should be displayed on fresh install", onboardingPage.isOnboardingPageDisplayed(), is(true));

        step("Change currency to " + currency + " on onboarding page");
        onboardingPage.changeCurrency(currency);
        Assertion.assertThat("Currency on onboarding should be " + currency, onboardingPage.getSelectedCurrency(), equalToIgnoringCase(currency));

        step("Finish onboarding by skipping it");
        onboardingPage.finishOnboardingBySkipping();

        // Wrap the verification in try/finally so any open Welcome Rewards / coach-mark overlay is always
        // dismissed before this pooled driver is returned to the pool, even if an assertion fails midway
        // (an open overlay could otherwise block a subsequent test that reuses this driver).
        try {
            step("Verify Welcome Rewards section appears naturally after skipping onboarding (no deeplink)");
            Assertion.assertThat("Welcome Rewards should appear naturally after onboarding (without deeplink) when currency is " + currency, homePage.isWelcomeRewardsShownNaturally(), is(true));

            step("Close the Welcome Rewards bottom sheet");
            homePage.closeWelcomeRewards();

            step("Verify Welcome Rewards can be re-opened via the member/welcome-rewards deeplink");
            homePage.openWelcomeRewardsViaDeeplink();
            Assertion.assertThat("Welcome Rewards should be shown again via deeplink when currency is " + currency, homePage.isWelcomeRewardsShown(20), is(true));
        } finally {
            // Best-effort cleanup: dismiss the Welcome Rewards bottom sheet so the pooled session is
            // returned clean regardless of pass/fail.
            homePage.closeWelcomeRewards();
        }
    }

    @Author(names = {"Areeb"})
    @Module(name = "AccountPage")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test(dataProvider = "verifyClickAffiliateMenuGuestDataProvider")
    public void verifyClickAffiliateMenuGuest(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);

        step("Open app");
        homePage.gotoHome();

        step("Login as guest");
        loginPage.loginAsGuest();

        step("Open account page");
        homePage.openAccount();

        step("Click affiliate menu");
        accountPage.openAffiliate();

        step("Verify affiliate page is shown");
        Assertion.assertThat("Affiliate page should be shown", accountPage.verifyAffiliate(), is(true));
    }

    @DataProvider(name = "verifyClickAffiliateMenuGuestDataProvider")
    public Object[][] verifyClickAffiliateMenuGuestDataProvider() {
        return TestData.getObjectParams("verifyClickAffiliateMenuGuest");
    }

    @Author(names = {"Areeb"})
    @Module(name = "AccountPage")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test(dataProvider = "verifyClickAffiliateMenuLoginDataProvider")
    public void verifyClickAffiliateMenuLogin(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");

        step("Open app");
        homePage.gotoHome();

        step("Login as user");
        homePage.loginViaDeeplink(username, password);

        step("Open account page");
        homePage.openAccount();

        step("Click affiliate menu");
        accountPage.openAffiliate();

        step("Verify affiliate page is shown");
        Assertion.assertThat("Affiliate page should be shown", accountPage.verifyAffiliate(), is(true));
    }

    @DataProvider(name = "verifyClickAffiliateMenuLoginDataProvider")
    public Object[][] verifyClickAffiliateMenuLoginDataProvider() {
        return TestData.getObjectParams("verifyClickAffiliateMenuLogin");
    }

    @Author(names = {"Areeb"})
    @Module(name = "SmartProfile")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test
    public void verifyChangePassportInfoForDuplicateProfile() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyChangePassportInfoForDuplicateProfile");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        var smartProfilePage = SmartProfilePage.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        int profileIndex = 2;

        step("Open app");
        homePage.gotoHome();

        step("Login as username: " + username + " via deeplink");
        homePage.loginViaDeeplink(username, password);

        step("Open account page");
        homePage.openAccount();

        step("Click passenger menu");
        accountPage.clickSavePassengerData();

        step("Verify smart profile page is shown");
        String ownerName = smartProfilePage.getOwnerName();
        String profileName = smartProfilePage.getProfileName(profileIndex);
        Assertion.assertThat("Smart profile page should be shown", ownerName, is(not(emptyOrNullString())));
        Assertion.assertThat("Duplicate profile exists", ownerName, is(equalToIgnoringCase(profileName)));

        step("Get passport information of duplicate profile: " + profileName);
        SmartProfilePage.PassportInfo passportInfo = smartProfilePage.getProfilePassportInfo(profileIndex);
        Assertion.assertThat("Passport number should not be empty", passportInfo.passportNumber(), is(not(emptyOrNullString())));
        Assertion.assertThat("Issue date should not be empty", passportInfo.issueDate(), is(not(emptyOrNullString())));
        Assertion.assertThat("Expiry date should not be empty", passportInfo.expiryDate(), is(not(emptyOrNullString())));
        Assertion.assertThat("Country should not be empty", passportInfo.country(), is(not(emptyOrNullString())));

        step("Change passport information");
        SmartProfilePage.PassportInfo changedPassportInfo = smartProfilePage.changeProfilePassportInfo(profileIndex);
        Assertion.assertThat("Changed passport number should not be empty", changedPassportInfo.passportNumber(), is(not(emptyOrNullString())));
        Assertion.assertThat("Changed issue date should not be empty", changedPassportInfo.issueDate(), is(not(emptyOrNullString())));
        Assertion.assertThat("Changed expiry date should not be empty", changedPassportInfo.expiryDate(), is(not(emptyOrNullString())));
        Assertion.assertThat("Changed country should not be empty", changedPassportInfo.country(), is(not(emptyOrNullString())));

        step("Verify passport information is updated");
        SmartProfilePage.PassportInfo updatedPassportInfo = smartProfilePage.getProfilePassportInfo(profileIndex);
        Assertion.assertThat("Passport info should be updated", updatedPassportInfo, is(equalTo(changedPassportInfo)));
    }
}