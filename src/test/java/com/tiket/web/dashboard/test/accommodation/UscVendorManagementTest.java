package com.tiket.web.dashboard.test.accommodation;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.WebTest;
import com.tiket.web.dashboard.page.accommodation.internal_supplier_dashboard.UscVendorManagementPage;
import com.tiket.web.dashboard.page.common.LoginMemberAdmin;
import com.tiket.web.dashboard.page.member_dashboard.MemberLoginPage;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.core.Is.is;

public class UscVendorManagementTest extends WebTest {

    @Author(names = {"Raihan Yasier"})
    @Module(name = "USCVendorManagement")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyAddNewVendorSeeInactiveEditVendorAndSeeSupportAgreement() {
        Map<String, String> param = getParam("verifyAddNewVendorSeeInactiveEditVendorAndSeeSupportAgreement");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        UscVendorManagementPage<?> uscVendorManagementPage = UscVendorManagementPage.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");
        String penaltyText = param.get("penaltyText");
        String disputeText = param.get("disputeText");
        String reconciliationText = param.get("reconciliationText");
        String boldText = param.get("boldText");
        String italicText = param.get("italicText");
        String underlineText = param.get("underlineText");
        String bulletItem1 = param.get("bulletItem1");
        String bulletItem2 = param.get("bulletItem2");
        String numberedItem1 = param.get("numberedItem1");
        String numberedItem2 = param.get("numberedItem2");
        String expectedAddSuccessMessage = param.get("expectedAddSuccessMessage");
        String expectedEditSuccessMessage = param.get("expectedEditSuccessMessage");
        int maxPagesToScan = Integer.parseInt(param.getOrDefault("maxPagesToScan", "10"));

        if (bulletItem1 == null || bulletItem2 == null || numberedItem1 == null || numberedItem2 == null) {
            throw new IllegalArgumentException("bulletItem1/2 and numberedItem1/2 params are required for this test.");
        }
        List<String> bulletItems = List.of(bulletItem1, bulletItem2);
        List<String> numberedItems = List.of(numberedItem1, numberedItem2);

        step("User Log in to Panel dashboard");
        memberLoginPage.gotoMemberLoginPage();
        memberLoginPage.adminLogin(email, password);
        loginMemberAdmin.gotoHome();

        step("Go to Internal Supplier Dashboard and open USC Vendor Management page");
        loginMemberAdmin.goToAccommodationSupplierDashboard();
        uscVendorManagementPage.goToVendorManagementMenu();
        Assertion.assertThat("USC Vendor Management page should be displayed", uscVendorManagementPage.isPageDisplayed(), is(true));

        step("[USC Vendor Management] Verify internal user is able to add a new vendor");
        String newVendorCode = uscVendorManagementPage.generateVendorCode();
        boolean testPassed = false;
        try {
            uscVendorManagementPage.clickAddNewVendorButton();
            Assertion.assertThat("Add New Vendor drawer should be displayed", uscVendorManagementPage.isAddVendorDrawerDisplayed(), is(true));
            uscVendorManagementPage.fillVendorName(newVendorCode);
            uscVendorManagementPage.fillAllAgreements(penaltyText, disputeText, reconciliationText);
            uscVendorManagementPage.clickAddVendorSubmit();
            Assertion.assertThat("Snackbar message should confirm success adding vendor", uscVendorManagementPage.verifySnackbarMessage(expectedAddSuccessMessage), is(true));

            step("[USC Vendor Management] Verify newly added vendor appears in the list with Inactive status");
            Assertion.assertThat("Newly added vendor " + newVendorCode + " should be present in the list", uscVendorManagementPage.findVendorCodeAcrossPages(newVendorCode, maxPagesToScan), is(true));
            Assertion.assertThat("Newly added vendor status should be Inactive", uscVendorManagementPage.getVendorStatus(newVendorCode), is("Inactive"));

            step("[USC Vendor Management] Verify internal user is able to edit the vendor with rich-text formatting on all three agreements");
            uscVendorManagementPage.clickActionForVendor(newVendorCode);
            uscVendorManagementPage.clickActionMenuItem("Edit vendor");
            Assertion.assertThat("Edit Vendor drawer should be displayed for " + newVendorCode, uscVendorManagementPage.isEditVendorDrawerDisplayed(newVendorCode), is(true));
            uscVendorManagementPage.editAllAgreementsWithAllFormats(boldText, italicText, underlineText, bulletItems, numberedItems);
            Assertion.assertThat("Penalty editor HTML should include bold, italic, underline, bullet list, and numbered list tags",
                    uscVendorManagementPage.editorHtmlHasAllRichTextFormats(UscVendorManagementPage.PENALTY_EDITOR_INDEX), is(true));
            Assertion.assertThat("Dispute editor HTML should include bold, italic, underline, bullet list, and numbered list tags",
                    uscVendorManagementPage.editorHtmlHasAllRichTextFormats(UscVendorManagementPage.DISPUTE_EDITOR_INDEX), is(true));
            Assertion.assertThat("Reconciliation editor HTML should include bold, italic, underline, bullet list, and numbered list tags",
                    uscVendorManagementPage.editorHtmlHasAllRichTextFormats(UscVendorManagementPage.RECONCILIATION_EDITOR_INDEX), is(true));
            uscVendorManagementPage.clickSaveChanges();
            Assertion.assertThat("Snackbar message should confirm success updating vendor", uscVendorManagementPage.verifySnackbarMessage(expectedEditSuccessMessage), is(true));

            step("[USC Vendor Management] Verify internal user is able to see Support Agreement pop up with formatted content persisted");
            uscVendorManagementPage.clickActionForVendor(newVendorCode);
            uscVendorManagementPage.clickActionMenuItem("Support Agreements");
            Assertion.assertThat("Support Agreement popup should be displayed for " + newVendorCode, uscVendorManagementPage.isSupportAgreementPopupDisplayed(newVendorCode), is(true));
            Assertion.assertThat("Support Agreement popup should show all three agreement sections", uscVendorManagementPage.getSupportAgreementSections(), hasItems("Penalty Agreement", "Dispute Agreement", "Reconciliation Agreement"));

            for (String section : List.of("Penalty Agreement", "Dispute Agreement", "Reconciliation Agreement")) {
                Assertion.assertThat(section + " should show boldText wrapped in a strong tag",
                        uscVendorManagementPage.supportAgreementSectionHasFormattedText(section, "strong", boldText), is(true));
                Assertion.assertThat(section + " should show italicText wrapped in an em tag",
                        uscVendorManagementPage.supportAgreementSectionHasFormattedText(section, "em", italicText), is(true));
                Assertion.assertThat(section + " should show underlineText wrapped in a u tag",
                        uscVendorManagementPage.supportAgreementSectionHasFormattedText(section, "u", underlineText), is(true));
                Assertion.assertThat(section + " should show bulletItem1 as a ul/li",
                        uscVendorManagementPage.supportAgreementSectionHasFormattedText(section, "ul//li", bulletItems.get(0)), is(true));
                Assertion.assertThat(section + " should show bulletItem2 as a ul/li",
                        uscVendorManagementPage.supportAgreementSectionHasFormattedText(section, "ul//li", bulletItems.get(1)), is(true));
                Assertion.assertThat(section + " should show numberedItem1 as an ol/li",
                        uscVendorManagementPage.supportAgreementSectionHasFormattedText(section, "ol//li", numberedItems.get(0)), is(true));
                Assertion.assertThat(section + " should show numberedItem2 as an ol/li",
                        uscVendorManagementPage.supportAgreementSectionHasFormattedText(section, "ol//li", numberedItems.get(1)), is(true));
            }
            uscVendorManagementPage.closeSupportAgreementPopup();
            testPassed = true;
        } finally {
            uscVendorManagementPage.cleanupVendorViaApi(newVendorCode, email, testPassed);
        }
    }

    @Author(names = {"Raihan Yasier"})
    @Module(name = "USCVendorManagement")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyViewVendorDetailAddConfigEditB2CFilterFeatureAndSeeConfigChangeHistory() {
        Map<String, String> param = getParam("verifyViewVendorDetailAddConfigEditB2CFilterFeatureAndSeeConfigChangeHistory");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        UscVendorManagementPage<?> uscVendorManagementPage = UscVendorManagementPage.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");
        String penaltyText = param.get("penaltyText");
        String disputeText = param.get("disputeText");
        String reconciliationText = param.get("reconciliationText");
        String storeScope = param.getOrDefault("storeScope", "B2C");

        String configNewValue = param.getOrDefault("configNewValue",
                "AUTO_" + storeScope.replace(" ", "_").toUpperCase());
        String expectedAddSuccessMessage = param.get("expectedAddSuccessMessage");
        String expectedConfigUpdatedSnackbar = param.get("expectedConfigUpdatedSnackbar");
        int expectedHistoryMinRows = Integer.parseInt(param.getOrDefault("expectedHistoryMinRows", "2"));
        int maxPagesToScan = Integer.parseInt(param.getOrDefault("maxPagesToScan", "10"));

        step("User Log in to Panel dashboard");
        memberLoginPage.gotoMemberLoginPage();
        memberLoginPage.adminLogin(email, password);
        loginMemberAdmin.gotoHome();

        step("Go to Internal Supplier Dashboard and open USC Vendor Management page");
        loginMemberAdmin.goToAccommodationSupplierDashboard();
        uscVendorManagementPage.goToVendorManagementMenu();
        Assertion.assertThat("USC Vendor Management page should be displayed", uscVendorManagementPage.isPageDisplayed(), is(true));

        step("[Vendor Management] Add a brand-new vendor so this test is self-contained");
        String newVendorCode = uscVendorManagementPage.generateVendorCode();
        boolean testPassed = false;
        try {
            uscVendorManagementPage.clickAddNewVendorButton();
            Assertion.assertThat("Add New Vendor drawer should be displayed", uscVendorManagementPage.isAddVendorDrawerDisplayed(), is(true));
            uscVendorManagementPage.fillVendorName(newVendorCode);
            uscVendorManagementPage.fillAllAgreements(penaltyText, disputeText, reconciliationText);
            uscVendorManagementPage.clickAddVendorSubmit();
            Assertion.assertThat("Snackbar should confirm success adding vendor " + newVendorCode, uscVendorManagementPage.verifySnackbarMessage(expectedAddSuccessMessage), is(true));

            step("[Vendor Detail] Verify user is able to open the Vendor Detail page via 'View vendor'");
            Assertion.assertThat("Newly added vendor " + newVendorCode + " should be present in the list", uscVendorManagementPage.findVendorCodeAcrossPages(newVendorCode, maxPagesToScan), is(true));
            uscVendorManagementPage.clickActionForVendor(newVendorCode);
            uscVendorManagementPage.clickActionMenuItem("View vendor");
            Assertion.assertThat("Vendor Detail page should be displayed for " + newVendorCode, uscVendorManagementPage.isVendorDetailPageDisplayed(newVendorCode), is(true));

            step("[Vendor Detail] Assert the Feature list starts empty for a brand-new vendor");
            Assertion.assertThat("Feature list should show the empty state on a brand-new vendor", uscVendorManagementPage.isFeatureListEmpty(), is(true));

            step("[Add Configuration] Pick the first feature whose JSON Preview is usable for editing and capture its default JSON as the basis schema");
            uscVendorManagementPage.clickAddConfigurationButton();
            Assertion.assertThat("Add New Configuration page should be displayed", uscVendorManagementPage.isAddNewConfigurationPageDisplayed(), is(true));

            String configFeatureName = uscVendorManagementPage.selectFirstUsableFeatureForEditing();
            Assertion.assertThat("A non-empty feature name should be captured from the first usable feature card",
                    configFeatureName != null && !configFeatureName.isEmpty(), is(true));
            Assertion.assertThat("JSON Preview should be displayed after selecting '" + configFeatureName + "'",
                    uscVendorManagementPage.isJsonPreviewDisplayed(), is(true));

            String basisJson = uscVendorManagementPage.getJsonPreviewValue();
            Assertion.assertThat("JSON Preview should return a usable basis JSON for '" + configFeatureName + "'",
                    UscVendorManagementPage.isUsableBasisJson(basisJson), is(true));
            uscVendorManagementPage.submitAddConfiguration();
            Assertion.assertThat("'Configuration added successfully!' modal should be displayed", uscVendorManagementPage.isConfigurationAddedModalDisplayed(), is(true));

            step("[Config change history — pre-edit initial entry] Choose 'Maybe later' to skip the immediate edit, filter the feature list by '" + configFeatureName + "', open Config history via 'More action', and assert it already contains exactly ONE initial entry auto-created when the configuration was first added (there is no 'empty' state for a configured feature)");
            uscVendorManagementPage.clickMaybeLaterInAddedModal();
            uscVendorManagementPage.filterFeatureList(configFeatureName);
            Assertion.assertThat("Filtered feature list should contain the freshly-configured feature '" + configFeatureName + "' after 'Maybe later'",
                    uscVendorManagementPage.isFeatureRowPresent(configFeatureName), is(true));
            uscVendorManagementPage.clickMoreAction();
            uscVendorManagementPage.clickConfigChangeHistoryMenuItem();
            Assertion.assertThat("Config change history modal should be displayed for freshly-configured feature '" + configFeatureName + "'",
                    uscVendorManagementPage.isConfigChangeHistoryModalDisplayed(), is(true));
            int preEditHistoryRowCount = uscVendorManagementPage.getConfigChangeHistoryRowCount();
            Assertion.assertThat("For a freshly-configured feature (Add Configuration submitted, NO user edits yet), Config change history should already contain exactly 1 row \u2014 the initial entry auto-created when the config was first added. got=" + preEditHistoryRowCount,
                    preEditHistoryRowCount, is(1));
            String preEditRow1FeatureName = uscVendorManagementPage.getHistoryFeatureName(1);
            Assertion.assertThat("Pre-edit initial history row Feature Name should start with '" + configFeatureName + "'. actual='" + preEditRow1FeatureName + "'",
                    preEditRow1FeatureName != null && preEditRow1FeatureName.startsWith(configFeatureName), is(true));
            uscVendorManagementPage.closeConfigChangeHistoryModal();

            step("[Edit Configuration] Open Edit for '" + configFeatureName + "' via the feature row and edit the " + storeScope + " configuration JSON by mutating one value on the live basis JSON");
            uscVendorManagementPage.clickEditFeatureButton(configFeatureName);
            Assertion.assertThat("Edit Configuration page should be displayed for '" + configFeatureName + "'", uscVendorManagementPage.isEditConfigurationPageDisplayed(configFeatureName), is(true));
            String editConfigSubtitle = uscVendorManagementPage.getEditConfigurationSubtitle(configFeatureName);
            uscVendorManagementPage.selectStoreScopeTab(storeScope);
            String previousJson = uscVendorManagementPage.getJsonEditorValue();

            String sourceJson = (previousJson != null && previousJson.trim().startsWith("{"))
                    ? previousJson : basisJson;
            String desiredJson = UscVendorManagementPage.mutateFirstJsonStringValue(sourceJson, configNewValue);
            uscVendorManagementPage.setJsonEditorValue(desiredJson);
            uscVendorManagementPage.clickSaveChangesOnEditConfiguration();

            step("[Value Different] Assert the modal is displayed and shows the correct Old vs New JSON payloads");
            Assertion.assertThat("'Value different' modal should be displayed after Save changes", uscVendorManagementPage.isValueDifferentModalDisplayed(), is(true));
            Assertion.assertThat("'Value different' modal should render both Old value and New value panels with their editors", uscVendorManagementPage.isValueDifferentModalStructureDisplayed(), is(true));
            Assertion.assertThat("'Value different' modal Old panel should show the pre-edit JSON",
                    UscVendorManagementPage.normalizeJson(uscVendorManagementPage.getValueDifferentOldValue()),
                    is(UscVendorManagementPage.normalizeJson(previousJson)));
            Assertion.assertThat("'Value different' modal New panel should show the newly edited JSON",
                    UscVendorManagementPage.normalizeJson(uscVendorManagementPage.getValueDifferentNewValue()),
                    is(UscVendorManagementPage.normalizeJson(desiredJson)));

            uscVendorManagementPage.clickConfirmChanges();
            Assertion.assertThat("Snackbar should confirm configuration updated successfully", uscVendorManagementPage.verifySnackbarMessage(expectedConfigUpdatedSnackbar), is(true));

            step("[Vendor Detail] Filter the feature list by name and see the newly configured feature with all its column values");
            uscVendorManagementPage.filterFeatureList(configFeatureName);
            Assertion.assertThat("Filtered feature list should contain the row '" + configFeatureName + "'", uscVendorManagementPage.isFeatureRowPresent(configFeatureName), is(true));
            String featureDescription = uscVendorManagementPage.getFeatureListDescription(configFeatureName);
            Assertion.assertThat("Feature row Description should equal the Edit Configuration subtitle '" + editConfigSubtitle + "'",
                    featureDescription, is(editConfigSubtitle));
            String expectedTodayDate = UscVendorManagementPage.getTodayDisplayDate();
            String featureLastUpdatedDate = uscVendorManagementPage.getFeatureListLastUpdatedDate(configFeatureName);
            Assertion.assertThat("Feature row Last Updated date should be today's date formatted as '" + expectedTodayDate + "'",
                    featureLastUpdatedDate, is(expectedTodayDate));
            String featureLastUpdatedActor = uscVendorManagementPage.getFeatureListLastUpdatedActor(configFeatureName);
            Assertion.assertThat("Feature row Last Updated actor should reference the logged-in user email '" + email + "'",
                    featureLastUpdatedActor.contains(email), is(true));

            step("[Config change history] Verify user is able to see the change history via 'More action' with row content matching the edit");
            uscVendorManagementPage.clickMoreAction();
            uscVendorManagementPage.clickConfigChangeHistoryMenuItem();
            Assertion.assertThat("Config change history modal should be displayed", uscVendorManagementPage.isConfigChangeHistoryModalDisplayed(), is(true));
            int historyRowCount = uscVendorManagementPage.getConfigChangeHistoryRowCount();
            Assertion.assertThat("Config change history should have at least " + expectedHistoryMinRows + " row(s), got " + historyRowCount,
                    historyRowCount >= expectedHistoryMinRows, is(true));

            String row1FeatureName = uscVendorManagementPage.getHistoryFeatureName(1);
            Assertion.assertThat("History row 1 Feature Name should start with '" + configFeatureName + "' (plus version suffix)",
                    row1FeatureName != null && row1FeatureName.startsWith(configFeatureName), is(true));
            Assertion.assertThat("History row 1 Store Scope should be '" + storeScope + "'",
                    uscVendorManagementPage.getHistoryStoreScope(1), is(storeScope));
            String row1BeforeCell = uscVendorManagementPage.getHistoryBeforeValue(1);
            String row1AfterCell = uscVendorManagementPage.getHistoryAfterValue(1);

            if (row1BeforeCell != null && row1BeforeCell.trim().startsWith("{")) {
                Assertion.assertThat("History row 1 Config Value (Before) should match the pre-edit JSON",
                        UscVendorManagementPage.normalizeJson(row1BeforeCell),
                        is(UscVendorManagementPage.normalizeJson(previousJson)));
            }
            Assertion.assertThat("History row 1 Config Value (after) should match the newly edited JSON",
                    UscVendorManagementPage.normalizeJson(row1AfterCell),
                    is(UscVendorManagementPage.normalizeJson(desiredJson)));
            String row1LatestEditedDate = uscVendorManagementPage.getHistoryLatestEditedDate(1);
            Assertion.assertThat("History row 1 Latest Edited date should be today's date formatted as '" + expectedTodayDate + "'",
                    row1LatestEditedDate, is(expectedTodayDate));
            String row1LatestEditedActor = uscVendorManagementPage.getHistoryLatestEditedActor(1);
            Assertion.assertThat("History row 1 Latest Edited actor should reference the logged-in user email '" + email + "'",
                    row1LatestEditedActor.contains(email), is(true));

            step("[History read-only] Assert every history row exposes only a 'View' action (no Revert/Restore/Rollback/Edit/Delete affordances) \u2014 the history table must be read-only");
            for (int r = 1; r <= historyRowCount; r++) {
                Assertion.assertThat("History row " + r + " must only expose a 'View' action button (no revert/restore/rollback/edit/delete affordances)",
                        uscVendorManagementPage.historyRowHasOnlyViewAction(r), is(true));
            }

            step("[Value Different from History] Click View on the latest history row and assert the modal mirrors the row's Before/After JSON");
            uscVendorManagementPage.clickViewOnHistoryRow(1);
            Assertion.assertThat("'Value different' modal should be displayed after clicking View on the latest history row",
                    uscVendorManagementPage.isValueDifferentModalDisplayed(), is(true));
            Assertion.assertThat("'Value different' modal (from history) should render both Old value and New value panels with their editors",
                    uscVendorManagementPage.isValueDifferentModalStructureDisplayed(), is(true));

            if (row1BeforeCell != null && row1BeforeCell.trim().startsWith("{")) {
                Assertion.assertThat("'Value different' modal (from history) Old panel should match row 1's Config Value (Before)",
                        UscVendorManagementPage.normalizeJson(uscVendorManagementPage.getValueDifferentOldValue()),
                        is(UscVendorManagementPage.normalizeJson(row1BeforeCell)));
            }
            Assertion.assertThat("'Value different' modal (from history) New panel should match row 1's Config Value (after)",
                    UscVendorManagementPage.normalizeJson(uscVendorManagementPage.getValueDifferentNewValue()),
                    is(UscVendorManagementPage.normalizeJson(row1AfterCell)));

            step("[Cancel-without-save creates no history row] Close the Value Different + Config history modals, reopen Edit on '" + configFeatureName + "', mutate the JSON to a probe value, click Cancel (discard any prompt), then reopen Config history and assert the row count is UNCHANGED");
            int rowsBeforeCancel = historyRowCount;
            uscVendorManagementPage.closeValueDifferentModal();
            uscVendorManagementPage.closeConfigChangeHistoryModal();
            uscVendorManagementPage.filterFeatureList(configFeatureName);
            Assertion.assertThat("Feature row for '" + configFeatureName + "' should still be present after closing the history modal before the cancel-no-row probe",
                    uscVendorManagementPage.isFeatureRowPresent(configFeatureName), is(true));
            uscVendorManagementPage.clickEditFeatureButton(configFeatureName);
            Assertion.assertThat("Edit Configuration page should be displayed again for '" + configFeatureName + "' before the cancel-no-row probe",
                    uscVendorManagementPage.isEditConfigurationPageDisplayed(configFeatureName), is(true));
            String cancelProbeJson = UscVendorManagementPage.mutateFirstJsonStringValue(desiredJson, "AUTO_CANCEL_PROBE");
            if (!cancelProbeJson.equals(desiredJson)) {
                uscVendorManagementPage.setJsonEditorValue(cancelProbeJson);
            }
            uscVendorManagementPage.clickCancelOnEditConfiguration();
            uscVendorManagementPage.confirmDiscardChangesIfPrompted();
            uscVendorManagementPage.filterFeatureList(configFeatureName);
            Assertion.assertThat("Feature row for '" + configFeatureName + "' should be present again on the feature list after cancelling the edit",
                    uscVendorManagementPage.isFeatureRowPresent(configFeatureName), is(true));
            uscVendorManagementPage.clickMoreAction();
            uscVendorManagementPage.clickConfigChangeHistoryMenuItem();
            Assertion.assertThat("Config change history modal should reopen after the cancelled edit",
                    uscVendorManagementPage.isConfigChangeHistoryModalDisplayed(), is(true));
            int rowsAfterCancel = uscVendorManagementPage.getConfigChangeHistoryRowCount();
            Assertion.assertThat("Cancelling an edit WITHOUT saving must NOT add a new history row. before=" + rowsBeforeCancel + " after=" + rowsAfterCancel,
                    rowsAfterCancel, is(rowsBeforeCancel));
            uscVendorManagementPage.closeConfigChangeHistoryModal();
            testPassed = true;
        } finally {
            uscVendorManagementPage.cleanupVendorViaApi(newVendorCode, email, testPassed);
        }
    }

    @Author(names = {"Raihan Yasier"})
    @Module(name = "USCVendorManagement")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyViewSupportAgreementEditConfigB2BAndBothStoresActivateDeactivateVendor() {
        Map<String, String> param = getParam("verifyViewSupportAgreementEditConfigB2BAndBothStoresActivateDeactivateVendor");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        UscVendorManagementPage<?> uscVendorManagementPage = UscVendorManagementPage.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");
        String penaltyText = param.get("penaltyText");
        String disputeText = param.get("disputeText");
        String reconciliationText = param.get("reconciliationText");

        String b2bAffiliateNewValue = param.getOrDefault("b2bAffiliateNewValue", "AUTO_B2B_AFFILIATE");
        String bothStoresNewValue = param.getOrDefault("bothStoresNewValue", "AUTO_BOTH_STORES");
        String expectedAddSuccessMessage = param.get("expectedAddSuccessMessage");
        String expectedConfigUpdatedSnackbar = param.get("expectedConfigUpdatedSnackbar");
        String expectedActivateSnackbar = param.get("expectedActivateSnackbar");
        String expectedDeactivateSnackbar = param.get("expectedDeactivateSnackbar");
        int maxPagesToScan = Integer.parseInt(param.getOrDefault("maxPagesToScan", "10"));

        String expectedTodayDate = UscVendorManagementPage.getTodayDisplayDate();

        step("User Log in to Panel dashboard");
        memberLoginPage.gotoMemberLoginPage();
        memberLoginPage.adminLogin(email, password);
        loginMemberAdmin.gotoHome();

        step("Go to Internal Supplier Dashboard and open USC Vendor Management page");
        loginMemberAdmin.goToAccommodationSupplierDashboard();
        uscVendorManagementPage.goToVendorManagementMenu();
        Assertion.assertThat("USC Vendor Management page should be displayed", uscVendorManagementPage.isPageDisplayed(), is(true));

        step("[Vendor Management] Add a brand-new vendor so this test is self-contained");
        String newVendorCode = uscVendorManagementPage.generateVendorCode();
        boolean testPassed = false;
        try {
            uscVendorManagementPage.clickAddNewVendorButton();
            Assertion.assertThat("Add New Vendor drawer should be displayed", uscVendorManagementPage.isAddVendorDrawerDisplayed(), is(true));
            uscVendorManagementPage.fillVendorName(newVendorCode);
            uscVendorManagementPage.fillAllAgreements(penaltyText, disputeText, reconciliationText);
            uscVendorManagementPage.clickAddVendorSubmit();
            Assertion.assertThat("Snackbar should confirm success adding vendor " + newVendorCode, uscVendorManagementPage.verifySnackbarMessage(expectedAddSuccessMessage), is(true));

            step("[Vendor Detail] Open Vendor Detail via 'View vendor'");
            Assertion.assertThat("Newly added vendor " + newVendorCode + " should be present in the list", uscVendorManagementPage.findVendorCodeAcrossPages(newVendorCode, maxPagesToScan), is(true));
            uscVendorManagementPage.clickActionForVendor(newVendorCode);
            uscVendorManagementPage.clickActionMenuItem("View vendor");
            Assertion.assertThat("Vendor Detail page should be displayed for " + newVendorCode, uscVendorManagementPage.isVendorDetailPageDisplayed(newVendorCode), is(true));

            step("[Vendor Detail → More action] Verify user is able to see Support Agreements popup with the three agreement sections and their saved texts");
            uscVendorManagementPage.clickMoreAction();
            uscVendorManagementPage.clickSupportAgreementsMenuItem();
            Assertion.assertThat("Support Agreement popup should be displayed for " + newVendorCode,
                    uscVendorManagementPage.isSupportAgreementPopupDisplayed(newVendorCode), is(true));
            Assertion.assertThat("Support Agreement popup should list Penalty / Dispute / Reconciliation Agreement sections",
                    uscVendorManagementPage.getSupportAgreementSections(),
                    hasItems("Penalty Agreement", "Dispute Agreement", "Reconciliation Agreement"));
            Assertion.assertThat("Penalty Agreement section should contain the plain text set during creation",
                    uscVendorManagementPage.supportAgreementSectionContainsText("Penalty Agreement", penaltyText), is(true));
            Assertion.assertThat("Dispute Agreement section should contain the plain text set during creation",
                    uscVendorManagementPage.supportAgreementSectionContainsText("Dispute Agreement", disputeText), is(true));
            Assertion.assertThat("Reconciliation Agreement section should contain the plain text set during creation",
                    uscVendorManagementPage.supportAgreementSectionContainsText("Reconciliation Agreement", reconciliationText), is(true));
            uscVendorManagementPage.closeSupportAgreementPopup();

            step("[Add Configuration] Pick the first feature whose JSON Preview is usable for editing and capture its default JSON as the basis schema");
            uscVendorManagementPage.clickAddConfigurationButton();
            Assertion.assertThat("Add New Configuration page should be displayed", uscVendorManagementPage.isAddNewConfigurationPageDisplayed(), is(true));

            String configFeatureName = uscVendorManagementPage.selectFirstUsableFeatureForEditing();
            Assertion.assertThat("A non-empty feature name should be captured from the first usable feature card",
                    configFeatureName != null && !configFeatureName.isEmpty(), is(true));
            Assertion.assertThat("JSON Preview should be displayed after selecting '" + configFeatureName + "'",
                    uscVendorManagementPage.isJsonPreviewDisplayed(), is(true));

            String basisJson = uscVendorManagementPage.getJsonPreviewValue();
            Assertion.assertThat("JSON Preview should return a usable basis JSON for '" + configFeatureName + "'",
                    UscVendorManagementPage.isUsableBasisJson(basisJson), is(true));
            uscVendorManagementPage.submitAddConfiguration();
            Assertion.assertThat("'Configuration added successfully!' modal should be displayed",
                    uscVendorManagementPage.isConfigurationAddedModalDisplayed(), is(true));

            step("[Edit Configuration → B2B Affiliate] Choose 'Yes, edit now' and save a new JSON payload on the B2B Affiliate tab");
            uscVendorManagementPage.clickYesEditNowInAddedModal();
            Assertion.assertThat("Edit Configuration page should be displayed for '" + configFeatureName + "'",
                    uscVendorManagementPage.isEditConfigurationPageDisplayed(configFeatureName), is(true));
            uscVendorManagementPage.selectStoreScopeTab("B2B Affiliate");
            String previousJsonB2B = uscVendorManagementPage.getJsonEditorValue();

            String sourceJsonB2B = (previousJsonB2B != null && previousJsonB2B.trim().startsWith("{"))
                    ? previousJsonB2B : basisJson;
            String desiredJsonB2B = UscVendorManagementPage.mutateFirstJsonStringValue(sourceJsonB2B, b2bAffiliateNewValue);
            uscVendorManagementPage.setJsonEditorValue(desiredJsonB2B);
            uscVendorManagementPage.clickSaveChangesOnEditConfiguration();
            Assertion.assertThat("'Value different' modal should be displayed after Save changes on B2B Affiliate",
                    uscVendorManagementPage.isValueDifferentModalDisplayed(), is(true));
            Assertion.assertThat("'Value different' modal (B2B Affiliate) Old panel should match the pre-edit JSON",
                    UscVendorManagementPage.normalizeJson(uscVendorManagementPage.getValueDifferentOldValue()),
                    is(UscVendorManagementPage.normalizeJson(previousJsonB2B)));
            Assertion.assertThat("'Value different' modal (B2B Affiliate) New panel should match the newly edited JSON",
                    UscVendorManagementPage.normalizeJson(uscVendorManagementPage.getValueDifferentNewValue()),
                    is(UscVendorManagementPage.normalizeJson(desiredJsonB2B)));
            uscVendorManagementPage.clickConfirmChanges();
            Assertion.assertThat("Snackbar should confirm B2B Affiliate configuration updated successfully",
                    uscVendorManagementPage.verifySnackbarMessage(expectedConfigUpdatedSnackbar), is(true));

            step("[Config change history after B2B Affiliate edit] Open the history modal and verify the latest row reflects the B2B Affiliate edit");
            uscVendorManagementPage.clickMoreAction();
            uscVendorManagementPage.clickConfigChangeHistoryMenuItem();
            Assertion.assertThat("Config change history modal should be displayed after B2B Affiliate edit",
                    uscVendorManagementPage.isConfigChangeHistoryModalDisplayed(), is(true));
            String row1FeatureNameB2B = uscVendorManagementPage.getHistoryFeatureName(1);
            Assertion.assertThat("History row 1 Feature Name should start with '" + configFeatureName + "' after B2B Affiliate edit",
                    row1FeatureNameB2B != null && row1FeatureNameB2B.startsWith(configFeatureName), is(true));
            Assertion.assertThat("History row 1 Store Scope should be 'B2B Affiliate' after B2B Affiliate edit",
                    uscVendorManagementPage.getHistoryStoreScope(1), is("B2B Affiliate"));
            String row1BeforeCellB2B = uscVendorManagementPage.getHistoryBeforeValue(1);
            if (row1BeforeCellB2B != null && row1BeforeCellB2B.trim().startsWith("{")) {
                Assertion.assertThat("History row 1 Config Value (Before) should match the pre-edit JSON (B2B Affiliate)",
                        UscVendorManagementPage.normalizeJson(row1BeforeCellB2B),
                        is(UscVendorManagementPage.normalizeJson(previousJsonB2B)));
            }
            Assertion.assertThat("History row 1 Config Value (after) should match the newly edited JSON (B2B Affiliate)",
                    UscVendorManagementPage.normalizeJson(uscVendorManagementPage.getHistoryAfterValue(1)),
                    is(UscVendorManagementPage.normalizeJson(desiredJsonB2B)));
            Assertion.assertThat("History row 1 Latest Edited date should be today's date formatted as '" + expectedTodayDate + "' (B2B Affiliate)",
                    uscVendorManagementPage.getHistoryLatestEditedDate(1), is(expectedTodayDate));
            Assertion.assertThat("History row 1 Latest Edited actor should reference the logged-in email '" + email + "' (B2B Affiliate)",
                    uscVendorManagementPage.getHistoryLatestEditedActor(1).contains(email), is(true));
            uscVendorManagementPage.closeConfigChangeHistoryModal();

            step("[Edit Configuration → Both stores] Re-enter Edit Configuration via the feature row's Edit button and save a new JSON payload on the Both stores tab");
            uscVendorManagementPage.filterFeatureList(configFeatureName);
            Assertion.assertThat("Feature row '" + configFeatureName + "' should be visible before clicking its Edit button",
                    uscVendorManagementPage.isFeatureRowPresent(configFeatureName), is(true));
            uscVendorManagementPage.clickEditFeatureButton(configFeatureName);
            Assertion.assertThat("Edit Configuration page should be displayed for '" + configFeatureName + "' (Both stores flow)",
                    uscVendorManagementPage.isEditConfigurationPageDisplayed(configFeatureName), is(true));
            uscVendorManagementPage.selectStoreScopeTab("Both stores");
            String previousJsonBoth = uscVendorManagementPage.getJsonEditorValue();
            String sourceJsonBoth = (previousJsonBoth != null && previousJsonBoth.trim().startsWith("{"))
                    ? previousJsonBoth : basisJson;
            String desiredJsonBoth = UscVendorManagementPage.mutateFirstJsonStringValue(sourceJsonBoth, bothStoresNewValue);
            uscVendorManagementPage.setJsonEditorValue(desiredJsonBoth);
            uscVendorManagementPage.clickSaveChangesOnEditConfiguration();
            Assertion.assertThat("'Value different' modal should be displayed after Save changes on Both stores",
                    uscVendorManagementPage.isValueDifferentModalDisplayed(), is(true));
            Assertion.assertThat("'Value different' modal (Both stores) Old panel should match the pre-edit JSON",
                    UscVendorManagementPage.normalizeJson(uscVendorManagementPage.getValueDifferentOldValue()),
                    is(UscVendorManagementPage.normalizeJson(previousJsonBoth)));
            Assertion.assertThat("'Value different' modal (Both stores) New panel should match the newly edited JSON",
                    UscVendorManagementPage.normalizeJson(uscVendorManagementPage.getValueDifferentNewValue()),
                    is(UscVendorManagementPage.normalizeJson(desiredJsonBoth)));
            uscVendorManagementPage.clickConfirmChanges();
            Assertion.assertThat("Snackbar should confirm Both stores configuration updated successfully",
                    uscVendorManagementPage.verifySnackbarMessage(expectedConfigUpdatedSnackbar), is(true));

            step("[Config change history after Both stores edit] Verify the edit fanned out into two rows (one B2C, one B2B Affiliate) with the same payload — 'Both stores' is not a scope value the history table uses");
            uscVendorManagementPage.clickMoreAction();
            uscVendorManagementPage.clickConfigChangeHistoryMenuItem();
            Assertion.assertThat("Config change history modal should be displayed after Both stores edit",
                    uscVendorManagementPage.isConfigChangeHistoryModalDisplayed(), is(true));

            String row1FeatureNameBoth = uscVendorManagementPage.getHistoryFeatureName(1);
            String row2FeatureNameBoth = uscVendorManagementPage.getHistoryFeatureName(2);
            Assertion.assertThat("History row 1 Feature Name should start with '" + configFeatureName + "' after Both stores edit",
                    row1FeatureNameBoth != null && row1FeatureNameBoth.startsWith(configFeatureName), is(true));
            Assertion.assertThat("History row 2 Feature Name should start with '" + configFeatureName + "' after Both stores edit",
                    row2FeatureNameBoth != null && row2FeatureNameBoth.startsWith(configFeatureName), is(true));

            Set<String> topTwoScopes = new HashSet<>(Arrays.asList(
                    uscVendorManagementPage.getHistoryStoreScope(1),
                    uscVendorManagementPage.getHistoryStoreScope(2)));
            Assertion.assertThat("Top two history rows after a Both stores edit should be a { 'B2C', 'B2B Affiliate' } pair (that is how the app records a Both-stores change)",
                    topTwoScopes, is(new HashSet<>(Arrays.asList("B2C", "B2B Affiliate"))));

            String normalizedDesiredBoth = UscVendorManagementPage.normalizeJson(desiredJsonBoth);
            Assertion.assertThat("History row 1 Config Value (after) should match the newly edited JSON (Both stores)",
                    UscVendorManagementPage.normalizeJson(uscVendorManagementPage.getHistoryAfterValue(1)),
                    is(normalizedDesiredBoth));
            Assertion.assertThat("History row 2 Config Value (after) should also match the newly edited JSON (Both stores fans out with the same payload)",
                    UscVendorManagementPage.normalizeJson(uscVendorManagementPage.getHistoryAfterValue(2)),
                    is(normalizedDesiredBoth));

            Assertion.assertThat("History row 1 Latest Edited date should be today's date formatted as '" + expectedTodayDate + "' (Both stores)",
                    uscVendorManagementPage.getHistoryLatestEditedDate(1), is(expectedTodayDate));
            Assertion.assertThat("History row 2 Latest Edited date should be today's date formatted as '" + expectedTodayDate + "' (Both stores)",
                    uscVendorManagementPage.getHistoryLatestEditedDate(2), is(expectedTodayDate));
            Assertion.assertThat("History row 1 Latest Edited actor should reference the logged-in email '" + email + "' (Both stores)",
                    uscVendorManagementPage.getHistoryLatestEditedActor(1).contains(email), is(true));
            Assertion.assertThat("History row 2 Latest Edited actor should reference the logged-in email '" + email + "' (Both stores)",
                    uscVendorManagementPage.getHistoryLatestEditedActor(2).contains(email), is(true));
            uscVendorManagementPage.closeConfigChangeHistoryModal();

            step("[Vendor Status → Activate] Activate the vendor via the confirmation modal and assert the success snackbar");
            uscVendorManagementPage.clickMoreAction();
            uscVendorManagementPage.clickActivateMenuItem();
            Assertion.assertThat("Activate confirmation modal should be displayed for " + newVendorCode,
                    uscVendorManagementPage.isActivateModalDisplayed(newVendorCode), is(true));
            uscVendorManagementPage.clickYesActivate();
            Assertion.assertThat("Snackbar should confirm the vendor has been activated (expected to contain '" + expectedActivateSnackbar + "')",
                    uscVendorManagementPage.verifySnackbarMessage(expectedActivateSnackbar), is(true));

            step("[Vendor Status → Deactivate] Deactivate the vendor via the confirmation modal and assert the success snackbar");
            uscVendorManagementPage.clickMoreAction();
            uscVendorManagementPage.clickDeactivateMenuItem();
            Assertion.assertThat("Deactivate confirmation modal should be displayed for " + newVendorCode,
                    uscVendorManagementPage.isDeactivateModalDisplayed(newVendorCode), is(true));
            uscVendorManagementPage.clickYesDeactivate();
            Assertion.assertThat("Snackbar should confirm the vendor has been deactivated (expected to contain '" + expectedDeactivateSnackbar + "')",
                    uscVendorManagementPage.verifySnackbarMessage(expectedDeactivateSnackbar), is(true));
            testPassed = true;
        } finally {
            uscVendorManagementPage.cleanupVendorViaApi(newVendorCode, email, testPassed);
        }
    }

    @Author(names = {"Raihan Yasier"})
    @Module(name = "USCVendorManagement")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyMinFeaturesGuardActivateFromListCancelDeactivateAndEditWhileInactive() {
        Map<String, String> param = getParam("verifyMinFeaturesGuardActivateFromListCancelDeactivateAndEditWhileInactive");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        UscVendorManagementPage<?> uscVendorManagementPage = UscVendorManagementPage.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");
        String penaltyText = param.getOrDefault("penaltyText", "Auto Penalty Agreement");
        String disputeText = param.getOrDefault("disputeText", "Auto Dispute Agreement");
        String reconciliationText = param.getOrDefault("reconciliationText", "Auto Reconciliation Agreement");
        String expectedAddSuccessMessage = param.getOrDefault("expectedAddSuccessMessage", "You’ve added a new vendor");
        String expectedActivateSnackbar = param.getOrDefault("expectedActivateSnackbar", "has been activated");
        String expectedDeactivateSnackbar = param.getOrDefault("expectedDeactivateSnackbar", "has been deactivated");
        int maxPagesToScan = Integer.parseInt(param.getOrDefault("maxPagesToScan", "10"));

        step("User Log in to Panel dashboard");
        memberLoginPage.gotoMemberLoginPage();
        memberLoginPage.adminLogin(email, password);
        loginMemberAdmin.gotoHome();

        step("Go to Internal Supplier Dashboard and open USC Vendor Management page");
        loginMemberAdmin.goToAccommodationSupplierDashboard();
        uscVendorManagementPage.goToVendorManagementMenu();
        Assertion.assertThat("USC Vendor Management page should be displayed", uscVendorManagementPage.isPageDisplayed(), is(true));

        step("[Vendor Management] Add a brand-new vendor so this test is self-contained");
        String newVendorCode = uscVendorManagementPage.generateVendorCode();
        boolean testPassed = false;
        try {
            uscVendorManagementPage.clickAddNewVendorButton();
            Assertion.assertThat("Add New Vendor drawer should be displayed", uscVendorManagementPage.isAddVendorDrawerDisplayed(), is(true));
            uscVendorManagementPage.fillVendorName(newVendorCode);
            uscVendorManagementPage.fillAllAgreements(penaltyText, disputeText, reconciliationText);
            uscVendorManagementPage.clickAddVendorSubmit();
            Assertion.assertThat("Snackbar should confirm success adding vendor " + newVendorCode,
                    uscVendorManagementPage.verifySnackbarMessage(expectedAddSuccessMessage), is(true));

            step("[Vendor Detail] Open Vendor Detail via 'View vendor' for the freshly created vendor with 0 configurations");
            Assertion.assertThat("Newly added vendor " + newVendorCode + " should be present in the list",
                    uscVendorManagementPage.findVendorCodeAcrossPages(newVendorCode, maxPagesToScan), is(true));
            uscVendorManagementPage.clickActionForVendor(newVendorCode);
            uscVendorManagementPage.clickActionMenuItem("View vendor");
            Assertion.assertThat("Vendor Detail page should be displayed for " + newVendorCode,
                    uscVendorManagementPage.isVendorDetailPageDisplayed(newVendorCode), is(true));

            step("[Activate @ 0 configs] Click Activate on a fresh vendor and assert the 'Minimum features required' modal blocks the flow (guardrail for vendors with 0 configurations)");
            uscVendorManagementPage.clickMoreAction();
            uscVendorManagementPage.clickActivateMenuItem();
            Assertion.assertThat("'Minimum features required' modal should be displayed when trying to Activate a vendor with 0 configurations",
                    uscVendorManagementPage.isMinimumFeaturesRequiredModalDisplayed(), is(true));

            step("[Add Configuration via 'Add features' shortcut] Follow the modal's 'Add features' button and add the first configuration on the vendor");
            uscVendorManagementPage.clickAddFeaturesInMinFeaturesModal();
            Assertion.assertThat("Add New Configuration page should be displayed after clicking 'Add features' on the guardrail modal",
                    uscVendorManagementPage.isAddNewConfigurationPageDisplayed(), is(true));
            String configFeatureName = uscVendorManagementPage.selectFirstUsableFeatureForEditing();
            Assertion.assertThat("A non-empty feature name should be captured from the first usable feature card",
                    configFeatureName != null && !configFeatureName.isEmpty(), is(true));
            uscVendorManagementPage.submitAddConfiguration();
            Assertion.assertThat("'Configuration added successfully!' modal should be displayed",
                    uscVendorManagementPage.isConfigurationAddedModalDisplayed(), is(true));
            uscVendorManagementPage.clickMaybeLaterInAddedModal();

            step("[Activate @ ≥1 config] Re-open More action, click Activate, and this time assert the real Activate confirmation modal is shown (no longer blocked by the 'Minimum features required' guardrail)");
            Assertion.assertThat("Vendor Detail page should still be displayed after adding the configuration",
                    uscVendorManagementPage.isVendorDetailPageDisplayed(newVendorCode), is(true));
            uscVendorManagementPage.clickMoreAction();
            uscVendorManagementPage.clickActivateMenuItem();
            Assertion.assertThat("Activate confirmation modal should be displayed for " + newVendorCode + " once the vendor has at least one configuration",
                    uscVendorManagementPage.isActivateModalDisplayed(newVendorCode), is(true));
            uscVendorManagementPage.clickYesActivate();
            Assertion.assertThat("Snackbar should confirm the vendor has been activated (expected to contain '" + expectedActivateSnackbar + "')",
                    uscVendorManagementPage.verifySnackbarMessage(expectedActivateSnackbar), is(true));

            step("[Vendor List] Return to the vendor list and assert the Status column for " + newVendorCode + " is now 'Active'");
            uscVendorManagementPage.goToVendorManagementMenu();
            Assertion.assertThat("Vendor " + newVendorCode + " should still be findable after activation",
                    uscVendorManagementPage.findVendorCodeAcrossPages(newVendorCode, maxPagesToScan), is(true));
            Assertion.assertThat("Vendor " + newVendorCode + " Status column should be 'Active' after activation",
                    uscVendorManagementPage.getVendorStatus(newVendorCode), is("Active"));

            step("[Deactivate → Cancel] Re-open the vendor, trigger Deactivate, then click Cancel in the confirmation modal and assert the vendor stays Active");
            uscVendorManagementPage.clickActionForVendor(newVendorCode);
            uscVendorManagementPage.clickActionMenuItem("View vendor");
            Assertion.assertThat("Vendor Detail page should be displayed for " + newVendorCode + " before triggering Cancel-deactivate",
                    uscVendorManagementPage.isVendorDetailPageDisplayed(newVendorCode), is(true));
            uscVendorManagementPage.clickMoreAction();
            uscVendorManagementPage.clickDeactivateMenuItem();
            Assertion.assertThat("Deactivate confirmation modal should be displayed for " + newVendorCode + " before Cancel",
                    uscVendorManagementPage.isDeactivateModalDisplayed(newVendorCode), is(true));
            uscVendorManagementPage.clickCancelStatusModal();

            uscVendorManagementPage.goToVendorManagementMenu();
            Assertion.assertThat("Vendor " + newVendorCode + " should still be findable after Cancel-deactivate",
                    uscVendorManagementPage.findVendorCodeAcrossPages(newVendorCode, maxPagesToScan), is(true));
            Assertion.assertThat("Vendor " + newVendorCode + " Status column should REMAIN 'Active' after Cancel on the deactivation modal",
                    uscVendorManagementPage.getVendorStatus(newVendorCode), is("Active"));

            step("[Deactivate → Confirm] Re-open the vendor, trigger Deactivate, click Yes and assert the vendor becomes Inactive back on the list");
            uscVendorManagementPage.clickActionForVendor(newVendorCode);
            uscVendorManagementPage.clickActionMenuItem("View vendor");
            Assertion.assertThat("Vendor Detail page should be displayed for " + newVendorCode + " before confirming deactivation",
                    uscVendorManagementPage.isVendorDetailPageDisplayed(newVendorCode), is(true));
            uscVendorManagementPage.clickMoreAction();
            uscVendorManagementPage.clickDeactivateMenuItem();
            Assertion.assertThat("Deactivate confirmation modal should be displayed for " + newVendorCode + " before Confirm",
                    uscVendorManagementPage.isDeactivateModalDisplayed(newVendorCode), is(true));
            uscVendorManagementPage.clickYesDeactivate();
            Assertion.assertThat("Snackbar should confirm the vendor has been deactivated (expected to contain '" + expectedDeactivateSnackbar + "')",
                    uscVendorManagementPage.verifySnackbarMessage(expectedDeactivateSnackbar), is(true));

            uscVendorManagementPage.goToVendorManagementMenu();
            Assertion.assertThat("Vendor " + newVendorCode + " should still be findable after Confirm-deactivate",
                    uscVendorManagementPage.findVendorCodeAcrossPages(newVendorCode, maxPagesToScan), is(true));
            Assertion.assertThat("Vendor " + newVendorCode + " Status column should be 'Inactive' after Confirm on the deactivation modal",
                    uscVendorManagementPage.getVendorStatus(newVendorCode), is("Inactive"));

            step("[Edit while Inactive] Re-open the (now Inactive) vendor and verify the feature row's Edit button still opens the Edit Configuration page normally");
            uscVendorManagementPage.clickActionForVendor(newVendorCode);
            uscVendorManagementPage.clickActionMenuItem("View vendor");
            Assertion.assertThat("Vendor Detail page should be displayed for the Inactive vendor " + newVendorCode,
                    uscVendorManagementPage.isVendorDetailPageDisplayed(newVendorCode), is(true));
            uscVendorManagementPage.filterFeatureList(configFeatureName);
            Assertion.assertThat("Feature row '" + configFeatureName + "' should still be visible on the Inactive vendor's detail page",
                    uscVendorManagementPage.isFeatureRowPresent(configFeatureName), is(true));
            uscVendorManagementPage.clickEditFeatureButton(configFeatureName);
            Assertion.assertThat("Edit Configuration page should still open for '" + configFeatureName + "' even though the vendor is Inactive",
                    uscVendorManagementPage.isEditConfigurationPageDisplayed(configFeatureName), is(true));
            testPassed = true;
        } finally {
            uscVendorManagementPage.cleanupVendorViaApi(newVendorCode, email, testPassed);
        }
    }

    @Author(names = {"Raihan Yasier"})
    @Module(name = "USCVendorManagement")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyEditRowActionEnabledAndOpensDrawerForActiveAndInactiveVendor() {
        Map<String, String> param = getParam("verifyEditRowActionEnabledAndOpensDrawerForActiveAndInactiveVendor");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        UscVendorManagementPage<?> uscVendorManagementPage = UscVendorManagementPage.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");
        String knownActiveVendorCode = param.get("knownActiveVendorCode");
        String knownInactiveVendorCode = param.get("knownInactiveVendorCode");
        String expectedActiveStatus = param.getOrDefault("expectedActiveStatus", "Active");
        String expectedInactiveStatus = param.getOrDefault("expectedInactiveStatus", "Inactive");
        int maxPagesToScan = Integer.parseInt(param.getOrDefault("maxPagesToScan", "10"));

        if (knownActiveVendorCode == null || knownActiveVendorCode.isEmpty()
                || knownInactiveVendorCode == null || knownInactiveVendorCode.isEmpty()) {
            throw new IllegalArgumentException(
                    "knownActiveVendorCode and knownInactiveVendorCode params are required for this test.");
        }

        step("User Log in to Panel dashboard");
        memberLoginPage.gotoMemberLoginPage();
        memberLoginPage.adminLogin(email, password);
        loginMemberAdmin.gotoHome();

        step("Go to Internal Supplier Dashboard and open USC Vendor Management page");
        loginMemberAdmin.goToAccommodationSupplierDashboard();
        uscVendorManagementPage.goToVendorManagementMenu();
        Assertion.assertThat("USC Vendor Management page should be displayed",
                uscVendorManagementPage.isPageDisplayed(), is(true));

        for (String[] scenario : new String[][]{
                {knownInactiveVendorCode, expectedInactiveStatus},
                {knownActiveVendorCode, expectedActiveStatus}}) {
            String vendorCode = scenario[0];
            String expectedStatus = scenario[1];

            step("[Row action] Locate " + expectedStatus + " vendor '" + vendorCode + "' on the Vendor list");
            Assertion.assertThat("Vendor '" + vendorCode + "' should be findable within the first " + maxPagesToScan + " pages",
                    uscVendorManagementPage.findVendorCodeAcrossPages(vendorCode, maxPagesToScan), is(true));
            Assertion.assertThat("Vendor '" + vendorCode + "' Status column should be '" + expectedStatus + "'",
                    uscVendorManagementPage.getVendorStatus(vendorCode), is(expectedStatus));

            step("[Row action] Open row action menu and assert 'Edit vendor' is enabled for the " + expectedStatus + " vendor '" + vendorCode + "'");
            uscVendorManagementPage.clickActionForVendor(vendorCode);
            Assertion.assertThat("'Edit vendor' menu item should be enabled on the " + expectedStatus + " row of vendor '" + vendorCode + "'",
                    uscVendorManagementPage.isActionMenuItemEnabled("Edit vendor"), is(true));

            step("[Row action] Click 'Edit vendor' and assert the Edit Vendor drawer opens with the vendor code in the title");
            uscVendorManagementPage.clickActionMenuItem("Edit vendor");
            Assertion.assertThat("Edit Vendor drawer should be displayed with title referencing '" + vendorCode + "'",
                    uscVendorManagementPage.isEditVendorDrawerDisplayed(vendorCode), is(true));

            step("[Row action] Close the Edit Vendor drawer via Cancel and return to the Vendor list");
            uscVendorManagementPage.clickDrawerCancel();
            uscVendorManagementPage.goToVendorManagementMenu();
            Assertion.assertThat("USC Vendor Management page should be displayed again after closing the drawer",
                    uscVendorManagementPage.isPageDisplayed(), is(true));
        }
    }

    @Author(names = {"Raihan Yasier"})
    @Module(name = "USCVendorManagement")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyVendorListShowsColumnsAlphabeticallySortedAndLatestUpdatedReflectsCreateAndEdit() {
        Map<String, String> param = getParam("verifyVendorListShowsColumnsAlphabeticallySortedAndLatestUpdatedReflectsCreateAndEdit");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        UscVendorManagementPage<?> uscVendorManagementPage = UscVendorManagementPage.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");
        String penaltyText = param.getOrDefault("penaltyText", "Auto Penalty Agreement");
        String disputeText = param.getOrDefault("disputeText", "Auto Dispute Agreement");
        String reconciliationText = param.getOrDefault("reconciliationText", "Auto Reconciliation Agreement");
        String editedPenaltyText = param.getOrDefault("editedPenaltyText", penaltyText + " - Edited");
        String editedDisputeText = param.getOrDefault("editedDisputeText", disputeText + " - Edited");
        String editedReconciliationText = param.getOrDefault("editedReconciliationText", reconciliationText + " - Edited");
        String expectedAddSuccessMessage = param.getOrDefault("expectedAddSuccessMessage", "You\u2019ve added a new vendor");
        String expectedEditSuccessMessage = param.getOrDefault("expectedEditSuccessMessage", "Vendor saved successfully");
        int maxPagesToScan = Integer.parseInt(param.getOrDefault("maxPagesToScan", "10"));
        int minPageSize = Integer.parseInt(param.getOrDefault("minPageSize", "2"));

        List<String> expectedHeaders = Arrays.asList(
                param.getOrDefault("expectedNoHeader", "No."),
                param.getOrDefault("expectedVendorCodeHeader", "Vendor Code"),
                param.getOrDefault("expectedStatusHeader", "Status"),
                param.getOrDefault("expectedLatestUpdatedHeader", "Latest Updated"));

        step("User Log in to Panel dashboard");
        memberLoginPage.gotoMemberLoginPage();
        memberLoginPage.adminLogin(email, password);
        loginMemberAdmin.gotoHome();

        step("Go to Internal Supplier Dashboard and open USC Vendor Management page");
        loginMemberAdmin.goToAccommodationSupplierDashboard();
        uscVendorManagementPage.goToVendorManagementMenu();
        Assertion.assertThat("USC Vendor Management page should be displayed",
                uscVendorManagementPage.isPageDisplayed(), is(true));

        step("[Vendor list] Verify the table header renders the expected named columns");
        List<String> actualHeaders = uscVendorManagementPage.getListColumnHeaderTexts();
        Assertion.assertThat("Vendor list header row should contain all named columns " + expectedHeaders + " but got " + actualHeaders,
                actualHeaders.containsAll(expectedHeaders), is(true));

        step("[Vendor list] Verify the page-1 vendor codes are alphabetically sorted (case-insensitive) and count \u2265 " + minPageSize);
        List<String> pageOneCodes = uscVendorManagementPage.getAllVendorCodesOnCurrentPage();
        Assertion.assertThat("Vendor list page 1 should render at least " + minPageSize + " rows (actual=" + pageOneCodes.size() + ")",
                pageOneCodes.size() >= minPageSize, is(true));
        Assertion.assertThat("Vendor list page-1 codes should be alphabetically sorted (case-insensitive), actual order=" + pageOneCodes,
                UscVendorManagementPage.isAlphabeticallySortedIgnoreCase(pageOneCodes), is(true));

        String expectedTodayPrefix = UscVendorManagementPage.getTodayListDate();

        step("[Add vendor] Create a fresh vendor and verify Latest Updated reflects today + creator email");
        String newVendorCode = uscVendorManagementPage.generateVendorCode();
        boolean testPassed = false;
        try {
            uscVendorManagementPage.clickAddNewVendorButton();
            Assertion.assertThat("Add New Vendor drawer should be displayed",
                    uscVendorManagementPage.isAddVendorDrawerDisplayed(), is(true));
            uscVendorManagementPage.fillVendorName(newVendorCode);
            uscVendorManagementPage.fillAllAgreements(penaltyText, disputeText, reconciliationText);
            uscVendorManagementPage.clickAddVendorSubmit();
            Assertion.assertThat("Snackbar should confirm the vendor was created",
                    uscVendorManagementPage.verifySnackbarMessage(expectedAddSuccessMessage), is(true));

            String[] created = uscVendorManagementPage.readLatestUpdatedAcrossPages(newVendorCode, maxPagesToScan);
            String createdDate = created[0];
            String createdActor = created[1];
            Assertion.assertThat("Newly created vendor '" + newVendorCode + "' should be findable and have a populated Latest Updated cell within " + maxPagesToScan + " pages",
                    !createdDate.isEmpty(), is(true));
            Assertion.assertThat("Latest Updated date for newly created vendor '" + newVendorCode + "' should start with today ('" + expectedTodayPrefix + "'), actual='" + createdDate + "'",
                    createdDate.startsWith(expectedTodayPrefix), is(true));
            Assertion.assertThat("Latest Updated actor for newly created vendor '" + newVendorCode + "' should reference the creator email '" + email + "', actual='" + createdActor + "'",
                    createdActor.contains(email), is(true));

            step("[Edit vendor] Edit the same vendor and verify Latest Updated is refreshed to today + editor email");
            uscVendorManagementPage.clickActionForVendor(newVendorCode);
            uscVendorManagementPage.clickActionMenuItem("Edit vendor");
            Assertion.assertThat("Edit Vendor drawer should be displayed for '" + newVendorCode + "'",
                    uscVendorManagementPage.isEditVendorDrawerDisplayed(newVendorCode), is(true));
            uscVendorManagementPage.fillAllAgreements(editedPenaltyText, editedDisputeText, editedReconciliationText);
            uscVendorManagementPage.clickSaveChanges();
            Assertion.assertThat("Snackbar should confirm the vendor was edited",
                    uscVendorManagementPage.verifySnackbarMessage(expectedEditSuccessMessage), is(true));

            String editedDate = uscVendorManagementPage.getVendorLatestUpdatedDate(newVendorCode);
            String editedActor = uscVendorManagementPage.getVendorLatestUpdatedActor(newVendorCode);
            Assertion.assertThat("Latest Updated date after edit for '" + newVendorCode + "' should start with today ('" + expectedTodayPrefix + "'), actual='" + editedDate + "'",
                    editedDate.startsWith(expectedTodayPrefix), is(true));
            Assertion.assertThat("Latest Updated actor after edit for '" + newVendorCode + "' should reference the editor email '" + email + "', actual='" + editedActor + "'",
                    editedActor.contains(email), is(true));
            testPassed = true;
        } finally {
            uscVendorManagementPage.cleanupVendorViaApi(newVendorCode, email, testPassed);
        }
    }

    @Author(names = {"Raihan Yasier"})
    @Module(name = "USCVendorManagement")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyAddVendorNegativeValidationMatrixBlocksSubmitAcrossNameAndAgreementFields() {
        Map<String, String> param = getParam("verifyAddVendorNegativeValidationMatrixBlocksSubmitAcrossNameAndAgreementFields");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        UscVendorManagementPage<?> uscVendorManagementPage = UscVendorManagementPage.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");
        String knownActiveVendorCode = param.get("knownActiveVendorCode");
        String knownInactiveVendorCode = param.get("knownInactiveVendorCode");
        String validPenaltyText = param.getOrDefault("validPenaltyText", "Auto Penalty Agreement");
        String validDisputeText = param.getOrDefault("validDisputeText", "Auto Dispute Agreement");
        String validReconciliationText = param.getOrDefault("validReconciliationText", "Auto Reconciliation Agreement");
        String invalidNameLowercase = param.getOrDefault("invalidNameLowercase", "vendorlower");
        String invalidNameWithSpaces = param.getOrDefault("invalidNameWithSpaces", "VENDOR NAME");
        String invalidNameWithHyphens = param.getOrDefault("invalidNameWithHyphens", "VENDOR-NAME");
        String whitespaceAgreementValue = param.getOrDefault("whitespaceAgreementValue", "   ");

        if (knownActiveVendorCode == null || knownActiveVendorCode.isEmpty()
                || knownInactiveVendorCode == null || knownInactiveVendorCode.isEmpty()) {
            throw new IllegalArgumentException(
                    "knownActiveVendorCode and knownInactiveVendorCode params are required for this test.");
        }

        String baseValidName = uscVendorManagementPage.generateVendorCode();

        List<String[]> cases = List.of(
                new String[]{"empty_name", "", validPenaltyText, validDisputeText, validReconciliationText},
                new String[]{"lowercase_name", invalidNameLowercase, validPenaltyText, validDisputeText, validReconciliationText},
                new String[]{"spaces_in_name", invalidNameWithSpaces, validPenaltyText, validDisputeText, validReconciliationText},
                new String[]{"hyphens_in_name", invalidNameWithHyphens, validPenaltyText, validDisputeText, validReconciliationText},
                new String[]{"duplicate_active_name", knownActiveVendorCode, validPenaltyText, validDisputeText, validReconciliationText},
                new String[]{"duplicate_inactive_name", knownInactiveVendorCode, validPenaltyText, validDisputeText, validReconciliationText},
                new String[]{"empty_penalty", baseValidName, "", validDisputeText, validReconciliationText},
                new String[]{"empty_dispute", baseValidName, validPenaltyText, "", validReconciliationText},
                new String[]{"empty_reconciliation", baseValidName, validPenaltyText, validDisputeText, ""},
                new String[]{"whitespace_penalty", baseValidName, whitespaceAgreementValue, validDisputeText, validReconciliationText},
                new String[]{"whitespace_dispute", baseValidName, validPenaltyText, whitespaceAgreementValue, validReconciliationText},
                new String[]{"whitespace_reconciliation", baseValidName, validPenaltyText, validDisputeText, whitespaceAgreementValue});

        step("User Log in to Panel dashboard");
        memberLoginPage.gotoMemberLoginPage();
        memberLoginPage.adminLogin(email, password);
        loginMemberAdmin.gotoHome();

        step("Go to Internal Supplier Dashboard and open USC Vendor Management page");
        loginMemberAdmin.goToAccommodationSupplierDashboard();
        uscVendorManagementPage.goToVendorManagementMenu();
        Assertion.assertThat("USC Vendor Management page should be displayed",
                uscVendorManagementPage.isPageDisplayed(), is(true));

        List<String> failedCases = new ArrayList<>();

        for (String[] c : cases) {
            String caseName = c[0];
            String nameValue = c[1];
            String penaltyValue = c[2];
            String disputeValue = c[3];
            String reconciliationValue = c[4];

            step("[" + caseName + "] Open Add New Vendor drawer");
            uscVendorManagementPage.clickAddNewVendorButton();
            Assertion.assertThat("[" + caseName + "] Add New Vendor drawer should open",
                    uscVendorManagementPage.isAddVendorDrawerDisplayed(), is(true));

            step("[" + caseName + "] Fill vendor name='" + nameValue + "' and agreements (penalty='" + penaltyValue + "', dispute='" + disputeValue + "', reconciliation='" + reconciliationValue + "')");
            uscVendorManagementPage.fillVendorName(nameValue);
            uscVendorManagementPage.fillAllAgreements(penaltyValue, disputeValue, reconciliationValue);

            step("[" + caseName + "] Attempt to submit and verify vendor was NOT added");
            boolean submitEnabled = uscVendorManagementPage.isAddVendorSubmitEnabled();
            boolean errorVisibleBeforeClick = uscVendorManagementPage.isAnyDrawerFieldErrorVisible();
            boolean drawerStillOpenAfterAttempt;
            if (submitEnabled) {
                uscVendorManagementPage.attemptAddVendorSubmitNoWait();
                drawerStillOpenAfterAttempt = uscVendorManagementPage.isAddVendorDrawerStillOpen();
            } else {
                drawerStillOpenAfterAttempt = uscVendorManagementPage.isAddVendorDrawerStillOpen();
            }
            boolean errorVisibleAfterAttempt = uscVendorManagementPage.isAnyDrawerFieldErrorVisible();
            boolean invalidBlocked = !submitEnabled || drawerStillOpenAfterAttempt;

            String verdict = "case=" + caseName
                    + ", submitEnabled=" + submitEnabled
                    + ", errorVisibleBeforeClick=" + errorVisibleBeforeClick
                    + ", errorVisibleAfterAttempt=" + errorVisibleAfterAttempt
                    + ", drawerStillOpenAfterAttempt=" + drawerStillOpenAfterAttempt;
            if (!invalidBlocked) {
                failedCases.add(verdict);
            }
            Assertion.assertThat("[" + caseName + "] Invalid input should be blocked (" + verdict + ")",
                    invalidBlocked, is(true));

            step("[" + caseName + "] Close the drawer to reset state for the next case");
            uscVendorManagementPage.closeAddVendorDrawerSafely();
            Assertion.assertThat("[" + caseName + "] Vendor list should be shown again after closing the drawer",
                    uscVendorManagementPage.isPageDisplayed(), is(true));
        }

        Assertion.assertThat("All negative cases should be blocked. Failed cases: " + failedCases,
                failedCases.isEmpty(), is(true));
    }

    @Author(names = {"Raihan Yasier"})
    @Module(name = "USCVendorManagement")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyCommercialUserReadOnlyWalkthroughAndAllWriteAffordancesAreHidden() {
        Map<String, String> param = getParam("verifyCommercialUserReadOnlyWalkthroughAndAllWriteAffordancesAreHidden");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        UscVendorManagementPage<?> uscVendorManagementPage = UscVendorManagementPage.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");
        String vendorName = param.get("vendorName");
        String featureNameToView = param.get("featureNameToView");
        int maxPagesToScan = Integer.parseInt(param.getOrDefault("maxPagesToScan", "10"));
        List<String> storeScopes = Arrays.asList("B2C", "B2B Affiliate", "Both stores");
        List<String> forbiddenRowMenuItems = Arrays.asList("Edit vendor", "Activate", "Deactivate");
        List<String> forbiddenMoreActionItems = Arrays.asList("Activate", "Deactivate");

        step("Commercial user logs in to Panel dashboard");
        memberLoginPage.gotoMemberLoginPage();
        memberLoginPage.adminLogin(email, password);
        loginMemberAdmin.gotoHome();

        step("[Vendor list] Open USC Vendor Management page, assert vendor table is populated AND 'Add new vendor' write CTA is hidden");
        loginMemberAdmin.goToAccommodationSupplierDashboard();
        uscVendorManagementPage.goToVendorManagementMenu();
        Assertion.assertThat("USC Vendor Management page should be displayed for Commercial user",
                uscVendorManagementPage.isPageDisplayed(), is(true));
        Assertion.assertThat("Vendor list table should be populated for Commercial user",
                uscVendorManagementPage.isVendorTableDisplayed(), is(true));
        Assertion.assertThat("'Add new vendor' button should NOT be present for Commercial user",
                uscVendorManagementPage.isAddNewVendorButtonAbsent(), is(true));

        step("[Vendor list] Locate target vendor '" + vendorName + "' across pages before opening its actions");
        Assertion.assertThat("Target vendor '" + vendorName + "' should be present in the list within " + maxPagesToScan + " page(s)",
                uscVendorManagementPage.findVendorCodeAcrossPages(vendorName, maxPagesToScan), is(true));

        step("[Row action menu] Open row action menu for '" + vendorName + "' and assert write-only items (Edit vendor / Activate / Deactivate) are NOT present");
        uscVendorManagementPage.clickActionForVendor(vendorName);
        List<String> visibleRowLabels = uscVendorManagementPage.getVisibleDropdownMenuLabels();
        for (String forbidden : forbiddenRowMenuItems) {
            Assertion.assertThat("Row action menu for '" + vendorName + "' should NOT contain '" + forbidden + "' (visible items were: " + visibleRowLabels + ")",
                    uscVendorManagementPage.isActionMenuItemPresent(forbidden), is(false));
        }

        step("[Support Agreements popup] From the still-open row action menu, click 'Support Agreements' and assert the popup shows Penalty / Dispute / Reconciliation sections");
        uscVendorManagementPage.clickActionMenuItem("Support Agreements");
        Assertion.assertThat("Support Agreement popup should be displayed for '" + vendorName + "'",
                uscVendorManagementPage.isSupportAgreementPopupDisplayed(vendorName), is(true));
        Assertion.assertThat("Support Agreement popup should list Penalty / Dispute / Reconciliation Agreement sections",
                uscVendorManagementPage.getSupportAgreementSections(),
                hasItems("Penalty Agreement", "Dispute Agreement", "Reconciliation Agreement"));
        uscVendorManagementPage.closeSupportAgreementPopup();

        step("[Vendor Detail] Open Vendor Detail via 'View vendor' and assert the 'Add configuration' write CTA is hidden");
        uscVendorManagementPage.clickActionForVendor(vendorName);
        uscVendorManagementPage.clickActionMenuItem("View vendor");
        Assertion.assertThat("Vendor Detail page should be displayed for '" + vendorName + "'",
                uscVendorManagementPage.isVendorDetailPageDisplayed(vendorName), is(true));
        Assertion.assertThat("'Add configuration' button should NOT be present on Vendor Detail for Commercial user",
                uscVendorManagementPage.isAddConfigurationButtonAbsent(), is(true));

        step("[Vendor Detail — More action] Open More action menu and assert Activate / Deactivate are NOT present");
        uscVendorManagementPage.clickMoreAction();
        List<String> visibleMoreLabels = uscVendorManagementPage.getVisibleDropdownMenuLabels();
        for (String forbidden : forbiddenMoreActionItems) {
            Assertion.assertThat("More action menu on Vendor Detail should NOT contain '" + forbidden + "' (visible items were: " + visibleMoreLabels + ")",
                    uscVendorManagementPage.isActionMenuItemPresent(forbidden), is(false));
        }
        uscVendorManagementPage.closeAnyOpenDropdown();

        step("[Feature list] Filter by '" + featureNameToView + "' and open its View Configuration page");
        uscVendorManagementPage.filterFeatureList(featureNameToView);
        Assertion.assertThat("Filtered feature list should contain the row '" + featureNameToView + "'",
                uscVendorManagementPage.isFeatureRowPresent(featureNameToView), is(true));
        uscVendorManagementPage.clickViewFeatureButton(featureNameToView);
        Assertion.assertThat("View Configuration page should be displayed for '" + featureNameToView + "'",
                uscVendorManagementPage.isViewConfigurationPageDisplayed(featureNameToView), is(true));

        step("[View Configuration] Per store scope (B2C, B2B Affiliate, Both stores) assert: Save is absent AND JSON editor is non-editable AND JSON value is non-empty");
        for (String storeScope : storeScopes) {
            uscVendorManagementPage.selectStoreScopeTab(storeScope);
            Assertion.assertThat("'Save changes' button should NOT be present on store scope '" + storeScope + "' for Commercial user",
                    uscVendorManagementPage.isEditConfigSaveButtonAbsent(), is(true));
            Assertion.assertThat("JSON editor should be non-editable (contenteditable='false' or read-only wrapper class) on store scope '" + storeScope + "' for Commercial user",
                    uscVendorManagementPage.isJsonEditorReadOnly(), is(true));
            Assertion.assertThat("JSON viewer for store scope '" + storeScope + "' should show a non-empty JSON object for '" + featureNameToView + "'",
                    uscVendorManagementPage.isJsonEditorValueNonEmpty(), is(true));
        }

        step("[Config change history] Navigate back to Vendor Detail, open Config history, assert modal + ≥1 row + NO revert/restore/apply/rollback/undo buttons");
        uscVendorManagementPage.navigateBack();
        Assertion.assertThat("Vendor Detail page should be displayed again after navigating back from View Configuration",
                uscVendorManagementPage.isVendorDetailPageDisplayed(vendorName), is(true));
        uscVendorManagementPage.clickMoreAction();
        uscVendorManagementPage.clickConfigChangeHistoryMenuItem();
        Assertion.assertThat("Config change history modal should be displayed for Commercial user",
                uscVendorManagementPage.isConfigChangeHistoryModalDisplayed(), is(true));
        Assertion.assertThat("Config change history should have at least one row for a vendor with existing configurations",
                uscVendorManagementPage.getConfigChangeHistoryRowCount() >= 1, is(true));
        Assertion.assertThat("Config change history should NOT contain any Revert / Restore / Apply / Rollback / Undo button for Commercial user",
                uscVendorManagementPage.hasAnyRevertOrRestoreButtonInHistory(), is(false));
        uscVendorManagementPage.closeConfigChangeHistoryModal();
    }

    @Author(names = {"Raihan Yasier"})
    @Module(name = "USCVendorManagement")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyFeatureListColumnsAndCrossCheckDescriptionAndHowToUseBetweenAddAndEditFlows() {
        Map<String, String> param = getParam("verifyFeatureListColumnsAndCrossCheckDescriptionAndHowToUseBetweenAddAndEditFlows");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        UscVendorManagementPage<?> uscVendorManagementPage = UscVendorManagementPage.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");
        String vendorName = param.get("vendorName");
        String featureNameToView = param.get("featureNameToView");
        int maxPagesToScan = Integer.parseInt(param.getOrDefault("maxPagesToScan", "10"));
        boolean expectSetupBadge = Boolean.parseBoolean(param.getOrDefault("expectSetupBadge", "false"));
        List<String> expectedFeatureListColumns = Arrays.asList("No.", "Feature Name", "Description", "Last Updated", "Action");

        if (vendorName == null || vendorName.isEmpty() || featureNameToView == null || featureNameToView.isEmpty()) {
            throw new IllegalArgumentException(
                    "vendorName and featureNameToView params are required for this test.");
        }

        step("User Log in to Panel dashboard");
        memberLoginPage.gotoMemberLoginPage();
        memberLoginPage.adminLogin(email, password);
        loginMemberAdmin.gotoHome();

        step("Go to Internal Supplier Dashboard and open USC Vendor Management page");
        loginMemberAdmin.goToAccommodationSupplierDashboard();
        uscVendorManagementPage.goToVendorManagementMenu();
        Assertion.assertThat("USC Vendor Management page should be displayed",
                uscVendorManagementPage.isPageDisplayed(), is(true));

        step("[Vendor Detail] Locate target vendor '" + vendorName + "' and open its Vendor Detail page");
        Assertion.assertThat("Target vendor '" + vendorName + "' should be present in the list within " + maxPagesToScan + " page(s)",
                uscVendorManagementPage.findVendorCodeAcrossPages(vendorName, maxPagesToScan), is(true));
        uscVendorManagementPage.clickActionForVendor(vendorName);
        uscVendorManagementPage.clickActionMenuItem("View vendor");
        Assertion.assertThat("Vendor Detail page should be displayed for '" + vendorName + "'",
                uscVendorManagementPage.isVendorDetailPageDisplayed(vendorName), is(true));

        step("[Feature list] Assert the feature list column headers are exactly: " + expectedFeatureListColumns);
        List<String> actualFeatureListColumns = uscVendorManagementPage.getFeatureListColumnHeaders();
        Assertion.assertThat("Feature list column headers should exactly match the expected order, actual=" + actualFeatureListColumns,
                actualFeatureListColumns, is(expectedFeatureListColumns));

        step("[Feature list] Filter by '" + featureNameToView + "' and capture the Description shown on its row");
        uscVendorManagementPage.filterFeatureList(featureNameToView);
        Assertion.assertThat("Filtered feature list should contain the row '" + featureNameToView + "'",
                uscVendorManagementPage.isFeatureRowPresent(featureNameToView), is(true));
        String listRowDescription = uscVendorManagementPage.getFeatureListRowDescription(featureNameToView);
        Assertion.assertThat("Description column of feature row '" + featureNameToView + "' should not be empty",
                listRowDescription.isEmpty(), is(false));

        step("[Add Configuration] Click 'Add configuration' to open the Add New Configuration page and its feature-list side panel");
        uscVendorManagementPage.clickAddConfigurationButton();
        Assertion.assertThat("Add New Configuration page should be displayed",
                uscVendorManagementPage.isAddNewConfigurationPageDisplayed(), is(true));

        step("[Cross-check A: Description] Assert the feature card for '" + featureNameToView + "' on the Add page shows the SAME Description subtitle as the Vendor Detail row (list value='" + listRowDescription + "')");
        String cardDescription = uscVendorManagementPage.getFeatureCardDescriptionByName(featureNameToView);
        Assertion.assertThat("Feature card '" + featureNameToView + "' on Add page should have a non-empty description subtitle",
                cardDescription.isEmpty(), is(false));
        Assertion.assertThat("Feature card description on Add page should equal the Vendor Detail row Description. cardDescription='" + cardDescription + "', listRowDescription='" + listRowDescription + "'",
                cardDescription, is(listRowDescription));

        step("[Add Configuration] Select the feature card for '" + featureNameToView + "' and capture its 'How to use' text");
        Assertion.assertThat("Feature card '" + featureNameToView + "' should be clickable on the Add page",
                uscVendorManagementPage.clickFeatureCardByName(featureNameToView), is(true));
        String howToUseOnAddPage = uscVendorManagementPage.getHowToUseText();
        Assertion.assertThat("'How to use' text on Add Configuration page for '" + featureNameToView + "' should be non-empty",
                howToUseOnAddPage.isEmpty(), is(false));

        step("[Add Configuration] Assert the JSON Preview (Default Value analogue) for '" + featureNameToView + "' is non-empty");
        Assertion.assertThat("JSON Preview on Add Configuration page for '" + featureNameToView + "' should show a non-empty JSON value",
                uscVendorManagementPage.isJsonPreviewNonEmpty(), is(true));

        step("[Setup indicator — best-effort] Check whether the feature card '" + featureNameToView + "' shows any setup / configured indicator. Hard-assertion only if expectSetupBadge=true (currently=" + expectSetupBadge + ").");
        boolean setupIndicator = uscVendorManagementPage.hasSetupIndicatorForFeature(featureNameToView);
        if (expectSetupBadge) {
            Assertion.assertThat("Feature card '" + featureNameToView + "' should carry a setup / configured indicator (expectSetupBadge=true)",
                    setupIndicator, is(true));
        }

        step("[Navigation] Cancel Add Configuration and return to Vendor Detail before opening the Edit Configuration page");
        uscVendorManagementPage.cancelAddConfiguration();
        Assertion.assertThat("Vendor Detail page should be displayed again after cancelling Add Configuration",
                uscVendorManagementPage.isVendorDetailPageDisplayed(vendorName), is(true));

        step("[Edit Configuration] Filter again by '" + featureNameToView + "' and click Edit to open the Edit Configuration page");
        uscVendorManagementPage.filterFeatureList(featureNameToView);
        Assertion.assertThat("Filtered feature list should contain the row '" + featureNameToView + "' again after returning to Vendor Detail",
                uscVendorManagementPage.isFeatureRowPresent(featureNameToView), is(true));
        uscVendorManagementPage.clickEditFeatureButton(featureNameToView);
        Assertion.assertThat("Edit Configuration page should be displayed for '" + featureNameToView + "'",
                uscVendorManagementPage.isEditConfigurationPageDisplayed(featureNameToView), is(true));

        step("[Cross-check B: How to use] Assert the 'How to use' text on the Edit Configuration page equals the text captured on the Add Configuration page");
        String howToUseOnEditPage = uscVendorManagementPage.getHowToUseText();
        Assertion.assertThat("'How to use' text on Edit Configuration page for '" + featureNameToView + "' should be non-empty",
                howToUseOnEditPage.isEmpty(), is(false));
        Assertion.assertThat("'How to use' text should be identical between Add and Edit pages for '" + featureNameToView + "'. addPage='" + howToUseOnAddPage + "', editPage='" + howToUseOnEditPage + "'",
                howToUseOnEditPage, is(howToUseOnAddPage));
    }

    @Author(names = {"Raihan Yasier"})
    @Module(name = "USCVendorManagement")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyConfigEditorValidationGatingAndPostSavePersistence() {
        Map<String, String> param = getParam("verifyConfigEditorValidationGatingAndPostSavePersistence");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        UscVendorManagementPage<?> uscVendorManagementPage = UscVendorManagementPage.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");
        String vendorName = param.get("vendorName");
        String featureNameToView = param.get("featureNameToView");
        int maxPagesToScan = Integer.parseInt(param.getOrDefault("maxPagesToScan", "10"));

        if (vendorName == null || vendorName.isEmpty() || featureNameToView == null || featureNameToView.isEmpty()) {
            throw new IllegalArgumentException(
                    "vendorName and featureNameToView params are required for this test.");
        }

        step("User Log in to Panel dashboard");
        memberLoginPage.gotoMemberLoginPage();
        memberLoginPage.adminLogin(email, password);
        loginMemberAdmin.gotoHome();

        step("Go to Internal Supplier Dashboard and open USC Vendor Management page");
        loginMemberAdmin.goToAccommodationSupplierDashboard();
        uscVendorManagementPage.goToVendorManagementMenu();
        Assertion.assertThat("USC Vendor Management page should be displayed",
                uscVendorManagementPage.isPageDisplayed(), is(true));

        step("[Vendor Detail] Locate target vendor '" + vendorName + "' and open its Vendor Detail page");
        Assertion.assertThat("Target vendor '" + vendorName + "' should be present in the list within " + maxPagesToScan + " page(s)",
                uscVendorManagementPage.findVendorCodeAcrossPages(vendorName, maxPagesToScan), is(true));
        uscVendorManagementPage.clickActionForVendor(vendorName);
        uscVendorManagementPage.clickActionMenuItem("View vendor");
        Assertion.assertThat("Vendor Detail page should be displayed for '" + vendorName + "'",
                uscVendorManagementPage.isVendorDetailPageDisplayed(vendorName), is(true));

        step("[Edit Configuration] Filter by '" + featureNameToView + "' and click Edit to open the Edit Configuration page");
        uscVendorManagementPage.filterFeatureList(featureNameToView);
        Assertion.assertThat("Filtered feature list should contain the row '" + featureNameToView + "'",
                uscVendorManagementPage.isFeatureRowPresent(featureNameToView), is(true));
        uscVendorManagementPage.clickEditFeatureButton(featureNameToView);
        Assertion.assertThat("Edit Configuration page should be displayed for '" + featureNameToView + "'",
                uscVendorManagementPage.isEditConfigurationPageDisplayed(featureNameToView), is(true));

        step("[Baseline — unchanged JSON] Capture current valid JSON. Save must be DISABLED (no diff to save) while validation status reads OK");
        String originalJson = uscVendorManagementPage.getJsonEditorValue();
        Assertion.assertThat("Baseline JSON captured from the editor should be non-empty",
                originalJson != null && !originalJson.trim().isEmpty(), is(true));
        Assertion.assertThat("Baseline (unchanged): 'Save changes' button should be DISABLED because the editor content is identical to the persisted value",
                uscVendorManagementPage.isEditConfigSaveButtonEnabled(), is(false));
        String baselineStatus = uscVendorManagementPage.getJsonValidationStatusText();
        Assertion.assertThat("Baseline: JSON validation status should indicate a valid state (contain 'No errors' or 'valid'/'validates'). actual='" + baselineStatus + "'",
                baselineStatus.toLowerCase().contains("no errors") || baselineStatus.toLowerCase().contains("valid"), is(true));

        step("[Valid dirty change] Apply a small valid modification (change the first JSON string value) and assert Save becomes ENABLED and validation status remains OK");
        String validChangedJson = UscVendorManagementPage.mutateFirstJsonStringValue(originalJson, "AUTO_CFG_VALID");
        if (validChangedJson.equals(originalJson)) {
            validChangedJson = originalJson.replaceFirst("\\}\\s*$", ",\n  \"__autoValidProbe\": \"AUTO_CFG_VALID\"\n}");
        }
        Assertion.assertThat("Valid-changed JSON must differ from the baseline JSON. baseline='" + originalJson + "', changed='" + validChangedJson + "'",
                !validChangedJson.equals(originalJson), is(true));
        uscVendorManagementPage.setJsonEditorValue(validChangedJson);
        Assertion.assertThat("Valid dirty change: 'Save changes' button should be ENABLED (content is dirty and still valid)",
                uscVendorManagementPage.isEditConfigSaveButtonEnabled(), is(true));
        String validChangeStatus = uscVendorManagementPage.getJsonValidationStatusText();
        Assertion.assertThat("Valid dirty change: JSON validation status should remain in the valid state ('No errors' / 'valid'). actual='" + validChangeStatus + "'",
                validChangeStatus.toLowerCase().contains("no errors") || validChangeStatus.toLowerCase().contains("valid"), is(true));

        step("[Syntax error] Break the valid-changed JSON syntactically and assert Save is DISABLED and validation status changes away from the OK state");
        String syntaxBrokenJson = UscVendorManagementPage.mutateJsonToInvalidSyntax(validChangedJson);
        Assertion.assertThat("Syntax-broken JSON must differ from the valid-changed JSON. valid='" + validChangedJson + "', broken='" + syntaxBrokenJson + "'",
                !syntaxBrokenJson.equals(validChangedJson), is(true));
        uscVendorManagementPage.setJsonEditorValue(syntaxBrokenJson);
        Assertion.assertThat("Syntax error: 'Save changes' button should be DISABLED after entering a syntactically invalid JSON",
                uscVendorManagementPage.isEditConfigSaveButtonEnabled(), is(false));
        String syntaxStatus = uscVendorManagementPage.getJsonValidationStatusText();
        Assertion.assertThat("Syntax error: JSON validation status should change away from the OK state (should NOT contain 'No errors'). actual='" + syntaxStatus + "', baseline='" + baselineStatus + "'",
                syntaxStatus.equalsIgnoreCase(baselineStatus), is(false));

        step("[Restore #1] Restore the valid-changed JSON and assert Save becomes ENABLED again");
        uscVendorManagementPage.setJsonEditorValue(validChangedJson);
        Assertion.assertThat("After restoring the valid-changed JSON, 'Save changes' should be ENABLED again (content is dirty and valid)",
                uscVendorManagementPage.isEditConfigSaveButtonEnabled(), is(true));

        step("[Schema mismatch] Replace JSON with a structurally-valid but schema-violating payload (flip a boolean/number field to a quoted string) and assert Save is DISABLED and validation status changes away from the OK state");
        String schemaBrokenJson = UscVendorManagementPage.mutateJsonToInvalidSchema(validChangedJson);
        Assertion.assertThat("Schema-mutated JSON must differ from the valid-changed JSON. valid='" + validChangedJson + "', mutated='" + schemaBrokenJson + "'. If they are equal, the baseline JSON had no non-string primitive that could be flipped for a schema-violation test.",
                !schemaBrokenJson.equals(validChangedJson), is(true));
        uscVendorManagementPage.setJsonEditorValue(schemaBrokenJson);
        Assertion.assertThat("Schema violation: 'Save changes' button should be DISABLED after entering a JSON that violates the schema",
                uscVendorManagementPage.isEditConfigSaveButtonEnabled(), is(false));
        String schemaStatus = uscVendorManagementPage.getJsonValidationStatusText();
        Assertion.assertThat("Schema violation: JSON validation status should change away from the OK state. actual='" + schemaStatus + "', baseline='" + baselineStatus + "'",
                schemaStatus.equalsIgnoreCase(baselineStatus), is(false));

        step("[Restore #2] Restore to the original JSON and assert Save reverts to DISABLED (unchanged) — editor is back to a clean baseline before the persistence phase");
        uscVendorManagementPage.setJsonEditorValue(originalJson);
        Assertion.assertThat("After restoring the original JSON, 'Save changes' should be DISABLED again (content matches persisted value, no diff)",
                uscVendorManagementPage.isEditConfigSaveButtonEnabled(), is(false));

        step("[Persistence — save a fresh valid change] Apply a unique valid modification (mutate the first JSON string value to a run-scoped probe) and confirm Save becomes ENABLED for the commit");
        String expectedConfigUpdatedSnackbar = param.getOrDefault("expectedConfigUpdatedSnackbar", "Configuration updated");
        String persistProbe = "AUTO_CFG_PERSIST_" + System.currentTimeMillis();
        String persistJson = UscVendorManagementPage.mutateFirstJsonStringValue(originalJson, persistProbe);
        if (persistJson.equals(originalJson)) {
            persistJson = originalJson.replaceFirst("\\}\\s*$", ",\n  \"__autoPersistProbe\": \"" + persistProbe + "\"\n}");
        }
        Assertion.assertThat("Persistence: probe JSON must differ from the original JSON. original='" + originalJson + "', probe='" + persistJson + "'",
                !persistJson.equals(originalJson), is(true));
        uscVendorManagementPage.setJsonEditorValue(persistJson);
        Assertion.assertThat("Persistence: 'Save changes' must be ENABLED after applying the fresh valid change",
                uscVendorManagementPage.isEditConfigSaveButtonEnabled(), is(true));

        step("[Persistence — commit] Click Save changes, confirm the Value Different modal shows the correct Old vs New payloads, then confirm the commit and assert the success snackbar");
        uscVendorManagementPage.clickSaveChangesOnEditConfiguration();
        Assertion.assertThat("Persistence: 'Value different' modal should be displayed after clicking Save changes",
                uscVendorManagementPage.isValueDifferentModalDisplayed(), is(true));
        Assertion.assertThat("Persistence: 'Value different' modal Old panel should mirror the pre-commit JSON",
                UscVendorManagementPage.normalizeJson(uscVendorManagementPage.getValueDifferentOldValue()),
                is(UscVendorManagementPage.normalizeJson(originalJson)));
        Assertion.assertThat("Persistence: 'Value different' modal New panel should mirror the JSON we are about to persist",
                UscVendorManagementPage.normalizeJson(uscVendorManagementPage.getValueDifferentNewValue()),
                is(UscVendorManagementPage.normalizeJson(persistJson)));
        uscVendorManagementPage.clickConfirmChanges();
        Assertion.assertThat("Persistence: snackbar must confirm the configuration was updated successfully (expected: '" + expectedConfigUpdatedSnackbar + "')",
                uscVendorManagementPage.verifySnackbarMessage(expectedConfigUpdatedSnackbar), is(true));

        step("[Persistence — reopen] Navigate back to the feature list on Vendor Detail, filter by the target feature, and re-open the Edit Configuration page (forces a fresh load of the persisted value from the server)");
        Assertion.assertThat("After committing, the user should be routed back to the Vendor Detail page for '" + vendorName + "'",
                uscVendorManagementPage.isVendorDetailPageDisplayed(vendorName), is(true));
        uscVendorManagementPage.filterFeatureList(featureNameToView);
        Assertion.assertThat("After save, the filtered feature list should still contain the row '" + featureNameToView + "'",
                uscVendorManagementPage.isFeatureRowPresent(featureNameToView), is(true));
        uscVendorManagementPage.clickEditFeatureButton(featureNameToView);
        Assertion.assertThat("Edit Configuration page should be displayed again for '" + featureNameToView + "' after reopening",
                uscVendorManagementPage.isEditConfigurationPageDisplayed(featureNameToView), is(true));

        step("[Persistence — assert] Read the JSON editor value from the freshly-loaded Edit Configuration page and assert semantic equality with the payload we just saved (whitespace/indentation-insensitive), and assert Save is DISABLED because the reloaded content is unchanged vs. the persisted value");
        String reloadedJson = uscVendorManagementPage.getJsonEditorValue();
        Assertion.assertThat("Persistence: reloaded JSON must be non-empty after reopening Edit Configuration",
                reloadedJson != null && !reloadedJson.trim().isEmpty(), is(true));
        Assertion.assertThat("Persistence: reloaded JSON must equal the payload we saved (semantic equality). saved='" + persistJson + "', reloaded='" + reloadedJson + "'",
                UscVendorManagementPage.normalizeJson(reloadedJson),
                is(UscVendorManagementPage.normalizeJson(persistJson)));
        Assertion.assertThat("Persistence: on reopen with no edits, 'Save changes' should be DISABLED (reloaded content is identical to the persisted value)",
                uscVendorManagementPage.isEditConfigSaveButtonEnabled(), is(false));
    }

    @Author(names = {"Raihan Yasier"})
    @Module(name = "USCVendorManagement")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyConfigHistoryVersionSequencePerFeatureAndUnifiedVendorHistoryAcrossFeatures() {
        Map<String, String> param = getParam("verifyConfigHistoryVersionSequencePerFeatureAndUnifiedVendorHistoryAcrossFeatures");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        UscVendorManagementPage<?> uscVendorManagementPage = UscVendorManagementPage.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");
        String penaltyText = param.get("penaltyText");
        String disputeText = param.get("disputeText");
        String reconciliationText = param.get("reconciliationText");
        String expectedAddSuccessMessage = param.get("expectedAddSuccessMessage");
        String expectedConfigUpdatedSnackbar = param.getOrDefault("expectedConfigUpdatedSnackbar", "Configuration updated");
        int maxPagesToScan = Integer.parseInt(param.getOrDefault("maxPagesToScan", "10"));

        step("User Log in to Panel dashboard");
        memberLoginPage.gotoMemberLoginPage();
        memberLoginPage.adminLogin(email, password);
        loginMemberAdmin.gotoHome();

        step("Go to Internal Supplier Dashboard and open USC Vendor Management page");
        loginMemberAdmin.goToAccommodationSupplierDashboard();
        uscVendorManagementPage.goToVendorManagementMenu();
        Assertion.assertThat("USC Vendor Management page should be displayed",
                uscVendorManagementPage.isPageDisplayed(), is(true));

        step("[Vendor Management] Add a brand-new vendor so this test starts from a clean, fully-isolated Vendor Detail (no pre-existing configs or history)");
        String newVendorCode = uscVendorManagementPage.generateVendorCode();
        boolean testPassed = false;
        try {
            uscVendorManagementPage.clickAddNewVendorButton();
            Assertion.assertThat("Add New Vendor drawer should be displayed",
                    uscVendorManagementPage.isAddVendorDrawerDisplayed(), is(true));
            uscVendorManagementPage.fillVendorName(newVendorCode);
            uscVendorManagementPage.fillAllAgreements(penaltyText, disputeText, reconciliationText);
            uscVendorManagementPage.clickAddVendorSubmit();
            Assertion.assertThat("Snackbar should confirm success adding vendor " + newVendorCode,
                    uscVendorManagementPage.verifySnackbarMessage(expectedAddSuccessMessage), is(true));

            step("[Vendor Detail] Open the Vendor Detail page for the freshly-created vendor via 'View vendor' and assert the feature list starts empty");
            Assertion.assertThat("Newly added vendor " + newVendorCode + " should be present in the list within " + maxPagesToScan + " page(s)",
                    uscVendorManagementPage.findVendorCodeAcrossPages(newVendorCode, maxPagesToScan), is(true));
            uscVendorManagementPage.clickActionForVendor(newVendorCode);
            uscVendorManagementPage.clickActionMenuItem("View vendor");
            Assertion.assertThat("Vendor Detail page should be displayed for " + newVendorCode,
                    uscVendorManagementPage.isVendorDetailPageDisplayed(newVendorCode), is(true));
            Assertion.assertThat("Feature list should show the empty state on a brand-new vendor",
                    uscVendorManagementPage.isFeatureListEmpty(), is(true));

            step("[Feature X — add + first user edit] Add Configuration \u2192 pick the first usable feature (this becomes 'feature X') \u2192 Save \u2192 'Yes, edit now' \u2192 mutate JSON to a first-edit probe \u2192 Save changes \u2192 confirm Value Different \u2192 snackbar");
            uscVendorManagementPage.clickAddConfigurationButton();
            Assertion.assertThat("Add New Configuration page should be displayed for feature X",
                    uscVendorManagementPage.isAddNewConfigurationPageDisplayed(), is(true));
            String featureX = uscVendorManagementPage.selectFirstUsableFeatureForEditing();
            Assertion.assertThat("A non-empty feature name should be captured for feature X",
                    featureX != null && !featureX.isEmpty(), is(true));
            String basisJsonX = uscVendorManagementPage.getJsonPreviewValue();
            Assertion.assertThat("JSON Preview should return a usable basis JSON for feature X '" + featureX + "'",
                    UscVendorManagementPage.isUsableBasisJson(basisJsonX), is(true));
            uscVendorManagementPage.submitAddConfiguration();
            Assertion.assertThat("'Configuration added successfully!' modal should be displayed after adding feature X",
                    uscVendorManagementPage.isConfigurationAddedModalDisplayed(), is(true));
            uscVendorManagementPage.clickYesEditNowInAddedModal();
            Assertion.assertThat("Edit Configuration page should be displayed for feature X '" + featureX + "' after 'Yes, edit now'",
                    uscVendorManagementPage.isEditConfigurationPageDisplayed(featureX), is(true));
            String editX_first_source = uscVendorManagementPage.getJsonEditorValue();
            String editX_first_json = UscVendorManagementPage.mutateFirstJsonStringValue(
                    (editX_first_source != null && editX_first_source.trim().startsWith("{")) ? editX_first_source : basisJsonX,
                    "AUTO_X_EDIT_1");
            uscVendorManagementPage.setJsonEditorValue(editX_first_json);
            uscVendorManagementPage.clickSaveChangesOnEditConfiguration();
            Assertion.assertThat("Feature X first edit: 'Value different' modal should be displayed after Save",
                    uscVendorManagementPage.isValueDifferentModalDisplayed(), is(true));
            uscVendorManagementPage.clickConfirmChanges();
            Assertion.assertThat("Feature X first edit: snackbar should confirm configuration updated (expected='" + expectedConfigUpdatedSnackbar + "')",
                    uscVendorManagementPage.verifySnackbarMessage(expectedConfigUpdatedSnackbar), is(true));

            step("[Feature X — history after first user edit] Filter feature list by '" + featureX + "', open Config history, and assert exactly TWO rows exist (the auto-created initial entry PLUS the just-committed first user edit), both carrying '" + featureX + "'");
            Assertion.assertThat("After saving feature X's first user edit, the user should be routed back to the Vendor Detail page for " + newVendorCode,
                    uscVendorManagementPage.isVendorDetailPageDisplayed(newVendorCode), is(true));
            uscVendorManagementPage.filterFeatureList(featureX);
            Assertion.assertThat("Filtered feature list should contain feature X '" + featureX + "' after first user edit",
                    uscVendorManagementPage.isFeatureRowPresent(featureX), is(true));
            uscVendorManagementPage.clickMoreAction();
            uscVendorManagementPage.clickConfigChangeHistoryMenuItem();
            Assertion.assertThat("Config history modal should be displayed for feature X after first user edit",
                    uscVendorManagementPage.isConfigChangeHistoryModalDisplayed(), is(true));
            int xHistoryAfterFirstEdit = uscVendorManagementPage.getConfigChangeHistoryRowCount();
            Assertion.assertThat("Feature X history after the FIRST user edit should have exactly 2 rows (auto-created initial entry + user's first edit), got " + xHistoryAfterFirstEdit,
                    xHistoryAfterFirstEdit, is(2));
            for (int r = 1; r <= xHistoryAfterFirstEdit; r++) {
                String rowFeatureName = uscVendorManagementPage.getHistoryFeatureName(r);
                Assertion.assertThat("Feature X history row " + r + " Feature Name should start with '" + featureX + "'. actual='" + rowFeatureName + "'",
                        rowFeatureName != null && rowFeatureName.startsWith(featureX), is(true));
            }
            uscVendorManagementPage.closeConfigChangeHistoryModal();

            step("[Feature X — second user edit] Click Edit on the feature X row, mutate the JSON to a second-edit probe, Save changes, confirm Value Different, snackbar");
            uscVendorManagementPage.clickEditFeatureButton(featureX);
            Assertion.assertThat("Edit Configuration page should be displayed for feature X '" + featureX + "' for the second user edit",
                    uscVendorManagementPage.isEditConfigurationPageDisplayed(featureX), is(true));
            String editX_second_source = uscVendorManagementPage.getJsonEditorValue();
            String editX_second_json = UscVendorManagementPage.mutateFirstJsonStringValue(
                    (editX_second_source != null && editX_second_source.trim().startsWith("{")) ? editX_second_source : editX_first_json,
                    "AUTO_X_EDIT_2");
            Assertion.assertThat("Feature X second edit JSON must differ from the first edit JSON. first='" + editX_first_json + "', second='" + editX_second_json + "'",
                    !editX_second_json.equals(editX_first_json), is(true));
            uscVendorManagementPage.setJsonEditorValue(editX_second_json);
            uscVendorManagementPage.clickSaveChangesOnEditConfiguration();
            Assertion.assertThat("Feature X second edit: 'Value different' modal should be displayed after Save",
                    uscVendorManagementPage.isValueDifferentModalDisplayed(), is(true));
            uscVendorManagementPage.clickConfirmChanges();
            Assertion.assertThat("Feature X second edit: snackbar should confirm configuration updated (expected='" + expectedConfigUpdatedSnackbar + "')",
                    uscVendorManagementPage.verifySnackbarMessage(expectedConfigUpdatedSnackbar), is(true));

            step("[Feature X — sequential version check] Reopen Config history for feature X and assert exactly THREE rows (initial + two user edits), all carrying feature X. The version numbers on the two USER-EDIT rows (row 1 latest and row 2 previous) must both be extractable as 'v(N)' and differ by exactly 1, proving strictly sequential increment per edit");
            uscVendorManagementPage.filterFeatureList(featureX);
            Assertion.assertThat("Filtered feature list should still contain feature X '" + featureX + "' after second user edit",
                    uscVendorManagementPage.isFeatureRowPresent(featureX), is(true));
            uscVendorManagementPage.clickMoreAction();
            uscVendorManagementPage.clickConfigChangeHistoryMenuItem();
            Assertion.assertThat("Config history modal should be displayed for feature X after second user edit",
                    uscVendorManagementPage.isConfigChangeHistoryModalDisplayed(), is(true));
            int xHistoryAfterSecondEdit = uscVendorManagementPage.getConfigChangeHistoryRowCount();
            Assertion.assertThat("Feature X history after the SECOND user edit should have exactly 3 rows (initial + first edit + second edit), got " + xHistoryAfterSecondEdit,
                    xHistoryAfterSecondEdit, is(3));
            for (int r = 1; r <= xHistoryAfterSecondEdit; r++) {
                String rowFeatureName = uscVendorManagementPage.getHistoryFeatureName(r);
                Assertion.assertThat("Feature X history row " + r + " Feature Name should start with '" + featureX + "'. actual='" + rowFeatureName + "'",
                        rowFeatureName != null && rowFeatureName.startsWith(featureX), is(true));
            }
            String xRow1AfterSecondEdit = uscVendorManagementPage.getHistoryFeatureName(1);
            String xRow2AfterSecondEdit = uscVendorManagementPage.getHistoryFeatureName(2);
            int xRow1Version = UscVendorManagementPage.extractVersionFromHistoryFeatureName(xRow1AfterSecondEdit);
            int xRow2Version = UscVendorManagementPage.extractVersionFromHistoryFeatureName(xRow2AfterSecondEdit);
            Assertion.assertThat("Feature X history row 1 (latest user edit) should end with a 'v(N)' version marker. actual='" + xRow1AfterSecondEdit + "'",
                    xRow1Version > 0, is(true));
            Assertion.assertThat("Feature X history row 2 (previous user edit) should end with a 'v(N)' version marker. actual='" + xRow2AfterSecondEdit + "'",
                    xRow2Version > 0, is(true));
            Assertion.assertThat("Feature X user-edit versions must be strictly sequential: row 1 (" + xRow1Version + ") should be exactly one greater than row 2 (" + xRow2Version + ")",
                    xRow1Version - xRow2Version, is(1));
            uscVendorManagementPage.closeConfigChangeHistoryModal();

            step("[Feature Y — add + first user edit] Add a SECOND configuration on the same vendor: click Add Configuration, select the first usable feature that is NOT feature X (this becomes 'feature Y'), Save \u2192 'Yes, edit now' \u2192 mutate JSON to a first-edit probe \u2192 Save changes \u2192 confirm \u2192 snackbar");
            uscVendorManagementPage.clickAddConfigurationButton();
            Assertion.assertThat("Add New Configuration page should be displayed for feature Y",
                    uscVendorManagementPage.isAddNewConfigurationPageDisplayed(), is(true));
            String featureY = uscVendorManagementPage.selectFirstUsableFeatureForEditingExcluding(
                    new java.util.HashSet<>(java.util.Collections.singletonList(featureX)));
            Assertion.assertThat("A non-empty feature name should be captured for feature Y",
                    featureY != null && !featureY.isEmpty(), is(true));
            Assertion.assertThat("Feature Y '" + featureY + "' must be different from feature X '" + featureX + "'",
                    !featureY.equals(featureX), is(true));
            String basisJsonY = uscVendorManagementPage.getJsonPreviewValue();
            Assertion.assertThat("JSON Preview should return a usable basis JSON for feature Y '" + featureY + "'",
                    UscVendorManagementPage.isUsableBasisJson(basisJsonY), is(true));
            uscVendorManagementPage.submitAddConfiguration();
            Assertion.assertThat("'Configuration added successfully!' modal should be displayed after adding feature Y",
                    uscVendorManagementPage.isConfigurationAddedModalDisplayed(), is(true));
            uscVendorManagementPage.clickYesEditNowInAddedModal();
            Assertion.assertThat("Edit Configuration page should be displayed for feature Y '" + featureY + "' after 'Yes, edit now'",
                    uscVendorManagementPage.isEditConfigurationPageDisplayed(featureY), is(true));
            String editY_first_source = uscVendorManagementPage.getJsonEditorValue();
            String editY_first_json = UscVendorManagementPage.mutateFirstJsonStringValue(
                    (editY_first_source != null && editY_first_source.trim().startsWith("{")) ? editY_first_source : basisJsonY,
                    "AUTO_Y_EDIT_1");
            uscVendorManagementPage.setJsonEditorValue(editY_first_json);
            uscVendorManagementPage.clickSaveChangesOnEditConfiguration();
            Assertion.assertThat("Feature Y first edit: 'Value different' modal should be displayed after Save",
                    uscVendorManagementPage.isValueDifferentModalDisplayed(), is(true));
            uscVendorManagementPage.clickConfirmChanges();
            Assertion.assertThat("Feature Y first edit: snackbar should confirm configuration updated (expected='" + expectedConfigUpdatedSnackbar + "')",
                    uscVendorManagementPage.verifySnackbarMessage(expectedConfigUpdatedSnackbar), is(true));

            step("[Unified vendor history — opened via feature Y's row] Filter by feature Y, open Config history via feature Y's 'More action', and assert the modal shows the FULL vendor-wide history: exactly 5 rows total (feature X's 3 rows + feature Y's 2 rows), with 3 rows attributed to feature X and 2 rows attributed to feature Y, no row conflating both feature names, AND feature X's two user-edit version markers still differ by exactly 1 (per-feature version sequencing is preserved inside the unified list)");
            uscVendorManagementPage.filterFeatureList(featureY);
            Assertion.assertThat("Filtered feature list should contain feature Y '" + featureY + "' after Y first user edit",
                    uscVendorManagementPage.isFeatureRowPresent(featureY), is(true));
            uscVendorManagementPage.clickMoreAction();
            uscVendorManagementPage.clickConfigChangeHistoryMenuItem();
            Assertion.assertThat("Config history modal should be displayed when opened via feature Y's row",
                    uscVendorManagementPage.isConfigChangeHistoryModalDisplayed(), is(true));
            int unifiedHistoryRowCountViaY = uscVendorManagementPage.getConfigChangeHistoryRowCount();
            Assertion.assertThat("Unified vendor history after all edits should have exactly 5 rows (feature X: initial + 2 user edits = 3; feature Y: initial + 1 user edit = 2), got " + unifiedHistoryRowCountViaY,
                    unifiedHistoryRowCountViaY, is(5));
            int xRowCountViaY = 0;
            int yRowCountViaY = 0;
            java.util.List<Integer> xVersionsViaY = new java.util.ArrayList<>();
            java.util.List<String> allFeatureNamesViaY = new java.util.ArrayList<>();
            for (int r = 1; r <= unifiedHistoryRowCountViaY; r++) {
                String rowFeatureName = uscVendorManagementPage.getHistoryFeatureName(r);
                allFeatureNamesViaY.add("row" + r + "='" + rowFeatureName + "'");
                boolean matchesX = rowFeatureName != null && rowFeatureName.startsWith(featureX);
                boolean matchesY = rowFeatureName != null && rowFeatureName.startsWith(featureY);
                Assertion.assertThat("Unified history (via Y) row " + r + " Feature Name must start with EITHER feature X '" + featureX + "' OR feature Y '" + featureY + "', not something else or both. actual='" + rowFeatureName + "'",
                        matchesX ^ matchesY, is(true));
                if (matchesX) {
                    xRowCountViaY++;
                    int ver = UscVendorManagementPage.extractVersionFromHistoryFeatureName(rowFeatureName);
                    if (ver > 0) {
                        xVersionsViaY.add(ver);
                    }
                }
                if (matchesY) {
                    yRowCountViaY++;
                }
            }
            Assertion.assertThat("Unified history (via Y): exactly 3 rows must be attributed to feature X '" + featureX + "'. actualCount=" + xRowCountViaY + " rows=" + allFeatureNamesViaY,
                    xRowCountViaY, is(3));
            Assertion.assertThat("Unified history (via Y): exactly 2 rows must be attributed to feature Y '" + featureY + "'. actualCount=" + yRowCountViaY + " rows=" + allFeatureNamesViaY,
                    yRowCountViaY, is(2));
            java.util.Collections.sort(xVersionsViaY, java.util.Collections.reverseOrder());
            Assertion.assertThat("Unified history (via Y): at least 2 rows attributed to feature X must carry an extractable 'v(N)' version marker (the two user-edit rows). extractedVersions(desc)=" + xVersionsViaY,
                    xVersionsViaY.size() >= 2, is(true));
            Assertion.assertThat("Unified history (via Y): feature X's two highest user-edit version markers must be strictly sequential (top1 - top2 == 1). extractedVersions(desc)=" + xVersionsViaY,
                    xVersionsViaY.get(0) - xVersionsViaY.get(1), is(1));
            uscVendorManagementPage.closeConfigChangeHistoryModal();

            step("[List filter is independent of history modal content] Reopen Config history via feature X's 'More action' and assert IDENTICAL content: the vendor-wide history is 5 rows (3 for X + 2 for Y) regardless of which feature the user filtered on the list \u2014 the feature-list filter must not scope the Config history modal");
            uscVendorManagementPage.filterFeatureList(featureX);
            Assertion.assertThat("Filtered feature list should contain feature X '" + featureX + "' before reopening history via X",
                    uscVendorManagementPage.isFeatureRowPresent(featureX), is(true));
            uscVendorManagementPage.clickMoreAction();
            uscVendorManagementPage.clickConfigChangeHistoryMenuItem();
            Assertion.assertThat("Config history modal should be displayed when reopened via feature X's row",
                    uscVendorManagementPage.isConfigChangeHistoryModalDisplayed(), is(true));
            int unifiedHistoryRowCountViaX = uscVendorManagementPage.getConfigChangeHistoryRowCount();
            Assertion.assertThat("Unified vendor history opened via feature X's row must have the SAME row count as when opened via feature Y's row (proving the feature-list filter does not scope the history modal). viaY=" + unifiedHistoryRowCountViaY + " viaX=" + unifiedHistoryRowCountViaX,
                    unifiedHistoryRowCountViaX, is(unifiedHistoryRowCountViaY));
            int xRowCountViaX = 0;
            int yRowCountViaX = 0;
            java.util.List<String> allFeatureNamesViaX = new java.util.ArrayList<>();
            for (int r = 1; r <= unifiedHistoryRowCountViaX; r++) {
                String rowFeatureName = uscVendorManagementPage.getHistoryFeatureName(r);
                allFeatureNamesViaX.add("row" + r + "='" + rowFeatureName + "'");
                if (rowFeatureName != null && rowFeatureName.startsWith(featureX)) {
                    xRowCountViaX++;
                }
                if (rowFeatureName != null && rowFeatureName.startsWith(featureY)) {
                    yRowCountViaX++;
                }
            }
            Assertion.assertThat("Unified history (via X): feature X row count must match the count observed via Y (" + xRowCountViaY + "). actual=" + xRowCountViaX + " rows=" + allFeatureNamesViaX,
                    xRowCountViaX, is(xRowCountViaY));
            Assertion.assertThat("Unified history (via X): feature Y row count must match the count observed via Y (" + yRowCountViaY + "). actual=" + yRowCountViaX + " rows=" + allFeatureNamesViaX,
                    yRowCountViaX, is(yRowCountViaY));
            uscVendorManagementPage.closeConfigChangeHistoryModal();
            testPassed = true;
        } finally {
            uscVendorManagementPage.cleanupVendorViaApi(newVendorCode, email, testPassed);
        }
    }
}
