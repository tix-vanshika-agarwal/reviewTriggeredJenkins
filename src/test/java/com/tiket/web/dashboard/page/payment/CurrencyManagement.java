package com.tiket.web.dashboard.page.payment;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.bson.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.mongodb.client.FindIterable;
import com.tiket.dbclient.TiketMongoDBManager;
import com.tiket.model.Deeplink;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;

public class CurrencyManagement<T extends WebDriver> extends WebPage<T> {
    private static final String paymentPath = "/admin/payment_v2";
    private static final Locator LOADING = new Locator("LOADING", new By.ByXPath("//*[@data-testid='loading']"));
    private static final Locator NO_RECORDS_LIST = new Locator("NO_RECORDS_LIST", new By.ByXPath("//*[text()='There are no records to display']"));
    private static final Locator CURRENCY_LIST_TABLE = new Locator("CURRENCY_LIST_TABLE", new By.ByXPath("//*[contains(@class,'TableBody')]"));
    private static final Locator CURRENCY_CONFIG_SIDEMENU = new Locator("CURRENCY_CONFIG_SIDEMENU", new By.ByXPath("//*[@data-testid='sidemenu']//*[contains(text(),'Currency Configuration')]"));
    private static final Locator CURRENCY_MANAGEMENT_SUBMENU = new Locator("CURRENCY_MANAGEMENT_SUBMENU", new By.ByXPath("//*[contains(@data-testid,'submenu')]//*[contains(text(),'Currency Management')]"));
    private static final Locator TOTAL_DATA_LIST = new Locator("TOTAL_DATA_LIST", new By.ByXPath("//*[contains(@class,'Pagination')]//*[contains(text(),'of')]"));
    private static final Locator MESSAGE_CURRENCY_EN = new Locator("MESSAGE_CURRENCY_EN", new By.ByXPath("(//input[@id='input-new-message-currency-here'])[1]"));
    private static final Locator MESSAGE_CURRENCY_ID = new Locator("MESSAGE_CURRENCY_ID", new By.ByXPath("(//input[@id='input-new-message-currency-here'])[2]"));
    private static final Locator ADD_MESSAGE_CURRENCY = new Locator("ADD_MESSAGE_CURRENCY", new By.ByXPath("//*[@data-testid='input-hours']/ancestor::*[contains(@class,'text-left')]/following-sibling::*//*[contains(@class,'cursor-pointer')]"));
    private static final Locator SUBMITTED_MESSAGE_ID = new Locator("SUBMITTED_MESSAGE_ID", new By.ByXPath("(//div[contains(@class,'grid')]//div[contains(@class,'text-left') and not(.//input)])[last()]"));

    private static final Locator PAGE_TITLE = new Locator("PAGE_TITLE", new By.ByXPath("//h1[contains(@class,'page_title') and normalize-space(text())='Currency Management']"));
    private static final Locator CURRENCY_FILTER_INPUT = new Locator("CURRENCY_FILTER_INPUT", new By.ByCssSelector("input[placeholder='Currency Filter']"));
    private static final Locator STATUS_FILTER_INPUT = new Locator("STATUS_FILTER_INPUT", new By.ByCssSelector("input[placeholder='Status']"));
    private static final Locator GBV_FILTER_INPUT = new Locator("GBV_FILTER_INPUT", new By.ByCssSelector("input[placeholder='GBV Filter']"));
    private static final Locator SEARCH_CURRENCY_CODE_INPUT = new Locator("SEARCH_CURRENCY_CODE_INPUT", new By.ById("search-currency-code"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", new By.ByXPath("//button[normalize-space(text())='Search']"));
    private static final Locator RESET_FILTER_BUTTON = new Locator("RESET_FILTER_BUTTON", new By.ByXPath("//button[normalize-space(text())='Reset Filter']"));
    private static final Locator MESSAGE_BUTTON = new Locator("MESSAGE_BUTTON", new By.ByXPath("//button[normalize-space(text())='Message']"));
    private static final Locator SUGGESTED_CURRENCY_BUTTON = new Locator("SUGGESTED_CURRENCY_BUTTON", new By.ByXPath("//button[normalize-space(text())='Suggested Currency']"));
    private static final Locator ALL_CURRENCY_CODE_CELLS = new Locator("ALL_CURRENCY_CODE_CELLS", new By.ByXPath("//div[contains(@class,'rdt_TableRow')]//div[@data-column-id='1']//div[@data-tag='allowRowEvents']"));
    private static final Locator TOAST_MESSAGE = new Locator("TOAST_MESSAGE", new By.ByXPath("//*[contains(@class,'text_content')]"));
    private static final Locator CONFIRM_YES_BUTTON = new Locator("CONFIRM_YES_BUTTON", new By.ByXPath("//button[starts-with(normalize-space(text()),'Yes') or normalize-space(text())='OK' or normalize-space(text())='Confirm']"));

    private static final Locator VERTICAL_STATUS_DROPDOWN_INPUT = new Locator("VERTICAL_STATUS_DROPDOWN_INPUT", new By.ByCssSelector("input[placeholder='Vertical currency status']"));
    private static final Locator VERTICAL_STATUS_SAVE_BUTTON = new Locator("VERTICAL_STATUS_SAVE_BUTTON", new By.ByCssSelector("button[data-testid='btn-save']"));
    private static final Locator DROPDOWN_OPTIONS = new Locator("DROPDOWN_OPTIONS", new By.ByXPath("//*[contains(@data-testid,'dropdown-item-')]"));

    private static final Locator SUGGESTED_MODAL_TITLE = new Locator("SUGGESTED_MODAL_TITLE", new By.ByXPath("//h2[normalize-space(text())='Suggested Currency']"));
    private static final Locator SELECT_CURRENCIES_INPUT = new Locator("SELECT_CURRENCIES_INPUT", new By.ByCssSelector("input[placeholder='Select currencies']"));
    private static final Locator SUGGESTED_SELECTED_CHIPS = new Locator("SUGGESTED_SELECTED_CHIPS", new By.ByXPath("//button[contains(@class,'bg-blue-500')]//span[contains(@class,'text-left')]"));
    private static final Locator MAX_SUGGESTED_ERROR = new Locator("MAX_SUGGESTED_ERROR", new By.ByXPath("//h2[normalize-space(text())='Oops...']/ancestor::section//h1[contains(text(),'max 3 suggested currencies')]"));
    private static final Locator OK_BUTTON = new Locator("OK_BUTTON", new By.ByXPath("//button[normalize-space(text())='OK']"));
    private static final Locator MODAL_CLOSE_BUTTON = new Locator("MODAL_CLOSE_BUTTON", new By.ByXPath("//section[contains(@class,'modal_popup')]//button[contains(@class,'modal_close')]"));

    private Locator getRowByCode(String code) {
        return new Locator("CURRENCY_ROW_"+code.toUpperCase(), new By.ByXPath("//div[@role='row'][.//div[@data-column-id='1']//div[normalize-space(text())='"+code+"']]"));
    }
    private Locator getVerticalStatusLabel(String code) {
        return new Locator("VERTICAL_STATUS_LABEL_"+code.toUpperCase(), new By.ByXPath("//div[@role='row'][.//div[@data-column-id='1']//div[normalize-space(text())='"+code+"']]//div[@data-column-id='7']//div[contains(@class,'label_')]"));
    }
    private Locator getUserCurrencyToggle(String code) {
        return new Locator("USER_CURRENCY_TOGGLE_"+code.toUpperCase(), new By.ByXPath("//div[@role='row'][.//div[@data-column-id='1']//div[normalize-space(text())='"+code+"']]//div[@data-column-id='8']//label"));
    }
    private Locator getUserCurrencyCheckbox(String code) {
        return new Locator("USER_CURRENCY_CHECKBOX_"+code.toUpperCase(), new By.ByXPath("//div[@role='row'][.//div[@data-column-id='1']//div[normalize-space(text())='"+code+"']]//div[@data-column-id='8']//input[@type='checkbox']"));
    }
    private Locator getConsumerVisibleToggle(String code) {
        return new Locator("CONSUMER_VISIBLE_TOGGLE_"+code.toUpperCase(), new By.ByXPath("//div[@role='row'][.//div[@data-column-id='1']//div[normalize-space(text())='"+code+"']]//div[@data-column-id='9']//label"));
    }
    private Locator getConsumerVisibleCheckbox(String code) {
        return new Locator("CONSUMER_VISIBLE_CHECKBOX_"+code.toUpperCase(), new By.ByXPath("//div[@role='row'][.//div[@data-column-id='1']//div[normalize-space(text())='"+code+"']]//div[@data-column-id='9']//input[@type='checkbox']"));
    }
    private Locator getSuggestedChip(String code) {
        return new Locator("SUGGESTED_CHIP_"+code.toUpperCase(), new By.ByXPath("//button[contains(@class,'bg-blue-500') and .//span[normalize-space(text())='"+code+"']]"));
    }
    private Locator getSuggestedChipRemove(String code) {
        // Each chip is a button with the code label and a close (cross) svg; removal must click the cross icon.
        // SVG elements live in their own namespace, so XPath must match them via local-name().
        return new Locator("SUGGESTED_CHIP_REMOVE_"+code.toUpperCase(), new By.ByXPath("//button[contains(@class,'bg-blue-500') and .//span[normalize-space(text())='"+code+"']]//*[local-name()='svg']"));
    }

    public String getSubmittedMessageID() {
        return driverTools.getText(SUBMITTED_MESSAGE_ID)
                .orElseThrow(() -> new NoSuchElementException(
                        "Submitted message ID text not found"));
    }

    private Locator getFilterCurrency(String filterName) {
        return new Locator("CURRENCY_FILTER_"+filterName.toUpperCase(), new By.ByXPath("//*[text()='"+filterName+"']/preceding-sibling::input"));
    }
    private Locator getDropdownItemFilter(String itemName) {
        itemName = itemName.trim().replaceAll("\s+", "_");
        return new Locator("CURRENCY_FILTER_"+itemName.toUpperCase(), new By.ByXPath("//*[@data-testid='dropdown-item-"+itemName+"']"));
    }
    private Locator getButtonLocator(String button) {
        return new Locator("BUTTON_"+button.toUpperCase(), new By.ByXPath("//button[normalize-space(text())='"+button+"']"));
    }

    public CurrencyManagement(Session<T> session) {
        super(session);
    }

    public boolean isCurrencyConfigPageOpened() {
        if (!driverTools.waitForElementToDisappear(LOADING, 5)) {
            logger.log("Element not disappeared or even not appeared");
        }
        driverTools.waitForElementToDisappear(NO_RECORDS_LIST, 5);
        return driverTools.waitForElementToPresence(CURRENCY_LIST_TABLE, 3).isPresent();
    }

    public void openCurrencyConfigPage() {
        if (!isCurrencyConfigPageOpened()) {
            Deeplink deeplink = new Deeplink("ADMIN_CARE", "https://gatotkaca.tiket.com", "https://preprod.tiket.com", "https://tiket.com");
            String url = switch (Configs.ENVIRONMENT) {
                    case PROD -> deeplink.prodDeeplink()+paymentPath;
                    case PREPROD -> deeplink.preprodDeeplink()+paymentPath;
                    case GK -> deeplink.gkDeeplink()+paymentPath;
                    default -> deeplink.gkDeeplink()+paymentPath;
            };
            driverTools.get(url);
            if (!driverTools.getCurrentURL().contains(url)) Assertion.skip("Failed to open payment dashboard "+url);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            WebElement cardConfig = driverTools.waitForElementToPresence(CURRENCY_CONFIG_SIDEMENU).orElseThrow();
            js.executeScript("arguments[0].click();", cardConfig);
            WebElement bankConfig = driverTools.waitForElementToPresence(CURRENCY_MANAGEMENT_SUBMENU).orElseThrow();
            js.executeScript("arguments[0].click();", bankConfig);
            driverTools.waitForElementToDisappear(NO_RECORDS_LIST, 5);
            driverTools.waitForElementToPresence(CURRENCY_LIST_TABLE, 3);
        }
    }

    public void selectFilterinCurrencyList(String filterName, String itemName) {
        driverTools.click(getFilterCurrency(filterName));
        driverTools.click(getDropdownItemFilter(itemName));
    }

    /**
     * Selects an option from a currency-list filter dropdown by matching the rendered option text instead of the
     * data-testid suffix. Tolerant of label casing (the UI renders options upper-cased, e.g. ALL / ACTIVE / INACTIVE).
     */
    public void selectFilterByVisibleText(String filterName, String value) {
        // Scroll the trigger to the viewport center first; otherwise the sticky page header intercepts the click.
        driverTools.scrollAndClick(getFilterCurrency(filterName));
        clickVisibleDropdownOption(value);
    }

    /** Opens a filter dropdown, reads the available option labels, then closes it without selecting. */
    public List<String> getFilterOptions(String filterName) {
        driverTools.scrollAndClick(getFilterCurrency(filterName));
        List<String> options = getDropdownOptionTexts();
        // Close the dropdown again by clicking the same filter trigger.
        driverTools.scrollAndClick(getFilterCurrency(filterName));
        return options;
    }

    /** Applies a list filter and refreshes the listing so the displayed records reflect the selection. */
    public void applyListFilter(String filterName, String value) {
        selectFilterByVisibleText(filterName, value);
        clickButton("Search");
        driverTools.waitForElementToDisappear(LOADING, 10);
    }

    public void clearCurrencyCodeSearch() {
        searchCurrencyCode("");
    }

    /**
     * Clicks the currently-visible dropdown option whose text matches {@code value} case-insensitively. Dropdown
     * options for closed dropdowns remain in the DOM (hidden) and share data-testids across dropdowns, so we must
     * match on the visible option only. Retries briefly to absorb the option-render race after the dropdown opens.
     */
    private void clickVisibleDropdownOption(String value) {
        driverTools.waitForElementsToAppear(DROPDOWN_OPTIONS);
        String target = value.trim().toLowerCase();
        By optionBy = DROPDOWN_OPTIONS.by();
        for (int attempt = 0; attempt < 15; attempt++) {
            for (WebElement option : driver.findElements(optionBy)) {
                try {
                    if (!option.isDisplayed()) {
                        continue;
                    }
                    if (optionMatches(option.getText(), target)) {
                        driverTools.click(option);
                        return;
                    }
                } catch (StaleElementReferenceException ignored) {
                    // element re-rendered; re-find on next iteration
                }
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        throw new NoSuchElementException("No visible dropdown option matching '" + value + "'");
    }

    /**
     * Matches a rendered dropdown option against a target value (already lower-cased). Uses exact match or a
     * whole-token match so that, for example, "Inactive" does not accidentally match "Active" (substring) while
     * a currency option like "JPY - Japanese Yen" still matches the code "JPY".
     */
    private boolean optionMatches(String optionText, String target) {
        String text = optionText.trim().toLowerCase();
        if (text.isEmpty()) {
            return false;
        }
        if (text.equals(target)) {
            return true;
        }
        for (String token : text.split("[^a-z0-9]+")) {
            if (token.equals(target)) {
                return true;
            }
        }
        return false;
    }

    public void clickButton(String button) {
        driverTools.clickWithJavascriptExecutor(getButtonLocator(button));
        driverTools.waitForElementToDisappear(NO_RECORDS_LIST, 5);
    }

    public int getTotalDataList() {
        String totalData = driverTools.waitForElementToPresence(TOTAL_DATA_LIST).map(driverTools::getText).orElseThrow();
        Pattern p = Pattern.compile("(\\d+)$");
        Matcher m = p.matcher(totalData);
        if (m.find()) {
            totalData = m.group(1);
        } else {
            Assertion.skip(totalData+" not match with regex");
        }
        logger.log("total currency list data: "+totalData);
        return Integer.parseInt(totalData);
    }

    public int getNumberOfCurrencyMongoDB(String configType, String dataBase, String collectionName, String queryString) {
        FindIterable<Document> result = TiketMongoDBManager.searchInMongoDB(configType, dataBase, collectionName, queryString);
        int count = (int) result.spliterator().getExactSizeIfKnown();
        if (count == -1) {
            count = 0;
            for (Document doc : result) {
                count++;
            }
        }
        return count;
    }

    public void inputMessage(String messageId, String messageEn) {
        WebElement messageCurrencyEn = driverTools.waitForElementToAppear(MESSAGE_CURRENCY_EN)
                .orElseThrow(() -> new NoSuchElementException(MESSAGE_CURRENCY_EN + " not found"));
        driverTools.click(messageCurrencyEn);
        driverTools.sendKeys(messageCurrencyEn, messageEn);

        WebElement messageCurrencyId = driverTools.waitForElementToAppear(MESSAGE_CURRENCY_ID)
                .orElseThrow(() -> new NoSuchElementException(MESSAGE_CURRENCY_ID + " not found"));
        driverTools.click(messageCurrencyId);
        driverTools.sendKeys(messageCurrencyId, messageId);

        driverTools.click(ADD_MESSAGE_CURRENCY);
    }

    public boolean isPageComponentDisplayed(String component) {
        Locator locator = switch (component.trim().toLowerCase()) {
            case "page title", "title" -> PAGE_TITLE;
            case "currency filter" -> CURRENCY_FILTER_INPUT;
            case "status filter", "status" -> STATUS_FILTER_INPUT;
            case "gbv filter" -> GBV_FILTER_INPUT;
            case "currency code search", "search currency code" -> SEARCH_CURRENCY_CODE_INPUT;
            case "search button", "search" -> SEARCH_BUTTON;
            case "reset filter button", "reset filter" -> RESET_FILTER_BUTTON;
            case "message button", "message" -> MESSAGE_BUTTON;
            case "suggested currency button", "suggested currency" -> SUGGESTED_CURRENCY_BUTTON;
            case "currency listing table", "currency table", "table" -> CURRENCY_LIST_TABLE;
            default -> throw new IllegalArgumentException("Unknown component: "+component);
        };
        return driverTools.waitForElementToPresence(locator, 5).isPresent();
    }

    public void searchCurrencyCode(String code) {
        driverTools.clearInput(SEARCH_CURRENCY_CODE_INPUT);
        driverTools.sendKeys(SEARCH_CURRENCY_CODE_INPUT, code);
        clickButton("Search");
    }

    public List<String> getDisplayedCurrencyCodes() {
        if (driverTools.isElementPresent(NO_RECORDS_LIST)) {
            return List.of();
        }
        return driverTools.getTexts(ALL_CURRENCY_CODE_CELLS);
    }

    public void clickResetFilter() {
        clickButton("Reset Filter");
    }

    public String getSearchCurrencyCodeValue() {
        return driverTools.getValue(SEARCH_CURRENCY_CODE_INPUT);
    }

    public String getStatusFilterValue() {
        return driverTools.getValue(STATUS_FILTER_INPUT);
    }

    public String getGbvFilterValue() {
        return driverTools.getValue(GBV_FILTER_INPUT);
    }

    public String getVerticalCurrencyStatus(String code) {
        String raw = driverTools.getText(getVerticalStatusLabel(code))
                .orElseThrow(() -> new NoSuchElementException("Vertical status label not found for "+code));
        return raw.replaceAll("[^A-Za-z]", "").toUpperCase();
    }

    /**
     * Opens the vertical-status modal dropdown and returns the offered statuses as canonical, letters-only upper-case
     * tokens (e.g. {@code "ACTIVE"}, {@code "INACTIVE"}, {@code "REVIEW"}). Normalising here lets callers assert with an
     * exact match instead of a substring matcher - a substring check for "Active" would also match "Inactive".
     */
    public List<String> getAvailableVerticalStatuses(String code) {
        driverTools.clickWithJavascriptExecutor(getVerticalStatusLabel(code));
        driverTools.waitForElementToPresence(VERTICAL_STATUS_DROPDOWN_INPUT).orElseThrow();
        driverTools.click(VERTICAL_STATUS_DROPDOWN_INPUT);
        List<String> statuses = getDropdownOptionTexts().stream()
                .map(status -> status.replaceAll("[^A-Za-z]", "").toUpperCase())
                .filter(status -> !status.isEmpty())
                .collect(Collectors.toList());
        closeOpenModal();
        return statuses;
    }

    /**
     * Returns the text of the options of the currently-open dropdown only. Closed dropdowns keep their options in the
     * DOM (hidden) and share data-testids across every dropdown, so only {@code isDisplayed()} options are collected -
     * otherwise this would leak options from unrelated dropdowns (e.g. Currency Filter labels into the GBV filter, or
     * Status-filter values into the vertical-status modal). Uses the {@code textContent} attribute rather than
     * {@code getText()} because options that are present but not yet laid out report an empty visible text; retries
     * briefly to absorb the option-render race.
     */
    private List<String> getDropdownOptionTexts() {
        driverTools.waitForElementsToAppear(DROPDOWN_OPTIONS);
        By optionBy = DROPDOWN_OPTIONS.by();
        List<String> previous = List.of();
        for (int attempt = 0; attempt < 25; attempt++) {
            List<String> texts = new ArrayList<>();
            for (WebElement option : driver.findElements(optionBy)) {
                try {
                    if (!option.isDisplayed()) {
                        continue;
                    }
                    String text = option.getAttribute("textContent");
                    if (text != null && !text.trim().isEmpty()) {
                        texts.add(text.trim());
                    }
                } catch (StaleElementReferenceException ignored) {
                    // re-find on next iteration
                }
            }
            // Options can render in progressively (especially the suggested-currency list), so don't return on the
            // first non-empty poll - wait until the displayed set settles (identical on two consecutive polls),
            // otherwise we'd capture a partial list while the rest are still being laid out.
            if (!texts.isEmpty() && texts.equals(previous)) {
                return texts;
            }
            previous = texts;
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        return previous;
    }

    /**
     * Changes the vertical currency status of {@code code} to {@code targetStatus}, routing through the valid
     * transition path when necessary. ACTIVE and INACTIVE are not directly interchangeable in the UI (only the
     * adjacent transitions ACTIVE&lt;-&gt;REVIEW and REVIEW&lt;-&gt;INACTIVE are allowed), so this method inserts the
     * intermediate REVIEW step automatically when jumping between the two extremes.
     */
    public void changeVerticalCurrencyStatus(String code, String targetStatus) {
        String target = targetStatus.trim().toUpperCase();
        String current = getVerticalCurrencyStatus(code);
        if (current.equals(target)) {
            logger.log("Vertical currency status for "+code+" already "+target);
            return;
        }
        if ((current.equals("INACTIVE") && target.equals("ACTIVE"))
                || (current.equals("ACTIVE") && target.equals("INACTIVE"))) {
            logger.log(current+" cannot move directly to "+target+" for "+code+"; routing via REVIEW");
            applyVerticalCurrencyStatus(code, "Review");
        }
        applyVerticalCurrencyStatus(code, targetStatus);
    }

    private void applyVerticalCurrencyStatus(String code, String targetStatus) {
        waitForToastToDisappear();
        // Use a JS click: a previously-closed status modal can leave an aria-hidden overlay in the DOM that still
        // intercepts native clicks on the listing label.
        driverTools.clickWithJavascriptExecutor(getVerticalStatusLabel(code));
        driverTools.waitForElementToPresence(VERTICAL_STATUS_DROPDOWN_INPUT).orElseThrow();
        driverTools.click(VERTICAL_STATUS_DROPDOWN_INPUT);
        clickVisibleDropdownOption(targetStatus);
        driverTools.click(VERTICAL_STATUS_SAVE_BUTTON);
        clickConfirmIfPresent();
        driverTools.waitForElementToDisappear(VERTICAL_STATUS_DROPDOWN_INPUT, 5);
        driverTools.waitForElementToDisappear(LOADING, 10);
    }

    public boolean isUserCurrencyEnabled(String code) {
        return driverTools.isElementSelected(getUserCurrencyCheckbox(code));
    }

    public void setUserCurrency(String code, boolean enable) {
        if (isUserCurrencyEnabled(code) == enable) {
            logger.log("User Currency for "+code+" already "+(enable ? "enabled" : "disabled"));
            return;
        }
        waitForToastToDisappear();
        driverTools.click(getUserCurrencyToggle(code));
        clickConfirmIfPresent();
        driverTools.waitForElementToDisappear(LOADING, 10);
        waitForCheckboxState(getUserCurrencyCheckbox(code), enable);
    }

    public boolean isConsumerVisibleEnabled(String code) {
        return driverTools.isElementSelected(getConsumerVisibleCheckbox(code));
    }

    public void setConsumerVisible(String code, boolean enable) {
        if (isConsumerVisibleEnabled(code) == enable) {
            logger.log("Consumer Visible for "+code+" already "+(enable ? "enabled" : "disabled"));
            return;
        }
        waitForToastToDisappear();
        driverTools.click(getConsumerVisibleToggle(code));
        clickConfirmIfPresent();
        driverTools.waitForElementToDisappear(LOADING, 10);
        waitForCheckboxState(getConsumerVisibleCheckbox(code), enable);
    }

    /**
     * Polls a toggle's hidden checkbox until it reflects the expected state. The toggle's backend update and the
     * resulting UI change are asynchronous and the loading spinner can come and go faster than a single check, so
     * relying on the spinner alone is racy.
     */
    private void waitForCheckboxState(Locator checkbox, boolean expected) {
        for (int attempt = 0; attempt < 25; attempt++) {
            if (driverTools.isElementSelected(checkbox) == expected) {
                return;
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        // Fail loudly instead of silently returning: a toggle stuck in the wrong state would otherwise surface as a
        // confusing downstream assertion failure far from the actual cause.
        throw new IllegalStateException("Checkbox " + checkbox + " did not reach selected=" + expected + " within timeout");
    }

    public void openSuggestedCurrency() {
        waitForToastToDisappear();
        driverTools.clickWithJavascriptExecutor(SUGGESTED_CURRENCY_BUTTON);
        // The modal title is always present in the DOM (hidden); wait for it to become visible to confirm the
        // popup actually opened.
        driverTools.waitForElementToBeVisible(SUGGESTED_MODAL_TITLE);
    }

    public boolean isSuggestedCurrencyPopupDisplayed() {
        return driverTools.isElementDisplayed(SUGGESTED_MODAL_TITLE);
    }

    public List<String> getSelectedSuggestedCurrencies() {
        if (!driverTools.isElementPresent(SUGGESTED_SELECTED_CHIPS)) {
            return List.of();
        }
        return driverTools.getTexts(SUGGESTED_SELECTED_CHIPS).stream()
                .map(this::extractCurrencyCode)
                .collect(Collectors.toList());
    }

    /**
     * Returns the ISO currency codes currently offered in the "Select currencies" dropdown. Only a curated subset of
     * currencies can be configured as suggested, so the test reads the actual options rather than assuming codes.
     * Each option is normalised to its ISO code so downstream API checks ({@code isSuggestedViaApi}) and chip
     * locators - both of which key on the bare code - keep working even if the dropdown renders descriptive labels
     * like {@code "JPY - Japanese Yen"}.
     */
    public List<String> getAvailableSuggestedOptions() {
        driverTools.click(SELECT_CURRENCIES_INPUT);
        List<String> options = getDropdownOptionTexts().stream()
                .map(this::extractCurrencyCode)
                .collect(Collectors.toList());
        // Close the dropdown without selecting anything by clicking a neutral spot in the modal.
        driverTools.click(SUGGESTED_MODAL_TITLE);
        return options;
    }

    /**
     * Extracts the 3-letter ISO currency code from a dropdown/chip label. Matches a standalone upper-case 3-letter
     * token (e.g. {@code "JPY"} from {@code "JPY - Japanese Yen"} or {@code "Japanese Yen (JPY)"}); falls back to the
     * trimmed, upper-cased label when no such token is present (e.g. the label is already a bare code).
     */
    private String extractCurrencyCode(String label) {
        if (label == null) {
            return "";
        }
        Matcher matcher = Pattern.compile("\\b[A-Z]{3}\\b").matcher(label);
        if (matcher.find()) {
            return matcher.group();
        }
        return label.trim().toUpperCase();
    }

    public void addSuggestedCurrency(String code) {
        waitForToastToDisappear();
        driverTools.click(SELECT_CURRENCIES_INPUT);
        clickVisibleDropdownOption(code);
        // Selecting a currency adds a pending chip and surfaces a "Yes, add" confirmation that commits the change.
        clickConfirmIfPresent();
        driverTools.waitForElementToDisappear(LOADING, 10);
    }

    /**
     * Attempts to add a currency when 3 suggested currencies are already configured and returns whether the
     * "max 3 suggested currencies" guard was triggered. The guard may surface immediately on selection or after the
     * "Yes, add" confirmation, so both points are checked; the error dialog is dismissed before returning.
     */
    public boolean attemptAddSuggestedBeyondLimit(String code) {
        waitForToastToDisappear();
        driverTools.click(SELECT_CURRENCIES_INPUT);
        clickVisibleDropdownOption(code);
        if (driverTools.waitForElementToPresence(MAX_SUGGESTED_ERROR, 3).isPresent()) {
            driverTools.click(OK_BUTTON);
            return true;
        }
        clickConfirmIfPresent();
        return isMaxSuggestedErrorDisplayed();
    }

    public void removeSuggestedCurrency(String code) {
        waitForToastToDisappear();
        // The remove control is the close (cross) svg. SVG elements never satisfy the "clickable" check and
        // scrollIntoView fails on them, so click at the icon's coordinates with real mouse events (Actions).
        // Clicking the chip's text label does not remove it - only the cross icon does.
        WebElement cross = driverTools.waitForElementToPresence(getSuggestedChipRemove(code)).orElseThrow();
        driverTools.clickWithActions(cross);
        clickConfirmIfPresent();
        driverTools.waitForElementToDisappear(LOADING, 10);
        driverTools.waitForElementToDisappear(getSuggestedChip(code), 10);
    }

    public boolean isMaxSuggestedErrorDisplayed() {
        boolean displayed = driverTools.waitForElementToPresence(MAX_SUGGESTED_ERROR, 5).isPresent();
        if (displayed) {
            driverTools.click(OK_BUTTON);
        }
        return displayed;
    }

    public String getToastMessage() {
        driverTools.waitForElementToPresence(TOAST_MESSAGE, 10);
        By toastBy = TOAST_MESSAGE.by();
        for (int attempt = 0; attempt < 25; attempt++) {
            for (WebElement toast : driver.findElements(toastBy)) {
                try {
                    if (toast.isDisplayed()) {
                        String text = toast.getText().trim();
                        if (!text.isEmpty()) {
                            return text;
                        }
                    }
                } catch (StaleElementReferenceException ignored) {
                    // toast re-rendered; re-find on next iteration
                }
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        throw new NoSuchElementException("Toast message not displayed");
    }

    /**
     * Waits for every currently visible toast/snackbar to disappear. Called before each mutating action so the
     * toast surfaced by the next action can be read in isolation (otherwise the lingering previous snackbar can be
     * verified by mistake, or can overlay/intercept clicks on the listing).
     *
     * <p>The {@code text_content} locator can match several nodes at once. Selenium's {@code invisibilityOfElementLocated}
     * only inspects the first match, so an unrelated persistent node would make it report "gone" prematurely or never -
     * here we poll until no matched toast is displayed, which is correct regardless of how many nodes match.
     */
    public void waitForToastToDisappear() {
        for (int attempt = 0; attempt < 50; attempt++) {
            if (!driverTools.isElementDisplayed(TOAST_MESSAGE)) {
                return;
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void clickConfirmIfPresent() {
        driverTools.waitForElementToBeClickable(CONFIRM_YES_BUTTON, 5)
                .ifPresent(driverTools::click);
    }

    private void closeOpenModal() {
        driverTools.click(MODAL_CLOSE_BUTTON);
    }

    public void closeSuggestedCurrency() {
        closeOpenModal();
        driverTools.waitForElementToDisappear(SUGGESTED_MODAL_TITLE, 5);
    }
}
