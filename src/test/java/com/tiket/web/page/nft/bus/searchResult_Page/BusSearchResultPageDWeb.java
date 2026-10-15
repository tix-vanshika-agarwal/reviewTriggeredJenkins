package com.tiket.web.page.nft.bus.searchResult_Page;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.hamcrest.core.Is.is;

public class BusSearchResultPageDWeb <T extends WebDriver> extends BusSearchResultPageWeb<T> {

    public BusSearchResultPageDWeb(Session<T> session) {
        super(session);
    }

    private static final Locator CHANGE_ORIGIN = new Locator("CHANGE_ORIGIN", new By.ByXPath("(//span[contains(@class, 'ChangeSearch_origin')])[1]"));
    private static final Locator CLOSE_POP_UP = new Locator("CLOSE_POP_UP", new By.ByXPath("//*[contains(@class, 'Modal_modal_close')]"));
    private static final Locator CHANGE_DESTINATION = new Locator("CHANGE_DESTINATION", new By.ByXPath("(//span[contains(@class, 'ChangeSearch_destination')])[1]"));
    private static final Locator CHANGE_PASSENGER = new Locator("CHANGE_PASSENGER", new By.ByXPath("(//*[contains(@class, 'ChangeSearch_passenger')])[1]"));
    private static final Locator PASSENGER_INPUT = new Locator("PASSENGER_INPUT", new By.ByXPath("//input[contains(@class, 'QuantityEditor_quantity_input')]"));
    private static final Locator CLASS_FILTER = new Locator("CLASS_FILTER", new By.ByXPath("//*[contains(@class, 'FilterSortForm_section_title')]//*[contains(text(), 'Kelas') or contains(text(), 'Class')]"));
    private static final Locator EXECUTIVE_CLASS = new Locator("EXECUTIVE_CLASS", new By.ByXPath("//*[contains(@class, 'CheckStyleForm_label_sublabel_container')]//*[contains(text(), 'Eks') or contains(text(), 'Ex') or contains(text(), 'EX')]"));
    private static final Locator SAVE_FILTER = new Locator("SAVE_FILTER", new By.ByXPath("//*[contains(@class, 'Button_variant_primary')]"));

    public void verifySearchResultIsCorrect(String origin, String destination, String departureDate, String returnDate, int totalPassenger, String language) {
        driverTools.waitForElementToAppear(SEARCH_RESULT);
        Assertion.assertThat("Search result is displayed", driverTools.isElementDisplayed(SEARCH_RESULT), is(true));

        driverTools.scrollIntoWebView(CHANGE_ORIGIN);
        driverTools.clickWithJavascriptExecutor(CHANGE_ORIGIN);
        WebElement changeOrigin = driverTools.waitForElementToAppear(getChangeLocationLocator(origin)).orElseThrow();
        Assertion.assertThat("Origin location is correct in SRP", driverTools.isElementDisplayed(changeOrigin), is(true));
        driverTools.click(CLOSE_POP_UP);

        driverTools.scrollIntoWebView(CHANGE_DESTINATION);
        driverTools.clickWithJavascriptExecutor(CHANGE_DESTINATION);
        WebElement changeDestination = driverTools.waitForElementToAppear(getChangeLocationLocator(destination)).orElseThrow();
        Assertion.assertThat("Destination location is correct in SRP", driverTools.isElementDisplayed(changeDestination), is(true));
        driverTools.click(CLOSE_POP_UP);

        Assertion.assertThat("Departure date is correct in SRP", driverTools.isElementDisplayed(getChangeDateLocator(formatDate(departureDate, language))), is(true));

        if (returnDate != null && !returnDate.isEmpty()) {
            Assertion.assertThat("Return date is correct in SRP", driverTools.isElementDisplayed(getChangeDateLocator(formatDate(returnDate, language))), is(true));
        }

        driverTools.scrollIntoWebView(CHANGE_PASSENGER);
        driverTools.clickWithJavascriptExecutor(CHANGE_PASSENGER);
        WebElement passengerInput = driverTools.waitForElementToAppear(PASSENGER_INPUT).orElseThrow();
        String srpTotalPassenger = driverTools.getValue(passengerInput);
        Assertion.assertThat("Total passenger is correct in SRP", srpTotalPassenger, is(Integer.toString(totalPassenger)));
        driverTools.click(CLOSE_POP_UP);
    }

    private Locator getChangeLocationLocator(String location) {
        return new Locator("CHANGE_LOCATION", AppiumBy.xpath("//*[contains(@class, 'List_title') and text()='" + location + "']"));
    }

    private Locator getChangeDateLocator(String date) {
        return new Locator("CHANGE_DATE", AppiumBy.xpath("//*[contains(text(), '" + date + "')]"));
    }

    public void selectFilterByExecutiveClass() {
        driverTools.waitForElementToAppear(CLASS_FILTER);
        driverTools.click(CLASS_FILTER);
        driverTools.clickWithJavascriptExecutor(EXECUTIVE_CLASS);
        driverTools.click(SAVE_FILTER);

        driverTools.waitForElementToDisappear(SAVE_FILTER);
    }

    public void editPassengerQueryParam(String queryParam, String newValue) {

        logger.log("On DWEB, no need to change the value of " + queryParam + " to " + newValue);
    }
}
