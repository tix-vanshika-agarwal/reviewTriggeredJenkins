package com.tiket.web.dWeb.test.platform;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.apiPage.HotelApi;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dWeb.page.account.AccountPage;
import com.tiket.web.page.flight.bookingForm.FlightBookingFormPageWeb;
import com.tiket.web.dWeb.page.flight.FlightProductReviewPage;
import com.tiket.web.page.flight.searchresultpage.FlightSearchResultPageWeb;
import com.tiket.web.page.flight.landingpage.FlightLandingPageWeb;
import com.tiket.web.dWeb.page.home.SettingsPage;
import com.tiket.web.dWeb.page.payment.PaymentPage;
import com.tiket.web.page.common.home.HomePageWeb;
import com.tiket.web.page.common.home.LoginPageWeb;
import com.tiket.web.page.platform.eiffel.membership.AccountPageWeb;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import javax.security.auth.login.FailedLoginException;
import java.util.Map;

import static com.tiket.core.CoreFunctions.*;
import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.*;

public class PlatformTest extends BaseTest {

    @Author(names = "Prerna")
    @Module(name = "SmartProfile")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test
    public void testUserAddSmartProfile() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testUserAddSmartProfile");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        AccountPage<?> accountPage = new AccountPage<>(session);
        AccountPageWeb<?> accountPageWeb = AccountPageWeb.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        String firstName = param.get("firstName");
        String lastName = param.get("lastName");
        String ninCode = param.get("NINCode");
        String phoneNumber = param.get("phoneNumber");

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, 1);

        step("Open Save Passenger Data");
        homePage.goToSavePassengerData();

        step("Add Passenger data and fill required fields");
        accountPageWeb.addSmartProfile(firstName, lastName, ninCode, email, phoneNumber);

        step("Verify if the profile successfully created");
        Assertion.assertThat("Profile with name " + firstName + " created", accountPage.verifySmartProfileByName(firstName, lastName), is(true));

        step("Delete the created profile");
        accountPage.deleteSmartProfile(firstName, lastName);

        step("Verify if the profile is deleted ?");
        Assertion.assertThat("Profile with name " + firstName + " deleted", accountPage.verifySmartProfileByName(firstName, lastName), is(false));
    }

    @Author(names = "Prerna")
    @Module(name = "Insurance")
    @Tribe(name = "Insurance")
    @Vertical(name = "Insurance")
    @Test
    public void testAccessingInsuranceClaimLandingPage() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testAccessingInsuranceClaimLandingPage");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        step("Go to home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Open insurance page");
        homePage.goToClaimCenterPage();

        step("Check if user is on insurance page");
        Assertion.assertThat("User is on insurance page", homePage.isOnInsurancePage(), is(true));
    }

    @Author(names = "Prerna")
    @Module(name = "Member")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test
    public void testHomeIconsLogin() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testHomeIconsLogin");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Change currency");
        homePage.selectCurrency("IDR");

        step("Open vertical icons ans verify landing pages");
        Assertion.assertThat("Vertical pages are opened correctly", homePage.verifyVerticalIcons(), is(true));
    }

    @Author(names = "Kartik")
    @Module(name = "Message")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test
    public void VerifyContextualActionBtnHelpCenter() throws FailedLoginException, InterruptedException {
        Map<String, String> map = getParam("VerifyContextualActionBtnHelpCenter");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        SettingsPage<?> settingsPage = new SettingsPage<>(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);

        String email = map.get("email");
        String password = map.get("password");
        String otp = map.get("otp");
        String username = map.get("username");
        String startDate = generateRandomDate(map.get("rangeAddDate"));
        String query = map.get("hotelName");
        int adult = Integer.parseInt(map.get("adult"));
        int night = Integer.parseInt(map.get("night"));
        int room = Integer.parseInt(map.get("room"));

        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = salutationList[0];

        step("Create new hotel issued order");
        hotelApi.createIssuedOrderApi(username, password, startDate, query, adult, night, room, orderFullName, guestTitle, guestFullName, orderNameTitle, "IDR", true, false, "", "en", true);

        step("Get data from the issued order");
        String issuedOrderID = String.valueOf(hotelApi.getOrderId());
        Assertion.assertThat("Verify Booking via API is successful", !issuedOrderID.isEmpty(), is(true));

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, 1);

        step("Change currency");
        homePage.selectCurrency("IDR");

        step("Open settings page");
        homePage.openSettings();

        step("Open help center on settings page");
        settingsPage.openHelpCenter();

        step("find issued order in help center");
        Assertion.assertThat("Verify order is present on help center page", settingsPage.checkOrderIDHelpCenter(issuedOrderID.trim()), is(true));

        step("click on refund/reschedule button");
        Boolean refundPage = settingsPage.ClickAndProceedRefund();

        step("verify on refund/reschedule page");
        Assertion.assertThat("Vertical pages are opened correctly", refundPage, is(true));
    }

    @Author(names = "Kartik")
    @Module(name = "Message")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test
    public void VerifyPopularArticleSectionHelpCenter() throws FailedLoginException, InterruptedException {
        Map<String, String> map = getParam("VerifyPopularArticleSectionHelpCenter");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        SettingsPage<?> settingsPage = new SettingsPage<>(session);

        String email = map.get("email");
        String password = map.get("password");
        String otp = map.get("otp");

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, 1);

        step("Change currency");
        homePage.selectCurrency("IDR");

        step("Open settings page");
        homePage.openSettings();

        step("Open help center on settings page");
        settingsPage.openHelpCenter();

        step("find Popular article section");
        Assertion.assertThat("is Popular article section visible", settingsPage.checkPopularArticle(), is(true));

        step("click on any popular article randomly");
        String selectedArticle = settingsPage.selectPopularArticle();

        step("verify if the article is opened");
        Assertion.assertThat("is Popular article section visible", settingsPage.checkSelectedPopularArticle(selectedArticle), is(true));
    }

    @Author(names = "Aditi")
    @Module(name = "Message")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test
    public void testVerifyDynamicRequestForm() throws FailedLoginException, InterruptedException {
        Map<String, String> map = getParam("testVerifyDynamicRequestForm");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        SettingsPage<?> settingsPage = new SettingsPage<>(session);

        String email = map.get("email");
        String password = map.get("password");
        String otp = map.get("otp");

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, 1);

        step("Change currency");
        homePage.selectCurrency("IDR");

        step("Open settings page");
        homePage.openSettings();

        step("Open help center on settings page");
        settingsPage.openHelpCenter();

        step("find Popular article section");
        Assertion.assertThat("is Popular article section visible", settingsPage.checkPopularArticle(), is(true));

        step("click on any popular article randomly");
        String selectedArticle = settingsPage.selectPopularArticle();

        step("verify if the article is opened");
        Assertion.assertThat("is Popular article section visible", settingsPage.checkSelectedPopularArticle(selectedArticle), is(true));

        step("Go to send message tab");
        settingsPage.clickSendMessage();

        step("Check that an active order is present or not");
        settingsPage.clickActiveOrder();

        step("Submit complaint");
        Assertion.assertThat("Complaint successfully submitted", settingsPage.submitRequest(), is(true));

    }

    @Author(names = "Aditi")
    @Module(name = "ClaimCenter")
    @Tribe(name = "Insurance")
    @Vertical(name = "Insurance")
    @Test
    public void testAccessInsuranceClaimCenterPageLogin() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testAccessInsuranceClaimCenterPageLogin");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("User open home page");
        homePage.goToHomePage();

        step("Go to account page");
        loginPage.goToAccountPage();
        homePage.waitForHomePageToOpen();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);
        homePage.goToHomePage();

        step("User open Claim center page");
        homePage.goToClaimCenterPage();

        step("Check if claim center page opened");
        Assertion.assertThat("Claim Center page is opened", homePage.isClaimCenterPageOpened(), is(true));
    }

    @Author(names = "Aditi")
    @Module(name = "TnC")
    @Tribe(name = "Homepage")
    @Vertical(name = "Platform")
    @Test
    public void testOpenTnCPageLogin() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testOpenTnCPageLogin");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, 1);

        step("User open Terms and Conditions page");
        boolean res = homePage.openTnC();

        step("Check if Terms and Conditions page opened");
        Assertion.assertThat("Terms and Conditions page is opened", res, is(true));
    }

    @Author(names = "Aditi")
    @Module(name = "PrivacyPolicy")
    @Tribe(name = "Homepage")
    @Vertical(name = "Platform")
    @Test
    public void testOpenPrivacyPolicyPageLogin() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testOpenPrivacyPolicyPageLogin");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, 1);

        step("User open Privacy Policy page");
        boolean res = homePage.openPrivacyPolicy();

        step("Check if Privacy Policy page opened");
        Assertion.assertThat("Privacy Policy page is opened", res, is(true));
    }

    @Author(names = "Aditi")
    @Module(name = "VoucherBox")
    @Tribe(name = "Homepage")
    @Vertical(name = "Platform")
    @Test
    public void testOpenVoucherBoxLogin() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testOpenVoucherBoxLogin");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, 1);

        step("User open Voucher Box page");
        boolean res = homePage.goToVoucherBoxPage();

        step("Check if Voucher Box opened");
        Assertion.assertThat("Voucher Box page is opened", res, is(true));
    }

    @Author(names = "Aditi")
    @Module(name = "Review")
    @Tribe(name = "Homepage")
    @Vertical(name = "Platform")
    @Test
    public void testOpenReviewSectionLogin() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testOpenReviewSectionLogin");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, 1);

        step("User open My Review page");
        boolean res = homePage.goToReviewPage();

        step("Check if Review section opened");
        Assertion.assertThat("Review page is opened", res, is(true));

    }

    @Author(names = "Aditi")
    @Module(name = "Language")
    @Tribe(name = "Homepage")
    @Vertical(name = "Platform")
    @Test
    public void testChangeLanguageToENG() {
        Map<String, String> param = getParam("testChangeLanguageToENG");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        String currentLang = "id";
        String langToBeChanged = "en";

        step("User open home page");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginAsGuest();

        step("Get current language");
        String lang = homePage.getLanguage();

        if (lang != "id") {
            homePage.selectBahasaLanguage();
        }
        Assertion.assertThat("Current language is ID", homePage.getLanguage(), is(currentLang));

        step("Change language to ENG");
        homePage.selectEnglishLanguage();
        Assertion.assertThat("Changed language is EN", homePage.getLanguage(), is(langToBeChanged));
    }

    @Author(names = "Aditi")
    @Module(name = "Language")
    @Tribe(name = "Homepage")
    @Vertical(name = "Platform")
    @Test
    public void testChangeLanguageToENGLogin() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testChangeLanguageToENGLogin");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");

        String currentLang = "id";
        String langToBeChanged = "en";

        step("User open home page");
        homePage.goToHomePage();

        step("Open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, "123456", 1);

        step("Get current language");
        String lang = homePage.getLanguage();

        if (lang != "id") {
            homePage.selectBahasaLanguage();
        }
        Assertion.assertThat("Current language is ID", homePage.getLanguage(), is(currentLang));

        step("Change language to ENG");
        homePage.selectEnglishLanguage();
        Assertion.assertThat("Changed language is EN", homePage.getLanguage(), is(langToBeChanged));
    }

    @Author(names = "Aditi")
    @Module(name = "Language")
    @Tribe(name = "Homepage")
    @Vertical(name = "Platform")
    @Test
    public void testChangeLanguageToID() {
        Map<String, String> param = getParam("testChangeLanguageToID");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        String currentLang = "en";
        String langToBeChanged = "id";

        step("User open home page");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginAsGuest();

        step("Get current language");
        String lang = homePage.getLanguage();

        if (lang != "en") {
            homePage.selectEnglishLanguage();
        }
        Assertion.assertThat("Current language is EN", homePage.getLanguage(), is(currentLang));

        step("Change language to ID");
        homePage.selectBahasaLanguage();
        Assertion.assertThat("Changed language is ID", homePage.getLanguage(), is(langToBeChanged));
    }

    @Author(names = "Aditi")
    @Module(name = "Language")
    @Tribe(name = "Homepage")
    @Vertical(name = "Platform")
    @Test
    public void testChangeLanguageToIDLogin() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testChangeLanguageToIDLogin");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");

        String currentLang = "en";
        String langToBeChanged = "id";

        step("User open home page");
        homePage.goToHomePage();

        step("Open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, "123456", 1);

        step("Get current language");
        String lang = homePage.getLanguage();

        if (lang != "en") {
            homePage.selectEnglishLanguage();
        }
        Assertion.assertThat("Current language is EN", homePage.getLanguage(), is(currentLang));

        step("Change language to ID");
        homePage.selectBahasaLanguage();
        Assertion.assertThat("Changed language is ID", homePage.getLanguage(), is(langToBeChanged));
    }

    @Author(names = "Aditi")
    @Module(name = "Currency")
    @Tribe(name = "Homepage")
    @Vertical(name = "Platform")
    @Test
    public void testChangeCurrency() {
        Map<String, String> param = getParam("testChangeCurrency");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginAsGuest();

        step("Check the current currency");
        String currentCurrency = homePage.getCurrentCurrency();
        String randomCurrency = homePage.getRandomCurrency(currentCurrency);

        step("Change current currency from " + currentCurrency + " to " + randomCurrency);
        homePage.selectCurrency(randomCurrency);
        String changedCurrency = homePage.getCurrentCurrency();
        Assertion.assertThat("Currency successfully changed from " + currentCurrency + " to " + randomCurrency, changedCurrency, is(randomCurrency));

    }

    @Author(names = "Aditi")
    @Module(name = "SmartProfile")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test
    public void testAddSmartProfileBookingPage() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testAddSmartProfileBookingPage");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        AccountPage<?> accountPage = new AccountPage<>(session);
        FlightLandingPageWeb<?> flightLandingPageWeb = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPage<?> flightProductReviewPage = new FlightProductReviewPage<>(session);
        FlightBookingFormPageWeb<?> flightBookingFormPage = FlightBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String airline = param.get("airline");
        String tripType = param.get("tripType");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);

        int adultPassengers = Integer.parseInt(param.get("adultPassengers"));
        int childPassengers = Integer.parseInt(param.get("childPassengers"));
        int infantPassengers = Integer.parseInt(param.get("infantPassengers"));

        String firstName = CoreFunctions.randomName();
        String lastName = CoreFunctions.randomLastName();

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, "123456", 1);

        step("User open Flight vertical");
        homePage.openVerticalPageViaURL("Flights");

        step("Toggle trip type");
        flightLandingPageWeb.toggleTripType(tripType);

        step("Select origin airport");
        flightLandingPageWeb.selectOrigin(originAirport);

        step("Select destination airport");
        flightLandingPageWeb.selectDestination(destinationAirport);

        step("Select date");
        flightLandingPageWeb.selectDate(departureDate);

        step("add passenger");
        flightLandingPageWeb.addPassenger(adultPassengers, childPassengers, infantPassengers);

        step("search flight");
        flightLandingPageWeb.searchFlight();

        step("filter airline {" + airline + " }");
        flightSearchResultPage.filterAirline(airline);

        step("select flight");
        flightSearchResultPage.selectFlight();

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("Click toggle to fill passengers details and save ");
        flightBookingFormPage.fillPassengerDetailsLogin(firstName, lastName);
        flightBookingFormPage.fillNationality();

        step("Go to payment page");
        flightBookingFormPage.goToPaymentPageFromBookingFormPage();

        step("Skip Flight may rescheduled page by clicking no thanks");
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("Payment page should be displayed");
        paymentPage.getOrderId();

        step("User open account page");
        homePage.goToAccountPage();

        step("Open Save Passenger Data");
        homePage.goToSavePassengerData();

        step("Verify if the profile successfully created");
        Assertion.assertThat("Profile with name " + firstName + " created", accountPage.verifySmartProfileByName(firstName, lastName), is(true));

    }

    @Author(names = "Aditi")
    @Module(name = "SmartProfile")
    @Tribe(name = "Member")
    @Vertical(name = "Platform")
    @Test
    public void testUserEditSmartProfile() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testUserEditSmartProfile");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        AccountPageWeb<?> accountPageWeb = AccountPageWeb.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        String firstName = CoreFunctions.randomName();
        String lastName = CoreFunctions.randomLastName();
        String ninCode = param.get("NINCode");
        String phoneNumber = param.get("phoneNumber");
        String emergencyName = CoreFunctions.randomName();

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Open Save Passenger Data");
        homePage.goToSavePassengerData();

        step("Add Passenger data and fill required fields");
        accountPageWeb.addSmartProfile(firstName, lastName, ninCode, email, phoneNumber);

        step("Verify if the profile successfully created");
        Assertion.assertThat("Profile with name " + firstName + " " + lastName + " created", accountPageWeb.verifySmartProfileByName(firstName, lastName), is(true));

        step("Edit the smart profile just added");
        accountPageWeb.editSmartProfile(firstName, lastName, emergencyName);

        step("Verify if the profile details successfully changed");
        Assertion.assertThat("Profile with name " + firstName + " " + lastName + " edited", accountPageWeb.verifySmartProfileByName(emergencyName, lastName), is(true));
        Assertion.assertThat("Profile with name " + emergencyName + " " + lastName + " present", accountPageWeb.verifySmartProfileByName(emergencyName, lastName), is(true));

    }

    @Author(names = { "Mridul" })
    @Module(name = "Homepage")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "Platform")
    @Test
    public void loginChangeCountry() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("loginChangeCountry");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        SettingsPage<?> settingsPage = new SettingsPage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        String country = param.get("country");

        step("User open home page");
        homePage.goToHomePage();

        step("login in using { " + username + " }");
        loginPage.loginIntoWeb(username, password, otp, 1);

        step("go to tiket home page");
        homePage.goToHomePage();

        step("Go to account page");
        loginPage.goToAccountPage();

        step("Click on \"To Account Centre\"");
        settingsPage.openAccountCenter();

        step("Click Country Or Region ");
        settingsPage.openCountryRegion();

        step("Change Country to " + country);
        settingsPage.changeCountryRegion(country);

        step("Check the Country had been changed successfully");
        Assertion.assertThat("Check the Country had been changed successfully", settingsPage.checkCountryName(country), is(country.toLowerCase()));
    }

    @Author(names = { "Fandy" })
    @Module(name = "Homepage")
    @Tribe(name = "Discovery")
    @Vertical(name = "Grand")
    @Test
    public void testGlobalSearchWithoutKeywordWeb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testGlobalSearchWithoutKeywordWeb");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("Go to tiket homepage");
        homePage.goToHomePage();

        step("User click global search");
        homePage.clickSearchColumn();

        step("Verify global search content without input any keyword");
        Assertion.assertThat("Verify Global Search is successfully asserted ", homePage.verifyContentGS(), is(true));
        Assertion.assertThat("Verify content Global Search Modal is successfully asserted", homePage.verifyContentGSModule(), is(true));
    }
}
