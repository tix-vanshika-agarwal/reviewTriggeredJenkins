package com.tiket.web.dashboard.page.payment;

import com.mongodb.client.FindIterable;
import com.tiket.core.CoreFunctions;
import com.tiket.dbclient.TiketMongoDBManager;
import com.tiket.model.Deeplink;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.bson.Document;
import org.openqa.selenium.*;

import java.util.NoSuchElementException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PaymentInstallmentPage<T extends WebDriver> extends BasePage<T> {

    private static final String installmentConfigPath = "/admin/payment_v2/card-payment-config/installment-configuration";
    private static final Locator CARD_CONFIG_SIDEMENU = new Locator("CARD_CONFIG_SIDEMENU", By.xpath("//*[@data-testid='sidemenu']//*[contains(text(),'Card Payment Configuration')]"));
    private static final Locator INSTALLMENT_CONFIG_SUBMENU = new Locator("INSTALLMENT_CONFIG_SUBMENU", By.xpath("//*[contains(@data-testid,'submenu')]//*[contains(text(),'Installment Configuration')]"));
    private static final Locator INSTALLMENT_CONFIG_TITLE = new Locator("INSTALLMENT_CONFIG_TITLE", By.xpath("//h1[contains(text(),'Installment Configuration')]"));
    private static final Locator DROPDOWN_OPTIONS_CONTAINER = new Locator("DROPDOWN_OPTIONS_CONTAINER",
            By.xpath("//div[contains(@class,'styles_dropdown_options')]"));
    private static final Locator LOADING = new Locator("LOADING", By.xpath("//*[@data-testid='loading']"));
    private static final Locator INSTALLMENT_LIST_LOADING = new Locator("INSTALLMENT_LIST_LOADING", By.xpath("//*[@data-testid='list-loading-indicator']"));
    private static final Locator BANK_NAME = new Locator("BANK_NAME", By.xpath("//*[@id='bank-name']"));
    private static final Locator CARD_NAME = new Locator("CARD_NAME", By.xpath("//*[@id='card-name']"));
    private static final Locator RECIPIENT_EMAIL = new Locator("RECIPIENT_EMAIL", By.xpath("//*[contains(text(),'Recipient Email')]/following-sibling::*//input"));
    private static final Locator RECIPIENT_EMAIL_OPTION = new Locator("RECIPIENT_EMAIL_OPTION", By.xpath("//*[contains(@id,'option')][contains(text(),'Insert')]"));
    private static final Locator SEND_EMAIL = new Locator("SEND_EMAIL", By.xpath("//input[@name='sendEmail'][@value='true']/parent::*"));
    private static final Locator NOT_SEND_EMAIL = new Locator("NOT_SEND_EMAIL", By.xpath("//input[@name='sendEmail'][@value='false']/parent::*"));
    private static final Locator PROMO_TITLE = new Locator("PROMO_TITLE", By.xpath("//*[@id='promo-title']"));
    private static final Locator BIN_TEXT_BOX = new Locator("BIN_TEXT_BOX", By.xpath("//*[@id='bin']"));
    private static final Locator BIN_MODAL_INPUT_BIN = new Locator("BIN_MODAL_INPUT_BIN", By.xpath("//*[@data-testid='bin-number-input']"));
    private static final String INSTALLMENT_DETAILS_SCOPE = "//span[normalize-space()='Installment Details']/ancestor::div[contains(@class,'card_container')]";
    private static final Locator DURATION = new Locator("Duration", By.xpath(INSTALLMENT_DETAILS_SCOPE + "//input[@id='duration']"));
    private static final Locator ADD_DURATION_BUTTON = new Locator("ADD_DURATION_BUTTON",
            By.xpath(INSTALLMENT_DETAILS_SCOPE + "//button[contains(@class,'KV-nSG_button') and contains(@class,'KV-nSG_variant_primary') and contains(normalize-space(),'Add Duration')]"));
    private static final Locator FOR_FINANCE = new Locator("FOR_FINANCE", By.xpath(INSTALLMENT_DETAILS_SCOPE + "//input[@id='for-finance-in-']"));
    private static final Locator FOR_BANK = new Locator("FOR_BANK", By.xpath(INSTALLMENT_DETAILS_SCOPE + "//input[@id='for-bank-in-']"));
    private static final Locator MINIMUM_PAYMENT = new Locator("MINIMUM_PAYMENT", By.xpath(INSTALLMENT_DETAILS_SCOPE + "//input[@id='min-payment-idr']"));
    private static final Locator RECONCILE_RATE = new Locator("RECONCILE_RATE", By.xpath(INSTALLMENT_DETAILS_SCOPE + "//input[@id='recon-rate']"));
    private static final Locator INSTALLMENT_DETAIL_GRID_ROWS = new Locator("INSTALLMENT_DETAIL_GRID_ROWS",
            By.xpath(INSTALLMENT_DETAILS_SCOPE + "//div[contains(@class,'grid-cols-6')]"));
    private static final Locator MONTH_DETAILS_SECTION = new Locator("MONTH_DETAILS_SECTION",
            By.xpath("//span[contains(@class,'HcPVsG_weight_bold') and normalize-space()='Month Details']"));
    private static final Locator MONTH_DETAILS_ROWS = new Locator("MONTH_DETAILS_ROWS",
            By.xpath("//span[normalize-space()='Month Details']/parent::div//table//tbody/tr"));
    private static final Locator TOTAL_INTEREST_DISPLAY = new Locator("TOTAL_INTEREST_DISPLAY",
            By.xpath(INSTALLMENT_DETAILS_SCOPE + "//p[contains(normalize-space(),'Total Interest')]/following-sibling::p"));
    private static final Locator INPUT_FILTER_SEARCHBY = new Locator("INPUT_FILTER_SEARCHBY", By.xpath("//*[@data-testid='response-code-field']"));
    private static final Locator START_DATE_CREATE_INSTALLMENT = new Locator("START_DATE_CREATE_INSTALLMENT", By.xpath("//*[@id='start-date']"));
    private static final Locator END_DATE_CREATE_INSTALLMENT = new Locator("END_DATE_CREATE_INSTALLMENT", By.xpath("//*[@id='end-date']"));
    private static final Locator END_DATE_EDIT_INSTALLMENT = new Locator("END_DATE_EDIT_INSTALLMENT",
            By.xpath("//div[contains(@class,'styles_customdatepicker')][.//span[contains(normalize-space(),'End Date')]]//input"));
    private static final Locator PREVIOUS_MONTH_CALENDAR = new Locator("PREVIOUS_MONTH_CALENDAR", By.xpath("//*[contains(@aria-label,'Previous Month')]"));
    private static final Locator NEXT_MONTH_CALENDAR = new Locator("NEXT_MONTH_CALENDAR", By.xpath("//*[contains(@aria-label,'Next Month')]"));
    private static final Locator CURRENT_MONTH_CALENDAR = new Locator("CURRENT_MONTH_CALENDAR", By.xpath("//*[contains(@class,'current-month')]"));
    private static final Locator CONFIGURATION_LOG_SECTION = new Locator("CONFIGURATION_LOG_SECTION",
            By.xpath("//span[contains(@class,'HcPVsG_weight_bold') and normalize-space()='Configuration Log']"));
    private static final String CONFIGURATION_LOG_TABLE_SCOPE =
            "//span[normalize-space()='Configuration Log']/ancestor::div[contains(@class,'card_container')][1]//table[@data-testid='table-control']";
    private static final Locator CONFIGURATION_LOG_ROWS = new Locator("CONFIGURATION_LOG_ROWS",
            By.xpath(CONFIGURATION_LOG_TABLE_SCOPE + "//tbody//tr[contains(@data-testid,'row-key-')]"));
    private static final Locator TOTAL_INTEREST = TOTAL_INTEREST_DISPLAY;
    private static final Locator INACTIVE_MESSAGE_ID = new Locator("INACTIVE_MESSAGE_ID", By.xpath("//textarea[@placeholder='Inactive Message - ID']"));
    private static final Locator INACTIVE_MESSAGE_EN = new Locator("INACTIVE_MESSAGE_EN", By.xpath("//textarea[@placeholder='Inactive Message - EN']"));
    private static final Locator BIN_MODAL_ROW_LABEL = new Locator("BIN_MODAL_ROW_LABEL", By.xpath("//label[contains(@class,'OHu2-q_check')]"));
    private static final Locator BIN_MODAL_SAVE_BUTTON = new Locator("BIN_MODAL_SAVE_BUTTON", By.xpath("(//button[contains(@class,'KV-nSG_button') and contains(@class,'KV-nSG_variant_primary') and normalize-space()='Save'])[2]"));
    private static final Locator SNACKBAR_TEXT = new Locator("SNACKBAR_TEXT", By.xpath("//*[contains(@class,'snackbar')]//*[contains(@class,'text_content')]"));
    private static final Locator FIRST_ROW_EXPAND_TOGGLE = new Locator("FIRST_ROW_EXPAND_TOGGLE", By.cssSelector("[data-testid='table-control-content-expandable-row-0'] svg.cursor-pointer"));
    private static final Locator EXPANDED_ROW_CONTENT = new Locator("EXPANDED_ROW_CONTENT", By.xpath("(//*[contains(@data-testid,'row-expanded') or contains(@class,'expanded_row') or contains(@class,'expanded_content')])[1]"));
    private static final Locator INSTALLMENT_LIST_EMPTY = new Locator("INSTALLMENT_LIST_EMPTY", By.xpath("//*[@data-testid='table-control']//*[contains(text(),'no data')]"));
    private static final Locator INSTALLMENT_REPORT_TITLE = new Locator("INSTALLMENT_REPORT_TITLE", By.xpath("//h1[contains(text(),'Installment Report')]"));
    private static final Locator INSTALLMENT_REPORT_EMPTY = new Locator("INSTALLMENT_REPORT_EMPTY", By.xpath("//*[@data-testid='table-control']//*[contains(text(),'no data')]"));
    private static final Locator PAGINATION_LIST = new Locator("PAGINATION_LIST", By.xpath("//*[@data-testid='pagination']//button//*[contains(@class,'lowEmphasis')]"));
    private static final Locator ROW_ON_TABLE = new Locator("ROW_ON_TABLE", By.xpath("//*[contains(@data-testid,'row-key')]"));
    private static final Locator FOOTER = new Locator("FOOTER", By.xpath("//*[@data-testid='footer']"));
    private static final Locator SHOWING_DATA_PER_PAGE = new Locator("SHOWING_DATA_PER_PAGE", By.xpath("//*[contains(@class,'footer')]//*[contains(text(),'Showing')]"));
    private static final Locator MONTH_HEADING_REPORT_LEFT = new Locator("MONTH_HEADING_REPORT_LEFT", By.xpath("(//*[contains(@class,'month_heading')])[1]"));
    private static final Locator MONTH_HEADING_REPORT_RIGHT = new Locator("MONTH_HEADING_REPORT_RIGHT", By.xpath("(//*[contains(@class,'month_heading')])[2]"));
    private static final Locator PREV_DATE_REPORT = new Locator("PREV_DATE_REPORT", By.xpath("//*[contains(@class,'prev_button')]"));
    private static final Locator NEXT_DATE_REPORT = new Locator("NEXT_DATE_REPORT", By.xpath("//*[contains(@class,'next_button')]"));
    private static final Locator EDIT = new Locator("EDIT", By.xpath("//div[contains(@class,'styles_dropdown_option')]//p[normalize-space()='Edit']/ancestor::div[@data-testid='dropdown-item-Edit']"));

    private Locator getFormDropdownInput(String placeholder) {
        return new Locator("FORM_DROPDOWN_" + placeholder.replaceAll("\\s+", "_").toUpperCase(), By.xpath("//input[@placeholder='" + placeholder + "']"));
    }

    private Locator getFormDropdownChevron(String placeholder) {
        return new Locator("FORM_DROPDOWN_CHEVRON_" + placeholder.replaceAll("\\s+", "_").toUpperCase(),
                By.xpath("//input[@placeholder='" + placeholder + "']/following-sibling::button[contains(@class,'XmDoza_clickable_icon')]"));
    }

    private Locator getFilterDropdownChevron(String filterName) {
        return new Locator("FILTER_DROPDOWN_CHEVRON_" + filterName.replaceAll("\\s+", "_").toUpperCase(),
                By.xpath("//*[@data-testid='dropdown-btn-default']//label[normalize-space()='" + filterName
                        + "']/following-sibling::button[contains(@class,'XmDoza_clickable_icon')]"
                        + " | //input[@placeholder='" + filterName
                        + "']/following-sibling::button[contains(@class,'XmDoza_clickable_icon')]"));
    }

    private Locator dropdownFilter(String filterName) {
        return new Locator("DROPDOWN_" + filterName.toUpperCase(), By.xpath("//*[@data-testid='dropdown-btn-default']//label[normalize-space()='" + filterName + "']/preceding-sibling::input | //input[@placeholder='" + filterName + "']"));
    }

    private Locator getDropdownOption(String optionText) {
        String normalizedOption = optionText.trim();
        String spaceUnderscoreId = normalizedOption.replaceAll("\\s+", "_");
        String upperUnderscoreId = normalizedOption.toUpperCase().replaceAll("[^A-Z0-9]+", "_");
        return new Locator("DROPDOWN_OPTION_" + upperUnderscoreId, By.xpath(
                "(//div[contains(@class,'styles_dropdown_options')]//div[contains(@class,'styles_dropdown_option')][@data-testid='dropdown-item-"
                        + upperUnderscoreId + "']"
                        + " | //div[contains(@class,'styles_dropdown_options')]//div[contains(@class,'styles_dropdown_option')][@data-testid='dropdown-item-"
                        + spaceUnderscoreId + "']"
                        + " | //div[contains(@class,'styles_dropdown_content')]//div[contains(@class,'styles_dropdown_option')][@data-testid='dropdown-item-"
                        + upperUnderscoreId + "']"
                        + " | //div[contains(@class,'styles_dropdown_content')]//div[contains(@class,'styles_dropdown_option')][@data-testid='dropdown-item-"
                        + spaceUnderscoreId + "']"
                        + " | //div[contains(@class,'styles_dropdown_options')]//div[contains(@class,'styles_dropdown_option')][.//p[normalize-space()='"
                        + normalizedOption + "']]"
                        + " | //div[contains(@class,'styles_dropdown_content')]//div[contains(@class,'styles_dropdown_option')][.//p[normalize-space()='"
                        + normalizedOption + "']]"
                        + " | //*[@data-testid='dropdown-item-" + upperUnderscoreId + "']"
                        + " | //*[@data-testid='dropdown-item-" + spaceUnderscoreId + "'])[1]"));
    }

    private Locator dropdownItem(String item) {
        return getDropdownOption(item);
    }
    private Locator getButtonByText(String button) {
        return new Locator("BUTTON_"+button.toUpperCase(), By.xpath("//button[contains(text(),'"+button+"')]"));
    }
    private Locator getFilterInstallmentReport(String filter) {
        return new Locator("FILTER_DROPDOWN_"+filter.toUpperCase(), By.xpath("//*[@id='filter-single-dropdown-"+filterToId.get(filter)+"-select']"));
    }
    private Locator getFilterDateInstallmentReport(String filter) {
        return new Locator("FILTER_DATE_"+filter.toUpperCase(), By.xpath("//*[@id='filter-date-"+filterToId.get(filter)+"-date']"));
    }
    private Locator getDropdownItem(String item) {
        return new Locator("DROPDOWN_ITEM_"+item.toUpperCase(), By.xpath("//*[@data-testid='dropdown-item-"+item.replaceAll("[^a-zA-Z0-9]+", "_")+"']"));
    }
    private Locator getCalendarInstallmentReport(String calendar) { // calendar format "d MMMM yyyy"
        return new Locator("CALENDAR_"+calendar.toUpperCase(), By.xpath("//*[contains(@aria-label,'"+calendar+"')]"));
    }
    private Locator getRowByColumn(String row, String columnName) {
        return new Locator("ROW_"+row+"_COLUMN_"+columnName.toUpperCase(), By.xpath("//*[@data-testid='row-cell-"+row+"-"+columnToId.get(columnName)+"']"));
    }
    private Locator getRowByColumnReport(String row, String columnName) {
        return new Locator("ROW_"+row+"_COLUMN_"+columnName.toUpperCase(), By.xpath("//*[@data-testid='row-cell-"+row+"-"+columnToIdReport.get(columnName)+"']"));
    }
    private Locator getCalendarDate(String day, String month, String year) {
        return new Locator("DATE_"+month.toUpperCase()+"_"+day+"_"+year, By.xpath("//*[contains(@aria-label,'"+month+" "+day+"')][contains(@aria-label,'"+year+"')]"));
    }
    private Locator getCardSummary(String cardName) {
        return new Locator("CARD_SUMMARY_"+cardName, By.xpath("//*[contains(text(),'"+cardName+"')]/following-sibling::*"));
    }
    private final Map<String, String> configurationLogColumnIds = Map.of(
            "Activity", "activity",
            "Username", "username",
            "Old Value", "oldValue",
            "New Value", "newValue",
            "Time", "time",
            "Timestamp", "time"
    );

    Map<String, String> columnToId = Map.of(
            "Bank Name", "bankName",
            "BIN", "bin",
            "Card Type", "cardType",
            "Card Name", "cardName",
            "Promo Title", "promoTitle",
            "Promo Start End Date", "promoStartEndDate",
            "Promo Status", "promoStatus",
            "Action", "action"
    );

    private final Map<String, String> filterSearchByToTestId = Map.of(
            "Bank Name", "Bank_Name",
            "Card Name", "Card_Name",
            "Promo Name", "Promo_Name"
    );

    Map<String, String> filterToId = Map.of(
            "Bank", "bank",
            "Tenor", "tenor",
            "Order By", "orderBy",
            "Start Date", "startDate",
            "End Date", "endDate"
    );
    Map<String, String> columnToIdReport = Map.ofEntries(
            new AbstractMap.SimpleEntry<>("Order ID", "orderId"),
            new AbstractMap.SimpleEntry<>("Bank", "bank"),
            new AbstractMap.SimpleEntry<>("Card Number", "cardNumber"),
            new AbstractMap.SimpleEntry<>("Account Holder Name", "accountHolderName"),
            new AbstractMap.SimpleEntry<>("Auth Code", "authCode"),
            new AbstractMap.SimpleEntry<>("Payment Gateway", "paymentGateway"),
            new AbstractMap.SimpleEntry<>("Card Type", "cardType"),
            new AbstractMap.SimpleEntry<>("Tenor", "tenor"),
            new AbstractMap.SimpleEntry<>("Date", "date"),
            new AbstractMap.SimpleEntry<>("Invoice", "invoice"),
            new AbstractMap.SimpleEntry<>("Base Price", "basePrice"),
            new AbstractMap.SimpleEntry<>("FDR Price", "fdrPrice"),
            new AbstractMap.SimpleEntry<>("Payment Fee", "paymentFee"),
            new AbstractMap.SimpleEntry<>("Percent Payment Fee", "percentPaymentFee"),
            new AbstractMap.SimpleEntry<>("Installment (+FDR)", "installmentFDR"),
            new AbstractMap.SimpleEntry<>("Percent Installment (+FDR)", "percentInstallmentFDR"),
            new AbstractMap.SimpleEntry<>("Bank Invoice", "bankInvoice"),
            new AbstractMap.SimpleEntry<>("Percent Bank Invoice", "percentBankInvoice"),
            new AbstractMap.SimpleEntry<>("Reconcile Internal", "reconcileInternal"),
            new AbstractMap.SimpleEntry<>("Percent Reconcile Internal", "percentReconcileInternal"),
            new AbstractMap.SimpleEntry<>("Profit", "profit")
    );

    public PaymentInstallmentPage(Session<T> session) {
        super(session);
    }

    public boolean isPaymentInstallmentPageOpened() {
        if (!driverTools.getCurrentURL().contains("installment-configuration")) {
            return false;
        }
        if (!driverTools.waitForElementToDisappear(LOADING)) {
            logger.log("Element not disappeared or even not appeared");
        }
        driverTools.waitForElementToDisappear(INSTALLMENT_LIST_LOADING, 5);
        return driverTools.waitForElementToPresence(INSTALLMENT_CONFIG_TITLE, 3).isPresent();
    }

    public void openPaymentInstallmentPage() {
        if (!isPaymentInstallmentPageOpened()) {
            Deeplink deeplink = new Deeplink("ADMIN_CARE", "https://gatotkaca.tiket.com", "https://preprod.tiket.com", "https://tiket.com");
            String url = switch (Configs.ENVIRONMENT) {
                    case PROD -> deeplink.prodDeeplink() + installmentConfigPath;
                    case PREPROD -> deeplink.preprodDeeplink() + installmentConfigPath;
                    case GK -> deeplink.gkDeeplink() + installmentConfigPath;
                    default -> deeplink.gkDeeplink() + installmentConfigPath;
            };
            driverTools.get(url);
            driverTools.waitForElementToDisappear(INSTALLMENT_LIST_LOADING, 10);
            driverTools.waitForElementToPresence(INSTALLMENT_CONFIG_TITLE, 10);
        }
    }

    public boolean isEmptyInstallmentReport() {
        return driverTools.waitForElementToPresence(INSTALLMENT_REPORT_EMPTY, 3).isPresent();
    }

    public boolean isPaymentInstallmentReportPageOpened() {
        if (!driverTools.waitForElementToDisappear(LOADING)) {
            logger.log("Element not disappeared or even not appeared");
        }
        return driverTools.waitForElementToPresence(INSTALLMENT_REPORT_TITLE, 3).isPresent();
    }

    public void clickOnLocator(Locator locator) {
        Optional<WebElement> btn = driverTools.waitForElementToBeClickable(locator, 3);
        if (btn.isPresent()) {
            driverTools.click(btn.get());
        } else {
            driverTools.clickWithJavascriptExecutor(locator);
        }
    }

    public void clickButton(String button) {
        clickOnLocator(getButtonByText(button));
    }

    public void clickOnCreateInstallment() {
        driverTools.waitForElementToAppear(getButtonByText("Create New Installment"), 10).ifPresent(driverTools::click);
    }

    public void clickAddDuration() {
        WebElement addDurationButton = driverTools.waitForElementToAppear(ADD_DURATION_BUTTON, 10)
                .orElseThrow(() -> new NoSuchElementException("Add Duration button not found"));
        driverTools.scrollElementToCenter(addDurationButton);
        driverTools.clickWithJavascriptExecutor(addDurationButton);
    }

    public void setBankName(String value) {
        WebElement bankName = driverTools.waitForElementToAppear(BANK_NAME, 3).orElseThrow();
        driverTools.clearInput(bankName);
        driverTools.click(bankName);
        driverTools.sendKeys(bankName, value);
    }

    public void setCardName(String value) {
        WebElement cardName = driverTools.waitForElementToAppear(CARD_NAME, 3).orElseThrow();
        driverTools.clearInput(cardName);
        driverTools.click(cardName);
        driverTools.sendKeys(cardName, value);
    }

    private Locator getVisibleDropdownOptionRow(String optionText) {
        String normalizedOption = optionText.trim();
        String optionId = normalizedOption.toUpperCase().replaceAll("[^A-Z0-9]+", "_");
        String optionsScope = "(//div[contains(@class,'styles_dropdown_options')])[last()]";
        return new Locator("DROPDOWN_OPTION_ROW_" + optionId, By.xpath(
                optionsScope + "//div[contains(@class,'styles_dropdown_option')][@data-testid='dropdown-item-" + optionId + "']"
                        + " | " + optionsScope + "//div[contains(@class,'styles_dropdown_option')][.//p[normalize-space()='"
                        + normalizedOption + "']]"));
    }

    private boolean isFormDropdownValueSelected(String placeholder, String optionText) {
        WebElement dropdownInput = driverTools.waitForElementToAppear(getFormDropdownInput(placeholder), 5).orElse(null);
        if (dropdownInput == null) {
            return false;
        }
        String expected = removeAllWhitespace(optionText);
        String selectedValue = driverTools.getValue(dropdownInput);
        if (selectedValue != null && removeAllWhitespace(selectedValue).equalsIgnoreCase(expected)) {
            return true;
        }
        String dataValue = dropdownInput.getAttribute("data-value");
        return dataValue != null && removeAllWhitespace(dataValue).equalsIgnoreCase(expected);
    }

    private void openFormDropdown(String placeholder) {
        Locator dropdownInputLocator = getFormDropdownInput(placeholder);
        driverTools.scrollToElement(driverTools.waitForElementToAppear(dropdownInputLocator, 10).orElseThrow());

        Locator chevronLocator = getFormDropdownChevron(placeholder);
        if (driverTools.element(chevronLocator).isPresent()) {
            driverTools.click(chevronLocator);
        } else {
            driverTools.click(dropdownInputLocator);
        }
        driverTools.waitForElementToAppear(DROPDOWN_OPTIONS_CONTAINER, 10);
    }

    private void selectFormDropdownOption(String placeholder, String optionText) {
        String normalizedOption = optionText.trim();
        Locator optionRowLocator = getVisibleDropdownOptionRow(normalizedOption);

        for (int attempt = 1; attempt <= 3; attempt++) {
            openFormDropdown(placeholder);
            driverTools.click(optionRowLocator);
            driverTools.waitForElementToDisappear(optionRowLocator, 3);

            if (isFormDropdownValueSelected(placeholder, normalizedOption)) {
                logger.log("Selected " + placeholder + " option: " + normalizedOption + " on attempt " + attempt);
                return;
            }
            logger.log(placeholder + " option not selected on attempt " + attempt + ", retrying");
        }

        throw new NoSuchElementException(placeholder + " option was not selected: " + normalizedOption);
    }

    public void selectCardType(String cardType) {
        selectFormDropdownOption("Card Type", resolveCardType(cardType));
    }

    public String resolveCardType(String cardType) {
        if (cardType == null || cardType.isBlank()) {
            return "MASTERCARD";
        }
        String normalizedCardType = cardType.trim();
        if ("Credit".equalsIgnoreCase(normalizedCardType)) {
            return "MASTERCARD";
        }
        return normalizedCardType;
    }

    public void selectRecipientEmail(String value) {
        driverTools.waitForElementToPresence(RECIPIENT_EMAIL, 3).ifPresentOrElse(driverTools::click, () -> { throw new NoSuchElementException("Element not found for"+RECIPIENT_EMAIL); });
        driverTools.sendKeys(RECIPIENT_EMAIL, value);
        driverTools.click(RECIPIENT_EMAIL_OPTION);
    }

    public void selectEmailAction(String emailAction) {
        switch (emailAction) {
            case "SEND": {
                driverTools.waitForElementToPresence(SEND_EMAIL).ifPresentOrElse(driverTools::click, () -> { throw new NoSuchElementException("Element not found for"+SEND_EMAIL); });
                break;
            }
            case "NOT_SEND": {
                driverTools.waitForElementToPresence(NOT_SEND_EMAIL).ifPresentOrElse(driverTools::click, () -> { throw new NoSuchElementException("Element not found for"+NOT_SEND_EMAIL); });
                break;
            }
        }
    }

    public void setPromoTitle(String value) {
        WebElement promoTitle = driverTools.waitForElementToAppear(PROMO_TITLE, 3).orElseThrow();
        driverTools.scrollToElement(promoTitle);
        driverTools.click(promoTitle);
        driverTools.sendKeys(promoTitle, value);
    }

    public void setBin(String value) {
        driverTools.waitForElementToAppear(BIN_TEXT_BOX, 3).ifPresentOrElse(driverTools::click, () -> { throw new NoSuchElementException("element not found for"+BIN_TEXT_BOX); });
        WebElement inputBin = driverTools.waitForElementToPresence(BIN_MODAL_INPUT_BIN).orElseThrow();
        try {
            driverTools.click(inputBin);
        } catch (ElementClickInterceptedException e) {
            driverTools.click(inputBin);
        }
        driverTools.sendKeys(inputBin, value);
        clickButton("Search");
        clickButton("Select All");
        clickButton("Done");
    }

    public void selectPromoStatus(String promoStatus) {
        selectFormDropdownOption("Promo Status", promoStatus);
    }

    private Locator getLastInstallmentDetailInput(String inputId) {
        return new Locator("LAST_INSTALLMENT_INPUT_" + inputId.toUpperCase().replace("-", "_"),
                By.xpath("(" + INSTALLMENT_DETAILS_SCOPE + "//input[@id='" + inputId + "'])[last()]"));
    }

    private Locator getLastTotalInterestDisplay() {
        return new Locator("LAST_TOTAL_INTEREST_DISPLAY",
                By.xpath("(" + INSTALLMENT_DETAILS_SCOPE + "//p[contains(normalize-space(),'Total Interest')]/following-sibling::p)[last()]"));
    }

    private void fillLastInstallmentDetailInput(String inputId, String value) {
        WebElement field = driverTools.waitForElementToAppear(getLastInstallmentDetailInput(inputId), 10)
                .orElseThrow(() -> new NoSuchElementException("Installment detail input not found: " + inputId));
        driverTools.scrollElementToCenter(field);
        driverTools.click(field);
        driverTools.clearInput(field);
        driverTools.sendKeys(field, value);
    }

    public void setAddDuration(String value) {
        fillLastInstallmentDetailInput("duration", value);
    }

    public void setForFinanceValue(String value) {
        fillLastInstallmentDetailInput("for-finance-in-", value);
    }

    public void setForBankValue(String value) {
        fillLastInstallmentDetailInput("for-bank-in-", value);
    }

    public void setMinimumPayment(String value) {
        fillLastInstallmentDetailInput("min-payment-idr", value);
    }

    public void setReconcileRate(String value) {
        fillLastInstallmentDetailInput("recon-rate", value);
    }

    public void selectDateOnCalendar(boolean isStartDate, String day, String month, String year, int maxIter) {
        Locator dateField = isStartDate ? START_DATE_CREATE_INSTALLMENT : END_DATE_CREATE_INSTALLMENT;
        WebElement dateElement = driverTools.waitForElementToAppear(dateField, 10)
                .orElseThrow(() -> new NoSuchElementException("Date field not found: " + dateField));
        openDatePickerAndSelectDate(dateElement, day, month, year, maxIter);
    }

    public void selectEndDateOnEditCalendar(String day, String month, String year, int maxIter) {
        WebElement dateElement = driverTools.waitForElementToAppear(END_DATE_EDIT_INSTALLMENT, 10)
                .orElseThrow(() -> new NoSuchElementException("Edit end date field not found: " + END_DATE_EDIT_INSTALLMENT));
        openDatePickerAndSelectDate(dateElement, day, month, year, maxIter);
    }

    private void openDatePickerAndSelectDate(WebElement dateElement, String day, String month, String year, int maxIter) {
        driverTools.scrollElementToCenter(dateElement);
        driverTools.click(dateElement);

        String currentDate = driverTools.getText(CURRENT_MONTH_CALENDAR).orElseThrow(); // MMMM yyyy
        logger.log("Comparing current month "+currentDate.split(" ")[0]+" with expected month "+month);
        logger.log("Comparing current year "+currentDate.split(" ")[1]+" with expected year "+year);
        while (!currentDate.split(" ")[0].toLowerCase().equals(month.toLowerCase()) || !currentDate.split(" ")[1].toLowerCase().equals(year.toLowerCase()) || maxIter == 0) {
            if (Integer.parseInt(currentDate.split(" ")[1]) == Integer.parseInt(year)) {
                if (CoreFunctions.getMonthNumber(currentDate.split(" ")[0]) < CoreFunctions.getMonthNumber(month)) {
                    driverTools.click(NEXT_MONTH_CALENDAR);
                } else {
                    driverTools.click(PREVIOUS_MONTH_CALENDAR);
                }
            } else {
                if (Integer.parseInt(currentDate.split(" ")[1]) < Integer.parseInt(year)) {
                    driverTools.click(NEXT_MONTH_CALENDAR);
                } else {
                    driverTools.click(PREVIOUS_MONTH_CALENDAR);
                }
            }
            currentDate = driverTools.getText(CURRENT_MONTH_CALENDAR).orElseThrow();
            maxIter--;
        }
        driverTools.click(getCalendarDate(day, month, year));
    }

    public void saveInstallment(String popupButton) {
        driverTools.scrollToElement(getButtonByText("Save"));
        driverTools.click(getButtonByText("Save"));
        try {
            driverTools.click(getButtonByText(popupButton));
        } catch (ElementClickInterceptedException e) {
            driverTools.click(getButtonByText(popupButton));
        }
    }

    public void filterSearchByInstallmentList(String filterName, String filterValue) {
        selectSearchByFilter(filterName);
        driverTools.waitForElementToPresence(INPUT_FILTER_SEARCHBY, 10)
                .orElseThrow(() -> new NoSuchElementException("Element not found " + INPUT_FILTER_SEARCHBY));
        driverTools.sendKeysWithJsExecutorForceInputValueAndTriggerEvents(INPUT_FILTER_SEARCHBY, filterValue);
        WebElement searchInput = driverTools.waitForElementToPresence(INPUT_FILTER_SEARCHBY, 5).orElseThrow();
        String actualValue = driverTools.getValue(searchInput);
        logger.log("Search filter value set. expected=" + filterValue + ", actual=" + actualValue);
    }

    private void selectSearchByFilter(String filterName) {
        Locator searchByInput = dropdownFilter("Search By");
        driverTools.scrollToElement(driverTools.waitForElementToAppear(searchByInput, 10)
                .orElseThrow(() -> new NoSuchElementException("Search By dropdown not found")));

        Locator chevronLocator = getFilterDropdownChevron("Search By");
        if (driverTools.element(chevronLocator).isPresent()) {
            driverTools.click(chevronLocator);
        } else {
            driverTools.click(searchByInput);
        }
        driverTools.waitForElementToAppear(DROPDOWN_OPTIONS_CONTAINER, 10);

        String dropdownItemId = filterSearchByToTestId.getOrDefault(filterName, filterName.replaceAll("\\s+", "_"));
        Locator filterOptionLocator = getDropdownItem(dropdownItemId);
        driverTools.click(filterOptionLocator);
        driverTools.waitForElementToDisappear(filterOptionLocator, 3);
    }

    private String getTableCellText(WebElement cell) {
        String text = driverTools.getText(cell);
        if (text != null && !text.trim().isEmpty()) {
            return text.trim();
        }

        List<WebElement> textElements = cell.findElements(By.xpath(".//p[contains(@class,'HcPVsG_text')] | .//span[contains(@class,'HcPVsG_text')]"));
        String joinedText = textElements.stream()
                .map(driverTools::getText)
                .filter(value -> value != null && !value.trim().isEmpty())
                .reduce((left, right) -> left + right)
                .orElse("");
        if (!joinedText.trim().isEmpty()) {
            return joinedText.trim();
        }

        Object textContent = ((JavascriptExecutor) driver).executeScript("return arguments[0].textContent;", cell);
        return textContent == null ? "" : textContent.toString().trim();
    }
    
    public void filterDropdownInstallmentReport(String filterName, String dropdownItem) {
        driverTools.click(getFilterInstallmentReport(filterName));
        driverTools.click(getDropdownItem(dropdownItem));
        logger.log("Select dropdown item: "+dropdownItem+", on filter: "+filterName);
    }

    /**
     * Select calendar on Installment Report filter
     *
     * @param filterName whether start date or end date
     * @param date date format "d MMMM yyyy"
     */
    public void filterDateInstallmentReport(String filterName, String date, int maxIter) {
        driverTools.click(getFilterDateInstallmentReport(filterName));

        String targetMonth = date.split(" ")[1];
        String targetYear = date.split(" ")[2];
        boolean targetFound = false;

        while (maxIter > 0) {
            String leftHeading = driverTools.getText(driverTools.waitForElementToAppear(MONTH_HEADING_REPORT_LEFT).orElseThrow());
            String rightHeading = driverTools.getText(driverTools.waitForElementToAppear(MONTH_HEADING_REPORT_RIGHT).orElseThrow());

            String leftMonth = leftHeading.split(" ")[0];
            String leftYear = leftHeading.split(" ")[1];
            String rightMonth = rightHeading.split(" ")[0];
            String rightYear = rightHeading.split(" ")[1];

            boolean targetInLeft = leftMonth.equalsIgnoreCase(targetMonth) && leftYear.equals(targetYear);
            boolean targetInRight = rightMonth.equalsIgnoreCase(targetMonth) && rightYear.equals(targetYear);

            if (targetInLeft || targetInRight) {
                targetFound = true;
                break;
            }

            int targetMonthNum = CoreFunctions.getMonthNumber(targetMonth);
            int leftMonthNum = CoreFunctions.getMonthNumber(leftMonth);

            if (Integer.parseInt(targetYear) < Integer.parseInt(leftYear)
                    || (Integer.parseInt(targetYear) == Integer.parseInt(leftYear) && targetMonthNum < leftMonthNum)) {
                driverTools.click(PREV_DATE_REPORT);
            } else {
                driverTools.click(NEXT_DATE_REPORT);
            }

            maxIter--;
        }

        if (!targetFound) {
            throw new NoSuchElementException("Target month " + targetMonth + " " + targetYear + " not reached within max iterations");
        }

        driverTools.clickWithJavascriptExecutor(getCalendarInstallmentReport(date));
        driverTools.click(getFilterDateInstallmentReport(filterName));
        logger.log("Select date " + date + " as " + filterName);
    }

    public void selectLastPageInstallmentReport() {
        driverTools.waitForElementToDisappear(INSTALLMENT_LIST_LOADING, 5);
        List<WebElement> pageList = driverTools.waitForElementsToAppear(PAGINATION_LIST).orElseThrow();
        driverTools.scrollToElement(FOOTER);
        driverTools.click(pageList.get(pageList.size()-1));
    }

    public String getRowValueFromColumn(String rowIndex, String columnName) {
        driverTools.waitForElementToDisappear(INSTALLMENT_LIST_LOADING, 10);
        WebElement cell = driverTools.waitForElementToPresence(getRowByColumn(rowIndex, columnName), 15).orElseThrow();
        return getTableCellText(cell);
    }

    public String getRowValueFromColumnReport(String rowIndex, String columnName) {
        WebElement cell = driverTools.waitForElementToPresence(getRowByColumnReport(rowIndex, columnName)).orElseThrow();
        return driverTools.getText(cell);
    }

    public void clickOnEditIcon(){
        clickOnLocator(getRowByColumn("0", "Action"));
        clickOnLocator(dropdownItem("Edit"));
    }

    public void scrollToConfigurationLog() {
        driverTools.waitForElementToPresence(CONFIGURATION_LOG_SECTION).ifPresentOrElse(driverTools::scrollToElement, () -> { throw new NoSuchElementException("Element not found for "+CONFIGURATION_LOG_SECTION); });
    }

    public void clickOnDeleteIcon() throws InterruptedException {
        clickOnLocator(getRowByColumn("0", "Action"));
        clickOnLocator(dropdownItem("Delete"));
        driverTools.scrollToElement(getButtonByText("Yes, delete"));
        clickButton("Yes, delete");
    }

    public String getCardSummaryData(String cardName) {
        WebElement card = driverTools.waitForElementToAppear(getCardSummary(cardName), 3).orElseThrow();
        return driverTools.getText(card);
    }

    public String getBankName(String configType, String dataBase, String collectionName, String queryString) {
        FindIterable<Document> result = TiketMongoDBManager.searchInMongoDB(configType, dataBase, collectionName, queryString);
        String bankName = "";
        for (Document document : result) {
            bankName = document.getString("bankName");
        }
        return bankName;
    }

    public Map<String, String> getInstallmentDetails(String configType, String dataBase, String collectionName, String queryString) {
        FindIterable<Document> result = TiketMongoDBManager.searchInMongoDB(configType, dataBase, collectionName, queryString);
        for (Document document : result) {
            Map<String, String> mongoData = new HashMap<>();
            mongoData.put( "bankName", document.getString("bankName"));
            return mongoData;
        }
        return null;
    }

    public int getRowCountOnTable() {
        List<WebElement> rows = driverTools.waitForElementsToAppear(ROW_ON_TABLE).orElseThrow();
        return rows.size();
    }

    public int getColumnIndexByColumnName(String columnName, List<List<String>> csvContent) {
        for (int i = 0; i < csvContent.get(0).size(); i++) {
            logger.log("column on index "+i+" is -> "+csvContent.get(0).get(i));
            if (csvContent.get(0).get(i).contains(columnName)) {
                return i;
            }
        }
        Assertion.skip("Column name -> "+columnName+", not found on CSV");
        return -1;
    }

    public int getTotalDataInstallmentReport() {
        String text = driverTools.getText(SHOWING_DATA_PER_PAGE).orElseThrow();
        Pattern pattern = Pattern.compile("\\d+$");
        Matcher matcher = pattern.matcher(text);
        if (!matcher.find()) return -1;
        return Integer.parseInt(matcher.group());
    }

    private Locator getConfigurationLogCell(String rowIndex, String columnName) {
        String columnId = configurationLogColumnIds.get(columnName);
        if (columnId == null) {
            throw new NoSuchElementException("Unknown configuration log column: " + columnName);
        }
        return new Locator("CONFIG_LOG_" + rowIndex + "_" + columnName.toUpperCase().replace(" ", "_"),
                By.xpath(CONFIGURATION_LOG_TABLE_SCOPE + "//*[@data-testid='row-cell-" + rowIndex + "-" + columnId + "']"));
    }

    public void waitForInstallmentListLoaded() {
        driverTools.waitForElementToDisappear(INSTALLMENT_LIST_LOADING, 15);
        driverTools.waitForElementToAppear(ROW_ON_TABLE, 20);
    }

    public String getFirstRowBankName() {
        return getNormalizedRowValue("0", "Bank Name");
    }

    public String getFirstRowCardName() {
        return getNormalizedRowValue("0", "Card Name");
    }

    public String getFirstRowPromoTitle() {
        return getNormalizedRowValue("0", "Promo Title");
    }

    public String getFirstRowBin() {
        return getNormalizedRowValue("0", "BIN");
    }

    public void searchInstallment(String filterName, String filterValue) {
        filterSearchByInstallmentList(filterName, filterValue);
        clickButton("Search");
        driverTools.waitForElementToDisappear(INSTALLMENT_LIST_LOADING, 15);
        if (!isInstallmentListEmpty()) {
            driverTools.waitForElementToAppear(ROW_ON_TABLE, 10);
        }
    }

    public void resetInstallmentFilter() {
        clickButton("Reset Filter");
        driverTools.waitForElementToDisappear(INSTALLMENT_LIST_LOADING, 10);
    }

    public boolean isInstallmentListEmpty() {
        driverTools.waitForElementToDisappear(INSTALLMENT_LIST_LOADING, 5);
        return driverTools.waitForElementToPresence(INSTALLMENT_LIST_EMPTY, 3).isPresent();
    }

    public String getNormalizedRowValue(String rowIndex, String columnName) {
        return normalizeTableCellText(getRowValueFromColumn(rowIndex, columnName));
    }

    public boolean rowValueContains(String rowIndex, String columnName, String expected) {
        return removeAllWhitespace(getNormalizedRowValue(rowIndex, columnName))
                .contains(removeAllWhitespace(expected));
    }

    public boolean rowValueContainsIgnoringCase(String rowIndex, String columnName, String expected) {
        return removeAllWhitespace(getNormalizedRowValue(rowIndex, columnName))
                .toLowerCase()
                .contains(removeAllWhitespace(expected).toLowerCase());
    }

    private String normalizeTableCellText(String text) {
        if (text == null) {
            return "";
        }
        return text.replaceAll("\\R", "").replaceAll(" +", " ").trim();
    }

    private String removeAllWhitespace(String value) {
        return value == null ? "" : value.replaceAll("\\s+", "");
    }

    private Locator getBinModalRowBinText(int rowIndex) {
        return new Locator("BIN_MODAL_ROW_BIN_TEXT_" + rowIndex, By.xpath(
                "(//label[contains(@class,'OHu2-q_check')])[" + rowIndex + "]"
                        + "/ancestor::div[contains(@class,'grid')][1]"
                        + "//div[contains(@class,'col-span-4')]//p[contains(@class,'HcPVsG_text')]"));
    }

    private String getBinValueForModalRow(int rowIndex) {
        WebElement binText = driverTools.waitForElementToAppear(getBinModalRowBinText(rowIndex), 5)
                .orElseThrow(() -> new NoSuchElementException("BIN value not found for row " + rowIndex));
        String binValue = driverTools.getText(binText);
        return binValue == null ? "" : binValue.trim();
    }

    private Locator getBinModalRowLabel(int rowIndex) {
        return new Locator("BIN_MODAL_ROW_LABEL_" + rowIndex, By.xpath("(//label[contains(@class,'OHu2-q_check')])[" + rowIndex + "]"));
    }

    private Locator getBinModalRowCheckbox(int rowIndex) {
        return new Locator("BIN_MODAL_ROW_CHECKBOX_" + rowIndex, By.xpath("(//label[contains(@class,'OHu2-q_check')]//input[@type='checkbox' and contains(@class,'OHu2-q_checkbox')])[" + rowIndex + "]"));
    }

    private Locator getBinModalRowCustomCheckbox(int rowIndex) {
        return new Locator("BIN_MODAL_ROW_CUSTOM_CHECKBOX_" + rowIndex, By.xpath("(//label[contains(@class,'OHu2-q_check')]//span[contains(@class,'OHu2-q_custom_checkbox')])[" + rowIndex + "]"));
    }

    private void selectBinModalRow(int rowIndex) {
        WebElement checkbox = driverTools.waitForElementToPresence(getBinModalRowCheckbox(rowIndex), 15)
                .orElseThrow(() -> new NoSuchElementException("BIN modal checkbox row " + rowIndex + " not found"));
        if (!checkbox.isSelected()) {
            WebElement clickTarget = driverTools.waitForElementToPresence(getBinModalRowCustomCheckbox(rowIndex), 5)
                    .or(() -> driverTools.waitForElementToPresence(getBinModalRowLabel(rowIndex), 5))
                    .orElse(checkbox);
            driverTools.scrollElementToCenter(clickTarget);
            driverTools.clickWithJavascriptExecutor(clickTarget);
        }
    }

    public void openBinSelectionModal() {
        WebElement binField = driverTools.waitForElementToAppear(BIN_TEXT_BOX, 5).orElseThrow(() -> new NoSuchElementException("BIN field not found"));
        driverTools.scrollToElement(binField);
        driverTools.clickWithJavascriptExecutor(binField);
        driverTools.waitForElementToAppear(BIN_MODAL_INPUT_BIN, 15);
        clickButton("Search");
        driverTools.waitForElementToAppear(BIN_MODAL_ROW_LABEL, 15);
    }

    public void clickBinModalSaveButton() {
        WebElement saveButton = driverTools.waitForElementToAppear(BIN_MODAL_SAVE_BUTTON, 10)
                .orElseThrow(() -> new NoSuchElementException("BIN modal Save button not found"));
        driverTools.scrollElementToCenter(saveButton);
        driverTools.clickWithJavascriptExecutor(saveButton);
    }

    public List<String> selectFirstTwoBinsInModal() {
        openBinSelectionModal();
        List<String> selectedBins = new ArrayList<>();
        int min = CoreFunctions.generateRandomInt(10, 100);
        logger.log("min value: " + min);
        int i = 2;
        while (i>1){
            WebElement customCheckbox = driverTools.waitForElementToAppear(getBinModalRowCustomCheckbox(min), 5).orElseThrow();
            driverTools.scrollElementToCenter(customCheckbox);
            driverTools.clickWithJavascriptExecutor(customCheckbox);
            selectedBins.add(getBinValueForModalRow(min));
            min += 2;
            i--;
        }

        clickBinModalSaveButton();
        return selectedBins;
    }

    public void clickAddBinIcon() {
        openBinSelectionModal();
    }

    public void selectAcquirer(String acquirer) {
        driverTools.scrollIntoWebView(dropdownFilter("Acquirer"));
        clickOnLocator(dropdownFilter("Acquirer"));
        clickOnLocator(dropdownItem(acquirer));
    }

    public void setTotalInterest(String expectedValue) {
        WebElement totalInterestDisplay = driverTools.waitForElementToAppear(getLastTotalInterestDisplay(), 10)
                .orElseThrow(() -> new NoSuchElementException("Total Interest display not found"));
        String expected = expectedValue == null ? "" : expectedValue.trim();
        String actual = Optional.ofNullable(driverTools.getText(totalInterestDisplay)).orElse("").trim();
        if (!actual.equals(expected)) {
            driverTools.dummyWait(1);
            actual = Optional.ofNullable(driverTools.getText(totalInterestDisplay)).orElse("").trim();
        }
        if (!actual.equals(expected)) {
            throw new AssertionError("Total Interest mismatch. expected=" + expected + ", actual=" + actual);
        }
        logger.log("Total Interest on last duration row -> expected: " + expected + ", actual: " + actual);
    }

    public void configureDuration(String duration, String totalInterest, String financeValue, String bankValue, String minimumPayment, String reconcileRate) {
        setAddDuration(duration);
        setForFinanceValue(financeValue);
        setForBankValue(bankValue);
        setTotalInterest(totalInterest);
        setMinimumPayment(minimumPayment);
        setReconcileRate(reconcileRate);
    }

    public List<InstallmentDurationDetail> getConfiguredDurationDetails() {
        List<WebElement> gridRows = driverTools.waitForElementsToAppear(INSTALLMENT_DETAIL_GRID_ROWS, 10).orElseThrow();
        List<InstallmentDurationDetail> details = new ArrayList<>();
        for (WebElement gridRow : gridRows) {
            details.add(readDurationDetailFromGridRow(gridRow));
        }
        logger.log("Captured configured duration details: " + details);
        return details;
    }

    public List<InstallmentDurationDetail> getMonthDetailsRows() {
        driverTools.waitForElementToAppear(MONTH_DETAILS_SECTION, 10);
        List<WebElement> rows = driverTools.waitForElementsToAppear(MONTH_DETAILS_ROWS, 10).orElse(List.of());
        List<InstallmentDurationDetail> details = new ArrayList<>();
        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.xpath("./td"));
            if (cells.size() >= 6) {
                details.add(new InstallmentDurationDetail(
                        textOrEmpty(cells.get(0)),
                        textOrEmpty(cells.get(1)),
                        textOrEmpty(cells.get(2)),
                        textOrEmpty(cells.get(3)),
                        textOrEmpty(cells.get(4)),
                        textOrEmpty(cells.get(5))
                ));
            } else {
                details.add(parseMonthDetailsFromRowText(driverTools.getText(row)));
            }
        }
        logger.log("Month details rows: " + details);
        return details;
    }

    private String textOrEmpty(WebElement element) {
        String text = driverTools.getText(element);
        return text == null ? "" : text.trim();
    }

    public boolean monthDetailsMatchConfiguredValues(List<InstallmentDurationDetail> configuredDetails) {
        List<InstallmentDurationDetail> monthDetails = getMonthDetailsRows();
        if (monthDetails.size() < configuredDetails.size()) {
            logger.log("Month details row count mismatch. expected at least " + configuredDetails.size() + ", found " + monthDetails.size());
            return false;
        }
        for (int index = 0; index < configuredDetails.size(); index++) {
            InstallmentDurationDetail configured = configuredDetails.get(index);
            InstallmentDurationDetail actual = monthDetails.get(index);
            if (!configured.matchesActual(actual)) {
                logger.log("Month details mismatch at index " + index + ". configured=" + configured + ", actual=" + actual);
                return false;
            }
        }
        return true;
    }

    private InstallmentDurationDetail readDurationDetailFromGridRow(WebElement gridRow) {
        WebElement durationInput = gridRow.findElement(By.xpath(".//input[@id='duration']"));
        WebElement totalInterestText = gridRow.findElement(By.xpath(".//p[contains(normalize-space(),'Total Interest')]/following-sibling::p"));
        WebElement financeInput = gridRow.findElement(By.xpath(".//input[@id='for-finance-in-']"));
        WebElement bankInput = gridRow.findElement(By.xpath(".//input[@id='for-bank-in-']"));
        WebElement minPaymentInput = gridRow.findElement(By.xpath(".//input[@id='min-payment-idr']"));
        WebElement reconcileInput = gridRow.findElement(By.xpath(".//input[@id='recon-rate']"));

        return new InstallmentDurationDetail(
                driverTools.getValue(durationInput).trim(),
                driverTools.getText(totalInterestText).trim(),
                driverTools.getValue(financeInput).trim(),
                driverTools.getValue(bankInput).trim(),
                driverTools.getValue(minPaymentInput).trim(),
                driverTools.getValue(reconcileInput).trim()
        );
    }

    private InstallmentDurationDetail parseMonthDetailsFromRowText(String rowText) {
        String normalized = rowText == null ? "" : rowText.replaceAll("\\s+", " ").trim();
        String[] parts = normalized.split(" ");
        if (parts.length >= 6) {
            return new InstallmentDurationDetail(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
        }
        return new InstallmentDurationDetail(normalized, "", "", "", "", "");
    }

    public void setInactiveMessageId(String value) {
        WebElement inactiveMessageId = driverTools.waitForElementToAppear(INACTIVE_MESSAGE_ID, 5).orElseThrow();
        driverTools.scrollToElement(inactiveMessageId);
        driverTools.clearInput(inactiveMessageId);
        driverTools.sendKeys(inactiveMessageId, value);
    }

    public void setInactiveMessageEn(String value) {
        WebElement inactiveMessageEn = driverTools.waitForElementToAppear(INACTIVE_MESSAGE_EN, 5).orElseThrow();
        driverTools.scrollToElement(inactiveMessageEn);
        driverTools.clearInput(inactiveMessageEn);
        driverTools.sendKeys(inactiveMessageEn, value);
    }

    public void expandFirstRow() {
        driverTools.waitForElementToAppear(FIRST_ROW_EXPAND_TOGGLE, 10).ifPresentOrElse(driverTools::click, () -> driverTools.click(getRowByColumn("0", "Bank Name")));
    }

    public String getExpandedRowContent() {
        return driverTools.waitForElementToAppear(EXPANDED_ROW_CONTENT, 10).map(driverTools::getText).orElse("").replace("\n", " ").trim();
    }

    public void clickActionOnFirstRow(String action) {
        clickOnLocator(getRowByColumn("0", "Action"));
        clickOnLocator(dropdownItem(action));
    }

    public void clickEdit(){
        clickOnLocator(getRowByColumn("0", "Action"));
        driverTools.waitForElementToPresence(EDIT, 10);
        driverTools.clickWithJavascriptExecutor(EDIT);
    }

    public String getSnackbarText() {
        return driverTools.waitForElementToAppear(SNACKBAR_TEXT, 10).map(driverTools::getText).orElse("").trim();
    }

    public String getConfigurationLogValue(String rowIndex, String columnName) {
        return driverTools.waitForElementToAppear(getConfigurationLogCell(rowIndex, columnName), 10)
                .map(driverTools::getText)
                .orElse("")
                .trim();
    }

    public int getConfigurationLogRowCount() {
        List<WebElement> rows = driverTools.waitForElementsToAppear(CONFIGURATION_LOG_ROWS, 10).orElse(List.of());
        return rows.size();
    }

    public String getLatestConfigurationLogRowIndex() {
        if (getConfigurationLogRowCount() == 0) {
            throw new NoSuchElementException("Configuration log has no rows");
        }
        return "0";
    }

    public String getCreateConfigurationLogRowIndex() {
        int rowCount = getConfigurationLogRowCount();
        if (rowCount == 0) {
            throw new NoSuchElementException("Configuration log has no rows");
        }
        return String.valueOf(rowCount - 1);
    }

    public String getLatestConfigurationLogValue(String columnName) {
        return getConfigurationLogValue(getLatestConfigurationLogRowIndex(), columnName);
    }

    public String getCreateConfigurationLogValue(String columnName) {
        return getConfigurationLogValue(getCreateConfigurationLogRowIndex(), columnName);
    }

    public Document getInstallmentDocument(String configType, String database, String collectionName, String queryString) {
        FindIterable<Document> result = TiketMongoDBManager.searchInMongoDB(configType, database, collectionName, queryString);
        for (Document document : result) {
            return document;
        }
        return null;
    }

    public void exportInstallmentReport() {
        clickButton("Installment Report");
        driverTools.waitForElementToAppear(INSTALLMENT_REPORT_TITLE, 10);
        clickButton("Export to Excel");
    }
}
