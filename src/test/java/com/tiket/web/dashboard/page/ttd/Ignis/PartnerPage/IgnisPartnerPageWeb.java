package com.tiket.web.dashboard.page.ttd.Ignis.PartnerPage;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.is;

public class IgnisPartnerPageWeb<T extends WebDriver> extends WebPage<T> {
    public IgnisPartnerPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> IgnisPartnerPageWeb<?> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new IgnisPartnerPageDweb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new IgnisPartnerPageMweb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    // Latest Order Section
    private static final Locator LATEST_ORDER_PRODUCT_DROPDOWN = new Locator("LATEST_ORDER_PRODUCT_DROPDOWN", By.xpath("//div[@class='home-order-report'][1]//fieldset"));
    private static final Locator LATEST_ORDER_PRODUCT_DROPDOWN_SELECTION = new Locator("LATEST_ORDER_PRODUCT_DROPDOWN_SELECTION", By.xpath("//div[@class='menu-item']"));
    private static final Locator LATEST_ORDER_SEE_ALL_ORDER = new Locator("LATEST_ORDER_SEE_ALL_ORDER", By.xpath("//div[@class='home-order-report'][1]//div[@class='title']/a"));
    private static final Locator LATEST_ORDER_TODAY_CHIP = new Locator("LATEST_ORDER_TODAY_CHIP", By.xpath("//div[@class='home-order-report'][1]//button[@name='today']"));
    private static final Locator LATEST_ORDER_LAST_3_DAYS_CHIP = new Locator("LATEST_ORDER_LAST_3_DAYS_CHIP", By.xpath("//div[@class='home-order-report'][1]//button[@name='lastNDays-3']"));
    private static final Locator LATEST_ORDER_LAST_7_DAYS_CHIP = new Locator("LATEST_ORDER_LAST_7_DAYS_CHIP", By.xpath("//div[@class='home-order-report'][1]//button[@name='lastNDays-7']"));
    private static final Locator LATEST_ORDER_TABLE_ROWS_TICKETS_COUNT = new Locator("LATEST_ORDER_TABLE_ROWS_TICKETS_COUNT", By.xpath("//div[@class='home-order-report'][1]//table/tbody/tr/td[last()]"));
    private static final Locator LATEST_ORDER_TABLE_ROWS_TRANSACTION_DATE = new Locator("LATEST_ORDER_TABLE_ROWS_TRANSACTION_DATE", By.xpath("//div[@class='home-order-report'][1]//table/tbody/tr/td[1]"));
    private static final Locator LATEST_ORDER_TOTAL_TICKETS_COUNT = new Locator("LATEST_ORDER_TOTAL_TICKETS_COUNT", By.xpath("//div[@class='home-order-report'][1]//div[@class='right']/p"));
    private static final Locator LATEST_ORDER_EMPTY_RESULT = new Locator("LATEST_ORDER_EMPTY_RESULT", By.xpath("//div[@class='home-order-report'][1]/div[@class='general-empty-item']"));

    // Upcoming Visitor Section
    private static final Locator UPCOMING_VISITOR_PRODUCT_DROPDOWN = new Locator("UPCOMING_VISITOR_PRODUCT_DROPDOWN", By.xpath("//div[@class='home-order-report'][2]//fieldset"));
    private static final Locator UPCOMING_VISITOR_PRODUCT_DROPDOWN_SELECTION = new Locator("UPCOMING_VISITOR_PRODUCT_DROPDOWN_SELECTION", By.xpath("//div[@class='menu-item']"));
    private static final Locator UPCOMING_VISITOR_SEE_ALL_ORDER = new Locator("UPCOMING_VISITOR_SEE_ALL_ORDER", By.xpath("//div[@class='home-order-report'][2]//div[@class='title']/a"));
    private static final Locator UPCOMING_VISITOR_TODAY_CHIP = new Locator("UPCOMING_VISITOR_TODAY_CHIP", By.xpath("//div[@class='home-order-report'][2]//button[@name='today']"));
    private static final Locator UPCOMING_VISITOR_TOMORROW_CHIP = new Locator("UPCOMING_VISITOR_TOMORROW_CHIP", By.xpath("//div[@class='home-order-report'][2]//button[@name='tomorrow']"));
    private static final Locator UPCOMING_VISITOR_TABLE_ROWS_TICKETS_COUNT = new Locator("UPCOMING_VISITOR_TABLE_ROWS_TICKETS_COUNT", By.xpath("//div[@class='home-order-report'][2]//table/tbody/tr/td[last()]"));
    private static final Locator UPCOMING_VISITOR_TABLE_ROWS_VISIT_DATE = new Locator("UPCOMING_VISITOR_TABLE_ROWS_VISIT_DATE", By.xpath("//div[@class='home-order-report'][2]//table/tbody/tr/td[1]"));
    private static final Locator UPCOMING_VISITOR_TOTAL_TICKETS_COUNT = new Locator("UPCOMING_VISITOR_TOTAL_TICKETS_COUNT", By.xpath("//div[@class='home-order-report'][2]//div[@class='right']/p"));
    private static final Locator UPCOMING_VISITOR_EMPTY_RESULT = new Locator("UPCOMING_VISITOR_EMPTY_RESULT", By.xpath("//div[@class='home-order-report'][2]/div[@class='general-empty-item']"));

    // Things To Do Section
    private static final Locator LIVE_PRODUCT_COUNT_TEXT = new Locator("LIVE_PRODUCT_COUNT_TEXT", By.xpath("//div[@class='product-status-counter'][1]/div[@class='count']"));
    private static final Locator SEE_ALL_THINGS_TO_DO_HYPERLINK = new Locator("SEE_ALL_THINGS_TO_DO_HYPERLINK", By.xpath("//div[@class='home-product-summary']/a/button"));

    // Redeem Voucher Section
    private static final Locator REDEEM_CODE_INPUT = new Locator("REDEEM_CODE_INPUT", By.xpath("//input[@name='code']"));
    private static final Locator REDEEM_NOW_BUTTON = new Locator("REDEEM_NOW_BUTTON", By.xpath("//div[@class='home-redeem-form']/button"));

    public String selectProduct(String productTitle, Locator dropdown, Locator dropdownSelection) {
        driverTools.waitForElementToAppear(dropdown);
        driverTools.click(dropdown);

        driverTools.dummyWait(1);
        List<WebElement> productNames = driverTools.elementsUntilExist(dropdownSelection).get();

        String chosenProductText = "";
        if (productTitle.equalsIgnoreCase("random")) {
            int randomInt = CoreFunctions.generateRandomInt(1, productNames.size()-1);
            chosenProductText = driverTools.getText(productNames.get(randomInt));
            driverTools.click(productNames.get(randomInt));
        } else {
            for (WebElement e : productNames) {
                String productNameText = driverTools.getText(e);
                if (productNameText.equalsIgnoreCase(productTitle)) {
                    chosenProductText = productNameText;
                    driverTools.click(e);
                    break;
                }
            }

            if (chosenProductText.isEmpty()) {
                throw new NoSuchElementException("There is no option for '" + productTitle + "' on dropdown selection");
            }
        }

        return chosenProductText;
    }

    public String selectLatestOrderProduct(String productTitle) {
        return selectProduct(productTitle, LATEST_ORDER_PRODUCT_DROPDOWN, LATEST_ORDER_PRODUCT_DROPDOWN_SELECTION);
    }

    public String selectUpcomingVisitorProduct(String productTitle) {
        return selectProduct(productTitle, UPCOMING_VISITOR_PRODUCT_DROPDOWN, UPCOMING_VISITOR_PRODUCT_DROPDOWN_SELECTION);
    }

    public void applyLatestOrderQuickFilter(String quickFilterType) {
        switch (quickFilterType.toLowerCase()) {
            case "today" -> {driverTools.click(LATEST_ORDER_TODAY_CHIP);}
            case "last3day" -> {driverTools.click(LATEST_ORDER_LAST_3_DAYS_CHIP);}
            case "last7day" -> {driverTools.click(LATEST_ORDER_LAST_7_DAYS_CHIP);}
            default -> {throw new NoSuchElementException("There is no quickFilterType '"+ quickFilterType +"'");}
        }
    }

    public void applyUpcomingVisitorQuickFilter(String quickFilterType) {
        switch (quickFilterType.toLowerCase()) {
            case "today" -> {driverTools.click(UPCOMING_VISITOR_TODAY_CHIP);}
            case "tomorrow" -> {driverTools.click(UPCOMING_VISITOR_TOMORROW_CHIP);}
            default -> {throw new NoSuchElementException("There is no quickFilterType '"+ quickFilterType +"'");}
        }
    }

    public void clickLatestOrderSeeAllOrder() {
        driverTools.click(LATEST_ORDER_SEE_ALL_ORDER);
    }

    public void clickUpcomingVisitorSeeAllOrder() {
        driverTools.click(UPCOMING_VISITOR_SEE_ALL_ORDER);
    }

    public int getLatestOrderTotalTicketsText() {
        driverTools.dummyWait(1);
        return Integer.parseInt(driverTools.getText(LATEST_ORDER_TOTAL_TICKETS_COUNT).orElseThrow());
    }

    public int getUpcomingVisitorTotalTicketsText() {
        driverTools.dummyWait(1);
        return Integer.parseInt(driverTools.getText(UPCOMING_VISITOR_TOTAL_TICKETS_COUNT).orElseThrow());
    }

    public int getTotalTicketsTableRow(Locator emptyResult, Locator tableRow) {
        if (driverTools.waitForElementToAppear(emptyResult).isPresent()) {
            return 0;
        }

        driverTools.elementsUntilExist(tableRow);
        List<String> rowElements = driverTools.getTexts(tableRow);
        int result = 0;
        for (String e : rowElements) {
            result += Integer.parseInt(e);
            logger.log("result : " + result);
        }

        return result;
    }

    public int getLatestOrderTotalTicketsTableRow() {
        return getTotalTicketsTableRow(LATEST_ORDER_EMPTY_RESULT, LATEST_ORDER_TABLE_ROWS_TICKETS_COUNT);
    }

    public int getUpcomingVisitorTotalTicketsTableRow() {
        return getTotalTicketsTableRow(UPCOMING_VISITOR_EMPTY_RESULT, UPCOMING_VISITOR_TABLE_ROWS_TICKETS_COUNT);
    }

    public Boolean verifyTransactionDateWithQuickFilter(String quickFilterType) {
        if (driverTools.waitForElementToAppear(LATEST_ORDER_EMPTY_RESULT).isPresent()) {
            Assertion.assertThat("Result is empty, no data to be verified", true, is(true));
            return true;
        }

        LocalDate todayDate = LocalDate.now();
        LocalDate quickFilterLimitDate = null;

        switch (quickFilterType.toLowerCase()) {
            case "today" -> {quickFilterLimitDate = LocalDate.now();}
            case "last3day" -> {quickFilterLimitDate = LocalDate.now().minusDays(3);}
            case "last7day" -> {quickFilterLimitDate = LocalDate.now().minusDays(7);}
            default -> {throw new NoSuchElementException("There is no quickFilterType '"+ quickFilterType +"'");}
        }

        List<String> rowDateElements = driverTools.getTexts(LATEST_ORDER_TABLE_ROWS_TRANSACTION_DATE);
        for (int i = 0; i < rowDateElements.size(); i++) {
            int date = Integer.parseInt(rowDateElements.get(i).split(" ")[0]);
            int month = CoreFunctions.getMonthNumber(rowDateElements.get(i).split(" ")[1]);
            int year = Integer.parseInt(rowDateElements.get(i).split(" ")[2]);
            LocalDate dataDate = LocalDate.of(year, month, date);

            Assertion.assertThat("Assert if data date is valid", dataDate.compareTo(quickFilterLimitDate) >= 0 && dataDate.compareTo(todayDate) <= 0, is(true));
        }

        return true;
    }

    public Boolean verifyVisitDateWithQuickFilter(String quickFilterType) {
        if (driverTools.waitForElementToAppear(UPCOMING_VISITOR_EMPTY_RESULT).isPresent()) {
            Assertion.assertThat("Result is empty, no data to be verified", true, is(true));
            return true;
        }

        LocalDate quickFilterLimitDate = null;

        switch (quickFilterType.toLowerCase()) {
            case "today" -> {quickFilterLimitDate = LocalDate.now();}
            case "tomorrow" -> {quickFilterLimitDate = LocalDate.now().plusDays(1);}
            default -> {throw new NoSuchElementException("There is no quickFilterType '"+ quickFilterType +"'");}
        }

        List<String> rowDateElements = driverTools.getTexts(UPCOMING_VISITOR_TABLE_ROWS_VISIT_DATE);
        for (int i = 0; i < rowDateElements.size(); i++) {
            int date = Integer.parseInt(rowDateElements.get(i).split(" ")[0]);
            int month = CoreFunctions.getMonthNumber(rowDateElements.get(i).split(" ")[1]);
            int year = Integer.parseInt(rowDateElements.get(i).split(" ")[2]);
            LocalDate dataDate = LocalDate.of(year, month, date);

            Assertion.assertThat("Assert if data date is valid", dataDate.compareTo(quickFilterLimitDate) == 0, is(true));
        }

        return true;
    }
    public int getLiveProductCount() {
        driverTools.dummyWait(1);
        return Integer.parseInt(driverTools.getText(LIVE_PRODUCT_COUNT_TEXT).orElseThrow());
    }

    public void clickSeeAllThingToDo() {
        driverTools.click(SEE_ALL_THINGS_TO_DO_HYPERLINK);
    }

    public void inputRedeemCode(String redeemCode) {
        driverTools.waitForElementToAppear(REDEEM_CODE_INPUT);
        driverTools.sendKeys(REDEEM_CODE_INPUT, redeemCode);
    }

    public void submitRedeemCode() {
        driverTools.click(REDEEM_NOW_BUTTON);
    }

    public void goToPage() {
        switch (Configs.ENVIRONMENT) {
            case GK -> {driverTools.get("https://gatotkaca.tiket.com/ignis/");}
            case PREPROD -> {driverTools.get("https://preprod.tiket.com/ignis/");}
            case PROD -> {driverTools.get("https://www.tiket.com/ignis/");}
            default -> {throw new NoSuchElementException("There is no '"+ Configs.ENVIRONMENT.name() +"' environment");}
        }
    }
}
