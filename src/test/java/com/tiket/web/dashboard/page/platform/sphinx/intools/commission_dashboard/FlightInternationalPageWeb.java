package com.tiket.web.dashboard.page.platform.sphinx.intools.commission_dashboard;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.text.DecimalFormat;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FlightInternationalPageWeb<T extends WebDriver> extends WebPage<T> {
    public FlightInternationalPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> FlightInternationalPageWeb<?> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new FlightInternationalPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new FlightInternationalPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    // Main Page
    private static final Locator VENDOR_ID_INPUT = new Locator("VENDOR_ID_INPUT", By.xpath("//input[@id='vendor-id']"));
    private static final Locator VENDOR_ID_SELECTION = new Locator("VENDOR_ID_SELECTION", By.xpath("//div/div/div/em"));
    private static final Locator VENDOR_ID_SAVE_BUTTON = new Locator("VENDOR_ID_SAVE_BUTTON", By.xpath("//button[text()='Save']"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", By.xpath("//button[text()='Search']"));

    private static final Locator VENDOR_ID_ROWS = new Locator("VENDOR_ID_ROWS", By.xpath("//table/tbody/tr/td[3]"));

    // Detail Transactions
    private static final Locator ORDER_ID_ROWS = new Locator("ORDER_ID_ROWS", By.xpath("//table/tbody/tr/td[4]"));

    private static final Locator MORE_TRANSACTIONS_BUTTON = new Locator("MORE_TRANSACTIONS_BUTTON", By.xpath("//div[./span[text()='Transactions']]"));

    // Detail Transactions Page
    private static final Locator ADD_TRANSACTIONS_BUTTON = new Locator("ADD_TRANSACTIONS_BUTTON", By.xpath("//button[text()='Add transaction']"));

    // Add Transaction Modal
    private static final Locator ADD_TRANSACTION_MODAL = new Locator("ADD_TRANSACTION_MODAL", By.xpath("//section[./header/h2[text()='Add Transaction']]"));
    private static final Locator ADD_TRANSACTION_MODAL_AMOUNT_INPUT = new Locator("ADD_TRANSACTION_MODAL_AMOUNT_INPUT", By.xpath("//input[@name='amount']"));
    private static final Locator ADD_TRANSACTION_ORDER_ID_INPUT = new Locator("ADD_TRANSACTION_ORDER_ID_INPUT", By.xpath("//input[@name='orderId']"));
    private static final Locator ADD_TRANSACTION_MODAL_BUTTON = new Locator("ADD_TRANSACTION_MODAL_BUTTON", By.xpath("(//button[text()='Add transaction'])[2]"));

    public void inputVendorIdFilter(String vendorId) {
        driverTools.waitForWebToLoad();
        driverTools.clickWithJavascriptExecutor(VENDOR_ID_INPUT);
        driverTools.sendKeys(VENDOR_ID_INPUT, vendorId);
        driverTools.dummyWait(3);

        List<WebElement> selections = driverTools.waitForElementsToAppear(VENDOR_ID_SELECTION).orElse(null);
        if (selections == null || selections.isEmpty()) throw new NoSuchElementException("There are no Vendor Id");

        for (WebElement selection : selections) {
            if(driverTools.getText(selection).equals(vendorId)) {
                driverTools.scrollIntoWebView(selection);
                driverTools.click(selection);
                break;
            }
        }
        driverTools.click(VENDOR_ID_SAVE_BUTTON);
    }

    public void clickSearchButton() {
        driverTools.click(SEARCH_BUTTON);
        driverTools.dummyWait(3);
    }

    public void clickMoreButtonWithVendorId(String vendorId) {
        List<WebElement> ids = driverTools.waitForElementsToAppear(VENDOR_ID_ROWS).orElse(null);

        if (ids == null || ids.isEmpty()) throw new NoSuchElementException("There are no Vendor Id");

        int index = 1;
        for (WebElement id : ids) {
            if (driverTools.getText(id).equalsIgnoreCase(vendorId)) {
                break;
            }
            index++;
        }

        int actionIndex = getColumnIndexForFlightInternationalListTable("Action");
        Locator loc = new Locator("ROWS_INDEX_" + index, By.xpath("//table/tbody/tr[" + index + "]/td[" + actionIndex + "]/div"));
        driverTools.scrollIntoWebView(loc);
        driverTools.click(loc);
    }

    public void clickMoreTransactionsButton() {
        driverTools.click(MORE_TRANSACTIONS_BUTTON);
    }

    public void clickAddTransactionButton() {
        driverTools.waitForElementToAppear(ADD_TRANSACTIONS_BUTTON);
        driverTools.clickWithJavascriptExecutor(ADD_TRANSACTIONS_BUTTON);
    }

    public void inputAddTransactionOrderIdInput(String orderId) {
        driverTools.waitForElementToAppear(ADD_TRANSACTION_MODAL, 3);
        driverTools.scrollIntoWebView(ADD_TRANSACTION_ORDER_ID_INPUT);
        driverTools.sendKeys(ADD_TRANSACTION_ORDER_ID_INPUT, orderId);
    }
    public void inputAddTransactionAmountInput(String amount) {
        driverTools.waitForElementToAppear(ADD_TRANSACTION_MODAL, 3);
        driverTools.scrollIntoWebView(ADD_TRANSACTION_MODAL_AMOUNT_INPUT);
        driverTools.sendKeys(ADD_TRANSACTION_MODAL_AMOUNT_INPUT, amount);
    }

    public void saveTransactionModal() {
        driverTools.clickWithJavascriptExecutor(ADD_TRANSACTION_MODAL_BUTTON);
    }

    public boolean validateRowTransactionWithOrderId(String orderId, String vendorId, String transactionAmount) {
        driverTools.refreshWebpage();
        driverTools.waitForWebToLoad();

        List<WebElement> rows = driverTools.waitForElementsToAppear(ORDER_ID_ROWS).orElseThrow();
        int index = 1;

        for (WebElement row : rows) {
            if (driverTools.getText(row).equals(orderId)) break;
            index++;
        }

        int vendorIdIndex = getColumnIndexForTableDetail("Vendor ID");
        int transactionAmountIndex = getColumnIndexForTableDetail("Transaction Amount");

        Locator vendorIdLoc = new Locator("VENDOR_ID", By.xpath("//table/tbody/tr[" + index + "]/td[" + vendorIdIndex +"]"));
        Locator transactionAmoutLoc = new Locator("TRANSACTION_AMOUNT", By.xpath("//table/tbody/tr[" + index + "]/td[" + transactionAmountIndex +"]"));

        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        String amountFormatted = decimalFormat.format(Integer.parseInt(transactionAmount));

        driverTools.scrollToElement(vendorIdLoc);

        boolean isVendorIdPresent =  driverTools.getText(vendorIdLoc).orElseThrow().equalsIgnoreCase(vendorId) ;

        driverTools.scrollToElement(transactionAmoutLoc);
        boolean transactionAmountValue = driverTools.getText(transactionAmoutLoc).orElseThrow().contains(amountFormatted);

        return isVendorIdPresent && transactionAmountValue;
    }

    public String getConfigIdFromUrl() {
        String url = driverTools.getCurrentURL();
        Pattern pattern = Pattern.compile("flight-international/(\\d+)/");
        Matcher matcher = pattern.matcher(url);

        if (matcher.find()) {
            return matcher.group(1);
        }
        throw new NoSuchElementException();
    }

    private int getColumnIndexForFlightInternationalListTable(String columnTitle) {
        List<WebElement> cols = driverTools.getDriver().findElements(
                By.xpath("//th[text()='" + columnTitle + "']/preceding-sibling::th[not(contains(@style, 'none'))]")
        );
        return cols.size() + 1;
    }


    private int getColumnIndexForTableDetail(String columnTitle) {
        List<WebElement> cols = driverTools.getDriver().findElements(
                By.xpath("//th[.//div[text()='" + columnTitle + "']]/preceding-sibling::th[not(contains(@style, 'none'))]")
        );
        return cols.size() + 1;
    }
}