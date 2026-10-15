package com.tiket.web.dashboard.page.ttd.CMS.CMSInventoryManagementProductListPage;

import com.tiket.appium.DriverTools;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


import static com.tiket.core.CoreFunctions.randomName;
import static org.openqa.selenium.By.xpath;

public class CMSInventoryManagementProductListPageWeb<T extends WebDriver> extends WebPage<T> {
    public CMSInventoryManagementProductListPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> CMSInventoryManagementProductListPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new CMSInventoryManagementProductListPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new CMSInventoryManagementProductListPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    private static final Locator SEARCH_BAR = new Locator("SEARCH_BAR", xpath("//div[@class='input-box']//input[@type='text']"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", xpath("(//button[@data-testid='button-submit'])[1]"));
    private static final Locator PRODUCT_EDIT_ICON = new Locator("EDIT_ICON", xpath("//div[@class='icon-action__item' and span[text()='Edit']]"));
    private static final Locator EDIT_BUTTON_FOR_MULTIGUEST = new Locator("EDIT_BUTTON_FOR_MULTIGUEST", xpath("//tr[contains(@class,'table-v2_body-list')]//button[@class='action-btn' and .//span[normalize-space()='Edit']]"));
    public static final Locator DRAFT_EDITOR_INPUT = new Locator("DRAFT_EDITOR_INPUT", xpath("(//div[contains(@class,'public-DraftEditor-content') and @contenteditable='true' and contains(., 'Lorem Ipsum extra')])[1]"));
    public static final Locator DRAFT_EDITOR_INPUT2 = new Locator("DRAFT_EDITOR_INPUT", xpath("(//div[contains(@class,'public-DraftEditor-content') and @contenteditable='true' and contains(., 'Lorem Ipsum extra')])[2]"));

    public static final Locator SUBMIT_BUTTON = new Locator("SUBMIT_BUTTON", xpath("//button[@data-testid='button-submit' and text()='Submit']"));
    public static final Locator DETAILS_BUTTON = new Locator("DETAILS_BUTTON", By.xpath("(//span[text()='Details'])[4]"));
    public static final Locator ADDITIONAL_INFO = new Locator("ADDITIONAL_INFO", xpath("//span[text()='Additional Info']"));
    public static final Locator CLONE_BUTTON = new Locator("CLONE_BUTTON", xpath("//tr[contains(@class,'table-v2_body-list')]//button[@class='action-btn']//span[normalize-space()='Clone']"));
    public static final Locator MULTIPLE_CLONE_MENU_ITEM = new Locator("MULTIPLE_CLONE_MENU_ITEM", xpath("//li[contains(@class, 'clone-menu-item') and normalize-space(text())='Multiple Clone']"));
    public static final Locator MULTIPLE_CLONE_OPTION = new Locator("MULTIPLE_CLONE_OPTION", xpath("//div[contains(@class, 'tix-react-select__option') and text()='1']"));

    public static final Locator CONTINUE_BUTTON = new Locator("CONTINUE_BUTTON", xpath("//button[@data-testid='button-submit' and text()='Continue']"));
    public static final Locator CLONE_DROPDOWN = new Locator("CLONE_DROPDOWN", xpath("(//div[contains(@class, 'tix-react-select__indicator') and contains(@class, 'tix-react-select__dropdown-indicator') and contains(@class, 'css-tlfecz-indicatorContainer')])[last()]"));
    public static final Locator CLONE_NAME_EN = new Locator("CLONE_NAME_EN", xpath("(//div[contains(@class, 'input-box')]//input[@type='text'])[last()]"));
    public static final Locator CLONE_NAME_ID = new Locator("CLONE_NAME_ID", xpath("(//div[contains(@class, 'input-box')]//input[@type='text'])[last()-1]"));
    public static final Locator BUTTON_CLONE = new Locator("BUTTON_CLONE", xpath("//button[@data-testid='button-submit' and normalize-space(text())='Clone']"));
    private static final Locator SCHEDULE_BUTTON = new Locator("SCHEDULE_BUTTON", xpath("(//tr[contains(@class,'table-v2_body-list')]//button[@class='action-btn']//span[normalize-space()='Schedule'])[1]"));
    private static final Locator UPDATE_SCHEDULE_BUTTON = new Locator("UPDATE_SCHEDULE_BUTTON", xpath("//div[@class='ss__add']//span[text()='Update Schedule']"));
    private static final Locator ADD_SCHEDULE = new Locator("ADD_SCHEDULE", xpath("//a[text()='Add Schedule']"));
    private static final Locator DATE_PICKER = new Locator("DATE_PICKER", xpath("(//div[@class='cell-days' and text()='10'])[2]"));
    private static final Locator SUBMIT = new Locator("SUBMIT", xpath("//button[@type='button' and text()='Submit']"));
    private static final Locator BUTTON_SAVE_AND_PUBLISH = new Locator("BUTTON_SAVE_AND_PUBLISH", xpath("//button[@type='button' and contains(text(),'Yes, save and publish')]"));
    private static final Locator ALERT_SUCCESS_ADD_SCHEDULE = new Locator("ALERT_SUCCESS_ADD_SCHEDULE", xpath("//div[@data-testid='alert-showing' and contains(text(), 'Success Add schedule')]"));
    private static final Locator DELETE_SCHEDULE = new Locator("DELETE_SCHEDULE", xpath("(//div[@class='icon' and .//span[text()='Delete']])[last()]"));
    private static final Locator CONFIRM_DELETE_SCHEDULE = new Locator("CONFIRM_DELETE_SCHEDULE", xpath("//button[@type='error' and text()='Submit']"));
    private static final Locator DELETE_SCHEDULE_SUCCESS_ALERT = new Locator("DELETE_SCHEDULE_SUCCESS_ALERT", xpath("//div[@data-testid='alert-showing' and contains(text(), 'Successfully deleted the schedule')]"));
    private static final Locator QUOTA_INPUT = new Locator("QUOTA_INPUT", xpath("//div[@class='input-wrapper']/input[@type='number']"));
    private static final Locator ADD_NEW_DATE = new Locator("ADD_NEW_DATE", xpath("//div[@class='right-fly-seatmap' and text()='+ Add New Date']"));
    private static final Locator ADD_VENUE = new Locator("ADD_VENUE", xpath("(//input[contains(@id, 'react-select') and @type='text'])[9]"));
    private static final Locator ADD_DATE_BUTTON = new Locator("ADD_DATE_BUTTON", xpath("//button[@data-testid='button-submit' and text()='ADD DATE']"));
    private static final Locator DATE_ADD_SNACKBAR = new Locator("DATE_ADD_SNACKBAR", xpath("//div[@data-testid='alert-showing' and text()='Success to add new Date Seat Map.']"));
    private static final Locator EVENT_DATE = new Locator("EVENT_DATE", xpath("//input[@class='datepicker-box']"));
    private static final Locator NEXT_MONTH = new Locator("NEXT_MONTH", xpath("(//button[normalize-space()='Next Month'])[1]"));
    private static final Locator DATE = new Locator("DATE", xpath("//div[contains(@class,'react-datepicker__day')  and not(contains(@class,'react-datepicker__day--outside-month'))  and normalize-space()='8']"));
    private static final Locator COMMISSION_BASED_RADIOBUTTON = new Locator("COMMISSION_BASED_RADIOBUTTON", By.id("COMMISSION_TYPE"));
    private static final Locator MARKUP_BASED_RADIOBUTTON = new Locator("MARKUP_BASED_RADIOBUTTON", By.id("MARK_UP_TYPE"));
    private static final Locator MARKUP_AMOUNT_RADIOBUTTON = new Locator("MARKUP_AMOUNT_RADIOBUTTON", By.id("MARK_UP"));
    private static final Locator PACKAGE_NAME_ROWS = new Locator("PACKAGE_NAME_ROWS", By.xpath("//tr[contains(@class,'table-v2_body-list')]/td[2]"));
    private static final Locator PACKAGE_EDIT_BUTTONS = new Locator("PACKAGE_EDIT_BUTTONS", By.xpath("//tr[contains(@class,'table-v2_body-list')]//button[@class='action-btn' and .//span[normalize-space()='Edit']][1]"));
    private static final Locator COMMISSION_PERCENTAGE_INPUT = new Locator("COMMISSION_PERCENTAGE_INPUT", By.xpath("//div[@class='input-name' and text()='Commission Percentage']"));
    private static final Locator SCHEDULE_BUTTON_FOR_MANUAL_SEATING = new Locator("SCHEDULE_BUTTON_FOR_MANUAL_SEATING", By.xpath("(//div[@class='icon-action__item' and span[text()='Schedule']][1])[5]"));
    private static final Locator SEAT_MAP_ROW_1 = new Locator("SEAT_MAP_ROW_1", By.xpath("(//span[normalize-space()='Seat Map'])[1]"));
    private static final Locator TOTAL_SEAT_AVAILABLE_VALUE = new Locator("TOTAL_SEAT_AVAILABLE_VALUE", By.xpath("//div[text()='Total seat available']/following-sibling::div[@class='value-summary col-2']"));
    private static final Locator ASSIGNED_TO_THIS_PACKAGE_VALUE = new Locator("ASSIGNED_TO_THIS_PACKAGE_VALUE", By.xpath("//div[text()='Assigned to this package']/following-sibling::div[@class='value-summary col-2']"));
    private static final Locator MAPPING_SECTION_CAT1_TAB = new Locator("MAPPING_SECTION_CAT1_TAB", By.xpath("//button[.//span[text()='CAT-1']]"));
    private static final Locator SCHEDULE_SUMMARY_TITLE = new Locator("SCHEDULE_SUMMARY_TITLE", By.xpath("//div[contains(@class,'title-summary') and text()='Schedule Summary']"));
    private static final Locator TOTAL_SEAT_AVAILABLE_LABEL = new Locator("TOTAL_SEAT_AVAILABLE_LABEL", By.xpath("//div[contains(@class,'padding-left-0') and normalize-space(text())='Total seat available']"));
    private static final Locator ASSIGNED_TO_THIS_PACKAGE_LABEL = new Locator("ASSIGNED_TO_THIS_PACKAGE_LABEL", By.xpath("//div[contains(@class,'padding-left-0') and normalize-space(text())='Assigned to this package']"));
    private static final Locator ASSIGN_TO_OTHER_PACKAGE_LABEL = new Locator("ASSIGN_TO_OTHER_PACKAGE_LABEL", By.xpath("//div[contains(@class,'padding-left-0') and normalize-space(text())='Assign to other package']"));
    private static final Locator BLOCKED_FOR_PARTNER_LABEL = new Locator("BLOCKED_FOR_PARTNER_LABEL", By.xpath("//div[contains(@class,'padding-left-0') and normalize-space(text())='Blocked for partner']"));
    private static final Locator BLOCKED_FOR_ALL_LABEL = new Locator("BLOCKED_FOR_ALL_LABEL", By.xpath("//div[contains(@class,'padding-left-0') and normalize-space(text())='Blocked for all']"));
    private static final Locator VOID_LABEL = new Locator("VOID_LABEL", By.xpath("//div[contains(@class,'padding-left-0') and normalize-space(text())='Void']"));
    private static final Locator PARTNER_BOOKED_LABEL = new Locator("PARTNER_BOOKED_LABEL", By.xpath("//div[contains(@class,'padding-left-0') and normalize-space(text())='Partner Booked']"));
    private static final Locator AVAILABLE_LABEL = new Locator("AVAILABLE_LABEL", By.xpath("//div[contains(@class,'padding-left-0') and normalize-space(text())='Available']"));
    private static final Locator SOLD_LABEL = new Locator("SOLD_LABEL", By.xpath("//div[contains(@class,'padding-left-0') and normalize-space(text())='Sold']"));
    private static final Locator BLOCK_BUTTON = new Locator("BLOCK_BUTTON", By.xpath("//button[contains(@class,'tix-button') and contains(@class,'secondary') and normalize-space(text())='Block']"));
    private static final Locator BLOCK_FOR_PARTNER_RADIO = new Locator("BLOCK_FOR_PARTNER_RADIO", By.xpath("//label[contains(., 'Block for partner')]/preceding-sibling::div//input[@type='radio']"));
    private static final Locator BLOCK_FOR_ALL_PACKAGES_RADIO = new Locator("BLOCK_FOR_ALL_PACKAGES_RADIO", By.xpath("//label[contains(., 'Block for all packages')]/preceding-sibling::div//input[@type='radio']"));
    private static final Locator BLOCKED_FOR_PARTNER_VALUE = new Locator("BLOCKED_FOR_PARTNER_VALUE", By.xpath("//div[normalize-space(text())='Blocked for partner']/following-sibling::div[contains(@class,'value-summary')]"));
    private static final Locator SELECT_ALL_AVAILABLE_SEAT_CHECKBOX = new Locator("SELECT_ALL_AVAILABLE_SEAT_CHECKBOX", By.xpath("//div[@class='checkbox-availbale-section']//input[@type='checkbox']"));
    private static final Locator ASSIGN_TO_SCHEDULE_BUTTON = new Locator("ASSIGN_TO_SCHEDULE_BUTTON", By.xpath("//button[normalize-space(text())='Assign to this schedule']"));
    private static final Locator SAVE_BLOCK_PARTNER = new Locator("SAVE_BUTTON", By.xpath("(//button[normalize-space(text())='Save'])[2]"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", By.xpath("//button[normalize-space(text())='Save']"));
    private static final Locator PACKAGE_SEARCH_INPUT = new Locator("PACKAGE_SEARCH_INPUT", By.xpath("//div[@class='input-wrapper']/input[@type='text']"));
    private static final Locator SCHEDULE_BLOCKED_PARTNER = new Locator("SCHEDULE_BLOCKED_PARTNER", By.xpath("//tr[td[normalize-space(text())='MANUAL_SEATING']]/td[contains(@class,'icon-actions')]//span[normalize-space(text())='Schedule']"));
    private static final Locator BLOCKED_FOR_ALL_VALUE = new Locator("BLOCKED_FOR_ALL_VALUE", By.xpath("//div[normalize-space(text())='Blocked for all']/following-sibling::div[contains(@class,'value-summary')]"));
    private static final Locator MARKUP_PERCENTAGE_INPUTS = new Locator("MARKUP_PERCENTAGE_INPUTS", By.xpath("//div[text()='Mark Up Percentage']/following-sibling::input"));
    private static final Locator MARKUP_AMOUNT_INPUTS = new Locator("MARKUP_AMOUNT_INPUTS", By.xpath("//div[text()='Mark Up Amount']/following-sibling::input"));

    private static final Locator ADD_PACKAGE_BUTTON = new Locator("ADD_PACKAGE_BUTTON", By.xpath("//*[contains(@class, 'add-package-button')]//span[text()='Add package']"));
    private static final Locator SEARCH_PACKAGE_NAME = new Locator("SEARCH_PACKAGE_NAME", By.xpath("//input[@placeholder=\"Search\"]"));
    private static final Locator PACKAGE_NAME_1 = new Locator("PACKAGE_NAME_1", By.xpath("(//input[@name='languageNameContry'])[1]"));
    private static final Locator PACKAGE_NAME_2 = new Locator("PACKAGE_NAME_2", By.xpath("(//input[@name='languageNameContry'])[2]"));
    private static final Locator SUB_TITLE_1 = new Locator("SUB_TITLE_1", By.xpath("(//input[@name='subTitle'])[1]"));
    private static final Locator SUB_TITLE_2 = new Locator("SUB_TITLE_2", By.xpath("(//input[@name='subTitle'])[2]"));
    private static final Locator WHATS_INCLUDED_1 = new Locator("WHATS_INCLUDED_1", By.xpath("(//input[@name='whatsIncluded'])[1]"));
    private static final Locator WHATS_INCLUDED_2 = new Locator("WHATS_INCLUDED_2", By.xpath("(//input[@name='whatsIncluded'])[2]"));
    private static final Locator WHATS_EXCLUDED_1 = new Locator("WHATS_EXCLUDED_1", By.xpath("(//input[@name='whatsExcluded'])[1]"));
    private static final Locator WHATS_EXCLUDED_2 = new Locator("WHATS_EXCLUDED_2", By.xpath("(//input[@name='whatsExcluded'])[2]"));
    private static final Locator GENERAL_TEXT_INPUT = new Locator("GENERAL_TEXT_INPUT", By.xpath("(//div[@contenteditable='true' and contains(@aria-describedby, 'placeholder')])[1]"));
    private static final Locator MINIMUM_PAX_INPUT_1 = new Locator("MINIMUM_PAX_INPUT", By.xpath("(//div[div[text()='Minimum Pax']]//input)[1]"));
    private static final Locator MINIMUM_PAX_INPUT_2 = new Locator("MINIMUM_PAX_INPUT_2", By.xpath("(//div[div[text()='Minimum Pax']]//input)[2]"));
    private static final Locator PAXES = new Locator("PAXES", By.xpath("(//div[@class='input-box']//div[text()='Maximum Pax']/following-sibling::input)[1]"));
    private static final Locator DROPDOWN_SEAT_TYPE = new Locator("DROPDOWN_SEAT_TYPE", By.xpath("(//div[contains(@class,'tix-react-select__dropdown-indicator')])[16]"));
    private static final Locator SEAT_SELECTION_AVAILABLE = new Locator("SEAT_SELECTION_AVAILABLE", By.xpath("//div[contains(@class,'tix-react-select__option') and text()='Seat Selection Available']"));
    private static final Locator SEAT_SELECTED_AUTOMATICALLY = new Locator("SEAT_SELECTED_AUTOMATICALLY", By.xpath("//div[contains(@class,'tix-react-select__option') and starts-with(text(),'Seat Selected Automatically')]"));
    private static final Locator DROPDOWN_CODE = new Locator("DROPDOWN_CODE", By.xpath("(//div[contains(@class,'tix-react-select__dropdown-indicator')])[17]"));
    private static final Locator DROPDOWN_CODE_NEW_PRODUCT = new Locator("DROPDOWN_CODE_NEW_PRODUCT", By.xpath("(//div[contains(@class,'tix-react-select__dropdown-indicator')])[16]"));
    private static final Locator ADULT_OPTION = new Locator("ADULT_OPTION", By.xpath("//div[contains(@class,'tix-react-select__option') and text()='Adult']"));
    private static final Locator DESCRIPTION_INPUT_1 = new Locator("DESCRIPTION_INPUT_1", By.xpath("(//input[@name='Description'])[1]"));
    private static final Locator DESCRIPTION_INPUT_2 = new Locator("DESCRIPTION_INPUT_2", By.xpath("(//input[@name='Description'])[2]"));
    private static final Locator MINIMUM_SELLING_PRICE = new Locator("MINIMUM_SELLING_PRICE", By.xpath("//input[@name='formattedMinimumSellingPrice']"));
    private static final Locator PARTNER_PRICE = new Locator("PARTNER_PRICE", By.xpath("//input[@name='formattedPartnerPrice']"));
    private static final Locator EVENT_DURATION = new Locator("EVENT_DURATION", By.xpath("//div[div[text()='Event Duration in minutes']]//input"));
    private static final Locator BOOKING_DEADLINE = new Locator("BOOKING_DEADLINE", By.xpath("(//input[@type='number' and @min='0'])[2]"));
    private static final Locator PACKAGE_UPDATE_SUCCESS = new Locator("PACKAGE_UPDATE_SUCCESS", By.xpath("//*[contains(text(), 'Success')]"));
    private static final Locator CHECKBOX_SUBMIT = new Locator("CHECKBOX_SUBMIT", By.xpath("//input[@data-testid='checkbox-submit']"));
    private static final Locator COUNTRY_OPTION_INDONESIA = new Locator("COUNTRY_OPTION_INDONESIA", By.xpath("//div[@id='react-select-9-option-0' and contains(@class,'css-1n7v3ny-option') and text()='Indonesia']"));
    private static final Locator PRIMARY_CATEGORY_INPUT = new Locator("PRIMARY_CATEGORY_INPUT", By.xpath("//div[contains(@class,'tix-react-select__value-container')]//input[@type='text' and contains(@id,'react-select')]"));
    private static final Locator TOURS_OPTION = new Locator("TOURS_OPTION", By.xpath("//div[contains(@class,'tix-react-select__option')]"));
    private static final Locator DROPDOWN_INDICATOR = new Locator("DROPDOWN_INDICATOR", By.xpath("(//div[contains(@class,'tix-react-select__dropdown-indicator')])[2]"));
    private static final Locator COUNTRY_INPUT = new Locator("COUNTRY_INPUT", By.xpath("//div[contains(@class,'css-1hwfws3')]//input[@id='react-select-9-input']"));
    private static final Locator COUNTRY_OPTION = new Locator("COUNTRY_OPTION", By.xpath("//div[@id='react-select-9-option-0' and contains(@class,'css-1n7v3ny-option') and text()='Indonesia']"));
    private static final Locator PARTNER_INPUT = new Locator("PARTNER_INPUT", By.xpath("//input[@id='react-select-5-input']"));
    private static final Locator PARTNER_OPTION = new Locator("PARTNER_OPTION", By.xpath("//div[contains(@class,'tix-react-select__option')]"));
    private static final Locator SUPPLIER_INPUT = new Locator("SUPPLIER_INPUT", By.xpath("//div[contains(@class,'tix-react-select__value-container') and .//div[text()='Supplier']]"));
    private static final Locator SUPPLIER_OPTION = new Locator("SUPPLIER_OPTION", By.xpath("//div[@id='react-select-8-option-2']"));
    private static final Locator STATUS_DROPDOWN = new Locator("STATUS_DROPDOWN", By.xpath("(//div[contains(@class,'tix-react-select__value-container')])[5]"));
    private static final Locator STATUS_OPTION = new Locator("STATUS_OPTION", By.xpath("(//div[contains(@class,'tix-react-select__option')])[2]"));
    private static final Locator MANUAL_SEATING_SCHEDULE = new Locator("MANUAL_SEATING_SCHEDULE", By.xpath("(//tr[contains(@class,'table-v2_body-list')]//button[@class='action-btn']//span[normalize-space()='Schedule'])[5]"));
    private static final Locator SEAT_MAP_ICON_FIRST = new Locator("SEAT_MAP_ICON", By.xpath("//span[text()='Seat Map']"));
    private static final Locator EXPORT_XLSX_BUTTON = new Locator("EXPORT_XLSX_BUTTON", By.xpath("//button[text()='Export to XLSX']"));
    private static final Locator FIRST_ROW_PACKAGE_NAME = new Locator("FIRST_ROW_PACKAGE_NAME", By.xpath("(//tr[contains(@class,'table-v2_body-list')])[1]/td[2]//*[contains(@class,'b3') and contains(@class,'high-emphasis')]"));
    private static final Locator FIRST_ROW_PRODUCT_NAME = new Locator("FIRST_ROW_PRODUCT_NAME", By.xpath("//table[@class='table-tiket']//tbody/tr[1]"));
    private static final Locator SHOW_ALL_PACKAGES_BUTTON = new Locator("SHOW_ALL_PACKAGES_BUTTON", By.xpath("//button[contains(., 'Show all packages')]"));
    private static final Locator DELETE_PACKAGE_ICON = new Locator("DELETE_PACKAGE_ICON", By.xpath("//tr[contains(@class,'table-v2_body-list')]//button[@class='action-btn']//span[normalize-space()='Delete'][1]"));
    private static final Locator CONFIRM_DELETE_PACKAGE_BUTTON = new Locator("CONFIRM_DELETE_PACKAGE_BUTTON", By.cssSelector("button.swal2-confirm.swal2-styled"));
    private static final Locator ADD_UPDATE_PRODUCT_BUTTON = new Locator("ADD_UPDATE_PRODUCT_BUTTON", By.xpath("//div[contains(@class, 'action-add') and contains(., 'Add/Update product')]"));
    private static final Locator ADD_NEW_PRODUCT_OPTION = new Locator("ADD_NEW_PRODUCT_OPTION", By.xpath("//div[contains(@class, 'bulk-menu')]//span[normalize-space()='Add new Product']"));
    private static final Locator PRODUCT_NAME_INPUT_1 = new Locator("PRODUCT_NAME_INPUT_1", By.xpath("(//div[@class='input-name' and text()='Product Name']/following-sibling::input[@type='text'])[1]"));
    private static final Locator PRODUCT_NAME_INPUT_2 = new Locator("PRODUCT_NAME_INPUT_2", By.xpath("(//div[@class='input-name' and text()='Product Name']/following-sibling::input[@type='text'])[2]"));
    private static final Locator META_TITLE_INPUT_1 = new Locator("META_TITLE_INPUT_1", By.xpath("(//div[@class='input-name' and text()='Meta Title (optional)']/following-sibling::input[@type='text'])[1]"));
    private static final Locator META_TITLE_INPUT_2 = new Locator("META_TITLE_INPUT_2", By.xpath("(//div[@class='input-name' and text()='Meta Title (optional)']/following-sibling::input[@type='text'])[2]"));
    private static final Locator META_DESCRIPTION_INPUT_1 = new Locator("META_DESCRIPTION_INPUT_1", By.xpath("(//div[@class='input-name' and text()='Meta Description (optional)']/following-sibling::input[@type='text'])[1]"));
    private static final Locator META_DESCRIPTION_INPUT_2 = new Locator("META_DESCRIPTION_INPUT_2", By.xpath("(//div[@class='input-name' and text()='Meta Description (optional)']/following-sibling::input[@type='text'])[2]"));
    private static final Locator URL_INPUT_1 = new Locator("URL_INPUT_1", By.xpath("(//div[@class='input-name' and text()='URL (optional)']/following-sibling::input[@type='text'])[1]"));
    private static final Locator URL_INPUT_2 = new Locator("URL_INPUT_2", By.xpath("(//div[@class='input-name' and text()='URL (optional)']/following-sibling::input[@type='text'])[2]"));
    private static final Locator IMPORTANT_INFO_EDITOR_1 = new Locator("IMPORTANT_INFO_EDITOR_1", By.xpath("(//div[@class='RichEditor-editor' and @data-testid='editor-input']//div[@class='notranslate public-DraftEditor-content' and @contenteditable='true'])[2]"));
    private static final Locator IMPORTANT_INFO_EDITOR_2 = new Locator("IMPORTANT_INFO_EDITOR_2", By.xpath("(//div[@class='RichEditor-editor' and @data-testid='editor-input']//div[@class='notranslate public-DraftEditor-content' and @contenteditable='true'])[9]"));
    private static final Locator HOW_TO_GET_THERE_1 = new Locator("HOW_TO_GET_THERE_1", By.xpath("(//div[@class='RichEditor-editor' and @data-testid='editor-input']//div[@class='notranslate public-DraftEditor-content' and @contenteditable='true'])[3]"));
    private static final Locator HOW_TO_GET_THERE_2 = new Locator("HOW_TO_GET_THERE_2", By.xpath("(//div[@class='RichEditor-editor' and @data-testid='editor-input']//div[@class='notranslate public-DraftEditor-content' and @contenteditable='true'])[10]"));
    private static final Locator ITINERARY_TITLE_INPUT_1 = new Locator("ITINERARY_TITLE_INPUT_1", By.xpath("(//div[@class='input-name' and text()='Title']/following-sibling::input[@type='text'])[1]"));
    private static final Locator ITINERARY_TITLE_INPUT_2 = new Locator("ITINERARY_TITLE_INPUT_2", By.xpath("(//div[@class='input-name' and text()='Title']/following-sibling::input[@type='text'])[2]"));
    private static final Locator ITINERARY_DESCRIPTION_INPUT_1 = new Locator("ITINERARY_DESCRIPTION_INPUT_1", By.xpath("(//div[@class='input-name' and text()='Description']/following-sibling::input[@type='text'])[1]"));
    private static final Locator ITINERARY_DESCRIPTION_INPUT_2 = new Locator("ITINERARY_DESCRIPTION_INPUT_2", By.xpath("(//div[@class='input-name' and text()='Description']/following-sibling::input[@type='text'])[2]"));
    private static final Locator HOW_TO_REDEEM_1 = new Locator("HOW_TO_REDEEM_1", By.xpath("(//div[@class='RichEditor-editor' and @data-testid='editor-input']//div[@class='notranslate public-DraftEditor-content' and @contenteditable='true'])[4]"));
    private static final Locator HOW_TO_REDEEM_2 = new Locator("HOW_TO_REDEEM_2", By.xpath("(//div[@class='RichEditor-editor' and @data-testid='editor-input']//div[@class='notranslate public-DraftEditor-content' and @contenteditable='true'])[11]"));
    private static final Locator TnC_1 = new Locator("TnC_1", By.xpath("(//div[@class='RichEditor-editor' and @data-testid='editor-input']//div[@class='notranslate public-DraftEditor-content' and @contenteditable='true'])[5]"));
    private static final Locator TnC_2 = new Locator("TnC_2", By.xpath("(//div[@class='RichEditor-editor' and @data-testid='editor-input']//div[@class='notranslate public-DraftEditor-content' and @contenteditable='true'])[12]"));
    private static final Locator DESCRIPTION_TEXTAREA = new Locator("DESCRIPTION_TEXTAREA", By.xpath("(//div[contains(@class,'tix-textarea__name') and normalize-space()='Description']/following-sibling::div[contains(@class,'RichEditor-editor')]//div[@contenteditable='true' and contains(@class,'public-DraftEditor-content')])[1]"));
    private static final Locator COUNTRY_CODE_DROPDOWN = new Locator("COUNTRY_CODE_DROPDOWN", By.xpath("//div[contains(@class,'tix-react-select__single-value') and text()='Select Country Code']"));
    private static final Locator COUNTRY_CODE_INDONESIA_OPTION = new Locator("COUNTRY_CODE_INDONESIA_OPTION", By.xpath("//div[contains(@class,'tix-react-select__option') and text()='Indonesia (+62)']"));
    private static final Locator PHONE_NUMBER_INPUT = new Locator("PHONE_NUMBER_INPUT", By.xpath("//div[@class='input-name' and text()='Phone No.']/following-sibling::input[@type='text']"));
    private static final Locator LOCATION_INPUT = new Locator("LOCATION_INPUT", By.xpath("//input[contains(@class,'pac-target-input') and @placeholder='Enter a location']"));
    private static final Locator LOCATION_JAKARTA_OPTION = new Locator("LOCATION_JAKARTA_OPTION", By.xpath("(//div[contains(@class,'pac-item')]//span[@class='pac-matched' and text()='Jakarta'])[1]"));
    private static final Locator STREET_INPUT_1 = new Locator("STREET_INPUT_1", By.xpath("(//div[@class='input-name' and text()='Street']/following-sibling::input[@type='text'])[1]"));
    private static final Locator STREET_INPUT_2 = new Locator("STREET_INPUT_2", By.xpath("(//div[@class='input-name' and text()='Street']/following-sibling::input[@type='text'])[2]"));
    private static final Locator POPULARITY_SCORE_INPUT = new Locator("POPULARITY_SCORE_INPUT", By.xpath("//div[@class='input-name' and text()='Popularity Score']/following-sibling::input[@type='text']"));
    private static final Locator PARTNER_DROPDOWN = new Locator("PARTNER_DROPDOWN", By.xpath("//div[contains(@class,'tix-react-select__placeholder') and text()='Partner']"));
    private static final Locator DROPDOWN_FIRST_OPTION = new Locator("DROPDOWN_FIRST_OPTION", By.xpath("(//div[contains(@class,'tix-react-select__option')])[1]"));
    private static final Locator PRODUCT_STATUS = new Locator("PRODUCT_STATUS", By.xpath("//div[contains(@class,'tix-react-select__placeholder') and text()='Product Status']"));
    private static final Locator PRODUCT_STATUS_OPTION = new Locator("PRODUCT_STATUS_OPTION", By.xpath("//div[contains(@class,'tix-react-select__option') and text()='Active']"));
    private static final Locator PRIMARY_CATEGORY = new Locator("PRIMARY_CATEGORY", By.xpath("//div[contains(@class,'tix-react-select__placeholder') and text()='Primary Category']"));
    private static final Locator PRIMARY_CATEGORY_OPTION = new Locator("PRIMARY_CATEGORY_OPTION", By.xpath("//div[text()='Classes & Workshops']"));
    private static final Locator SUB_CATEGORY = new Locator("SUB_CATEGORY", By.xpath("//div[contains(@class,'tix-react-select__placeholder') and text()='Sub-category']"));
    private static final Locator SUB_CATEGORY_OTHER_WORKSHOPS = new Locator("SUB_CATEGORY_OTHER_WORKSHOPS", By.xpath("//div[contains(@class,'tix-react-select__menu')]//div[contains(@class,'tix-react-select__option') and translate(normalize-space(), ' ', '')='OTHER-WORKSHOPS']"));
    private static final Locator PRODUCT_CONFIGURATION_DESCRIPTION = new Locator("PRODUCT_CONFIGURATION_DESCRIPTION", By.xpath("//textarea[@name='email']"));
    private static final Locator COMMISSION_PERCENTAGE = new Locator("COMMISSION_PERCENTAGE", By.xpath("//div[text()='Commision Percentage (%)']/following::input[@type='number'][1]"));
    private static final Locator SELECT_PRODUCT_TAG = new Locator("SELECT_PRODUCT_TAG", By.xpath("//div[text()='Select Product Tag']"));
    private static final Locator PRODUCT_TAG_FIRST_OPTION = new Locator("PRODUCT_TAG_FIRST_OPTION", By.xpath("//div[text()='GL1Q']"));
    private static final Locator SELECT_FACILITIES_DROPDOWN = new Locator("SELECT_FACILITIES_DROPDOWN", By.xpath("//div[text()='Select Facilities']"));
    private static final Locator SELECT_FACILITIES_OPTION = new Locator("SELECT_FACILITIES_OPTION", By.xpath("//div[text()='Bar']"));
    private static final Locator PRODUCT_EDIT_BTN = new Locator("PRODUCT_EDIT_BTN", By.xpath("(//div[@class='icon-action__item' and span[text()='Edit']])[1]"));
    private static final Locator OK_BUTTON = new Locator("OK_BUTTON", By.xpath("//button[contains(@class,'swal2-confirm')]"));
    private static final Locator SUPPLIER_BMG = new Locator("SUPPLIER_BMG", By.xpath("//div[contains(@class,'tix-react-select__option') and text()='BE MY GUEST']"));
    private static final Locator UPDATE_SUPPLIER_BUTTON = new Locator("UPDATE_SUPPLIER_BUTTON", By.xpath("(//div[@class='icon-action__item' and .//span[text()='Update']])[1]"));
    private static final Locator UPDATE_SUCCESS_ALERT = new Locator("UPDATE_SUCCESS_ALERT", By.xpath("//h2[@id='swal2-title' and text()='Updated!']"));
    private static final Locator SUPPLIER_INPUT_2 = new Locator("SUPPLIER_INPUT_2", By.xpath("//div[text()='Supplier']/ancestor::div[contains(@class,'tix-react-select__value-container')]//input[@type='text']"));
    private static final Locator PACKAGE_PAGINATION_NEXT = new Locator("PACKAGE_PAGINATION_NEXT", By.xpath("//span[@data-testid='pagination-next']"));
    private static final Locator SESSION_RADIO = new Locator("SESSION_RADIO", By.xpath("//input[@value='\"session\"']"));
    private static final Locator QUOTA_INPUT_SESSION = new Locator("QUOTA_INPUT_SESSION", By.xpath("//div[text()='Quota']/following-sibling::div//input"));
    private static final Locator INPUT_HOURS_SESSION = new Locator("INPUT_HOURS_SESSION", By.xpath("//div[@data-testid='timepicker-wrapper']//input[@name='hours']"));
    private static final Locator INPUT_MINUTES_SESSION = new Locator("INPUT_MINUTES_SESSION", By.xpath("//div[@data-testid='timepicker-wrapper']//input[@name='minutes']"));
    private static final Locator SESSION_TITLE_INPUT_EN = new Locator("SESSION_TITLE_INPUT_EN", By.xpath("(//input[@name='Session title'])[1]"));
    private static final Locator SESSION_TITLE_INPUT_ID = new Locator("SESSION_TITLE_INPUT_ID", By.xpath("(//input[@name='Session title'])[2]"));
    private static final Locator SESSION_DETAIL_INPUT_EN_RTE = new Locator("SESSION_DETAIL_INPUT_EN_RTE", By.xpath("(//div[@contenteditable='true'])[1]"));
    private static final Locator SESSION_DETAIL_INPUT_ID_RTE = new Locator("SESSION_DETAIL_INPUT_ID_RTE", By.xpath("(//div[@contenteditable='true'])[2]"));

    public void goToPage() {
        switch (Configs.ENVIRONMENT) {
            case GK -> {
                driver.get("https://gatotkaca.tiket.com/admin/event-ms/to-do/product-management/product-list");
            }
            case PREPROD -> {
                driver.get("https://preprod.tiket.com/admin/event-ms/to-do/product-management/product-list");
            }
            case PROD -> {
                driver.get("https://www.tiket.com/admin/event-ms/to-do/product-management/product-list");
            }
        }
        driverTools.waitForWebToLoad();
        driverTools.refreshWebpage();
        driverTools.waitForWebToLoad();
    }

    public void inputSearchBar(String productName) {
        driverTools.waitForElementToAppear(SEARCH_BAR);
        driverTools.dummyWait(3);
        driverTools.sendKeys(SEARCH_BAR, productName);
    }

    public void pressSearchButton() {
        driverTools.waitForElementToAppear(SEARCH_BUTTON);
        driverTools.clickWithJavascriptExecutor(SEARCH_BUTTON);
        driverTools.dummyWait(1);
    }

    public void goToPackage() {
        driverTools.waitForElementToAppear(PRODUCT_EDIT_ICON);
        driverTools.scrollToElement(PRODUCT_EDIT_ICON);
        driverTools.clickWithJavascriptExecutor(PRODUCT_EDIT_ICON);
        driverTools.switchToWindowContainingUrl("edit-product");
        driverTools.waitForElementToAppear(EDIT_BUTTON_FOR_MULTIGUEST);
        driverTools.scrollToElement(EDIT_BUTTON_FOR_MULTIGUEST);
    }


    public String editPackage(String description) {
        driverTools.clickWithJavascriptExecutor(EDIT_BUTTON_FOR_MULTIGUEST);
        driverTools.sendKeys(DRAFT_EDITOR_INPUT, description);
        driverTools.sendKeys(DRAFT_EDITOR_INPUT2, description);
        String textTocheck = driverTools.getText(DRAFT_EDITOR_INPUT).get();
        driverTools.scrollAndClick(SUBMIT_BUTTON);
        driverTools.dummyWait(5);
        return textTocheck;
    }

    public String cloneProduct() {
        String name = "Cloned Preprod Automation " + randomName();
        driverTools.waitForElementToAppear(CLONE_BUTTON);
        driverTools.scrollToElement(CLONE_BUTTON);
        driverTools.clickWithJavascriptExecutor(CLONE_BUTTON);
        driverTools.waitForElementToAppear(MULTIPLE_CLONE_MENU_ITEM);
        driverTools.clickWithJavascriptExecutor(MULTIPLE_CLONE_MENU_ITEM);
        driverTools.waitForElementToAppear(CLONE_DROPDOWN);
        driverTools.click(CLONE_DROPDOWN);
        driverTools.waitForElementToAppear(MULTIPLE_CLONE_OPTION);
        driverTools.click(MULTIPLE_CLONE_OPTION);
        driverTools.waitForElementToAppear(CONTINUE_BUTTON);
        driverTools.click(CONTINUE_BUTTON);
        driverTools.clearInput(CLONE_NAME_EN);
        driverTools.clearInput(CLONE_NAME_ID);
        driverTools.sendKeys(CLONE_NAME_EN, name);
        driverTools.sendKeys(CLONE_NAME_ID, name);
        driverTools.click(CLONE_NAME_ID);
        driverTools.click(CHECKBOX_SUBMIT);
        driverTools.click(BUTTON_CLONE);
        return name;
    }

    public boolean isPackageCloned(String packageName) {
        driverTools.dummyWait(1);
        Locator clonedPackageName = new Locator("TEXT_EXACT_MATCH", By.xpath("//*[normalize-space(text())='" + packageName.trim() + "']"));

        //checking package name when total package page > 1
        boolean isMatch = false;
        int currentPage = 1;
        int maxPages = 15;
        while (currentPage <= maxPages) {
            if (driverTools.isElementDisplayed(clonedPackageName)) {
                isMatch = true;
                break;
            }
            // Before attempting to click "Next", ensure it is still enabled.
            if (!driverTools.isElementEnabled(PACKAGE_PAGINATION_NEXT)) {
                logger.log("Max package page is reached");
                break;
            }
            driverTools.waitForElementToAppear(PACKAGE_PAGINATION_NEXT);
            driverTools.scrollToElement(PACKAGE_PAGINATION_NEXT);
            driverTools.click(PACKAGE_PAGINATION_NEXT);
            currentPage++;
        }
        return isMatch;
    }

    private Locator getTextLocator(String text) {
        return new Locator("", By.xpath("//p[contains(text(),'" + text + "')]"));
    }

    public void goToProductInB2C(String productName) {
        switch (Configs.ENVIRONMENT) {
            case GK -> {
                driver.get("https://gatotkaca.tiket.com/to-do/" + productName);
            }
            case PREPROD -> {
                driver.get("https://preprod.tiket.com/to-do/" + productName);
            }
            case PROD -> {
                driver.get("https://www.tiket.com/to-do/" + productName);
            }
        }
        driverTools.waitForWebToLoad();
    }

    public boolean checkUpdateinB2C(String description) {
        driverTools.click(DETAILS_BUTTON);
        driverTools.waitForElementToAppear(ADDITIONAL_INFO);
        driverTools.clickWithJavascriptExecutor(ADDITIONAL_INFO);
        driverTools.click(ADDITIONAL_INFO);
        driverTools.waitForElementToAppear(getTextLocator(description));
        return driverTools.isElementDisplayed(getTextLocator(description));
    }

    public void addSchedule() {
        driverTools.waitForElementToAppear(UPDATE_SCHEDULE_BUTTON);
        driverTools.click(UPDATE_SCHEDULE_BUTTON);
        driverTools.waitForElementToAppear(ADD_SCHEDULE);
        driverTools.click(ADD_SCHEDULE);
        driverTools.waitForElementToAppear(DATE_PICKER);
        driverTools.click(DATE_PICKER);
        driverTools.scrollToElement(SUBMIT);
        driverTools.click(SUBMIT);
        driverTools.waitForElementToAppear(BUTTON_SAVE_AND_PUBLISH, 2);
        driverTools.click(BUTTON_SAVE_AND_PUBLISH);
    }

    public boolean isScheduleAdded() {
        driverTools.waitForElementToAppear(ALERT_SUCCESS_ADD_SCHEDULE);
        return driverTools.isElementDisplayed(ALERT_SUCCESS_ADD_SCHEDULE);
    }

    public void deleteSchedule() {
        driverTools.waitForElementToAppear(DELETE_SCHEDULE);
        driverTools.clickWithJavascriptExecutor(DELETE_SCHEDULE);
        driverTools.waitForElementToAppear(CONFIRM_DELETE_SCHEDULE);
        driverTools.click(CONFIRM_DELETE_SCHEDULE);
    }

    public boolean isScheduleDeleted() {
        driverTools.waitForElementToAppear(DELETE_SCHEDULE_SUCCESS_ALERT);
        return driverTools.isElementDisplayed(DELETE_SCHEDULE_SUCCESS_ALERT);
    }

    public void editPackageButton() {
        driverTools.clickWithJavascriptExecutor(EDIT_BUTTON_FOR_MULTIGUEST);
        driverTools.waitForElementToAppear(SUBMIT_BUTTON);
    }

    public void enterQuota(String quota) {
        driverTools.waitForElementToAppear(QUOTA_INPUT);
        driverTools.clearInput(QUOTA_INPUT);
        driverTools.sendKeys(QUOTA_INPUT, quota);
        driverTools.clickWithJavascriptExecutor(SUBMIT_BUTTON);
        driverTools.waitForElementToAppear(CONTINUE_BUTTON);
    }

    public boolean isQuotaUpdated(String quota) {
        driverTools.waitForElementToAppear(QUOTA_INPUT);
        String currentQuota = driverTools.getValueAttribute(QUOTA_INPUT, "value");
        return currentQuota.equals(quota);
    }

    public void goToPackageSchedule() {
        driverTools.waitForElementToAppear(SCHEDULE_BUTTON);
        driverTools.scrollToElement(SCHEDULE_BUTTON);
        driverTools.click(SCHEDULE_BUTTON);
        driverTools.switchToWindowContainingUrl("schedule-manange");
    }

    public void clickManualSeatingScheduleButton() {
        driverTools.waitForElementToAppear(SCHEDULE_BUTTON_FOR_MANUAL_SEATING);
        driverTools.scrollToElement(SCHEDULE_BUTTON_FOR_MANUAL_SEATING);
        driverTools.clickWithJavascriptExecutor(SCHEDULE_BUTTON_FOR_MANUAL_SEATING);
        driverTools.switchToWindowContainingUrl("/schedule-manange");
        driverTools.waitForElementToAppear(SEAT_MAP_ROW_1);
    }

    public void clickSeatMapButton() {
        driverTools.waitForElementToAppear(SEAT_MAP_ROW_1);
        driverTools.clickWithJavascriptExecutor(SEAT_MAP_ROW_1);
    }

    public boolean checkAllSeatsAreAssigned() {
        driverTools.waitForElementToAppear(TOTAL_SEAT_AVAILABLE_VALUE);
        driverTools.scrollToElement(TOTAL_SEAT_AVAILABLE_VALUE);
        String totalSeatText = driverTools.getText(TOTAL_SEAT_AVAILABLE_VALUE).orElseThrow();
        driverTools.waitForElementToAppear(ASSIGNED_TO_THIS_PACKAGE_VALUE);
        String assignedSeatText = driverTools.getText(ASSIGNED_TO_THIS_PACKAGE_VALUE).orElseThrow();

        logger.log("🔍 Total seat available: " + totalSeatText);
        logger.log("🔍 Assigned to this package: " + assignedSeatText);

        try {
            int total = Integer.parseInt(totalSeatText);
            int assigned = Integer.parseInt(totalSeatText);
            return total == assigned;
        } catch (NumberFormatException e) {
            logger.log("Failed to parse seat counts: " + e.getMessage());
            return false;
        }
    }

    public boolean checkSeatMapScheduleSummaryIsDisplayed() {
        driverTools.waitForElementToAppear(TOTAL_SEAT_AVAILABLE_LABEL);
        driverTools.scrollToElement(TOTAL_SEAT_AVAILABLE_LABEL);

        boolean isTotalSeatAvailableDisplayed = driverTools.isElementDisplayed(TOTAL_SEAT_AVAILABLE_LABEL);
        boolean isAssignedToThisPackageDisplayed = driverTools.isElementDisplayed(ASSIGNED_TO_THIS_PACKAGE_LABEL);
        boolean isAssignToOtherPackageDisplayed = driverTools.isElementDisplayed(ASSIGN_TO_OTHER_PACKAGE_LABEL);
        boolean isBlockedForPartnerDisplayed = driverTools.isElementDisplayed(BLOCKED_FOR_PARTNER_LABEL);
        boolean isBlockedForAllDisplayed = driverTools.isElementDisplayed(BLOCKED_FOR_ALL_LABEL);
        boolean isVoidDisplayed = driverTools.isElementDisplayed(VOID_LABEL);
        boolean isPartnerBookedDisplayed = driverTools.isElementDisplayed(PARTNER_BOOKED_LABEL);
        boolean isAvailableDisplayed = driverTools.isElementDisplayed(AVAILABLE_LABEL);
        boolean isSoldDisplayed = driverTools.isElementDisplayed(SOLD_LABEL);

        return isTotalSeatAvailableDisplayed && isAssignedToThisPackageDisplayed && isAssignToOtherPackageDisplayed && isBlockedForPartnerDisplayed
                && isBlockedForAllDisplayed && isVoidDisplayed && isPartnerBookedDisplayed && isAvailableDisplayed && isSoldDisplayed;
    }

    public void clickSearchInput(String packageName) {
        driverTools.waitForElementToAppear(SEARCH_PACKAGE_NAME);
        driverTools.scrollToElement(SEARCH_PACKAGE_NAME);
        driverTools.clickWithJavascriptExecutor(SEARCH_PACKAGE_NAME);
        driverTools.sendKeys(SEARCH_PACKAGE_NAME, packageName + Keys.ENTER);
    }

    public boolean isFirstRowPackageNameDisplayed(String packageName) {
        driverTools.waitForElementToAppear(FIRST_ROW_PACKAGE_NAME, 30);
        driverTools.scrollElementToCenter(FIRST_ROW_PACKAGE_NAME);
        String firstRowPackageName = driverTools.getText(FIRST_ROW_PACKAGE_NAME).orElse("");
        return firstRowPackageName.equalsIgnoreCase(packageName);
    }

    public boolean isFirstRowProductNameDisplayed(String productName) {
        driverTools.waitForElementToAppear(FIRST_ROW_PRODUCT_NAME);
        String firstRowPackageName = driver.findElement(FIRST_ROW_PRODUCT_NAME.by()).getText();
        logger.log("First row product name: " + productName);
        return firstRowPackageName.contains(productName);
    }

    public void clickDeletePackageButton() {
        driverTools.waitForElementToAppear(DELETE_PACKAGE_ICON);
        driverTools.scrollToElement(DELETE_PACKAGE_ICON);
        driverTools.clickWithJavascriptExecutor(DELETE_PACKAGE_ICON);
        driverTools.waitForElementToAppear(CONFIRM_DELETE_PACKAGE_BUTTON);
        driverTools.click(CONFIRM_DELETE_PACKAGE_BUTTON);
    }


    public void clickOnScheduleBlockedPartner() {
        driverTools.waitForElementToAppear(SCHEDULE_BLOCKED_PARTNER);
        driverTools.scrollToElement(SCHEDULE_BLOCKED_PARTNER);
        driverTools.clickWithJavascriptExecutor(SCHEDULE_BLOCKED_PARTNER);
        driverTools.switchToWindowContainingUrl("/schedule-manange");
    }

    public Locator findFirstUnblockedSeat() {

        driverTools.waitForElementToAppear(SELECT_ALL_AVAILABLE_SEAT_CHECKBOX);
        driverTools.scrollToElement(SELECT_ALL_AVAILABLE_SEAT_CHECKBOX);
        List<WebElement> allSeatWrappers = driver.findElements(By.xpath("//div[contains(@class,'tix-seat-aqua')]"));

        for (WebElement seatWrapper : allSeatWrappers) {
            String classAttr = seatWrapper.getAttribute("class");
            if (!classAttr.contains("block")) {
                try {
                    WebElement seatLabel = seatWrapper.findElement(By.xpath(".//div[contains(@class,'bg-seat')]"));
                    String seatText = seatLabel.getText().trim();

                    String xpath = "//div[contains(@class,'bg-seat') and normalize-space(text())='" + seatText + "']";
                    return new Locator("FIRST_UNBLOCKED_SEAT_" + seatText, By.xpath(xpath));
                } catch (NoSuchElementException e) {
                    // If no label found inside the seat, skip it
                }
            }
        }

        throw new NoSuchElementException("No unblocked seats found.");
    }

    public void clickOnFirstUnblockedSeat() {
        Locator firstUnblockedSeat = findFirstUnblockedSeat();
        driverTools.waitForElementToAppear(firstUnblockedSeat);
        driverTools.clickWithJavascriptExecutor(firstUnblockedSeat);
    }

    public void clickBlockButton() {
        driverTools.waitForElementToAppear(BLOCK_BUTTON);
        driverTools.clickWithJavascriptExecutor(BLOCK_BUTTON);
    }

    public void clickSaveButton() {
        driverTools.waitForElementToAppear(SAVE_BUTTON);
        driverTools.clickWithJavascriptExecutor(SAVE_BUTTON);
    }

    public void clickBlockForPartnerRadio() {
        driverTools.waitForElementToAppear(BLOCK_FOR_PARTNER_RADIO);
        driverTools.clickWithJavascriptExecutor(BLOCK_FOR_PARTNER_RADIO);
    }

    public void clickAssignToScheduleButton() {
        driverTools.waitForElementToAppear(ASSIGN_TO_SCHEDULE_BUTTON);
        driverTools.clickWithJavascriptExecutor(ASSIGN_TO_SCHEDULE_BUTTON);
    }

    public int getBlockedForPartnerValueBefore() {
        driverTools.waitForElementToAppear(BLOCKED_FOR_PARTNER_VALUE);
        driverTools.scrollToElement(BLOCKED_FOR_PARTNER_VALUE);
        String blockedForPartnerText = driverTools.getText(BLOCKED_FOR_PARTNER_VALUE).orElseThrow();
        logger.log("🔍 Blocked for partner before blocking new seat: " + blockedForPartnerText);
        int blockedForPartnerCountBefore = Integer.parseInt(blockedForPartnerText);
        return blockedForPartnerCountBefore;
    }

    public void clickSaveBlockForPartnerButton() {
        driverTools.waitForElementToAppear(SAVE_BLOCK_PARTNER);
        driverTools.click(SAVE_BLOCK_PARTNER);
    }

    public boolean isSeatBlockedForPartner(int blockedSeatCountBefore) {
        driverTools.waitForElementToAppear(BLOCKED_FOR_PARTNER_VALUE);
        driverTools.scrollToElement(BLOCKED_FOR_PARTNER_VALUE);
        String blockedForPartnerText = driverTools.getText(BLOCKED_FOR_PARTNER_VALUE).orElseThrow();
        int blockedForPartnerCountAfter = Integer.parseInt(blockedForPartnerText);
        logger.log("🔍 Blocked for partner after blocking new seat: " + blockedForPartnerCountAfter);
        return blockedForPartnerCountAfter > blockedSeatCountBefore;
    }

    public int getBlockedForAllPackagesValueBefore() {
        driverTools.waitForElementToAppear(BLOCKED_FOR_ALL_VALUE);
        driverTools.scrollToElement(BLOCKED_FOR_ALL_VALUE);
        String blockedForPartnerText = driverTools.getText(BLOCKED_FOR_ALL_VALUE).orElseThrow();
        logger.log("🔍 Blocked for all packages before blocking new seat: " + blockedForPartnerText);
        int blockedForPartnerCountBefore = Integer.parseInt(blockedForPartnerText);
        return blockedForPartnerCountBefore;
    }

    public void clickBlockForAllPackagesRadio() {
        driverTools.waitForElementToAppear(BLOCK_FOR_ALL_PACKAGES_RADIO);
        driverTools.clickWithJavascriptExecutor(BLOCK_FOR_ALL_PACKAGES_RADIO);
    }

    public boolean isSeatBlockedForAllPackages(int blockedSeatCountBefore) {
        driverTools.waitForElementToAppear(BLOCKED_FOR_ALL_VALUE);
        driverTools.scrollToElement(BLOCKED_FOR_ALL_VALUE);
        String blockedForPartnerText = driverTools.getText(BLOCKED_FOR_ALL_VALUE).orElseThrow();
        logger.log("🔍 Blocked for all packages: " + blockedForPartnerText);
        return Integer.parseInt(blockedForPartnerText) > blockedSeatCountBefore;
    }

    public void clickEditProductButton() {
        driverTools.waitForElementToAppear(PRODUCT_EDIT_ICON, 10);
        driverTools.clickWithJavascriptExecutor(PRODUCT_EDIT_ICON);
        driverTools.switchToWindowContainingUrl("edit-product");
    }

    public void clickAddNewDate() {
        driverTools.waitForElementToAppear(ADD_NEW_DATE, 30);
        driverTools.clickWithJavascriptExecutor(ADD_NEW_DATE);
    }

    public void addVenue(String venueName) {
        driverTools.waitForElementToBeClickable(ADD_VENUE);
        driverTools.clickElementByCoordinateDWeb(ADD_VENUE);

        driverTools.sendKeys(ADD_VENUE, venueName);
        Locator SELECT_VENUE = new Locator("SELECT_VENUE", By.xpath("//div[contains(@class, 'tix-react-select__option') and text()='" + venueName + "']"));
        driverTools.click(SELECT_VENUE);
    }

    public void selectEventDate() {
        driverTools.waitForElementToAppear(EVENT_DATE);
        driverTools.click(EVENT_DATE);

        driverTools.clickWithJavascriptExecutor(NEXT_MONTH);
        driverTools.waitForElementToBeClickable(DATE, 10);
        driverTools.clickWithJavascriptExecutor(DATE);
        driverTools.clickWithJavascriptExecutor(ADD_DATE_BUTTON);
    }

    public boolean isVenueAdded() {
        driverTools.waitForElementToAppear(DATE_ADD_SNACKBAR);
        return driverTools.isElementDisplayed(DATE_ADD_SNACKBAR);
    }

    public boolean isNewDateAdded() {
        LocalDate currentDate = LocalDate.now();
        LocalDate nextMonth = currentDate.plusMonths(1);
        LocalDate nextMonth10th = nextMonth.withDayOfMonth(10);

        String month = nextMonth.getMonth().toString().substring(0, 3).toLowerCase();
        month = month.substring(0, 1).toUpperCase() + month.substring(1);
        String nextMonthDate = String.format("%02d %s", nextMonth10th.getDayOfMonth(), month);

        Locator ACTUAL_DATE = new Locator("ACTUAL_DATE", xpath("//td[contains(text(), '" + nextMonthDate + "')]"));
        driverTools.waitForElementToAppear(ACTUAL_DATE);
        return driverTools.isElementDisplayed(ACTUAL_DATE);
    }

    public boolean verifyProductIsMarkupBased() {
        driverTools.dummyWait(3);
        return driverTools.isElementSelected(MARKUP_BASED_RADIOBUTTON);
    }

    public String getMarkupType() {
        if (driverTools.isElementSelected(MARKUP_AMOUNT_RADIOBUTTON)) {
            return "amount";
        } else return "percentage";
    }

    public boolean verifyProductIsCommissionBased() {
        driverTools.dummyWait(3);
        driverTools.waitForElementToAppear(COMMISSION_BASED_RADIOBUTTON);
        driverTools.scrollToElement(COMMISSION_BASED_RADIOBUTTON);
        return driverTools.isElementSelected(COMMISSION_BASED_RADIOBUTTON);
    }

    public void clickRandomPackageEditButton() {
        List<WebElement> buttons = driverTools.waitForElementsToAppear(PACKAGE_EDIT_BUTTONS).get();
        List<WebElement> packageNames = driverTools.waitForElementsToAppear(PACKAGE_NAME_ROWS).get();
        int selectedRow = CoreFunctions.generateRandomInt(0, buttons.size() - 1);
        logger.log(String.format("Clicking edit button for Package : %s", driverTools.getText(packageNames.get(selectedRow))));
        driverTools.clickElementByCoordinateDWeb(buttons.get(selectedRow));
        driverTools.dummyWait(2);
    }

    public boolean verifyPackageIsCommissionBased() {
        List<WebElement> inputs = driverTools.waitForElementsToAppear(COMMISSION_PERCENTAGE_INPUT).get();
        if (inputs.isEmpty()) {
            return false;
        }
        return true;
    }

    public boolean verifyPackageIsMarkupBased(String markupType) {
        logger.log("MARKUP TYPE" + markupType);
        List<WebElement> inputs = markupType.equalsIgnoreCase("amount") ? driverTools.waitForElementsToAppear(MARKUP_AMOUNT_INPUTS).orElse(new ArrayList<>()) : driverTools.waitForElementsToAppear(MARKUP_PERCENTAGE_INPUTS).orElse(new ArrayList<>());

        for (WebElement input : inputs) {
            if (!driverTools.isElementEnabled(input)) return false;
        }

        return true;
    }


    public void addPackageButton() {
        driverTools.waitForElementToAppear(ADD_PACKAGE_BUTTON);
        driverTools.clickWithJavascriptExecutor(ADD_PACKAGE_BUTTON);
    }

    public void clickSubmitButton() {
        driverTools.waitForElementToAppear(SUBMIT_BUTTON);
        driverTools.clickWithJavascriptExecutor(SUBMIT_BUTTON);
    }

    public void fillPackageDetails(String packageName, String value) {
        driverTools.sendKeys(PACKAGE_NAME_1, packageName);
        driverTools.sendKeys(PACKAGE_NAME_2, packageName);
        driverTools.sendKeys(SUB_TITLE_1, packageName);
        driverTools.sendKeys(SUB_TITLE_2, packageName);
        driverTools.sendKeysWithJsExecutor(WHATS_INCLUDED_1, packageName);
        driverTools.sendKeysWithJsExecutor(WHATS_INCLUDED_2, packageName);
        driverTools.scrollToElement(WHATS_EXCLUDED_1);
        driverTools.sendKeys(WHATS_EXCLUDED_1, packageName);
        driverTools.sendKeys(WHATS_EXCLUDED_2, packageName);
        if (driverTools.isElementDisplayed(GENERAL_TEXT_INPUT)) {
            driverTools.waitForElementToAppear(GENERAL_TEXT_INPUT); // General text input is used for multiple fields because of the dynamic nature of the page
            driverTools.scrollToElement(GENERAL_TEXT_INPUT);
            driverTools.sendKeys(GENERAL_TEXT_INPUT, packageName);
            driverTools.sendKeys(GENERAL_TEXT_INPUT, packageName);
            driverTools.sendKeys(GENERAL_TEXT_INPUT, value);
            driverTools.sendKeys(GENERAL_TEXT_INPUT, value);
            driverTools.sendKeys(GENERAL_TEXT_INPUT, packageName);
            driverTools.sendKeys(GENERAL_TEXT_INPUT, packageName);
        }
        if (driverTools.isElementDisplayed(EVENT_DURATION)) {
            driverTools.waitForElementToAppear(EVENT_DURATION);
            driverTools.scrollToElement(EVENT_DURATION);
            driverTools.sendKeys(EVENT_DURATION, value);
        }
        if (driverTools.isElementDisplayed(BOOKING_DEADLINE)) {
            driverTools.waitForElementToAppear(BOOKING_DEADLINE);
            driverTools.scrollToElement(BOOKING_DEADLINE);
            driverTools.sendKeys(BOOKING_DEADLINE, value);
        }
    }

    public void fillMinimumAndMaximumPax(String minimumPax, String maximumPax, String description, String value) {
        if (driverTools.isElementDisplayed(DROPDOWN_CODE)) {
            driverTools.waitForElementToAppear(DROPDOWN_CODE);
            driverTools.scrollElementToCenter(DROPDOWN_CODE);
            driverTools.click(DROPDOWN_CODE);
        } else {
            driverTools.waitForElementToAppear(DROPDOWN_CODE_NEW_PRODUCT);
            driverTools.scrollElementToCenter(DROPDOWN_CODE_NEW_PRODUCT);
            driverTools.click(DROPDOWN_CODE_NEW_PRODUCT);
        }
        driverTools.waitForElementToPresence(ADULT_OPTION);
        driverTools.click(ADULT_OPTION);
        driverTools.scrollToElement(MINIMUM_PAX_INPUT_1);
        driverTools.sendKeys(MINIMUM_PAX_INPUT_1, minimumPax);
        driverTools.sendKeys(PAXES, maximumPax);
        driverTools.scrollToElement(MINIMUM_PAX_INPUT_2);
        driverTools.sendKeys(MINIMUM_PAX_INPUT_2, minimumPax);
        driverTools.sendKeys(PAXES, maximumPax);
        driverTools.scrollToElement(DESCRIPTION_INPUT_1);
        driverTools.sendKeys(DESCRIPTION_INPUT_1, description);
        driverTools.sendKeys(DESCRIPTION_INPUT_2, description);
        driverTools.scrollToElement(MINIMUM_SELLING_PRICE);
        driverTools.sendKeys(MINIMUM_SELLING_PRICE, value);
        driverTools.scrollToElement(PARTNER_PRICE);
        driverTools.sendKeys(PARTNER_PRICE, value);
    }

    public void selectSeatTypeDropdown() {
        driverTools.scrollToElement(DROPDOWN_SEAT_TYPE);
        driverTools.click(DROPDOWN_SEAT_TYPE);
    }

    public void selectSeatSelectionAvailable() {
        driverTools.waitForElementToAppear(SEAT_SELECTION_AVAILABLE);
        driverTools.clickWithJavascriptExecutor(SEAT_SELECTION_AVAILABLE);
    }

    public void selectSeatSelectedAutomatically() {
        driverTools.waitForElementToAppear(SEAT_SELECTED_AUTOMATICALLY);
        driverTools.click(SEAT_SELECTED_AUTOMATICALLY);
    }

    public void selectPrimaryCategory(String category) {
        Locator OPTION = new Locator("OPTION", By.xpath("//div[contains(@class,'tix-react-select__option') and text()='" + category + "']"));
        driverTools.refreshWebpage();
        driverTools.dummyWait(1);
        driverTools.waitForElementToAppear(DROPDOWN_INDICATOR);
        driverTools.click(DROPDOWN_INDICATOR);
        driverTools.waitForElementToAppear(OPTION);
        driverTools.click(OPTION);
    }

    public void searchByCountry(String country) {
        driverTools.waitForElementToAppear(COUNTRY_INPUT);
        driverTools.sendKeys(COUNTRY_INPUT, country);
        driverTools.waitForElementToAppear(COUNTRY_OPTION);
        driverTools.click(COUNTRY_OPTION);
    }

    public void searchByPartner(String partner) {
        driverTools.dummyWait(1);
        driverTools.waitForElementToAppear(PARTNER_INPUT);
        driverTools.click(PARTNER_INPUT);
        driverTools.sendKeys(PARTNER_INPUT, partner);
        driverTools.waitForElementToAppear(PARTNER_OPTION);
        driverTools.click(PARTNER_OPTION);
    }

    public boolean verifyCategoryValues(String headerName, String expectedValue) {
        driverTools.dummyWait(1);
        List<WebElement> headers = driverTools.waitForElementsToAppear(new Locator("TABLE_HEADERS", By.xpath("//table//th"))).orElse(List.of());
        int categoryIndex = -1;
        for (int i = 0; i < headers.size(); i++) {
            if (headers.get(i).getText().trim().equalsIgnoreCase(headerName)) {
                categoryIndex = i + 1;
                break;
            }
        }
        if (categoryIndex == -1) return false;
        List<WebElement> categoryCells = driver.findElements(By.xpath("//table//tr/td[" + categoryIndex + "]"));
        return !categoryCells.isEmpty() && categoryCells.stream().allMatch(cell -> {
            String text = cell.getText();
            return text == null || text.trim().isEmpty() || text.trim().toLowerCase().contains(expectedValue.toLowerCase());
        });
    }

    public void searchBySupplier(String supplier) {
        driverTools.dummyWait(1);
        driverTools.waitForElementToAppear(SUPPLIER_INPUT);
        driverTools.click(SUPPLIER_INPUT);
        driverTools.sendKeysWithJsExecutor(SUPPLIER_INPUT, supplier);
        driverTools.waitForElementToAppear(SUPPLIER_OPTION);
        driverTools.click(SUPPLIER_OPTION);
    }

    public void selectStatus() {
        driverTools.waitForElementToAppear(STATUS_DROPDOWN);
        driverTools.click(STATUS_DROPDOWN);
        driverTools.waitForElementToAppear(STATUS_OPTION);
        driverTools.click(STATUS_OPTION);
    }

    public boolean verifySelectedPartner(String partnerName) {
        Locator SELECTED_PARTNER = new Locator("SELECTED_PARTNER", By.xpath("//div[contains(@class,'tix-react-select__single-value') and text()='" + partnerName + "']"));
        driverTools.waitForElementToAppear(SELECTED_PARTNER);
        return driverTools.isElementDisplayed(SELECTED_PARTNER);
    }

    public void clickScheduleManualProductButton() {
        driverTools.waitForElementToAppear(MANUAL_SEATING_SCHEDULE);
        driverTools.scrollToElement(MANUAL_SEATING_SCHEDULE);
        driverTools.click(MANUAL_SEATING_SCHEDULE);
    }

    public void clickOnSeatMapIcon() {
        driverTools.switchToWindowContainingUrl("schedule-manange");
        driverTools.click(SEAT_MAP_ICON_FIRST);
    }

    public void clickOnExportXLSXButton() {
        driverTools.waitForElementToAppear(EXPORT_XLSX_BUTTON);
        driverTools.clickWithJavascriptExecutor(EXPORT_XLSX_BUTTON);
    }

    public void clickShowAllPackagesButton() {
        driverTools.waitForElementToAppear(SHOW_ALL_PACKAGES_BUTTON);
        driverTools.scrollToElement(SHOW_ALL_PACKAGES_BUTTON);
        driverTools.clickWithJavascriptExecutor(SHOW_ALL_PACKAGES_BUTTON);
    }

    public boolean isPackageDisplayed(String packageName) {
        Locator PACKAGE_NAME_B2C = new Locator("PACKAGE_NAME_B2C", By.xpath("//h3[contains(@class,'PackageCard_package_title') and contains(text(),'" + packageName + "')]"));
        driverTools.waitForElementToAppear(PACKAGE_NAME_B2C);
        return driverTools.isElementDisplayed(PACKAGE_NAME_B2C);
    }

    public void refreshPage() {
        driverTools.refreshWebpage();
    }

    public void clickAddProductButton() {
        driverTools.waitForElementToAppear(ADD_UPDATE_PRODUCT_BUTTON);
        driverTools.clickWithJavascriptExecutor(ADD_UPDATE_PRODUCT_BUTTON);
        driverTools.waitForElementToAppear(ADD_NEW_PRODUCT_OPTION);
        driverTools.clickWithJavascriptExecutor(ADD_NEW_PRODUCT_OPTION);
    }

    public void fillNewProductDetails(String productDetails, String locationInput) {
        driverTools.waitForElementToAppear(PRODUCT_NAME_INPUT_1);
        driverTools.scrollToElement(PRODUCT_NAME_INPUT_1);
        driverTools.sendKeys(PRODUCT_NAME_INPUT_1, productDetails);
        driverTools.sendKeys(PRODUCT_NAME_INPUT_2, productDetails);
        driverTools.waitForElementToAppear(META_TITLE_INPUT_1);
        driverTools.sendKeys(META_TITLE_INPUT_1, productDetails);
        driverTools.sendKeys(META_TITLE_INPUT_2, productDetails);
        driverTools.waitForElementToAppear(META_DESCRIPTION_INPUT_1);
        driverTools.sendKeys(META_DESCRIPTION_INPUT_1, productDetails);
        driverTools.sendKeys(META_DESCRIPTION_INPUT_2, productDetails);
        driverTools.waitForElementToAppear(URL_INPUT_1);
        driverTools.scrollToElement(URL_INPUT_1);
        driverTools.sendKeys(URL_INPUT_1, productDetails);
        driverTools.sendKeys(URL_INPUT_2, productDetails);
        driverTools.waitForElementToAppear(WHATS_INCLUDED_1);
        driverTools.scrollToElement(WHATS_INCLUDED_1);
        driverTools.sendKeys(WHATS_INCLUDED_1, productDetails);
        driverTools.sendKeys(WHATS_INCLUDED_2, productDetails);
        driverTools.waitForElementToAppear(IMPORTANT_INFO_EDITOR_1);
        driverTools.scrollToElement(IMPORTANT_INFO_EDITOR_1);
        driverTools.sendKeys(IMPORTANT_INFO_EDITOR_1, productDetails);
        driverTools.sendKeys(IMPORTANT_INFO_EDITOR_2, productDetails);
        driverTools.waitForElementToAppear(HOW_TO_GET_THERE_1);
        driverTools.scrollToElement(HOW_TO_GET_THERE_1);
        driverTools.sendKeys(HOW_TO_GET_THERE_1, productDetails);
        driverTools.sendKeys(HOW_TO_GET_THERE_2, productDetails);
        driverTools.waitForElementToAppear(ITINERARY_TITLE_INPUT_1);
        driverTools.scrollToElement(ITINERARY_TITLE_INPUT_1);
        driverTools.sendKeys(ITINERARY_TITLE_INPUT_1, productDetails);
        driverTools.waitForElementToAppear(ITINERARY_DESCRIPTION_INPUT_1);
        driverTools.scrollToElement(ITINERARY_DESCRIPTION_INPUT_1);
        driverTools.sendKeys(ITINERARY_DESCRIPTION_INPUT_1, productDetails);
        driverTools.sendKeys(ITINERARY_TITLE_INPUT_2, productDetails);
        driverTools.sendKeys(ITINERARY_DESCRIPTION_INPUT_2, productDetails);
        driverTools.waitForElementToAppear(HOW_TO_REDEEM_1);
        driverTools.scrollToElement(HOW_TO_REDEEM_1);
        driverTools.sendKeys(HOW_TO_REDEEM_1, productDetails);
        driverTools.sendKeys(HOW_TO_REDEEM_2, productDetails);
        driverTools.waitForElementToAppear(TnC_1);
        driverTools.scrollToElement(TnC_1);
        driverTools.sendKeys(TnC_1, productDetails);
        driverTools.sendKeys(TnC_2, productDetails);

        driverTools.waitForElementToAppear(DESCRIPTION_TEXTAREA);
        driverTools.scrollToElement(DESCRIPTION_TEXTAREA);
        driverTools.sendKeys(DESCRIPTION_TEXTAREA, productDetails);
        driverTools.waitForElementToAppear(COUNTRY_CODE_DROPDOWN);
        driverTools.scrollElementToCenter(COUNTRY_CODE_DROPDOWN);
        driverTools.click(COUNTRY_CODE_DROPDOWN);
        driverTools.waitForElementToAppear(COUNTRY_CODE_INDONESIA_OPTION);
        driverTools.clickWithJavascriptExecutor(COUNTRY_CODE_INDONESIA_OPTION);
        driverTools.waitForElementToAppear(PHONE_NUMBER_INPUT);
        driverTools.scrollElementToCenter(PHONE_NUMBER_INPUT);
        driverTools.clickWithJavascriptExecutor(PHONE_NUMBER_INPUT);
        driverTools.sendKeys(PHONE_NUMBER_INPUT, CoreFunctions.randomPhone());
        driverTools.waitForElementToAppear(LOCATION_INPUT);
        driverTools.scrollToElement(LOCATION_INPUT);
        driverTools.sendKeys(LOCATION_INPUT, locationInput);
        driverTools.waitForElementToAppear(LOCATION_JAKARTA_OPTION);
        driverTools.clickWithJavascriptExecutor(LOCATION_JAKARTA_OPTION);
        driverTools.waitForElementToAppear(STREET_INPUT_1);
        driverTools.scrollToElement(STREET_INPUT_1);
        driverTools.sendKeys(STREET_INPUT_1, productDetails);
        driverTools.sendKeys(STREET_INPUT_2, productDetails);

    }

    public void fillProductConfigurationDetails(String productDetails, String commissionPercentage) {
        driverTools.waitForElementToAppear(PARTNER_DROPDOWN);
        driverTools.scrollToElement(PARTNER_DROPDOWN);
        driverTools.click(PARTNER_DROPDOWN);
        driverTools.waitForElementToAppear(DROPDOWN_FIRST_OPTION);
        driverTools.clickWithJavascriptExecutor(DROPDOWN_FIRST_OPTION);
        driverTools.waitForElementToAppear(PRODUCT_STATUS);
        driverTools.scrollToElement(PRODUCT_STATUS);
        driverTools.click(PRODUCT_STATUS);
        driverTools.waitForElementToAppear(PRODUCT_STATUS_OPTION);
        driverTools.clickWithJavascriptExecutor(PRODUCT_STATUS_OPTION);
        driverTools.scrollAndClick(PRIMARY_CATEGORY);
        driverTools.waitForElementToAppear(PRIMARY_CATEGORY_OPTION);
        driverTools.scrollElementToCenter(PRIMARY_CATEGORY_OPTION);
        driverTools.clickWithJavascriptExecutor(PRIMARY_CATEGORY_OPTION);
        driverTools.dummyWait(1);
        driverTools.scrollAndClick(SUB_CATEGORY);
        driverTools.waitForElementToAppear(SUB_CATEGORY_OTHER_WORKSHOPS);
        driverTools.scrollElementToCenter(SUB_CATEGORY_OTHER_WORKSHOPS);
        driverTools.clickWithJavascriptExecutor(SUB_CATEGORY_OTHER_WORKSHOPS);
        driverTools.waitForElementToAppear(PRODUCT_CONFIGURATION_DESCRIPTION);
        driverTools.scrollToElement(PRODUCT_CONFIGURATION_DESCRIPTION);
        driverTools.sendKeys(PRODUCT_CONFIGURATION_DESCRIPTION, productDetails);
        driverTools.waitForElementToAppear(COMMISSION_PERCENTAGE);
        driverTools.scrollToElement(COMMISSION_PERCENTAGE);
        driverTools.sendKeys(COMMISSION_PERCENTAGE, commissionPercentage);
        driverTools.waitForElementToAppear(SELECT_PRODUCT_TAG);
        driverTools.scrollToElement(SELECT_PRODUCT_TAG);
        driverTools.click(SELECT_PRODUCT_TAG);
        driverTools.waitForElementToAppear(PRODUCT_TAG_FIRST_OPTION);
        driverTools.clickWithJavascriptExecutor(PRODUCT_TAG_FIRST_OPTION);
        driverTools.waitForElementToAppear(SELECT_FACILITIES_DROPDOWN);
        driverTools.scrollToElement(SELECT_FACILITIES_DROPDOWN);
        driverTools.click(SELECT_FACILITIES_DROPDOWN);
        driverTools.waitForElementToAppear(SELECT_FACILITIES_OPTION);
        driverTools.clickWithJavascriptExecutor(SELECT_FACILITIES_OPTION);
    }

    public void goToEditProduct() {
        driverTools.waitForElementToAppear(PRODUCT_EDIT_BTN);
        driverTools.clickWithJavascriptExecutor(PRODUCT_EDIT_BTN);
    }

    public boolean isProductUpdated() {
        return driverTools.isElementDisplayed(UPDATE_SUCCESS_ALERT);
       }
   
    public void clickOkOnSuccessPopup() {
        driverTools.click(OK_BUTTON);
    }

    public void enterSupplierProduct(String supplier) {
        driverTools.waitForElementToAppear(SUPPLIER_INPUT_2);
        driverTools.sendKeys(SUPPLIER_INPUT_2, supplier);

        driverTools.clickWithJavascriptExecutor(SUPPLIER_BMG);
    }

    public void clickUpdateSupplierProduct() {
        driverTools.waitForElementToAppear(UPDATE_SUPPLIER_BUTTON,10);
        driverTools.click(UPDATE_SUPPLIER_BUTTON);
    }

    public boolean isSupplierProductUpdated() {
        driverTools.waitForElementToAppear(UPDATE_SUCCESS_ALERT,30);
        return driverTools.isElementDisplayed(UPDATE_SUCCESS_ALERT);
    }

    public void clickSessionRadio() {
        driverTools.waitForElementToAppear(SESSION_RADIO);
        driverTools.clickWithJavascriptExecutor(SESSION_RADIO);
    }

    public void addScheduleSession(String quota, String hours, String minutes, String titleEn, String titleId, String detailsEn, String detailsId) {
        driverTools.waitForElementToAppear(UPDATE_SCHEDULE_BUTTON);
        driverTools.click(UPDATE_SCHEDULE_BUTTON);
        driverTools.waitForElementToAppear(ADD_SCHEDULE);
        driverTools.click(ADD_SCHEDULE);
        
        driverTools.waitForElementToAppear(DATE_PICKER);
        driverTools.click(DATE_PICKER);

        driverTools.scrollToElement(SESSION_RADIO);
        
        clickSessionRadio();
    
        driverTools.waitForElementToAppear(QUOTA_INPUT_SESSION);
        driverTools.sendKeys(QUOTA_INPUT_SESSION, quota);
        
        driverTools.waitForElementToAppear(INPUT_HOURS_SESSION);
        driverTools.sendKeys(INPUT_HOURS_SESSION, hours);

        driverTools.waitForElementToAppear(INPUT_MINUTES_SESSION);
        driverTools.sendKeys(INPUT_MINUTES_SESSION, minutes);

        driverTools.waitForElementToAppear(SESSION_TITLE_INPUT_EN);
        driverTools.sendKeys(SESSION_TITLE_INPUT_EN, titleEn);

        driverTools.waitForElementToAppear(SESSION_TITLE_INPUT_ID);
        driverTools.sendKeys(SESSION_TITLE_INPUT_ID, titleId);

        driverTools.waitForElementToAppear(SESSION_DETAIL_INPUT_EN_RTE);
        driverTools.clickWithJavascriptExecutor(SESSION_DETAIL_INPUT_EN_RTE);
        driverTools.sendKeys(SESSION_DETAIL_INPUT_EN_RTE, detailsEn);
        
        driverTools.waitForElementToAppear(SESSION_DETAIL_INPUT_ID_RTE);
        driverTools.clickWithJavascriptExecutor(SESSION_DETAIL_INPUT_ID_RTE);
        driverTools.sendKeys(SESSION_DETAIL_INPUT_ID_RTE, detailsId);
        
        driverTools.scrollToElement(SUBMIT);
        driverTools.click(SUBMIT);
        driverTools.waitForElementToAppear(BUTTON_SAVE_AND_PUBLISH, 2);
        driverTools.click(BUTTON_SAVE_AND_PUBLISH);
    }

    public void switchToEditProductWindow() {
        driverTools.switchToWindowContainingUrl("edit-product");
    }

}
