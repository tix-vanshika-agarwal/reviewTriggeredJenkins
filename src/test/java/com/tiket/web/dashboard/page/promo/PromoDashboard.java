package com.tiket.web.dashboard.page.promo;

import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.bson.Document;
import org.bson.types.ObjectId;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.gson.Gson;
import com.mongodb.client.FindIterable;
import com.tiket.core.CoreFunctions;
import static com.tiket.core.CoreFunctions.capitalize;
import static com.tiket.core.CoreFunctions.generateRandomInt;
import static com.tiket.core.CoreFunctions.getCurrentDateDD_MM_YYYY;
import static com.tiket.core.CoreFunctions.getCurrentTime;
import static com.tiket.core.CoreFunctions.isBrowserAlertPresent;
import static com.tiket.core.CoreFunctions.randomInt;
import static com.tiket.core.CoreFunctions.removeText;
import com.tiket.dbclient.TiketMongoDBManager;
import com.tiket.model.Deeplink;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.model.TestDataElement;
import com.tiket.pojo.discount.PromoCodeDataResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import com.tiket.web.dashboard.page.member_dashboard.MemberLoginPage;
import com.tiket.web.dashboard.page.payment.PaymentDashboardLoginPage;

import jdk.jshell.spi.ExecutionControl.NotImplementedException;

public class PromoDashboard <T extends WebDriver> extends BasePage<T> {
    private static final String STATUS_ACTIVE = "Active";
    private static final String STATUS_INACTIVE = "Inactive";
    private static final String STATUS_IN_APPROVAL = "In Approval";
    private static final String ANY_STATUS = "";
    private static final String PROMO_CREATION_TEST_DATA = "verifyCreatingInstantPromo";

    private static final String adminPath = "/admin/member";
    private static final String FIXED_VALUE_FIELD_ID = "fixed-value";
    private static final String COST_ALLOCATION_FIELD_ID = "select-cost-allocation";
    private static final String INSTANT_POINT_CASHBACK_TOGGLE_TESTID_PREFIX = "toggle-instant-point-cashback-";
    private static final String PROMO_DETAIL_ROOT_XPATH = "//div[@data-testid='promo-detail-id'"
            + " or @data-testid='promo-approval-detail-id']";
    private static final Locator COST_ALLOC_FIXED_VALUE_LOCATOR = new Locator(
            "COST_ALLOC_FIXED_VALUE",
            By.xpath("//input[contains(@name, 'Allocation.partnerCost.value')]"));
    private static final Locator RESET_FILTER_BUTTON = new Locator("RESET_FILTER_BUTTON",
            By.xpath("//button[contains(text(),'Reset Filter')]"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON",
            By.xpath("//button[contains(text(),'Search')]"));
    private static final Locator SEARCH_PROMO = new Locator("SEARCH_PROMO",
            By.xpath("//input[@id='filter-input-code-text']"));
    private static final Locator SEARCH_LINKING = new Locator("SEARCH_LINKING",
            By.xpath("//input[@id='filter-input-name-text']"));
    private static final Locator SEARCH_PROMO_TYPE = new Locator("SEARCH_PROMO_TYPE",
            By.xpath("//input[@id='type-of-promo']"));
    private static final Locator SEARCH_PROMO_STATUS = new Locator("SEARCH_PROMO_STATUS",
            By.xpath("//input[@id='status']"));
    private static final Locator SEARCH_PROMO_DATE_CREATED = new Locator(
            "SEARCH_PROMO_DATE_CREATED", By.xpath("//input[@id='filter-date-startDate-date']"));
    private static final Locator SEARCH_PROMO_DATE_ENDED = new Locator("SEARCH_PROMO_DATE_ENDED",
            By.xpath("//input[@id='filter-date-endDate-date']"));
    private static final Locator DASH_LOGO = new Locator("DASH_LOGO",
            By.xpath("//img[@alt='logo_tiket']"));
    private static final Locator NEW_DASHBOARD = new Locator("NEW_DASHBOARD",
            By.xpath("//a[contains(@href,'/admin/discount_v2/promo')"
                    + " and not(contains(@href,'/cs/'))"
                    + " and not(contains(@href,'/approval'))"
                    + " and not(contains(@href,'/linking'))"
                    + " and not(contains(@href,'/add'))"
                    + " and not(contains(@href,'adjustment-rule'))]"));
    private static final Locator B2B_DASHBOARD = new Locator("B2B_DASHBOARD",
            By.xpath("//a[@href='/admin/b2b-dashboard']"));
    private static final Locator APPROVAL_PAGE = new Locator("APPROVAL_PAGE",
            By.xpath("//a[@href='/admin/discount_v2/promo/approval']"));
    private static final Locator LINKING_PAGE = new Locator("LINKING_PAGE",
        By.xpath("//a[@href='/admin/discount_v2/promo/linking']"));
    private static final Locator PROMO_ADJUSTMENT_RULE_PAGE = new Locator("PROMO_ADJUSTMENT_RULE_PAGE",
            By.xpath("//a[@href='/admin/discount_v2/promo/adjustment-rule']"));
    private static final Locator ADD_NEW_RULE_BUTTON = new Locator("ADD_NEW_RULE_BUTTON",
            By.xpath("//button[contains(text(), 'Add new rule')]"));
    private static final Locator RULE_NAME_INPUT = new Locator("RULE_NAME_INPUT",
            By.xpath("//input[@name='ruleName']"));
    private static final Locator CONDITION_DROPDOWN = new Locator("CONDITION_DROPDOWN",
            By.xpath("//div[contains(@class, 'styles_input_inner_wrapper') and ./*[text()='Condition']]//input"));
    private static final Locator OPERATOR_DROPDOWN = new Locator("OPERATOR_DROPDOWN",
            By.xpath("//div[@data-testid='dropdown-btn-operator']//input"));
    private static final Locator DROPDOWN_SELECTIONS_CONTAINER = new Locator("DROPDOWN_SELECTIONS_CONTAINER",
            By.xpath("//div[contains(@class, 'styles_dropdown_options')]"));
    private static final Locator DROPDOWN_SELECTIONS = new Locator("DROPDOWN_SELECTIONS",
            By.xpath("//div[contains(@class, 'styles_dropdown_options')]/div"));
    private static final Locator ADJUSTMENT_RULE_OPEN_DROPDOWN_PANEL = new Locator(
            "ADJUSTMENT_RULE_OPEN_DROPDOWN_PANEL",
            By.xpath("(//div[contains(@class, 'styles_dropdown_options')])[last()]"));
    private static final Locator ADJUSTMENT_RULE_OPEN_DROPDOWN_ROWS = new Locator(
            "ADJUSTMENT_RULE_OPEN_DROPDOWN_ROWS",
            By.xpath("(//div[contains(@class, 'styles_dropdown_options')])[last()]/div"));
    private static final Locator CONDITION_VALUE_DROPDOWN = new Locator("CONDITION_VALUE_DROPDOWN",
            By.xpath("//div[contains(@class, 'styles_input_inner_wrapper') and ./*[text()='Value']]//input"));
    private static final Locator ADJUSTMENT_VARIABLE_DROPDOWN = new Locator("ADJUSTMENT_VARIABLE_DROPDOWN",
            By.xpath("//div[contains(@class, 'styles_input_inner_wrapper') and ./*[text()='Variable']]//input"));
    private static final Locator ADJUSTMENT_VALUE_INPUT = new Locator("ADJUSTMENT_VALUE_INPUT",
            By.xpath("//div[contains(@class, 'styles_input_inner_wrapper') and ./*[text()='Adjustment']]//input"));
    private static final Locator NEXT_BUTTON = new Locator("NEXT_BUTTON",
            By.xpath("//button[contains(@class, 'Button_variant_primary') and text()='Next']"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON",
            By.xpath("//button[contains(@class, 'Button_variant_primary') and text()='Save']"));
    private static final Locator SAVE_CONFIRM_BUTTON = new Locator("SAVE_CONFIRM_BUTTON",
            By.xpath("//section[contains(@class, 'Modal_modal_popup')]//button[contains(@class, 'Button_variant_primary') and text()='Save']"));
    private static final Locator SEARCH_RULE_NAME_INPUT = new Locator("SEARCH_RULE_NAME_INPUT",
            By.xpath("//input[@data-testid='filter-input-param-text' or @id='filter-input-param-text']"));
    private static final Locator RULE_SETTING_SELECTIONS_CONTAINER = new Locator("RULE_SETTING_SELECTIONS_CONTAINER",
            By.xpath("//div[@data-testid='dropdown-content']"));
    private static final String DROPDOWN_OPTION_ROW_CLASS = "styles_dropdown_option";
    private static final Locator RULE_SETTING_SELECTIONS = new Locator("RULE_SETTING_SELECTIONS",
            By.xpath("//div[contains(@class, '" + DROPDOWN_OPTION_ROW_CLASS + "')]"));
    private static final By DROPDOWN_OPTION_ROWS_UNDER_PORTAL =
            By.xpath(".//div[contains(@class,'" + DROPDOWN_OPTION_ROW_CLASS + "')]");
    private static final Locator RULE_DELETE_BUTTON = new Locator("RULE_DELETE_BUTTON",
            By.xpath("//section[contains(@class, 'Modal_modal_popup')]//button[text()='Delete']"));
    private static final Locator ADJUSTMENT_RULE_TABLE_RULE_NAME_PRIMARY_TEXT = new Locator(
            "ADJUSTMENT_RULE_TABLE_RULE_NAME_PRIMARY_TEXT",
            By.xpath("//td[contains(@data-testid,'ruleName')]//div/p[1]"));
    private static final Locator RULE_DETAIL_MODAL_CLOSE_BUTTON = new Locator("RULE_DETAIL_MODAL_CLOSE_BUTTON",
            By.xpath("//button[@data-testid='modal-close-button']"));
    private static final Locator RULE_DETAIL_MODAL_EDIT_BUTTON = new Locator("RULE_DETAIL_MODAL_EDIT_BUTTON",
            By.xpath("//section[contains(@class, 'Modal_modal_popup')]//button[text()='Edit']"));
    private static final Locator PROMO_ADJUSTMENT_RULES_SET_DEFAULT_BUTTON = new Locator("PROMO_ADJUSTMENT_RULES_SET_DEFAULT_BUTTON",
            By.xpath("//section[contains(@class, 'Modal')]//button[text()='Set']"));
    private static final Locator PROMO_ADJUSTMENT_RULES_REMOVE_DEFAULT_BUTTON = new Locator("PROMO_ADJUSTMENT_RULES_REMOVE_DEFAULT_BUTTON",
            By.xpath("//section[contains(@class, 'Modal')]//button[text()='Remove']"));
    private static final Locator INSTANT_POINT_CASHBACK_PAGE = new Locator("INSTANT_POINT_CASHBACK_PAGE",
            By.xpath("//a[@href='/admin/discount_v2/promo/instant-point-cashback']"));
    private static final Locator APPROVAL_PAGE_STATUS = new Locator("APPROVAL_PAGE_STATUS",
            By.xpath("//input[@id='status']"));
    private static final Locator PROMO_SELECTED = new Locator("PROMO_SELECTED",
            By.xpath("//tbody/tr[1]/td[2]/p"));
    private static final Locator APPROVAL_PAGE_STATUS_INAPPROVAL = new Locator(
            "APPROVAL_PAGE_STATUS_INAPPROVAL", By.xpath("//span[text()='In Approval']"));
    private static final Locator APPROVE_BUTTON = new Locator("APPROVE_BUTTON",
            By.xpath("//button[contains(text(),'Approve')]"));
    private static final Locator SEND_FOR_EDIT_BUTTON = new Locator("SEND_FOR_EDIT_BUTTON",
            By.xpath("//button[contains(text(),'Send For Edit')]"));
    private static final Locator PROMO_COUNT = new Locator("PROMO_COUNT",
            By.xpath("//p[contains(text(),'Showing')]"));
    private static final Locator APPROVAL_POP_UP = new Locator("APPROVAL_POP_UP",
            By.xpath("//div[contains(@class, 'Snackbar_text_content')]"));
    private static final Locator APPROVAL_MODAL_APPROVE = new Locator("APPROVAL_MODAL_APPROVE",
            By.xpath("//footer//button[contains(text(),'Approve')]"));
    private static final Locator PROMO_NAME = new Locator("PROMO_NAME", new By.ByXPath(
            "//div[@class='flex flex-col justify-between align-content sm:flex-row']//span"));
    private static final Locator OVERVIEW_STATUS = new Locator("OVERVIEW_STATUS",
            By.xpath("//div[@data-testid=\"status-label\"]"));
    private static final Locator OVERVIEW_STATUS_HEADING = new Locator("OVERVIEW_STATUS_HEADING",
            By.xpath("//div[@class=\"pr-2\"]//span[text()='Status']"));
    private static final Locator OVERVIEW_CURRENCY = new Locator("OVERVIEW_CURRENCY",
            By.xpath("//span[text()='Currency']"));
    private static final Locator OVERVIEW_PROMO_TYPE = new Locator("OVERVIEW_PROMO_TYPE",
            By.xpath("//span[text()='Promo Type']"));
    private static final Locator ACTIVE_PROMO = new Locator("ACTIVE_PROMO",
            By.xpath("//span[text()='Active']"));
    private static final Locator INACTIVE_PROMO = new Locator("INACTIVE_PROMO",
            By.xpath("//span[text()='Inactive']"));
    private static final Locator INACTIVE_MODAL_BUTTON = new Locator("INACTIVE_MODAL_BUTTON",
            By.xpath("//button[text()='Inactive']"));
    private static final Locator CAR_ADMIN_LOCATOR = new Locator("CAR_ADMIN_LOCATOR",
            By.xpath("//*[text()='Car Admin']"));
    private static final Locator ELEMENT_LOCATOR = new Locator("ELEMENT_LOCATOR",
            By.xpath("//*[contains(@class,'cursor-pointer ')]"));
    private static final Locator DATE_LOCATOR = new Locator("DATE_LOCATOR",
            By.xpath("//span[text()='Date Created']/../following-sibling::*"));
    private static final Locator TYPE_LOCATOR = new Locator("TYPE_LOCATOR",
            By.xpath("//span[text()='Promo Type']/../following-sibling::*"));
    private static final Locator CURRENCY_LOCATOR = new Locator("CURRENCY_LOCATOR",
            By.xpath("//span[text()='Currency']/../following-sibling::*"));
    private static final Locator CREATE_NEW_PROMO_LOCATOR = new Locator("CREATE_NEW_PROMO_LOCATOR",
            By.xpath("//*[text()='Create New Promo']"));
    private static final Locator CREATE_UNIQUE_LINK_BUTTON = new Locator(
            "CREATE_UNIQUE_LINK_BUTTON", By.xpath("//*[text()='Create Unique Link']"));
    private static final Locator PROMO_TYPE_DROPDOWN_LOCATOR = new Locator(
            "PROMO_TYPE_DROPDOWN_LOCATOR", By.xpath("//*[@id='type-of-promo']"));
    private static final Locator CAMPAIGN_NAME_LOCATOR = new Locator("CAMPAIGN_NAME_LOCATOR",
            By.xpath("//*[@id='campaign-name']"));
    private static final Locator UNIQUE_IDENTIFIER_LOCATOR = new Locator(
            "UNIQUE_IDENTIFIER_LOCATOR", By.xpath("//*[@id='unique-identifier-or-unique-link-name']"));
    private static final Locator PROMO_NAME_INPUT_LOCATOR = new Locator("PROMO_NAME_INPUT_LOCATOR",
            By.xpath("//*[@id='promo-code']"));
    private static final Locator CANCEL_BUTTON_LOCATOR = new Locator("CANCEL_BUTTON_LOCATOR",
            By.xpath("//button[text()='Cancel']"));
    private static final Locator DISCOUNT_DASHBOARD_LINK = new Locator("DISCOUNT_DASHBOARD_LINK",
            By.xpath("//a[normalize-space()='Discount New Dashboard']"));
    private static final Locator BLACKLIST_PAGE_LINK = new Locator("BLACKLIST_PAGE_LINK",
            By.xpath("//*[@data-testid='submenu-id-/promo/blacklist']//a"));
    private static final Locator PROMO_BLACKLIST_PAGE_TITLE = new Locator("PROMO_BLACKLIST_PAGE_TITLE",
            By.xpath("//h1[text()='Promo Blacklist']"));
    private static final Locator SEARCH_PROMO_BLACKLIST_INPUT = new Locator("SEARCH_PROMO_BLACKLIST_INPUT",
            By.xpath("//input[@data-testid='filter-input-promoCode-text']"));
    private static final Locator SEARCH_PROMO_BLACKLIST_BUTTON = new Locator("SEARCH_PROMO_BLACKLIST_BUTTON",
            By.xpath("//button[@data-testid='filter-submit-btn']"));
    private static final Locator PROMO_BLACKLIST_TABLE_ROWS = new Locator("PROMO_BLACKLIST_TABLE_ROWS",
            By.xpath("//table[@data-testid='table-control']/tbody/tr"));
    private static final Locator SAVE_AS_DRAFT_BUTTON_LOCATOR = new Locator(
            "SAVE_AS_DRAFT_BUTTON_LOCATOR", By.xpath("//*[text()='Save as Draft']"));
    private static final Locator SUCCESS_MESSAGE_LOCATOR = new Locator("SUCCESS_MESSAGE_LOCATOR",
            By.xpath("//*[contains(@class,'dcXu0G_text_content')]"));
    private static final Locator CASHBACK_IN_PROMO_TYPE_DROPDOWN_LOCATOR = new Locator(
            "CASHBACK_IN_PROMO_TYPE_DROPDOWN_LOCATOR", By.xpath("//*[text()='Cashback']"));
    private static final Locator DESCRIPTION_BOX_LOCATOR = new Locator("DESCRIPTION_BOX_LOCATOR",
            By.xpath("//*[@id='description-optional']"));
    private static final Locator ADD_VALUES_BUTTON_LOCATOR = new Locator(
        "ADD_VALUES_BUTTON_LOCATOR", By.xpath("//*[text()='Add Values']"));
    private static final Locator VERIFIED_RADIOBOX_LOCATOR = new Locator(
            "VERIFIED_RADIOBOX_LOCATOR",
            By.xpath("//input[contains(@value,'VERIFIED')]/following-sibling::span"));
    private static final Locator PERCENTAGE_RADIO_LOCATOR = new Locator("PERCENTAGE_RADIO_LOCATOR",
            By.xpath("//input[contains(@value,'percentageAmount')]/following-sibling::span"));
    private static final Locator USERNAME_LOCATOR = new Locator("USERNAME_LOCATOR",
            By.xpath("//*[text()='Username']"));
    private static final Locator EQUAL_TO_LOCATOR = new Locator("EQUAL_TO_LOCATOR",
            By.xpath("//*[contains(text(),'Equal to')]"));
    private static final Locator EQUAL_TO_MULTI_VALUE_LOCATOR = new Locator(
            "EQUAL_TO_MULTI_VALUE_LOCATOR", By.xpath("//*[contains(text(),'Equal to (Multi Value)')]"));
    private static final Locator EN_DESC_LOCATOR = new Locator("EN_DESC_LOCATOR",
            By.xpath("//*[@id='description'][1]"));
    private static final Locator ID_DESC_LOCATOR = new Locator("ID_DESC_LOCATOR",
            By.xpath("(//*[@id='description'])[2]"));
    private static final Locator EN_TERMS_LOCATOR = new Locator("EN_TERMS_LOCATOR",
            By.xpath("(//*[@data-placeholder='Terms and Condition'])[1]"));
    private static final Locator ID_TERMS_LOCATOR = new Locator("ID_TERMS_LOCATOR",
            By.xpath("(//*[@data-placeholder='Terms and Condition'])[2]"));
    private static final Locator SAVE_BUTTON_LOCATOR = new Locator("SAVE_BUTTON_LOCATOR",
            By.xpath("//*[text()='Save']"));
    private static final Locator ADD_PERIOD_BUTTON_LOCATOR = new Locator(
            "ADD_PERIOD_BUTTON_LOCATOR", By.xpath("//*[text()='Add Period']"));
    private static final Locator PREVIEW_BUTTON_LOCATOR = new Locator("PREVIEW_BUTTON_LOCATOR",
            By.xpath("//button[text()='Preview']"));
    private static final Locator TOP_LOCATOR = new Locator("TOP_LOCATOR",
            By.xpath("//nav"));
    private static final Locator BOTTOM_LOCATOR = new Locator("BOTTOM_LOCATOR",
            By.xpath("//span[text()='PT Global Tiket Network']"));
    private static final Locator PERCENTAGE_LOCATOR = new Locator("PERCENTAGE_LOCATOR",
            By.xpath("((//span[text()='Cashback'])[5]/following-sibling::*)[2]"));
    private static final Locator PROMO_LIST_LOCATOR = new Locator("PROMO_LIST_LOCATOR",
            By.xpath("//a[text()='Promo List']"));
    private static final Locator LEAVE_BUTTON_LOCATOR = new Locator("LEAVE_BUTTON_LOCATOR",
            By.xpath("//button[text()='Leave']"));
    private static final Locator CREATE_CAMPAIGN_BUTTON_LOCATOR = new Locator(
            "CREATE_CAMPAIGN_BUTTON_LOCATOR", By.xpath("//button[text()='Create Campaign']"));
    private static final Locator APPROVAL_BUTTON_LOCATOR = new Locator("APPROVAL_BUTTON_LOCATOR",
            By.xpath("//button[text()='Send for Approval']"));
    private static final Locator GENERAL_INFO_FIELDS = new Locator("GENERAL_INFO_FIELDS",
            new By.ByXPath("//span[text()='General Information']/following-sibling::div/div/div[1]"));
    private static final Locator FRAUD_PREVENTION_FIELDS = new Locator("FRAUD_PREVENTION_FIELDS",
            new By.ByXPath("//span[text()='Fraud Prevention']/following-sibling::div/div/div[1]"));
    private static final Locator PROMO_AFFILIATE_SECTION_HEADING = new Locator(
            "PROMO_AFFILIATE_SECTION_HEADING", By.xpath("//span[text()='Promo Affiliate']"));
    private static final Locator PROMO_AFFILIATE_FIELD_LABELS = new Locator(
            "PROMO_AFFILIATE_FIELD_LABELS",
            By.xpath("//span[text()='Promo Affiliate']/following-sibling::div/div/div[1]"));
    private static final Locator PROMO_DETAIL_ID = new Locator("PROMO_DETAIL_ID",
            By.xpath(PROMO_DETAIL_ROOT_XPATH));
    private static final Locator PROMO_DETAIL_RULE_SET_TABLE_BODY_ROWS = new Locator(
            "PROMO_DETAIL_RULE_SET_TABLE_BODY_ROWS",
            By.xpath(PROMO_DETAIL_ROOT_XPATH
                    + "//span[contains(.,'Promo Rules')]/ancestor::div[contains(@class,'Card_card_container')][1]"
                    + "//div[contains(@class,'Tabs_tab_panel_active')]//tbody/tr"));
    private static final Locator PROMO_DETAIL_RULE_SET_ACTIVE_PANEL = new Locator(
            "PROMO_DETAIL_RULE_SET_ACTIVE_PANEL",
            By.xpath(PROMO_DETAIL_ROOT_XPATH
                    + "//span[contains(.,'Promo Rules')]/ancestor::div[contains(@class,'Card_card_container')][1]"
                    + "//div[contains(@class,'Tabs_tab_panel_active')]"));
    private static final Locator PROMO_RULES_SECTION_HEADING = new Locator(
            "PROMO_RULES_SECTION_HEADING",
            By.xpath(PROMO_DETAIL_ROOT_XPATH + "//span[contains(.,'Promo Rules')]"));
    private static final Locator PROMO_RULES_CARD_FOR_TABS = new Locator(
            "PROMO_RULES_CARD_FOR_TABS",
            By.xpath(PROMO_DETAIL_ROOT_XPATH
                    + "//span[contains(.,'Promo Rules')]/ancestor::div[contains(@class,'Card_card_container')][1]"));
    private static final Locator PROMO_DETAIL_TNC = new Locator("PROMO_DETAIL_TNC", new By.ByXPath(
            "//span[text()='Terms and Condition']/parent::div/following-sibling::div//p"));
    private static final Locator EDIT_BUTTON_LOCATOR = new Locator("EDIT_BUTTON_LOCATOR",
            By.xpath("//button[text()='Edit']"));
    private static final Locator DELETE_BUTTON_LOCATOR = new Locator("DELETE_BUTTON_LOCATOR",
            By.xpath("//button[text()='Delete']"));
    private static final Locator MODAL_DELETE_BUTTON_LOCATOR = new Locator(
            "MODAL_DELETE_BUTTON_LOCATOR",
            By.xpath("//footer[contains(@class,'modal_footer')]//button[text()='Delete']"));
    private static final Locator SAVE_CHANGES_BUTTON_LOCATOR = new Locator(
            "SAVE_CHANGES_BUTTON_LOCATOR", By.xpath("//button[text()='Save Changes']"));
    private static final Locator SAVE_CHANGES_BUTTON_POPUP_LOCATOR = new Locator(
            "SAVE_CHANGES_BUTTON_POPUP_LOCATOR", By.xpath("(//button[text()='Save Changes'])[2]"));
    private static final Locator PROMO_CODE_NAME_LOCATOR = new Locator("PROMO_CODE_NAME_LOCATOR",
            By.xpath("//*[@id='promo-code']"));
    private static final Locator ACTION_DROPDOWN_LOCATOR = new Locator("ACTION_DROPDOWN_LOCATOR",
            By.xpath("//div[@data-testid='dropdown-btn-default']//p[text()='Setting']"));
    private static final Locator INACTIVATE_MODAL_HEADER = new Locator("INACTIVATE_MODAL_HEADER",
            By.xpath("//section//h2[contains(text(), 'Are you sure you want to inactivate this campaign')]"));
    private static final Locator INACTIVATE_MODAL_BUTTON = new Locator("INACTIVATE_MODAL_BUTTON",
            By.xpath("//footer//button[text()='Inactivate']"));
    private static final Locator SAVE_EDIT_BUTTON = new Locator("SAVE_EDIT_BUTTON",
            By.xpath("//button[text()='Save Changes']"));
    private static final Locator SAVE_EDIT_MODAL_BUTTON = new Locator("SAVE_EDIT_MODAL_BUTTON",
            By.xpath("//footer//button[text()='Save Changes']"));
    private static final Locator CLONE_BUTTON_LOCATOR = new Locator("CLONE_BUTTON_LOCATOR",
            By.xpath("//button[text()='Clone']"));
    private static final Locator CAMPAIGN_NAME_FIELD_LOCATOR = new Locator(
            "CAMPAIGN_NAME_FIELD_LOCATOR", By.xpath("//input[@id='campaign-name']"));
    private static final Locator PROMO_CODE_FIELD_LOCATOR = new Locator("PROMO_CODE_FIELD_LOCATOR",
            By.xpath("//input[@id='promo-code']"));
    private static final Locator PREVIEW_PROMO_CODE_FIELD_LOCATOR = new Locator(
            "PREVIEW_PROMO_CODE_FIELD_LOCATOR",
            By.xpath("//div[@class='flex flex-row py-3'][1]//div[2]//span"));
    private static final Locator MODAL_HEADER_TEXT = new Locator("MODAL_HEADER_TEXT",
            By.xpath("//header[contains(@class , 'modal_header')]//h2"));
    private static final Locator PROMO_STATUS_TEXT = new Locator("PROMO_STATUS_TEXT",
            By.xpath("//span[text()='Status']/parent::div/following-sibling::div/div"));
    private static final Locator TAB_CHANGES_HISTORY = new Locator("TAB_CHANGES_HISTORY",
            By.xpath("//*[text()='Changes History']/parent::button"));
    private static final Locator TOGGLE_EDIT_CAMPAIGN_PERIOD = new Locator(
        "TOGGLE_EDIT_CAMPAIGN_PERIOD",
        By.xpath("//input[@name='editQuotaAndCampaignPeriods']/following-sibling::div"));
    private static final Locator NO_DATA_PROMO_LIST = new Locator("NO_DATA_PROMO_LIST",
            By.xpath("//*[contains(text(),'no data')]"));
    private static final Locator PROMO_DETAIL_LOADING = new Locator("PROMO_DETAIL_LOADING",
            By.xpath("//*[contains(@data-testid,'loading')]"));
    private static final Locator ACCORDION_CHANGES_HISTORY = new Locator(
            "ACCORDION_CHANGES_HISTORY", By.xpath(
            "//*[@data-testid='accordion-changes-history']//*[contains(@class,'accordion_button')]"));
    private static final Locator CHANGES_HISTORY_DATE_ACTOR = new Locator(
            "CHANGES_HISTORY_DATE_ACTOR", By.xpath(
            "//*[@data-testid='accordion-changes-history']//div[contains(@class,'Accordion_accordion_button')]//span[not(contains(@class,'px-2'))]"));
    private static final Locator FOOTER = new Locator("FOOTER",
            By.xpath("//*[@data-testid='footer']"));
    private static final Locator CHANGES_HISTORY_ACTIVITY = new Locator("CHANGES_HISTORY_ACTIVITY",
            By.xpath(
                    "//*[@aria-hidden='false']//span[text()='Activity']/parent::div/following-sibling::div//span"));
    private static final Locator DISCOUNT_AMOUNT_LOCATOR = new Locator("DISCOUNT_AMOUNT_LOCATOR",
            By.xpath("//span[text()='Discount']/parent::td/following-sibling::td"));
    private static final Locator EDIT_CAMPAIGN_TOGGLE_BUTTON = new Locator(
            "EDIT_CAMPAIGN_TOGGLE_BUTTON",
            By.xpath("//input[@name='editQuotaAndCampaignPeriods']/following-sibling::div"));
    private static final Locator FIRST_CAMPAIGN_PERIOD_DELETE_BUTTON = new Locator(
            "FIRST_CAMPAIGN_PERIOD_DELETE_BUTTON",
            By.xpath("//*[normalize-space()='Campaign Period 1']/following::*[normalize-space()='Delete'][1]"));
    private static final Locator DUPLICATE_RULE_SET_LOCATOR = new Locator("DUPLICATE_RULE_SET_LOCATOR", By.xpath("//button[text()='Duplicate Rule Set']"));
    private static final Locator REMAINING_QUOTA_TABLE = new Locator("REMAINING_QUOTA_TABLE",
            By.xpath("//td[@data-testid='row-cell-0-promoCodeDataQuotaLevel']//p"));
    private static final Locator INPUT_ADD_QUOTA_FIELD = new Locator("INPUT_ADD_QUOTA_FIELD",
            By.xpath("//input[@id='add-quota']"));
    private static final Locator ADD_ADD_QUOTA_BUTTON = new Locator("ADD_ADD_QUOTA_BUTTON",
            By.xpath("//button[text()='Add']"));
    private static final Locator ADD_QUOTA_TODAY_RADIO = new Locator("ADD_QUOTA_TODAY_RADIO",
            By.xpath("//input[contains(@value,'SINGLE_DAY')]/parent::span"));
    private static final Locator UNIQUE_LINK_NAME_INPUT = new Locator("UNIQUE_LINK_NAME_INPUT",
            By.xpath("//input[@id='unique-link-name']"));
    private static final Locator CREATE_LINK_SUBMIT = new Locator("CREATE_LINK_SUBMIT",
            By.xpath("//button[@data-testid='promo-link-btn-save']"));
    private static final Locator STATUS_IN_TABLE = new Locator("STATUS_IN_TABLE",
            By.xpath("//div[@data-testid='status-label']"));
    private static final Locator LIST_LOADING = new Locator("LIST_LOADING",
            By.xpath("//*[contains(@data-testid,'list-loading')]"));
    private static final Locator COLLAPSIBLE_BUTTON = new Locator("COLLAPSIBLE_BUTTON", By.xpath(
            "//td[@data-testid='table-control-content-expandable-row-0']//*[local-name()='svg']"));
    private static final Locator PROMO_LINKING_QUOTA_DASH = new Locator("PROMO_LINKING_QUOTA_DASH",
            By.xpath(
                    "//div[contains(@class,'linked_campaigns')]//span[text()='Quota']/parent::div/following-sibling::div[1]//span"));
    private static final Locator ADD_CAMPAIGN_LINK_BUTTON = new Locator("ADD_CAMPAIGN_LINK_BUTTON",
            By.xpath("//button[text()='Add Campaign']"));
    private static final Locator UPLOAD_INPUT = new Locator("UPLOAD_INPUT",
            By.xpath("//input[@data-testid='input-upload-file']"));
    private static final Locator UPLOAD_BOX = new Locator("UPLOAD_BOX",
            By.xpath("//div[contains(@class,'styles_upload_button')]"));
    private static final Locator GENERATE_TNC_BUTTON = new Locator("GENERATE_TNC_BUTTON",
            By.xpath("//button[text()='Generate promo description & TnC']"));
    private static final Locator ACTIVE_DATE = new Locator("ACTIVE_DATE",
        By.xpath("(//*[contains(@class, 'Day_day') and not(@disabled)])"));
    private static final Locator ANY_INSTANT_POINT_CASHBACK_VERTICAL_TOGGLE = new Locator(
            "ANY_INSTANT_POINT_CASHBACK_VERTICAL_TOGGLE",
            By.cssSelector("input[data-testid^='" + INSTANT_POINT_CASHBACK_TOGGLE_TESTID_PREFIX + "']"));
    private static final Locator ANY_INSTANT_POINT_CASHBACK_TOGGLE_LABEL = new Locator(
            "ANY_INSTANT_POINT_CASHBACK_TOGGLE_LABEL",
            By.xpath("//label[.//input[contains(@data-testid,'toggle-instant-point-cashback')]]"));
    private static final Locator SUBMIT_INSTANT_POINT_CASHBACK_VERTICAL_MODAL = new Locator("SUBMIT_INSTANT_POINT_CASHBACK_VERTICAL_MODAL", By.xpath("//section[contains(@class, 'Modal')]//button[text()='Proceed']"));
    private static final Locator TRANSACTION_VALUE_RADIOBOX_LOCATOR = new Locator(
            "TRANSACTION_VALUE_RADIOBOX_LOCATOR",
            By.xpath("//input[contains(@value,'TRANSACTION')]/following-sibling::span"));
    private static final Locator BUDGET_NAME_VALUE = new Locator("BUDGET_NAME_VALUE",
            By.xpath("//span[text()='Budget Name']/parent::div/following-sibling::div//span"));
    private static final Locator SEE_BANK_BIN_DETAILS = new Locator("SEE_BANK_BIN_DETAILS",
            By.xpath("//td[./span[text()='Bank BIN']]/following-sibling::td/button"));
    private static final Locator BANK_BIN_TABLE = new Locator("BANK_BIN_TABLE",
            By.xpath("//table[contains(@class, 'bin_table')]"));
    private static final Locator EDIT_QUOTA_CAMPAIGN_TOGGLE = new Locator("EDIT_QUOTA_CAMPAIGN_TOGGLE",
            By.xpath("//input[@name='editQuotaAndCampaignPeriods']/following-sibling::div"));
    private static final Locator PROMO_BLACKLIST_PAGINATION = new Locator("PROMO_BLACKLIST_PAGINATION",
            By.xpath("//div[@data-testid='cursor-pagination']"));
    private static final Locator PROMO_BLACKLIST_NEXT_BTN = new Locator("PROMO_BLACKLIST_NEXT_BTN",
            By.xpath("//div[@data-testid='cursor-pagination']//button[.//span[text()='Next']]"));
    private static final Locator PROMO_BLACKLIST_PREV_BTN = new Locator("PROMO_BLACKLIST_PREV_BTN",
            By.xpath("//div[@data-testid='cursor-pagination']//button[.//span[text()='Prev']]"));
    private static final Locator GROUP_PAGE_LINK = new Locator("GROUP_PAGE_LINK",
            By.xpath("//*[@data-testid='submenu-id-/promo/group']//a"));
    private static final Locator PROMO_GROUP_PAGE_TITLE = new Locator("PROMO_GROUP_PAGE_TITLE",
            By.xpath("//h1[text()='Promo Group']"));
    private static final Locator SEARCH_PROMO_GROUP_INPUT = new Locator("SEARCH_PROMO_GROUP_INPUT",
            By.xpath("//input[@data-testid='filter-input-groupName-text']"));
   private static final Locator ADD_BLACKLIST_BUTTON = new Locator("ADD_BLACKLIST_BUTTON",
            By.xpath("//button[.//span[text()='Add Blacklist']]"));
    private static final Locator UPLOAD_FILE_INPUT = new Locator("UPLOAD_FILE_INPUT",
            By.xpath("//input[@type='file' and @accept='.csv']"));
    private static final Locator MODAL_ADD_TO_BLACKLIST_BUTTON = new Locator("MODAL_ADD_TO_BLACKLIST_BUTTON",
            By.xpath("//footer[contains(@class, 'Modal_modal_footer')]//button[text()='Add to Blacklist']"));
    private static final Locator REMOVE_BLACKLIST_ROW_BUTTON = new Locator("REMOVE_BLACKLIST_ROW_BUTTON",
            By.xpath("//td[@data-testid='row-cell-0-action']//button[.//span[text()='Remove']]"));
    private static final Locator CONFIRM_DELETE_BLACKLIST_BUTTON = new Locator("CONFIRM_DELETE_BLACKLIST_BUTTON",
            By.xpath("//footer[contains(@class, 'Modal_modal_footer')]//button[text()='Delete']"));
    private static final Locator NO_DATA_MATCH_MESSAGE = new Locator("NO_DATA_MATCH_MESSAGE",
            By.xpath("//h2[text()='No data match']"));

    public int getBinTableColumnIndex(String colName) {
        Locator loc = new Locator("", By.xpath(
                "//table[contains(@class, 'bin_table')]/thead/tr/th[normalize-space()='" + colName + "']/preceding-sibling::*"));
        List<WebElement> webElements = driver.findElements(loc.by());
        return webElements.size() + 1;
    }

    private static final Locator CHECKER_PAGE = new Locator("CHECKER_PAGE",
            By.xpath("//a[@href='/admin/discount_v2/cs/promo']"));

    public Locator dropdownBoxLocator(String id) {
        return new Locator("DROPDOWN_LOCATOR", By.xpath("//*[@id='" + id + "']"));
    }

    public Locator inputLocator(String id){
        return new Locator("INPUT_LOCATOR", By.xpath("//*[@id='" + id + "']"));
    }

    public Locator inputLocator(String id, int element){
        return new Locator("INPUT_LOCATOR", By.xpath("//*[@id='" + id + "'][" + element + "]"));
    }

    public Locator inputLocatorDataTest(String id){
        return new Locator("INPUT_LOCATOR", By.xpath("//*[@data-testid='" + id + "']"));
    }

    private WebElement resolveDropdownOptionElement(String text) {
        String want = normalizeDropdownLabel(text);
        Optional<WebElement> fromPortal = findDropdownOptionInOpenPanel(
                RULE_SETTING_SELECTIONS_CONTAINER, DROPDOWN_OPTION_ROWS_UNDER_PORTAL, want);
        if (fromPortal.isPresent()) {
            return fromPortal.get();
        }
        for (int attempt = 0; attempt < 10; attempt++) {
            try {
                driverTools.waitForElementToAppear(DROPDOWN_SELECTIONS_CONTAINER);
                List<WebElement> selections = driverTools.waitForElementsToAppear(DROPDOWN_SELECTIONS).orElseThrow();
                for (WebElement selection : selections) {
                    if (want.equals(normalizeDropdownLabel(driverTools.getText(selection)))) {
                        return selection;
                    }
                }
                break;
            } catch (StaleElementReferenceException ignored) {
            }
        }
        throw new org.openqa.selenium.NoSuchElementException(
                "Option '" + text + "' not found in open dropdown (portal or DROPDOWN_SELECTIONS). "
                        + "Check test data spelling vs UI (e.g. Logged in vs Logged In).");
    }

    private static String normalizeDropdownLabel(String s) {
        if (s == null) {
            return "";
        }
        return s.strip().replaceAll("\\s+", " ").toLowerCase(Locale.ROOT);
    }

    private Optional<WebElement> findDropdownOptionInOpenPanel(
            Locator panelLocator, By optionRelativeXPath, String normalizedExpected) {
        final int maxAttempts = 12;
        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            try {
                Optional<WebElement> panel = driverTools.waitForElementToAppear(panelLocator, 5);
                if (panel.isEmpty()) {
                    continue;
                }
                List<WebElement> options = panel.get().findElements(optionRelativeXPath);
                int n = options.size();
                for (int i = 0; i < n; i++) {
                    Optional<WebElement> panelFresh = driverTools.waitForElementToAppear(panelLocator, 3);
                    if (panelFresh.isEmpty()) {
                        break;
                    }
                    List<WebElement> fresh = panelFresh.get().findElements(optionRelativeXPath);
                    if (i >= fresh.size()) {
                        break;
                    }
                    WebElement opt = fresh.get(i);
                    if (normalizedExpected.equals(normalizeDropdownLabel(driverTools.getText(opt)))) {
                        return Optional.of(opt);
                    }
                }
            } catch (StaleElementReferenceException ignored) {
            }
        }
        return Optional.empty();
    }

    public Locator insideDropdownFilterStatusLocator(String text) {
        return new Locator("SELECT_INSIDE_DROPDOWN_FILTER_STATUS_LOCATOR", By.xpath(String.format(
                "//div[@data-testid='dropdown-content']//*[text()='%s']/parent::span/preceding-sibling::span",
                text)));
    }

    public Locator dateLocator(String text) {
        return new Locator("DATE_LOCATOR",
                By.xpath("(//span[normalize-space()='" + text + "' and not(contains(@class,'disabled'))])[1]"));
    }

    public Locator previewSideLocator(String text) {
        if (text == "Discount") {
            return new Locator("PREVIEW_SIDE_LOCATOR",
                    By.xpath("(//span[text()='" + text + "']/../following-sibling::*)[2]"));
        }
        return new Locator("PREVIEW_SIDE_LOCATOR",
                By.xpath("//span[text()='" + text + "']/../following-sibling::*"));
    }

    public Locator getSpecificPromoLocator(String text) {
        return new Locator("ELEMENT_LOCATOR",
                By.xpath("//*[contains(@class,'cursor-pointer') and text()='" + text + "']"));
    }

    public Locator getFirstRowCellByColumn(String column) {
        return new Locator("GET_CELL_BY_COLUMN", new By.ByXPath(
                "//td[position() = count(//th[div/p[text()='" + column
                        + "']]/preceding-sibling::th) + 1]"));
    }

    public Locator actionDropdownItem(String actionType) {
        return new Locator("ACTION_DROPDOWN_ITEM", new By.ByXPath(
                "//td[@data-testid='row-cell-0-action']//*[text()='" + actionType + "']"));
    }

    public Locator getFieldsPromoDetails(String fieldName) {
        if ("Budget Name".equals(fieldName)) {
            return new Locator("FIELDS_PROMO_DETAIL", new By.ByXPath(
                    "//span[text()='Budget Name']/parent::div/following-sibling::div[1]"));
        }
        return new Locator("FIELDS_PROMO_DETAIL", new By.ByXPath(
                "//span[text()='" + fieldName + "']/parent::div/following-sibling::div/span"));
    }

    public Locator getFieldRuleSetValue(String index) {
        return new Locator("FIELD_RULE_SET", new By.ByXPath(
                "//div[@class='mt-10 w-full']//div[@class='-CoQhG_tab_panel -CoQhG_tab_panel_active']//tr/td["
                        + index + "]"));
    }

    public Locator getCampaignOverviewValueLocator(String key) {
        return new Locator("CAMPAIGN_OVERVIEW_VALUE_LOCATOR", new By.ByXPath(
                String.format("//span[text()='%s']/parent::div/following-sibling::div/span", key)));
    }

    public Locator getPromoRulesValueLocator(String key) {
        return new Locator("GET_PROMO_RULES_VALUE", new By.ByXPath(
                String.format("//span[text()='%s']/parent::td/following-sibling::td//span", key)));
    }

    public Locator radioButtonLocator(String value) {
        return new Locator("RADIO_BUTTON_LOCATOR", new By.ByXPath(
                String.format("//input[contains(@value,'%s')]/following-sibling::span", value)));
    }

    public Locator approvalTab(String tab) {
        return new Locator("APPROVAL_TAB_LOCATOR", new By.ByXPath(
                String.format("//section[contains(@class,'tab_container')]//span[text()='%s']", tab)));
    }

    public Locator getCampaignLinkDropdown(String number) {
        return new Locator("CAMPAIGN_LINK_DROPDOWN",
                new By.ByXPath(String.format("//input[@id='campaign-%s']/parent::div", number)));
    }

    public Locator getCampaignLinkInput(String number) {
        return new Locator("CAMPAIGN_LINK_INPUT",
                new By.ByXPath(String.format("//input[@id='campaign-%s']", number)));
    }

    public Locator getCampaignLinkDropdownValue(String value) {
        return new Locator("CAMPAIGN_LINK_DROPDOWN_VALUE",
                new By.ByXPath(String.format("//div[@data-testid='dropdown-item-%s']//p", value)));
    }

    public Locator getApproveButtonRow(String data) {
        return new Locator("APPROVE_BUTTON_ROW", new By.ByXPath(String.format(
                "//p[contains(text(),'%s')]/parent::td/following-sibling::td[contains(@data-testid,'action')]//button[text()='Approve']",
                data)));
    }

    public Locator getPromoCodeInTableLink(String promoCode) {
        return new Locator("PROMO_CODE_IN_TABLE_LINK", new By.ByXPath(
                String.format("//div[contains(@class,'styles_linked_campaigns')]//span[text()='%s']",
                        promoCode)));
    }

    private Locator getChangesHistoryValue(String fieldName) {
        return new Locator("CHANGES_HISTORY_" + fieldName.toUpperCase(), By.xpath(
                "//*[@aria-hidden='false']//span[text()='" + fieldName
                        + "']/parent::div/following-sibling::div//*[@data-testid]"));
    }

    private Locator getQuotaType(String fieldName) {
        return new Locator("QUOTA_TYPE_"+fieldName.toUpperCase(), By.xpath(String.format("//label[text()='%s']//parent::span//preceding-sibling::span", fieldName)));
    }

    private Locator getSelectPaymentMethod(int element) {
        return new Locator("SELECT_PAYMENT_METHOD_"+element, By.xpath("//input[contains(@value,'singlePayment')]/following-sibling::span["+element+"]"));
    }

    private Locator getRuleSettingDropdownByRuleName(String rule) {
        return new Locator("RULE_SETTING_DROPDOWN_" + rule, By.xpath("//tr[./td[2]//*[text()='" + rule + "']]/td[last()]/div"));
    }

    private Locator instantPointCashbackToggleLocator(String verticalKey) {
        return new Locator(
                "IPC_TOGGLE_" + verticalKey,
                By.cssSelector("input[data-testid='" + INSTANT_POINT_CASHBACK_TOGGLE_TESTID_PREFIX + verticalKey + "']"));
    }

    private Locator instantPointCashbackToggleKnobLocator(String verticalKey) {
        return new Locator(
                "IPC_TOGGLE_KNOB_" + verticalKey,
                By.xpath("//input[@data-testid='" + INSTANT_POINT_CASHBACK_TOGGLE_TESTID_PREFIX + verticalKey
                        + "']/ancestor::label//div[contains(@class,'Toggle_toggle')]"));
    }

    private String getRuleDetailPageAttributeValue(String attributeName) {
        return driverTools.getText(new Locator("RULE_ATTRIBUTE_" + attributeName, By.xpath("//section[contains(@class, 'Modal')]//span[contains(@class, Text_variant_lowEmphasis) and contains(text(), '" + attributeName + "')]/following-sibling::span"))).orElseThrow();
    }

    private boolean isDefaultNudgeOfAdjustmentRuleExist(String ruleName) {
        return driverTools.waitForElementToAppear(
                new Locator("DEFAULT_NUDGE" + ruleName, By.xpath("//tr[./td[2]//*[text()='" + ruleName + "']]//div[contains(@class, 'Label') and text()='Default']"))
        ).isPresent();
    }

    private String getPromoStatus(String status) throws NotImplementedException {
        String result = STATUS_MAP.get(status);
        if (result == null) {
            throw new NotImplementedException("Unimplemented status type!");
        }
        return result;
    }

    String EXPECTEDTEXT1 = "Promo must approved by CCO due to breach discount value limit";
    String EXPECTEDTEX2 = "Successfully approve the campaign.";
    private Map<String, String> overviewPromoDetail = new HashMap<>();
    private Map<String, String> generalInfoPromoDetail = new HashMap<>();
    private Map<String, String> fraudPreventionPromoDetail = new HashMap<>();
    private Map<String, String> promoCodePromoDetail = new HashMap<>();
    private Map<String, String> ruleSetPromoDetail = new HashMap<>();
    private static final Map<String, String> STATUS_MAP = Map.of(
            "In Approval", "IN_APPROVAL",
            "Active", "ACTIVE",
            "Inactive", "INACTIVE"
    );
    public Document promoData = new Document();

    public PromoDashboard(Session<T> session) {
        super(session);
    }

    private static final Map<String, String> PAYMENT_METHOD_NAME_TO_PROMO_DETAIL_UI_LABEL =
            Collections.unmodifiableMap(buildPaymentMethodNameToPromoDetailUiLabels());

    private static Map<String, String> buildPaymentMethodNameToPromoDetailUiLabels() {
        Map<String, String> m = new HashMap<>(128);
        putPm(m, "ALL_PAYMENT_METHODS", "All payment method");
        putPm(m, "CREDITCARD", "Credit Card");
        putPm(m, "KLIKBCA", "Klik BCA");
        putPm(m, "VA_BCA", "Virtual Account BCA");
        putPm(m, "SAKUKU", "Sakuku");
        putPm(m, "DEBIT_ONLINE", "Debit Online");
        putPm(m, "KREDIVO", "Kredivo");
        putPm(m, "GOPAY", "GoPay");
        putPm(m, "LINKAJA", "LinkAja");
        putPm(m, "PAY_AT_HOTEL", "Pay at Hotel");
        putPm(m, "PAY_LATER", "Blibli Tiket PayLater");
        putPm(m, "CORPORATE_BILLING", "Corporate Billing - Deposit");
        putPm(m, "OVO", "OVO");
        putPm(m, "JUSPAY_CARD", "Credit/Debit Card");
        putPm(m, "CREDIT_CARD", "Credit Card");
        return m;
    }

    private static void putPm(Map<String, String> m, String value, String label) {
        m.put(value, label);
    }

    public void maximize() {
        driverTools.maximizeWindow();
    }

    public void goToAdminCarePage(String username, String password) throws InterruptedException {
        driverTools.maximizeWindow();
        consumeStaleBeforeUnloadAlert();
        new MemberLoginPage<>(session).adminLoginViaCookie(username, password);
    }

    private void consumeStaleBeforeUnloadAlert() {
        Deeplink deeplink = new Deeplink("ADMIN_CARE", "https://gatotkaca.tiket.com", "https://preprod.tiket.com", "https://tiket.com");
        String url = switch (Configs.ENVIRONMENT) {
            case PROD -> deeplink.prodDeeplink() + adminPath;
            case PREPROD -> deeplink.preprodDeeplink() + adminPath;
            case GK -> deeplink.gkDeeplink() + adminPath;
            default -> deeplink.gkDeeplink() + adminPath;
        };
        try {
            driverTools.get(url);
            if (isBrowserAlertPresent(driver)) {
                driverTools.acceptBrowserAlert();
            }
        } catch (UnhandledAlertException e) {
            driverTools.acceptBrowserAlert();
        }
    }

    public void goToAdminCarePageViaUI(String username, String password) throws InterruptedException {
        driverTools.maximizeWindow();
        Deeplink deeplink = new Deeplink("ADMIN_CARE", "https://gatotkaca.tiket.com", "https://preprod.tiket.com", "https://tiket.com");
        String url = switch (Configs.ENVIRONMENT) {
            case PROD -> deeplink.prodDeeplink() + adminPath;
            case PREPROD -> deeplink.preprodDeeplink() + adminPath;
            case GK -> deeplink.gkDeeplink() + adminPath;
            default -> deeplink.gkDeeplink() + adminPath;
        };
        try {
            driverTools.get(url);
            if (isBrowserAlertPresent(driver)) {
                driverTools.acceptBrowserAlert();
            }
        } catch (UnhandledAlertException e) {
            driverTools.acceptBrowserAlert();
        }
        if (!isAdminCareLoggedIn()) {
            PaymentDashboardLoginPage<?> paymentAdminCareLoginPage = new PaymentDashboardLoginPage<>(session);
            paymentAdminCareLoginPage.loginToPaymentDashBoard(username, password);
        }
    }

    public boolean isAdminCareLoggedIn() {
        Optional<WebElement> dashboardLogo = driverTools.waitForElementToAppear(DASH_LOGO, 1);
        return dashboardLogo.isPresent();
    }

    public void clickOnSearch() {
        driverTools.waitForElementToDisappear(APPROVAL_POP_UP, 5);
        driverTools.scrollAndClick(SEARCH_BUTTON);
    }

    public void filterSearchByStatus(String status) {
        clickDropdown("status");
        driverTools.waitForElementToAppear(insideDropdownFilterStatusLocator(status))
                .ifPresent(driverTools::click);
        clickDropdown("status");
        driverTools.waitForElementToDisappear(insideDropdownFilterStatusLocator(status));
    }

    public void clickApprovalStatus() {
        try {
            WebElement approvalPageStatus = driverTools.waitForElementToAppear(APPROVAL_PAGE_STATUS)
                    .orElseThrow();
            driverTools.clickWithJavascriptExecutor(approvalPageStatus);
        } catch (StaleElementReferenceException e) {
            logger.log("Stale element encountered , so Refreshing the page and retrying");
            refreshPage();
            WebElement approvalPageStatus = driverTools.waitForElementToAppear(APPROVAL_PAGE_STATUS)
                    .orElseThrow();
            driverTools.clickWithJavascriptExecutor(approvalPageStatus);
        }
    }

    public boolean isOverviewStatusHeadingDisplayed() {
        WebElement overviewStatusHeading = driverTools.waitForElementToAppear(
                OVERVIEW_STATUS_HEADING).orElseThrow();
        return driverTools.isElementDisplayed(overviewStatusHeading);
    }

    public boolean isOverviewStatusDisplayed() {
        WebElement overviewStatus = driverTools.waitForElementToAppear(OVERVIEW_STATUS)
                .orElseThrow();
        return driverTools.isElementDisplayed(overviewStatus);
    }

    public void clickApprovalStatusSelect() {
        WebElement approvalStatus = driverTools.waitForElementToAppear(
                APPROVAL_PAGE_STATUS_INAPPROVAL).orElseThrow();
        driverTools.clickWithJavascriptExecutor(approvalStatus);
    }

    public void clickAprroveButton() {
        int tries = 3;
        for (int i = 0; i < tries; i++) {
            try {
                WebElement approveBtn = driverTools.waitForElementToAppear(APPROVE_BUTTON)
                        .orElseThrow();
                driverTools.waitForElementToBeClickable(approveBtn);
                driverTools.click(approveBtn);
                return;
            } catch (StaleElementReferenceException e) {
                logger.log("Stale element encountered, try again");
            }
        }
    }

    public void clickApproveButton(String row) {
        int tries = 3;
        for (int i = 0; i < tries; i++) {
            try {
                WebElement approveBtn = driverTools.waitForElementToAppear(getApproveButtonRow(row))
                        .orElseThrow();
                driverTools.waitForElementToBeClickable(approveBtn);
                driverTools.click(approveBtn);
                return;
            } catch (StaleElementReferenceException e) {
                logger.log("Stale element encountered, try again");
            }
        }
    }

    public boolean getApprovalPopUpText() {
        WebElement approvalPopUp = driverTools.waitForElementToAppear(APPROVAL_POP_UP)
                .orElseThrow();
        String approvalPopUpText = driverTools.getText(approvalPopUp);
        if (approvalPopUpText.equals(EXPECTEDTEXT1) || approvalPopUpText.equals(EXPECTEDTEX2)) {
            return true;
        }
        return false;
    }

    public String getTextAfterApproval() {
        WebElement approvalPopUp = driverTools.waitForElementToAppear(APPROVAL_POP_UP)
                .orElseThrow();
        String approvalText = driverTools.getText(approvalPopUp);
        return approvalText;
    }

    public boolean isPopUpdisplayed() {
        WebElement approvalPopUp = driverTools.waitForElementToAppear(APPROVAL_POP_UP)
                .orElseThrow();
        return driverTools.isElementDisplayed(approvalPopUp);
    }

    public void clickOnApproveModal() throws InterruptedException {
        Thread.sleep(500);
        WebElement approvalModal = driverTools.waitForElementToAppear(APPROVAL_MODAL_APPROVE)
                .orElseThrow();
        driverTools.waitForElementToBeClickable(approvalModal);
        driverTools.clickWithJavascriptExecutor(approvalModal);
        driverTools.waitForElementToDisappear(SUCCESS_MESSAGE_LOCATOR);
    }

    public void clickOnInactiveModal() {
        driverTools.waitForElementToAppear(INACTIVE_MODAL_BUTTON).ifPresent(driverTools::click);
    }

    public void clickOnSelectedPromo() {
        WebElement approvalPromo = driverTools.waitForElementToAppear(PROMO_SELECTED).orElseThrow();
        driverTools.clickWithJavascriptExecutor(approvalPromo);
        driverTools.waitForElementToDisappear(PROMO_DETAIL_LOADING);
    }

    public boolean isApproveButtonDisplayed() {
        WebElement approveButton = driverTools.waitForElementToAppear(APPROVE_BUTTON).orElseThrow();
        return driverTools.isElementDisplayed(approveButton);
    }

    public boolean isSendForEditButtonDisplayed() {
        WebElement sendForEdit = driverTools.waitForElementToAppear(SEND_FOR_EDIT_BUTTON)
                .orElseThrow();
        return driverTools.isElementDisplayed(sendForEdit);
    }

    public void openPromoPage() {
        WebElement dashLogo = driverTools.waitForElementToAppear(DASH_LOGO).orElseThrow();
        WebElement newDashboard = driverTools.waitForElementToAppear(NEW_DASHBOARD).orElseThrow();
        driverTools.waitForElementToAppear(dashLogo);
        driverTools.scrollIntoWebView(
                B2B_DASHBOARD);
        driverTools.click(newDashboard);
    }

    public void openApprovalPage() {
        driverTools.waitForElementToDisappear(LIST_LOADING);
        WebElement approvalPage = driverTools.waitForElementToBeClickable(APPROVAL_PAGE, 3)
                .orElseThrow();
        driverTools.clickWithJavascriptExecutor(approvalPage);
    }

    public void openPromoCheckerPage() {
        WebElement checkerPage = driverTools.waitForElementToAppear(CHECKER_PAGE, 20).orElseThrow();
        driverTools.scrollIntoWebView(CHECKER_PAGE);
        driverTools.clickWithJavascriptExecutor(checkerPage);
        new WebDriverWait(driver, Duration.ofSeconds(25))
                .until(ExpectedConditions.urlContains("/discount_v2/cs/promo"));
        driverTools.waitForElementToDisappear(LIST_LOADING, 25);
    }

    public void goToPromoLinkingPage() {
        WebElement linkingPage = driverTools.waitForElementToAppear(LINKING_PAGE).orElseThrow();
        driverTools.click(linkingPage);
    }

    public void goToInstantPointCashbackPage() {
        driverTools.waitForElementToAppear(INSTANT_POINT_CASHBACK_PAGE);
        driverTools.click(INSTANT_POINT_CASHBACK_PAGE);
        driverTools.dummyWait(3);
    }

    public void inputSearchPromo(String entry) {
        try {
            WebElement searchPromo = driverTools.waitForElementToBeClickable(SEARCH_PROMO)
                    .orElseThrow();
            driverTools.clickWithJavascriptExecutor(searchPromo);
            driverTools.clearInput(searchPromo);
            driverTools.sendKeys(searchPromo, entry);
        } catch (StaleElementReferenceException e) {
            logger.log("Stale element encountered , so Refreshing the page and retrying");
            refreshPage();
            WebElement searchPromo = driverTools.waitForElementToBeClickable(SEARCH_PROMO)
                    .orElseThrow();
            driverTools.click(searchPromo);
            driverTools.clearInput(searchPromo);
            driverTools.sendKeys(searchPromo, entry);
        }
    }

    public void inputSearchLinking(String entry) {
        try {
            WebElement searchLink = driverTools.waitForElementToAppear(SEARCH_LINKING)
                    .orElseThrow();
            driverTools.click(searchLink);
            driverTools.sendKeys(searchLink, entry);
        } catch (StaleElementReferenceException e) {
            logger.log("Stale element encountered , so Refreshing the page and retrying");
            refreshPage();
            WebElement searchLink = driverTools.waitForElementToAppear(SEARCH_LINKING)
                    .orElseThrow();
            driverTools.click(searchLink);
            driverTools.sendKeys(searchLink, entry);
        }
    }

    public void clickResetFilter() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        for (int attempt = 0; attempt < 8; attempt++) {
            try {
                WebElement btn = driverTools.waitForElementToAppear(RESET_FILTER_BUTTON).orElseThrow();
                js.executeScript(
                        "arguments[0].scrollIntoView({block: 'center', inline: 'nearest'}); arguments[0].click();",
                        btn);
                return;
            } catch (StaleElementReferenceException e) {
                logger.log("Reset Filter stale on scroll+click, retry " + (attempt + 1));
            }
        }
        throw new IllegalStateException("Could not click Reset Filter after stale retries");
    }

    public String getSearchTextSearch() {
        WebElement searchText = driverTools.element(SEARCH_PROMO).orElseThrow();
        return driverTools.getValue(searchText);
    }

    public String getSearchTextType() {
        WebElement textType = driverTools.element(SEARCH_PROMO_TYPE).orElseThrow();
        return driverTools.getValue(textType);
    }

    public String getSearchTextStatus() {
        WebElement status = driverTools.element(SEARCH_PROMO_STATUS).orElseThrow();
        return driverTools.getValue(status);
    }

    public String getSearchTextDateCreated() {
        WebElement dateCreated = driverTools.element(SEARCH_PROMO_DATE_CREATED).orElseThrow();
        return driverTools.getValue(dateCreated);
    }

    public String getSearchTextDateEnded() {
        WebElement dateEnded = driverTools.element(SEARCH_PROMO_DATE_ENDED).orElseThrow();
        return driverTools.getValue(dateEnded);
    }

    public String getPromoNameOnPromoDetail() {
        WebElement promoName = driverTools.waitForElementToAppear(PROMO_NAME).orElseThrow();
        return driverTools.getText(promoName);
    }

    public void refreshPage() {
        driver.navigate().refresh();
    }

    public int extractNumber() {
        WebElement promoCount = driverTools.waitForElementToAppear(PROMO_COUNT).orElseThrow();
        String promoCountText = promoCount.getText();
        String[] promoCountParts = promoCountText.split(" ");
        String promoCnt = promoCountParts[promoCountParts.length - 1];
        return Integer.parseInt(promoCnt);
    }

    public boolean isOverviewCurDisplayed() {
        WebElement currency = driverTools.waitForElementToAppear(OVERVIEW_CURRENCY).orElseThrow();
        return driverTools.isElementDisplayed(currency);
    }

    public boolean isOverviewCurValueDisplayed() {
        WebElement currencyValue = driverTools.waitForElementToAppear(CURRENCY_LOCATOR)
                .orElseThrow();
        return driverTools.isElementDisplayed(currencyValue);
    }

    public boolean isOverviewetPromoTypeDisplayed() {
        WebElement promoType = driverTools.waitForElementToAppear(OVERVIEW_PROMO_TYPE)
                .orElseThrow();
        return driverTools.isElementDisplayed(promoType);
    }

    public boolean isOverviewetPromoTypeValueDisplayed() {
        WebElement promoTypeValue = driverTools.waitForElementToAppear(TYPE_LOCATOR).orElseThrow();
        return driverTools.isElementDisplayed(promoTypeValue);
    }

    public boolean isOverviewDateCreatedDisplayed() {
        WebElement overviewDateCreated = driverTools.waitForElementToAppear(DATE_LOCATOR)
                .orElseThrow();
        return driverTools.isElementDisplayed(overviewDateCreated);
    }

    public boolean isOverviewDateCreatedValueDisplayed() {
        WebElement overviewDateCreatedValue = driverTools.waitForElementToAppear(DATE_LOCATOR)
                .orElseThrow();
        return driverTools.isElementDisplayed(overviewDateCreatedValue);
    }

    public String isValuePresentDate() {
        WebElement overviewDateCreated = driverTools.element(DATE_LOCATOR).orElseThrow();
        String overviewDateCreatedText = driverTools.getText(overviewDateCreated).trim();
        return overviewDateCreatedText;
    }

    public String isValuePresentCur() {
        WebElement overviewCurrency = driverTools.element(CURRENCY_LOCATOR).orElseThrow();
        String currencyText = overviewCurrency.getText();
        return currencyText;
    }

    public String isValuePresentPromoType() {
        WebElement promoType = driverTools.element(TYPE_LOCATOR).orElseThrow();
        String promoTypeText = driverTools.getText(promoType);
        return promoTypeText;
    }

    public void waitForOverviewStatus(String expectedStatus) {
        new WebDriverWait(driver, Duration.ofSeconds(15))
                .withMessage("Overview status did not become '" + expectedStatus + "'")
                .until(ExpectedConditions.textToBePresentInElementLocated(OVERVIEW_STATUS.by(), expectedStatus));
    }

    public String isValuePresentPromoStatus() {
        WebElement overviewStatus = driverTools.element(OVERVIEW_STATUS).orElseThrow();
        String overviewStatusText = driverTools.getText(overviewStatus);
        return overviewStatusText;
    }

    public void selectPromoDropdown() {
        WebElement dropdownSelection = driverTools.waitForElementToAppear(APPROVAL_PAGE_STATUS)
                .orElseThrow();
        driverTools.clickWithJavascriptExecutor(dropdownSelection);
    }

    public void selectActivePromo() {
        WebElement activePromo = driverTools.waitForElementToAppear(ACTIVE_PROMO).orElseThrow();
        driverTools.clickWithJavascriptExecutor(activePromo);
        driverTools.waitForElementToAppear(APPROVAL_PAGE_STATUS);
    }

    public void selectInactivePromo() {
        WebElement inactivePromo = driverTools.waitForElementToAppear(INACTIVE_PROMO).orElseThrow();
        driverTools.clickWithJavascriptExecutor(inactivePromo);
        driverTools.waitForElementToAppear(APPROVAL_PAGE_STATUS);
    }

    public String getPromoCampaignName() {
        driverTools.waitForElementToAppear(PROMO_NAME);
        WebElement selectedPromoCampaignName = driverTools.element(PROMO_NAME).orElseThrow();
        String campaignName = driverTools.getText(selectedPromoCampaignName).trim();
        return campaignName;
    }

    public String parseAndFormatDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        Date date;
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
        return outputFormat.format(date);
    }

    public List<String> getPromoCodeMongoData(String configType, String dataBase,
                                              String collectionName, String queryString, String searchKey) {
        FindIterable<Document> result = TiketMongoDBManager.searchInMongoDB(configType, dataBase,
                collectionName, queryString);
        List<String> mongoData = new ArrayList<>();
        for (Document document : result) {
            if (document.containsKey(searchKey)) {
                Object value = document.get(searchKey);
                if (value instanceof Date) {
                    Date date = (Date) value;
                    SimpleDateFormat dateFormat = new SimpleDateFormat(
                            "yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
                    mongoData.add(dateFormat.format(date));
                } else {
                    mongoData.add((String) value);
                }
            }
        }
        return mongoData;
    }

    public boolean isDataMatched(String dashboardData, List<String> MongoDBList) {
        for (String dataMongoDB : MongoDBList) {
            if (dataMongoDB.matches(
                    "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}[+-]\\d{2}:\\d{2}")) {
                String formattedDate = parseAndFormatDate(dataMongoDB);
                if (formattedDate != null && dashboardData.equals(formattedDate)) {
                    return true;
                }
            } else {
                if (dashboardData.equals(dataMongoDB)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setPromoDocumentFromDB(String configType, String dbName, String collectionName,
                                       String queryString, String sortBy) {
        FindIterable<Document> result = TiketMongoDBManager.searchInMongoDBWithSortLimit(configType,
                dbName, collectionName, queryString, sortBy, 1);
        promoData = result.first();
    }

    public String getFieldPromoDocument(Document data, String fieldName) {
        Object value = data.get(fieldName);
        String valueString = "";
        if (value instanceof Date) {
            Date date = (Date) value;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            valueString = dateFormat.format(date);
        } else {
            valueString = String.valueOf(value);
        }
        logger.log("Got value: " + valueString + ", from field name: " + fieldName);
        return valueString;
    }

    public void scorllToCarAdmin() {
        WebElement promoPage = driverTools.waitForElementToAppear(CAR_ADMIN_LOCATOR).orElseThrow();
        driverTools.scrollIntoWebView(promoPage);
    }

    public boolean isPromoNameCorrect(String name) {
        WebElement element = driverTools.waitForElementToAppear(ELEMENT_LOCATOR).orElseThrow();
        return driverTools.getText(element).contains(name);
    }

    public void clickPromo() {
        WebElement promo = driverTools.waitForElementToAppear(ELEMENT_LOCATOR).orElseThrow();
        driverTools.click(promo);
    }

    public void clickPromo(String promoCode) {
        driverTools.waitForElementToDisappear(APPROVAL_POP_UP, 5);
        Locator row = getSpecificPromoLocator(promoCode);
        driverTools.waitForElementToAppear(row, 20).orElseThrow();
        driverTools.clickWithJavascriptExecutor(row);
    }

    public boolean checkPromoDetails(String date, String currency, String type) {
        WebElement actualDate = driverTools.waitForElementToAppear(DATE_LOCATOR).orElseThrow();
        WebElement actualCurrency = driverTools.waitForElementToAppear(CURRENCY_LOCATOR)
                .orElseThrow();
        WebElement actualType = driverTools.waitForElementToAppear(TYPE_LOCATOR).orElseThrow();
        return (driverTools.getText(actualDate).contains(date) &&
                driverTools.getText(actualCurrency).contains(currency) &&
                driverTools.getText(actualType).contains(type));
    }

    public void clickCreateNewPromoButton() {
        WebElement button = driverTools.waitForElementToAppear(CREATE_NEW_PROMO_LOCATOR)
                .orElseThrow();
        driverTools.clickWithJavascriptExecutor(button);
    }

    public void clickPromoTypeDropdown() {
        WebElement dropdown = driverTools.waitForElementToAppear(PROMO_TYPE_DROPDOWN_LOCATOR)
                .orElseThrow();
        driverTools.clickWithJavascriptExecutor(dropdown);
    }

    public String sendTextInCampaignNameInput() {
        WebElement input = driverTools.waitForElementToAppear(CAMPAIGN_NAME_LOCATOR).orElseThrow();
        driverTools.clickWithJavascriptExecutor(input);
        String randomName = UUID.randomUUID().toString().replace("-", "").substring(0, 6);
        String finalName = "AUTOMATIONTESTING" + randomName.toUpperCase();
        driverTools.sendKeys(input, finalName);
        return finalName;
    }

    public void sendTextInUniqueIdentifierInput(String identifier) {
        WebElement input = driverTools.waitForElementToAppear(UNIQUE_IDENTIFIER_LOCATOR)
                .orElseThrow();
        driverTools.clickWithJavascriptExecutor(input);
        driverTools.sendKeys(input, identifier);
    }

    public void sendTextInPromoNameInput(String name) {
        WebElement input = driverTools.waitForElementToAppear(PROMO_NAME_INPUT_LOCATOR)
                .orElseThrow();
        driverTools.sendKeys(input, name);
    }

    public void clickCancelButton() throws InterruptedException {
        WebElement button = driverTools.waitForElementToAppear(CANCEL_BUTTON_LOCATOR).orElseThrow();
        driverTools.scrollIntoWebView(button);
        driverTools.waitForElementToBeClickable(button);
        Thread.sleep(1000);
        driverTools.clickWithJavascriptExecutor(button);
    }

    public void clickSaveAsDraftButton() {
        WebElement button = driverTools.waitForElementToAppear(SAVE_AS_DRAFT_BUTTON_LOCATOR)
                .orElseThrow();
        driverTools.click(button);
    }

    public boolean checkSuccessMessage(String message) {
        WebElement snackbar = driverTools.waitForElementToAppear(APPROVAL_POP_UP)
                .orElseThrow();
        return driverTools.getText(snackbar).contains(message);
    }

    public void clickCashbackInDropdown() {
        WebElement cashback = driverTools.waitForElementToAppear(
                CASHBACK_IN_PROMO_TYPE_DROPDOWN_LOCATOR).orElseThrow();
        driverTools.click(cashback);
    }

    public void clickDropdown(String id) {
        WebElement dropdown = driverTools.waitForElementToAppear(dropdownBoxLocator(id))
            .orElseThrow();
        driverTools.scrollElementToCenter(dropdown);
        driverTools.click(dropdown);
    }

    public String getDropdownValue(String id) {
        WebElement dropdown = driverTools.waitForElementToAppear(dropdownBoxLocator(id))
                .orElseThrow();
        return dropdown.getAttribute("value");
    }

    public void clickDropdown(String id, int element) {
        List<WebElement> elements = driverTools.elements(dropdownBoxLocator(id)).orElseThrow();
        WebElement selector = driverTools.waitForElementToAppear(elements.get(element)).orElseThrow();
        driverTools.scrollElementToCenter(selector);
        driverTools.waitForElementToBeClickable(selector, 10);
        driverTools.clickWithJavascriptExecutor(selector);
    }

    public void clickHybridCostAllocationDropdown(String id, String value) {
        List<WebElement> dropdown = driverTools.elements(dropdownBoxLocator(id)).get();
        for (WebElement element : dropdown) {
            driverTools.clickWithJavascriptExecutor(element);
            clickInsideDropdownThenWaitToDisappear(value);
        }
    }

    public void clickDropdownPaymentMethod(String id) {
        Locator loc = dropdownBoxLocator(id);
        driverTools.waitForElementToAppear(loc).orElseThrow();
        driverTools.scrollElementToCenter(loc);
        driverTools.click(loc);
        driverTools.dummyWait(1);
    }

    public void clickInsideDropdown(String text) {
        WebElement option = resolveDropdownOptionElement(text);
        driverTools.scrollElementToCenter(option);
        driverTools.waitForElementToBeClickable(option, 10);
        try {
            driverTools.click(option);
        } catch (ElementClickInterceptedException | StaleElementReferenceException e) {
            driverTools.clickWithJavascriptExecutor(option);
        }
    }

    public void clickInsideDropdownThenWaitToDisappear(String text) {
        WebElement insideDropdown = resolveDropdownOptionElement(text);
        driverTools.scrollElementToCenter(insideDropdown);
        driverTools.waitForElementToBeClickable(insideDropdown, 10);
        try {
            driverTools.click(insideDropdown);
        } catch (ElementClickInterceptedException | StaleElementReferenceException e) {
            driverTools.clickWithJavascriptExecutor(insideDropdown);
        }
        driverTools.waitForElementToDisappear(insideDropdown);
    }

    public void sendTextInDescriptionBox(String description) {
        WebElement input = driverTools.waitForElementToAppear(DESCRIPTION_BOX_LOCATOR)
                .orElseThrow();
        driverTools.clickWithJavascriptExecutor(input);
        driverTools.sendKeys(input, description);
    }

    public void scorllTillClickable() throws InterruptedException {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        int distanceInPixels = 400;
        js.executeScript("window.scrollBy(0, arguments[0]);", distanceInPixels);
        Thread.sleep(500);
    }

    public void scrollToElementById(String id) throws InterruptedException {
        driverTools.scrollElementToCenter(inputLocator(id));
    }

    public void selectDateFromCalendar(String dateFieldId, String date) throws InterruptedException {
        Locator dateFieldLocator = dropdownBoxLocator(dateFieldId);
        WebElement dateField = driverTools.waitForElementToAppear(dateFieldLocator).orElseThrow();
        driverTools.scrollElementToCenter(dateField);

        driverTools.click(dateField);
        driverTools.waitForElementToAppear(SAVE_BUTTON_LOCATOR).orElseThrow();
        WebElement saveButton = driverTools.waitForElementToAppear(SAVE_BUTTON_LOCATOR).orElseThrow();
        driverTools.scrollElementToCenter(saveButton);
        selectDate(date);
        driverTools.click(saveButton);
        driverTools.waitForElementToDisappear(SAVE_BUTTON_LOCATOR);
    }

    @Deprecated
    public void scorllTillClickable(int loop, int distanceInPixels) throws InterruptedException {
        for (int j = 0; j < loop; j++) {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollBy(0, arguments[0]);", distanceInPixels);
             try { Thread.sleep(500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }
    }

    public String clearTextInInput(String id) {
        WebElement input = driverTools.waitForElementToAppear(inputLocator(id)).orElseThrow();
        String value = driverTools.getValue(input);
        driverTools.scrollToElement(input);
        driverTools.click(inputLocator(id));
        driverTools.clearInput(inputLocator(id));
        driverTools.sendKeys(inputLocator(id), "0");
        logger.log("value in cleared element -> " + value);
        return value;
    }

    public void sendTextInInput(String id, String text) {
        WebElement input = driverTools.waitForElementToAppear(inputLocator(id)).orElseThrow();
        driverTools.scrollElementToCenter(input);
        driverTools.clickWithJavascriptExecutor(input);
        driverTools.sendKeys(input, text);
        logger.log("Sent keys -> " + text + " in element id -> " + id);
    }

    public void sendTextInInputDataTest(String id, String text){
        WebElement input = driverTools.waitForElementToAppear(inputLocatorDataTest(id)).orElseThrow();
        driverTools.click(input);
        driverTools.sendKeys(input,text);
        logger.log("Sent keys -> "+text+" in element id -> "+id);
    }

    public void clickAddValues() {
        WebElement button = driverTools.waitForElementToAppear(ADD_VALUES_BUTTON_LOCATOR)
                .orElseThrow();
        driverTools.click(button);
    }

    public void clickVerifiedRadioBox() {
        WebElement radioBox = driverTools.waitForElementToAppear(VERIFIED_RADIOBOX_LOCATOR)
                .orElseThrow();
        driverTools.click(radioBox);
    }

    public void clickTransactionRadioBox() {
        WebElement radioBox = driverTools.waitForElementToAppear(TRANSACTION_VALUE_RADIOBOX_LOCATOR)
                .orElseThrow();
        driverTools.waitForElementToBeClickable(radioBox, 10);
        driverTools.clickWithJavascriptExecutor(radioBox);
    }

    public void clickUsername() {
        WebElement username = driverTools.waitForElementToAppear(USERNAME_LOCATOR).orElseThrow();
        driverTools.click(username);
    }

    public void clickEqualToInDropdown() {
        WebElement equalTo = driverTools.waitForElementToAppear(EQUAL_TO_LOCATOR).orElseThrow();
        driverTools.click(equalTo);
    }

    public void clickEqualToMultiValueInDropdown() {
        WebElement equalTo = driverTools.waitForElementToAppear(EQUAL_TO_MULTI_VALUE_LOCATOR)
                .orElseThrow();
        driverTools.click(equalTo);
    }

    public String getPromoCodeMongoFullData(String configType, String dataBase,
                                            String collectionName, String queryString) {
        FindIterable<Document> result = TiketMongoDBManager.searchInMongoDB(configType, dataBase,
                collectionName, queryString);
        Document lastDocument = null;
        for (Document document : result) {
            lastDocument = document;
        }
        if (lastDocument == null) {
            throw new IllegalStateException(
                    "No MongoDB document in collection '" + collectionName + "' for query: " + queryString);
        }
        return lastDocument.toJson();
    }

    public String[] getPaymentNames(String promoCodeData) {
        logger.log("promoCodeData -> " + promoCodeData);
        JSONObject jsonMap = new JSONObject(promoCodeData);
        JSONArray ruleSets = jsonMap.getJSONArray("ruleSets");
        List<String> paymentNameList = new ArrayList<>();
        for (int i = 0; i < ruleSets.length(); i++) {
            JSONObject ruleSet = ruleSets.getJSONObject(i);
            JSONArray discountRules = ruleSet.getJSONArray("discountRules");
            for (int j = 0; j < discountRules.length(); j++) {
                JSONObject discountRule = discountRules.getJSONObject(j);
                JSONArray paymentMethodObjectList = discountRule
                        .getJSONObject("promoCodePriceRangeObject")
                        .getJSONArray("paymentMethodObjectList");
                for (int k = 0; k < paymentMethodObjectList.length(); k++) {
                    JSONObject paymentMethodObject = paymentMethodObjectList.getJSONObject(k);
                    String paymentMethodName = paymentMethodObject.getString("paymentMethodName");
                    paymentNameList.add(paymentMethodName);
                }
            }
        }
        String[] paymentNames = paymentNameList.toArray(new String[0]);
        logger.log("Payment Name -> " + Arrays.toString(paymentNames));
        return paymentNames;
    }

    public boolean checkDbData(String mongoData, String dashboardData) {
        String finalData = mongoData.replaceAll("[^a-zA-Z0-9 ]", " ");
        return finalData.contains(dashboardData);
    }

    public boolean getJsonStringValue(String jsonData, String myValue, String key) {
        Gson gson = new Gson();
        Map<String, Object> jsonMap = gson.fromJson(jsonData, Map.class);
        Object dbvalue = jsonMap.get(key);
        String finalValue = dbvalue.toString();
        logger.log(key + " : " + finalValue);
        return finalValue.contains(myValue);
    }

    public String getJsonStringValue(String jsonData, String key) {
        Gson gson = new Gson();
        Map<String, Object> jsonMap = gson.fromJson(jsonData, Map.class);
        Object dbvalue = jsonMap.get(key);
        String finalValue = dbvalue.toString();
        logger.log(key + " : " + finalValue);
        return finalValue;
    }

    public void selectDate(String text) throws InterruptedException {
        boolean present = driverTools.waitForElementToAppear(dateLocator(text))
                .isPresent();
        WebElement insideDropdown = driverTools.waitForElementToAppear(present ? dateLocator(text) : ACTIVE_DATE)
            .orElseThrow();
        driverTools.scrollElementToCenter(insideDropdown);
        driverTools.waitForElementToBeClickable(insideDropdown, 10);
        driverTools.clickWithJavascriptExecutor(insideDropdown);
    }

    public void clickSaveButton() {
        WebElement button = driverTools.waitForElementToAppear(SAVE_BUTTON_LOCATOR).orElseThrow();
        driverTools.click(button);
    }

    public void clickAddPeriod() {
        WebElement button = driverTools.waitForElementToAppear(ADD_PERIOD_BUTTON_LOCATOR)
            .orElseThrow();
        driverTools.scrollElementToCenter(button);
        driverTools.click(button);
    }

    public void clickPreviewButton() throws InterruptedException {
        ensureMandatoryMetadataDropdownSelected();
        scrollToBottom();
        driverTools.scrollAndClick(PREVIEW_BUTTON_LOCATOR);
        driverTools.waitForElementToAppear(OVERVIEW_STATUS_HEADING, 30).orElseThrow();
    }

    public void scrollToTop() throws InterruptedException {
        WebElement top = driverTools.waitForElementToAppear(TOP_LOCATOR).orElseThrow();
        driverTools.scrollIntoWebView(top);
        Thread.sleep(1000);
    }

    public void scrollToBottom() throws InterruptedException {
        WebElement bottom = driverTools.waitForElementToAppear(BOTTOM_LOCATOR).orElseThrow();
        driverTools.scrollIntoWebView(bottom);
        Thread.sleep(1000);
    }

    public boolean checkAssertions(String text, String value) {
        WebElement field = driverTools.waitForElementToAppear(previewSideLocator(text))
                .orElseThrow();
        logger.log(text + " : " + field.getText());
        String answer = field.getText();
        if (text == "Max. AOV" || text == "Min. AOV") {
            answer = answer.replace(",", "");
        }
        return answer.contains(value);
    }

    public void clickLeaveButton() throws InterruptedException {
        WebElement button = driverTools.waitForElementToAppear(LEAVE_BUTTON_LOCATOR).orElseThrow();
        driverTools.waitForElementToBeClickable(LEAVE_BUTTON_LOCATOR).orElseThrow();
        scorllTillClickable();
        driverTools.clickWithJavascriptExecutor(button);
    }

    public boolean checkAllocation(String percentage) {
        WebElement value = driverTools.waitForElementToAppear(PERCENTAGE_LOCATOR).orElseThrow();
        logger.log("percentage : " + value.getText());
        return value.getText().contains(percentage);
    }

    public void goToPromoList() {
        WebElement list = driverTools.waitForElementToBeClickable(PROMO_LIST_LOCATOR).orElseThrow();
        driverTools.click(list);
    }

    public void clickCreateCampaignButton() throws InterruptedException {
        scrollToBottom();
        WebElement list = driverTools.waitForElementToAppear(CREATE_CAMPAIGN_BUTTON_LOCATOR)
                .orElseThrow();
        driverTools.click(list);
    }

    public void clickSendForApprovalButton() {
        WebElement list = driverTools.waitForElementToBeClickable(APPROVAL_BUTTON_LOCATOR).orElseThrow();
        driverTools.click(list);
    }

    public boolean compareHashMaps(Map<String, String> first, Map<String, String> second) {
        List<String> incorrectValue = first.entrySet().stream()
                .filter(e -> !second.containsKey(e.getKey()) || !second.get(e.getKey())
                        .equals(e.getValue()))
                .map(e -> "First Map Key: " + e.getKey() + " Value: " + e.getValue()
                        + ", Second Map Value: " + (second.containsKey(e.getKey()) ? second.get(e.getKey())
                        : "null"))
                .collect(Collectors.toList());
        List<String> extraKeys = second.keySet().stream()
                .filter(e -> !first.containsKey(e))
                .map(e -> "Extra key: " + e + " present in Second Map")
                .collect(Collectors.toList());
        incorrectValue.forEach(logger::fail);
        extraKeys.forEach(logger::fail);

        return incorrectValue.isEmpty() && extraKeys.isEmpty();
    }

    public Map<String, String> getOverviewPromoDetailData() {
        Map<String, String> dataMap = new HashMap<>();
        String statusData = driverTools.getText(OVERVIEW_STATUS).orElseThrow();
        dataMap.put("status", statusData);
        String dateCreated = driverTools.getText(DATE_LOCATOR).orElseThrow();
        dataMap.put("dateCreated", dateCreated);

        String promoType = driverTools.getText(TYPE_LOCATOR).orElseThrow();
        dataMap.put("promoType", promoType);
        String currency = driverTools.getText(CURRENCY_LOCATOR).orElseThrow();
        dataMap.put("currency", currency);
        return dataMap;
    }

    public void setOverviewPromoDetailData() {
        overviewPromoDetail = getOverviewPromoDetailData();
        logger.log("Overview Promo Detail Data set");
    }

    public void setOverviewPromoDataFromDB() {
        overviewPromoDetail.put("status",
                capitalize(getFieldPromoDocument(promoData, "promoCodeStatus")));
        String dateCreated = parseAndFormatDate(getFieldPromoDocument(promoData, "createdDate"));
        overviewPromoDetail.put("dateCreated", dateCreated);
        overviewPromoDetail.put("promoType",
                getFieldPromoDocument(promoData, "promoCodeDiscountType"));
        overviewPromoDetail.put("currency", getFieldPromoDocument(promoData, "currency"));
    }

    public boolean verifyOverviewPromoApproval() {
        Map<String, String> overviewPromoApproval = new HashMap<>();
        overviewPromoApproval = getOverviewPromoDetailData();

        return compareHashMaps(overviewPromoDetail, overviewPromoApproval);
    }

    public Map<String, String> getGeneralInfoFraudPreventionData(String section) {
        Map<String, String> dataMap = new HashMap<>();

        WebElement overview = driverTools.waitForElementToAppear(OVERVIEW_STATUS_HEADING)
                .orElseThrow();
        driverTools.scrollIntoWebView(overview);
        List<String> fields = new ArrayList<>();
        switch (section) {
            case "general":
                fields = driverTools.getTexts(GENERAL_INFO_FIELDS);
                break;
            case "fraud":
                fields = driverTools.getTexts(FRAUD_PREVENTION_FIELDS);
                break;
        }
        for (String field : fields) {
            dataMap.put(field, driverTools.getText(getFieldsPromoDetails(field)).orElse(""));
        }
        return dataMap;
    }

    public void setGeneralInfoPromoData() {
        generalInfoPromoDetail = getGeneralInfoFraudPreventionData("general");
        logger.log("General Info Promo Data set");
    }

    public String getCampaignType(String configType) {
        String idCampaign = getFieldPromoDocument(promoData, "campaignType");
        Document query = new Document("_id", new ObjectId(idCampaign));
        Document campaignTypeData = TiketMongoDBManager.searchInMongoDBWithDocument(configType,
                "promoCode", "campaignType", query).first();
        String campaignType = getFieldPromoDocument(campaignTypeData, "name");
        return campaignType;
    }

    public String getDivisionName(String configType) {
        String divisionCode = getFieldPromoDocument(promoData, "division");
        Document query = new Document("code", divisionCode);
        Document divisionData = TiketMongoDBManager.searchInMongoDBWithDocument(configType,
                "tix_gift_voucher", "division", query).first();
        String divisionName = getFieldPromoDocument(divisionData, "name");
        return divisionName;
    }

    public boolean verifyGeneralInfoPromoApproval() {
        Map<String, String> generalInfoPromoApproval = new HashMap<>();
        generalInfoPromoApproval = getGeneralInfoFraudPreventionData("general");
        return compareHashMaps(generalInfoPromoDetail, generalInfoPromoApproval);
    }

    public void setFraudPreventionPromoData() {
        fraudPreventionPromoDetail = getGeneralInfoFraudPreventionData("fraud");
        logger.log("Fraud Prevention Promo Data set");
    }

    public boolean verifyFraudPreventionPromoApproval() {
        Map<String, String> fraudPreventionPromoApproval = new HashMap<>();
        fraudPreventionPromoApproval = getGeneralInfoFraudPreventionData("fraud");
        return compareHashMaps(fraudPreventionPromoDetail, fraudPreventionPromoApproval);
    }

    public Map<String, String> getPromoCodeFieldsData() {
        Map<String, String> dataMap = new HashMap<>();
        WebElement quotaType = driverTools.waitForElementToAppear(
                getFieldsPromoDetails("Quota Type")).orElseThrow();
        driverTools.scrollIntoWebView(quotaType);
        dataMap.put("Promo Code",
                driverTools.getText(getFieldsPromoDetails("Promo Code")).orElseThrow());
        String visibilityRaw = driverTools.getText(getFieldsPromoDetails("Visibility")).orElseThrow();
        dataMap.put("Visibility", stripOuterParentheses(visibilityRaw));
        return dataMap;
    }

    private static String stripOuterParentheses(String raw) {
        if (raw == null) {
            return "";
        }
        String t = raw.trim();
        if (t.length() >= 2 && t.startsWith("(") && t.endsWith(")")) {
            return t.substring(1, t.length() - 1).trim();
        }
        return t;
    }

    public void setPromoCodeFieldsData() {
        promoCodePromoDetail = getPromoCodeFieldsData();
        logger.log("Promo Code Section Promo Data set");
    }

    public void setPromoCodeDataFromDB() {
        promoCodePromoDetail.put("Promo Code", getFieldPromoDocument(promoData, "code"));
        String visibility = "show on promo suggestion screen";
        Boolean visibilityValue = promoData.getBoolean("isExcludedPromoSuggestion");
        visibility = visibilityValue ? "not " + visibility : visibility;
        promoCodePromoDetail.put("Visibility", capitalize(visibility));
    }

    public boolean verifyPromoCodePromoApproval() {
        Map<String, String> promoCodeApproval = new HashMap<>();
        promoCodeApproval = getPromoCodeFieldsData();
        return compareHashMaps(promoCodePromoDetail, promoCodeApproval);
    }

    public boolean verifyShortDescTncShown() {
        return driverTools.isElementDisplayed(getFieldsPromoDetails("Short Description"))
                && driverTools.isElementDisplayed(PROMO_DETAIL_TNC);
    }

    public Map<String, String> getRuleSetFieldsData() {
        Map<String, String> dataMap = new HashMap<>();
        WebElement tnc = driverTools.waitForElementToAppear(PROMO_DETAIL_TNC).orElseThrow();
        driverTools.scrollIntoWebView(tnc);
        List<String> fieldKey = new ArrayList<>(driverTools.getTexts(getFieldRuleSetValue("1")));
        List<String> fieldValue = new ArrayList<>(driverTools.getTexts(getFieldRuleSetValue("2")));
        if (fieldKey.size() != fieldValue.size()) {
            logger.log("List Key size -> " + fieldKey.size() + " not same with List Value size -> "
                    + fieldValue.size());
            return null;
        }
        for (int i = 0; i < fieldKey.size(); i++) {
            dataMap.put(fieldKey.get(i), fieldValue.get(i));
        }
        return dataMap;
    }

    public void setRuleSetFieldsData() {
        ruleSetPromoDetail = getRuleSetFieldsData();
        logger.log("Rule Set Promo Data set");
    }

    public boolean verifyRuleSetPromoApproval() {
        Map<String, String> ruleSetApproval = new HashMap<>();
        ruleSetApproval = getRuleSetFieldsData();
        return compareHashMaps(ruleSetPromoDetail, ruleSetApproval);
    }

    public void clickEditButton() {
        WebElement button = driverTools.waitForElementToAppear(EDIT_BUTTON_LOCATOR).orElseThrow();
        driverTools.click(button);
    }

    public void clickDeleteButton() {
        WebElement button = driverTools.waitForElementToAppear(DELETE_BUTTON_LOCATOR).orElseThrow();
        driverTools.click(button);
    }

    public void clickModalDeleteButton() {
        WebElement button = driverTools.waitForElementToAppear(MODAL_DELETE_BUTTON_LOCATOR)
                .orElseThrow();
        driverTools.click(button);
    }

    public void clickSaveChangesButton() {
        driverTools.scrollIntoWebView(SAVE_CHANGES_BUTTON_LOCATOR);
        WebElement button = driverTools.waitForElementToAppear(SAVE_CHANGES_BUTTON_LOCATOR)
                .orElseThrow();
        driverTools.click(button);
        driverTools.waitForElementToDisappear(SAVE_CHANGES_BUTTON_LOCATOR);
    }

    public void clickSaveChangesPopupButton() {
        WebElement button = driverTools.waitForElementToAppear(SAVE_CHANGES_BUTTON_POPUP_LOCATOR)
                .orElseThrow();
        driverTools.click(button);
        driverTools.waitForElementToDisappear(SAVE_CHANGES_BUTTON_POPUP_LOCATOR);
    }

    public void clearTextInDescriptionBox() {
        WebElement input = driverTools.waitForElementToAppear(DESCRIPTION_BOX_LOCATOR)
                .orElseThrow();
        input.clear();
    }

    public String getTextInDescriptionBox() {
        driverTools.waitForElementToAppear(DESCRIPTION_BOX_LOCATOR)
                .ifPresent(driverTools::scrollIntoWebView);
        return driverTools.getValue(DESCRIPTION_BOX_LOCATOR);
    }

    public String returnCampaignName() {
        WebElement input = driverTools.waitForElementToAppear(PROMO_CODE_NAME_LOCATOR)
                .orElseThrow();
        return input.getAttribute("value");
    }

    public boolean checkSearchedPromoCode(String promoCode) {
        WebElement displayedPromoCode = driverTools.waitForElementToAppear(
                getFirstRowCellByColumn("Promo Code")).orElseThrow();
        if (!driverTools.getText(displayedPromoCode).equals(promoCode)) {
            return false;
        }
        return true;
    }

    public void clickActionDropDown() {
        WebElement actionDropdown = driverTools.waitForElementToAppear(ACTION_DROPDOWN_LOCATOR)
                .orElseThrow();
        driverTools.clickWithJavascriptExecutor(actionDropdown);
    }

    public void clickActionTypeInDropdown(String actionName) {
        WebElement actionDropdownItem = driverTools.waitForElementToAppear(
                actionDropdownItem(actionName)).orElseThrow();
        driverTools.click(actionDropdownItem);
    }

    public boolean checkInactivateModalDisplayed() {
        driverTools.waitForElementToAppear(INACTIVATE_MODAL_HEADER);
        if (!driverTools.isElementDisplayed(INACTIVATE_MODAL_HEADER)) {
            return false;
        }
        return true;
    }

    public void clickInactivateButton() {
        WebElement inactivateBtn = driverTools.waitForElementToAppear(INACTIVATE_MODAL_BUTTON)
                .orElseThrow();
        inactivateBtn.click();
    }

    public void clickSaveChangesBtn() {
        WebElement saveBtn = driverTools.waitForElementToAppear(SAVE_EDIT_BUTTON).orElseThrow();
        saveBtn.click();
        WebElement savePopupBtn = driverTools.waitForElementToAppear(SAVE_EDIT_MODAL_BUTTON)
                .orElseThrow();
        savePopupBtn.click();
    }

    public void clickClone() {
        WebElement list = driverTools.waitForElementToAppear(CLONE_BUTTON_LOCATOR).orElseThrow();
        driverTools.clickWithJavascriptExecutor(list);
    }

    public boolean verifyCloneUrl() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        try {
            wait.until(d -> isPromoAddCloneQueryUrl(d.getCurrentUrl()));
        } catch (TimeoutException e) {
            logger.log("Timed out waiting for clone create URL; current: " + driver.getCurrentUrl());
            return false;
        }
        String currentUrl = driver.getCurrentUrl();
        logger.log("url: " + currentUrl);
        return isPromoAddCloneQueryUrl(currentUrl);
    }

    private static boolean isPromoAddCloneQueryUrl(String currentUrl) {
        if (currentUrl == null || !currentUrl.contains("/promo/add")) {
            return false;
        }
        if (currentUrl.contains("type=clone")) {
            return true;
        }
        return hasNonBlankCloneIdParam(currentUrl);
    }

    private static boolean hasNonBlankCloneIdParam(String url) {
        int idx = url.indexOf("cloneId=");
        if (idx < 0) {
            return false;
        }
        int valueStart = idx + "cloneId=".length();
        int valueEnd = url.indexOf('&', valueStart);
        String value = valueEnd < 0 ? url.substring(valueStart) : url.substring(valueStart, valueEnd);
        value = value.replaceFirst("#.*$", "");
        return !value.isBlank();
    }

    public boolean isCampaignNameFieldClickable() {
        WebElement list = driverTools.waitForElementToAppear(CAMPAIGN_NAME_FIELD_LOCATOR)
                .orElseThrow();
        return driverTools.isElementEnabled(list);
    }

    public void scrollToPromoCodeNameField() {
        WebElement list = driverTools.waitForElementToAppear(PROMO_CODE_FIELD_LOCATOR)
                .orElseThrow();
        driverTools.scrollIntoWebView(list);
    }

    public boolean isPromoCodeNameEqualsTo(String promoCode) {
        WebElement list = driverTools.waitForElementToAppear(PROMO_CODE_FIELD_LOCATOR)
                .orElseThrow();
        String text = driverTools.getValue(list);
        logger.log("text: " + text);
        return text.equalsIgnoreCase(promoCode);
    }

    public void scrollToPreviewPromoCodeNameField() {
        WebElement list = driverTools.waitForElementToAppear(PREVIEW_PROMO_CODE_FIELD_LOCATOR)
                .orElseThrow();
        driverTools.scrollIntoWebView(list);
    }

    public boolean isPreviewPromoCodeNameEqualsTo(String promoCode) {
        WebElement list = driverTools.waitForElementToAppear(PREVIEW_PROMO_CODE_FIELD_LOCATOR)
                .orElseThrow();
        String text = driverTools.getText(list);
        logger.log("text: " + text);
        return text.equalsIgnoreCase(promoCode);
    }

    public boolean modalHeaderTextIsVisible() {
        driverTools.waitForElementToAppear(MODAL_HEADER_TEXT);
        return driverTools.isElementDisplayed(MODAL_HEADER_TEXT);
    }

    public boolean isStatusMatched(String configType, String dataBase, String collectionName,
                                   String queryString, String searchKey) {
        List<String> statusMongoDBList = getPromoCodeMongoData(configType, dataBase, collectionName,
                queryString, searchKey);
        String status = isValuePresentPromoStatus();
        status = status.toUpperCase();
        System.out.println("abcd " + status);
        System.out.println("abcdef " + statusMongoDBList);
        boolean isMatched = isDataMatched(status, statusMongoDBList);
        return isMatched;
    }

    public String returnQueryWithPromoCodeName() {
        String promoCode = getPromoCampaignName();
        String queryString = "{ \"code\": \"" + promoCode + "\" }";
        return queryString;
    }

    public void clickPercentageRadioButton() {
        driverTools.clickWithJavascriptExecutor(PERCENTAGE_RADIO_LOCATOR);
    }

    public void clickPercentageRadioButton(boolean partialPercentage) {
        List<WebElement> elements = driverTools.elements(PERCENTAGE_RADIO_LOCATOR).get();
        driverTools.clickWithJavascriptExecutor(elements.get(0));
        if (!partialPercentage && elements.size() > 1) {
            driverTools.clickWithJavascriptExecutor(elements.get(1));
        }
    }

    public void clickSelectPaymentMethodRadioButton() {
        WebElement element = driverTools.waitForElementToAppear(getSelectPaymentMethod(1)).orElseThrow();
        driverTools.clickWithJavascriptExecutor(element);
    }

    public void addCurrentDateToPromoDescription() {
        String currentDescription = getTextInDescriptionBox();
        String currentDate = getCurrentDateDD_MM_YYYY();
        String text = currentDescription.split("//")[0] + "//" + currentDate;
        clearTextInDescriptionBox();
        sendTextInDescriptionBox(text);
    }

    public String getCampaignOverviewValue(String key) {
        driverTools.waitForElementToAppear(getCampaignOverviewValueLocator(key));
        return driverTools.getText(getCampaignOverviewValueLocator(key)).orElseThrow();
    }

    public String getPromoRulesValue(String key) {
        driverTools.waitForElementToAppear(getPromoRulesValueLocator(key));
        return driverTools.getText(getPromoRulesValueLocator(key)).orElseThrow();
    }

    public String getPromoStatusOverview() throws NotImplementedException {
        driverTools.waitForElementToAppear(PROMO_STATUS_TEXT);
        return getPromoStatus(driverTools.getText(PROMO_STATUS_TEXT).orElseThrow());
    }

    public String getPromoStatusKeyFromValue(String value) {
        return STATUS_MAP.entrySet().stream()
                .filter(entry -> entry.getValue().equals(value))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown value: " + value));
    }

    public void waitForListLoaderDissapear() {
        String message =
                driverTools.waitForElementToDisappear(LIST_LOADING) ? "List loader dissapeared"
                        : "List loader not dissapeared";
        logger.log(message);
    }

    public void goToTabChangesHistory() {
        driverTools.clickWithJavascriptExecutor(TAB_CHANGES_HISTORY);
    }

    public void clickToggleEditCampaignPeriod() {
        WebElement toggle = driverTools.waitForElementToAppear(TOGGLE_EDIT_CAMPAIGN_PERIOD)
                .orElseThrow();
        driverTools.click(toggle);
    }

    public boolean isPromoListEmpty() {
        waitForListLoaderDissapear();
        return driverTools.waitForElementToAppear(NO_DATA_PROMO_LIST, 3).isPresent();
    }

    public void deleteInApprovalPromo() {
        waitForListLoaderDissapear();
        driverTools.scrollToElement(FOOTER);
        Optional<WebElement> emptyData = driverTools.waitForElementToAppear(NO_DATA_PROMO_LIST, 2);
        if (emptyData.isEmpty()) {
            clickActionDropDown();
            clickActionTypeInDropdown("Delete");
            driverTools.clickWithJavascriptExecutor(MODAL_DELETE_BUTTON_LOCATOR);
        }
        driverTools.scrollToElement(CREATE_NEW_PROMO_LOCATOR);
    }

    public void goToPromoListWithAlertHandler() {
        WebElement promoList = driverTools.waitForElementToAppear(NEW_DASHBOARD).orElseThrow();
        promoList.click();
        try {
            clickLeaveButton();
        } catch (InterruptedException e) {
            logger.log(e.getLocalizedMessage());
        }
        try {
            driver.switchTo().alert().accept();
        } catch (NoAlertPresentException e) {
            logger.log(e.getLocalizedMessage());
        }
    }

    public int openLatestChangesHistoryByActivty(String activity) {
        List<WebElement> changes = driverTools.waitForElementsToAppear(ACCORDION_CHANGES_HISTORY)
                .orElseThrow();
        WebElement latestChanges;
        driverTools.scrollToElement(FOOTER);
        for (int i = changes.size() - 1; i >= 0; i--) {
            latestChanges = changes.get(i);
            logger.log("i value is: " + i);
            driverTools.clickWithJavascriptExecutor(latestChanges);
            WebElement historyActivity = driverTools.waitForElementToAppear(
                    CHANGES_HISTORY_ACTIVITY).orElseThrow();
            String activityType = driverTools.getText(historyActivity);
            if (activityType.equals(activity)) {
                logger.log("Found Activity: " + activity + " on Accordion element: " + i);
                return i;
            }
            driverTools.clickWithJavascriptExecutor(latestChanges);
            driverTools.waitForElementToDisappear(CHANGES_HISTORY_ACTIVITY);
        }
        Assertion.fail("not found any such activity: " + activity);
        return 0;
    }

    public String getChangesHistoryDateActor(int index) {
        List<WebElement> changes = driverTools.waitForElementsToAppear(CHANGES_HISTORY_DATE_ACTOR)
                .orElseThrow();
        WebElement latestChanges = changes.get(index);
        driverTools.scrollToElement(FOOTER);
        return driverTools.getText(latestChanges);
    }

    public String getChangesHistoryValueByFieldName(String fieldName) {
        Optional<WebElement> field = driverTools.waitForElementToAppear(
                getChangesHistoryValue(fieldName));
        return field.isPresent() ? driverTools.getText(field.get()) : fieldName + " not found";
    }

    public boolean isEditCampaignPeriodAndPromoQuotaEnabled() {
        Locator campaignStart = inputLocator("campaign-start-date");
        return driverTools.isElementEnabled(campaignStart);
    }

    public void clickPercentageRadioButtonWithJs(boolean partialPercentage) {
        Locator firstPct = percentageRadioSpanAtIndex(1);
        driverTools.waitForElementToAppear(firstPct).orElseThrow();
        driverTools.clickWithJavascriptExecutor(firstPct);
        if (!partialPercentage && driver.findElements(PERCENTAGE_RADIO_LOCATOR.by()).size() > 1) {
            driverTools.clickWithJavascriptExecutor(percentageRadioSpanAtIndex(2));
        }
    }

    private static Locator percentageRadioSpanAtIndex(int oneBasedIndex) {
        return new Locator("PERCENTAGE_RADIO_SPAN_" + oneBasedIndex, By.xpath(
                "(//input[contains(@value,'percentageAmount')]/following-sibling::span)[" + oneBasedIndex + "]"));
    }

    public float getDiscountAmountValue() {
        String raw = driverTools.getText(DISCOUNT_AMOUNT_LOCATOR).orElseThrow();
        logger.log("discount amount -> " + removeText(raw));
        return removeText(raw);
    }

    public void editGenerateRandomAmountOfDiscount(String id, int min, int max) {
        clickPercentageRadioButtonWithJs(true);
        Locator fixedAmount = radioButtonLocator("fixedAmount");
        driverTools.waitForElementToAppear(fixedAmount).orElseThrow();
        driverTools.clickWithJavascriptExecutor(fixedAmount);
        String text = String.valueOf(generateRandomInt(min, max));
        driverTools.sendKeys(inputLocator(id), text);
    }

    public void clickToggleAndEditCampaignPeriod() throws InterruptedException {
        driverTools.scrollIntoWebView(EDIT_CAMPAIGN_TOGGLE_BUTTON);
        driverTools.clickWithJavascriptExecutor(EDIT_CAMPAIGN_TOGGLE_BUTTON);
        scorllTillClickable(2, 250);
        replaceCampaignPeriodWithToday();
    }

    private void replaceCampaignPeriodWithToday() throws InterruptedException {
        String todayDay = String.valueOf(LocalDate.now().getDayOfMonth());
        driverTools.waitForElementToAppear(FIRST_CAMPAIGN_PERIOD_DELETE_BUTTON, 5).ifPresent(driverTools::clickWithJavascriptExecutor);
        driverTools.waitForElementToAppear(MODAL_DELETE_BUTTON_LOCATOR, 2).ifPresent(driverTools::clickWithJavascriptExecutor);
        selectDateFromCalendar("campaign-start-date", todayDay);
        selectDateFromCalendar("campaign-end-date", todayDay);
        clickAddPeriod();
        logger.log("Campaign period replaced with today (day " + todayDay + ") until end of day");
    }

    public void scrollTillClickable(int i, int distanceInPixels) throws InterruptedException {
        driverTools.scroll(i, distanceInPixels);
    }

    public String formatPeriodAndTime(String text, String key) {
        String regex = "(\\w+)•(\\d{2} \\w{3} \\d{4}) - (\\d{2} \\w{3} \\d{4})•(\\d{2}:\\d{2}:\\d{2}) - (\\d{2}:\\d{2}:\\d{2})";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            String type = matcher.group(1);
            String startDate = matcher.group(2);
            String endDate = matcher.group(3);
            String startTime = matcher.group(4);
            String endTime = matcher.group(5);
            String startDateTime = startDate + " " + startTime;
            String endDateTime = endDate + " " + endTime;
            switch (key) {
                case "start":
                    return startDateTime;
                case "end":
                    return endDateTime;
                case "type":
                    return type;
                default:
                    logger.log("Invalid key");
            }
        } else {
            logger.log("No match found");
        }
        return "";
    }

    public String getCampaignEndDateFromDb(String promoCodeData) {
        JSONObject jsonMap = new JSONObject(promoCodeData);
        JSONArray campaignPeriods = jsonMap.getJSONArray("campaignPeriods");
        JSONObject endDateObject = campaignPeriods.getJSONObject(0).getJSONObject("endDate");
        long endDateTimestamp = endDateObject.getLong("$date");
        ZonedDateTime zonedDateTime = Instant.ofEpochMilli(endDateTimestamp)
                .atZone(ZoneId.systemDefault());
        ZonedDateTime adjustedDateTime = zonedDateTime.minusHours(7);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss");
        return adjustedDateTime.format(formatter);
    }

    public void clickQuotaType(String fieldName) {
        WebElement element = driverTools.waitForElementToAppear(getQuotaType(fieldName)).orElseThrow();
        driverTools.clickWithJavascriptExecutor(element);
    }

    public void clickDuplicateRuleSet() {
        WebElement element = driverTools.waitForElementToAppear(DUPLICATE_RULE_SET_LOCATOR).orElseThrow();
        driverTools.clickWithJavascriptExecutor(element);
    }

    public void inputAddQuotaAndSave(String text, boolean isToday) {
        WebElement input = driverTools.waitForElementToAppear(INPUT_ADD_QUOTA_FIELD).orElseThrow();
        if (isToday) {
            driverTools.clickWithJavascriptExecutor(ADD_QUOTA_TODAY_RADIO);
        }
        input.sendKeys(text);
        driverTools.waitForElementToBeClickable(ADD_ADD_QUOTA_BUTTON);
        driverTools.clickWithJavascriptExecutor(ADD_ADD_QUOTA_BUTTON);
    }

    public int getCurrentQuotaFromList() {
        WebElement quota = driverTools.waitForElementToAppear(REMAINING_QUOTA_TABLE).orElseThrow();
        String quotaText = quota.getText();
        String[] parts = quotaText.split("/");
        return (int) Double.parseDouble(parts[1].trim());
    }

    public void navigateToOtherApprovalTab(String tab) {
        WebElement tabElement = driverTools.waitForElementToAppear(approvalTab(tab), 3)
                .orElseThrow();
        tabElement.click();
    }

    public List<String> getQuotasTvFromDb(String promoCodeData) {
        List<String> quotas = new ArrayList<>();
        JSONObject jsonMap = new JSONObject(promoCodeData);
        JSONArray ruleSets = jsonMap.getJSONArray("ruleSets");
        for (int i = 0; i < ruleSets.length(); i++) {
            JSONObject ruleSet = ruleSets.getJSONObject(i);
            JSONArray discountRules = ruleSet.getJSONArray("discountRules");
            for (int j = 0; j < discountRules.length(); j++) {
                JSONObject discountRule = discountRules.getJSONObject(j);
                int quotaValue = discountRule.getInt("quota");
                quotas.add(String.valueOf(quotaValue));
            }
        }
        return quotas;
    }

    public List<String> getPromoCodeMongoAdditionalQuota(String configType, String dataBase,
                                                         String collectionName, String queryString) {
        FindIterable<Document> result = TiketMongoDBManager.searchInMongoDB(configType, dataBase,
                collectionName, queryString);
        List<String> mongoData = new ArrayList<>();
        for (Document document : result) {
            if (document.containsKey("additionalQuotaDetails")) {
                List<Document> additionalQuotaDetails = (List<Document>) document.get(
                        "additionalQuotaDetails");
                for (Document detail : additionalQuotaDetails) {
                    if (detail.containsKey("additionalQuota")) {
                        Object value = detail.get("additionalQuota");
                        mongoData.add(value.toString());
                    }
                }
            }
        }
        return mongoData;
    }

    public String getTodayDate() {
        return CoreFunctions.getCurrentDateYYYY_MM_DD();
    }

    public List<String> createPromo(String uniqueLinkName,
                                    String username,
                                    String accountId,
                                    String quotaLevel,
                                    String quotaPromoCodeLevel,
                                    String code,
                                    String campaignName,
                                    String startDate,
                                    String endDate,
                                    String createdDate,
                                    String updatedDate) throws ExecutionException, InterruptedException {
        return createPromo(uniqueLinkName, username, accountId, quotaLevel, quotaPromoCodeLevel,
                code, campaignName, startDate, endDate, createdDate, updatedDate, null);
    }

    public List<String> createPromo(String uniqueLinkName,
                                    String username,
                                    String accountId,
                                    String quotaLevel,
                                    String quotaPromoCodeLevel,
                                    String code,
                                    String campaignName,
                                    String startDate,
                                    String endDate,
                                    String createdDate,
                                    String updatedDate,
                                    String budgetId) throws ExecutionException, InterruptedException {
        PromoCreationHelper<?> promoCreationHelper = new PromoCreationHelper<>(session);
        String randomizer = getCurrentTime() + randomInt(2);
        String newCampaignName = campaignName + randomizer;
        String newPromoCode = code + randomizer;
        String tags = uniqueLinkName + randomizer;
        PromoCodeDataResponse res = promoCreationHelper.createPromoApi(
                username,
                accountId,
                tags,
                quotaLevel,
                quotaPromoCodeLevel,
                newPromoCode,
                newCampaignName,
                startDate,
                endDate,
                createdDate,
                updatedDate,
                budgetId
        );
        String createdCode = res.data.code;
        String createdId = res.data.id;
        promoCreationHelper.approvePromoApi(username, accountId, createdId);
        return Arrays.asList(createdCode, createdId);
    }

    public List<String> createCreditCardPromo(String uniqueLinkName,
                                              String username,
                                              String accountId,
                                              String quotaLevel,
                                              String quotaPromoCodeLevel,
                                              String code,
                                              String campaignName,
                                              String startDate,
                                              String endDate,
                                              String createdDate,
                                              String updatedDate) throws ExecutionException, InterruptedException {
        return createCreditCardPromo(uniqueLinkName, username, accountId, quotaLevel, quotaPromoCodeLevel,
                code, campaignName, startDate, endDate, createdDate, updatedDate, null);
    }

    public List<String> createCreditCardPromo(String uniqueLinkName,
                                              String username,
                                              String accountId,
                                              String quotaLevel,
                                              String quotaPromoCodeLevel,
                                              String code,
                                              String campaignName,
                                              String startDate,
                                              String endDate,
                                              String createdDate,
                                              String updatedDate,
                                              String budgetId) throws ExecutionException, InterruptedException {
        PromoCreationHelper<?> promoCreationHelper = new PromoCreationHelper<>(session);
        String randomizer = getCurrentTime() + randomInt(2);
        String newCampaignName = campaignName + randomizer;
        String newPromoCode = code + randomizer;
        String tags = uniqueLinkName + randomizer;
        PromoCodeDataResponse res = promoCreationHelper.createCreditCardPromoApi(
                username,
                accountId,
                tags,
                quotaLevel,
                quotaPromoCodeLevel,
                newPromoCode,
                newCampaignName,
                startDate,
                endDate,
                createdDate,
                updatedDate,
                budgetId
        );
        String createdCode = res.data.code;
        String createdId = res.data.id;
        promoCreationHelper.approvePromoApi(username, accountId, createdId);
        return Arrays.asList(createdCode, createdId);
    }

    public void inputPromoLinking(List<String> promoToUse, int useThis, String number) {
        driverTools.click(getCampaignLinkDropdown(number));
        driverTools.sendKeys(getCampaignLinkInput(number), promoToUse.get(useThis));
        WebElement firstValue = driverTools.waitForElementToAppear(
                getCampaignLinkDropdownValue(promoToUse.get(useThis)), 3).orElseThrow();
        driverTools.waitForElementToBeClickable(firstValue, 10);
        driverTools.click(firstValue);
        driverTools.waitForElementToDisappear(firstValue);
    }

    public void inputPromoLinking(String promoToUse, String number) throws InterruptedException {
        scrollToBottom();
        driverTools.click(getCampaignLinkDropdown(number));
        driverTools.sendKeys(getCampaignLinkInput(number), promoToUse);
        WebElement firstValue = driverTools.waitForElementToAppear(
                getCampaignLinkDropdownValue(promoToUse), 3).orElseThrow();
        driverTools.waitForElementToBeClickable(firstValue, 10);
        driverTools.click(firstValue);
        driverTools.waitForElementToDisappear(firstValue);
    }

    public void clickSubmitPromoLink() {
        driverTools.scrollIntoWebView(CREATE_LINK_SUBMIT);
        driverTools.click(CREATE_LINK_SUBMIT);
    }

    public String createPromoLinking(
            String uniqueLinkName,
            String username,
            String accountId,
            String quotaLevel,
            String quotaPromoCodeLevel,
            String code,
            String campaignName,
            String startDate,
            String endDate,
            String createdDate,
            String updatedDate) throws ExecutionException, InterruptedException {
        List<String> promoToUse = new ArrayList<>();
        int i = 0;
        while (i < 3) {
            List<String> data = createPromo(
                    username,
                    accountId,
                    uniqueLinkName,
                    quotaLevel,
                    quotaPromoCodeLevel,
                    code,
                    campaignName,
                    startDate,
                    endDate,
                    createdDate,
                    updatedDate
            );
            String createdCode = data.get(0);
            if (!promoToUse.contains(createdCode)) {
                promoToUse.add(createdCode);
                i++;
            }
        }

        String tagsLink = uniqueLinkName + getCurrentTime();
        driverTools.click(CREATE_UNIQUE_LINK_BUTTON);
        WebElement linkNameInput = driverTools.waitForElementToAppear(UNIQUE_LINK_NAME_INPUT).orElseThrow();
        driverTools.sendKeys(linkNameInput, tagsLink);
        inputPromoLinking(promoToUse, 0, "1");
        inputPromoLinking(promoToUse, 1, "2");
        clickSubmitPromoLink();
        return tagsLink;
    }

    public String getStatusInDashboard() {
        WebElement status = driverTools.waitForElementToAppear(STATUS_IN_TABLE).orElseThrow();
        return driverTools.getText(status).toUpperCase();
    }

    public void clickCollapsible() {
        WebElement collapsible = driverTools.waitForElementToAppear(COLLAPSIBLE_BUTTON)
                .orElseThrow();
        driverTools.click(collapsible);
    }

    public String getPromoLinkingQuotaDash() {
        WebElement element = driverTools.waitForElementToAppear(PROMO_LINKING_QUOTA_DASH)
                .orElseThrow();
        return driverTools.getText(element).trim();
    }

    private static String readHtmlInputCurrentValue(WebElement element) {
        try {
            String dom = element.getDomProperty("value");
            if (dom != null && !dom.isBlank()) {
                return dom.trim();
            }
        } catch (Exception ignored) {
        }
        String attr = element.getAttribute("value");
        return attr != null ? attr.trim() : "";
    }

    public List<String> fetchExistingStartEndDateLink(String config, String dbName, String fallbackPromoCode) {
        String refPromo = "";
        for (int i = 0; i < 45; i++) {
            WebElement element = driverTools.waitForElementToAppear(getCampaignLinkInput("1"))
                    .orElseThrow();
            refPromo = readHtmlInputCurrentValue(element);
            if (!refPromo.isEmpty()) {
                break;
            }
            driverTools.dummyWait(1);
        }
        if (refPromo.isEmpty() && fallbackPromoCode != null && !fallbackPromoCode.isBlank()) {
            refPromo = fallbackPromoCode.trim();
        }
        if (refPromo.isEmpty()) {
            throw new IllegalStateException(
                    "Campaign link slot 1 has no promo code yet; cannot load dates from Mongo. "
                            + "Provide fallbackPromoCode (sheet 'code') or wait for the form to hydrate.");
        }
        String data = getPromoCodeMongoFullData(config, dbName,
                "promoCodeData", "{ \"code\": \"" + refPromo + "\", \"promoCodeStatus\": \"ACTIVE\" }");
        JSONObject jsonObject = new JSONObject(data);
        JSONArray campaignPeriods = jsonObject.getJSONArray("campaignPeriods");
        JSONObject firstPeriod = campaignPeriods.getJSONObject(0);
        Date startDate = new Date(firstPeriod.getJSONObject("startDate").getLong("$date"));
        Date endDate = new Date(firstPeriod.getJSONObject("endDate").getLong("$date"));
        SimpleDateFormat desiredFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedStartDate = desiredFormat.format(startDate);
        String formattedEndDate = desiredFormat.format(endDate);
        return Arrays.asList(formattedStartDate, formattedEndDate);
    }

    public void clickAddCampaignLink() {
        driverTools.click(ADD_CAMPAIGN_LINK_BUTTON);
    }

    public boolean isNewlyEditedPromoShown(String promoCode) {
        WebElement element = driverTools.waitForElementToAppear(getPromoCodeInTableLink(promoCode))
                .orElseThrow();
        return element.isDisplayed();
    }

    public String extractNameFromPrefix(String name, String prefix) {
        String regex = "^" + prefix + "\\d+_.*";
        if (name.matches(regex)) {
            return name.substring(name.indexOf('_') + 1);
        } else {
            return name;
        }
    }

    public int getPromoLinkMembersCount(String jsonData) {
        String jsonString = jsonData.replace("=", ":");
        jsonString = jsonString
                .replaceAll("(\\w+):", "\"$1\":")
                .replaceAll(":\\[", ":\\[\"")
                .replaceAll("\\],", "\"],")
                .replaceAll("\\]}", "\"]}")
                .replaceAll(":([a-zA-Z0-9@.]+)", ":\"$1\"");
        JSONArray jsonArray = new JSONArray(jsonString);
        return jsonArray.length();
    }

    public void uploadFile(String path) {
        String resolvedPath = resolveLocalUploadPath(path);
        driverTools.waitForElementToPresence(UPLOAD_BOX, 3).orElseThrow();
        driverTools.sendKeys(UPLOAD_INPUT, resolvedPath);
    }

    private static String resolveLocalUploadPath(String path) {
        if (path == null || path.isBlank()) {
            return path;
        }
        File asGiven = new File(path);
        if (asGiven.isFile()) {
            return asGiven.getAbsolutePath();
        }
        String relativeToProject = path.replaceFirst("^/+", "");
        File underUserDir = new File(System.getProperty("user.dir"), relativeToProject);
        if (underUserDir.isFile()) {
            return underUserDir.getAbsolutePath();
        }
        return path;
    }

    public void clickGenerateTnc() {
        ensureMandatoryMetadataDropdownSelected();
        driverTools.waitForElementToAppear(GENERATE_TNC_BUTTON).orElseThrow();
        driverTools.click(GENERATE_TNC_BUTTON);
    };

    public void waitForGenerateTncFinish() throws InterruptedException {
        driverTools.waitForElementToAppear(EN_DESC_LOCATOR, 30).orElseThrow();
        scrollToBottom();
    }

    private boolean fillFieldIfPresent(Map<String, String> params, String fieldIdKey, String valueKey, FieldType fieldType)
            throws InterruptedException {
        if (!params.containsKey(fieldIdKey) || !params.containsKey(valueKey)) {
            return false;
        }
        fillField(params.get(fieldIdKey), params.get(valueKey), fieldType);
        return true;
    }

    private void selectDateIfPresent(Map<String, String> params, String fieldIdKey, String dateKey)
            throws InterruptedException {
        if (params.containsKey(fieldIdKey) && params.containsKey(dateKey)) {
            selectDateFromCalendar(params.get(fieldIdKey), params.get(dateKey));
        }
    }

    private void selectDropdownOptionIfPresent(Map<String, String> params, String dropdownIdKey, String optionKey) {
        if (params.containsKey(dropdownIdKey) && params.containsKey(optionKey)) {
            clickDropdown(params.get(dropdownIdKey));
            clickInsideDropdown(params.get(optionKey));
        }
    }

    public void fillField(String fieldId, String value, FieldType fieldType) throws InterruptedException {
        switch (fieldType) {
            case DROPDOWN:
                scrollToElementById(fieldId);
                clickDropdown(fieldId);
                clickInsideDropdown(value);
                break;
            case TEXT_INPUT:
                scrollToElementById(fieldId);
                sendTextInInput(fieldId, value);
                break;
            case DATE_PICKER:
                selectDateFromCalendar(fieldId, value);
                break;
            case RADIO_BUTTON:
                WebElement radio = driverTools.waitForElementToAppear(radioButtonLocator(value)).orElseThrow();
                driverTools.scrollElementToCenter(radio);
                driverTools.click(radio);
                break;
            case CHECKBOX:
                WebElement checkbox = driverTools.waitForElementToAppear(inputLocator(fieldId)).orElseThrow();
                driverTools.scrollAndClick(checkbox);
                break;
            default:
                logger.log("Unknown field type: " + fieldType);
        }
    }

    private static final String MANDATORY_METADATA_DROPDOWN_ID = "dropdown-mandatory";
    private static final String MANDATORY_METADATA_DROPDOWN_DEFAULT = "Label1";

    public void ensureMandatoryMetadataDropdownSelected() {
        ensureMandatoryMetadataDropdownSelected(MANDATORY_METADATA_DROPDOWN_DEFAULT);
    }

    public void ensureMandatoryMetadataDropdownSelected(String value) {
        Optional<WebElement> dropdown =
                driverTools.waitForElementToPresence(dropdownBoxLocator(MANDATORY_METADATA_DROPDOWN_ID), 3);
        if (dropdown.isEmpty()) {
            logger.log("[Mandatory Metadata] '#" + MANDATORY_METADATA_DROPDOWN_ID + "' not on page; skipping");
            return;
        }
        driverTools.scrollElementToCenter(dropdown.get());
        String current = dropdown.get().getAttribute("value");
        if (current != null && !current.isBlank()) {
            logger.log("[Mandatory Metadata] '#" + MANDATORY_METADATA_DROPDOWN_ID + "' already '" + current + "'; skipping");
            return;
        }
        logger.log("[Mandatory Metadata] selecting '" + value + "' in '#" + MANDATORY_METADATA_DROPDOWN_ID + "'");
        clickDropdown(MANDATORY_METADATA_DROPDOWN_ID);
        clickInsideDropdown(value);
    }

    public String fillGeneralInformationSection(Map<String, String> params) throws InterruptedException {
        String generatedName;
        if (params.containsKey("promoTypeId")) {
            if (params.containsKey("instant")) {
                fillField(params.get("promoTypeId"), params.get("instant"), FieldType.DROPDOWN);
            } else if (params.containsKey("cashback")) {
                fillField(params.get("promoTypeId"), params.get("cashback"), FieldType.DROPDOWN);
            } else if (params.containsKey("hybrid")) {
                fillField(params.get("promoTypeId"), params.get("hybrid"), FieldType.DROPDOWN);
            }
        }
        fillFieldIfPresent(params, "campaignTypeId", "testing", FieldType.DROPDOWN);
        generatedName = sendTextInCampaignNameInput();

        if (params.containsKey("identifier")) {
            sendTextInUniqueIdentifierInput(params.get("identifier"));
        }

        if (params.containsKey("description")) {
            sendTextInDescriptionBox(params.get("description"));
        }
        scorllTillClickable();
        fillFieldIfPresent(params, "divisionId", "ben", FieldType.DROPDOWN);
        fillFieldIfPresent(params, "selectCurrencyId", "idr", FieldType.DROPDOWN);
        fillFieldIfPresent(params, "verticalId", "hotel", FieldType.DROPDOWN);

        ensureMandatoryMetadataDropdownSelected(
                params.getOrDefault("dropdownMandatory", MANDATORY_METADATA_DROPDOWN_DEFAULT));
        return generatedName;
    }

    public void fillCampaignPeriodSection(Map<String, String> params) throws InterruptedException {
        selectDateIfPresent(params, "campaignStartDateId", "startDate");
        selectDateIfPresent(params, "campaignEndDateId", "endDate");
        driverTools.scrollAndClick(ADD_PERIOD_BUTTON_LOCATOR);

        scorllTillClickable();
    }

    public void fillAffiliateAudienceSection(Map<String, String> params) throws InterruptedException {
    }

    public void fillQuotaTypeSection(Map<String, String> params) throws InterruptedException {
        boolean isTransactionValueLevel = params.containsKey("promo")
                && "At transaction value".equals(params.get("promo"));

        if (isTransactionValueLevel) {
            clickTransactionRadioBox();
            fillFieldIfPresent(params, "promoVisibilityID", "promoValue", FieldType.DROPDOWN);
        } else {
            if (!fillFieldIfPresent(params, "budgetId", "budgetValue", FieldType.DROPDOWN)) {
                fillFieldIfPresent(params, "quotaId", "quotaText", FieldType.TEXT_INPUT);
            }
        }
    }

    public void fillDynamicPromoValueSection(Map<String, String> params) throws InterruptedException {
    }

    public void fillPromoRulesParameterSection(Map<String, String> params) throws InterruptedException {
        fillFieldIfPresent(params, "ruleVerticalId", "hotel", FieldType.DROPDOWN);
        if (params.containsKey("parameterId")) {
            if (params.containsKey("accountIdParam")) {
                fillField(params.get("parameterId"), params.get("accountIdParam"), FieldType.DROPDOWN);
            } else if (params.containsKey("accomodationType")) {
                fillField(params.get("parameterId"), params.get("accomodationType"), FieldType.DROPDOWN);
            }
        }
        if (params.containsKey("operatorId")) {
            scrollToElementById(params.get("operatorId"));
            clickDropdown(params.get("operatorId"));
            if (params.containsKey("path")) {
                clickEqualToMultiValueInDropdown();
            } else {
                clickEqualToInDropdown();
            }
        }
        if (params.containsKey("path")) {
            uploadFile(params.get("path"));
            scrollTillClickable(1, 200);
        } else if (params.containsKey("valuesId") && params.containsKey("valuesText")) {
            fillField(params.get("valuesId"), params.get("valuesText"), FieldType.TEXT_INPUT);
            driverTools.scrollAndClick(ADD_VALUES_BUTTON_LOCATOR);
        }
    }

    public void fillTransactionValueSection(Map<String, String> params) throws InterruptedException {
        boolean isTransactionValueLevel = params.containsKey("promo")
                && "At transaction value".equals(params.get("promo"));
        fillFieldIfPresent(params, "minTraValueId", "minTraText", FieldType.TEXT_INPUT);
        fillFieldIfPresent(params, "maxTraValueId", "maxTraText", FieldType.TEXT_INPUT);
        if (isTransactionValueLevel) {
            fillFieldIfPresent(params, "budgetId", "budgetValue", FieldType.DROPDOWN);
            fillFieldIfPresent(params, "ruleNameEN", "countText", FieldType.TEXT_INPUT);
            fillFieldIfPresent(params, "ruleNameID", "countText", FieldType.TEXT_INPUT);
        }
    }

    public void fillDiscountSection(Map<String, String> params) throws InterruptedException {
        boolean isTransactionValueLevel = params.containsKey("promo")
                && "At transaction value".equals(params.get("promo"));
        boolean hasPercentage = !isTransactionValueLevel
                && params.containsKey("percentage") && params.containsKey("percText");

        if (hasPercentage) {
            boolean partialPercentage = Boolean.parseBoolean(
                    params.getOrDefault("partialPercentage", "false"));
            clickPercentageRadioButton(partialPercentage);
            fillField(params.get("percentage"), params.get("percText"), FieldType.TEXT_INPUT);
            fillFieldIfPresent(params, "maxAmount", "maxAmountText", FieldType.TEXT_INPUT);
            fillFieldIfPresent(params, "amountOfCashbackId", "cashbackText", FieldType.TEXT_INPUT);
        } else {
            if (!fillFieldIfPresent(params, "amountOfDiscountId", "cashbackText", FieldType.TEXT_INPUT)) {
                fillFieldIfPresent(params, "amountOfCashbackId", "cashbackText", FieldType.TEXT_INPUT);
            }
        }

        if (params.containsKey("instant") && !hasPercentage && !isTransactionValueLevel) {
            if (params.containsKey("fixedValueText")) {
                fillField(FIXED_VALUE_FIELD_ID, params.get("fixedValueText"), FieldType.TEXT_INPUT);
            } else if (params.containsKey("cashbackText")) {
                fillField(FIXED_VALUE_FIELD_ID, params.get("cashbackText"), FieldType.TEXT_INPUT);
            }
        }
    }

    public void fillPaymentMethodSection(Map<String, String> params) throws InterruptedException {
        if (params.containsKey("paymentMethodId") && params.containsKey("paymentMethodText")) {
            clickSelectPaymentMethodRadioButton();
            clickDropdownPaymentMethod(params.get("paymentMethodId"));
            clickInsideDropdown(params.get("paymentMethodText"));
            clickDropdown(params.get("paymentMethodId"));
        }
    }

    public void fillCostAllocationSection(Map<String, String> params) throws InterruptedException {
        List<WebElement> dropdowns = driverTools.elements(dropdownBoxLocator(COST_ALLOCATION_FIELD_ID))
                .orElse(new ArrayList<>());
        if (dropdowns.isEmpty()) {
            selectCostAllocationIfPresent(params);
            return;
        }
        List<WebElement> fixedValueInputs = driverTools.elements(COST_ALLOC_FIXED_VALUE_LOCATOR)
                .orElse(new ArrayList<>());
        String fixedValue = resolveCostAllocationFixedValue(params);
        for (int i = 0; i < dropdowns.size(); i++) {
            String currentValue = dropdowns.get(i).getAttribute("value");
            if ("Both".equalsIgnoreCase(currentValue) && i < fixedValueInputs.size()) {
                fillIfEmpty(fixedValueInputs.get(i), fixedValue);
            } else if (currentValue == null || currentValue.isEmpty()) {
                selectCostAllocationIfPresent(params);
            }
        }
    }

    private void selectCostAllocationIfPresent(Map<String, String> params) throws InterruptedException {
        if (!params.containsKey("costAllocationId") || !params.containsKey("tiket")) {
            return;
        }
        if (params.containsKey("hybrid")) {
            clickHybridCostAllocationDropdown(params.get("costAllocationId"), params.get("tiket"));
        } else {
            fillField(params.get("costAllocationId"), params.get("tiket"), FieldType.DROPDOWN);
        }
    }

    private void fillIfEmpty(WebElement input, String value) {
        String existingValue = input.getAttribute("value");
        if (value == null || (existingValue != null && !existingValue.isEmpty())) {
            return;
        }
        driverTools.scrollElementToCenter(input);
        driverTools.clickWithJavascriptExecutor(input);
        driverTools.sendKeys(input, value);
    }

    private String resolveCostAllocationFixedValue(Map<String, String> params) {
        if (params.containsKey("costAllocationFixedValue")) return params.get("costAllocationFixedValue");
        if (params.containsKey("fixedValueText")) return params.get("fixedValueText");
        return params.get("cashbackText");
    }

    public void fillFraudPreventionSection(Map<String, String> params) throws InterruptedException {
        fillFieldIfPresent(params, "selectUsageCountId", "countText", FieldType.TEXT_INPUT);
        fillFieldIfPresent(params, "usagePeriodId", "campaignPeriod", FieldType.DROPDOWN);
        if (params.containsKey("platformId") && params.containsKey("android")) {
            fillField(params.get("platformId"), params.get("android"), FieldType.DROPDOWN);
            clickDropdown(params.get("platformId"));
        }
        if (params.containsKey("instant")) {
            fillFieldIfPresent(params, "loginStateId", "loggedIn", FieldType.DROPDOWN);
            driverTools.scrollAndClick(VERIFIED_RADIOBOX_LOCATOR);
        }
        driverTools.scrollAndClick(USERNAME_LOCATOR);
    }

    public void fillPromoCodeSection(Map<String, String> params, String promoName) throws InterruptedException {
        sendTextInPromoNameInput(promoName);
        ensureMandatoryMetadataDropdownSelected();
        driverTools.scrollAndClick(GENERATE_TNC_BUTTON);
        waitForGenerateTncFinish();
    }

    public String fillPromoForm(Map<String, String> params) throws InterruptedException {
        step("[1/12] General Information");
        String generatedName = fillGeneralInformationSection(params);
        step("[2/12] Affiliate Audience");
        fillAffiliateAudienceSection(params);
        step("[3/12] Campaign Period");
        fillCampaignPeriodSection(params);
        step("[4/12] Quota Type");
        fillQuotaTypeSection(params);
        step("[5/12] Dynamic Promo Value");
        fillDynamicPromoValueSection(params);
        step("[6/12] Promo Rules (Rule Set & Parameter)");
        fillPromoRulesParameterSection(params);
        step("[7/12] Transaction Value");
        fillTransactionValueSection(params);
        step("[8/12] Discount");
        fillDiscountSection(params);
        step("[9/12] Payment Method");
        fillPaymentMethodSection(params);
        step("[10/12] Cost Allocation");
        fillCostAllocationSection(params);
        step("[11/12] Fraud Prevention");
        fillFraudPreventionSection(params);
        step("[12/12] Promo Code");
        fillPromoCodeSection(params, generatedName);

        return generatedName;
    }

    public String editPromoFields(Map<String, String> params) throws InterruptedException {
        clickEditButton();
        if (params.containsKey("description")) {
            clearTextInDescriptionBox();
            sendTextInDescriptionBox(params.get("description"));
        }
        scorllTillClickable();
        selectDropdownOptionIfPresent(params, "divisionId", "alltierben");
        selectDropdownOptionIfPresent(params, "selectCurrencyId", "usd");

        String promoCode = returnCampaignName();
        driverTools.scrollAndClick(EDIT_QUOTA_CAMPAIGN_TOGGLE);
        clickPreviewButton();
        clickSaveChangesButton();
        clickSaveChangesPopupButton();
        return promoCode;
    }

    public void submitPromoForm() throws InterruptedException {
        clickPreviewButton();
        clickCreateCampaignButton();
        clickSendForApprovalButton();
    }

    public void savePromoFormAsDraft() throws InterruptedException {
        clickCancelButton();
        clickSaveAsDraftButton();
    }

    public void searchAndFilterPromo(String promoCode, String filterStatus) throws InterruptedException {
        clickResetFilter();
        Thread.sleep(1000);
        inputSearchPromo(promoCode);
        if (filterStatus != null && !filterStatus.isEmpty()) {
            filterSearchByStatus(filterStatus);
        }
        clickOnSearch();
    }

    public void performPromoAction(String actionName) {
        clickActionDropDown();
        clickActionTypeInDropdown(actionName);
    }

    public void approvePromo(String promoCode) throws InterruptedException {
        goToPromoList();
        dismissLeavePageModalIfShown();
        openApprovalPage();
        inputSearchPromo(promoCode);
        clickOnSearch();
        clickAprroveButton();
        clickOnApproveModal();
    }

    public void savePromoChanges() throws InterruptedException {
        clickPreviewButton();
        clickSaveChangesButton();
        clickSaveChangesPopupButton();
    }

    public void ensurePromoIsActive(String promoCode) throws InterruptedException {
        if (!makePromoActive(promoCode)) {
            logger.log("No recoverable promo found for '" + promoCode + "'");
        }
        goToCleanPromoList();
    }

    public String ensureActivePromoOrCreate(String promoCode, Map<String, String> ownParams) throws InterruptedException {
        String activePromoCode = promoCode;
        if (!makePromoActive(promoCode)) {
            activePromoCode = createAndSubmitFreshPromo(ownParams);
            approvePromo(activePromoCode);
        }
        goToCleanPromoList();
        return activePromoCode;
    }

    public boolean cloneCurrentPromoToActive(String promoCode) throws InterruptedException {
        boolean isActive = cloneOpenPromoToActive(promoCode);
        goToCleanPromoList();
        return isActive;
    }

    public String createAndSubmitFreshPromo(Map<String, String> ownParams) throws InterruptedException {
        Map<String, String> createParams = resolvePromoCreationParams(ownParams);
        goToPromoList();
        clickCreateNewPromoButton();
        String freshPromoCode = fillPromoForm(createParams);
        submitPromoForm();
        return freshPromoCode;
    }

    public void filterPromoListByApprovalStatus() {
        clickApprovalStatus();
        clickApprovalStatusSelect();
        clickApprovalStatus();
        clickOnSearch();
    }

    public void dismissLeavePageModalIfShown() {
        driverTools.waitForElementToAppear(LEAVE_BUTTON_LOCATOR, 3).ifPresent(driverTools::clickWithJavascriptExecutor);
    }

    private boolean makePromoActive(String promoCode) throws InterruptedException {
        return hasPromoWithStatus(promoCode, STATUS_ACTIVE)
                || approveIfInApproval(promoCode)
                || reactivateIfInactive(promoCode)
                || cloneToActive(promoCode);
    }

    private boolean approveIfInApproval(String promoCode) throws InterruptedException {
        if (!hasPromoWithStatus(promoCode, STATUS_IN_APPROVAL)) {
            return false;
        }
        approvePromo(promoCode);
        return hasPromoWithStatus(promoCode, STATUS_ACTIVE);
    }

    private boolean reactivateIfInactive(String promoCode) throws InterruptedException {
        if (!hasPromoWithStatus(promoCode, STATUS_INACTIVE)) {
            return false;
        }
        performPromoAction("Edit");
        clickToggleAndEditCampaignPeriod();
        savePromoChanges();
        if (!isEditSaved()) {
            goToCleanPromoList();
            return false;
        }
        approvePromo(promoCode);
        return hasPromoWithStatus(promoCode, STATUS_ACTIVE);
    }

    private boolean cloneToActive(String promoCode) throws InterruptedException {
        if (!hasPromoWithStatus(promoCode, STATUS_INACTIVE) && !hasPromoWithStatus(promoCode, ANY_STATUS)) {
            return false;
        }
        clickOnSelectedPromo();
        return cloneOpenPromoToActive(promoCode);
    }

    private boolean cloneOpenPromoToActive(String promoCode) throws InterruptedException {
        clickClone();
        renameCampaignIfEditable();
        replaceCampaignPeriodWithToday();
        submitPromoForm();
        approvePromo(promoCode);
        return hasPromoWithStatus(promoCode, STATUS_ACTIVE);
    }

    private void renameCampaignIfEditable() {
        driverTools.waitForElementToAppear(CAMPAIGN_NAME_FIELD_LOCATOR);
        if (!driverTools.isElementEnabled(CAMPAIGN_NAME_FIELD_LOCATOR)) {
            return;
        }
        String uniqueName = "automation_panel_" + UUID.randomUUID().toString().replace("-", "").substring(0, 12);
        driverTools.clearInput(CAMPAIGN_NAME_FIELD_LOCATOR);
        driverTools.sendKeys(CAMPAIGN_NAME_FIELD_LOCATOR, uniqueName);
    }

    private boolean hasPromoWithStatus(String promoCode, String status) throws InterruptedException {
        goToPromoList();
        searchAndFilterPromo(promoCode, status);
        boolean found = !isPromoListEmpty();
        logger.log("Promo '" + promoCode + "' with status '" + status + "' found: " + found);
        return found;
    }

    private boolean isEditSaved() {
        driverTools.waitForElementToAppear(APPROVAL_POP_UP, 5);
        logger.log("Save Changes toast: '" + driverTools.getText(APPROVAL_POP_UP).orElse("") + "'");
        return !driver.getCurrentUrl().contains("/promo/edit/");
    }

    private void goToCleanPromoList() {
        goToPromoList();
        dismissLeavePageModalIfShown();
        clickResetFilter();
    }

    private Map<String, String> resolvePromoCreationParams(Map<String, String> ownParams) {
        if (ownParams.containsKey("promoTypeId")) {
            return ownParams;
        }
        return Optional.ofNullable(TestDataElement.titleParams.get(PROMO_CREATION_TEST_DATA))
                .filter(rows -> !rows.isEmpty())
                .map(rows -> rows.get(0))
                .orElseThrow(() -> new IllegalStateException("No promo in the required status and no promo creation"
                        + " data: add the creation fields of " + PROMO_CREATION_TEST_DATA + " to this test's data"
                        + " or run it together with " + PROMO_CREATION_TEST_DATA));
    }

    private void step(String description) {
        logger.log("=== " + description + " ===");
    }

    public enum FieldType {
        DROPDOWN,
        TEXT_INPUT,
        DATE_PICKER,

        RADIO_BUTTON,
        CHECKBOX
    }

    private static String instantPointCashbackVerticalKeyFromTestId(String dataTestId) {
        if (dataTestId == null || !dataTestId.startsWith(INSTANT_POINT_CASHBACK_TOGGLE_TESTID_PREFIX)) {
            return null;
        }
        return dataTestId.substring(INSTANT_POINT_CASHBACK_TOGGLE_TESTID_PREFIX.length());
    }

    private List<String> listInstantPointCashbackVerticalKeysOnPage() {
        LinkedHashSet<String> keys = new LinkedHashSet<>();
        for (WebElement el : driver.findElements(ANY_INSTANT_POINT_CASHBACK_VERTICAL_TOGGLE.by())) {
            String key = instantPointCashbackVerticalKeyFromTestId(el.getAttribute("data-testid"));
            if (key != null) {
                keys.add(key);
            }
        }
        return new ArrayList<>(keys);
    }

    public String prepareInstantPointCashbackVerticalForToggleOffTest() {
        driverTools.waitForElementToAppear(ANY_INSTANT_POINT_CASHBACK_TOGGLE_LABEL, 30).orElseThrow();
        driverTools.waitForElementToPresence(ANY_INSTANT_POINT_CASHBACK_VERTICAL_TOGGLE, 15).orElseThrow();
        for (String key : listInstantPointCashbackVerticalKeysOnPage()) {
            if (isVerticalInstantPointCashbackChecked(key)) {
                return key;
            }
        }
        for (String key : listInstantPointCashbackVerticalKeysOnPage()) {
            if (!isVerticalInstantPointCashbackChecked(key)) {
                toggleInstantPointCashbackVertical(key);
                return key;
            }
        }
        throw new IllegalStateException("No instant point cashback vertical toggles found on page");
    }

    public void toggleInstantPointCashbackVertical(String verticalKey) {
        Locator inputLoc = instantPointCashbackToggleLocator(verticalKey);
        Locator knobLoc = instantPointCashbackToggleKnobLocator(verticalKey);
        driverTools.waitForElementToPresence(inputLoc, 15).orElseThrow();
        driverTools.scrollIntoWebView(knobLoc);
        boolean beforeSelected = driverTools.element(inputLoc).map(WebElement::isSelected).orElse(false);
        driverTools.clickWithJavascriptExecutor(knobLoc);
        driverTools.waitForElementToAppear(SUBMIT_INSTANT_POINT_CASHBACK_VERTICAL_MODAL, 15).orElseThrow();
        driverTools.click(SUBMIT_INSTANT_POINT_CASHBACK_VERTICAL_MODAL);
        new WebDriverWait(driver, Duration.ofSeconds(25)).until(d ->
                driverTools.element(inputLoc)
                        .map(WebElement::isSelected)
                        .orElse(beforeSelected) != beforeSelected);
    }

    public boolean isVerticalInstantPointCashbackChecked(String verticalKey) {
        Locator loc = instantPointCashbackToggleLocator(verticalKey);
        return driverTools.waitForElementToPresence(loc, 10)
                .map(WebElement::isSelected)
                .orElse(false);
    }

    public void clickSeeDetailsBankBIN() {
        driverTools.waitForElementToPresence(SEE_BANK_BIN_DETAILS);
        driverTools.clickWithJavascriptExecutor(SEE_BANK_BIN_DETAILS);
    }

    public void checkBinTableContainsBINId(String bin, String colName) {
        String binNorm = bin == null ? "" : bin.trim();
        if (binNorm.isEmpty()) {
            throw new IllegalArgumentException("BIN must not be null or blank");
        }
        driverTools.waitForElementsToAppear(BANK_BIN_TABLE);
        int colIndex = getBinTableColumnIndex(colName);
        Locator loc = new Locator("COLUMN_" + colName, By.xpath(
                "//table[contains(@class, 'bin_table')]/tbody/tr[not(.//td[@colspan])]//td[" + colIndex + "]"));
        List<WebElement> elements = driverTools.waitForElementsToAppear(loc).orElseThrow();
        boolean result = false;
        for (WebElement element : elements) {
            String cell = normalizeRuleSetRowCompareText(driverTools.getText(element));
            if (cell.equals(binNorm) || (!binNorm.isEmpty() && cell.contains(binNorm))) {
                result = true;
                break;
            }
        }
        Assertion.assertThat("Verify that BIN " + bin + " contained in the BIN Table", result, equalTo(true));
    }

    public boolean verifyTncInDescription() {
        boolean desc = driverTools.isElementDisplayed(EN_DESC_LOCATOR) && driverTools.isElementDisplayed(ID_DESC_LOCATOR);
        boolean tnc = driverTools.isElementDisplayed(EN_TERMS_LOCATOR) && driverTools.isElementDisplayed(ID_TERMS_LOCATOR);

        return desc && tnc;
    }

    public String getStatusOfPromo(){
        driverTools.waitForElementToAppear(STATUS_IN_TABLE).orElseThrow();
        return String.valueOf(driverTools.getText(STATUS_IN_TABLE));
    }

    public boolean waitForPromoStatus(String promoName, String expectedStatus, int maxAttempts) {
        String last = "";
        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            Optional<WebElement> status = driverTools.waitForElementToAppear(STATUS_IN_TABLE, 10);
            if (status.isPresent()) {
                last = String.valueOf(driverTools.getText(status.get()));
                logger.log("[Promo Status] attempt " + (attempt + 1) + " -> '" + last
                        + "' (want '" + expectedStatus + "')");
                if (last.contains(expectedStatus)) {
                    return true;
                }
            }
            refreshPage();
            inputSearchPromo(promoName);
            clickOnSearch();
            driverTools.dummyWait(2);
        }
        logger.log("[Promo Status] did not reach '" + expectedStatus + "'; last seen '" + last + "'");
        return false;
    }

    public void goToPromoAdjustmentRule() {
        driverTools.waitForElementToAppear(PROMO_ADJUSTMENT_RULE_PAGE);
        driverTools.click(PROMO_ADJUSTMENT_RULE_PAGE);
    }

    public void pressAddNewRuleButton() {
        driverTools.waitForElementToAppear(ADD_NEW_RULE_BUTTON);
        driverTools.click(ADD_NEW_RULE_BUTTON);
    }

    public void fillRuleName(String ruleName) {
        driverTools.click(RULE_NAME_INPUT);
        driverTools.clearInput(RULE_NAME_INPUT);
        driverTools.sendKeys(RULE_NAME_INPUT, ruleName);
    }

    public void saveDynamicPromo() {
        driverTools.click(NEXT_BUTTON);
        driverTools.click(SAVE_BUTTON);
        driverTools.click(SAVE_CONFIRM_BUTTON);
    }

    public void searchRuleName(String ruleName) {
        waitForListLoaderDissapear();
        WebElement searchInput = driverTools.waitForElementToAppear(SEARCH_RULE_NAME_INPUT, 20)
                .orElseThrow();
        driverTools.scrollElementToCenter(searchInput);
        driverTools.clickWithJavascriptExecutor(searchInput);
        driverTools.clearWithJsExecutor(SEARCH_RULE_NAME_INPUT);
        driverTools.sendKeysWithJsExecutorForceInputValueAndTriggerEvents(SEARCH_RULE_NAME_INPUT, ruleName);
        WebElement searchButton = driverTools.waitForElementToAppear(SEARCH_BUTTON, 10).orElseThrow();
        driverTools.scrollElementToCenter(searchButton);
        driverTools.clickWithJavascriptExecutor(searchButton);
        waitForListLoaderDissapear();
    }

    public void deleteRuleName(String ruleName) {
        driverTools.click(getRuleSettingDropdownByRuleName(ruleName));
        driverTools.scrollElementToCenter(RULE_SETTING_SELECTIONS_CONTAINER);
        List<WebElement> selections = driverTools.waitForElementsToAppear(RULE_SETTING_SELECTIONS).orElseThrow();
        for (WebElement selection: selections) {
            if (driverTools.getText(selection).equals("Delete")) {
                driverTools.scrollElementToCenter(selection);
                driverTools.clickElementByCoordinateDWeb(selection);
                break;
            }
        }
        driverTools.waitForElementToAppear(RULE_DELETE_BUTTON, 15).orElseThrow();
        driverTools.clickWithJavascriptExecutor(RULE_DELETE_BUTTON);
        driverTools.waitForElementToDisappear(RULE_DELETE_BUTTON, 20);
        waitForListLoaderDissapear();
    }

    private boolean isAdjustmentRuleNamePresentInTable(String ruleName) {
        String target = ruleName.trim();
        for (WebElement nameEl : driver.findElements(ADJUSTMENT_RULE_TABLE_RULE_NAME_PRIMARY_TEXT.by())) {
            if (target.equals(driverTools.getText(nameEl).trim())) {
                return true;
            }
        }
        return false;
    }

    public boolean waitUntilAdjustmentRuleAbsentFromList(String ruleName, int timeoutSeconds) {
        waitForListLoaderDissapear();
        if (ruleName == null) {
            return true;
        }
        try {
            new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds)).until(d -> {
                waitForListLoaderDissapear();
                return !isAdjustmentRuleNamePresentInTable(ruleName);
            });
            return true;
        } catch (TimeoutException e) {
            logger.log("Timed out after " + timeoutSeconds + "s waiting for rule to leave list: " + ruleName);
            return false;
        }
    }

    public void insertRuleCondition(String condition, String operator, String value) {
        String cond = condition == null ? "" : condition.trim();
        String op = operator == null ? "" : operator.trim();
        String val = value == null ? "" : value.trim();
        driverTools.click(CONDITION_DROPDOWN);
        driverTools.waitForElementToAppear(ADJUSTMENT_RULE_OPEN_DROPDOWN_PANEL);
        driverTools.scrollElementToCenter(ADJUSTMENT_RULE_OPEN_DROPDOWN_PANEL);
        List<WebElement> selections = driverTools.waitForElementsToAppear(ADJUSTMENT_RULE_OPEN_DROPDOWN_ROWS).orElseThrow();
        for (WebElement selection : selections) {
            if (cond.equals(driverTools.getText(selection).trim())) {
                driverTools.scrollElementToCenter(selection);
                driverTools.click(selection);
                break;
            }
        }
        driverTools.dummyWait(5);
        driverTools.click(OPERATOR_DROPDOWN);
        driverTools.waitForElementToAppear(ADJUSTMENT_RULE_OPEN_DROPDOWN_PANEL);
        driverTools.scrollElementToCenter(ADJUSTMENT_RULE_OPEN_DROPDOWN_PANEL);
        selections = driverTools.waitForElementsToAppear(ADJUSTMENT_RULE_OPEN_DROPDOWN_ROWS).orElseThrow();
        for (WebElement selection : selections) {
            if (op.equals(driverTools.getText(selection).trim())) {
                driverTools.scrollElementToCenter(selection);
                driverTools.click(selection);
                break;
            }
        }
        driverTools.click(CONDITION_VALUE_DROPDOWN);
        driverTools.waitForElementToAppear(ADJUSTMENT_RULE_OPEN_DROPDOWN_PANEL);
        driverTools.scrollElementToCenter(ADJUSTMENT_RULE_OPEN_DROPDOWN_PANEL);
        selections = driverTools.waitForElementsToAppear(ADJUSTMENT_RULE_OPEN_DROPDOWN_ROWS).orElseThrow();
        for (WebElement selection : selections) {
            if (val.equals(driverTools.getText(selection).trim())) {
                driverTools.scrollElementToCenter(selection);
                driverTools.click(selection);
                break;
            }
        }
    }

    public void insertRuleAdjustment(String variable, String value) {
        String var = variable == null ? "" : variable.trim();
        driverTools.click(ADJUSTMENT_VARIABLE_DROPDOWN);
        driverTools.waitForElementToAppear(ADJUSTMENT_RULE_OPEN_DROPDOWN_PANEL);
        driverTools.scrollElementToCenter(ADJUSTMENT_RULE_OPEN_DROPDOWN_PANEL);
        List<WebElement> selections = driverTools.waitForElementsToAppear(ADJUSTMENT_RULE_OPEN_DROPDOWN_ROWS).orElseThrow();
        for (WebElement selection : selections) {
            if (var.equals(driverTools.getText(selection).trim())) {
                driverTools.scrollElementToCenter(selection);
                driverTools.click(selection);
                break;
            }
        }
        driverTools.click(ADJUSTMENT_VALUE_INPUT);
        driverTools.clearInput(ADJUSTMENT_VALUE_INPUT);
        driverTools.sendKeys(ADJUSTMENT_VALUE_INPUT, value);
    }

    public HashMap<String, String> getDataFromCreateRuleForm() {
        HashMap<String, String> valueMap = new HashMap<>();
        valueMap.put("condition", driver.findElement(CONDITION_DROPDOWN.by()).getAttribute("value"));
        valueMap.put("operator", driver.findElement(OPERATOR_DROPDOWN.by()).getAttribute("value"));
        valueMap.put("conditionValue", driver.findElement(CONDITION_VALUE_DROPDOWN.by()).getAttribute("value"));
        valueMap.put("adjustmentVariable", toCamelCaseVariable(driver.findElement(ADJUSTMENT_VARIABLE_DROPDOWN.by()).getAttribute("value")));
        valueMap.put("adjustmentValue", driver.findElement(ADJUSTMENT_VALUE_INPUT.by()).getAttribute("value"));
        return valueMap;
    }

    public void openRuleNameDetailModal(String ruleName) {
        searchRuleName(ruleName);
        driverTools.clickElementByCoordinateDWeb(getRuleSettingDropdownByRuleName(ruleName));
        driverTools.scrollElementToCenter(RULE_SETTING_SELECTIONS_CONTAINER);
        List<WebElement> selections = driverTools.waitForElementsToAppear(RULE_SETTING_SELECTIONS).orElseThrow();
        for (WebElement selection: selections) {
            if (driverTools.getText(selection).equals("Detail")) {
                driverTools.clickElementByCoordinateDWeb(selection);
                break;
            }
        }
        driverTools.dummyWait(3);
    }

    public void verifyRuleNameValue(String ruleName, HashMap<String, String> valueMap) {
        Assertion.assertThat("Verify Rule Name is correct", getRuleDetailPageAttributeValue("Rule Name"), equalTo(ruleName));
        Assertion.assertThat("Verify Condition is correct", getRuleDetailPageAttributeValue("Condition"), equalTo(valueMap.get("condition")));
        Assertion.assertThat("Verify Condition Operator is correct", getRuleDetailPageAttributeValue("Operator"), equalTo(valueMap.get("operator")));
        Assertion.assertThat("Verify Adjustment Variable is correct", getRuleDetailPageAttributeValue("Variable"), equalTo(valueMap.get("adjustmentVariable")));
        Assertion.assertThat("Verify Adjustment Value is correct", getRuleDetailPageAttributeValue("Adjustment"), equalTo(valueMap.get("adjustmentValue")));
    }

    public void closeRuleNameDetailModal() {
        driverTools.click(RULE_DETAIL_MODAL_CLOSE_BUTTON);
        driverTools.dummyWait(3);
    }

    public void editRuleNameDetailModal() {
        driverTools.click(RULE_DETAIL_MODAL_EDIT_BUTTON);
        driverTools.dummyWait(3);
    }

    public void setDefaultToPromoRule(String ruleName) {
        searchRuleName(ruleName);
        driverTools.clickElementByCoordinateDWeb(getRuleSettingDropdownByRuleName(ruleName));
        driverTools.scrollElementToCenter(RULE_SETTING_SELECTIONS_CONTAINER);
        List<WebElement> selections = driverTools.waitForElementsToAppear(RULE_SETTING_SELECTIONS).orElseThrow();
        for (WebElement selection: selections) {
            if (driverTools.getText(selection).equals("Set as default")) {
                driverTools.scrollElementToCenter(selection);
                driverTools.click(selection);
            }
        }
        driverTools.waitForElementToAppear(PROMO_ADJUSTMENT_RULES_SET_DEFAULT_BUTTON);
        driverTools.clickWithJavascriptExecutor(PROMO_ADJUSTMENT_RULES_SET_DEFAULT_BUTTON);
        driverTools.dummyWait(3);
    }

    public void verifyAdjustmentRuleIsDefault(String ruleName) {
        searchRuleName(ruleName);
        Assertion.assertThat(String.format("Verify that %s is the default rule", ruleName), isDefaultNudgeOfAdjustmentRuleExist(ruleName), equalTo(true));
    }

    public void removeDefaultToPromoRule(String ruleName) {
        searchRuleName(ruleName);
        driverTools.clickElementByCoordinateDWeb(getRuleSettingDropdownByRuleName(ruleName));
        driverTools.scrollElementToCenter(RULE_SETTING_SELECTIONS_CONTAINER);
        List<WebElement> selections = driverTools.waitForElementsToAppear(RULE_SETTING_SELECTIONS).orElseThrow();
        for (WebElement selection: selections) {
            if (driverTools.getText(selection).equals("Remove as default")) {
                driverTools.scrollElementToCenter(selection);
                driverTools.click(selection);
            }
        }
        driverTools.waitForElementToAppear(PROMO_ADJUSTMENT_RULES_REMOVE_DEFAULT_BUTTON);
        driverTools.clickWithJavascriptExecutor(PROMO_ADJUSTMENT_RULES_REMOVE_DEFAULT_BUTTON);
    }

    private static String toCamelCaseVariable(String input) {
        if (input == null || input.isEmpty()) return "";
        String[] words = input.split("[\\s_-]+");
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            String word = words[i].toLowerCase();
            if (i == 0) {
                result.append(word);
            } else {
                result.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1));
            }
        }
        return result.toString();
    }

    public String getBudgetNameValue() {
        WebElement budgetNameField = driverTools.waitForElementToAppear(BUDGET_NAME_VALUE)
                .orElseThrow();
        String budgetNameValue = driverTools.getText(budgetNameField).strip();
        logger.log("Budget Name value: " + budgetNameValue);
        return budgetNameValue;
    }

    public String getQuotaTypeValue() {
        WebElement quotaTypeField = driverTools.waitForElementToAppear(
                        getFieldsPromoDetails("Quota Type"))
                .orElseThrow();
        String quotaTypeValue = driverTools.getText(quotaTypeField).strip();
        logger.log("Quota Type value: " + quotaTypeValue);
        return quotaTypeValue;
    }

    public boolean verifyBudgetSectionValues(String expectedBudgetName, String expectedQuotaType) {
        Map<String, String> actualValues = new HashMap<>();
        String actualBudgetName = getBudgetNameValue();
        String actualQuotaType = getQuotaTypeValue();
        actualValues.put("Budget Name", actualBudgetName);
        actualValues.put("Quota Type", actualQuotaType);
        logger.log("Verifying Budget section values:");
        logger.log("Expected Budget Name: " + expectedBudgetName + ", Actual: " + actualBudgetName);
        logger.log("Expected Quota Type: " + expectedQuotaType + ", Actual: " + actualQuotaType);
        Assertion.assertThat("Verify Budget Name value matches", actualBudgetName, is(expectedBudgetName));
        Assertion.assertThat("Verify Quota Type value matches", actualQuotaType, is(expectedQuotaType));
        return actualValues.containsValue(expectedBudgetName) && actualValues.containsValue(expectedQuotaType);
    }

    public static final String DEFAULT_PROMO_CODE_MONGO_DB = "promoCode";
    public static final String DEFAULT_PROMO_CODE_MONGO_COLLECTION = "promoCodeData";

    public void loadPromoDataByPromoCodeWithParamDefaults(String mongoConfig, String dbName, String collectionName,
            String promoCode) {
        String db = (dbName == null || dbName.isBlank()) ? DEFAULT_PROMO_CODE_MONGO_DB : dbName;
        String coll = (collectionName == null || collectionName.isBlank()) ? DEFAULT_PROMO_CODE_MONGO_COLLECTION
                : collectionName;
        String escaped = escapeMongoJsonString(promoCode);
        String query = "{\"code\": \"" + escaped + "\", \"promoCodeStatus\": \"ACTIVE\"}";
        setPromoDocumentFromDB(mongoConfig, db, coll, query, "{\"updatedDate\": -1}");
    }

    private static String escapeMongoJsonString(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    public void loadPromoDataByPromoCodeWithStatus(String mongoConfig, String dbName, String collectionName,
            String promoCode, String promoCodeStatus) {
        String db = (dbName == null || dbName.isBlank()) ? DEFAULT_PROMO_CODE_MONGO_DB : dbName;
        String coll = (collectionName == null || collectionName.isBlank()) ? DEFAULT_PROMO_CODE_MONGO_COLLECTION
                : collectionName;
        String escapedCode = escapeMongoJsonString(promoCode);
        String query;
        if (promoCodeStatus == null || promoCodeStatus.isBlank()) {
            query = "{\"code\": \"" + escapedCode + "\"}";
        } else {
            query = "{\"code\": \"" + escapedCode + "\", \"promoCodeStatus\": \""
                    + escapeMongoJsonString(promoCodeStatus) + "\"}";
        }
        setPromoDocumentFromDB(mongoConfig, db, coll, query, "{\"updatedDate\": -1}");
    }

    public int getLoadedPromoRuleSetCount() {
        List<?> rs = bsonDocumentList(promoData, "ruleSets");
        return rs == null ? 0 : rs.size();
    }

    public void selectPromoDetailRuleSetTabByZeroBasedIndex(int zeroBasedIndex) {
        if (zeroBasedIndex <= 0) {
            return;
        }
        int tabNumber = zeroBasedIndex + 1;
        driverTools.waitForElementToAppear(PROMO_RULES_CARD_FOR_TABS, 15).ifPresent(driverTools::scrollIntoWebView);
        Locator tab = new Locator("RULE_SET_TAB_" + tabNumber,
                By.xpath(PROMO_DETAIL_ROOT_XPATH
                        + "//span[contains(.,'Promo Rules')]/ancestor::div[contains(@class,'Card_card_container')][1]"
                        + "//button[.//span[normalize-space()='Rule Set " + tabNumber + "']]"));
        driverTools.waitForElementToAppear(tab, 15).ifPresent(driverTools::click);
        driverTools.waitForElementToAppear(PROMO_DETAIL_RULE_SET_ACTIVE_PANEL, 15);
    }

    public boolean isPromoDataLoaded() {
        return promoData != null && !promoData.isEmpty();
    }

    public Map<String, String> readPromoDetailPromoAffiliateSectionFromUi() {
        Map<String, String> dataMap = new HashMap<>();
        if (!driverTools.waitForElementToAppear(PROMO_AFFILIATE_SECTION_HEADING, 15).isPresent()) {
            logger.log("Promo Affiliate section heading not found");
            return dataMap;
        }
        driverTools.scrollIntoWebView(PROMO_AFFILIATE_SECTION_HEADING);
        List<String> fields = driverTools.getTexts(PROMO_AFFILIATE_FIELD_LABELS);
        for (String field : fields) {
            dataMap.put(field, driverTools.getText(getFieldsPromoDetails(field)).orElse(""));
        }
        return dataMap;
    }

    public Map<String, String> readPromoDetailActiveRuleSetTableRowsFromUi() {
        Map<String, String> out = new LinkedHashMap<>();
        driverTools.waitForElementToAppear(PROMO_DETAIL_ID, 15);
        driverTools.waitForElementToAppear(PROMO_RULES_SECTION_HEADING, 15).ifPresent(driverTools::scrollIntoWebView);
        List<WebElement> rows = driverTools.waitForElementsToAppear(PROMO_DETAIL_RULE_SET_TABLE_BODY_ROWS, 20)
                .orElse(Collections.emptyList());
        for (WebElement tr : rows) {
            List<WebElement> cells = tr.findElements(By.tagName("td"));
            if (cells.size() < 2) {
                continue;
            }
            String label = driverTools.getText(cells.get(0)).trim();
            if (label.isEmpty() || "-".equals(label)) {
                continue;
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < cells.size(); i++) {
                if (i > 1) {
                    sb.append(" | ");
                }
                sb.append(driverTools.getText(cells.get(i)).trim().replaceAll("\\s+", " "));
            }
            out.put(label, sb.toString());
        }
        return out;
    }

    public boolean verifyPromoDetailRuleSetParametersAndTransactionUi(int ruleSetIndex) {
        selectPromoDetailRuleSetTabByZeroBasedIndex(ruleSetIndex);
        Map<String, String> expected = buildExpectedPromoDetailRuleSetRowsFromDb(ruleSetIndex);
        Assertion.assertThat("Mongo promoData should contain ruleSets[" + ruleSetIndex + "] for rule UI check",
                expected.isEmpty(), is(false));
        Map<String, String> actual = readPromoDetailActiveRuleSetTableRowsFromUi();
        for (Map.Entry<String, String> entry : expected.entrySet()) {
            String key = entry.getKey();
            String exp = entry.getValue();
            String act = actual.get(key);
            logger.log("Rule set " + (ruleSetIndex + 1) + " row [" + key + "] expected=" + exp + " actual=" + act);
            Assertion.assertThat("Rule set " + (ruleSetIndex + 1) + " row: " + key,
                    normalizeRuleSetRowCompareText(act),
                    is(normalizeRuleSetRowCompareText(exp)));
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    private Map<String, String> buildExpectedPromoDetailRuleSetRowsFromDb(int ruleSetIndex) {
        Map<String, String> map = new LinkedHashMap<>();
        if (promoData == null) {
            return map;
        }
        List<Document> ruleSets = (List<Document>) promoData.get("ruleSets");
        if (ruleSets == null || ruleSetIndex < 0 || ruleSetIndex >= ruleSets.size()) {
            return map;
        }
        Document rs = ruleSets.get(ruleSetIndex);
        List<Document> transactionRules = (List<Document>) rs.get("transactionRules");
        List<Document> discountRules = (List<Document>) rs.get("discountRules");
        String identifier = Optional.ofNullable(rs.getString("identifier")).orElse("").trim();
        map.put("Rule Set Identifier", identifier.isEmpty() ? "-" : identifier);

        if (anyTransactionRuleUsesAccountIdParam(transactionRules)) {
            boolean dynamicUserType = anyTransactionRuleDynamicFlag(transactionRules);
            map.put("User Type", dynamicUserType ? "Dynamic" : "Static");
        }
        if (transactionRules != null && !transactionRules.isEmpty()) {
            int cols = transactionRules.size();
            String verticalJoined = verticalCellsExpectedFromPromo(cols);
            map.put("Vertical", verticalJoined);
            map.put("Parameter", joinTransactionRuleField(transactionRules,
                    tr -> Optional.ofNullable(tr.getString("param")).orElse("")));
            map.put("Operator", joinTransactionRuleField(transactionRules, PromoDashboard::operatorFromTransactionRule));
            map.put("Value", joinTransactionRuleField(transactionRules, this::expectedValueCellFromTransactionRule));
        }
        if (discountRules != null && !discountRules.isEmpty()) {
            Document dr = discountRules.get(0);
            Document price = dr.get("promoCodePriceRangeObject", Document.class);
            if (price != null) {
                map.put("Min. AOV", formatNumberForPromoDetailUi(toDouble(price.get("minPrice"))));
                map.put("Max. AOV", formatNumberForPromoDetailUi(toDouble(price.get("maxPrice"))));
                String discountType = discountTypeUiLabel();
                Document cashback = price.get("cashback", Document.class);
                if (cashback != null) {
                    double cap = toDouble(cashback.get("maxValue"));
                    map.put("Discount", discountType + " " + formatDiscountSuffixFromCashback(cashback));
                    map.put("Capped At", discountType + " " + formatNumberForPromoDetailUi(cap));
                }
                map.put("Payment Method", paymentMethodUiFromPriceRange(price));
                map.put("Bank BIN", bankBinUiFromPriceRange(price));
            }
        }
        return map;
    }

    private static String normalizeRuleSetRowCompareText(String raw) {
        if (raw == null) {
            return "";
        }
        return raw.replace('\u00a0', ' ').replaceAll("\\s+", " ").trim();
    }

    private static final ThreadLocal<DecimalFormat> PROMO_DETAIL_NUMBER_FORMAT = ThreadLocal.withInitial(() -> {
        DecimalFormatSymbols sym = DecimalFormatSymbols.getInstance(Locale.US);
        DecimalFormat df = new DecimalFormat("#,###", sym);
        df.setMaximumFractionDigits(0);
        df.setMinimumFractionDigits(0);
        return df;
    });

    private static String formatNumberForPromoDetailUi(double value) {
        return PROMO_DETAIL_NUMBER_FORMAT.get().format(Math.round(value));
    }

    private static String formatDiscountSuffixFromCashback(Document cashback) {
        double pct = toDouble(cashback.get("percent"));
        if (pct > 0) {
            return BigDecimal.valueOf(pct).stripTrailingZeros().toPlainString() + "%";
        }
        return formatNumberForPromoDetailUi(toDouble(cashback.get("value")));
    }

    private static double toDouble(Object o) {
        if (o == null) {
            return 0;
        }
        if (o instanceof Number) {
            return ((Number) o).doubleValue();
        }
        try {
            return Double.parseDouble(String.valueOf(o));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private String discountTypeUiLabel() {
        String raw = promoData.getString("promoCodeDiscountType");
        if (raw == null || raw.isBlank()) {
            return "Discount";
        }
        return switch (raw.toUpperCase(Locale.ROOT)) {
            case "CASHBACK" -> "Cashback";
            case "INSTANT" -> "Instant";
            case "DISCOUNT" -> "Discount";
            default -> raw.substring(0, 1).toUpperCase(Locale.ROOT) + raw.substring(1).toLowerCase(Locale.ROOT);
        };
    }

    private String verticalCellsExpectedFromPromo(int columnCount) {
        List<?> verticals = bsonDocumentList(promoData, "verticals");
        String cell;
        if (verticals == null || verticals.isEmpty()) {
            cell = "General";
        } else {
            cell = verticalCodeToUiLabel(String.valueOf(verticals.get(0)));
        }
        return String.join(" | ", Collections.nCopies(Math.max(columnCount, 1), cell));
    }

    private static List<?> bsonDocumentList(Document doc, String key) {
        if (doc == null) {
            return null;
        }
        Object o = doc.get(key);
        return o instanceof List ? (List<?>) o : null;
    }

    private static String verticalCodeToUiLabel(String code) {
        if (code == null || code.isBlank()) {
            return "General";
        }
        String lower = code.replace('_', ' ').toLowerCase(Locale.ROOT);
        return lower.substring(0, 1).toUpperCase(Locale.ROOT) + lower.substring(1);
    }

    private static boolean anyTransactionRuleDynamicFlag(List<Document> transactionRules) {
        if (transactionRules == null) {
            return false;
        }
        for (Document tr : transactionRules) {
            if (coerceMongoBoolean(tr.get("dynamicValue"))) {
                return true;
            }
        }
        return false;
    }

    private static boolean anyTransactionRuleUsesAccountIdParam(List<Document> transactionRules) {
        if (transactionRules == null) {
            return false;
        }
        for (Document tr : transactionRules) {
            String p = Optional.ofNullable(tr.getString("param")).orElse("").trim();
            if ("accountId".equalsIgnoreCase(p)) {
                return true;
            }
        }
        return false;
    }

    private static boolean coerceMongoBoolean(Object v) {
        if (v == null) {
            return false;
        }
        if (v instanceof Boolean) {
            return (Boolean) v;
        }
        if (v instanceof Number) {
            return ((Number) v).intValue() != 0;
        }
        if (v instanceof String) {
            String s = ((String) v).trim();
            return "true".equalsIgnoreCase(s) || "1".equals(s);
        }
        return false;
    }

    private static String operatorFromTransactionRule(Document tr) {
        Document prv = tr.get("promoCodeRuleValue", Document.class);
        if (prv == null) {
            return "";
        }
        return Optional.ofNullable(prv.getString("operator")).orElse("");
    }

    private String expectedValueCellFromTransactionRule(Document tr) {
        Document prv = tr.get("promoCodeRuleValue", Document.class);
        if (prv == null) {
            return "-";
        }
        String operator = Optional.ofNullable(prv.getString("operator")).orElse("").trim();
        if ("IN".equalsIgnoreCase(operator)) {
            return "See Details";
        }
        Object v = prv.get("value");
        String s = v == null ? "" : String.valueOf(v).trim();
        if (s.isEmpty()) {
            return "-";
        }
        if (shouldExpectSeeDetailsForRuleValue(s)) {
            return "See Details";
        }
        return s;
    }

    private static boolean shouldExpectSeeDetailsForRuleValue(String value) {
        if (value.length() > 80) {
            return true;
        }
        if (value.startsWith("{")) {
            return true;
        }
        if (value.contains("\"bucket\"")) {
            return true;
        }
        if (value.contains("/PROMOCODE/")) {
            return true;
        }
        if (value.contains("|")) {
            return true;
        }
        return value.matches("[a-fA-F0-9]{24}");
    }

    private String joinTransactionRuleField(List<Document> transactionRules, Function<Document, String> fn) {
        List<String> parts = new ArrayList<>();
        for (Document tr : transactionRules) {
            String p = fn.apply(tr);
            parts.add(p == null ? "" : p);
        }
        return String.join(" | ", parts);
    }

    @SuppressWarnings("unchecked")
    private String paymentMethodUiFromPriceRange(Document price) {
        List<Document> pml = (List<Document>) price.get("paymentMethodObjectList");
        if (pml == null || pml.isEmpty()) {
            return "All payment method";
        }
        String name = pml.get(0).getString("paymentMethodName");
        if (name == null || name.isBlank()) {
            return "All payment method";
        }
        String key = name.trim();
        if ("ALL_PAYMENT_METHODS".equalsIgnoreCase(key)) {
            return "All payment method";
        }
        String label = PAYMENT_METHOD_NAME_TO_PROMO_DETAIL_UI_LABEL.get(key);
        if (label != null) {
            return label;
        }
        label = PAYMENT_METHOD_NAME_TO_PROMO_DETAIL_UI_LABEL.get(key.toUpperCase(Locale.ROOT));
        if (label != null) {
            return label;
        }
        return key.replace('_', ' ');
    }

    @SuppressWarnings("unchecked")
    private static String bankBinUiFromPriceRange(Document price) {
        List<Document> pml = (List<Document>) price.get("paymentMethodObjectList");
        if (pml == null || pml.isEmpty()) {
            return "-";
        }
        Document pm = pml.get(0);
        List<?> bins = bsonDocumentList(pm, "binNumbers");
        if (bins != null && !bins.isEmpty()) {
            return bins.stream().map(String::valueOf).collect(Collectors.joining(", "));
        }
        List<?> raw = bsonDocumentList(pm, "binNumberListRaw");
        if (raw != null && !raw.isEmpty()) {
            return raw.stream().map(String::valueOf).collect(Collectors.joining(", "));
        }
        return "-";
    }

    public void setPromoDetailGeneralAndFraudFromDb(String mongoConfig) {
        setPromoDetailGeneralAndFraudFromDb(mongoConfig, false);
    }

    public void setPromoDetailGeneralAndFraudFromDb(String mongoConfig, boolean promoCheckerBudgetSurface) {
        generalInfoPromoDetail.clear();
        generalInfoPromoDetail.put("Campaign Type", getCampaignType(mongoConfig));
        generalInfoPromoDetail.put("Campaign Name", getFieldPromoDocument(promoData, "campaignName"));
        generalInfoPromoDetail.put("Unique Identifier",
                getFieldPromoDocument(promoData, "tags").replace("[", "").replace("]", "").replace("\"", "").trim());
        generalInfoPromoDetail.put("Description", getFieldPromoDocument(promoData, "description"));
        generalInfoPromoDetail.put("Division", getDivisionName(mongoConfig));
        generalInfoPromoDetail.put("Period and Time", formatCampaignPeriodLabelForPromoDetail());
        generalInfoPromoDetail.put("Quota Type", quotaLevelToPromoDetailUiLabel());
        String budget = budgetLabelForPromoDetail(promoCheckerBudgetSurface);
        if (budget != null) {
            generalInfoPromoDetail.put("Budget", budget);
        }
        generalInfoPromoDetail.put("Dynamic Promo", dynamicPromoLabelForPromoDetail());
        Document fraudPrevention = promoData.get("fraudPrevention", Document.class);
        generalInfoPromoDetail.put("Usage Count", getFieldPromoDocument(fraudPrevention, "usageCount"));
        generalInfoPromoDetail.put("Usage Period", getFieldPromoDocument(fraudPrevention, "usagePeriod"));
        generalInfoPromoDetail.put("Platform", formatPlatformsListForPromoDetail(fraudPrevention));
        generalInfoPromoDetail.put("Login State", getFieldPromoDocument(fraudPrevention, "userLogin"));
        generalInfoPromoDetail.put("Phone Number", getFieldPromoDocument(fraudPrevention, "phoneNumberVerification"));
        generalInfoPromoDetail.put("Validated by", getFieldPromoDocument(fraudPrevention, "validatedBy"));
    }

    public boolean verifyPromoDetailGeneralAndFraudUi() {
        for (Map.Entry<String, String> entry : generalInfoPromoDetail.entrySet()) {
            String key = entry.getKey();
            String expected = entry.getValue();
            logger.log("key/label: " + key + " / " + expected);
            String actual = driverTools.getText(getFieldsPromoDetails(key)).orElse("");
            if ("Validated by".equals(key)) {
                Assertion.assertThat(
                        "Validated by (token order insensitive)",
                        canonicalValidatedByForCompare(actual),
                        is(canonicalValidatedByForCompare(expected)));
                continue;
            }
            if ("Period and Time".equals(key)) {
                Assertion.assertThat(
                        "Period and Time",
                        normalizePromoDetailWhitespaceAndBullets(actual),
                        is(normalizePromoDetailWhitespaceAndBullets(expected)));
                continue;
            }
            if ("Budget".equals(key)) {
                Assertion.assertThat(
                        "Actual = Expected for Budget",
                        normalizeBudgetPromoDetailCompare(actual),
                        is(normalizeBudgetPromoDetailCompare(expected)));
                continue;
            }
            String normalizedActual = normalizeEmptyDisplayForCompare(actual);
            String normalizedExpected = normalizeEmptyDisplayForCompare(expected);
            Assertion.assertThat("Actual = Expected for " + key, normalizedActual, is(normalizedExpected));
        }
        verifyBudgetNameHrefContainsMongoBudgetIdIfApplicable();
        return true;
    }

    private void verifyBudgetNameHrefContainsMongoBudgetIdIfApplicable() {
        if (promoData == null || promoData.isEmpty()) {
            return;
        }
        String budgetId = promoData.getString("budgetId");
        if (budgetId == null) {
            return;
        }
        String id = budgetId.trim();
        if (id.isEmpty() || "null".equalsIgnoreCase(id)) {
            return;
        }
        if (driver.findElements(By.xpath("//span[text()='Budget Name']")).isEmpty()) {
            logger.log("Budget Name row not shown; skip budgetId href check");
            return;
        }
        Locator budgetLink = new Locator("BUDGET_NAME_DETAIL_LINK",
                By.xpath("//span[text()='Budget Name']/parent::div/following-sibling::div//a[@href]"));
        Optional<WebElement> link = driverTools.waitForElementToAppear(budgetLink, 10);
        Assertion.assertThat(
                "Budget Name row should contain a link when Mongo has budgetId and the row is visible",
                link.isPresent(),
                is(true));
        String href = link.get().getAttribute("href");
        Assertion.assertThat(
                "Budget Name link href should contain Mongo budgetId " + id,
                href != null && href.contains(id),
                is(true));
    }

    private static String normalizePromoDetailWhitespaceAndBullets(String raw) {
        if (raw == null) {
            return "";
        }
        return raw.replace('\u2022', ' ')
                .replace('•', ' ')
                .replaceAll("\\s+", " ")
                .trim();
    }

    private static String normalizeEmptyDisplayForCompare(String raw) {
        if (raw == null) {
            return "";
        }
        String t = raw.trim();
        if (t.isEmpty() || "-".equals(t) || "\u2013".equals(t) || "–".equals(t)) {
            return "";
        }
        return t;
    }

    private static String normalizeBudgetPromoDetailCompare(String raw) {
        if (raw == null) {
            return "";
        }
        String t = raw.replace('\u00a0', ' ')
                .replace(",", "")
                .replaceAll("\\s+", " ")
                .trim()
                .toLowerCase(Locale.ROOT);
        if (t.isEmpty() || "-".equals(t) || "\u2013".equals(t) || "–".equals(t)) {
            return "";
        }
        return t;
    }

    private static String canonicalValidatedByForCompare(String raw) {
        if (raw == null || "null".equalsIgnoreCase(raw)) {
            return "";
        }
        String compact = raw.replace(", ", ",").replace(" ", "");
        List<String> tokens = extractValidatedByTokens(compact);
        Collections.sort(tokens);
        return String.join("_", tokens);
    }

    private static List<String> extractValidatedByTokens(String s) {
        if (s == null || s.isEmpty()) {
            return Collections.emptyList();
        }
        final String[] ordered = {"PHONE_NUMBER", "DEVICE_ID", "USERNAME", "EMAIL", "PHONE"};
        List<String> out = new ArrayList<>();
        int i = 0;
        while (i < s.length()) {
            String matched = null;
            for (String tok : ordered) {
                if (s.regionMatches(true, i, tok, 0, tok.length())) {
                    if (matched == null || tok.length() > matched.length()) {
                        matched = tok;
                    }
                }
            }
            if (matched == null) {
                i++;
                continue;
            }
            out.add(matched.toUpperCase(Locale.ROOT));
            i += matched.length();
            if (i < s.length() && (s.charAt(i) == '_' || s.charAt(i) == ',')) {
                i++;
            }
        }
        return out;
    }

    @SuppressWarnings("unchecked")
    private String formatCampaignPeriodLabelForPromoDetail() {
        try {
            List<Document> periods = (List<Document>) promoData.get("campaignPeriods");
            if (periods == null || periods.isEmpty()) {
                return "-";
            }
            Document p = periods.get(0);
            String type = Optional.ofNullable(p.getString("campaignPeriodType")).orElse("");
            Date start = p.getDate("startDate");
            Date end = p.getDate("endDate");
            if (start == null || end == null) {
                return "-";
            }
            ZoneId utc = ZoneOffset.UTC;
            LocalDate startDay = start.toInstant().atZone(utc).toLocalDate();
            LocalDate endDay = end.toInstant().atZone(utc).toLocalDate();
            DateTimeFormatter dayFmt = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH);
            String startStr = startDay.format(dayFmt);
            String endStr = endDay.format(dayFmt);
            String timePart = "00:00:00 - 23:59:59";
            return type + " \u2022 " + startStr + " - " + endStr + " \u2022 " + timePart;
        } catch (Exception e) {
            logger.log("formatCampaignPeriodLabelForPromoDetail: " + e.getMessage());
            return "-";
        }
    }

    private String quotaLevelToPromoDetailUiLabel() {
        String raw = getFieldPromoDocument(promoData, "quotaLevel");
        if (raw == null || raw.isBlank() || "null".equalsIgnoreCase(raw)) {
            return "-";
        }
        switch (raw.toUpperCase(Locale.ROOT)) {
            case "PROMO_CODE":
                return "At promo code level";
            case "TRANSACTION_VALUE":
                return "At transaction value";
            default:
                return raw;
        }
    }

    private static final long BUDGET_QUOTA_SENTINEL_UNLIMITED_MIN = 999_000_000L;

    private String budgetLabelForPromoDetail(boolean promoCheckerBudgetSurface) {
        boolean quotaOnly = promoOrRuleSetHasQuotaOnlyBudgetingFlag();
        boolean hasBudgetId = hasOutboundBudgetIdOnPromo();
        if (promoCheckerBudgetSurface) {
            if (hasBudgetId && quotaOnly) {
                return "Limited";
            }
            if (hasBudgetId) {
                return null;
            }
            if (quotaOnly) {
                return "Limited";
            }
            String numericChecker = budgetNumericCapFromPromo();
            if (numericChecker != null) {
                return numericChecker;
            }
            return "-";
        }
        if (hasBudgetId) {
            return null;
        }
        if ("TRANSACTION_VALUE".equalsIgnoreCase(getFieldPromoDocument(promoData, "quotaLevel"))) {
            return "-";
        }
        String numericGeneric = budgetNumericCapFromPromo();
        if (numericGeneric != null) {
            return numericGeneric;
        }
        return "-";
    }

    private boolean promoOrRuleSetHasQuotaOnlyBudgetingFlag() {
        String root = Optional.ofNullable(promoData.getString("budgetingSystemFlag")).orElse("").trim();
        if ("QUOTA_ONLY".equals(root)) {
            return true;
        }
        List<?> ruleSets = bsonDocumentList(promoData, "ruleSets");
        if (ruleSets == null) {
            return false;
        }
        for (Object rsObj : ruleSets) {
            if (!(rsObj instanceof Document)) {
                continue;
            }
            List<?> discountRules = bsonDocumentList((Document) rsObj, "discountRules");
            if (discountRules == null) {
                continue;
            }
            for (Object drObj : discountRules) {
                if (!(drObj instanceof Document)) {
                    continue;
                }
                String f = Optional.ofNullable(((Document) drObj).getString("budgetingSystemFlag")).orElse("").trim();
                if ("QUOTA_ONLY".equals(f)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hasOutboundBudgetIdOnPromo() {
        String id = promoData.getString("budgetId");
        if (id == null) {
            return false;
        }
        String t = id.trim();
        return !t.isEmpty() && !"null".equalsIgnoreCase(t);
    }

    private static boolean isSentinelUnlimitedQuota(Object qty) {
        if (qty == null) {
            return false;
        }
        double d = toDouble(qty);
        if (d <= 0) {
            return false;
        }
        return d >= BUDGET_QUOTA_SENTINEL_UNLIMITED_MIN;
    }

    private String budgetNumericCapFromPromo() {
        for (String field : new String[] {"maxQty", "quotaPromoCodeLevel"}) {
            Object o = promoData.get(field);
            if (o == null || isSentinelUnlimitedQuota(o)) {
                continue;
            }
            String s = String.valueOf(o).trim();
            if (s.isEmpty() || "0".equals(s) || "null".equalsIgnoreCase(s)) {
                continue;
            }
            double d = toDouble(o);
            if (d == 0) {
                continue;
            }
            if (Math.abs(d - Math.rint(d)) < 1e-9) {
                return String.valueOf((long) Math.rint(d));
            }
            return s;
        }
        return null;
    }

    private String dynamicPromoLabelForPromoDetail() {
        String raw = getFieldPromoDocument(promoData, "dynamicPromoRuleName");
        if (raw == null || raw.isBlank() || "null".equalsIgnoreCase(raw)) {
            return "No adjustment";
        }
        return raw.trim();
    }

    private String formatPlatformsListForPromoDetail(Document fraudPrevention) {
        if (fraudPrevention == null) {
            return "";
        }
        Object pl = fraudPrevention.get("platforms");
        if (pl instanceof List) {
            return ((List<?>) pl).stream().map(String::valueOf).collect(Collectors.joining(", "));
        }
        return getFieldPromoDocument(fraudPrevention, "platforms").replace("[", "").replace("]", "");
    }

    public void goToDicountDashboard() {
        driverTools.waitForElementToAppear(DISCOUNT_DASHBOARD_LINK);
        driverTools.clickWithJavascriptExecutor(DISCOUNT_DASHBOARD_LINK);
    }

    public void openPromoBlacklistPage() {
        WebElement blacklistLink = driverTools.waitForElementToBeClickable(BLACKLIST_PAGE_LINK).orElseThrow();
        driverTools.clickWithJavascriptExecutor(blacklistLink);
        driverTools.waitForElementToAppear(PROMO_BLACKLIST_PAGE_TITLE, 15).orElseThrow();
        driverTools.dummyWait(1);
    }

    public void inputSearchPromoBlacklist(String promoCode) {
        WebElement searchInput = driverTools.waitForElementToBeClickable(SEARCH_PROMO_BLACKLIST_INPUT).orElseThrow();
        driverTools.click(searchInput);
        driverTools.clearInput(SEARCH_PROMO_BLACKLIST_INPUT);
        driverTools.sendKeys(SEARCH_PROMO_BLACKLIST_INPUT, promoCode);
    }

    public void clickSearchPromoBlacklist() {
        WebElement searchBtn = driverTools.waitForElementToBeClickable(SEARCH_PROMO_BLACKLIST_BUTTON).orElseThrow();
        driverTools.click(searchBtn);
        driverTools.dummyWait(2);
    }

    public String isPromoBlacklistSearchResultCorrect() {
        Locator firstRowPromoCode = new Locator("FIRST_ROW_PROMO_CODE", By.xpath("//td[@data-testid='row-cell-0-targetValue']/p"));
        WebElement displayedPromoCode = driverTools.waitForElementToAppear(firstRowPromoCode).orElseThrow();
        return driverTools.getText(displayedPromoCode);
    }

    public int getPromoBlacklistSearchResultCount() {
        return driverTools.elements(PROMO_BLACKLIST_TABLE_ROWS).map(List::size).orElse(0);
    }

    public boolean isPromoBlacklistPaginationDisplayed() {
        return driverTools.waitForElementToAppear(PROMO_BLACKLIST_PAGINATION).isPresent();
    }

    public boolean isPromoBlacklistNextButtonEnabled() {
        WebElement nextBtn = driverTools.waitForElementToAppear(PROMO_BLACKLIST_NEXT_BTN).orElseThrow();
        return driverTools.isElementEnabled(nextBtn);
    }

    public void clickPromoBlacklistNextPage() {
        WebElement nextBtn = driverTools.waitForElementToBeClickable(PROMO_BLACKLIST_NEXT_BTN).orElseThrow();
        driverTools.clickWithJavascriptExecutor(nextBtn);
        // Wait for list to update/load
        driverTools.dummyWait(2);
    }

    public void clickPromoBlacklistPrevPage() {
        WebElement prevBtn = driverTools.waitForElementToBeClickable(PROMO_BLACKLIST_PREV_BTN).orElseThrow();
        driverTools.clickWithJavascriptExecutor(prevBtn);
        // Wait for list to update/load
        driverTools.dummyWait(2);
    }

    public void clickAddBlacklistButton() {
        WebElement btn = driverTools.waitForElementToBeClickable(ADD_BLACKLIST_BUTTON).orElseThrow();
        driverTools.clickWithJavascriptExecutor(btn);
    }

    public void uploadSingleFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new RuntimeException("File not found at path: " + filePath);
        }
        driverTools.waitForElementToAppear(UPLOAD_FILE_INPUT, 2);
        driverTools.sendKeys(UPLOAD_FILE_INPUT, file.getAbsolutePath());
    }

    public void clickModalAddToBlacklistButton() {
        WebElement btn = driverTools.waitForElementToBeClickable(MODAL_ADD_TO_BLACKLIST_BUTTON).orElseThrow();
        driverTools.clickWithJavascriptExecutor(btn);
        driverTools.waitForElementToDisappear(MODAL_ADD_TO_BLACKLIST_BUTTON, 10);
        driverTools.dummyWait(2);
    }

    public void clickRemoveBlacklistRowButton() {
        WebElement removeBtn = driverTools.waitForElementToBeClickable(REMOVE_BLACKLIST_ROW_BUTTON).orElseThrow();
        driverTools.click(removeBtn);
        driverTools.dummyWait(1);
    }

    public void clickConfirmDeleteBlacklist() {
        WebElement deleteBtn = driverTools.waitForElementToBeClickable(CONFIRM_DELETE_BLACKLIST_BUTTON).orElseThrow();
        driverTools.click(deleteBtn);
        driverTools.waitForElementToDisappear(CONFIRM_DELETE_BLACKLIST_BUTTON, 10);
        driverTools.dummyWait(2);
    }

    public boolean isNoDataMatchMessageDisplayed() {
        return driverTools.waitForElementToAppear(NO_DATA_MATCH_MESSAGE, 5).isPresent();
    }

    public void openPromoGroupPage() {
        WebElement groupLink = driverTools.waitForElementToBeClickable(GROUP_PAGE_LINK).orElseThrow();
        driverTools.clickWithJavascriptExecutor(groupLink);
        driverTools.waitForElementToAppear(PROMO_GROUP_PAGE_TITLE, 15).orElseThrow();
        driverTools.dummyWait(1);
    }

    public void inputSearchPromoGroup(String promoGroup) {
        WebElement searchInput = driverTools.waitForElementToBeClickable(SEARCH_PROMO_GROUP_INPUT).orElseThrow();
        driverTools.click(searchInput);
        driverTools.clearInput(SEARCH_PROMO_GROUP_INPUT);
        driverTools.sendKeys(SEARCH_PROMO_GROUP_INPUT, promoGroup);
    }

    public String isPromoGroupSearchResultCorrect() {
        Locator firstRowPromoGroup = new Locator("FIRST_ROW_PROMO_GROUP", By.xpath("(//td[@data-testid='row-cell-0-groupName']/p)[1]"));
        WebElement displayedPromoGroup = driverTools.waitForElementToAppear(firstRowPromoGroup).orElseThrow();
        return driverTools.getText(displayedPromoGroup);
    }

}
