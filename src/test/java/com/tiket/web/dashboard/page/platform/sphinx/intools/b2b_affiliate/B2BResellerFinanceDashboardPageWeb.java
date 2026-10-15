package com.tiket.web.dashboard.page.platform.sphinx.intools.b2b_affiliate;

import com.tiket.api.b2b.affiliate.kafka.AffiliateDepositKafkaPublishPOSTApi;
import com.tiket.apiPage.AffiliateApi;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

public class B2BResellerFinanceDashboardPageWeb<T extends WebDriver> extends WebPage<T> {
    public B2BResellerFinanceDashboardPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> B2BResellerFinanceDashboardPageWeb<?> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new B2BResellerFinanceDashboardPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new B2BResellerFinanceDashboardPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    private static final Locator RESELLER_ID_INPUT = new Locator("RESELLER_ID_INPUT", By.xpath("//input[@name='Reseller ID']"));
    private static final Locator RESELLER_ID_INPUT_SAVE_BUTTON = new Locator("RESELLER_ID_INPUT_SAVE_BUTTON", By.xpath("//button[@data-testid='search-save']"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", By.xpath("//button[@data-testid='search-submit']"));
    private static final Locator ACTION_ROWS = new Locator("ACTION_ROWS", By.xpath("//td[@data-field='action']/div"));
    private static final Locator ACTION_TRANSACTIONS = new Locator("ACTION_TRANSACTIONS", By.xpath("//div[@data-component='list-item' and ./span[text()='Transactions']]"));
    private static final Locator SEARCH_BY_INPUT = new Locator("SEARCH_BY_INPUT", By.xpath("//input[@id='search-by' and @placeholder='Search by']"));
    private static final Locator SEARCH_BY_ORDER_ID_OPTION = new Locator("SEARCH_BY_ORDER_ID_OPTION", By.xpath("//label[normalize-space(text())='Tiket.com Order ID']"));
    private static final Locator ORDER_ID_INPUT = new Locator("ORDER_ID_INPUT", By.xpath("//input[@id='tiketcom-order-id']"));
    private static final Locator START_DATE_INPUT = new Locator("START_DATE_INPUT", By.xpath("//input[@data-testid='input-daterange-start-default']"));
    private static final Locator PREVIOUS_MONTH_BUTTON = new Locator("PREVIOUS_MONTH_BUTTON", By.xpath("//button[contains(@class, 'CalendarDesktop_prev_button')]"));
    private static final Locator FIRST_ROW_FINANCE_DATA = new Locator("FIRST_ROW_FINANCE_DATA", By.xpath("(//table[@data-testid='table:list']//tbody/tr)[1]"));
    private static final Locator TRANSACTION_PAGE_HEADER = new Locator("TRANSACTION_PAGE_HEADER", By.xpath("//div[normalize-space(text())='View List of Transactions by:']"));
    private static final Locator ADD_TRANSACTION_MAIN_BTN = new Locator("ADD_TRANSACTION_MAIN_BTN", By.xpath("//button[@data-testid='button-download' and contains(text(), 'Add transaction')]"));
    private static final Locator ADD_TRANSACTION_MODAL_TITLE = new Locator("ADD_TRANSACTION_MODAL_TITLE", By.xpath("//section[contains(@class, 'Modal_modal_popup')]//h2[text()='Add Transaction']"));
    private static final Locator AMOUNT_INPUT = new Locator("AMOUNT_INPUT", By.xpath("//input[@name='amount']"));
    private static final Locator ADDITION_RADIO_BTN = new Locator("ADDITION_RADIO_BTN", By.xpath("//input[@name='type' and contains(@value,'Addition')]"));
    private static final Locator MODAL_SUBMIT_BTN = new Locator("MODAL_SUBMIT_BTN", By.xpath("//footer[contains(@class, 'Modal_modal_footer')]//button[contains(text(), 'Add transaction')]"));
    private static final Locator RESELLER_NAME_INPUT = new Locator("RESELLER_NAME_INPUT", By.id("reseller-name"));
    private static final Locator SUCCESS_SNACKBAR_MSG = new Locator("SUCCESS_SNACKBAR_MSG", By.xpath("//div[contains(@class, 'Snackbar_text_content')]"));
    public void inputResellerId(String id) {
        driverTools.click(RESELLER_ID_INPUT);
        driverTools.sendKeys(RESELLER_ID_INPUT, id);
        driverTools.click(getResellerIdSelection(id));
        driverTools.click(RESELLER_ID_INPUT_SAVE_BUTTON);
    }

    public Locator getResellerIdSelection(String id) {
        return new Locator("RESELLER_ID_SELECTION_" + id, By.xpath("//div[@class='list-content--item' and ./span[text()='" + id + "']]"));
    }

    public void clickSearchButton() {
        driverTools.scrollElementToCenter(SEARCH_BUTTON);
        driverTools.click(SEARCH_BUTTON);
        driverTools.dummyWait(2);
    }

    public void clickFirstAction() {
        List<WebElement> dropdowns = driverTools.waitForElementsToAppear(ACTION_ROWS).orElse(null);
        if (dropdowns == null || dropdowns.isEmpty())
            throw new NoSuchElementException("There are no Action Dropdowns to click");

        driverTools.click(dropdowns.get(0));
    }

    public void clickMoreActionTransactions() {
        driverTools.waitForElementToAppear(ACTION_TRANSACTIONS, 2);
        driverTools.click(ACTION_TRANSACTIONS);
    }

    public String getCurrentConfigId() {
        String url = driverTools.getCurrentURL();
        Pattern pattern = Pattern.compile("reseller/(\\d+)/");
        Matcher matcher = pattern.matcher(url);

        if (matcher.find()) {
            return matcher.group(1);
        }
        throw new NoSuchElementException();
    }

    public void hitPrerequisiteApiToRepublishOrder(String transactionId, String orderID, String orderDetailId, int baseFare, int tiketCommissionValue,
                                                   int publishedFareToReseller, int resellerSubsidyCampaignValue, int customerFinalPriceInVendorPreferredCurrency, int airlineCommission) {
        AffiliateApi affiliateApi = new AffiliateApi(this.session);
        affiliateApi.getDeleteAffiliateOrderResponseCodeWithWarning(transactionId, orderID);
        affiliateApi.getAffiliateClearLockTransactionResponseCode();
        AffiliateDepositKafkaPublishPOSTApi.Inputs affiliateDepositOrder = new AffiliateDepositKafkaPublishPOSTApi.Inputs(
                Long.parseLong(orderID), Long.parseLong(orderDetailId), baseFare, tiketCommissionValue,
                publishedFareToReseller, resellerSubsidyCampaignValue, customerFinalPriceInVendorPreferredCurrency, airlineCommission);
        // this api needs to be called twice to ensure the order is republished
        affiliateApi.getAffiliateDepositKafkaPublishResponseCode(affiliateDepositOrder);
        affiliateApi.getAffiliateDepositKafkaPublishResponseCode(affiliateDepositOrder);
    }

    public Locator getStartDateLocator(String formattedDate) {
        return new Locator("START_DATE", By.xpath(String.format("//span[@aria-label='%s']/ancestor::button", formattedDate)));
    }

    public Locator getEndDateLocator(String formattedDate) {
        return new Locator("END_DATE", By.xpath(String.format("//span[@aria-label='%s']/ancestor::button", formattedDate)));
    }

    public void selectSearchByOrderId(String orderID) {
        driverTools.waitForElementToAppear(SEARCH_BY_INPUT, 5);
        driverTools.click(SEARCH_BY_INPUT);
        driverTools.waitForElementToAppear(SEARCH_BY_ORDER_ID_OPTION, 5);
        driverTools.click(SEARCH_BY_ORDER_ID_OPTION);
        driverTools.waitForElementToAppear(ORDER_ID_INPUT, 5);
        driverTools.click(ORDER_ID_INPUT);
        driverTools.sendKeys(ORDER_ID_INPUT, orderID);
    }

    public void searchDateBy(String startDate, String endDate) {
        driverTools.waitForElementToAppear(START_DATE_INPUT, 5);
        driverTools.clickWithJavascriptExecutor(START_DATE_INPUT);
        goToPreviousMonthUntilDateVisible(startDate, 12);
        driverTools.clickWithJavascriptExecutor(getStartDateLocator(startDate));
        goToPreviousMonthUntilDateVisible(endDate, 12);
        driverTools.clickWithJavascriptExecutor(getEndDateLocator(endDate));
        driverTools.clickWithJavascriptExecutor(SEARCH_BUTTON);
    }

    public void goToPreviousMonthUntilDateVisible(String formattedDate, int maxAttempts) {
        Locator dateLocator = new Locator("DATE_LOCATOR", By.xpath(String.format("//span[@aria-label='%s']/ancestor::button", formattedDate)));
        for (int i = 0; i < maxAttempts; i++) {
            if (driverTools.isElementDisplayed(dateLocator)) {
                return;
            }
            driverTools.clickWithJavascriptExecutor(PREVIOUS_MONTH_BUTTON);
            driverTools.waitForElementToAppear(dateLocator, 1);
        }
        driverTools.isElementDisplayed(dateLocator);
    }

    public String convertNumToString(int num){
        NumberFormat formatter = NumberFormat.getInstance(new Locale("en", "IN"));
        String formatted = formatter.format(num);
        return formatted;
    }

    public boolean validateFirstRowFinanceData(String orderId, String orderDetailId, int baseFare, int tiketCommissionValue, int publishedFareToReseller, int resellerSubsidyCampaignValue, int customerFinalPriceInVendorPreferredCurrency, int airlineCommission) {
        driverTools.waitForElementToAppear(FIRST_ROW_FINANCE_DATA, 5);
        String firstRowData = driverTools.getText(FIRST_ROW_FINANCE_DATA).orElseThrow();
        logger.log("Validating first row finance data for Order ID: " + firstRowData);

        boolean containsOrderId = firstRowData.contains(orderId);
        boolean containsOrderDetailId = firstRowData.contains(orderDetailId);
        boolean containsBaseFare = firstRowData.contains(convertNumToString(baseFare));
        boolean containsTiketCommissionValue = firstRowData.contains(convertNumToString(tiketCommissionValue));
        boolean containsPublishedFareToReseller = firstRowData.contains(convertNumToString(publishedFareToReseller));
        boolean containsResellerSubsidyCampaignValue = firstRowData.contains(convertNumToString(resellerSubsidyCampaignValue));
        boolean containsCustomerFinalPriceInVendorPreferredCurrency = firstRowData.contains(convertNumToString(customerFinalPriceInVendorPreferredCurrency));
        boolean containsAirlineCommission = firstRowData.contains(String.valueOf(convertNumToString(airlineCommission)));

        return containsOrderId && containsOrderDetailId && containsBaseFare
                && containsTiketCommissionValue && containsPublishedFareToReseller
                && containsResellerSubsidyCampaignValue && containsCustomerFinalPriceInVendorPreferredCurrency
                && containsAirlineCommission;
    }

    public void verifyTransactionPageLoaded() {
        WebElement header = driverTools.waitForElementToAppear(TRANSACTION_PAGE_HEADER).orElseThrow(() -> new NoSuchElementException("Transaction page header not found. Page may not have loaded."));
        Assertion.assertThat("Verify Transaction Page is loaded successfully", header.isDisplayed(), is(true));
    }

    public void clickMainAddTransactionButton() {
        WebElement addBtn = driverTools.waitForElementToBeClickable(ADD_TRANSACTION_MAIN_BTN).orElseThrow(() -> new NoSuchElementException("Main 'Add transaction' button not found."));
        driverTools.click(addBtn);
    }

    public void verifyAddTransactionModalAppears() {
        WebElement modalTitle = driverTools.waitForElementToAppear(ADD_TRANSACTION_MODAL_TITLE).orElseThrow(() -> new NoSuchElementException("Add Transaction modal did not appear."));
        Assertion.assertThat("Verify Add Transaction Modal is visible", modalTitle.isDisplayed(), is(true));
    }

    public void enterTransactionAmount(String amount) {
        WebElement amountInput = driverTools.waitForElementToBeClickable(AMOUNT_INPUT).orElseThrow(() -> new NoSuchElementException("Amount input not found."));
        amountInput.clear();
        amountInput.sendKeys(amount);
    }

    public void selectAdditionTypeOfAmount() {
        WebElement additionRadio = driverTools.waitForElementToPresence(ADDITION_RADIO_BTN).orElseThrow(() -> new NoSuchElementException("Addition radio button not found."));
        boolean isSelected = driverTools.isElementSelected(additionRadio);
        if (!isSelected) {
            driverTools.click(additionRadio);
        }
    }

    public void clickSubmitTransactionButton() {
        WebElement submitBtn = driverTools.waitForElementToBeClickable(MODAL_SUBMIT_BTN).orElseThrow(() -> new NoSuchElementException("Submit 'Add transaction' button inside modal not found."));
        driverTools.click(submitBtn);
    }

    public String getResellerNameFromModal() {
        WebElement resellerInput = driverTools.waitForElementToAppear(RESELLER_NAME_INPUT).orElseThrow(() -> new NoSuchElementException("Reseller Name input not found in modal."));
        String resellerName = resellerInput.getAttribute("value").trim();
        return resellerName;
    }

    public void verifyTransactionSuccessSnackbar(String expectedMsg) {
        WebElement snackbar = driverTools.waitForElementToAppear(SUCCESS_SNACKBAR_MSG).orElseThrow(() -> new NoSuchElementException("Transaction success snackbar did not appear in the DOM."));
        Assertion.assertThat("Verify success snackbar appears on screen", snackbar.isDisplayed(), is(true));
        Assertion.assertThat("Verify success snackbar message content", snackbar.getText().trim(), containsString(expectedMsg));

        boolean isDisappeared = driverTools.waitForElementToDisappear(SUCCESS_SNACKBAR_MSG, 5);
        Assertion.assertThat("Verify success snackbar successfully disappears", isDisappeared, is(true));
    }
}
