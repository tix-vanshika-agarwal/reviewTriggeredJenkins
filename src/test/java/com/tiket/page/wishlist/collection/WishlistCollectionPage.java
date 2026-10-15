package com.tiket.page.wishlist.collection;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.hamcrest.Matchers.is;

public class WishlistCollectionPage<T extends WebDriver> extends BasePage<T> {

    private static final Locator COLLECTION_NAME = new Locator("COLLECTION_NAME", AppiumBy.xpath("//h3[contains(@class, 'CoverImageBanner_banner_title')]"));
    private static final Locator SELECTED_TAB_NAME = new Locator("SELECTED_TAB_NAME", AppiumBy.xpath("//span[contains(@class, 'Tabs_tab_text') and contains(@class, 'Text_variant_activity')]"));
    private static final Locator UNSELECTED_TAB_NAME = new Locator("UNSELECTED_TAB_NAME", AppiumBy.xpath("//span[contains(@class, 'Tabs_tab_text') and contains(@class, 'Text_variant_lowEmphasis')]"));
    private static final Locator ALL_TAB = new Locator("ALL_TAB", AppiumBy.xpath("//span[contains(@class, 'Tabs_tab_text') and (text()='Semua' or text()='All')]"));
    private static final Locator HOTEL_TAB = new Locator("HOTEL_TAB", AppiumBy.xpath("//span[contains(@class, 'Tabs_tab_text') and (text()='Hotel' or text()='Hotels')]"));
    private static final Locator NHA_TAB = new Locator("NHA_TAB", AppiumBy.xpath("//span[contains(@class, 'Tabs_tab_text') and (text()='Vila & Apt.' or text()='Villas & Apt.')]"));
    private static final Locator TTD_TAB = new Locator("TTD_TAB", AppiumBy.xpath("//span[contains(@class, 'Tabs_tab_text') and (text()='To Do' or text()='To Dos')]"));
    private static final Locator CONTENT_AMOUNT = new Locator("CONTENT_AMOUNT", AppiumBy.xpath("//span[contains(@class, 'WishlistFilter_amount')]"));
    private static final Locator CONTENT_TITLES = new Locator("CONTENT_TITLES", AppiumBy.xpath("//p[contains(@class, 'ProductInfo_product_title')]"));

    public WishlistCollectionPage(Session<T> session) {
        super(session);
    }

    public String getCollectionName() {
        driverTools.switchToWebViewUrl("wishlist/collection");
        String collectionName = driverTools.getText(COLLECTION_NAME).orElseThrow();
        driverTools.switchToNative();

        return collectionName;
    }

    public Set<String> getTabbingContent() {
        Set<String> hotelContent = getHotelCollectionContent();
        Set<String> homesContent = getNHACollectionContent();
        Set<String> todoContent = getTTDCollectionContent();

        Set<String> filteredContents = new HashSet<>();
        filteredContents.addAll(hotelContent);
        filteredContents.addAll(homesContent);
        filteredContents.addAll(todoContent);

        return filteredContents;
    }

    public int getAllCollectionContentAmount() {
        driverTools.switchToWebViewUrl("wishlist/collection");

        int contentAmount = 0;
        String selectedTab = driverTools.getText(SELECTED_TAB_NAME).orElseThrow();
        String allTab = driverTools.getText(ALL_TAB).orElseThrow();

        if (!selectedTab.equals(allTab)) {
            driverTools.clickWithJavascriptExecutor(ALL_TAB);
        }

        try {
            contentAmount = Integer.parseInt(driverTools.getText(CONTENT_AMOUNT).orElseThrow());
        } catch (NoSuchElementException e) {
            logger.log("collection is empty");
        }

        driverTools.switchToNative();
        return contentAmount;
    }

    public Set<String> getAllCollectionContent() {
        driverTools.switchToWebViewUrl("wishlist/collection");

        int contentAmount = 0;
        Set<String> contentNamesSet = new HashSet<>();
        String selectedTab = driverTools.getText(SELECTED_TAB_NAME).orElseThrow();
        String allTab = driverTools.getText(ALL_TAB).orElseThrow();

        if (!selectedTab.equals(allTab)) {
            driverTools.clickWithJavascriptExecutor(ALL_TAB);
        }
        logger.log("Waiting for elements to load");
        driverTools.waitForElementToDisappear(CONTENT_TITLES);

        try {
            contentAmount = Integer.parseInt(driverTools.getText(CONTENT_AMOUNT).orElseThrow());
        } catch (NoSuchElementException e) {
            logger.log("collection is empty");
        }

        for (int i = 0; i < 10 && contentNamesSet.size() != contentAmount; i++) {
            List<String> contentNames = driverTools.getTexts(CONTENT_TITLES);
            contentNamesSet.addAll(contentNames);

            driverTools.swipeUp();
        }

        driverTools.switchToNative();
        return contentNamesSet;
    }

    public Set<String> getHotelCollectionContent() {
        driverTools.switchToWebViewUrl("wishlist/collection");

        int contentAmount = 0;
        Set<String> contentNamesSet = new HashSet<>();
        String selectedTab = driverTools.getText(SELECTED_TAB_NAME).orElseThrow();
        String allTab = driverTools.getText(HOTEL_TAB).orElseThrow();

        if (!selectedTab.equals(allTab)) {
            driverTools.clickWithJavascriptExecutor(HOTEL_TAB);
        }
        logger.log("Waiting for elements to load");
        driverTools.waitForElementToDisappear(CONTENT_TITLES);

        try {
            contentAmount = Integer.parseInt(driverTools.getText(CONTENT_AMOUNT).orElseThrow());
        } catch (NoSuchElementException e) {
            logger.log("collection is empty");
        }

        for (int i = 0; i < 10 && contentNamesSet.size() != contentAmount; i++) {
            List<String> contentNames = driverTools.getTexts(CONTENT_TITLES);
            contentNamesSet.addAll(contentNames);

            driverTools.swipeUp();
        }

        driverTools.switchToNative();
        return contentNamesSet;
    }

    public Set<String> getNHACollectionContent() {
        driverTools.switchToWebViewUrl("wishlist/collection");

        int contentAmount = 0;
        Set<String> contentNamesSet = new HashSet<>();
        String selectedTab = driverTools.getText(SELECTED_TAB_NAME).orElseThrow();
        String allTab = driverTools.getText(NHA_TAB).orElseThrow();

        if (!selectedTab.equals(allTab)) {
            driverTools.clickWithJavascriptExecutor(NHA_TAB);
        }
        logger.log("Waiting for elements to load");
        driverTools.waitForElementToDisappear(CONTENT_TITLES);

        try {
            contentAmount = Integer.parseInt(driverTools.getText(CONTENT_AMOUNT).orElseThrow());
        } catch (NoSuchElementException e) {
            logger.log("collection is empty");
        }

        for (int i = 0; i < 10 && contentNamesSet.size() != contentAmount; i++) {
            List<String> contentNames = driverTools.getTexts(CONTENT_TITLES);
            contentNamesSet.addAll(contentNames);

            driverTools.swipeUp();
        }

        driverTools.switchToNative();
        return contentNamesSet;
    }

    public Set<String> getTTDCollectionContent() {
        driverTools.switchToWebViewUrl("wishlist/collection");

        int contentAmount = 0;
        Set<String> contentNamesSet = new HashSet<>();
        String selectedTab = driverTools.getText(SELECTED_TAB_NAME).orElseThrow();
        String allTab = driverTools.getText(TTD_TAB).orElseThrow();

        if (!selectedTab.equals(allTab)) {
            driverTools.clickWithJavascriptExecutor(TTD_TAB);
        }
        logger.log("Waiting for elements to load");
        driverTools.waitForElementToDisappear(CONTENT_TITLES);

        try {
            contentAmount = Integer.parseInt(driverTools.getText(CONTENT_AMOUNT).orElseThrow());
        } catch (NoSuchElementException e) {
            logger.log("collection is empty");
        }

        for (int i = 0; i < 10 && contentNamesSet.size() != contentAmount; i++) {
            List<String> contentNames = driverTools.getTexts(CONTENT_TITLES);
            contentNamesSet.addAll(contentNames);

            driverTools.swipeUp();
        }

        driverTools.switchToNative();
        return contentNamesSet;
    }
}