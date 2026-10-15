package com.tiket.web.dashboard.page.platform.discovery;

import com.tiket.core.CoreFunctions;
import com.tiket.logging.ILogger;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.member_dashboard.MemberLoginPage;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DashboardPages<T extends WebDriver> extends BasePage<T> {
    public DashboardPages(Session<T> session) {
        super(session);
    }

    ILogger logger = BaseTest.mainLogger.get();
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

    private static final String PAGES_ADMIN_URL = "/admin/home/v2/pages";
    private static final Locator EMAIL_LABEL = new Locator("EMAIL_LABEL", By.xpath("//span[@data-testid='user-info-name']"));
    private static final Locator SEARCH_BAR_ELEMENT = new Locator("SEARCH_BAR_ELEMENT", By.id("search-pages"));
    private static final Locator TEXT_PAGES = new Locator("TEXT_PAGES", By.xpath("//p[contains(text(),'Pages')]"));
    private static final Locator ADD_NEW_PAGE_BUTTON = new Locator("ADD_NEW_PAGE_BUTTON", By.xpath("//span[text()='Add a New Page']"));
    private static final Locator ALL_PAGES_TABLE = new Locator("ALL_PAGES_TABLE", By.xpath("//table[contains(@class, 'Table_table')]"));
    private static final Locator SEARCH_RESULT_ELEMENT = new Locator("SEARCH_RESULT_ELEMENT", By.xpath("//table[contains(@class,'Table')]//tbody/tr[count(td)>=4]"));
    private static final Locator TEMPLATE_NAME_SEARCH_INPUT = new Locator("TEMPLATE_NAME_SEARCH_INPUT", By.xpath("//input[@id='template-name']"));
    private static final Locator GET_IN_BUTTON = new Locator("GET_IN_BUTTON", By.xpath("//button[normalize-space(.)='GET IN']"));
    private static final Locator MOBILE_MODULES_MENU = new Locator("MOBILE_MODULES_MENU", By.xpath("//span[text()='Mobile Modules']"));
    private static final Locator MORE_MENU_POP_OVER = new Locator("MORE_MENU_POP_OVER", By.className("action-popover"));
    private static final Locator ARCHIVE_MODULE_MENU = new Locator("ARCHIVE_MODULE_MENU", By.xpath("//p[contains(@class,'Text_size_b3') and normalize-space()='Archive Module']"));
    private static final Locator ARCHIVE_CONFIRM_BUTTON = new Locator("ARCHIVE_CONFIRM_BUTTON", By.xpath("//button[normalize-space()='Archive']"));
    private static final Locator ARCHIVE_SUCCESS_SNACKBAR = new Locator("ARCHIVE_SUCCESS_SNACKBAR", By.xpath("//div[normalize-space()='Module archived successfully']"));
    private static final Locator SEARCH_MODULES_BUTTON = new Locator("SEARCH_MODULES_BUTTON", By.xpath("//button[normalize-space()='Search']"));
    private static final Locator ADD_CUSTOM_MODULE_BUTTON = new Locator("ADD_CUSTOM_MODULE_BUTTON", By.xpath("//a[normalize-space()='Add Custom Module']"));
    private static final Locator INPUT_MODULE_NAME = new Locator("INPUT_MODULE_NAME", By.id("module-name"));
    private static final Locator SELECT_TEMPLATE_CODE = new Locator("SELECT_TEMPLATE_CODE", By.xpath("//input[@id='template-code']"));
    private static final Locator SELECT_SEGMENTATION = new Locator("SELECT_SEGMENTATION", By.xpath("//input[@id='segmentation']"));
    private static final Locator CONTAINER_OPTIONS_MODULE = new Locator("CONTAINER_OPTIONS_MODULE", By.className("container-options"));
    private static final Locator ANDROID_PLATFORM_CHECKBOX = new Locator("ANDROID_PLATFORM_CHECKBOX", By.xpath("//input[@value='ANDROID']"));
    private static final Locator IOS_PLATFORM_CHECKBOX = new Locator("IOS_PLATFORM_CHECKBOX", By.xpath("//input[@value='IOS']"));
    private static final Locator MWEB_PLATFORM_CHECKBOX = new Locator("MWEB_PLATFORM_CHECKBOX", By.xpath("//span[normalize-space()='Mobile Web']"));
    private static final Locator CHOOSE_CURRENCY = new Locator("CHOOSE_CURRENCY", By.xpath("//input[@id='currency']"));
    private static final Locator PERIOD_ALWAYS_ON = new Locator("ALWAYS_ON_PERIOD_ELEMENT", By.xpath("//span[normalize-space()='Always On']"));
    private static final Locator SAVE_AND_GO_TO_CONTENT = new Locator("SAVE_AND_GO_CONTENT", By.xpath("//button[normalize-space()='Save and go to content']"));
    private static final Locator SUBMIT_GENERAL = new Locator("SUBMIT_GENERAL", By.xpath("//button[contains(@class,'Button_button') and normalize-space()='Submit']"));
    private static final Locator EDIT_GENERAL_SUCCESS_SUBMIT = new Locator("EDIT_GENERAL_SUCCESS_SUBMIT", By.xpath("//div[(contains(@class,'Snackbar_text_content') or contains(@class,'snackbar-container'))" + " and (contains(normalize-space(.),'Successfully edited')" + " or contains(normalize-space(.),'Success'))]"));
    private static final Locator BREADCRUMB_BACK_TO_MOBILE_MODULE = new Locator("BREADCRUMB_BACK_TO_MOBILE_MODULE", By.xpath("//span[normalize-space()='Page For Automation Dashboard - Mobile Modules']"));
    private static final Locator BACK_TO_MOBILE_MODULE = new Locator("BACK_TO_MOBILE_MODULE", By.xpath("//span[contains(@class,'BreadcrumbItem_text') and contains(.,'Mobile Modules')]"));
    private static final Locator EDIT_GENERAL_BUTTON = new Locator("EDIT_GENERAL_BUTTON", By.xpath("//a[contains(@href,'/edit-general/')]"));
    private static final Locator ELLIPSIS_LOADING_ELEMENT = new Locator("ELLIPSIS_LOADING_ELEMENT", By.xpath("//div[@class='lds-ellipsis']"));
    private static final Locator SHOW_TO_USER_THAT = new Locator("SHOW_TO_USER_THAT", By.xpath("//p[normalize-space()='Show to users that (required)']"));
    private static final Locator DRP_START_DATE = new Locator("DRP_START_DATE", By.xpath("//span[contains(@class, 'date-picker-container')][1]"));
    private static final Locator DRP_END_DATE = new Locator("DRP_END_DATE", By.xpath("//span[contains(@class, 'date-picker-container')][2]"));
    private static final Locator DRP_START_DATE_INPUT = new Locator("DRP_START_DATE_INPUT", By.xpath("//span[contains(@class, 'date-picker-container')][1]//input"));
    private static final Locator DRP_END_DATE_INPUT = new Locator("DRP_END_DATE_INPUT", By.xpath("//span[contains(@class, 'date-picker-container')][2]//input"));
    private static final Locator DATE_PICKER_DONE = new Locator("DATE_PICKER_DONE", By.xpath("(//button[@data-testid='submit-date-time'])[3]"));
    private static final Locator START_DAILY_CALENDAR_ICON = new Locator("START_DAILY_CALENDAR_ICON", By.xpath("//input[@id='start-daily']/following-sibling::*[1]" + " | //input[@id='start-daily']/parent::*//*[contains(@class,'icon') or contains(@class,'calendar') or name()='svg'][last()]"));
    private static final Locator REPEAT_EVERY = new Locator("REPEAT_EVERY", By.xpath("//input[@name='repeatEvery']"));
    private static final Locator SELECT_REPETITION = new Locator("SELECT_REPETITION", By.xpath("//label[text()='Select Repetition']/preceding-sibling::input"));
    private static final Locator SELECT_START_DAILY = new Locator("SELECT_START_DAILY", By.xpath("//input[@id='start-daily']"));
    private static final Locator SELECT_TIME = new Locator("SELECT_TIME", By.xpath("//input[@id='select-start-time']"));
    private static final Locator SELECT_END_TIME = new Locator("SELECT_END_TIME", By.xpath("//input[@id='select-end-time']"));
    private static final Locator END_CONDITION_INPUT = new Locator("END_CONDITION_INPUT", By.xpath("//label[text()='End']/parent::div"));
    private static final Locator TIME_POPOVER = new Locator("TIME_POPOVER", By.xpath("//div[contains(@class,'TimeDropDown_select_collapse_card_body_')]"));
    private static final Locator CUSTOM_PERIOD_END_INPUT = new Locator("CUSTOM_PERIOD_END_INPUT", By.xpath("//input[@id='end']"));
    private static final Locator MINUTE_DOWN_ARROW = new Locator("MINUTE_DOWN_ARROW", By.xpath("//div[@data-testid='time-dropdown-popover']//div[2]//button[2]"));
    private static final Locator MINUTE_UP_ARROW = new Locator("MINUTE_UP_ARROW", By.xpath("//div[@data-testid='time-dropdown-popover']//div[2]//button[1]"));
    private static final Locator FORM_GENERAL_SETTINGS = new Locator("FORM_GENERAL_SETTINGS", By.className("form-edit-general-container"));
    private static final Locator FORM_CONTENT_SETTINGS = new Locator("FORM_CONTENT_SETTINGS", By.xpath("//div[@id='dynamicData-option-input-0contentType']//span[normalize-space()='Custom Data Source']"));
    private static final Locator IMAGE_UPLOAD_RADIO = new Locator("IMAGE_UPLOAD_RADIO", By.xpath("//input[@name='dynamicData-option-input-0contentType' and contains(@value,'IMAGE_UPLOAD')]"));
    private static final Locator CDS_RADIO = new Locator("CDS_RADIO", By.xpath("//input[@name='dynamicData-option-input-0contentType' and contains(@value,'CUSTOM_DATA_SOURCE')]"));
    private static final Locator NO_BACKGROUND_RADIO = new Locator("NO_BACKGROUND_RADIO", By.xpath("//input[@name='dynamicData-option-input-1' and contains(@value,'noBackground')]"));
    private static final Locator NO_BUTT0N_RADIO = new Locator("NO_BUTTON_RADIO", By.xpath("//input[@value='\"NO_BUTTON\"']"));
    private static final Locator CHOOSE_BANNER_SIZE = new Locator("CHOOSE_BANNER_SIZE", By.xpath("//input[@id='choose-banner-size']"));
    private static final Locator BANNER_SIZE_OPTION_BOX = new Locator("BANNER_SIZE_OPTION_BOX", By.xpath("//div[contains(@class, 'Card_card_container')]"));
    private static final Locator BANNER_1_1_REGULAR = new Locator("BANNER_1_1_REGULAR", By.xpath("//div[@data-testid='option-single-0']"));
    private static final Locator SLUG_ID = new Locator("SLUG_ID", By.xpath("(//input[@id='full-url--slug'])[1]"));
    private static final Locator SLUG_EN = new Locator("SLUG_EN", By.xpath("(//input[@id='full-url--slug'])[2]"));
    private static final Locator TAB_CONTENT_EN = new Locator("TAB_CONTENT_EN", By.xpath("//button[normalize-space()='Content EN']"));
    private static final Locator SUBMIT_CONTENT_MODULE = new Locator("SUBMIT_CONTENT_MODULE", By.xpath("//button[normalize-space()='Submit']"));
    private static final Locator CHECKLIST_USE_CHIPS = new Locator("CHECKLIST_USE_CHIPS", By.xpath("(//span[contains(@class,'Check_children')])[2]"));
    private static final Locator SNACKBAR_SUCCESS = new Locator("SNACKBAR_SUCCESS", By.xpath("//div[contains(normalize-space(),'Successfully')]"));
    private static final Locator SNACKBAR_SUCCESSFUL = new Locator("SNACKBAR_SUCCESSFUL", By.xpath("//div[contains(normalize-space(),'Success')]"));
    private Locator chipsDropdown(int index) {
        return new Locator("CHIPS_DROPDOWN_" + index,
                By.xpath(String.format("//div[contains(@class,'styles_module_content_chips')]/div[%d]/div[1]/div[1]//*[name()='svg']", index)));
    }
    private static final Locator VALUE_CHIPS = new Locator("VALUE_CHIPS", By.xpath("//input[contains(@class,'QuantityEditor_quantity_input')]"));

    private static final Locator BANNER_IMAGE_UPLOAD_INPUT = new Locator("BANNER_IMAGE_UPLOAD_INPUT", By.xpath("//div[contains(@class,'tab_panel_active')]//input[@type='file']"));

    private static final Locator DW_URL_INPUT = new Locator("DW_URL_INPUT", By.xpath("//input[@placeholder='Link']"));
    private static final Locator DW_WEBVIEW_SIZE_INPUT = new Locator("DW_WEBVIEW_SIZE_INPUT", By.xpath("//input[@placeholder='input height (px)']"));
    private static final Locator DW_BOTTOM_CTA_TEXT_ID = new Locator("DW_BOTTOM_CTA_TEXT_ID", By.xpath("//input[@placeholder='Bottom CTA Text (ID)']"));
    private static final Locator DW_BOTTOM_CTA_TEXT_EN = new Locator("DW_BOTTOM_CTA_TEXT_EN", By.xpath("//input[@placeholder='Bottom CTA Text (EN)']"));
    private static final Locator DW_BOTTOM_CTA_LINK_ID = new Locator("DW_BOTTOM_CTA_LINK_ID", By.xpath("//input[@placeholder='Bottom CTA Link (ID)']"));
    private static final Locator DW_BOTTOM_CTA_LINK_EN = new Locator("DW_BOTTOM_CTA_LINK_EN", By.xpath("//input[@placeholder='Bottom CTA Link (EN)']"));

    private Locator seeAllButtonOption(String option) {
        return new Locator(String.format("SEE_ALL_%S", option.replace(" ", "_").replace("&", "AND").toUpperCase()),
                By.xpath(String.format("//span[normalize-space()='%s']", option)));
    }

    private Locator dataSourceColumn(int index) {
        return new Locator("DATA_SOURCE_COLUMN_" + index,
                By.xpath(String.format("(//input[@id='data-source'])[%d]", index)));
    }

    private Locator tabNameInput(String lang, int index) {
        return new Locator("TAB_NAME_" + lang.toUpperCase() + "_" + index,
                By.xpath(String.format("(//div[@aria-hidden='false']//div[contains(@class,'styles_%s_tab')]//div[contains(@class,'styles_input_inner_wrapper')]//input)[%d]", lang.toLowerCase(), index)));
    }

    private int getTotalChips() {
        String value = Optional.ofNullable(driverTools.getValue(VALUE_CHIPS)).orElse("1").trim();
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 1;
        }
    }

    public boolean isSuccessSnackbarAppear(){
        return driverTools.waitForElementToAppear(SNACKBAR_SUCCESSFUL).isPresent();
    }

    private Locator selectEndConditions(String option) {
        return new Locator(String.format("CONDITION_%S", option.toUpperCase()),
                By.xpath(String.format("//p[normalize-space()='%s']", option)));
    }

    private Locator timeOption(String time) {
        String uiTime = time.replace("-", ":");
        return new Locator("TIME_OPTION_" + time.replace("-", "_"),
                By.xpath(String.format("//div[contains(@class,'TimeDropDown_select_collapse_card_body_')]" +
                                "//div[contains(@class,'TimeDropDown_item_') and normalize-space(.)='%s']", uiTime)));
    }

    private Locator selectStartTime(String time) {
        return new Locator(String.format("SELECT_START_TIME_%S", time), By.xpath(String.format("//div[contains(text(),'Start Time')]/following::div[text()='%s']", time.replace("-", ":"))));
    }

    private Locator selectEndTime(String time) {
        return new Locator(String.format("SELECT_END_TIME_%S", time), By.xpath(String.format("//div[contains(text(), 'End Time')]/following::div[text()='%s']", time.replace("-", ":"))));
    }

    private Locator selectRepetition(String option) {
        return new Locator(String.format("SELECT_%S_LOCATOR", option.toUpperCase()),
                By.xpath(String.format("//p[normalize-space()='%s']", option)));
    }

    private Locator selectDayInOpenDatePicker(String date) {
        LocalDate parsed = LocalDate.parse(date, DateTimeFormatter.ofPattern("EEE MMM dd yyyy", Locale.ENGLISH));
        int day = parsed.getDayOfMonth();
        return new Locator(String.format("SELECT_DATE_%s", date.replace(" ", "_")),
                By.xpath(String.format("//div[contains(@class,'DatePickerDesktop')]"
                        + "//button[contains(@class,'Day_day') and not(@disabled)]//span[normalize-space()='%d']", day)));
    }

    private void clickDatePickerDone() {
        WebElement doneButton = driverTools.waitForElementToBeClickable(DATE_PICKER_DONE, 10)
                .orElseThrow(() -> new IllegalStateException("Done button not found in date picker"));
        driverTools.scrollElementToCenter(doneButton);
        try {
            driverTools.clickWithActions(doneButton);
        } catch (Exception e) {
            doneButton.click();
        }
    }

    private void waitForDatePickerOpen() {
        driverTools.waitForElementToAppear(DATE_PICKER_DONE, 10);
    }

    private void waitForDatePickerToClose() {
        driverTools.waitForElementToDisappear(DATE_PICKER_DONE, 5);
    }

    private void clickDateInPicker(Locator dayLocator) {
        WebElement dayElement = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(d -> driver.findElements(dayLocator.by()).stream()
                        .filter(WebElement::isDisplayed)
                        .findFirst()
                        .orElse(null));
        if (dayElement == null) {
            throw new IllegalStateException("Date not visible in picker: " + dayLocator);
        }
        WebElement clickTarget = dayElement;
        try {
            clickTarget = dayElement.findElement(By.xpath("./ancestor::button[contains(@class,'Day_day')][1]"));
        } catch (org.openqa.selenium.NoSuchElementException ignored) {
            logger.log("Day button ancestor not found, clicking day span directly");
        }
        driverTools.scrollElementToCenter(clickTarget);
        driverTools.clickWithJavascriptExecutor(clickTarget);
    }

    private void confirmDatePickerDone() {
        clickDatePickerDone();
        waitForDatePickerToClose();
    }

    private void waitForStartDailyValue() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(d -> !Optional.ofNullable(driverTools.getValue(SELECT_START_DAILY)).orElse("").trim().isBlank());
    }

    private void selectStartDailyWithDone() {
        driverTools.scrollIntoWebView(choosePeriod("CUSTOM"));
        scrollFormFieldIntoView(SELECT_START_DAILY);
        waitProcess(Duration.ofMillis(300));
        WebElement calendarIcon = driverTools.waitForElementToPresence(START_DAILY_CALENDAR_ICON, 10).orElseThrow();
        driverTools.scrollElementToCenter(calendarIcon);
        try {
            driverTools.clickWithActions(calendarIcon);
        } catch (Exception e) {
            logger.log("Actions click on Start Daily calendar failed, using JS: " + e.getMessage());
            driverTools.clickWithJavascriptExecutor(calendarIcon);
        }
        waitForDatePickerOpen();
        confirmDatePickerDone();
        try {
            waitForStartDailyValue();
        } catch (TimeoutException e) {
            logger.log("Start Daily value not set after Done, retrying Done click");
            confirmDatePickerDone();
            waitForStartDailyValue();
        }

        String actualStartDaily = Optional.ofNullable(driverTools.getValue(SELECT_START_DAILY)).orElse("").trim();
        sheetData.put("expectedStartDaily", actualStartDaily);
        logger.log("START DAILY after Done: " + actualStartDaily);
    }

    private Locator choosePeriod(String period) {
        return switch (period) {
            case "RANGE" ->
                    new Locator("RANGE_PERIOD_ELEMENT", By.xpath("//span[normalize-space()='Date Range']"));
            case "CUSTOM" ->
                    new Locator("CUSTOM_PERIOD_ELEMENT", By.xpath("//span[normalize-space()='Custom Period']"));
            default ->
                    new Locator("ALWAYS_ON_PERIOD_ELEMENT", By.xpath("//span[normalize-space()='Always On']"));
        };
    }

    private Locator periodRadio(String periodLabel) {
        return new Locator(String.format("PERIOD_RADIO_%S", periodLabel.replace(" ", "_").toUpperCase()),
                By.xpath(String.format("//span[normalize-space()='%s']/preceding::input[@type='radio'][1]", periodLabel)));
    }

    private Locator chooseShownTo(String to) {
        return new Locator(String.format("SHOW_TO_%S", to.toUpperCase()), By.xpath(String.format("//span[normalize-space()='%s']", to)));
    }

    private Locator shownToCheckbox(String option) {
        return new Locator(String.format("SHOW_TO_%S_CHECKBOX", option.toUpperCase()),
                By.xpath(String.format("//span[normalize-space()='%s']/preceding::input[@type='checkbox'][1]", option)));
    }

    private Locator getLocatorTemplateCode(String widgetName) {
        return new Locator(String.format("LOCATOR_%S", widgetName),
                By.xpath(String.format("//p[contains(@class,'Text_size_b2') and normalize-space()='%s']", widgetName)));
    }

    private Locator selectOptionTemplateCode(String templateName) {
        return new Locator(String.format("TEMPLATE_LOCATOR_%S", templateName),
                By.xpath(String.format("//div[contains(@data-testid,'option-single')][.//p[normalize-space()='%s']]", templateName)));
    }

    private Locator selectOptionSegmentation(String segmentation) {
        return new Locator(String.format("SEGMENTATION_LOCATOR_%S", segmentation),
                By.xpath(String.format("//div[contains(@class,'SelectItem_select_item')]//span[normalize-space()='%s']", segmentation)));
    }

    private Locator selectCurrencyOptions(String currency) {
        return new Locator(String.format("CURRENCY_%S", currency.toUpperCase()),
                By.xpath(String.format("//div[contains(@data-testid,'option-single') and .//p[normalize-space()='%s']]", currency)));
    }

    public void clickAddCustomModule() {
        clickAddCustomModuleButton();
        waitProcess(Duration.ofSeconds(5));
    }

    public void goToHomePageModule(String username, String password) {
        driverTools.maximizeWindow();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        String currentUrl = driverTools.getCurrentURL();
        if (!currentUrl.contains(PAGES_ADMIN_URL)) {
            memberLoginPage.adminLoginViaCookie(username, password);
            memberLoginPage.goToPage(PAGES_ADMIN_URL);
        }
    }

    public void clickGetInButton() {
        Optional<WebElement> getIn = driverTools.waitForElementToBeClickable(GET_IN_BUTTON, 5);
        if (getIn.isEmpty()) {
            logger.log("GET IN button not present; continuing. Current URL: " + driverTools.getCurrentURL());
            return;
        }
        try {
            driverTools.click(GET_IN_BUTTON);
        } catch (Exception e) {
            driverTools.clickWithJavascriptExecutor(GET_IN_BUTTON);
        }
        driverTools.waitForElementToDisappear(GET_IN_BUTTON, 10);
    }

    private boolean waitForAdminDashboardReady() {
        boolean pagesLoaded = driverTools.waitForElementToAppear(TEXT_PAGES, 5).isPresent();
        boolean searchBarLoaded = driverTools.waitForElementToAppear(SEARCH_BAR_ELEMENT, 2).isPresent();
        boolean pagesTableLoaded = driverTools.waitForElementToAppear(ALL_PAGES_TABLE, 2).isPresent();
        boolean addPageButtonLoaded = driverTools.waitForElementToAppear(ADD_NEW_PAGE_BUTTON, 2).isPresent();

        logger.log("TEXT_PAGES loaded: " + pagesLoaded);
        logger.log("SEARCH_BAR_ELEMENT loaded: " + searchBarLoaded);
        logger.log("ALL_PAGES_TABLE loaded: " + pagesTableLoaded);
        logger.log("ADD_NEW_PAGE_BUTTON loaded: " + addPageButtonLoaded);

        return pagesLoaded && searchBarLoaded && pagesTableLoaded && addPageButtonLoaded;
    }

    public boolean isPageModuleDashboardLoaded(String email) {
        return waitForAdminDashboardReady();
    }

    public void goToMobileModules(String pageName) {
        searchPage(pageName);
        driverTools.waitForElementToAppear(MOBILE_MODULES_MENU);
        driverTools.click(MOBILE_MODULES_MENU);
        handleNewWindow();
        waitProcess(Duration.ofSeconds(3));
        driver.navigate().refresh();
    }

    public void inputGeneralSettingsModule() {
        final String templateCode = getValueOrDefault(sheetData.get("templateCode"), "BANNER_CAROUSEL_V2");
        final String platforms = getValueOrDefault(sheetData.get("platforms"), "ANDROID IOS");
        final String currency = getValueOrDefault(sheetData.get("currency"), "IDR");
        final String shownTo = getValueOrDefault(sheetData.get("shownTo"), "LOGIN");
        final String segmentation = getValueOrDefault(sheetData.get("segmentation"), "All");
        final String period = getValueOrDefault(sheetData.get("period"), "Always On");
        String templateName = String.format(getValueOrDefault(sheetData.get("widgetName"), "AUTOMATION_") + "%S_%S", templateCode, CoreFunctions.generateRandomString(6));
        sheetData.put("widgetName", templateName);

        driverTools.waitForElementToAppear(FORM_GENERAL_SETTINGS, 3);

        driverTools.sendKeys(INPUT_MODULE_NAME, templateName);
        inputTemplateCode(templateCode);
        inputCheckBoxPlatform(platforms);
        inputSegmentation(segmentation);
        inputCurrency(currency);
        inputShownTo(shownTo);
        inputPeriod(period);

        driverTools.clickWithJavascriptExecutor(SAVE_AND_GO_TO_CONTENT);
        waitProcess(Duration.ofSeconds(3));
    }

    public void inputContentModule(String templateCode) {
        handleNewWindow();
        switch (templateCode) {
            case "BANNER_CAROUSEL_V2":
                inputMandatoryBannerCarouselV2();
                driverTools.clickWithJavascriptExecutor(BACK_TO_MOBILE_MODULE);
                break;
            case "DYNAMIC_WIDGET":
                sheetData.put("expectedTemplateCode", templateCode);
                inputDynamicWidgetContent();
                driverTools.clickWithJavascriptExecutor(BACK_TO_MOBILE_MODULE);
                break;
            case "BANNER_GRID":
                break;
            case "BANNER_LOYALTY":
                break;
            default:
                Assertion.skip(String.format("Sorry, module %S is currently not supported. " + "\nPlease add the steps in Method: 'inputContentModule'", templateCode));
        }
        waitProcess(Duration.ofSeconds(3));
    }

    public void inputMandatoryBannerCarouselV2CustomDataSource(){
        driverTools.waitForElementToAppear(FORM_CONTENT_SETTINGS, 2);
        driverTools.click(FORM_CONTENT_SETTINGS);
        driverTools.click(CHOOSE_BANNER_SIZE);
        driverTools.waitForElementToAppear(BANNER_1_1_REGULAR);
        driverTools.clickWithJavascriptExecutor(BANNER_1_1_REGULAR);
        driverTools.clickWithJavascriptExecutor(CDS_RADIO);
        driverTools.clickWithJavascriptExecutor(NO_BACKGROUND_RADIO);
        driverTools.clickWithJavascriptExecutor(CHECKLIST_USE_CHIPS);
        driverTools.clickWithJavascriptExecutor(NO_BUTT0N_RADIO);
    }

    public void inputChipsBannerCarouselV2(String data_source){
        String tabNameId = CoreFunctions.generateRandomString(6);
        String tabNameEn = CoreFunctions.generateRandomString(6);

        int totalChips = getTotalChips();
        for (int i = 1; i <= totalChips; i++) {
            Locator dataSource = dataSourceColumn(i);

            driverTools.click(chipsDropdown(i));
            driverTools.waitForElementToAppear(tabNameInput("en", i));
            driverTools.clickWithActions(tabNameInput("en", i));
            driverTools.waitForElementToAppear(tabNameInput("en", i));
            driverTools.sendKeys(tabNameInput("en", i), tabNameEn);
            driverTools.waitForElementToAppear(tabNameInput("id", i));
            driverTools.clickWithActions(tabNameInput("id", i));
            driverTools.waitForElementToAppear(tabNameInput("id", i));
            driverTools.sendKeys(tabNameInput("id", i), tabNameId);
            driverTools.clickWithActions(dataSource);
            driverTools.sendKeys(dataSource, data_source);
        }

        driverTools.clickWithJavascriptExecutor(SUBMIT_CONTENT_MODULE);
        waitProcess(Duration.ofSeconds(3));
    }

    private void inputDynamicWidgetContent() {
        final String url = getValueOrDefault(sheetData.get("dynamicUrl"), "https://preprod.tiket.com/id-id/dynamic-content-module/mission/regress-milestone-one");
        final String webviewSize = getValueOrDefault(sheetData.get("webviewSize"), "240");
        final String seeAllButton = getValueOrDefault(sheetData.get("seeAllButton"), "Bottom CTA");
        final String ctaTextId = getValueOrDefault(sheetData.get("bottomCtaTextId"), "Lihat Semua");
        final String ctaTextEn = getValueOrDefault(sheetData.get("bottomCtaTextEn"), "See All");
        final String ctaLinkId = getValueOrDefault(sheetData.get("bottomCtaLinkId"), "/mission/regress-milestone-one");
        final String ctaLinkEn = getValueOrDefault(sheetData.get("bottomCtaLinkEn"), "/mission/regress-milestone-one");

        driverTools.waitForElementToAppear(DW_URL_INPUT, 10);
        driverTools.clear(DW_URL_INPUT);
        driverTools.sendKeys(DW_URL_INPUT, url);

        driverTools.waitForElementToAppear(DW_WEBVIEW_SIZE_INPUT, 5);
        driverTools.clear(DW_WEBVIEW_SIZE_INPUT);
        driverTools.sendKeys(DW_WEBVIEW_SIZE_INPUT, webviewSize);

        driverTools.scrollIntoWebView(seeAllButtonOption(seeAllButton));
        driverTools.clickWithJavascriptExecutor(seeAllButtonOption(seeAllButton));

        if (seeAllButton.toLowerCase().contains("bottom")) {
            driverTools.waitForElementToAppear(DW_BOTTOM_CTA_TEXT_ID, 5);
            driverTools.sendKeys(DW_BOTTOM_CTA_TEXT_ID, ctaTextId);
            driverTools.sendKeys(DW_BOTTOM_CTA_TEXT_EN, ctaTextEn);
            driverTools.sendKeys(DW_BOTTOM_CTA_LINK_ID, ctaLinkId);
            driverTools.sendKeys(DW_BOTTOM_CTA_LINK_EN, ctaLinkEn);
        }

        driverTools.scrollIntoWebView(SUBMIT_CONTENT_MODULE);
        driverTools.clickWithJavascriptExecutor(SUBMIT_CONTENT_MODULE);
        waitProcess(Duration.ofSeconds(3));
    }

    private void inputMandatoryBannerCarouselV2() {
        String filePath = System.getProperty("user.dir") + "/src/test/resources/dashboard/platform.discovery/account-active.png";
        List<String> files = Collections.singletonList(filePath);

        driverTools.waitForElementToAppear(FORM_CONTENT_SETTINGS);
        driverTools.clickWithJavascriptExecutor(IMAGE_UPLOAD_RADIO);
        driverTools.clickWithJavascriptExecutor(NO_BACKGROUND_RADIO);
        chooseBannerSize();
        driverTools.waitForElementToAppear(CHOOSE_BANNER_SIZE);
        driverTools.clickWithJavascriptExecutor(NO_BUTT0N_RADIO);

        inputImageAndSlug(files);
        driverTools.clickWithJavascriptExecutor(SUBMIT_CONTENT_MODULE);
        waitProcess(Duration.ofSeconds(3));
    }

    public void addNewUpcomingBookingWidget() {
        checkIfUpcomingBookingWidgetIsPresent();

        String widgetName = sheetData.get("widgetName") + CoreFunctions.generateRandomString(5);
        clickAddCustomModuleButton();

        driverTools.waitForElementToAppear(INPUT_MODULE_NAME, 2);
        driverTools.sendKeys(INPUT_MODULE_NAME, widgetName);

        inputTemplateCode(sheetData.get("expectedTemplateCode"));
        inputCheckBoxPlatform(sheetData.get("expectedPlatform"));
        inputCurrency(sheetData.get("expectedCurrency"));
        inputShownTo(sheetData.get("expectedShownTo"));

        driverTools.scrollIntoWebView(PERIOD_ALWAYS_ON);
        driverTools.clickWithJavascriptExecutor(PERIOD_ALWAYS_ON);

        submitGeneralSettingsAndReturnToModules();

        sheetData.put("widgetName", widgetName);
    }

    public Map<String, String> getDataAfterSuccess() {
        driverTools.waitForElementToAppear(SEARCH_RESULT_ELEMENT, 3);
        Map<String, String> resultData = new HashMap<>();
        int totalResultFound = driverTools.elements(SEARCH_RESULT_ELEMENT).isEmpty() ? 0 : driverTools.elements(SEARCH_RESULT_ELEMENT).get().size();

        if (totalResultFound == 0) {
            Assertion.fail("Search result found 0");
        }

        resultData.put("totalSearchResult", String.valueOf(totalResultFound));
        WebElement searchResultElement = driverTools.element(SEARCH_RESULT_ELEMENT).orElseThrow();
        List<WebElement> resultElements = driverTools.elements(new Locator("td", By.tagName("td")), searchResultElement).orElseThrow();
        List<String> getTextFromElement = new ArrayList<>();

        for (WebElement elem : resultElements) {
            getTextFromElement.add(elem.getText());
        }

        while (getTextFromElement.get(1).equals("-")) {
            logger.log("CASE TITLE = '-'");
            waitProcess(Duration.ofSeconds(2));

            resultElements = driverTools.elements(new Locator("resultElements", By.tagName("td")), searchResultElement).orElseThrow();
            getTextFromElement = new ArrayList<>();

            if (!resultElements.isEmpty()) {
                for (WebElement elem : resultElements) {
                    getTextFromElement.add(driverTools.getText(elem));
                }
            }
        }

        resultData.put("actualWidgetName", getTextFromElement.get(1));
        resultData.put("actualTemplateCode", getTextFromElement.get(2));
        resultData.put("actualPlatform", getTextFromElement.get(4));
        resultData.put("actualCurrency", getTextFromElement.get(5));
        if (getTextFromElement.size() > 6) {
            resultData.put("segmentation", getTextFromElement.get(6));
        }
        if (getTextFromElement.size() > 7) {
            resultData.put("widgetStatus", getTextFromElement.get(7));
        }

        driverTools.click(EDIT_GENERAL_BUTTON);
        waitProcess(Duration.ofSeconds(5));
        if (driverTools.isElementDisplayed(ELLIPSIS_LOADING_ELEMENT)) {
            driverTools.waitForElementToDisappear(ELLIPSIS_LOADING_ELEMENT);
        }
        driverTools.waitForElementToAppear(INPUT_MODULE_NAME, 5);

        String moduleNameFromForm = Optional.ofNullable(driverTools.getValue(INPUT_MODULE_NAME)).orElse("").trim();
        if (!moduleNameFromForm.isBlank()) {
            resultData.put("actualWidgetName", moduleNameFromForm);
        }

        String isTemplateCodeClickable = driverTools.getValueAttribute(SELECT_TEMPLATE_CODE, "disabled") == null ? "NO" : "YES";
        resultData.put("templateCodeIsDisabled", isTemplateCodeClickable);

        resultData.put("actualShownTo", getActualShownTo());
        getDataOfPeriodSection(resultData);

        return resultData;
    }

    private String getActualShownTo() {
        driverTools.scrollIntoWebView(SHOW_TO_USER_THAT);
        List<String> selected = new ArrayList<>();
        for (String option : List.of("Login", "Logout")) {
            if (driverTools.isElementSelected(shownToCheckbox(option))) {
                selected.add(option.toUpperCase());
            }
        }
        return String.join(",", selected);
    }

    private void getDataOfPeriodSection(Map<String, String> resultData) {
        driverTools.scrollIntoWebView(SHOW_TO_USER_THAT);
        Locator customPeriodRadio = periodRadio("Custom Period");
        Locator rangePeriodRadio = periodRadio("Date Range");
        Locator alwaysOnRadio = periodRadio("Always On");
        if (driverTools.isElementSelected(customPeriodRadio)) {
            resultData.put("actualPeriod", "CUSTOM");
            resultData.put("actualRepeatEvery", Optional.ofNullable(driverTools.getValue(REPEAT_EVERY)).orElse("").trim());
            resultData.put("actualRepetition", Optional.ofNullable(driverTools.getValue(SELECT_REPETITION)).orElse("").trim());
            resultData.put("actualStartDaily", Optional.ofNullable(driverTools.getValue(SELECT_START_DAILY)).orElse("").trim());
            String startTimeValue = Optional.ofNullable(driverTools.getValue(SELECT_TIME)).orElse("").trim();
            String endTimeValue = Optional.ofNullable(driverTools.getValue(SELECT_END_TIME)).orElse("").trim();
            resultData.put("actualSelectTime", String.format("%s - %s", startTimeValue, endTimeValue));
            resultData.put("actualEndCondition", Optional.ofNullable(driverTools.getValue(END_CONDITION_INPUT)).orElse("").trim());
        } else if (driverTools.isElementSelected(rangePeriodRadio)) {
            resultData.put("actualPeriod", "RANGE");
            resultData.put("actualDrpStartDate", Optional.ofNullable(driverTools.getValue(DRP_START_DATE_INPUT)).orElse("").trim());
            resultData.put("actualDrpEndDate", Optional.ofNullable(driverTools.getValue(DRP_END_DATE_INPUT)).orElse("").trim());
        } else if (driverTools.isElementSelected(alwaysOnRadio)) {
            resultData.put("actualPeriod", "Always On");
        } else {
            throw new IllegalStateException("No period type is selected in the general settings form");
        }
        logger.log("Detected saved period: " + resultData.get("actualPeriod"));
    }

    public void searchNewlyCreatedWidget(String widgetName) {
        searchWidgetByNameOrTemplateCode("name", widgetName);
        if (countSearchResultRows() == 0 && sheetData.get("expectedTemplateCode") != null) {
            logger.log("No rows for template name '" + widgetName + "'; searching by template code");
            searchWidgetByNameOrTemplateCode("code", sheetData.get("expectedTemplateCode"));
        }
    }

    public void archiveUpdatedUpcomingBookingWidget() {
        archiveCreatedWidgetAfterVerify();
    }

    public void archiveDynamicWidget() {
        archiveCreatedWidgetAfterVerify();
    }

    private void archiveCreatedWidgetAfterVerify() {
        goBackToMobileModules();
        String widgetName = sheetData.get("widgetName");
        String templateCode = sheetData.get("expectedTemplateCode");
        logger.log("Archive widget after verify: " + widgetName);
        searchWidgetByNameOrTemplateCode("name", widgetName);
        if (countSearchResultRows() == 0) {
            logger.log("No rows for name '" + widgetName + "'; searching by template code: " + templateCode);
            searchWidgetByNameOrTemplateCode("code", templateCode);
        }
        archiveWidget(templateCode);
    }

    public void clickOnEditGeneral() {
        String originalWidgetName = sheetData.get("widgetName");
        sheetData.put("originalWidgetName", originalWidgetName);
        logger.log("Search module to edit by original name: " + originalWidgetName);
        searchWidgetByNameOrTemplateCode("name", originalWidgetName);
        driverTools.waitForElementToAppear(SEARCH_RESULT_ELEMENT, 1);
        int totalResult = countSearchResultRows();

        if (totalResult == 0) {
            logger.log("No result for original name; searching by template code: " + sheetData.get("expectedTemplateCode"));
            searchWidgetByNameOrTemplateCode("code", sheetData.get("expectedTemplateCode"));
            driverTools.waitForElementToAppear(SEARCH_RESULT_ELEMENT, 1);
            totalResult = countSearchResultRows();
        }

        while (totalResult == 0) {
            addNewUpcomingBookingWidget();
            searchWidgetByNameOrTemplateCode("code", sheetData.get("expectedTemplateCode"));
            driverTools.waitForElementToAppear(SEARCH_RESULT_ELEMENT, 1);
            totalResult = countSearchResultRows();
            if (totalResult > 0) {
                break;
            }
        }
        this.prevData = getDataAfterSuccess();
    }

    public void modifyGeneralUpcomingWidget() {
        waitForEditGeneralFormReady();
        updateTemplateName();
        updateInputCheckBoxPlatform(prevData.get("actualPlatform"), sheetData.get("expectedPlatform"));
        inputCurrency(sheetData.get("expectedCurrency"));
        updateShownTo(prevData.get("actualShownTo"), sheetData.get("expectedShownTo"));
        updatePeriodTo(sheetData.get("expectedPeriod"));
        driverTools.scrollIntoWebView(SUBMIT_GENERAL);
        driverTools.clickWithJavascriptExecutor(SUBMIT_GENERAL);
        waitForEditGeneralSaveSuccess();
        goBackToMobileModules();
    }

    private void waitForEditGeneralSaveSuccess() {
        boolean saveSucceeded = driverTools.waitForElementToAppear(EDIT_GENERAL_SUCCESS_SUBMIT,5).isPresent();
        if (!saveSucceeded) {
            throw new IllegalStateException("General settings save was not confirmed. Current URL: " + driverTools.getCurrentURL());
        }
        driverTools.waitForElementToDisappear(EDIT_GENERAL_SUCCESS_SUBMIT,5);
        if (driverTools.isElementDisplayed(ELLIPSIS_LOADING_ELEMENT)) {
            driverTools.waitForElementToDisappear(ELLIPSIS_LOADING_ELEMENT,5);
        }
        logger.log("General settings saved successfully");
    }

    private void waitForEditGeneralFormReady() {
        driverTools.waitForElementToAppear(INPUT_MODULE_NAME, 10);
        scrollFormFieldIntoView(INPUT_MODULE_NAME);
    }

    private void scrollFormFieldIntoView(Locator locator) {
        WebElement field = driverTools.waitForElementToPresence(locator, 10).orElseThrow();
        driverTools.scrollElementToCenter(field);
        waitProcess(Duration.ofMillis(300));
    }

    private void updateTemplateName() {
        scrollFormFieldIntoView(INPUT_MODULE_NAME);
        String baseName = prevData.get("actualWidgetName").replaceAll("U+$", "");
        String finalName = baseName + "U";

        sheetData.put("prevTitle", prevData.get("actualWidgetName"));
        sheetData.put("widgetName", finalName);

        replaceInputValue(INPUT_MODULE_NAME, finalName);
        logger.log("Updated module name from '" + baseName + "' to: " + finalName);
    }

    private void replaceInputValue(Locator locator, String value) {
        scrollFormFieldIntoView(locator);
        driverTools.sendKeysWithJsExecutorForceInputValueAndTriggerEvents(locator, value);
        waitProcess(Duration.ofMillis(500));

        String actual = Optional.ofNullable(driverTools.getValue(locator)).orElse("").trim();
        if (!value.equals(actual)) {
            logger.log("React input value not applied (expected=" + value + ", actual=" + actual + "); retrying with clear + sendKeys");
            deleteInput(locator);
            driverTools.sendKeys(locator, value);
            actual = Optional.ofNullable(driverTools.getValue(locator)).orElse("").trim();
        }
        if (!value.equals(actual)) {
            throw new IllegalStateException(
                    "Failed to set " + locator + ". Expected: " + value + ", actual: " + actual);
        }
    }

    private void deleteInput(Locator locator) {
        WebElement input = driverTools.waitForElementToPresence(locator, 10).orElseThrow();
        driverTools.scrollElementToCenter(input);
        waitProcess(Duration.ofMillis(300));
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'}); arguments[0].focus();", input);
        try {
            driverTools.clickWithActions(input);
        } catch (Exception e) {
            logger.log("Actions click on input failed, using JS focus: " + e.getMessage());
            driverTools.clickWithJavascriptExecutor(input);
        }
        driverTools.clear(input);
    }

    private void inputTemplateCode(String templateCode) {
        driverTools.scrollIntoWebView(SELECT_TEMPLATE_CODE);
        driverTools.clickWithJavascriptExecutor(SELECT_TEMPLATE_CODE);
        Locator templateOption = selectOptionTemplateCode(templateCode);
        driverTools.waitForElementToAppear(templateOption, 5);
        driverTools.scrollIntoWebView(templateOption);
        driverTools.clickWithJavascriptExecutor(templateOption);
    }

    private void inputSegmentation(String segmentation) {
        WebElement segInput = driver.findElement(SELECT_SEGMENTATION.by());
        String currentValue = segInput.getAttribute("value");
        if (currentValue != null && !currentValue.isEmpty()) {
            logger.log("Value already present" + currentValue);
            return;
        }
        driverTools.scrollIntoWebView(SELECT_SEGMENTATION);
        driverTools.clickWithJavascriptExecutor(SELECT_SEGMENTATION);
        Locator segmentationOption = selectOptionSegmentation(segmentation);
        driverTools.waitForElementToAppear(segmentationOption, 5);
        driverTools.scrollIntoWebView(segmentationOption);
        driverTools.clickWithJavascriptExecutor(segmentationOption);
    }

    private void inputCheckBoxPlatform(String platforms) {
        String[] plats = platforms.split("[,\\s]+");
        for (String platform : plats) {
            switch (platform) {
                case "ANDROID", "Android", "android":
                    driverTools.clickWithJavascriptExecutor(ANDROID_PLATFORM_CHECKBOX);
                    break;
                case "IOS", "Ios", "ios":
                    driverTools.clickWithJavascriptExecutor(IOS_PLATFORM_CHECKBOX);
                    break;
                case "MWEB", "MOBILE_WEB", "Mweb", "mweb":
                    driverTools.clickWithJavascriptExecutor(MWEB_PLATFORM_CHECKBOX);
                    break;
                default:
                    throw new NotImplementedException(String.format("%s is not known value", platform));
            }
        }
    }

    private void updateInputCheckBoxPlatform(String previousPlatforms, String updatedPlatforms) {
        inputCheckBoxPlatform(previousPlatforms);
        inputCheckBoxPlatform(updatedPlatforms);
    }

    private void inputCurrency(String currency) {
        if (currency.equalsIgnoreCase("ALL")) {
            currency = "All";
        }

        driverTools.scrollIntoWebView(CHOOSE_CURRENCY);
        driverTools.clickWithJavascriptExecutor(CHOOSE_CURRENCY);
        Locator currencyOption = selectCurrencyOptions(currency);
        driverTools.waitForElementToAppear(currencyOption, 5);
        driverTools.scrollIntoWebView(currencyOption);
        driverTools.clickWithJavascriptExecutor(currencyOption);
    }

    private void inputShownTo(String to) {
        String[] tos = to.split(" ");

        if (tos.length <= 1) {
            Arrays.fill(tos, null);
            tos = to.split(",");
        }

        for (String t : tos) {
            Character c = t.toUpperCase().charAt(0);
            String temp = t.substring(1).toLowerCase();
            t = c + temp;

            Locator shownToLocator = chooseShownTo(t);
            driverTools.scrollIntoWebView(shownToLocator);
            driverTools.clickWithJavascriptExecutor(shownToLocator);
        }
    }

    private void updateShownTo(String previousShown, String updatedShown) {
        inputShownTo(previousShown);
        inputShownTo(updatedShown);
    }

    private void updatePeriodTo(String period) {
        logger.log("Modify period to: " + period.replaceAll("\\s+", "_").toUpperCase());
        applyPeriodSelection(period);
    }

    private void inputPeriod(String period) {
        applyPeriodSelection(period);
    }

    private void applyPeriodSelection(String period) {
        period = period.replaceAll("\\s+", "_").toUpperCase();
        driverTools.scrollIntoWebView(SHOW_TO_USER_THAT);
        switch (period) {
            case "ALWAYS_ON" -> driverTools.clickWithJavascriptExecutor(choosePeriod(period));
            case "RANGE" -> setDateRangePeriod();
            case "CUSTOM" -> setCustomPeriod();
            default -> {
                logger.log(String.format("Period '%S' is not recognised.", period));
                Assertion.skip(String.format("Period '%S' is not recognised.", period));
            }
        }
    }

    private void setDateRangePeriod() {
        driverTools.clickWithJavascriptExecutor(choosePeriod("RANGE"));
        driverTools.scrollIntoWebView(SHOW_TO_USER_THAT);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd yyyy", Locale.ENGLISH);
        LocalDate dateNow = LocalDate.parse(CoreFunctions.getCurrentDateYYYY_MM_DD());
        dateNow = dateNow.plusDays(1);
        String startDate = formatter.format(dateNow);
        LocalDate endDateLocal = dateNow.plusDays(1);
        String endDate = formatter.format(endDateLocal);

        String expectedStartDate = CoreFunctions.convertDateFormat(String.valueOf(dateNow), "yyyy-MM-dd", "d MMM yyyy - HH:HH");
        String expectedEndDate = CoreFunctions.convertDateFormat(String.valueOf(endDateLocal), "yyyy-MM-dd", "d MMM yyyy - HH:HH");
        sheetData.put("expectedStartDate", expectedStartDate);
        sheetData.put("expectedEndDate", expectedEndDate);

        driverTools.click(DRP_START_DATE);
        waitForDatePickerOpen();
        clickDateInPicker(selectDayInOpenDatePicker(startDate));
        confirmDatePickerDone();

        driverTools.click(DRP_END_DATE);
        waitForDatePickerOpen();
        navigateDatePickerToDayIfNeeded(dateNow, endDate);
        clickDateInPicker(selectDayInOpenDatePicker(endDate));
        confirmDatePickerDone();
    }

    private void navigateDatePickerToDayIfNeeded(LocalDate startDate, String endDate) {
        Locator endDayLocator = selectDayInOpenDatePicker(endDate);
        if (driverTools.isElementPresent(endDayLocator)) {
            return;
        }
        LocalDate lastDayOfMonth = startDate.withDayOfMonth(startDate.lengthOfMonth());
        if (!startDate.equals(lastDayOfMonth)) {
            return;
        }
        final Locator nextMonthButton = new Locator("NEXT_MONTH_BUTTON",
                By.xpath("//div[contains(@class,'DatePickerDesktop')]//span[contains(@class,'nav right')]"));
        int attempts = 0;
        while (!driverTools.isElementPresent(endDayLocator) && attempts < 3) {
            driverTools.clickWithJavascriptExecutor(nextMonthButton);
            waitProcess(Duration.ofMillis(300));
            attempts++;
        }
    }

    private void setCustomPeriod() {
        Locator customPeriod = choosePeriod("CUSTOM");
        driverTools.scrollIntoWebView(customPeriod);
        driverTools.clickWithJavascriptExecutor(customPeriod);

        if (driverTools.waitForElementToBeClickable(REPEAT_EVERY, 5).isEmpty()) {
            throw new IllegalStateException("Repeat Every input is not clickable");
        }

        String repeatOption = Optional.ofNullable(sheetData.get("repeatOption")).orElse("Day");
        String repeatEvery = String.valueOf(CoreFunctions.generateRandomInt(1, 5));
        sheetData.put("expectedRepeatEvery", repeatEvery);
        replaceInputValue(REPEAT_EVERY, repeatEvery);

        selectStartDailyWithDone();

        driverTools.click(SELECT_REPETITION);
        Locator repetitionOption = selectRepetition(repeatOption);
        if (driverTools.waitForElementToBeClickable(repetitionOption, 5).isEmpty()) {
            throw new IllegalStateException("Repetition option not clickable: " + repeatOption);
        }
        driverTools.click(repetitionOption);

        String startTime = Optional.ofNullable(sheetData.get("startTime")).filter(value -> !value.isBlank())
                .orElseThrow(() -> new IllegalStateException("startTime is missing from test data"));
        String endTime = Optional.ofNullable(sheetData.get("endTime")).filter(value -> !value.isBlank())
                .orElseThrow(() -> new IllegalStateException("endTime is missing from test data"));

        // Select start time from the roller
        driverTools.scrollIntoWebView(SELECT_TIME);
        driverTools.click(SELECT_TIME);
        driverTools.waitForElementToAppear(TIME_POPOVER, 5);

        Locator startTimeOption = timeOption(startTime);

        if (driverTools.waitForElementToBeClickable(startTimeOption, 5).isEmpty()) {
            throw new IllegalStateException("Start time option was not clickable: " + startTime);
        }

        driverTools.click(startTimeOption);
        driverTools.waitForElementToDisappear(TIME_POPOVER, 5);

        // Select end time from the roller
        driverTools.scrollIntoWebView(SELECT_END_TIME);
        driverTools.click(SELECT_END_TIME);
        driverTools.waitForElementToAppear(TIME_POPOVER, 5);

        Locator endTimeOption = timeOption(endTime);

        if (driverTools.waitForElementToBeClickable(endTimeOption, 5).isEmpty()) {
            throw new IllegalStateException("End time option was not clickable: " + endTime);
        }

        driverTools.click(endTimeOption);
        driverTools.waitForElementToDisappear(TIME_POPOVER, 5);

        // End condition: Never
        driverTools.scrollIntoWebView(CUSTOM_PERIOD_END_INPUT);
        driverTools.clickWithJavascriptExecutor(CUSTOM_PERIOD_END_INPUT);
        Locator neverOption = selectEndConditions("Never");
        if (driverTools.waitForElementToBeClickable(neverOption, 5).isEmpty()) {
            throw new IllegalStateException("Never end condition not clickable");
        }
        driverTools.click(neverOption);
    }

    private void selectTimeWithArrow(Locator timeInputLocator, Locator arrowLocator, int clicks) {
        if (driverTools.isElementPresent(TIME_POPOVER)) {
            driverTools.clickWithJavascriptExecutor(timeInputLocator);
            driverTools.waitForElementToDisappear(TIME_POPOVER, 3);
            waitProcess(Duration.ofMillis(300));
        }
        driverTools.clickWithJavascriptExecutor(timeInputLocator);
        driverTools.waitForElementToAppear(TIME_POPOVER, 5);
        for (int i = 0; i < clicks; i++) {
            driverTools.clickWithJavascriptExecutor(arrowLocator);
            waitProcess(Duration.ofMillis(200));
        }
        driverTools.clickWithJavascriptExecutor(timeInputLocator);
        driverTools.waitForElementToDisappear(TIME_POPOVER, 3);
        waitProcess(Duration.ofMillis(200));
    }

    private void searchPage(String pageName) {
        waitProcess(Duration.ofSeconds(4));
        driverTools.waitForElementToAppear(ALL_PAGES_TABLE, 10);

        driverTools.sendKeys(SEARCH_BAR_ELEMENT, pageName);
        driverTools.sendKeys(SEARCH_BAR_ELEMENT, String.valueOf(Keys.ENTER));
        waitProcess(Duration.ofSeconds(2));
    }

    private void searchWidgetByNameOrTemplateCode(String by, String name) {
        waitForMobileModulesSearchReady();

        if (by.equals("code")) {
            selectTemplateCodeFilterAndSearch(name);
        } else {
            driverTools.waitForElementToAppear(TEMPLATE_NAME_SEARCH_INPUT, 5);
            driverTools.click(TEMPLATE_NAME_SEARCH_INPUT);
            driverTools.clear(TEMPLATE_NAME_SEARCH_INPUT);
            driverTools.sendKeys(TEMPLATE_NAME_SEARCH_INPUT, name);
            clickSearchModules();
        }
    }

    private void selectTemplateCodeFilterAndSearch(String templateCode) {
        selectTemplateCodeFilterAndSearch(templateCode, true);
    }

    private void selectTemplateCodeFilterAndSearch(String templateCode, boolean waitForResults) {
        driverTools.scrollIntoWebView(SELECT_TEMPLATE_CODE);
        driverTools.clickWithJavascriptExecutor(SELECT_TEMPLATE_CODE);
        waitProcess(Duration.ofSeconds(1));

        Locator option = selectOptionTemplateCode(templateCode);
        if (!driverTools.isElementDisplayed(option)) {
            option = getLocatorTemplateCode(templateCode);
        }
        driverTools.waitForElementToAppear(option, 5);
        driverTools.scrollIntoWebView(option);
        driverTools.clickWithJavascriptExecutor(option);
        waitProcess(Duration.ofSeconds(1));

        clickSearchModules(waitForResults);
        waitForTemplateCodeDropdownClosed();
    }

    private void waitForTemplateCodeDropdownClosed() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.invisibilityOfElementLocated(
                    By.xpath("//div[contains(@data-testid,'option-single')]")));
        } catch (Exception e) {
            logger.log("Template code dropdown panel still visible or not present: " + e.getMessage());
        }
    }

    private void checkIfUpcomingBookingWidgetIsPresent() {
        waitForMobileModulesSearchReady();
        selectTemplateCodeFilterAndSearch("UPCOMING_WIDGET_V2", false);
        int totalSearchResult = countSearchResultRows();
        if (totalSearchResult == 0) {
            logger.log("No UPCOMING_WIDGET_V2 modules found; proceeding to create.");
            scrollMobileModulesPageToTop();
            return;
        }
        logger.log("Found " + totalSearchResult + " UPCOMING_WIDGET_V2 module(s) to archive before create.");
        for (int i = 0; i < totalSearchResult; i++) {
            archiveWidget("UPCOMING_WIDGET_V2");
            waitProcess(Duration.ofSeconds(3));
        }
        scrollMobileModulesPageToTop();
    }

    private void scrollMobileModulesPageToTop() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");
        waitProcess(Duration.ofMillis(300));
    }

    private void clickAddCustomModuleButton() {
        waitForMobileModulesSearchReady();
        scrollMobileModulesPageToTop();
        WebElement addButton = driverTools.waitForElementToBeClickable(ADD_CUSTOM_MODULE_BUTTON, 10)
                .orElseThrow(() -> new IllegalStateException("Add Custom Module button not found"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center', inline:'nearest'});", addButton);
        waitProcess(Duration.ofMillis(300));
        try {
            driverTools.clickWithActions(addButton);
        } catch (Exception e) {
            logger.log("Actions click on Add Custom Module failed, using JS: " + e.getMessage());
            driverTools.clickWithJavascriptExecutor(addButton);
        }
        logger.log("Clicked Add Custom Module");
    }

    private int countSearchResultRows() {
        return driverTools.elements(SEARCH_RESULT_ELEMENT).map(List::size).orElse(0);
    }

    public void normalizeTemplateNumber(int maxTotalTemplate, String templateCode) {
        try {
            wait.until(
                    (ExpectedCondition<Boolean>) wd -> ((JavascriptExecutor) Objects.requireNonNull(wd))
                            .executeScript("return document.readyState")
                            .equals("complete"));

            if (driverTools.isElementDisplayed(INPUT_MODULE_NAME)) {
                goBackToMobileModules();
            }

            searchWidgetByNameOrTemplateCode("code", templateCode);
            driverTools.waitForElementToAppear(SEARCH_RESULT_ELEMENT, 1);
            int totalSearchResult = countSearchResultRows();
            if (totalSearchResult >= maxTotalTemplate) {
                int deviation = totalSearchResult - maxTotalTemplate;
                for (int i = 0; i < deviation; i++) {
                    archiveWidget(templateCode);
                }
            }
        } catch (Exception e) {
            logger.log("Failed in normalizing the widget number.");
            logger.log("error Message: " + e.getMessage());
        }
    }

    private void clickMoreMenuInRow(WebElement row) {
        List<WebElement> actionButtons = row.findElements(By.xpath(".//td[last()]//div[contains(@class,'icon_btn')]"));
        if (actionButtons.isEmpty()) {
            actionButtons = row.findElements(By.xpath(".//div[contains(@class,'icon_btn')]"));
        }

        if (actionButtons.isEmpty()) {
            List<WebElement> ellipsisIcons = row.findElements(By.xpath(".//i[contains(@class,'elipsis') or contains(@class,'ellipsis')]"));
            if (!ellipsisIcons.isEmpty()) {
                clickMoreMenuButton(ellipsisIcons.get(ellipsisIcons.size() - 1), "ellipsis-icon");
                return;
            }
            throw new IllegalStateException("More menu not found in table row");
        }

        WebElement moreButton = actionButtons.get(actionButtons.size() - 1);
        clickMoreMenuButton(moreButton, "more-menu icon_btn index " + (actionButtons.size() - 1));
    }

    private void clickArchivePageMenu() {
        WebElement archivePage = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(ARCHIVE_MODULE_MENU.by()));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", archivePage);
        waitProcess(Duration.ofMillis(300));
        try {
            driverTools.clickWithActions(archivePage);
        } catch (Exception e) {
            logger.log("Actions click on Archive Page failed, using JS: " + e.getMessage());
            driverTools.clickWithJavascriptExecutor(archivePage);
        }
        logger.log("Clicked Archive Page menu");
    }

    private void clickMoreMenuButton(WebElement element, String label) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", element);
        waitProcess(Duration.ofMillis(500));
        try {
            driverTools.clickWithActions(element);
        } catch (Exception e) {
            logger.log("Actions click failed on " + label + ", using JS: " + e.getMessage());
            driverTools.clickWithJavascriptExecutor(element);
        }
        logger.log("Clicked more menu candidate: " + label);
    }

    private void archiveWidget(String templateCode) {
        List<WebElement> rows = driverTools.elements(SEARCH_RESULT_ELEMENT).orElse(Collections.emptyList());
        if (rows.isEmpty()) {
            logger.log("No module rows to archive for template: " + templateCode);
            return;
        }
        WebElement searchResultElement = rows.get(0);
        List<WebElement> resultElements = driverTools.elements(new Locator("td", By.tagName("td")), searchResultElement).orElseThrow();
        if (resultElements.size() < 3) {
            throw new IllegalStateException("Search result row has too few columns to archive: " + resultElements.size());
        }
        List<String> resultElementText = new ArrayList<>();
        for (WebElement elem : resultElements) {
            resultElementText.add(driverTools.getText(elem));
        }

        logger.log(String.format("trying to archive module '%s' with name '%s'", resultElementText.get(2), resultElementText.get(1)));

        clickMoreMenuInRow(searchResultElement);
        clickArchivePageMenu();

        driverTools.waitForElementToAppear(ARCHIVE_CONFIRM_BUTTON, 10);
        WebElement archiveConfirm = driverTools.element(ARCHIVE_CONFIRM_BUTTON)
                .orElseThrow(() -> new IllegalStateException("Archive confirm button not found"));
        driverTools.scrollElementToCenter(archiveConfirm);
        try {
            driverTools.clickWithActions(archiveConfirm);
        } catch (Exception e) {
            logger.log("Actions click on Archive confirm failed, using JS click: " + e.getMessage());
            driverTools.clickWithJavascriptExecutor(archiveConfirm);
        }

        driverTools.waitForElementToAppear(ARCHIVE_SUCCESS_SNACKBAR, 15);
        driverTools.waitForElementToDisappear(ARCHIVE_SUCCESS_SNACKBAR, 10);

        selectTemplateCodeFilterAndSearch(templateCode);
    }

    private void clickSearchModules() {
        clickSearchModules(true);
    }

    private void clickSearchModules(boolean waitForResults) {
        if (driverTools.isElementDisplayed(MORE_MENU_POP_OVER)) {
            driver.findElement(By.tagName("body")).sendKeys(Keys.ESCAPE);
            driverTools.waitForElementToDisappear(MORE_MENU_POP_OVER, 3);
        }
        WebElement searchButton = driverTools.waitForElementToAppear(SEARCH_MODULES_BUTTON, 10).orElseThrow();
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", searchButton);
        waitProcess(Duration.ofMillis(300));
        driverTools.clickWithActions(searchButton);
        if (waitForResults) {
            driverTools.waitForElementToAppear(SEARCH_RESULT_ELEMENT, 15);
        }
        waitProcess(Duration.ofSeconds(2));
    }

    private void chooseBannerSize() {
        WebElement label = driver.findElement(CHOOSE_BANNER_SIZE.by());
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", label);
        driverTools.click(CHOOSE_BANNER_SIZE);

        driverTools.click(BANNER_1_1_REGULAR);
        driverTools.waitForElementToDisappear(BANNER_SIZE_OPTION_BOX, 2);
    }
    private void inputImageAndSlug(List<String> files) {
        final String slugId = getValueOrDefault(sheetData.get("slugId"), "/hotel");
        final String slugEn = getValueOrDefault(sheetData.get("slugEn"), "/hotel");
        uploadImage(files);
        driverTools.waitForElementToBeClickable(SLUG_ID);
        driverTools.sendKeys(SLUG_ID, slugId);
        driverTools.waitForElementToBeClickable(TAB_CONTENT_EN);
        driverTools.scrollElementToCenter(TAB_CONTENT_EN);
        driverTools.clickWithJavascriptExecutor(TAB_CONTENT_EN);
        waitProcess(Duration.ofSeconds(2));
        uploadImage(files);
        driverTools.waitForElementToBeClickable(SLUG_EN);
        driverTools.sendKeys(SLUG_EN, slugEn);
    }

    private void uploadImage(List<String> files) {
        try {
            driverTools.waitForElementToPresence(BANNER_IMAGE_UPLOAD_INPUT, 10);
            String absoluteImagePath = Paths.get(files.get(0)).toAbsolutePath().toString();
            driverTools.sendKeys(BANNER_IMAGE_UPLOAD_INPUT, absoluteImagePath);
            waitProcess(Duration.ofSeconds(3)); // Wait for upload to process
            logger.log("Uploaded banner image: " + absoluteImagePath);
        } catch (Exception e) {
            logger.log("Banner image upload failed: " + e.getMessage());
            throw new RuntimeException("Banner image upload failed: " + e.getMessage());
        }
    }

    private void submitGeneralSettingsAndReturnToModules() {
        driverTools.scrollIntoWebView(SAVE_AND_GO_TO_CONTENT);
        driverTools.clickWithJavascriptExecutor(SAVE_AND_GO_TO_CONTENT);
        waitProcess(Duration.ofSeconds(3));
        if (driverTools.isElementDisplayed(ELLIPSIS_LOADING_ELEMENT)) {
            logger.log("Getting loading ellipsis screen");
            driverTools.waitForElementToDisappear(ELLIPSIS_LOADING_ELEMENT, 5);
        }
        handleNewWindow();
        if (driverTools.isElementDisplayed(BACK_TO_MOBILE_MODULE)) {
            driverTools.clickWithJavascriptExecutor(BACK_TO_MOBILE_MODULE);
            waitForMobileModulesSearchReady();
        } else {
            goBackToMobileModules();
        }
    }

    private void waitForMobileModulesSearchReady() {
        driverTools.waitForElementToAppear(TEMPLATE_NAME_SEARCH_INPUT, 10);
    }

    private void goBackToMobileModules() {
        driverTools.scrollIntoWebView(BREADCRUMB_BACK_TO_MOBILE_MODULE);
        driverTools.waitForElementToAppear(BREADCRUMB_BACK_TO_MOBILE_MODULE, 10);
        driverTools.clickWithJavascriptExecutor(BREADCRUMB_BACK_TO_MOBILE_MODULE);
        waitForMobileModulesSearchReady();
    }

    public void handleNewWindow() {
        try {
            wait.until(d -> d.getWindowHandles().size() > 1);
            Set<String> allTabs = driver.getWindowHandles();
            if (allTabs.size() > 1) {
                String currentTab = driver.getWindowHandle();
                logger.log("All windows are: " + allTabs);
                for (String tab : allTabs) {
                    if (!tab.equals(currentTab)) {
                        driver.switchTo().window(tab);
                        break;
                    }
                }
            }
        } catch (TimeoutException e) {
            logger.log(e.getMessage());
        }
    }

    private void handleGetInPageAppearing() {
        if (driverTools.isElementDisplayed(GET_IN_BUTTON)) {
            driverTools.click(GET_IN_BUTTON);
        }
    }

    private String getValueOrDefault(String value, String def) {
        return Optional.ofNullable(value).orElse(def);
    }

    private void waitProcess(Duration duration) {
        wait = new WebDriverWait(driver, duration.plus(Duration.ofMillis(500)));
        long expectedTime = System.currentTimeMillis() + duration.toMillis();
        wait.pollingEvery(duration);
        logger.log("waiting...");
        wait.until(driver -> {
            long time = System.currentTimeMillis();
            return time >= expectedTime;
        });
        logger.log("done waiting");
    }

    @Getter
    @Setter
    private Map<String, String> prevData, sheetData;
}
