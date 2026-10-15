package com.tiket.web.dashboard.test.pricing.PriceAdjustmentRules;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.core.Is.is;

import java.time.Year;
import java.util.Map;

import org.testng.annotations.Test;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.member_dashboard.MemberLoginPage;
import com.tiket.web.dashboard.page.member_dashboard.MemberPage;
import com.tiket.web.dashboard.page.member_dashboard.PricingDashboardHome;
import com.tiket.web.dashboard.page.pricing.PriceAdjustmentRules.PriceAdjustmentRulesPage;

public class PriceAdjustmentRulesTest extends BaseTest {

    private void ensurePricingDashboardWithMemberLoginFlow(Session<?> session, Map<String, String> param) {
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        MemberPage<?> memberPage = new MemberPage<>(session);
        PricingDashboardHome<?> pricingDashboardHome = new PricingDashboardHome<>(session);

        if (pricingDashboardHome.isPricingDashboardLoaded()) {
            step("Logout user");
            memberLoginPage.logoutAdmin();
            Assertion.assertThat("User is logged out", memberLoginPage.isAdminLogin(), is(false));
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
            Assertion.assertThat("Pricing Dashboard is loaded",
                    pricingDashboardHome.isPricingDashboardLoaded(), is(true));
        }
    }

    private static String resolveStartMonthYear(Map<String, String> param) {
        String s = param.get("startDate");
        if (s != null && !s.isBlank()) {
            return s.trim();
        }
        int filterYear = parseIntTestParam(param, "dateFilterYear", Year.now().getValue());
        int year = parseIntTestParam(param, "startDateYear", filterYear);
        int m = parseIntTestParam(param, "startDateMonth", 1);
        return CoreFunctions.getMonthName(m, "en") + " " + year;
    }

    private static String resolveEndMonthYear(Map<String, String> param) {
        String s = param.get("endDate");
        if (s != null && !s.isBlank()) {
            return s.trim();
        }
        int filterYear = parseIntTestParam(param, "dateFilterYear", Year.now().getValue());
        int year = parseIntTestParam(param, "endDateYear", filterYear);
        int m = parseIntTestParam(param, "endDateMonth", 12);
        return CoreFunctions.getMonthName(m, "en") + " " + year;
    }

    private static int parseIntTestParam(Map<String, String> param, String key, int defaultValue) {
        String v = param.get(key);
        if (v == null || v.isBlank()) {
            return defaultValue;
        }
        return Integer.parseInt(v.trim());
    }

    private static String requireStringTestParam(Map<String, String> param, String key) {
        String v = param.get(key);
        if (v == null || v.isBlank()) {
            throw new IllegalArgumentException(
                    "Missing test data for key \"" + key + "\" in preprod/gk/prod params (use key:value pairs separated by commas).");
        }
        return v.trim();
    }

    @Author(names = { "Rounit" })
    @Module(name = "PriceAdjustmentRules")
    @Tribe(name = "Poseidon")
    @Vertical(name = "Pricing")
    @Test
    public void testCompletedReleasedOnPAR() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Map<String, String> param = getParam("testCompletedReleasedOnPAR");
        BaseTest.paramsThreadLocal.set(param);

        PriceAdjustmentRulesPage<?> priceAdjustmentRulesPage = new PriceAdjustmentRulesPage<>(session);

        ensurePricingDashboardWithMemberLoginFlow(session, param);

        step("Open Campaign & Rule Management Section");
        priceAdjustmentRulesPage.clickCampAndRuleManagementSidebarButton();

        step("Open Price Adjustment Rules Page");
        priceAdjustmentRulesPage.clickPriceAdjustmentRulesSidebarButton();
        Assertion.assertThat("Price Adjustment Rules Page is loaded",
                priceAdjustmentRulesPage.isPriceAdjustmentRulesPageLoaded(), is(true));

        step("Verify new tab Completed/Released on Price Adjustment Rules page");
        Assertion.assertThat("Verify new tab Completed Released on Price Adjustment Rules page",
                priceAdjustmentRulesPage.isCompRelSectionLoaded(), is(true));
    }

    @Author(names = { "Rounit" })
    @Module(name = "PriceAdjustmentRules")
    @Tribe(name = "Poseidon")
    @Vertical(name = "Pricing")
    @Test
    public void testNeedApprovalOnPAR() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Map<String, String> param = getParam("testNeedApprovalOnPAR");
        BaseTest.paramsThreadLocal.set(param);

        PriceAdjustmentRulesPage<?> priceAdjustmentRulesPage = new PriceAdjustmentRulesPage<>(session);

        ensurePricingDashboardWithMemberLoginFlow(session, param);

        step("Open Campaign & Rule Management Section");
        priceAdjustmentRulesPage.clickCampAndRuleManagementSidebarButton();

        step("Open Price Adjustment Rules Page");
        priceAdjustmentRulesPage.clickPriceAdjustmentRulesSidebarButton();
        Assertion.assertThat("Price Adjustment Rules Page is loaded",
                priceAdjustmentRulesPage.isPriceAdjustmentRulesPageLoaded(), is(true));

        step("Verify new tab Need Approval on Price Adjustment Rules page");
        Assertion.assertThat("Verify new tab Need Approval on Price Adjustment Rules page",
                priceAdjustmentRulesPage.isNeedApprovalSectionLoaded(), is(true));
    }

    @Author(names = { "Rounit" })
    @Module(name = "PriceAdjustmentRules")
    @Tribe(name = "Poseidon")
    @Vertical(name = "Pricing")
    @Test
    public void testZeroBesideNeedApprovalSection() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Map<String, String> param = getParam("testZeroBesideNeedApprovalSection");
        BaseTest.paramsThreadLocal.set(param);

        PriceAdjustmentRulesPage<?> priceAdjustmentRulesPage = new PriceAdjustmentRulesPage<>(session);

        ensurePricingDashboardWithMemberLoginFlow(session, param);

        step("Open Campaign & Rule Management Section");
        priceAdjustmentRulesPage.clickCampAndRuleManagementSidebarButton();

        step("Open Price Adjustment Rules Page");
        priceAdjustmentRulesPage.clickPriceAdjustmentRulesSidebarButton();
        Assertion.assertThat("Price Adjustment Rules Page is loaded",
                priceAdjustmentRulesPage.isPriceAdjustmentRulesPageLoaded(), is(true));

        step("Verify will shown an notification in number as 0 beside Need Approval tab if there is no rules need to be approve");
        Assertion.assertThat(
                "Verify will shown an notification in number as 0 beside Need Approval tab if there is no rules need to be approve",
                priceAdjustmentRulesPage.isNeedApprovalSectionShowZero(), is(true));
    }

    @Author(names = { "Rounit" })
    @Module(name = "PriceAdjustmentRules")
    @Tribe(name = "Poseidon")
    @Vertical(name = "Pricing")
    @Test
    public void testNumberBesideNeedApprovalSection() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Map<String, String> param = getParam("testNumberBesideNeedApprovalSection");
        BaseTest.paramsThreadLocal.set(param);

        PriceAdjustmentRulesPage<?> priceAdjustmentRulesPage = new PriceAdjustmentRulesPage<>(session);

        ensurePricingDashboardWithMemberLoginFlow(session, param);

        step("Open Campaign & Rule Management Section");
        priceAdjustmentRulesPage.clickCampAndRuleManagementSidebarButton();

        step("Open Price Adjustment Rules Page");
        priceAdjustmentRulesPage.clickPriceAdjustmentRulesSidebarButton();
        Assertion.assertThat("Price Adjustment Rules Page is loaded",
                priceAdjustmentRulesPage.isPriceAdjustmentRulesPageLoaded(), is(true));

        step("Change the Vertical to TESTING");
        priceAdjustmentRulesPage.changeToOtherVertical();

        step("Verify will shown an notification in number as many rules need to be approved beside Need Approval tab if there are some rules need to be approve");
        Assertion.assertThat(
                "Verify will shown an notification in number as many rules need to be approved beside Need Approval tab if there are some rules need to be approve",
                priceAdjustmentRulesPage.isNeedApprovalSectionGreaterThanZero(), is(true));
    }

    @Author(names = { "Rounit" })
    @Module(name = "PriceAdjustmentRules")
    @Tribe(name = "Poseidon")
    @Vertical(name = "Pricing")
    @Test
    public void testPendingRuleWithAllFilter() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Map<String, String> param = getParam("testPendingRuleWithAllFilter");
        BaseTest.paramsThreadLocal.set(param);

        PriceAdjustmentRulesPage<?> priceAdjustmentRulesPage = new PriceAdjustmentRulesPage<>(session);

        ensurePricingDashboardWithMemberLoginFlow(session, param);

        String startMonthYear = resolveStartMonthYear(param);
        String endMonthYear = resolveEndMonthYear(param);
        String ruleName = requireStringTestParam(param, "ruleName");

        step("Open Campaign & Rule Management Section");
        priceAdjustmentRulesPage.clickCampAndRuleManagementSidebarButton();

        step("Open Price Adjustment Rules Page");
        priceAdjustmentRulesPage.clickPriceAdjustmentRulesSidebarButton();
        Assertion.assertThat("Price Adjustment Rules Page is loaded",
                priceAdjustmentRulesPage.isPriceAdjustmentRulesPageLoaded(), is(true));

        step("Change the Vertical to TESTING");
        priceAdjustmentRulesPage.changeToOtherVertical();

        step("Open Need Approval Section");
        priceAdjustmentRulesPage.clickNeedApprovalSection();

        step("Enter Start Date from Calendar");
        priceAdjustmentRulesPage.selectStartDateFromCalendar(startMonthYear);

        step("Enter End Date from Calendar");
        priceAdjustmentRulesPage.selectEndDateFromCalendar(endMonthYear);

        step("Enter the Rule name");
        priceAdjustmentRulesPage.clickRuleNameField(ruleName);

        step("Click Submit Button");
        priceAdjustmentRulesPage.clickSubmitButton();

        step("Verify the Rule Name after applying filter");
        Assertion.assertThat("Verify the Rule Name after applying filter",
                priceAdjustmentRulesPage.verifyRuleName(ruleName), is(true));
    }

    @Author(names = { "Rounit" })
    @Module(name = "PriceAdjustmentRules")
    @Tribe(name = "Poseidon")
    @Vertical(name = "Pricing")
    @Test
    public void testFilterRuleByCheckBox() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Map<String, String> param = getParam("testFilterRuleByCheckBox");
        BaseTest.paramsThreadLocal.set(param);

        PriceAdjustmentRulesPage<?> priceAdjustmentRulesPage = new PriceAdjustmentRulesPage<>(session);

        ensurePricingDashboardWithMemberLoginFlow(session, param);
        String ruleName = param.get("ruleName");

        step("Open Campaign & Rule Management Section");
        priceAdjustmentRulesPage.clickCampAndRuleManagementSidebarButton();

        step("Open Price Adjustment Rules Page");
        priceAdjustmentRulesPage.clickPriceAdjustmentRulesSidebarButton();
        Assertion.assertThat("Price Adjustment Rules Page is loaded",
                priceAdjustmentRulesPage.isPriceAdjustmentRulesPageLoaded(), is(true));

        step("Change the Vertical to TESTING");
        priceAdjustmentRulesPage.changeToOtherVertical();

        step("Open Need Approval Section");
        priceAdjustmentRulesPage.clickNeedApprovalSection();

        step("Check the Show Mine only Section");
        priceAdjustmentRulesPage.clickShowMineCheckBox();

        step("Click Submit Button");
        priceAdjustmentRulesPage.clickSubmitButton();

        step("Click Rule Changes Button");
        priceAdjustmentRulesPage.clickRuleChangesButton();

        step("Verify the last updated by");
        priceAdjustmentRulesPage.verifyRuleUpdateBy();

        step("Verify the Rule Name after applying filter");
        Assertion.assertThat("Verify the Rule Name after applying filter",
                priceAdjustmentRulesPage.verifyRuleName(ruleName), is(true));
    }

    @Author(names = { "Rounit" })
    @Module(name = "PriceAdjustmentRules")
    @Tribe(name = "Poseidon")
    @Vertical(name = "Pricing")
    @Test
    public void testResetFilterWithFilledValues() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Map<String, String> param = getParam("testResetFilterWithFilledValues");
        BaseTest.paramsThreadLocal.set(param);

        PriceAdjustmentRulesPage<?> priceAdjustmentRulesPage = new PriceAdjustmentRulesPage<>(session);

        ensurePricingDashboardWithMemberLoginFlow(session, param);
        String startMonthYear = resolveStartMonthYear(param);
        String endMonthYear = resolveEndMonthYear(param);
        String ruleName = requireStringTestParam(param, "ruleName");

        step("Open Campaign & Rule Management Section");
        priceAdjustmentRulesPage.clickCampAndRuleManagementSidebarButton();

        step("Open Price Adjustment Rules Page");
        priceAdjustmentRulesPage.clickPriceAdjustmentRulesSidebarButton();
        Assertion.assertThat("Price Adjustment Rules Page is loaded",
                priceAdjustmentRulesPage.isPriceAdjustmentRulesPageLoaded(), is(true));

        step("Change the Vertical to TESTING");
        priceAdjustmentRulesPage.changeToOtherVertical();

        step("Open Need Approval Section");
        priceAdjustmentRulesPage.clickNeedApprovalSection();

        step("Enter Start Date from Calendar");
        priceAdjustmentRulesPage.selectStartDateFromCalendar(startMonthYear);

        step("Enter End Date from Calendar");
        priceAdjustmentRulesPage.selectEndDateFromCalendar(endMonthYear);

        step("Enter the Rule name");
        priceAdjustmentRulesPage.clickRuleNameField(ruleName);

        step("Click Submit Button");
        priceAdjustmentRulesPage.clickSubmitButton();

        step("Click Reset Filter Button");
        priceAdjustmentRulesPage.clickResetFilterButton();

        step("Verify reset the filter by clicking Reset button when some filter applied");
        Assertion.assertThat("Verify reset the filter by clicking Reset button when some filter applied",
                priceAdjustmentRulesPage.verifyResetFilter(), is(true));
    }

    @Author(names = { "Rounit" })
    @Module(name = "PriceAdjustmentRules")
    @Tribe(name = "Poseidon")
    @Vertical(name = "Pricing")
    @Test
    public void testApproveButtonIsNotAccessible() {
        Map<String, String> param = getParam("testApproveButtonIsNotAccessible");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        PriceAdjustmentRulesPage<?> priceAdjustmentRulesPage = new PriceAdjustmentRulesPage<>(session);

        ensurePricingDashboardWithMemberLoginFlow(session, param);

        step("Open Campaign & Rule Management Section");
        priceAdjustmentRulesPage.clickCampAndRuleManagementSidebarButton();

        step("Open Price Adjustment Rules Page");
        priceAdjustmentRulesPage.clickPriceAdjustmentRulesSidebarButton();
        Assertion.assertThat("Price Adjustment Rules Page is loaded",
                priceAdjustmentRulesPage.isPriceAdjustmentRulesPageLoaded(), is(true));

        step("Change the Vertical to TESTING");
        priceAdjustmentRulesPage.changeToOtherVertical();

        step("Open Need Approval Section");
        priceAdjustmentRulesPage.clickNeedApprovalSection();

        step("Verify button Approve/Reject will be greyed-out if user access is non-approver");
        Assertion.assertThat("Verify button Approve/Reject will be greyed-out if user access is non-approver",
                priceAdjustmentRulesPage.verifyApproveRejectButton(false), is(true));
    }

    @Author(names = { "Rounit" })
    @Module(name = "PriceAdjustmentRules")
    @Tribe(name = "Poseidon")
    @Vertical(name = "Pricing")
    @Test
    public void testApproveButtonIsAccessible() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Map<String, String> param = getParam("testApproveButtonIsAccessible");
        BaseTest.paramsThreadLocal.set(param);

        PriceAdjustmentRulesPage<?> priceAdjustmentRulesPage = new PriceAdjustmentRulesPage<>(session);
        PricingDashboardHome<?> pricingDashboardHome = new PricingDashboardHome<>(session);

        if (pricingDashboardHome.isPricingDashboardLoaded()) {
            step("Logout user");
            priceAdjustmentRulesPage.LogOut();
        }

        if (pricingDashboardHome.isPricingDashboardLoaded()) {
            step("Logout user");
            priceAdjustmentRulesPage.LogOut();
        }

        ensurePricingDashboardWithMemberLoginFlow(session, param);

        step("Open Campaign & Rule Management Section");
        priceAdjustmentRulesPage.clickCampAndRuleManagementSidebarButton();

        step("Open Price Adjustment Rules Page");
        priceAdjustmentRulesPage.clickPriceAdjustmentRulesSidebarButton();
        Assertion.assertThat("Price Adjustment Rules Page is loaded",
                priceAdjustmentRulesPage.isPriceAdjustmentRulesPageLoaded(), is(true));

        step("Change the Vertical to TESTING");
        priceAdjustmentRulesPage.changeToOtherVertical();

        step("Open Need Approval Section");
        priceAdjustmentRulesPage.clickNeedApprovalSection();

        step("Verify button Approve/Reject will be accessible if user access is approver");
        Assertion.assertThat("Verify button Approve/Reject will be accessible if user access is approver",
                priceAdjustmentRulesPage.verifyApproveRejectButton(true), is(true));
    }

    @Author(names = { "Rounit" })
    @Module(name = "PriceAdjustmentRules")
    @Tribe(name = "Poseidon")
    @Vertical(name = "Pricing")
    @Test
    public void verifyAbleToSeeDetailsOfChangesOnRules() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyAbleToSeeDetailsOfChangesOnRules");
        BaseTest.paramsThreadLocal.set(param);

        PriceAdjustmentRulesPage<?> priceAdjustmentRulesPage = new PriceAdjustmentRulesPage<>(session);

        ensurePricingDashboardWithMemberLoginFlow(session, param);

        step("Open Campaign & Rule Management Section");
        priceAdjustmentRulesPage.clickCampAndRuleManagementSidebarButton();

        step("Open Price Adjustment Rules Page");
        priceAdjustmentRulesPage.clickPriceAdjustmentRulesSidebarButton();
        Assertion.assertThat("Price Adjustment Rules Page is loaded",
                priceAdjustmentRulesPage.isPriceAdjustmentRulesPageLoaded(), is(true));

        step("Change the Vertical to TESTING");
        priceAdjustmentRulesPage.changeToOtherVertical();

        step("Open Need Approval Section");
        priceAdjustmentRulesPage.clickNeedApprovalSection();

        step("Click on Rules Changes Button");
        priceAdjustmentRulesPage.clickOnRuleChangesButton(1);

        step("Verify able to see the details of changes on the rules");
        Assertion.assertThat("Verify able to see the details of changes on the rules",
                priceAdjustmentRulesPage.verifyRuleChangesSection(), is(true));
    }

    @Author(names = { "Rounit" })
    @Module(name = "PriceAdjustmentRules")
    @Tribe(name = "Poseidon")
    @Vertical(name = "Pricing")
    @Test
    public void verifyPrevRuleDetailsShowRulesInfo() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyPrevRuleDetailsShowRulesInfo");
        BaseTest.paramsThreadLocal.set(param);

        PriceAdjustmentRulesPage<?> priceAdjustmentRulesPage = new PriceAdjustmentRulesPage<>(session);

        ensurePricingDashboardWithMemberLoginFlow(session, param);
        String ruleName = param.get("ruleName");

        step("Open Campaign & Rule Management Section");
        priceAdjustmentRulesPage.clickCampAndRuleManagementSidebarButton();

        step("Open Price Adjustment Rules Page");
        priceAdjustmentRulesPage.clickPriceAdjustmentRulesSidebarButton();
        Assertion.assertThat("Price Adjustment Rules Page is loaded",
                priceAdjustmentRulesPage.isPriceAdjustmentRulesPageLoaded(), is(true));

        step("Open Need Approval Section");
        priceAdjustmentRulesPage.clickNeedApprovalSection();

        step("Change the Vertical to TESTING");
        priceAdjustmentRulesPage.changeToOtherVertical();

        step("Click on Rules Changes Button");
        priceAdjustmentRulesPage.clickOnRuleChangesButton(3);

        step("Verify that the previous rule details display the correct information before the update");
        Assertion.assertThat(
                "Verify that the previous rule details display the correct information before the update",
                priceAdjustmentRulesPage.verifyPreviousRuleDetailInformation(ruleName), is(true));
    }

    @Author(names = { "Rounit" })
    @Module(name = "PriceAdjustmentRules")
    @Tribe(name = "Poseidon")
    @Vertical(name = "Pricing")
    @Test
    public void verifyNewRuleDetailShowUpdatedRule() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyNewRuleDetailShowUpdatedRule");
        BaseTest.paramsThreadLocal.set(param);

        PriceAdjustmentRulesPage<?> priceAdjustmentRulesPage = new PriceAdjustmentRulesPage<>(session);

        ensurePricingDashboardWithMemberLoginFlow(session, param);
        String ruleName = param.get("ruleName");

        step("Open Campaign & Rule Management Section");
        priceAdjustmentRulesPage.clickCampAndRuleManagementSidebarButton();

        step("Open Price Adjustment Rules Page");
        priceAdjustmentRulesPage.clickPriceAdjustmentRulesSidebarButton();
        Assertion.assertThat("Price Adjustment Rules Page is loaded",
                priceAdjustmentRulesPage.isPriceAdjustmentRulesPageLoaded(), is(true));

        step("Open Need Approval Section");
        priceAdjustmentRulesPage.clickNeedApprovalSection();

        step("Change the Vertical to TESTING");
        priceAdjustmentRulesPage.changeToOtherVertical();

        step("Click on Rules Changes Button");
        priceAdjustmentRulesPage.clickOnRuleChangesButton(1);

        step("Verify that the new rule details display the updated information");
        Assertion.assertThat("Verify that the new rule details display the updated information",
                priceAdjustmentRulesPage.verifyNewRuleDetailInformation(ruleName), is(true));
    }

    @Author(names = { "Rounit" })
    @Module(name = "PriceAdjustmentRules")
    @Tribe(name = "Poseidon")
    @Vertical(name = "Pricing")
    @Test
    public void verifyEditButtonForBelongingUser() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyEditButtonForBelongingUser");
        BaseTest.paramsThreadLocal.set(param);

        PriceAdjustmentRulesPage<?> priceAdjustmentRulesPage = new PriceAdjustmentRulesPage<>(session);

        ensurePricingDashboardWithMemberLoginFlow(session, param);
        String username = param.get("username");

        step("Open Campaign & Rule Management Section");
        priceAdjustmentRulesPage.clickCampAndRuleManagementSidebarButton();

        step("Open Price Adjustment Rules Page");
        priceAdjustmentRulesPage.clickPriceAdjustmentRulesSidebarButton();
        Assertion.assertThat("Price Adjustment Rules Page is loaded",
                priceAdjustmentRulesPage.isPriceAdjustmentRulesPageLoaded(), is(true));

        step("Open Need Approval Section");
        priceAdjustmentRulesPage.clickNeedApprovalSection();

        step("Change the Vertical to TESTING");
        priceAdjustmentRulesPage.changeToOtherVertical();

        step("Click on Rules Changes Button");
        priceAdjustmentRulesPage.clickOnRuleChangesButton(2);

        step("Verify that the Edit button is shown only for rules updated by the belonging user");
        Assertion.assertThat(
                "Verify Edit button will shown only for rules created updated by the belonging user",
                priceAdjustmentRulesPage.verifyEditButtonForBelongingUserIsEnabled(username),
                is(true));
    }

    @Author(names = { "Rounit" })
    @Module(name = "PriceAdjustmentRules")
    @Tribe(name = "Poseidon")
    @Vertical(name = "Pricing")
    @Test
    public void verifyEditButtonForNonBelongingUser() {
        Map<String, String> param = getParam("verifyEditButtonForNonBelongingUser");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        PriceAdjustmentRulesPage<?> priceAdjustmentRulesPage = new PriceAdjustmentRulesPage<>(session);
        String username = param.get("username");

        ensurePricingDashboardWithMemberLoginFlow(session, param);

        step("Open Campaign & Rule Management Section");
        priceAdjustmentRulesPage.clickCampAndRuleManagementSidebarButton();

        step("Open Price Adjustment Rules Page");
        priceAdjustmentRulesPage.clickPriceAdjustmentRulesSidebarButton();
        Assertion.assertThat("Price Adjustment Rules Page is loaded",
                priceAdjustmentRulesPage.isPriceAdjustmentRulesPageLoaded(), is(true));

        step("Open Need Approval Section");
        priceAdjustmentRulesPage.clickNeedApprovalSection();

        step("Change the Vertical to TESTING");
        priceAdjustmentRulesPage.changeToOtherVertical();

        step("Click on Rules Changes Button");
        priceAdjustmentRulesPage.clickOnRuleChangesButton(2);

        step("Verify that the Edit button is hidden for rules updated by a non-belonging user");
        Assertion.assertThat("Verify Edit button will hide for rules created updated by the non belonging user",
                priceAdjustmentRulesPage.verifyEditButtonForBelongingUserIsDisabled(username),
                is(true));
    }

    @Author(names = { "Rounit" })
    @Module(name = "PriceAdjustmentRules")
    @Tribe(name = "Poseidon")
    @Vertical(name = "Pricing")
    @Test
    public void verifyEditButtonOnRulesForNonApprover() {
        Map<String, String> param = getParam("verifyEditButtonOnRulesForNonApprover");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        PriceAdjustmentRulesPage<?> priceAdjustmentRulesPage = new PriceAdjustmentRulesPage<>(session);

        ensurePricingDashboardWithMemberLoginFlow(session, param);

        step("Open Campaign & Rule Management Section");
        priceAdjustmentRulesPage.clickCampAndRuleManagementSidebarButton();

        step("Open Price Adjustment Rules Page");
        priceAdjustmentRulesPage.clickPriceAdjustmentRulesSidebarButton();
        Assertion.assertThat("Price Adjustment Rules Page is loaded",
                priceAdjustmentRulesPage.isPriceAdjustmentRulesPageLoaded(), is(true));

        step("Open Need Approval Section");
        priceAdjustmentRulesPage.clickNeedApprovalSection();

        step("Change the Vertical to TESTING");
        priceAdjustmentRulesPage.changeToOtherVertical();

        step("Click on Rules Changes Button");
        priceAdjustmentRulesPage.clickOnRuleChangesButton(2);

        step("Verify that the Edit button is hidden on all rules when the user is not an approver");
        Assertion.assertThat(
                "Verify that the Edit button is hidden on all rules when the user is not an approver",
                priceAdjustmentRulesPage.verifyEditButtonOnRulesForNonApproverIsDisabled(), is(true));
    }

    @Author(names = { "Rounit" })
    @Module(name = "PriceAdjustmentRules")
    @Tribe(name = "Poseidon")
    @Vertical(name = "Pricing")
    @Test
    public void verifyPrevRuleDetailIsBlankForUpdated() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyPrevRuleDetailIsBlankForUpdated");
        BaseTest.paramsThreadLocal.set(param);

        PriceAdjustmentRulesPage<?> priceAdjustmentRulesPage = new PriceAdjustmentRulesPage<>(session);

        ensurePricingDashboardWithMemberLoginFlow(session, param);
        String text = param.get("text");

        step("Open Campaign & Rule Management Section");
        priceAdjustmentRulesPage.clickCampAndRuleManagementSidebarButton();

        step("Open Price Adjustment Rules Page");
        priceAdjustmentRulesPage.clickPriceAdjustmentRulesSidebarButton();
        Assertion.assertThat("Price Adjustment Rules Page is loaded",
                priceAdjustmentRulesPage.isPriceAdjustmentRulesPageLoaded(), is(true));

        step("Open Need Approval Section");
        priceAdjustmentRulesPage.clickNeedApprovalSection();

        step("Change the Vertical to TESTING");
        priceAdjustmentRulesPage.changeToOtherVertical();

        step("Click on Rules Changes Button");
        priceAdjustmentRulesPage.clickOnRuleChangesButton(2);

        step("Verify for the created then updated the rules only shown the New Rule Detail and let the Previous Rule Detail blank");
        Assertion.assertThat(
                "Verify for the created then updated the rules only shown the New Rule Detail and let the Previous Rule Detail blank",
                priceAdjustmentRulesPage.verifyPreviousRuleDetailIsBlank(), is(emptyString()));
    }

    @Author(names = { "Rounit" })
    @Module(name = "PriceAdjustmentRules")
    @Tribe(name = "Poseidon")
    @Vertical(name = "Pricing")
    @Test
    public void verifyPrevRuleDetailIsBlankForCreated() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyPrevRuleDetailIsBlankForCreated");
        BaseTest.paramsThreadLocal.set(param);

        PriceAdjustmentRulesPage<?> priceAdjustmentRulesPage = new PriceAdjustmentRulesPage<>(session);

        ensurePricingDashboardWithMemberLoginFlow(session, param);
        String text = param.get("text");

        step("Open Campaign & Rule Management Section");
        priceAdjustmentRulesPage.clickCampAndRuleManagementSidebarButton();

        step("Open Price Adjustment Rules Page");
        priceAdjustmentRulesPage.clickPriceAdjustmentRulesSidebarButton();
        Assertion.assertThat("Price Adjustment Rules Page is loaded",
                priceAdjustmentRulesPage.isPriceAdjustmentRulesPageLoaded(), is(true));

        step("Open Need Approval Section");
        priceAdjustmentRulesPage.clickNeedApprovalSection();

        step("Change the Vertical to TESTING");
        priceAdjustmentRulesPage.changeToOtherVertical();

        step("Click on Rules Changes Button");
        priceAdjustmentRulesPage.clickOnRuleChangesButton(2);

        step("Verify for the created then updated the rules only shown the New Rule Detail and let the Previous Rule Detail blank");
        Assertion.assertThat(
                "Verify for the created then updated the rules only shown the New Rule Detail and let the Previous Rule Detail blank",
                priceAdjustmentRulesPage.verifyPreviousRuleDetailIsBlank(), is(emptyString()));
    }

}