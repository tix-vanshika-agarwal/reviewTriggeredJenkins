package com.tiket.web.dashboard.page.affiliate;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;

public class AffiliateOrderTable<T extends WebDriver> extends BasePage<T> {
    public AffiliateOrderTable(Session<T> session) {
        super(session);
    }

    private static final Locator ORDER_TABLE = new Locator("ORDER_TABLE", By.xpath("//span[text()='Order Table']"));
    private static final Locator ITINERARY_ID = new Locator("ITINERARY_ID", By.xpath("//span[text()='Itinerary ID']"));
    private static final Locator ITINERARY_ID_INPUT = new Locator("ITINERARY_ID_INPUT", By.xpath("//input[@id='itinerary-id']"));
    private static final Locator SEARCH = new Locator("SEARCH", By.xpath("//span[text()='Search']"));
    private static final Locator RESET_FILTER = new Locator("RESET_FILTER", By.xpath("//span[text()='Reset Filter']"));
    private static final Locator AFFILIATE_REFERENCE_ID = new Locator("AFFILIATE_REFERENCE_ID", By.xpath("//p[text()='Affiliate Ref ID']"));
    private static final Locator NO_DATA_AVAILABLE = new Locator("NO_DATA_AVAILABLE", By.xpath("//h1[text()='No data available']"));
    private static final Locator ORDER_ID = new Locator("ORDER_ID", By.xpath("//input[@id='order-id' and contains(@class, 'styles_input__')]"));
    private static final Locator ORDER_ID_VERIFY = new Locator("ORDER_ID_VERIFY", By.xpath("(//div[contains(@class, 'flex') and contains(@class, 'gap-[4px]')]/p)[2]"));
    private static final Locator REFUND_INFO_BUTTON = new Locator("REFUND_INFO_BUTTON", By.xpath("//button[normalize-space()='View Details']"));

    public void goToItineraryId() {
        driverTools.waitForElementToAppear(ORDER_TABLE, 5);
        driverTools.click(ORDER_TABLE);
        driverTools.waitForElementToAppear(ITINERARY_ID, 5);
        driverTools.click(ITINERARY_ID);
    }

    public void inputItineraryId(String itinerary) {
        driverTools.waitForElementToAppear(ITINERARY_ID_INPUT);
        driverTools.click(ITINERARY_ID_INPUT);
        driverTools.sendKeys(ITINERARY_ID_INPUT, itinerary);
    }

    public void searchItineraryId() {
        driverTools.waitForElementToAppear(SEARCH, 5);
        driverTools.click(SEARCH);
        driverTools.waitForElementToAppear(AFFILIATE_REFERENCE_ID, 5);
        Optional<String> actualMessage11 = driverTools.getText(AFFILIATE_REFERENCE_ID);
        String actualMessage1 = actualMessage11.orElseThrow();
        String expectedMessage1 = "Affiliate Ref ID";
        Assertion.assertThat("Verify search result", actualMessage1, equalTo(expectedMessage1));
    }

    public void resetItineraryId() {
        driverTools.waitForElementToAppear(RESET_FILTER, 5);
        driverTools.click(RESET_FILTER);
        driverTools.waitForElementToAppear(NO_DATA_AVAILABLE, 5);
        Optional<String> actualMessage11 = driverTools.getText(NO_DATA_AVAILABLE);
        String actualMessage1 = actualMessage11.orElseThrow();
        String expectedMessage1 = "No data available";
        Assertion.assertThat("ReferenceID page is reset", actualMessage1, equalTo(expectedMessage1));
    }

    public void inputOrderId(String orderId) {
        driverTools.waitForElementToAppear(ORDER_TABLE, 5);
        driverTools.click(ORDER_TABLE);
        driverTools.waitForElementToAppear(ORDER_ID, 5);
        driverTools.click(ORDER_ID);
        driverTools.sendKeys(ORDER_ID, orderId);
        driverTools.waitForElementToAppear(SEARCH, 5);
        driverTools.click(SEARCH);
    }

    public void searchOrderId(String expectedOrderId) {
        driverTools.waitForElementToAppear(SEARCH, 5);
        driverTools.click(SEARCH);
        driverTools.waitForElementToAppear(ORDER_ID_VERIFY, 5);
        String actualOrderId = driverTools.getText(ORDER_ID_VERIFY).orElseThrow();
        logger.log("Actual order id: "+ actualOrderId);
        Assertion.assertThat("Verify search result", expectedOrderId, is(actualOrderId));
    }

    public void goToRefundInfo() {
       driverTools.waitForElementToAppear(REFUND_INFO_BUTTON, 5);
       driverTools.scrollIntoWebView(REFUND_INFO_BUTTON);
       int handleCountBeforeClick = driverTools.getWindowHandles().size();
       driverTools.clickWithJavascriptExecutor(REFUND_INFO_BUTTON);
       driverTools.waitForNewTabAndSwitchToLatest(handleCountBeforeClick);
    }
}