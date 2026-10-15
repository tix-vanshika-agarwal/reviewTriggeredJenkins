package com.tiket.web.dashboard.test.platform.sphinx;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.common.LoginMemberAdmin;
import com.tiket.web.dashboard.page.platform.sphinx.insurance.InsuranceDashboardHomePageWeb;
import com.tiket.web.dashboard.page.platform.sphinx.insurance.credential.CredentialPremiumPageWeb;
import com.tiket.web.dashboard.page.platform.sphinx.insurance.credential.InsuranceCredentialDashboardPageWeb;
import com.tiket.web.dashboard.page.platform.sphinx.insurance.insurance_report.InsuranceReportDashboardPageWeb;
import com.tiket.web.dashboard.page.platform.sphinx.insurance.product_design.CreateNewProductDesignWeb;
import com.tiket.web.dashboard.page.platform.sphinx.insurance.product_design.ProductDesignDashboardPageWeb;
import com.tiket.web.dashboard.page.platform.sphinx.insurance.product_design.SetOrderPageWeb;
import com.tiket.web.dashboard.page.platform.sphinx.insurance.simulation.CalculateSimulationPageWeb;
import com.tiket.web.dashboard.page.platform.sphinx.insurance.variable.VariableDashboardPageWeb;
import com.tiket.web.dashboard.page.member_dashboard.MemberLoginPage;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.is;

public class InsuranceTest extends BaseTest {


    @Author(names = {"Rizki Maulana"})
    @Module(name = "Simulation")
    @Tribe(name = "Insurance")
    @Vertical(name = "Platform")
    @Test
    public void testInsuranceCalculateSimulation() {
        Map<String, String> param = getParam("testInsuranceCalculateSimulation");
        Session<?> session = BaseTest.sessionThreadLocal.get();

        String username = param.get("username");
        String password = param.get("password");
        String vertical = param.get("vertical");
        String totalAmount = param.get("totalAmount");
        String[] variables = param.get("variables").split("-");
        String variableInputRaw = param.get("variableInput");

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        CalculateSimulationPageWeb<?> simulationPage = CalculateSimulationPageWeb.getInstance(session);
        simulationPage.setSheetData(param);

        step("Go to Insurance Simulation Dashboard");
        simulationPage.goToSimulationDashboard();

        step("Input necessary fields");
        simulationPage.inputVertical(vertical);
        simulationPage.inputTotalAmountOrder(totalAmount);
        simulationPage.removeUnnecessaryVariable(variables);
        simulationPage.inputNecessaryVariables(variableInputRaw);

        step("Click 'Simulate' button");
        simulationPage.clickCalculateSimulation();

        step("Assertions");
        Assertion.assertThat("User successfully simulate insurance", simulationPage.verifySimulation(), is(true));

        step("Log out");
        simulationPage.logout();
    }

    @Author(names = {"Rizki Maulana"})
    @Module(name = "Insurance Product Design")
    @Tribe(name = "Sphinx")
    @Vertical(name = "Platform")
    @Test
    public void testSetPrimaryActive() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testSetPrimaryActive");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");

        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<WebDriver>(session);
        InsuranceDashboardHomePageWeb<?> insuranceDashboardHomePageWeb = InsuranceDashboardHomePageWeb.getInstance(session);
        ProductDesignDashboardPageWeb<?> productDesignDashboardPageWeb = ProductDesignDashboardPageWeb.getInstance(session);
        SetOrderPageWeb<?> setOrderPageWeb = SetOrderPageWeb.getInstance(session);

        step("User has access to Insurance Dashboard");
        loginMemberAdmin.gotoHome();
        loginMemberAdmin.loginAsGuest();
        loginMemberAdmin.inputEmail(email);
        loginMemberAdmin.inputPassword(password);
        loginMemberAdmin.clickLogin();

        step("User is in Product Design Dashboard");
        loginMemberAdmin.goToInsuranceDashboard();
        insuranceDashboardHomePageWeb.clickSidebarMenu("Insurance Product Design");

        step("Click 'Set Order' button");
        productDesignDashboardPageWeb.clickSetOrderButton();

        step("Click 'Primary Inactive Button");
        String selectedProductDesign = setOrderPageWeb.setRandomProductDesignFromInactive();

        step("Click 'Save' button");
        setOrderPageWeb.clickSaveButton();

        step("Assertions");
        productDesignDashboardPageWeb.clickSetOrderButton();
        Assertion.assertThat("Button will be changed to \"Primary Active\" button", setOrderPageWeb.verifyHasConvertedIntoPrimaryActive(selectedProductDesign), is(true));

        step("Reset State");
        setOrderPageWeb.resetStateToPrimaryActive(selectedProductDesign);
    }

    @Author(names = {"Rizki Maulana"})
    @Module(name = "Insurance Product Design")
    @Tribe(name = "Sphinx")
    @Vertical(name = "Platform")
    @Test
    public void testCreateNewProductDesign() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testCreateNewProductDesign");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");
        String productDesignName = param.get("productDesignName");
        String verticalName = param.get("verticalName");
        String caseGroupName = param.get("caseGroupName");

        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<WebDriver>(session);
        InsuranceDashboardHomePageWeb<?> insuranceDashboardHomePageWeb = InsuranceDashboardHomePageWeb.getInstance(session);
        ProductDesignDashboardPageWeb<?> productDesignDashboardPageWeb = ProductDesignDashboardPageWeb.getInstance(session);
        CreateNewProductDesignWeb<?> createNewProductDesignWeb = CreateNewProductDesignWeb.getInstance(session);

        step("User has access to Insurance Dashboard");
        loginMemberAdmin.loginViaCookie(email, password);

        step("User is in Product Design Dashboard");
        loginMemberAdmin.goToInsuranceDashboard();
        insuranceDashboardHomePageWeb.clickSidebarMenu("Insurance Product Design");

        step("Click \"Create New Insurance Product\" button");
        productDesignDashboardPageWeb.clickCreateNewProductDesignButton();

        step("Input necessary fields");
        createNewProductDesignWeb.insertProductDesignName(productDesignName);
        createNewProductDesignWeb.selectTemplateName(caseGroupName);
        createNewProductDesignWeb.selectVertical(verticalName);
        createNewProductDesignWeb.clickCreateInsuranceNextButton();
        createNewProductDesignWeb.clickFillSubtemplateNextButton();

        step("Click \"Save\" button");
        createNewProductDesignWeb.clickManageSubtemplateSaveButton();

        step("Assertions");
        boolean result = productDesignDashboardPageWeb.validateProductDesignExistWithCorrectNecessaryData(productDesignName, caseGroupName, verticalName);
        Assertion.assertThat("User successfully create new product design", result, is(true));

        step("Reset State");
        productDesignDashboardPageWeb.deleteRecentlyCreatedProductDesign();
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "Insurance Variable")
    @Tribe(name = "Sphinx")
    @Vertical(name = "Platform")
    @Test
    public void testCreateNewVariable() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testCreateNewVariable");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");
        String name = param.get("name");
        String variableName = name + CoreFunctions.generateRandomInt(5);
        String parameter = param.get("parameter");
        String usage = param.get("usage");
        String inputType = param.get("inputType");
        String dataType = param.get("dataType");
        String arithmeticOption = param.get("arithmeticOption");


        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<WebDriver>(session);
        InsuranceDashboardHomePageWeb<?> insuranceDashboardHomePageWeb = InsuranceDashboardHomePageWeb.getInstance(session);
        VariableDashboardPageWeb<?> variableDashboardPageWeb = VariableDashboardPageWeb.getInstance(session);

        step("User has access to Insurance Dashboard");
        loginMemberAdmin.loginViaCookie(email, password);

        step("User Navigates to Variable Dashboard");
        loginMemberAdmin.goToInsuranceDashboard();
        insuranceDashboardHomePageWeb.clickSidebarMenu("Variable");

        step("User verify that Variable page is Opened");
        Assertion.assertThat("Variable page is opened", variableDashboardPageWeb.verifyVariablePageIsOpened(), is(true));

        step("User click 'Create New Variable' button");
        variableDashboardPageWeb.clickCreateNewVariableButton();

        step("User passes the variable name.");
        variableDashboardPageWeb.inputVariableName(variableName);

        step("User passes the variable parameter.");
        variableDashboardPageWeb.inputVariableParameter(parameter);

        step("User selects the variable usage.");
        variableDashboardPageWeb.selectVariableUsage(usage);

        step("User Selects the input type.");
        variableDashboardPageWeb.selectVariableInputType(inputType);

        step("User Selects the data type.");
        variableDashboardPageWeb.selectVariableDataType(dataType);

        step("User Selects the arithmetic type.");
        variableDashboardPageWeb.selectVariableArithmeticType(arithmeticOption);

        step("User clicks 'Submit Variable' button");
        variableDashboardPageWeb.clickSubmitButton();

        step("User verify that variable is successfully added");
        Assertion.assertThat("Variable is successfully added", variableDashboardPageWeb.verifySuccessSnackbarIsDisplayed(), is(true));

        step("User verify that variable is displayed in the list");
        Assertion.assertThat("Variable is displayed in the list", variableDashboardPageWeb.isNewVariableCreated(variableName), is(true));

        step("User change State to Default State");
        variableDashboardPageWeb.resetStateToDefault();

        step("User Logs out.");
        loginMemberAdmin.logout();
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "Insurance Variable")
    @Tribe(name = "Sphinx")
    @Vertical(name = "Platform")
    @Test
    public void testSetToggleOffStatusInVariableDashboard() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testSetToggleOffStatusInVariableDashboard");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");
        String name = param.get("name");
        String parameter = param.get("parameter");
        String usage = param.get("usage");
        String inputType = param.get("inputType");
        String dataType = param.get("dataType");
        String arithmeticOption = param.get("arithmeticOption");

        String variableName = name + CoreFunctions.generateRandomInt(5);

        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<WebDriver>(session);
        InsuranceDashboardHomePageWeb<?> insuranceDashboardHomePageWeb = InsuranceDashboardHomePageWeb.getInstance(session);
        VariableDashboardPageWeb<?> variableDashboardPageWeb = VariableDashboardPageWeb.getInstance(session);

        step("User has access to Insurance Dashboard");
        loginMemberAdmin.loginViaCookie(email, password);

        step("User Navigates to Variable Dashboard");
        loginMemberAdmin.goToInsuranceDashboard();
        insuranceDashboardHomePageWeb.clickSidebarMenu("Variable");

        step("User verify that Variable page is Opened");
        Assertion.assertThat("Variable page is opened", variableDashboardPageWeb.verifyVariablePageIsOpened(), is(true));

        step("User click 'Create New Variable' button");
        variableDashboardPageWeb.clickCreateNewVariableButton();

        step("User passes the variable name.");
        variableDashboardPageWeb.inputVariableName(variableName);

        step("User passes the variable parameter.");
        variableDashboardPageWeb.inputVariableParameter(parameter);

        step("User selects the variable usage.");
        variableDashboardPageWeb.selectVariableUsage(usage);

        step("User Selects the input type.");
        variableDashboardPageWeb.selectVariableInputType(inputType);

        step("User Selects the data type.");
        variableDashboardPageWeb.selectVariableDataType(dataType);

        step("User Selects the arithmetic type.");
        variableDashboardPageWeb.selectVariableArithmeticType(arithmeticOption);

        step("User clicks 'Submit Variable' button");
        variableDashboardPageWeb.clickSubmitButton();

        step("User verify that variable is successfully added");
        Assertion.assertThat("Variable is successfully added", variableDashboardPageWeb.verifySuccessSnackbarIsDisplayed(), is(true));

        step("User verify that variable is displayed in the list");
        Assertion.assertThat("Variable is displayed in the list", variableDashboardPageWeb.isNewVariableCreated(variableName), is(true));

        step("User clicks on the toggle button to set it to OFF status");
        variableDashboardPageWeb.makeToggleButtonON();
        variableDashboardPageWeb.makeToggleButtonOFF();

        step("User verify that variable is inactivated");
        Assertion.assertThat("Variable is inactivated", variableDashboardPageWeb.isToggleButtonOFF(), is(true));

        step("User change State to Default State");
        variableDashboardPageWeb.resetStateToDefault();

        step("User Logs out.");
        loginMemberAdmin.logout();
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "Insurance Report")
    @Tribe(name = "Sphinx")
    @Vertical(name = "Platform")
    @Test
    public void testDownloadReportFromReportDashboard() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testDownloadReportFromReportDashboard");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");
        String vertical = param.get("vertical");
        String partnerName = param.get("partnerName");


        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<WebDriver>(session);
        InsuranceDashboardHomePageWeb<?> insuranceDashboardHomePageWeb = InsuranceDashboardHomePageWeb.getInstance(session);
        InsuranceReportDashboardPageWeb<?> insuranceReportDashboardPageWeb = InsuranceReportDashboardPageWeb.getInstance(session);

        step("User has access to Insurance Dashboard");
        loginMemberAdmin.loginViaCookie(email, password);

        step("User Navigates to Insurance Report Page");
        loginMemberAdmin.goToInsuranceDashboard();
        insuranceDashboardHomePageWeb.clickSidebarMenu("Insurance Report");

        step("User verify that Insurance page is Opened");
        Assertion.assertThat("Insurance Report page is opened", insuranceReportDashboardPageWeb.verifyInsurancePageIsOpened(), is(true));

        step("User clicks on the Download button");
        insuranceReportDashboardPageWeb.clickDownloadButton();

        step("User verify that Report Download Page is opened");
        Assertion.assertThat("Report Download Page is opened", insuranceReportDashboardPageWeb.isReportDownloadPageOpened(), is(true));

        step("User inputs the date");
        insuranceReportDashboardPageWeb.inputDate();

        step("User verifies that Start Date is before End Date");
        Assertion.assertThat("Start Date is before End Date", insuranceReportDashboardPageWeb.isStartDateBeforeEndDate(), is(true));

        step("User Enters Flight as vertical.");
        insuranceReportDashboardPageWeb.selectVerticalProduct(vertical);

        step("User selects the partner name from dropdown.");
        insuranceReportDashboardPageWeb.selectPartnerName(partnerName);

        step("User clicks on the Download Report button");
        insuranceReportDashboardPageWeb.clickDownloadReportButton();

        step("User Verifies that the Report Downloaded.");
        Assertion.assertThat("Report Downloading is started.", insuranceReportDashboardPageWeb.isExportCompleted(), is(true));

        step("User Logs out.");
        loginMemberAdmin.logout();
    }

    @Author(names = {"Ankit Raj"})
    @Module(name = "Insurance Credential")
    @Tribe(name = "Sphinx")
    @Vertical(name = "Platform")
    @Test
    public void testSetToggleOnCredentialStatus() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testSetToggleOnCredentialStatus");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");

        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<WebDriver>(session);
        InsuranceDashboardHomePageWeb<?> insuranceDashboardHomePageWeb = InsuranceDashboardHomePageWeb.getInstance(session);
        InsuranceCredentialDashboardPageWeb<?> credentialDashboardPageWeb = InsuranceCredentialDashboardPageWeb.getInstance(session);

        step("User has access to Insurance Dashboard");
        loginMemberAdmin.loginViaCookie(email, password);

        step("User is in Credential Dashboard");
        loginMemberAdmin.goToInsuranceDashboard();
        insuranceDashboardHomePageWeb.clickSidebarMenu("Insurance Credential & Premium");

        step("User click chevron down to expand credential group");
        credentialDashboardPageWeb.clickExpandChevronSecond();

        step("User click toggle on Status on 1 of credential");
        credentialDashboardPageWeb.toggleCredentialStatusToOn();

        step("Appear pop up confirmation to activate the Status");
        Assertion.assertThat("Confirmation popup appears", credentialDashboardPageWeb.isConfirmationPopupVisible(), is(true));

        // Click the activate/confirm button in the popup
        credentialDashboardPageWeb.clickActivateButton();

        step("Credential will be activated");
        Assertion.assertThat("Credential is activated", credentialDashboardPageWeb.verifyCredentialIsActivated(), is(true));

        step("User Logs out");
        loginMemberAdmin.logout();
    }

    @Author(names = {"Ankit Raj"})
    @Module(name = "Insurance Credential")
    @Tribe(name = "Sphinx")
    @Vertical(name = "Platform")
    @Test
    public void testCreateNewPremium() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testCreateNewPremium");
        BaseTest.paramsThreadLocal.set(param);

        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<WebDriver>(session);
        InsuranceDashboardHomePageWeb<?> insuranceDashboardHomePageWeb = InsuranceDashboardHomePageWeb.getInstance(session);
        CredentialPremiumPageWeb<?> credentialPremiumPageWeb = CredentialPremiumPageWeb.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");
        String insuranceName = param.get("insuranceName");
        String premiumName = "Premium_" + System.currentTimeMillis();
        String formula = param.get("formula");
        String currency = param.get("currency");
        String uom = param.get("uom");
        String uomDiscount = param.get("uomDiscount");
        String uomDiscountAmount = param.get("uomDiscountAmount");
        String brokerFee = param.get("brokerFee");
        String commissionFee = param.get("commissionFee");
        String adminFee = param.get("adminFee");
        String groupRuleName = param.get("groupRuleName");
        String verticalProduct = param.get("verticalProduct");
        String variableName = param.get("variableName");
        String operator = param.get("operator");
        String value = param.get("value");

        step("User has access to Insurance Dashboard");
        loginMemberAdmin.loginViaCookie(email, password);

        step("User is in Credential Dashboard");
        loginMemberAdmin.goToInsuranceDashboard();
        insuranceDashboardHomePageWeb.clickSidebarMenu("Insurance Credential & Premium");

        step("User filter by insurance name and click search");
        credentialPremiumPageWeb.filterByInsuranceName(insuranceName);

        step("User click chevron icon on 1 of credential group");
        credentialPremiumPageWeb.clickChevronExpand();

        step("User click More button");
        credentialPremiumPageWeb.clickMoreButton();

        step("User click Edit button");
        credentialPremiumPageWeb.clickEditButton();

        step("User is redirected to Edit page");
        boolean isOnEditPage = credentialPremiumPageWeb.isOnEditPage();
        Assertion.assertThat("User is on edit page", isOnEditPage, is(true));

        step("User click Add new premium");
        credentialPremiumPageWeb.clickAddNewPremium();

        step("User input all the necessary fields");
        credentialPremiumPageWeb.fillAllMandatoryFields(premiumName, formula, currency, uom, uomDiscount,uomDiscountAmount, brokerFee, commissionFee, adminFee);

        step("User input Group Rules details");
        credentialPremiumPageWeb.fillGroupRules(groupRuleName, verticalProduct, variableName, operator, value);

        step("User click Next button");
        credentialPremiumPageWeb.clickNextButton();

        step("Click confirm button");
        credentialPremiumPageWeb.clickConfirmButton();

        step("User click Save button");
        credentialPremiumPageWeb.clickSaveButton();

        step("User click chevron icon on 1 of credential group");
        credentialPremiumPageWeb.clickChevronExpand();

        step("User click More button");
        credentialPremiumPageWeb.clickMoreButton();

        step("User click Edit button");
        credentialPremiumPageWeb.clickEditButton();

        step("User verify that premium is created successfully");
        boolean isPremiumCreated = credentialPremiumPageWeb.isPremiumCreated(premiumName);
        Assertion.assertThat("Premium is created successfully", isPremiumCreated, is(true));

        step("User delete the premium");
        credentialPremiumPageWeb.deletePremium();

        step("User saves changes");
        credentialPremiumPageWeb.clickSaveButton();

        step("User click chevron icon on 1 of credential group");
        credentialPremiumPageWeb.clickChevronExpand();

        step("User click More button");
        credentialPremiumPageWeb.clickMoreButton();

        step("User click Edit button");
        credentialPremiumPageWeb.clickEditButton();

        step("User verify that premium is deleted successfully");
        boolean isPremiumDeleted = credentialPremiumPageWeb.verifyPremiumDeleted(premiumName);
        Assertion.assertThat("Premium is deleted successfully", isPremiumDeleted, is(true));

        step("User Logs out");
        loginMemberAdmin.logout();
    }

    @Author(names = {"Ankit Raj"})
    @Module(name = "Insurance Credential")
    @Tribe(name = "Sphinx")
    @Vertical(name = "Platform")
    @Test
    public void testSearchByCredentialNameAndCheckProductDesignDetails() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testSearchByCredentialNameAndCheckProductDesignDetails");
        BaseTest.paramsThreadLocal.set(param);

        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<WebDriver>(session);
        InsuranceDashboardHomePageWeb<?> insuranceDashboardHomePageWeb = InsuranceDashboardHomePageWeb.getInstance(session);
        ProductDesignDashboardPageWeb<?> productDesignDashboardPageWeb = ProductDesignDashboardPageWeb.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");
        String credentialName = param.get("credentialName");

        step("User has access to Insurance Dashboard");
        loginMemberAdmin.loginViaCookie(email, password);

        step("User is in Credential Dashboard");
        loginMemberAdmin.goToInsuranceDashboard();
        insuranceDashboardHomePageWeb.clickSidebarMenu("Insurance Product Design");

        step("User filter by insurance name and click search");
        productDesignDashboardPageWeb.inputCredentialName(credentialName);

        step("User verifies that all expanded rows contain the credential name");
        boolean allRowsContainCredentialName = productDesignDashboardPageWeb.verifyAllExpandedRowsContainCredentialName(credentialName);
        Assertion.assertThat("All expanded rows contain the credential name", allRowsContainCredentialName, is(true));

        step("User Logs out");
        loginMemberAdmin.logout();
    }

}
