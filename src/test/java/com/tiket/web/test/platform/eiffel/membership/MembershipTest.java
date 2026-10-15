package com.tiket.web.test.platform.eiffel.membership;

import com.tiket.annotation.Author;
import com.tiket.core.CoreFunctions;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.testbase.Configs;
import com.tiket.apiPage.SmartProfileApi;
import com.tiket.web.page.common.home.HomePageWeb;
import com.tiket.web.page.common.home.LoginPageWeb;
import com.tiket.web.page.common.home.Setting_Page.SettingsPageWeb;
import com.tiket.web.page.platform.eiffel.membership.AccountPageWeb;
import com.tiket.web.page.platform.eiffel.membership.AccountPageWeb.ArticleVisit;
import com.tiket.web.page.platform.eiffel.membership.AccountPageWeb.PassportRenewalCopy;
import com.tiket.pojo.member.MainProfileData;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import javax.security.auth.login.FailedLoginException;
import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.*;

public class MembershipTest extends BaseTest {

    @Author(names = "Saurabh")
    @Module(name = "VoucherBox")
    @Tribe(name = "Membership")
    @Vertical(name = "Platform")
    @Test
    public void openVoucherBoxLoginTest() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("openVoucherBoxLoginTest");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("User open home page");
        homePage.goToHomePage();

        step("User logs in with email and OTP");
        loginPage.loginIfNotWithSameEmail(email, password, otp, 1);

        step("User open Voucher Box page");
        boolean res = homePage.goToVoucherBoxPage();

        step("Check if Voucher Box opened");
        Assertion.assertThat("Voucher Box page is opened", res, is(true));
    }

    @Author(names = {"Mridul"})
    @Module(name = "Homepage")
    @Tribe(name = "Membership")
    @Vertical(name = "Platform")
    @Test
    public void loginChangeCountryWeb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("loginChangeCountryWeb");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        SettingsPageWeb<?> settingsPage = SettingsPageWeb.getInstance(session);
        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        String country = param.get("country");

        step("User open home page");
        homePage.goToHomePage();

        step("login in using { " + username + " }");
        loginPage.loginIfNotWithSameEmail(username, password, otp, 1);

        step("Go to account page");
        loginPage.goToAccountPage();

        step("Click on \"To Account Centre\"");
        settingsPage.openAccountCenter();

        step("Click Country Or Region ");
        settingsPage.openCountryRegion();

        step("Change Country to "+country);
        settingsPage.changeCountryRegion(country);

        step("Check the Country had been changed successfully");
        Assertion.assertThat("Check the Country had been changed successfully",settingsPage.checkCountryName(country),is(country.toLowerCase()));

    }

    @Author(names = {"Aditi","Ishika"})
    @Module(name = "SmartProfile")
    @Tribe(name = "Membership")
    @Vertical(name = "Platform")
    @Test
    public void testAddAndDeleteSmartProfileWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testAddAndDeleteSmartProfileWeb");
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        SettingsPageWeb<?> settingsPage = SettingsPageWeb.getInstance(session) ;
        AccountPageWeb<?> accountPage = AccountPageWeb.getInstance(session) ;

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        String firstName = param.get("firstName");
        String lastName = param.get("lastName");
        String ninCode= param.get("ninCode");
        String phoneNumber = param.get("phoneNumber");
        String country = param.get("country") ;

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Open Save Passenger Data");
        loginPage.goToAccountPage();
        accountPage.goToSavePassengerData();

        step("Click Add Passenger Data") ;
        accountPage.clickAddPassengerData();

        step("Add Passenger data and fill required fields");
        accountPage.addSmartProfile(firstName, lastName, ninCode, email, phoneNumber);

        step("Verify if the profile successfully created");
        Assertion.assertThat("Profile with name " + firstName +" created", accountPage.verifySmartProfileByName(firstName, lastName), is(true));

        step("Delete the created profile");
        accountPage.deleteSmartProfile(firstName, lastName);

        step("Verify if the profile is deleted ?");
        Assertion.assertThat("Profile with name " + firstName +" deleted", accountPage.verifySmartProfileByName(firstName, lastName), is(false));
    }

    @Author(names = {"Mridul"})
    @Module(name = "SmartProfile")
    @Tribe(name = "Membership")
    @Vertical(name = "Platform")
    @Test
    public void editMainProfileWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("editMainProfileWeb");
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        AccountPageWeb<?> accountPage = AccountPageWeb.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");

        step("Given user is already logged in");
        homePage.goToHomePage();
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("And user go to Save Passenger Data menu");
        loginPage.goToAccountPage();
        accountPage.goToSavePassengerData();

        step("When user click on the \"Account Owner\" profile");
        accountPage.openAccountOwnerProfile();

        MainProfileData entered = new MainProfileData();

        step("And input the main profile changes (nationality, identity number, emergency contact)");
        accountPage.editMainProfileSection(entered);

        step("When user click on \"Passport\" menu, input passport details and click Continue");
        accountPage.editPassportSection(entered);

        step("When user click on \"Driver's license\", input the license number and click Continue");
        accountPage.editDriverLicenseSection(entered);

        step("When user delete existing frequent flyer entries");
        accountPage.clearFrequentFlyerSection();

        step("When user click \"Add New\" in Frequent Flyer menu, input airline and number, then click Save");
        accountPage.addFrequentFlyerSection(entered);

        step("When user click on Save");
        accountPage.saveAccountOwnerProfile();

        step("Then user should see the success snackbar \"The profile successfully updated.\"");
        Assertion.assertThat("Profile updated success snackbar should be shown",
                accountPage.getSuccessSnackbarText(), either(containsStringIgnoringCase("successfully updated")).or(containsStringIgnoringCase("berhasil diperbarui")));

        step("And user can re-open the profile and read back the saved data");
        MainProfileData saved = accountPage.getAccountOwnerProfileData();

        step("And the changed data should be validated as persisted");
        Assertion.assertThat("Nationality persisted", entered.nationality, containsStringIgnoringCase(saved.nationality));
        Assertion.assertThat("National identity number persisted", saved.identityNumber, is(entered.identityNumber));
        Assertion.assertThat("Emergency contact name persisted", saved.emergencyContactName, containsStringIgnoringCase(entered.emergencyContactName));
        Assertion.assertThat("Emergency phone number persisted", saved.emergencyPhoneNumber, containsString(entered.emergencyPhoneNumber));
        Assertion.assertThat("Passport number persisted", saved.passportNumber, equalToIgnoringCase(entered.passportNumber));
        Assertion.assertThat("Passport issue date persisted", saved.passportIssueDate, containsString(entered.passportIssueDate));
        Assertion.assertThat("Passport expiry date persisted", saved.passportExpiryDate, containsString(entered.passportExpiryDate));
        Assertion.assertThat("Issuing country persisted", saved.issuingCountry, containsStringIgnoringCase(entered.issuingCountry));
        Assertion.assertThat("Driver's license number persisted", saved.driverLicenseNumber, is(entered.driverLicenseNumber));
        Assertion.assertThat("Airline persisted", saved.airline, containsStringIgnoringCase(entered.airline));
        Assertion.assertThat("Frequent flyer number persisted", saved.frequentFlyerNumber, equalToIgnoringCase(entered.frequentFlyerNumber));
    }

    @Author(names = {"Aditi", "Yanuar"})
    @Module(name = "Currency")
    @Tribe(name = "Membership")
    @Vertical(name = "Platform")
    @Test
    public void testChangeCurrencyLogin() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testChangeCurrencyLogin");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        AccountPageWeb<?> accountPage = AccountPageWeb.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, "123456", 1);

        step("Check the current currency");
        String currentCurrency = accountPage.getCurrentCurrency();
        String randomCurrency = accountPage.getRandomCurrency(currentCurrency);

        step("Change current currency from "+ currentCurrency + " to " + randomCurrency);
        accountPage.changeCurrency(randomCurrency);
        String changedCurrency = accountPage.getCurrentCurrency();
        Assertion.assertThat("Currency successfully changed from " + currentCurrency + " to " + randomCurrency, randomCurrency, is(changedCurrency));

    }

    @Author(names = "Saurabh")
    @Module(name = "PrivacyPolicy")
    @Tribe(name = "Membership")
    @Vertical(name = "Platform")
    @Test
    public void testOpenPrivacyPolicyPage() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testOpenPrivacyPolicyPage");
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

    @Author(names = "Saurabh")
    @Module(name = "Review")
    @Tribe(name = "Membership")
    @Vertical(name = "Platform")
    @Test
    public void testOpenReviewSection() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testOpenReviewSection");
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



        @Author(names = "Saurabh")
        @Module(name = "Language")
        @Tribe(name = "Membership")
        @Vertical(name = "Platform")
        @Test
        public void testChangeLanguageToENGNonLogin() {
            Map<String, String> param = getParam("testChangeLanguageToENGNonLogin");
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

            if (!lang.equalsIgnoreCase("id")){
                homePage.selectBahasaLanguage();
                lang = homePage.getLanguage();
            }
            Assertion.assertThat("Current language is ID", lang, is(currentLang));

            step("Change language to ENG");
            homePage.selectEnglishLanguage();

            if(Configs.PLATFORM== Platform.DWEB){
                Assertion.assertThat("Changed language is EN", homePage.getLanguage(), is(langToBeChanged));}
            else {
                Assertion.assertThat("Changed language is EN", homePage.getLanguage(), is(langToBeChanged));
            }
        }

    @Author(names = "Saurabh")
    @Module(name = "Language")
    @Tribe(name = "Membership")
    @Vertical(name = "Platform")
    @Test
    public void testChangeLanguageToEnglishLogin() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testChangeLanguageToEnglishLogin");
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

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Get current language");
        String lang = homePage.getLanguage();

        if (!lang.equalsIgnoreCase("id")) {
            homePage.selectBahasaLanguage();
            lang = homePage.getLanguage();
        }
        Assertion.assertThat("Current language is ID", lang, is(currentLang));

        step("Change language to ENG");
        homePage.selectEnglishLanguage();
        if(Configs.PLATFORM== Platform.DWEB){
            Assertion.assertThat("Changed language is EN", homePage.getLanguage(), is(langToBeChanged));}
        else {
            Assertion.assertThat("Changed language is EN", homePage.getLanguage(), is(langToBeChanged));
        }
    }
    @Author(names = "Saurabh")
    @Module(name = "Language")
    @Tribe(name = "Membership")
    @Vertical(name = "Platform")
    @Test
    public void testChangeLanguageToIDNonLogin(){
        Map<String, String> param = getParam("testChangeLanguageToIDNonLogin");
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

        if (!lang.equals("en")){
            homePage.selectEnglishLanguage();
            lang = homePage.getLanguage();
        }
        Assertion.assertThat("Current language is EN", lang, is(currentLang));

        step("Change language to ID");
        homePage.selectBahasaLanguage();
        if(Configs.PLATFORM== Platform.DWEB) {
            Assertion.assertThat("Changed language is ID", homePage.getLanguage(), is(langToBeChanged));
        }else {
            Assertion.assertThat("Changed language is ID", homePage.getLanguage(), is(langToBeChanged));
        }
    }

    @Author(names = "Saurabh")
    @Module(name = "Language")
    @Tribe(name = "Membership")
    @Vertical(name = "Platform")
    @Test
    public void testChangeLangToIDLogin() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testChangeLangToIDLogin");
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

        if (!lang.equalsIgnoreCase("en")) {
            homePage.selectEnglishLanguage();
        }
        Assertion.assertThat("Current language is EN", homePage.getLanguage(), is(currentLang));

        step("Change language to ID");
        homePage.selectBahasaLanguage();
        if(Configs.PLATFORM== Platform.DWEB) {
            Assertion.assertThat("Changed language is ID", homePage.getLanguage(), is(langToBeChanged));
        }else {
            Assertion.assertThat("Changed language is ID", homePage.getLanguage(), is(langToBeChanged));
        }
    }

    @Author(names = {"Yanuar"})
    @Module(name = "SmartProfile")
    @Tribe(name = "Membership")
    @Vertical(name = "Platform")
    @Test
    public void verifyPassportRenewalBannerOnPassengerList() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyPassportRenewalBannerOnPassengerList");
        BaseTest.paramsThreadLocal.set(param);
        Session<WebDriver> session = sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        AccountPageWeb<?> accountPage = AccountPageWeb.getInstance(session);
        SmartProfileApi<?> smartProfileApi = new SmartProfileApi<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String firstName = param.get("firstName");
        String lastName = param.get("lastName");
        String language = param.get("language");
        PassportRenewalCopy copy = PassportRenewalCopy.forLanguage(language);

        try {
            step("Given the user is logged in");
            homePage.goToHomePage();
            loginPage.loginWithSpecificAccountViaCookie(email, password);

            step("And the account has an Indonesian passport expiring within the renewal window");
            smartProfileApi.upsertIndonesianPassportProfile(
                    email,
                    password,
                    firstName,
                    lastName,
                    param.get("salutation"),
                    param.get("passportNumber"),
                    param.get("passportIssuedDate"),
                    param.get("passportIssuingCountry"),
                    Integer.parseInt(param.get("passportExpiryOffsetDays")));

            step("And the session is Indonesia with language=" + language);
            homePage.changeCountryToIndonesia();
            homePage.selectLanguage(language);

            step("When the user opens the passenger list");
            loginPage.goToAccountPage();
            accountPage.goToSavePassengerData();
            accountPage.waitForPassengerList();

            step("Then the passport-renewal banner occupies the banner slot instead of the default banner");
            Assertion.assertThat("Passport-renewal banner is shown above the profile rows",
                    accountPage.isPassportRenewalBannerDisplayed(), is(true));
            Assertion.assertThat("Renewal banner replaced the default one rather than joining it",
                    accountPage.passengerListBannerCount(), is(1));

            step("And the banner shows a warning icon and the renewal copy");
            Assertion.assertThat("Renewal banner shows a warning icon",
                    accountPage.isPassportRenewalWarningIconDisplayed(), is(true));
            Assertion.assertThat("Renewal banner copy describes the passport-renewal steps",
                    accountPage.getPassportRenewalBannerText(),
                    containsStringIgnoringCase(copy.bannerCopy()));

            step("And the action link is labelled See how");
            Assertion.assertThat("Action link label is See how",
                    accountPage.getSeeHowLinkText(), containsStringIgnoringCase(copy.seeHowLabel()));

            step("And the qualifying profile row carries the expiring-passport indicator");
            Assertion.assertThat("Profile row for the expiring Indonesian passport shows the expiry indicator",
                    accountPage.hasExpiringPassportIndicator(firstName, lastName), is(true));

            step("When the user opens the created profile");
            accountPage.openSavedPassenger(firstName, lastName);

            step("Then the passport card shows the expiry warning and the renewal banner");
            Assertion.assertThat("Passport section title is shown",
                    accountPage.getPassportDetailTitle(),
                    containsStringIgnoringCase(copy.passportSectionTitle()));
            Assertion.assertThat("Passport card shows the expiry-soon warning",
                    accountPage.getPassportExpiryWarningText(),
                    containsStringIgnoringCase(copy.expirySoonWarning()));
            Assertion.assertThat("Passport card shows the renewal banner copy",
                    accountPage.getPassportDetailBannerText(),
                    containsStringIgnoringCase(copy.bannerCopy()));
            Assertion.assertThat("Passport card action link is See how",
                    accountPage.getPassportDetailSeeHowText(),
                    containsStringIgnoringCase(copy.seeHowLabel()));

            step("When the user taps the action link");
            ArticleVisit article = accountPage.openSeeHowArticle();

            step("Then the stories article for renewing a passport opens");
            Assertion.assertThat("See how opens the passport-renewal article",
                    article.url(), containsString(AccountPageWeb.PASSPORT_RENEWAL_ARTICLE_PATH));
        } finally {
            smartProfileApi.deletePassportProfileQuietly(email, password, firstName, lastName);
        }
    }

    @Author(names = { "Yanuar" })
    @Module(name = "SmartProfile")
    @Tribe(name = "Membership")
    @Vertical(name = "Platform")
    @Test
    public void verifyCreateAdditionalProfileAsMononym() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyCreateAdditionalProfileAsMononym");
        BaseTest.paramsThreadLocal.set(param);
        Session<WebDriver> session = sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        AccountPageWeb<?> accountPage = AccountPageWeb.getInstance(session);
        SmartProfileApi<?> smartProfileApi = new SmartProfileApi<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String nationality = param.get("nationality");
        String firstName = String.valueOf(param.get("firstName") + CoreFunctions.generateRandomString(10)).toLowerCase();
        String ninCode = CoreFunctions.generateRandomNumeric(16);
        String profileEmail = CoreFunctions.randomEmail();
        String phoneNumber = CoreFunctions.randomPhone();

        step("Given the user is logged in and on the saved-passenger list");
        homePage.goToHomePage();
        loginPage.loginWithSpecificAccountViaCookie(email, password);
        accountPage.goToAccountPageAsMember(email, password);
        accountPage.goToSavePassengerData();
        accountPage.waitForPassengerList();

        step("When the user taps Add Profile");
        accountPage.clickAddPassengerData();
        accountPage.waitForProfileForm();

        step("Then the create form offers the mononym option, unticked, with Last Name enabled");
        Assertion.assertThat("Create form offers the no-last-name option",
                accountPage.isNoLastNameOptionDisplayed(), is(true));
        Assertion.assertThat("No-last-name option starts unticked",
                accountPage.isNoLastNameChecked(), is(false));
        Assertion.assertThat("Last Name starts enabled",
                accountPage.isLastNameFieldEnabled(), is(true));

        step("When the user enters the single-token name " + firstName + " and ticks the option");
        accountPage.enterFirstName(firstName);
        accountPage.tickNoLastName();

        step("Then Last Name is disabled and cleared");
        Assertion.assertThat("Ticking the option disables Last Name",
                accountPage.isLastNameFieldDisabled(), is(true));
        Assertion.assertThat("Ticking the option clears Last Name",
                accountPage.isLastNameFieldEmpty(), is(true));

        step("When the remaining mandatory fields are filled and the profile is saved");
        accountPage.fillMononymMandatoryFields(nationality, ninCode, profileEmail, phoneNumber);

        step("Then the save succeeds and the list comes back");
        accountPage.saveProfileAndWaitForList();

        try {
            step("And the new row shows the first name only, with no force-filled last name");
            Assertion.assertThat("Saved row renders the single name, not a doubled one",
                    accountPage.getSavedPassengerDisplayName(firstName), is(firstName));

            step("When the user re-opens that profile");
            accountPage.openSavedProfile(firstName);

            step("Then it is still a mononym: option ticked, Last Name empty and disabled");
            Assertion.assertThat("Re-opened profile kept the first name",
                    accountPage.getFirstNameValue(), is(firstName));
            Assertion.assertThat("Re-opened profile keeps the option ticked",
                    accountPage.isNoLastNameChecked(), is(true));
            Assertion.assertThat("Re-opened profile has an empty Last Name",
                    accountPage.isLastNameFieldEmpty(), is(true));
            Assertion.assertThat("Re-opened profile keeps Last Name disabled",
                    accountPage.isLastNameFieldDisabled(), is(true));
        } finally {
            smartProfileApi.deleteProfileByNameQuietly(email, password, firstName, "");
        }
    }
}
