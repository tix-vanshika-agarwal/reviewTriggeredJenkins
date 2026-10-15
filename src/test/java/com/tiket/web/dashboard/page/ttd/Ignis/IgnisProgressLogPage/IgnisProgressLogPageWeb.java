package com.tiket.web.dashboard.page.ttd.Ignis.IgnisProgressLogPage;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import lombok.val;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.either;
import static org.hamcrest.core.Is.is;

public class IgnisProgressLogPageWeb<T extends WebDriver> extends WebPage<T> {
    public IgnisProgressLogPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> IgnisProgressLogPageWeb<?> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new IgnisProgressLogPageDweb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new IgnisProgressLogPageMweb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    private static final int TRANSACTION_DATE_XSL_INDEX = 4;
    private static final int SETTLEMENT_DATE_XSL_INDEX = 5;
    private static final int VISIT_DATE_XSL_INDEX = 6;

    private static final Locator MAIN_PAGE_COMPONENT = new Locator("MAIN_PAGE_COMPONENT", By.xpath("//div[@class='progress-log-list']"));
    private static final Locator FIRST_ROW_TYPE = new Locator("FIRST_ROW_TYPE", By.xpath("//tbody/tr[1]/td[1]"));
    private static final Locator FIRST_ROW_PRODUCT_NAME = new Locator("FIRST_ROW_PRODUCT_NAME", By.xpath("//tbody/tr[1]/td[2]//p[last()]"));
    private static final Locator FIRST_ROW_COMPLETE_BADGE_STATUS = new Locator("FIRST_ROW_COMPLETE_BADGE_STATUS", By.xpath("//tbody/tr[1]/td[4]//div[contains(@class,'color_green')]"));
    private static final Locator FIRST_ROW_REQUEST_TIME = new Locator("FIRST_ROW_REQUEST_TIME", By.xpath("//tbody/tr[1]/td[3]"));
    private static final Locator FIRST_ROW_TMS_TYPE = new Locator("FIRST_ROW_TMS_TYPE", By.xpath("//tbody/tr[1]/td[2]"));
    private static final Locator FIRST_ROW_TMS_CATEGORY_NAME = new Locator("FIRST_ROW_TMS_CATEGORY_NAME", By.xpath("//tbody/tr[1]/td[3]//span[@class='package-name-col']"));
    private static final Locator FIRST_ROW_TMS_COMPLETE_BADGE_STATUS = new Locator("FIRST_ROW_TMS_COMPLETE_BADGE_STATUS", By.xpath("//tbody/tr[1]/td[6]//div[contains(@class,'badge-green')]"));
    private static final Locator FIRST_ROW_TMS_REQUEST_TIME = new Locator("FIRST_ROW_TMS_REQUEST_TIME", By.xpath("//tbody/tr[1]/td[5]"));
    private static final Locator FIRST_ROW_TMS_DOWNLOAD_BUTTON = new Locator("FIRST_ROW_TMS_DOWNLOAD_BUTTON", By.xpath("//tbody/tr[1]/td[last()]/button"));

    public Boolean isInPage() {
        return driverTools.waitForElementToPresence(MAIN_PAGE_COMPONENT).isPresent();
    }

    public void clickDownloadButtonRow(int i) {
        driverTools.dummyWait(5);
        driverTools.refreshWebpage();
        driverTools.dummyWait(1);
        Locator locator = getDownloadButtonByRowIndex(i);
        driverTools.waitForElementToAppear(locator);
        driverTools.click(locator);
        driverTools.dummyWait(1);
    }

    private Locator getDownloadButtonByRowIndex(int i) {
        return new Locator("DOWNLOAD_BUTTON_"+i, By.xpath("//tbody/tr["+i+"]//button"));
    }

    public boolean verifyFirstRow(String type, String productName) {
        driverTools.dummyWait(5);
        driverTools.refreshWebpage();
        driverTools.dummyWait(1);

        String displayedType = driverTools.getText(FIRST_ROW_TYPE).orElseThrow();
        Assertion.assertThat("Verify the displayed type is correct", type.toLowerCase(), containsString(displayedType.toLowerCase()));

        String displayedProductName = driverTools.getText(FIRST_ROW_PRODUCT_NAME).orElseThrow();
        Assertion.assertThat("Verify the product name is matched", displayedProductName.toLowerCase(), is(productName.toLowerCase()));

        boolean isCompletedBadgeDisplayed = driverTools.waitForElementToAppear(FIRST_ROW_COMPLETE_BADGE_STATUS).isPresent();
        Assertion.assertThat("Verify the status is already completed", isCompletedBadgeDisplayed, is(true));

        String displayedRequestTime = driverTools.getText(FIRST_ROW_REQUEST_TIME).orElseThrow().split(" - ")[0];
        String[] displayedRequestTimeArray = displayedRequestTime.split(" ");
        String month = CoreFunctions.getMonthNumber(displayedRequestTimeArray[1]) < 10 ? "0" + CoreFunctions.getMonthNumber(displayedRequestTimeArray[1]) : String.valueOf(CoreFunctions.getMonthNumber(displayedRequestTimeArray[1]));
        displayedRequestTime = displayedRequestTimeArray[0] + "-" + month + "-" + displayedRequestTimeArray[2];

        LocalDate today = LocalDate.now();
        String todayDateString = today.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        Assertion.assertThat("Verify the request time is today", displayedRequestTime, is(todayDateString));

        return true;
    }

    public boolean verifyTMSProgressLog(String type, String categoryName, String quantity) {
        driverTools.dummyWait(5);
        driverTools.refreshWebpage();
        driverTools.dummyWait(1);

        String displayedType = driverTools.getText(FIRST_ROW_TMS_TYPE).orElseThrow();
        Assertion.assertThat("Verify the displayed type is correct", type.toLowerCase(), containsString(displayedType.toLowerCase()));

        String displayedProductName = driverTools.getText(FIRST_ROW_TMS_CATEGORY_NAME).orElseThrow();
        Assertion.assertThat("Verify the category name is matched", displayedProductName.toLowerCase(), is(categoryName.toLowerCase()));

        boolean isCompletedBadgeDisplayed = driverTools.waitForElementToAppear(FIRST_ROW_TMS_COMPLETE_BADGE_STATUS).isPresent();
        Assertion.assertThat("Verify the status is already completed", isCompletedBadgeDisplayed, is(true));

        String displayedRequestTime = driverTools.getText(FIRST_ROW_TMS_REQUEST_TIME).orElseThrow().split(" - ")[0];
        String[] displayedRequestTimeArray = displayedRequestTime.split(" ");
        String month = CoreFunctions.getMonthNumber(displayedRequestTimeArray[1]) < 10 ? "0" + CoreFunctions.getMonthNumber(displayedRequestTimeArray[1]) : String.valueOf(CoreFunctions.getMonthNumber(displayedRequestTimeArray[1]));
        displayedRequestTime = displayedRequestTimeArray[0] + "-" + month + "-" + displayedRequestTimeArray[2];

        LocalDate today = LocalDate.now();
        String todayDateString = today.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        Assertion.assertThat("Verify the request time is today", displayedRequestTime, is(todayDateString));

        return true;
    }

    public void downloadFirstRowReport() {
        driverTools.dummyWait(3);
        driverTools.refreshWebpage();
        driverTools.waitForElementsToAppear(FIRST_ROW_TMS_DOWNLOAD_BUTTON);
        driverTools.click(FIRST_ROW_TMS_DOWNLOAD_BUTTON);
        driverTools.dummyWait(1);
    }

    public boolean verifyExactValueForExportedXSL(String name, int columnIndex) throws IOException {
        if (name.equalsIgnoreCase("all")) return true;

        String downloadDir = System.getProperty("user.home") + File.separator + "Downloads";
        FileInputStream file = new FileInputStream(CoreFunctions.getLastModified(downloadDir));
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheetAt(0);

        Iterator<Row> rowIterator = sheet.iterator();
        Row row = rowIterator.next();
        boolean result = true;
        while (rowIterator.hasNext()) {
            row = rowIterator.next();
            String dataPoint = String.valueOf(row.getCell(columnIndex));
            logger.log(String.format("Verifying row %s with data point %s for %s", row.getRowNum(), dataPoint, columnIndex == 9 ? "product" : "package"));
            if (!(name.equalsIgnoreCase(dataPoint))) {
                result = false;
                break;
            }
        }
        file.close();

        return result;
    }

    public boolean verifyDateRangeForExportedXSL(String startDateString, String endDateString, String filterBy) throws IOException {
        String downloadDir = System.getProperty("user.home") + File.separator + "Downloads";
        FileInputStream file = new FileInputStream(CoreFunctions.getLastModified(downloadDir));
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheetAt(0);

        Iterator<Row> rowIterator = sheet.iterator();
        Row row = rowIterator.next();

        LocalDateTime startDate = CoreFunctions.convertStringToTemporal(startDateString, "dd/MM/yyyy", LocalDateTime.class);
        LocalDateTime endDate = CoreFunctions.convertStringToTemporal(endDateString, "dd/MM/yyyy", LocalDateTime.class);

        boolean result = true;
        while (rowIterator.hasNext()) {
            String dataPoint = "-";
            row = rowIterator.next();
            switch (filterBy.toLowerCase()) {
                case "transaction" -> {
                    dataPoint = String.valueOf(row.getCell(TRANSACTION_DATE_XSL_INDEX));
                }
                case "settlement" -> {
                    dataPoint = String.valueOf(row.getCell(SETTLEMENT_DATE_XSL_INDEX));
                }
                case "visit" -> {
                    dataPoint = String.valueOf(row.getCell(VISIT_DATE_XSL_INDEX));
                    if (dataPoint.equals("-")) {
                        dataPoint = String.valueOf(row.getCell(TRANSACTION_DATE_XSL_INDEX));
                    }
                }
            }
            LocalDateTime targetDate = CoreFunctions.convertStringToTemporal(dataPoint, "dd MMM yyyy HH:mm:ss", LocalDateTime.class);

            logger.log(String.format("Checking row %s for date %s is between %s and %s", row.getRowNum(), dataPoint, startDate, endDate));

            if (!CoreFunctions.checkLocalDateTimeIsBetween(targetDate, startDate, endDate)) {
                logger.fail(String.format("Row %s is not between the date range filter", row.getRowNum()));
                result = false;
                break;
            }
        }

        file.close();
        return result;
    }
}
