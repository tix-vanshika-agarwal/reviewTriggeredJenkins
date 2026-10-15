package com.tiket.web.dashboard.page.ttd.Ignis.IgnisRefundConfigPage;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.logging.log4j.core.Core;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class IgnisRefundConfigPageWeb<T extends WebDriver> extends WebPage<T> {
    public IgnisRefundConfigPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> IgnisRefundConfigPageWeb<?> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new IgnisRefundConfigPageDweb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new IgnisRefundConfigPageMweb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    private static final Locator FILE_UPLOAD_INPUT = new Locator("FILE_UPLOAD_INPUT", By.xpath("//input[@type='file']"));
    private static final Locator CONTINUE_BUTTON = new Locator("CONTINUE_BUTTON", By.xpath("//div[@class='bulk-refund-action']/button[contains(@class,'primary')]"));
    private static final Locator CONTINUE_UPLOAD_BUTTON = new Locator("CONTINUE_UPLOAD_BUTTON", By.xpath("//div[@class='modal']//button[@class='button-submit']"));
    private static final Locator UPLOAD_BUTTON = new Locator("CONTINUE_BUTTON", By.xpath("//button[contains(@class,'upload-refund-button')]"));
    private static final Locator DROPDOWN_REFUND_REASON_SELECTION = new Locator("DROPDOWN_REFUND_REASON_SELECTION", By.xpath("//div[@class='menu-item']"));
    private static final Locator DROPDOWN_REFUND_REASON = new Locator("DROPDOWN_REFUND_REASON", By.xpath("//div[@class='form-control form-select sort-select']"));
    private static final Locator RESCHEDULE_REFUND_TYPE_RADIO_BTN = new Locator("RESCHEDULE_REFUND_TYPE_RADIO_BTN", By.xpath("//label[.//input[@value='RESCHEDULED']]"));
    private static final Locator CANCELED_REFUND_TYPE_RADIO_BTN = new Locator("CANCELED_REFUND_TYPE_RADIO_BTN", By.xpath("//label[.//input[@value='CANCELED']]"));
    private static final Locator REFUND_SUBMISSION_DEADLINE_DATE_INPUT = new Locator("REFUND_SUBMISSION_DEADLINE_DATE_INPUT", By.xpath("//div[@class='refund-deadline-input']/div[@class='date-picker-input']"));
    private static final Locator NEXT_CALENDAR = new Locator("NEXT_CALENDAR", By.xpath("//div[@class='DayPicker-NavBar nav-bar']/button[2]"));
    private static final Locator REFUND_SUBMISSION_DEADLINE_TIME_HOUR_INPUT = new Locator("REFUND_SUBMISSION_DEADLINE_TIME_HOUR_INPUT", By.xpath("//div[@class='refund-deadline-input']//input[@name='hours']"));
    private static final Locator REFUND_SUBMISSION_DEADLINE_TIME_MINUTE_INPUT = new Locator("REFUND_SUBMISSION_DEADLINE_TIME_MINUTE_INPUT", By.xpath("//div[@class='refund-deadline-input']//input[@name='minutes']"));
    private static final Locator REFUND_PERCENTAGE_INPUT = new Locator("REFUND_PERCENTAGE_INPUT", By.xpath("//input[@name='refundPercentage']"));
    private static final Locator REFUND_CONFIG_SAVE_BTN = new Locator("REFUND_CONFIG_SAVE_BTN", By.xpath("//div[@class='refund-config-details-footer']/button[contains(@class,'primary')]"));
    private static final Locator MODAL_PRIMARY_BTN = new Locator("MODAL_PRIMARY_BTN", By.xpath("//div[@class='modal']//button[contains(@class,'primary')]"));
    private static final Locator EXTEND_VALIDITY_DATE_INPUT = new Locator("EXTEND_VALIDITY_DATE_INPUT", By.xpath("//div[@class='refund-extend-validity-input']/div[@class='date-picker-input']"));
    private static final Locator EXTEND_VALIDITY_TIME_HOUR_INPUT = new Locator("EXTEND_VALIDITY_TIME_HOUR_INPUT", By.xpath("//div[@class='refund-extend-validity-input']//input[@name='hours']"));
    private static final Locator EXTEND_VALIDITY_TIME_MINUTE_INPUT = new Locator("EXTEND_VALIDITY_TIME_MINUTE_INPUT", By.xpath("//div[@class='refund-extend-validity-input']//input[@name='minutes']"));

    public void createAndUploadCSV(String orderDetailId) throws IOException {
        String filePath = createCSV(orderDetailId);
        uploadCSV(filePath);
        clickSubmitCSV();
    }

    private void clickSubmitCSV() {
        driverTools.waitForElementToAppear(CONTINUE_BUTTON);
        driverTools.click(CONTINUE_BUTTON);

        driverTools.waitForElementToAppear(CONTINUE_UPLOAD_BUTTON);
        driverTools.click(CONTINUE_UPLOAD_BUTTON);

        driverTools.waitForWebToLoad();
    }

    private void uploadCSV(String filePath) {
        driverTools.waitForElementToAppear(UPLOAD_BUTTON);
        driverTools.click(UPLOAD_BUTTON);

        driverTools.sendKeys(FILE_UPLOAD_INPUT, filePath);
    }

    private String createCSV(String orderDetailId) throws IOException {
        String directoryPath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "dashboard" + File.separator + "ttd" + File.separator;
        String fileName = "OrderDetailIdList.csv";

        List<String> fileContent = new ArrayList<>();
        fileContent.add("Order Detail Id");
        fileContent.add(orderDetailId);

        File file = CoreFunctions.createCSV(directoryPath, fileName, fileContent);

        if (file.exists()) {
            logger.log("Successfully created new CSV file");
        } else {
            throw new RuntimeException("Failed to create new CSV file");
        }

        return file.getAbsolutePath();
    }

    public void chooseRefundReason(String refundReason) {
        driverTools.waitForElementToAppear(DROPDOWN_REFUND_REASON);
        driverTools.click(DROPDOWN_REFUND_REASON);

        driverTools.waitForElementToAppear(DROPDOWN_REFUND_REASON_SELECTION);
        List<WebElement> dropdownSelection = driverTools.elementsUntilExist(DROPDOWN_REFUND_REASON_SELECTION).get();
        boolean found = false;
        for (WebElement e : dropdownSelection) {
            String elementText = driverTools.getText(e);
            if (elementText.equalsIgnoreCase(refundReason)) {
                found = true;
                driverTools.click(e);
                break;
            }
        }

        if (!found) {
            throw new NoSuchElementException("There is no refund reason '"+refundReason+"'");
        }
    }

    public void chooseRefundType(String refundType) {
        refundType = refundType.toLowerCase();

        if (refundType.contains("reschedule")) {
            driverTools.waitForElementToAppear(RESCHEDULE_REFUND_TYPE_RADIO_BTN);
            driverTools.click(RESCHEDULE_REFUND_TYPE_RADIO_BTN);
        } else if (refundType.contains("cancel") || refundType.contains("batal")) {
            driverTools.waitForElementToAppear(CANCELED_REFUND_TYPE_RADIO_BTN);
            driverTools.click(CANCELED_REFUND_TYPE_RADIO_BTN);
        } else {
            throw new NoSuchElementException("There is no refund type '"+refundType+"'");
        }
    }

    public void fillRefundSubmissionDeadline(String refundDeadlineDateRange, String refundDeadlineTime) {
        fillRefundDeadlineDate(refundDeadlineDateRange);
        fillHourMinutes(refundDeadlineTime, REFUND_SUBMISSION_DEADLINE_TIME_HOUR_INPUT, REFUND_SUBMISSION_DEADLINE_TIME_MINUTE_INPUT);
    }

    private void fillHourMinutes(String hoursMinutes, Locator hoursInputLocator, Locator minutesInputLocator) {
        String hours = hoursMinutes.split("-")[0];
        String minutes = hoursMinutes.split("-")[1];

        driverTools.waitForElementToAppear(hoursInputLocator);
        driverTools.sendKeys(hoursInputLocator, hours);

        driverTools.waitForElementToAppear(minutesInputLocator);
        driverTools.sendKeys(minutesInputLocator, minutes);
    }

    private void fillRefundDeadlineDate(String refundDeadlineDateRange) {
        driverTools.waitForElementToAppear(REFUND_SUBMISSION_DEADLINE_DATE_INPUT);
        driverTools.click(REFUND_SUBMISSION_DEADLINE_DATE_INPUT);

        int minPlusDay = Integer.parseInt(refundDeadlineDateRange.split("-")[0]);
        int maxPlusDay = Integer.parseInt(refundDeadlineDateRange.split("-")[1]);
        LocalDate todayDate = LocalDate.now();
        LocalDate targetDate = todayDate.plusDays(CoreFunctions.generateRandomInt(minPlusDay, maxPlusDay));
        int monthDiff = ((targetDate.getYear() * 12) + targetDate.getMonthValue()) - ((todayDate.getYear() * 12) + todayDate.getMonthValue());
        increaseMonthCalendar(monthDiff);
        selectDate(targetDate);
    }

    private void selectDate(LocalDate targetDate) {
        Locator locator = getDateLocatorByDay(targetDate.getDayOfMonth());
        driverTools.waitForElementToAppear(locator);
        driverTools.click(locator);
    }

    private Locator getDateLocatorByDay(int dayOfMonth) {
        return new Locator("DATE_"+dayOfMonth, By.xpath("//div[@class='DayPicker-Body']//div[@aria-disabled='false']/div[@class='day-cell' and text()='"+dayOfMonth+"']"));
    }

    private void increaseMonthCalendar(int monthDiff) {
        for (int i = 0 ; i < monthDiff ; i++) {
            driverTools.waitForElementToAppear(NEXT_CALENDAR);
            driverTools.click(NEXT_CALENDAR);
        }
    }

    public void fillRefundPercentage(String refundPercentage) {
        driverTools.waitForElementToAppear(REFUND_PERCENTAGE_INPUT);
        driverTools.sendKeys(REFUND_PERCENTAGE_INPUT, refundPercentage);
    }

    public void submitRequest() {
        driverTools.waitForElementToAppear(REFUND_CONFIG_SAVE_BTN);
        driverTools.click(REFUND_CONFIG_SAVE_BTN);

        driverTools.waitForElementToAppear(MODAL_PRIMARY_BTN);
        driverTools.click(MODAL_PRIMARY_BTN);
        driverTools.dummyWait(1);
    }

    public void checkProgressLog() {
        driverTools.waitForElementToAppear(MODAL_PRIMARY_BTN);
        driverTools.click(MODAL_PRIMARY_BTN);
    }

    public void fillExtendValidity(String extendValidityDateRange, String extendValidityTime) {
        fillExtendValidityDate(extendValidityDateRange);
        fillHourMinutes(extendValidityTime, EXTEND_VALIDITY_TIME_HOUR_INPUT, EXTEND_VALIDITY_TIME_MINUTE_INPUT);
    }

    private void fillExtendValidityDate(String extendValidityDateRange) {
        driverTools.waitForElementToAppear(EXTEND_VALIDITY_DATE_INPUT);
        driverTools.click(EXTEND_VALIDITY_DATE_INPUT);

        int diffRange = Integer.parseInt(extendValidityDateRange.split("-")[1]) - Integer.parseInt(extendValidityDateRange.split("-")[0]);
        int minPlusDay = Integer.parseInt(extendValidityDateRange.split("-")[1]) + 1;
        int maxPlusDay = minPlusDay + diffRange;
        LocalDate todayDate = LocalDate.now();
        LocalDate targetDate = todayDate.plusDays(CoreFunctions.generateRandomInt(minPlusDay, maxPlusDay));
        int monthDiff = ((targetDate.getYear() * 12) + targetDate.getMonthValue()) - ((todayDate.getYear() * 12) + todayDate.getMonthValue());
        increaseMonthCalendar(monthDiff);
        selectDate(targetDate);
    }
}
