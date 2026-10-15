package com.tiket.web.dashboard.page.accommodation.internal_supplier_dashboard;

import com.tiket.api.accommodation.hotel.supply.DeleteUscConfigValueApi;
import com.tiket.api.accommodation.hotel.supply.DeleteUscVendorApi;
import com.tiket.api.accommodation.hotel.supply.GetUscConfigValuesApi;
import com.tiket.api.accommodation.hotel.supply.GetUscVendorApi;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import io.restassured.response.Response;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URI;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.core.Is.is;

public class UscVendorManagementPage<T extends WebDriver> extends WebPage<T> {
    public UscVendorManagementPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> UscVendorManagementPage<?> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new UscVendorManagementPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new UscVendorManagementPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    private static final String VENDOR_MANAGEMENT_PATH = "/admin/accommodation/supplier-dashboard/vendor-management";
    private static final Locator VENDOR_MANAGEMENT_MENU = new Locator("VENDOR_MANAGEMENT_MENU", By.xpath("//a[@href='" + VENDOR_MANAGEMENT_PATH + "']//span[@data-testid='nav-label' and normalize-space()='Vendor Management']"));
    private static final List<String> CLEANUP_STORE_IDS = List.of("TIKETCOM", "TIKETB2B");
    private static final Locator ANY_PAGE_TITLE = new Locator("ANY_PAGE_TITLE", By.id("title-page-id"));
    private static final Locator PAGE_TITLE = new Locator("PAGE_TITLE", By.xpath("//span[@id='title-page-id' and normalize-space()='Vendor Management']"));
    private static final Locator ADD_NEW_VENDOR_BUTTON = new Locator("ADD_NEW_VENDOR_BUTTON", By.xpath("//button[@data-testid='test-id-fuc-button' and .//span[normalize-space()='Add new vendor']]"));
    private static final Locator NEXT_PAGE_BUTTON = new Locator("NEXT_PAGE_BUTTON", By.xpath("//div[@data-testid='test-id-um-pagination']//button[@aria-label='Next']"));
    private static final Locator TABLE_ROWS = new Locator("TABLE_ROWS", By.xpath("//table//tbody/tr"));
    private static final Locator SNACKBAR_MESSAGE = new Locator("SNACKBAR_MESSAGE", By.xpath("//div[@id='snackbar-portal']//div[contains(@class,'Snackbar_text_content')]"));

    private static final Locator ADD_VENDOR_DRAWER_TITLE = new Locator("ADD_VENDOR_DRAWER_TITLE", By.xpath("//*[normalize-space()='Add New Vendor']"));
    private static final Locator RIGHT_SIDEBAR_DRAWER = new Locator("RIGHT_SIDEBAR_DRAWER", By.xpath("//aside[@data-testid='right-sidebar' and @role='dialog']"));
    private static final Locator DRAWER_CLOSE_BUTTON = new Locator("DRAWER_CLOSE_BUTTON", By.xpath("//button[@data-testid='right-sidebar-close']"));
    private static final Locator VENDOR_NAME_INPUT = new Locator("VENDOR_NAME_INPUT", By.xpath("//input[@placeholder='Vendor Name']"));
    private static final Locator PENALTY_AGREEMENT_EDITOR = new Locator("PENALTY_AGREEMENT_EDITOR", By.xpath("(//div[@contenteditable='true' and contains(@class,'ProseMirror')])[1]"));
    private static final Locator DISPUTE_AGREEMENT_EDITOR = new Locator("DISPUTE_AGREEMENT_EDITOR", By.xpath("(//div[@contenteditable='true' and contains(@class,'ProseMirror')])[2]"));
    private static final Locator RECONCILIATION_AGREEMENT_EDITOR = new Locator("RECONCILIATION_AGREEMENT_EDITOR", By.xpath("(//div[@contenteditable='true' and contains(@class,'ProseMirror')])[3]"));
    private static final Locator ADD_VENDOR_SUBMIT_BUTTON = new Locator("ADD_VENDOR_SUBMIT_BUTTON", By.xpath("//button[.//span[normalize-space()='Add vendor']]"));
    private static final Locator SAVE_CHANGES_BUTTON = new Locator("SAVE_CHANGES_BUTTON", By.xpath("//button[.//span[normalize-space()='Save changes']]"));
    private static final Locator DRAWER_CANCEL_BUTTON = new Locator("DRAWER_CANCEL_BUTTON", By.xpath("//button[.//span[normalize-space()='Cancel']]"));

    public static final int PENALTY_EDITOR_INDEX = 1;
    public static final int DISPUTE_EDITOR_INDEX = 2;
    public static final int RECONCILIATION_EDITOR_INDEX = 3;

    private static final Locator SUPPORT_AGREEMENT_PENALTY_SECTION = new Locator("SUPPORT_AGREEMENT_PENALTY_SECTION", By.xpath("//*[normalize-space()='Penalty Agreement']"));
    private static final Locator SUPPORT_AGREEMENT_DISPUTE_SECTION = new Locator("SUPPORT_AGREEMENT_DISPUTE_SECTION", By.xpath("//*[normalize-space()='Dispute Agreement']"));
    private static final Locator SUPPORT_AGREEMENT_RECONCILIATION_SECTION = new Locator("SUPPORT_AGREEMENT_RECONCILIATION_SECTION", By.xpath("//*[normalize-space()='Reconciliation Agreement']"));
    private static final Locator POPUP_CLOSE_BUTTON = new Locator("POPUP_CLOSE_BUTTON", By.xpath("//button[@data-testid='modal-close-button']"));

    private static final Locator VENDOR_DETAIL_BREADCRUMB = new Locator("VENDOR_DETAIL_BREADCRUMB", By.xpath("//span[normalize-space()='Vendor Detail' and contains(@class,'Text_weight_bold')]"));
    private static final Locator FEATURE_LIST_EMPTY_STATE = new Locator("FEATURE_LIST_EMPTY_STATE", By.xpath("//*[normalize-space()='Feature list is empty']"));
    private static final Locator ADD_CONFIGURATION_BUTTON = new Locator("ADD_CONFIGURATION_BUTTON", By.xpath("//button[@data-testid='vendor-detail-add-config']"));
    private static final Locator MORE_ACTION_BUTTON = new Locator("MORE_ACTION_BUTTON", By.xpath("//button[.//span[starts-with(normalize-space(),'More action')]]"));
    private static final Locator CONFIG_HISTORY_MENU_ITEM = new Locator("CONFIG_HISTORY_MENU_ITEM", By.xpath("//div[@data-testid='dropdown-item'][.//*[normalize-space()='Config history']]"));
    private static final Locator SUPPORT_AGREEMENTS_MENU_ITEM = new Locator("SUPPORT_AGREEMENTS_MENU_ITEM", By.xpath("//div[@data-testid='dropdown-item'][.//*[normalize-space()='Support Agreements']]"));
    private static final Locator ACTIVATE_MENU_ITEM = new Locator("ACTIVATE_MENU_ITEM", By.xpath("//div[@data-testid='dropdown-item'][.//*[normalize-space()='Activate']]"));
    private static final Locator DEACTIVATE_MENU_ITEM = new Locator("DEACTIVATE_MENU_ITEM", By.xpath("//div[@data-testid='dropdown-item'][.//*[normalize-space()='Deactivate']]"));

    private static final Locator ACTIVATE_YES_BUTTON = new Locator("ACTIVATE_YES_BUTTON", By.xpath("//div[@id='toggle-vendor-status-modal']//button[.//span[normalize-space()='Yes, Activated']]"));
    private static final Locator DEACTIVATE_YES_BUTTON = new Locator("DEACTIVATE_YES_BUTTON", By.xpath("//div[@id='toggle-vendor-status-modal']//button[.//span[normalize-space()='Yes, Deactivate']]"));
    private static final Locator STATUS_MODAL_CANCEL_BUTTON = new Locator("STATUS_MODAL_CANCEL_BUTTON", By.xpath("//div[@id='toggle-vendor-status-modal']//button[.//span[normalize-space()='Cancel']]"));
    private static final Locator FEATURE_LIST_SEARCH_INPUT = new Locator("FEATURE_LIST_SEARCH_INPUT", By.xpath("//input[@data-testid='vendor-detail-search-input']"));
    private static final Locator FEATURE_LIST_ROWS = new Locator("FEATURE_LIST_ROWS", By.xpath("//table//tbody/tr"));

    private static final Locator ADD_NEW_CONFIGURATION_TITLE = new Locator("ADD_NEW_CONFIGURATION_TITLE", By.xpath("//*[normalize-space()='Add New Configuration']"));
    private static final Locator CONFIG_SEARCH_INPUT = new Locator("CONFIG_SEARCH_INPUT", By.xpath("//input[@placeholder='Search Box' and contains(@class,'SearchBox_accessibility_search')]"));
    private static final Locator FEATURE_LIST_SCROLL = new Locator("FEATURE_LIST_SCROLL", By.xpath("//div[@data-testid='feature-list-scroll']"));
    private static final Locator JSON_PREVIEW_LABEL = new Locator("JSON_PREVIEW_LABEL", By.xpath("//div[@data-testid='preview-json-input']//*[normalize-space()='JSON Preview']"));

    private static final Locator FIRST_FEATURE_CARD = new Locator("FIRST_FEATURE_CARD", By.xpath("(//div[@data-testid='feature-list-scroll']//div[starts-with(@data-testid,'feature-item-')])[1]"));
    private static final Locator JSON_PREVIEW_CONTENT = new Locator("JSON_PREVIEW_CONTENT", By.cssSelector("[data-testid='preview-json-input'] .cm-content"));
    private static final Locator ADD_CONFIGURATION_SUBMIT_BUTTON = new Locator("ADD_CONFIGURATION_SUBMIT_BUTTON", By.xpath("//button[@data-testid='btn-submit' and .//span[normalize-space()='Add configuration']]"));
    private static final Locator NEW_CONFIGURATION_CANCEL_BUTTON = new Locator("NEW_CONFIGURATION_CANCEL_BUTTON", By.xpath("//button[@data-testid='btn-cancel']"));

    private static final Locator CONFIGURATION_ADDED_MODAL_TITLE = new Locator("CONFIGURATION_ADDED_MODAL_TITLE", By.xpath("//div[@id='create-config-success-modal']//*[normalize-space()='Configuration added successfully!'] | //*[normalize-space()='Configuration added successfully!']"));
    private static final Locator YES_EDIT_NOW_BUTTON = new Locator("YES_EDIT_NOW_BUTTON", By.xpath("//button[.//span[normalize-space()='Yes, edit now']]"));
    private static final Locator MAYBE_LATER_BUTTON = new Locator("MAYBE_LATER_BUTTON", By.xpath("//button[.//span[normalize-space()='Maybe later']]"));

    private static final Locator EDIT_CONFIGURATION_B2C_TAB = new Locator("EDIT_CONFIGURATION_B2C_TAB", By.xpath("//button[contains(@class,'SegmentedControl_button')][.//span[normalize-space()='B2C']]"));
    private static final Locator EDIT_CONFIGURATION_B2B_AFFILIATE_TAB = new Locator("EDIT_CONFIGURATION_B2B_AFFILIATE_TAB", By.xpath("//button[contains(@class,'SegmentedControl_button')][.//span[normalize-space()='B2B Affiliate']]"));
    private static final Locator EDIT_CONFIGURATION_BOTH_STORES_TAB = new Locator("EDIT_CONFIGURATION_BOTH_STORES_TAB", By.xpath("//button[contains(@class,'SegmentedControl_button')][.//span[normalize-space()='Both stores']]"));
    private static final Locator EDIT_CONFIG_JSON_EDITOR = new Locator("EDIT_CONFIG_JSON_EDITOR", By.xpath("//div[@data-testid='edit-config-json-editor']"));
    private static final Locator EDIT_CONFIG_JSON_CONTENT = new Locator("EDIT_CONFIG_JSON_CONTENT", By.cssSelector("[data-testid='edit-config-json-editor'] .cm-content"));
    private static final Locator EDIT_CONFIG_SAVE_BUTTON = new Locator("EDIT_CONFIG_SAVE_BUTTON", By.xpath("//button[@data-testid='btn-submit' and .//span[normalize-space()='Save changes']]"));
    private static final Locator EDIT_CONFIG_CANCEL_BUTTON = new Locator("EDIT_CONFIG_CANCEL_BUTTON", By.xpath("//button[@data-testid='btn-cancel']"));

    private static final Locator VALUE_DIFFERENT_MODAL_TITLE = new Locator("VALUE_DIFFERENT_MODAL_TITLE", By.xpath("//div[@id='modal-json-diff']//h2[normalize-space()='Value different'] | //h2[normalize-space()='Value different']"));
    private static final Locator CONFIRM_CHANGES_BUTTON = new Locator("CONFIRM_CHANGES_BUTTON", By.xpath("//button[.//span[normalize-space()='Confirm changes']]"));
    private static final Locator VALUE_DIFF_OLD_PANEL_HEADER = new Locator("VALUE_DIFF_OLD_PANEL_HEADER", By.xpath("//div[@id='modal-json-diff']//*[normalize-space()='Old value']"));
    private static final Locator VALUE_DIFF_NEW_PANEL_HEADER = new Locator("VALUE_DIFF_NEW_PANEL_HEADER", By.xpath("//div[@id='modal-json-diff']//*[normalize-space()='New value']"));

    private static final Locator VALUE_DIFF_OLD_EDITOR_CONTENT = new Locator("VALUE_DIFF_OLD_EDITOR_CONTENT", By.xpath("//div[@id='modal-json-diff']//*[normalize-space()='Old value']/following::*[contains(concat(' ',normalize-space(@class),' '),' cm-content ')][1]"));
    private static final Locator VALUE_DIFF_NEW_EDITOR_CONTENT = new Locator("VALUE_DIFF_NEW_EDITOR_CONTENT", By.xpath("//div[@id='modal-json-diff']//*[normalize-space()='New value']/following::*[contains(concat(' ',normalize-space(@class),' '),' cm-content ')][1]"));
    private static final Locator VALUE_DIFF_CLOSE_BUTTON = new Locator("VALUE_DIFF_CLOSE_BUTTON", By.xpath("//div[@id='modal-json-diff']//button[@data-testid='modal-close-button']"));

    private static final Locator MINIMUM_FEATURES_MODAL_TITLE = new Locator("MINIMUM_FEATURES_MODAL_TITLE", By.xpath("//*[normalize-space()='Minimum features required']"));
    private static final Locator MIN_FEATURES_ADD_FEATURES_BUTTON = new Locator("MIN_FEATURES_ADD_FEATURES_BUTTON", By.xpath("//*[normalize-space()='Minimum features required']/ancestor::div[.//button[.//span[normalize-space()='Add features']]][1]//button[.//span[normalize-space()='Add features']]"));

    private static final Locator CONFIG_CHANGE_HISTORY_MODAL_TITLE = new Locator("CONFIG_CHANGE_HISTORY_MODAL_TITLE", By.xpath("//div[@id='config-change-history-modal']//h2//span[normalize-space()='Config change history']"));
    private static final Locator CONFIG_CHANGE_HISTORY_ROWS = new Locator("CONFIG_CHANGE_HISTORY_ROWS", By.xpath("//div[@id='config-change-history-modal']//tbody/tr"));
    private static final Locator CONFIG_CHANGE_HISTORY_CLOSE_BUTTON = new Locator("CONFIG_CHANGE_HISTORY_CLOSE_BUTTON", By.xpath("//div[@id='config-change-history-modal']//button[@data-testid='modal-close-button']"));
    private static final Locator DISCARD_CHANGES_CONFIRM_BUTTON = new Locator("DISCARD_CHANGES_CONFIRM_BUTTON",
            By.xpath("//button[.//span[normalize-space()='Discard changes'"
                    + " or normalize-space()='Discard'"
                    + " or normalize-space()='Yes, discard'"
                    + " or normalize-space()='Yes, discard changes']]"));

    private static Locator getRowByVendorCode(String code) {
        return new Locator("ROW_" + code, By.xpath("//tr[./td[normalize-space()='" + code + "']]"));
    }

    private static Locator getStatusLabelByVendorCode(String code) {
        return new Locator("STATUS_LABEL_" + code, By.xpath("//tr[./td[normalize-space()='" + code + "']]//div[contains(@class,'Label_truncation_lines')]"));
    }

    private static Locator getActionTriggerByVendorCode(String code) {
        return new Locator("ACTION_TRIGGER_" + code, By.xpath("//tr[./td[normalize-space()='" + code + "']]//div[@data-testid='dropdown-action-trigger']"));
    }

    private static Locator getLatestUpdatedDateByVendorCode(String code) {
        return new Locator("LATEST_UPDATED_DATE_" + code, By.xpath("//tr[./td[normalize-space()='" + code + "']]/td[4]//div[normalize-space()][1]"));
    }

    private static Locator getLatestUpdatedActorByVendorCode(String code) {
        return new Locator("LATEST_UPDATED_ACTOR_" + code, By.xpath("//tr[./td[normalize-space()='" + code + "']]/td[4]//span[normalize-space()]"));
    }

    private static Locator getActionMenuItem(String label) {
        return new Locator("ACTION_MENU_" + label.toUpperCase().replace(" ", "_"), By.xpath("//div[@data-testid='dropdown-item' and normalize-space()='" + label + "']"));
    }

    private static Locator getEditVendorDrawerTitle(String code) {
        return new Locator("EDIT_VENDOR_DRAWER_TITLE_" + code, By.xpath("//*[normalize-space()='Edit Vendor " + code + "']"));
    }

    private static Locator getSupportAgreementPopupTitle(String code) {
        String displayCode = toTitleCase(code);
        return new Locator("SUPPORT_AGREEMENT_POPUP_TITLE_" + code, By.xpath("//h2[contains(normalize-space(),'Support Agreement " + displayCode + "')]"));
    }

    private static String toTitleCase(String code) {
        if (code == null || code.isEmpty()) {
            return code;
        }
        String[] parts = code.split("_");
        StringBuilder sb = new StringBuilder();
        for (String part : parts) {
            if (part.isEmpty()) {
                continue;
            }
            if (sb.length() > 0) {
                sb.append(' ');
            }
            sb.append(Character.toUpperCase(part.charAt(0)));
            sb.append(part.substring(1).toLowerCase());
        }
        return sb.toString();
    }

    private static Locator getAgreementEditor(int editorIndex) {
        return new Locator("AGREEMENT_EDITOR_" + editorIndex, By.xpath("(//div[@contenteditable='true' and contains(@class,'ProseMirror')])[" + editorIndex + "]"));
    }

    private static Locator getBoldButton(int editorIndex) {
        return new Locator("BOLD_BUTTON_" + editorIndex, By.xpath("(//button[@aria-label='Bold'])[" + editorIndex + "]"));
    }

    private static Locator getItalicButton(int editorIndex) {
        return new Locator("ITALIC_BUTTON_" + editorIndex, By.xpath("(//button[@aria-label='Italic'])[" + editorIndex + "]"));
    }

    private static Locator getUnderlineButton(int editorIndex) {
        return new Locator("UNDERLINE_BUTTON_" + editorIndex, By.xpath("(//button[@aria-label='Underline'])[" + editorIndex + "]"));
    }

    private static Locator getBulletListButton(int editorIndex) {
        return new Locator("BULLET_LIST_BUTTON_" + editorIndex, By.xpath("(//button[@aria-label='Bullet list'])[" + editorIndex + "]"));
    }

    private static Locator getNumberedListButton(int editorIndex) {
        return new Locator("NUMBERED_LIST_BUTTON_" + editorIndex, By.xpath("(//button[@aria-label='Numbered list'])[" + editorIndex + "]"));
    }

    private static Locator getSupportAgreementSectionContent(String label) {
        return new Locator("SUPPORT_AGREEMENT_SECTION_" + label.toUpperCase().replace(" ", "_"), By.xpath("//*[normalize-space()='" + label + "']/following-sibling::*[1]"));
    }

    private static Locator getVendorDetailTitle(String code) {
        String displayCode = toTitleCase(code);
        return new Locator("VENDOR_DETAIL_TITLE_" + code, By.xpath("//span[@id='title-page-id' and normalize-space()='" + displayCode + "']"));
    }

    private static Locator getActivateModalTitle(String code) {
        String displayCode = toTitleCase(code);
        return new Locator("ACTIVATE_MODAL_TITLE_" + code,
                By.xpath("//div[@id='toggle-vendor-status-modal']//*[normalize-space()='Activate " + displayCode + "?']"));
    }

    private static Locator getDeactivateModalTitle(String code) {
        String displayCode = toTitleCase(code);
        return new Locator("DEACTIVATE_MODAL_TITLE_" + code,
                By.xpath("//div[@id='toggle-vendor-status-modal']//*[normalize-space()='Deactivate " + displayCode + "?']"));
    }

    private static Locator getConfigFeatureCard(String name) {
        return new Locator("CONFIG_FEATURE_CARD_" + name, By.xpath("//div[@data-testid='feature-list-scroll']//div[starts-with(@data-testid,'feature-item-') and .//div[normalize-space()='" + name + "']]"));
    }

    private static Locator getEditConfigurationTitle(String featureName) {
        return new Locator("EDIT_CONFIGURATION_TITLE_" + featureName, By.xpath("//*[normalize-space()='Configure " + featureName + "']"));
    }

    private static Locator getEditConfigurationSubtitleLocator(String featureName) {
        return new Locator("EDIT_CONFIGURATION_SUBTITLE_" + featureName,
                By.xpath("//div[normalize-space()='Configure " + featureName + "']/following-sibling::div[1]"));
    }

    private static Locator getFeatureListRowByName(String name) {
        return new Locator("FEATURE_LIST_ROW_" + name, By.xpath("//tr[./td[normalize-space()='" + name + "']]"));
    }

    private static Locator getFeatureListEditButton(String name) {
        return new Locator("FEATURE_LIST_EDIT_" + name, By.xpath("//tr[./td[normalize-space()='" + name + "']]//button[.//span[normalize-space()='Edit']]"));
    }

    private static Locator getFeatureListViewButton(String name) {
        return new Locator("FEATURE_LIST_VIEW_" + name, By.xpath("//tr[./td[normalize-space()='" + name + "']]//button[.//span[normalize-space()='View']]"));
    }

    private static Locator getViewConfigurationTitle(String featureName) {
        return new Locator("VIEW_CONFIGURATION_TITLE_" + featureName,
                By.xpath("//*[normalize-space()='Configure " + featureName + "' or normalize-space()='View " + featureName + "']"));
    }

    private static Locator getFeatureListDescriptionCell(String name) {
        return new Locator("FEATURE_LIST_DESC_" + name, By.xpath("(//tr[./td[normalize-space()='" + name + "']])[1]/td[3]"));
    }

    private static Locator getFeatureListLastUpdatedDateCell(String name) {
        return new Locator("FEATURE_LIST_LU_DATE_" + name, By.xpath("(//tr[./td[normalize-space()='" + name + "']])[1]/td[4]//span[1]"));
    }

    private static Locator getFeatureListLastUpdatedActorCell(String name) {
        return new Locator("FEATURE_LIST_LU_ACTOR_" + name, By.xpath("(//tr[./td[normalize-space()='" + name + "']])[1]/td[4]//div[contains(@class,'lowEmphasis')]"));
    }

    private static Locator getHistoryRowCell(int rowIndex, int colIndex) {
        return new Locator("HISTORY_ROW_" + rowIndex + "_COL_" + colIndex,
                By.xpath("(//div[@id='config-change-history-modal']//tbody/tr)[" + rowIndex + "]/td[" + colIndex + "]"));
    }

    private static Locator getHistoryLatestEditedDateCell(int rowIndex) {
        return new Locator("HISTORY_ROW_" + rowIndex + "_LE_DATE",
                By.xpath("(//div[@id='config-change-history-modal']//tbody/tr)[" + rowIndex + "]/td[6]//span[1]"));
    }

    private static Locator getHistoryLatestEditedActorCell(int rowIndex) {
        return new Locator("HISTORY_ROW_" + rowIndex + "_LE_ACTOR",
                By.xpath("(//div[@id='config-change-history-modal']//tbody/tr)[" + rowIndex + "]/td[6]//div[contains(@class,'lowEmphasis')]"));
    }

    private static Locator getConfigHistoryViewButton(int rowIndex) {
        return new Locator("CONFIG_HISTORY_VIEW_" + rowIndex, By.xpath("(//div[@id='config-change-history-modal']//tbody/tr)[" + rowIndex + "]//button[.//span[starts-with(normalize-space(),'View')]]"));
    }

    private static Locator getHistoryRowAllButtons(int rowIndex) {
        return new Locator("CONFIG_HISTORY_ROW_" + rowIndex + "_ALL_BUTTONS",
                By.xpath("(//div[@id='config-change-history-modal']//tbody/tr)[" + rowIndex + "]//button"));
    }

    /**
     * Waits for the Supplier Dashboard to finish loading before clicking the menu, then verifies the Vendor Management
     * page actually opened. A click fired while the landing page is still bootstrapping can get swallowed, so the click
     * is retried, with a direct navigation to the menu's href as the last resort.
     */
    public void goToVendorManagementMenu() {
        new WebDriverWait(driverTools.getDriver(), Duration.ofSeconds(30)).until(d ->
                "complete".equals(((JavascriptExecutor) d).executeScript("return document.readyState"))
                        && d.getCurrentUrl().contains("/supplier-dashboard"));
        driverTools.waitForElementToAppear(ANY_PAGE_TITLE, 15);

        for (int attempt = 1; attempt <= 3; attempt++) {
            driverTools.waitForElementToBeClickable(VENDOR_MANAGEMENT_MENU, 10);
            driverTools.clickWithJavascriptExecutor(VENDOR_MANAGEMENT_MENU);
            if (driverTools.waitForElementToAppear(PAGE_TITLE, 10).isPresent()) {
                return;
            }
            logger.log("Vendor Management page not shown after menu click attempt " + attempt + ", retrying");
        }

        logger.log("Vendor Management menu click did not open the page, navigating to " + VENDOR_MANAGEMENT_PATH + " directly");
        URI current = URI.create(driverTools.getDriver().getCurrentUrl());
        driverTools.getDriver().get(current.getScheme() + "://" + current.getAuthority() + VENDOR_MANAGEMENT_PATH);
        Assertion.assertThat("Vendor Management page should open after 3 menu clicks and a direct navigation to " + VENDOR_MANAGEMENT_PATH,
                driverTools.waitForElementToAppear(PAGE_TITLE, 15).isPresent(), is(true));
    }

    public boolean isPageDisplayed() {
        return driverTools.isElementDisplayed(PAGE_TITLE);
    }

    public void clickAddNewVendorButton() {
        driverTools.waitForElementToBeClickable(ADD_NEW_VENDOR_BUTTON, 10);
        driverTools.clickWithJavascriptExecutor(ADD_NEW_VENDOR_BUTTON);
    }

    public boolean isVendorCodePresent(String code) {
        try {
            driverTools.waitForElementToAppear(getRowByVendorCode(code), 5);
            return driverTools.isElementDisplayed(getRowByVendorCode(code));
        } catch (Exception e) {
            return false;
        }
    }

    public boolean findVendorCodeAcrossPages(String code, int maxPages) {
        driverTools.waitForElementToAppear(TABLE_ROWS, 15);
        for (int i = 0; i < maxPages; i++) {
            if (isVendorCodePresent(code)) {
                return true;
            }
            if (!driverTools.isElementDisplayed(NEXT_PAGE_BUTTON)) {
                return false;
            }
            WebElement nextBtn = driverTools.getDriver().findElement(By.xpath("//div[@data-testid='test-id-um-pagination']//button[@aria-label='Next']"));
            if (!nextBtn.isEnabled()) {
                return false;
            }
            driverTools.clickWithJavascriptExecutor(NEXT_PAGE_BUTTON);
            driverTools.dummyWait(2);
        }
        return isVendorCodePresent(code);
    }

    public String getVendorStatus(String code) {
        Locator statusLabel = getStatusLabelByVendorCode(code);
        driverTools.waitForElementToAppear(statusLabel, 10);
        return driverTools.getText(statusLabel).orElse("").trim();
    }

    public List<String> getListColumnHeaderTexts() {
        driverTools.waitForElementToAppear(TABLE_ROWS, 15);
        List<WebElement> headers = driverTools.getDriver().findElements(By.xpath("//table//thead//th"));
        List<String> out = new ArrayList<>();
        for (WebElement th : headers) {
            String text = th.getText() == null ? "" : th.getText().trim();
            if (!text.isEmpty()) {
                out.add(text);
            }
        }
        return out;
    }

    public List<String> getAllVendorCodesOnCurrentPage() {
        driverTools.waitForElementToAppear(TABLE_ROWS, 15);
        List<WebElement> codeCells = driverTools.getDriver().findElements(By.xpath("//table//tbody/tr/td[2]"));
        List<String> out = new ArrayList<>();
        for (WebElement td : codeCells) {
            String text = td.getText() == null ? "" : td.getText().trim();
            if (!text.isEmpty()) {
                out.add(text);
            }
        }
        return out;
    }

    public String getVendorLatestUpdatedDate(String code) {
        return pollForNonEmptyText(getLatestUpdatedDateByVendorCode(code), 4, 8, 2);
    }

    public String getVendorLatestUpdatedActor(String code) {
        return pollForNonEmptyText(getLatestUpdatedActorByVendorCode(code), 4, 8, 2);
    }

    public String[] readLatestUpdatedAcrossPages(String code, int maxPages) {
        driverTools.waitForElementToAppear(TABLE_ROWS, 15);
        for (int i = 0; i < maxPages; i++) {
            String[] found = tryReadOnCurrentPage(code);
            if (found != null) {
                return found;
            }
            if (!goToNextPageIfPossible()) {
                break;
            }
        }
        String[] finalTry = tryReadOnCurrentPage(code);
        return finalTry != null ? finalTry : new String[]{"", ""};
    }

    private String[] tryReadOnCurrentPage(String code) {
        if (!isVendorCodePresent(code)) {
            return null;
        }
        String date = pollForNonEmptyText(getLatestUpdatedDateByVendorCode(code), 3, 5, 1);
        String actor = pollForNonEmptyText(getLatestUpdatedActorByVendorCode(code), 3, 5, 1);
        if (date.isEmpty() && actor.isEmpty()) {
            return null;
        }
        return new String[]{date, actor};
    }

    private boolean goToNextPageIfPossible() {
        if (!driverTools.isElementDisplayed(NEXT_PAGE_BUTTON)) {
            return false;
        }
        WebElement nextBtn;
        try {
            nextBtn = driverTools.getDriver().findElement(NEXT_PAGE_BUTTON.by());
        } catch (Exception e) {
            return false;
        }
        if (!nextBtn.isEnabled()) {
            return false;
        }
        driverTools.clickWithJavascriptExecutor(NEXT_PAGE_BUTTON);
        driverTools.dummyWait(2);
        return true;
    }

    private String pollForNonEmptyText(Locator loc, int attempts, int perAttemptWaitSeconds, int betweenAttemptsSleepSeconds) {
        String last = "";
        for (int i = 0; i < attempts; i++) {
            try {
                driverTools.waitForElementToAppear(loc, perAttemptWaitSeconds);
                last = driverTools.getText(loc).orElse("").trim();
                if (!last.isEmpty()) {
                    return last;
                }
            } catch (Exception ignored) {}
            driverTools.dummyWait(betweenAttemptsSleepSeconds);
        }
        return last;
    }

    public static String getTodayListDate() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, MMM dd, yyyy", Locale.ENGLISH));
    }

    public static boolean isAlphabeticallySortedIgnoreCase(List<String> items) {
        for (int i = 1; i < items.size(); i++) {
            if (items.get(i - 1).compareToIgnoreCase(items.get(i)) > 0) {
                return false;
            }
        }
        return true;
    }

    public void clickActionForVendor(String code) {
        Locator trigger = getActionTriggerByVendorCode(code);
        driverTools.waitForElementToBeClickable(trigger, 10);
        driverTools.scrollElementToCenter(trigger);
        driverTools.clickWithJavascriptExecutor(trigger);
        driverTools.dummyWait(1);
    }

    public void clickActionMenuItem(String label) {
        Locator item = getActionMenuItem(label);
        driverTools.waitForElementToBeClickable(item, 10);
        driverTools.clickWithJavascriptExecutor(item);
        driverTools.dummyWait(1);
    }

    public boolean isActionMenuItemPresent(String label) {
        Locator item = getActionMenuItem(label);
        try {
            WebElement el = driverTools.getDriver().findElement(item.by());
            return el.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public List<String> getVisibleDropdownMenuLabels() {
        List<String> labels = new ArrayList<>();
        try {
            List<WebElement> items = driverTools.getDriver().findElements(By.xpath("//div[@data-testid='dropdown-item']"));
            for (WebElement el : items) {
                try {
                    if (el.isDisplayed()) {
                        String text = el.getText();
                        if (text != null && !text.trim().isEmpty()) {
                            labels.add(text.trim());
                        }
                    }
                } catch (Exception ignored) {
                }
            }
        } catch (Exception ignored) {
        }
        return labels;
    }

    public void closeAnyOpenDropdown() {
        try {
            new org.openqa.selenium.interactions.Actions(driverTools.getDriver()).sendKeys(Keys.ESCAPE).perform();
            driverTools.dummyWait(1);
        } catch (Exception ignored) {
        }
        try {
            List<WebElement> items = driverTools.getDriver().findElements(By.xpath("//div[@data-testid='dropdown-item']"));
            if (!items.isEmpty()) {
                ((JavascriptExecutor) driverTools.getDriver()).executeScript("document.body.click();");
                driverTools.dummyWait(1);
            }
        } catch (Exception ignored) {
        }
    }

    public boolean isAddNewVendorButtonAbsent() {
        try {
            List<WebElement> found = driverTools.getDriver().findElements(ADD_NEW_VENDOR_BUTTON.by());
            for (WebElement el : found) {
                try {
                    if (el.isDisplayed()) {
                        return false;
                    }
                } catch (Exception ignored) {
                }
            }
            return true;
        } catch (Exception e) {
            return true;
        }
    }

    public boolean isAddConfigurationButtonAbsent() {
        try {
            List<WebElement> found = driverTools.getDriver().findElements(
                    By.xpath("//button[.//span[normalize-space()='Add configuration']]"));
            for (WebElement el : found) {
                try {
                    if (el.isDisplayed()) {
                        return false;
                    }
                } catch (Exception ignored) {
                }
            }
            return true;
        } catch (Exception e) {
            return true;
        }
    }

    public boolean isJsonEditorReadOnly() {
        try {
            WebElement content = driverTools.getDriver().findElement(EDIT_CONFIG_JSON_CONTENT.by());
            String ce = content.getAttribute("contenteditable");
            if (ce != null && ce.equalsIgnoreCase("false")) {
                return true;
            }
            String parentClass = (String) ((JavascriptExecutor) driverTools.getDriver()).executeScript(
                    "let n = arguments[0]; while(n && n.classList){ if([...n.classList].some(c=>/readonly|readOnly|read-only|nonEditable|non-editable/i.test(c))) return [...n.classList].join(' '); n = n.parentElement; } return '';",
                    content);
            if (parentClass != null && !parentClass.isEmpty()) {
                return true;
            }
            return ce != null && ce.equalsIgnoreCase("false");
        } catch (Exception e) {
            return true;
        }
    }

    public boolean hasAnyRevertOrRestoreButtonInHistory() {
        try {
            List<WebElement> found = driverTools.getDriver().findElements(By.xpath(
                    "//div[@id='config-change-history-modal']//button[.//span[normalize-space()='Revert' or normalize-space()='Restore' or normalize-space()='Apply' or normalize-space()='Rollback' or normalize-space()='Undo']]"));
            for (WebElement el : found) {
                try {
                    if (el.isDisplayed()) {
                        return true;
                    }
                } catch (Exception ignored) {
                }
            }
        } catch (Exception ignored) {
        }
        return false;
    }

    public boolean isActionMenuItemEnabled(String label) {
        Locator item = getActionMenuItem(label);
        try {
            driverTools.waitForElementToAppear(item, 10);
        } catch (Exception e) {
            return false;
        }
        WebElement el;
        try {
            el = driverTools.getDriver().findElement(item.by());
        } catch (Exception e) {
            return false;
        }
        if (!el.isDisplayed() || !el.isEnabled()) {
            return false;
        }
        String ariaDisabled = el.getAttribute("aria-disabled");
        if (ariaDisabled != null && ariaDisabled.equalsIgnoreCase("true")) {
            return false;
        }
        String cls = el.getAttribute("class");
        if (cls != null && cls.toLowerCase(Locale.ROOT).contains("disabled")) {
            return false;
        }
        return true;
    }

    public boolean isAddVendorDrawerDisplayed() {
        try {
            driverTools.waitForElementToAppear(ADD_VENDOR_DRAWER_TITLE, 10);
            return driverTools.isElementDisplayed(ADD_VENDOR_DRAWER_TITLE);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isEditVendorDrawerDisplayed(String code) {
        try {
            Locator title = getEditVendorDrawerTitle(code);
            driverTools.waitForElementToAppear(title, 10);
            return driverTools.isElementDisplayed(title);
        } catch (Exception e) {
            return false;
        }
    }

    public void fillVendorName(String name) {
        driverTools.waitForElementToBeClickable(VENDOR_NAME_INPUT, 10);
        driverTools.clearInput(VENDOR_NAME_INPUT);
        driverTools.sendKeys(VENDOR_NAME_INPUT, name);
    }

    public void fillPenaltyAgreement(String text) {
        fillContentEditable(PENALTY_AGREEMENT_EDITOR, text);
    }

    public void fillDisputeAgreement(String text) {
        fillContentEditable(DISPUTE_AGREEMENT_EDITOR, text);
    }

    public void fillReconciliationAgreement(String text) {
        fillContentEditable(RECONCILIATION_AGREEMENT_EDITOR, text);
    }

    public void fillAllAgreements(String penalty, String dispute, String reconciliation) {
        fillPenaltyAgreement(penalty);
        fillDisputeAgreement(dispute);
        fillReconciliationAgreement(reconciliation);
    }

    private void fillContentEditable(Locator editor, String text) {
        driverTools.waitForElementToAppear(editor, 10);
        driverTools.scrollToElement(editor);
        driverTools.click(editor);
        try {
            driverTools.clearWithJsExecutor(editor);
        } catch (Exception ignored) {
        }
        driverTools.sendKeys(editor, text);
        driverTools.dummyWait(1);
    }

    public void focusAgreementEditor(int editorIndex) {
        Locator editor = getAgreementEditor(editorIndex);
        driverTools.waitForElementToAppear(editor, 10);
        driverTools.scrollToElement(editor);
        driverTools.click(editor);
    }

    public String getAgreementEditorHtml(int editorIndex) {
        WebElement editor = driverTools.waitForElementToAppear(getAgreementEditor(editorIndex), 10).orElseThrow();
        return (String) ((JavascriptExecutor) driverTools.getDriver()).executeScript("return arguments[0].innerHTML;", editor);
    }

    public void clearAgreementEditor(int editorIndex) {
        WebElement editor = driverTools.waitForElementToAppear(getAgreementEditor(editorIndex), 10).orElseThrow();
        driverTools.scrollToElement(getAgreementEditor(editorIndex));
        driverTools.click(getAgreementEditor(editorIndex));
        ((JavascriptExecutor) driverTools.getDriver()).executeScript(
                "const el = arguments[0]; el.focus(); const r = document.createRange(); r.selectNodeContents(el); const s = window.getSelection(); s.removeAllRanges(); s.addRange(r);",
                editor);
        editor.sendKeys(Keys.BACK_SPACE);
        driverTools.dummyWait(1);
    }

    public void applyBold(int editorIndex) {
        driverTools.waitForElementToBeClickable(getBoldButton(editorIndex), 5);
        driverTools.clickWithJavascriptExecutor(getBoldButton(editorIndex));
    }

    public void applyItalic(int editorIndex) {
        driverTools.waitForElementToBeClickable(getItalicButton(editorIndex), 5);
        driverTools.clickWithJavascriptExecutor(getItalicButton(editorIndex));
    }

    public void applyUnderline(int editorIndex) {
        driverTools.waitForElementToBeClickable(getUnderlineButton(editorIndex), 5);
        driverTools.clickWithJavascriptExecutor(getUnderlineButton(editorIndex));
    }

    public void applyBulletList(int editorIndex) {
        driverTools.waitForElementToBeClickable(getBulletListButton(editorIndex), 5);
        driverTools.clickWithJavascriptExecutor(getBulletListButton(editorIndex));
    }

    public void applyNumberedList(int editorIndex) {
        driverTools.waitForElementToBeClickable(getNumberedListButton(editorIndex), 5);
        driverTools.clickWithJavascriptExecutor(getNumberedListButton(editorIndex));
    }

    public void typeInAgreementEditor(int editorIndex, String text) {
        driverTools.sendKeys(getAgreementEditor(editorIndex), text);
    }

    public void pressEnterInAgreementEditor(int editorIndex) {
        WebElement editor = driverTools.waitForElementToAppear(getAgreementEditor(editorIndex), 10).orElseThrow();
        editor.sendKeys(Keys.ENTER);
    }

    public void fillAgreementWithAllFormats(int editorIndex, String boldText, String italicText, String underlineText, List<String> bulletItems, List<String> numberedItems) {
        clearAgreementEditor(editorIndex);
        focusAgreementEditor(editorIndex);

        applyBold(editorIndex);
        typeInAgreementEditor(editorIndex, boldText);
        applyBold(editorIndex);
        typeInAgreementEditor(editorIndex, " ");

        applyItalic(editorIndex);
        typeInAgreementEditor(editorIndex, italicText);
        applyItalic(editorIndex);
        typeInAgreementEditor(editorIndex, " ");

        applyUnderline(editorIndex);
        typeInAgreementEditor(editorIndex, underlineText);
        applyUnderline(editorIndex);

        pressEnterInAgreementEditor(editorIndex);

        applyBulletList(editorIndex);
        for (int i = 0; i < bulletItems.size(); i++) {
            typeInAgreementEditor(editorIndex, bulletItems.get(i));
            if (i < bulletItems.size() - 1) {
                pressEnterInAgreementEditor(editorIndex);
            }
        }

        pressEnterInAgreementEditor(editorIndex);
        pressEnterInAgreementEditor(editorIndex);

        applyNumberedList(editorIndex);
        for (int i = 0; i < numberedItems.size(); i++) {
            typeInAgreementEditor(editorIndex, numberedItems.get(i));
            if (i < numberedItems.size() - 1) {
                pressEnterInAgreementEditor(editorIndex);
            }
        }
        driverTools.dummyWait(1);
    }

    public void editAllAgreementsWithAllFormats(String boldText, String italicText, String underlineText, List<String> bulletItems, List<String> numberedItems) {
        int[] indices = {PENALTY_EDITOR_INDEX, DISPUTE_EDITOR_INDEX, RECONCILIATION_EDITOR_INDEX};
        for (int idx : indices) {
            fillAgreementWithAllFormats(idx, boldText, italicText, underlineText, bulletItems, numberedItems);
        }
    }

    public boolean editorHtmlHasAllRichTextFormats(int editorIndex) {
        String html = getAgreementEditorHtml(editorIndex);
        return html.contains("<strong>") && html.contains("<em>") && html.contains("<u>") && html.contains("<ul>") && html.contains("<ol>");
    }

    public String getSupportAgreementSectionHtml(String label) {
        Locator content = getSupportAgreementSectionContent(label);
        WebElement el = driverTools.waitForElementToAppear(content, 10).orElseThrow();
        return (String) ((JavascriptExecutor) driverTools.getDriver()).executeScript("return arguments[0].innerHTML;", el);
    }

    public boolean supportAgreementSectionHasAllRichTextFormats(String label) {
        String html = getSupportAgreementSectionHtml(label);
        return html.contains("<strong>") && html.contains("<em>") && html.contains("<u>") && html.contains("<ul>") && html.contains("<ol>");
    }

    public boolean supportAgreementSectionHasFormattedText(String sectionLabel, String tagPath, String text) {
        String xpath = "//*[normalize-space()='" + sectionLabel + "']"
                + "/ancestor::*[.//ul or .//ol][1]"
                + "//" + tagPath + "[normalize-space()='" + text + "']";
        return !driverTools.getDriver().findElements(By.xpath(xpath)).isEmpty();
    }

    public void clickAddVendorSubmit() {
        driverTools.waitForElementToBeClickable(ADD_VENDOR_SUBMIT_BUTTON, 15);
        driverTools.clickWithJavascriptExecutor(ADD_VENDOR_SUBMIT_BUTTON);
        driverTools.waitForElementToDisappear(ADD_VENDOR_SUBMIT_BUTTON, 15);
    }

    public boolean isAddVendorSubmitEnabled() {
        try {
            WebElement el = driverTools.getDriver().findElement(ADD_VENDOR_SUBMIT_BUTTON.by());
            if (!el.isDisplayed() || !el.isEnabled()) {
                return false;
            }
            String ariaDisabled = el.getAttribute("aria-disabled");
            if (ariaDisabled != null && ariaDisabled.equalsIgnoreCase("true")) {
                return false;
            }
            String cls = el.getAttribute("class");
            if (cls != null && cls.toLowerCase(Locale.ROOT).contains("disabled")) {
                return false;
            }
            String disabledAttr = el.getAttribute("disabled");
            if (disabledAttr != null && !disabledAttr.equalsIgnoreCase("false")) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void attemptAddVendorSubmitNoWait() {
        try {
            WebElement el = driverTools.getDriver().findElement(ADD_VENDOR_SUBMIT_BUTTON.by());
            driverTools.scrollElementToCenter(ADD_VENDOR_SUBMIT_BUTTON);
            ((JavascriptExecutor) driverTools.getDriver()).executeScript("arguments[0].click();", el);
        } catch (Exception ignored) {
        }
        driverTools.dummyWait(2);
    }

    public boolean isAddVendorDrawerStillOpen() {
        try {
            return driverTools.isElementDisplayed(ADD_VENDOR_DRAWER_TITLE);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isAnyDrawerFieldErrorVisible() {
        List<By> errorSelectors = List.of(
                By.xpath("//aside[@data-testid='right-sidebar']//*[@role='alert']"),
                By.xpath("//aside[@data-testid='right-sidebar']//*[contains(@data-testid,'error')]"),
                By.xpath("//aside[@data-testid='right-sidebar']//*[contains(@class,'error') or contains(@class,'Error')]"),
                By.xpath("//aside[@data-testid='right-sidebar']//*[contains(@class,'invalid') or contains(@class,'Invalid')]"),
                By.xpath("//aside[@data-testid='right-sidebar']//*[contains(@class,'red') or contains(@class,'danger') or contains(@class,'Danger')]"));
        for (By by : errorSelectors) {
            try {
                List<WebElement> found = driverTools.getDriver().findElements(by);
                for (WebElement el : found) {
                    try {
                        if (el.isDisplayed()) {
                            String text = el.getText();
                            if (text != null && !text.trim().isEmpty()) {
                                return true;
                            }
                        }
                    } catch (Exception ignored) {
                    }
                }
            } catch (Exception ignored) {
            }
        }
        return false;
    }

    public boolean isSuccessSnackbarPresent(int timeoutSeconds) {
        try {
            driverTools.waitForElementToAppear(SNACKBAR_MESSAGE, timeoutSeconds);
            return driverTools.isElementDisplayed(SNACKBAR_MESSAGE);
        } catch (Exception e) {
            return false;
        }
    }

    public void closeAddVendorDrawerSafely() {
        try {
            if (driverTools.isElementDisplayed(DRAWER_CANCEL_BUTTON)) {
                driverTools.clickWithJavascriptExecutor(DRAWER_CANCEL_BUTTON);
                driverTools.dummyWait(1);
            }
        } catch (Exception ignored) {
        }
        List<By> discardButtons = List.of(
                By.xpath("//button[.//span[normalize-space()='Yes, discard']]"),
                By.xpath("//button[.//span[normalize-space()='Yes, Discard']]"),
                By.xpath("//button[.//span[normalize-space()='Discard changes']]"),
                By.xpath("//button[.//span[normalize-space()='Discard']]"),
                By.xpath("//button[.//span[normalize-space()='Yes']]"));
        for (By by : discardButtons) {
            try {
                WebElement el = driverTools.getDriver().findElement(by);
                if (el.isDisplayed() && el.isEnabled()) {
                    ((JavascriptExecutor) driverTools.getDriver()).executeScript("arguments[0].click();", el);
                    driverTools.dummyWait(1);
                    break;
                }
            } catch (Exception ignored) {
            }
        }
        try {
            if (driverTools.isElementDisplayed(ADD_VENDOR_DRAWER_TITLE)) {
                driverTools.clickWithJavascriptExecutor(DRAWER_CLOSE_BUTTON);
                driverTools.dummyWait(1);
                for (By by : discardButtons) {
                    try {
                        WebElement el = driverTools.getDriver().findElement(by);
                        if (el.isDisplayed() && el.isEnabled()) {
                            ((JavascriptExecutor) driverTools.getDriver()).executeScript("arguments[0].click();", el);
                            driverTools.dummyWait(1);
                            break;
                        }
                    } catch (Exception ignored) {
                    }
                }
            }
        } catch (Exception ignored) {
        }
        try {
            driverTools.waitForElementToDisappear(ADD_VENDOR_DRAWER_TITLE, 5);
        } catch (Exception ignored) {
        }
    }

    public void clickSaveChanges() {
        driverTools.waitForElementToBeClickable(SAVE_CHANGES_BUTTON, 15);
        driverTools.clickWithJavascriptExecutor(SAVE_CHANGES_BUTTON);
        driverTools.waitForElementToDisappear(SAVE_CHANGES_BUTTON, 15);
    }

    public void clickDrawerCancel() {
        driverTools.waitForElementToBeClickable(DRAWER_CANCEL_BUTTON, 5);
        driverTools.clickWithJavascriptExecutor(DRAWER_CANCEL_BUTTON);
    }

    public boolean isSupportAgreementPopupDisplayed(String code) {
        try {
            Locator title = getSupportAgreementPopupTitle(code);
            driverTools.waitForElementToAppear(title, 10);
            return driverTools.isElementDisplayed(title);
        } catch (Exception e) {
            return false;
        }
    }

    public List<String> getSupportAgreementSections() {
        List<String> sections = new ArrayList<>();
        for (Locator sectionLocator : List.of(
                SUPPORT_AGREEMENT_PENALTY_SECTION,
                SUPPORT_AGREEMENT_DISPUTE_SECTION,
                SUPPORT_AGREEMENT_RECONCILIATION_SECTION)) {
            try {
                if (driverTools.isElementDisplayed(sectionLocator)) {
                    driverTools.getText(sectionLocator).ifPresent(t -> sections.add(t.trim()));
                }
            } catch (StaleElementReferenceException e) {
                driverTools.getText(sectionLocator).ifPresent(t -> sections.add(t.trim()));
            }
        }
        return sections;
    }

    public void closeSupportAgreementPopup() {
        driverTools.waitForElementToBeClickable(POPUP_CLOSE_BUTTON, 5);
        driverTools.clickWithJavascriptExecutor(POPUP_CLOSE_BUTTON);
        driverTools.waitForElementToDisappear(POPUP_CLOSE_BUTTON, 5);
    }

public boolean verifySnackbarMessage(String expectedMessage) {
    if (expectedMessage == null || expectedMessage.isEmpty()) {
        throw new IllegalArgumentException("expectedMessage must be non-empty");
    }
    driverTools.waitForElementToAppear(SNACKBAR_MESSAGE, 15);
    String actualMessage = driverTools.getText(SNACKBAR_MESSAGE).orElse("");
    return actualMessage.contains(expectedMessage);
}

    public String generateVendorCode() {
        String suffix = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return "AT" + suffix;
    }

    public boolean isVendorDetailPageDisplayed(String vendorCode) {
        try {
            driverTools.waitForElementToAppear(VENDOR_DETAIL_BREADCRUMB, 15);
            Locator title = getVendorDetailTitle(vendorCode);
            driverTools.waitForElementToAppear(title, 10);
            return driverTools.isElementDisplayed(VENDOR_DETAIL_BREADCRUMB) && driverTools.isElementDisplayed(title);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isFeatureListEmpty() {
        try {
            driverTools.waitForElementToAppear(FEATURE_LIST_EMPTY_STATE, 10);
            return driverTools.isElementDisplayed(FEATURE_LIST_EMPTY_STATE);
        } catch (Exception e) {
            return false;
        }
    }

    public void clickAddConfigurationButton() {
        driverTools.waitForElementToBeClickable(ADD_CONFIGURATION_BUTTON, 10);
        driverTools.clickWithJavascriptExecutor(ADD_CONFIGURATION_BUTTON);
    }

    public boolean isAddNewConfigurationPageDisplayed() {
        try {
            driverTools.waitForElementToAppear(ADD_NEW_CONFIGURATION_TITLE, 15);
            return driverTools.isElementDisplayed(ADD_NEW_CONFIGURATION_TITLE);
        } catch (Exception e) {
            return false;
        }
    }

    public void searchConfigurationFeature(String keyword) {
        driverTools.waitForElementToBeClickable(CONFIG_SEARCH_INPUT, 10);
        driverTools.clearInput(CONFIG_SEARCH_INPUT);
        driverTools.sendKeys(CONFIG_SEARCH_INPUT, keyword);
        driverTools.dummyWait(1);
    }

    public void selectConfigurationFeature(String featureName) {
        Locator card = getConfigFeatureCard(featureName);
        driverTools.waitForElementToBeClickable(card, 10);
        driverTools.clickWithJavascriptExecutor(card);
        driverTools.dummyWait(1);
    }

    public String selectFirstAvailableFeature() {
        driverTools.waitForElementToBeClickable(FIRST_FEATURE_CARD, 15);
        return clickFeatureCardAtIndex(1);
    }

    public String selectFirstUsableFeatureForEditing() {
        return selectFirstUsableFeatureForEditingExcluding(java.util.Collections.emptySet());
    }

    public String selectFirstUsableFeatureForEditingExcluding(java.util.Set<String> excludeNames) {
        driverTools.waitForElementToBeVisible(FEATURE_LIST_SCROLL, 15);
        By cardsXpath = By.xpath("//div[@data-testid='feature-list-scroll']//div[starts-with(@data-testid,'feature-item-')]");
        int total = 0;
        long deadline = System.currentTimeMillis() + 20_000L;
        while (System.currentTimeMillis() < deadline) {
            total = driverTools.getDriver().findElements(cardsXpath).size();
            if (total > 0) {
                break;
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        if (total == 0) {
            throw new IllegalStateException("No feature cards found in the Add New Configuration list after waiting up to 20 seconds.");
        }
        java.util.Set<String> excluded = excludeNames == null ? java.util.Collections.emptySet() : excludeNames;
        List<String> skipped = new ArrayList<>();
        for (int i = 1; i <= total; i++) {
            String name = clickFeatureCardAtIndex(i);
            if (excluded.contains(name)) {
                skipped.add("'" + name + "' (excluded)");
                continue;
            }
            String preview = getJsonPreviewValue();
            if (isUsableBasisJson(preview)) {
                return name;
            }
            skipped.add("'" + name + "' (preview=" + (preview == null ? "null" : preview.trim()) + ")");
        }
        throw new IllegalStateException(
                "None of the " + total + " feature(s) in the Add New Configuration list has a usable JSON Preview "
                        + "(non-empty object with at least one string value to mutate) after excluding " + excluded + ". Skipped: " + skipped);
    }

    private String clickFeatureCardAtIndex(int oneBasedIndex) {
        Locator card = new Locator("FEATURE_CARD_AT_" + oneBasedIndex,
                By.xpath("(//div[@data-testid='feature-list-scroll']//div[starts-with(@data-testid,'feature-item-')])[" + oneBasedIndex + "]"));
        driverTools.waitForElementToBeClickable(card, 10);
        WebElement el = driverTools.getDriver().findElement(card.by());
        Object txt = ((JavascriptExecutor) driverTools.getDriver())
                .executeScript("return arguments[0].innerText;", el);
        String innerText = txt == null ? "" : txt.toString();
        String name = innerText.split("\\r?\\n")[0].trim();
        driverTools.scrollToElement(card);
        driverTools.clickWithJavascriptExecutor(card);
        driverTools.dummyWait(1);
        return name;
    }

    public static boolean isUsableBasisJson(String json) {
        if (json == null) {
            return false;
        }
        String trimmed = json.trim();
        if (!trimmed.startsWith("{")) {
            return false;
        }
        if (trimmed.replaceAll("\\s", "").equals("{}")) {
            return false;
        }
        return Pattern.compile(":\\s*\"").matcher(json).find();
    }

    public String getJsonPreviewValue() {
        return readCodeMirrorContent(JSON_PREVIEW_CONTENT);
    }

    public static int extractVersionFromHistoryFeatureName(String featureCellText) {
        if (featureCellText == null) {
            return -1;
        }
        Matcher m = Pattern.compile("v(\\d+)\\s*$", Pattern.CASE_INSENSITIVE).matcher(featureCellText.trim());
        return m.find() ? Integer.parseInt(m.group(1)) : -1;
    }

    public static String mutateFirstJsonStringValue(String json, String newValue) {
        if (json == null || newValue == null) {
            return json;
        }
        Pattern p = Pattern.compile("(:\\s*\")([^\"]*)(\")");
        Matcher m = p.matcher(json);
        if (m.find()) {
            return json.substring(0, m.start()) + m.group(1) + newValue + m.group(3) + json.substring(m.end());
        }
        return json;
    }

    public boolean isJsonPreviewDisplayed() {
        try {
            driverTools.waitForElementToAppear(JSON_PREVIEW_LABEL, 10);
            return driverTools.isElementDisplayed(JSON_PREVIEW_LABEL);
        } catch (Exception e) {
            return false;
        }
    }

    public void submitAddConfiguration() {
        driverTools.waitForElementToBeClickable(ADD_CONFIGURATION_SUBMIT_BUTTON, 10);
        driverTools.clickWithJavascriptExecutor(ADD_CONFIGURATION_SUBMIT_BUTTON);
    }

    public boolean isConfigurationAddedModalDisplayed() {
        try {
            driverTools.waitForElementToAppear(CONFIGURATION_ADDED_MODAL_TITLE, 15);
            return driverTools.isElementDisplayed(CONFIGURATION_ADDED_MODAL_TITLE);
        } catch (Exception e) {
            return false;
        }
    }

    public void clickYesEditNowInAddedModal() {
        driverTools.waitForElementToBeClickable(YES_EDIT_NOW_BUTTON, 10);
        driverTools.clickWithJavascriptExecutor(YES_EDIT_NOW_BUTTON);
        driverTools.waitForElementToDisappear(YES_EDIT_NOW_BUTTON, 10);
    }

    public void clickMaybeLaterInAddedModal() {
        driverTools.waitForElementToBeClickable(MAYBE_LATER_BUTTON, 10);
        driverTools.clickWithJavascriptExecutor(MAYBE_LATER_BUTTON);
        driverTools.waitForElementToDisappear(MAYBE_LATER_BUTTON, 10);
    }

    public boolean isEditConfigurationPageDisplayed(String featureName) {
        try {
            Locator title = getEditConfigurationTitle(featureName);
            driverTools.waitForElementToAppear(title, 15);
            return driverTools.isElementDisplayed(title);
        } catch (Exception e) {
            return false;
        }
    }

    public void selectStoreScopeTab(String storeScope) {
        Locator tab = switch (storeScope) {
            case "B2C" -> EDIT_CONFIGURATION_B2C_TAB;
            case "B2B Affiliate" -> EDIT_CONFIGURATION_B2B_AFFILIATE_TAB;
            case "Both stores" -> EDIT_CONFIGURATION_BOTH_STORES_TAB;
            default -> throw new IllegalArgumentException("Unsupported store scope: " + storeScope);
        };
        int maxAttempts = 3;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            driverTools.waitForElementToBeClickable(tab, 10);
            WebElement tabEl = driverTools.getDriver().findElement(tab.by());
            try {
                tabEl.click();
            } catch (Exception ignored) {
                driverTools.clickWithJavascriptExecutor(tab);
            }
            driverTools.dummyWait(1);
            if (isStoreScopeTabActive(storeScope)) {
                return;
            }
        }
        throw new IllegalStateException("Failed to activate store-scope tab '" + storeScope
                + "' after " + maxAttempts + " attempt(s). The click was dispatched but the "
                + "SegmentedControl did not mark this tab as active — subsequent edit-config "
                + "actions would silently target the wrong scope.");
    }

    private boolean isStoreScopeTabActive(String storeScope) {
        Locator tab = switch (storeScope) {
            case "B2C" -> EDIT_CONFIGURATION_B2C_TAB;
            case "B2B Affiliate" -> EDIT_CONFIGURATION_B2B_AFFILIATE_TAB;
            case "Both stores" -> EDIT_CONFIGURATION_BOTH_STORES_TAB;
            default -> throw new IllegalArgumentException("Unsupported store scope: " + storeScope);
        };
        try {
            WebElement el = driverTools.getDriver().findElement(tab.by());
            String cls = el.getAttribute("class");
            if (cls == null) {
                return false;
            }
            return cls.contains("highEmphasis") && !cls.contains("lowEmphasis");
        } catch (Exception e) {
            return false;
        }
    }

    public void setJsonEditorValue(String json) {
        WebElement editorContainer = driverTools.waitForElementToAppear(EDIT_CONFIG_JSON_EDITOR, 15).orElseThrow();
        JavascriptExecutor js = (JavascriptExecutor) driverTools.getDriver();
        Object result = js.executeScript(
                "try {"
                + "  var container = arguments[0];"
                + "  var newValue = arguments[1];"
                + "  var content = container.querySelector('.cm-content');"
                + "  if (!content) return 'no-content';"
                + "  content.focus();"
                + "  var cmEditor = container.querySelector('.cm-editor');"
                + "  if (cmEditor && cmEditor.cmView && cmEditor.cmView.view) {"
                + "    var view = cmEditor.cmView.view;"
                + "    view.dispatch({ changes: { from: 0, to: view.state.doc.length, insert: newValue } });"
                + "    return 'ok:cmview';"
                + "  }"
                + "  var range = document.createRange();"
                + "  range.selectNodeContents(content);"
                + "  var sel = window.getSelection();"
                + "  sel.removeAllRanges();"
                + "  sel.addRange(range);"
                + "  document.execCommand('delete');"
                + "  document.execCommand('insertText', false, newValue);"
                + "  return 'ok:execCommand';"
                + "} catch (e) { return 'error: ' + (e && e.message); }",
                editorContainer, json);
        String status = result == null ? "null" : result.toString();
        if (!status.startsWith("ok")) {
            WebElement content = editorContainer.findElement(By.cssSelector(".cm-content"));
            content.click();
            String selectAll = System.getProperty("os.name", "").toLowerCase().contains("mac")
                    ? Keys.chord(Keys.COMMAND, "a")
                    : Keys.chord(Keys.CONTROL, "a");
            content.sendKeys(selectAll);
            content.sendKeys(Keys.DELETE);
            content.sendKeys(json);
        }
        driverTools.dummyWait(1);
    }

    public String getJsonEditorValue() {
        WebElement editorContainer = driverTools.waitForElementToAppear(EDIT_CONFIG_JSON_EDITOR, 15).orElseThrow();
        JavascriptExecutor js = (JavascriptExecutor) driverTools.getDriver();
        Object value = js.executeScript(
                "try {"
                + "  var container = arguments[0];"
                + "  var cmEditor = container.querySelector('.cm-editor');"
                + "  if (cmEditor && cmEditor.cmView && cmEditor.cmView.view) {"
                + "    return cmEditor.cmView.view.state.doc.toString();"
                + "  }"
                + "  var content = container.querySelector('.cm-content');"
                + "  return content ? content.innerText : '';"
                + "} catch (e) { return ''; }",
                editorContainer);
        return value == null ? "" : value.toString();
    }

    public String buildJsonPayload(String pipeSeparatedKeyValues) {
        if (pipeSeparatedKeyValues == null || pipeSeparatedKeyValues.isEmpty()) {
            return "{}";
        }
        String[] pairs = pipeSeparatedKeyValues.split("\\|");
        StringBuilder sb = new StringBuilder("{\n");
        for (int i = 0; i < pairs.length; i++) {
            String[] kv = pairs[i].split("=", 2);
            String key = kv[0].trim();
            String value = kv.length > 1 ? kv[1].trim() : "";
            sb.append("  \"").append(key).append("\": \"").append(value).append("\"");
            if (i < pairs.length - 1) {
                sb.append(",");
            }
            sb.append("\n");
        }
        sb.append("}");
        return sb.toString();
    }

    public boolean isEditConfigSaveButtonEnabled() {
        try {
            WebElement el = driverTools.getDriver().findElement(EDIT_CONFIG_SAVE_BUTTON.by());
            if (!el.isDisplayed() || !el.isEnabled()) {
                return false;
            }
            String ariaDisabled = el.getAttribute("aria-disabled");
            if (ariaDisabled != null && ariaDisabled.equalsIgnoreCase("true")) {
                return false;
            }
            String cls = el.getAttribute("class");
            if (cls != null && cls.toLowerCase(Locale.ROOT).contains("disabled")) {
                return false;
            }
            String disabledAttr = el.getAttribute("disabled");
            if (disabledAttr != null && !disabledAttr.equalsIgnoreCase("false")) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getJsonValidationStatusText() {
        try {
            List<WebElement> found = driverTools.getDriver().findElements(
                    By.xpath("//*[@data-testid='json-validation-status']"));
            for (WebElement el : found) {
                try {
                    if (el.isDisplayed()) {
                        String text = el.getText();
                        if (text != null) {
                            return text.trim();
                        }
                    }
                } catch (Exception ignored) {
                }
            }
        } catch (Exception ignored) {
        }
        return "";
    }

    public static String mutateJsonToInvalidSyntax(String validJson) {
        if (validJson == null || validJson.isEmpty()) {
            return "{ invalid";
        }
        String trimmed = validJson.trim();
        if (trimmed.endsWith("}")) {
            return trimmed.substring(0, trimmed.length() - 1);
        }
        if (trimmed.endsWith("]")) {
            return trimmed.substring(0, trimmed.length() - 1);
        }
        return trimmed + " ,,";
    }

    public static String mutateJsonToInvalidSchema(String validJson) {
        if (validJson == null || validJson.isEmpty()) {
            return validJson;
        }
        Matcher m = Pattern.compile("(:\\s*)(true|false)(\\s*[,}\\]])").matcher(validJson);
        if (m.find()) {
            return validJson.substring(0, m.start()) + m.group(1) + "\"" + m.group(2) + "\"" + m.group(3) + validJson.substring(m.end());
        }
        m = Pattern.compile("(:\\s*)(-?\\d+(?:\\.\\d+)?)(\\s*[,}\\]])").matcher(validJson);
        if (m.find()) {
            return validJson.substring(0, m.start()) + m.group(1) + "\"" + m.group(2) + "\"" + m.group(3) + validJson.substring(m.end());
        }
        m = Pattern.compile("(:\\s*)\"([^\"]*)\"(\\s*[,}\\]])").matcher(validJson);
        if (m.find()) {
            return validJson.substring(0, m.start()) + m.group(1) + "99999" + m.group(3) + validJson.substring(m.end());
        }
        return validJson;
    }

    public void clickSaveChangesOnEditConfiguration() {
        driverTools.waitForElementToBeClickable(EDIT_CONFIG_SAVE_BUTTON, 15);
        driverTools.clickWithJavascriptExecutor(EDIT_CONFIG_SAVE_BUTTON);
    }

    public boolean isValueDifferentModalDisplayed() {
        try {
            driverTools.waitForElementToAppear(VALUE_DIFFERENT_MODAL_TITLE, 10);
            return driverTools.isElementDisplayed(VALUE_DIFFERENT_MODAL_TITLE);
        } catch (Exception e) {
            return false;
        }
    }

    public void clickConfirmChanges() {
        driverTools.waitForElementToBeClickable(CONFIRM_CHANGES_BUTTON, 10);
        driverTools.clickWithJavascriptExecutor(CONFIRM_CHANGES_BUTTON);
        driverTools.waitForElementToDisappear(CONFIRM_CHANGES_BUTTON, 10);
    }

    public void filterFeatureList(String keyword) {
        driverTools.waitForElementToBeClickable(FEATURE_LIST_SEARCH_INPUT, 10);
        driverTools.clearInput(FEATURE_LIST_SEARCH_INPUT);
        driverTools.sendKeys(FEATURE_LIST_SEARCH_INPUT, keyword);
        driverTools.dummyWait(2);
    }

    public boolean isFeatureRowPresent(String featureName) {
        try {
            Locator row = getFeatureListRowByName(featureName);
            driverTools.waitForElementToAppear(row, 10);
            return driverTools.isElementDisplayed(row);
        } catch (Exception e) {
            return false;
        }
    }

    public List<String> getFeatureListColumnHeaders() {
        driverTools.waitForElementToAppear(FEATURE_LIST_ROWS, 15);
        List<WebElement> headers = driverTools.getDriver().findElements(By.xpath("//table//thead//th"));
        List<String> out = new ArrayList<>();
        for (WebElement th : headers) {
            String text = th.getText() == null ? "" : th.getText().trim();
            if (!text.isEmpty()) {
                out.add(text);
            }
        }
        return out;
    }

    public String getFeatureListRowDescription(String featureName) {
        Locator cell = getFeatureListDescriptionCell(featureName);
        driverTools.waitForElementToAppear(cell, 10);
        return driverTools.getText(cell).orElse("").trim();
    }

    public String getFeatureCardDescriptionByName(String featureName) {
        String xpath = "//div[@data-testid='feature-list-scroll']"
                + "//div[starts-with(@data-testid,'feature-item-') and .//div[normalize-space()='" + featureName + "']]"
                + "//div[contains(@class,'line-clamp-2')][1]";
        try {
            List<WebElement> found = driverTools.getDriver().findElements(By.xpath(xpath));
            for (WebElement el : found) {
                try {
                    if (el.isDisplayed()) {
                        String text = el.getText();
                        if (text != null && !text.trim().isEmpty()) {
                            return text.trim();
                        }
                    }
                } catch (Exception ignored) {
                }
            }
        } catch (Exception ignored) {
        }
        return "";
    }

    public boolean clickFeatureCardByName(String featureName) {
        String xpath = "//div[@data-testid='feature-list-scroll']"
                + "//div[starts-with(@data-testid,'feature-item-') and .//div[normalize-space()='" + featureName + "']]";
        try {
            List<WebElement> found = driverTools.getDriver().findElements(By.xpath(xpath));
            for (WebElement el : found) {
                try {
                    if (el.isDisplayed()) {
                        ((JavascriptExecutor) driverTools.getDriver()).executeScript(
                                "arguments[0].scrollIntoView({block:'center'});", el);
                        driverTools.dummyWait(1);
                        ((JavascriptExecutor) driverTools.getDriver()).executeScript("arguments[0].click();", el);
                        driverTools.dummyWait(2);
                        return true;
                    }
                } catch (Exception ignored) {
                }
            }
        } catch (Exception ignored) {
        }
        return false;
    }

    public String getHowToUseText() {
        String xpath = "//span[normalize-space()='How to use']/ancestor::div[.//*[contains(@class,'ProseMirror')]][1]//*[contains(@class,'ProseMirror')]";
        try {
            List<WebElement> found = driverTools.getDriver().findElements(By.xpath(xpath));
            for (WebElement el : found) {
                try {
                    if (el.isDisplayed()) {
                        String text = el.getText();
                        if (text != null && !text.trim().isEmpty()) {
                            return text.trim();
                        }
                    }
                } catch (Exception ignored) {
                }
            }
        } catch (Exception ignored) {
        }
        return "";
    }

    public boolean isJsonPreviewNonEmpty() {
        try {
            driverTools.waitForElementToAppear(JSON_PREVIEW_CONTENT, 10);
            WebElement content = driverTools.getDriver().findElement(JSON_PREVIEW_CONTENT.by());
            String text = content.getText();
            if (text == null) {
                return false;
            }
            String trimmed = text.trim();
            return !trimmed.isEmpty() && !trimmed.equals("{}") && !trimmed.equals("[]");
        } catch (Exception e) {
            return false;
        }
    }

    public boolean hasSetupIndicatorForFeature(String featureName) {
        String cardXpath = "//div[@data-testid='feature-list-scroll']"
                + "//div[starts-with(@data-testid,'feature-item-') and .//div[normalize-space()='" + featureName + "']]";
        try {
            WebElement card = driverTools.getDriver().findElement(By.xpath(cardXpath));
            if (!card.isDisplayed()) {
                return false;
            }
            String innerHtml = (String) ((JavascriptExecutor) driverTools.getDriver()).executeScript(
                    "return arguments[0].innerHTML;", card);
            if (innerHtml == null) {
                return false;
            }
            String lower = innerHtml.toLowerCase(Locale.ROOT);
            for (String marker : new String[]{"configured", "setup", "set up", "badge", "chip", "pill", "tag_", "label_"}) {
                if (lower.contains(marker)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public void cancelAddConfiguration() {
        try {
            driverTools.waitForElementToBeClickable(NEW_CONFIGURATION_CANCEL_BUTTON, 10);
            driverTools.clickWithJavascriptExecutor(NEW_CONFIGURATION_CANCEL_BUTTON);
            driverTools.dummyWait(1);
        } catch (Exception ignored) {
        }
    }

    public void clickMoreAction() {
        driverTools.waitForElementToBeClickable(MORE_ACTION_BUTTON, 10);
        driverTools.clickWithJavascriptExecutor(MORE_ACTION_BUTTON);
        driverTools.dummyWait(1);
    }

    public void clickConfigChangeHistoryMenuItem() {
        driverTools.waitForElementToBeClickable(CONFIG_HISTORY_MENU_ITEM, 10);
        driverTools.clickWithJavascriptExecutor(CONFIG_HISTORY_MENU_ITEM);
        driverTools.dummyWait(1);
    }

    public void clickSupportAgreementsMenuItem() {
        driverTools.waitForElementToBeClickable(SUPPORT_AGREEMENTS_MENU_ITEM, 10);
        driverTools.clickWithJavascriptExecutor(SUPPORT_AGREEMENTS_MENU_ITEM);
        driverTools.dummyWait(1);
    }

    public void clickActivateMenuItem() {
        driverTools.waitForElementToBeClickable(ACTIVATE_MENU_ITEM, 10);
        driverTools.clickWithJavascriptExecutor(ACTIVATE_MENU_ITEM);
        driverTools.dummyWait(1);
    }

    public boolean isMinimumFeaturesRequiredModalDisplayed() {
        try {
            driverTools.waitForElementToAppear(MINIMUM_FEATURES_MODAL_TITLE, 10);
            return driverTools.isElementDisplayed(MINIMUM_FEATURES_MODAL_TITLE);
        } catch (Exception e) {
            return false;
        }
    }

    public void clickAddFeaturesInMinFeaturesModal() {
        driverTools.waitForElementToBeClickable(MIN_FEATURES_ADD_FEATURES_BUTTON, 10);
        driverTools.clickWithJavascriptExecutor(MIN_FEATURES_ADD_FEATURES_BUTTON);
        driverTools.waitForElementToDisappear(MINIMUM_FEATURES_MODAL_TITLE, 10);
    }

    public void clickDeactivateMenuItem() {
        driverTools.waitForElementToBeClickable(DEACTIVATE_MENU_ITEM, 10);
        driverTools.clickWithJavascriptExecutor(DEACTIVATE_MENU_ITEM);
        driverTools.dummyWait(1);
    }

    public boolean isActivateModalDisplayed(String code) {
        try {
            Locator title = getActivateModalTitle(code);
            driverTools.waitForElementToAppear(title, 10);
            return driverTools.isElementDisplayed(title);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isDeactivateModalDisplayed(String code) {
        try {
            Locator title = getDeactivateModalTitle(code);
            driverTools.waitForElementToAppear(title, 10);
            return driverTools.isElementDisplayed(title);
        } catch (Exception e) {
            return false;
        }
    }

    public void clickYesActivate() {
        driverTools.waitForElementToBeClickable(ACTIVATE_YES_BUTTON, 10);
        driverTools.clickWithJavascriptExecutor(ACTIVATE_YES_BUTTON);
        driverTools.waitForElementToDisappear(ACTIVATE_YES_BUTTON, 10);
    }

    public void clickYesDeactivate() {
        driverTools.waitForElementToBeClickable(DEACTIVATE_YES_BUTTON, 10);
        driverTools.clickWithJavascriptExecutor(DEACTIVATE_YES_BUTTON);
        driverTools.waitForElementToDisappear(DEACTIVATE_YES_BUTTON, 10);
    }

    public void clickCancelStatusModal() {
        driverTools.waitForElementToBeClickable(STATUS_MODAL_CANCEL_BUTTON, 10);
        driverTools.clickWithJavascriptExecutor(STATUS_MODAL_CANCEL_BUTTON);
        driverTools.waitForElementToDisappear(STATUS_MODAL_CANCEL_BUTTON, 10);
    }

    public void clickEditFeatureButton(String featureName) {
        Locator btn = getFeatureListEditButton(featureName);
        driverTools.waitForElementToBeClickable(btn, 10);
        driverTools.clickWithJavascriptExecutor(btn);
        driverTools.dummyWait(1);
    }

    public boolean supportAgreementSectionContainsText(String sectionLabel, String text) {
        return getSupportAgreementSectionHtml(sectionLabel).contains(text);
    }

    public boolean isConfigChangeHistoryModalDisplayed() {
        try {
            driverTools.waitForElementToAppear(CONFIG_CHANGE_HISTORY_MODAL_TITLE, 10);
            return driverTools.isElementDisplayed(CONFIG_CHANGE_HISTORY_MODAL_TITLE);
        } catch (Exception e) {
            return false;
        }
    }

    public int getConfigChangeHistoryRowCount() {
        try {
            driverTools.waitForElementToAppear(CONFIG_CHANGE_HISTORY_ROWS, 10);
            return driverTools.getDriver().findElements(CONFIG_CHANGE_HISTORY_ROWS.by()).size();
        } catch (Exception e) {
            return 0;
        }
    }

    public void clickViewOnHistoryRow(int rowIndex) {
        Locator viewBtn = getConfigHistoryViewButton(rowIndex);
        driverTools.waitForElementToBeClickable(viewBtn, 10);
        driverTools.clickWithJavascriptExecutor(viewBtn);
        driverTools.dummyWait(1);
    }

    public void closeConfigChangeHistoryModal() {
        driverTools.waitForElementToBeClickable(CONFIG_CHANGE_HISTORY_CLOSE_BUTTON, 10);
        driverTools.clickWithJavascriptExecutor(CONFIG_CHANGE_HISTORY_CLOSE_BUTTON);
        driverTools.waitForElementToDisappear(CONFIG_CHANGE_HISTORY_MODAL_TITLE, 10);
    }

    public void closeValueDifferentModal() {
        try {
            driverTools.waitForElementToBeClickable(VALUE_DIFF_CLOSE_BUTTON, 5);
            driverTools.clickWithJavascriptExecutor(VALUE_DIFF_CLOSE_BUTTON);
            driverTools.waitForElementToDisappear(VALUE_DIFFERENT_MODAL_TITLE, 5);
        } catch (Exception ignored) { }
    }

    public boolean historyRowHasOnlyViewAction(int rowIndex) {
        List<WebElement> buttons = driverTools.getDriver().findElements(getHistoryRowAllButtons(rowIndex).by());
        if (buttons.isEmpty()) {
            return false;
        }
        for (WebElement btn : buttons) {
            try {
                if (!btn.isDisplayed()) {
                    continue;
                }
            } catch (Exception ignored) {
                continue;
            }
            String label = btn.getText() == null ? "" : btn.getText().trim().toLowerCase();
            if (label.isEmpty()) {
                continue;
            }
            if (!label.startsWith("view")) {
                return false;
            }
        }
        return true;
    }

    public void clickCancelOnEditConfiguration() {
        driverTools.waitForElementToBeClickable(EDIT_CONFIG_CANCEL_BUTTON, 10);
        driverTools.clickWithJavascriptExecutor(EDIT_CONFIG_CANCEL_BUTTON);
        driverTools.dummyWait(1);
    }

    public void confirmDiscardChangesIfPrompted() {
        try {
            List<WebElement> buttons = driverTools.getDriver().findElements(DISCARD_CHANGES_CONFIRM_BUTTON.by());
            for (WebElement btn : buttons) {
                try {
                    if (btn.isDisplayed()) {
                        driverTools.clickWithJavascriptExecutor(DISCARD_CHANGES_CONFIRM_BUTTON);
                        driverTools.dummyWait(1);
                        return;
                    }
                } catch (Exception ignored) { }
            }
        } catch (Exception ignored) { }
    }

    public boolean isValueDifferentModalStructureDisplayed() {
        try {
            driverTools.waitForElementToAppear(VALUE_DIFF_OLD_PANEL_HEADER, 10);
            driverTools.waitForElementToAppear(VALUE_DIFF_NEW_PANEL_HEADER, 10);
            driverTools.waitForElementToAppear(VALUE_DIFF_OLD_EDITOR_CONTENT, 10);
            driverTools.waitForElementToAppear(VALUE_DIFF_NEW_EDITOR_CONTENT, 10);
            return driverTools.isElementDisplayed(VALUE_DIFF_OLD_PANEL_HEADER)
                    && driverTools.isElementDisplayed(VALUE_DIFF_NEW_PANEL_HEADER);
        } catch (Exception e) {
            return false;
        }
    }

    public String getValueDifferentOldValue() {
        return readCodeMirrorContent(VALUE_DIFF_OLD_EDITOR_CONTENT);
    }

    public String getValueDifferentNewValue() {
        return readCodeMirrorContent(VALUE_DIFF_NEW_EDITOR_CONTENT);
    }

    private String readCodeMirrorContent(Locator content) {
        driverTools.waitForElementToAppear(content, 10);
        WebElement el = driverTools.getDriver().findElement(content.by());
        JavascriptExecutor js = (JavascriptExecutor) driverTools.getDriver();
        Object txt = js.executeScript("return arguments[0].innerText;", el);
        return txt == null ? "" : txt.toString();
    }

public static String normalizeJson(String s) {
    if (s == null || s.trim().isEmpty()) {
        return "";
    }
    try {
        // JSON object keys are unordered, so sort them recursively (array order is preserved).
        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper()
                .configure(com.fasterxml.jackson.databind.SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
        return mapper.writeValueAsString(mapper.readValue(s, Object.class));
    } catch (Exception e) {
        return s.replaceAll("\\s+", "");
    }
}

    public boolean valueDifferentPanelsMatch(String expectedOldJson, String expectedNewJson) {
        return normalizeJson(getValueDifferentOldValue()).equals(normalizeJson(expectedOldJson))
                && normalizeJson(getValueDifferentNewValue()).equals(normalizeJson(expectedNewJson));
    }

    public String getEditConfigurationSubtitle(String featureName) {
        Locator subtitle = getEditConfigurationSubtitleLocator(featureName);
        driverTools.waitForElementToAppear(subtitle, 10);
        return driverTools.getText(subtitle).orElse("").trim();
    }

    public static String getTodayDisplayDate() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, MMM dd, yyyy", Locale.ENGLISH));
    }

    public String getFeatureListDescription(String featureName) {
        driverTools.waitForElementToAppear(getFeatureListDescriptionCell(featureName), 10);
        return driverTools.getText(getFeatureListDescriptionCell(featureName)).orElse("").trim();
    }

    public String getFeatureListLastUpdatedDate(String featureName) {
        driverTools.waitForElementToAppear(getFeatureListLastUpdatedDateCell(featureName), 10);
        return driverTools.getText(getFeatureListLastUpdatedDateCell(featureName)).orElse("").trim();
    }

    public String getFeatureListLastUpdatedActor(String featureName) {
        driverTools.waitForElementToAppear(getFeatureListLastUpdatedActorCell(featureName), 10);
        return driverTools.getText(getFeatureListLastUpdatedActorCell(featureName)).orElse("").trim();
    }

    public String getHistoryFeatureName(int rowIndex) {
        return readHistoryCell(rowIndex, 2);
    }

    public String getHistoryStoreScope(int rowIndex) {
        return readHistoryCell(rowIndex, 3);
    }

    public String getHistoryBeforeValue(int rowIndex) {
        return readHistoryCell(rowIndex, 4);
    }

    public String getHistoryAfterValue(int rowIndex) {
        return readHistoryCell(rowIndex, 5);
    }

    public String getHistoryLatestEditedDate(int rowIndex) {
        return pollForNonEmptyText(getHistoryLatestEditedDateCell(rowIndex), 4, 5, 1);
    }

    public String getHistoryLatestEditedActor(int rowIndex) {
        return pollForNonEmptyText(getHistoryLatestEditedActorCell(rowIndex), 4, 5, 1);
    }

    private String readHistoryCell(int rowIndex, int colIndex) {
        Locator cell = getHistoryRowCell(rowIndex, colIndex);
        return pollForNonEmptyText(cell, 4, 5, 1);
    }

    public boolean isVendorTableDisplayed() {
        try {
            driverTools.waitForElementToAppear(TABLE_ROWS, 15);
            return !driverTools.getDriver().findElements(TABLE_ROWS.by()).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickViewFeatureButton(String featureName) {
        Locator btn = getFeatureListViewButton(featureName);
        driverTools.waitForElementToBeClickable(btn, 10);
        driverTools.clickWithJavascriptExecutor(btn);
        driverTools.dummyWait(1);
    }

    public boolean isViewConfigurationPageDisplayed(String featureName) {
        try {
            Locator title = getViewConfigurationTitle(featureName);
            driverTools.waitForElementToAppear(title, 15);
            return driverTools.isElementDisplayed(title);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isEditConfigSaveButtonAbsent() {
        return driverTools.getDriver().findElements(EDIT_CONFIG_SAVE_BUTTON.by()).isEmpty();
    }

    public boolean isJsonEditorValueNonEmpty() {
        String value = getJsonEditorValue();
        if (value == null) {
            return false;
        }
        String trimmed = value.trim();
        return trimmed.startsWith("{") && !trimmed.replaceAll("\\s", "").equals("{}");
    }

    public void navigateBack() {
        driverTools.getDriver().navigate().back();
        driverTools.dummyWait(2);
    }

    /**
     * Test-data cleanup for a vendor created during a test, meant to be called from a test's finally block.
     * Skips everything when the vendor was never created (e.g. the test failed before submitting it). Otherwise:
     * GET config values by vendor code, DELETE each one until none are left, DELETE the vendor, then GET the vendor
     * to confirm it no longer exists.
     * <p>
     * When the test passed, every cleanup check is a hard assertion so leftover data fails the run. When the test
     * already failed, cleanup is best-effort: failed checks and API errors are logged as warnings and the remaining
     * steps still run, so the original test failure is the one that gets reported.
     */
    public void cleanupVendorViaApi(String vendorCode, String username, boolean testPassed) {
        if (testPassed) {
            runVendorCleanup(vendorCode, username, true);
            return;
        }
        try {
            runVendorCleanup(vendorCode, username, false);
        } catch (Throwable cleanupFailure) {
            logger.softWarning(forReport("[Cleanup API] Cleanup of vendor '" + vendorCode + "' did not finish; the test already failed, so this is not reported as a failure: " + cleanupFailure));
        }
    }

    private void runVendorCleanup(String vendorCode, String username, boolean strict) {
        logger.step("[Cleanup API] Remove vendor '" + vendorCode + "' and its config values");
        logger.log("[Cleanup API] Check whether vendor '" + vendorCode + "' was created");
        if (isVendorDeletedViaApi(vendorCode, username)) {
            logger.log("[Cleanup API] Vendor '" + vendorCode + "' does not exist, nothing to clean up");
            return;
        }

        int maxVendorDeleteAttempts = 3;
        boolean vendorDeleted = false;
        for (int attempt = 1; attempt <= maxVendorDeleteAttempts && !vendorDeleted; attempt++) {
            deleteAllConfigValuesViaApi(vendorCode, username, strict);

            logger.log("[Cleanup API] DELETE vendor '" + vendorCode + "' (attempt " + attempt + "/" + maxVendorDeleteAttempts + ")");
            Response response = new DeleteUscVendorApi(new DeleteUscVendorApi.Inputs(vendorCode, username)).hitApiRaw();
            String body = response.getBody().asString();
            vendorDeleted = isSuccessfulSoftDelete(body, "code", vendorCode);
            if (!vendorDeleted) {
                boolean stillReferenced = isVendorStillReferencedByConfigs(body);
                boolean transientError = response.getStatusCode() >= 500;
                if (!stillReferenced && !transientError) {
                    break;
                }
                // The config-values list can already be empty while the vendor delete still sees the references.
                logger.log("[Cleanup API] Vendor '" + vendorCode + "' not deleted ("
                        + (stillReferenced ? "still referenced by config values" : "HTTP " + response.getStatusCode()) + "), retrying after 3s");
                driverTools.dummyWait(3);
            }
        }
        verifyCleanup(strict, "Cleanup: vendor '" + vendorCode + "' should be deleted (max " + maxVendorDeleteAttempts + " attempts)",
                vendorDeleted);

        logger.log("[Cleanup API] GET vendor '" + vendorCode + "' and verify it no longer exists");
        verifyCleanup(strict, "Cleanup: GET vendor '" + vendorCode + "' should return DATA_NOT_EXIST after deletion",
                isVendorDeletedViaApi(vendorCode, username));
    }

    /** Extent writes log text as HTML, so angle brackets in an API error must not reach the report. */
    private static String forReport(String text) {
        return text == null ? "" : text.replace("<", "[").replace(">", "]");
    }

    /** Hard assertion when strict; otherwise only a warning so cleanup carries on. */
    private void verifyCleanup(boolean strict, String reason, boolean passed) {
        if (strict) {
            Assertion.assertThat(reason, passed, is(true));
        } else if (!passed) {
            logger.softWarning(forReport("[Cleanup API] Check failed (test already failed, continuing cleanup): " + reason));
        }
    }

    /** Ids of the vendor's config values in the given store that are not soft-deleted yet (first 50, oldest first). */
    private List<String> getActiveConfigValueIdsViaApi(String vendorCode, String username, String storeId) {
        String body = new GetUscConfigValuesApi(new GetUscConfigValuesApi.Inputs(vendorCode, username, storeId)).hitApi(apiTools).getBody();
        org.json.JSONObject json = new org.json.JSONObject(body);
        List<String> ids = new ArrayList<>();
        org.json.JSONObject data = json.optJSONObject("data");
        org.json.JSONArray content = data == null ? null : data.optJSONArray("content");
        if (content == null) {
            return ids;
        }
        for (int i = 0; i < content.length(); i++) {
            org.json.JSONObject item = content.getJSONObject(i);
            org.json.JSONObject audit = item.optJSONObject("audit");
            if (audit == null || !audit.optBoolean("isDeleted", false)) {
                ids.add(item.getString("id"));
            }
        }
        return ids;
    }

    /** True when the DELETE response is SUCCESS and the config value is flagged as deleted. */
    private boolean deleteConfigValueViaApi(String configValueId, String username, String storeId) {
        String body = new DeleteUscConfigValueApi(new DeleteUscConfigValueApi.Inputs(configValueId, username, storeId)).hitApi(apiTools).getBody();
        return isSuccessfulSoftDelete(body, "id", configValueId);
    }

    /**
     * For each store (TIKETCOM, TIKETB2B): GET config values by vendor code and DELETE them until none are left,
     * then GET once more to verify the list is empty before the vendor itself is deleted.
     */
    private void deleteAllConfigValuesViaApi(String vendorCode, String username, boolean strict) {
        for (String storeId : CLEANUP_STORE_IDS) {
            logger.log("[Cleanup API] GET config values of vendor '" + vendorCode + "' (x-store-id: " + storeId + ")");
            List<String> configIds = getActiveConfigValueIdsViaApi(vendorCode, username, storeId);
            int maxRounds = 5;
            for (int round = 1; !configIds.isEmpty() && round <= maxRounds; round++) {
                logger.log("[Cleanup API] DELETE " + configIds.size() + " config value(s) of vendor '" + vendorCode + "' (x-store-id: " + storeId + ", round " + round + "): " + configIds);
                for (String configId : configIds) {
                    verifyCleanup(strict, "Cleanup: config value '" + configId + "' of vendor '" + vendorCode + "' (x-store-id: " + storeId + ") should be deleted",
                            deleteConfigValueViaApi(configId, username, storeId));
                }
                logger.log("[Cleanup API] GET config values of vendor '" + vendorCode + "' (x-store-id: " + storeId + ") again to verify none are left before deleting the vendor");
                configIds = getActiveConfigValueIdsViaApi(vendorCode, username, storeId);
            }
            verifyCleanup(strict, "Cleanup: vendor '" + vendorCode + "' should have no config values left in x-store-id " + storeId + " (max " + maxRounds + " delete rounds). remaining=" + configIds,
                    configIds.isEmpty());
        }
    }

    /** True when GET vendor by code answers code DATA_NOT_EXIST (HTTP 400 on this service), i.e. the vendor is absent. */
    private boolean isVendorDeletedViaApi(String vendorCode, String username) {
        String body = new GetUscVendorApi(new GetUscVendorApi.Inputs(vendorCode, username)).hitApiRaw().getBody().asString();
        try {
            return isDataNotExist(body);
        } catch (org.json.JSONException notJson) {
            return false;
        }
    }

    private static boolean isSuccessfulSoftDelete(String body, String keyField, String expectedKey) {
        try {
            org.json.JSONObject json = new org.json.JSONObject(body);
            org.json.JSONObject data = json.optJSONObject("data");
            org.json.JSONObject content = data == null ? null : data.optJSONObject("content");
            org.json.JSONObject audit = content == null ? null : content.optJSONObject("audit");
            return "SUCCESS".equals(json.optString("code"))
                    && expectedKey.equals(content == null ? null : content.optString(keyField))
                    && audit != null && audit.optBoolean("isDeleted", false);
        } catch (org.json.JSONException notJson) {
            return false;
        }
    }

    private static boolean isVendorStillReferencedByConfigs(String body) {
        try {
            org.json.JSONObject json = new org.json.JSONObject(body);
            return "BAD_REQUEST".equals(json.optString("code"))
                    && json.optString("message").toLowerCase().contains("config values still reference");
        } catch (org.json.JSONException notJson) {
            return false;
        }
    }

    private static boolean isDataNotExist(String body) {
        return "DATA_NOT_EXIST".equals(new org.json.JSONObject(body).optString("code"));
    }
}
