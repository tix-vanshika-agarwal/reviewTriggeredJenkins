package com.tiket.web.dashboard.page.ttd.Ignis.SalesReportPage;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;

public class IgnisSalesReportPageWeb<T extends WebDriver> extends WebPage<T> {
    public IgnisSalesReportPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> IgnisSalesReportPageWeb<?> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new IgnisSalesReportPageDweb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new IgnisSalesReportPageMweb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    // Home
    private static final Locator MENU_REPORT_DROPDOWN = new Locator("MENU_REPORT_DROPDOWN", By.xpath("//div[@class='menu__item--nested'][1]"));
    private static final Locator SALES_REPORT_ANCHOR = new Locator("SALES_REPORT_ANCHOR", By.xpath("//a[@href='/ignis/sales-report']"));

    // Sales report page
    private static final Locator THINGSTODO_DROPDOWN = new Locator("THINGSTODO_DROPDOWN", By.xpath("//div[@class='form-control form-select filterable-select filter__product']//fieldset"));
    private static final Locator THINGSTODO_DROPDOWN_SELECTION = new Locator("THINGSTODO_DROPDOWN_SELECTION", By.xpath("//div[@class='menu-item']"));
    private static final Locator PACKAGE_DROPDOWN = new Locator("PACKAGE_DROPDOWN", By.xpath("//div[@class='form-control form-select-multiple filter__packages']//fieldset"));
    private static final Locator PACKAGE_DROPDOWN_SELECTION = new Locator("PACKAGE_DROPDOWN_SELECTION", By.xpath("//div[@class='item__text']"));
    private static final Locator PACKAGE_SELECT_BUTTON = new Locator("PACKAGE_SELECT_BUTTON", By.xpath("//button[@class='btn btn-primary actions-item__search']"));
    private static final Locator TIME_PERIOD_DROPDOWN = new Locator("TIME_PERIOD_DROPDOWN", By.xpath("//div[@class='form-control filter__date-range']//fieldset"));
    private static final Locator TIME_PERIOD_POPUP = new Locator("TIME_PERIOD_POPUP", By.xpath("//div[@class='date-range__popup']"));
    private static final Locator TIME_PERIOD_POPUP_DROPDOWN = new Locator("TIME_PERIOD_POPUP_DROPDOWN", By.xpath("//div[@class='form-control form-select']//fieldset"));
    private static final Locator TIME_PERIOD_POPUP_DROPDOWN_SELECTION = new Locator("TIME_PERIOD_POPUP_DROPDOWN_SELECTION",  By.xpath("//div[@class='menu-item']"));
    private static final Locator TIME_PERIOD_POPUP_DROPDOWN_SELECTION_CUSTOM = new Locator("TIME_PERIOD_POPUP_DROPDOWN_SELECTION_CUSTOM", By.xpath("//div[contains(@class, 'DayPicker-Day')]"));
    private static final Locator TIME_PERIOD_POPUP_DROPDOWN_PREVIOUS_BUTTON = new Locator("TIME_PERIOD_POPUP_DROPDOWN_PREVIOUS_BUTTON", By.xpath("//button[@style='float: left;']"));
    private static final Locator TIME_PERIOD_POPUP_DROPDOWN_NEXT_BUTTON = new Locator("TIME_PERIOD_POPUP_DROPDOWN_NEXT_BUTTON", By.xpath("//button[@style='float: right;']"));
    private static final Locator TIME_PERIOD_SAVE_BUTTON = new Locator("TIME_PERIOD_SAVE_BUTTON", By.xpath("//button[@class='btn btn-primary actions-item__search']"));
    private static final Locator TIME_PERIOD_SELECTED = new Locator("TIME_PERIOD_SELECTED", By.xpath("//div[contains(@class, 'filter__date-range')]//label"));
    private static final Locator BY_VISIT_DATE_RADIOBUTTON = new Locator("BY_VISIT_DATE_RADIOBUTTON", By.xpath("//div[@class='date-field-item radio-container'][1]"));
    private static final Locator BY_TRANSACTION_DATE_RADIOBUTTON = new Locator("BY_TRANSACTION_DATE_RADIOBUTTON", By.xpath("//div[@class='date-field-item radio-container'][2]"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", By.xpath("//button[@class='btn btn-tertiary filter-actions-item__search']"));

    // result
    private static final Locator FILTER_RESULT = new Locator("FILTER_RESULT", By.xpath("//div[@class='heading__subtitle']//span[1]"));
    private static final Locator TIME_PERIOD_RESULT = new Locator("TIME_PERIOD_RESULT", By.xpath("//div[@class='heading__subtitle']//span[2]"));
    private static final Locator TICKET_SOLD_QUANTITY_VALUE = new Locator("TICKET_SOLD_QUANTITY_VALUE", By.xpath("//div[@class='calculation__card'][1]//div[@class='card__value']"));
    private static final Locator TICKET_SOLD_IDR_VALUE = new Locator("TICKET_SOLD_IDR_VALUE", By.xpath("//div[@class='calculation__card'][2]//div[@class='card__value']"));
    private static final Locator REDEEMED_TICKET_VALUE = new Locator("REDEEMED_TICKET_VALUE", By.xpath("//div[@class='calculation__card'][3]//div[@class='card__value']"));
    private static final Locator NOT_REDEEMED_TICKET_VALUE = new Locator("NOT_REDEEMED_TICKET_VALUE", By.xpath("//div[@class='calculation__card'][4]//div[@class='card__value']"));


    public void goToSalesReportPage() {
        driverTools.click(MENU_REPORT_DROPDOWN);

        driverTools.waitForElementToAppear(SALES_REPORT_ANCHOR);
        driverTools.click(SALES_REPORT_ANCHOR);
    }

    public String selectProduct(String productName) {
        driverTools.waitForElementToAppear(THINGSTODO_DROPDOWN);
        driverTools.click(THINGSTODO_DROPDOWN);

        driverTools.dummyWait(1);
        List<WebElement> productNames = driverTools.elementsUntilExist(THINGSTODO_DROPDOWN_SELECTION).get();

        String chosenProductText = "";
        if (productName.equalsIgnoreCase("random")) {
            int randomInt = CoreFunctions.generateRandomInt(1, productNames.size()-1);
            chosenProductText = driverTools.getText(productNames.get(randomInt));
            driverTools.click(productNames.get(randomInt));
        } else {
            for (WebElement e : productNames) {
                String productNameText = driverTools.getText(e);
                if (productNameText.equalsIgnoreCase(productName)) {
                    chosenProductText = productNameText;
                    driverTools.click(e);
                    break;
                }
            }

            if (chosenProductText.isEmpty()) {
                throw new NoSuchElementException("There is no option for '" + productName + "' on Product selection");
            }
        }
        return chosenProductText;
    }

    public String selectPackage(String packageName) {
        driverTools.waitForElementToAppear(PACKAGE_DROPDOWN);
        driverTools.click(PACKAGE_DROPDOWN);

        driverTools.dummyWait(1);
        List<WebElement> packageNames = driverTools.elementsUntilExist(PACKAGE_DROPDOWN_SELECTION).get();

        String chosenPackageText = "";
        if (packageName.equalsIgnoreCase("random")) {
            int randomInt = CoreFunctions.generateRandomInt(1, packageNames.size()-1);
            chosenPackageText = driverTools.getText(packageNames.get(randomInt));
            driverTools.click(packageNames.get(randomInt));
        } else {
            for (WebElement e : packageNames) {
                String packageNameText = driverTools.getText(e);
                if (packageNameText.equalsIgnoreCase(packageName)) {
                    chosenPackageText = packageNameText;
                    driverTools.click(e);
                    break;
                }
            }

            if (chosenPackageText.isEmpty()) {
                throw new NoSuchElementException("There is no option for '" + packageName + "' on Package selection");
            }
        }
        driverTools.click(PACKAGE_SELECT_BUTTON);
        return chosenPackageText;
    }

    public String selectTimePeriod(String timePeriod, String before, String after) {
        driverTools.waitForElementToAppear(TIME_PERIOD_DROPDOWN);
        driverTools.click(TIME_PERIOD_DROPDOWN);

        driverTools.waitForElementToAppear(TIME_PERIOD_POPUP);

        driverTools.waitForElementToAppear(TIME_PERIOD_POPUP_DROPDOWN);
        driverTools.click(TIME_PERIOD_POPUP_DROPDOWN);

        driverTools.dummyWait(1);
        List<WebElement> timePeriods = driverTools.elementsUntilExist(TIME_PERIOD_POPUP_DROPDOWN_SELECTION).get();

        String chosenTimePeriodText = "";
        for (WebElement e : timePeriods) {
            String timePeriodText = driverTools.getText(e);
            if (timePeriodText.equalsIgnoreCase(timePeriod)) {
                chosenTimePeriodText = timePeriodText;
                driverTools.click(e);
                if (timePeriod.equalsIgnoreCase("custom") || timePeriod.equalsIgnoreCase("pilih periode waktu")) {
                    Date todaysDate = new Date();
                    Date beforeDate = convertStringIntoDate(before);

                    long todayAndBeforeMonthDifference = getMonthDifference(todaysDate, beforeDate);
                    if (todayAndBeforeMonthDifference < -1) {
                        for (int i = 0; i < (todayAndBeforeMonthDifference + 1)*-1; i++) {
                            driverTools.click(TIME_PERIOD_POPUP_DROPDOWN_PREVIOUS_BUTTON);
                            driverTools.dummyWait(1);
                        }
                    } else if (todayAndBeforeMonthDifference > 0) {
                        for (int i = 0; i < todayAndBeforeMonthDifference; i++) {
                            driverTools.click(TIME_PERIOD_POPUP_DROPDOWN_NEXT_BUTTON);
                            driverTools.dummyWait(1);
                        }
                    }

                    String beforeDateDayPickerString = convertDateIntoDateString(before);
                    String afterDateDayPickerString = convertDateIntoDateString(after);
                    List<WebElement> days = driverTools.elementsUntilExist(TIME_PERIOD_POPUP_DROPDOWN_SELECTION_CUSTOM).get();
                    for  (WebElement day : days) {
                        String val = driverTools.getValueAttribute(day, "aria-label");
                        if (val == null) continue;
                        if (driverTools.getValueAttribute(day, "aria-label").equalsIgnoreCase(beforeDateDayPickerString) ||
                            driverTools.getValueAttribute(day, "aria-label").equalsIgnoreCase(afterDateDayPickerString)
                        ) {
                            driverTools.click(day);
                        }
                    }
                }
                break;
            }
        }

        if (chosenTimePeriodText.isEmpty()) {
            throw new NoSuchElementException("There is no option for '" + timePeriod + "' on Time Period selection");
        }

        driverTools.click(TIME_PERIOD_SAVE_BUTTON);

        return chosenTimePeriodText;
    }

    public void setVisitOrTransactionFilter(String filter) {
        switch (filter.toLowerCase()) {
            case "visit" -> {
                driverTools.scrollElementToCenter(BY_VISIT_DATE_RADIOBUTTON);
                driverTools.clickWithJavascriptExecutor(BY_VISIT_DATE_RADIOBUTTON);
            }
            case "transaction" -> {
                driverTools.scrollElementToCenter(BY_TRANSACTION_DATE_RADIOBUTTON);
                driverTools.clickWithJavascriptExecutor(BY_TRANSACTION_DATE_RADIOBUTTON);
            }
            default -> {
                throw new NoSuchElementException("There is no filter '"+ filter +"'");
            }
        }
    }

    public void clickSearch() {
        driverTools.scrollElementToCenter(SEARCH_BUTTON);
        driverTools.clickWithJavascriptExecutor(SEARCH_BUTTON);
        driverTools.dummyWait(5);
    }

    public int getQuantityTicketSold() {
        driverTools.waitForElementsToAppear(TICKET_SOLD_QUANTITY_VALUE);
        String value = driverTools.getText(TICKET_SOLD_QUANTITY_VALUE).orElse(null);
        if (value != null) {
            return Integer.parseInt(value);
        }
        return 0;
    }

    public String getIdrTicketSold() {
        driverTools.waitForElementsToAppear(TICKET_SOLD_IDR_VALUE);
        String value = driverTools.getText(TICKET_SOLD_IDR_VALUE).orElse(null);
        if (value != null) {
            return value;
        }
        return "IDR 0";
    }

    public int getRedeemedTicket() {
        driverTools.waitForElementsToAppear(REDEEMED_TICKET_VALUE);
        String value = driverTools.getText(REDEEMED_TICKET_VALUE).orElse(null);
        if (value != null) {
            return Integer.parseInt(value);
        }
        return 0;
    }

    public int getNotRedeemedTicket() {
        driverTools.waitForElementsToAppear(NOT_REDEEMED_TICKET_VALUE);
        String value = driverTools.getText(NOT_REDEEMED_TICKET_VALUE).orElse(null);
        if (value != null) {
            return Integer.parseInt(value);
        }
        return 0;
    }

    public Boolean validateFilterByMatchFilterResultLabel(String filter) {
        switch(filter) {
            case "visit" -> {
                return "Periode Tanggal Kunjungan:".equals(driverTools.getText(FILTER_RESULT).get());
            }
            case "transaction" -> {
                return "Periode Transaksi:".equals(driverTools.getText(FILTER_RESULT).get());
            }
        }
        return true;
    }

    public Boolean validateFilterDateMatchWithResultDate(){
        String selectedTimePeriod = driverTools.getText(TIME_PERIOD_SELECTED).orElseThrow();
        String actualTimePeriod = driverTools.getText(TIME_PERIOD_RESULT).orElse("-");

        if (!selectedTimePeriod.contains("(")) {
            return selectedTimePeriod.equals(actualTimePeriod);
        } else {
            String temp = "";
            int startIndex = selectedTimePeriod.indexOf('(');
            int endIndex = selectedTimePeriod.indexOf(')');
            if (startIndex != -1 && endIndex != -1 && endIndex > startIndex) {
                // Extract the substring between the parentheses
                temp = selectedTimePeriod.substring(startIndex + 1, endIndex);
            }
            return temp.equals(actualTimePeriod);
        }
    }



    private String convertDateIntoDateString(String inputDateString) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        LocalDate date = LocalDate.parse(inputDateString, inputFormatter);

        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("EEE MMM dd yyyy", Locale.ENGLISH);

        return date.format(outputFormatter);
    }

    private Date convertStringIntoDate(String dateString) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

        try {
            Date date = formatter.parse(dateString);
            return date;
        } catch (ParseException e) {
            System.err.println("Error parsing date: " + dateString + ". Reason: " + e.getMessage());
            return null;
        }
    }

    private long getMonthDifference(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("One or both input dates are null.");
        }

        LocalDate localDate1 = date1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localDate2 = date2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        return ChronoUnit.MONTHS.between(localDate1, localDate2);
    }
}