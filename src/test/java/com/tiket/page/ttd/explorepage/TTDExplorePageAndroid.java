package com.tiket.page.ttd.explorepage;

import com.tiket.appium.Swipe;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.page.ttd.searchresultpage.TTDSearchResultPage;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.NoSuchElementException;

import static com.tiket.core.CoreFunctions.generateRandomInt;

public class TTDExplorePageAndroid<T extends WebDriver> extends TTDExplorePage<T> {

    public static final Locator CONTENT_EXPLOREPAGE = new Locator("CONTENT_EXPLOREPAGE", AppiumBy.id("rv_destination"));
    public static final Locator SEARCHBAR = new Locator("SEARCHBAR", AppiumBy.id("tds_search_et"));
    public static final Locator BACK_BUTTON = new Locator("BACK_BUTTON", AppiumBy.id("tds_icon_navigation_toolbar"));
    public static final Locator POPULAR_DESTINATION = new Locator("POPULAR_DESTINATION", AppiumBy.id("rv_popular_destination"));
    public static final Locator SEARCH_BAR_SEARCHPAGE = new Locator("SEARCH_BAR_SEARCHPAGE", AppiumBy.id("tds_search_et"));
    public static final Locator CLEAR_SEARCH_INPUT = new Locator("CLEAR_SEARCH_INPUT", AppiumBy.id("tds_search_clear"));
    public static final Locator LOCATION_CHIPS_BUTTON = new Locator("LOCATION_CHIPS_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'rv_destination')]/*[2]//*[contains(@resource-id,'tds_chips_text')]"));
    private static final Locator LOCATION_NAME_ONLY_CHIPS_BUTTON = new Locator("LOCATION_NAME_ONLY_CHIPS_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'rv_destination')]/*[2]//*[contains(@resource-id,'tds_chips_text')]"));
    public static final Locator ALL_LOCATION_CHIPS_BUTTON = new Locator("ALL_LOCATION_CHIPS_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'rv_destination')]//*[contains(@resource-id,'tds_chips_text') and preceding-sibling::*[contains(@resource-id,'tds_chips_icon_left')]]"));
    public static final Locator NEAREST_LOCATION_SECTION = new Locator("NEAREST_LOCATION_SECTION", AppiumBy.id("cg_favorite"));
    public static final Locator INDONESIA_POPULAR_DESTINATION = new Locator("INDONESIA_POPULAR_DESTINATION", AppiumBy.xpath("//*[contains(@resource-id,'rv_destination')]//*[contains(@resource-id,'tv_suggestion_title') and contains(@text,'Indonesia')]"));
    public static final Locator WORLD_POPULAR_DESTINATION = new Locator("WORLD_POPULAR_DESTINATION", AppiumBy.xpath("//*[contains(@resource-id,'rv_destination')]//*[contains(@resource-id,'tv_suggestion_title') and (contains(@text,'Dunia') or contains(@text,'World'))]"));
    public static final Locator DESTINATION_LIST_TITLE = new Locator("DESTINATION_LIST_TITLE", AppiumBy.xpath("//*[contains(@resource-id,'v_divider')]/following-sibling::*[contains(@resource-id,'tv_header_title') and (contains(@text,'Destinasi') or contains(@text,'Destination'))]"));

    public TTDExplorePageAndroid(Session<T> session) {
        super(session);
    }

    @Override
    public boolean isExplorePageValid() {
        boolean searchBarElementDisplayed = driverTools.waitForElementToAppear(SEARCHBAR).isPresent();
        boolean contentExploreElementDisplayed = driverTools.waitForElementToAppear(CONTENT_EXPLOREPAGE).isPresent();
        boolean popularDestinationElementDisplayed = driverTools.waitForElementToAppear(POPULAR_DESTINATION).isPresent();

        return searchBarElementDisplayed && contentExploreElementDisplayed && popularDestinationElementDisplayed;
    }

    @Override
    public String selectDestination(String keyword, String type) {
        if (keyword.equalsIgnoreCase("random")) {
            if (type.equalsIgnoreCase("-")) {
                return selectRandomDestination();
            } else {
                throw new IllegalArgumentException("type must be '-' if the search_type is 'random'");
            }
        } else {
            return searchAndClickDestinationLabel(keyword, type);
        }
    }

    @Override
    public void inputSearchKeyword(String keyword) {
        String searchInput = "";
        if(keyword.contains("/")) {
            searchInput += keyword.split("/")[0];
        } else {
            searchInput += keyword;
        }

        driverTools.sendKeys(SEARCH_BAR_SEARCHPAGE, searchInput);
        driverTools.click(CLEAR_SEARCH_INPUT);
        driverTools.sendKeys(SEARCH_BAR_SEARCHPAGE, searchInput);
    }

    @Override
    public String selectRandomDestination() {
        int randomNumber = generateRandomInt(0,10);
        String selectedDestination = "";

        if(randomNumber < 5) {
            selectedDestination = pickAndClickDestinationCard();
        } else {
            selectedDestination = pickAndClickDestinationLabel();
        }

        return selectedDestination;
    }

    @Override
    public String searchAndClickDestinationLabel(String keyword, String type) {
        final Locator TARGET_DESTINATION_BUTTON = getTargetDestinationButtonLocator(keyword, type);
        final Locator TARGET_DESTINATION_LABEL = getTargetDestinationLabelLocator(keyword, type);

        driverTools.swipeToElement(TARGET_DESTINATION_BUTTON, Swipe.UP,5).orElseThrow();
        String clickedDestinationTitleLabel = driverTools.getText(TARGET_DESTINATION_LABEL).orElseThrow();
        driverTools.click(TARGET_DESTINATION_BUTTON);
        return clickedDestinationTitleLabel;
    }

    @Override
    public String pickAndClickDestinationCard() {

        String clickedDestinationTitleCard = "";
        boolean isElementDisplayed = false;
        for (int i = 0; i < 20; i++) {
            int randomIndex = generateRandomInt(1,6);
            final Locator POPULAR_DESTINATION_NAME = getPopularDestinationLocator(randomIndex);
            final Locator CLICKABLE_DESTINATION_CARD = getClickablePopularDestinationLocator(randomIndex);

            if (driverTools.isElementDisplayed(POPULAR_DESTINATION_NAME)) {
                clickedDestinationTitleCard += driverTools.getText(POPULAR_DESTINATION_NAME).get();
                logger.log("Found '"+ clickedDestinationTitleCard +"' destination card");
                isElementDisplayed = true;
                driverTools.click(CLICKABLE_DESTINATION_CARD);
                break;
            } else {
                logger.softWarning("Random index '"+ randomIndex +"' for destination card is not found");
            }
        }

        if (!isElementDisplayed) {
            throw new NoSuchElementException("After 20 times try, POPULAR_DESTINATION_NAME is still not found");
        }

        return clickedDestinationTitleCard;
    }

    @Override
    public String pickAndClickDestinationLabel() {
        String clickedDestinationTitleLabel = "";

        int doFilterLimit = generateRandomInt(1,3);
        logger.log("Scrolling up for '"+ doFilterLimit +"' times");
        for (int i = 0;i < doFilterLimit; i++) {
            driverTools.swipeUp();
        }

        boolean isElementDisplayed = false;
        for (int i = 0; i < 20; i++) {
            int randomIndex = generateRandomInt(1,10);
            final Locator TARGET_DESTINATION_LABEL = getTargetDestinationLocator(randomIndex);

            if (driverTools.isElementDisplayed(TARGET_DESTINATION_LABEL)) {
                clickedDestinationTitleLabel += driverTools.getText(TARGET_DESTINATION_LABEL).get();
                logger.log("Found '"+ clickedDestinationTitleLabel +"' destination label");
                isElementDisplayed = true;
                driverTools.click(TARGET_DESTINATION_LABEL);
                break;
            } else {
                logger.softWarning("Random index '"+ randomIndex +"' for destination label is not found");
            }
        }

        if (!isElementDisplayed) {
            throw new NoSuchElementException("After 20 times try, TARGET_DESTINATION_LABEL is still not found");
        }

        return clickedDestinationTitleLabel;
    }

    private Locator getTargetDestinationButtonLocator(String keyword, String type) {
        if(keyword.contains("/")) {
            String[] keywordSplit = keyword.split("/");
            if (type.contains("country")) {
                return new Locator("TARGET_DESTINATION_LABEL", AppiumBy.xpath("//*[contains(@resource-id, 'tv_title') and (@text='" + keywordSplit[0] + "' or @text='" + keywordSplit[1] + "')]/following-sibling::*[1]"));
            } else if (type.contains("city") || type.contains("region")) {
                return new Locator("TARGET_DESTINATION_LABEL", AppiumBy.xpath("//*[contains(@resource-id, 'tds_chips_text') and (@text='" + keywordSplit[0] + "' or @text='" + keywordSplit[1] + "')]"));
            }
        } else {
            if (type.contains("country")) {
                return new Locator("TARGET_DESTINATION_LABEL", AppiumBy.xpath("//*[contains(@resource-id, 'tv_title') and @text='" + keyword + "']/following-sibling::*[1]"));
            } else if (type.contains("city") || type.contains("region")) {
                return new Locator("TARGET_DESTINATION_LABEL", AppiumBy.xpath("//*[contains(@resource-id, 'tds_chips_text') and @text='" + keyword + "']"));
            }
        }
        logger.fail("Test data seach_type is not 'country', 'city', or 'region' please check the test data");
        throw new IllegalArgumentException("Test Data '"+ type +"' is not valid");
    }

    private Locator getTargetDestinationLabelLocator(String keyword, String type) {
        if(keyword.contains("/")) {
            String[] keywordSplit = keyword.split("/");
            if (type.contains("country")) {
                return new Locator("TARGET_DESTINATION_LABEL", AppiumBy.xpath("//*[contains(@resource-id, 'tv_title') and (@text='" + keywordSplit[0] + "' or @text='" + keywordSplit[1] + "')]"));
            } else if (type.contains("city") || type.contains("region")) {
                return new Locator("TARGET_DESTINATION_LABEL", AppiumBy.xpath("//*[contains(@resource-id, 'tds_chips_text') and (@text='" + keywordSplit[0] + "' or @text='" + keywordSplit[1] + "')]"));
            }
        } else {
            if (type.contains("country")) {
                return new Locator("TARGET_DESTINATION_LABEL", AppiumBy.xpath("//*[contains(@resource-id, 'tv_title') and @text='" + keyword + "']"));
            } else if (type.contains("city") || type.contains("region")) {
                return new Locator("TARGET_DESTINATION_LABEL", AppiumBy.xpath("//*[contains(@resource-id, 'tds_chips_text') and @text='" + keyword + "']"));
            }
        }
        logger.fail("Test data seach_type is not 'country', 'city', or 'region' please check the test data");
        throw new IllegalArgumentException("Test Data '"+ type +"' is not valid");
    }

    private Locator getTargetDestinationLocator(int index) {
        return new Locator("TARGET_DESTINATION_LABEL", AppiumBy.xpath("(//*[contains(@resource-id, 'tds_chips_text')])["+ index +"]"));
    }

    private Locator getPopularDestinationLocator(int index) {
        return new Locator("POPULAR_DESTINATION_NAME", AppiumBy.xpath("(//*[contains(@resource-id, 'rv_popular_destination')]//child::*[contains(@resource-id, 'tv_name')])["+ index +"]"));
    }

    private Locator getClickablePopularDestinationLocator(int index) {
        return new Locator("POPULAR_DESTINATION_NAME", AppiumBy.xpath("(//*[contains(@resource-id, 'rv_popular_destination')]//child::*[contains(@resource-id, 'tv_name')])["+ index +"]/parent::*"));
    }

    @Override
    public boolean isNearbyLocationSectionVisible() {
        return driverTools.waitForElementToAppear(NEAREST_LOCATION_SECTION).isPresent();
    }

    @Override
    public int getNearbyDestinationChips() {
        driverTools.waitForElementToAppear(LOCATION_CHIPS_BUTTON);
        return driverTools.elements(LOCATION_CHIPS_BUTTON).orElseThrow().size();
    }

    @Override
    public String chooseLocationChips(int index) {
        String chosenLocationChip = "";
        driverTools.waitForElementToAppear(LOCATION_CHIPS_BUTTON);
        List<WebElement> locationChips = driverTools.elements(LOCATION_CHIPS_BUTTON).orElseThrow();

        if (index < 0 || index >= locationChips.size()) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + locationChips.size());
        }

        chosenLocationChip += driverTools.getText(locationChips.get(index));
        driverTools.click(locationChips.get(index));
        return chosenLocationChip;
    }

    @Override
    public List<String> getNearbyDestinationChipText() {
        driverTools.waitForElementToAppear(LOCATION_NAME_ONLY_CHIPS_BUTTON);
        return driverTools.getTexts(LOCATION_NAME_ONLY_CHIPS_BUTTON);
    }

    @Override
    public String clickNearbyDestinationChips(String index) {
        String targetNearbyDestinationChip = index;
        if (targetNearbyDestinationChip.equalsIgnoreCase("all locations") || targetNearbyDestinationChip.equalsIgnoreCase("semua lokasi")) {
            driverTools.click(ALL_LOCATION_CHIPS_BUTTON);
        } else {
            int indexInt = 0;
            driverTools.waitForElementToAppear(LOCATION_NAME_ONLY_CHIPS_BUTTON);
            List<WebElement> nearbyDestinationChips = driverTools.elements(LOCATION_NAME_ONLY_CHIPS_BUTTON).orElseThrow();

            if (index.equalsIgnoreCase("random")) {
                indexInt += generateRandomInt(0, nearbyDestinationChips.size()-1);
            } else {
                indexInt += Integer.parseInt(index);
            }
            logger.log("indexInt : " + indexInt);

            targetNearbyDestinationChip = driverTools.getText(nearbyDestinationChips.get(indexInt));
            driverTools.click(nearbyDestinationChips.get(indexInt));
        }
        return targetNearbyDestinationChip;
    }
}
