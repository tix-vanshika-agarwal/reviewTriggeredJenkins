package com.tiket.web.dashboard.test.b2bCorp_internal;

import com.tiket.apiPage.OrderApi;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.testbase.BaseTest;
import com.tiket.web.WebTest;
import com.tiket.web.dashboard.page.b2bCorp_internal.CorporateDataPage;
import com.tiket.web.dashboard.page.b2bCorp_internal.CorporateInternalDashboardSidebar;
import com.tiket.web.dashboard.page.b2bCorp_internal.RegisteredCorporatePage;
import com.tiket.web.dashboard.page.b2bCorp_internal.TiketPlusForCorporateInternal;
import com.tiket.web.dashboard.page.b2b_external.TiketPlusForCorporateExternal;
import com.tiket.web.dashboard.page.common.LoginMemberAdmin;
import com.tiket.web.page.common.home.LoginPageWeb;
import org.hamcrest.core.Is;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.core.CoreFunctions.randomName;
import static com.tiket.testdata.TestData.getObjectParams;
import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.equalToIgnoringCase;

public class B2BInternalDashboardTest extends WebTest {
    @Author(names = {"Manan Kumar"})
    @Module(name = "Corporate Data")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "testActionButtonOnLegalDocumentShowsAllOptions_DataProvider")
    public void testActionButtonOnLegalDocumentShowsAllOptions(Map<String, String> param) throws Exception {
        WebTest.paramsThreadLocal.set(param);

        Session<?> session = WebTest.sessionThreadLocal.get();
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        CorporateDataPage<?> corporateDataPage = new CorporateDataPage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String corporateName = param.get("corporateName");

        step("Login via cookie injection");
        loginMemberAdmin.loginViaCookie(email, password);

        step("Go to Corporate Internal Dashboard");
        loginMemberAdmin.goToDashboard();

        step("Check if user is logged in");
        boolean isLoggedInCRP = corporateDataPage.checkUserIsLoggedIn();

        step("Assert that user is is logged in to corporate dashboard");
        Assertion.assertThat("User is logged in", isLoggedInCRP, is(true));

        step("Click ON corporate information");
        corporateDataPage.clickCorporateInformation();

        step("Click ON corporate data");
        corporateDataPage.clickCorporateData();

        step("Send corporate name to search");
        corporateDataPage.inputSearch(corporateName);

        step("Click Search");
        corporateDataPage.clickSearch();

        step("Check if search is successful");
        corporateDataPage.checkIfCorporateExists(corporateName);

        step("User clicks on See Details Action Button");
        corporateDataPage.clickSeeDetailsBtn();

        step("User go to Corporate Document page.");
        corporateDataPage.clickCorporateDocument();

        step("User clicks on Action button on Legal Document page.");
        corporateDataPage.clickActionButtonOnLegalDocument();

        step("Verify that all options are displayed in the Action button on Legal Document page.");
        boolean isAllOptionsDisplayed = corporateDataPage.isLegalDocumentOptionsDisplayed();
        Assertion.assertThat("Verify that all options are displayed in the Action button on Legal Document page.", isAllOptionsDisplayed, is(true));
    }

    @DataProvider(name = "testActionButtonOnLegalDocumentShowsAllOptions_DataProvider")
    public Object[][] testActionButtonOnLegalDocumentShowsAllOptions_DataProvider() {
        return getObjectParams("testActionButtonOnLegalDocumentShowsAllOptions");
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "testVerifyAssignAMTabDisplaysAssignedAMInfo_DataProvider")
    public void testVerifyAssignAMTabDisplaysAssignedAMInfo(Map<String, String> param) throws Exception {
        WebTest.paramsThreadLocal.set(param);

        Session<?> session = WebTest.sessionThreadLocal.get();
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        CorporateDataPage<?> corporateDataPage = new CorporateDataPage<>(session);
        CorporateInternalDashboardSidebar<?> corporateInternalDashboardSidebar = new CorporateInternalDashboardSidebar<>(session);
        RegisteredCorporatePage<?> registeredCorporatePage = new RegisteredCorporatePage<>(session);


        String email = param.get("email");
        String password = param.get("password");

        step("Login via cookie injection");
        loginMemberAdmin.loginViaCookie(email, password);

        step("Go to Corporate Internal Dashboard");
        loginMemberAdmin.goToDashboard();

        step("Check if user is logged in");
        boolean isLoggedInCRP = corporateDataPage.checkUserIsLoggedIn();

        step("Assert that user is is logged in to corporate dashboard");
        Assertion.assertThat("User is logged in", isLoggedInCRP, is(true));

        step("Click ON corporate information");
        corporateDataPage.clickCorporateInformation();

        step("Click ON Registered Corporate");
        corporateInternalDashboardSidebar.openRegisteredCorporate();

        step("Check if page is opened successfully");
        boolean isPageOpened = registeredCorporatePage.isPageOpened();
        Assertion.assertThat("Registered Corporate page is opened", isPageOpened, is(true));

        step("Click on Status dropdown");
        registeredCorporatePage.clickStatusDropdown();

        step("Select Approved option from Status dropdown");
        registeredCorporatePage.selectApprovedOption();

        step("Click on Save button");
        registeredCorporatePage.clickSaveButton();

        step("Verify that Lead name Data is Present.");
        boolean isLeadDataPresent = registeredCorporatePage.isLeadDataPresent();
        Assertion.assertThat("Lead name is present", isLeadDataPresent, is(true));

        step("Click on Lead name");
        registeredCorporatePage.clickLeadName();

        step("Click on Negotiation & Pre-Onboarding button");
        registeredCorporatePage.clickNegotiationButton();

        step("Verify that AM info is present");
        boolean isAMInfoPresent = registeredCorporatePage.isAMInfoPresent();
        Assertion.assertThat("AM info is present", isAMInfoPresent, is(true));
    }

    @DataProvider(name = "testVerifyAssignAMTabDisplaysAssignedAMInfo_DataProvider")
    public Object[][] testVerifyAssignAMTabDisplaysAssignedAMInfo_DataProvider() {
        return getObjectParams("testVerifyAssignAMTabDisplaysAssignedAMInfo");
    }
  
    @Author(names = {"Ankit Raj"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "testClickingRegisteredCorpOpenRegisteredCorpPage")
    public void testClickingRegisteredCorpOpenRegisteredCorpPage(Map<String, String> param) {
        WebTest.paramsThreadLocal.set(param);

        Session<?> session = WebTest.sessionThreadLocal.get();
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        CorporateInternalDashboardSidebar<?> corporateInternalDashboardSidebar = new CorporateInternalDashboardSidebar<>(session);
        RegisteredCorporatePage<?> registeredCorporatePage = new RegisteredCorporatePage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String heading = param.get("heading");

        step("Go to member login page");
        loginMemberAdmin.gotoHome();

        step("Log in");
        loginMemberAdmin.loginAsGuest();

        step("Input email");
        loginMemberAdmin.inputEmail(email);

        step("Input password");
        loginMemberAdmin.inputPassword(password);

        step("Click Login");
        boolean isLoggedInMemberAdmin = loginMemberAdmin.clickLogin();

        step("Assert that user is logged in to Member Admin");
        Assertion.assertThat("User is logged in", isLoggedInMemberAdmin, is(true));

        step("Open B2B Corporate");
        loginMemberAdmin.goToB2BCorporateDashboard();

        step("Expand the corporate information");
        corporateInternalDashboardSidebar.expandCorporateInformation();

        step("Click the registered information");
        corporateInternalDashboardSidebar.openRegisteredCorporate();

        step("Check if page is opened successfully");
        boolean isPageOpened = registeredCorporatePage.isPageOpened();
        Assertion.assertThat("Registered Corporate page is opened", isPageOpened, is(true));

        step("Verify Heading");
        boolean headValue = registeredCorporatePage.verifyHeading(heading);
        Assertion.assertThat("Heading is correct", headValue, is(true));

        step("Verify Lead Name is present");
        boolean leadName = registeredCorporatePage.verifyLeadName();
        Assertion.assertThat("Lead Name is present", leadName, is(true));

        step("Verify CorporateName is present");
        boolean CorporateName = registeredCorporatePage.verifyCorporateName();
        Assertion.assertThat("CorporateName is present", CorporateName, is(true));

        step("Verify Type is present");
        boolean type = registeredCorporatePage.verifyType();
        Assertion.assertThat("Type is present", type, is(true));

        step("Verify Registration PIC");
        boolean RegistrationPIC = registeredCorporatePage.verifyRegistrationPIC();
        Assertion.assertThat("Registration PIC is present", RegistrationPIC, is(true));

        step("Verify Monthly Budget is present");
        boolean monthly_budget = registeredCorporatePage.verifyMonthlyBudget();
        Assertion.assertThat("Monthly Budget is present", monthly_budget, is(true));

        step("Verify Account Manager is present");
        boolean accountManager = registeredCorporatePage.verifyAccountManager();
        Assertion.assertThat("Account Manager is present", accountManager, is(true));

        step("Verify Payment method is present");
        boolean paymentMethod = registeredCorporatePage.verifyPaymentMethod();
        Assertion.assertThat("Payment Method is present", paymentMethod, is(true));

        step("Verify Status");
        boolean status = registeredCorporatePage.verifyStatus();
        Assertion.assertThat("Status is present", status, is(true));

        step("Logout");
        loginMemberAdmin.logout();
    }

    @DataProvider(name = "testClickingRegisteredCorpOpenRegisteredCorpPage")
    public Object[][] testClickingRegisteredCorpOpenRegisteredCorpPage() {
        return getObjectParams("testClickingRegisteredCorpOpenRegisteredCorpPage");
    }

  @Author(names = {"Manan Kumar"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "testPaymentPreferenceChangeCreatesNewAgreement_DataProvider")
    public void testPaymentPreferenceChangeCreatesNewAgreement(Map<String, String> param) throws Exception {

        WebTest.paramsThreadLocal.set(param);

        Session<?> session = WebTest.sessionThreadLocal.get();
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        CorporateDataPage<?> corporateDataPage = new CorporateDataPage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String corporateName = param.get("corporateName");
        String fee = param.get("fee");
        String limit = param.get("limit");
        String terms = param.get("terms");

        step("Login via cookie injection");
        loginMemberAdmin.loginViaCookie(email, password);

        step("Go to Corporate Internal Dashboard");
        loginMemberAdmin.goToDashboard();

        step("Check if user is logged in");
        boolean isLoggedInCRP = corporateDataPage.checkUserIsLoggedIn();

        step("Assert that user is is logged in to corporate dashboard");
        Assertion.assertThat("User is logged in", isLoggedInCRP, is(true));

        step("Click ON corporate information");
        corporateDataPage.clickCorporateInformation();

        step("Click ON corporate data");
        corporateDataPage.clickCorporateData();

        step("Send corporate name to search");
        corporateDataPage.inputSearch(corporateName);

        step("Check if search is successful");
        Assertion.assertThat("Check if corporate Name exists",corporateDataPage.checkIfCorporateExists(corporateName),is(true));

        step("User clicks on See Details Action Button");
        corporateDataPage.clickSeeDetailsBtn();

        step("Verify that Payments details are present.");
        boolean isPaymentDetailsPresent = corporateDataPage.isPaymentDetailsPresent();
        Assertion.assertThat("Verify that Payments details are present.", isPaymentDetailsPresent, is(true));

        step("User Click on Payment Edit Option.");
        corporateDataPage.clickPaymentEdit();

        step("User selects Payment Options.");
        corporateDataPage.openPaymentOptions(fee, limit, terms);

        step("User clicks on Save button.");
        corporateDataPage.clickPaymentSaveButton();

        step("Verify that payment change Snack bar is displayed.");
        boolean isPaymentChangeSnackBarDisplayed = corporateDataPage.isPaymentChangeSnackBarDisplayed();
        Assertion.assertThat("Verify that payment change Snack bar is displayed.", isPaymentChangeSnackBarDisplayed, is(true));

        step("User go to Corporate Document page.");
        corporateDataPage.clickCorporateDocument();

        step("User verifies that new agreement is created.");
        boolean isNewAgreementCreated = corporateDataPage.isE_agreementGenerated(corporateName);
        Assertion.assertThat("Verify that new agreement is created.", isNewAgreementCreated, is(true));

        step("Delete Newly generated agreement");
        String snackbarText=corporateDataPage.deleteDocument();
        Assertion.assertThat("Verify that document delete Snack bar is displayed.",snackbarText,containsString("successfully deleted"));

        step("User logs out.");
        loginMemberAdmin.logout();
    }

    @DataProvider(name = "testPaymentPreferenceChangeCreatesNewAgreement_DataProvider")
    public Object[][] testPaymentPreferenceChangeCreatesNewAgreement_DataProvider() {
        return getObjectParams("testPaymentPreferenceChangeCreatesNewAgreement");
    }
  
  @Author(names = {"Ankit Raj"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "testRegisteredTabShowsCorporateRegisteredData")
    public void testRegisteredTabShowsCorporateRegisteredData(Map<String, String> param) {
  
      WebTest.paramsThreadLocal.set(param);

        Session<?> session = WebTest.sessionThreadLocal.get();
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        CorporateInternalDashboardSidebar<?> corporateInternalDashboardSidebar = new CorporateInternalDashboardSidebar<>(session);
        RegisteredCorporatePage<?> registeredCorporatePage = new RegisteredCorporatePage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String heading = param.get("heading");
  
        step("Login via cookie injection");
        loginMemberAdmin.loginViaCookie(email, password);
        
        step("Open B2B Corporate");
        loginMemberAdmin.goToB2BCorporateDashboard();

        step("Expand the corporate information");
        corporateInternalDashboardSidebar.expandCorporateInformation();

        step("Click the registered information");
        corporateInternalDashboardSidebar.openRegisteredCorporate();

        step("Check if page is opened successfully");
        boolean isPageOpened = registeredCorporatePage.isPageOpened();
        Assertion.assertThat("Registered Corporate page is opened", isPageOpened, is(true));

        step("Verify Heading");
        boolean headValue = registeredCorporatePage.verifyHeading(heading);
        Assertion.assertThat("Heading is correct", headValue, is(true));

        step("Verify Lead Name is present");
        boolean leadName = registeredCorporatePage.verifyLeadName();
        Assertion.assertThat("Lead Name is present", leadName, is(true));

        step("Verify CorporateName is present");
        boolean CorporateName = registeredCorporatePage.verifyCorporateName();
        Assertion.assertThat("CorporateName is present", CorporateName, is(true));

        step("Verify Type is present");
        boolean type = registeredCorporatePage.verifyType();
        Assertion.assertThat("Type is present", type, is(true));

        step("Verify Registration PIC");
        boolean RegistrationPIC = registeredCorporatePage.verifyRegistrationPIC();
        Assertion.assertThat("Registration PIC is present", RegistrationPIC, is(true));

        step("Verify Monthly Budget is present");
        boolean monthly_budget = registeredCorporatePage.verifyMonthlyBudget();
        Assertion.assertThat("Monthly Budget is present", monthly_budget, is(true));

        step("Verify Account Manager is present");
        boolean accountManager = registeredCorporatePage.verifyAccountManager();
        Assertion.assertThat("Account Manager is present", accountManager, is(true));

        step("Verify Payment method is present");
        boolean paymentMethod = registeredCorporatePage.verifyPaymentMethod();
        Assertion.assertThat("Payment Method is present", paymentMethod, is(true));

        step("Verify Status");
        boolean status = registeredCorporatePage.verifyStatus();
        Assertion.assertThat("Status is present", status, is(true));
  
        step("Click on Status Dropdown");
        registeredCorporatePage.clickStatusDropdown();

        step("Select approved Option");
        registeredCorporatePage.selectApprovedOption();

        step("Click Save");
        registeredCorporatePage.clickSaveButton();

        step("Extract the first corporate Name");
        String corpName = registeredCorporatePage.extractCorporateName();

        step("Click on First Lead Name");
        registeredCorporatePage.clickLeadName();

        step("Check if corporate Name Matches");
        boolean nameMatch = registeredCorporatePage.checkIfCorporateNameMatches(corpName);
        Assertion.assertThat("Corporate Name Matches", nameMatch, is(true));

        step("Toggle is disable");
        boolean isDisabled = registeredCorporatePage.isCheckboxDisabled();
        Assertion.assertThat("Toggle is disable", isDisabled, is(true));

        step("Click on Registered");
        registeredCorporatePage.clickOnRegistered();

        step("Verify if Corporate Details is present");
        boolean isCorporateDetails = registeredCorporatePage.isCorporateDetailsPresent();
        Assertion.assertThat("Corporate Details is present ", isCorporateDetails, is(true));

        step("Verify if Payment Details is present");
        boolean isPaymentDetails = registeredCorporatePage.isPaymentDetailsPresent();
        Assertion.assertThat("Payment Details is present ", isPaymentDetails, is(true));

        step("Logout");
        loginMemberAdmin.logout();
    }

    @DataProvider(name = "testRegisteredTabShowsCorporateRegisteredData")
    public Object[][] testRegisteredTabShowsCorporateRegisteredData() {
        return getObjectParams("testRegisteredTabShowsCorporateRegisteredData");
    }

    @Author(names = {"Ankit Raj"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "testClickingEmployeeListShowsEmployeeDetails")
    public void testClickingEmployeeListShowsEmployeeDetails(Map<String, String> param) throws Exception {
        WebTest.paramsThreadLocal.set(param);

        Session<?> session = WebTest.sessionThreadLocal.get();
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        CorporateInternalDashboardSidebar<?> corporateInternalDashboardSidebar = new CorporateInternalDashboardSidebar<>(session);
        CorporateDataPage<?> corporateDataPage = new CorporateDataPage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        
        step("Login via cookie injection");
        loginMemberAdmin.loginViaCookie(email, password);
      
        step("Open B2B Corporate");
        loginMemberAdmin.goToB2BCorporateDashboard();

        step("Expand the corporate information");
        corporateInternalDashboardSidebar.expandCorporateInformation();

        step("Click on Corporate Data");
        corporateDataPage.clickCorporateData();
        
        step("Click on See Details");
        corporateDataPage.clickSeeDetailsBtn();
  
        step("Click on Employee List");
        corporateDataPage.clickEmployeeList();

        step("Check if Employee Name and Email exists");
        boolean isEmployeeNameEmailPresent = corporateDataPage.isEmployeeNameEmailPresent();
        Assertion.assertThat("Employee Name and Email exists", isEmployeeNameEmailPresent, is(true));

        step("Check if User Role exists");
        boolean isUserRolePresent = corporateDataPage.isUserRolePresent();
        Assertion.assertThat("User Role exists", isUserRolePresent, is(true));

        step("Check if Department Permission exists");
        boolean isDepartmentPermissionPresent = corporateDataPage.isDepartmentPermissionPresent();
        Assertion.assertThat("Department Permission exists", isDepartmentPermissionPresent, is(true));

        step("Check if Group Segment exists");
        boolean isGroupSegmentPresent = corporateDataPage.isGroupSegmentPresent();
        Assertion.assertThat("Group Segment exists", isGroupSegmentPresent, is(true));

        step("Check if Status exists");
        boolean isStatusPresent = corporateDataPage.isStatusPresent();
        Assertion.assertThat("Status exists", isStatusPresent, is(true));

        step("Check if Created Date exists");
        boolean isCreatedDatePresent = corporateDataPage.isCreatedDatePresent();
        Assertion.assertThat("Created Date exists", isCreatedDatePresent, is(true));

        step("Check if Update Date exists");
        boolean isUpdateDatePresent = corporateDataPage.isUpdateDatePresent();
        Assertion.assertThat("Update Date exists", isUpdateDatePresent, is(true));
        
        step("Logout");
        loginMemberAdmin.logout();
    }
  
    @DataProvider(name = "testClickingEmployeeListShowsEmployeeDetails")
    public Object[][] testClickingEmployeeListShowsEmployeeDetails() {
        return getObjectParams("testClickingEmployeeListShowsEmployeeDetails");
    }

    @Author(names = {"Ankit Raj"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "testClickingOnCorporateDataShowsCompanyAndPaymentDetails")
    public void testClickingOnCorporateDataShowsCompanyAndPaymentDetails(Map<String, String> param) throws Exception {
        WebTest.paramsThreadLocal.set(param);

        Session<?> session = WebTest.sessionThreadLocal.get();
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        CorporateInternalDashboardSidebar<?> corporateInternalDashboardSidebar = new CorporateInternalDashboardSidebar<>(session);
        CorporateDataPage<?> corporateDataPage = new CorporateDataPage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        
        step("Login via cookie injection");
        loginMemberAdmin.loginViaCookie(email, password);
      
        step("Open B2B Corporate");
        loginMemberAdmin.goToB2BCorporateDashboard();

        step("Expand the corporate information");
        corporateInternalDashboardSidebar.expandCorporateInformation();

        step("Click on Corporate Data");
        corporateDataPage.clickCorporateData();
        
        step("Click on See Details");
        corporateDataPage.clickSeeDetailsBtn();
          
        step("Check if Company Details exists");
        boolean isCorporateDetailsExists = corporateDataPage.isCompanyDetailsPresent();
        Assertion.assertThat("Corporate Details exists", isCorporateDetailsExists, is(true));

        step("Check if Payment Details exists");
        boolean isPaymentDetailsExists = corporateDataPage.isPaymentDetailsPresent();
        Assertion.assertThat("Payment Details exists", isPaymentDetailsExists, is(true));
  
        step("Logout");
        loginMemberAdmin.logout();
    }
  
    @DataProvider(name = "testClickingOnCorporateDataShowsCompanyAndPaymentDetails")
    public Object[][] testClickingOnCorporateDataShowsCompanyAndPaymentDetails() {
        return getObjectParams("testClickingOnCorporateDataShowsCompanyAndPaymentDetails");
    }

    @Author(names = {"Ankit Raj"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "testVerifyClickingOnLeadNameTakesToManagerRegisteredCorpPage")
    public void testVerifyClickingOnLeadNameTakesToManagerRegisteredCorpPage(Map<String, String> param) {

        WebTest.paramsThreadLocal.set(param);

        Session<?> session = WebTest.sessionThreadLocal.get();
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        CorporateInternalDashboardSidebar<?> corporateInternalDashboardSidebar = new CorporateInternalDashboardSidebar<>(session);
        RegisteredCorporatePage<?> registeredCorporatePage = new RegisteredCorporatePage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String heading = param.get("heading");

        step("Login via cookie injection");
        loginMemberAdmin.loginViaCookie(email, password);

        step("Open B2B Corporate");
        loginMemberAdmin.goToB2BCorporateDashboard();

        step("Expand the corporate information");
        corporateInternalDashboardSidebar.expandCorporateInformation();

        step("Click the registered information");
        corporateInternalDashboardSidebar.openRegisteredCorporate();

        step("Check if page is opened successfully");
        boolean isPageOpened = registeredCorporatePage.isPageOpened();
        Assertion.assertThat("Registered Corporate page is opened", isPageOpened, is(true));

        step("Verify Heading");
        boolean headValue = registeredCorporatePage.verifyHeading(heading);
        Assertion.assertThat("Heading is correct", headValue, is(true));

        step("Verify Lead Name is present");
        boolean leadName = registeredCorporatePage.verifyLeadName();
        Assertion.assertThat("Lead Name is present", leadName, is(true));

        step("Verify CorporateName is present");
        boolean CorporateName = registeredCorporatePage.verifyCorporateName();
        Assertion.assertThat("CorporateName is present", CorporateName, is(true));

        step("Verify Type is present");
        boolean type = registeredCorporatePage.verifyType();
        Assertion.assertThat("Type is present", type, is(true));

        step("Verify Registration PIC");
        boolean RegistrationPIC = registeredCorporatePage.verifyRegistrationPIC();
        Assertion.assertThat("Registration PIC is present", RegistrationPIC, is(true));

        step("Verify Monthly Budget is present");
        boolean monthly_budget = registeredCorporatePage.verifyMonthlyBudget();
        Assertion.assertThat("Monthly Budget is present", monthly_budget, is(true));

        step("Verify Account Manager is present");
        boolean accountManager = registeredCorporatePage.verifyAccountManager();
        Assertion.assertThat("Account Manager is present", accountManager, is(true));

        step("Verify Payment method is present");
        boolean paymentMethod = registeredCorporatePage.verifyPaymentMethod();
        Assertion.assertThat("Payment Method is present", paymentMethod, is(true));

        step("Verify Status");
        boolean status = registeredCorporatePage.verifyStatus();
        Assertion.assertThat("Status is present", status, is(true));

        step("Click on Status Dropdown");
        registeredCorporatePage.clickStatusDropdown();

        step("Select approved Option");
        registeredCorporatePage.selectApprovedOption();

        step("Click Save");
        registeredCorporatePage.clickSaveButton();

        step("Extract the first corporate Name");
        String corpName = registeredCorporatePage.extractCorporateName();

        step("Click on First Lead Name");
        registeredCorporatePage.clickLeadName();

        step("Check if corporate Name Matches");
        boolean nameMatch = registeredCorporatePage.checkIfCorporateNameMatches(corpName);
        Assertion.assertThat("Corporate Name Matches", nameMatch, is(true));

        step("Toggle is disable");
        boolean isDisabled = registeredCorporatePage.isCheckboxDisabled();
        Assertion.assertThat("Toggle is disable", isDisabled, is(true));

        step("Logout");
        loginMemberAdmin.logout();
    }

    @DataProvider(name = "testVerifyClickingOnLeadNameTakesToManagerRegisteredCorpPage")
    public Object[][] testVerifyClickingOnLeadNameTakesToManagerRegisteredCorpPage() {
        return getObjectParams("testVerifyClickingOnLeadNameTakesToManagerRegisteredCorpPage");
    }

    @Author(names = {"Ankit Raj"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")

    @Test(dataProvider = "testNegotiationAndPreOnboardingShowsCorporateRegisteredData")
    public void testNegotiationAndPreOnboardingShowsCorporateRegisteredData(Map<String, String> param) {
        WebTest.paramsThreadLocal.set(param);

        Session<?> session = WebTest.sessionThreadLocal.get();
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        CorporateInternalDashboardSidebar<?> corporateInternalDashboardSidebar = new CorporateInternalDashboardSidebar<>(session);
        RegisteredCorporatePage<?> registeredCorporatePage = new RegisteredCorporatePage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String heading = param.get("heading");

        step("Login via cookie injection");
        loginMemberAdmin.loginViaCookie(email, password);

        step("Open B2B Corporate");
        loginMemberAdmin.goToB2BCorporateDashboard();

        step("Expand the corporate information");
        corporateInternalDashboardSidebar.expandCorporateInformation();

        step("Click the registered information");
        corporateInternalDashboardSidebar.openRegisteredCorporate();

        step("Check if page is opened successfully");
        boolean isPageOpened = registeredCorporatePage.isPageOpened();
        Assertion.assertThat("Registered Corporate page is opened", isPageOpened, is(true));

        step("Verify Heading");
        boolean headValue = registeredCorporatePage.verifyHeading(heading);
        Assertion.assertThat("Heading is correct", headValue, is(true));

        step("Verify Lead Name is present");
        boolean leadName = registeredCorporatePage.verifyLeadName();
        Assertion.assertThat("Lead Name is present", leadName, is(true));

        step("Verify CorporateName is present");
        boolean CorporateName = registeredCorporatePage.verifyCorporateName();
        Assertion.assertThat("CorporateName is present", CorporateName, is(true));

        step("Verify Type is present");
        boolean type = registeredCorporatePage.verifyType();
        Assertion.assertThat("Type is present", type, is(true));

        step("Verify Registration PIC");
        boolean RegistrationPIC = registeredCorporatePage.verifyRegistrationPIC();
        Assertion.assertThat("Registration PIC is present", RegistrationPIC, is(true));

        step("Verify Monthly Budget is present");
        boolean monthly_budget = registeredCorporatePage.verifyMonthlyBudget();
        Assertion.assertThat("Monthly Budget is present", monthly_budget, is(true));

        step("Verify Account Manager is present");
        boolean accountManager = registeredCorporatePage.verifyAccountManager();
        Assertion.assertThat("Account Manager is present", accountManager, is(true));

        step("Verify Payment method is present");
        boolean paymentMethod = registeredCorporatePage.verifyPaymentMethod();
        Assertion.assertThat("Payment Method is present", paymentMethod, is(true));

        step("Verify Status");
        boolean status = registeredCorporatePage.verifyStatus();
        Assertion.assertThat("Status is present", status, is(true));

        step("Click on Status Dropdown");
        registeredCorporatePage.clickStatusDropdown();

        step("Select approved Option");
        registeredCorporatePage.selectApprovedOption();

        step("Click Save");
        registeredCorporatePage.clickSaveButton();

        step("Extract the first corporate Name");
        String corpName = registeredCorporatePage.extractCorporateName();

        step("Click on First Lead Name");
        registeredCorporatePage.clickLeadName();

        step("Check if corporate Name Matches");
        boolean nameMatch = registeredCorporatePage.checkIfCorporateNameMatches(corpName);
        Assertion.assertThat("Corporate Name Matches", nameMatch, is(true));
  
        step("Click on Negotiation & Pre-Onboarding");
        registeredCorporatePage.clickNegotiationButton();

        step("Check if Assign AM is present");
        boolean isAssign = registeredCorporatePage.isAssignAMPresent();
        Assertion.assertThat("Assign AM is present", isAssign, is(true));

        step("Check if Under Negotiation is present");
        boolean isNegotiation = registeredCorporatePage.isUnderNegotiationPresent();
        Assertion.assertThat("Under Negotiation is present", isNegotiation, is(true));

        step("Check if Document Submission is present");
        boolean isDocument = registeredCorporatePage.isDocumentSubmissionPresent();
        Assertion.assertThat("Document Submission is present", isDocument, is(true));

        step("Check if Proceed To Onboarding is present");
        boolean isOnboarding = registeredCorporatePage.isProceedToOnBoardingPresent();
        Assertion.assertThat("Proceed To Onboarding is present", isOnboarding, is(true));
  
        step("Logout");
        loginMemberAdmin.logout();
    }
    
    @DataProvider(name = "testNegotiationAndPreOnboardingShowsCorporateRegisteredData")
    public Object[][] testNegotiationAndPreOnboardingShowsCorporateRegisteredData() {
        return getObjectParams("testNegotiationAndPreOnboardingShowsCorporateRegisteredData");
    }

    @Author(names = {"Abhishek Sharma"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void verifyNewPelitaCHACodeCRPDealB2B() {
        Map<String, String> param = getParam("verifyNewPelitaCHACodeCRPDealB2B");
        BaseTest.paramsThreadLocal.set(param);
        Session<WebDriver> session = sessionThreadLocal.get();

        CorporateInternalDashboardSidebar<?> corporateInternalDashboardSidebar = new CorporateInternalDashboardSidebar<>(session);
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        CorporateDataPage<?> corporateDataPage = new CorporateDataPage<>(session);

        String internalEmail = param.get("adminEmail");
        String internalPassword = param.get("adminPassword");
        String chaCodeHeading = param.get("chaCodeHeading");
        String code = param.get("code");
        String dateFormat = param.get("dateFormat");

        step("Login via cookie injection to Internal Dashboard");
        loginMemberAdmin.loginViaCookie(internalEmail, internalPassword);

        step("Go to Corporate Internal Dashboard");
        loginMemberAdmin.goToDashboard();

        step("Check if user is logged in to corporate dashboard");
        Assertion.assertThat("User is logged in", corporateDataPage.checkUserIsLoggedIn(), Is.is(true));

        step("Expand the Corporate Deal and Click %s CHA Codes".formatted(chaCodeHeading));
        corporateInternalDashboardSidebar.expandCorporateDealAndClickCHACodeByHeading(chaCodeHeading);

        step("Search for code: " + code);
        corporateInternalDashboardSidebar.searchCode(code);

        step("Verify Data from table");
        Assertion.assertThat("Verify Code Heading present", corporateInternalDashboardSidebar.getTableHeadingIndex("Code"), not(is(-1)));
        Assertion.assertThat("Verify Code", corporateInternalDashboardSidebar.getTableValueByHeading("Code"), containsStringIgnoringCase(code));
        Assertion.assertThat("Verify Used (%) Heading present", corporateInternalDashboardSidebar.getTableHeadingIndex("Used (%)"), not(is(-1)));
        Assertion.assertThat("Verify Used (%)", corporateInternalDashboardSidebar.getTableValueByHeading("Used (%)"), allOf(notNullValue(), containsString(" %")));
        Assertion.assertThat("Verify Reset Date Heading present", corporateInternalDashboardSidebar.getTableHeadingIndex("Reset Date"), not(is(-1)));
        String resetDate = corporateInternalDashboardSidebar.getTableValueByHeading("Reset Date");
        Assertion.assertThat("Verify Reset Date: " + resetDate, CoreFunctions.isDate(resetDate, dateFormat), is(true));
    }

    @Author(names = {"Harshit Ranjan"})
    @Module(name = "Invoice & Billing")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test
    public void verifyFailedIssuedIsNotPresentInInvoice() throws Exception {
        Map<String, String> param = getParam("verifyFailedIssuedIsNotPresentInInvoice");
        BaseTest.paramsThreadLocal.set(param);
        Session<WebDriver> session = sessionThreadLocal.get();

        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        CorporateDataPage<?> corporateDataPage = new CorporateDataPage<>(session);
        CorporateInternalDashboardSidebar<?> corporateInternalDashboardSidebar = new CorporateInternalDashboardSidebar<>(session);
        TiketPlusForCorporateInternal<?> corporateInternal = new TiketPlusForCorporateInternal<>(session);
        OrderApi<?> orderApi = new OrderApi<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String company = param.get("company");
        String orderId_eticketIssued = param.get("orderId_eticketIssued");
        String orderId_failed = param.get("orderId_failed");
        String orderId_processingEticket = param.get("orderId_processingEticket");

        step("Get order status of order %s from the internal order detail endpoint".formatted(orderId_eticketIssued));
        String orderStatusEticketIssued = orderApi.getOrderStatus(orderId_eticketIssued);

        step("Assert that order %s is Eticket Issued".formatted(orderId_eticketIssued));
        Assertion.assertThat("Order " + orderId_eticketIssued + " status should be Eticket Issued", orderStatusEticketIssued, containsStringIgnoringCase("ETICKET_ISSUED"));

        step("Get order status of order %s from the internal order detail endpoint".formatted(orderId_failed));
        String orderStatusFailed = orderApi.getOrderStatus(orderId_failed);

        step("Assert that order %s is failed issued".formatted(orderId_failed));
        Assertion.assertThat("Order " + orderId_failed + " status should be failed issued", orderStatusFailed, containsStringIgnoringCase("BOOKING_UNSUCCESSFUL"));

        step("Get order status of order %s from the internal order detail endpoint".formatted(orderId_processingEticket));
        String orderStatusProcessingEticket = orderApi.getOrderStatus(orderId_processingEticket);

        step("Assert that order %s is Processing Eticket".formatted(orderId_processingEticket));
        Assertion.assertThat("Order " + orderId_processingEticket + " status should be Processing Eticket", orderStatusProcessingEticket, containsStringIgnoringCase("PROCESSING_ETICKET"));

        step("Login via cookie injection");
        loginMemberAdmin.loginViaCookie(email, password);

        step("Go to Corporate Internal Dashboard");
        loginMemberAdmin.goToDashboard();

        step("Check if user is logged in");
        boolean isLoggedInCRP = corporateDataPage.checkUserIsLoggedIn();
        Assertion.assertThat("User is logged in", isLoggedInCRP, is(true));

        step("Navigate to Invoice & Billing");
        corporateInternalDashboardSidebar.goToInvoiceAndBilling();

        step("Verify Invoice & Billing page is opened");
        Assertion.assertThat("Invoice & Billing page should be opened", corporateInternal.isInvoiceAndBillingPageOpened(), is(true));

        step("Click Create new invoice");
        corporateInternal.clickCreateNewInvoice();

        step("Search and select corporate");
        corporateInternal.selectCorporate(company);

        step("Select System Order invoice type");
        corporateInternal.selectSystemOrderInvoiceType();

        step("Continue to add orders");
        corporateInternal.clickContinue();

        step("Click to add orders");
        corporateInternal.clickAddOrder();

        step("Search Processing Eticket order %s and verify it is not present in the row".formatted(orderId_processingEticket));
        boolean isProcessingEticketPresent = corporateInternal.searchAndVerifyOrderInRow(orderId_processingEticket);
        Assertion.assertThat("Processing Eticket order " + orderId_processingEticket + " should not be present", isProcessingEticketPresent, is(false));

        step("Search Eticket Issued order %s and verify it is present in the row".formatted(orderId_eticketIssued));
        boolean isEticketIssuedPresent = corporateInternal.searchAndVerifyOrderInRow(orderId_eticketIssued);
        Assertion.assertThat("Eticket Issued order " + orderId_eticketIssued + " should be present", isEticketIssuedPresent, is(true));

        step("Search Failed Issued order %s and verify it is not present in the row".formatted(orderId_failed));
        boolean isFailedIssuedPresent = corporateInternal.searchAndVerifyOrderInRow(orderId_failed);
        Assertion.assertThat("Failed Issued order " + orderId_failed + " should not be present", isFailedIssuedPresent, is(false));
    }

    @Author(names = {"Harshit Ranjan"})
    @Module(name = "Invoice & Billing")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test
    public void verifyPreviouslyChargedOrderIdNotPresent() throws Exception {
        Map<String, String> param = getParam("verifyPreviouslyChargedOrderIdNotPresent");
        BaseTest.paramsThreadLocal.set(param);
        Session<WebDriver> session = sessionThreadLocal.get();

        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        CorporateDataPage<?> corporateDataPage = new CorporateDataPage<>(session);
        CorporateInternalDashboardSidebar<?> corporateInternalDashboardSidebar = new CorporateInternalDashboardSidebar<>(session);
        TiketPlusForCorporateInternal<?> corporateInternal = new TiketPlusForCorporateInternal<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String company = param.get("company");
        String notes = CoreFunctions.generateRandomString(5) + " Notes";
        String termsAndConditions = CoreFunctions.generateRandomString(5) + " TnC";

        step("Login via cookie injection");
        loginMemberAdmin.loginViaCookie(email, password);

        step("Go to Corporate Internal Dashboard");
        loginMemberAdmin.goToDashboard();

        step("Check if user is logged in");
        Assertion.assertThat("User is logged in", corporateDataPage.checkUserIsLoggedIn(), is(true));

        step("Navigate to Invoice & Billing");
        corporateInternalDashboardSidebar.goToInvoiceAndBilling();

        step("Verify Invoice & Billing page is opened");
        Assertion.assertThat("Invoice & Billing page should be opened", corporateInternal.isInvoiceAndBillingPageOpened(), is(true));

        step("Click Create new invoice");
        corporateInternal.clickCreateNewInvoice();

        step("Search and select corporate");
        corporateInternal.selectCorporate(company);

        step("Select System Order invoice type");
        corporateInternal.selectSystemOrderInvoiceType();

        step("Continue to add orders");
        corporateInternal.clickContinue();

        step("Add the first available order and store its order ID");
        String chargedOrderId = corporateInternal.addFirstOrderAndGetOrderId();
        Assertion.assertThat("First order ID should be captured", chargedOrderId.isEmpty(), is(false));

        step("Add Notes and Terms & Conditions");
        corporateInternal.enterNotes(notes);
        corporateInternal.enterTermsAndConditions(termsAndConditions);

        step("Continue to Review");
        corporateInternal.clickContinue();

        step("Verify Review Invoice page is opened");
        Assertion.assertThat("Review Invoice page should be opened", corporateInternal.isReviewInvoicePageOpened(), is(true));

        step("Publish the invoice");
        corporateInternal.clickPublishInvoice();

        step("Publish and send the invoice");
        corporateInternal.clickPublishAndSend();

        step("Verify the invoice is generated with Unpaid status");
        Assertion.assertThat("Invoice status should be Unpaid", corporateInternal.getInvoiceStatus(), containsString("Unpaid"));

        step("Store the generated invoice number");
        String invoiceNumber = corporateInternal.getInvoiceNumber();

        step("Navigate to Invoice & Billing to create another invoice");
        corporateInternalDashboardSidebar.goToInvoiceAndBilling();

        step("Verify Invoice & Billing page is opened");
        Assertion.assertThat("Invoice & Billing page should be opened", corporateInternal.isInvoiceAndBillingPageOpened(), is(true));

        step("Click Create new invoice");
        corporateInternal.clickCreateNewInvoice();

        step("Search and select corporate");
        corporateInternal.selectCorporate(company);

        step("Select System Order invoice type");
        corporateInternal.selectSystemOrderInvoiceType();

        step("Continue to add orders");
        corporateInternal.clickContinue();

        step("Open the Add order modal");
        corporateInternal.clickAddOrder();

        step("Search previously charged order %s and verify it is not present in the row".formatted(chargedOrderId));
        boolean isChargedOrderPresent = corporateInternal.searchAndVerifyOrderInRow(chargedOrderId);
        Assertion.assertThat("Previously charged order " + chargedOrderId + " should not be present", isChargedOrderPresent, is(false));

        step("Come back to the Invoice & Billing list");
        corporateInternal.goBack();

        step("Search the generated invoice number in the input box");
        corporateInternal.searchInvoiceNumber(invoiceNumber);

        step("Click on the action button of the invoice row");
        corporateInternal.clickActionButton();


        step("Click on More Action button");
        corporateInternal.clickMoreAction();

        step("Click on Cancel Invoice option");
        corporateInternal.clickCancelInvoiceOption();

        step("Accept the cancellation confirmation checkbox");
        corporateInternal.acceptCancelInvoiceCheckbox();

        step("Confirm cancel invoice");
        corporateInternal.confirmCancelInvoice();

        step("Verify the invoice status is Cancelled");
        Assertion.assertThat("Invoice status should be Cancelled", corporateInternal.getInvoiceStatus(), containsString("Cancelled"));
    }

    @Author(names = {"Harshit Ranjan"})
    @Module(name = "Invoice & Billing")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test
    public void verifyOnlyIssuedOrderDetailIsShownWhileSelectingOrder() throws Exception {
        Map<String, String> param = getParam("verifyOnlyIssuedOrderDetailIsShownWhileSelectingOrder");
        BaseTest.paramsThreadLocal.set(param);
        Session<WebDriver> session = sessionThreadLocal.get();

        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        CorporateDataPage<?> corporateDataPage = new CorporateDataPage<>(session);
        CorporateInternalDashboardSidebar<?> corporateInternalDashboardSidebar = new CorporateInternalDashboardSidebar<>(session);
        TiketPlusForCorporateInternal<?> corporateInternal = new TiketPlusForCorporateInternal<>(session);
        OrderApi<?> orderApi = new OrderApi<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String company = param.get("company");
        String orderId = param.get("orderId");

        step("Get orderDetailId -> status map for order %s from the internal order detail endpoint".formatted(orderId));
        Map<String, String> orderDetailStatusById = orderApi.getOrderDetailStatusById(orderId);

        step("Assert that order %s has exactly 2 order details".formatted(orderId));
        Assertion.assertThat("Order " + orderId + " should have 2 order details", orderDetailStatusById.size(), is(2));

        step("Identify the issued and the non-issued orderDetailId from their statuses");
        String issuedOrderDetailId = null;
        String notIssuedOrderDetailId = null;
        for (Map.Entry<String, String> entry : orderDetailStatusById.entrySet()) {
            if (entry.getValue().toUpperCase().contains("ISSUED")) {
                issuedOrderDetailId = entry.getKey();
            } else {
                notIssuedOrderDetailId = entry.getKey();
            }
        }
        Assertion.assertThat("An issued orderDetailId should exist", issuedOrderDetailId, notNullValue());
        Assertion.assertThat("A non-issued orderDetailId should exist", notIssuedOrderDetailId, notNullValue());

        step("Login via cookie injection");
        loginMemberAdmin.loginViaCookie(email, password);

        step("Go to Corporate Internal Dashboard");
        loginMemberAdmin.goToDashboard();

        step("Check if user is logged in");
        Assertion.assertThat("User is logged in", corporateDataPage.checkUserIsLoggedIn(), is(true));

        step("Navigate to Invoice & Billing");
        corporateInternalDashboardSidebar.goToInvoiceAndBilling();

        step("Verify Invoice & Billing page is opened");
        Assertion.assertThat("Invoice & Billing page should be opened", corporateInternal.isInvoiceAndBillingPageOpened(), is(true));

        step("Click Create new invoice");
        corporateInternal.clickCreateNewInvoice();

        step("Search and select corporate");
        corporateInternal.selectCorporate(company);

        step("Select System Order invoice type");
        corporateInternal.selectSystemOrderInvoiceType();

        step("Continue to add orders");
        corporateInternal.clickContinue();

        step("Open the Add order modal");
        corporateInternal.clickAddOrder();

        step("Search the order %s in the Add order modal".formatted(orderId));
        corporateInternal.searchOrderInModal(orderId);

        step("Verify the issued orderDetailId %s is present in the row".formatted(issuedOrderDetailId));
        Assertion.assertThat("Issued orderDetailId " + issuedOrderDetailId + " should be present",
                corporateInternal.isOrderDetailIdPresentInRow(issuedOrderDetailId), is(true));

        step("Verify the non-issued orderDetailId %s is not present in the row".formatted(notIssuedOrderDetailId));
        Assertion.assertThat("Non-issued orderDetailId " + notIssuedOrderDetailId + " should not be present",
                corporateInternal.isOrderDetailIdPresentInRow(notIssuedOrderDetailId), is(false));
    }

    @Author(names = {"Prachi Upadhyay"})
    @Module(name = "MICE")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void verifyAmSupervisorCanCreateQuotationFromRequestList() throws Exception {
        Map<String, String> param = getParam("verifyAmSupervisorCanCreateQuotationFromRequestList");
        BaseTest.paramsThreadLocal.set(param);
        Session<WebDriver> session = sessionThreadLocal.get();

        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        CorporateInternalDashboardSidebar<?> corporateInternalDashboardSidebar = new CorporateInternalDashboardSidebar<>(session);
        TiketPlusForCorporateExternal<?> tiketPlusForCorporateExternal = new TiketPlusForCorporateExternal<>(session);
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        CorporateDataPage<?> corporateDataPage = new CorporateDataPage<>(session);
        TiketPlusForCorporateInternal<?> corporateInternal = new TiketPlusForCorporateInternal<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String eventType = param.get("eventType");
        String eventName = param.get("eventName") + " " + randomName();
        String participants = param.get("participateNum");
        String area = param.get("eventArea");
        String description = param.get("eventDesc");
        int startIdx = Integer.parseInt(param.get("startDate"));
        int endIdx = Integer.parseInt(param.get("endDate"));
        String productType = param.get("productType");
        String addReq = param.get("addReq");
        String firstPartName = param.get("firstPartName");
        String secondPartName = param.get("secondPartName");
        String idNumFirst = param.get("idNumFirst");
        String idNumSec = param.get("idNumSec");
        String travelApprovalNum = param.get("travelApprovalNum");
        int firstPartDobDay = Integer.parseInt(param.get("firstPartDOB"));
        int secPartDobDay = Integer.parseInt(param.get("secPartDOB"));
        String requestType = param.get("requestType");
        String requestorName = param.get("requestor");
        String snackbarMsg = param.get("snackbarMsg");

        String amSupervisorEmail = param.get("amSupervisorEmail");
        String amSupervisorPassword = param.get("amSupervisorPassword");
        String itemName = param.get("itemName");
        String itemDesc = param.get("itemDesc");
        String amount = param.get("amount");
        String quantity = param.get("quantity");
        String bankAccountNumber = param.get("bankAccountNumber");
        String internalSnackbarMsg = param.get("internalSnackbarMsg");
        String quotationCreatedSnackbarMsg = param.get("quotationCreatedSnackbarMsg");

        // ==============================================================================
        // PART 1: Create Request from External Dashboard
        // ==============================================================================
        step("Step 1: Login to External Corporate Dashboard and Navigate to External Corporate Dashboard");
        loginPage.loginWithSpecificAccountViaCookie(username, password);
        loginPage.navigateToExternalCorporateDashboard();

        step("Step 2: Expand tiketPlus for Corporate");
        corporateInternalDashboardSidebar.expandTiketPlusForCorporate();

        step("Step 3: Open the Request List and Click on the New Request Button");
        corporateInternalDashboardSidebar.openRequestList();
        tiketPlusForCorporateExternal.clickNewRequestButton();

        step("Step 4: Verify the New Request Page is Loaded with New Event Request and TakeOver Payment Options");
        tiketPlusForCorporateExternal.verifyNewRequestPageLoaded();

        step("Step 5: Click on the New Event Request Button and Verify New Booking Request Page Opened");
        tiketPlusForCorporateExternal.clickCreateEventRequestButton();
        tiketPlusForCorporateExternal.verifyNewBookingRequestPageOpened();

        step("Step 6: Fill in the Event Details");
        tiketPlusForCorporateExternal.selectEventType(eventType);
        tiketPlusForCorporateExternal.fillEventDetails(eventName, participants, area, description);
        tiketPlusForCorporateExternal.selectDateInNextMonth("event-start-date", startIdx);
        tiketPlusForCorporateExternal.selectDateInNextMonth("event-end-date", endIdx);
        String startDate = tiketPlusForCorporateExternal.getStartDate();
        String endDate = tiketPlusForCorporateExternal.getEndDate();

        step("Step 7: Click 'Next: Event Products' and verify the Event Product section opens");
        tiketPlusForCorporateExternal.clickNextEventProducts();
        tiketPlusForCorporateExternal.verifyEventProductsSectionOpens();

        step("Step 8: Select Product Types and fill Additional Requirements");
        tiketPlusForCorporateExternal.selectProductTypes(productType);
        tiketPlusForCorporateExternal.fillAdditionalRequirements(addReq);

        step("Step 9: Click on the Next Contact And Approval and verify Contact And Participant Section Opens");
        tiketPlusForCorporateExternal.clickNextContactAndApproval();
        tiketPlusForCorporateExternal.verifyContactAndParticipantSectionOpens();

        step("Step 10: Fill details for the first participant.");
        tiketPlusForCorporateExternal.fillSingleParticipant(firstPartName, idNumFirst, firstPartDobDay);
        String firstParticipantDOB = tiketPlusForCorporateExternal.getDOB();

        step("Step 11: Click 'Add Participant' button to save the first person");
        tiketPlusForCorporateExternal.clickAddParticipant();

        step("Step 12: Fill details for the second participant");
        tiketPlusForCorporateExternal.fillSingleParticipant(secondPartName, idNumSec, secPartDobDay);
        String secondParticipantDOB = tiketPlusForCorporateExternal.getDOB();

        step("Step 13: Click 'Add Participant' button to save the second person");
        tiketPlusForCorporateExternal.clickAddParticipant();

        step("Step 14: Fill the Additional Information section");
        tiketPlusForCorporateExternal.fillTravelApprovalNumber(travelApprovalNum);

        step("Step 15: Click 'Next: Review & Submit' and verify the page opens");
        tiketPlusForCorporateExternal.clickNextReviewAndSubmit();
        tiketPlusForCorporateExternal.verifyReviewAndSubmitPageOpens();

        step("Step 16: Verify all details on the summary page match the input data");
        tiketPlusForCorporateExternal.verifySummaryDetails(
                eventName, eventType, participants, area, description,
                productType, addReq,
                firstPartName, idNumFirst, firstParticipantDOB,
                secondPartName, idNumSec, secondParticipantDOB,
                travelApprovalNum
        );

        step("Step 17: Click the final Submit Request button and Verify the confirmation modal appears");
        tiketPlusForCorporateExternal.clickFinalSubmitRequestButton();
        tiketPlusForCorporateExternal.verifySubmitConfirmationModalAppears();

        step("Step 18: Click 'Submit request' to finalize the booking and Verify the Snackbar Message");
        tiketPlusForCorporateExternal.clickSubmitRequestConfirmation();
        tiketPlusForCorporateExternal.verifySnackbarAndWaitUntilDismissed(snackbarMsg);

        step("Step 19: Search for the newly created event in the Dashboard");
        tiketPlusForCorporateExternal.searchForEventInDashboard(eventName);

        step("Step 20: Verify the event details in the Dashboard table match the requested data");
        tiketPlusForCorporateExternal.verifyEventInDashboardTable(eventName, requestType, requestorName, startDate, endDate, area, participants);

        step("Step 21: Extract and Store Request Number for Internal Dashboard");
        String requestNumber = tiketPlusForCorporateExternal.getRequestNumber(eventName);
        Assertion.assertThat("Request Number should not be empty", requestNumber.isEmpty(), is(false));

        // ==============================================================================
        // PART 2: Create Quotation from Internal Dashboard
        // ==============================================================================
        step("Step 22: Login to internal dashboard by AM supervisor via cookie injection");
        loginMemberAdmin.loginViaCookie(amSupervisorEmail, amSupervisorPassword);

        step("Step 23: Go to Corporate Internal Dashboard");
        loginMemberAdmin.goToDashboard();

        step("Step 24: Check if user is logged in");
        Assertion.assertThat("User is logged in", corporateDataPage.checkUserIsLoggedIn(), is(true));

        step("Step 25: Click on tiketPlus for Corporate");
        corporateInternalDashboardSidebar.expandTiketPlusForCorporate();

        step("Step 26: Click on Request List");
        corporateInternalDashboardSidebar.openRequestList();

        step("Step 27: Search and verify the newly created request by Request Number");
        corporateInternal.searchByRequestNumber(requestNumber);
        boolean isRequestFound = corporateInternal.isRequestNumberVisibleInTable(requestNumber);
        Assertion.assertThat("Verified that the correct request number is present", isRequestFound, is(true));

        step("Step 28: Click on 3 dots (Action menu)");
        corporateInternal.clickRequestActionDots();

        step("Step 29: Click Start Progress from the dropdown menu");
        corporateInternal.clickStartProgressOption();

        step("Step 30: Confirm 'Start progress' in the popup modal");
        corporateInternal.confirmStartProgress();

        step("Step 31: Verify the success snackbar message appears at the bottom");
        corporateInternal.verifySnackbarAndWaitUntilDismissed(internalSnackbarMsg);

        step("Step 31a: Re-open the 3-dots action menu");
        corporateInternal.clickRequestActionDots();

        step("Step 31b: Click 'View Details' from the dropdown menu");
        corporateInternal.clickViewDetailsOption();

        step("Step 32: Click on create quotation");
        corporateInternal.clickCreateQuotation();

        step("Step 33: Hit Next: Quotation Details on the prefilled Basic Information page");
        corporateInternal.clickNextQuotationDetailsButton();

        step("Step 34: Fill Quotation Details");
        corporateInternal.setQuotationDetailsItemName(itemName);
        corporateInternal.setQuotationDetailsItemDescription(itemDesc);
        corporateInternal.setQuotationDetailsItemAmount(amount);
        corporateInternal.setQuotationDetailsItemQuantity(quantity);
        int daysToDepartureDate = 2;
        int daysToReturnDate = 5;
        String today = corporateInternal.getTodayDateFormatted();

        String departureDate = corporateInternal.selectQuotationStartDateForAmSupervisor(today, daysToDepartureDate);
        corporateInternal.selectQuotationEndDateForAmSupervisor(departureDate, daysToReturnDate);

        step("Step 35: Hit Next: Vendor Payment");
        corporateInternal.clickNextVendorPaymentButton();

        step("Step 36: Fill Vendor Payment Details");
        corporateInternal.selectVendorPaymentInventorySource("Tiket.com");
        corporateInternal.selectVendorPaymentCurrency();
        corporateInternal.selectVendorPaymentProductCategory();
        corporateInternal.selectVendorPaymentRegionalHotel();
        corporateInternal.selectVendorPaymentRevenueCategory();
        corporateInternal.selectVendorPaymentPaymentDueDateForAmSupervisor(today);
        corporateInternal.selectVendorPaymentVendorName();
        corporateInternal.selectVendorPaymentProductProvider();
        corporateInternal.selectVendorPaymentPaymentType();
        corporateInternal.selectVendorPaymentFirstVendorBankNameForAmSupervisor();
        corporateInternal.setVendorPaymentBankAccountNumber(bankAccountNumber);
        corporateInternal.clickVendorPaymentCheckAccount();
        Assertion.assertThat("Verify account", corporateInternal.getVendorPaymentAccountVerifyText(), containsStringIgnoringCase("account verified successfully"));

        step("Step 37: Hit Next: Review & Submit");
        corporateInternal.clickNextReviewAndSubmitButtonForAmSupervisor();

        step("Step 38: Hit Submit quotation");
        corporateInternal.clickSubmitQuotationButton();

        step("Step 39: Confirm Modal Submission");
        corporateInternal.clickModalSubmitButton();

        step("Step 40: Verify the success snackbar message for quotation creation");
        corporateInternal.verifySnackbarAndWaitUntilDismissed("You've created the quotation.");

        step("Step 41: Click the 3-dots action menu for the specific quotation in the list");
        corporateInternal.clickRequestActionDots();

        step("Step 41a: Click 'See quotation' from the dropdown menu");
        corporateInternal.clickSeeQuotationOption();

        step("Step 42: Verify Receiver Information on Details Page");
        Assertion.assertThat("Verify Receiver Request Number", corporateInternal.getReceiverRequestNumber(), containsString(requestNumber));
        Assertion.assertThat("Verify Receiver Name", corporateInternal.getReceiverName(), notNullValue());
        Assertion.assertThat("Verify Receiver Email", corporateInternal.getReceiverEmail(), containsStringIgnoringCase("@"));

        step("Step 43: Verify Itemized Quotation Details");
        Assertion.assertThat("Verify Item Name", corporateInternal.getQuotationItemName(), containsStringIgnoringCase(itemName));
        Assertion.assertThat("Verify Item Description", corporateInternal.getQuotationItemDescription(), containsStringIgnoringCase(itemDesc));
        String expectedTotalAmountFormatted = "IDR " + CoreFunctions.thousandSeparator(amount, ".");
        Assertion.assertThat("Verify Total Amount in breakdown", corporateInternal.getQuotationTotalAmount(), containsString(expectedTotalAmountFormatted));

        step("Step 44: Verify Activity Logs");
        Assertion.assertThat("Verify 'Quotation created' log exists", corporateInternal.isQuotationCreatedLogVisible(), is(true));
        Assertion.assertThat("Verify 'Quotation submitted' log exists", corporateInternal.isQuotationSubmittedLogVisible(), is(true));
    }
    
    public void verifyAmSupervisorCanSeeMiceOrderDetailsFromBookingManagementList() throws Exception {
        Map<String, String> param = getParam("verifyAmSupervisorCanSeeMiceOrderDetailsFromBookingManagementList");
        BaseTest.paramsThreadLocal.set(param);
        Session<WebDriver> session = sessionThreadLocal.get();

        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        CorporateDataPage<?> corporateDataPage = new CorporateDataPage<>(session);
        CorporateInternalDashboardSidebar<?> corporateInternalDashboardSidebar = new CorporateInternalDashboardSidebar<>(session);
        TiketPlusForCorporateInternal<?> corporateInternal = new TiketPlusForCorporateInternal<>(session);

        String email = param.get("amSupervisorEmail");
        String password = param.get("amSupervisorPassword");

        step("Login to internal dashboard by AM supervisor via cookie injection");
        loginMemberAdmin.loginViaCookie(email, password);

        step("Go to Corporate Internal Dashboard");
        loginMemberAdmin.goToDashboard();

        step("Check if user is logged in");
        Assertion.assertThat("User is logged in", corporateDataPage.checkUserIsLoggedIn(), is(true));

        step("Click on tiketPlus for Corporate");
        corporateInternalDashboardSidebar.expandTiketPlusForCorporate();

        step("Click on Booking Management");
        corporateInternalDashboardSidebar.openBookingManagement();

        step("Verify Booking Management page is opened");
        Assertion.assertThat("Booking Management page is opened", corporateInternal.isBookingManagementPageOpened(), is(true));

        step("Verify Order details all columns are present");
        Assertion.assertThat("All expected columns in Booking Management are present", corporateInternal.verifyBookingManagementColumns(), is(true));

        step("Verify order details are populated in the table and Quote Number contains 'GTNCORP'");
        Assertion.assertThat("Order details row is present with valid Quote Number format", corporateInternal.verifyOrderDetailsAndQuoteNumberFormat("GTNCORP"), is(true));

        step("Logout");
        loginMemberAdmin.logout();
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "Corporate Data")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test
    public void testAddSuperAdminFromInternalDashboard() throws Exception {
        Map<String, String> param = getParam("testAddSuperAdminFromInternalDashboard");
        BaseTest.paramsThreadLocal.set(param);
        Session<WebDriver> session = sessionThreadLocal.get();

        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        CorporateDataPage<?> corporateDataPage = new CorporateDataPage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String corporateName = param.get("corporateName");
        String superAdminRole = param.get("superAdminRole");
        String superAdminName = CoreFunctions.randomName();
        String superAdminEmail = superAdminName + "@yopmail.com";
        String superAdminPhone = CoreFunctions.randomPhone();

        step("Login to internal dashboard by AM supervisor via cookie injection");
        loginMemberAdmin.loginViaCookie(email, password);

        step("Go to Corporate Internal Dashboard");
        loginMemberAdmin.goToDashboard();

        step("Check if user is logged in");
        Assertion.assertThat("User is logged in", corporateDataPage.checkUserIsLoggedIn(), is(true));

        step("Go to Corporate Data page and search the corporate");
        corporateDataPage.clickCorporateInformation();
        corporateDataPage.clickCorporateData();

        step("USer enter corporate Deatils");
        corporateDataPage.inputSearch(corporateName);
        Assertion.assertThat("Searched corporate is listed", corporateDataPage.checkIfCorporateExists(corporateName), is(true));

        step("Go to corporate data detail page");
        corporateDataPage.clickSeeDetailsBtn();

        step("Click on Employee List tab");
        corporateDataPage.clickEmployeeList();

        step("Verify Add SuperAdmin button is present");
        Assertion.assertThat("Add super administrator button is present", corporateDataPage.isAddSuperAdminButtonPresent(), is(true));

        step("Click on Add SuperAdmin button");
        corporateDataPage.clickAddSuperAdminButton();

        step("Verify Add SuperAdmin popup appears");
        Assertion.assertThat("Add super administrator popup is displayed", corporateDataPage.isAddSuperAdminPopupDisplayed(), is(true));

        step("Verify Employee name field is present and fill it with " + superAdminName);
        Assertion.assertThat("Employee name field is present", corporateDataPage.isSuperAdminNamePresent(), is(true));
        corporateDataPage.fillSuperAdminName(superAdminName);

        step("Verify Employee email field is present and fill it with " + superAdminEmail);
        Assertion.assertThat("Employee email field is present", corporateDataPage.isSuperAdminEmailPresent(), is(true));
        corporateDataPage.fillSuperAdminEmail(superAdminEmail);

        step("Verify Phone number field is present and fill it with " + superAdminPhone);
        Assertion.assertThat("Phone number field is present", corporateDataPage.isSuperAdminPhoneNumberPresent(), is(true));
        corporateDataPage.fillSuperAdminPhoneNumber(superAdminPhone);

        step("Verify Department field is present");
        Assertion.assertThat("Department field is present", corporateDataPage.isSuperAdminDepartmentFieldPresent(), is(true));

        step("Verify Group field is present");
        Assertion.assertThat("Group field is present", corporateDataPage.isSuperAdminGroupFieldPresent(), is(true));

        step("Verify Travel policy field is present");
        Assertion.assertThat("Travel policy field is present", corporateDataPage.isSuperAdminTravelPolicyFieldPresent(), is(true));

        step("Click on Save");
        corporateDataPage.clickSaveSuperAdmin();

        step("Verify the success snackbar");
        Assertion.assertThat("Success snackbar is shown for the new super administrator",
                corporateDataPage.isSnackbarAppeared(superAdminName + " has been successfully added as a Super Administrator."), is(true));

        step("Verify the new SuperAdmin is on top of the employee list with the same name and email");
        Assertion.assertThat("Top employee name matches the added super administrator", corporateDataPage.getFirstEmployeeName(), is(superAdminName));
        Assertion.assertThat("Top employee email matches the added super administrator", corporateDataPage.getFirstEmployeeEmail(), equalToIgnoringCase(superAdminEmail));

        step("Verify the top employee role is Super administrator");
        Assertion.assertThat("Top employee role is Super administrator", corporateDataPage.getFirstEmployeeRole(), equalToIgnoringCase(superAdminRole));

        step("Click on the copy button of the new SuperAdmin registration link and store the link");
        String registrationLink = corporateDataPage.copyFirstEmployeeRegistrationLink();
        Assertion.assertThat("Registration link is copied", registrationLink, startsWith("http"));

        step("Verify the registration link copied snackbar");
        Assertion.assertThat("Registration link copied snackbar is shown", corporateDataPage.isSnackbarAppeared("Registration link copied to clipboard."), is(true));

        step("Logout");
        loginMemberAdmin.logout();

        step("Open the copied registration link via direct URL hit");
        corporateDataPage.openUrl(registrationLink);

        step("Verify the password field is displayed and fill it");
        Assertion.assertThat("Password field is displayed on the registration page", corporateDataPage.isRegisterPasswordFieldPresent(), is(true));
        corporateDataPage.fillRegisterPassword(password);

        step("Click on Register");
        corporateDataPage.clickRegister();

        step("Verify the account registration success message");
        Assertion.assertThat("Congrats message is displayed", corporateDataPage.isRegisterSuccessTitleDisplayed(), is(true));

        step("Click on OK");
        corporateDataPage.clickRegisterSuccessOk();

        step("Skip the passkey popup if it appears");
        LoginPageWeb.getInstance(session).skipPassKey();

        step("Verify the corporate dashboard is open");
        Assertion.assertThat("User is logged in to the corporate dashboard", corporateDataPage.checkUserIsLoggedIn(), is(true));
    }
}
