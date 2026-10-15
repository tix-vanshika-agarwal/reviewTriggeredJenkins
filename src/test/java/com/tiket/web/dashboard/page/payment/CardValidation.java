package com.tiket.web.dashboard.page.payment;

import com.tiket.model.Deeplink;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CardValidation<T extends WebDriver> extends BasePage<T> {

    private static final String paymentPath = "/admin/payment_v2";
    private static final String CARD_VALIDATION_API_PATH = "/ms-gateway/tix-credit-card/mcPgConfig/getAll";
    public static final String SUPPORTED = "SUPPORTED";
    public static final String NOT_SUPPORTED = "NOT_SUPPORTED";
    private static final int CLIENT_API_MAX_ATTEMPTS = 5;
    private static final long CLIENT_API_RETRY_DELAY_MS = 2000L;

    private static final Locator CARD_VALIDATION_TITLE = new Locator("CARD_VALIDATION_TITLE", By.xpath("//h1[contains(text(),'Card Validation')]"));
    private static final Locator CARD_VALIDATION_PAGE = new Locator("CARD_VALIDATION_PAGE", By.xpath("//*[@data-testid='card-validation-page']"));
    private static final Locator CARD_VALIDATION_TABLE = new Locator("CARD_VALIDATION_TABLE", By.xpath("//*[@data-testid='card-validation-page']//*[@data-testid='table-control']"));
    private static final Locator COLUMN_HEADERS = new Locator("COLUMN_HEADERS", By.xpath("//*[@data-testid='card-validation-page']//th//p"));
    private static final Locator LOADING = new Locator("LOADING", By.xpath("//*[@data-testid='loading']"));
    private static final Locator CARD_CONFIG_SIDEMENU = new Locator("CARD_CONFIG_SIDEMENU", By.xpath("//*[@data-testid='sidemenu']//*[contains(text(),'Card Payment Configuration')]"));
    private static final Locator CARD_VALIDATION_SUBMENU = new Locator("CARD_VALIDATION_SUBMENU", By.xpath("//*[contains(@data-testid,'submenu')]//*[contains(text(),'Card Validation')]"));
    private static final Locator SNACKBAR_ACTION = new Locator("SNACKBAR_ACTION", By.xpath("//*[contains(@class,'snackbar')]//*[contains(@class,'text_content')]"));
    private static final Locator MODAL_CONFIRM_BUTTON = new Locator("MODAL_CONFIRM_BUTTON", By.xpath("//*[contains(@class,'modal_footer')]//button[contains(text(),'Save') or contains(text(),'Yes') or contains(text(),'Confirm') or contains(text(),'Update') or contains(text(),'OK')]"));
    private static final Locator CURRENCY_CELLS = new Locator("CURRENCY_CELLS", By.xpath("//*[@data-testid='card-validation-page']//td[contains(@data-testid,'-pymCurrency')]//p"));

    private final Map<String, String> cardBrandToTestId = Map.of(
            "Visa", "visa",
            "Mastercard", "mastercard",
            "American Express", "americanExpress",
            "JCB", "jcb",
            "UnionPay", "unionPay",
            "Discover", "discover",
            "Diners", "diners",
            "BCA Card", "bcaCard",
            "KKI", "kki"
    );

    private final Map<String, String> cardBrandToApiField = Map.of(
            "Visa", "visa",
            "Mastercard", "masterCard",
            "American Express", "amex",
            "JCB", "jcb",
            "UnionPay", "union_pay",
            "Discover", "discover",
            "Diners", "diners",
            "BCA Card", "bca",
            "KKI", "kartuKredit"
    );

    private Locator getColumnHeader(String header) {
        return new Locator("COLUMN_HEADER_" + header.toUpperCase(), By.xpath("//*[@data-testid='card-validation-page']//th//p[normalize-space(text())='" + header + "']"));
    }

    private String testIdFor(String brand) {
        String id = cardBrandToTestId.get(brand);
        if (id == null) {
            throw new IllegalArgumentException("Unsupported card brand '" + brand + "'. Supported brands: " + cardBrandToTestId.keySet());
        }
        return id;
    }

    private String apiFieldFor(String brand) {
        String field = cardBrandToApiField.get(brand);
        if (field == null) {
            throw new IllegalArgumentException("Unsupported card brand '" + brand + "'. Supported brands: " + cardBrandToApiField.keySet());
        }
        return field;
    }

    private String cellXpath(String currency, String brand) {
        String suffix = testIdFor(brand);
        return "//*[@data-testid='card-validation-page']//tr[.//td[contains(@data-testid,'-pymCurrency')]//p[normalize-space(text())='" + currency + "']]"
                + "//td[@data-testid[substring(., string-length(.) - " + suffix.length() + ") = '-" + suffix + "']]";
    }

    private Locator getStatusLabel(String currency, String brand) {
        return new Locator("STATUS_" + currency.toUpperCase() + "_" + brand.toUpperCase(), By.xpath(cellXpath(currency, brand) + "//div[contains(@class,'label_')]"));
    }

    private Locator getActionMenuButton(String currency, String brand) {
        return new Locator("ACTION_" + currency.toUpperCase() + "_" + brand.toUpperCase(), By.xpath(cellXpath(currency, brand) + "//*[@data-testid='dropdown-btn-default']"));
    }

    private Locator getDropdownItem(String currency, String brand, String status) {
        return new Locator("DROPDOWN_ITEM_" + currency.toUpperCase() + "_" + brand.toUpperCase() + "_" + status, By.xpath(cellXpath(currency, brand) + "//*[@data-testid='dropdown-item-" + status + "']"));
    }

    private Locator getDropdownOptions(String currency, String brand) {
        return new Locator("DROPDOWN_OPTIONS_" + currency.toUpperCase() + "_" + brand.toUpperCase(), By.xpath(cellXpath(currency, brand) + "//*[contains(@class,'dropdown_options')]//*[contains(@data-testid,'dropdown-item-')]"));
    }

    public CardValidation(Session<T> session) {
        super(session);
    }

    public boolean isCardValidationPageOpened() {
        if (!driverTools.waitForElementToDisappear(LOADING)) {
            logger.log("Loading element not disappeared or even not appeared");
        }
        return driverTools.waitForElementToPresence(CARD_VALIDATION_TITLE, 3).isPresent();
    }

    public void openCardValidationPage() {
        if (!isCardValidationPageOpened()) {
            Deeplink deeplink = new Deeplink("ADMIN_CARE", "https://gatotkaca.tiket.com", "https://preprod.tiket.com", "https://tiket.com");
            String url = switch (Configs.ENVIRONMENT) {
                    case PROD -> deeplink.prodDeeplink() + paymentPath;
                    case PREPROD -> deeplink.preprodDeeplink() + paymentPath;
                    case GK -> deeplink.gkDeeplink() + paymentPath;
                    default -> deeplink.gkDeeplink() + paymentPath;
            };
            driverTools.get(url);
            if (!driverTools.getCurrentURL().contains(url)) Assertion.skip("Failed to open payment dashboard " + url);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            WebElement cardConfig = driverTools.waitForElementToPresence(CARD_CONFIG_SIDEMENU).orElseThrow();
            js.executeScript("arguments[0].click();", cardConfig);
            WebElement cardValidation = driverTools.waitForElementToPresence(CARD_VALIDATION_SUBMENU).orElseThrow();
            js.executeScript("arguments[0].click();", cardValidation);
            driverTools.waitForElementToPresence(CARD_VALIDATION_TITLE, 3);
        }
    }

    public void refreshPage() {
        driverTools.refreshWebpage();
        if (!isCardValidationPageOpened()) {
            Assertion.skip("Card Validation page not opened after refresh");
        }
    }

    public void logException(String currency, String cardBrand, Exception e) {
        logger.log("Failed to restore card validation status for " + currency + " - " + cardBrand + ": " + e.getMessage());
    }

    public boolean isCardValidationMatrixDisplayed() {
        driverTools.waitForElementToDisappear(LOADING);
        return driverTools.waitForElementToPresence(CARD_VALIDATION_PAGE, 3).isPresent()
                && driverTools.waitForElementToPresence(CARD_VALIDATION_TABLE, 3).isPresent();
    }

    public boolean isColumnHeaderDisplayed(String header) {
        return driverTools.waitForElementToPresence(getColumnHeader(header), 3).isPresent();
    }

    public List<String> getColumnHeaders() {
        driverTools.waitForElementToDisappear(LOADING);
        List<WebElement> headers = driverTools.waitForElementsToAppear(COLUMN_HEADERS, 5).orElseThrow();
        List<String> texts = new ArrayList<>();
        for (WebElement header : headers) {
            texts.add(driverTools.getValueAttribute(header, "textContent").trim());
        }
        logger.log("Card Validation column headers -> " + texts);
        return texts;
    }

    public List<String> getCurrencies() {
        driverTools.waitForElementToDisappear(LOADING);
        List<WebElement> cells = driverTools.waitForElementsToAppear(CURRENCY_CELLS, 5).orElseThrow();
        List<String> currencies = new ArrayList<>();
        for (WebElement cell : cells) {
            String text = driverTools.getValueAttribute(cell, "textContent").trim();
            if (!text.isEmpty()) {
                currencies.add(text);
            }
        }
        logger.log("Card Validation currencies -> " + currencies);
        return currencies;
    }

    public String getCardStatus(String currency, String brand) {
        driverTools.waitForElementToDisappear(LOADING);
        WebElement label = driverTools.waitForElementToPresence(getStatusLabel(currency, brand), 5).orElseThrow();
        String labelClass = driverTools.getValueAttribute(label, "class");
        logger.log("Status label class for " + currency + " - " + brand + " -> " + labelClass);
        if (labelClass.contains("color_green")) {
            return SUPPORTED;
        } else if (labelClass.contains("color_red")) {
            return NOT_SUPPORTED;
        }
        String rawText = driverTools.getText(label);
        String normalized = rawText.trim().toUpperCase().replace(" ", "_");
        if (SUPPORTED.equals(normalized)) {
            return SUPPORTED;
        } else if (NOT_SUPPORTED.equals(normalized)) {
            return NOT_SUPPORTED;
        }
        throw new IllegalStateException("Unable to determine card status for " + currency + " - " + brand
                + " (class='" + labelClass + "', text='" + rawText + "')");
    }

    public String getCardStatusWhenMatching(String currency, String brand, String expected) {
        String status = "";
        for (int attempt = 0; attempt < CLIENT_API_MAX_ATTEMPTS; attempt++) {
            refreshPage();
            status = getCardStatus(currency, brand);
            if (status.equalsIgnoreCase(expected)) {
                return status;
            }
            if (attempt < CLIENT_API_MAX_ATTEMPTS - 1) {
                logger.log("Matrix status for " + currency + " - " + brand + " not yet '" + expected
                        + "' (attempt " + (attempt + 1) + "); retrying after " + CLIENT_API_RETRY_DELAY_MS + "ms");
                sleep(CLIENT_API_RETRY_DELAY_MS);
            }
        }
        return status;
    }

    public void openActionMenu(String currency, String brand) {
        Locator actionButton = getActionMenuButton(currency, brand);
        Locator options = getDropdownOptions(currency, brand);
        for (int attempt = 0; attempt < 3; attempt++) {
            if (driverTools.isElementVisible(options)) {
                return;
            }
            driverTools.click(actionButton);
            driverTools.waitForElementToBeClickable(options, 2);
        }
        if (!driverTools.isElementVisible(options)) {
            throw new IllegalStateException("Action menu did not open for " + currency + " - " + brand);
        }
    }

    public void closeActionMenu(String currency, String brand) {
        Locator actionButton = getActionMenuButton(currency, brand);
        if (driverTools.isElementVisible(getDropdownOptions(currency, brand))) {
            driverTools.click(actionButton);
        }
    }

    public List<String> getAvailableStatusOptions(String currency, String brand) {
        openActionMenu(currency, brand);
        try {
            List<WebElement> options = driverTools.waitForElementsToAppear(getDropdownOptions(currency, brand), 3).orElseThrow();
            List<String> statuses = new ArrayList<>();
            for (WebElement option : options) {
                String testId = driverTools.getValueAttribute(option, "data-testid");
                statuses.add(testId.replace("dropdown-item-", "").trim());
            }
            logger.log("Available status options for " + currency + " - " + brand + " -> " + statuses);
            return statuses;
        } finally {
            closeActionMenu(currency, brand);
        }
    }

    public String updateCardStatus(String currency, String brand, String newStatus) {
        driverTools.waitForElementToDisappear(SNACKBAR_ACTION, 5);
        logger.log("Updating " + currency + " - " + brand + " status to " + newStatus);
        openActionMenu(currency, brand);
        driverTools.click(getDropdownItem(currency, brand, newStatus));
        driverTools.waitForElementToPresenceSafe(MODAL_CONFIRM_BUTTON, 3).ifPresent(this::jsClick);
        return getSnackbarText();
    }

    private void jsClick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    public String getSnackbarText() {
        for (int attempt = 0; attempt < 5; attempt++) {
            String text = driverTools.waitForElementToPresence(SNACKBAR_ACTION, attempt == 0 ? 5 : 1)
                    .map(element -> driverTools.getValueAttribute(element, "textContent").trim())
                    .orElse("");
            if (!text.isEmpty()) {
                return text;
            }
            sleep(300);
        }
        return "";
    }

    public JSONArray getCardValidationConfigViaClientApi() {
        String script = "var xhr = new XMLHttpRequest();"
                + "xhr.open('GET', '" + CARD_VALIDATION_API_PATH + "', false);"
                + "xhr.setRequestHeader('accept', '*/*');"
                + "xhr.setRequestHeader('lang', 'en');"
                + "xhr.setRequestHeader('x-audience', 'tiket.com');"
                + "xhr.setRequestHeader('x-cookie-session-v2', 'true');"
                + "xhr.send(null);"
                + "return JSON.stringify({status: xhr.status, body: xhr.responseText});";
        String raw = String.valueOf(((JavascriptExecutor) driver).executeScript(script));
        logger.log("Card Validation client API raw -> " + raw);

        JSONObject envelope = new JSONObject(raw);
        int httpStatus = envelope.optInt("status", -1);
        String body = envelope.optString("body", "");
        if (httpStatus != 200) {
            throw new IllegalStateException("Card Validation client API returned HTTP " + httpStatus + " -> " + body);
        }
        JSONObject json = new JSONObject(body);
        if (json.isNull("data")) {
            throw new IllegalStateException("Card Validation client API response has no 'data' array -> " + body);
        }
        return json.getJSONArray("data");
    }

    public String getCardStatusFromConfig(JSONArray data, String currency, String brand) {
        for (int i = 0; i < data.length(); i++) {
            JSONObject row = data.getJSONObject(i);
            if (row.getString("currency").equalsIgnoreCase(currency)) {
                return row.getString(apiFieldFor(brand));
            }
        }
        return "";
    }

    public JSONArray getCardValidationConfigWhenMatching(List<String[]> combos, Map<String, String> expectedByKey) {
        JSONArray config = null;
        for (int attempt = 0; attempt < CLIENT_API_MAX_ATTEMPTS; attempt++) {
            config = getCardValidationConfigViaClientApi();
            boolean allMatch = true;
            for (String[] combo : combos) {
                String expected = expectedByKey.get(combo[0] + "|" + combo[1]);
                if (!getCardStatusFromConfig(config, combo[0], combo[1]).equalsIgnoreCase(expected)) {
                    allMatch = false;
                    break;
                }
            }
            if (allMatch) {
                return config;
            }
            if (attempt < CLIENT_API_MAX_ATTEMPTS - 1) {
                logger.log("Client API not yet consistent with panel (attempt " + (attempt + 1) + "); retrying after " + CLIENT_API_RETRY_DELAY_MS + "ms");
                sleep(CLIENT_API_RETRY_DELAY_MS);
            }
        }
        logger.log("Client API did not converge with panel after " + CLIENT_API_MAX_ATTEMPTS + " attempts (~"
                + (CLIENT_API_MAX_ATTEMPTS * CLIENT_API_RETRY_DELAY_MS) + "ms); returning last snapshot so assertions surface the mismatch");
        return config;
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
