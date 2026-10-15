package com.tiket.web.dashboard.page.nft.train;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.is;

public class TrainReportingSalesPage <T extends WebDriver> extends BasePage<T> {
    private static final Locator TRAIN_ADMIN_MENU = new Locator("TRAIN_ADMIN_MENU", new By.ByXPath("//div[@class='menu-list' and contains(text(),'Train Admin')]"));
    private static final Locator TRAIN_REPORTING_SALES_MENU = new Locator("TRAIN_REPORTING_SALES_MENU", new By.ByXPath("//div[@class='menu-list' and contains(text(), 'Train Admin')]/following-sibling::div[contains(@class, 'ReactCollapse--collapse')]//div[@class='submenu-list' and contains(text(), 'Train Reporting Sales')]"));
    private static final Locator LINE_CHART_TAB_LIST = new Locator("LINE_CHART", new By.ByXPath("//div[contains(@class, 'tab-list') and contains(text(),'Line Chart')]"));
    private static final Locator MONTHLY_BUTTON = new Locator("MONTHLY_BUTTON", new By.ByXPath("//button[contains(text(),'MONTHLY')]"));
    private static final Locator HAMBURGER_MENU_BUTTON = new Locator("HAMBURGER_MENU_BUTTON", new By.ByXPath("//div[@class='apexcharts-menu-icon' and @title='Menu']"));
    private static final Locator DOWNLOAD_CSV_BUTTON = new Locator("DOWNLOAD_CSV_BUTTON", new By.ByXPath("//div[@title='Download CSV']"));
    private static final Locator DATE_FROM_INPUT = new Locator("DATE_FROM_INPUT", new By.ByXPath("//div[contains(text(),'From:')]/following-sibling::div[@class='input-box']//input[@class='input-datepicker']"));
    private static final Locator DATE_PICKER_LEFT_ARROW = new Locator("DATE_PICKER_LEFT_ARROW", new By.ByXPath("//i[contains(@class, 'tix-chevron-left')]"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", new By.ByXPath("//button[contains(@class, 'form-control btn-search') and contains(text(),'SEARCH')]"));
    private static final Locator WEEKLY_BUTTON = new Locator("WEEKLY_BUTTON", new By.ByXPath("//button[contains(text(),'WEEKLY')]"));
    private static final Locator DOWNLOAD_PNG_BUTTON = new Locator("DOWNLOAD_PNG_BUTTON", new By.ByXPath("//div[@title='Download PNG']"));
    private static final Locator DAILY_BUTTON = new Locator("DAILY_BUTTON", new By.ByXPath("//button[contains(text(),'DAILY')]"));
    private static final Locator DOWNLOAD_SVG_BUTTON = new Locator("DOWNLOAD_SVG_BUTTON", new By.ByXPath("//div[@title='Download SVG']"));
    private static final Locator GENERAL_TAB_LIST = new Locator("GENERAL_TAB_LIST", new By.ByXPath("//div[contains(@class, 'tab-list') and contains(text(),'General')]"));
    private static final Locator DATE_PICKER_RIGHT_ARROW = new Locator("DATE_PICKER_RIGHT_ARROW", new By.ByXPath("//i[contains(@class, 'tix-chevron-right')]"));
    private static final Locator EXPORT_TO_EXCEL_BUTTON = new Locator("EXPORT_TO_EXCEL_BUTTON", new By.ByXPath("//button[@class='download-button' and contains(text(),'EXPORT TO EXCEL')]"));


    private static final DateTimeFormatter DATE_PICKER_FORMATTER = DateTimeFormatter.ofPattern("MMMM d, yyyy");

    public TrainReportingSalesPage(Session<T> session) {
        super(session);
    }

    public void goToTrainReportingSalesMenu(){
        driverTools.click(TRAIN_ADMIN_MENU);
        driverTools.click(TRAIN_REPORTING_SALES_MENU);
    }

    public void goToLineChartTabList(){
        driverTools.click(LINE_CHART_TAB_LIST);
    }

    public void downloadCsvReportingSalesMonthly(){
        LocalDate todayDate = LocalDate.now();
        LocalDate aMonthBeforeToday = todayDate.minusMonths(1);

        String formattedAMonthBeforeTodayDate = formatDate(aMonthBeforeToday);
        String formattedAMonthBeforeTodayDayOfMonth = String.valueOf(aMonthBeforeToday.getDayOfMonth());

        Locator FROM_DATE = new Locator("FROM_DATE", new By.ByXPath("//td[contains(@aria-label, '" + formattedAMonthBeforeTodayDate + "')]//div[contains(text(),'" + formattedAMonthBeforeTodayDayOfMonth + "')]"));

        driverTools.click(DATE_FROM_INPUT);
        driverTools.click(DATE_PICKER_LEFT_ARROW);
        driverTools.click(FROM_DATE);

        driverTools.click(SEARCH_BUTTON);

        WebElement monthlyButton = driverTools.waitForElementToAppear(MONTHLY_BUTTON).orElseThrow();
        driverTools.click(monthlyButton);
        WebElement hamburgerMenuButton = driverTools.waitForElementToAppear(HAMBURGER_MENU_BUTTON).orElseThrow();
        driverTools.click(hamburgerMenuButton);
        WebElement downloadCsvButton = driverTools.waitForElementToAppear(DOWNLOAD_CSV_BUTTON).orElseThrow();
        driverTools.scrollIntoWebView(downloadCsvButton);
        driverTools.click(downloadCsvButton);
    }

    private String formatDate(LocalDate date) {
        return date.format(DATE_PICKER_FORMATTER);
    }

    public String exportFileNameBuilder(String fileName, String fileType) {
        return fileName + fileType;
    }

    public void downloadPngReportingSalesWeekly(){
        driverTools.waitForElementToAppear(WEEKLY_BUTTON);
        driverTools.click(WEEKLY_BUTTON);
        driverTools.waitForElementToAppear(HAMBURGER_MENU_BUTTON);
        driverTools.click(HAMBURGER_MENU_BUTTON);
        driverTools.waitForElementToAppear(DOWNLOAD_PNG_BUTTON);
        driverTools.scrollIntoWebView(DOWNLOAD_PNG_BUTTON);
        driverTools.click(DOWNLOAD_PNG_BUTTON);
    }

    public void downloadSvgReportingSalesDaily(){
        driverTools.waitForElementToAppear(DAILY_BUTTON);
        driverTools.click(DAILY_BUTTON);
        driverTools.waitForElementToAppear(HAMBURGER_MENU_BUTTON);
        driverTools.click(HAMBURGER_MENU_BUTTON);
        driverTools.waitForElementToAppear(DOWNLOAD_SVG_BUTTON);
        driverTools.scrollIntoWebView(DOWNLOAD_SVG_BUTTON);
        driverTools.click(DOWNLOAD_SVG_BUTTON);
    }

    public boolean verifyDownloadedExportFile(String timeOutMilliSec, String fileName, String fileType) {
        String downloadDir = System.getProperty("user.home") + File.separator + "Downloads";
        logger.log("Download folder: " + downloadDir);

        String expectedFilename = exportFileNameBuilder(fileName, fileType);
        logger.log("Expected filename: " + expectedFilename);

        long startTime = Instant.now().toEpochMilli();
        logger.log("Start time: " + startTime);
        long waitTime = startTime + Integer.parseInt(timeOutMilliSec);
        logger.log("Wait time: " + waitTime);

        while (Instant.now().toEpochMilli() < waitTime) {
            File[] listFiles = new File(downloadDir).listFiles();

            if (listFiles == null) {
                logger.log("Failed to list files in download directory.");
                break;
            }

            for (File file : listFiles) {
                if (file.getName().equals(expectedFilename)) {
                    Assertion.assertThat("Verify the downloaded file name is equal to expected name", file.getName(), is(expectedFilename));
                    logger.log("Found file: " + file.getName());
                    if (file.delete()) {
                        logger.log(file.getName() + " has been deleted.");
                    } else {
                        logger.log("Failed to delete file: " + file.getName());
                    }
                    return true;
                }
            }
        }

        return false;
    }

    public boolean verifyDownloadedExportCSV(String timeOutMilliSec, String csvFileName) {
        return verifyDownloadedExportFile(timeOutMilliSec, csvFileName, ".csv");
    }

    public boolean verifyDownloadedExportPNG(String timeOutMilliSec, String pngFileName) {
        return verifyDownloadedExportFile(timeOutMilliSec, pngFileName, ".png");
    }

    public boolean verifyDownloadedExportSVG(String timeOutMilliSec, String svgFileName) {
        return verifyDownloadedExportFile(timeOutMilliSec, svgFileName, ".svg");
    }

    public void goToGeneralTabList(){
        driverTools.click(GENERAL_TAB_LIST);
    }

    public void downloadXlsxReportingSales(){
        LocalDate todayDate = LocalDate.now();
        LocalDate threeDaysBeforeToday = todayDate.minusDays(3);

        String formattedThreeDaysBeforeTodayDate = formatDate(threeDaysBeforeToday);
        String formattedThreeDaysBeforeTodayDayOfMonth = String.valueOf(threeDaysBeforeToday.getDayOfMonth());

        Locator FROM_DATE = new Locator("FROM_DATE", new By.ByXPath("//td[contains(@aria-label, '" + formattedThreeDaysBeforeTodayDate + "')]//div[contains(text(),'" + formattedThreeDaysBeforeTodayDayOfMonth + "')]"));

        driverTools.click(DATE_FROM_INPUT);
        driverTools.click(DATE_PICKER_RIGHT_ARROW);
        driverTools.click(FROM_DATE);

        driverTools.click(SEARCH_BUTTON);

        driverTools.waitForElementToAppear(EXPORT_TO_EXCEL_BUTTON);
        driverTools.click(EXPORT_TO_EXCEL_BUTTON);
    }

    public boolean verifyDownloadedExportXlsx(String timeOutMilliSec, String xlsxFileName) {
        return verifyDownloadedExportFile(timeOutMilliSec, xlsxFileName, ".xlsx");
    }

}
