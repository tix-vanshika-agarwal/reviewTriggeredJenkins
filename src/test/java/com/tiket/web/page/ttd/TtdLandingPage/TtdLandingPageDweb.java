package com.tiket.web.page.ttd.TtdLandingPage;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.model.SortItem;
import com.tiket.testbase.Assertion;
import org.openqa.selenium.*;

import java.util.List;

import static com.tiket.core.CoreFunctions.generateRandomInt;
import static org.hamcrest.Matchers.is;

public class TtdLandingPageDweb<T extends WebDriver> extends TtdLandingPageWeb<T> {

    public TtdLandingPageDweb(Session<T> session) {
        super(session);
    }

    private static final Locator POPULAR_ATTRACTIONS_RANK = new Locator("POPULAR_ATTRACTIONS_RANK", new By.ByXPath("//*[contains(text(),'Popular Attractions') or contains(text(),'Atraksi Populer')]//following-sibling::*//*[contains(@class,'PopularSection_search_result_item')]//*[contains(@class,'SearchItem_rank_indicator')]"));
    private static final Locator POPULAR_ATTRACTIONS_SEE_ALL_BUTTON = new Locator("POPULAR_ATTRACTIONS_SEE_ALL_BUTTON", new By.ByXPath("//*[contains(text(),'Popular Attractions') or contains(text(),'Atraksi Populer')]//following-sibling::*[contains(@class,'PopularSection_see_all')]//*[contains(@class,'Button')]"));
    private static final Locator POPULAR_DESTINATIONS_ITEM = new Locator("POPULAR_DESTINATIONS_ITEM", new By.ByXPath("//*[contains(text(),'Popular Destinations') or contains(text(),'Destinasi Populer')]//following-sibling::*//*[contains(@class,'PopularSection_search_result_item_content')]//span"));
    private static final Locator POPULAR_DESTINATIONS_ACTIVITIES = new Locator("POPULAR_DESTINATIONS_ACTIVITIES", new By.ByXPath("//*[contains(text(),'Popular Destinations') or contains(text(),'Destinasi Populer')]//ancestor::*[contains(@class, 'PopularSection_tab_list')]//following-sibling::*[contains(@class, 'Tabs_tab_panels')]//*[contains(@class,'PopularSection_search_result_item')]//*[contains(@class,'SearchItem_search_item_description')]"));
    private static final Locator POPULAR_DESTINATIONS_TAB = new Locator("POPULAR_DESTINATIONS_TAB", new By.ByXPath("//*[contains(text(),'Popular Destinations') or contains(text(),'Destinasi Populer')]"));

    private Locator popularDestinations(int index) {
        return new Locator("POPULAR_DESTINATIONS", new By.ByXPath("(//*[contains(text(),'Popular Destinations') or contains(text(),'Destinasi Populer')]//following-sibling::*//*[contains(@class,'PopularSection_search_result_item')]//*[contains(@class,'SearchItem_search_item_description')])["+index+"]//preceding-sibling::*[contains(@class,'SearchItem_search_item_title')]"));
    }

    @Override
    public void inputSearchKeyword(String keyword) {
        String searchInput = "";
        if(keyword.contains("/")) {
            searchInput += keyword.split("/")[0];
        } else {
            searchInput += keyword;
        }
        // Wait for the search input to render — landing page navigation can take a moment
        // and going straight to scrollIntoWebView fails (~40ms) on slow page loads.
        driverTools.waitForElementToAppear(SEARCH_INPUT, 20);
        driverTools.clickWithJavascriptExecutor(SEARCH_INPUT);
        driverTools.sendKeys(SEARCH_INPUT, searchInput);
    }

    @Override
    public void selectPopularAttractions(int rank) {
        driverTools.waitForElementToAppear(POPULAR_ATTRACTIONS_RANK);
        List<WebElement> toDoRank = driverTools.elements(POPULAR_ATTRACTIONS_RANK).orElseThrow();
        driverTools.clickWithJavascriptExecutor(toDoRank.get(rank-1));
    }

    @Override
    public int getPopularDestinationsAmount() {
        driverTools.waitForElementToAppear(POPULAR_DESTINATIONS_ITEM);
        List<WebElement> items = driverTools.elements(POPULAR_DESTINATIONS_ITEM).orElseThrow();
        return items.size();
    }

    @Override
    public String selectPopularDestinationsWithLotsActivity() {
        List<WebElement> popularDestinationsActivities = driverTools.waitForElementsToAppear(POPULAR_DESTINATIONS_ACTIVITIES).orElseThrow();
        int selectedIndex = 0;
        int selectedTotalActivities = 0;
        for (int i = 0; i < popularDestinationsActivities.size(); i++) {
            logger.log("index: " + i);
            String totalActivity = driverTools.getText(popularDestinationsActivities.get(i));
            logger.log("Getting the total activities: " + totalActivity);
            int currentTotalActivities = Integer.parseInt(totalActivity.replaceAll("[^0-9]", ""));
            if (currentTotalActivities > selectedTotalActivities) {
                selectedTotalActivities = currentTotalActivities;
                selectedIndex = i;
            }
        }

        selectedIndex++; // index start from 1
        logger.log("total of the most activities: " + selectedTotalActivities + " on index: " + selectedIndex);
        WebElement selectedDestination = driverTools.waitForElementToPresence(popularDestinations(selectedIndex)).orElseThrow();
        String destinations = driverTools.getText(selectedDestination);
        driverTools.clickWithJavascriptExecutor(selectedDestination);
        return destinations;
    }

    @Override
    public String selectPopularDestinations(String destination) {
        WebElement popularDestinationsTab = driverTools.waitForElementToAppear(POPULAR_DESTINATIONS_TAB).orElseThrow();
        driverTools.scrollIntoWebView(popularDestinationsTab);
        driverTools.clickWithJavascriptExecutor(popularDestinationsTab);
        String titleItem = "";
        List<WebElement> items = driverTools.waitForElementsToAppear(POPULAR_DESTINATIONS_ITEM).orElseThrow();
        if (destination.equalsIgnoreCase(String.valueOf(SortItem.RANDOM))) {
            int randomItem = generateRandomInt(0, items.size()-1);
            logger.log("index of randomItem -> " + randomItem);
            titleItem = driverTools.getText(items.get(randomItem));
            driverTools.clickWithJavascriptExecutor(items.get(randomItem));
        } else {
            for (WebElement webElement : items) {
                titleItem = popularDestinationTitleText(webElement);
                logger.log("Getting the popular destinations: " + titleItem);
                if (destination.equalsIgnoreCase(titleItem)) {
                    driverTools.scrollIntoWebView(webElement);
                    driverTools.clickWithJavascriptExecutor(webElement);
                    break;
                }
            }
        }
        return titleItem;
    }
    private String popularDestinationTitleText(WebElement element) {
        String text = driverTools.getText(element);
        if (text != null && !text.isBlank()) {
            return text.trim();
        }
        Object inner = ((JavascriptExecutor) driver).executeScript(
                "var e = arguments[0]; return (e && e.innerText) ? e.innerText : (e && e.textContent) || '';", element);
        return inner == null ? "" : inner.toString().trim();
    }

    @Override
    public boolean checkPopularAttractionsRank(int expectedTotalRank) {
        driverTools.waitForElementToAppear(POPULAR_ATTRACTIONS_RANK);
        List<WebElement> toDoRank = driverTools.elements(POPULAR_ATTRACTIONS_RANK).orElseThrow();
        Assertion.assertThat("Total rank should be equal to expected total rank", toDoRank.size(), is(expectedTotalRank));
        for (int i = 1; i <= expectedTotalRank; i++) {
            driverTools.scrollIntoWebView(toDoRank.get(i-1));
            Assertion.assertThat("Popular attractions should have rank " + i, driverTools.getText(toDoRank.get(i-1)), is(String.valueOf(i)));
        }
        return toDoRank.size() == expectedTotalRank;
    }

    @Override
    public void clickSeeAllPopularAttractions() {
        driverTools.scrollIntoWebView(POPULAR_ATTRACTIONS_SEE_ALL_BUTTON);
        driverTools.clickWithJavascriptExecutor(POPULAR_ATTRACTIONS_SEE_ALL_BUTTON);
    }

    @Override
    public void search() {
        driverTools.sendKeys(SEARCH_INPUT, String.valueOf(Keys.ENTER));
    }

    @Override
    public void searchProduct(String product) {
        driverTools.waitForElementsToAppear(SEARCH_INPUT);
        driverTools.click(SEARCH_INPUT);

        driverTools.sendKeys(SEARCH_INPUT, product);

        driverTools.waitForElementToAppear(FIRST_SEARCH_ITEM);
        driverTools.click(FIRST_SEARCH_ITEM);
    }
}
