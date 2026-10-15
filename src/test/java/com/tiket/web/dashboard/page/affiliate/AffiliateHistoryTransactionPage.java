package com.tiket.web.dashboard.page.affiliate;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class AffiliateHistoryTransactionPage<T extends WebDriver> extends BasePage<T> {
    public AffiliateHistoryTransactionPage(Session<T> session) {
        super(session);
    }

    private static final Locator FINANCE_SECTION = new Locator("FINANCE_SECTION", By.xpath("//span[normalize-space()='Finance']"));
    private static final Locator DROPDOWN_MENU = new Locator("DROPDOWN_MENU", By.xpath("//span[contains(text(),'History Transaction')]"));
    private static final Locator HISTORY_TRANSACTION_API = new Locator("HISTORY_TRANSACTION_API", By.xpath("//span[normalize-space()='History Transaction (API)']"));
    private static final Locator TABLEAU_TABLE = new Locator("TABLEAU_TABLE", By.xpath("//tableau-viz"));
    private static final Locator ORDER_TYPE_DROPDOWN = new Locator("ORDER_TYPE_DROPDOWN", By.xpath("//input[@id='order-type']/ancestor::div[contains(@class, 'styles_input_inner_wrapper')]" + "//div[contains(@class, 'DropdownFormDisplay_icon_container')]"));
    private static final Locator TYPE_DROPDOWN = new Locator("TYPE_DROPDOWN", By.xpath("//input[@id='type']/ancestor::div[contains(@class, 'styles_input_inner_wrapper')]" + "//div[contains(@class, 'DropdownFormDisplay_icon_container')]"));
    private static final Locator SEARCH_DATE_BY_DROPDOWN = new Locator("SEARCH_DATE_BY_DROPDOWN", By.xpath("//input[@id='search-date-by']/ancestor::div[contains(@class, 'styles_input_inner_wrapper')]" + "//div[contains(@class, 'DropdownFormDisplay_icon_container')]"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", By.xpath("//button[@data-testid='dropdown-apply-button']"));
    private static final Locator START_PAYMENT_DATE_INPUT = new Locator("START_PAYMENT_DATE_INPUT", By.id("start-payment-date"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", By.xpath("//button[.//span[normalize-space(text())='Search' or normalize-space(text())='Cari']]"));
    private static final Locator RESET_BUTTON = new Locator("RESET_BUTTON", By.xpath("//button[.//span[normalize-space()='Reset Filter']]"));
    private static final Locator NO_DATA_MESSAGE = new Locator("NO_DATA_MESSAGE", By.xpath("//h2[normalize-space(text())='There is no data yet' or normalize-space(text())='Belum ada data']"));
    private static final Locator DOWNLOAD_BUTTON = new Locator("DOWNLOAD_BUTTON", By.xpath("//button[normalize-space()='Download' or normalize-space()='Unduh']"));
    private static final Locator SUCCESS_DOWNLOAD_MESSAGE = new Locator("SUCCESS_DOWNLOAD_MESSAGE", By.xpath("//div[contains(@class, 'Snackbar_text_content') and (text()='Successfully download reseller report' or text()='Berhasil mengunduh laporan reseller')]"));
    private static final Locator SEARCH_DATE_BY_OPTION = new Locator("SEARCH_DATE_BY_OPTION", By.xpath("//div[@data-testid='option-single-0' and contains(@class, 'DropdownMenu_dropdown_option')]//p[normalize-space(text())='Payment Date']"));
    private static final Locator PRODUCT_AFFILIATE_OPTION = new Locator("PRODUCT_AFFILIATE_OPTION", By.xpath(String.format("//div[contains(@class, 'DropdownMenu_dropdown_option')]//span[contains(@class, 'Check_children') and normalize-space(text())='Hotel']")));
    private static final Locator PREVIOUS_MONTH_BUTTON = new Locator("PREVIOUS_MONTH_BUTTON", By.xpath("//button[@aria-label='Previous month']"));
    private static final Locator HISTORY_TRANSACTION_NEW = new Locator("HISTORY_TRANSACTION_NEW", By.xpath("//span[text()='History Transaction (New)']"));
    private static final Locator ORDERTABLE = new Locator("ORDERTABLE", By.xpath("//span[normalize-space()='Order Table']"));
    private static final Locator AFFILIATE_REFERENCE_ID_CHIP = new Locator("AFFILIATE_REFERENCE_ID_CHIP", By.xpath("//button[contains(@class, 'Chip_chip') and .//span[normalize-space(text())='Affiliate Reference ID']]"));
    private static final Locator AFFILIATE_REF_ID_INPUT = new Locator("AFFILIATE_REF_ID_INPUT", By.id("affiliate-ref-id"));
    private static final Locator ORDER_ID_INPUT = new Locator("ORDER_ID_INPUT", By.id("order-id"));
    private static final Locator AFFILIATE_REF_ID_VALUE = new Locator("AFFILIATE_REF_ID_VALUE", By.xpath("//p[text()='Affiliate Ref ID']/following-sibling::div//p"));
    private static final Locator ORDER_ID_VALUE = new Locator("ORDER_ID_VALUE", By.xpath("//p[text()='Order ID']/following-sibling::div//p[1]"));

    public void clickOrderTable() {
        driverTools.click(ORDERTABLE);
    }

    public void clickHistoryTransactionNew() {
        driverTools.waitForElementToBeClickable(HISTORY_TRANSACTION_NEW, 5);
        driverTools.click(HISTORY_TRANSACTION_NEW);
    }

    public boolean isHistoryTransactionNew() {
        return driverTools.getCurrentURL().contains("dashboard/transaction");
    }

    public void searchByAffiliateReferenceId(String affiliateReferenceId) {
        driverTools.click(AFFILIATE_REFERENCE_ID_CHIP);
        driverTools.waitForElementToBeClickable(AFFILIATE_REF_ID_INPUT, 5);
        driverTools.sendKeys(AFFILIATE_REF_ID_INPUT, affiliateReferenceId);
        driverTools.click(SEARCH_BUTTON);
    }

    public void searchByOrderId(String orderId) {
        driverTools.waitForElementToBeClickable(ORDER_ID_INPUT, 5);
        driverTools.sendKeys(ORDER_ID_INPUT, orderId);
        driverTools.click(SEARCH_BUTTON);
    }

    public boolean isOrderIdValueDisplayed(String orderId) {
        driverTools.waitForElementToAppear(ORDER_ID_VALUE, 5);
        return driverTools.getText(ORDER_ID_VALUE).toString().contains(orderId);
    }

    public boolean isAffiliateRefIdValueDisplayed(String affiliateRefId) {
        driverTools.waitForElementToAppear(AFFILIATE_REF_ID_VALUE, 5);
        return driverTools.getText(AFFILIATE_REF_ID_VALUE).toString().contains(affiliateRefId);
    }

    public void clickFinanceDropdown() {
        driverTools.click(FINANCE_SECTION);
    }

    public boolean isFinanceDropdown() {
        return driverTools.isElementDisplayed(DROPDOWN_MENU);
    }

    public void clickHistoryTransactionAPI() {
        driverTools.clickWithJavascriptExecutor(HISTORY_TRANSACTION_API);
    }

    public boolean isHistoryTransactionAPI() {
        return driverTools.getCurrentURL().contains("hotel-history-transaction");
    }

    public void goToHistoryTransactionPage() {
        switch (Configs.ENVIRONMENT) {
            case GK -> driverTools.get("https://gatotkaca.tiket.com/affiliate/v2/dashboard/hotel-history-transaction");
            case PREPROD -> driverTools.get("https://preprod.tiket.com/affiliate/v2/dashboard/hotel-history-transaction");
            case PROD -> driverTools.get("https://www.tiket.com/affiliate/v2/dashboard/hotel-history-transaction");
        }
    }

    public boolean isTableauTableExists() {
        driverTools.waitForElementsToAppear(TABLEAU_TABLE, 3);
        return driverTools.isElementDisplayed(TABLEAU_TABLE);
    }

    public void goToTransactionPage() {
        switch (Configs.ENVIRONMENT) {
            case GK -> driverTools.get("https://gatotkaca.tiket.com/affiliate/v2/dashboard/transaction");
            case PREPROD -> driverTools.get("https://preprod.tiket.com/affiliate/v2/dashboard/transaction");
            case PROD -> driverTools.get("https://www.tiket.com/affiliate/v2/dashboard/transaction");
        }
    }

    public void selectDropdownOption(Locator dropdown, String optionText) {
        driverTools.click(dropdown);
        Locator DROPDOWN_OPTION = new Locator("DROPDOWN_OPTION", By.xpath(String.format("//div[contains(@class, 'DropdownMenu_dropdown_option')]//span[contains(@class, 'Check_children') and normalize-space(text())='%s']", optionText)));
        driverTools.click(DROPDOWN_OPTION);
        driverTools.click(SAVE_BUTTON);
    }

    public void searchTransactionByOrderType(String OrderType, String type, String startDate, String endDate) {
        selectDropdownOption(ORDER_TYPE_DROPDOWN, OrderType);
        selectDropdownOption(TYPE_DROPDOWN, type);
        driverTools.click(START_PAYMENT_DATE_INPUT);
        goToPreviousMonthUntilDateVisible(startDate, 12);
        driverTools.click(getStartDateLocator(startDate));
        goToPreviousMonthUntilDateVisible(endDate, 12);
        driverTools.click(getEndDateLocator(endDate));
        driverTools.click(SEARCH_BUTTON);
    }

    public void searchTransactionByProductAndDate(String product, String startDate, String endDate) {
        selectDropdownOption(ORDER_TYPE_DROPDOWN, product);
        driverTools.click(START_PAYMENT_DATE_INPUT);
        goToPreviousMonthUntilDateVisible(startDate, 12);
        driverTools.click(getStartDateLocator(startDate));
        goToPreviousMonthUntilDateVisible(endDate, 12);
        driverTools.click(getEndDateLocator(endDate));
        driverTools.click(SEARCH_BUTTON);
    }

    public void searchDateBy(String product, String startDate, String endDate) {
        selectDropdownOption(ORDER_TYPE_DROPDOWN, product);
        driverTools.click(SEARCH_DATE_BY_DROPDOWN);
        driverTools.click(SEARCH_DATE_BY_OPTION);
        driverTools.click(START_PAYMENT_DATE_INPUT);
        goToPreviousMonthUntilDateVisible(startDate, 12);
        driverTools.click(getStartDateLocator(startDate));
        goToPreviousMonthUntilDateVisible(endDate, 12);
        driverTools.click(getEndDateLocator(endDate));
        driverTools.click(SEARCH_BUTTON);
    }

    public void clickOrderTypeDropdown() {
        driverTools.click(ORDER_TYPE_DROPDOWN);
    }

    public Locator getStartDateLocator(String formattedDate) {
      
        formattedDate = formattedDate.replaceFirst("^0", "");

        return new Locator(
                "START_DATE",
                By.xpath(String.format(
                        "//button[not(@disabled) and .//span[@aria-label='%s']]",
                        formattedDate))
        );
    }

    public Locator getEndDateLocator(String formattedDate) {
        return new Locator("END_DATE", By.xpath(String.format(
                "//button[not(@disabled) and .//span[@aria-label='%s']]",
                formattedDate)));
    }

    public void clickResetButton() {
        driverTools.click(RESET_BUTTON);
    }

    public void clickDownloadButton() {
        driverTools.click(DOWNLOAD_BUTTON);
    }

    public boolean isDownloadSuccessful() {
        driverTools.waitForElementToAppear(SUCCESS_DOWNLOAD_MESSAGE, 3);
        return driverTools.isElementDisplayed(SUCCESS_DOWNLOAD_MESSAGE);
    }

    public boolean isFilterReset() {
        driverTools.waitForElementToAppear(NO_DATA_MESSAGE, 3);
        return driverTools.isElementDisplayed(NO_DATA_MESSAGE);
    }

    public boolean isProductAffiliateOptionPresent() {
        driverTools.waitForElementToAppear(PRODUCT_AFFILIATE_OPTION);
        return driverTools.isElementDisplayed(PRODUCT_AFFILIATE_OPTION);
    }

    public boolean goToPreviousMonthUntilDateVisible(String formattedDate, int maxAttempts) {

        formattedDate = formattedDate.replaceFirst("^0", "");

        Locator dateLocator = new Locator(
                "DATE_LOCATOR",
                By.xpath(String.format(
                        "//button[not(@disabled) and .//span[@aria-label='%s']]",
                        formattedDate))
        );

        for (int i = 0; i < maxAttempts; i++) {

            if (driverTools.isElementDisplayed(dateLocator)) {
                return true;
            }

            driverTools.click(PREVIOUS_MONTH_BUTTON);
            driverTools.waitForElementToAppear(dateLocator, 1);
        }

        return driverTools.isElementDisplayed(dateLocator);
    }

    public boolean verifyTransactionDatesWithinRange(String startDate, String endDate) {
        String cleanStartDate = startDate.replaceAll("\\s+\\w+$", "");
        String cleanEndDate = endDate.replaceAll("\\s+\\w+$", "");

        DateTimeFormatter formatterLong = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.ENGLISH);
        DateTimeFormatter formatterShort = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.ENGLISH);
        LocalDate minDate, maxDate;
        try {
            if (cleanStartDate == null || cleanStartDate.isEmpty() || cleanEndDate == null || cleanEndDate.isEmpty()) {
                logger.log("Start date or end date is null/empty.");
                return false;
            }
            minDate = LocalDate.parse(cleanStartDate, formatterLong);
            maxDate = LocalDate.parse(cleanEndDate, formatterLong);
        } catch (Exception e) {
            logger.log("Error parsing start or end date: " + e.getMessage());
            return false;
        }

        driverTools.waitForElementToAppear(DROPDOWN_MENU, 1);
        List<WebElement> rows = driverTools.getDriver().findElements(By.xpath("//table[contains(@class,'Table_table__')]//tbody/tr[not(contains(@class,'grand_total_row'))]"));
        List<WebElement> headers = driverTools.getDriver().findElements(By.xpath("//table[contains(@class,'Table_table__')]//thead//th"));

        int transactionDateIndex = -1;
        for (int i = 0; i < headers.size(); i++) {
            String header = headers.get(i).getText().trim();
            if (header.equalsIgnoreCase("Transaction Date")) {
                transactionDateIndex = i + 1;
                break;
            }
        }

        if (transactionDateIndex == -1) {
            logger.log("Transaction Date column not found in table headers.");
            return false;
        }

        int currentYear = LocalDate.now().getYear();

        for (WebElement row : rows) {
            String dateStr = row.findElement(By.xpath("./td[" + transactionDateIndex + "]")).getText().trim();
            try {
                if (!dateStr.isEmpty()) {
                    String cleanDateStr = dateStr.trim();
                    LocalDate date;
                    try {
                        date = LocalDate.parse(cleanDateStr, formatterLong);
                    } catch (Exception e1) {
                        try {
                            date = LocalDate.parse(cleanDateStr, formatterShort);
                        } catch (Exception e2) {
                            if (cleanDateStr.matches("\\d{1,2} \\w{3}")) {
                                date = LocalDate.parse(cleanDateStr + " " + currentYear, formatterShort);
                            } else {
                                logger.log("Unrecognized date format: " + cleanDateStr);
                                return false;
                            }
                        }
                    }
                    logger.log("Transaction date value: '" + dateStr + "', parsed as: " + date + ", expected between: " + minDate + " and " + maxDate);
                    if (date.isBefore(minDate) || date.isAfter(maxDate)) {
                        logger.log("Transaction date " + date + " is out of range (" + minDate + " to " + maxDate + ").");
                        return false;
                    }
                }
            } catch (Exception e) {
                logger.log("Error parsing transaction date: " + e.getMessage());
                return false;
            }
        }
        return true;
    }
}