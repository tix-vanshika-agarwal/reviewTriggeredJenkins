package com.tiket.web.dashboard.test.pricing.SubsidyBudgeting;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.common.LoginMemberAdmin;
import com.tiket.web.dashboard.page.member_dashboard.MemberLoginPage;
import com.tiket.web.dashboard.page.member_dashboard.MemberPage;
import com.tiket.web.dashboard.page.member_dashboard.PricingDashboardHome;

import com.tiket.web.dashboard.page.platform.pricing.PricingDashboardPageWeb;
import com.tiket.web.dashboard.page.pricing.MemberLoginPagePricing.MemberLoginPagePricingPage;
import com.tiket.web.dashboard.page.pricing.PriceAdjustmentRules.PriceAdjustmentRulesPage;
import com.tiket.web.dashboard.page.pricing.SubsidyBudgeting.SubsidyBudgetingPage;
import org.hamcrest.CoreMatchers;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.core.Is.is;

public class SubsidyBudgetingTest extends BaseTest {
    @Author(names = {"Rounit"})
    @Module(name = "SubsidyBudgeting")
    @Tribe(name = "Poseidon")
    @Vertical(name = "Pricing")
    @Test
    public void verifyFilterBudgetStatusACTIVE() {
        Map<String, String> param = getParam("verifyFilterBudgetStatusACTIVE");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        MemberPage<?> memberPage = new MemberPage<>(session);
        PricingDashboardHome<?> pricingDashboardHome = new PricingDashboardHome<>(session);
        SubsidyBudgetingPage<?> subsidyBudgetingPage = new SubsidyBudgetingPage<>(session);

        String budgetName = param.get("budgetName");
        String budgetStatus = param.get("budgetStatus");

        if (!pricingDashboardHome.isPricingDashboardLoaded()) {
            String username = param.get("username");
            String password = param.get("password");

            step("Login via cookie injection");
            memberLoginPage.adminLoginViaCookie(username, password);

            step("Verify Member Page is Loaded or not");
            Assertion.assertThat("Member page is loaded", memberPage.isMemberDashboardLoaded(), is(true));

            step("Click to open Pricing Dashboard");
            memberPage.goToPricingDashboard(username, password);
            Assertion.assertThat("Pricing Dashboard is loaded", pricingDashboardHome.isPricingDashboardLoaded(), is(true));
        }

        step("Open Subsidy Budgeting Section");
        subsidyBudgetingPage.clickSubsidyBudgetingSidebarButton();
        Assertion.assertThat("Subsidy Budgeting is loaded", subsidyBudgetingPage.isSubsidyBudgetingPageLoaded(), is(true));

        step("Click on Show Filter Button");
        subsidyBudgetingPage.clickOnShowFilterButton();

        step("Click on Status Filter Button");
        subsidyBudgetingPage.clickSubsidyBudgetingStatusFilter();

        step("Click on Active Status Checkbox Filter");
        subsidyBudgetingPage.clickSubsidyBudgetActiveStatusCheckbox();

        step("Click the Search button");
        subsidyBudgetingPage.clickSearchButton();


        step("Verify filter budget status is ACTIVE");
        Assertion.assertThat("Verify filter budget status is ACTIVE", subsidyBudgetingPage.verifyFilterActiveBudgetStatus(), is(true));

    }

    @Author(names = {"Rounit"})
    @Module(name = "SubsidyBudgeting")
    @Tribe(name = "Poseidon")
    @Vertical(name = "Pricing")
    @Test
    public void verifySearchForInvalidBudgetName() {
        Map<String, String> param = getParam("verifySearchForInvalidBudgetName");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        MemberPage<?> memberPage = new MemberPage<>(session);
        PricingDashboardHome<?> pricingDashboardHome = new PricingDashboardHome<>(session);
        SubsidyBudgetingPage<?> subsidyBudgetingPage = new SubsidyBudgetingPage<>(session);

        String invalidBudgetName = param.get("invalidBudgetName");

        if (!pricingDashboardHome.isPricingDashboardLoaded()) {
            String username = param.get("username");
            String password = param.get("password");

            step("Login via cookie injection");
            memberLoginPage.adminLoginViaCookie(username, password);

            step("Verify Member Page is Loaded or not");
            Assertion.assertThat("Member page is loaded", memberPage.isMemberDashboardLoaded(), is(true));

            step("Click to open Pricing Dashboard");
            memberPage.goToPricingDashboard(username, password);
            Assertion.assertThat("Pricing Dashboard is loaded", pricingDashboardHome.isPricingDashboardLoaded(), is(true));
        }

        step("Open Subsidy Budgeting Section");
        subsidyBudgetingPage.clickSubsidyBudgetingSidebarButton();
        Assertion.assertThat("Subsidy Budgeting is loaded", subsidyBudgetingPage.isSubsidyBudgetingPageLoaded(), is(true));

        step("Click on Show Filter Button");
        subsidyBudgetingPage.clickOnShowFilterButton();

        step("Click on Search Budget Name");
        subsidyBudgetingPage.clickOnSearchBudgetName();

        step("Enter Invalid Budget Name");
        subsidyBudgetingPage.enterInvalidBudgetName(invalidBudgetName);

        step("Click the Search button");
        subsidyBudgetingPage.clickSearchButton();

        step("Verify saerch budget by invalid Budget Name");
        Assertion.assertThat("Verify search budget by invalid Budget Name", subsidyBudgetingPage.verifyNoResultResetFilterOption(), is(true));
    }

    @Author(names = {"Rounit"})
    @Module(name = "SubsidyBudgeting")
    @Tribe(name = "Poseidon")
    @Vertical(name = "Pricing")
    @Test
    public void verifyUserWithPrivilegeCanAccessEditPage() {
        Map<String, String> param = getParam("verifyUserWithPrivilegeCanAccessEditPage");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        MemberPage<?> memberPage = new MemberPage<>(session);
        PricingDashboardHome<?> pricingDashboardHome = new PricingDashboardHome<>(session);
        SubsidyBudgetingPage<?> subsidyBudgetingPage = new SubsidyBudgetingPage<>(session);

        if (!pricingDashboardHome.isPricingDashboardLoaded()) {
            String username = param.get("username");
            String password = param.get("password");

            step("Login via cookie injection");
            memberLoginPage.adminLoginViaCookie(username, password);

            step("Verify Member Page is Loaded or not");
            Assertion.assertThat("Member page is loaded", memberPage.isMemberDashboardLoaded(), is(true));

            step("Click to open Pricing Dashboard");
            memberPage.goToPricingDashboard(username, password);
            Assertion.assertThat("Pricing Dashboard is loaded", pricingDashboardHome.isPricingDashboardLoaded(), is(true));
        }

        step("Open Subsidy Budgeting Section");
        subsidyBudgetingPage.clickSubsidyBudgetingSidebarButton();
        Assertion.assertThat("Subsidy Budgeting is loaded", subsidyBudgetingPage.isSubsidyBudgetingPageLoaded(), is(true));

        step("Click on Show Filter Button");
        subsidyBudgetingPage.clickOnShowFilterButton();

        step("Click on Settings button");
        subsidyBudgetingPage.clickOnSettingsButton();

        step("Click on Edit button");
        subsidyBudgetingPage.clickOnEditButton();

        step("Verify user with privileges can access the Update Subsidy Budgeting dashboard");
        Assertion.assertThat("Verify user with privileges can access the Update Subsidy Budgeting dashboard", subsidyBudgetingPage.isUpdateSubsidyBudgetingPageLoaded(), is(true));
    }

    @Author(names = {"Tasya"})
    @Module(name = "SubsidyBudgeting")
    @Tribe(name = "Hades")
    @Vertical(name = "Pricing")
    @Test
    public void testSubsidyBudgetFilerByVertical() {
        Map<String, String> param = getParam("testSubsidyBudgetFilerByVertical");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        MemberPage<?> memberPage = new MemberPage<>(session);
        PricingDashboardHome<?> pricingDashboardHome = new PricingDashboardHome<>(session);
        SubsidyBudgetingPage<?> subsidyBudgetingPage = new SubsidyBudgetingPage<>(session);

        String verticalName = param.get("verticalName");

        if (!pricingDashboardHome.isPricingDashboardLoaded()) {
            String username = param.get("username");
            String password = param.get("password");

            step("Login via cookie injection");
            memberLoginPage.adminLoginViaCookie(username, password);

            step("Verify Member Page is Loaded or not");
            Assertion.assertThat("Member page is loaded", memberPage.isMemberDashboardLoaded(), is(true));

            step("Click to open Pricing Dashboard");
            memberPage.goToPricingDashboard(username, password);
            Assertion.assertThat("Pricing Dashboard is loaded", pricingDashboardHome.isPricingDashboardLoaded(), is(true));
        }

        step("Open Subsidy Budgeting Section");
        subsidyBudgetingPage.clickSubsidyBudgetingSidebarButton();
        Assertion.assertThat("Subsidy Budgeting is loaded", subsidyBudgetingPage.isSubsidyBudgetingPageLoaded(), is(true));

        step("Click on Show Filter Button");
        subsidyBudgetingPage.clickOnShowFilterButton();

        step("Select vertical " + verticalName + " from dropdown");
        subsidyBudgetingPage.selectVerticalAndCloseDropdown(verticalName);

        step("Click the Search button");
        subsidyBudgetingPage.clickSearchButton();

        step("Verify only " + verticalName + " vertical appears in results");
        Assertion.assertThat("Verify filter by vertical is correct", subsidyBudgetingPage.verifyFilterResult(verticalName), is(true));
    }


    @Author(names = {"Rounit", "Tasya"})
    @Module(name = "SubsidyBudgeting")
    @Tribe(name = "Hades")
    @Vertical(name = "Pricing")
    @Test
    public void verifyFilterBudgetStatusINACTIVE() {
        Map<String, String> param = getParam("verifyFilterBudgetStatusINACTIVE");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        MemberPage<?> memberPage = new MemberPage<>(session);
        PricingDashboardHome<?> pricingDashboardHome = new PricingDashboardHome<>(session);
        SubsidyBudgetingPage<?> subsidyBudgetingPage = new SubsidyBudgetingPage<>(session);

        String budgetName = param.get("budgetName");

        if (!pricingDashboardHome.isPricingDashboardLoaded()) {
            String username = param.get("username");
            String password = param.get("password");

            step("Login via cookie injection");
            memberLoginPage.adminLoginViaCookie(username, password);

            step("Verify Member Page is Loaded or not");
            Assertion.assertThat("Member page is loaded", memberPage.isMemberDashboardLoaded(), is(true));

            step("Click to open Pricing Dashboard");
            memberPage.goToPricingDashboard(username, password);
            Assertion.assertThat("Pricing Dashboard is loaded", pricingDashboardHome.isPricingDashboardLoaded(), is(true));
        }

        step("Open Subsidy Budgeting Section");
        subsidyBudgetingPage.clickSubsidyBudgetingSidebarButton();
        Assertion.assertThat("Subsidy Budgeting is loaded", subsidyBudgetingPage.isSubsidyBudgetingPageLoaded(), is(true));

        step("Click on Show Filter Button");
        subsidyBudgetingPage.clickOnShowFilterButton();

        step("Click on Status input field");
        subsidyBudgetingPage.clickStatusInputField();

        step("Select INACTIVE option from dropdown");
        subsidyBudgetingPage.selectInactiveOption();

        step("Click the Search button");
        subsidyBudgetingPage.clickSearchButton();

        step("Verify filter budget status is INACTIVE by Budget Status");
        Assertion.assertThat("Verify filter budget status is INACTIVE by Budget Status", subsidyBudgetingPage.verifyInactiveSearchResult(), is(true));
    }

    @Author(names = {"Muksal"})
    @Module(name = "SubsidyBudgeting")
    @Tribe(name = "Hades")
    @Vertical(name = "Pricing")
    @Test
    public void verifySearchBudgetName() {
        Map<String, String> param = getParam("verifySearchBudgetName");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        MemberPage<?> memberPage = new MemberPage<>(session);
        PricingDashboardHome<?> pricingDashboardHome = new PricingDashboardHome<>(session);
        SubsidyBudgetingPage<?> subsidyBudgetingPage = new SubsidyBudgetingPage<>(session);

        String budgetName = param.get("budgetName");

        if (!pricingDashboardHome.isPricingDashboardLoaded()) {
            String username = param.get("username");
            String password = param.get("password");

            step("Login via cookie injection");
            memberLoginPage.adminLoginViaCookie(username, password);

            step("Verify Member Page is Loaded or not");
            Assertion.assertThat("Member page is loaded", memberPage.isMemberDashboardLoaded(), is(true));

            step("Click to open Pricing Dashboard");
            memberPage.goToPricingDashboard(username, password);
            Assertion.assertThat("Pricing Dashboard is loaded", pricingDashboardHome.isPricingDashboardLoaded(), is(true));
        }

        step("Open Subsidy Budgeting Section");
        subsidyBudgetingPage.clickSubsidyBudgetingSidebarButton();
        Assertion.assertThat("Subsidy Budgeting is loaded", subsidyBudgetingPage.isSubsidyBudgetingPageLoaded(), is(true));

        step("Click on Show Filter Button");
        subsidyBudgetingPage.clickOnShowFilterButton();

        step("Click and fill the budget name value on Search by Budget name input field");
        subsidyBudgetingPage.searchByByBudgetName(budgetName);

        step("Click the Search button");
        subsidyBudgetingPage.clickSearchButton();

        step("Verify the budget rule user search");
        Assertion.assertThat("Verify the budget rule user search", subsidyBudgetingPage.verifySearchBudgedNameResult(budgetName), is(true));
    }

    @Author(names = {"Rounit", "Tasya"})
    @Module(name = "SubsidyBudgeting")
    @Tribe(name = "Poseidon")
    @Vertical(name = "Pricing")
    @Test
    public void verifyUpdateSubsidyBudStatusToActive() {
        Map<String, String> param = getParam("verifyUpdateSubsidyBudStatusToActive");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        MemberPage<?> memberPage = new MemberPage<>(session);
        PricingDashboardHome<?> pricingDashboardHome = new PricingDashboardHome<>(session);
        SubsidyBudgetingPage<?> subsidyBudgetingPage = new SubsidyBudgetingPage<>(session);

        String message = param.get("message");
        String message2 = param.get("message2");
        String revertMessage1 = param.get("revertMessage1");
        String revertMessage2 = param.get("revertMessage2");

        if (!pricingDashboardHome.isPricingDashboardLoaded()) {
            String username = param.get("username");
            String password = param.get("password");

            step("Login via cookie injection");
            memberLoginPage.adminLoginViaCookie(username, password);

            step("Verify Member Page is Loaded or not");
            Assertion.assertThat("Member page is loaded", memberPage.isMemberDashboardLoaded(), is(true));

            step("Click to open Pricing Dashboard");
            memberPage.goToPricingDashboard(username, password);
            Assertion.assertThat("Pricing Dashboard is loaded", pricingDashboardHome.isPricingDashboardLoaded(), is(true));
        }

        step("Open Subsidy Budgeting Section");
        subsidyBudgetingPage.clickSubsidyBudgetingSidebarButton();
        Assertion.assertThat("Subsidy Budgeting is loaded", subsidyBudgetingPage.isSubsidyBudgetingPageLoaded(), is(true));

        step("Click on Show Filter Button");
        subsidyBudgetingPage.clickOnShowFilterButton();

        step("Click on Status input field");
        subsidyBudgetingPage.clickStatusInputField();

        step("Select INACTIVE option from dropdown");
        subsidyBudgetingPage.selectInactiveOption();

        step("Click the Search button");
        subsidyBudgetingPage.clickSearchButton();

        step("Select Second Budget");
        subsidyBudgetingPage.selectSecondInactiveBudget();

        step("Click the Radio Icon for ACTIVE status");
        subsidyBudgetingPage.clickRadioButtonOfActiveOption();

        step("Click the Yes button on the Pop - up");
        subsidyBudgetingPage.clickYesButton();

        step("Verify the status is updated or not");
        Assertion.assertThat("Verify the status is updated or not", subsidyBudgetingPage.verifyThePopUpMessage(message, message2), is(true));

        step("Revert the changed budget");
        subsidyBudgetingPage.clickTheResetFilterButton();
        subsidyBudgetingPage.selectFirstActiveBud();
        subsidyBudgetingPage.clickRadioButtonOfInActiveOption();
        Assertion.assertThat("Verify the status is reverted successfully", subsidyBudgetingPage.verifyThePopUpRevertMessage(revertMessage1, revertMessage2), is(true));

        step("Close Pop Up");
        subsidyBudgetingPage.closePopupIfPresent();

    }

    @Author(names = {"Rounit"})
    @Module(name = "SubsidyBudgeting")
    @Tribe(name = "Poseidon")
    @Vertical(name = "Pricing")
    @Test
    public void verifyValueOfTiketBudAndVendorBudIsGreaterThanZero() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyValueOfTiketBudAndVendorBudIsGreaterThanZero");
        BaseTest.paramsThreadLocal.set(param);

        MemberLoginPagePricingPage memberLoginHelper = new MemberLoginPagePricingPage();
        SubsidyBudgetingPage<?> subsidyBudgetingPage = new SubsidyBudgetingPage<>(session);

        memberLoginHelper.ensurePricingDashboardIsLoaded(param);
        String ruleName = param.get("ruleName");

        String day = param.get("day");

        step("Open Subsidy Budgeting Section");
        subsidyBudgetingPage.clickSubsidyBudgetingSidebarButton();
        Assertion.assertThat("Subsidy Budgeting is loaded", subsidyBudgetingPage.isSubsidyBudgetingPageLoaded(), is(true));

        step("Click on Show Filter Button");
        subsidyBudgetingPage.clickOnShowFilterButton();

        step("Enter the Rule name");
        subsidyBudgetingPage.clickSubsidyBudgetRuleNameField(ruleName);

        step("Click the Search button");
        subsidyBudgetingPage.clickSearchButton();


        step("Verify the multiple day period value");
        Assertion.assertThat("Verify the multiple day period value", subsidyBudgetingPage.verifyMultipleDayPeriodValue(day), is(true));

        step("Verify value of tiket Budget and Vendor Budget when period filled is multiple day");
        Assertion.assertThat("Verify value of tiket Budget and Vendor Budget when period filled is multiple day", subsidyBudgetingPage.verifyTiketBudVendorBudIsGreaterThanZero(), is(true));
    }

    @Author(names = {"Rounit"})
    @Module(name = "SubsidyBudgeting")
    @Tribe(name = "Poseidon")
    @Vertical(name = "Pricing")
    @Test
    public void verifyUpdateSubsidyBudStatusToInactive() {
        Map<String, String> param = getParam("verifyUpdateSubsidyBudStatusToInactive");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPagePricingPage memberLoginHelper = new MemberLoginPagePricingPage();
        SubsidyBudgetingPage<?> subsidyBudgetingPage = new SubsidyBudgetingPage<>(session);

        memberLoginHelper.ensurePricingDashboardIsLoaded(param);

        String message = param.get("message");
        String message2 = param.get("message2");
        String revertMessage1 = param.get("revertMessage1");
        String revertMessage2 = param.get("revertMessage2");

        step("Open Subsidy Budgeting Section");
        subsidyBudgetingPage.clickSubsidyBudgetingSidebarButton();
        Assertion.assertThat("Subsidy Budgeting is loaded", subsidyBudgetingPage.isSubsidyBudgetingPageLoaded(), is(true));

        step("Click on Show Filter Button");
        subsidyBudgetingPage.clickOnShowFilterButton();

        step("Click on Status Filter Button");
        subsidyBudgetingPage.clickSubsidyBudgetingStatusFilter();

        step("Click on Active Status Checkbox Filter");
        subsidyBudgetingPage.clickSubsidyBudgetActiveStatusCheckbox();

        step("Click the Search button");
        subsidyBudgetingPage.clickSearchButton();

        step("Select Third Active Budget");
        subsidyBudgetingPage.selectThirdActiveBud();

        step("Click the Radio Icon for INACTIVE status");
        subsidyBudgetingPage.clickRadioButtonOfInActiveOption();

        step("Click the Yes button on the Pop - up");
        subsidyBudgetingPage.clickYesButton();

        step("Verify the status is updated or not");
        Assertion.assertThat("Verify the status is updated or not", subsidyBudgetingPage.verifyThePopUpMessage(message, message2), is(true));

        step("Revert the changed Budget");
        subsidyBudgetingPage.wrapperFunction();
        Assertion.assertThat("Verify the status is reverted successfully", subsidyBudgetingPage.verifyThePopUpRevertMessage(revertMessage1, revertMessage2), is(true));
    }

    @Author(names = {"Muksal"})
    @Module(name = "SubsidyBudgeting")
    @Tribe(name = "Hades")
    @Vertical(name = "Pricing")
    @Test
    public void verifyWithPrivilegeCanAccessSBD() {
        Map<String, String> param = getParam("verifyWithPrivilegeCanAccessSBD");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        MemberPage<?> memberPage = new MemberPage<>(session);
        PricingDashboardHome<?> pricingDashboardHome = new PricingDashboardHome<>(session);
        SubsidyBudgetingPage<?> subsidyBudgetingPage = new SubsidyBudgetingPage<>(session);

        String budgetName = param.get("budgetName");

        if (!pricingDashboardHome.isPricingDashboardLoaded()) {
            String username = param.get("username");
            String password = param.get("password");

            step("Login via cookie injection");
            memberLoginPage.adminLoginViaCookie(username, password);

            step("Verify Member Page is Loaded or not");
            Assertion.assertThat("Member page is loaded", memberPage.isMemberDashboardLoaded(), is(true));

            step("Click to open Pricing Dashboard");
            memberPage.goToPricingDashboard(username, password);
            Assertion.assertThat("Pricing Dashboard is loaded", pricingDashboardHome.isPricingDashboardLoaded(), is(true));
        }

        step("Open Subsidy Budgeting Section");
        subsidyBudgetingPage.clickSubsidyBudgetingSidebarButton();
        Assertion.assertThat("Subsidy Budgeting is loaded", subsidyBudgetingPage.isSubsidyBudgetingPageLoaded(), is(true));
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "SubsidyBudgeting")
    @Tribe(name = "Poseidon")
    @Vertical(name = "Pricing")
    @Test
    public void testVerifySubsidyBudgetUpdateToMultipleRules() {
        Map<String, String> param = getParam("testVerifySubsidyBudgetUpdateToMultipleRules");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        MemberPage<?> memberPage = new MemberPage<>(session);
        PricingDashboardHome<?> pricingDashboardHome = new PricingDashboardHome<>(session);
        SubsidyBudgetingPage<?> subsidyBudgetingPage = new SubsidyBudgetingPage<>(session);
        PricingDashboardPageWeb<?> pricingDashboardPageWeb = PricingDashboardPageWeb.getInstance(session);

        String budgetId = param.get("budgetId");

        if (!pricingDashboardHome.isPricingDashboardLoaded()) {
            String username = param.get("username");
            String password = param.get("password");

            step("Login via cookie injection");
            memberLoginPage.adminLoginViaCookie(username, password);

            step("Verify Member Page is Loaded or not");
            Assertion.assertThat("Member page is loaded", memberPage.isMemberDashboardLoaded(), is(true));

            step("Click to open Pricing Dashboard");
            memberPage.goToPricingDashboard(username, password);
            Assertion.assertThat("Pricing Dashboard is loaded", pricingDashboardHome.isPricingDashboardLoaded(), is(true));
        }

        step("GIVEN subsidy budget created with one rule is selected");
        subsidyBudgetingPage.goDirectlyToEditPage(budgetId);
        Assertion.assertThat("Update Subsidy Budget page is loaded",
                subsidyBudgetingPage.isUpdateSubsidyBudgetingPageLoaded(), is(true));

        step("WHEN user go to edit page to the one of subsidy budgeting");
        subsidyBudgetingPage.goToSettingsAndClickEdit();

        step("AND all the mandatory fields are prefilled");
        Assertion.assertThat("Mandatory fields should be prefilled",
                subsidyBudgetingPage.verifyMandatoryFieldsPrefilled(), is(true));

        step("AND select one or more of rules");
        subsidyBudgetingPage.selectMultipleRules();

        step("Verify multiple rules are selected before saving");
        int selectedCountBefore = subsidyBudgetingPage.getSelectedRulesCount();
        Assertion.assertThat("Multiple rules should be selected", selectedCountBefore > 1, is(true));

        step("AND click 'Save' button");
        subsidyBudgetingPage.clickSaveButtonOnUpdatePage();

        step("THEN rule successfully update");
        Assertion.assertThat("Update should be successful",
                subsidyBudgetingPage.verifySubsidyBudgetUpdateComplete(), is(true));

        step("AND able to see multiple rule selected when go to edit page again");
        subsidyBudgetingPage.goDirectlyToEditPage(budgetId);
        Assertion.assertThat("Update page should load again",
                subsidyBudgetingPage.isUpdateSubsidyBudgetingPageLoaded(), is(true));

        step("Verify multiple rules are still selected after page reload");
        Assertion.assertThat("Multiple rules should still be selected",
                subsidyBudgetingPage.verifyUpdatedRulesInEditPage(), is(true));

        int selectedCountAfter = subsidyBudgetingPage.getSelectedRulesCount();
        Assertion.assertThat("Rules count should be preserved", selectedCountAfter > 1, is(true));

        step("POST-REQUISITE: Cleanup - uncheck all selected rules for next test run");
        subsidyBudgetingPage.cleanupAfterTest();

        step("AND click 'Save' button");
        subsidyBudgetingPage.clickSaveButtonOnUpdatePage();

        step("Log out");
        pricingDashboardPageWeb.logout();
    }


    @Author(names = {"Manan Kumar"})
    @Module(name = "SubsidyBudgeting")
    @Tribe(name = "Pricing")
    @Vertical(name = "Pricing")
    @Test
    public void testUnauthorizedSubsidyDashboardAccess() {
        Map<String, String> map = getParam("testUnauthorizedSubsidyDashboardAccess");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        MemberPage<?> memberPage = new MemberPage<>(session);
        PricingDashboardHome<?> pricingDashboardHome = new PricingDashboardHome<>(session);
        SubsidyBudgetingPage<?> subsidyBudgetingPage = new SubsidyBudgetingPage<>(session);
        PricingDashboardPageWeb<?> pricingDashboardPageWeb =  PricingDashboardPageWeb.getInstance(session);

        String email = map.get("email");
        String password = map.get("password");

        step("User Log in to Panel dashboard");
        memberLoginPage.gotoMemberLoginPage();
        memberLoginPage.adminLogin(email, password);

        step("User navigates to Pricing Dashboard");
        pricingDashboardPageWeb.goToPage();

        step("Verify Subsidy Budgeting Dashboard is not accessible");
        Assertion.assertThat("Subsidy Dashboard should not be loaded", pricingDashboardHome.isPricingDashboardLoaded(), is(false));

        step("Verify user is redirected back to Member Dashboard");
        Assertion.assertThat("User should be redirected to Member Dashboard", memberPage.isMemberDashboardLoaded(), CoreMatchers.is(true));

        step("User logs out");
        loginMemberAdmin.logout();
    }
    @Author(names = {"Nishtha"})
    @Module(name = "SubsidyBudgeting")
    @Tribe(name = "Poseidon")
    @Vertical(name = "Pricing")
    @Test
    public void testCreateNewSubsidyBudgetWithoutRules() {
        Map<String, String> param = getParam("testCreateNewSubsidyBudgetWithoutRules");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        MemberPage<?> memberPage = new MemberPage<>(session);
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        PricingDashboardHome<?> pricingDashboardHome = new PricingDashboardHome<>(session);
        SubsidyBudgetingPage<?> subsidyBudgetingPage = new SubsidyBudgetingPage<>(session);

        PriceAdjustmentRulesPage<?> priceAdjustmentRulesPage = new PriceAdjustmentRulesPage<>(session);

        if (pricingDashboardHome.isPricingDashboardLoaded()) {
            step("Logout user");
            priceAdjustmentRulesPage.LogOut();
        }

        if (!pricingDashboardHome.isPricingDashboardLoaded()) {
            String username = param.get("username");
            String password = param.get("password");

            step("Login via cookie injection");
            memberLoginPage.adminLoginViaCookie(username, password);

            step("Verify Member Page is Loaded or not");
            Assertion.assertThat("Member page is loaded", memberPage.isMemberDashboardLoaded(), is(true));

            step("Click to open Pricing Dashboard");
            memberPage.goToPricingDashboard(username, password);
            Assertion.assertThat("Pricing Dashboard is loaded", pricingDashboardHome.isPricingDashboardLoaded(), is(true));
        }

        step("Open Subsidy Budgeting Section");
        subsidyBudgetingPage.clickSubsidyBudgetingSidebarButton();
        Assertion.assertThat("Subsidy Budgeting is loaded", subsidyBudgetingPage.isSubsidyBudgetingPageLoaded(), is(true));

        step("User fills the info to Create New Subsidy Budget");
        String budgetName = "aut_" + CoreFunctions.randomName();
        String quota = param.get("quota");
        String vertical = param.get("vertical");
        String currency = param.get("currency");
        subsidyBudgetingPage.createNewBudget(budgetName, quota, vertical, currency);

        step("Click Save Button and confirm save on popup");
        subsidyBudgetingPage.clickSaveButton();
        subsidyBudgetingPage.clickConfirmSaveButton();

        step("Verify Success Snackbar Text");
        String expectedMessage = param.get("expectedMessage");
        Assertion.assertThat("Verify Success Snackbar Text", subsidyBudgetingPage.verifySnackbarMessage(expectedMessage), is(true));

        step("Click on Show Filter Button");
        subsidyBudgetingPage.clickOnShowFilterButton();

        step("Click on Search Budget Name");
        subsidyBudgetingPage.clickOnSearchBudgetName();

        step("Enter Invalid Budget Name");
        subsidyBudgetingPage.enterInvalidBudgetName(budgetName);

        step("Click the Search button");
        subsidyBudgetingPage.clickSearchButton();

        step("Verify user can see new budget on the budget list");
        Assertion.assertThat("Verify new budget is displayed", subsidyBudgetingPage.verifyNewBudgetInTable(budgetName), is(true));

        step("Click on Settings Button");
        subsidyBudgetingPage.clickOnSettingsButton();
        subsidyBudgetingPage.clickDeleteButton();

        step("Log out from Pricing Dashboard");
        loginMemberAdmin.logout();
    }

    @Author(names = {"Nishtha"})
    @Module(name = "SubsidyBudgeting")
    @Tribe(name = "Poseidon")
    @Vertical(name = "Pricing")
    @Test
    public void testCreateNewSubsidyBudgetWithInvalidQuota() {
        Map<String, String> param = getParam("testCreateNewSubsidyBudgetWithInvalidQuota");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        MemberPage<?> memberPage = new MemberPage<>(session);
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        PricingDashboardHome<?> pricingDashboardHome = new PricingDashboardHome<>(session);
        SubsidyBudgetingPage<?> subsidyBudgetingPage = new SubsidyBudgetingPage<>(session);
        PriceAdjustmentRulesPage<?> priceAdjustmentRulesPage = new PriceAdjustmentRulesPage<>(session);

        if (pricingDashboardHome.isPricingDashboardLoaded()) {
            step("Logout user");
            priceAdjustmentRulesPage.LogOut();
        }

        if (!pricingDashboardHome.isPricingDashboardLoaded()) {
            String username = param.get("username");
            String password = param.get("password");

            step("Login via cookie injection");
            memberLoginPage.adminLoginViaCookie(username, password);

            step("Verify Member Page is Loaded or not");
            Assertion.assertThat("Member page is loaded", memberPage.isMemberDashboardLoaded(), is(true));

            step("Click to open Pricing Dashboard");
            memberPage.goToPricingDashboard(username, password);
            Assertion.assertThat("Pricing Dashboard is loaded", pricingDashboardHome.isPricingDashboardLoaded(), is(true));
        }

        step("Open Subsidy Budgeting Section");
        subsidyBudgetingPage.clickSubsidyBudgetingSidebarButton();
        Assertion.assertThat("Subsidy Budgeting is loaded", subsidyBudgetingPage.isSubsidyBudgetingPageLoaded(), is(true));

        step("User fills the info to Create New Subsidy Budget");
        String budgetName = "aut_" + CoreFunctions.randomName();
        String quota = param.get("quota");
        String vertical = param.get("vertical");
        String currency = param.get("currency");
        subsidyBudgetingPage.createNewBudget(budgetName, quota, vertical, currency);

        step("Verify Error Text");
        String errorMessage = param.get("errorMessage");
        Assertion.assertThat("Verify Error Message", subsidyBudgetingPage.verifyErrorMessage(errorMessage), is(true));

        step("Log out from Pricing Dashboard");
        loginMemberAdmin.logout();
    }

    @Author(names = {"Nishtha"})
    @Module(name = "SubsidyBudgeting")
    @Tribe(name = "Poseidon")
    @Vertical(name = "Pricing")
    @Test
    public void testUpdatingSubsidyBudgetToNoRules() {
        Map<String, String> param = getParam("testUpdatingSubsidyBudgetToNoRules");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        MemberPage<?> memberPage = new MemberPage<>(session);
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        PricingDashboardHome<?> pricingDashboardHome = new PricingDashboardHome<>(session);
        SubsidyBudgetingPage<?> subsidyBudgetingPage = new SubsidyBudgetingPage<>(session);

        if (!pricingDashboardHome.isPricingDashboardLoaded()) {
            String username = param.get("username");
            String password = param.get("password");

            step("Login via cookie injection");
            memberLoginPage.adminLoginViaCookie(username, password);

            step("Verify Member Page is Loaded or not");
            Assertion.assertThat("Member page is loaded", memberPage.isMemberDashboardLoaded(), is(true));

            step("Click to open Pricing Dashboard");
            memberPage.goToPricingDashboard(username, password);
            Assertion.assertThat("Pricing Dashboard is loaded", pricingDashboardHome.isPricingDashboardLoaded(), is(true));
        }

        step("Open Subsidy Budgeting Section");
        subsidyBudgetingPage.clickSubsidyBudgetingSidebarButton();
        Assertion.assertThat("Subsidy Budgeting is loaded", subsidyBudgetingPage.isSubsidyBudgetingPageLoaded(), is(true));

        step("User fills the info to Create New Subsidy Budget");
        String budgetName = "aut_" + CoreFunctions.randomName();
        String quota = param.get("quota");
        String vertical = param.get("vertical");
        String currency = param.get("currency");
        subsidyBudgetingPage.createNewBudget(budgetName, quota, vertical, currency);
        subsidyBudgetingPage.clickOnBudgetRule1();

        step("Click Save Button and confirm save on popup");
        subsidyBudgetingPage.clickSaveButton();
        subsidyBudgetingPage.clickConfirmSaveButton();

        step("Verify Success Snackbar Text");
        String expectedMessage = param.get("expectedMessage");
        Assertion.assertThat("Verify Success Snackbar Text", subsidyBudgetingPage.verifySnackbarMessage(expectedMessage), is(true));

        step("Click on Show Filter Button");
        subsidyBudgetingPage.clickOnShowFilterButton();

        step("Click on Search Budget Name");
        subsidyBudgetingPage.clickOnSearchBudgetName();

        step("Enter Invalid Budget Name");
        subsidyBudgetingPage.enterInvalidBudgetName(budgetName);

        step("Click the Search button");
        subsidyBudgetingPage.clickSearchButton();

        step("Click on Settings Button");
        subsidyBudgetingPage.clickOnSettingsButton();

        step("Click on Edit Button");
        subsidyBudgetingPage.clickOnEditButton();

        step("Remove all the rules from the budget");
        subsidyBudgetingPage.clickOnDeleteAllRulesButton();

        step("Click Save Button and confirm save on popup");
        subsidyBudgetingPage.clickSaveButton();
        subsidyBudgetingPage.clickConfirmSaveButton();

        step("Click the Search button");
        subsidyBudgetingPage.clickSearchButton();

        step("Click on Settings Button");
        subsidyBudgetingPage.clickOnSettingsButton();

        step("Click on Edit Button");
        subsidyBudgetingPage.clickOnEditButton();

        step("Verify no rules are set for particular budget");
        Assertion.assertThat("Verify no rules are set", subsidyBudgetingPage.isDeleteAllButtonDisplayed(), is(false));

        step("Click Save Button and confirm save on popup");
        subsidyBudgetingPage.clickSaveButton();
        subsidyBudgetingPage.clickConfirmSaveButton();

        step("Click on Settings Button and delete the existing budget");
        subsidyBudgetingPage.clickOnSettingsButton();
        subsidyBudgetingPage.clickDeleteButton();

        step("Log out from Pricing Dashboard");
        loginMemberAdmin.logout();
    }

    @Author(names = {"Muksal"})
    @Module(name = "SubsidyBudgeting")
    @Tribe(name = "Hades")
    @Vertical(name = "Pricing")
    @Test
    public void testCreateSubsidyBudgetVariants() {
        Map<String, String> param = getParam("testCreateSubsidyBudgetVariants");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPagePricingPage memberLoginHelper = new MemberLoginPagePricingPage();
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        SubsidyBudgetingPage<?> subsidyBudgetingPage = new SubsidyBudgetingPage<>(session);

        String tiketAmount = param.get("tiketAmount");
        String vendorAmount = param.get("vendorAmount");
        String quota = param.get("quota");
        String idrCurrency = param.get("idrCurrency");
        String nonIdrCurrency = param.get("nonIdrCurrency");
        String vertical = param.get("vertical");
        String expectedMessage = param.get("expectedMessage");
        String ruleName = param.get("ruleName");

        // --- Scenario 1: CV, IDR, No Rules ---
        memberLoginHelper.ensurePricingDashboardIsLoaded(param);
        subsidyBudgetingPage.clickSubsidyBudgetingSidebarButton();
        Assertion.assertThat("[Create - CV IDR No Rules] Subsidy Budgeting is loaded", subsidyBudgetingPage.isSubsidyBudgetingPageLoaded(), is(true));

        step("[Create - CV IDR No Rules] Fill budget form");
        String budgetCV_IDR = "aut_" + CoreFunctions.randomName();
        subsidyBudgetingPage.createNewCVBudget(budgetCV_IDR, tiketAmount, vendorAmount, vertical, idrCurrency);

        step("[Create - CV IDR No Rules] Save and confirm");
        subsidyBudgetingPage.clickSaveButton();
        subsidyBudgetingPage.clickConfirmSaveButton();
        Assertion.assertThat("[Create - CV IDR No Rules] Verify success snackbar", subsidyBudgetingPage.verifySnackbarMessage(expectedMessage), is(true));

        step("[Create - CV IDR No Rules] Search and verify budget in table");
        subsidyBudgetingPage.clickOnShowFilterButton();
        subsidyBudgetingPage.clickOnSearchBudgetName();
        subsidyBudgetingPage.enterInvalidBudgetName(budgetCV_IDR);
        subsidyBudgetingPage.clickSearchButton();
        Assertion.assertThat("[Create - CV IDR No Rules] Budget is displayed in table", subsidyBudgetingPage.verifyNewBudgetInTable(budgetCV_IDR), is(true));

        step("[Create - CV IDR No Rules] Cleanup - delete budget");
        subsidyBudgetingPage.clickOnSettingsButton();
        subsidyBudgetingPage.clickDeleteButton();
        subsidyBudgetingPage.confirmDeleteButton();

        step("[Create - CV IDR No Rules] Logout");
        loginMemberAdmin.logout();

        // --- Scenario 2: CV, non-IDR, No Rules ---
        memberLoginHelper.ensurePricingDashboardIsLoaded(param);
        subsidyBudgetingPage.clickSubsidyBudgetingSidebarButton();
        Assertion.assertThat("[Create - CV non-IDR No Rules] Subsidy Budgeting is loaded", subsidyBudgetingPage.isSubsidyBudgetingPageLoaded(), is(true));

        step("[Create - CV non-IDR No Rules] Fill budget form");
        String budgetCV_nonIDR = "aut_" + CoreFunctions.randomName();
        subsidyBudgetingPage.createNewCVBudget(budgetCV_nonIDR, tiketAmount, vendorAmount, vertical, nonIdrCurrency);

        step("[Create - CV non-IDR No Rules] Save and confirm");
        subsidyBudgetingPage.clickSaveButton();
        subsidyBudgetingPage.clickConfirmSaveButton();
        Assertion.assertThat("[Create - CV non-IDR No Rules] Verify success snackbar", subsidyBudgetingPage.verifySnackbarMessage(expectedMessage), is(true));

        step("[Create - CV non-IDR No Rules] Search and verify budget in table");
        subsidyBudgetingPage.clickOnShowFilterButton();
        subsidyBudgetingPage.clickOnSearchBudgetName();
        subsidyBudgetingPage.enterInvalidBudgetName(budgetCV_nonIDR);
        subsidyBudgetingPage.clickSearchButton();
        Assertion.assertThat("[Create - CV non-IDR No Rules] Budget is displayed in table", subsidyBudgetingPage.verifyNewBudgetInTable(budgetCV_nonIDR), is(true));

        step("[Create - CV non-IDR No Rules] Cleanup - delete budget");
        subsidyBudgetingPage.clickOnSettingsButton();
        subsidyBudgetingPage.clickDeleteButton();
        subsidyBudgetingPage.confirmDeleteButton();

        step("[Create - CV non-IDR No Rules] Logout");
        loginMemberAdmin.logout();

        // --- Scenario 3: TQ, No Rules ---
        memberLoginHelper.ensurePricingDashboardIsLoaded(param);
        subsidyBudgetingPage.clickSubsidyBudgetingSidebarButton();
        Assertion.assertThat("[Create - TQ No Rules] Subsidy Budgeting is loaded", subsidyBudgetingPage.isSubsidyBudgetingPageLoaded(), is(true));

        step("[Create - TQ No Rules] Fill budget form");
        String budgetTQ = "aut_" + CoreFunctions.randomName();
        subsidyBudgetingPage.createNewBudget(budgetTQ, quota, vertical, idrCurrency);

        step("[Create - TQ No Rules] Save and confirm");
        subsidyBudgetingPage.clickSaveButton();
        subsidyBudgetingPage.clickConfirmSaveButton();
        Assertion.assertThat("[Create - TQ No Rules] Verify success snackbar", subsidyBudgetingPage.verifySnackbarMessage(expectedMessage), is(true));

        step("[Create - TQ No Rules] Search and verify budget in table");
        subsidyBudgetingPage.clickOnShowFilterButton();
        subsidyBudgetingPage.clickOnSearchBudgetName();
        subsidyBudgetingPage.enterInvalidBudgetName(budgetTQ);
        subsidyBudgetingPage.clickSearchButton();
        Assertion.assertThat("[Create - TQ No Rules] Budget is displayed in table", subsidyBudgetingPage.verifyNewBudgetInTable(budgetTQ), is(true));

        step("[Create - TQ No Rules] Cleanup - delete budget");
        subsidyBudgetingPage.clickOnSettingsButton();
        subsidyBudgetingPage.clickDeleteButton();
        subsidyBudgetingPage.confirmDeleteButton();

        step("[Create - TQ No Rules] Logout");
        loginMemberAdmin.logout();

        // --- Scenario 4: CV, IDR, With Rule ---
        memberLoginHelper.ensurePricingDashboardIsLoaded(param);
        subsidyBudgetingPage.clickSubsidyBudgetingSidebarButton();
        Assertion.assertThat("[Create - CV IDR With Rule] Subsidy Budgeting is loaded", subsidyBudgetingPage.isSubsidyBudgetingPageLoaded(), is(true));

        step("[Create - CV IDR With Rule] Fill budget form and select rule");
        String budgetCV_IDR_Rule = "aut_" + CoreFunctions.randomName();
        subsidyBudgetingPage.createNewCVBudget(budgetCV_IDR_Rule, tiketAmount, vendorAmount, vertical, idrCurrency);
        subsidyBudgetingPage.searchRuleByName(ruleName);
        subsidyBudgetingPage.clickOnBudgetRule1();

        step("[Create - CV IDR With Rule] Save and confirm");
        subsidyBudgetingPage.clickSaveButton();
        subsidyBudgetingPage.clickConfirmSaveButton();
        Assertion.assertThat("[Create - CV IDR With Rule] Verify success snackbar", subsidyBudgetingPage.verifySnackbarMessage(expectedMessage), is(true));

        step("[Create - CV IDR With Rule] Search and verify budget in table");
        subsidyBudgetingPage.clickOnShowFilterButton();
        subsidyBudgetingPage.clickOnSearchBudgetName();
        subsidyBudgetingPage.enterInvalidBudgetName(budgetCV_IDR_Rule);
        subsidyBudgetingPage.clickSearchButton();
        Assertion.assertThat("[Create - CV IDR With Rule] Budget is displayed in table", subsidyBudgetingPage.verifyNewBudgetInTable(budgetCV_IDR_Rule), is(true));

        step("[Create - CV IDR With Rule] Cleanup - delete budget");
        subsidyBudgetingPage.clickOnSettingsButton();
        subsidyBudgetingPage.clickDeleteButton();
        subsidyBudgetingPage.confirmDeleteButton();

        step("[Create - CV IDR With Rule] Logout");
        loginMemberAdmin.logout();

        // --- Scenario 5: CV, non-IDR, With Rule ---
        memberLoginHelper.ensurePricingDashboardIsLoaded(param);
        subsidyBudgetingPage.clickSubsidyBudgetingSidebarButton();
        Assertion.assertThat("[Create - CV non-IDR With Rule] Subsidy Budgeting is loaded", subsidyBudgetingPage.isSubsidyBudgetingPageLoaded(), is(true));

        step("[Create - CV non-IDR With Rule] Fill budget form and select rule");
        String budgetCV_nonIDR_Rule = "aut_" + CoreFunctions.randomName();
        subsidyBudgetingPage.createNewCVBudget(budgetCV_nonIDR_Rule, tiketAmount, vendorAmount, vertical, nonIdrCurrency);
        subsidyBudgetingPage.searchRuleByName(ruleName);
        subsidyBudgetingPage.clickOnBudgetRule1();

        step("[Create - CV non-IDR With Rule] Save and confirm");
        subsidyBudgetingPage.clickSaveButton();
        subsidyBudgetingPage.clickConfirmSaveButton();
        Assertion.assertThat("[Create - CV non-IDR With Rule] Verify success snackbar", subsidyBudgetingPage.verifySnackbarMessage(expectedMessage), is(true));

        step("[Create - CV non-IDR With Rule] Search and verify budget in table");
        subsidyBudgetingPage.clickOnShowFilterButton();
        subsidyBudgetingPage.clickOnSearchBudgetName();
        subsidyBudgetingPage.enterInvalidBudgetName(budgetCV_nonIDR_Rule);
        subsidyBudgetingPage.clickSearchButton();
        Assertion.assertThat("[Create - CV non-IDR With Rule] Budget is displayed in table", subsidyBudgetingPage.verifyNewBudgetInTable(budgetCV_nonIDR_Rule), is(true));

        step("[Create - CV non-IDR With Rule] Cleanup - delete budget");
        subsidyBudgetingPage.clickOnSettingsButton();
        subsidyBudgetingPage.clickDeleteButton();
        subsidyBudgetingPage.confirmDeleteButton();

        step("[Create - CV non-IDR With Rule] Logout");
        loginMemberAdmin.logout();

        // --- Scenario 6: TQ, With Rule ---
        memberLoginHelper.ensurePricingDashboardIsLoaded(param);
        subsidyBudgetingPage.clickSubsidyBudgetingSidebarButton();
        Assertion.assertThat("[Create - TQ With Rule] Subsidy Budgeting is loaded", subsidyBudgetingPage.isSubsidyBudgetingPageLoaded(), is(true));

        step("[Create - TQ With Rule] Fill budget form and select rule");
        String budgetTQ_Rule = "aut_" + CoreFunctions.randomName();
        subsidyBudgetingPage.createNewBudget(budgetTQ_Rule, quota, vertical, idrCurrency);
        subsidyBudgetingPage.searchRuleByName(ruleName);
        subsidyBudgetingPage.clickOnBudgetRule1();

        step("[Create - TQ With Rule] Save and confirm");
        subsidyBudgetingPage.clickSaveButton();
        subsidyBudgetingPage.clickConfirmSaveButton();
        Assertion.assertThat("[Create - TQ With Rule] Verify success snackbar", subsidyBudgetingPage.verifySnackbarMessage(expectedMessage), is(true));

        step("[Create - TQ With Rule] Search and verify budget in table");
        subsidyBudgetingPage.clickOnShowFilterButton();
        subsidyBudgetingPage.clickOnSearchBudgetName();
        subsidyBudgetingPage.enterInvalidBudgetName(budgetTQ_Rule);
        subsidyBudgetingPage.clickSearchButton();
        Assertion.assertThat("[Create - TQ With Rule] Budget is displayed in table", subsidyBudgetingPage.verifyNewBudgetInTable(budgetTQ_Rule), is(true));

        step("[Create - TQ With Rule] Cleanup - delete budget");
        subsidyBudgetingPage.clickOnSettingsButton();
        subsidyBudgetingPage.clickDeleteButton();
        subsidyBudgetingPage.confirmDeleteButton();

        step("[Create - TQ With Rule] Logout");
        loginMemberAdmin.logout();

        // --- Scenario 7: CV, IDR, Active -> Deactivated -> Delete ---
        memberLoginHelper.ensurePricingDashboardIsLoaded(param);
        subsidyBudgetingPage.clickSubsidyBudgetingSidebarButton();
        Assertion.assertThat("[Create - CV IDR Active to Inactive] Subsidy Budgeting is loaded", subsidyBudgetingPage.isSubsidyBudgetingPageLoaded(), is(true));

        step("[Create - CV IDR Active to Inactive] Fill budget form");
        String budgetCV_IDR_Lifecycle = "aut_" + CoreFunctions.randomName();
        subsidyBudgetingPage.createNewCVBudget(budgetCV_IDR_Lifecycle, tiketAmount, vendorAmount, vertical, idrCurrency);
        subsidyBudgetingPage.selectFormStatus();

        step("[Create - CV IDR Active to Inactive] Save and confirm");
        subsidyBudgetingPage.clickSaveButton();
        subsidyBudgetingPage.clickConfirmSaveButton();
        Assertion.assertThat("[Create - CV IDR Active to Inactive] Verify success snackbar", subsidyBudgetingPage.verifySnackbarMessage(expectedMessage), is(true));

        step("[Create - CV IDR Active to Inactive] Search and verify budget in table");
        subsidyBudgetingPage.clickOnShowFilterButton();
        subsidyBudgetingPage.clickOnSearchBudgetName();
        subsidyBudgetingPage.enterInvalidBudgetName(budgetCV_IDR_Lifecycle);
        subsidyBudgetingPage.clickSearchButton();
        Assertion.assertThat("[Create - CV IDR Active to Inactive] Budget is displayed in table", subsidyBudgetingPage.verifyNewBudgetInTable(budgetCV_IDR_Lifecycle), is(true));

        step("[Create - CV IDR Active to Inactive] Verify budget status is Active");
        Assertion.assertThat("[Create - CV IDR Active to Inactive] Budget status is Active", subsidyBudgetingPage.verifyBudgetActiveStatusInTable(), is(true));

        step("[Create - CV IDR Active to Inactive] Deactivate the budget");
        subsidyBudgetingPage.clickOnSettingsButton();
        subsidyBudgetingPage.clickOnEditButton();
        subsidyBudgetingPage.selectFormStatus();
        subsidyBudgetingPage.clickSaveButton();
        subsidyBudgetingPage.clickConfirmSaveButton();

        step("[Create - CV IDR Active to Inactive] Refresh search results");
        subsidyBudgetingPage.enterInvalidBudgetName(budgetCV_IDR_Lifecycle);
        subsidyBudgetingPage.clickSearchButton();
        Assertion.assertThat("[Create - CV IDR Active to Inactive] Budget status is Inactive", subsidyBudgetingPage.verifyInactiveSearchResult(), is(true));

        step("[Create - CV IDR Active to Inactive] Cleanup - delete budget");
        subsidyBudgetingPage.clickOnSettingsButton();
        subsidyBudgetingPage.clickDeleteButton();
        subsidyBudgetingPage.confirmDeleteButton();

        step("[Create - CV IDR Active to Inactive] Logout");
        loginMemberAdmin.logout();
    }

}
