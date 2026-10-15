package com.tiket.web.dashboard.page.payment;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Deeplink;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.hamcrest.Matchers.*;

public class ForeignExchangeRatePage<T extends WebDriver> extends WebPage<T> {
    public ForeignExchangeRatePage(Session<T> session) {
        super(session);
    }

    private static final String paymentPath = "/admin/payment_v2";
    private static final Locator LOADING = new Locator("LOADING", new By.ByXPath("//*[@data-testid='loading']"));
    private static final Locator NO_RECORDS_LIST = new Locator("NO_RECORDS_LIST", new By.ByXPath("//*[text()='There are no records to display']"));
    private static final Locator CURRENCY_LIST_TABLE = new Locator("CURRENCY_LIST_TABLE", new By.ByXPath("//*[contains(@class,'TableBody')]"));
    private static final Locator CURRENCY_CONFIG_SIDEMENU = new Locator("CURRENCY_CONFIG_SIDEMENU", new By.ByXPath("//*[@data-testid='sidemenu']//*[contains(text(),'Currency Configuration')]"));
    private static final Locator FX_RATE_SUBMENU = new Locator("FX_RATE_SUBMENU", new By.ByXPath("//*[contains(@data-testid,'submenu')]//*[contains(text(),'FX Rate')]"));
    private static final Locator DYNAMIC_REFRESH_BUTTON = new Locator("DYNAMIC_REFRESH_BUTTON", By.xpath("//p[contains(text(), 'Latest rate update')]/../following-sibling::div//button"));
    private static final Locator DOWNLOAD_EXCHANGE_RATE_BTN = new Locator("DOWNLOAD_EXCHANGE_RATE_BTN", By.xpath("//button[contains((.), 'Download Exchange Rate')]"));
    private static final Locator FROM_CURRENCY_DROPDOWN = new Locator("FROM_CURRENCY_DROPDOWN", By.xpath("//input[@placeholder='From:']"));
    private static final Locator TO_CURRENCY_DROPDOWN = new Locator("TO_CURRENCY_DROPDOWN", By.xpath("//input[@placeholder='To:']"));
    private static final Locator CURRENCY_SEARCH_FIELD = new Locator("CURRENCY_SEARCH_FIELD", By.xpath("//input[@data-testid='response-code-field']"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", By.xpath("//button[text()='Search']"));
    private static final Locator RESET_FILTER_BUTTON = new Locator("RESET_FILTER_BUTTON", By.xpath("//button[text()='Reset Filter']"));
    private static final Locator FX_RATE_TABLE = new Locator("FX_RATE_TABLE", By.xpath("//div[@role='table' and contains(@class, 'rdt_Table')]"));
    private static final Locator BUY_INFO = new Locator("BUY_INFO", By.xpath("//span[contains(text(), 'Buy')]"));
    private static final Locator SELL_INFO = new Locator("SELL_INFO", By.xpath("//span[contains(text(), 'Sell')]"));
    private static final Locator NO_RECORDS_MESSAGE = new Locator("NO_RECORDS_MESSAGE", By.xpath("//div[contains(text(), 'There are no records to display')]"));
    private static final Locator DEVIATION_INPUT = new Locator("DEVIATION_INPUT", By.xpath("//input[@data-testid='foreign-exchange-value-input']"));
    private static final Locator SAVE_MODAL_BTN = new Locator("SAVE_MODAL_BTN", By.xpath("//button[@data-testid='btn-save']"));
    private static final Locator OVERRIDE_CONFIRM_TEXT = new Locator("OVERRIDE_CONFIRM_TEXT", By.xpath("//h2[text()='Override the deviation threshold']/../following-sibling::div/span"));
    private static final Locator YES_OVERRIDE_BTN = new Locator("YES_OVERRIDE_BTN", By.xpath("//button[text()='Yes, override it']"));
    private static final Locator COMPARISON_INPUT = new Locator("COMPARISON_INPUT", By.id("comparison-threshold"));
    private static final Locator OVERRIDE_COMPARISON_CONFIRM_TEXT = new Locator("OVERRIDE_COMPARISON_CONFIRM_TEXT", By.xpath("//h2[text()='Override rate comparison threshold']/../following-sibling::div/span"));
    private static final Locator REFRESH_CONFIRM_TEXT = new Locator("REFRESH_CONFIRM_TEXT", By.xpath("//h2[text()='Refresh Rate']/../following-sibling::div/span[contains(text(), 'Are you sure you want to refresh')]"));
    private static final Locator YES_REFRESH_BTN = new Locator("YES_REFRESH_BTN", By.xpath("//button[text()='Yes, refresh']"));
    private static final Locator DOWNLOAD_CONFIRM_TEXT = new Locator("DOWNLOAD_CONFIRM_TEXT", By.xpath("//h2[text()='Download Exchange Rate']/../following-sibling::div/span[contains(text(), 'Are you sure want to download')]"));
    private static final Locator YES_DOWNLOAD_BTN = new Locator("YES_DOWNLOAD_BTN", By.xpath("//button[text()='Yes, download']"));
    public boolean isCurrencyConfigPageOpened() {
        if (!driverTools.waitForElementToDisappear(LOADING, 5)) {
            logger.log("Element not disappeared or even not appeared");
        }
        driverTools.waitForElementToDisappear(NO_RECORDS_LIST, 5);
        return driverTools.waitForElementToPresence(CURRENCY_LIST_TABLE, 3).isPresent();
    }

    public void openFXRatePage() {
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
            WebElement fxConfig = driverTools.waitForElementToPresence(FX_RATE_SUBMENU).orElseThrow();
            js.executeScript("arguments[0].click();", fxConfig);
            driverTools.waitForElementToDisappear(NO_RECORDS_LIST, 5);
            driverTools.waitForElementToPresence(CURRENCY_LIST_TABLE, 3);
        }
    }

    public void verifyFxRatePageComponents() {
        WebElement refreshBtn = driverTools.waitForElementToAppear(DYNAMIC_REFRESH_BUTTON, 5).orElseThrow(() -> new NoSuchElementException("Refresh/Waiting button not found"));
        String refreshBtnText = refreshBtn.getText().trim();

        if (refreshBtnText.equalsIgnoreCase("Waiting...")) {
            logger.log("Refresh button is currently in 'Waiting...' state. Skipping refresh action verification.");
        } else {
            Assertion.assertThat("Verify Refresh Rate button is displayed", refreshBtn.isDisplayed(), is(true));
        }

        WebElement buyValue = driverTools.waitForElementToAppear(BUY_INFO, 5).orElseThrow();
        Assertion.assertThat("Verify Buy value is displayed", buyValue.isDisplayed(), is(true));

        WebElement sellValue = driverTools.waitForElementToAppear(SELL_INFO, 5).orElseThrow();
        Assertion.assertThat("Verify Sell value is displayed", sellValue.isDisplayed(), is(true));

        WebElement downloadBtn = driverTools.waitForElementToAppear(DOWNLOAD_EXCHANGE_RATE_BTN, 5).orElseThrow();
        Assertion.assertThat("Verify Download Exchange Rate button is displayed", downloadBtn.isDisplayed(), is(true));

        WebElement fromDropdown = driverTools.waitForElementToAppear(FROM_CURRENCY_DROPDOWN, 5).orElseThrow();
        Assertion.assertThat("Verify 'From' currency dropdown is displayed", fromDropdown.isDisplayed(), is(true));

        WebElement toDropdown = driverTools.waitForElementToAppear(TO_CURRENCY_DROPDOWN, 5).orElseThrow();
        Assertion.assertThat("Verify 'To' currency dropdown is displayed", toDropdown.isDisplayed(), is(true));

        WebElement searchField = driverTools.waitForElementToAppear(CURRENCY_SEARCH_FIELD, 5).orElseThrow();
        Assertion.assertThat("Verify Currency Code search field is displayed", searchField.isDisplayed(), is(true));

        WebElement searchBtn = driverTools.waitForElementToAppear(SEARCH_BUTTON, 5).orElseThrow();
        Assertion.assertThat("Verify Search button is displayed", searchBtn.isDisplayed(), is(true));

        WebElement resetBtn = driverTools.waitForElementToAppear(RESET_FILTER_BUTTON, 5).orElseThrow();
        Assertion.assertThat("Verify Reset Filter button is displayed", resetBtn.isDisplayed(), is(true));

        WebElement table = driverTools.waitForElementToAppear(FX_RATE_TABLE, 5).orElseThrow();
        Assertion.assertThat("Verify FX Rate listing table is displayed", table.isDisplayed(), is(true));

        By tableRowsXpath = By.xpath("//div[@role='row' and contains(@class, 'rdt_TableRow')]");
        List<WebElement> rows = driverTools.getDriver().findElements(tableRowsXpath);
        Assertion.assertThat("Verify the table has at least 1 row of data", rows.size(), is(greaterThan(0)));
    }

    public void filterAndVerifyFxRates(String fromCurrency, String toCurrency) {
        WebElement fromInput = driverTools.waitForElementToAppear(FROM_CURRENCY_DROPDOWN, 5).orElseThrow();
        fromInput.click();
        driverTools.dummyWait(2); // Wait for the dropdown animation

        String optionsXpath = "//div[contains(@class, 'styles_dropdown_option')]//p";
        List<WebElement> availableOptions = driverTools.getDriver().findElements(By.xpath(optionsXpath));
        Assertion.assertThat("Verify available currencies are populated in the From dropdown", availableOptions.size(), is(greaterThan(0)));

        By fromOptionXpath = By.xpath("//div[contains(@class, 'styles_dropdown_option')]//p[normalize-space(text())=\"" + fromCurrency + "\"]");
        List<WebElement> fromOptions = driverTools.getDriver().findElements(fromOptionXpath);
        for (WebElement opt : fromOptions) {
            if (opt.isDisplayed()) {
                opt.click();
                break;
            }
        }

        WebElement toInput = driverTools.waitForElementToAppear(TO_CURRENCY_DROPDOWN, 5).orElseThrow();
        toInput.click();
        driverTools.dummyWait(2);

        By toOptionXpath = By.xpath("//div[contains(@class, 'styles_dropdown_option')]//p[normalize-space(text())=\"" + toCurrency + "\"]");
        List<WebElement> toOptions = driverTools.getDriver().findElements(toOptionXpath);
        for (WebElement opt : toOptions) {
            if (opt.isDisplayed()) {
                opt.click();
                break;
            }
        }

        WebElement searchBtn = driverTools.waitForElementToAppear(SEARCH_BUTTON, 5).orElseThrow();
        searchBtn.click();
        driverTools.dummyWait(2); // Wait a moment for the table network request to finish filtering

        By tableRowsXpath = By.xpath("//div[@role='row' and contains(@class, 'rdt_TableRow')]");
        List<WebElement> rows = driverTools.getDriver().findElements(tableRowsXpath);
        Assertion.assertThat("Verify the filtered table has at least 1 record", rows.size(), is(greaterThan(0)));

        By convertToCellsXpath = By.xpath("//div[@role='row' and contains(@class, 'rdt_TableRow')]//div[@data-column-id='2']");
        List<WebElement> convertToCells = driverTools.getDriver().findElements(convertToCellsXpath);

        for (int i = 0; i < convertToCells.size(); i++) {
            String actualConvertTo = convertToCells.get(i).getText().trim();
            Assertion.assertThat("Verify row " + (i + 1) + " matches the 'To' currency filter. Expected: " + toCurrency + " Actual: " + actualConvertTo,
                    actualConvertTo, is(toCurrency));
        }
    }

    public void searchAndVerifyValidCurrency(String validCurrencyCode) {
        WebElement searchField = driverTools.waitForElementToAppear(CURRENCY_SEARCH_FIELD, 5).orElseThrow();
        searchField.clear();
        searchField.sendKeys(validCurrencyCode);
        WebElement searchBtn = driverTools.waitForElementToAppear(SEARCH_BUTTON, 5).orElseThrow();
        searchBtn.click();
        driverTools.dummyWait(2); // wait to get the table updated as per the search criteria
        By tableRowsXpath = By.xpath("//div[@role='row' and contains(@class, 'rdt_TableRow')]");
        List<WebElement> rows = driverTools.getDriver().findElements(tableRowsXpath);
        Assertion.assertThat("Verify at least 1 matching record is displayed", rows.size(), is(greaterThan(0)));
        By originCurrencyCellsXpath = By.xpath("//div[@role='row' and contains(@class, 'rdt_TableRow')]//div[@data-column-id='1']");
        List<WebElement> originCells = driverTools.getDriver().findElements(originCurrencyCellsXpath);

        for (int i = 0; i < originCells.size(); i++) {
            String actualOriginCurrency = originCells.get(i).getText().trim();
            Assertion.assertThat("Verify row " + (i + 1) + " matches the 'origin' currency filter. Expected: " + validCurrencyCode + " Actual: " + actualOriginCurrency, actualOriginCurrency, is(validCurrencyCode));
        }
    }

    public void searchAndVerifyInvalidCurrency(String invalidCurrencyCode) {
        WebElement searchField = driverTools.waitForElementToAppear(CURRENCY_SEARCH_FIELD, 5).orElseThrow();
        searchField.click();
        driverTools.clearInput(searchField);
        searchField.sendKeys(invalidCurrencyCode);
        WebElement searchBtn = driverTools.waitForElementToAppear(SEARCH_BUTTON, 5).orElseThrow();
        searchBtn.click();
        driverTools.dummyWait(2); // wait to get the search result updated
        WebElement noRecordsMsg = driverTools.waitForElementToAppear(NO_RECORDS_MESSAGE, 5).orElseThrow(() -> new NoSuchElementException("'No records to display' message did not appear"));
        Assertion.assertThat("Verify empty state message is displayed.", noRecordsMsg.isDisplayed(), is(true));
    }

    public Map<String, String> captureDefaultFilterValues() {
        Map<String, String> defaultValues = new HashMap<>();

        WebElement fromInput = driverTools.waitForElementToAppear(FROM_CURRENCY_DROPDOWN, 5).orElseThrow();
        WebElement toInput = driverTools.waitForElementToAppear(TO_CURRENCY_DROPDOWN, 5).orElseThrow();
        WebElement searchField = driverTools.waitForElementToAppear(CURRENCY_SEARCH_FIELD, 5).orElseThrow();

        String fromVal = fromInput.getAttribute("value");
        String toVal = toInput.getAttribute("value");
        String searchVal = searchField.getAttribute("value");

        defaultValues.put("from", fromVal == null ? "" : fromVal);
        defaultValues.put("to", toVal == null ? "" : toVal);
        defaultValues.put("search", searchVal == null ? "" : searchVal);
        return defaultValues;
    }

    public void clickResetAndVerifyRestoredValues(Map<String, String> expectedDefaults) {
        WebElement resetBtn = driverTools.waitForElementToAppear(RESET_FILTER_BUTTON, 5).orElseThrow();
        resetBtn.click();

        driverTools.dummyWait(2); // wait to let the UI update after click on reset button
        WebElement fromInput = driverTools.waitForElementToAppear(FROM_CURRENCY_DROPDOWN, 5).orElseThrow();
        WebElement toInput = driverTools.waitForElementToAppear(TO_CURRENCY_DROPDOWN, 5).orElseThrow();
        WebElement searchField = driverTools.waitForElementToAppear(CURRENCY_SEARCH_FIELD, 5).orElseThrow();

        String currentFromValue = fromInput.getAttribute("value") == null ? "" : fromInput.getAttribute("value");
        String currentToValue = toInput.getAttribute("value") == null ? "" : toInput.getAttribute("value");
        String currentSearchValue = searchField.getAttribute("value") == null ? "" : searchField.getAttribute("value");

        Assertion.assertThat("Verify 'From' dropdown reset to default", currentFromValue, is(expectedDefaults.get("from")));
        Assertion.assertThat("Verify 'To' dropdown reset to default", currentToValue, is(expectedDefaults.get("to")));
        Assertion.assertThat("Verify Search field reset to default", currentSearchValue, is(expectedDefaults.get("search")));
    }

    public void updateFirstRowDeviationThreshold(String newThresholdValue) {
        By firstRowThresholdBtnXpath = By.xpath("(//div[@role='row' and contains(@class, 'rdt_TableRow')])[1]//div[@data-column-id='5']//button");
        WebElement thresholdBtn = driverTools.waitForElementToAppear(new Locator("FIRST_ROW_THRESHOLD_BTN", firstRowThresholdBtnXpath), 5).orElseThrow();
        thresholdBtn.click();

        WebElement inputField = driverTools.waitForElementToAppear(DEVIATION_INPUT, 5).orElseThrow();
        String defaultThreshold = inputField.getAttribute("value");
        inputField.click();
        driverTools.clearInput(inputField);
        inputField.sendKeys(newThresholdValue);

        WebElement saveBtn = driverTools.waitForElementToAppear(SAVE_MODAL_BTN, 5).orElseThrow();
        saveBtn.click();

        WebElement confirmMsgElement = driverTools.waitForElementToAppear(OVERRIDE_CONFIRM_TEXT, 5).orElseThrow();
        String actualConfirmText = confirmMsgElement.getText().trim();
        String expectedConfirmText = "Are you sure you want to override the deviation threshold from " + defaultThreshold + "% to " + newThresholdValue + "%?";
        Assertion.assertThat("Verify override confirmation message matches expected format", actualConfirmText, is(expectedConfirmText));
        WebElement yesBtn = driverTools.waitForElementToAppear(YES_OVERRIDE_BTN, 5).orElseThrow();
        yesBtn.click();
    }


    public void verifySuccessSnackbar(String expectedMessage) {
        String snackbarXpath = "//div[contains(@class, 'dcXu0G_text_content') and text()='" + expectedMessage + "']";
        Locator dynamicSnackbarLocator = new Locator("DYNAMIC_SNACKBAR", By.xpath(snackbarXpath));
        WebElement snackbar = driverTools.waitForElementToAppear(dynamicSnackbarLocator, 10).orElseThrow(() -> new NoSuchElementException("Snackbar message '" + expectedMessage + "' did not appear in time"));
        Assertion.assertThat("Verify success snackbar is displayed", snackbar.isDisplayed(), is(true));
        boolean isDismissed = driverTools.waitForElementToDisappear(dynamicSnackbarLocator, 10);
        Assertion.assertThat("Verify success snackbar dismissed", isDismissed, is(true));
    }

    public void updateFirstRowComparisonThreshold(String newThresholdValue) {
        By firstRowThresholdBtnXpath = By.xpath("(//div[@role='row' and contains(@class, 'rdt_TableRow')])[1]//div[@data-column-id='6']//button");
        WebElement thresholdBtn = driverTools.waitForElementToAppear(new Locator("FIRST_ROW_COMP_THRESHOLD_BTN", firstRowThresholdBtnXpath), 5).orElseThrow();
        thresholdBtn.click();

        WebElement inputField = driverTools.waitForElementToAppear(COMPARISON_INPUT, 5).orElseThrow();
        String defaultThreshold = inputField.getAttribute("value");
        inputField.click();
        Keys modifier = System.getProperty("os.name").toLowerCase().contains("mac") ? Keys.COMMAND : Keys.CONTROL;
        inputField.sendKeys(Keys.chord(modifier, "a"), Keys.DELETE);
        inputField.sendKeys(newThresholdValue);
        WebElement saveBtn = driverTools.waitForElementToAppear(SAVE_MODAL_BTN, 5).orElseThrow();
        saveBtn.click();

        WebElement confirmMsgElement = driverTools.waitForElementToAppear(OVERRIDE_COMPARISON_CONFIRM_TEXT, 5).orElseThrow();
        String actualConfirmText = confirmMsgElement.getText().trim();
        String expectedConfirmText = "Are you sure you want to override rate comparison threshold from " + defaultThreshold + "% to " + newThresholdValue + "%";
        Assertion.assertThat("Verify override confirmation message matches expected format", actualConfirmText, is(expectedConfirmText));
        WebElement yesBtn = driverTools.waitForElementToAppear(YES_OVERRIDE_BTN, 5).orElseThrow();
        yesBtn.click();
    }

    public String getFirstRowThresholdValue(String thresholdType) {
        String columnId;
        if (thresholdType.equalsIgnoreCase("Deviation")) {
            columnId = "5";
        } else if (thresholdType.equalsIgnoreCase("Comparison")) {
            columnId = "6";
        } else {
            throw new IllegalArgumentException("Invalid threshold type. Use 'Deviation' or 'Comparison'.");
        }
        By firstRowThresholdBtnXpath = By.xpath("(//div[@role='row' and contains(@class, 'rdt_TableRow')])[1]//div[@data-column-id='" + columnId + "']//button//span");
        WebElement thresholdSpan = driverTools.waitForElementToAppear(new Locator("FIRST_ROW_" + thresholdType.toUpperCase() + "_SPAN", firstRowThresholdBtnXpath), 5).orElseThrow(() -> new NoSuchElementException(thresholdType + " Threshold span not found in the first row"));
        String actualValue = thresholdSpan.getText().trim();
        return actualValue;
    }

    public void conditionalRefreshRate(String refreshMsg) {
        WebElement refreshBtn = driverTools.waitForElementToAppear(DYNAMIC_REFRESH_BUTTON, 5).orElseThrow(
                () -> new NoSuchElementException("Refresh/Waiting button not found on the page")
        );

        String btnText = refreshBtn.getText().trim();
        if (btnText.equalsIgnoreCase("Waiting...")) {
            logger.log("Refresh rate is currently in cooldown ('Waiting...'). Skipping refresh flow.");
            return;
        }
        refreshBtn.click();
        WebElement confirmTextElement = driverTools.waitForElementToAppear(REFRESH_CONFIRM_TEXT, 5).orElseThrow();
        Assertion.assertThat("Verify refresh confirmation text appears", confirmTextElement.isDisplayed(), is(true));
        WebElement yesRefreshBtn = driverTools.waitForElementToAppear(YES_REFRESH_BTN, 5).orElseThrow();
        yesRefreshBtn.click();
        verifySuccessSnackbar(refreshMsg);
    }

    public void clickDownloadExchangeRate() throws IOException {
        WebElement downloadBtn = driverTools.waitForElementToAppear(DOWNLOAD_EXCHANGE_RATE_BTN, 5).orElseThrow();
        downloadBtn.click();

        WebElement confirmText = driverTools.waitForElementToAppear(DOWNLOAD_CONFIRM_TEXT, 5).orElseThrow(
                () -> new NoSuchElementException("Download confirmation modal did not appear")
        );
        Assertion.assertThat("Verify download modal is displayed", confirmText.isDisplayed(), is(true));
        WebElement yesBtn = driverTools.waitForElementToAppear(YES_DOWNLOAD_BTN, 5).orElseThrow();
        yesBtn.click();
        if(!CoreFunctions.isJenkinsSystem()){
            driverTools.dummyWait(2); // wait for the dialog box to appear
            String[] cmd = {"osascript", "-e", "tell application \"System Events\" to keystroke return"};
            Runtime.getRuntime().exec(cmd);
        }
    }

    public void verifyDownloadedFile(String start, String extension) throws InterruptedException {
        String downloadPath = System.getProperty("user.home") + File.separator + "Downloads";
        File dir = new File(downloadPath);

        File downloadedFile = null;
        int maxWaitSeconds = 15;
        boolean isDownloaded = false;
        for (int elapsedSeconds = 0; elapsedSeconds < maxWaitSeconds; elapsedSeconds++) {
            File[] files = dir.listFiles((d, name) -> name.startsWith(start) && name.endsWith(extension));

            if (files != null && files.length > 0) {
                Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());
                downloadedFile = files[0];
                if (downloadedFile.length() > 0) {
                    isDownloaded = true;
                    break;
                }
            }
            driverTools.dummyWait(1); // wait for the file to be downloaded.
        }
        Assertion.assertThat("Verify the file is downloaded successfully within " + maxWaitSeconds + " seconds", isDownloaded, is(true));
        if (isDownloaded) {
            String fileName = downloadedFile.getName();
            Assertion.assertThat("Verify the file name starts with "+start, fileName, startsWith(start));
            Assertion.assertThat("Verify the downloaded file format is "+extension, fileName, endsWith(extension));
            downloadedFile.delete();
        }
    }
}
