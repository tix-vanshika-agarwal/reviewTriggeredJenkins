package com.tiket.web.dashboard.page.platform.eiffel.loyalty;

import com.google.common.collect.Iterables;
import com.tiket.dbclient.TiketMysqlDBManager;
import com.tiket.dto.LoyaltyPointsDTO;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.BaseTest;
import nl.jiankai.mapper.ResultSetMapperFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.sql.SQLDataException;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class ApprovalPointPage<T extends WebDriver> extends BasePage<T> {
    private static final Locator APPROVAL_PAGE = new Locator("APPROVAL_PAGE", new By.ByXPath("//span[text()='Approval Point']"));
    private static final Locator APPROVAL_BUTTON = new Locator("APPROVAL_BUTTON", new By.ByXPath("//button[.='Approval']"));
    private static final Locator REJECT_BUTTON = new Locator("REJECT_BUTTON", new By.ByXPath(" (//button[.='Reject'])[1]"));
    private static final Locator ACCOUNT_ID = new Locator("ACCOUNT_ID", new By.ByXPath("//tbody[contains(@class,'Table_table_body')]//td[2]"));
    private static final Locator LAST_ROW = new Locator("LAST_ROW", new By.ByXPath("//tbody[contains(@class,'Table_table_body')]//tr[last()]"));
    private static final Locator AMOUNT = new Locator("AMOUNT", new By.ByXPath("//tbody[contains(@class,'Table_table_body')]//td[8]"));
    private static final Locator YES_ICON = new Locator("YES_ICON", new By.ByXPath("//button[.='Yes']"));
    private static final Locator APPROVED_MESSAGE = new Locator("APPROVED_MESSAGE", new By.ByXPath("//div[text()='REQUEST SUCCESSFULLY APPROVED']"));
    private static final Locator REJECTED_MESSAGE = new Locator("REJECTED_MESSAGE", new By.ByXPath("//div[text()='REQUEST SUCCESSFULLY REJECTED']"));
    private static final Locator SEE_DETAILS_BUTTON = new Locator("SEE_DETAILS_BUTTON", By.xpath("(//button[contains(@class, 'ApprovalBulkList_see_detail_button')])[last()]"));
    private static final Locator MODAL_SECTION = new Locator("MODAL_SECTION", By.xpath("//section[contains(@class, 'Modal_modal_popup')]"));
    private static final Locator DETAILS_TABLE_ROW = new Locator("DETAILS_TABLE_ROW", By.xpath(".//tbody[contains(@class, 'Table_table_body')]/tr"));
    private static final Locator BULK_REJECT_BUTTON = new Locator("BULK_REJECT_BUTTON", new By.ByXPath(" (//button[.='Reject'])[last()]"));
    private static final Locator CLOSE_MODAL_BTN = new Locator("CLOSE_MODAL_BTN", new By.ByXPath(" //button[contains(@class, 'Modal_modal_close')]"));

    private static final int CONFIRM_MAX_ATTEMPT = 3;

    public ApprovalPointPage(Session<T> session) {
        super(session);
    }

    public void openApprovalPage() {
        WebElement approvalPage = driverTools.waitForElementToAppear(APPROVAL_PAGE).orElseThrow();
        driverTools.scrollIntoWebView(approvalPage);
        driverTools.clickWithJavascriptExecutor(approvalPage);
    }

    public void clickApprovalButton() {
        driverTools.waitForElementToAppear(APPROVAL_BUTTON).orElseThrow();
        List<WebElement> approvalButtons = driverTools.elements(APPROVAL_BUTTON).orElseThrow();
        WebElement approvalButton = Iterables.getLast(approvalButtons);
        driverTools.clickWithJavascriptExecutor(approvalButton);
        confirmWithYes();
    }

    public void clickRejectIcon() {
        WebElement rejectButton = driverTools.waitForElementToAppear(REJECT_BUTTON).orElseThrow();
        driverTools.click(rejectButton);
        confirmWithYes();
    }

    public void clickRejectIconBulk() {
        driverTools.waitForElementToAppear(BULK_REJECT_BUTTON).ifPresent(driverTools::scrollElementToCenter);
        driverTools.click(BULK_REJECT_BUTTON);
        confirmWithYes();
    }

    /**
     * Clicks the "Yes" button of the confirmation modal.
     *
     * <p>The modal fades in on top of a {@code BaseModal_modal_overlay} backdrop. Selenium reports the
     * button as clickable as soon as it is displayed, but while the backdrop is still animating it
     * swallows the native click and the driver throws {@code ElementClickInterceptedException}. The
     * click therefore falls back to a JavaScript click (which is not blocked by the overlay), and the
     * whole attempt is repeated until the modal is actually gone - the only reliable signal that the
     * confirmation was registered.
     */
    private void confirmWithYes() {
        for (int attempt = 1; attempt <= CONFIRM_MAX_ATTEMPT; attempt++) {
            WebElement yesButton = driverTools.waitForElementToBeClickable(YES_ICON, 10)
                    .orElseThrow(() -> new AssertionError("Confirmation modal Yes button never became clickable"));
            driverTools.clickOrFallbackToClickWithJavascriptExecutor(yesButton);
            if (driverTools.waitForElementToDisappear(YES_ICON, 5)) {
                return;
            }
            logger.log("Confirmation modal is still open after clicking Yes (attempt " + attempt + "/"
                    + CONFIRM_MAX_ATTEMPT + "), retrying");
        }
        throw new AssertionError("Confirmation modal did not close after clicking Yes "
                + CONFIRM_MAX_ATTEMPT + " times");
    }

    public boolean getAccountId(String input) {
        WebElement account_id = driverTools.waitForElementToAppear(ACCOUNT_ID, 3).orElseThrow();
        return driverTools.getText(account_id).contains(input);
    }

    public boolean Amount(String input) {
        WebElement amount = driverTools.waitForElementToAppear(AMOUNT, 3).orElseThrow();
        return driverTools.getText(amount).contains(input);
    }

    public HashMap<String, Object> getDataDB(String accountID) throws Exception {
        HashMap<String, Object> map = new HashMap<>();
        try {
            String sqlQuery = "select * from tix_loyalty_core.loyalty_point_transactions lpt WHERE account_id  =" + accountID + " order by id DESC limit 1;";
            var db = TiketMysqlDBManager.getInstance("loyalty");
            var dbExecute = ResultSetMapperFactory.getResultSetMapperIdentity().map(db.executeQuery(sqlQuery), LoyaltyPointsDTO.class);
            LoyaltyPointsDTO result;
            if (dbExecute.stream().count() < 1) {
                throw new SQLDataException("Data not found in Database!");
            } else {
                result = dbExecute.get(0);
            }
            logger.log("Data found in Database : \n" + result.toString());
            map.put("status", result.getStatus());
            map.put("account_id", result.getAccount_id());
            map.put("type", result.getType());
            map.put("process_type", result.getProcess_type());
            map.put("amount", result.getAmount());
        } catch (SQLException e) {
            Assertion.skip("Error while fetching data from database: " + e.getMessage());
        }
        return map;
    }

    public Boolean verifyContains(String actual, String expected) {
        logger.log("This is actual value " + actual);
        logger.log("This is expected value " + expected);
        return expected.contains(actual);
    }

    public boolean successMessage(String input) {
        WebElement successMessage = driverTools.waitForElementToAppear(APPROVED_MESSAGE, 10).orElseThrow();
        return driverTools.getText(successMessage).contains(input);
    }

    public boolean rejectedMessage(String input) {
        WebElement rejectedMessage = driverTools.waitForElementToAppear(REJECTED_MESSAGE, 3).orElseThrow();
        return driverTools.getText(rejectedMessage).contains(input);
    }

    public boolean checkUpload(String Id) {
        WebElement data = driverTools.waitForElementToAppear(LAST_ROW, 3).orElseThrow();
        return driverTools.getText(data).contains(Id);
    }

    public boolean isApprovalEntryDisplayed() {
        return driverTools.waitForElementToAppear(APPROVAL_BUTTON, 5).isPresent();
    }

    public boolean isDeleteEntryPresentForAccountId(String accountId) {
        Locator entryLocator = deleteEntryLocator(accountId);
        return driverTools.waitForElementToAppear(entryLocator, 10).isPresent();
    }

    public int countDeleteEntriesForAccountId(String accountId) {
        Locator tableBody = new Locator("TABLE_BODY", new By.ByXPath("//tbody[contains(@class,'Table_table_body')]"));
        driverTools.waitForElementToAppear(tableBody, 10).orElseThrow(() -> new AssertionError(tableBody.name() + " not found after search"));
        Locator entryLocator = deleteEntryLocator(accountId);
        return driverTools.elements(entryLocator).map(List::size).orElse(0);
    }

    public void clickApprovalButtonForDeleteEntry(String accountId) {
        Locator entryLocator = new Locator("APPROVAL_FOR_DELETE_ENTRY",
                new By.ByXPath(String.format(
                        "(//tbody[contains(@class,'Table_table_body')]//tr[td[normalize-space()='%s'] and td[normalize-space()='DELETE']]//button[.='Approval'])[last()]",
                        accountId)));
        WebElement approvalButton = driverTools.waitForElementToAppear(entryLocator, 10).orElseThrow();
        driverTools.scrollElementToCenter(approvalButton);
        driverTools.clickWithJavascriptExecutor(approvalButton);
        confirmWithYes();
    }

    private static Locator deleteEntryLocator(String accountId) {
        return new Locator("DELETE_ENTRY_" + accountId,
                new By.ByXPath(String.format(
                        "//tbody[contains(@class,'Table_table_body')]//tr[td[normalize-space()='%s'] and td[normalize-space()='DELETE']]//button[.='Approval']",
                        accountId)));
    }

    public void waitForSnackbarToDisappearAndReload() {
        Locator snackbar = new Locator("SNACKBAR", new By.ByXPath("//div[contains(text(),'SUCCESSFULLY')]"));
        driverTools.waitForElementToDisappear(snackbar, 10);
        driverTools.get(driverTools.getDriver().getCurrentUrl());
        driverTools.waitForWebToLoad();
        Locator tableBody = new Locator("TABLE_BODY", new By.ByXPath("//tbody[contains(@class,'Table_table_body')]"));
        driverTools.waitForElementToAppear(tableBody, 10);
    }

    public void openPointApprovalBulkList() {
        new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(d -> d.getCurrentUrl().contains("approval-point"));

        JavascriptExecutor js = (JavascriptExecutor) driverTools.getDriver();
        js.executeScript("window.location.search = '?tab=approval-bulk';");
        logger.log("Navigated directly to approval-bulk tab via URL");

        driverTools.waitForWebToLoad();

        String xpathExpr = "//tbody[contains(@class, 'Table_table_body') "
                + "and .//button[contains(@class, 'ApprovalBulkList_see_detail_button')]]";
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .ignoring(Exception.class)
                .until(d -> {
                    Object result = ((JavascriptExecutor) d).executeScript(
                            "var node = document.evaluate(arguments[0], document, null, "
                          + "    XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;"
                          + "return node !== null && node.children.length > 0;",
                            xpathExpr);
                    return Boolean.TRUE.equals(result);
                });
    }

    public void checkUploadedFileInfo(String[] expectedInfo) {
        JavascriptExecutor js = (JavascriptExecutor) driverTools.getDriver();
        String tbodyXpath = "//tbody[contains(@class, 'Table_table_body') "
                + "and .//button[contains(@class, 'ApprovalBulkList_see_detail_button')]]";

        new WebDriverWait(driver, Duration.ofSeconds(15))
                .ignoring(Exception.class)
                .until(d -> {
                    Object result = js.executeScript(
                            "var tbody = document.evaluate(arguments[0], document, null, "
                          + "    XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;"
                          + "if (!tbody) return false;"
                          + "var lastRow = tbody.querySelector('tr:last-child');"
                          + "if (!lastRow) return false;"
                          + "var text = lastRow.textContent.trim();"
                          + "return text.length > 5;",
                            tbodyXpath);
                    return Boolean.TRUE.equals(result);
                });

        @SuppressWarnings("unchecked")
        List<String> cellTexts = (List<String>) js.executeScript(
                "var tbody = document.evaluate(arguments[0], document, null, "
              + "    XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;"
              + "var lastRow = tbody.querySelector('tr:last-child');"
              + "var cells = lastRow.querySelectorAll('td');"
              + "var result = [];"
              + "for (var i = 0; i < cells.length; i++) {"
              + "    result.push(cells[i].textContent.trim());"
              + "}"
              + "return result;",
                tbodyXpath);

        String[] actualInfo = cellTexts.toArray(new String[0]);
        logger.log("actual info: " + Arrays.toString(actualInfo));
        logger.log("expected info: " + Arrays.toString(expectedInfo));
        Assertion.assertThat("Verify file name uploaded is correct", expectedInfo[1], is(actualInfo[1]));
        Assertion.assertThat("Verify point type for the file is correct", expectedInfo[2], is(actualInfo[2]));
        Assertion.assertThat("Verify total data is correct", expectedInfo[3], is(actualInfo[3]));
        Assertion.assertThat("Verify uploader is correct", expectedInfo[4], is(actualInfo[4]));
    }

    public void checkUploadedFileDetails(String[][] expectedInfo) {
        JavascriptExecutor js = (JavascriptExecutor) driverTools.getDriver();

        String seeDetailXpath = "(//button[contains(@class, 'ApprovalBulkList_see_detail_button')])[last()]";
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .ignoring(Exception.class)
                .until(d -> {
                    Object node = js.executeScript(
                            "var btn = document.evaluate(arguments[0], document, null, "
                          + "    XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;"
                          + "if (!btn) return false;"
                          + "btn.scrollIntoView({block: 'center'});"
                          + "return true;",
                            seeDetailXpath);
                    return Boolean.TRUE.equals(node);
                });

        WebElement seeDetailsBtn = driverTools.waitForElementToPresence(SEE_DETAILS_BUTTON, 5).orElseThrow();
        driverTools.clickWithJavascriptExecutor(seeDetailsBtn);

        driverTools.waitForElementToAppear(MODAL_SECTION, 10).orElseThrow();
        List<WebElement> trElems = driverTools.elements(DETAILS_TABLE_ROW, driverTools.element(MODAL_SECTION).orElseThrow()).orElseThrow();
        Locator tdElement = new Locator("TD Element", By.xpath("./td"));
        String[][] tableData = new String[trElems.size()][];

        for (int i = 0; i < trElems.size(); i++) {
            List<WebElement> tds = driverTools.elements(tdElement, trElems.get(i)).orElseThrow();
            tableData[i] = new String[tds.size()];
            for (int j = 0; j < tds.size(); j++) {
                tableData[i][j] = (String) js.executeScript("return arguments[0].textContent.trim();", tds.get(j));
            }
        }

        logger.log("actualData:\n" + Arrays.deepToString(tableData));
        logger.log("expectedData:\n" + Arrays.deepToString(expectedInfo));
        for (int i = 0; i < expectedInfo.length; i++) {
            BaseTest.mainLogger.get().step("Verifying data number " + (i + 1));
            Assertion.assertThat("Verify accountId is the same", tableData[i][0], is(expectedInfo[i][0]));
            Assertion.assertThat("Verify email is present", tableData[i][1], is(notNullValue()));
            Assertion.assertThat("Verify amount added is correct", tableData[i][2], is(expectedInfo[i][1]));
            Assertion.assertThat("Verify Note added is correct", tableData[i][4], is(expectedInfo[i][2]));
        }

        driverTools.click(CLOSE_MODAL_BTN);
    }
}
