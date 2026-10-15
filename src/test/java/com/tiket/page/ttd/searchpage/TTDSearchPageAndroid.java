package com.tiket.page.ttd.searchpage;

import com.tiket.appium.Swipe;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.*;

import static com.tiket.core.CoreFunctions.generateRandomInt;
import static org.hamcrest.core.Is.is;

public class TTDSearchPageAndroid<T extends WebDriver> extends TTDSearchPage<T> {

    public static final Locator BACK_BUTTON = new Locator("BACK_BUTTON", AppiumBy.id("tds_icon_navigation_toolbar"));
    public static final Locator SEARCH_BAR_SEARCHPAGE = new Locator("SEARCH_BAR_SEARCHPAGE", AppiumBy.id("tds_search_et"));
    public static final Locator CLEAR_SEARCH_INPUT = new Locator("CLEAR_SEARCH_INPUT", AppiumBy.id("tds_search_clear"));
    public static final Locator TOP_KEYWORD_TITLE_SEARCHPAGE = new Locator("TOP_KEYWORD_TITLE_SEARCHPAGE", AppiumBy.xpath("//*[contains(@resource-id,'ly_header_top_keyword')] | //*[contains(@resource-id,'search_suggestion_history_view')]/*[contains(@resource-id,'tv_heading') and contains(@text,'Keywords') or contains(@text,'Kata Kunci')]"));
    public static final Locator TOP_KEYWORD_TEXT = new Locator("TOP_KEYWORD_TEXT", AppiumBy.xpath("//*[contains(@resource-id,'tv_name_top_keyword')] | //*[contains(@resource-id,'search_suggestion_history_view') and ./*[contains(@resource-id,'tv_heading') and contains(@text,'Keywords') or contains(@text,'Kata Kunci')]]//*[contains(@resource-id,'tds_chips_text')]"));
    public static final Locator RESULT_CONTENT = new Locator("RESULT_CONTENT", AppiumBy.id("suggestion_container"));
    public static final Locator NEARBY_ACTIVITY_BUTTTON = new Locator("NEARBY_ACTIVITY_BUTTTO", AppiumBy.id("cl_nearme_container"));
    public static final Locator POPULAR_DESTINATION_HEADING = new Locator("POPULAR_DESTINATION_HEADING", AppiumBy.xpath("//*[contains(@resource-id,'tv_heading') and contains(@text,'Destinasi Populer')]"));
    public static final Locator POPULAR_DESTINATION_CAROUSEL = new Locator("POPULAR_DESTINATION_CAROUSEL", AppiumBy.id("rv_popular_destination"));
    public static final Locator POPULAR_DESTINATION_NAME = new Locator("POPULAR_DESTINATION_NAME", AppiumBy.xpath("//*[contains(@resource-id,'rv_popular_destination')]//*[contains(@resource-id,'tv_name')]"));
    public static final Locator POPULAR_ACTIVITIES_HEADING = new Locator("POPULAR_ACTIVITIES_HEADING", AppiumBy.xpath("//*[contains(@resource-id,'tv_heading') and contains(@text,'Aktivitas Populer')]"));
    public static final Locator POPULAR_ACTIVITIES_NAME = new Locator("POPULAR_ACTIVITIES_NAME", AppiumBy.id("tv_item_name"));
    public static final Locator SEE_ALL_ACTIVITIES = new Locator("SEE_ALL_ACTIVITIES", AppiumBy.xpath("(//*[contains(@resource-id,'btn_cta')])[1]")); // TODO: Need Unique Locator - TTD - Android - Akbar
    public static final Locator POPULAR_PARTNER_HEADING = new Locator("POPULAR_PARTNER_HEADING", AppiumBy.xpath("//*[contains(@resource-id,'tv_heading') and contains(@text,'Partner Populer') or contains(@text,'Popular Partners')]"));
    public static final Locator RECENTLY_SEARCHED_HEADING = new Locator("RECENTLY_SEARCHED_HEADING", AppiumBy.xpath("//*[contains(@resource-id,'tv_heading') and (contains(@text,'Recently Searched') or contains(@text,'Pencarian Terakhir'))] | //*[contains(@resource-id,'search_suggestion_history_view')]/*[contains(@resource-id,'tv_heading') and contains(@text,'Recently Search') or contains(@text,'Pencarian Terakhir')]"));
    public static final Locator RECOMMENDED_ACTIVITY_SECTION = new Locator("RECOMMENDED_ACTIVITY_SECTION", AppiumBy.xpath("//*[contains(@resource-id,'tv_heading') and (contains(@text,'Rekomendasi Aktivitas') or contains(@text,'Recommended Activities'))]"));
    public static final Locator RECOMMENDED_DESTINATION_SECTION = new Locator("RECOMMENDED_DESTINATION_SECTION", AppiumBy.xpath("//*[contains(@resource-id,'tv_heading') and (contains(@text,'Rekomendasi Lokasi') or contains(@text,'Recommended Locations'))]"));
    public static final Locator SEE_ALL_POPULAR_ACTIVITIES = new Locator("SEE_ALL_POPULAR_ACTIVITIES", AppiumBy.xpath("//*[contains(@resource-id,'tv_heading') and contains(@text,'Aktivitas Populer') or contains(@text,'Popular Activities')]//following-sibling::*[contains(@resource-id,'btn_cta')]"));
    public static final Locator CLEAR_ALL = new Locator("CLEAR_ALL", AppiumBy.xpath("//*[contains(@resource-id,'btn_cta') and contains(@text,'Clear') or contains(@text,'Hapus')]"));
    public static final Locator SEE_ALL_POPULAR_DESTINATION= new Locator("SEE_ALL_POPULAR_DESTINATION", AppiumBy.xpath("//*[contains(@resource-id,'tv_heading') and contains(@text,'Popular Destinations') or contains(@text,'Destinasi Populer')]//following-sibling::*[contains(@resource-id,'btn_cta')]"));
    public static final Locator REFRESH_TOP_KEYWORD = new Locator("REFRESH_TOP_KEYWORD", AppiumBy.xpath("//*[contains(@resource-id,'tv_refresh_top_keyword')]//*[contains(@resource-id,'tds_button_view')] | //*[contains(@resource-id,'search_suggestion_history_view') and ./*[contains(@resource-id,'tv_heading') and contains(@text,'Keywords') or contains(@text,'Kata Kunci')]]/*[contains(@resource-id,'btn_cta')]"));
    public static final Locator RECOMMENDED_PARTNER_SECTION = new Locator("RECOMMENDED_PARTNER_SECTION", AppiumBy.xpath("//*[contains(@resource-id,'tv_heading') and (contains(@text,'Rekomendasi Partner') or contains(@text,'Recommended Partners'))]"));
    private static final Locator ATTRACTION_TAB = new Locator("ATTRACTION_TAB", AppiumBy.xpath("//*[contains(@resource-id,'tds_title_tab_layout') and (contains(@text,'Atraksi Populer') or contains(@text,'Popular Attractions'))]"));
    private static final Locator EVENT_TAB = new Locator("EVENT_TAB", AppiumBy.xpath("//*[contains(@resource-id,'tds_title_tab_layout') and (contains(@text,'Event Populer') or contains(@text,'Popular Events'))]"));
    private static final Locator DESTINATION_TAB = new Locator("DESTINATION_TAB", AppiumBy.xpath("//*[contains(@resource-id,'tds_title_tab_layout') and (contains(@text,'Destinasi Populer') or contains(@text,'Popular Destinations'))]"));
    private static final Locator NEW_ACTIVITY_TAB = new Locator("NEW_ACTIVITY_TAB", AppiumBy.xpath("//*[contains(@resource-id,'tds_title_tab_layout') and (contains(@text,'Aktivitas Baru') or contains(@text,'New Activities'))]"));
    private static final Locator TAB_CONTAINER = new Locator("TAB_CONTAINER", AppiumBy.id("tab_layout_tab"));
    private static final Locator TODO_RANK = new Locator("TODO_RANK", AppiumBy.id("tv_rank"));
    private static final Locator TODO_NAME = new Locator("TODO_NAME", AppiumBy.xpath("//*[contains(@resource-id,'rv_activities')]//*[contains(@resource-id,'tv_title')]"));
    private static final Locator TODO_LIST_CONTAINER = new Locator("TODO_LIST_CONTAINER", AppiumBy.id("rv_activities"));
    private static final Locator SEE_ALL_BUTTON = new Locator("SEE_ALL_BUTTON", AppiumBy.id("btn_see_all"));
    private static final Locator SEE_ALL_SEARCH_RESULT_BUTTON = new Locator("SEE_ALL_SEARCH_RESULT_BUTTON", AppiumBy.id("tds_button_view"));
    private static final Locator PRODUCT_TITLE = new Locator("PRODUCT_TITLE", AppiumBy.xpath("//*[contains(@resource-id,'rv_activities')]//*[contains(@resource-id,'tv_title')]"));
    private static final Locator LOCATION_BUTTON = new Locator("LOCATION_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'container_destination_selector')]//*[contains(@resource-id,'tv_location')]"));
    private static final Locator AREA_LOCATION_CHIP = new Locator("AREA_LOCATION_CHIP", AppiumBy.xpath("((//*[contains(@resource-id,'tv_title')]/following-sibling::*[contains(@resource-id,'cg_favorite')])[1]//*[contains(@resource-id,'tds_chips_text')])[1]"));
    private static final Locator NEAR_LOCATION_CHIPS = new Locator("NEAR_LOCATION_CHIPS", AppiumBy.xpath("(//*[contains(@resource-id,'tv_title')]/following-sibling::*[contains(@resource-id,'cg_favorite')])[1]//*[contains(@resource-id,'tds_chips_text')]"));

    public TTDSearchPageAndroid(Session<T> session) {
        super(session);
    }

    @Override
    public void selectSection(String section) {
        driverTools.waitForElementToAppear(TODO_NAME,3);
        WebElement sectionElement = null;
        switch (section.toLowerCase()) {
            case "attractions":
            case "atraksi":
                sectionElement = driverTools.swipeToElement(ATTRACTION_TAB, Swipe.UP, 5).orElseThrow();
                break;
            case "event":
            case "events":
                sectionElement = driverTools.swipeToElement(EVENT_TAB, Swipe.UP, 5).orElseThrow();
                break;
            case "destinations":
            case "destinasi":
                sectionElement = driverTools.swipeToElement(DESTINATION_TAB, Swipe.UP, 5).orElseThrow();
                break;
            case "aktivitas baru":
            case "new activities":
                driverTools.swipeToElement(ATTRACTION_TAB, Swipe.UP, 5);
                sectionElement = driverTools.swipeToElement(NEW_ACTIVITY_TAB, TAB_CONTAINER, Swipe.LEFT, 5).orElseThrow();
                break;
        }
        if (sectionElement == null) {
            logger.log("There is no section for: " + section);
        } else {
            driverTools.click(sectionElement);
        }
    }

    @Override
    public boolean checkToDoRank(int expectedTotalRank) {

        Set<Integer> verifiedRanks = new HashSet<>();
        int remainingSwipes = 15;

        while (verifiedRanks.size() < expectedTotalRank && remainingSwipes-- > 0) {

            List<WebElement> visibleRanks = driverTools.elements(TODO_RANK).orElseThrow();

            for (WebElement element : visibleRanks) {
                String rankText = element.getText().trim();
                int rank;
                try {
                    rank = Integer.parseInt(rankText);
                } catch (NumberFormatException ignored) {
                    continue;
                }

                if (rank <= expectedTotalRank && verifiedRanks.add(rank)) {
                    Assertion.assertThat(
                            "Product should have rank " + rank,
                            String.valueOf(rank),
                            is(rankText)
                    );
                }
            }

            if (verifiedRanks.size() < expectedTotalRank) {
                driverTools.swipeUp();
            }
        }

        Assertion.assertThat(
                "Not all ranks 1-" + expectedTotalRank + " were found",
                verifiedRanks.size(),
                is(expectedTotalRank)
        );

        return true;
    }
    @Override
    public int getTotalItems() {
        driverTools.swipeToElement(SEE_ALL_BUTTON, Swipe.UP, 3).orElseThrow();
        List<WebElement> toDoName = driverTools.elements(TODO_NAME).orElseThrow();
        return toDoName.size();
    }

    @Override
    public String clickItemOnList(String list) {
        String productName;
        driverTools.waitForElementToAppear(TODO_NAME,3);
        List<WebElement> toDoName = driverTools.elements(TODO_NAME).orElseThrow();
        int totalProduct = toDoName.size();
        logger.log("total products -> " + totalProduct);
        int index = list.equals("random") ? generateRandomInt(1, totalProduct-1) : Integer.parseInt(list);
        logger.log("index -> " + index);
        toDoName = driverTools.elements(TODO_NAME).orElseThrow();
        productName = driverTools.getText(toDoName.get(index));
        driverTools.click(toDoName.get(index));
        return productName;
    }

    @Override
    public void clickSeeAllButton() {
        driverTools.swipeToElement(SEE_ALL_BUTTON, Swipe.UP, 5).ifPresent(driverTools::click);
    }

    @Override
    public boolean isSearchPageValid() {
        boolean topKeywordElementDisplayed = driverTools.isElementDisplayed(TOP_KEYWORD_TITLE_SEARCHPAGE);
        return topKeywordElementDisplayed;
    }

    @Override
    public boolean isTopKeywordDisplayed() {
        boolean isTopKeywordDisplayed = driverTools.waitForElementToAppear(TOP_KEYWORD_TEXT, 5).isPresent();
        return isTopKeywordDisplayed;
    }

    @Override
    public boolean isPopularActivitiesDisplayed() {
        boolean isPopularActivitiesHeadingDisplayed = driverTools.waitForElementToAppear(POPULAR_ACTIVITIES_HEADING, 5).isPresent();
        boolean isPopularActivitiesNameDisplayed = driverTools.waitForElementToAppear(POPULAR_ACTIVITIES_NAME, 5).isPresent();

        if (!isPopularActivitiesHeadingDisplayed) {
            logger.warn("Popular Heading is not displayed");
        }

        if (!isPopularActivitiesNameDisplayed) {
            logger.warn("Popular Activities name is not displayed");
        }

        return isPopularActivitiesHeadingDisplayed && isPopularActivitiesNameDisplayed;
    }

    @Override
    public boolean isPopularDestinationDisplayed() {
        driverTools.swipeToElement(POPULAR_DESTINATION_CAROUSEL, Swipe.UP, 3);
        boolean isPopularDestinationHeadingDisplayed = driverTools.waitForElementToAppear(POPULAR_DESTINATION_HEADING, 5).isPresent();
        boolean isPopularDestinationCarrouselDisplayed = driverTools.waitForElementToAppear(POPULAR_DESTINATION_CAROUSEL, 5).isPresent();
        return isPopularDestinationHeadingDisplayed && isPopularDestinationCarrouselDisplayed;
    }

    @Override
    public boolean isPopularPartnersDisplayed() {
        driverTools.swipeToElement(POPULAR_PARTNER_HEADING, Swipe.UP, 3);
        boolean isPopularPartnerHeadingDisplayed = driverTools.isElementDisplayed(POPULAR_PARTNER_HEADING);
        return isPopularPartnerHeadingDisplayed;
    }

    @Override
    public boolean isRecentlySearchedValid(String clickedResult) {
        final Locator RECENTLY_SEARCHED_TARGET = getRecentlySearchedTargetLocator(clickedResult);

        boolean isRecentlySearchedHeadingDisplayed = driverTools.waitForElementToAppear(RECENTLY_SEARCHED_HEADING,5).orElseThrow().isDisplayed();
        boolean isRecentlySearchedResultDisplayed = driverTools.waitForElementToAppear(RECENTLY_SEARCHED_TARGET,5).orElseThrow().isDisplayed();

        if (!isRecentlySearchedHeadingDisplayed) {
            logger.warn("Recently Searched Heading is not displayed");
        }

        if (!isRecentlySearchedResultDisplayed) {
            logger.warn("Recently Searched Result is not displayed");
        }

        return isRecentlySearchedHeadingDisplayed && isRecentlySearchedResultDisplayed;
    }

    @Override
    public void inputSearchKeyword(String keyword) {
        driverTools.waitForElementToAppear(POPULAR_ACTIVITIES_NAME);

        String searchInput = "";
        if(keyword.contains("/")) {
            searchInput += keyword.split("/")[0];
        } else {
            searchInput += keyword;
        }
        driverTools.sendKeys(SEARCH_BAR_SEARCHPAGE, searchInput);
    }

    @Override
    public void clickAndCheckAllTopKeyword() {
        driverTools.waitForElementToAppear(TOP_KEYWORD_TEXT,5);
        List<WebElement> listOfTopKeywordElements = driverTools.elements(TOP_KEYWORD_TEXT).get();
        int topKeywordCount = listOfTopKeywordElements.size();

        for (int i = 1 ; i < topKeywordCount+1 ; i++) {
            WebElement topKeywordElement = driverTools.waitForElementToAppear(getTopKeywordLocatorByIndex(i)).orElseThrow();
            String topKeywordText = driverTools.getText(topKeywordElement);
            logger.log("Checking top keyword '"+ topKeywordText +"'");

            topKeywordElement.click();
            if (!driverTools.isElementDisplayed(RESULT_CONTENT)) {
                logger.log("Top Keyword '"+ topKeywordText +"' does not have any result");
            }

            String searchBarText = driverTools.getText(SEARCH_BAR_SEARCHPAGE).get();
            if (!searchBarText.equalsIgnoreCase(topKeywordText)) {
                logger.warn("Top Keyword '"+ topKeywordText +"' is having different text with search bar, which is '"+ searchBarText +"'");
            }

            driverTools.click(CLEAR_SEARCH_INPUT);
        }
    }

    @Override
    public void clickSeeAllActivities() {
        driverTools.click(SEE_ALL_ACTIVITIES);
    }

    @Override
    public String pickAndClickPopularDestination() {
        driverTools.swipeToElement(POPULAR_DESTINATION_NAME, Swipe.UP, 3);

        int randomNumber = generateRandomInt(1,3);

        Locator DESTINATION_NAME = getDestinationNameLocator(randomNumber);
        Locator DESTINATION_IMAGE = getDestinationImageLocator(randomNumber);

        WebElement destinationNameElement = driverTools.waitForElementToAppear(DESTINATION_NAME,5).orElseThrow();
        String destinationNameText = driverTools.getText(destinationNameElement);

        driverTools.click(DESTINATION_IMAGE);

        return destinationNameText;
    }

    @Override
    public void clickNearbyActivity() {
        driverTools.click(NEARBY_ACTIVITY_BUTTTON);
    }

    @Override
    public void clickClearSearch() {
        driverTools.waitForElementToAppear(CLEAR_SEARCH_INPUT).ifPresent(driverTools::click);
    }

    @Override
    public Map<String, String> pickAndClickPopularActivity() {
        Map<String, String> clickedPopularActivities = new HashMap<>();

        int randomNumber = generateRandomInt(1,2);

        Locator ACTIVITY_NAME = getActivityNameLocator(randomNumber);
        Locator ACTIVITY_TYPE = getActivityTypeLocator(randomNumber);
        Locator ACTIVITY_LOCATION = getActivityLocationLocator(randomNumber);

        WebElement activityNameElement = driverTools.waitForElementToAppear(ACTIVITY_NAME,20).orElseThrow();
        WebElement activityTypeElement = driverTools.waitForElementToAppear(ACTIVITY_TYPE,5).orElseThrow();
        WebElement activityLocationElement = driverTools.waitForElementToAppear(ACTIVITY_LOCATION,5).orElseThrow();

        clickedPopularActivities.put("name", driverTools.getText(activityNameElement));
        clickedPopularActivities.put("type", driverTools.getText(activityTypeElement));
        clickedPopularActivities.put("location", driverTools.getText(activityLocationElement));

        driverTools.click(ACTIVITY_NAME);

        return clickedPopularActivities;
    }

    @Override
    public String pickAndClickPopularPartner() {
        int randomNumber = generateRandomInt(1,3);
        Locator PARTNER_NAME = getPartnerNameLocator(randomNumber);
        WebElement partnerNameElement =  driverTools.swipeToElement(PARTNER_NAME,Swipe.UP,3).orElseThrow();
        driverTools.swipeUp();

        String partnerNameText = partnerNameElement.getText();
        driverTools.click(partnerNameElement);
        return partnerNameText;
    }

    @Override
    public void selectRecommendedActivity(String keyword) {
        Locator ACTIVITY_NAME = getRecommendedActivity(keyword);
        if (!driverTools.isElementDisplayed(ACTIVITY_NAME))
            ACTIVITY_NAME = getProductTitleLocator(keyword);
        driverTools.swipeToElement(ACTIVITY_NAME, Swipe.UP, 2);
        driverTools.waitForElementToAppear(ACTIVITY_NAME);
        driverTools.click(ACTIVITY_NAME);
    }

    @Override
    public void selectRecommendedActivitySRP(String keyword) {
        driverTools.waitForElementToAppear(RECOMMENDED_ACTIVITY_SECTION);
        Locator ACTIVITY_NAME = getRecommendedActivitySRP(keyword);
        driverTools.swipeToElement(ACTIVITY_NAME, Swipe.UP, 2);
        driverTools.waitForElementToAppear(ACTIVITY_NAME);
        driverTools.click(ACTIVITY_NAME);
    }

    private Locator getRecommendedActivitySRP(String keyword) {
        return new Locator("ACTIVITY_NAME", AppiumBy.xpath("//*[contains(@resource-id, 'tv_title') and @text='" + keyword + "']"));
    }

    private Locator getTopKeywordLocatorByIndex(int index) {
        return new Locator("TOP_KEYWORD_INDEX", AppiumBy.xpath("(//*[contains(@resource-id,'tv_name_top_keyword')])["+ index +"] | (//*[contains(@resource-id,'search_suggestion_history_view') and ./*[contains(@resource-id,'tv_heading') and contains(@text,'Keywords') or contains(@text,'Kata Kunci')]]//*[contains(@resource-id,'tds_chips_text')])["+ index +"]"));
    }

    private Locator getDestinationNameLocator(int index) {
        return new Locator("DESTINATION_NAME", AppiumBy.xpath("(//*[contains(@resource-id,'rv_popular_destination')]//*[contains(@resource-id,'tv_name')])["+ index +"]"));
    }

    private Locator getDestinationImageLocator(int index) {
        return new Locator("DESTINATION_IMAGE", AppiumBy.xpath("(//*[contains(@resource-id,'rv_popular_destination')]//*[contains(@resource-id,'iv_favoritedestination')])["+ index +"]"));
    }

    private Locator getActivityNameLocator(int index) {
        return new Locator("ACTIVITY_NAME", AppiumBy.xpath("((//*[contains(@text,'Aktivitas Populer') or contains(@text,'Popular Activities')]/parent::*/parent::*/parent::*) | (//*[contains(@text,'Aktivitas Populer') or contains(@text,'Popular Activities')]/parent::*/parent::*/parent::*/following-sibling::*[contains(@resource-id,'suggestion_container')]))["+ index +"]//*[contains(@resource-id,'tv_item_name')]"));
    }

    private Locator getActivityTypeLocator(int index) {
        return new Locator("ACTIVITY_TYPE", AppiumBy.xpath("((//*[contains(@text,'Aktivitas Populer') or contains(@text,'Popular Activities')]/parent::*/parent::*/parent::*) | (//*[contains(@text,'Aktivitas Populer') or contains(@text,'Popular Activities')]/parent::*/parent::*/parent::*/following-sibling::*[contains(@resource-id,'suggestion_container')]))["+ index +"]//*[contains(@resource-id,'tv_category')]"));
    }

    private Locator getActivityLocationLocator(int index) {
        return new Locator("ACTIVITY_LOCATION", AppiumBy.xpath("((//*[contains(@text,'Aktivitas Populer') or contains(@text,'Popular Activities')]/parent::*/parent::*/parent::*) | (//*[contains(@text,'Aktivitas Populer') or contains(@text,'Popular Activities')]/parent::*/parent::*/parent::*/following-sibling::*[contains(@resource-id,'suggestion_container')]))["+ index +"]//*[contains(@resource-id,'tv_location')]"));
    }

    private Locator getPartnerNameLocator(int index) {
        return new Locator("PARTNER_NAME", AppiumBy.xpath("((//*[contains(@text,'Partner Populer') or contains(@text,'Popular Partners')]/parent::*/parent::*/parent::*) | (//*[contains(@text,'Partner Populer') or contains(@text,'Popular Partners')]/parent::*/parent::*/parent::*/following-sibling::*[contains(@resource-id,'suggestion_container')]))["+ index +"]//*[contains(@resource-id,'tv_item_name')]"));
    }

    private Locator getRecentlySearchedTargetLocator(String clickedResult) {
        if(clickedResult.contains("/")) {
            String keywordId = clickedResult.split("/")[0];
            String keywordEng = clickedResult.split("/")[1];
            return new Locator("RECENTLY_SEARCHED_TARGET", AppiumBy.xpath("//*[contains(@resource-id,'tv_suggestion_name') and @text='"+ keywordId +"' or @text='"+ keywordEng +"'] | //*[contains(@resource-id,'search_suggestion_history_view') and ./*[contains(@resource-id,'tv_heading') and contains(@text,'Recently Search') or contains(@text,'Pencarian Terakhir')]]//*[contains(@resource-id,'tds_chips_text') and @text='"+ keywordId +"' or @text='"+ keywordEng +"']"));
        } else {
            return new Locator("RECENTLY_SEARCHED_TARGET", AppiumBy.xpath("//*[contains(@resource-id,'tv_suggestion_name') and @text='"+ clickedResult +"'] | //*[contains(@resource-id,'search_suggestion_history_view') and ./*[contains(@resource-id,'tv_heading') and contains(@text,'Recently Search') or contains(@text,'Pencarian Terakhir')]]//*[contains(@resource-id,'tds_chips_text') and contains(@text,'"+ clickedResult +"')]"));
        }
    }

    private Locator getRecommendedActivity(String keyword) {
        if(keyword.contains("/")) {
            String keywordId = keyword.split("/")[0];
            String keywordEng = keyword.split("/")[1];
            return new Locator("ACTIVITY_NAME", AppiumBy.xpath("//*[contains(@resource-id,'tv_heading') and (contains(@text,'Rekomendasi Aktivitas') or contains(@text,'Recommended Activities'))]//following::*[contains(@resource-id,'tv_category')]//parent::*//preceding-sibling::*[contains(@resource-id,'tv_title') or contains(@resource-id,'tv_item_name') and (contains(@text,'" + keywordId + "') or contains(@text,'" + keywordEng + "'))]"));

        } else {
            return new Locator("ACTIVITY_NAME", AppiumBy.xpath("//*[contains(@resource-id,'tv_heading') and (contains(@text,'Rekomendasi Aktivitas') or contains(@text,'Recommended Activities'))]//following::*[contains(@resource-id,'tv_category')]//parent::*//preceding-sibling::*[contains(@resource-id,'tv_title') or contains(@resource-id,'tv_item_name') and contains(@text,'"+ keyword +"')]"));
        }
    }

    private Locator getProductTitleLocator(String keyword) {
        if (keyword.contains("/")) {
            String keywordId = keyword.split("/")[0];
            String keywordEng = keyword.split("/")[1];
            return new Locator("PRODUCT_TITLE", AppiumBy.xpath("//*[(contains(@resource-id,'tv_title') or contains(@resource-id,'tv_item_name')) and (contains(@text,'" + keywordId + "') or contains(@text,'" + keywordEng + "'))]"));
        } else {
            return new Locator("PRODUCT_TITLE", AppiumBy.xpath("//*[(contains(@resource-id,'tv_title') or contains(@resource-id,'tv_item_name')) and contains(@text,'" + keyword + "')]"));
        }
    }

    @Override
    public void clickClearAll(){
        driverTools.waitForElementToAppear(CLEAR_ALL);
        driverTools.click(CLEAR_ALL);
    }

    @Override
    public boolean isSearchCleared() {
        boolean isClearAllDisplayed = driverTools.isElementDisplayed(CLEAR_ALL);
        return !isClearAllDisplayed;
    }

    @Override
    public void clickPopularActivitySeeAll() {
        driverTools.swipeToElement(SEE_ALL_POPULAR_ACTIVITIES, Swipe.UP, 4);
        driverTools.click(SEE_ALL_POPULAR_ACTIVITIES);
    }

    @Override
    public void clickPopularDestinationSeeAll() {
        driverTools.swipeToElement(SEE_ALL_POPULAR_DESTINATION, Swipe.UP, 4);
        driverTools.click(SEE_ALL_POPULAR_DESTINATION);
    }

    @Override
    public String getSearchFieldText() {
        return driverTools.getText(SEARCH_BAR_SEARCHPAGE).orElseThrow();
    }

    @Override
    public void clickRefreshTopKeyword() {
        driverTools.click(REFRESH_TOP_KEYWORD);
    }

    @Override
    public List<String> getTopKeywordList(){
        driverTools.waitForElementToAppear(TOP_KEYWORD_TEXT,5);
        List<WebElement> listOfTopKeywordElements = driverTools.elements(TOP_KEYWORD_TEXT).orElseThrow();
        List<String> listOfTopKeyword = new ArrayList<>();
        for(int i = 0; i < listOfTopKeywordElements.size(); i++) {
            WebElement topKeywordElement = driverTools.element(getTopKeywordLocatorByIndex(i+1)).orElseThrow();
            String topKeywordText = driverTools.getText(topKeywordElement);
            listOfTopKeyword.add(topKeywordText);
        }
        logger.log("List->"+listOfTopKeyword);
        return listOfTopKeyword;
    }

    @Override
    public String clickRandomKeyword() {
        driverTools.waitForElementToAppear(TOP_KEYWORD_TITLE_SEARCHPAGE);
        List<WebElement> listOfTopKeywordElements = driverTools.elements(TOP_KEYWORD_TEXT).orElseThrow();
        int randomIndex = generateRandomInt(0, listOfTopKeywordElements.size()-1);
        String text = driverTools.getText(listOfTopKeywordElements.get(randomIndex));
        driverTools.click(listOfTopKeywordElements.get(randomIndex));
        return text;
    }

    @Override
    public void clickBack() {
        driverTools.waitForElementToAppear(BACK_BUTTON);
        if(driverTools.isElementDisplayed(BACK_BUTTON)) {
            driverTools.click(BACK_BUTTON);
        }
    }

    private Locator getPartnerPageLocator(String keyword){
        return new Locator("PARTNER_PAGE", AppiumBy.xpath("(//*[contains(@resource-id,'tv_item_name') and contains(@text,'"+keyword+"')])[last()]"));
    }

    @Override
    public void findAndClickPartnerPage(String keyword){
        driverTools.hideKeyboard(1);
        driverTools.swipeToElement(RECOMMENDED_PARTNER_SECTION,Swipe.UP,5);
        driverTools.swipeUp(2);
        WebElement partnerPage = driverTools.swipeToElement(getPartnerPageLocator(keyword),Swipe.UP,2).orElseThrow();
        driverTools.click(partnerPage);
    }

    @Override
    public String selectRecommendedDestination(String destination) {
        driverTools.waitForElementToAppear(RECOMMENDED_DESTINATION_SECTION);
        final Locator DESTINATION_NAME = getRecommendedDestination(destination);
        driverTools.waitForElementToAppear(DESTINATION_NAME);
        driverTools.swipeToElement(DESTINATION_NAME, Swipe.UP, 2);
        String selectedDestination = driverTools.getText(DESTINATION_NAME).orElseThrow();
        driverTools.click(DESTINATION_NAME);

        return selectedDestination;
    }
    @Override
    public List<String> getProductTitleOrder() {
        driverTools.waitForElementToBeVisible(PRODUCT_TITLE);
        LinkedHashSet<String> productTitles = new LinkedHashSet<>();
        driverTools.swipeDown();

        int maxSwipe = 10;

        while (productTitles.size() < 10 && maxSwipe-- > 0) {

            List<WebElement> elements = driverTools.elements(PRODUCT_TITLE).orElseThrow();

            for (WebElement element : elements) {
                String title = element.getText().trim().toLowerCase();
                if (!title.isEmpty()) {
                    productTitles.add(title);
                }
            }

            if (productTitles.size() < 10) {
                driverTools.swipeUp();
            }
        }

        return new ArrayList<>(productTitles);
    }
    private Locator getRecommendedDestination(String destination) {
        if(destination.contains("/")) {
            String destinationId = destination.split("/")[0];
            String destinationEng = destination.split("/")[1];
            return new Locator("ACTIVITY_NAME", AppiumBy.xpath("//*[contains(@resource-id,'search_suggestion_view') and ./*[contains(@resource-id,'container_category_location')]]/*[contains(@resource-id,'tv_item_name') and @text='"+destinationId+"' or @text='"+destinationEng+"'] | //*[contains(@resource-id,'tv_destination') and @text='"+destinationId+"' or @text='"+destinationEng+"']"));
        } else {
            return new Locator("ACTIVITY_NAME", AppiumBy.xpath("//*[contains(@resource-id,'search_suggestion_view') and ./*[contains(@resource-id,'container_category_location')]]/*[contains(@resource-id,'tv_item_name') and @text='"+destination+"'] | //*[contains(@resource-id,'tv_destination') and @text='"+destination+"']"));
        }
    }

    @Override
    public void clickRecentlySearchedItem(String keyword) {
        final Locator RECENTLY_SEARCHED_TARGET = getRecentlySearchedTargetLocator(keyword);
        driverTools.waitForElementToAppear(RECENTLY_SEARCHED_TARGET).orElseThrow();
        driverTools.click(RECENTLY_SEARCHED_TARGET);
    }

    @Override
    public void clickLocationButton(){
        driverTools.waitForElementToPresence(LOCATION_BUTTON);
        driverTools.click(LOCATION_BUTTON);
    }

    @Override
    public int verifyLocationChips(){
        driverTools.waitForElementToPresence(AREA_LOCATION_CHIP);
        driverTools.waitForElementToPresence(NEAR_LOCATION_CHIPS);
        int locationChipsCount = driverTools.elements(NEAR_LOCATION_CHIPS).orElseThrow().size();
        logger.log("Location Chips Count: " + locationChipsCount);
        return locationChipsCount;
    }

    @Override
    public String getCityLocationChipText() {
        String locationText = driverTools.getText(AREA_LOCATION_CHIP).orElseThrow();
        logger.log("City Location Chip Text: " + locationText);
        return locationText;
    }

    @Override
    public void clickCityLocationChip() {
        driverTools.waitForElementToPresence(AREA_LOCATION_CHIP);
        driverTools.click(AREA_LOCATION_CHIP);
    }

    @Override
    public String getLocation(){
        String locationText = driverTools.getText(LOCATION_BUTTON).orElseThrow();
        logger.log("Selected Location: " + locationText);
        return locationText;
    }
}
