package com.tiket.web.dashboard.page.hotel.supply.mmmodule;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;
import java.util.Date;
import java.util.Locale;

public class ShutdownVendorPage <T extends WebDriver> extends BasePage<T> {
    public ShutdownVendorPage(Session<T> session) {
        super(session);
    }

    private static final Locator SHUTDOWN_VENDOR_HEADER_LABEL = new Locator("SHUTDOWN_VENDOR_HEADER_LABEL", new By.ByXPath("//span[contains(text(),'Turn On')]"));
    private static final Locator SUCCESSFUL_SNACKBAR = new Locator("SUCCESSFUL_SNACKBAR", new By.ByXPath("//div[contains(@class,\"Snackbar_text_content\")]"));
    private static final Locator EDIT_CONFIG_MODAL_START_DATE_INPUT = new Locator("EDIT_CONFIG_MODAL_START_DATE_INPUT", new By.ByXPath("//input[@id=\"start-date\"]"));
    private static final Locator EDIT_CONFIG_MODAL_START_MONTH_YEAR_LABEL = new Locator("EDIT_CONFIG_MODAL_START_MONTH_YEAR_LABEL", new By.ByXPath("(//div[@class=\"DayPicker-Month\"]/div[@class=\"DayPicker-Caption\"]/div)[1]"));
    private static final Locator EDIT_CONFIG_MODAL_START_PREV_MONTH_BUTTON = new Locator("EDIT_CONFIG_MODAL_START_PREV_MONTH_BUTTON", new By.ByXPath("(//div[@class=\"DatePicker-NavSpan\"][1])[1]"));
    private static final Locator EDIT_CONFIG_MODAL_START_NEXT_MONTH_BUTTON = new Locator("EDIT_CONFIG_MODAL_START_NEXT_MONTH_BUTTON", new By.ByXPath("(//div[@class=\"DatePicker-NavSpan\"][2])[1]"));
    private static final Locator EDIT_CONFIG_MODAL_END_DATE_INPUT = new Locator("EDIT_CONFIG_MODAL_END_DATE_INPUT", new By.ByXPath("//input[@id=\"end-date\"]"));
    private static final Locator EDIT_CONFIG_MODAL_END_MONTH_YEAR_LABEL = new Locator("EDIT_CONFIG_MODAL_END_MONTH_YEAR_LABEL", new By.ByXPath("(//div[@class=\"DayPicker-Month\"]/div[@class=\"DayPicker-Caption\"]/div)[2]"));
    private static final Locator EDIT_CONFIG_MODAL_END_PREV_MONTH_BUTTON = new Locator("EDIT_CONFIG_MODAL_END_PREV_MONTH_BUTTON", new By.ByXPath("(//div[@class=\"DatePicker-NavSpan\"][1])[2]"));
    private static final Locator EDIT_CONFIG_MODAL_END_NEXT_MONTH_BUTTON = new Locator("EDIT_CONFIG_MODAL_END_NEXT_MONTH_BUTTON", new By.ByXPath("(//div[@class=\"DatePicker-NavSpan\"][2])[2]"));
    private static final Locator EDIT_CONFIG_MODAL_SUBMIT_BUTTON = new Locator("EDIT_CONFIG_MODAL_SUBMIT_BUTTON", new By.ByXPath("//button[@type='submit'][text()='Save']"));

    private static Locator getVendorStartDate(String vendorName) {
        return new Locator("VENDOR_START_DATE", new By.ByXPath(String.format("//div[@class=\"table-responsive\"][1]//td[text()='%s']//following-sibling::td[1]", vendorName)));
    }

    private static Locator getVendorEndDate(String vendorName) {
        return new Locator("VENDOR_END_DATE", new By.ByXPath(String.format("//div[@class=\"table-responsive\"][1]//td[text()='%s']//following-sibling::td[2]", vendorName)));
    }

    private static Locator getVendorToggleInputByName(String vendorName) {
        return new Locator("VENDOR_TOGGLE_INPUT", new By.ByXPath(String.format("//div[@class=\"table-responsive\"][1]//td[text()='%s']//following-sibling::td//input[@class=\"switch-input\"]", vendorName)));
    }

    private static Locator getVendorToggleStatusByName(String vendorName) {
        return new Locator("VENDOR_TOGGLE_STATUS_LABEL", new By.ByXPath(String.format("(//div[@class='w-full block'])[last()]//td[contains(@class,'leading-5')][text()='%s']//following-sibling::td[contains(@class,'leading-5')][last()]", vendorName)));
    }

    private static Locator getVendorToggleButtonByName(String vendorName) {
        return new Locator("VENDOR_TOGGLE_BUTTON", new By.ByXPath(String.format("//td[contains(@class,'leading-5')][text()='%s']//ancestor::tr//*[@data-testid='status-toggle']//ancestor::div[contains(@class,'cursor-pointer')]", vendorName)));
    }

    private static Locator getEditVendorConfigButtonByName(String vendorName) {
        return new Locator("EDIT_VENDOR_CONFIG_BUTTON", new By.ByXPath(String.format("//td[contains(@class,'leading-5')][text()='%s']//ancestor::tr//*[contains(@data-testid,'vendor-shutdown')]", vendorName)));
    }

    private static Locator getEditVendorConfigModalTitleLabel() {
        return new Locator("EDIT_VENDOR_CONFIG_MODAL_TITLE", new By.ByXPath("//section//h2"));
    }

    private static Locator getEditConfigModalStartDayLabel(String day) {
        return new Locator(
                "EDIT_VENDOR_CONFIG_MODAL_START_DAY_LABEL",
                new By.ByXPath(String.format(
                        "//div[contains(@class,'DayPicker-Day') and text()='%s']",
                        day
                ))
        );
    }

    private static Locator getEditConfigModalEndDayLabel(String day) {
        return new Locator(
                "EDIT_VENDOR_CONFIG_MODAL_END_DAY_LABEL",
                new By.ByXPath(String.format(
                        "//div[contains(@class,'DayPicker-Day') and text()='%s']",
                        day
                ))
        );
    }

    public boolean isShutdownVendorPageDisplayed() {
        WebElement header = driverTools.waitForElementToAppear(SHUTDOWN_VENDOR_HEADER_LABEL, 30).orElseThrow();
        return driverTools.isElementDisplayed(header);
    }

    public boolean isVendorToggleOn(String vendorName) {
        WebElement toggle = driverTools.waitForElementToPresence(getVendorToggleStatusByName(vendorName)).orElseThrow();
        String currentStatus = driverTools.getText(toggle);
        return currentStatus != null && currentStatus.trim().equalsIgnoreCase("TRUE");
    }

    public void turnVendorToggle(String vendorName, boolean isExpectedlyEnabled) {
        WebElement toggle = driverTools.waitForElementToBeClickable(getVendorToggleButtonByName(vendorName)).orElseThrow();
        boolean toggleStatus = isVendorToggleOn(vendorName);
        if ((toggleStatus && !isExpectedlyEnabled) || (!toggleStatus && isExpectedlyEnabled)) {
            driverTools.clickElementByCoordinateDWeb(toggle);
            driverTools.waitForElementToAppear(SUCCESSFUL_SNACKBAR).orElseThrow();
            driverTools.waitForElementToDisappear(SUCCESSFUL_SNACKBAR);
        }
        driverTools.refreshWebpage();
    }

    public void clickEditVendorConfig(String vendorName) {
        WebElement edit = driverTools.waitForElementToBeClickable(getEditVendorConfigButtonByName(vendorName)).orElseThrow();
        driverTools.click(edit);
    }

    public boolean isEditVendorConfigModalDisplayed(String vendorName) {
        WebElement label = driverTools.waitForElementToAppear(getEditVendorConfigModalTitleLabel()).orElseThrow();
        return label.getText().equals(vendorName);
    }

    private int convertMonthToIntegerByFullDateFormat(String date) {
        String[] split = date.split(" ");
        try {
            Date d = new SimpleDateFormat("MMMM", Locale.ENGLISH).parse(split[1]);
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            return cal.get(Calendar.MONTH);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private int convertMonthToIntegerByMonthYearFormat(String monthYear) {
        String[] split = monthYear.split(" ");
        try {
            Date d = new SimpleDateFormat("MMMM", Locale.ENGLISH).parse(split[0]);
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            return cal.get(Calendar.MONTH);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private String convertDateFormat(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM yyyy");
        LocalDate localDate = LocalDate.parse(date, formatter);
        return localDate.toString();
    }

    private void findTargetMonth(String dateType, String targetDate) {
        WebElement label = null;
        WebElement nextMonth = null;
        WebElement prevMonth = null;
        switch (dateType) {
            case "START" -> {
                label = driverTools.waitForElementToAppear(EDIT_CONFIG_MODAL_START_MONTH_YEAR_LABEL, 3).orElseThrow();
                nextMonth = driverTools.waitForElementToBeClickable(EDIT_CONFIG_MODAL_START_NEXT_MONTH_BUTTON, 3).orElseThrow();
                prevMonth = driverTools.waitForElementToBeClickable(EDIT_CONFIG_MODAL_START_PREV_MONTH_BUTTON, 3).orElseThrow();
            }
            case "END" -> {
                label = driverTools.waitForElementToAppear(EDIT_CONFIG_MODAL_END_MONTH_YEAR_LABEL, 3).orElseThrow();
                nextMonth = driverTools.waitForElementToBeClickable(EDIT_CONFIG_MODAL_END_NEXT_MONTH_BUTTON, 3).orElseThrow();
                prevMonth = driverTools.waitForElementToBeClickable(EDIT_CONFIG_MODAL_END_PREV_MONTH_BUTTON, 3).orElseThrow();
            }
        }
        String strMonthYear = driverTools.getText(label);
        int currMonth = convertMonthToIntegerByMonthYearFormat(strMonthYear);
        System.out.println("curr: " + currMonth);
        int targetMonth = convertMonthToIntegerByFullDateFormat(targetDate);
        System.out.println("target: " + targetMonth);
        int diff = targetMonth - currMonth;
        int absDiff = Math.abs(diff);
        while (absDiff != 0) {
            if (diff > 0) {
                assert nextMonth != null;
                driverTools.click(nextMonth);
            } else {
                assert prevMonth != null;
                driverTools.click(prevMonth);
            }
            absDiff--;
        }
    }

    private static final Locator ACTIVE_DATE_ELEMENTS = new Locator(
            "ACTIVE_DATE_ELEMENTS",
            new By.ByXPath("//button[contains(@class,'day')]//span[contains(@class,'Text_variant') and not(contains(@class,'disabled'))]")
    );

    public void clickActiveDateByIndex(int index) {
        List<WebElement> activeDates = driverTools.waitForPresenceElementsList(ACTIVE_DATE_ELEMENTS, 5).orElseThrow();
        if (index < 0 || index >= activeDates.size()) {
            throw new IndexOutOfBoundsException(
                    "Invalid active date index: " + index + ". Available active dates: " + activeDates.size()
            );
        }
        WebElement date = driverTools.waitForElementToBeClickable(activeDates.get(index)).orElseThrow();
        date.click();
    }

    public void selectDateByIndex(int start, int end) {
        WebElement startDateField = driverTools.waitForElementToBeClickable(EDIT_CONFIG_MODAL_START_DATE_INPUT, 3).orElseThrow();
        driverTools.click(startDateField);
        clickActiveDateByIndex(start);
        WebElement endDateField = driverTools.waitForElementToBeClickable(EDIT_CONFIG_MODAL_END_DATE_INPUT, 3).orElseThrow();
        driverTools.click(endDateField);
        clickActiveDateByIndex(end);
    }


    public void selectDate(String dateType, String expectedDate) {
        String[] splitDate = expectedDate.split(" ");
        WebElement inputField = null;
        WebElement day = null;
        switch (dateType) {
            case "START" -> {
                inputField = driverTools.waitForElementToBeClickable(EDIT_CONFIG_MODAL_START_DATE_INPUT, 3).orElseThrow();
            }
            case "END" -> {
                inputField = driverTools.waitForElementToBeClickable(EDIT_CONFIG_MODAL_END_DATE_INPUT, 3).orElseThrow();
            }
        }
        assert inputField != null;
        driverTools.click(inputField);
        findTargetMonth(dateType, expectedDate);
        switch (dateType) {
            case "START" -> day = driverTools.waitForElementToBeClickable(getEditConfigModalStartDayLabel(splitDate[0]), 3).orElseThrow();
            case "END" -> day = driverTools.waitForElementToBeClickable(getEditConfigModalEndDayLabel(splitDate[0]), 3).orElseThrow();
        }
        driverTools.click(day);
    }

    public void submitEditVendorConfig() {
        WebElement button = driverTools.waitForElementToPresence(EDIT_CONFIG_MODAL_SUBMIT_BUTTON).orElseThrow();
        driverTools.click(button);
    }

    public boolean isSuccessfulSnackbarAppeared() {
        WebElement snackbar = driverTools.waitForElementToAppear(SUCCESSFUL_SNACKBAR).orElseThrow();
        return driverTools.isElementDisplayed(snackbar);
    }

    public boolean isVendorConfigStartAndEndDatePresentedCorrectly(String vendorName, String startDate, String endDate) {
        WebElement startDateCell = driverTools.waitForElementToPresence(getVendorStartDate(vendorName)).orElseThrow();
        WebElement endDateCell = driverTools.waitForElementToPresence(getVendorEndDate(vendorName)).orElseThrow();
        return driverTools.getText(startDateCell).contains(convertDateFormat(startDate)) && driverTools.getText(endDateCell).contains(convertDateFormat(endDate));
    }
}
