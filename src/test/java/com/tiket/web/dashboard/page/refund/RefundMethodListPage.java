package com.tiket.web.dashboard.page.refund;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class RefundMethodListPage <T extends WebDriver> extends BasePage<T> {

    public RefundMethodListPage(Session<T> session) { super(session); }

    private static final Locator MENU_REFUND_METHOD = new Locator("MENU_REFUND_METHOD", By.xpath("//span[contains(text(),'Refund Method')]"));
    private static final Locator MENU_REFUND_CONFIGURATION = new Locator("MENU_REFUND_CONFIGURATION", By.xpath("//span[contains(text(),'Refund Configuration')]"));
    private static final Locator REFUND_METHOD_LIST_HEADING = new Locator("REFUND_METHOD_LIST_HEADING", By.xpath("//h1[contains(text(),'Refund Method List')]"));
    private static final Locator FILTER_INPUT_FIELD = new Locator("FILTER_INPUT_FIELD", By.xpath("//input[@id='filter-input-name-field']"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", By.xpath("//button[@type='submit' and contains(normalize-space(.),'Search')]"));
    private static final Locator RESET_FILTER_BUTTON = new Locator("RESET_FILTER_BUTTON", By.xpath("//button[@type='reset' and contains(normalize-space(.),'Reset Filter')]"));
    private static final Locator TABLE_HEADINGS = new Locator("TABLE_HEADINGS", By.xpath("//div[contains(@data-testid,'table')]//table//thead//tr/th"));
    private static final Locator TABLE_ROWS = new Locator("TABLE_ROWS", By.xpath("//div[contains(@data-testid,'table')]//table//tbody//tr"));


    public void goToRefundMethodListPage() {
        if (!driverTools.waitForElementToAppear(MENU_REFUND_METHOD).isPresent()){
            driverTools.scrollAndClick(MENU_REFUND_CONFIGURATION);
        }
        driverTools.scrollAndClick(MENU_REFUND_METHOD);
    }

    public boolean isOnRefundMethodListPage() {
        return driverTools.waitForElementToAppear(REFUND_METHOD_LIST_HEADING).isPresent();
    }

    private int tableHeadingIndex(String heading) {
        driverTools.waitForElementToAppear(TABLE_HEADINGS);
        List<WebElement> headings = driverTools.elements(TABLE_HEADINGS).orElseThrow(() -> new IllegalStateException("No headings found"));
        if (headings.isEmpty()) {
            return -1;
        }

        for (int i = 0; i < headings.size(); i++) {
            if (headings.get(i).getText().equalsIgnoreCase(heading)) {
                return i;
            }
        }

        return -1;
    }

    public String getTableFirstValueByHeading(String heading) {
        int index = tableHeadingIndex(heading);
        if (index == -1) {
            throw new NoSuchElementException("No heading found for heading: " + heading);
        }
        Locator elementLocator = new Locator("ELEMENT", By.xpath("//div[contains(@data-testid,'table')]//table//tbody//tr[1]//td[%d]".formatted(index + 1)));
        WebElement element = driverTools.waitForElementToAppear(elementLocator).orElseThrow(() -> new NoSuchElementException("Element not found"));
        String text = driverTools.getText(element);
        if (text.isBlank()) {
            text = element.getAttribute("textContent").trim();
        }

        logger.log("First value for %s: %s".formatted(heading, text));
        return text;
    }

    public int getTableValuesCount() {
        driverTools.waitForElementToAppear(TABLE_HEADINGS);
        driverTools.waitForElementToAppear(TABLE_ROWS);
        List<WebElement> elements = driverTools.elements(TABLE_ROWS).orElseThrow(() -> new NoSuchElementException("No rows found"));
        logger.log("Getting table values count: " + elements.size());
        return elements.size();
    }

    public int getTableValuesCount(String heading, String value) {
        int index = tableHeadingIndex(heading);
        if (index == -1) {
            return 0;
        }

        driverTools.waitForElementToAppear(TABLE_ROWS);
        Locator loc = new Locator("loc", By.xpath("//div[contains(@data-testid,'table')]//table//tbody//tr//td[%d][normalize-space(.)='%s']".formatted(index + 1, value)));
        List<WebElement> elements = driverTools.elements(loc).orElseThrow(() -> new NoSuchElementException("Element not found"));
        logger.log("Table with heading: %s and value: %s count = %d".formatted(heading, value, elements.size()));
        return elements.size();
    }

    public void setFilterInputFieldValue(String filterInputFieldValue) {
        driverTools.clearInput(FILTER_INPUT_FIELD);
        driverTools.sendKeys(FILTER_INPUT_FIELD,  filterInputFieldValue);
    }
    public String getFilterInputFieldValue() {
        return driverTools.getValue(FILTER_INPUT_FIELD);
    }

    public void clickSearchButton(){
        driverTools.scrollAndClick(SEARCH_BUTTON);
        driverTools.dummyWait(2);
    }
    public Boolean isSearchButtonClickable() {
        return driverTools.waitForElementToBeClickable(SEARCH_BUTTON).isPresent();
    }

    public void clickResetFilterButton(){
        driverTools.scrollAndClick(RESET_FILTER_BUTTON);
        driverTools.dummyWait(2);
    }
    public Boolean isResetFilterButtonClickable() {
        return driverTools.waitForElementToBeClickable(RESET_FILTER_BUTTON).isPresent();
    }


}
