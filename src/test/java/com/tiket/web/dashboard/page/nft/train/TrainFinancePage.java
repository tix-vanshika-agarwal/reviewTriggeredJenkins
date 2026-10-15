package com.tiket.web.dashboard.page.nft.train;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.Matchers.is;

public class TrainFinancePage <T extends WebDriver> extends BasePage<T> {
    private static final Locator FINANCE_MENU = new Locator("FINANCE_MENU", new By.ByXPath("//div[@class='menu-list' and contains(text(),'Finance')]"));
    private static final Locator FINANCE_TRAIN_SUBMENU = new Locator("FINANCE_TRAIN_SUBMENU", new By.ByXPath("//div[@class='menu-list' and contains(text(), 'Finance')]/following-sibling::div[contains(@class, 'ReactCollapse--collapse')]//div[@class='submenu-list' and contains(text(), 'Train')]"));
    private static final Locator TIKETCOM_BALANCE_AMOUNT = new Locator("TIKETCOM_BALANCE_AMOUNT", new By.ByXPath("//div[contains(text(),'tiket.com balance at KAI (latest):')]//span[contains(text(),'IDR ')]"));
    private static final Locator TIKETCOM_LAST_BALANCE_AMOUNT = new Locator("TIKETCOM_LAST_BALANCE_AMOUNT", new By.ByXPath("//span[@class='amount']"));
    private static final Locator VIEW_SALES_REPORT = new Locator("VIEW_SALES_REPORT", new By.ByXPath("//button[@class='view-button' and contains(text(), 'VIEW')]"));
    private static final Locator OLDEST_TO_NEWEST_SORT = new Locator("OLDEST_TO_NEWEST_SORT", new By.ByXPath("//input[@id='DESC']/following-sibling::label[contains(text(),'Oldest to Newest')]"));
    private static final Locator NO_DATA_AVAILABLE = new Locator("NO_DATA_AVAILABLE", new By.ByXPath("//div[@class='no-result' and contains(text(), 'No Data Available')]"));

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public TrainFinancePage(Session<T> session) {
        super(session);
    }

    public void goToFinanceMenu(){
        driverTools.click(FINANCE_MENU);
        driverTools.click(FINANCE_TRAIN_SUBMENU);
    }

    public boolean verifySeeTiketcomBalanceAtKAI() {
        boolean isBalanceDisplayed = driverTools.waitForElementToAppear(TIKETCOM_BALANCE_AMOUNT).isPresent();
        boolean isLastBalanceDisplayed = driverTools.waitForElementToAppear(TIKETCOM_LAST_BALANCE_AMOUNT).isPresent();

        return isBalanceDisplayed && isLastBalanceDisplayed;
    }

    public boolean verifyBalanceIsEqual() {
        String balanceText = String.valueOf(driverTools.getText(TIKETCOM_BALANCE_AMOUNT));
        String lastBalanceText = String.valueOf(driverTools.getText(TIKETCOM_LAST_BALANCE_AMOUNT));

        String balanceNumeric = extractNumericText(balanceText);
        String lastBalanceNumeric = extractNumericText(lastBalanceText);
        Assertion.assertThat("Verify the balance is equal", balanceNumeric.equals(lastBalanceNumeric), is(true));
        return true;
    }

    private String extractNumericText(String text) {
        Pattern pattern = Pattern.compile("\\d{1,3}(,\\d{3})*");
        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            return matcher.group();
        } else {
            throw new IllegalStateException("No matching numeric text found.");
        }
    }

    public void seeSalesReport(){
        LocalDate todayDate = LocalDate.now();
        LocalDate sevenDaysBeforeToday = todayDate.minusDays(7);

        String formattedSevenDaysBeforeToday = formatDate(sevenDaysBeforeToday);
        String formattedTodayDate = formatDate(todayDate);

        Locator FROM_DATE = new Locator("FROM_DATE", new By.ByXPath("//p[contains(text(), 'From')]/following-sibling::div[@class='wrapper-datepicker datepicker']//input[@value='" + formattedSevenDaysBeforeToday + "']"));
        Locator TO_DATE = new Locator("TO_DATE", new By.ByXPath("//p[contains(text(), 'From')]/following-sibling::div[@class='wrapper-datepicker datepicker']//input[@value='" + formattedTodayDate + "']"));

        driverTools.waitForElementToAppear(OLDEST_TO_NEWEST_SORT);
        driverTools.click(OLDEST_TO_NEWEST_SORT);

        if (isDefaultDateRange(FROM_DATE, formattedSevenDaysBeforeToday) && isDefaultDateRange(TO_DATE, formattedTodayDate)) {
            driverTools.click(VIEW_SALES_REPORT);
        } else {
            throw new IllegalArgumentException("The date range is not the default of 7 days.");
        }
    }

    private String formatDate(LocalDate date) {
        return date.format(FORMATTER);
    }

    private boolean isDefaultDateRange(Locator locator, String expectedDate) {
        return driverTools.getValueAttribute(locator, "value").equals(expectedDate);
    }

    public boolean verifySeeSalesReport() {
        return driverTools.isElementDisplayed(NO_DATA_AVAILABLE);
    }

}
