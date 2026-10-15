package com.tiket.web.dashboard.page.nft.airport_transfer;

import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import com.tiket.web.dashboard.page.member_dashboard.MemberLoginPage;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.time.Instant;

import static org.hamcrest.core.Is.is;

public class AirportTransferVendorPage<T extends WebDriver> extends BasePage<T> {
    private static final Locator USERNAME_GREETING = new Locator("USERNAME_GREETING", new By.ByXPath("//div[contains(@class, 'header_dashboard')]"));
    private static final Locator ORDER_ID_TRANSACTION_FILTER = new Locator("ORDER_ID_TRANSACTION_FILTER", new By.ByXPath("(//input[@class='input-text v4 mobile'])[1]"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", new By.ByXPath("//button[@class='tix-button v4 search mobile button waves-effect waves-default']"));
    private static final Locator RESET_BUTTON = new Locator("RESET_BUTTON", new By.ByXPath("//button[@class='tix-button v4 reset mobile button waves-effect waves-secondary secondary']"));
    private static final Locator EXPORT_TRANSACTION_BUTTON = new Locator("EXPORT_TRANSACTION_BUTTON", new By.ByXPath("//button[@class='tix-button v4 mobile button waves-effect waves-default']"));
    private static final Locator ORDER_ID_FILTER = new Locator("ORDER_ID_FILTER", new By.ByXPath("(//input[@class='input-text v4'])[1]"));
    private static final Locator ALL_FILTER = new Locator("ALL_FILTER", new By.ByXPath("//div[@class='chip-filters first active']"));
    private static final Locator ORDER_VIEW = new Locator("ORDER_VIEW", new By.ByXPath("//div[@class='table-container']"));
    private static final Locator NOTE_TEXT_FIELD = new Locator("NOTE_TEXT_FIELD", new By.ByXPath("(//textarea)[last()]"));
    private static final Locator CONFIRMATION_BUTTON = new Locator("CONFIRMATION_BUTTON", new By.ByXPath("(//button[text()='Konfirmasi'])[last()]"));
    private static final Locator SELECT_ADMIN = new Locator("SELECT_ADMIN", new By.ByXPath("//div[contains(@class, 'select-admin-name__placeholder')]"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", new By.ByXPath("//button[@class='tix-button v4 button waves-effect waves-default block']"));
    private static final Locator SUCCESS_SNACKBAR = new Locator("SUCCESS_SNACKBAR", new By.ByXPath("//div[@class='tix-snackbar v4 type-default out-slide visible']"));
    private static final Locator ELLIPSIS_BUTTON = new Locator("ELLIPSIS_BUTTON", new By.ByXPath("(//div[@class='button_with_dropdown'])[last()]"));
    private static final Locator DOWNLOAD_VOUCHER_BUTTON = new Locator("DOWNLOAD_VOUCHER_BUTTON", new By.ByXPath("(//span[text()='Download Voucher'])[last()]"));
    private static final Locator ORDER_DATE_FILTER = new Locator("ORDER_DATE_FILTER", new By.ByXPath("//div[@class='desktop-filter-date field start']//input[@class='input-text v4']"));
    private static final Locator FIRST_DATE_CALENDAR_ORDER = new Locator("FIRST_DATE_CALENDAR_ORDER", new By.ByXPath("(//div[contains(@class,'DayPicker-Day') and normalize-space(text())='1' and @aria-disabled='false'])[1]"));
    private static final Locator LAST_DATE_CALENDAR_ORDER = new Locator("LAST_DATE_CALENDAR_ORDER", new By.ByXPath("(//div[contains(@class,'DayPicker-Day') and normalize-space(text())='28' and @aria-disabled='false'])[1]"));
    private static final Locator SAVE_DATE_BUTTON_ORDER = new Locator("SAVE_DATE_BUTTON", new By.ByXPath("//button[@class='tix-button v4 button waves-effect waves-default']"));
    private static final Locator LOADING_POPUP = new Locator("LOADING_POPUP", new By.ByXPath("//div[@class='popup-container']"));
    private static final Locator EXPORT_BUTTON = new Locator("EXPORT_BUTTON", new By.ByXPath("//button[@class='tix-button v4 btn-filter button waves-effect waves-default']"));
    private static final Locator CSV_BUTTON = new Locator("CSV_BUTTON", new By.ByXPath("//button[@class='tix-button v4 btn-filter-csv button waves-effect waves-default block']"));

    public AirportTransferVendorPage(Session<T> session) {
        super(session);
    }

    public boolean isAirportTransferLoggedIn() {
        driverTools.waitForElementToAppear(USERNAME_GREETING, 2);
        return driverTools.isElementDisplayed(USERNAME_GREETING);
    }

    public void openAirportTransferVendorPage(String username, String password, String menu) {
        driverTools.maximizeWindow();
        new MemberLoginPage<>(session).adminLoginViaCookie(username, password);

        if (menu.equals("finance")) {
            if (Configs.ENVIRONMENT == Environment.GK) {
                driverTools.get("https://gatotkaca.tiket.com/connect/airport-transfer/payment-list");
            } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
                driverTools.get("https://preprod.tiket.com/connect/airport-transfer/payment-list");
            }
        } else {
            if (Configs.ENVIRONMENT == Environment.GK) {
                driverTools.get("https://gatotkaca.tiket.com/connect/airport-transfer/" + menu + "/incoming");
            } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
                driverTools.get("https://preprod.tiket.com/connect/airport-transfer/" + menu + "/incoming");
            }
        }
    }


    public void filterTransactionData(String orderId) {
        driverTools.clickWithJavascriptExecutor(ORDER_ID_TRANSACTION_FILTER);
        driverTools.sendKeys(ORDER_ID_TRANSACTION_FILTER, orderId);

        driverTools.waitForElementToAppear(SEARCH_BUTTON).ifPresent(driverTools::clickWithJavascriptExecutor);

        Locator RESULT = new Locator("RESULT", new By.ByXPath("//span[@class='b3 high-emphasis hover tix-font v4'][text()='" + orderId + "']"));
        driverTools.waitForElementToAppear(RESULT, 2);
        Assertion.assertThat("The result of transaction data is displayed", driverTools.isElementDisplayed(RESULT), is(true));
    }

    public void resetSearchTransactionData() {
        driverTools.clickWithJavascriptExecutor(RESET_BUTTON);
    }

    public void verifyResetSearchTransactionData() {
        java.util.Optional<org.openqa.selenium.WebElement> orderFilterElement = driverTools.waitForElementToAppear(ORDER_ID_TRANSACTION_FILTER);
        String orderFilterValue = orderFilterElement.map(e -> driverTools.getValueAttribute(e, "value")).orElse("");
        Assertion.assertThat("Order ID filter is empty", orderFilterValue.isEmpty(), is(true));
    }

    public void exportTransactionData() {
        driverTools.clickWithJavascriptExecutor(EXPORT_TRANSACTION_BUTTON);
        WebElement currentElement = driver.switchTo().activeElement();
        currentElement.sendKeys(Keys.RETURN);
    }

    public boolean verifyDownloadedExport(String fileName, String timeOut) {
        String downloadDir = System.getProperty("user.home") + File.separator + "Downloads";
        logger.log("Download folder: " + downloadDir);
        logger.log("Expected file name: " + fileName);
        boolean fileDownloaded = false;

        long startTime = Instant.now().toEpochMilli();
        logger.log("Start time: " + startTime);
        long waitTime = startTime + Integer.parseInt(timeOut);
        logger.log("Wait time: " + waitTime);

        while (Instant.now().toEpochMilli() < waitTime) {
            File[] listFiles = new File(downloadDir).listFiles();
            for (File file : listFiles) {
                if (file.getName().contains(fileName)) {
                    String[] listFilesName = new File(downloadDir).list();
                    for (int i = 0; i < listFilesName.length; i++) {
                        logger.log("File name on dir: " + listFilesName[i]);
                    }
                    fileDownloaded = true;
                    file.delete();
                    break;
                }
            }
            if (fileDownloaded) break;
        }
        return fileDownloaded;
    }

    public boolean filterByOrderId(String orderId) {
        driverTools.clickWithJavascriptExecutor(ORDER_ID_FILTER);
        driverTools.clear(ORDER_ID_FILTER);
        WebElement currentElement = driver.switchTo().activeElement();
        currentElement.sendKeys(orderId);
        currentElement.sendKeys(Keys.RETURN);

        Locator FILTER_RESULT = new Locator("FILTER_RESULT", new By.ByXPath("//td[text()='" + orderId + "']"));
        driverTools.waitForElementToAppear(FILTER_RESULT, 2);
        return driverTools.isElementDisplayed(FILTER_RESULT);
    }

    public void openOrderTypeTab(String type) {
        Locator ORDER_TYPE_TAB = new Locator("ORDER_TYPE_TAB", AppiumBy.xpath("//a[@tabid='" + type + "']"));
        driverTools.waitForElementToAppear(ORDER_TYPE_TAB);
        driverTools.clickWithJavascriptExecutor(ORDER_TYPE_TAB);
    }

    public void filterByTransferType(String type) {
        if (type.equals("All")) {
            driverTools.clickWithJavascriptExecutor(ALL_FILTER);
        } else {
            driverTools.clickWithJavascriptExecutor(new Locator("TRANSFER_TYPE_FILTER", AppiumBy.xpath("//div[@class='chip-filters'][contains(text(), '" + type + "')]")));
        }
    }

    public boolean addNoteIncomingOrder(String note) {
        driverTools.clickWithJavascriptExecutor(NOTE_TEXT_FIELD);
        driverTools.clear(NOTE_TEXT_FIELD);
        driverTools.sendKeys(NOTE_TEXT_FIELD, note);
        driverTools.clickWithJavascriptExecutor(CONFIRMATION_BUTTON);
        driverTools.clickWithJavascriptExecutor(SAVE_BUTTON);
        Locator NOTE_TEXT = new Locator("NOTE_TEXT", new By.ByXPath("(//textarea[text()='" + note + "'])[last()]"));
        driverTools.waitForElementToAppear(NOTE_TEXT, 5);
        return driverTools.isElementDisplayed(NOTE_TEXT);
    }

    public void confirmIncomingOrder() {
        driverTools.clickWithJavascriptExecutor(CONFIRMATION_BUTTON);
        driverTools.clickWithJavascriptExecutor(SELECT_ADMIN);
        WebElement currentElement = driver.switchTo().activeElement();
        currentElement.sendKeys(Keys.RETURN);
        driverTools.clickWithJavascriptExecutor(SAVE_BUTTON);
    }

    public boolean verifyOrderView() {
        driverTools.waitForElementToAppear(ORDER_VIEW, 2);
        return driverTools.isElementDisplayed(ORDER_VIEW);
    }

    public boolean verifySuccessSnackbar() {
        driverTools.waitForElementToAppear(SUCCESS_SNACKBAR, 2);
        return driverTools.isElementDisplayed(SUCCESS_SNACKBAR);
    }

    public void downloadVoucher() {
        driverTools.clickWithJavascriptExecutor(ELLIPSIS_BUTTON);
        driverTools.clickWithJavascriptExecutor(DOWNLOAD_VOUCHER_BUTTON);
        WebElement currentElement = driver.switchTo().activeElement();
        currentElement.sendKeys(Keys.RETURN);
    }

    public void exportOrderData() {
        driverTools.clickWithJavascriptExecutor(ORDER_DATE_FILTER);
        driverTools.clickWithJavascriptExecutor(FIRST_DATE_CALENDAR_ORDER);
        driverTools.clickWithJavascriptExecutor(LAST_DATE_CALENDAR_ORDER);
        driverTools.clickWithJavascriptExecutor(SAVE_DATE_BUTTON_ORDER);

        if (driverTools.isElementDisplayed(LOADING_POPUP)) {
            driverTools.waitForElementToDisappear(LOADING_POPUP);
        }

        driverTools.clickWithJavascriptExecutor(EXPORT_BUTTON);
        driverTools.clickWithJavascriptExecutor(CSV_BUTTON);
        WebElement currentElement = driver.switchTo().activeElement();
        currentElement.sendKeys(Keys.RETURN);
    }
}