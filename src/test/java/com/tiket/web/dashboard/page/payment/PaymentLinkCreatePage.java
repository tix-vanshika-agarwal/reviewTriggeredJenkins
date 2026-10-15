package com.tiket.web.dashboard.page.payment;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static org.hamcrest.core.Is.is;

public class PaymentLinkCreatePage<T extends WebDriver> extends BasePage<T> {

    private static final String FORM = "//form[@id='payment-link-management-form']";
    private static final String PREFERENCES_SECTION = FORM + "//p[normalize-space()='Preferences Settings']/ancestor::div[contains(@class,'card_container')]";
    private static final String USAGE_SECTION = FORM + "//p[normalize-space()='Usage Settings']/ancestor::div[contains(@class,'mt-4')][1]";

    private static final Locator PAYMENT_LINK_FORM = new Locator("PAYMENT_LINK_FORM", By.id("payment-link-management-form"));
    private static final Locator CLIENT_ID_DROPDOWN = new Locator("CLIENT_ID_DROPDOWN",
            By.xpath(FORM + "//input[@placeholder='Client ID']"));
    private static final Locator EXPIRY_TIME_INPUT = new Locator("EXPIRY_TIME_INPUT", By.id("expiry-time"));
    private static final Locator BUFFER_EXPIRY_TIME_INPUT = new Locator("BUFFER_EXPIRY_TIME_INPUT", By.id("buffer-expiry-time"));
    private static final String CURRENCY_DROPDOWN_WRAPPER = PREFERENCES_SECTION
            + "//input[@placeholder='Currency']/ancestor::div[contains(@class,'styles_dropdown_wrapper')]";
    private static final String LIMIT_SETTINGS_DROPDOWN_WRAPPER = USAGE_SECTION
            + "//input[@placeholder='Limit settings']/ancestor::div[contains(@class,'styles_dropdown_wrapper')]";
    private static final Locator CURRENCY_DROPDOWN = new Locator("CURRENCY_DROPDOWN",
            By.xpath(CURRENCY_DROPDOWN_WRAPPER + "//input[@placeholder='Currency']"));
    private static final Locator LIMIT_SETTINGS_DROPDOWN = new Locator("LIMIT_SETTINGS_DROPDOWN",
            By.xpath(LIMIT_SETTINGS_DROPDOWN_WRAPPER + "//input[@placeholder='Limit settings']"));
    private static final Locator MAX_NUMBER_OF_TIMES_INPUT = new Locator("MAX_NUMBER_OF_TIMES_INPUT", By.id("max-number-of-times"));
    private static final Locator PRODUCT_TITLE_EN_INPUT = new Locator("PRODUCT_TITLE_EN_INPUT",
            By.xpath(FORM + "//input[@placeholder='Title' and not(ancestor::div[@id='id-details'])]"));
    private static final Locator PRODUCT_DESCRIPTION_EN_INPUT = new Locator("PRODUCT_DESCRIPTION_EN_INPUT",
            By.xpath(FORM + "//input[@placeholder='Description' and not(ancestor::div[@id='id-details'])]"));
    private static final Locator PRODUCT_TITLE_ID_INPUT = new Locator("PRODUCT_TITLE_ID_INPUT",
            By.xpath(FORM + "//div[@id='id-details']//input[@placeholder='Title']"));
    private static final Locator PRODUCT_DESCRIPTION_ID_INPUT = new Locator("PRODUCT_DESCRIPTION_ID_INPUT",
            By.xpath(FORM + "//div[@id='id-details']//input[@placeholder='Description']"));
    private static final Locator ITEM_NAME_EN_INPUT = new Locator("ITEM_NAME_EN_INPUT", By.id("item-name-en"));
    private static final Locator ITEM_NAME_ID_INPUT = new Locator("ITEM_NAME_ID_INPUT", By.id("item-name-id"));
    private static final Locator UNIT_PRICE_INPUT = new Locator("UNIT_PRICE_INPUT", By.id("unit-price"));
    private static final Locator QUANTITY_INPUT = new Locator("QUANTITY_INPUT", By.id("qty"));
    private static final Locator SHOW_QTY_CHECKBOX = new Locator("SHOW_QTY_CHECKBOX",
            By.xpath(FORM + "//div[contains(@class,'n8SXEW_container')]//input[@type='checkbox']"));
    private static final Locator SHOW_QTY_TOGGLE_SWITCH = new Locator("SHOW_QTY_TOGGLE_SWITCH",
            By.xpath(FORM + "//div[contains(@class,'n8SXEW_container')]//div[contains(@class,'n8SXEW_toggle')]"));
    private static final Locator SUBTOTAL_VALUE = new Locator("SUBTOTAL_VALUE", By.id("subtotal"));
    private static final Locator TOTAL_AMOUNT_VALUE = new Locator("TOTAL_AMOUNT_VALUE", By.id("total-amount"));
    private static final Locator PAYMENT_METHOD_DROPDOWN = new Locator("PAYMENT_METHOD_DROPDOWN", By.id("payment-method"));
    private static final String PAYMENT_METHOD_DROPDOWN_WRAPPER = FORM
            + "//input[@id='payment-method']/ancestor::div[contains(@class,'styles_dropdown_wrapper')]";
    private static final Locator PAYMENT_METHOD_SEARCH_INPUT = new Locator("PAYMENT_METHOD_SEARCH_INPUT",
            By.xpath(PAYMENT_METHOD_DROPDOWN_WRAPPER + "//input[@data-testid='dropdown-menu-multiple-filter-input']"));
    private static final Locator PAYMENT_METHOD_DONE_BUTTON = new Locator("PAYMENT_METHOD_DONE_BUTTON",
            By.xpath(PAYMENT_METHOD_DROPDOWN_WRAPPER
                    + "//div[contains(@class,'styles_dropdown_content')]//button[normalize-space()='Done']"));
    private static final Locator SAVE_CREATE_BUTTON = new Locator("SAVE_CREATE_BUTTON",
            By.xpath("//button[@type='submit' and @form='payment-link-management-form']"));
    private static final Locator YES_CREATE_PAYMENT_LINK_BUTTON = new Locator("YES_CREATE_PAYMENT_LINK_BUTTON",
            By.xpath("//button[normalize-space()='Yes, create']"));
    private static final Locator PAYMENT_LINK_CREATED_MODAL = new Locator("PAYMENT_LINK_CREATED_MODAL",
            By.xpath("//section[contains(@class,'modal_popup')][.//h2[normalize-space()='Payment Link Created']]"));
    private static final Locator PAYMENT_LINK_CREATED_MODAL_URL = new Locator("PAYMENT_LINK_CREATED_MODAL_URL",
            By.xpath("//section[contains(@class,'modal_popup')][.//h2[normalize-space()='Payment Link Created']]"
                    + "//input[@id='payment-link']"));
    private static final Locator PAYMENT_LINK_CREATED_MODAL_DONE = new Locator("PAYMENT_LINK_CREATED_MODAL_DONE",
            By.xpath("//section[contains(@class,'modal_popup')][.//h2[normalize-space()='Payment Link Created']]"
                    + "//button[normalize-space()='Done']"));
    private static final String TYPE_DROPDOWN_WRAPPER = FORM
            + "//input[@placeholder='Type']/ancestor::div[contains(@class,'styles_dropdown_wrapper')]";
    private static final Locator TYPE_DROPDOWN = new Locator("TYPE_DROPDOWN",
            By.xpath(TYPE_DROPDOWN_WRAPPER + "//input[@placeholder='Type']"));
    private static final Locator TYPE_FIRST_DROPDOWN_OPTION = new Locator("TYPE_FIRST_DROPDOWN_OPTION",
            By.xpath(TYPE_DROPDOWN_WRAPPER
                    + "//div[contains(@class,'styles_dropdown_content')]//div[contains(@class,'styles_dropdown_option')][1]"));
    private static final String CLIENT_ID_DROPDOWN_WRAPPER = FORM
            + "//input[@placeholder='Client ID']/ancestor::div[contains(@class,'styles_dropdown_wrapper')]";
    private static final Locator CLIENT_ID_DROPDOWN_CONTENT = new Locator("CLIENT_ID_DROPDOWN_CONTENT",
            By.xpath(CLIENT_ID_DROPDOWN_WRAPPER + "//div[contains(@class,'styles_dropdown_content')]"));
    private static final Locator CLIENT_ID_FIRST_DROPDOWN_OPTION = new Locator("CLIENT_ID_FIRST_DROPDOWN_OPTION",
            By.xpath(CLIENT_ID_DROPDOWN_WRAPPER
                    + "//div[contains(@class,'styles_dropdown_content')]//div[contains(@class,'styles_dropdown_option')][1]"));

    private String selectedClientId;

    public PaymentLinkCreatePage(Session<T> session) {
        super(session);
    }

    public String getSelectedClientId() {
        return selectedClientId;
    }

    public void waitForCreatePage() {
        driverTools.waitForElementToAppear(PAYMENT_LINK_FORM, 20);
        driverTools.waitForElementToAppear(CLIENT_ID_DROPDOWN, 20);
    }

    public void createPaymentLink(PaymentLinkConfig config) {
        waitForCreatePage();
        String clientIdToSelect = (config.clientId != null && !config.clientId.isBlank()) ? config.clientId : "4";
        config.clientId = selectClientIdFromDropdown(clientIdToSelect);
        configurePreferenceSettings(config);
        configureUsageSettings(config);
        fillTextField(PRODUCT_TITLE_EN_INPUT, config.productTitleEn);
        fillTextField(PRODUCT_DESCRIPTION_EN_INPUT, config.productDescriptionEn);
        fillTextField(PRODUCT_TITLE_ID_INPUT, config.productTitleId);
        fillTextField(PRODUCT_DESCRIPTION_ID_INPUT, config.productDescriptionId);
        fillTextField(ITEM_NAME_EN_INPUT, config.itemNameEn);
        fillTextField(ITEM_NAME_ID_INPUT, config.itemNameId);
        fillNumericField(UNIT_PRICE_INPUT, String.valueOf(config.unitPrice));
        fillNumericField(QUANTITY_INPUT, String.valueOf(config.quantity));
        selectFirstTypeFromDropdown();
        enableShowQtyIfPresent();
        verifyCalculatedAmounts(config.unitPrice * config.quantity);
        selectPaymentMethod(config);

        boolean res = confirmSavePaymentLinkCreation();
        if (!res) driverTools.waitForElementToBeClickable(SAVE_CREATE_BUTTON, 10).ifPresent(driverTools::click);
        confirmSavePaymentLinkCreation();
    }

    private boolean confirmSavePaymentLinkCreation() {
        return driverTools.waitForElementToBeClickable(YES_CREATE_PAYMENT_LINK_BUTTON, 10).map(yesCreateButton -> {
            driverTools.scrollElementToCenter(yesCreateButton);
            try {
                driverTools.clickWithActions(yesCreateButton);
            } catch (Exception e) {
                yesCreateButton.click();
            }
            driverTools.waitForElementToDisappear(YES_CREATE_PAYMENT_LINK_BUTTON, 15);
            return true;
        }).orElse(false);
    }

    private void configurePreferenceSettings(PaymentLinkConfig config) {
        LocalDateTime expiryTime = normalizeDateTime(
                config.expiryTime != null ? config.expiryTime : LocalDateTime.now().plusDays(1));
        logger.log("Setting expiry time to: " + expiryTime);
        setDateTimeField(EXPIRY_TIME_INPUT, "expiry-time", expiryTime);
        config.expiryTime = expiryTime;

        LocalDateTime bufferExpiryTime = ensureBufferExpiryAfter(expiryTime, config.bufferExpiryTime);
        config.bufferExpiryTime = bufferExpiryTime;
        logger.log("Setting buffer expiry time to: " + bufferExpiryTime);
        setDateTimeField(BUFFER_EXPIRY_TIME_INPUT, "buffer-expiry-time", bufferExpiryTime);

        if (readDateTimeInputValue(EXPIRY_TIME_INPUT).equals(readDateTimeInputValue(BUFFER_EXPIRY_TIME_INPUT))) {
            bufferExpiryTime = expiryTime.plusDays(1);
            config.bufferExpiryTime = bufferExpiryTime;
            logger.log("Buffer still matches expiry, retrying with: " + bufferExpiryTime);
            setDateTimeField(BUFFER_EXPIRY_TIME_INPUT, "buffer-expiry-time", bufferExpiryTime);
        }
        dismissInvalidDatePopupIfPresent();
        logger.log("Expiry time field: " + readDateTimeInputValue(EXPIRY_TIME_INPUT)
                + ", Buffer expiry time field: " + readDateTimeInputValue(BUFFER_EXPIRY_TIME_INPUT));

        selectScopedDropdownValue(CURRENCY_DROPDOWN, CURRENCY_DROPDOWN_WRAPPER, config.currency, true);
    }

    private LocalDateTime normalizeDateTime(LocalDateTime dateTime) {
        return dateTime.withSecond(0).withNano(0);
    }

    private LocalDateTime ensureBufferExpiryAfter(LocalDateTime expiryTime, LocalDateTime bufferExpiryTime) {
        LocalDateTime minimumBuffer = normalizeDateTime(expiryTime.plusDays(1));
        if (bufferExpiryTime == null || !bufferExpiryTime.toLocalDate().isAfter(expiryTime.toLocalDate())) {
            return minimumBuffer;
        }
        if (!bufferExpiryTime.isAfter(expiryTime)) {
            return minimumBuffer;
        }
        return normalizeDateTime(bufferExpiryTime);
    }

    private String readDateTimeInputValue(Locator inputLocator) {
        String value = driverTools.getValueAttribute(inputLocator, "value");
        return value != null ? value.trim() : "";
    }

    private void dismissInvalidDatePopupIfPresent() {
        Locator okButton = new Locator("INVALID_DATE_OK", By.xpath("//button[normalize-space()='OK']"));
        driverTools.waitForElementToAppear(okButton, 2).ifPresent(driverTools::click);
    }

    private void configureUsageSettings(PaymentLinkConfig config) {
        selectLimitSettingsSetLimit();
        fillNumericField(MAX_NUMBER_OF_TIMES_INPUT, String.valueOf(config.maxNumberOfTimes));
    }

    public String captureGeneratedPaymentLinkUrl() {
        driverTools.waitForElementToAppear(PAYMENT_LINK_CREATED_MODAL, 20);
        driverTools.waitForElementToAppear(PAYMENT_LINK_CREATED_MODAL_URL, 10);
        String inputValue = driverTools.getValueAttribute(PAYMENT_LINK_CREATED_MODAL_URL, "value");
        if (inputValue == null || !inputValue.startsWith("http")) {
            throw new IllegalStateException("Payment link URL not found in creation modal");
        }
        logger.log("Created payment link URL: " + inputValue);
        dismissPaymentLinkCreatedModal();
        return inputValue.trim();
    }

    private void dismissPaymentLinkCreatedModal() {
        WebElement doneButton = driverTools.waitForElementToBeClickable(PAYMENT_LINK_CREATED_MODAL_DONE, 10)
                .orElseThrow(() -> new IllegalStateException("Payment Link Created modal Done button not found"));
        driverTools.scrollElementToCenter(doneButton);
        try {
            driverTools.clickWithActions(doneButton);
        } catch (Exception e) {
            doneButton.click();
        }
        driverTools.waitForElementToDisappear(PAYMENT_LINK_CREATED_MODAL, 10);
    }

    private void selectFirstTypeFromDropdown() {
        String currentType = readDropdownInputValue(TYPE_DROPDOWN);
        if (!currentType.isBlank()) {
            logger.log("Type already set to: " + currentType);
            return;
        }
        openDropdown(TYPE_DROPDOWN, TYPE_DROPDOWN_WRAPPER);
        WebElement firstOption = driverTools.waitForElementToAppear(TYPE_FIRST_DROPDOWN_OPTION, 10).orElseThrow();
        String optionLabel = readDropdownOptionLabel(firstOption);
        logger.log("Selecting Type dropdown option: " + optionLabel);
        driverTools.click(firstOption);
        driverTools.waitForElementToDisappear(getDropdownContentLocator(TYPE_DROPDOWN_WRAPPER), 5);
        logger.log("Type field value: " + readDropdownInputValue(TYPE_DROPDOWN));
    }

    public void verifyCalculatedAmounts(long expectedAmount) {
        String subtotalText = driverTools.getValueAttribute(SUBTOTAL_VALUE, "value");
        String totalText = driverTools.getValueAttribute(TOTAL_AMOUNT_VALUE, "value");
        long subtotal = Long.parseLong(CoreFunctions.removeNonDigit(subtotalText));
        long total = Long.parseLong(CoreFunctions.removeNonDigit(totalText));
        logger.log("Payment link subtotal: " + subtotal + ", total: " + total);
        Assertion.assertThat("Subtotal should match unit price x quantity", subtotal, is(expectedAmount));
        Assertion.assertThat("Total amount should match unit price x quantity", total, is(expectedAmount));
    }

    public String selectClientIdFromDropdown(String clientId) {
        driverTools.waitForElementToAppear(CLIENT_ID_DROPDOWN);
        if (isClientIdValueSet(clientId)) {
            selectedClientId = clientId;
            logger.log("Client ID already set to: " + clientId);
            return clientId;
        }

        for (int attempt = 1; attempt <= 3; attempt++) {
            logger.log("Selecting Client ID '" + clientId + "', attempt " + attempt);
            dismissOpenDropdown();
            openClientIdDropdown();
            if (clickClientIdDropdownOption(clientId)) {
                waitForClientIdValue(clientId);
                if (isClientIdValueSet(clientId)) {
                    selectedClientId = clientId;
                    logger.log("Client ID shown in form field: " + selectedClientId);
                    return selectedClientId;
                }
            }
            if (attempt == 3) {
                setClientIdInputValueWithJs(clientId);
                if (isClientIdValueSet(clientId)) {
                    selectedClientId = clientId;
                    logger.log("Client ID set via JS fallback: " + selectedClientId);
                    return selectedClientId;
                }
            }
            logger.log("Client ID selection attempt " + attempt + " failed, current value: " + readClientIdInputValue());
        }

        String actualValue = readClientIdInputValue();
        throw new IllegalStateException("Failed to select Client ID '" + clientId + "', form shows: '" + actualValue + "'");
    }

    private void openClientIdDropdown() {
        if (driverTools.isElementPresent(CLIENT_ID_DROPDOWN_CONTENT)) {
            return;
        }
        clickDropdownChevron(CLIENT_ID_DROPDOWN);
        driverTools.waitForElementToAppear(CLIENT_ID_DROPDOWN_CONTENT, 10);
        Locator firstOption = new Locator("CLIENT_ID_DROPDOWN_FIRST_OPTION",
                By.xpath(CLIENT_ID_DROPDOWN_WRAPPER
                        + "//div[contains(@class,'styles_dropdown_content')]"
                        + "//div[contains(@class,'styles_dropdown_option')][1]"));
        driverTools.waitForElementToAppear(firstOption, 5);
    }

    private boolean clickClientIdDropdownOption(String clientId) {
        String scope = CLIENT_ID_DROPDOWN_WRAPPER + "//div[contains(@class,'styles_dropdown_content')]";
        Locator optionLabel = new Locator("CLIENT_ID_OPTION_LABEL_" + clientId,
                By.xpath(scope + "//div[@data-testid='dropdown-item-" + clientId + "']"
                        + "//p[normalize-space()='" + clientId + "']"));
        Locator optionByTestId = new Locator("CLIENT_ID_OPTION_TESTID_" + clientId,
                By.xpath(scope + "//div[@data-testid='dropdown-item-" + clientId + "']"));

        return driverTools.waitForElementToAppear(optionLabel, 5)
                .map(label -> {
                    logger.log("Selecting Client ID dropdown option: " + clientId);
                    clickDropdownOptionWithMouseEvents(label);
                    return true;
                })
                .orElseGet(() -> driverTools.waitForElementToAppear(optionByTestId, 5).map(option -> {
                    logger.log("Selecting Client ID dropdown option via testid: " + clientId);
                    clickDropdownOptionWithMouseEvents(option);
                    return true;
                }).orElse(false));
    }

    private void clickDropdownOptionWithMouseEvents(WebElement option) {
        driverTools.scrollElementToCenter(option);
        ((JavascriptExecutor) driver).executeScript(
                "const el = arguments[0];"
                        + "el.scrollIntoView({block:'center'});"
                        + "['pointerdown','mousedown','mouseup','click'].forEach(type => {"
                        + "  el.dispatchEvent(new MouseEvent(type, {bubbles:true, cancelable:true, view:window}));"
                        + "});",
                option);

    }

    private void setClientIdInputValueWithJs(String clientId) {
        WebElement input = driverTools.waitForElementToAppear(CLIENT_ID_DROPDOWN).orElseThrow();
        ((JavascriptExecutor) driver).executeScript(
                "const input = arguments[0];"
                        + "const value = arguments[1];"
                        + "input.value = value;"
                        + "input.setAttribute('data-value', value);"
                        + "input.dispatchEvent(new Event('input', { bubbles: true }));"
                        + "input.dispatchEvent(new Event('change', { bubbles: true }));",
                input, clientId);

    }

    private boolean isClientIdValueSet(String expectedValue) {
        return expectedValue.equals(readClientIdInputValue());
    }

    public String selectFirstClientIdFromDropdown() {
        driverTools.waitForElementToAppear(CLIENT_ID_DROPDOWN);
        clickDropdownChevron(CLIENT_ID_DROPDOWN);
        driverTools.waitForElementToAppear(CLIENT_ID_DROPDOWN_CONTENT, 10);
        WebElement firstOption = driverTools.waitForElementToAppear(CLIENT_ID_FIRST_DROPDOWN_OPTION, 10).orElseThrow();
        String optionLabel = readDropdownOptionLabel(firstOption);
        logger.log("Selecting first Client ID dropdown option: " + optionLabel);
        driverTools.click(firstOption);
        driverTools.waitForElementToDisappear(CLIENT_ID_DROPDOWN_CONTENT, 5);

        selectedClientId = readClientIdInputValue();
        if (selectedClientId.isBlank()) {
            selectedClientId = optionLabel;
        }
        logger.log("Client ID shown in form field: " + selectedClientId);
        return selectedClientId;
    }

    private String readClientIdInputValue() {
        String value = driverTools.getValueAttribute(CLIENT_ID_DROPDOWN, "value");
        if (value != null && !value.isBlank()) {
            return value.trim();
        }
        String dataValue = driverTools.getValueAttribute(CLIENT_ID_DROPDOWN, "data-value");
        if (dataValue != null && !dataValue.isBlank()) {
            return dataValue.trim();
        }
        return driverTools.getText(CLIENT_ID_DROPDOWN).orElse("").trim();
    }

    private void waitForClientIdValue(String expectedValue) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(d -> isClientIdValueSet(expectedValue));
        } catch (Exception e) {
            logger.log("Client ID field still not showing '" + expectedValue + "', current: " + readClientIdInputValue());
        }
        driverTools.waitForElementToDisappear(CLIENT_ID_DROPDOWN_CONTENT, 5);
    }

    private String readDropdownOptionLabel(WebElement option) {
        String optionText = driverTools.findElementInElement(option, By.tagName("p")).getText().trim();
        if (!optionText.isBlank()) {
            return optionText;
        }
        String testId = option.getAttribute("data-testid");
        if (testId != null && testId.startsWith("dropdown-item-")) {
            return testId.substring("dropdown-item-".length());
        }
        return optionText;
    }

    private void selectLimitSettingsSetLimit() {
        String currentLimit = readDropdownInputValue(LIMIT_SETTINGS_DROPDOWN);
        if (currentLimit == null || !"Set Limit".equalsIgnoreCase(currentLimit.trim())) {
            selectScopedDropdownValue(LIMIT_SETTINGS_DROPDOWN, LIMIT_SETTINGS_DROPDOWN_WRAPPER, "Set Limit");
        }
    }

    private void enableShowQtyIfPresent() {
        if (!driverTools.isElementPresent(SHOW_QTY_CHECKBOX)) {
            return;
        }
        WebElement checkbox = driverTools.waitForElementToPresence(SHOW_QTY_CHECKBOX, 5).orElseThrow();
        if (checkbox.isSelected()) {
            return;
        }
        WebElement toggle = driverTools.waitForElementToAppear(SHOW_QTY_TOGGLE_SWITCH, 5).orElseThrow();
        driverTools.scrollElementToCenter(toggle);
        try {
            driverTools.clickWithActions(toggle);
        } catch (Exception e) {
            driverTools.clickWithJavascriptExecutor(toggle);
        }
    }

    private void setDateTimeField(Locator inputLocator, String inputId, LocalDateTime dateTime) {
        driverTools.waitForElementToAppear(inputLocator);
        openDateTimePicker(inputLocator, inputId);
        setTimeInPicker(inputId, dateTime);
        selectDateInPicker(inputId, dateTime);
        setTimeInPicker(inputId, dateTime);
        clickDatePickerDone(inputId);
        waitForDateTimeInputPopulated(inputLocator);
    }

    private void waitForDateTimeInputPopulated(Locator inputLocator) {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(d -> !readDateTimeInputValue(inputLocator).isBlank());
    }

    private String getOpenPickerScope(String inputId) {
        return "//input[@id='" + inputId + "']/ancestor::div[contains(@class,'DateTime_date_time_container')]"
                + "//div[@data-testid='date-time-container' and not(ancestor::*[@aria-hidden='true'])]";
    }

    private void openDateTimePicker(Locator inputLocator, String inputId) {
        dismissOpenDropdown();
        WebElement input = driverTools.waitForElementToAppear(inputLocator).orElseThrow();
        driverTools.scrollElementToCenter(input);

        Locator calendarIcon = new Locator("CALENDAR_ICON_" + inputId,
                By.xpath("//input[@id='" + inputId + "']/following-sibling::button[contains(@class,'clickable_icon')]"));

        try {
            driverTools.clickWithActions(input);
        } catch (Exception e) {
            driverTools.click(input);
        }
        if (isDatePickerOpen(inputId)) {
            return;
        }

        WebElement calendarButton = driverTools.waitForElementToBeClickable(calendarIcon, 5)
                .orElseGet(() -> driverTools.findElementInElement(input,
                        By.xpath("./following-sibling::button[contains(@class,'clickable_icon')]")));
        driverTools.scrollElementToCenter(calendarButton);
        try {
            driverTools.clickWithActions(calendarButton);
        } catch (Exception e) {
            logger.log("Actions click on calendar icon failed, using JS: " + e.getMessage());
            driverTools.clickWithJavascriptExecutor(calendarButton);
        }
        waitForDatePickerOpen(inputId);
    }

    private void dismissOpenDropdown() {
        Locator openDropdown = new Locator("OPEN_DROPDOWN_CONTENT",
                By.xpath("//div[contains(@class,'styles_dropdown_content')]"));
        if (driverTools.isElementPresent(openDropdown)) {
            driverTools.click(PAYMENT_LINK_FORM);
            driverTools.waitForElementToDisappear(openDropdown, 5);
        }
    }

    private boolean isDatePickerOpen(String inputId) {
        return driverTools.waitForElementToAppear(getDateTimeDoneButton(inputId), 2).isPresent();
    }

    private void waitForDatePickerOpen(String inputId) {
        Locator openPicker = new Locator("OPEN_DATE_PICKER_" + inputId,
                By.xpath(getOpenPickerScope(inputId)));
        driverTools.waitForElementToAppear(openPicker, 10);
        driverTools.waitForElementToAppear(getDateTimeDoneButton(inputId), 10);
    }

    private void selectDateInPicker(String inputId, LocalDateTime dateTime) {
        String ariaLabelPrefix = dateTime.format(DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.ENGLISH));
        Locator dayButton = getDayButtonLocator(inputId, ariaLabelPrefix);
        if (driverTools.waitForElementToAppear(dayButton, 3).isEmpty()) {
            Locator nextMonthButton = new Locator("DATE_PICKER_NEXT_MONTH_" + inputId,
                    By.xpath(getOpenPickerScope(inputId)
                            + "//button[contains(@class,'Ntzmpq_next_button') and not(@disabled)]"));
            driverTools.waitForElementToAppear(nextMonthButton, 5).ifPresent(monthButton -> {
                driverTools.scrollElementToCenter(monthButton);
                driverTools.clickWithJavascriptExecutor(monthButton);
            });
            driverTools.waitForElementToAppear(dayButton, 10);
        }
        clickVisibleDayButton(dayButton);
    }

    private void clickVisibleDayButton(Locator dayLocator) {
        WebElement dayElement = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(d -> driver.findElements(dayLocator.by()).stream()
                        .filter(WebElement::isDisplayed)
                        .findFirst()
                        .orElse(null));
        if (dayElement == null) {
            throw new IllegalStateException("Date not visible in picker: " + dayLocator);
        }
        driverTools.scrollElementToCenter(dayElement);
        driverTools.clickWithJavascriptExecutor(dayElement);
    }

    private Locator getDayButtonLocator(String inputId, String ariaLabelPrefix) {
        return new Locator("DATE_PICKER_DAY_" + inputId + "_" + ariaLabelPrefix,
                By.xpath(getOpenPickerScope(inputId)
                        + "//button[contains(@class,'_6kqF6q_day') and not(@disabled)]"
                        + "[.//span[contains(@aria-label,'" + ariaLabelPrefix + "')]]"));
    }

    private Locator getDateTimeDoneButton(String inputId) {
        return new Locator("DATE_TIME_DONE_" + inputId,
                By.xpath(getOpenPickerScope(inputId) + "//button[@data-testid='submit-date-time']"));
    }

    private void clickDatePickerDone(String inputId) {
        WebElement doneButton = driverTools.waitForElementToBeClickable(getDateTimeDoneButton(inputId), 10)
                .orElseThrow(() -> new IllegalStateException("Done button not found for: " + inputId));
        driverTools.scrollElementToCenter(doneButton);
        try {
            driverTools.clickWithActions(doneButton);
        } catch (Exception e) {
            doneButton.click();
        }
        driverTools.waitForElementToDisappear(getDateTimeDoneButton(inputId), 5);
    }

    private void setTimeInPicker(String inputId, LocalDateTime dateTime) {
        Locator hourInput = new Locator("DATE_PICKER_HOUR_" + inputId,
                By.xpath(getOpenPickerScope(inputId)
                        + "//div[contains(@class,'iQRpmW_time_segment')][1]//input[contains(@class,'iQRpmW_input')]"));
        Locator minuteInput = new Locator("DATE_PICKER_MINUTE_" + inputId,
                By.xpath(getOpenPickerScope(inputId)
                        + "//div[contains(@class,'iQRpmW_time_segment')][2]//input[contains(@class,'iQRpmW_input')]"));
        int hour24 = dateTime.getHour();
        int hour12 = hour24 % 12;
        if (hour12 == 0) {
            hour12 = 12;
        }
        fillPickerTimeInput(hourInput, String.format("%02d", hour12));
        fillPickerTimeInput(minuteInput, String.format("%02d", dateTime.getMinute()));
    }

    private void fillPickerTimeInput(Locator locator, String value) {
        WebElement input = driverTools.waitForElementToAppear(locator, 5).orElseThrow();
        driverTools.scrollElementToCenter(input);
        driverTools.click(input);
        driverTools.clearInput(locator);
        driverTools.sendKeysWithJsExecutorForceInputValueAndTriggerEvents(locator, value);
    }

    private void fillTextField(Locator locator, String value) {
        WebElement input = driverTools.waitForElementToAppear(locator).orElseThrow();
        driverTools.scrollElementToCenter(input);
        try {
            driverTools.clickWithActions(input);
        } catch (Exception e) {
            driverTools.clickWithJavascriptExecutor(input);
        }
        driverTools.sendKeysWithJsExecutorForceInputValueAndTriggerEvents(locator, value);
    }

    private void fillNumericField(Locator locator, String value) {
        fillTextField(locator, value);
    }

    private void selectPaymentMethod(PaymentLinkConfig config) {
        String paymentMethod = config.paymentMethod;
        if (paymentMethod == null || paymentMethod.isBlank()) {
            throw new IllegalStateException("paymentMethod is required");
        }
        if (isPaymentMethodAlreadySelected(config)) {
            logger.log("Payment method already selected: " + readDropdownInputValue(PAYMENT_METHOD_DROPDOWN));
            return;
        }

        openDropdown(PAYMENT_METHOD_DROPDOWN, PAYMENT_METHOD_DROPDOWN_WRAPPER);
        driverTools.waitForElementToAppear(PAYMENT_METHOD_SEARCH_INPUT, 10);
        fillTextField(PAYMENT_METHOD_SEARCH_INPUT, paymentMethod.toLowerCase());

        Locator paymentMethodOption = getPaymentMethodCheckboxOption(paymentMethod);
        WebElement option = driverTools.waitForElementToAppear(paymentMethodOption, 10).orElseThrow(
                () -> new IllegalStateException("Payment method option not found: " + paymentMethod));
        driverTools.scrollElementToCenter(option);
        try {
            driverTools.clickWithJavascriptExecutor(option);
        } catch (Exception e) {
            driverTools.click(option);
        }

        WebElement doneButton = driverTools.waitForElementToBeClickable(PAYMENT_METHOD_DONE_BUTTON, 10)
                .orElseThrow(() -> new IllegalStateException("Payment method Done button not found"));
        driverTools.scrollElementToCenter(doneButton);
        try {
            driverTools.clickWithActions(doneButton);
        } catch (Exception e) {
            doneButton.click();
        }

        driverTools.waitForElementToDisappear(getDropdownContentLocator(PAYMENT_METHOD_DROPDOWN_WRAPPER), 10);
        logger.log("Payment method field value: " + readDropdownInputValue(PAYMENT_METHOD_DROPDOWN));
    }

    private Locator getPaymentMethodCheckboxOption(String paymentMethod) {
        String normalizedPaymentMethod = paymentMethod.toUpperCase();
        return new Locator("PAYMENT_METHOD_OPTION_" + normalizedPaymentMethod,
                By.xpath(PAYMENT_METHOD_DROPDOWN_WRAPPER + "//div[contains(@class,'styles_dropdown_content')]"
                        + "//label[contains(@class,'OHu2-q_check')][.//span[translate(normalize-space(.),"
                        + "'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')='" + normalizedPaymentMethod + "']]"));
    }

    private boolean isPaymentMethodAlreadySelected(PaymentLinkConfig config) {
        String currentValue = readDropdownInputValue(PAYMENT_METHOD_DROPDOWN);
        if (currentValue.isBlank()) {
            return false;
        }
        return matchesPaymentMethodValue(currentValue, config.paymentMethod)
                || matchesPaymentMethodValue(currentValue, config.paymentMethodConsumer);
    }

    private boolean matchesPaymentMethodValue(String fieldValue, String candidate) {
        return candidate != null && !candidate.isBlank()
                && fieldValue.toLowerCase().contains(candidate.toLowerCase());
    }

    private void closeDropdown(Locator inputLocator, String wrapperXpath) {
        Locator dropdownContent = getDropdownContentLocator(wrapperXpath);
        if (!driverTools.isElementPresent(dropdownContent)) {
            return;
        }
        WebElement input = driverTools.waitForElementToAppear(inputLocator).orElseThrow();
        driverTools.scrollElementToCenter(input);
        try {
            driverTools.clickWithActions(input);
        } catch (Exception e) {
            driverTools.clickWithJavascriptExecutor(input);
        }
        driverTools.waitForElementToDisappear(dropdownContent, 5);
    }

    private void selectDropdownValue(Locator dropdownLocator, String value) {
        selectScopedDropdownValue(dropdownLocator, null, value);
    }

    private void selectScopedDropdownValue(Locator dropdownLocator, String wrapperXpath, String value) {
        selectScopedDropdownValue(dropdownLocator, wrapperXpath, value, false);
    }

    private void selectScopedDropdownValue(Locator dropdownLocator, String wrapperXpath, String value, boolean verifySelection) {
        driverTools.waitForElementToAppear(dropdownLocator);
        if (isDropdownValueSet(dropdownLocator, value)) {
            logger.log(dropdownLocator.name() + " already set to: " + value);
            return;
        }
        openDropdown(dropdownLocator, wrapperXpath);
        if (!trySelectDropdownOption(value, wrapperXpath)) {
            throw new IllegalStateException("Dropdown option not found: " + value + " for " + dropdownLocator.name());
        }
        Locator dropdownContent = getDropdownContentLocator(wrapperXpath);
        driverTools.waitForElementToDisappear(dropdownContent, 10);
        if (verifySelection) {
            waitForDropdownValue(dropdownLocator, value, 10);
            if (!isDropdownValueSet(dropdownLocator, value)) {
                logger.log("Retrying dropdown selection for " + dropdownLocator.name() + " with value: " + value);
                openDropdown(dropdownLocator, wrapperXpath);
                if (!trySelectDropdownOption(value, wrapperXpath)) {
                    throw new IllegalStateException("Dropdown option not found on retry: " + value);
                }
                driverTools.waitForElementToDisappear(dropdownContent, 10);
                waitForDropdownValue(dropdownLocator, value, 10);
            }
        }
        logger.log(dropdownLocator.name() + " selected value: " + readDropdownInputValue(dropdownLocator));
    }

    private boolean isDropdownValueSet(Locator dropdownLocator, String expectedValue) {
        String currentValue = readDropdownInputValue(dropdownLocator);
        if (expectedValue.equalsIgnoreCase(currentValue)) {
            return true;
        }
        String dataValue = driverTools.getValueAttribute(dropdownLocator, "data-value");
        return expectedValue.equalsIgnoreCase(dataValue != null ? dataValue.trim() : "");
    }

    private void waitForDropdownValue(Locator dropdownLocator, String expectedValue, int seconds) {
        new WebDriverWait(driver, Duration.ofSeconds(seconds))
                .until(d -> isDropdownValueSet(dropdownLocator, expectedValue));
    }

    private String readDropdownInputValue(Locator inputLocator) {
        String value = driverTools.getValueAttribute(inputLocator, "value");
        if (value != null && !value.isBlank()) {
            return value.trim();
        }
        String dataValue = driverTools.getValueAttribute(inputLocator, "data-value");
        return dataValue != null ? dataValue.trim() : "";
    }

    private Locator getDropdownContentLocator(String wrapperXpath) {
        String xpath = wrapperXpath != null
                ? wrapperXpath + "//div[contains(@class,'styles_dropdown_content')]"
                : "//div[contains(@class,'styles_dropdown_content')]";
        return new Locator("DROPDOWN_CONTENT", By.xpath(xpath));
    }

    private void openDropdown(Locator inputLocator, String wrapperXpath) {
        dismissOpenDropdown();
        WebElement input = driverTools.waitForElementToAppear(inputLocator).orElseThrow();
        driverTools.scrollElementToCenter(input);

        String scope = wrapperXpath != null ? wrapperXpath : "";
        Locator dropdownButton = new Locator("DROPDOWN_BUTTON_" + inputLocator.name(),
                By.xpath(scope + "//div[@data-testid='dropdown-btn-default']"));
        Locator chevron = new Locator("DROPDOWN_CHEVRON_" + inputLocator.name(),
                By.xpath(scope + "//button[contains(@class,'clickable_icon')]"));

        if (tryClickDropdownTrigger(dropdownButton)) {
            waitForDropdownOptions(wrapperXpath);
            return;
        }
        if (tryClickDropdownTrigger(chevron)) {
            waitForDropdownOptions(wrapperXpath);
            return;
        }
        try {
            driverTools.clickWithActions(input);
        } catch (Exception e) {
            driverTools.clickWithJavascriptExecutor(input);
        }
        waitForDropdownOptions(wrapperXpath);
    }

    private void waitForDropdownOptions(String wrapperXpath) {
        Locator dropdownContent = getDropdownContentLocator(wrapperXpath);
        driverTools.waitForElementToAppear(dropdownContent, 10);
        String scope = wrapperXpath != null
                ? wrapperXpath + "//div[contains(@class,'styles_dropdown_content')]"
                : "//div[contains(@class,'styles_dropdown_content')]";
        Locator firstOption = new Locator("DROPDOWN_FIRST_OPTION",
                By.xpath(scope + "//div[contains(@class,'styles_dropdown_option')]"));
        driverTools.waitForElementToAppear(firstOption, 5);
    }

    private boolean tryClickDropdownTrigger(Locator locator) {
        return driverTools.waitForElementToBeClickable(locator, 3).map(trigger -> {
            driverTools.scrollElementToCenter(trigger);
            try {
                driverTools.clickWithActions(trigger);
            } catch (Exception e) {
                driverTools.clickWithJavascriptExecutor(trigger);
            }
            return true;
        }).orElse(false);
    }

    private void clickDropdownChevron(Locator inputLocator) {
        WebElement input = driverTools.waitForElementToAppear(inputLocator).orElseThrow();
        WebElement chevron = driverTools.findElementInElement(input,
                By.xpath("./following-sibling::button[contains(@class,'clickable_icon')]"
                        + " | ./ancestor::div[contains(@class,'inline_input_wrapper')]//button[contains(@class,'clickable_icon')]"));
        driverTools.scrollElementToCenter(chevron);
        try {
            driverTools.clickWithActions(chevron);
        } catch (Exception e) {
            driverTools.clickWithJavascriptExecutor(chevron);
        }
    }

    private void selectDropdownOption(String value) {
        selectDropdownOption(value, null);
    }

    private void selectDropdownOption(String value, String wrapperXpath) {
        if (!trySelectDropdownOption(value, wrapperXpath)) {
            throw new IllegalStateException("Dropdown option not found: " + value);
        }
    }

    private boolean trySelectDropdownOption(String value, String wrapperXpath) {
        if (value == null || value.isBlank()) {
            return false;
        }
        String scope = wrapperXpath != null
                ? wrapperXpath + "//div[contains(@class,'styles_dropdown_content')]"
                : "//div[contains(@class,'styles_dropdown_content')]";

        Locator optionByTestId = new Locator("DROPDOWN_OPTION_TESTID_" + value,
                By.xpath(scope + "//*[@data-testid='dropdown-item-" + value + "']"));
        if (clickDropdownOption(optionByTestId)) {
            return true;
        }

        Locator optionByNormalizedTestId = new Locator("DROPDOWN_OPTION_NORMALIZED_" + value,
                By.xpath(scope + "//*[@data-testid='dropdown-item-"
                        + value.replaceAll("[^a-zA-Z0-9]+", "_") + "']"));
        if (clickDropdownOption(optionByNormalizedTestId)) {
            return true;
        }

        Locator optionByExactText = new Locator("DROPDOWN_OPTION_TEXT_" + value,
                By.xpath(scope + "//div[contains(@class,'styles_dropdown_option')]"
                        + "//p[normalize-space()='" + value + "']"));
        if (clickDropdownOption(optionByExactText)) {
            return true;
        }

        Locator optionByPartialText = new Locator("DROPDOWN_OPTION_PARTIAL_" + value,
                By.xpath(scope + "//div[contains(@class,'styles_dropdown_option')]"
                        + "//p[contains(normalize-space(),'" + value + "')]"));
        return clickDropdownOption(optionByPartialText);
    }

    private boolean clickDropdownOption(Locator optionLocator) {
        return driverTools.waitForElementToBeClickable(optionLocator, 5).map(option -> {
            driverTools.scrollElementToCenter(option);
            try {
                driverTools.clickWithActions(option);
            } catch (Exception e) {
                try {
                    driverTools.clickWithJavascriptExecutor(option);
                } catch (Exception jsException) {
                    driverTools.click(option);
                }
            }
            return true;
        }).orElse(false);
    }
}
