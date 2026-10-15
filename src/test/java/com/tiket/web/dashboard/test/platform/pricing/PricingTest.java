package com.tiket.web.dashboard.test.platform.pricing;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.WebTest;
import com.tiket.web.dashboard.page.common.LoginMemberAdmin;
import com.tiket.web.dashboard.page.member_dashboard.MemberLoginPage;
import com.tiket.web.dashboard.page.member_dashboard.MemberPage;
import com.tiket.web.dashboard.page.member_dashboard.PricingDashboardHome;
import com.tiket.web.dashboard.page.platform.pricing.PricingDashboardPageWeb;
import com.tiket.web.dashboard.page.platform.pricing.criterion_group.AddCriterionGroupPageWeb;
import com.tiket.web.dashboard.page.platform.pricing.criterion_group.CriterionGroupListPageWeb;
import com.tiket.web.dashboard.page.platform.pricing.nudges_system.NudgesSystemCreatePageWeb;
import com.tiket.web.dashboard.page.platform.pricing.nudges_system.NudgesSystemListPageMWeb;
import com.tiket.web.dashboard.page.platform.pricing.nudges_system.NudgesSystemListPageWeb;
import com.tiket.web.dashboard.page.pricing.SubsidyBudgeting.SubsidyBudgetingPage;
import com.tiket.web.dashboard.page.pricingDashboard.NudgesSystem.NudgesSystemPageWeb;

import org.testng.annotations.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.CoreMatchers.is;


public class PricingTest extends WebTest {
    @Author(names = { "Rizki" })
    @Module(name = "CriterionGroup")
    @Tribe(name = "Poseidon")
    @Vertical(name = "Pricing")
    @Test
    public void testPricingRemovedDataPermanently() {
        Map<String, String> param = getParam("testPricingRemovedDataPermanently");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");
        String groupName = param.get("groupName");
        groupName += CoreFunctions.randomInt(6);
        String vertical = param.get("vertical");
        String criterion = param.get("criterion");
        String criterionValue = param.get("criterionValue");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        PricingDashboardPageWeb<?> pricingDashboardPageWeb =  PricingDashboardPageWeb.getInstance(session);
        CriterionGroupListPageWeb<?> criterionGroupListPageWeb = CriterionGroupListPageWeb.getInstance(session);
        AddCriterionGroupPageWeb<?> addCriterionGroupPageWeb = AddCriterionGroupPageWeb.getInstance(session);

        step("Login");
        memberLoginPage.gotoMemberLoginPage();
        memberLoginPage.adminLogin(username, password);

        step("Go to Pricing Dashboard");
        pricingDashboardPageWeb.goToPage();

        step("Testing setup");
        pricingDashboardPageWeb.clickCriterionGroupMenu();
        criterionGroupListPageWeb.clickAddCriterionButton();
        addCriterionGroupPageWeb.selectVertical(vertical);
        addCriterionGroupPageWeb.selectCriterion(criterion);
        addCriterionGroupPageWeb.inputCriterionGroupName(groupName);
        addCriterionGroupPageWeb.selectInputManual();
        addCriterionGroupPageWeb.inputCriterionValue(criterionValue);
        addCriterionGroupPageWeb.clickSave();

        step("Delete Group Name");
        criterionGroupListPageWeb.inputVerticalSearch(vertical);
        criterionGroupListPageWeb.inputGroupNameSearch(groupName);
        criterionGroupListPageWeb.clickSearch();
        criterionGroupListPageWeb.clickSettingsGroupName(groupName);
        criterionGroupListPageWeb.clickDeleteInSettingsGroupName(groupName);
        criterionGroupListPageWeb.confirmCriterionGroupDeletion();

        step("Assertions");
        criterionGroupListPageWeb.clickSearch();
        Assertion.assertThat("Criterion group successfully deleted", criterionGroupListPageWeb.verifyCriterionGroupNotExist(groupName), is(true));
    }

    @Author(names = { "Rizki" })
    @Module(name = "NudgesSystem")
    @Tribe(name = "Poseidon")
    @Vertical(name = "Pricing")
    @Test
    public void testPricingUnableToDuplicatePastStartDate() {
        Map<String, String> param = getParam("testPricingUnableToDuplicatePastStartDate");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");
        String nudgeNameToBeDuplicated = param.get("nudgeNameToBeDuplicated");
        String nudgeName = param.get("nudgeName");
        String priority = param.get("priority");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        PricingDashboardPageWeb<?> pricingDashboardPageWeb =  PricingDashboardPageWeb.getInstance(session);
        NudgesSystemListPageWeb<?> nudgesSystemListPageWeb = NudgesSystemListPageMWeb.getInstance(session);
        NudgesSystemCreatePageWeb<?> nudgesSystemCreatePageWeb = NudgesSystemCreatePageWeb.getInstance(session);

        step("Log in to dashboard");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("Go to Pricing Dashboard");
        pricingDashboardPageWeb.goToPage();

        step("Go to Nudges System");
        pricingDashboardPageWeb.clickNudgesSystemMenu();

        step("Click duplicate on Nudge Rule");
        nudgesSystemListPageWeb.inputNudgeNameSearch(nudgeNameToBeDuplicated);
        nudgesSystemListPageWeb.clickSearch();
        nudgesSystemListPageWeb.clickSettingsButtonByNudgeName(nudgeNameToBeDuplicated);
        nudgesSystemListPageWeb.clickDuplicateButtonByNudgeName(nudgeNameToBeDuplicated);

        step("Input necessary data");
        nudgesSystemCreatePageWeb.inputNudgeName(nudgeName);
        nudgesSystemCreatePageWeb.inputNudgePriorityInput(priority);

        step("Change status to Ongoing");
        nudgesSystemCreatePageWeb.goToConditionScheduleStep();
        nudgesSystemCreatePageWeb.changeStatusToOngoing();
        nudgesSystemCreatePageWeb.goToSummaryStep();

        step("Assertions");
        Assertion.assertThat("Unable to duplicate", nudgesSystemCreatePageWeb.verifySnackbarError(), is(true));

        step("Log out");
        pricingDashboardPageWeb.logout();
    }

    @Author(names = { "Rizki" })
    @Module(name = "NudgesSystem")
    @Tribe(name = "Poseidon")
    @Vertical(name = "Pricing")
    @Test
    public void testPricingUnableDuplicateExistLayerAndPriority() {
        Map<String, String> param = getParam("testPricingUnableDuplicateExistLayerAndPriority");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");
        String nudgeNameToBeDuplicated = param.get("nudgeNameToBeDuplicated");
        String nudgeName = param.get("nudgeName");
        nudgeName += CoreFunctions.randomInt(6);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        PricingDashboardPageWeb<?> pricingDashboardPageWeb =  PricingDashboardPageWeb.getInstance(session);
        NudgesSystemListPageWeb<?> nudgesSystemListPageWeb = NudgesSystemListPageMWeb.getInstance(session);
        NudgesSystemCreatePageWeb<?> nudgesSystemCreatePageWeb = NudgesSystemCreatePageWeb.getInstance(session);

        step("Log in to dashboard");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("Go to Pricing Dashboard");
        pricingDashboardPageWeb.goToPage();

        step("Go to Nudges System");
        pricingDashboardPageWeb.clickNudgesSystemMenu();

        step("Click duplicate on Nudge Rule");
        nudgesSystemListPageWeb.inputNudgeNameSearch(nudgeNameToBeDuplicated);
        nudgesSystemListPageWeb.clickSearch();
        String priority = nudgesSystemListPageWeb.getPriorityValueByNudgeName(nudgeNameToBeDuplicated);
        nudgesSystemListPageWeb.clickSettingsButtonByNudgeName(nudgeNameToBeDuplicated);
        nudgesSystemListPageWeb.clickDuplicateButtonByNudgeName(nudgeNameToBeDuplicated);

        step("Fill the name with value that's not existed yet");
        nudgesSystemCreatePageWeb.inputNudgeName(nudgeName);

        step("Fill with existing Layer and Priority");
        nudgesSystemCreatePageWeb.inputNudgePriorityInput(priority);

        step("Direct to the Summary step");
        nudgesSystemCreatePageWeb.goToSummaryStep();

        step("Assertions");
        Assertion.assertThat("Returning error message Layer and Priority already exist", nudgesSystemCreatePageWeb.verifySnackbarErrorLayerPriorityExist(), is(true));

        step("Log out");
        pricingDashboardPageWeb.logout();
    }

    @Author(names = { "Rizki" })
    @Module(name = "NudgesSystem")
    @Tribe(name = "Poseidon")
    @Vertical(name = "Pricing")
    @Test
    public void testPricingUnableDuplicateRuleNameExist() {
        Map<String, String> param = getParam("testPricingUnableDuplicateRuleNameExist");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");
        String nudgeNameToBeDuplicated = param.get("nudgeNameToBeDuplicated");
        String priority = param.get("priority");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        PricingDashboardPageWeb<?> pricingDashboardPageWeb =  PricingDashboardPageWeb.getInstance(session);
        NudgesSystemListPageWeb<?> nudgesSystemListPageWeb = NudgesSystemListPageMWeb.getInstance(session);
        NudgesSystemCreatePageWeb<?> nudgesSystemCreatePageWeb = NudgesSystemCreatePageWeb.getInstance(session);

        step("Log in to dashboard");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("Go to Pricing Dashboard");
        pricingDashboardPageWeb.goToPage();

        step("Go to Nudges System");
        pricingDashboardPageWeb.clickNudgesSystemMenu();

        step("Click duplicate on Nudge Rule");
        nudgesSystemListPageWeb.inputNudgeNameSearch(nudgeNameToBeDuplicated);
        nudgesSystemListPageWeb.clickSearch();
        nudgesSystemListPageWeb.clickSettingsButtonByNudgeName(nudgeNameToBeDuplicated);
        nudgesSystemListPageWeb.clickDuplicateButtonByNudgeName(nudgeNameToBeDuplicated);

        step("Input necessary data");
        nudgesSystemCreatePageWeb.inputNudgeName(nudgeNameToBeDuplicated);
        nudgesSystemCreatePageWeb.inputNudgePriorityInput(priority);

        step("Save");
        nudgesSystemCreatePageWeb.goToSummaryStep();
        nudgesSystemCreatePageWeb.clickSave();

        step("Assertions");
        Assertion.assertThat("Return error message name already exist", nudgesSystemCreatePageWeb.verifySnackbarErrorDuplicateNudgeName(), is(true));

        step("Log out");
        nudgesSystemCreatePageWeb.closeSaveConfirmationModal();
        pricingDashboardPageWeb.logout();
    }

    @Author(names = { "Ram Pratap Singh" })
    @Module(name = "SubsidyBudgeting")
    @Tribe(name = "Poseidon")
    @Vertical(name = "Pricing")
    @Test
    public void testAccessUpdateSubsidyDashboard() {
        Map<String, String> param = getParam("testAccessUpdateSubsidyDashboard");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");
        String subsidyName = param.get("subsidyName");
        String quota = String.valueOf(CoreFunctions.generateRandomInt(1,5000));


        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        PricingDashboardPageWeb<?> pricingDashboardPageWeb =  PricingDashboardPageWeb.getInstance(session);
        SubsidyBudgetingPage<?> subsidyBudgetingPage = new SubsidyBudgetingPage<>(session);

        step("Login");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("Go to Pricing Dashboard");
        pricingDashboardPageWeb.goToPage();

        step("Go to Subsidy Budgeting");
        pricingDashboardPageWeb.clickSubsidyBudgetingMenu();

        step("Click on Show Filter Button");
        subsidyBudgetingPage.clickOnShowFilterButton();

        step("search subsidy budget");
        pricingDashboardPageWeb.searchSubsidyBudgeting(subsidyName);

        step("User click settings button");
        pricingDashboardPageWeb.clickSettingsButton();

        step("User click edit button");
        pricingDashboardPageWeb.clickEditButton();

        step("User update quota");
        pricingDashboardPageWeb.updateQuota(quota);

        step("User click save button");
        pricingDashboardPageWeb.clickSaveButton();

        step("Assertions");
        Assertion.assertThat("Successfully edited the budget.", pricingDashboardPageWeb.verifyBudgetUpdated(), is(true));

        step("Log out");
        pricingDashboardPageWeb.logout();
    }

    @Author(names = { "Ram Pratap Singh" })
    @Module(name = "CriterionGroup")
    @Tribe(name = "Poseidon")
    @Vertical(name = "Pricing")
    @Test
    public void testVerifyAdminPrivilegeToCriterionGroup() {
        Map<String, String> param = getParam("testVerifyAdminPrivilegeToCriterionGroup");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        PricingDashboardPageWeb<?> pricingDashboardPageWeb = PricingDashboardPageWeb.getInstance(session);
        CriterionGroupListPageWeb<?> criterionGroupListPageWeb = CriterionGroupListPageWeb.getInstance(session);

        step("GIVEN user has privilege to access 'Criterion Group' module with admin role");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("AND user on Pricing Dashboard page");
        pricingDashboardPageWeb.goToPage();

        step("WHEN user click on the module of 'Criterion Group'");
        pricingDashboardPageWeb.clickCriterionGroupMenu();

        step("THEN redirected to 'Criterion Group' page");
        Assertion.assertThat("User should be redirected to Criterion Group page",
                criterionGroupListPageWeb.verifyCriterionGroupPageRedirect(), is(true));

        step("AND able to see all the 'Criterion Group' data");
        Assertion.assertThat("Criterion Group page header should be displayed",
                criterionGroupListPageWeb.verifyCriterionGroupPageHeaderDisplayed(), is(true));

        Assertion.assertThat("Criterion Group data should be displayed and accessible",
                criterionGroupListPageWeb.verifyCriterionGroupDataDisplayed(), is(true));

        Assertion.assertThat("User should have full access to Criterion Group module",
                criterionGroupListPageWeb.verifyUserHasAccessToCriterionGroup(), is(true));

        step("Log out");
        pricingDashboardPageWeb.logout();
    }

    @Author(names = { "Ram Pratap Singh" })
    @Module(name = "NudgesSystem")
    @Tribe(name = "Poseidon")
    @Vertical(name = "Pricing")
    @Test
    public void testVerifyQuickAccessToNudgeRuleDetails() {
        Map<String, String> param = getParam("testVerifyQuickAccessToNudgeRuleDetails");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");
        String nudgeName = param.get("nudgeName");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        PricingDashboardPageWeb<?> pricingDashboardPageWeb = PricingDashboardPageWeb.getInstance(session);
        NudgesSystemListPageWeb<?> nudgesSystemListPageWeb = NudgesSystemListPageWeb.getInstance(session);

        step("GIVEN nudge rule are created AND present in Nudges System list page");
        memberLoginPage.adminLoginViaCookie(username, password);

        step("Navigate to Pricing Dashboard");
        pricingDashboardPageWeb.goToPage();

        step("Go to Nudges System");
        pricingDashboardPageWeb.clickNudgesSystemMenu();

        step("Search for specific nudge rule");
        nudgesSystemListPageWeb.inputNudgeNameSearch(nudgeName);
        nudgesSystemListPageWeb.clickSearch();

        step("WHEN select one of nudges rule AND click on the 'Settings.Details'");
        nudgesSystemListPageWeb.clickDetailsOptionByNudgeName(nudgeName);

        step("THEN should view a modal displaying the comprehensive details of that nudge rule");
        Assertion.assertThat("Nudge details modal should be displayed",
                nudgesSystemListPageWeb.verifyNudgeDetailsModalDisplayed(), is(true));

        step("AND Modal will contains all data related to the rule that is shown in the summary page");
        Assertion.assertThat("Modal should contain comprehensive rule details",
                nudgesSystemListPageWeb.verifyComprehensiveNudgeDetailsModal(), is(true));

        Assertion.assertThat("Modal should contain rule details data",
                nudgesSystemListPageWeb.verifyModalContainsRuleDetails(), is(true));

        step("AND There will be an edit button in the modal, when it clicked user will be redirected to edit page for that particular rule");
        Assertion.assertThat("Edit button should be displayed in modal",
                nudgesSystemListPageWeb.verifyModalEditButtonDisplayed(), is(true));

        step("Click edit button and verify redirect to edit page");
        nudgesSystemListPageWeb.clickEditButtonInModal();
        Assertion.assertThat("User should be redirected to edit page",
                nudgesSystemListPageWeb.verifyRedirectToEditPage(), is(true));

        step("Log out");
        pricingDashboardPageWeb.logout();
    }

    @Author(names = { "Muksalmina" })
    @Module(name = "CriterionGroup")
    @Tribe(name = "Pricing")
    @Vertical(name = "Pricing")
    @Test
    public void testVerifyCriterionGroupAccessAndOperations() {
        Map<String, String> param = getParam("testVerifyCriterionGroupAccessAndOperations");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");
        String groupName = param.get("groupName") + CoreFunctions.randomInt(6);
        String duplicateGroupName = param.get("duplicateGroupName");
        String usedGroupName = param.get("usedGroupName");
        String vertical = param.get("vertical");
        String criterion = param.get("criterion");
        String criterionKey = param.get("criterionKey");
        String criterionValue = param.get("criterionValue");
        String newCriterionValue = param.get("newCriterionValue");
        String additionalCriterionValue = param.get("additionalCriterionValue");
        String nonExistentGroupName = param.get("nonExistentGroupName");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        PricingDashboardPageWeb<?> pricingDashboardPageWeb = PricingDashboardPageWeb.getInstance(session);
        CriterionGroupListPageWeb<?> criterionGroupListPage = CriterionGroupListPageWeb.getInstance(session);
        AddCriterionGroupPageWeb<?> addCriterionGroupPage = AddCriterionGroupPageWeb.getInstance(session);

        step("Login and navigate to Criterion Group");
        memberLoginPage.adminLoginViaCookie(username, password);
        pricingDashboardPageWeb.goToPage();
        pricingDashboardPageWeb.clickCriterionGroupMenu();

        // ===== 1a. Create Positive: Download CSV Template then Upload CSV =====
        step("[Create Positive - CSV] Upload CSV with valid values to create new Criterion Group");
        criterionGroupListPage.clickAddCriterionButton();
        addCriterionGroupPage.selectVertical(vertical);
        addCriterionGroupPage.selectCriterion(criterion);
        addCriterionGroupPage.inputCriterionGroupName(groupName + "CSV");
        addCriterionGroupPage.selectCSVUploadOption();
        String csvFilePath = System.getProperty("user.dir") + "/src/test/resources/dashboard/pricing/criterion_group.csv";
        addCriterionGroupPage.uploadCSVFile(csvFilePath);
        addCriterionGroupPage.clickSave();
        Assertion.assertThat("[Create Positive - CSV] Criterion Group created successfully via CSV Upload",
                criterionGroupListPage.verifyCreateSuccessMessage(), is(true));

        // ===== 1b. Create Positive: Manual Input =====
        step("[Create Positive - Manual] Add Criterion Group via Manual Input with valid values");
        criterionGroupListPage.clickAddCriterionButton();
        addCriterionGroupPage.selectVertical(vertical);
        addCriterionGroupPage.selectCriterion(criterion);
        addCriterionGroupPage.inputCriterionGroupName(groupName);
        addCriterionGroupPage.selectInputManual();
        addCriterionGroupPage.inputCriterionValue(criterionValue);
        addCriterionGroupPage.clickSave();

        criterionGroupListPage.inputVerticalSearch(vertical);
        criterionGroupListPage.inputGroupNameSearch(groupName);
        criterionGroupListPage.clickSearch();
        Assertion.assertThat("[Create Positive - Manual] Criterion Group created successfully via Manual Input",
                criterionGroupListPage.verifyCriterionGroupNotExist(groupName), is(false));

        // ===== 1c. Create Negative: Duplicate Name =====
        step("[Create Negative] Attempt to create with an already existing Criterion Group name");
        criterionGroupListPage.clickAddCriterionButton();
        addCriterionGroupPage.selectVertical(vertical);
        addCriterionGroupPage.selectCriterion(criterion);
        addCriterionGroupPage.inputCriterionGroupName(duplicateGroupName);
        addCriterionGroupPage.selectInputManual();
        addCriterionGroupPage.inputCriterionValue(criterionValue);
        addCriterionGroupPage.clickSave();
        Assertion.assertThat("[Create Negative] Error displayed for duplicate Criterion Group name",
                addCriterionGroupPage.verifyDuplicateNameError(), is(true));

        // ===== 2a. Update Positive: Replace existing criterion value =====
        step("[Update Positive - Replace] Navigate to list and open Edit for the created Criterion Group");
        pricingDashboardPageWeb.clickCriterionGroupMenu();
        criterionGroupListPage.inputVerticalSearch(vertical);
        criterionGroupListPage.inputGroupNameSearch(groupName);
        criterionGroupListPage.clickSearch();
        criterionGroupListPage.clickSettingsGroupName(groupName);
        criterionGroupListPage.clickEditInSettingsGroupName(groupName);
        Assertion.assertThat("[Update Positive - Replace] Redirected to Edit page",
                criterionGroupListPage.verifyOnEditPage(), is(true));

        step("[Update Positive - Replace] Delete the existing criterion value and replace with a new value");
        addCriterionGroupPage.deleteExistingCriterionValue(criterionValue);
        addCriterionGroupPage.inputCriterionValueOnEdit(newCriterionValue);
        addCriterionGroupPage.clickSave();
        Assertion.assertThat("[Update Positive - Replace] Criterion Group saved successfully with replaced value",
                criterionGroupListPage.verifyEditSuccessMessage(), is(true));

        // ===== 2b. Update Positive: Add a new criterion value =====
        step("[Update Positive - Add] Open Edit for the same Criterion Group");
        pricingDashboardPageWeb.clickCriterionGroupMenu();
        criterionGroupListPage.inputVerticalSearch(vertical);
        criterionGroupListPage.inputGroupNameSearch(groupName);
        criterionGroupListPage.clickSearch();
        criterionGroupListPage.clickSettingsGroupName(groupName);
        criterionGroupListPage.clickEditInSettingsGroupName(groupName);
        Assertion.assertThat("[Update Positive - Add] Redirected to Edit page",
                criterionGroupListPage.verifyOnEditPage(), is(true));

        step("[Update Positive - Add] Add a new criterion value alongside existing values");
        Assertion.assertThat("[Update Positive - Add] Existing value is present before adding",
                addCriterionGroupPage.verifyValueExistsInEditPage(newCriterionValue), is(true));
        addCriterionGroupPage.inputCriterionValueOnEdit(additionalCriterionValue);
        addCriterionGroupPage.clickSave();
        Assertion.assertThat("[Update Positive - Add] Criterion Group saved successfully with new value added",
                criterionGroupListPage.verifyEditSuccessMessage(), is(true));

        // ===== 3a. Filter Positive: by Group Name =====
        step("[Filter Positive] Filter Criterion Group list by Group Name");
        pricingDashboardPageWeb.clickCriterionGroupMenu();
        criterionGroupListPage.inputVerticalSearch(vertical);
        criterionGroupListPage.inputGroupNameSearch(groupName);
        criterionGroupListPage.clickSearch();
        Assertion.assertThat("[Filter Positive - Group Name] Results contain the searched Criterion Group",
                criterionGroupListPage.verifyFilterResultsContain(groupName), is(true));
        criterionGroupListPage.clickResetFilterButton();

        // ===== 3b. Filter Positive: by Criterion Key =====
        step("[Filter Positive] Filter Criterion Group list by Criterion Key");
        criterionGroupListPage.inputVerticalSearch(vertical);
        criterionGroupListPage.inputKeySearch(criterionKey);
        criterionGroupListPage.clickSearch();
        Assertion.assertThat("[Filter Positive - Key] Results displayed when filtering by Criterion Key",
                criterionGroupListPage.verifyFilterResultsContain(groupName), is(true));
        criterionGroupListPage.clickResetFilterButton();

        // ===== 3c. Filter Positive: by Vertical =====
        step("[Filter Positive] Filter Criterion Group list by Vertical");
        pricingDashboardPageWeb.clickCriterionGroupMenu();
        criterionGroupListPage.inputVerticalSearch(vertical);
        criterionGroupListPage.clickSearch();
        Assertion.assertThat("[Filter Positive - Vertical] Results displayed when filtering by Vertical",
                criterionGroupListPage.verifyCriterionGroupDataDisplayed(), is(true));
        criterionGroupListPage.clickResetFilterButton();

        // ===== 3d. Filter Negative: No Results =====
        step("[Filter Negative] Filter with a non-existent Criterion Group name");
        pricingDashboardPageWeb.clickCriterionGroupMenu();
        criterionGroupListPage.inputVerticalSearch(vertical);
        criterionGroupListPage.inputGroupNameSearch(nonExistentGroupName);
        criterionGroupListPage.clickSearch();
        Assertion.assertThat("[Filter Negative] No data found for non-existent Criterion Group name",
                criterionGroupListPage.verifyNoDataFound(), is(true));

        // ===== 7. Reset Filter =====
        step("[Reset Filter] Reset all applied filters and verify default list is restored");
        criterionGroupListPage.clickResetFilterButton();
        Assertion.assertThat("[Reset Filter] Filter cleared and Criterion Group list restored",
                criterionGroupListPage.verifyFilterIsReset(), is(true));

        // ===== 4. Download from Settings =====
        step("[Download] Search for the Criterion Group and download via Settings");
        criterionGroupListPage.inputVerticalSearch(vertical);
        criterionGroupListPage.inputGroupNameSearch(groupName);
        criterionGroupListPage.clickSearch();
        criterionGroupListPage.clickSettingsGroupName(groupName);
        criterionGroupListPage.clickDownloadInSettingsGroupName(groupName);
        Assertion.assertThat("[Download] Criterion Group download initiated successfully",
                criterionGroupListPage.verifyDownloadInitiated(), is(true));

        // ===== 5. Delete Positive: Unused CG =====
        step("[Delete Positive] Delete the manually-created Criterion Group");
        criterionGroupListPage.inputGroupNameSearch(groupName);
        criterionGroupListPage.clickSearch();
        criterionGroupListPage.clickSettingsGroupName(groupName);
        criterionGroupListPage.clickDeleteInSettingsGroupName(groupName);
        criterionGroupListPage.confirmCriterionGroupDeletion();
        criterionGroupListPage.clickSearch();
        Assertion.assertThat("[Delete Positive] Manual Criterion Group permanently deleted",
                criterionGroupListPage.verifyCriterionGroupNotExist(groupName), is(true));

        step("[Delete Positive] Delete the CSV-uploaded Criterion Group");
        criterionGroupListPage.inputGroupNameSearch(groupName + "CSV");
        criterionGroupListPage.clickSearch();
        criterionGroupListPage.clickSettingsGroupName(groupName + "CSV");
        criterionGroupListPage.clickDeleteInSettingsGroupName(groupName + "CSV");
        criterionGroupListPage.confirmCriterionGroupDeletion();
        criterionGroupListPage.clickSearch();
        Assertion.assertThat("[Delete Positive] CSV Criterion Group permanently deleted",
                criterionGroupListPage.verifyCriterionGroupNotExist(groupName + "CSV"), is(true));

        // ===== 6. Delete Negative: Used CG =====
        step("[Delete Negative] Attempt to delete a Criterion Group linked to a Price Adjustment Rule");
        criterionGroupListPage.inputGroupNameSearch(usedGroupName);
        criterionGroupListPage.clickSearch();
        criterionGroupListPage.clickSettingsGroupName(usedGroupName);
        criterionGroupListPage.clickDeleteInSettingsGroupName(usedGroupName);
        criterionGroupListPage.confirmCriterionGroupDeletion();
        Assertion.assertThat("[Delete Negative] Error displayed when deleting a Criterion Group in use",
                criterionGroupListPage.verifyDeleteWithDependencyError(), is(true));

        step("Log out");
        pricingDashboardPageWeb.logout();
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "CriterionGroup")
    @Tribe(name = "Poseidon")
    @Vertical(name = "Pricing")
    @Test
    public void verifyUserCantAccessCriterionGroup() {
        Map<String, String> param = getParam("verifyUserCantAccessCriterionGroup");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        MemberPage<?> memberPage = new MemberPage<>(session);
        PricingDashboardHome<?> pricingDashboardHome = new PricingDashboardHome<>(session);
        CriterionGroupListPageWeb<?> criterionGroupPage = new CriterionGroupListPageWeb<>(session);

        String text = param.get("text");

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

        step("Open Criterion Group Section");
        criterionGroupPage.clickCriterionGroupSideBarButton();

        step("Verify user without privileges cannot access the Criterion Group module");
        Assertion.assertThat("Verify user without privileges cannot access the Criterion Group module", criterionGroupPage.verifyPageIsNotAccessible(text), is(true));
    }
    
    @Author(names = {"Nasha"})
    @Module(name = "NudgesSystem")
    @Tribe(name = "Pricing")
    @Vertical(name = "Pricing")
    @Test
    public void testVerifyAccessCreationAndConfigurationOfNudgesrules() {
        Map<String, String> param = getParam("testVerifyAccessCreationAndConfigurationOfNudgesrules");
        BaseTest.paramsThreadLocal.set(param);
        
        String username = param.get("username");
        String password = param.get("password");
        String vertical = param.get("vertical");
        String priority = String.valueOf(CoreFunctions.randomInt(2));
        String campaignId = param.getOrDefault("campaignId", "123");
        String layer = param.getOrDefault("layer", "Landing Page"); 
        String baseNudgeName = "aut_" + CoreFunctions.randomName() + "_" + CoreFunctions.randomInt(4);
        String message = param.get("message");
        String funnel = param.get("funnel");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        PricingDashboardPageWeb<?> pricingDashboardPageWeb = PricingDashboardPageWeb.getInstance(session);
        NudgesSystemPageWeb<?> nudgesSystemPage = NudgesSystemPageWeb.getInstance(session);
        NudgesSystemListPageWeb<?> nudgesSystemListPage = NudgesSystemListPageWeb.getInstance(session);
        NudgesSystemCreatePageWeb<?> nudgesSystemCreatePage = NudgesSystemCreatePageWeb.getInstance(session);

        // Track every rule created in this run so teardown can delete them (avoids list bloat).
        // Value = isOngoing: ALL_TIME + ONGOING rules can't be deleted directly and must be paused first.
        Map<String, Boolean> createdRules = new LinkedHashMap<>();

        try {
            // 1. Examples: Menu and Access
            step("[Menu & Access] View and access the Nudges System menu on the dashboard");
            memberLoginPage.gotoMemberLoginPage();
            memberLoginPage.adminLogin(username, password);
            pricingDashboardPageWeb.goToPage();
            nudgesSystemPage.goToNudgesSystemPage();
            Assertion.assertThat("Grant access and display the Nudges System page", nudgesSystemPage.isNudgesSystemPageLoaded(), is(true));

            // 2a. Examples: Basic Rule Management - Create
            String baseRuleName = baseNudgeName + "_Base";
            createdRules.put(baseRuleName, false);
            step("[Create] Successfully create a new Nudges rule by filling in all mandatory fields");
            nudgesSystemPage.clickAddNudgeButton();
            nudgesSystemCreatePage.fillDynamicGeneralRule(vertical, baseRuleName, layer, priority);
            nudgesSystemCreatePage.fillDynamicTargetAction(funnel, campaignId, message);
            nudgesSystemCreatePage.fillDynamicConditionAndSchedule("ALL_TIME", "PAUSED");
            nudgesSystemCreatePage.goToSummaryStep();
            nudgesSystemCreatePage.clickSave();
            nudgesSystemListPage.inputNudgeNameSearch(baseRuleName);
            nudgesSystemListPage.clickSearch();
            Assertion.assertThat("Verify new Nudges rule is successfully created", nudgesSystemPage.verifyNudgeName(baseRuleName), is(true));

            // 2b. Examples: Basic Rule Management - Duplicate
            String duplicateRuleName = baseRuleName + "_Dupe";
            createdRules.put(duplicateRuleName, false);
            step("[Duplicate] Duplicate a rule and fill mandatory fields to save it");
            nudgesSystemListPage.clickSettingsButtonByNudgeName(baseRuleName);
            nudgesSystemListPage.clickDuplicateButtonByNudgeName(baseRuleName);
            nudgesSystemCreatePage.inputNudgeName(duplicateRuleName);
            nudgesSystemCreatePage.inputNudgePriorityInput(priority);
            nudgesSystemCreatePage.goToTargetActionStep();
            nudgesSystemCreatePage.goToConditionScheduleStep();
            nudgesSystemCreatePage.goToSummaryStep();
            nudgesSystemCreatePage.clickSave();
            nudgesSystemListPage.inputNudgeNameSearch(duplicateRuleName);
            nudgesSystemListPage.clickSearch();
            Assertion.assertThat("Verify duplicated Nudges rule is successfully saved",  nudgesSystemPage.verifyNudgeName(duplicateRuleName), is(true));

            // 3. Examples: End-to-End (E2E) Rule Configurations
            // E2E 1: Page = Landing Page, Schedule = All time, Status = Paused
            priority = String.valueOf(CoreFunctions.randomInt(2));
            String rule1 = baseNudgeName + "_LP_Paused";
            createdRules.put(rule1, false);
            step("[E2E Create] Page = Landing Page, Schedule = All time, Status = Paused");
            nudgesSystemPage.clickAddNudgeButton();
            nudgesSystemCreatePage.fillDynamicGeneralRule(vertical, rule1, layer, priority);
            nudgesSystemCreatePage.fillDynamicTargetAction("Landing Page", campaignId, message);
            nudgesSystemCreatePage.fillDynamicConditionAndSchedule("ALL_TIME", "PAUSED");
            nudgesSystemCreatePage.goToSummaryStep();
            nudgesSystemCreatePage.clickSave();
            nudgesSystemListPage.inputNudgeNameSearch(rule1);
            nudgesSystemListPage.clickSearch();
            Assertion.assertThat("Verify new Nudges rule with Landing Page, All time, Paused is successfully created", nudgesSystemPage.verifyNudgeName(rule1), is(true));

            // E2E 2: Page = PDP, Schedule = All time, Status = Ongoing
            String rule2 = baseNudgeName + "_PDP_Ongoing";
            createdRules.put(rule2, true); // ALL_TIME + ONGOING -> must be paused before delete
            step("[E2E Create] Page = PDP, Schedule = All time, Status = Ongoing");
            nudgesSystemPage.clickAddNudgeButton();
            nudgesSystemCreatePage.fillDynamicGeneralRule(vertical, rule2, "PDP", priority);
            nudgesSystemCreatePage.fillDynamicTargetAction("Product Display Page", campaignId, message);
            nudgesSystemCreatePage.fillDynamicConditionAndSchedule("ALL_TIME", "ONGOING");
            nudgesSystemCreatePage.goToSummaryStep();
            nudgesSystemCreatePage.clickSave();
            nudgesSystemListPage.inputNudgeNameSearch(rule2);
            nudgesSystemListPage.clickSearch();
            Assertion.assertThat("Verify new Nudges rule with PDP, All time, Ongoing is successfully created", nudgesSystemPage.verifyNudgeName(rule2), is(true));

            // E2E 3: Page = Booking Form, Schedule = All time, Status = Ongoing
            String rule3 = baseNudgeName + "_BF_AllTime_Ongoing";
            createdRules.put(rule3, true); // ALL_TIME + ONGOING -> must be paused before delete
            step("[E2E Create] Page = Booking Form, Schedule = All time, Status = Ongoing");
            nudgesSystemPage.clickAddNudgeButton();
            nudgesSystemCreatePage.fillDynamicGeneralRule(vertical, rule3, "Booking Form", priority);
            nudgesSystemCreatePage.fillDynamicTargetAction("Booking Form", campaignId, message);
            nudgesSystemCreatePage.fillDynamicConditionAndSchedule("ALL_TIME", "ONGOING");
            nudgesSystemCreatePage.goToSummaryStep();
            nudgesSystemCreatePage.clickSave();
            nudgesSystemListPage.inputNudgeNameSearch(rule3);
            nudgesSystemListPage.clickSearch();
            Assertion.assertThat("Verify new Nudges rule with Booking Form, All time, Ongoing is successfully created", nudgesSystemPage.verifyNudgeName(rule3), is(true));

            // E2E 4: Page = Booking Form, Schedule = All time, Status = Paused
            priority = String.valueOf(CoreFunctions.randomInt(2));
            String rule4 = baseNudgeName + "_BF_AllTime_Paused";
            createdRules.put(rule4, false);
            step("[E2E Create] Page = Booking Form, Schedule = All time, Status = Paused");
            nudgesSystemPage.clickAddNudgeButton();
            nudgesSystemCreatePage.fillDynamicGeneralRule(vertical, rule4, "Booking Form", priority);
            nudgesSystemCreatePage.fillDynamicTargetAction("Booking Form", campaignId, message);
            nudgesSystemCreatePage.fillDynamicConditionAndSchedule("ALL_TIME", "PAUSED");
            nudgesSystemCreatePage.goToSummaryStep();
            nudgesSystemCreatePage.clickSave();
            nudgesSystemListPage.inputNudgeNameSearch(rule4);
            nudgesSystemListPage.clickSearch();
            Assertion.assertThat("Verify new Nudges rule with Booking Form, All time, Paused is successfully created", nudgesSystemPage.verifyNudgeName(rule4), is(true));

            // E2E 5: Page = Booking Form, Schedule = Scheduled, Status = Paused
            priority = String.valueOf(CoreFunctions.randomInt(2));
            String rule5 = baseNudgeName + "_BF_Scheduled_Paused";
            createdRules.put(rule5, false);
            step("[E2E Create] Page = Booking Form, Schedule = Scheduled, Status = Paused");
            nudgesSystemPage.clickAddNudgeButton();
            nudgesSystemCreatePage.fillDynamicGeneralRule(vertical, rule5, "Booking Form", priority);
            nudgesSystemCreatePage.fillDynamicTargetAction("Booking Form", campaignId, message);
            nudgesSystemCreatePage.fillDynamicConditionAndSchedule("SCHEDULED", "PAUSED");
            nudgesSystemCreatePage.goToSummaryStep();
            nudgesSystemCreatePage.clickSave();
            nudgesSystemListPage.inputNudgeNameSearch(rule5);
            nudgesSystemListPage.clickSearch();
            Assertion.assertThat("Verify new Nudges rule with Booking Form, Scheduled, Paused is successfully created", nudgesSystemPage.verifyNudgeName(rule5), is(true));

            // E2E 6: Page = Booking Form, Schedule = Scheduled, Status = Ongoing
            String rule6 = baseNudgeName + "_BF_Scheduled_Ongoing";
            createdRules.put(rule6, false); // SCHEDULED status is deletable directly
            priority = String.valueOf(CoreFunctions.randomInt(2));
            step("[E2E Create] Page = Booking Form, Schedule = Scheduled, Status = Ongoing");
            nudgesSystemPage.clickAddNudgeButton();
            nudgesSystemCreatePage.fillDynamicGeneralRule(vertical, rule6, "Booking Form", priority);
            nudgesSystemCreatePage.fillDynamicTargetAction("Booking Form", campaignId, message);
            nudgesSystemCreatePage.fillDynamicConditionAndSchedule("SCHEDULED", "ONGOING");
            nudgesSystemCreatePage.goToSummaryStep();
            nudgesSystemCreatePage.clickSave();
            nudgesSystemListPage.inputNudgeNameSearch(rule6);
            nudgesSystemListPage.clickSearch();
            Assertion.assertThat("Verify new Nudges rule with Booking Form, Scheduled, Ongoing is successfully created", nudgesSystemPage.verifyNudgeName(rule6), is(true));
        } finally {
            // Teardown: delete every rule created above so the list doesn't accumulate across runs.
            // Runs even if an assertion above fails, which is exactly when leftovers would be created.
            cleanupNudgesRules(nudgesSystemPage, nudgesSystemListPage, createdRules);
            step("Log out session");
            pricingDashboardPageWeb.logout();
        }
    }

    /**
     * Deletes the given Nudges rules by exact name, reusing the proven search -> settings -> delete flow.
     * ALL_TIME + ONGOING rules (isOngoing == true) can't be deleted directly, so they are first paused via
     * the edit form (edit -> Condition & Schedule -> set status Paused -> save) before deletion.
     * Each deletion is isolated so one failure (e.g. a rule that was never created) doesn't abort the rest.
     */
    private void cleanupNudgesRules(NudgesSystemPageWeb<?> nudgesSystemPage,
                                    NudgesSystemListPageWeb<?> nudgesSystemListPage,
                                    Map<String, Boolean> nudgeRules) {
        for (Map.Entry<String, Boolean> entry : nudgeRules.entrySet()) {
            String nudgeName = entry.getKey();
            boolean isOngoing = Boolean.TRUE.equals(entry.getValue());
            try {
                if (isOngoing) {
                    // Pause the rule first (edit -> set status Paused -> save) so the platform allows deletion.
                    nudgesSystemPage.goToNudgesSystemPage();
                    nudgesSystemListPage.inputNudgeNameSearch(nudgeName);
                    nudgesSystemListPage.clickSearch();
                    nudgesSystemListPage.clickSettingsButtonByNudgeName(nudgeName);
                    nudgesSystemPage.clickEditButton();
                    nudgesSystemPage.updateStatusToPaused();
                }
                nudgesSystemPage.goToNudgesSystemPage();
                nudgesSystemListPage.inputNudgeNameSearch(nudgeName);
                nudgesSystemListPage.clickSearch();
                nudgesSystemListPage.clickSettingsButtonByNudgeName(nudgeName);
                nudgesSystemListPage.clickDeleteButton();
                nudgesSystemListPage.clickConfirmDeleteButton();
            } catch (Exception e) {
                step("[Teardown] Could not delete Nudges rule '" + nudgeName + "' -> " + e.getMessage());
            }
        }
    }

    @Author(names = {"Nasha"})
    @Module(name = "NudgesSystem")
    @Tribe(name = "Pricing")
    @Vertical(name = "Pricing")
    @Test
    public void testVerifyDeletionRulesForNudgesBasedOnStatus() {
        Map<String, String> param = getParam("testVerifyDeletionRulesForNudgesBasedOnStatus");
        BaseTest.paramsThreadLocal.set(param);
        
        String username = param.get("username");
        String password = param.get("password");
        String vertical = param.getOrDefault("vertical", "FLIGHT");
        String campaignId = param.getOrDefault("campaignId", "123");
        String message = param.getOrDefault("message", "Default Message");
        String layer = param.getOrDefault("layer", "Landing Page");
        String priority = String.valueOf(CoreFunctions.randomInt(2));

        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        PricingDashboardPageWeb<?> pricingDashboardPageWeb = PricingDashboardPageWeb.getInstance(session);
        NudgesSystemPageWeb<?> nudgesSystemPage = NudgesSystemPageWeb.getInstance(session);
        NudgesSystemListPageWeb<?> nudgesSystemListPage = NudgesSystemListPageWeb.getInstance(session);
        NudgesSystemCreatePageWeb<?> nudgesSystemCreatePage = NudgesSystemCreatePageWeb.getInstance(session);

        String baseName = "aut_delete_" + CoreFunctions.randomName() + "_" + CoreFunctions.randomInt(4);
        String targetScheduled = baseName + "_SCHEDULED";
        String targetPaused = baseName + "_PAUSED";
        String targetOngoing = baseName + "_ONGOING";

        step("[Setup] Login and navigate to Nudges System Dashboard");
        memberLoginPage.gotoMemberLoginPage();
        memberLoginPage.adminLogin(username, password);
        pricingDashboardPageWeb.goToPage();
        nudgesSystemPage.goToNudgesSystemPage();

        step("[Precondition] Create SCHEDULED rule");
        nudgesSystemPage.clickAddNudgeButton();
        nudgesSystemCreatePage.fillDynamicGeneralRule(vertical, targetScheduled, layer, priority);
        nudgesSystemCreatePage.fillDynamicTargetAction(layer, campaignId, message);
        nudgesSystemCreatePage.fillDynamicConditionAndSchedule("SCHEDULED", "PAUSED");
        nudgesSystemCreatePage.goToSummaryStep();
        nudgesSystemCreatePage.clickSave();

        step("[Precondition] Create PAUSED rule");
        priority = String.valueOf(CoreFunctions.randomInt(2));
        nudgesSystemPage.clickAddNudgeButton();
        nudgesSystemCreatePage.fillDynamicGeneralRule(vertical, targetPaused, layer, priority);
        nudgesSystemCreatePage.fillDynamicTargetAction(layer, campaignId, message);
        nudgesSystemCreatePage.fillDynamicConditionAndSchedule("ALL_TIME", "PAUSED");
        nudgesSystemCreatePage.goToSummaryStep();
        nudgesSystemCreatePage.clickSave();

        step("[Precondition] Create ONGOING rule");
        priority = String.valueOf(CoreFunctions.randomInt(2));
        nudgesSystemPage.clickAddNudgeButton();
        nudgesSystemCreatePage.fillDynamicGeneralRule(vertical, targetOngoing, layer, priority);
        nudgesSystemCreatePage.fillDynamicTargetAction(layer, campaignId, message);
        nudgesSystemCreatePage.fillDynamicConditionAndSchedule("ALL_TIME", "ONGOING");
        nudgesSystemCreatePage.goToSummaryStep();
        nudgesSystemCreatePage.clickSave();

        step("Filter and delete the created Scheduled rule: " + targetScheduled);
        nudgesSystemPage.goToNudgesSystemPage(); 
        nudgesSystemListPage.inputNudgeNameSearch(targetScheduled);
        nudgesSystemListPage.clickSearch();
        nudgesSystemListPage.clickSettingsButtonByNudgeName(targetScheduled);
        nudgesSystemListPage.clickDeleteButton();
        nudgesSystemListPage.clickConfirmDeleteButton();
        nudgesSystemListPage.inputNudgeNameSearch(targetScheduled);
        nudgesSystemListPage.clickSearch();
        Assertion.assertThat("Verify Scheduled rule is removed", nudgesSystemPage.verifyNudgeRuleIsDeleted(), is(true));

        step("Filter and delete the created Paused rule: " + targetPaused);
        nudgesSystemPage.goToNudgesSystemPage();
        nudgesSystemListPage.inputNudgeNameSearch(targetPaused);
        nudgesSystemListPage.clickSearch();
        nudgesSystemListPage.clickSettingsButtonByNudgeName(targetPaused);
        nudgesSystemListPage.clickDeleteButton();
        nudgesSystemListPage.clickConfirmDeleteButton();
        
        nudgesSystemListPage.inputNudgeNameSearch(targetPaused);
        nudgesSystemListPage.clickSearch();
        Assertion.assertThat("Verify Pure Paused rule is removed", nudgesSystemPage.verifyNudgeRuleIsDeleted(), is(true));

        step("Attempt to delete Ongoing rule and expect restriction error: " + targetOngoing);
        nudgesSystemPage.goToNudgesSystemPage();
        nudgesSystemListPage.inputNudgeNameSearch(targetOngoing);
        nudgesSystemListPage.clickSearch();
        nudgesSystemListPage.clickSettingsButtonByNudgeName(targetOngoing);
        nudgesSystemListPage.clickDeleteButton();

        Assertion.assertThat("Verify deletion is blocked for Ongoing rule", nudgesSystemListPage.isDeletionRestrictionErrorDisplayed(), is(true));
        nudgesSystemListPage.clickIUnderstandButton();

        step("[Cleanup] Deactivate then delete the Ongoing rule: " + targetOngoing);
        nudgesSystemPage.goToNudgesSystemPage();
        nudgesSystemListPage.inputNudgeNameSearch(targetOngoing);
        nudgesSystemListPage.clickSearch();
        nudgesSystemListPage.clickSettingsButtonByNudgeName(targetOngoing);
        nudgesSystemPage.clickEditButton();
        nudgesSystemPage.updateStatusToPaused();          // edit -> set status Paused -> save

        nudgesSystemPage.goToNudgesSystemPage();
        nudgesSystemListPage.inputNudgeNameSearch(targetOngoing);
        nudgesSystemListPage.clickSearch();
        nudgesSystemListPage.clickSettingsButtonByNudgeName(targetOngoing);
        nudgesSystemListPage.clickDeleteButton();
        nudgesSystemListPage.clickConfirmDeleteButton();

        nudgesSystemListPage.inputNudgeNameSearch(targetOngoing);
        nudgesSystemListPage.clickSearch();
        Assertion.assertThat("Verify Ongoing rule is removed after deactivation",
                nudgesSystemPage.verifyNudgeRuleIsDeleted(), is(true));

        step("Log out session");
        pricingDashboardPageWeb.logout();
    }

    @Author(names = {"Nasha"})
    @Module(name = "NudgesSystem")
    @Tribe(name = "Pricing")
    @Vertical(name = "Pricing")
    @Test
    public void testVerifyRedirectionFormPrefillingAndValidationRulesForNudges() {
        Map<String, String> param = getParam("testVerifyRedirectionFormPrefillingAndValidationRulesForNudges");
        BaseTest.paramsThreadLocal.set(param);
        
        String username = param.get("username");
        String password = param.get("password");
        String targetRule = param.get("targetRule");
        String existingLayer = param.get("existingLayer");
        String existingPriority = param.get("existingPriority");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        PricingDashboardPageWeb<?> pricingDashboardPageWeb = PricingDashboardPageWeb.getInstance(session);
        NudgesSystemPageWeb<?> nudgesSystemPage = NudgesSystemPageWeb.getInstance(session);
        NudgesSystemListPageWeb<?> nudgesSystemListPage = NudgesSystemListPageWeb.getInstance(session);
        NudgesSystemCreatePageWeb<?> nudgesSystemCreatePage = NudgesSystemCreatePageWeb.getInstance(session);
        
        
        step("[Setup] Login and navigate to Nudges System Dashboard");
        memberLoginPage.gotoMemberLoginPage();
        memberLoginPage.adminLogin(username, password);
        pricingDashboardPageWeb.goToPage();
        nudgesSystemPage.goToNudgesSystemPage();

        step("[Edit] Click Settings and select Edit option");
        nudgesSystemListPage.inputNudgeNameSearch(targetRule);
        nudgesSystemListPage.clickSearch();
        nudgesSystemListPage.clickSettingsButtonByNudgeName(targetRule);
        nudgesSystemPage.clickEditButton();
        step("[Edit] Verify redirected and form is prefilled correctly");
        Assertion.assertThat("Verify General rule is prefilled on Edit Page", nudgesSystemPage.verifyGeneralRuleIsPreFilled(), is(true));
        Assertion.assertThat("Verify Target Action is prefilled on Edit Page", nudgesSystemPage.verifyTargetActionIsPreFilled(), is(true));
        Assertion.assertThat("Verify Condition is prefilled on Edit Page", nudgesSystemPage.verifyConditionIsPreFilled(), is(true));
        Assertion.assertThat("Verify Schedule is prefilled on Edit Page", nudgesSystemPage.verifyScheduleIsPreFilled(), is(true));
        nudgesSystemPage.goToNudgesSystemPage();

        // DUPLICATE NAVIGATION & PREFILL CHECK
        step("[Duplicate] Click Settings and select Duplicate option");
        nudgesSystemListPage.inputNudgeNameSearch(targetRule);
        nudgesSystemListPage.clickSearch();
        nudgesSystemListPage.clickSettingsButtonByNudgeName(targetRule);
        nudgesSystemListPage.clickDuplicateButtonByNudgeName(targetRule);

        step("[Duplicate] Verify redirected and form is prefilled correctly");
        nudgesSystemCreatePage.inputNudgeName(targetRule);
        nudgesSystemCreatePage.inputNudgePriorityInput("123");
        nudgesSystemCreatePage.goToSummaryStep();
        Assertion.assertThat("Verify General rule is prefilled", nudgesSystemPage.verifyGeneralRuleIsPreFilled(), is(true));
        Assertion.assertThat("Verify Target Action is prefilled", nudgesSystemPage.verifyTargetActionIsPreFilled(), is(true));
        Assertion.assertThat("Verify Condition is prefilled", nudgesSystemPage.verifyConditionIsPreFilled(), is(true));
        Assertion.assertThat("Verify Schedule is prefilled", nudgesSystemPage.verifyScheduleIsPreFilled(), is(true));
        nudgesSystemPage.goToNudgesSystemPage();
        
        // DUPLICATE VALIDATION - NAME ALREADY EXISTS
        step("[Duplicate] Try to duplicate and save with an existing name");
        nudgesSystemListPage.inputNudgeNameSearch(targetRule);
        nudgesSystemListPage.clickSearch();
        nudgesSystemListPage.clickSettingsButtonByNudgeName(targetRule);
        nudgesSystemListPage.clickDuplicateButtonByNudgeName(targetRule);
        nudgesSystemCreatePage.inputNudgeName(targetRule);
        nudgesSystemCreatePage.inputNudgePriorityInput("123");
        nudgesSystemCreatePage.goToSummaryStep();
        nudgesSystemCreatePage.clickSave();
        Assertion.assertThat("Verify duplicate validation error with existing name", nudgesSystemCreatePage.verifySnackbarError(), is(true));
        nudgesSystemPage.goToNudgesSystemPage();

        // DUPLICATE VALIDATION - LAYER & PRIORITY PAIR CONFLICT
        step("[Duplicate] Try to save duplicate with existing Layer and Priority pair");
        nudgesSystemListPage.inputNudgeNameSearch(targetRule);
        nudgesSystemListPage.clickSearch();
        nudgesSystemListPage.clickSettingsButtonByNudgeName(targetRule);
        nudgesSystemListPage.clickDuplicateButtonByNudgeName(targetRule);
        nudgesSystemCreatePage.inputNudgeName(targetRule + "_UniqueName");
        nudgesSystemCreatePage.inputNudgeLayer(existingLayer);
        nudgesSystemCreatePage.inputNudgePriorityInput(existingPriority);
        nudgesSystemCreatePage.goToSummaryStep();
        Assertion.assertThat("Verify duplicate validation error with existing layer and priority", nudgesSystemCreatePage.verifySnackbarError(), is(true));
        nudgesSystemPage.goToNudgesSystemPage();

        // DUPLICATE VALIDATION - PAST START DATE
        step("[Duplicate] Try to save duplicate with a start date in the past");
        nudgesSystemListPage.filterByStatusDropdown("EXPIRED"); 
        String targetName = nudgesSystemListPage.getTopRowNudgeName();
        nudgesSystemListPage.clickSettingsButtonByNudgeName(targetName);
        nudgesSystemListPage.clickDuplicateButtonByNudgeName(targetName);
        nudgesSystemCreatePage.inputNudgeName(targetName + "_UniqueName2");
        nudgesSystemCreatePage.inputNudgePriorityInput("123");
        nudgesSystemCreatePage.goToSummaryStep();
        nudgesSystemCreatePage.clickSave();
        Assertion.assertThat("Verify duplicate validation error with existing layer and priority", nudgesSystemCreatePage.verifySnackbarError(), is(true));

        nudgesSystemPage.goToNudgesSystemPage();
        step("Log out session");
        pricingDashboardPageWeb.logout();
    }
    
    
    @Author(names = {"Nasha"})
    @Module(name = "SubsidyBudgeting")
    @Tribe(name = "Pricing")
    @Vertical(name = "Pricing")
    @Test
    public void testVerifyUserAccessViewUpdateAndDeleteOperationsForSubsidyBudgets() {
        Map<String, String> param = getParam("testVerifyUserAccessViewUpdateAndDeleteOperationsForSubsidyBudgets");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");
        String usernameDenied = param.get("usernameDenied");
        String passwordDenied = param.get("passwordDenied");
        String subsidyName = param.get("subsidyName");
        String budgetName = "aut"+ CoreFunctions.randomName();
        String message = param.get("message");
        String message2 = param.get("message2");
        String day = param.get("day");
        String quota = param.get("quota");
        String ruleName = param.get("ruleName");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        PricingDashboardPageWeb<?> pricingDashboardPageWeb = PricingDashboardPageWeb.getInstance(session);
        SubsidyBudgetingPage<?> subsidyBudgetingPage = new SubsidyBudgetingPage<>(session);
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);

        // 1. Privilege: Granted | Action: Access | Condition: navigating to the Subsidy Budgeting dashboard | Expected Outcome: successfully display the main dashboard ---
        step("[Access] Login with authorized user");
        memberLoginPage.gotoMemberLoginPage();
        memberLoginPage.adminLogin(username, password);

        step("[Access] Go to Pricing Dashboard");
        pricingDashboardPageWeb.goToPage();

        step("[Access] Navigate to the Subsidy Budgeting dashboard");
        pricingDashboardPageWeb.clickSubsidyBudgetingMenu();
        
        step("[Access] Verify main dashboard is successfully displayed");
        Assertion.assertThat("Successfully display the subsidy budgeting dashboard", subsidyBudgetingPage.isSubsidyBudgetingPageLoaded(), is(true));

        // 2. Privilege: Denied | Action: Access | Condition: navigating to the Subsidy Budgeting dashboard | Expected Outcome: deny access and display a restriction message ---
        step("[Access] Logout authorized user");
        pricingDashboardPageWeb.logout();

        step("[Access] Login with unauthorized user");
        memberLoginPage.gotoMemberLoginPage();
        memberLoginPage.adminLogin(usernameDenied, passwordDenied);

        step("[Access] Attempt to navigate to Subsidy Budgeting dashboard");
        pricingDashboardPageWeb.goToPage();
        pricingDashboardPageWeb.clickSubsidyBudgetingMenu();
        
        step("[Access] Verify access is denied");
        Assertion.assertThat("Verify user without privileges cannot access the Subsidy Budgeting module", subsidyBudgetingPage.isSubsidyBudgetingPageLoaded(), is(false));

        // 3. Privilege: Granted | Action: Access | Condition: navigating to the Update Subsidy Budgeting dashboard | Expected Outcome: successfully display the update dashboard ---
        step("[Access] Logout unauthorized user");
       loginMemberAdmin.logout();

        memberLoginPage.gotoMemberLoginPage();
        memberLoginPage.adminLogin(username, password);

        step("[Access] Go to Pricing Dashboard");
        pricingDashboardPageWeb.goToPage();

        step("[Access] Navigate to the Subsidy Budgeting dashboard");
        pricingDashboardPageWeb.clickSubsidyBudgetingMenu();

        step("[Access] Search for the subsidy budget");
        subsidyBudgetingPage.clickOnShowFilterButton();
        subsidyBudgetingPage.searchByByBudgetName(subsidyName);
        subsidyBudgetingPage.clickSearchButton();

        step("[Access] Click on Settings button");
        subsidyBudgetingPage.clickOnSettingsButton();

        step("[Access] Click on Edit button");
        subsidyBudgetingPage.clickOnEditButton();

        step("[Access] Verify update dashboard is successfully displayed");
        Assertion.assertThat("Verify update dashboard is successfully displayed", subsidyBudgetingPage.isUpdateSubsidyBudgetingPageLoaded(), is(true));

        // 4. Privilege: Granted | Action: View | Condition: filling the period filter with multiple days | Expected Outcome: correctly calculate and display "tiket Budget" and "Vendor Budget" values ---
        step("[View] Go back to Subsidy Budgeting main page");
        pricingDashboardPageWeb.clickSubsidyBudgetingMenu();

        step("[View] Show filter");
        subsidyBudgetingPage.clickOnShowFilterButton();

        step("[View] Enter the subsidy name to filter");
        subsidyBudgetingPage.clickSubsidyBudgetRuleNameField(subsidyName);

        step("[View] Click search");
        subsidyBudgetingPage.clickSearchButton();

        step("[View] Verify period filter matches multiple days");
        Assertion.assertThat("Verify the multiple day period value", subsidyBudgetingPage.verifyMultipleDayPeriodValue(day), is(true));

        step("[View] Verify tiket Budget and Vendor Budget values are correctly calculated and displayed");
        Assertion.assertThat("Verify value of tiket Budget and Vendor Budget is greater than zero", subsidyBudgetingPage.verifyTiketBudVendorBudIsGreaterThanZero(), is(true));

        step("[Precondition] Create a subsidy budget with Inactive status for update and delete tests");
        subsidyBudgetingPage.createNewBudget(budgetName, quota, "TESTING", "IDR");
        subsidyBudgetingPage.clickSaveButton();
        subsidyBudgetingPage.clickConfirmSaveButton();

        // 5. Privilege: Granted | Action: Update | Condition: saving the record without changing any values | Expected Outcome: successfully update/save the budget ---
        step("[Update] search for the created budget");
        subsidyBudgetingPage.clickOnShowFilterButton();
        subsidyBudgetingPage.searchByByBudgetName(budgetName);
        subsidyBudgetingPage.clickSearchButton();

        step("[Update] Click on Settings button");
        subsidyBudgetingPage.clickOnSettingsButton();

        step("[Update] Click on Edit button");
        subsidyBudgetingPage.clickOnEditButton();

        step("[Update] Save the record without changing any values");
        subsidyBudgetingPage.clickSaveButtonOnUpdatePage();

        step("[Update] Verify budget is successfully updated");
        Assertion.assertThat("Successfully update/save the budget", subsidyBudgetingPage.verifyUpdateSuccessMessage(), is(true));

        // 6. Privilege: Granted | Action: Update | Condition: changing the subsidy status from Inactive to Active | Expected Outcome: successfully save the Active status ---
        step("[Update] Go back to Subsidy Budgeting list");
        pricingDashboardPageWeb.clickSubsidyBudgetingMenu();

        step("[Update] Show Filter and select Inactive status");
        subsidyBudgetingPage.clickOnShowFilterButton();
        subsidyBudgetingPage.searchByByBudgetName(budgetName);
        subsidyBudgetingPage.clickSearchButton();

        step("[Update] Change status to Active");
        subsidyBudgetingPage.selectFirstActiveBud();
        subsidyBudgetingPage.clickRadioButtonOfActiveOption();
        subsidyBudgetingPage.clickYesButton();

        step("[Update] Verify status updated successfully to Active");
        Assertion.assertThat("Verify status updated successfully to Active", subsidyBudgetingPage.verifyThePopUpMessage(message, message2), is(true));

        // 7. Privilege: Granted | Action: Update | Condition: updating one subsidy budget to assign multiple rules | Expected Outcome: successfully save the budget with multiple rules ---
        step("[Update] Search for the created budget");
        pricingDashboardPageWeb.clickSubsidyBudgetingMenu();
        subsidyBudgetingPage.clickOnShowFilterButton();
        subsidyBudgetingPage.searchByByBudgetName(budgetName);
        subsidyBudgetingPage.clickSearchButton();

        step("[Update] Click on Settings button");
        subsidyBudgetingPage.clickOnSettingsButton();

        step("[Update] Click on Edit button");
        subsidyBudgetingPage.clickOnEditButton();

        step("[Update] Search by rule name before selecting rules");
        subsidyBudgetingPage.searchByRuleName(ruleName);

        step("[Update] Select multiple rules");
        subsidyBudgetingPage.selectTwoRules();

        step("[Update] Save the budget with multiple rules");
        subsidyBudgetingPage.clickSaveButtonOnUpdatePage();

        step("[Update] Verify budget saved with multiple rules");
        Assertion.assertThat("Verify update was successful", subsidyBudgetingPage.verifySubsidyBudgetUpdateComplete(), is(true));

        // 8. Privilege: Granted | Action: Update | Condition: updating a subsidy without assigning any rules to it | Expected Outcome: successfully save the budget with no rules attached ---
        step("[Update] Search for the created budget");
        pricingDashboardPageWeb.clickSubsidyBudgetingMenu();
        subsidyBudgetingPage.clickOnShowFilterButton();
        subsidyBudgetingPage.searchByByBudgetName(budgetName);
        subsidyBudgetingPage.clickSearchButton();

        step("[Update] Click on Settings button");
        subsidyBudgetingPage.clickOnSettingsButton();

        step("[Update] Click on Edit button");
        subsidyBudgetingPage.clickOnEditButton();

        step("[Update] Uncheck all rules");
        subsidyBudgetingPage.cleanupAfterTest();

        step("[Update] Save the budget with no rules attached");
        subsidyBudgetingPage.clickSaveButtonOnUpdatePage();

        step("[Update] Verify budget successfully saved with no rules attached");
        Assertion.assertThat("Verify budget successfully saved with no rules attached", subsidyBudgetingPage.verifyUpdateSuccessMessage(), is(true));

        // 9. Privilege: Granted | Action: Delete | Condition: initiating deletion on a budget with Inactive status | Expected Outcome: allow the deletion process to begin ---
        step("[Precondition] Change the budget status back to Inactive for deletion");
        pricingDashboardPageWeb.clickSubsidyBudgetingMenu();
        subsidyBudgetingPage.clickOnShowFilterButton();
        subsidyBudgetingPage.searchByByBudgetName(budgetName);
        subsidyBudgetingPage.clickSearchButton();
        subsidyBudgetingPage.selectFirstActiveBud();
        subsidyBudgetingPage.clickRadioButtonOfInActiveOption();
        subsidyBudgetingPage.clickYesButton();
        pricingDashboardPageWeb.clickSubsidyBudgetingMenu();
        subsidyBudgetingPage.clickOnShowFilterButton();
        subsidyBudgetingPage.searchByByBudgetName(budgetName);
        subsidyBudgetingPage.clickSearchButton();
        
        step("[Delete] Click settings on the inactive budget");
        subsidyBudgetingPage.clickOnSettingsButton();

        step("[Delete] Initiate deletion on a budget with Inactive status");
        subsidyBudgetingPage.clickDeleteButton();

        step("[Delete] Verify delete modal is displayed");
        Assertion.assertThat("Verify delete modal is displayed", subsidyBudgetingPage.isDeleteModalDisplayed(), is(true));

        // 10. Privilege: Granted | Action: Delete | Condition: clicking the "Yes, Delete" confirmation button | Expected Outcome: permanently remove the data from the list ---
        step("[Delete] Click Delete confirmation button");
        subsidyBudgetingPage.clickConfirmDeleteButton();

        step("[Delete] Verify the budget is permanently removed from the list");
        subsidyBudgetingPage.clickOnShowFilterButton();
        subsidyBudgetingPage.searchByByBudgetName(budgetName);
        subsidyBudgetingPage.clickSearchButton();
        Assertion.assertThat("Verify the budget is permanently removed from the list", subsidyBudgetingPage.verifyNoResultResetFilterOption(), is(true));

        step("Log out");
        pricingDashboardPageWeb.logout();
    }

    @Author(names = {"Nasha"})
    @Module(name = "SubsidyBudgeting")
    @Tribe(name = "Pricing")
    @Vertical(name = "Pricing")
    @Test
    public void testVerifyListOperationsDeleteRestrictionsAndDataLifecycleOnTheDashboard() {
        Map<String, String> param = getParam("testVerifyListOperationsDeleteRestrictionsAndDataLifecycleOnTheDashboard");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.getOrDefault("username", param.get("email"));
        String password = param.get("password");
        String validBudgetName = param.get("validBudgetName");
        String invalidBudgetName = param.get("invalidBudgetName");
        String verticalName = param.get("verticalName");
        String activeBudgetNameWithPassedSchedule = param.get("activeBudgetNameWithPassedSchedule");
        String quota = param.get("quota");
        String expectedMessage = param.get("expectedMessage");
        String updateMessage1 = param.get("updateMessage1");
        String updateMessage2 = param.get("updateMessage2");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        PricingDashboardPageWeb<?> pricingDashboardPageWeb = PricingDashboardPageWeb.getInstance(session);
        SubsidyBudgetingPage<?> subsidyBudgetingPage = new SubsidyBudgetingPage<>(session);

        step("Login with authorized user");
        memberLoginPage.gotoMemberLoginPage();
        memberLoginPage.adminLogin(username, password);

        step("Go to Pricing Dashboard");
        pricingDashboardPageWeb.goToPage();

        step("Navigate to the Subsidy Budgeting dashboard");
        pricingDashboardPageWeb.clickSubsidyBudgetingMenu();

        // 1. Search | entering a valid Budget Name | display the matching budget records ---
        step("[Search] Search with valid Budget Name");
        subsidyBudgetingPage.clickOnShowFilterButton();
        subsidyBudgetingPage.searchByByBudgetName(validBudgetName);
        subsidyBudgetingPage.clickSearchButton();
        Assertion.assertThat("Verify matching budget records displayed", subsidyBudgetingPage.verifySearchBudgedNameResult(validBudgetName), is(true));

        // 2. Search | entering an invalid Budget Name | show no data matched and display a "Reset Filter" button ---
        step("[Search] Search with invalid Budget Name");
        subsidyBudgetingPage.clickOnShowFilterButton();
        subsidyBudgetingPage.enterInvalidBudgetName(invalidBudgetName);
        subsidyBudgetingPage.clickSearchButton();
        Assertion.assertThat("Verify no results found and Reset Filter option displayed", subsidyBudgetingPage.verifyNoResultResetFilterOption(), is(true));

        // 3. Filter | selecting ACTIVE in the Budget Status filter | display only active budget records ---
        step("[Filter] Filter status ACTIVE");
        subsidyBudgetingPage.clickTheResetFilterButton();
        subsidyBudgetingPage.clickOnShowFilterButton();
        subsidyBudgetingPage.clickSubsidyBudgetingStatusFilter();
        subsidyBudgetingPage.clickSubsidyBudgetActiveStatusCheckbox();
        subsidyBudgetingPage.clickSearchButton();
        Assertion.assertThat("Verify active budgets displayed", subsidyBudgetingPage.verifyFilterActiveBudgetStatus(), is(true));

        // 4. Filter | selecting INACTIVE in the Budget Status filter | display only inactive budget records ---
        step("[Filter] Filter status INACTIVE");
        subsidyBudgetingPage.clickTheResetFilterButton();
        subsidyBudgetingPage.clickOnShowFilterButton();
        subsidyBudgetingPage.clickStatusInputField();
        subsidyBudgetingPage.selectInactiveOption();
        subsidyBudgetingPage.clickSearchButton();
        Assertion.assertThat("Verify inactive budgets displayed", subsidyBudgetingPage.verifyInactiveSearchResult(), is(true));

        // 5. Filter | selecting a specific vertical | display budget records matching the chosen vertical ---
        step("[Filter] Filter specific vertical");
        subsidyBudgetingPage.clickTheResetFilterButton();
        subsidyBudgetingPage.clickOnShowFilterButton();
        subsidyBudgetingPage.selectVerticalAndCloseDropdownScrollable(verticalName);
        subsidyBudgetingPage.clickSearchButton();
        Assertion.assertThat("Verify records matching chosen vertical displayed", subsidyBudgetingPage.verifyFilterResult(verticalName), is(true));

        // 6. Filter | applying filters that result in no matching data | show no data matched and display a "Reset Filter" button ---
        step("[Filter] Apply filters resulting in no match");
        subsidyBudgetingPage.clickTheResetFilterButton();
        subsidyBudgetingPage.clickOnShowFilterButton();
        subsidyBudgetingPage.enterInvalidBudgetName(invalidBudgetName);
        subsidyBudgetingPage.clickSearchButton();
        Assertion.assertThat("Verify Reset Filter button displayed for no match", subsidyBudgetingPage.verifyNoResultResetFilterOption(), is(true));

        // 8. Delete | targeting a subsidy budget when the status is active | prevent deletion and display a restriction error ---
        step("[Delete] Navigate back to Subsidy Budgeting");
        pricingDashboardPageWeb.clickSubsidyBudgetingMenu();

        step("[Delete] Filter ACTIVE status");
        subsidyBudgetingPage.clickTheResetFilterButton();
        subsidyBudgetingPage.clickOnShowFilterButton();
        subsidyBudgetingPage.clickSubsidyBudgetingStatusFilter();
        subsidyBudgetingPage.clickSubsidyBudgetActiveStatusCheckbox();
        subsidyBudgetingPage.clickSearchButton();

        step("[Delete] Target active budget and attempt to delete");
        subsidyBudgetingPage.clickOnSettingsButton();
        subsidyBudgetingPage.clickDeleteButton();
        Assertion.assertThat("Verify deletion is blocked for active budget", subsidyBudgetingPage.isDeletionRestrictionErrorDisplayed(), is(true));
        subsidyBudgetingPage.clickDismissErrorModal();

        // 9. Delete | targeting an active budget where the schedule has passed | prevent deletion and display a restriction error ---
        step("[Delete] Search for active budget with passed schedule");
        subsidyBudgetingPage.clickTheResetFilterButton();
        subsidyBudgetingPage.clickOnShowFilterButton();
        subsidyBudgetingPage.searchByByBudgetName(activeBudgetNameWithPassedSchedule);
        subsidyBudgetingPage.clickSearchButton();

        step("[Delete] Target active budget with passed schedule and attempt to delete");
        subsidyBudgetingPage.clickOnSettingsButton();
        subsidyBudgetingPage.clickDeleteButton();
        Assertion.assertThat("Verify deletion is blocked for budget with passed schedule",subsidyBudgetingPage.isDeletionRestrictionErrorDisplayed(), is(true));
        subsidyBudgetingPage.clickDismissErrorModal();
        
        // 10. E2E | executing add, edit (active/inactive), and delete flows | successfully process the full data lifecycle in the list ---
        step("[E2E] Create a new budget");
        subsidyBudgetingPage.clickTheResetFilterButton();
        String e2eBudgetName = "aut_e2e_" + CoreFunctions.randomName();
        subsidyBudgetingPage.createNewBudget(e2eBudgetName, quota, "TESTING", "IDR");
        subsidyBudgetingPage.clickSaveButton();
        subsidyBudgetingPage.clickConfirmSaveButton();
        Assertion.assertThat("Verify budget created successfully", subsidyBudgetingPage.verifySnackbarMessage(expectedMessage), is(true));

        step("[E2E] Search the newly created budget");
        subsidyBudgetingPage.clickOnShowFilterButton();
        subsidyBudgetingPage.searchByByBudgetName(e2eBudgetName);
        subsidyBudgetingPage.clickSearchButton();

        step("[E2E] Check the budget row checkbox");
        subsidyBudgetingPage.selectFirstActiveBud();

        step("[E2E] Change status to Active");
        subsidyBudgetingPage.clickRadioButtonOfActiveOption();
        subsidyBudgetingPage.clickYesButton();
        Assertion.assertThat("Verify status updated to active", subsidyBudgetingPage.verifyThePopUpMessage(updateMessage1, updateMessage2), is(true));

        step("[E2E] Change status to Inactive");
        subsidyBudgetingPage.selectFirstActiveBud();
        subsidyBudgetingPage.clickRadioButtonOfInActiveOption();
        subsidyBudgetingPage.clickYesButton();
        Assertion.assertThat("Verify status updated to inactive", subsidyBudgetingPage.verifyThePopUpMessage(updateMessage1, updateMessage2), is(true));

        step("[E2E] Delete the budget");
        subsidyBudgetingPage.clickOnSettingsButton();
        subsidyBudgetingPage.clickDeleteButton();
        subsidyBudgetingPage.clickConfirmDeleteButton();

        step("[E2E] Verify budget is permanently removed from the list");
        subsidyBudgetingPage.clickSearchButton();
        Assertion.assertThat("Verify budget is deleted", subsidyBudgetingPage.verifyNoResultResetFilterOption(), is(true));

        step("Log out");
        pricingDashboardPageWeb.logout();
    }
}
