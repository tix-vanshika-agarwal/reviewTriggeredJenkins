package com.tiket.page.ttd.explorepage;

import com.tiket.appium.Swipe;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.page.ttd.searchresultpage.TTDSearchResultPage;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.tiket.core.CoreFunctions.generateRandomInt;

public class TTDExplorePageIOS<T extends WebDriver> extends TTDExplorePage<T> {

    public static final Locator SEARCH_BAR = new Locator("SEARCH_BAR", AppiumBy.accessibilityId("ttd_search_input"));
    public static final Locator DESTINATION_TITLE = new Locator("DESTINATION_TITLE", AppiumBy.xpath("//XCUIElementTypeStaticText[@name='Destinasi' or @name='Destinations']"));
    public static final Locator POPULAR_DESTINATION = new Locator("POPULAR_DESTINATION", AppiumBy.accessibilityId("ttd_popular_destination"));
    public static final Locator TARGET_DESTINATION_LABEL = new Locator("TARGET_DESTINATION_LABEL", AppiumBy.xpath("//XCUIElementTypeOther[@name='populer_destination_chip']//XCUIElementTypeStaticText"));  //FIXME: Need Better Locator
    public static final Locator NEAREST_LOCATION_CHIP_TEXT = new Locator("NEAREST_LOCATION_CHIP_TEXT", AppiumBy.xpath("//*[contains(@label,'Lihat semua aktivitas') or contains(@label,'See all activities')]//following-sibling::*//*//XCUIElementTypeStaticText"));
    private static final Locator LOCATION_NAME_ONLY_CHIPS_BUTTON = new Locator("LOCATION_NAME_ONLY_CHIPS_BUTTON", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@label,'Nearest Location') or contains(@label,'Lokasi Terdekat')]/following-sibling::*[1]/XCUIElementTypeOther[not(@name='ttd_all_location_chip')]//XCUIElementTypeStaticText"));
    public static final Locator ALL_LOCATION_CHIPS_BUTTON = new Locator("ALL_LOCATION_CHIPS_BUTTON", AppiumBy.name("ttd_all_location_chip"));
    public static final Locator INDONESIA_POPULAR_DESTINATION = new Locator("INDONESIA_POPULAR_DESTINATION", AppiumBy.xpath("//XCUIElementTypeScrollView/XCUIElementTypeOther/XCUIElementTypeOther[2]//XCUIElementTypeStaticText[contains(@label,'Indonesia')]"));
    public static final Locator WORLD_POPULAR_DESTINATION = new Locator("WORLD_POPULAR_DESTINATION", AppiumBy.xpath("//XCUIElementTypeScrollView/XCUIElementTypeOther/XCUIElementTypeOther[3]//XCUIElementTypeStaticText[contains(@label,'Dunia') or contains(@label,'World')]"));
    public static final Locator DESTINATION_LIST_TITLE = new Locator("DESTINATION_LIST_TITLE", AppiumBy.xpath("//XCUIElementTypeScrollView/XCUIElementTypeOther/XCUIElementTypeOther[4]//XCUIElementTypeStaticText[contains(@label,'Destination') or contains(@label,'Destinasi')]"));

    public TTDExplorePageIOS(Session<T> session) {
        super(session);
    }

    @Override
    public boolean isExplorePageValid() {
        driverTools.waitForElementToAppear(SEARCH_BAR);
        driverTools.waitForElementToAppear(POPULAR_DESTINATION);
        boolean searchBarElementDisplayed = driverTools.isElementDisplayed(SEARCH_BAR);
        boolean destinationTitleElementDisplayed = driverTools.isElementDisplayed(POPULAR_DESTINATION);

        return searchBarElementDisplayed && destinationTitleElementDisplayed;
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
        final Locator TARGET_DESTINATION_LABEL = getTargetDestinationLocator(keyword, type);
        final Locator TARGET_DESTINATION_BUTTON = getTargetDestinationLocatorButton(keyword, type);

        if(type.equalsIgnoreCase("city")) {
            driverTools.waitForElementToAppear(DESTINATION_TITLE,5);
            inputSearchKeyword(keyword);
            driverTools.waitForElementToAppear(TARGET_DESTINATION_LABEL,5);
        }

        driverTools.swipeToElement(TARGET_DESTINATION_LABEL, Swipe.UP, 10).orElseThrow();
        String clickedDestinationTitleLabel = driverTools.getValueAttribute(TARGET_DESTINATION_LABEL,"label");
        driverTools.click(TARGET_DESTINATION_BUTTON);
        return clickedDestinationTitleLabel;
    }

    private Locator getTargetDestinationLocatorButton(String keyword, String type) {
        if(keyword.contains("/")) {
            String[] keywordSplit = keyword.split("/");
            switch (type) {
                case "country" -> {return new Locator("TARGET_DESTINATION_COUNTRY", AppiumBy.xpath("//XCUIElementTypeStaticText[@name = 'ttd_country_name' and (contains(@label,'" + keywordSplit[0] + "') or contains(@label,'" + keywordSplit[1] + "'))]/following-sibling::*[1]"));}
                case "region" -> {return new Locator("TARGET_DESTINATION_REGION", AppiumBy.iOSClassChain("**/XCUIElementTypeOther[`name == 'populer_destination_chip'`]/**/XCUIElementTypeStaticText[`label == '" + keywordSplit[0] + "' OR label == '" + keywordSplit[1] + "'`]"));}
                case "city" -> {return new Locator("TARGET_DESTINATION_CITY", AppiumBy.xpath("//XCUIElementTypeTable/XCUIElementTypeCell/XCUIElementTypeStaticText[contains(@label,'Kota') or contains(@label,'City')]/following-sibling::XCUIElementTypeStaticText[@label='" + keywordSplit[0] + "' or @label='" + keywordSplit[1] + "']"));}
            }
        } else {
            switch (type) {
                case "country" -> {return new Locator("TARGET_DESTINATION_COUNTRY", AppiumBy.xpath("//XCUIElementTypeStaticText[@name = 'ttd_country_name' and contains(@label,'" + keyword + "')]/following-sibling::*[1]"));}
                case "region" -> {return new Locator("TARGET_DESTINATION_REGION", AppiumBy.iOSClassChain("**/XCUIElementTypeOther[`name == 'populer_destination_chip'`]/**/XCUIElementTypeStaticText[`label == '" + keyword + "'`]"));}
                case "city" -> {return new Locator("TARGET_DESTINATION_CITY", AppiumBy.xpath("//XCUIElementTypeTable/XCUIElementTypeCell/XCUIElementTypeStaticText[contains(@label,'Kota') or contains(@label,'City')]/following-sibling::XCUIElementTypeStaticText[@label='" + keyword + "']"));}
            }
        }

        logger.fail("Test data seach_type is not 'country', 'city', or 'region' please check the test data");
        throw new IllegalArgumentException("Test Data '"+ type +"' is not valid");
    }

    @Override
    public void inputSearchKeyword(String keyword) {
        String searchInput = "";
        if(keyword.contains("/")) {
            searchInput += keyword.split("/")[0];
        } else {
            searchInput += keyword;
        }

        driverTools.waitForElementToPresence(SEARCH_BAR);
        driverTools.clickElementByCoordinate(SEARCH_BAR);
        driverTools.sendKeys(SEARCH_BAR, searchInput);
    }

    @Override
    public String pickAndClickDestinationCard() {
        String clickedDestinationTitleCard = "";
        boolean isElementDisplayed = false;

        for (int i = 0; i < 20; i++) {
            int randomIndex = generateRandomInt(1,6);
            final Locator POPULAR_DESTINATION_NAME = getPopularDestinationLocator(randomIndex);

            if (driverTools.isElementDisplayed(POPULAR_DESTINATION_NAME)) {
                clickedDestinationTitleCard += driverTools.element(POPULAR_DESTINATION_NAME).orElseThrow().getAttribute("label");
                logger.log("Found '"+ clickedDestinationTitleCard +"' destination card");
                isElementDisplayed = true;
                driverTools.click(POPULAR_DESTINATION_NAME);
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
        int labelCount = 0;
        String clickedDestinationTitleLabel = "";

        int doFilterLimit = generateRandomInt(1,3);
        logger.log("Scrolling up for '"+ doFilterLimit +"' times");
        for (int i = 0;i < doFilterLimit; i++) {
            driverTools.swipeUp();
        }

        Optional<WebElement> targetLabelElement = driverTools.elements(TARGET_DESTINATION_LABEL).orElseThrow().stream()
                .filter(element -> (
                        element.isDisplayed())
                ).findAny();

        clickedDestinationTitleLabel = targetLabelElement.orElseThrow().getAttribute("label");
        driverTools.click(targetLabelElement.orElseThrow());

        return clickedDestinationTitleLabel;
    }

    private Locator getTargetDestinationLocator(String keyword, String type) {
        if(keyword.contains("/")) {
            String[] keywordSplit = keyword.split("/");
            switch (type) {
                case "country" -> {return new Locator("TARGET_DESTINATION_COUNTRY", AppiumBy.xpath("//XCUIElementTypeStaticText[@name = 'ttd_country_name' and (contains(@label,'" + keywordSplit[0] + "') or contains(@label,'" + keywordSplit[1] + "'))]"));}
                case "region" -> {return new Locator("TARGET_DESTINATION_REGION", AppiumBy.iOSClassChain("**/XCUIElementTypeOther[`name == 'populer_destination_chip'`]/**/XCUIElementTypeStaticText[`label == '" + keywordSplit[0] + "' OR label == '" + keywordSplit[1] + "'`]"));}
                case "city" -> {return new Locator("TARGET_DESTINATION_CITY", AppiumBy.xpath("//XCUIElementTypeTable/XCUIElementTypeCell/XCUIElementTypeStaticText[contains(@label,'Kota') or contains(@label,'City')]/following-sibling::XCUIElementTypeStaticText[@label='" + keywordSplit[0] + "' or @label='" + keywordSplit[1] + "']"));}
            }
        } else {
            switch (type) {
                case "country" -> {return new Locator("TARGET_DESTINATION_COUNTRY", AppiumBy.xpath("//XCUIElementTypeStaticText[@name = 'ttd_country_name' and contains(@label,'" + keyword + "')]"));}
                case "region" -> {return new Locator("TARGET_DESTINATION_REGION", AppiumBy.iOSClassChain("**/XCUIElementTypeOther[`name == 'populer_destination_chip'`]/**/XCUIElementTypeStaticText[`label == '" + keyword + "'`]"));}
                case "city" -> {return new Locator("TARGET_DESTINATION_CITY", AppiumBy.xpath("//XCUIElementTypeTable/XCUIElementTypeCell/XCUIElementTypeStaticText[contains(@label,'Kota') or contains(@label,'City')]/following-sibling::XCUIElementTypeStaticText[@label='" + keyword + "']"));}
            }
        }

        logger.fail("Test data seach_type is not 'country', 'city', or 'region' please check the test data");
        throw new IllegalArgumentException("Test Data '"+ type +"' is not valid");
    }

    private Locator getPopularDestinationLocator(int index) {
        return new Locator("POPULAR_DESTINATION_NAME", AppiumBy.iOSClassChain("**/*[`name == 'ttd_popular_destination_card_title'`]["+ index +"]"));
    }

    @Override
    public boolean isNearbyLocationSectionVisible() {
        return driverTools.waitForElementToAppear(NEAREST_LOCATION_CHIP_TEXT, 20).isPresent();
    }

    @Override
    public int getNearbyDestinationChips() {
        driverTools.waitForElementToAppear(NEAREST_LOCATION_CHIP_TEXT);
        List<WebElement> nearestLocationChipElement = driverTools.elements(NEAREST_LOCATION_CHIP_TEXT).orElseThrow();
        return nearestLocationChipElement.size();
    }

    @Override
    public String chooseLocationChips(int index) {
        String chosenLocationChip;
        int indexInt = index - 1;

        if(indexInt < 0) {
            logger.fail("Index must be greater than 0");
            throw new IllegalArgumentException("Index must be greater than 0");
        }

        int totalChips = getNearbyDestinationChips();
        if(indexInt >= totalChips) {
            logger.fail("Index '"+ index +"' is out of bounds, total nearby location chips is '"+ totalChips +"'");
            throw new IndexOutOfBoundsException("Index '"+ index +"' is out of bounds, total nearby location chips is '"+ totalChips +"'");
        }

        List<WebElement> nearbyDestinationChips = driverTools.elements(NEAREST_LOCATION_CHIP_TEXT).orElseThrow();
        chosenLocationChip = driverTools.getValueAttribute(nearbyDestinationChips.get(indexInt), "label");
        driverTools.click(nearbyDestinationChips.get(indexInt));

        return chosenLocationChip;
    }

    @Override
    public List<String> getNearbyDestinationChipText() {
        driverTools.waitForElementToAppear(NEAREST_LOCATION_CHIP_TEXT);
        List<WebElement> nearestLocationChipElement = driverTools.elements(NEAREST_LOCATION_CHIP_TEXT).orElseThrow();
        List<String> result = new ArrayList<>();

        for(WebElement e : nearestLocationChipElement) {
            String text = driverTools.getValueAttribute(e,"label");
            if(text.contains("locations") || text.contains("lokasi")) {
                continue;
            }
            result.add(text);
        }

        return result;
    }

    @Override
    public String clickNearbyDestinationChips(String index) {
        String targetNearbyDestinationChip = index;
        if (targetNearbyDestinationChip.equalsIgnoreCase("all locations") || targetNearbyDestinationChip.equalsIgnoreCase("semua lokasi")) {
            driverTools.click(ALL_LOCATION_CHIPS_BUTTON);
        } else {
            int indexInt = 0;
            List<WebElement> nearbyDestinationChips = driverTools.elements(NEAREST_LOCATION_CHIP_TEXT).orElseThrow();

            if (index.equalsIgnoreCase("random")) {
                indexInt += generateRandomInt(0, nearbyDestinationChips.size() - 1);
            } else {
                indexInt += Integer.parseInt(index);
            }

            targetNearbyDestinationChip = driverTools.getValueAttribute(nearbyDestinationChips.get(indexInt), "label");
            driverTools.click(nearbyDestinationChips.get(indexInt));
        }
        return targetNearbyDestinationChip;
    }
}
