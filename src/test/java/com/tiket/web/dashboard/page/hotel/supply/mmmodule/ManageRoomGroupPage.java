package com.tiket.web.dashboard.page.hotel.supply.mmmodule;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ManageRoomGroupPage<T extends WebDriver> extends BasePage<T> {
    public ManageRoomGroupPage(Session<T> session) {
        super(session);
    }

    public static final String NAME_MODE_CUSTOM = "custom";
    public static final String NAME_MODE_SYSTEM = "system";
    private static final String DEFAULT_NUMBER_OF_BED = "1";

    // ---- Room group list page (outside the modal) ----
    // NOTE: the list/total/edit/delete DOM was not provided; these are best-effort and should be
    // verified against the live Manage Room Group list page.
    private static final Locator PAGE_TITLE_LABEL = new Locator("PAGE_TITLE_LABEL", new By.ByXPath("//div[contains(text(),'room Group')]"));
    private static final Locator TOTAL_ROOMS_LABEL = new Locator("TOTAL_ROOMS_LABEL", new By.ByXPath("//*[contains(normalize-space(text()),'Total Rooms') or contains(normalize-space(text()),'Total Room')]"));
    // Each room-group's room count is the badge shown in the room-group card title (e.g. the "100" badge for "All room Groups").
    private static final Locator ROOM_GROUP_ROOM_COUNT_CELL = new Locator("ROOM_GROUP_ROOM_COUNT_CELL", new By.ByXPath("//div[@id='room-group-card-title']//div[contains(@class,'room-group-card-badge')]"));
    private static final Locator CREATE_ROOM_GROUP_BUTTON = new Locator("CREATE_ROOM_GROUP_BUTTON", new By.ByXPath("//button[@data-testid=\"new-group-btn\"]"));
    // Snackbars render inside the SnackbarsPositioner container; a successful action shows a populated snackbar there.
    private static final Locator SUCCESSFUL_SNACKBAR = new Locator("SUCCESSFUL_SNACKBAR", new By.ByXPath("//div[contains(@class,'SnackbarsPositioner_snackbars_positioner')]//*[string-length(normalize-space(text()))>0] | //*[contains(translate(normalize-space(text()),'SUCCESS','success'),'success')]"));
    private static final Locator LOADING_INDICATOR_ICON = new Locator("LOADING_INDICATOR_ICON", new By.ByXPath("//div[contains(@class,'loading')]"));

    // ---- Room group list = left sidebar menu (ul#room-height-menu) ----
    private static final Locator SIDEBAR_MENU = new Locator("SIDEBAR_MENU", new By.ByXPath("//ul[@id='room-height-menu']"));

    // ---- Create / Edit room group modal (DOM-verified) ----
    private static final Locator ROOM_GROUP_FORM_MODAL = new Locator("ROOM_GROUP_FORM_MODAL", new By.ByXPath("//section[contains(@class,'Modal_modal_popup')][.//form and .//button[@data-testid='save-room-group-btn']]"));
    private static final Locator SAVE_ROOM_GROUP_BUTTON = new Locator("SAVE_ROOM_GROUP_BUTTON", new By.ByXPath("//button[@data-testid='save-room-group-btn']"));

    private static final Locator NAME_MODE_SYSTEM_RADIO = new Locator("NAME_MODE_SYSTEM_RADIO", new By.ByXPath("//label[.//input[@name='generateType' and contains(@value,'system')]]"));
    private static final Locator NAME_MODE_CUSTOM_RADIO = new Locator("NAME_MODE_CUSTOM_RADIO", new By.ByXPath("//label[.//input[@name='generateType' and contains(@value,'custom')]]"));
    private static final Locator GENERATE_NAME_BUTTON = new Locator("GENERATE_NAME_BUTTON", new By.ByXPath("//button[@data-testid='btn-generate-room-name']"));
    private static final Locator GENERATED_NAME_LABEL = new Locator("GENERATED_NAME_LABEL", new By.ByXPath("//div[contains(@id,'generatedGroupName')]//span[contains(text(),'EN')]"));
    // NOTE: the custom-name input only renders after picking the "Custom name" radio (not in the
    // captured DOM); locator is tolerant and may need adjustment once that state is inspected.
    private static final Locator CUSTOM_NAME_INPUT_BAHASA = new Locator("CUSTOM_NAME_INPUT", new By.ByXPath("//input[@id='type-a-custom-name-in-indonesia-optional']"));
    private static final Locator CUSTOM_NAME_INPUT_ENGLISH = new Locator("CUSTOM_NAME_INPUT", new By.ByXPath("//input[@id='type-a-custom-name-in-english-optional']"));
    private static final Locator BED_TYPE_NO_INFO_RADIO = new Locator("BED_TYPE_NO_INFO_RADIO", new By.ByXPath("//label[.//input[@name='bedTypeFields' and contains(@value,'no_information')]]"));
    private static final Locator BED_TYPE_CUSTOM_RADIO = new Locator("BED_TYPE_CUSTOM_RADIO", new By.ByXPath("//label[.//input[@name='bedTypeFields' and contains(@value,'custom_value')]]"));
    private static final Locator BED_TYPE_INPUT = new Locator("BED_TYPE_INPUT", new By.ByXPath("//input[@name='bedType__input']"));

    private static final Locator ROOM_TYPE_NO_INFO_RADIO = new Locator("ROOM_TYPE_NO_INFO_RADIO", new By.ByXPath("//label[.//input[@name='roomTypeFields' and contains(@value,'no_information')]]"));
    private static final Locator ROOM_TYPE_CUSTOM_RADIO = new Locator("ROOM_TYPE_CUSTOM_RADIO", new By.ByXPath("//label[.//input[@name='roomTypeFields' and contains(@value,'custom_value')]]"));
    private static final Locator ROOM_TYPE_INPUT = new Locator("ROOM_TYPE_INPUT", new By.ByXPath("//input[@name='roomType__input']"));

    private static final Locator ROOM_SIZE_NO_INFO_RADIO = new Locator("ROOM_SIZE_NO_INFO_RADIO", new By.ByXPath("//label[.//input[@name='roomSizeFields' and contains(@value,'no_information')]]"));
    private static final Locator ROOM_SIZE_CUSTOM_RADIO = new Locator("ROOM_SIZE_CUSTOM_RADIO", new By.ByXPath("//label[.//input[@name='roomSizeFields' and contains(@value,'custom_value')]]"));
    private static final Locator ROOM_SIZE_INPUT = new Locator("ROOM_SIZE_INPUT", new By.ByXPath("//input[@name='roomSize']"));

    private static final Locator ROOM_GROUP_FACILITIES_NO_INFO_RADIO = new Locator("ROOM_GROUP_FACILITIES_NO_INFO_RADIO", new By.ByXPath("//label[.//input[@name='roomGroupFacilitiesFields' and contains(@value,'no_information')]]"));
    private static final Locator ROOM_GROUP_FACILITIES_CUSTOM_RADIO = new Locator("ROOM_GROUP_FACILITIES_CUSTOM_RADIO", new By.ByXPath("//label[.//input[@name='roomGroupFacilitiesFields' and contains(@value,'custom_value')]]"));
    private static final Locator ROOM_GROUP_FACILITIES_INPUT = new Locator("ROOM_GROUP_FACILITIES_INPUT", new By.ByXPath("//input[@name='roomGroupFacility__input']"));

    private static final Locator NUMBER_OF_BED_INPUT = new Locator("NUMBER_OF_BED_INPUT", new By.ByXPath("//input[@id='no-of-bedroom']"));

    // Attribute dropdowns open a "Select Single Attribute" modal (radio options), not an inline list.
    private static final Locator SELECT_ATTRIBUTE_MODAL = new Locator("SELECT_ATTRIBUTE_MODAL", new By.ByXPath("//section[contains(@class,'Modal_modal_popup_small')][.//h2[normalize-space()='Select Single Attribute']]"));

    // ---- Delete confirmation modal ("This empty group will be deleted" -> primary "Agree" button) ----
    private static final Locator DELETE_CONFIRM_MODAL = new Locator("DELETE_CONFIRM_MODAL", new By.ByXPath("//section[contains(@class,'Modal_modal_popup')][.//h2[contains(normalize-space(.),'will be deleted')] or .//footer//button[normalize-space(.)='Agree']]"));
    private static final Locator DELETE_CONFIRM_BUTTON = new Locator("DELETE_CONFIRM_BUTTON", new By.ByXPath("//section[contains(@class,'Modal_modal_popup')]//footer//button[normalize-space(.)='Agree' or normalize-space(.)='Delete' or contains(@class,'Button_variant_primary')]"));
    private static final Locator CONFIRM_BUTTON = new Locator("CONFIRM_BUTTON", new By.ByXPath("(//button[normalize-space(.)='Confirm' or contains(@class,'Button_variant_primary')])[last()]"));
    private static final Locator SAVE_FACILITIES = new Locator("SAVE_FACILITIES", new By.ByXPath("(//button[normalize-space(.)='Save' or contains(@class,'Button_variant_primary')])[last()]"));
    private static final Locator NEW_GROUP_RADIO = new Locator("NEW_GROUP_RADIO", new By.ByXPath("//span[contains(text(),'New Group')]"));
    private Locator getAttributeOption(String value) {
        return new Locator("ATTRIBUTE_OPTION", new By.ByXPath(String.format("//span[contains(@class,'children') and normalize-space(text())=%s]", CoreFunctions.toXPathLiteral(value))));
    }

    private static Locator getAttributeOption2(String value) {
        return new Locator("ATTRIBUTE_OPTION", new By.ByXPath(String.format("(//span[contains(@class,'children')]//*[text()=%s])[last()]", CoreFunctions.toXPathLiteral(value))));

    }

    /** Sidebar list item (the room-group list) matched by its visible name. */
    private static Locator getRoomGroupRowByName(String name) {
        return new Locator("ROOM_GROUP_ROW_BY_NAME", new By.ByXPath(String.format(
                "//ul[@id='room-height-menu']/li[starts-with(@id,'sidebar-') and @id!='sidebar-all' and @id!='sidebar-others']//a[contains(normalize-space(.),%s)]",
                CoreFunctions.toXPathLiteral(name))));
    }

    /** Sidebar anchor for the given RG name — exact match on the anchor's normalized text. */
    private static Locator getRoomGroupSidebarRowByExactName(String name) {
        return new Locator("ROOM_GROUP_SIDEBAR_ROW_EXACT", new By.ByXPath(String.format(
                "//ul[@id='room-height-menu']/li[starts-with(@id,'sidebar-') and @id!='sidebar-all' and @id!='sidebar-others']//a[normalize-space(.)=%1$s or text()=%1$s]",
                CoreFunctions.toXPathLiteral(name))));
    }

    /** Room-group card in the main content whose title matches the given name. */
    private static Locator getRoomGroupCardByName(String name) {
        return new Locator("ROOM_GROUP_CARD_BY_NAME", new By.ByXPath(String.format(
                "//div[contains(@class,'room-group-card')]//div[contains(@data-testid,'room-group-card-title')][text()=%s]",
                CoreFunctions.toXPathLiteral(name))));
    }

    /** Room-group card in the main content whose title matches the given name after whitespace normalization. */
    private static Locator getRoomGroupCardByExactName(String name) {
        return new Locator("ROOM_GROUP_CARD_BY_EXACT_NAME", new By.ByXPath(String.format(
                "//div[contains(@class,'room-group-card')]//div[contains(@data-testid,'room-group-card-title')][normalize-space(.)=%1$s or text()=%1$s]",
                CoreFunctions.toXPathLiteral(name))));
    }

    /** Room count badge inside the room-group card title for the given name. */
    private static Locator getRoomGroupRoomCountCellByName(String name) {
        return new Locator("ROOM_GROUP_ROOM_COUNT_BY_NAME", new By.ByXPath(String.format(
                "//div[@id='room-group-card-title'][.//div[contains(@class,'room-grouping-table-title') and normalize-space(.)=%s]]//div[contains(@class,'room-group-card-badge')]",
                CoreFunctions.toXPathLiteral(name))));
    }

    private static Locator getEditRoomGroupButtonByName(String name) {
        return new Locator("EDIT_ROOM_GROUP_BUTTON", new By.ByXPath(String.format(
                "//div[@id='room-group-card-title'][.//div[contains(@class,'room-grouping-table-title') and normalize-space(.)=%s]]//div[contains(@class,'justify-end')]//button[contains(@data-testid,'edit') or contains(@aria-label,'edit') or contains(@title,'Edit')]",
                CoreFunctions.toXPathLiteral(name))));
    }

    /**
     * Delete action inside the matching room-group card's title actions area. The delete button has no
     * data-testid (unlike the edit button); it is identified by its delete SVG icon, with a fallback to
     * the action button that is not the edit button.
     */
    private static Locator getDeleteRoomGroupButtonByName(String name) {
        return new Locator("DELETE_ROOM_GROUP_BUTTON", new By.ByXPath(String.format(
                "//div[@id='room-group-card-title'][.//div[contains(@class,'room-grouping-table-title') and contains(normalize-space(.),%s)]]//div[contains(@class,'justify-end')]//button[.//*[contains(@id,'delete')] or contains(@data-testid,'delete') or (not(contains(@data-testid,'edit')) and not(.//*[contains(@id,'edit')]))]",
                CoreFunctions.toXPathLiteral(name))));
    }

    // ---- Move room / "Others" section / Organize by system ----
    // Single-room move (floating hover button), multi-select (row checkboxes + header Move button),
    // and the "Move room to" popup are anchored on verified DOM. The "Organize rooms by system"
    // snackbar/countdown locators remain best-effort and should be tightened against the live page.

    /** The "Others" sidebar entry holds rooms that are not assigned to any room group. */
    private static final String OTHERS_GROUP_TITLE = "Others";
    private static final Locator SIDEBAR_OTHERS = new Locator("SIDEBAR_OTHERS", new By.ByXPath("//ul[@id='room-height-menu']/li[@id='sidebar-others']//a"));

    // Floating "Move" button: appears (wrapper toggles off aria-hidden) when a room row is hovered,
    // and also acts as the trigger after selecting room checkboxes. Anchored on the visible "Move"
    // label + the move.svg icon (stable), and excluded while its wrapper is still aria-hidden (the
    // inactive state) so the wait blocks until the button is actually live. Click via JS executor
    // because the wrapper keeps inline display:flex even while logically hidden.
    private static final Locator FLOATING_MOVE_BUTTON = new Locator("FLOATING_MOVE_BUTTON", new By.ByXPath("//button[     .//span[normalize-space()='Move']     and     .//img[contains(@src,'move')]     and     not(ancestor::*[contains(@style,'z-index: -1')]) ]"));

    // "Move room to" floating popup (a div.fixed, NOT a section.Modal_modal_popup). It holds the
    // move_type radios ("Another Group" = existing), a readonly dropdown of target groups, and a
    // Confirm button that stays disabled until a target is selected.
    private static final Locator MOVE_POPUP = new Locator("MOVE_POPUP", new By.ByXPath("//div[contains(@class,'fixed')][.//input[@name='move_type'] and .//button[normalize-space(.)='Confirm']]"));
    private static final Locator MOVE_BUTTON = new Locator("MOVE_BUTTON", new By.ByXPath("//button[@data-testid='test-id-move-multiple-room-button']"));
    // Radio options. "existing" -> move into another existing group; the counterpart selects a new group.
    private static final Locator MOVE_POPUP_EXISTING_RADIO = new Locator("MOVE_POPUP_EXISTING_RADIO", new By.ByXPath("//div[contains(@class,'fixed')]//label[.//input[@name='move_type' and contains(@value,'existing')]]"));
    private static final Locator MOVE_POPUP_NEW_RADIO = new Locator("MOVE_POPUP_NEW_RADIO", new By.ByXPath("//div[contains(@class,'fixed')]//label[.//input[@name='move_type' and contains(@value,'new')]]"));
    // The readonly group selector input (id flips between select-another-room-group and move-room-to).
    private static final Locator MOVE_POPUP_GROUP_INPUT = new Locator("MOVE_POPUP_GROUP_INPUT", new By.ByXPath("//input[(@name='targetGroupingKey' or @name='room')and (@id='select-another-room-group' or @id='move-room-to')]"));
    // Selecting "New Group" auto-generates the name (no input), shown via an info banner; just confirm.
    private static final Locator MOVE_POPUP_CONFIRM_BUTTON = new Locator("MOVE_POPUP_CONFIRM_BUTTON", new By.ByXPath("//div[contains(@class,'fixed')][.//input[@name='move_type']]//button[normalize-space(.)='Confirm']"));

    // Confirmation modal shown when the source group becomes empty after the move ("... will be deleted").
    private static final Locator MOVE_DELETE_PREVIOUS_GROUP_MODAL = new Locator("MOVE_DELETE_PREVIOUS_GROUP_MODAL", new By.ByXPath("//section[contains(@class,'Modal_modal_popup')][.//h2[contains(normalize-space(.),'will be deleted')]]"));
    private static final Locator MOVE_DELETE_PREVIOUS_GROUP_AGREE_BUTTON = new Locator("MOVE_DELETE_PREVIOUS_GROUP_AGREE_BUTTON", new By.ByXPath("//section[contains(@class,'Modal_modal_popup')]//footer//button[normalize-space(.)='Agree']"));

    // Dedicated header bulk-move button (separate from the floating hover button); disabled until
    // at least one room checkbox is ticked.
    private static final Locator MOVE_MULTIPLE_ROOMS_BUTTON = new Locator("MOVE_MULTIPLE_ROOMS_BUTTON", new By.ByXPath("//button[@data-testid='test-id-move-multiple-room']"));

    // Organize rooms by system -> success snackbar + countdown timer.
    private static final Locator ORGANIZE_BY_SYSTEM_BUTTON = new Locator("ORGANIZE_BY_SYSTEM_BUTTON", new By.ByXPath("//button[contains(translate(normalize-space(.),'ORGANIZE','organize'),'organize rooms by system')]"));
    private static final Locator COUNTDOWN_TIMER = new Locator("COUNTDOWN_TIMER", new By.ByXPath("//span[contains(.,'Room organizing process will be completed in')]/span"));

    // Relative locators resolved inside a single room row (use with element(Locator, WebElement)).
    // The room name is the 2nd cell; the 1st cell is the leading row number ("1.", "2.", ...).
    private static final Locator ROOM_NAME_CELL_IN_ROW = new Locator("ROOM_NAME_CELL_IN_ROW", new By.ByXPath(".//td[2]"));
    private static final Locator ROOM_CHECKBOX_IN_ROW = new Locator("ROOM_CHECKBOX_IN_ROW", new By.ByXPath(".//input[@data-testid='room-group-card-checkbox']"));

    // Sidebar group entries (excludes the "All" and "Others" pseudo-entries), used to count room groups.
    private static final Locator SIDEBAR_GROUP_ITEMS = new Locator("SIDEBAR_GROUP_ITEMS", new By.ByXPath("//ul[@id='room-height-menu']/li[starts-with(@id,'sidebar-') and @id!='sidebar-all' and @id!='sidebar-others']"));

    private static final Locator FILTER_TRIGGER = new Locator("FILTER_TRIGGER", new By.ByCssSelector("[data-testid='room-group-filter-trigger']"));
    private static final Locator FILTER_OPTION_ALL = new Locator("FILTER_OPTION_ALL", new By.ByXPath("//button[normalize-space(.)='All'][following-sibling::button[@data-testid='room-group-filter-verified']]"));
    private static final Locator FILTER_OPTION_VERIFIED = new Locator("FILTER_OPTION_VERIFIED", new By.ByCssSelector("[data-testid='room-group-filter-verified']"));
    private static final Locator FILTER_OPTION_UNVERIFIED = new Locator("FILTER_OPTION_UNVERIFIED", new By.ByCssSelector("[data-testid='room-group-filter-unverified']"));
    private static final Locator FILTER_OPTION_EMPTY = new Locator("FILTER_OPTION_EMPTY", new By.ByCssSelector("[data-testid='room-group-filter-empty-group']"));
    private static final Locator FILTER_OPTION_WITH_ROOMS = new Locator("FILTER_OPTION_WITH_ROOMS", new By.ByCssSelector("[data-testid='room-group-filter-with-rooms']"));
    private static final Locator FILTER_EMPTY_VIEW = new Locator("FILTER_EMPTY_VIEW", new By.ByCssSelector("[data-testid='room-group-filter-empty-view']"));

    private static final Locator EDIT_VERIFICATION_BUTTON = new Locator("EDIT_VERIFICATION_BUTTON", new By.ByCssSelector("[data-testid='room-group-card-edit-verification']"));
    private static final Locator CANCEL_VERIFICATION_BUTTON = new Locator("CANCEL_VERIFICATION_BUTTON", new By.ByCssSelector("[data-testid='room-group-card-cancel-verification']"));
    private static final Locator VERIFY_BUTTON = new Locator("VERIFY_BUTTON", new By.ByCssSelector("[data-testid='room-group-card-verify-button']"));
    private static final Locator UNVERIFY_BUTTON = new Locator("UNVERIFY_BUTTON", new By.ByCssSelector("[data-testid='room-group-card-unverify-button']"));
    private static final Locator SELECT_ALL_VERIFY_CHECKBOX = new Locator("SELECT_ALL_VERIFY_CHECKBOX", new By.ByXPath("//*[@data-testid='room-group-card-verify-checkbox-all']/following-sibling::span[1]"));
    private static final Locator VERIFY_CHECKBOX_IN_ROW = new Locator("VERIFY_CHECKBOX_IN_ROW", new By.ByXPath(".//input[@data-testid='room-group-card-verify-checkbox']"));
    private static final Locator SELECTION_COUNT = new Locator("SELECTION_COUNT", new By.ByXPath("//span[contains(normalize-space(.),'room selected') or contains(normalize-space(.),'rooms selected')]"));

    private static final Locator VERIFY_CONFIRM_MODAL = new Locator("VERIFY_CONFIRM_MODAL", new By.ByCssSelector("[data-testid='modal-verify-rooms-confirm']"));
    private static final Locator VERIFY_CONFIRM_PRIMARY = new Locator("VERIFY_CONFIRM_PRIMARY", new By.ByXPath("//div[@data-testid='modal-verify-rooms-confirm']//button[normalize-space(.)='Verify']"));
    private static final Locator VERIFY_CONFIRM_CANCEL = new Locator("VERIFY_CONFIRM_CANCEL", new By.ByXPath("//div[@data-testid='modal-verify-rooms-confirm']//button[normalize-space(.)='Cancel']"));
    private static final Locator UNVERIFY_CONFIRM_MODAL = new Locator("UNVERIFY_CONFIRM_MODAL", new By.ByCssSelector("[data-testid='modal-verify-rooms-confirm']"));
    private static final Locator UNVERIFY_CONFIRM_PRIMARY = new Locator("UNVERIFY_CONFIRM_PRIMARY", new By.ByXPath("//div[@data-testid='modal-verify-rooms-confirm']//button[normalize-space(.)='Unverify']"));
    private static final Locator UNVERIFY_CONFIRM_CANCEL = new Locator("UNVERIFY_CONFIRM_CANCEL", new By.ByXPath("//div[@data-testid='modal-verify-rooms-confirm']//button[normalize-space(.)='Cancel']"));

    private static final Locator VERIFY_SUCCESS_SNACKBAR = new Locator("VERIFY_SUCCESS_SNACKBAR", new By.ByXPath("//div[contains(@class,'SnackbarsPositioner')]//*[contains(text(),'Successfully verified')]"));
    private static final Locator UNVERIFY_SUCCESS_SNACKBAR = new Locator("UNVERIFY_SUCCESS_SNACKBAR", new By.ByXPath("//div[contains(@class,'SnackbarsPositioner')]//*[contains(text(),'Successfully unverified')]"));

    private static final String ROW_VERIFIED_FILL = "#00aa5b";
    private static final String ROW_UNVERIFIED_FILL = "#d8dce8";

    /** Room-count badge of the room-group card whose title contains the given text (first match). */
    private static Locator getRoomCountBadgeByTitle(String groupTitle) {
        return new Locator("ROOM_COUNT_BADGE_BY_TITLE", new By.ByXPath(String.format(
                "(//div[@id='room-group-card-title'][.//div[contains(@class,'room-grouping-table-title') and contains(normalize-space(.),%s)]]//div[contains(@class,'room-group-card-badge')])[1]",
                CoreFunctions.toXPathLiteral(groupTitle))));
    }

    /** All room rows shown inside the given room-group card. */
    private static Locator getAllRoomRowsInGroup(String groupTitle) {
        return new Locator("ALL_ROOM_ROWS_IN_GROUP", new By.ByXPath(String.format(
                "//div[contains(@data-testid,'room-group-card-title')][normalize-space(.)=%1$s or text()=%1$s]//ancestor::div[contains(@class,'room-group-card')]//tr[@data-testid='tr-table-body']",
                CoreFunctions.toXPathLiteral(groupTitle))));
    }

    /** "Edit verification" button scoped to the given RG card. */
    private static Locator getEditVerificationButtonByName(String groupTitle) {
        return new Locator("EDIT_VERIFICATION_BUTTON_BY_NAME", new By.ByXPath(String.format(
                "//div[contains(@data-testid,'room-group-card-title')][normalize-space(.)=%1$s or text()=%1$s]//ancestor::div[contains(@class,'room-group-card')]//button[@data-testid='room-group-card-edit-verification']",
                CoreFunctions.toXPathLiteral(groupTitle))));
    }

    /** RG-level "Verified" text indicator scoped by RG name. */
    private static Locator getRoomGroupVerifiedLabelByName(String groupTitle) {
        return new Locator("RG_VERIFIED_LABEL_BY_NAME", new By.ByXPath(String.format(
                "//div[contains(@class,'room-group-card')]//div[contains(@data-testid,'room-group-card-title')][normalize-space(.)=%1$s or text()=%1$s]//*[text()='Verified']",
                CoreFunctions.toXPathLiteral(groupTitle))));
    }

    private static Locator getRoomGroupVerifiedIconByName(String groupTitle) {
        return new Locator("RG_VERIFIED_ICON_BY_NAME", new By.ByXPath(String.format(
                "//div[contains(@data-testid,'room-group-card-title')][normalize-space(.)=%1$s or text()=%1$s]//*[@fill=%2$s]",
                CoreFunctions.toXPathLiteral(groupTitle), CoreFunctions.toXPathLiteral(ROW_VERIFIED_FILL))));
    }

    /** Room row inside the group card at position {@code index} (1-based), regardless of vendor. */
    private static Locator getNthRoomRowInGroup(String groupTitle, int index) {
        return new Locator("NTH_ROOM_ROW_IN_GROUP", new By.ByXPath(String.format(
                "(//div[contains(@data-testid,'room-group-card-title')][normalize-space(.)=%1$s or text()=%1$s]//ancestor::div[contains(@class,'room-group-card')]//tr[@data-testid='tr-table-body'])[%2$d]",
                CoreFunctions.toXPathLiteral(groupTitle), index)));
    }

    /** All room rows shown inside the room-group card whose title contains the given text. */
    private static Locator getRoomRowsInGroup(String groupTitle,String vendorName,String vendorId) {
        String val = vendorName.toUpperCase() + " - " + vendorId;
        return new Locator("ROOM_ROWS_IN_GROUP", new By.ByXPath(String.format(
                "//div[contains(@class,'room-group-card')] [.//div[contains(@class,'room-grouping-table-title') and normalize-space()=%s]] //tr[@data-testid='tr-table-body' and .//td[contains(.,%s)]]",
                CoreFunctions.toXPathLiteral(groupTitle),CoreFunctions.toXPathLiteral(val))));
    }

    /** The room row inside the given group card that contains the given room name. */
    private static Locator getRoomRowInGroup(String groupTitle, String roomName) {
        return new Locator("ROOM_ROW_IN_GROUP", new By.ByXPath(String.format(
                "//div[contains(@class,'room-group-card')][.//div[contains(@class,'room-grouping-table-title') and contains(normalize-space(.),%s)]]//tr[@data-testid='tr-table-body'][.//td[contains(normalize-space(.),%s)]]",
                CoreFunctions.toXPathLiteral(groupTitle), CoreFunctions.toXPathLiteral(roomName))));
    }

    /** A target room group option in the move popup dropdown, matched by its exact data-label. */
    private static Locator getMovePopupGroupOption(String name) {
        return new Locator("MOVE_POPUP_GROUP_OPTION", new By.ByXPath(String.format("(//li[(@data-name='targetGroupingKey' or @data-name='room') and @data-label=%s][not(contains(@class,'pointer-events-none'))])[1]", CoreFunctions.toXPathLiteral(name))));
    }

    // ---- Page-level actions ----

    public boolean isManageRoomGroupPageLoaded() {
        waitForLoading();
        return driverTools.waitForElementToPresence(PAGE_TITLE_LABEL, 30).isPresent();
    }

    public int getTotalRoomsFromLabel() {
        WebElement label = driverTools.waitForElementToPresence(TOTAL_ROOMS_LABEL, 30).orElseThrow();
        return CoreFunctions.getIntFromString(driverTools.getText(label)).orElse(-1);
    }

    public int getTotalRoomsFromRoomGroupList() {
        List<WebElement> cells = driverTools.waitForElementsToAppear(ROOM_GROUP_ROOM_COUNT_CELL, 30).orElseThrow();
        int total = 0;
        for (WebElement cell : cells) {
            total += CoreFunctions.getIntFromString(driverTools.getText(cell)).orElse(0);
        }
        return total;
    }

    public void clickCreateRoomGroup() {
        WebElement button = driverTools.waitForElementToBeClickable(CREATE_ROOM_GROUP_BUTTON, 20).orElseThrow();
        driverTools.clickWithJavascriptExecutor(button);
    }

    public boolean isRoomGroupFormDisplayed() {
        return driverTools.waitForElementToAppear(ROOM_GROUP_FORM_MODAL, 20).isPresent();
    }

    public void clickEditRoomGroupByName(String name) {
        driverTools.refreshWebpage();
        waitForLoading();
        selectRoomGroupInSidebar(name);
        WebElement button = driverTools.waitForElementToBeClickable(getEditRoomGroupButtonByName(name), 20).orElseThrow();
        driverTools.clickWithJavascriptExecutor(button);
    }

    public void waitForLoading(){
        driverTools.waitForElementToAppear(LOADING_INDICATOR_ICON, 5);
        driverTools.waitForElementToDisappear(LOADING_INDICATOR_ICON, 60);
    }

    /** Reloads the page and waits for the room-group data to finish loading. Call before reading
     *  room counts / group counts so the assertions run against freshly fetched data. */
    public void refreshPage() {
        driverTools.refreshWebpage();
        waitForLoading();
    }

    /**
     * Fills the create/edit room-group form. Each attribute is gated by a radio: a blank value selects
     * "No Info available"; a non-blank value selects the "custom value" radio and sets the field.
     * Returns the effective room-group name to validate against the list: the custom name typed in, or
     * the system-generated name read after clicking "Click to Generate".
     */
    public String fillRoomGroupForm(String nameMode, String customName, String bedType, String roomType, String roomSize, String roomFacilities) {
        setBedType(bedType);
        setRoomType(roomType);
        setRoomSize(roomSize);
        setRoomGroupFacilities(roomFacilities);
        setNumberOfBed(DEFAULT_NUMBER_OF_BED);

        if (NAME_MODE_SYSTEM.equalsIgnoreCase(nameMode)) {
            clickRadio(NAME_MODE_SYSTEM_RADIO);
            return generateRoomGroupName();
        }
        clickRadio(NAME_MODE_CUSTOM_RADIO);
        inputCustomRoomGroupName(customName);
        return customName;
    }

    private void setBedType(String value) {
        if (isBlank(value)) {
            clickRadio(BED_TYPE_NO_INFO_RADIO);
            return;
        }
        clickRadio(BED_TYPE_CUSTOM_RADIO);
        selectSingleAttribute(BED_TYPE_INPUT, value);
        driverTools.waitForElementToAppear(SELECT_ATTRIBUTE_MODAL, 2);
        driverTools.clickWithJavascriptExecutor(CONFIRM_BUTTON);
    }

    private void setRoomType(String value) {
        if (isBlank(value)) {
            clickRadio(ROOM_TYPE_NO_INFO_RADIO);
            return;
        }
        clickRadio(ROOM_TYPE_CUSTOM_RADIO);
        selectSingleAttribute(ROOM_TYPE_INPUT, value);
    }

    /** Sets the required "No. of Bed" attribute (defaults to 1 so the room group can be saved). */
    public void setNumberOfBed(String value) {
        selectSingleAttribute(NUMBER_OF_BED_INPUT, isBlank(value) ? DEFAULT_NUMBER_OF_BED : value);
    }

    private void setRoomSize(String value) {
        if (isBlank(value)) {
            clickRadio(ROOM_SIZE_NO_INFO_RADIO);
            return;
        }
        clickRadio(ROOM_SIZE_CUSTOM_RADIO);
        WebElement input = driverTools.waitForElementToBeClickable(ROOM_SIZE_INPUT, 10).orElseThrow();
        driverTools.clear(input);
        driverTools.sendKeys(input, value);
    }

    private void setRoomGroupFacilities(String value) {
        if (isBlank(value)) {
            clickRadio(ROOM_GROUP_FACILITIES_NO_INFO_RADIO);
            return;
        }
        clickRadio(ROOM_GROUP_FACILITIES_CUSTOM_RADIO);
        selectSingleAttribute2(ROOM_GROUP_FACILITIES_INPUT, value);
        driverTools.clickWithJavascriptExecutor(SAVE_FACILITIES);
    }

    private void clickRadio(Locator radioLabelLocator) {
        WebElement radio = driverTools.waitForElementToBeClickable(radioLabelLocator, 10).orElseThrow();
        try {
            driverTools.click(radio);
        } catch (Exception e) {
            driverTools.clickWithJavascriptExecutor(radio);
        }
    }

    private void selectSingleAttribute(Locator inputLocator, String value) {
        WebElement input = driverTools.waitForElementToBeClickable(inputLocator, 10).orElseThrow();
        driverTools.clickWithJavascriptExecutor(input);
        WebElement option = driverTools.waitForElementToBeClickable(getAttributeOption(value), 10).orElseThrow();
        driverTools.clickWithJavascriptExecutor(option);
        driverTools.waitForElementToDisappear(SELECT_ATTRIBUTE_MODAL, 10);
    }

    private void selectSingleAttribute2(Locator inputLocator, String value) {
        WebElement input = driverTools.waitForElementToBeClickable(inputLocator, 10).orElseThrow();
        driverTools.clickWithJavascriptExecutor(input);
        WebElement option = driverTools.waitForElementToBeClickable(getAttributeOption2(value), 10).orElseThrow();
        driverTools.clickWithJavascriptExecutor(option);
        driverTools.waitForElementToDisappear(SELECT_ATTRIBUTE_MODAL, 10);
    }

    private void inputCustomRoomGroupName(String name) {
        WebElement input = driverTools.waitForElementToBeClickable(CUSTOM_NAME_INPUT_BAHASA, 10).orElseThrow();
        driverTools.clear(input);
        driverTools.sendKeys(input, name);
        input = driverTools.waitForElementToBeClickable(CUSTOM_NAME_INPUT_ENGLISH, 10).orElseThrow();
        driverTools.clearInput(input);
        driverTools.sendKeys(input, name);

    }

    private String generateRoomGroupName() {
        if(!driverTools.waitForElementToAppear(GENERATE_NAME_BUTTON).isPresent()){
            submitRoomGroup();
        }
        driverTools.clickWithJavascriptExecutor(GENERATE_NAME_BUTTON);
        WebElement nameLabel = driverTools.waitForElementToPresence(GENERATED_NAME_LABEL, 10).orElseThrow();
        for (int attempt = 0; attempt < 10; attempt++) {
            String text = driverTools.getText(nameLabel).trim();
            text = text.replace("EN:", "").trim(); // remove the "EN" suffix
            if (!text.isEmpty() && !text.toLowerCase().contains("generate a name")) {
                return text;
            }
            driverTools.dummyWait(1);
        }
        return driverTools.getText(nameLabel).trim();
    }

    public void submitRoomGroup() {
        WebElement button = driverTools.waitForElementToBeClickable(SAVE_ROOM_GROUP_BUTTON, 20).orElseThrow();
        driverTools.clickWithJavascriptExecutor(button);
    }

    public boolean isSuccessfulSnackbarAppeared() {
        return driverTools.waitForElementToAppear(SUCCESSFUL_SNACKBAR, 30).isPresent();
    }

    public boolean isRoomGroupDisplayedInList(String name) {
        boolean result = driverTools.waitForElementToPresence(getRoomGroupRowByName(name), 30).isPresent();
        waitForLoading();
        return  result;
    }

    public boolean isRoomGroupNotDisplayedInList(String name) {
        return driverTools.waitForElementToAbsence(getRoomGroupRowByName(name), 30);
    }

    /** Selects a room group from the left sidebar so its card is shown in the main content. */
    public void selectRoomGroupInSidebar(String name) {
        waitForLoading();
        driverTools.waitForElementToPresence(SIDEBAR_MENU, 30).orElseThrow();
        WebElement sidebarItem = driverTools.waitForElementToBeClickable(getRoomGroupRowByName(name), 20).orElseThrow();
        driverTools.clickWithJavascriptExecutor(sidebarItem);
        waitForLoading();
        driverTools.waitForElementToAppear(getRoomGroupCardByName(name), 20).orElseThrow();
    }

    /** Selects a room group from the left sidebar by its exact visible name. */
    public void selectRoomGroupInSidebarByExactName(String name) {
        name = trimName(name);
        waitForLoading();
        driverTools.waitForElementToPresence(SIDEBAR_MENU, 30).orElseThrow();
        WebElement sidebarItem = driverTools.waitForElementToBeClickable(getRoomGroupSidebarRowByExactName(name), 20).orElseThrow();
        driverTools.clickWithJavascriptExecutor(sidebarItem);
        waitForLoading();
        driverTools.waitForElementToAppear(getRoomGroupCardByExactName(name), 20).orElseThrow();
    }

    public int getRoomCountOfRoomGroup(String name) {
        selectRoomGroupInSidebar(name);
        WebElement cell = driverTools.waitForElementToPresence(getRoomGroupRoomCountCellByName(name), 20).orElseThrow();
        return CoreFunctions.getIntFromString(driverTools.getText(cell)).orElse(-1);
    }

    public boolean isDeleteButtonDisplayedByRoomGroupName(String name) {
        return driverTools.waitForElementToBeClickable(getDeleteRoomGroupButtonByName(name), 20).isPresent();
    }

    public void clickDeleteButtonByRoomGroupName(String name) {
        WebElement deleteButton = driverTools.waitForElementToBeClickable(getDeleteRoomGroupButtonByName(name), 20).orElseThrow();
        driverTools.clickWithJavascriptExecutor(deleteButton);
    }

    public boolean isDeleteConfirmModalDisplayed() {
        return driverTools.waitForElementToAppear(DELETE_CONFIRM_MODAL, 10).isPresent();
    }

    public void deleteRoomGroupByName(String name) {
        selectRoomGroupInSidebar(name);
        WebElement deleteButton = driverTools.waitForElementToBeClickable(getDeleteRoomGroupButtonByName(name), 20).orElseThrow();
        driverTools.clickWithJavascriptExecutor(deleteButton);
        driverTools.waitForElementToAppear(DELETE_CONFIRM_MODAL, 10).orElseThrow();
        WebElement confirmButton = driverTools.waitForElementToBeClickable(DELETE_CONFIRM_BUTTON, 10).orElseThrow();
        driverTools.clickWithJavascriptExecutor(confirmButton);
        driverTools.waitForElementToDisappear(DELETE_CONFIRM_MODAL, 20);
        waitForLoading();
    }

    // ---- "Others" section + group selection helpers ----

    /** Selects the "Others" entry (ungrouped rooms) from the left sidebar. */
    public void selectOthersInSidebar() {
        waitForLoading();
        driverTools.waitForElementToPresence(SIDEBAR_MENU, 30).orElseThrow();
        WebElement othersItem = driverTools.waitForElementToBeClickable(SIDEBAR_OTHERS, 20).orElseThrow();
        driverTools.clickWithJavascriptExecutor(othersItem);
        waitForLoading();
    }

    /** Selects a room group or the "Others" section so its card (with its rooms) is shown. */
    private void selectGroup(String groupTitle) {
        if (OTHERS_GROUP_TITLE.equalsIgnoreCase(groupTitle) || "others".equalsIgnoreCase(groupTitle)) {
            selectOthersInSidebar();
            return;
        }
        selectRoomGroupInSidebar(groupTitle);
    }

    // ---- Reading rooms ----

    public String getFirstRoomNameInGroup(String groupTitle,String vendorName,String vendorId) {
        selectGroup(groupTitle);
        List<WebElement> rows = driverTools.waitForElementsToAppear(getRoomRowsInGroup(groupTitle,vendorName,vendorId), 30).orElseThrow();
        WebElement nameCell = driverTools.element(ROOM_NAME_CELL_IN_ROW, rows.get(0)).orElseThrow();
        return driverTools.getText(nameCell).trim();
    }

    /**
     * Returns the room count shown on the group's badge. Returns 0 when the group is not present in the
     * sidebar (e.g. it was auto-deleted because a move emptied it). "Others" is always read directly.
     */
    public int getRoomCountOfGroup(String groupTitle) {
        boolean isOthers = OTHERS_GROUP_TITLE.equalsIgnoreCase(groupTitle) || "others".equalsIgnoreCase(groupTitle);
        if (!isOthers && !driverTools.waitForElementToPresenceSafe(getRoomGroupRowByName(groupTitle), 5).isPresent()) {
            return 0;
        }
        selectGroup(groupTitle);
        WebElement badge = driverTools.waitForElementToPresence(getRoomCountBadgeByTitle(groupTitle), 20).orElseThrow();
        return CoreFunctions.getIntFromString(driverTools.getText(badge)).orElse(-1);
    }

    /** Number of room groups in the sidebar (excludes the "All" and "Others" pseudo-entries). */
    public int getRoomGroupCount() {
        waitForLoading();
        return driverTools.waitForElementsToAppear(SIDEBAR_GROUP_ITEMS, 20).map(List::size).orElse(0);
    }

    public boolean isRoomInGroup(String groupTitle, String roomName) {
        selectGroup(groupTitle);
        waitForLoading();
        return driverTools.waitForElementToPresence(getRoomRowInGroup(groupTitle, roomName), 30).isPresent();
    }

    public boolean isRoomNotInGroup(String groupTitle, String roomName) {
        if (!OTHERS_GROUP_TITLE.equalsIgnoreCase(groupTitle) && !"others".equalsIgnoreCase(groupTitle)
                && !driverTools.waitForElementToPresenceSafe(getRoomGroupRowByName(groupTitle), 5).isPresent()) {
            // The source group was auto-deleted because the move emptied it, so the room is not there.
            return true;
        }
        selectGroup(groupTitle);
        return driverTools.waitForElementToAbsence(getRoomRowInGroup(groupTitle, roomName), 20);
    }

    // ---- Room id capture (for API cleanup that unmaps moved rooms back to "Others") ----

    private static final Pattern HOTEL_ID_UUID = Pattern.compile(
            "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}");
    // Room id is a 24-char Mongo ObjectId shown in the "Room ID" cell (e.g. 5cc1d2700bae4818b8341575).
    // Lookarounds ensure we match exactly that token and not a shorter/longer hex run.
    private static final Pattern ROOM_OBJECT_ID = Pattern.compile("(?<![0-9a-f])[0-9a-f]{24}(?![0-9a-f])");

    private static final String MANAGE_ROOM_GROUP_URL_GK_TEMPLATE =
            "https://en.gatotkaca.tiket.com/admin/mm-dashboard/v4/hotel-library/manage-room-group/%s";
    private static final String MANAGE_ROOM_GROUP_URL_PREPROD_TEMPLATE =
            "https://en.preprod.tiket.com/admin/mm-dashboard/v4/hotel-library/manage-room-group/%s";
    private static final String MANAGE_ROOM_GROUP_URL_PROD_TEMPLATE =
            "https://en.tiket.com/admin/mm-dashboard/v4/hotel-library/manage-room-group/%s";

    /** Navigates the browser directly to the Manage Room Group page for {@code hotelId}. */
    public void openManageRoomGroupByHotelId(String hotelId) {
        if (hotelId == null || hotelId.isBlank()) {
            throw new RuntimeException("openManageRoomGroupByHotelId: hotelId must be non-blank");
        }
        String template = switch (Configs.ENVIRONMENT) {
            case GK -> MANAGE_ROOM_GROUP_URL_GK_TEMPLATE;
            case PREPROD -> MANAGE_ROOM_GROUP_URL_PREPROD_TEMPLATE;
            case PROD -> MANAGE_ROOM_GROUP_URL_PROD_TEMPLATE;
        };
        String url = String.format(template, hotelId);
        try {
            driverTools.get(url);
        } catch (Exception timeoutException) {
            logger.log("openManageRoomGroupByHotelId: navigation raised (checking readiness anyway): "
                    + timeoutException.getMessage());
        }
    }

    /** Extracts the hotel UUID from the current Manage Room Group page URL (used as the unmap hotelId). */
    public String getHotelIdFromUrl() {
        String url = driverTools.getDriver().getCurrentUrl();
        Matcher matcher = HOTEL_ID_UUID.matcher(url);
        if (matcher.find()) {
            return matcher.group();
        }
        throw new RuntimeException("Hotel id (UUID) not found in Manage Room Group URL: " + url);
    }

    /** Builds the current browser cookie header (name=value; ...) for reuse in dashboard gateway API calls. */
    public String getBrowserCookieHeader() {
        StringBuilder header = new StringBuilder();
        for (org.openqa.selenium.Cookie cookie : driverTools.getDriver().manage().getCookies()) {
            if (header.length() > 0) {
                header.append("; ");
            }
            header.append(cookie.getName()).append('=').append(cookie.getValue());
        }
        return header.toString();
    }

    /**
     * Reads the room id (24-char ObjectId in the "Room ID" cell) of a room row. Uses the row's full
     * textContent so it works whether or not the row exposes a selection checkbox.
     */
    private String readRoomId(WebElement row) {
        String rowText = driverTools.getValueAttribute(row, "textContent");
        Matcher matcher = ROOM_OBJECT_ID.matcher(rowText);
        if (matcher.find()) {
            return matcher.group();
        }
        throw new RuntimeException("Room id (24-char ObjectId) not found in room row: " + rowText);
    }

    /** Room id of the first room in the group (the room that a single-room move will act on). */
    public String getFirstRoomId(String groupTitle, String vendorName, String vendorId) {
        selectGroup(groupTitle);
        WebElement row = driverTools.waitForElementsToAppear(getRoomRowsInGroup(groupTitle, vendorName, vendorId), 30).orElseThrow().get(0);
        return readRoomId(row);
    }

    /** Room ids of the first {@code count} rooms in the group (the rooms a bulk move will act on). */
    public List<String> getFirstNRoomIds(String groupTitle, int count, String vendorName, String vendorId) {
        selectGroup(groupTitle);
        List<WebElement> rows = driverTools.waitForElementsToAppear(getRoomRowsInGroup(groupTitle, vendorName, vendorId), 30).orElseThrow();
        List<String> ids = new ArrayList<>();
        for (WebElement row : rows) {
            if (ids.size() >= count) {
                break;
            }
            ids.add(readRoomId(row));
        }
        return ids;
    }

    // ---- Move a single room through the room group list (hover the room row -> floating "Move") ----

    /**
     * Hovers the first room row of the group so the floating "Move" button surfaces, clicks it, and
     * waits for the "Move room to" popup. Operating on the first row directly avoids depending on
     * parsing a room name (the room table has a leading row-number cell).
     */
    private void hoverFirstRoomAndClickMove(String groupTitle,String vendorName,String vendorId) {
        selectGroup(groupTitle);
        WebElement row = driverTools.waitForElementsToAppear(getRoomRowsInGroup(groupTitle,vendorName,vendorId), 30).orElseThrow().get(0);
        driverTools.scrollIntoWebView(row);
        driverTools.hoverWithJavascriptExecutor(row);
        WebElement moveButton = driverTools.waitForElementToAppear(FLOATING_MOVE_BUTTON, 10).orElseThrow();
        driverTools.clickWithJavascriptExecutor(moveButton);
        driverTools.waitForElementToAppear(MOVE_POPUP, 20).orElseThrow();
    }

    /** Picks an existing target group from the move popup: ensure the "existing" radio, open the
     *  dropdown, then click the option whose data-label matches the target group. */
    private void selectExistingGroupInMovePopup(String targetGroup) {
        driverTools.waitForElementToBeClickable(MOVE_BUTTON, 5).ifPresent(driverTools::clickWithJavascriptExecutor);
        WebElement groupInput = driverTools.waitForElementToBeClickable(MOVE_POPUP_GROUP_INPUT, 10).orElseThrow();
        driverTools.clickWithJavascriptExecutor(groupInput);
        WebElement option = driverTools.waitForElementToPresence(getMovePopupGroupOption(targetGroup), 10).orElseThrow();
        driverTools.clickWithJavascriptExecutor(option);
    }

    /** Selects the "New Group" radio in the move popup. The group name is auto-generated by the system. */
    private void selectNewGroupInMovePopup() {
        WebElement newRadio = driverTools.waitForElementToBeClickable(MOVE_POPUP_NEW_RADIO, 10).orElseThrow();
        driverTools.clickWithJavascriptExecutor(newRadio);
    }

    /** Clicks Confirm (enabled only once a target is chosen) and agrees to the optional
     *  "previous room group will be deleted" prompt that appears when the source group empties. */
    private void confirmMovePopup() {
        WebElement confirm = driverTools.waitForElementToBeClickable(MOVE_POPUP_CONFIRM_BUTTON, 20).orElseThrow();
        driverTools.clickWithJavascriptExecutor(confirm);
        if (driverTools.waitForElementToAppear(MOVE_DELETE_PREVIOUS_GROUP_MODAL, 5).isPresent()) {
            driverTools.clickWithJavascriptExecutor(MOVE_DELETE_PREVIOUS_GROUP_AGREE_BUTTON);
            driverTools.waitForElementToDisappear(MOVE_DELETE_PREVIOUS_GROUP_MODAL, 30);
        }
        driverTools.waitForElementToDisappear(MOVE_POPUP, 30);
        waitForLoading();
    }

    private void clickConfirmButton() {
        WebElement confirm = driverTools.waitForElementToBeClickable(CONFIRM_BUTTON, 20).orElseThrow();
        driverTools.clickWithJavascriptExecutor(confirm);
        waitForLoading();
    }

    /** Moves the first room of the source group into an existing target group. */
    public void moveFirstRoomToExistingGroupViaList(String sourceGroup, String targetGroup,String vendorName,String vendorId) {
        hoverFirstRoomAndClickMove(sourceGroup,vendorName,vendorId);
        selectExistingGroupInMovePopup(targetGroup);
        confirmMovePopup();
    }

    /** Moves the first room of the source group into a brand new (system-named) group. */
    public void moveFirstRoomToNewGroupViaList(String sourceGroup,String vendorName,String vendorId) {
        hoverFirstRoomAndClickMove(sourceGroup,vendorName,vendorId);
        selectNewGroupInMovePopup();
        confirmMovePopup();
    }

    public void clicknewGroup() {
        WebElement newGroupRadio = driverTools.waitForElementToBeClickable(NEW_GROUP_RADIO, 10).orElseThrow();
        driverTools.clickWithJavascriptExecutor(newGroupRadio);
    }

    /** Moves the first room from the "Others" section into an existing group. */
    public void moveFirstRoomFromOthersToExistingGroupViaList(String targetGroup,String vendorName,String vendorId) {
        hoverFirstRoomAndClickMove(OTHERS_GROUP_TITLE,vendorName,vendorId);
        selectExistingGroupInMovePopup(targetGroup);
        confirmMovePopup();
    }

    // ---- Move multiple rooms (checkbox selection + "Move selected rooms") ----

    /** Ticks the checkbox of the first {@code count} rooms in the group and returns how many were selected. */
    private int selectFirstNRooms(String groupTitle, int count,String vendorName,String vendorId) {
        selectGroup(groupTitle);
        List<WebElement> rows = driverTools.waitForElementsToAppear(getRoomRowsInGroup(groupTitle,vendorName,vendorId), 30).orElseThrow();
        int selected = 0;
        for (WebElement row : rows) {
            if (selected >= count) {
                break;
            }
            WebElement checkbox = driverTools.element(ROOM_CHECKBOX_IN_ROW, row).orElseThrow();
            driverTools.clickWithJavascriptExecutor(checkbox);
            selected++;
        }
        return selected;
    }

    private void clickMoveSelectedRooms() {
        // The bulk-move trigger is the dedicated header button (enabled once rooms are ticked),
        // not the floating hover button used for single-room moves.
        WebElement button = driverTools.waitForElementToBeClickable(MOVE_MULTIPLE_ROOMS_BUTTON, 20).orElseThrow();
        driverTools.clickWithJavascriptExecutor(button);
    }

    private void clickMoveButton() {
        WebElement button = driverTools.waitForElementToBeClickable(MOVE_BUTTON, 20).orElseThrow();
        driverTools.clickWithJavascriptExecutor(button);

    }

    public int moveMultipleRoomsToExistingGroup(String sourceGroup, int count, String targetGroup,String vendorName,String vendorId) {
        clickMoveSelectedRooms();
        int moved = selectFirstNRooms(sourceGroup, count,vendorName,vendorId);
        selectExistingGroupInMovePopup(targetGroup);
        clickConfirmButton();
        return moved;
    }

    public int moveMultipleRoomsToNewGroup(String sourceGroup, int count,String vendorName,String vendorId) {
        clickMoveSelectedRooms();
        int moved = selectFirstNRooms(sourceGroup, count,vendorName,vendorId);
        clickMoveButton();
        clicknewGroup();
        clickConfirmButton();
        return moved;
    }

    public int moveMultipleRoomsFromOthersToExistingGroup(int count, String targetGroup,String vendorName,String vendorId) {
        clickMoveSelectedRooms();
        int moved = selectFirstNRooms(OTHERS_GROUP_TITLE, count,vendorName,vendorId);
        selectExistingGroupInMovePopup(targetGroup);
        clickConfirmButton();
        return moved;
    }

    // ---- Organize rooms by system (send room groups to Cetus) ----

    public void clickOrganizeRoomsBySystem() {
        WebElement button = driverTools.waitForElementToBeClickable(ORGANIZE_BY_SYSTEM_BUTTON, 20).orElseThrow();
        driverTools.clickWithJavascriptExecutor(button);
        driverTools.waitForElementToAppear(CONFIRM_BUTTON, 3).ifPresent(driverTools::clickWithJavascriptExecutor);
    }

    public boolean isOrganizeSuccessSnackbarAppeared() {
        return isSuccessfulSnackbarAppeared();
    }

    public boolean isCountdownTimerDisplayed() {
        return driverTools.waitForElementToAppear(COUNTDOWN_TIMER, 30).isPresent();
    }

    public enum FilterOption { ALL, VERIFIED, UNVERIFIED, EMPTY, WITH_ROOMS }

    /** Opens the filter popover and clicks the requested option. "All" resets both dimensions. */
    public void applyFilter(FilterOption option) {
        WebElement trigger = driverTools.waitForElementToBeClickable(FILTER_TRIGGER, 20).orElseThrow();
        driverTools.clickWithJavascriptExecutor(trigger);
        Locator target;
        switch (option) {
            case ALL:         target = FILTER_OPTION_ALL; break;
            case VERIFIED:    target = FILTER_OPTION_VERIFIED; break;
            case UNVERIFIED:  target = FILTER_OPTION_UNVERIFIED; break;
            case EMPTY:       target = FILTER_OPTION_EMPTY; break;
            case WITH_ROOMS:  target = FILTER_OPTION_WITH_ROOMS; break;
            default: throw new IllegalArgumentException("Unknown filter: " + option);
        }
        WebElement opt = driverTools.waitForElementToBeClickable(target, 10).orElseThrow();
        driverTools.clickWithJavascriptExecutor(opt);
        driverTools.waitForElementToAppear(LOADING_INDICATOR_ICON, 3);
        driverTools.waitForElementToDisappear(LOADING_INDICATOR_ICON, 30);
    }

    /** True if the "no room groups match this filter" empty view is displayed. */
    public boolean isEmptyFilterViewDisplayed() {
        return driverTools.waitForElementToAppear(FILTER_EMPTY_VIEW, 5).isPresent();
    }

    /** Enters selection mode for the given RG by clicking its "Edit verification" button. */
    public void clickEditVerification(String groupName) {
        groupName = trimName(groupName);
        WebElement button = driverTools.waitForElementToBeClickable(getEditVerificationButtonByName(groupName), 20).orElseThrow();
        driverTools.clickWithJavascriptExecutor(button);
        driverTools.waitForElementToAppear(CANCEL_VERIFICATION_BUTTON, 10).orElseThrow();
    }

    /** Exits selection mode (discards pending selection). Best-effort — no-op if not in selection mode. */
    public void cancelVerification() {
        driverTools.waitForElementToBeClickable(CANCEL_VERIFICATION_BUTTON, 2)
                .ifPresent(driverTools::clickWithJavascriptExecutor);
        driverTools.waitForElementToDisappear(CANCEL_VERIFICATION_BUTTON, 5);
    }

    /** Toggles the header "select all" checkbox (checks all rooms in the active RG). */
    public void clickSelectAllVerifyCheckbox() {
        WebElement box = driverTools.waitForElementToBeClickable(SELECT_ALL_VERIFY_CHECKBOX, 10).orElseThrow();
        driverTools.clickWithJavascriptExecutor(box);
    }

    /** Ticks the row-level Verify checkbox on the {@code index}-th (1-based) row of the given RG card. */
    public void checkNthRoomVerifyCheckbox(String groupName, int index) {
        groupName = trimName(groupName);
        WebElement row = driverTools.waitForElementToPresence(getNthRoomRowInGroup(groupName, index), 10).orElseThrow();
        WebElement checkbox = row.findElement(VERIFY_CHECKBOX_IN_ROW.by());
        driverTools.clickWithJavascriptExecutor(checkbox);
    }

    /** Number of rooms currently ticked, read from the "N rooms selected" counter next to the buttons. */
    public int getSelectedCount() {
        return driverTools.waitForElementToPresence(SELECTION_COUNT, 5)
                .map(e -> CoreFunctions.getIntFromString(driverTools.getText(e)).orElse(0))
                .orElse(0);
    }

    /** Number of room rows currently rendered inside the given RG card (vendor-agnostic). */
    public int getRoomRowCountInGroup(String groupName) {
        groupName = trimName(groupName);
        return driverTools.waitForElementsToAppear(getAllRoomRowsInGroup(groupName), 10)
                .map(List::size).orElse(0);
    }

    public boolean isVerifyButtonPresent() {
        return driverTools.waitForElementToAppear(VERIFY_BUTTON, 5).isPresent();
    }

    public boolean isUnverifyButtonPresent() {
        return driverTools.waitForElementToAppear(UNVERIFY_BUTTON, 5).isPresent();
    }

    /** True only when the Verify button is present AND not in HTML `disabled` state. */
    public boolean isVerifyButtonEnabled() {
        return driverTools.waitForElementToAppear(VERIFY_BUTTON, 5).map(WebElement::isEnabled).orElse(false);
    }

    /** True only when the Unverify button is present AND not in HTML `disabled` state. */
    public boolean isUnverifyButtonEnabled() {
        return driverTools.waitForElementToAppear(UNVERIFY_BUTTON, 5).map(WebElement::isEnabled).orElse(false);
    }

    /** Clicks the Verify action button — expects the confirm modal to open on the next step. */
    public void clickVerify() {
        WebElement button = driverTools.waitForElementToBeClickable(VERIFY_BUTTON, 10).orElseThrow();
        driverTools.clickWithJavascriptExecutor(button);
    }

    /** Clicks the Unverify action button — expects the confirm modal to open on the next step. */
    public void clickUnverify() {
        WebElement button = driverTools.waitForElementToBeClickable(UNVERIFY_BUTTON, 10).orElseThrow();
        driverTools.clickWithJavascriptExecutor(button);
    }

    public boolean isVerifyConfirmModalDisplayed() {
        return driverTools.waitForElementToAppear(VERIFY_CONFIRM_MODAL, 10).isPresent();
    }

    public boolean isUnverifyConfirmModalDisplayed() {
        return driverTools.waitForElementToAppear(UNVERIFY_CONFIRM_MODAL, 10).isPresent();
    }

    /** Confirms Verify in the modal and waits for the modal to close + loading spinner to resolve. */
    public void confirmVerify() {
        WebElement primary = driverTools.waitForElementToBeClickable(VERIFY_CONFIRM_PRIMARY, 10).orElseThrow();
        driverTools.clickWithJavascriptExecutor(primary);
        driverTools.waitForElementToDisappear(VERIFY_CONFIRM_MODAL, 15);
    }

    /** Confirms Unverify in the modal and waits for the modal to close + loading spinner to resolve. */
    public void confirmUnverify() {
        WebElement primary = driverTools.waitForElementToBeClickable(UNVERIFY_CONFIRM_PRIMARY, 10).orElseThrow();
        driverTools.clickWithJavascriptExecutor(primary);
        driverTools.waitForElementToDisappear(UNVERIFY_CONFIRM_MODAL, 15);
    }

    /** Dismisses the Verify confirm modal without applying. */
    public void cancelVerifyConfirmation() {
        driverTools.waitForElementToBeClickable(VERIFY_CONFIRM_CANCEL, 5)
                .ifPresent(driverTools::clickWithJavascriptExecutor);
        driverTools.waitForElementToDisappear(VERIFY_CONFIRM_MODAL, 5);
    }

    public boolean isVerifySuccessSnackbarAppeared() {
        return driverTools.waitForElementToAppear(VERIFY_SUCCESS_SNACKBAR, 30).isPresent();
    }

    public boolean isUnverifySuccessSnackbarAppeared() {
        return driverTools.waitForElementToAppear(UNVERIFY_SUCCESS_SNACKBAR, 30).isPresent();
    }

    /** True if the RG-level "Verified" badge is showing next to the given group's title. */
    public boolean isRoomGroupVerified(String groupName) {
        groupName = trimName(groupName);
        return driverTools.waitForElementToAppear(getRoomGroupVerifiedLabelByName(groupName), 5).isPresent() && driverTools.waitForElementToAppear(getRoomGroupVerifiedIconByName(groupName), 5).isPresent();
    }

    /** True if the {@code index}-th (1-based) row of the given RG shows the green verified-indicator dot. */
    public boolean isNthRoomVerified(String groupName, int index) {
        groupName = trimName(groupName);
        WebElement row = driverTools.waitForElementToPresence(getNthRoomRowInGroup(groupName, index), 10).orElseThrow();
        return !row.findElements(new By.ByXPath(".//td[2]//*[@fill='" + ROW_VERIFIED_FILL + "']")).isEmpty();
    }

    /** True if the {@code index}-th (1-based) row of the given RG shows the grey unverified-indicator dot. */
    public boolean isNthRoomUnverified(String groupName, int index) {
        groupName = trimName(groupName);
        WebElement row = driverTools.waitForElementToPresence(getNthRoomRowInGroup(groupName, index), 10).orElseThrow();
        return !row.findElements(new By.ByXPath(".//td[2]//*[@fill='" + ROW_UNVERIFIED_FILL + "']")).isEmpty();
    }

    /** Best-effort UI reset: ensures every room in the given RG is unverified. No-op if none are verified. */
    public void resetAllRoomsToUnverifiedInGroup(String groupName) {
        groupName = trimName(groupName);
        try {
            applyFilter(FilterOption.ALL);
            clickEditVerification(groupName);
            clickSelectAllVerifyCheckbox();
            if (!isUnverifyButtonEnabled()) {
                cancelVerification();
                return;
            }
            clickUnverify();
            if (isUnverifyConfirmModalDisplayed()) confirmUnverify();
        } catch (Exception ignored) {
            try { cancelVerification(); } catch (Exception ignored2) { }
        }
    }

    private static Locator getSourceTagActorLabelForGroup(String groupName) {
        return new Locator("SOURCE_TAG_ACTOR_LABEL", new By.ByXPath(String.format(
                "//div[contains(@class,'room-group-card')][.//div[contains(@data-testid,'room-group-card-title')][normalize-space(.)=%1$s or text()=%1$s]]//div[contains(concat(' ',normalize-space(@class),' '),' cursor-help ')]",
                CoreFunctions.toXPathLiteral(groupName))));
    }

    private static final Locator SOURCE_TAG_TOOLTIP_CONTAINER = new Locator(
            "SOURCE_TAG_TOOLTIP_CONTAINER", new By.ByXPath(
            "//div[starts-with(@data-testid,'Tooltip_container_')]"));

    private static final List<String> KNOWN_SOURCE_TAGS = List.of(
            "User-rule-based", "User-Cetus", "Rule-based", "Cetus", "User");

    private static final Locator MOVE_DELETE_PREVIOUS_GROUP_DISAGREE_BUTTON = new Locator(
            "MOVE_DELETE_PREVIOUS_GROUP_DISAGREE_BUTTON", new By.ByXPath(
            "//section[contains(@class,'Modal_modal_popup')][.//h2[contains(normalize-space(.),'will be deleted')]]//footer//button[normalize-space(.)='Disagree' or normalize-space(.)='Cancel']"));

    private static Locator getNthRoomVerifyCheckboxInGroup(String groupName, int index) {
        return new Locator("NTH_ROOM_VERIFY_CHECKBOX_IN_GROUP", new By.ByXPath(String.format(
                "(//div[contains(@class,'room-group-card')][.//div[contains(@class,'room-grouping-table-title') and (normalize-space()=%1$s or text()=%1$s)]]//input[@data-testid='room-group-card-verify-checkbox'])[%2$d]",
                CoreFunctions.toXPathLiteral(groupName), index)));
    }

    /** Reads the source-tag for {@code groupName} by hovering the actor label and parsing the tooltip. */
    public String getRoomGroupTagByRoomGroupName(String groupName) {
        groupName = trimName(groupName);
        WebElement actor = driverTools.waitForElementToPresence(getSourceTagActorLabelForGroup(groupName), 5).orElse(null);
        if (actor == null) return "";
        driverTools.hoverToElement(actor);
        String tooltipText = driverTools.waitForElementToAppear(SOURCE_TAG_TOOLTIP_CONTAINER, 5)
                .map(el -> driverTools.getText(el))
                .map(String::trim)
                .orElse("");
        if (tooltipText.isEmpty()) return "";
        for (String tag : KNOWN_SOURCE_TAGS) {
            if (tooltipText.startsWith(tag + "-based")
                    || tooltipText.startsWith(tag + ":")
                    || tooltipText.startsWith(tag + " ")) {
                return tag;
            }
        }
        return tooltipText;
    }

    /** Discovers the first Room Group in the sidebar whose source-tag equals {@code targetTag}. */
    public String findFirstRoomGroupNameWithTag(String targetTag) {
        waitForLoading();
        driverTools.waitForElementToPresence(SIDEBAR_MENU, 30).orElseThrow();

        if (driverTools.waitForElementToPresence(SIDEBAR_OTHERS, 3).isPresent()) {
            driverTools.waitForElementToBeClickable(SIDEBAR_OTHERS, 10)
                    .ifPresent(driverTools::clickWithJavascriptExecutor);
            waitForLoading();
        } else {
            List<WebElement> allItems = driverTools.waitForElementsToAppear(SIDEBAR_GROUP_ITEMS, 30).orElseThrow();
            if (allItems.isEmpty()) return "";
            WebElement lastItem = allItems.get(allItems.size() - 1);
            driverTools.clickWithJavascriptExecutor(lastItem);
            waitForLoading();
        }

        List<WebElement> items = driverTools.waitForElementsToAppear(SIDEBAR_GROUP_ITEMS, 30).orElseThrow();
        List<String> names = new ArrayList<>();
        for (WebElement item : items) {
            String text = driverTools.getText(item);
            if (text != null) {
                String trimmed = text.trim();
                if (!trimmed.isEmpty()) names.add(trimmed);
            }
        }

        for (String name : names) {
            try {
                selectRoomGroupInSidebarByExactName(name);
                String tag = getRoomGroupTagByRoomGroupName(name);
                if (targetTag.equals(tag)) {
                    return name;
                }
            } catch (Exception ignored) {
            }
        }
        return "";
    }

    /** Moves every room out of {@code sourceGroup} into {@code targetGroup} via the multi-select flow. */
    public int moveAllRoomsFromGroupToExistingGroup(String sourceGroup, String targetGroup) {
        sourceGroup = trimName(sourceGroup);
        targetGroup = trimName(targetGroup);
        clickMoveSelectedRooms();
        List<WebElement> rows = driverTools.waitForElementsToAppear(getAllRoomRowsInGroup(sourceGroup), 30).orElseThrow();
        int moved = 0;
        for (WebElement row : rows) {
            WebElement checkbox = driverTools.element(ROOM_CHECKBOX_IN_ROW, row).orElseThrow();
            driverTools.clickWithJavascriptExecutor(checkbox);
            moved++;
        }
        selectExistingGroupInMovePopup(targetGroup);
        clickConfirmButton();
        if (driverTools.waitForElementToAppear(MOVE_DELETE_PREVIOUS_GROUP_MODAL, 3).isPresent()) {
            driverTools.waitForElementToBeClickable(MOVE_DELETE_PREVIOUS_GROUP_DISAGREE_BUTTON, 5)
                    .ifPresent(driverTools::clickWithJavascriptExecutor);
            driverTools.waitForElementToDisappear(MOVE_DELETE_PREVIOUS_GROUP_MODAL, 15);
        }
        waitForLoading();
        return moved;
    }

    /** Best-effort click on the {@code index}-th Verify checkbox in {@code groupName}; never throws. */
    public boolean attemptCheckNthRoomVerifyCheckboxInGroup(String groupName, int index) {
        groupName = trimName(groupName);
        try {
            return driverTools.waitForElementToPresence(getNthRoomVerifyCheckboxInGroup(groupName, index), 3)
                    .filter(WebElement::isEnabled)
                    .map(cb -> {
                        driverTools.clickWithJavascriptExecutor(cb);
                        return Boolean.TRUE;
                    })
                    .orElse(Boolean.FALSE);
        } catch (Exception ignored) {
            return false;
        }
    }

    /** True if the "Edit verification" button is present for {@code groupName}. */
    public boolean isEditVerificationButtonPresentForGroup(String groupName) {
        groupName = trimName(groupName);
        return driverTools.waitForElementToPresence(getEditVerificationButtonByName(groupName), 3).isPresent();
    }

    /** True if the RG card for {@code groupName} is currently rendered. */
    public boolean isRoomGroupCardRendered(String groupName) {
        groupName = trimName(groupName);
        return driverTools.waitForElementToPresence(getRoomGroupCardByExactName(groupName), 3).isPresent();
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    /** Strips leading/trailing whitespace from a room-group name so callers can pass sheet values
     *  (which often carry a stray trailing space) without breaking exact-match XPaths. */
    private static String trimName(String value) {
        return value == null ? null : value.trim();
    }
}
