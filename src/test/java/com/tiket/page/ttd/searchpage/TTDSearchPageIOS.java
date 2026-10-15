package com.tiket.page.ttd.searchpage;

import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tiket.core.CoreFunctions.generateRandomInt;
import static org.hamcrest.core.Is.is;

public class TTDSearchPageIOS<T extends WebDriver> extends TTDSearchPage<T> {

    public static final Locator BACK_BUTTON = new Locator("BACK_BUTTON", AppiumBy.id("ttd_close_btn"));
    public static final Locator SEARCH_BAR_SEARCHPAGE = new Locator("SEARCH_BAR_SEARCHPAGE", AppiumBy.xpath("//XCUIElementTypeTextField[@name='ttd_search_input']"));
    public static final Locator SEARCH_INPUT_FIELD = new Locator("SEARCH_INPUT_FIELD", AppiumBy.name("ttd_search_input"));
    public static final Locator CLEAR_SEARCH_INPUT = new Locator("CLEAR_SEARCH_INPUT", AppiumBy.accessibilityId("ttd_search_clear_text"));
    public static final Locator SEARCH_BAR_CLOSE_BUTTON = new Locator("SEARCH_BAR_CLOSE_BUTTON", AppiumBy.id("ttd_close_btn"));
    public static final Locator TOP_KEYWORD_SEARCHPAGE = new Locator("TOP_KEYWORD_SEARCHPAGE", AppiumBy.xpath("//XCUIElementTypeTable//XCUIElementTypeOther//XCUIElementTypeStaticText[@name='Refresh']/parent::*"));
    public static final Locator TOP_KEYWORD_TEXT = new Locator("TOP_KEYWORD_TEXT", AppiumBy.xpath("//XCUIElementTypeStaticText[@name='ttd_top_keyword_item'] | //XCUIElementTypeCell[contains(@name,'ttd_top_keyword')]//XCUIElementTypeStaticText"));
    public static final Locator RESULT_SECTION = new Locator("RESULT_SECTION", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@name,'Rekomendasi') or contains(@name,'Recommendation')]")); // FIXME: better locator required
    public static final Locator POPULAR_ACTIVITIES_HEADING = new Locator("POPULAR_ACTIVITIES_HEADING", AppiumBy.xpath("//XCUIElementTypeOther[@name='Aktivitas Populer' or @name='Popular Actitvity']"));
    public static final Locator SEE_ALL_ACTIVITIES = new Locator("SEE_ALL_ACTIVITIES", AppiumBy.xpath("((//XCUIElementTypeStaticText[(contains(@name,'Aktivitas Populer') or contains(@name,'Popular Activities'))])/following-sibling::*)[1]")); // TODO: Need Unique Locator - TTD - iOS - Akbar
    public  static final Locator EXPLORE_BY_NEARBY_ACTIVITY = new Locator("EXPLORE_BY_NEARBY_ACTIVITY", AppiumBy.accessibilityId("ttd_explore_by_nearby"));
    public static final Locator FIRST_POPULAR_DESTINATION = new Locator("FIRST_POPULAR_DESTINATION", AppiumBy.xpath("//XCUIElementTypeOther[@name='Destinasi Populer' or @name='Popular Destinations'][1]//following-sibling::XCUIElementTypeCell[1]/XCUIElementTypeStaticText[2]"));
    public static final Locator FIRST_POPULAR_PARTNER = new Locator("FIRST_POPULAR_PARTNER", AppiumBy.xpath("(//XCUIElementTypeStaticText[@name='popular_partner_title'])[1]"));
    public static final Locator RECOMMENDED_ACTIVITY_SECTION = new Locator("RECOMMENDED_ACTIVITY_SECTION", AppiumBy.xpath("//XCUIElementTypeStaticText[(@label='Rekomendasi Aktivitas') or (@label='Recommended Activity') or (@label='Recommended Activities')]"));
    public static final Locator NEXT_CATEGORY_AFTER_ALL_CATEGORY = new Locator("NEXT_CATEGORY_AFTER_ALL_CATEGORY", AppiumBy.xpath("//XCUIElementTypeStaticText[@name='Semua' or @name='All']/parent::*/following-sibling::*[1]//XCUIElementTypeStaticText"));
    public static final Locator KEYBOARD_DELETE_BTN = new Locator("KEYBOARD_DELETE_BTN", AppiumBy.xpath("//XCUIElementTypeKey[@name='delete']"));
    public static final Locator CLEAR_ALL_RECENT_SEARCH = new Locator("CLEAR_ALL_RECENT_SEARCH", AppiumBy.xpath("//XCUIElementTypeOther[@name='Pencarian Terakhir' or @name='Recently Searched']//XCUIElementTypeStaticText[@name='Hapus semua' or @name='Clear all']"));
    public static final Locator SEE_ALL_POPULAR_ACTIVITIES = new Locator("SEE_ALL_POPULAR_ACTIVITIES", AppiumBy.xpath("//XCUIElementTypeOther[@name='Aktivitas Populer' or @name='Popular Activities']//XCUIElementTypeStaticText[@name='Lihat semua' or @name='See All' or @name='See all']"));
    public static final Locator SEE_ALL_POPULAR_DESTINATION = new Locator("SEE_ALL_POPULAR_DESTINATION", AppiumBy.xpath("//XCUIElementTypeOther[@name='Destinasi Populer' or @name='Popular Destinations']/XCUIElementTypeStaticText[@name='Lihat semua' or @name='See All' or @name='See all']"));
    public static final Locator REFRESH_TOP_KEYWORD = new Locator("REFRESH_TOP_KEYWORD", AppiumBy.xpath("(//XCUIElementTypeStaticText[@name='Refresh'])[1]"));
    private static final Locator ATTRACTION_TAB = new Locator("ATTRACTION_TAB", AppiumBy.xpath("//*[@label='Atraksi Populer' or @label='Popular Attractions']"));
    private static final Locator EVENT_TAB = new Locator("EVENT_TAB", AppiumBy.xpath("//*[@label='Event Populer' or @label='Popular Events']"));
    private static final Locator DESTINATION_TAB = new Locator("DESTINATION_TAB", AppiumBy.xpath("//*[@label='Destinasi Populer' or @label='Popular Destinations']"));
    private static final Locator NEW_ACTIVITY_TAB = new Locator("NEW_ACTIVITY_TAB", AppiumBy.xpath("//*[(@label='Aktivitas Baru' or @label='New Activities') and @visible='true']"));
    private static final Locator TAB_CONTAINER = new Locator("TAB_CONTAINER", AppiumBy.xpath("//XCUIElementTypeScrollView/XCUIElementTypeOther[1]"));
    private static final Locator SEE_ALL_BUTTON = new Locator("SEE_ALL_BUTTON", AppiumBy.xpath("//XCUIElementTypeButton[contains(@label,'See all') or contains(@label,'Lihat semua')]"));
    private static final Locator TODO_RANK = new Locator("TODO_RANK", AppiumBy.xpath("//*[@name='ttd_top_keyword']//following-sibling::XCUIElementTypeCell[last()]//XCUIElementTypeTable//XCUIElementTypeCell[@visible='true']//XCUIElementTypeStaticText[contains(@label,',')]//following-sibling::*[1]"));
    private static final Locator TODO_NAME = new Locator("TODO_NAME", AppiumBy.xpath("//*[@name='ttd_top_keyword']//following-sibling::XCUIElementTypeCell[last()]//XCUIElementTypeTable//XCUIElementTypeCell[@visible='true']//XCUIElementTypeStaticText[last()]"));
    private static final Locator RECOMMENDED_DESTINATION_SECTION = new Locator("RECOMMENDED_DESTINATION_SECTION", AppiumBy.xpath("//XCUIElementTypeOther[@label='Rekomendasi Lokasi' or @label='Recommendation Locations']"));
    private static final Locator PRODUCT_TITLE = new Locator("PRODUCT_TITLE", AppiumBy.xpath("//XCUIElementTypeTable/XCUIElementTypeCell/XCUIElementTypeTable[./XCUIElementTypeCell[@visible='true']]/XCUIElementTypeCell/XCUIElementTypeStaticText[last()]"));
    private static final Locator LOCATION_BUTTON = new Locator("LOCATION_BUTTON", AppiumBy.xpath("(//*[@type='XCUIElementTypeOther']//*[@type='XCUIElementTypeOther']//*[@type='XCUIElementTypeStaticText'])[2]"));
    private static final Locator AREA_LOCATION_CHIP = new Locator("AREA_LOCATION_CHIP", AppiumBy.xpath("(//*[(contains(@label,'See all activities in') or contains(@label,'Lihat semua aktivitas di')) and @type='XCUIElementTypeStaticText']/following-sibling::*//*[@ type ='XCUIElementTypeStaticText'])[1]"));
    private static final Locator NEAR_LOCATION_CHIPS = new Locator("NEAR_LOCATION_CHIPS", AppiumBy.xpath("//*[(contains(@label,'See all activities in') or contains(@label,'Lihat semua aktivitas di')) and @type='XCUIElementTypeStaticText']/following-sibling::*//*[@ type ='XCUIElementTypeStaticText']"));

    public TTDSearchPageIOS(Session<T> session) {
        super(session);
    }

    @Override
    public void selectSection(String section) {
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
                driverTools.waitForElementToPresence(TAB_CONTAINER);
                driverTools.swipeByDistance(TAB_CONTAINER, Swipe.LEFT, 0.6);
                sectionElement = driverTools.waitForElementToPresence(NEW_ACTIVITY_TAB).orElseThrow();
                break;
        }
        if (sectionElement == null) {
            logger.log("There is no section for: " + section);
        } else {
            try {
                driverTools.clickElementByCoordinate(sectionElement);
            } catch (Exception e) {
                driverTools.click(sectionElement);
            }

        }
    }

    @Override
    public boolean checkToDoRank(int expectedTotalRank) {
        driverTools.swipeToElement(SEE_ALL_BUTTON, Swipe.UP, 3).orElseThrow();
        driverTools.waitForElementToPresence(TODO_RANK);
        for (int i = 1; i <= expectedTotalRank; i++) {
            List<WebElement> toDoRank = driverTools.elements(TODO_RANK, 20).orElseThrow();
            Assertion.assertThat("Product should have rank " + i, toDoRank.get(i-1).getText(), is(String.valueOf(i)));
        }
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
        List<WebElement> toDoName = driverTools.elements(TODO_NAME, 20).orElseThrow();
        int totalProduct = toDoName.size();
        logger.log("total products -> " + totalProduct);
        int index = list.equals("random") ? generateRandomInt(1, totalProduct-1) : Integer.parseInt(list);
        logger.log("index -> " + index);
        productName = driverTools.getText(toDoName.get(index));
        driverTools.clickElementByCoordinate(toDoName.get(index));
        return productName;
    }

    @Override
    public void clickSeeAllButton() {
        driverTools.swipeToElement(SEE_ALL_BUTTON, Swipe.UP, 5);

        if (driverTools.isElementPresent(SEE_ALL_BUTTON)){
            driverTools.click(SEE_ALL_BUTTON);
        } else {
            driverTools.swipeDown(5);
        }
    }

    @Override
    public boolean isSearchPageValid() {
        boolean topKeywordElementDisplayed = driverTools.isElementDisplayed(TOP_KEYWORD_SEARCHPAGE);
        boolean searchBarElementDisplayed = driverTools.isElementDisplayed(SEARCH_BAR_SEARCHPAGE);
        return topKeywordElementDisplayed && searchBarElementDisplayed;
    }

    @Override
    public boolean isPopularDestinationDisplayed() {
        return false;
    }

    @Override
    public boolean isTopKeywordDisplayed() {
        driverTools.waitForElementToAppear(TOP_KEYWORD_TEXT);
        return driverTools.isElementDisplayed(TOP_KEYWORD_TEXT);
    }

    @Override
    public boolean isPopularPartnersDisplayed() {
        return false;
    }

    @Override
    public boolean isRecentlySearchedValid(String clickedResult) {
        return driverTools.waitForElementToAppear(getRecentlySearchedLocator(clickedResult)).isPresent();
    }

    @Override
    public void inputSearchKeyword(String keyword) {
        String searchInput = "";
        if(keyword.contains("/")) {
            searchInput += keyword.split("/")[0];
        } else {
            searchInput += keyword;
        }

        WebElement searchBar = driverTools.waitForElementToPresence(SEARCH_BAR_SEARCHPAGE, 20).orElseThrow();
        driverTools.sendKeys(searchBar, searchInput);
    }

    @Override
    public void clickAndCheckAllTopKeyword() {
        driverTools.waitForElementToAppear(TOP_KEYWORD_TEXT,5);
        List<WebElement> listOfTopKeywordElements = driverTools.elements(TOP_KEYWORD_TEXT).orElseThrow();
        int topKeywordCount = listOfTopKeywordElements.size();

        //checking only top 3 keyword
        for (int i = 1 ; i <= 3 ; i++) {
            WebElement topKeywordElement = driverTools.element(getTopKeywordLocatorByIndex(i)).orElseThrow();
            String topKeywordText = driverTools.getText(topKeywordElement);
            logger.log("Checking top keyword '"+ topKeywordText +"'");

            topKeywordElement.click();
            if (!driverTools.isElementDisplayed(RESULT_SECTION)) {
                logger.log("Top Keyword '"+ topKeywordText +"' does not have any result");
            }

            //clearing search field
            driverTools.click(SEARCH_BAR_SEARCHPAGE);
            if(driverTools.isElementDisplayed(CLEAR_SEARCH_INPUT)){
                clickClearSearch();
            } else {
                driverTools.click(KEYBOARD_DELETE_BTN);
                if(driverTools.isElementDisplayed(CLEAR_SEARCH_INPUT)) {
                    clickClearSearch();
                } else {
                    for (int j = 1; j <= topKeywordText.length()-1; j++) {
                        driverTools.click(KEYBOARD_DELETE_BTN);
                    }
                }
            }
        }
    }

    private Locator getTopKeywordLocatorByIndex(int index) {
        return new Locator("TOP_KEYWORD_"+index, AppiumBy.xpath("(//XCUIElementTypeStaticText[@name='ttd_top_keyword_item'])["+ index +"] | (//XCUIElementTypeCell[contains(@name,'ttd_top_keyword')]//XCUIElementTypeStaticText)["+ index +"]"));
    }

    @Override
    public void clickSeeAllActivities() {
        WebElement seeAllActivities = driverTools.waitForElementToPresence(SEE_ALL_ACTIVITIES).orElseThrow();
        driverTools.click(seeAllActivities);
    }

    private Locator getPopularDestinationNameElement(int index){
        return new Locator("POPULAR_DESTINATION", AppiumBy.xpath("(//XCUIElementTypeStaticText[@name='ttd_popular_destination_card_title'])["+index+"]"));
    }

    @Override
    public String pickAndClickPopularDestination() {
        driverTools.swipeUp(2);

        WebElement firstPopularDestinationNameElement = driverTools.waitForElementToAppear(getPopularDestinationNameElement(1)).orElseThrow();
        String firstPopularDestinationName = driverTools.getText(firstPopularDestinationNameElement);

        driverTools.click(firstPopularDestinationNameElement);

        return firstPopularDestinationName;
    }

    public void clickNearbyActivity() {
        driverTools.click(EXPLORE_BY_NEARBY_ACTIVITY);
    }

    @Override
    public void clickClearSearch() {
        driverTools.waitForElementToAppear(CLEAR_SEARCH_INPUT).ifPresent(driverTools::click);
    }

    @Override
    public String pickAndClickPopularPartner() {
        driverTools.swipeUp(3);
        String partnerName = driverTools.getText(FIRST_POPULAR_PARTNER).orElseThrow();
        driverTools.click(FIRST_POPULAR_PARTNER);
        return partnerName;
    }

    @Override
    public Map<String, String> pickAndClickPopularActivity() {
        Map<String, String> clickedPopularActivities = new HashMap<>();

        int randomNumber = generateRandomInt(1,3);

        Locator activityName = getActivityNameLocator(randomNumber);
        Locator activityType = getActivityTypeLocator(randomNumber);
        Locator activityLocation = getActivityLocationLocator(randomNumber);

        WebElement activityNameElement = driverTools.waitForElementToAppear(activityName,5).orElseThrow();
        WebElement activityTypeElement = driverTools.waitForElementToAppear(activityType,5).orElseThrow();
        WebElement activityLocationElement = driverTools.waitForElementToAppear(activityLocation,5).orElseThrow();

        String activityNameString = driverTools.getText(activityNameElement);
        String activityTypeString = driverTools.getText(activityTypeElement);
        activityTypeString = activityTypeString.substring(0, activityTypeString.length()-3); //removing " •"
        String activityLocationString = driverTools.getText(activityLocationElement);

        clickedPopularActivities.put("name", activityNameString);
        clickedPopularActivities.put("type", activityTypeString);
        clickedPopularActivities.put("location", activityLocationString);

        driverTools.click(activityName);

        return clickedPopularActivities;
    }

    @Override
    public boolean isPopularActivitiesDisplayed() {
        boolean isPopularActivitiesHeadingDisplayed = driverTools.waitForElementToAppear(POPULAR_ACTIVITIES_HEADING, 5).isPresent();
        if (!isPopularActivitiesHeadingDisplayed) {
            logger.warn("Popular Heading is not displayed");
        }
        return isPopularActivitiesHeadingDisplayed;
    }

    @Override
    public void selectRecommendedActivity(String keyword) {
        driverTools.waitForElementToAppear(RECOMMENDED_ACTIVITY_SECTION);
        Locator activityName = getRecommendedActivity(keyword);
        WebElement activityNameElement = driverTools.swipeToElement(activityName, Swipe.UP, 5).orElseThrow();
        driverTools.click(activityNameElement);
    }

    @Override
    public void selectRecommendedActivitySRP(String keyword) {
        /// implementation pending
    }

    private Locator getRecommendedActivity(String keyword) {
        if(keyword.contains("/")) {
            String keywordId = keyword.split("/")[0];
            String keywordEng = keyword.split("/")[1];
            return new Locator("ACTIVITY_NAME", AppiumBy.xpath("//*[(contains(@label,'Rekomendasi Aktivitas') or contains(@label,'Recommended Activities')) or contains(@label,' • ')]//following::XCUIElementTypeStaticText[@name='ttd_search_page_search_result_title' and (contains(@label,'"+keywordId+"') or contains(@label,'"+keywordEng+"'))]"));
        } else {
            return new Locator("ACTIVITY_NAME", AppiumBy.xpath("//*[(contains(@label,'Rekomendasi Aktivitas') or contains(@label,'Recommended Activities')) or contains(@label,' • ')]//following::XCUIElementTypeStaticText[@name='ttd_search_page_search_result_title' and contains(@label,'"+keyword+"')]"));
        }
    }

    private Locator getSearchSuggestion(String keyword) {
        if(keyword.contains("/")) {
            String keywordId = keyword.split("/")[0];
            String keywordEng = keyword.split("/")[1];
            return new Locator("ACTIVITY_NAME", AppiumBy.xpath("//XCUIElementTypeStaticText[@name='ttd_search_page_search_result_title' and (contains(@label,'"+keywordId+"') or contains(@label,'"+keywordEng+"'))]"));
        } else {
            return new Locator("ACTIVITY_NAME", AppiumBy.xpath("//XCUIElementTypeStaticText[@name='ttd_search_page_search_result_title' and contains(@label,'"+keyword+"')]"));
        }
    }

    private Locator getExactRecommendedSearchSuggestion(String keyword) {
        if(keyword.contains("/")) {
            String keywordId = keyword.split("/")[0];
            String keywordEng = keyword.split("/")[1];
            return new Locator("ACTIVITY_NAME", AppiumBy.xpath("//XCUIElementTypeStaticText[@name='ttd_search_page_search_result_title' and (@label='"+keywordId+"' or @label='"+keywordEng+"')]"));
        } else {
            return new Locator("ACTIVITY_NAME", AppiumBy.xpath("//XCUIElementTypeStaticText[@name='ttd_search_page_search_result_title' and @label='"+keyword+"']"));
        }
    }

    private Locator getActivityNameLocator(int index) {
        return new Locator("ACTIVITY_NAME", AppiumBy.xpath("(//XCUIElementTypeStaticText[@name='popular_activity_title'])["+index+"]"));
    }

    /** element's label get extra char with " •", such as "Attraction •", don't forget to clean it up */
    private Locator getActivityTypeLocator(int index) {
        return new Locator("ACTIVITY_TYPE", AppiumBy.xpath("(//XCUIElementTypeStaticText[@name='popular_activity_category'])["+index+"]"));
    }

    private Locator getActivityLocationLocator(int index) {
        return new Locator("ACTIVITY_LOCATION", AppiumBy.xpath("(//XCUIElementTypeStaticText[@name='popular_activity_location'])["+index+"]"));
    }

    private Locator getRecentlySearchedLocator(String clickedResult) {
        if(clickedResult.contains("/")) {
            String keywordId = clickedResult.split("/")[0];
            String keywordEng = clickedResult.split("/")[1];
            return new Locator("TARGET_RECENTLY_SEARCHED", AppiumBy.xpath("//XCUIElementTypeOther[@name='Recently Searched' or @name='Pencarian Terakhir']/following-sibling::*[1]//XCUIElementTypeStaticText[contains(@label,'"+keywordId+"') or contains(@label,'"+keywordEng+"')]"));
        } else {
            return new Locator("TARGET_RECENTLY_SEARCHED", AppiumBy.xpath("//XCUIElementTypeOther[@name='Recently Searched' or @name='Pencarian Terakhir']/following-sibling::*[1]//XCUIElementTypeStaticText[contains(@label,'"+clickedResult+"')]"));
        }
    }

    @Override
    public void clickClearAll() {
        driverTools.click(CLEAR_ALL_RECENT_SEARCH);
    }

    @Override
    public boolean isSearchCleared() {
        boolean isClearAllDisplayed = driverTools.isElementDisplayed(CLEAR_ALL_RECENT_SEARCH);
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
        return driverTools.getValueAttribute(SEARCH_INPUT_FIELD,"value");
    }

    @Override
    public void clickRefreshTopKeyword() {
        driverTools.waitForElementToPresence(REFRESH_TOP_KEYWORD);
        driverTools.clickElementByCoordinate(REFRESH_TOP_KEYWORD);
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
        driverTools.waitForElementToAppear(TOP_KEYWORD_TEXT);
        List<WebElement> listOfTopKeywordElements = driverTools.elements(TOP_KEYWORD_TEXT).orElseThrow();
        int randomIndex = generateRandomInt(0, listOfTopKeywordElements.size()-1);
        String text = driverTools.getText(listOfTopKeywordElements.get(randomIndex));
        driverTools.click(listOfTopKeywordElements.get(randomIndex));
        return text;
    }

    @Override
    public void clickBack() {
        driverTools.click(BACK_BUTTON);
    }

    private Locator getPartnerPageLocator(String keyword){
        return new Locator("PARTNER_PAGE", AppiumBy.xpath("(//XCUIElementTypeOther[@name='Rekomendasi Partner' or @name='Recommended Partners'])/following-sibling::XCUIElementTypeCell//XCUIElementTypeStaticText[contains(@name,'"+keyword+"')]"));
    }
    @Override
    public void findAndClickPartnerPage(String keyword){
        driverTools.swipeDown();
        driverTools.swipeDown();
        driverTools.waitForElementToPresence(getPartnerPageLocator(keyword));
        WebElement partnerPage = driverTools.swipeToElement(getPartnerPageLocator(keyword),Swipe.UP,6).orElseThrow();
        driverTools.click(partnerPage);
    }

    @Override
    public String selectRecommendedDestination(String destination) {
        driverTools.waitForElementToAppear(RECOMMENDED_DESTINATION_SECTION);
        final Locator DESTINATION_NAME = getExactRecommendedSearchSuggestion(destination);
        driverTools.waitForElementToAppear(DESTINATION_NAME);
        driverTools.swipeToElement(DESTINATION_NAME, Swipe.UP, 2);
        String selectedDestination = driverTools.getText(DESTINATION_NAME).orElseThrow();
        driverTools.click(DESTINATION_NAME);

        return selectedDestination;
    }

    @Override
    public List<String> getProductTitleOrder() {
        driverTools.swipeToElement(SEE_ALL_BUTTON, Swipe.UP, 4);
        List<String> productTitleOrderUILowerCase = new ArrayList<>();
        List<WebElement> productTitleList = driverTools.elements(PRODUCT_TITLE, 20).orElseThrow();
        for (int i = 0; i < productTitleList.size(); i++) {
            String productTitle = driverTools.getText(productTitleList.get(i));
            logger.log(i + " productTitle -> " + productTitle);
            productTitleOrderUILowerCase.add(productTitle.toLowerCase());
        }

        return productTitleOrderUILowerCase;
    }

    @Override
    public void clickRecentlySearchedItem(String keyword) {
        Locator recentlySearched = getRecentlySearchedLocator(keyword);
        driverTools.waitForElementToAppear(recentlySearched,5);
        WebElement recentlySearchedElement = driverTools.swipeToElement(recentlySearched, Swipe.UP, 5).orElseThrow();
        driverTools.click(recentlySearchedElement);
    }

    @Override
    public void clickLocationButton(){
        driverTools.waitForElementToPresence(LOCATION_BUTTON).ifPresent(driverTools::click);
    }

    @Override
    public int verifyLocationChips(){
        driverTools.waitForElementToPresence(NEAR_LOCATION_CHIPS,20);
        List<String> locationChips = driverTools.getTexts(NEAR_LOCATION_CHIPS);
        logger.log("Location Chips Count: " + locationChips.toString());
        return locationChips.size();
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