package com.tiket.web.page.ttd.TtdLandingPage;

import com.tiket.model.Locator;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.model.SortItem;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.Configs;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static com.tiket.core.CoreFunctions.generateRandomInt;
import static org.hamcrest.Matchers.is;

public class TtdLandingPageMweb<T extends WebDriver> extends TtdLandingPageWeb<T> {

    public TtdLandingPageMweb(Session<T> session) {
        super(session);
    }

    public static final Locator SEARCH_ITEM = new Locator("SEARCH_ITEM", new By.ByXPath("//*[contains(@class,'SearchItem_search_item_description')]//preceding-sibling::*[contains(@class,'SearchItem_search_item_title')]"));
    private static final Locator POPULAR_ATTRACTIONS_RANK = new Locator("POPULAR_ATTRACTIONS_RANK", By.xpath("//div[contains(@class, 'Tabs_tab_panel_active')]//a[contains(@class, 'search_result_item')]//span[contains(@class, 'SearchItem_rank_indicator__')]"));
    private static final Locator POPULAR_ATTRACTIONS_TAB_BUTTON = new Locator("POPULAR_ATTRACTIONS_TAB_BUTTON", By.xpath("//span[text()='Popular Attractions' or text()='Atraksi Populer']"));
    private static final Locator POPULAR_DESTINATION_TAB_BUTTON = new Locator("POPULAR_DESTINATION_TAB_BUTTON", By.xpath("//span[text()='Popular Destinations' or text()='Destinasi Populer']"));
    private static final Locator SEARCH_SEE_ALL_BUTTON = new Locator("SEARCH_SEE_ALL_BUTTON", By.xpath("//div[contains(@class, 'Tabs_tab_panel_active')]//div[contains(@class, 'see_all_container')]//a"));
    private static final Locator POPULAR_DESTINATIONS_ITEM = new Locator("POPULAR_DESTINATIONS_ITEM", By.xpath("//div[contains(@class, 'Tabs_tab_panel_active__')]//a[contains(@class, 'PopularSection_search_result_item')]//span[contains(@class, 'SearchItem_search_item_title')]"));
    private static final Locator POPULAR_DESTINATIONS_ACTIVITIES = new Locator("POPULAR_DESTINATIONS_ACTIVITIES", By.xpath("//div[contains(@class, 'Tabs_tab_panel_active__')]//a[contains(@class, 'PopularSection_search_result_item')]//div[contains(@class, 'description')]"));

    private Locator popularDestinations(int index) {
        return new Locator("POPULAR_DESTINATIONS", By.xpath("(//div[contains(@class, 'Tabs_tab_panel_active__')]//a[contains(@class, 'PopularSection_search_result_item')])[" + index + "]"));
    }
    @Override
    public void inputSearchKeyword(String keyword) {
        String searchInput = "";
        if(keyword.contains("/")) {
            searchInput += keyword.split("/")[0];
        } else {
            searchInput += keyword;
        }
        driverTools.click(SEARCH_INPUT);
        WebElement search_inputs = driverTools.element(SEARCH_INPUT_INTERNAL).orElseThrow();
        driverTools.sendKeys(search_inputs,searchInput);
    }

    @Override
    public void search() {
        driverTools.sendKeys(SEARCH_INPUT_INTERNAL, String.valueOf(Keys.ENTER));
    }

    @Override
    public void selectRecommendedActivity(String keyword) {
        driverTools.waitForElementToAppear(SEARCH_ITEM);

        String keyword1, keyword2;
        if (keyword.contains("/")) {
            String[] keywords = keyword.split("/");
            keyword1 = keywords[0];
            keyword2 = keywords[1];
        } else {
            keyword1 = keyword;
            keyword2 = keyword;
        }

        String searchItem = driverTools.getText(SEARCH_ITEM).orElseThrow();
        if (searchItem.equalsIgnoreCase(keyword1) || searchItem.equalsIgnoreCase(keyword2)) {
            driverTools.click(SEARCH_ITEM);
        } else {
            List<WebElement> searchItems = driverTools.elements(SEARCH_ITEM).orElseThrow();
            for (WebElement searchItemElement : searchItems) {
                String searchItemText = driverTools.getText(searchItemElement);
                if (searchItemText.contains(keyword1) || searchItemText.contains(keyword2)) {
                    driverTools.scrollIntoWebView(searchItemElement);
                    driverTools.clickWithJavascriptExecutor(searchItemElement);
                    return;
                }
            }
            logger.warn("No " + keyword + " on the search item list, just getting " + searchItem);
        }
    }

    @Override
    public void selectPopularAttractions(int rank) {
        driverTools.scrollIntoWebView(POPULAR_ATTRACTIONS_TAB_BUTTON);
        driverTools.clickWithJavascriptExecutor(POPULAR_ATTRACTIONS_TAB_BUTTON);
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
        driverTools.waitForElementToAppear(POPULAR_DESTINATION_TAB_BUTTON);
        driverTools.click(POPULAR_DESTINATION_TAB_BUTTON);
        driverTools.dummyWait(3);

        List<WebElement> popularDestinationsActivities = driverTools.waitForElementsToAppear(POPULAR_DESTINATIONS_ACTIVITIES).orElseThrow();
        int selectedIndex = 1;
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
        driverTools.click(POPULAR_DESTINATION_TAB_BUTTON);
        String titleItem = "";
        List<WebElement> items = driverTools.waitForElementsToAppear(POPULAR_DESTINATIONS_ITEM).orElseThrow();
        if (destination.equalsIgnoreCase(String.valueOf(SortItem.RANDOM))) {
            int randomItem = generateRandomInt(0, items.size()-1);
            logger.log("index of randomItem -> " + randomItem);
            titleItem = driverTools.getText(items.get(randomItem));
            driverTools.clickWithJavascriptExecutor(items.get(randomItem));
        } else {
            for (WebElement webElement : items) {
                titleItem = driverTools.getText(webElement);
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

    @Override
    public boolean checkPopularAttractionsRank(int expectedTotalRank) {
        driverTools.scrollIntoWebView(POPULAR_ATTRACTIONS_TAB_BUTTON);
        driverTools.clickWithJavascriptExecutor(POPULAR_ATTRACTIONS_TAB_BUTTON);
        driverTools.waitForElementToAppear(POPULAR_ATTRACTIONS_RANK);
        List<WebElement> toDoRank = driverTools.elements(POPULAR_ATTRACTIONS_RANK).orElseThrow();
        for (int i = 1; i <= expectedTotalRank; i++) {
            driverTools.scrollIntoWebView(toDoRank.get(i-1));
            Assertion.assertThat("Popular attractions should have rank " + i, toDoRank.get(i-1).getText(), is(String.valueOf(i)));
        }
        return true;
    }

    @Override
    public void clickSeeAllPopularAttractions() {
        driverTools.scrollIntoWebView(POPULAR_ATTRACTIONS_TAB_BUTTON);
        driverTools.clickWithJavascriptExecutor(POPULAR_ATTRACTIONS_TAB_BUTTON);
        driverTools.scrollIntoWebView(SEARCH_SEE_ALL_BUTTON);
        driverTools.clickWithJavascriptExecutor(SEARCH_SEE_ALL_BUTTON);
    }

    @Override
    public void searchProduct(String product) {
        driverTools.waitForElementsToAppear(SEARCH_INPUT);
        driverTools.click(SEARCH_INPUT);

        driverTools.sendKeys(SEARCH_INPUT, product);

        driverTools.waitForElementToAppear(FIRST_SEARCH_PRODUCT_ITEM);
        driverTools.click(FIRST_SEARCH_PRODUCT_ITEM);
    }
}
