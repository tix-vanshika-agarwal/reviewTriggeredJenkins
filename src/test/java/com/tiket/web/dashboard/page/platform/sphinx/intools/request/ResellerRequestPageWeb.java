package com.tiket.web.dashboard.page.platform.sphinx.intools.request;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.*;

public class ResellerRequestPageWeb<T extends WebDriver> extends WebPage<T> {
    public ResellerRequestPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> ResellerRequestPageWeb<?> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new ResellerRequestPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new ResellerRequestPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    private static final Locator REQUEST_HISTORY_TAB = new Locator("REQUEST_HISTORY_TAB", By.xpath("//button[text()='Request History']"));

    // Approve Request Modal
    private static final Locator APPROVE_REQUEST_MODAL = new Locator("APPROVE_REQUEST_MODAL", By.xpath("//section[.//h2[text()='Approve request?']]"));
    private static final Locator REMARKS_INPUT = new Locator("REMARKS_INPUT", By.xpath("//textarea[@id='remarks']"));
    private static final Locator APPROVE_BUTTON = new Locator("APPROVE_BUTTON", By.xpath("//section//button[text()='Approve']"));

    // Request History Tab
    private static final Locator SEARCH_BY_DROPDOWN = new Locator("SEARCH_BY_DROPDOWN", By.xpath("//input[@placeholder='Search by']"));
    private static final Locator SEARCH_BY_ID_SELECTION = new Locator("SEARCH_BY_ID_SELECTION", By.xpath("//div[./label[text()='Reseller ID']]"));
    private static final Locator RESELLER_ID_INPUT = new Locator("RESELLER_ID_INPUT", By.xpath("//input[@name='Reseller ID']"));
    private static final Locator RESELLER_ID_SAVE_BUTTON = new Locator("RESELLER_ID_SAVE_BUTTON", By.xpath("//button[text()='Save']"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", By.xpath("//button[@data-testid='search-submit']"));
    private static final Locator GRID_FIRST_ROW = new Locator("GRID_FIRST_ROW", By.xpath("//tbody[@data-testid='table:list-body']/tr[1]"));
    private static final Locator FIRST_ROW_RESELLER_NAME = new Locator("FIRST_ROW_RESELLER_NAME", By.xpath("//tbody[@data-testid='table:list-body']/tr[1]/td[2]"));
    private static final Locator FIRST_ROW_TRANSACTION_AMOUNT = new Locator("FIRST_ROW_TRANSACTION_AMOUNT", By.xpath("//tbody[@data-testid='table:list-body']/tr[1]/td[8]"));
    private static final Locator SUCCESS_SNACKBAR_MSG = new Locator("SUCCESS_SNACKBAR_MSG", By.xpath("//div[contains(@class, 'Snackbar_text_content')]"));
    public void clickApproveByResellerId(String target) {
        int index = getIndexByResellerId(target);
        clickApproveButtonWithRowIndex(index);
    }

    public int getIndexByResellerId(String target) {
        int resellerIdTableIndex = getIndexOfTableColumn("Reseller ID");
        List<WebElement> ids = driverTools.waitForElementsToAppear(
                new Locator("RESELLER_ID_ROWS", By.xpath("//table/tbody/tr/td[" + resellerIdTableIndex + "]"))
        ).orElse(null);
        if (ids == null || ids.isEmpty()) throw new NoSuchElementException("There are no data in the table");

        int index = 1;
        for (WebElement id : ids) {
            if (driverTools.getText(id).equalsIgnoreCase(target)) return index;
            index++;
        }
        throw new NoSuchElementException("There are no Reseller Id for Id " + target);
    }

    public void clickApproveButtonWithRowIndex(int index) {
        Locator targetRow = new Locator("STATUS_APPROVE_ROW_" + index, By.xpath("//table/tbody/tr[" + index +"]/td[14]/div/button[2]"));
        if (!driverTools.isElementPresent(targetRow)) throw new NoSuchElementException("There are no Approve Button for row with index " + index);
        driverTools.click(targetRow);
    }

    public void approveRequestInModal(String remarks) {
        driverTools.waitForElementToAppear(APPROVE_REQUEST_MODAL, 2);
        driverTools.clickWithJavascriptExecutor(REMARKS_INPUT);
        driverTools.sendKeys(REMARKS_INPUT, remarks);
        driverTools.click(APPROVE_BUTTON);
    }

    public void clickRequestHistoryTab() {
        driverTools.scrollElementToCenter(REQUEST_HISTORY_TAB);
        driverTools.click(REQUEST_HISTORY_TAB);
    }

    public void setSearchByToResellerId() {
        driverTools.click(SEARCH_BY_DROPDOWN);
        driverTools.click(SEARCH_BY_ID_SELECTION);
    }

    public void setResellerId(String id) {
        driverTools.click(RESELLER_ID_INPUT);
        driverTools.sendKeys(RESELLER_ID_INPUT, id);

        Locator loc = new Locator("SELECTION_ID_" + id, By.xpath("//div[./span[text()='" + id + "']]"));
        driverTools.scrollIntoWebView(loc);
        driverTools.click(loc);
        driverTools.click(RESELLER_ID_SAVE_BUTTON);
    }

    public void clickSearch() {
        driverTools.click(SEARCH_BUTTON);
        driverTools.dummyWait(2);
    }

    public boolean verifyDataInsertedIntoRequestHistory(String id, String amount, String remarks) {

        int resellerIdTableIndex = getIndexOfTableColumn("Reseller ID");
        int amountTableIndex = getIndexOfTableColumn("Transaction Amount with Currency");
        int remarksTableIndex = getIndexOfTableColumn("Remarks");

        Locator resellerIdLoc = new Locator("RESELLER_ID", By.xpath("//table/tbody/tr[1]/td[" + resellerIdTableIndex + "]"));
        Locator amountLoc = new Locator("AMOUNT", By.xpath("//table/tbody/tr[1]/td[" + amountTableIndex + "]"));
        Locator remarksLoc = new Locator("REMARKS", By.xpath("//table/tbody/tr[1]/td[" + remarksTableIndex + "]"));

        return driverTools.getText(resellerIdLoc).orElseThrow().equalsIgnoreCase(id) &&
                driverTools.getText(amountLoc).orElseThrow().contains(amount) &&
                driverTools.getText(remarksLoc).orElseThrow().equalsIgnoreCase(remarks);
    }

    private int getIndexOfTableColumn(String colTitle) {
        List<WebElement> elements = driverTools.getDriver().findElements(
                By.xpath("//th[text()='" + colTitle +"']/preceding-sibling::th")
        );
        return elements.size()+1;
    }

    public void verifyNewTransactionInGrid(String expectedResellerName, String expectedAmount) {
        WebElement firstRow = driverTools.waitForElementToAppear(GRID_FIRST_ROW).orElseThrow(() -> new NoSuchElementException("No data found in the transaction grid."));
        Assertion.assertThat("Verify new data is displayed in the grid", firstRow.isDisplayed(), is(true));

        String actualResellerName = driverTools.waitForElementToAppear(FIRST_ROW_RESELLER_NAME).orElseThrow().getText().trim();
        Assertion.assertThat("Verify reseller name is the same as inputted", actualResellerName, is(equalTo(expectedResellerName)));

        String actualAmount = driverTools.waitForElementToAppear(FIRST_ROW_TRANSACTION_AMOUNT).orElseThrow().getText().trim();
        String normalizedAmount = actualAmount.replace(",", "");
        Assertion.assertThat("Verify transaction amount is the same as inputted", normalizedAmount, containsString(expectedAmount));
    }

    public void verifySnackbarMessage(String expectedMessage) {
        WebElement snackbar = driverTools.waitForElementToAppear(SUCCESS_SNACKBAR_MSG, 5).orElseThrow(() -> new NoSuchElementException("Snackbar did not appear in the DOM."));
        Assertion.assertThat("Verify snackbar appears on screen", snackbar.isDisplayed(), is(true));
        Assertion.assertThat("Verify snackbar message content", snackbar.getText().trim(), containsString(expectedMessage));
        boolean isDisappeared = driverTools.waitForElementToDisappear(SUCCESS_SNACKBAR_MSG,5);
        Assertion.assertThat("Verify snackbar successfully disappears", isDisappeared, is(true));
    }

    public void verifyApprovedTransactionInHistoryGrid(String expectedResellerName, String expectedAmount) {
        int resellerNameColIndex = getIndexOfTableColumn("Reseller Name");
        int amountColIndex = getIndexOfTableColumn("Transaction Amount with Currency");
        int statusColIndex = getIndexOfTableColumn("Status");

        Locator actualResellerLoc = new Locator("HISTORY_FIRST_ROW_RESELLER", By.xpath("//table/tbody/tr[1]/td[" + resellerNameColIndex + "]"));
        Locator actualAmountLoc = new Locator("HISTORY_FIRST_ROW_AMOUNT", By.xpath("//table/tbody/tr[1]/td[" + amountColIndex + "]"));
        Locator actualStatusLoc = new Locator("HISTORY_FIRST_ROW_STATUS", By.xpath("//table/tbody/tr[1]/td[" + statusColIndex + "]"));

        driverTools.waitForElementToAppear(actualResellerLoc, 5).orElseThrow(() -> new NoSuchElementException("No data found in Request History grid."));

        String actualResellerName = driverTools.getText(actualResellerLoc).orElseThrow().trim();
        String actualAmount = driverTools.getText(actualAmountLoc).orElseThrow().trim();
        String actualStatus = driverTools.getText(actualStatusLoc).orElseThrow().trim();

        String normalizedAmount = actualAmount.replace(",", "");
        Assertion.assertThat("Verify reseller name is the same as inputted", actualResellerName, is(equalTo(expectedResellerName)));
        Assertion.assertThat("Verify transaction amount is the same as inputted", normalizedAmount, containsString(expectedAmount));
        Assertion.assertThat("Verify transaction is marked as Approved", actualStatus.toLowerCase(), is(equalTo("approved")));
    }
}
