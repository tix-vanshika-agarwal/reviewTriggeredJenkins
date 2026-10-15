package com.tiket.page.ttd.landingpage;

import com.tiket.appium.Swipe;
import com.tiket.model.Locator;
import com.tiket.model.PriceType;
import com.tiket.model.Session;
import com.tiket.page.home.HomePageAndroid;
import com.tiket.page.ttd.destinationpage.TTDDestinationPageAndroid;
import com.tiket.page.ttd.searchresultpage.TTDSearchResultPage;
import com.tiket.pojo.ttd.TTDLandingPagePOJO;
import com.tiket.testbase.Assertion;
import com.tiket.web.page.ttd.TtdProductDetailPage.TtdProductDetailPageWeb;
import io.appium.java_client.AppiumBy;
import org.hamcrest.CoreMatchers;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.tiket.core.CoreFunctions.generateRandomInt;
import static com.tiket.core.CoreFunctions.getPrice;
import static com.tiket.page.home.HomePageAndroid.SCREEN;
import static com.tiket.page.ttd.explorepage.TTDExplorePageAndroid.*;
import static com.tiket.page.ttd.searchresultpage.TTDSearchResultPageAndroid.BACK_BUTTON_SRP;
import static com.tiket.page.ttd.searchresultpage.TTDSearchResultPageAndroid.SRP_APP_BAR;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.Is.is;

public class TTDLandingPageAndroid<T extends WebDriver> extends TTDLandingPage<T> {

    public static final Locator PERMISSION_TYPE_ALLOW_ONCE = new Locator("PERMISSION_TYPE_ALLOW_ONCE", AppiumBy.xpath("//*[contains(@resource-id,'permission_allow_one_time_button')]"));
    public static final Locator SEARCH_BAR_LANDINGPAGE = new Locator("SEARCH_BAR_LANDINGPAGE", AppiumBy.xpath("//*[@class='android.view.ViewGroup' and ./*[contains(@resource-id, 'tds_text_search_box')]]"));
    public static final Locator SEARCH_BAR_SRP = new Locator("SEARCH_BAR_SRP", AppiumBy.xpath("//*[contains(@resource-id, 'container_tap_area_keyword')]"));
    public static final Locator TTD_LANDING_PAGE = new Locator("TTD_LANDING_PAGE", AppiumBy.id("home_view"));
    public static final Locator EXPLORE_DESTINATION_LANDINGPAGE = new Locator("EXPLORE_DESTINATION_LANDINGPAGE", AppiumBy.id("ll_container"));
    public static final Locator EXPLORE_ARROW_LANDINGPAGE = new Locator("EXPLORE_ARROW_LANDINGPAGE", AppiumBy.id("iv_explore_destination"));
    public static final Locator EXPLORE_TEXT_LANDINGPAGE = new Locator("EXPLORE_TEXT_LANDINGPAGE", AppiumBy.id("tv_explore_destination"));
    public static final Locator ENABLE_LOCATION_BUTTON_LANDINGPAGE = new Locator("ENABLE_LOCATION_BUTTON_LANDINGPAGE", AppiumBy.id("container_enable_location"));
    public static final Locator ENABLE_LOCATION_BUTTON_LANDINGPAGE_NEW = new Locator("ENABLE_LOCATION_BUTTON_LANDINGPAGE_NEW", AppiumBy.id("container_location_off"));
    private static final Locator SELECTED_LOCATION = new Locator("SELECTED_LOCATION", AppiumBy.xpath("//*[contains(@resource-id,'iv_badge')]//preceding-sibling::*//*[contains(@resource-id,'tv_chips_title')]"));
    private static final Locator SELECTED_LANDING_PAGE_TAB = new Locator("SELECTED_LANDING_PAGE_TAB", AppiumBy.xpath("//*[contains(@resource-id,'tds_title_tab_layout') and @selected='true']"));
    private static final Locator LANDING_PAGE_TAB = new Locator("LANDING_PAGE_TAB", AppiumBy.id("tds_title_tab_layout"));
    public static final Locator ALLOW_ACCESS_BUTTON_LANDINGPAGE = new Locator("ALLOW_ACCESS_BUTTON_LANDINGPAGE", AppiumBy.xpath("//*[contains(@resource-id,'tds_btn_primary_info_view')]//*[contains(@resource-id,'tds_button_view')]"));
    public static final Locator BACK_BUTTON_LANDINGPAGE = new Locator("BACK_BUTTON_LANDINGPAGE", AppiumBy.id("appbar_back_icon"));
    public static final Locator SEE_ALL_BUTTON_LANDINGPAGE = new Locator("SEE_ALL_BUTTON_LANDINGPAGE", AppiumBy.xpath("//*[contains(@resource-id, 'btn_see_all')]"));
    public static final Locator FIRST_CARD_TTD = new Locator("FIRST_CARD_TTD ", AppiumBy.xpath("(//*[contains(@resource-id, 'cv_card_container')])[1]"));

    //TTD Product Category
    public static final Locator ATTRACTIONS_CATEGORY = new Locator("ATTRACTIONS_CATEGORY", AppiumBy.xpath("//*[contains(@resource-id, 'cl_container')]//*[contains(@text,'Attractions') or contains(@text, 'Atraksi')]"));
    public static final Locator PLAYGROUNDS_CATEGORY = new Locator("PLAYGROUNDS_CATEGORY", AppiumBy.xpath("//*[contains(@resource-id, 'cl_container')]//*[contains(@text,'Playgrunds') or contains(@text, 'Tempat Bermain')]"));
    public static final Locator SPA_AND_BEAUTY_CATEGORY = new Locator("SPA_AND_BEAUTY_CATEGORY", AppiumBy.xpath("//*[contains(@resource-id, 'cl_container')]//*[contains(@text,'Spa & Beauty') or contains(@text, 'Spa & Kecantikan' )]"));
    public static final Locator TOURS_CATEGORY = new Locator("TOURS_CATEGORY", AppiumBy.xpath("//*[contains(@resource-id, 'cl_container')]//*[contains(@text,'Tours') or contains(@text, 'Tur' )]"));
    public static final Locator ENTERTAINMENTS_CATEGORY = new Locator("ENTERTAINMENTS_CATEGORY", AppiumBy.xpath("//*[contains(@resource-id, 'cl_container')]//*[contains(@text,'Entertainments') or contains(@text, 'Entertainment')]"));
    public static final Locator SPORT_AND_OUTDOORS_CATEGORY = new Locator("SPORT_AND_OUTDOORS_CATEGORY", AppiumBy.xpath("//*[contains(@resource-id, 'cl_container')]//*[contains(@text,'Sports & Outdoors') or contains(@text, 'Olahraga & Outdoor' )]"));
    public static final Locator EVENT_CATEGORY = new Locator("EVENT_CATEGORY", AppiumBy.xpath("//*[contains(@resource-id, 'cl_container')]//*[contains(@text,'Events') or contains(@text, 'Event')]"));
    private static final Locator JR_PASS_CATEGORY = new Locator("JR_PASS_CATEGORY", AppiumBy.xpath("//*[contains(@resource-id, 'cl_container')]//*[contains(@text,'JR Pass') or contains(@text, 'JR Pass')]"));
    private static final Locator TRAVEL_ESSENTIAL_CATEGORY = new Locator("TRAVEL_ESSENTIAL_CATEGORY", AppiumBy.xpath("//*[contains(@resource-id, 'cl_container')]//*[contains(@text,'Travel Essential') or contains(@text, 'Pelengkap Perjalanan')]"));
    private static final Locator UMROH_CATEGORY = new Locator("UMROH_CATEGORY", AppiumBy.xpath("//*[contains(@resource-id, 'cl_container')]//*[contains(@text,'Umroh') or contains(@text, 'Umroh')]"));
    private static final Locator WIFI_ESIM_CATEGORY = new Locator("WIFI_ESIM_CATEGORY", AppiumBy.xpath("//*[contains(@resource-id, 'cl_container')]//*[contains(@text,'Wifi & e-SIM') or contains(@text, 'Wifi & e-SIM')]"));

    private static final Locator SEE_ALL_MENU = new Locator("SEE_ALL_MENU", AppiumBy.xpath("(//*[contains(@resource-id,'tv_menu')])[last()]/parent::*"));
    private static final Locator PRODUCT_CATEGORY = new Locator("PRODUCT_CATEGORY", AppiumBy.id("tv_menu"));
    private static final Locator PRODUCT_CATEGORY_WITHOUT_ALL = new Locator("PRODUCT_CATEGORY_WITHOUT_ALL", AppiumBy.xpath("//*[contains(@resource-id,'tv_menu') and not(contains(@text,'All') or contains(@text,'Semua'))]"));
    private static final Locator TOP_PRODUCT_CATEGORY = new Locator("TOP_PRODUCT_CATEGORY", AppiumBy.xpath("//*[contains(@resource-id,'tv_top_category')]"));
    private static final Locator DESTINATION_CONTAINER = new Locator("DESTINATION_CONTAINER", AppiumBy.xpath("//*[contains(@resource-id, 'destination_chips_group')]"));
    private static final Locator LOCATION_TEXT = new Locator("LOCATION_TEXT", AppiumBy.id("tv_location"));
    private static final Locator CURRENT_LOCATION_OPTION = new Locator("CURRENT_LOCATION_OPTION", AppiumBy.xpath("//*[@class='android.view.ViewGroup' and ./*[@text='See all activities in'] or ./*[@text='Lihat semua aktivitas di']]"));
    public static final Locator SEE_ALL_BUTTON_CTA_WITH_LOCATION_TAG = new Locator("SEE_ALL_BUTTON_CTA_WITH_LOCATION_TAG", AppiumBy.xpath("//*[contains(@resource-id, 'rv_destination')]/android.view.ViewGroup[.//*[contains(@resource-id,'tds_chips_text')]]//*[contains(@resource-id, 'btn_see_all')]"));
    public static final Locator DESTINATION_DOT = new Locator("DESTINATION_DOT", AppiumBy.xpath("//*[contains(@resource-id, 'iv_badge')]"));
    public static final Locator CTA_SECTION_WITH_LOCATION_TAG = new Locator("CTA_SECTION_WITH_LOCATION_TAG", AppiumBy.xpath("//*[contains(@resource-id, 'rv_destination')]/android.view.ViewGroup[.//*[contains(@resource-id,'tds_chips_text')]]"));
    public static final Locator CTA_SECTION_PRODUCT_CARD = new Locator("CTA_SECTION_PRODUCT_CARD", AppiumBy.xpath("//*[contains(@resource-id, 'rv_destination')]/android.view.ViewGroup//*[contains(@resource-id, 'card_view')]"));
    private static final Locator LOCATION_TAGS = new Locator("LOCATION_TAGS", AppiumBy.id("tds_chips_text"));
    private static final Locator RECENTLY_VIEWED_SECTION = new Locator("RECENTLY_VIEWED_SECTION", AppiumBy.id("recently_viewed"));
    private static final Locator SEE_ALL_RECENTLY_VIEWED = new Locator("SEE_ALL_RECENTLY_VIEWED", AppiumBy.xpath("//*[contains(@resource-id,'recently_viewed')]//*[contains(@resource-id,'btn_cta')]"));
    private static final Locator RECENTLY_VIEWED_POPUP_ITEM = new Locator("RECENTLY_VIEWED_POPUP_ITEM", AppiumBy.xpath("//*[contains(@resource-id,'item_layout')]//*[contains(@resource-id,'tv_title')]"));
    private static final Locator RECENTLY_VIEWED_POPUP_DELETE_BUTTON = new Locator("RECENTLY_VIEWED_POPUP_DELETE_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'item_layout')]//*[contains(@resource-id,'btn_delete_item')]"));
    private static final Locator RECENTLY_VIEWED_POPUP_CONTAINER = new Locator("RECENTLY_VIEWED_POPUP_CONTAINER", AppiumBy.xpath("//*[contains(@resource-id,'item_layout')]//parent::*[contains(@resource-id,'rv_content')]"));
    public static final Locator PRICE_TEXTS = new Locator("PRICE_TEXTS", AppiumBy.xpath("//*[contains(@resource-id,'tv_base_price')] | //*[contains(@resource-id,'tv_final_price')] | //*[contains(@resource-id,'price_module_final_price')] | //*[contains(@resource-id,'tv_price_after_discount')]"));
    public static final Locator CROSSED_PRICE = new Locator("CROSSED_PRICE", AppiumBy.id("tv_base_price"));
    private static final Locator RECENTLY_VIEWED_CONTAINER = new Locator("RECENTLY_VIEWED_CONTAINER", AppiumBy.xpath("//*[contains(@resource-id,'recently_viewed')]/*[contains(@resource-id,'rv_content')]"));
    public static final Locator FIRST_RECENTLY_VIEWED_PRODUCT_NAME = new Locator("FIRST_RECENTLY_VIEWED_PRODUCT_NAME", AppiumBy.xpath("//*[contains(@resource-id,'recently_viewed')]/*[contains(@resource-id,'rv_content')]/child::*[1]//*[contains(@resource-id,'tv_title')]"));
    public static final Locator FIRST_RECENTLY_VIEWED_CROSSED_BUTTON = new Locator("FIRST_RECENTLY_VIEWED_CROSSED_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'recently_viewed')]/*[contains(@resource-id,'rv_content')]/child::*[1]//*[contains(@resource-id,'btn_delete_recently_viewed_item')]"));
    private static final Locator LOCATION_TEXT_WHEN_OFF = new Locator("LOCATION_TEXT_WHEN_OFF", AppiumBy.id("tv_subtitle_location_off"));
    private static final Locator LOCATION_TEXT_NEW = new Locator("LOCATION_TEXT", AppiumBy.xpath("//*[contains(@resource-id,'container_destination')]//android.widget.TextView[contains(@resource-id,'tv_destination')]"));
    private static final Locator FLOATING_BUTTON_ICON = new Locator("FLOATING_BUTTON_ICON", AppiumBy.id("tds_extended_fab_group"));
    private static final Locator FIRST_GROUP_MENU_CHIP = new Locator("FIRST_GROUP_MENU_CHIP", AppiumBy.xpath("(//*[contains(@resource-id, 'tv_menu')])[10]"));
    public static final Locator SELECTED_TAB = new Locator("SELECTED_TAB", AppiumBy.xpath("//*[contains(@resource-id, 'tds_title_tab_layout') and @selected='true']"));
    public static final Locator UNSELECTED_TAB = new Locator("UNSELECTED_TAB", AppiumBy.xpath("//*[contains(@resource-id, 'tds_title_tab_layout') and @selected='false']"));

    // Bottomsheet Activity Locator
    private static final Locator BOTTOMSHEET_CROSS = new Locator("BOTTOMSHEET_CROSS", AppiumBy.xpath("//*[contains(@resource-id,'iv_back_button') or contains(@resource-id,'btn_close') or contains(@resource-id,'ic_close')]"));
    private static final Locator BOTTOMSHEET_TITLE_TEXT = new Locator("BOTTOMSHEET_TITLE_TEXT", AppiumBy.id("tv_all_categories"));
    private static final Locator BOTTOMSHEET_CATEGORY_TITLE = new Locator("BOTTOMSHEET_CATEGORY_TITLE", AppiumBy.xpath("//*[contains(@resource-id,'rv_category_with_sub_categories')]//*[contains(@resource-id,'tv_category_title')]"));
    private static final Locator BOTTOMSHEET_CATEGORY_CHEVRON = new Locator("BOTTOMSHEET_CATEGORY_CHEVRON", AppiumBy.xpath("//*[contains(@resource-id,'rv_category_with_sub_categories')]//*[contains(@resource-id,'container_category_title')]//android.widget.ImageView"));
    private static final Locator FIRST_BOTTOMSHEET_SUB_CATEGORY_CHIP = new Locator("FIRST_BOTTOMSHEET_SUB_CATEGORY_CHIP", AppiumBy.xpath("(//*[contains(@resource-id,'cl_sub_category')])[1]"));

    //promo section Locator
    private static final Locator PROMO_SECTION = new Locator("PROMO_SECTION", AppiumBy.id("tv_section_title"));
    private static final Locator PROMO_SECTION_CLAIM_BTN = new Locator("PROMO_SECTION_CLAIM_BTN", AppiumBy.id("tv_claim"));
    private static final Locator SPECIAL_OFFER = new Locator("SPECIAL_OFFER", AppiumBy.xpath("//*[contains(@resource-id,'tds_title_tab_layout') and (@text='Special Offers' or @text='Penawaran Spesial')]"));

    //searchbar location chip
    private static final Locator LOCATION_CHIP_SEARCHBAR = new Locator("LOCATION_CHIP_SEARCHBAR", AppiumBy.xpath("//*[contains(@resource-id,'container_destination_selector')]//*[contains(@resource-id,'tv_location')]"));

    public TTDLandingPageAndroid(Session<T> session) {
        super(session);
    }
    private static Locator getUnselectedTab(String tabName) {
        return new Locator("SPECIFIC_UNSELECTED_TAB", AppiumBy.xpath("//*[contains(@resource-id, 'tds_title_tab_layout') and @selected='false' and @text='"+tabName+"']"));
    };

    @Override
    public void checkTabbing() {
        driverTools.swipeUp();
        driverTools.swipeToPresentedElement(SELECTED_TAB, Swipe.UP, 5);

        String selectedTab = driverTools.getText(SELECTED_TAB).orElseThrow();
        Assertion.assertThat("Default selected tab is 'For You' or 'Untukmu'", selectedTab, anyOf(is("For You"), is("Untukmu")));

        List<String> unselectedTabs = driverTools.getTexts(UNSELECTED_TAB);

        for (String tab: unselectedTabs) {
            logger.log("clicking tab -> " + tab);
            Locator tabLocator = getUnselectedTab(tab);
            WebElement tabButton  = driverTools.element(tabLocator).orElseThrow();
            driverTools.clickElementByCoordinate(tabButton);

            selectedTab = driverTools.getText(SELECTED_TAB).orElseThrow();
            Assertion.assertThat("Tab "+ tab +" gets selected", selectedTab, is(tab));
        }
    }

    @Override
    public void clickSeeAllRecentlyViewed() {
        driverTools.click(SEE_ALL_RECENTLY_VIEWED);
    }

    @Override
    public int getTotalRecentlyViewedItems() {
        driverTools.waitForElementToAppear(RECENTLY_VIEWED_POPUP_ITEM);
        List<WebElement> itemList = driverTools.elements(RECENTLY_VIEWED_POPUP_ITEM).orElseThrow();
        int totalItems = itemList.size();
        logger.log("total items -> " + totalItems);

        String lastItemName = driverTools.getText(itemList.get(totalItems-1));
        driverTools.swipeByDistance(RECENTLY_VIEWED_POPUP_CONTAINER, Swipe.UP, 0.9);
        driverTools.waitForElementToAppear(nextRecentlyViewedPopupItem(lastItemName));

        List<WebElement> nextItemList = driverTools.elements(nextRecentlyViewedPopupItem(lastItemName)).orElseThrow();
        int nextTotalItems = nextItemList.size();
        logger.log("next total items -> " + nextTotalItems);

        return totalItems + nextTotalItems;
    }

    private Locator nextRecentlyViewedPopupItem(String firstItemName) {
        return new Locator("NEXT_RECENTLY_VIEWED_POPUP_ITEM_" + firstItemName.toUpperCase(), AppiumBy.xpath("//*[contains(@resource-id,'item_layout')]//*[contains(@resource-id,'tv_title') and @text='" + firstItemName + "']//following::*[contains(@resource-id,'tv_title')]"));
    }

    @Override
    public String selectRecentlyViewedItem(String list) {
        String productName;
        List<WebElement> itemName = driverTools.elements(RECENTLY_VIEWED_POPUP_ITEM).orElseThrow();
        int totalProduct = itemName.size();
        logger.log("total products -> " + totalProduct);
        int index = list.equals("random") ? generateRandomInt(1, totalProduct-1) : Integer.parseInt(list);
        logger.log("index -> " + index);
        itemName = driverTools.elements(RECENTLY_VIEWED_POPUP_ITEM).orElseThrow();
        productName = driverTools.getText(itemName.get(index));
        driverTools.click(itemName.get(index));
        return productName;
    }

    @Override
    public String removeFirstRecentlyViewedItem() {
        String deletedItems = driverTools.getText(RECENTLY_VIEWED_POPUP_ITEM).orElseThrow();
        driverTools.click(RECENTLY_VIEWED_POPUP_DELETE_BUTTON);
        return deletedItems;
    }

    @Override
    public String getFirstRecentlyViewedItem() {
        return driverTools.getText(RECENTLY_VIEWED_POPUP_ITEM).orElseThrow();
    }

    @Override
    public boolean isRecentlyViewedDisplayed() {
        return driverTools.waitForElementToAppear(RECENTLY_VIEWED_SECTION).isPresent();
    }

    @Override
    public boolean isLandingPageValid(){
        return driverTools.isElementDisplayed(SEARCH_BAR_LANDINGPAGE);
    }

    @Override
    public void clickSearchBar() {
        driverTools.swipeToElement(SEARCH_BAR_LANDINGPAGE, Swipe.DOWN, 20);
        WebElement searchBar = driverTools.waitForElementToPresence(SEARCH_BAR_LANDINGPAGE).orElseThrow();
        driverTools.click(searchBar);
    }

    @Override
    public void clickSearchBarSRP() {
        WebElement searchBar = driverTools.waitForElementToPresence(SEARCH_BAR_SRP).orElseThrow();
        driverTools.click(searchBar);
    }

    @Override
    public void clickExploreArrow() {
        driverTools.click(EXPLORE_ARROW_LANDINGPAGE);
    }

    @Override
    public String openProductByCategory(String productCategory){
        boolean isNewUI = driverTools.waitForElementToPresence(FIRST_GROUP_MENU_CHIP).isPresent();
        logger.log("isNewUI -> " + isNewUI);

        driverTools.waitForElementToAppear(PRODUCT_CATEGORY);
        logger.log("opening product by category : " + productCategory);
        driverTools.waitForElementToAppear(SEARCH_BAR_LANDINGPAGE);

        if(productCategory.equalsIgnoreCase("random")) {
            int randomIndex = generateRandomInt(0, productCategoryList.length-1);
            productCategory = productCategoryList[randomIndex];
            if(!isNewUI){
                for(int i = 0; i < 3; i++) {
                    randomIndex = generateRandomInt(0, productCategoryList.length-1);
                    productCategory = productCategoryList[randomIndex];

                    try {
                        driverTools.getText(productCategoryElement(productCategory, "-")).orElseThrow();
                        break;
                    } catch (NoSuchElementException e) {
                        logger.log("product category '"+ productCategory +"' not found, retrying...");
                    }
                }
            }
        }

        WebElement productCategoryElement;

        //open selected category
        String productCategoryTitle = "";
        if (isNewUI) {
            Locator productCategoryLocator = productCategoryElement(productCategory, "-");
            Locator productCategoryTitleLocator = productCategoryElement(productCategory, "bottomsheet");

            if(driverTools.waitForElementToAppear(productCategoryLocator).isEmpty()) {
                clickSeeAllMenus();

                driverTools.swipeToElement(productCategoryTitleLocator, Swipe.UP_LONG, 5).orElseThrow();
                productCategoryElement = driverTools.element(productCategoryTitleLocator).orElseThrow();
            } else {
                productCategoryElement = driverTools.element(productCategoryLocator).orElseThrow();
            }
        } else {
            driverTools.waitForElementToAppear(productCategoryElement(productCategory, "-"));
            productCategoryElement = driverTools.swipeToElement(productCategoryElement(productCategory, "-"), Swipe.LEFT, 4).orElseThrow();
        }

        productCategoryTitle += driverTools.getText(productCategoryElement);
        logger.log("\"" + productCategoryTitle + "\" is selected");
        driverTools.clickElementByCoordinate(productCategoryElement);

        return productCategoryTitle;
    }

    private void clickSeeAllMenus() {
        driverTools.waitForElementToAppear(SEE_ALL_MENU);
        driverTools.clickElementByCoordinate(SEE_ALL_MENU);
    }

    private Locator productCategoryElement(String productCategory, String pageType) {
        productCategory = getProductCategory(productCategory);

        String productCategoryID;
        String productCategoryENG;
        if (productCategory.contains("/")) {
            productCategoryID = productCategory.split("/")[1];
            productCategoryENG = productCategory.split("/")[0];
        } else {
            productCategoryID = productCategory;
            productCategoryENG = productCategory;
        }

        return new Locator("PRODUCT_CATEGORY : " + productCategory, AppiumBy.xpath("//*[(contains(@resource-id,'tv_menu') or contains(@resource-id,'tv_category_title')) and (contains(@text,'" + productCategoryID + "') or contains(@text,'" + productCategoryENG + "'))]"));
    }

    public void backToHomePage(){
        if (driverTools.waitForElementToAppear(BACK_BUTTON_LANDINGPAGE).isPresent())
            driverTools.click(BACK_BUTTON_LANDINGPAGE);
        if (driverTools.isElementDisplayed(BACK_BUTTON_SRP))
            driverTools.click(BACK_BUTTON_SRP);
    }

    @Override
    public String getExploreText() {
        if (driverTools.isElementDisplayed(LOCATION_TEXT_NEW)) {
            Optional<WebElement> locationElement = driverTools.waitForElementToPresence(LOCATION_TEXT);
            Optional<WebElement> locationOffElement = driverTools.waitForElementToPresence(LOCATION_TEXT_WHEN_OFF);

            if (locationElement.isPresent() && driverTools.isElementDisplayed(LOCATION_TEXT)) {
                String location = driverTools.getText(locationElement.get());
                logger.log("location text -> " + location);
                return location;
            } else if (locationOffElement.isPresent() && driverTools.isElementDisplayed(LOCATION_TEXT_WHEN_OFF)) {
                String location = driverTools.getText(locationOffElement.get());
                logger.log("location text when off -> " + location);
                return location;
            } else {
                throw new RuntimeException("Location text not found");
            }
        }else{
            WebElement webElement = driverTools.waitForElementToBeClickable(EXPLORE_TEXT_LANDINGPAGE).orElseThrow();
            return driverTools.getText(webElement);
        }
    }

    @Override
    public void choosePopularDestination() {
        //FIXME: Pending implementation
    }

    @Override
    public void clickEnableLocation() {
        if (driverTools.isElementDisplayed(ENABLE_LOCATION_BUTTON_LANDINGPAGE_NEW)) {
            driverTools.click(ENABLE_LOCATION_BUTTON_LANDINGPAGE_NEW);
            driverTools.click(ALLOW_ACCESS_BUTTON_LANDINGPAGE);
            driverTools.click(PERMISSION_TYPE_ALLOW_ONCE);
        }
    }

    @Override
    public void clickSeeAllButton() {
        WebElement seeAllLandingPageButton = driverTools.swipeToElement(SEE_ALL_BUTTON_LANDINGPAGE, Swipe.UP,10).orElseThrow();
        driverTools.click(seeAllLandingPageButton);
    }

    @Override
    public boolean isLocationOff() {
       return driverTools.waitForElementToPresence(ENABLE_LOCATION_BUTTON_LANDINGPAGE_NEW,4).isPresent();
    }

    @Override
    public String getLocationText(){
        driverTools.swipeUp(2);
        driverTools.waitForElementToAppear(LOCATION_TEXT);
        return driverTools.getText(LOCATION_TEXT).orElseThrow();
    }

    @Override
    public void clickLocationText() {
        driverTools.click(LOCATION_TEXT);
    }

    @Override
    public String getRandomLocationTag() {
        driverTools.swipeToElement(LOCATION_TAGS, Swipe.UP, 6).orElseThrow();
        List<WebElement> locationTagDisplayedList = driverTools.elements(LOCATION_TAGS).orElseThrow();
        int randomIndex = generateRandomInt(0, locationTagDisplayedList.size() - 1);
        WebElement randomElement = locationTagDisplayedList.get(randomIndex);
        String chipText = driverTools.getText(randomElement);

        driverTools.click(randomElement);

        return chipText;
    }


    @Override
    public void clickSeeAllButtonLocationTags() {
        WebElement locationTagsSeeAll = driverTools.swipeToElement(SEE_ALL_BUTTON_CTA_WITH_LOCATION_TAG, Swipe.UP,3).orElseThrow();
        driverTools.click(locationTagsSeeAll);
    }

    @Override
    public Boolean isProductCardVisible(){
        driverTools.waitForElementToAppear(CTA_SECTION_WITH_LOCATION_TAG);
        driverTools.swipeToElement(CTA_SECTION_WITH_LOCATION_TAG, Swipe.UP, 5);
        return driverTools.swipeToElement(CTA_SECTION_PRODUCT_CARD, Swipe.UP, 5).orElseThrow().isDisplayed();
    }

    private Locator getHeadingLocator(String sectionName) {
        return new Locator("HEADING_LOCATOR", AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and contains(@text, '"+sectionName+"')]"));
    }

    @Override
    public void findSpecificSection(String sectionName) {
        driverTools.swipeToElement(SPECIAL_OFFER, Swipe.UP, 2);
        driverTools.click(SPECIAL_OFFER);
        driverTools.waitForElementToPresence(getHeadingLocator(sectionName));
        driverTools.swipeToElement(getHeadingLocator(sectionName), Swipe.UP, 20);
        logger.log("Found section '" + sectionName + "' "+ driverTools.isElementDisplayed(getHeadingLocator(sectionName)));
    }

    private Locator getChipsLocator(String chipsName) {
        return new Locator("CHIPS_LOCATOR", AppiumBy.xpath("//*[contains(@resource-id,'tds_chips_text') and contains(@text, '"+chipsName+"')]"));
    }

    @Override
    public void clickChips(String chipsName) {
        if(chipsName != null) {
            driverTools.swipeToElement(getChipsLocator(chipsName), Swipe.UP, 3);
            driverTools.click(getChipsLocator(chipsName));
        }
    }

    private Locator getReloadLocator(String sectionName) {
        if (!sectionName.contains("/"))
            return new Locator("RELOAD_SECTION", AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and contains(@text, '" + sectionName + "')]//parent::*//following-sibling::*[contains(@resource-id,'error_view')]"));
        else {
            String[] multilangTexts= sectionName.split("/");
            Assertion.assertThat("There is valid texts for both languages", multilangTexts.length, is(2));
            return new Locator("RELOAD_SECTION", AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and (contains(@text, '" + multilangTexts[0] + "') or contains(@text, '"+multilangTexts[1]+"'))]//parent::*//following-sibling::*[contains(@resource-id,'error_view')]"));
        }
    }

    @Override
    public boolean isReloadSectionVisible(String sectionName) {
        driverTools.swipeUp();
        driverTools.click(getReloadLocator(sectionName));
        return driverTools.isElementDisplayed(getReloadLocator(sectionName));
    }

    @Override
    public boolean getSectionName(String sectionName , String productName) {
        driverTools.swipeToElement(SPECIAL_OFFER, Swipe.UP, 2);
        driverTools.clickElementByCoordinate(SPECIAL_OFFER);
        Optional<WebElement> elementDisplayed  =driverTools.swipeToElement(SectionNameLocator(sectionName), Swipe.UP, 30);
        Optional<WebElement> isProductDisplayed  =driverTools.swipeToElement(SectionNameLocator(productName), Swipe.UP, 2);
        return driverTools.isElementDisplayed(elementDisplayed.orElseThrow()) && driverTools.isElementDisplayed(isProductDisplayed.orElseThrow());
    }

    @Override
    public boolean verifyRedirectionCTA(String productName) {
        driverTools.click(SectionNameLocator(productName));
        boolean productDisappeared = driverTools.waitForElementToDisappear(SectionNameLocator(productName), 5);
        if(productDisappeared){
            Optional<WebElement> firstCardElement = driverTools.waitForElementToPresence(FIRST_CARD_TTD);
            Optional<WebElement> destinationNameElement = firstCardElement.isPresent() ? Optional.empty() : driverTools.waitForElementToPresence(TTDDestinationPageAndroid.NAME_DESTINATIONPAGE);
            driverTools.switchToWebView();
            Optional<WebElement> productNameElement = firstCardElement.isPresent() || destinationNameElement.isPresent() ? Optional.empty() :  driverTools.waitForElementToPresence(TtdProductDetailPageWeb.PRODUCT_NAME_WEBVIEW);
            driverTools.switchToNative();

            return firstCardElement.isPresent() || productNameElement.isPresent() || destinationNameElement.isPresent();
        }
        return false;
    }

    @Override
    public boolean verifyDisplayedCurrency(String currency) {
        for(int i = 0; i < 3; i++) {
            driverTools.swipeUp();
            driverTools.swipeToElement(PRICE_TEXTS, Swipe.UP, 5);
            List<String> priceTexts = driverTools.getTexts(PRICE_TEXTS);

            for(String priceText : priceTexts) {
                if(priceText.matches(".*\\d.*")) {
                    if(priceText.toLowerCase().contains(currency.toLowerCase())) {
                        logger.log("Passed -> "+ priceText);
                    } else {
                        logger.fail("Fail -> "+ priceText);
                        return false;
                    }
                }
            }
        }

        return true;
    }

    @Override
    public void goToSection(String cardTitle) {
        HomePageAndroid<T> homePage = new HomePageAndroid<>(session);
        driverTools.swipeByDistanceToElement(homePage.getCardLocator(cardTitle), SCREEN,Swipe.UP,0.6,50).orElseThrow();
        driverTools.swipeByDistance(SCREEN,Swipe.UP,0.05);
    }

    @Override
    public String getCampaignProduct(String productName) {
        HomePageAndroid<T> homePage = new HomePageAndroid<>(session);
        Locator campaignLabel = homePage.getCampaignLabelProduct(productName);
        driverTools.swipeToElement(campaignLabel, Swipe.UP, 3);
        return driverTools.getText(campaignLabel).orElseThrow();
    }

    @Override
    public String findPrice(PriceType priceType, String currency) {
        String price = "";
        int loop = 0;
        while (price.isEmpty() && loop < 10) {
            driverTools.swipeUp(2);
            if (driverTools.isElementDisplayed(PRICE_TEXTS)) {
                List<WebElement> priceItems = driverTools.elements(PRICE_TEXTS).orElseThrow();
                for (WebElement priceItem : priceItems) {
                    String priceNumber = driverTools.getText(priceItem).split(" ")[1];
                    price = getPrice(priceNumber, priceType);
                    if (!price.isEmpty()) break;
                }
            }
            loop++;
        }

        return price;
    }

    @Override
    public void swipeToTop() {
        driverTools.swipeToElement(TOP_PRODUCT_CATEGORY, Swipe.DOWN, 10);
    }

    @Override
    public void goToProductCard(TTDLandingPagePOJO.Card productCard) {
        findSpecificSection(productCard.pageModuleTitle);
        driverTools.swipeUp();

        if(productCard.chipsName != null) {
            clickPageModuleChip(productCard.pageModuleTitle, productCard.chipsName);
        }

    }

    @Override
    public boolean isProductCardPriceVisible(TTDLandingPagePOJO.Card productCard, String price) {
        Assertion.assertThat("Price for product card '" + productCard.productName + "' is " + price, getProductCardPriceText(productCard).split(" ")[1], is(price));
        return true;
    }

    @Override
    public String getProductCardPriceText(TTDLandingPagePOJO.Card productCard) {
        driverTools.swipeToElement(getProductCardTitleLocator(productCard.pageModuleTitle, productCard.productName), getProductCardSliderLocator(productCard.pageModuleTitle), Swipe.LEFT, 7).orElseThrow();
        driverTools.swipeDown();
        WebElement element = driverTools.swipeToElement(getProductCardPriceTextLocator(productCard.pageModuleTitle, productCard.productName), Swipe.UP, 2).orElseThrow();

        return driverTools.getText(element);
    }

    @Override
    public String getFirstProductNameRecentlyViewed() {
        driverTools.swipeToElement(FIRST_RECENTLY_VIEWED_PRODUCT_NAME, RECENTLY_VIEWED_CONTAINER, Swipe.RIGHT, 3);
        return driverTools.getText(FIRST_RECENTLY_VIEWED_PRODUCT_NAME).orElseThrow();
    }

    @Override
    public void removeFirstRecentlyViewedProduct() {
        driverTools.swipeToElement(FIRST_RECENTLY_VIEWED_CROSSED_BUTTON, RECENTLY_VIEWED_CONTAINER, Swipe.RIGHT, 3);
        driverTools.click(FIRST_RECENTLY_VIEWED_CROSSED_BUTTON);
    }

    @Override
    public void scrollUntilSRP() {
        driverTools.swipeToElement(SRP_APP_BAR, Swipe.UP, 25);
    }

    @Override
    public Boolean scrollUpUntilFloatingButtonShown() {
        driverTools.swipeToElement(FLOATING_BUTTON_ICON, Swipe.UP_LONG, 10);
        return driverTools.isElementDisplayed(FLOATING_BUTTON_ICON);
    }

    @Override
    public void clickFloatingButton() {
        driverTools.click(FLOATING_BUTTON_ICON);
    }

    @Override
    public String getFirstGroupMenuChipText() {
        driverTools.waitForElementsToAppear(FIRST_GROUP_MENU_CHIP, 20);
        return driverTools.getText(FIRST_GROUP_MENU_CHIP).orElseThrow();
    }

    @Override
    public void openGroupMenus(String groupMenuTitle) {
        Locator groupMenuChipLocator = getGroupMenuChipLocatorByText(groupMenuTitle);
        driverTools.click(groupMenuChipLocator);
    }

    @Override
    public void clickMenuByIndex(int index) {
        Locator menuIconLocator = getMenuIconLocatorByIndex(index);
        driverTools.click(menuIconLocator);
    }

    @Override
    public void clickBottomsheetCrossButton() {
        driverTools.waitForElementsToAppear(BOTTOMSHEET_CROSS);
        driverTools.click(BOTTOMSHEET_CROSS);
    }

    @Override
    public Boolean verifySeeAllGroupMenu(String groupMenuType) {
        return switch (groupMenuType) {
            case "category" -> verifyAllActivityGroupMenu();
            case "destination" -> verifyAllDestinationGroupMenu();
            default -> null;
        };
    }

    @Override
    public void clickMenuByName(String name) {
        Locator menuTextLocator = getMenuIconLocatorByName(name);
        driverTools.clickElementByCoordinate(menuTextLocator);
    }

    private Locator getMenuIconLocatorByName(String name) {
        return new Locator("MENU_ICON_" + name.toUpperCase(), AppiumBy.xpath("//*[contains(@resource-id,'tv_menu') and @text='" + name + "']"));
    }

    @Override
    public void clickDestinationMenuByName(String name) {
        Locator menuTextLocator = getDestinationMenuIconLocatorByName(name);
        driverTools.swipeLeftUntilFound(DESTINATION_CONTAINER, menuTextLocator, 10);
        driverTools.clickElementByCoordinate(menuTextLocator);
    }

    private Locator getDestinationMenuIconLocatorByName(String name) {
        return new Locator("DESTINATION_MENU_ICON_" + name.toUpperCase(), AppiumBy.xpath("//*[@class='android.view.ViewGroup' and ./*[@text='" + name + "']]"));
    }

    private Boolean verifyAllDestinationGroupMenu() {
        driverTools.waitForElementsToAppear(ALL_LOCATION_CHIPS_BUTTON);
        boolean isLocationChipsDisplayed = driverTools.isElementDisplayed(ALL_LOCATION_CHIPS_BUTTON);
        boolean isCloseBackButtonDisplayed = driverTools.isElementDisplayed(BOTTOMSHEET_CROSS) || driverTools.isElementDisplayed(BACK_BUTTON);
        boolean isSearchBarDisplayed = driverTools.isElementDisplayed(SEARCHBAR);
        boolean isPopularDestinationIndonesiaSectionDisplayed = driverTools.isElementDisplayed(INDONESIA_POPULAR_DESTINATION);
        boolean isPopularDestinationWorldSectionDisplayed = driverTools.isElementDisplayed(WORLD_POPULAR_DESTINATION);
        boolean isDestinationSelectionSectionDisplayed = driverTools.isElementDisplayed(DESTINATION_LIST_TITLE);

        return isLocationChipsDisplayed
                && isCloseBackButtonDisplayed
                && isSearchBarDisplayed
                && isPopularDestinationIndonesiaSectionDisplayed
                && isPopularDestinationWorldSectionDisplayed
                && isDestinationSelectionSectionDisplayed;
    }

    private Boolean verifyAllActivityGroupMenu() {
        driverTools.waitForElementsToAppear(FIRST_BOTTOMSHEET_SUB_CATEGORY_CHIP);
        boolean isCrossButtonDisplayed = driverTools.isElementDisplayed(BOTTOMSHEET_CROSS);
        String bottomsheetTitleText = driverTools.getText(BOTTOMSHEET_TITLE_TEXT).orElseThrow();
        int categoryTitlePresentCount = driverTools.elements(BOTTOMSHEET_CATEGORY_TITLE).get().size();
        int categoryChevronPresentCount = driverTools.elements(BOTTOMSHEET_CATEGORY_CHEVRON).get().size();
        boolean isSubCategoryChipDisplayed = driverTools.isElementDisplayed(FIRST_BOTTOMSHEET_SUB_CATEGORY_CHIP);

        return isCrossButtonDisplayed
                && (bottomsheetTitleText.equals("All Categories") || bottomsheetTitleText.equals("Semua Kategori"))
                && categoryTitlePresentCount == categoryChevronPresentCount
                && isSubCategoryChipDisplayed;
    }

    private Locator getMenuIconLocatorByIndex(int index) {
        return new Locator("MENU_ICON_" + index, AppiumBy.xpath("(//*[contains(@resource-id, 'tv_menu')])['" + index + "']"));
    }

    private Locator getGroupMenuChipLocatorByText(String groupMenuTitle) {
        switch (groupMenuTitle.toLowerCase()) {
            case "activities":
                return new Locator("GROUP_MENU_CHIP_ACTIVITIES", AppiumBy.xpath("//*[@class='android.view.ViewGroup' and ./*[contains(@text,'activities')] or contains(@text,'aktivitas')]"));
            case "destination":
                return new Locator("GROUP_MENU_CHIP_DESTINATION", AppiumBy.xpath("//*[contains(@resource-id, 'tv_explore_destination')]"));
            case "attractions":
                return new Locator("GROUP_MENU_CHIP_ATTRACTION", AppiumBy.xpath("(//*[@class='android.view.ViewGroup' and ./*[contains(@text,'Attraction')] or contains(@text,'Atraksi')])[1]"));
            default:
                logger.fail("Group menu '"+ groupMenuTitle +"' is not yet supported");
        }

        return null;
    }

    private Locator getProductCardSliderLocator(String pageModuleTitle) {
        return new Locator("PRODUCT_CARD_TITLE", AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and @text='"+pageModuleTitle+"']//following-sibling::*[contains(@resource-id,'recycler_view')]"));
    }

    private Locator getProductCardPriceTextLocator(String pageModuleTitle, String productName) {
        return new Locator("PRODUCT_CARD_PRICE", AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and @text='"+pageModuleTitle+"']//following::*[contains(@resource-id,'tv_title') and contains(@text,'"+productName+"')]//following-sibling::*[contains(@resource-id,'price_view')]//*[contains(@resource-id,'price_module_final_price')]"));
    }

    private Locator getProductCardTitleLocator(String pageModuleTitle, String productName) {
        return new Locator("PRODUCT_CARD_TITLE", AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and @text='"+pageModuleTitle+"']//following::*[contains(@resource-id,'tv_title') and contains(@text,'"+productName+"')]"));
    }

    private void clickPageModuleChip(String pageModuleTitle, String chipsName) {
        WebElement element = driverTools.swipeToElement(getChipsLocatorByPageModuleTitle(pageModuleTitle, chipsName), getChipsGroupContainerLocatorByPageModuleTitle(pageModuleTitle, chipsName), Swipe.LEFT, 5).orElseThrow();
        driverTools.click(element);
    }

    private Locator getChipsGroupContainerLocatorByPageModuleTitle(String pageModuleTitle, String chipsName) {
        return new Locator("CHIPS_GROUP", AppiumBy.xpath("//*[contains(@resource-id,'constraint_root') and .//*[contains(@resource-id,'tv_title') and @text=\""+pageModuleTitle+"\"]]//*[contains(@resource-id,'chip_group')]"));
    }

    private Locator getChipsLocatorByPageModuleTitle(String pageModuleTitle, String chipsName) {
        return new Locator("CHIPS", AppiumBy.xpath("//*[contains(@resource-id,'constraint_root') and .//*[contains(@resource-id,'tv_title') and @text=\""+pageModuleTitle+"\"]]//*[contains(@resource-id,'tds_chips_text') and contains(@text,'"+ chipsName+"')]"));
    }

    private Locator SectionNameLocator(String labelType) {
        return new Locator(labelType, AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and contains(@text,'"+ labelType+"')]"));
    }

    @Override
    public void openProductWithDiscount() {
        WebElement crossedPrice = driverTools.swipeToElement(CROSSED_PRICE, Swipe.UP, 5).orElseThrow();
        driverTools.click(crossedPrice);
    }

    @Override
    public boolean verifyPromoSection() {
        driverTools.swipeToElement(PROMO_SECTION, Swipe.UP, 2);
        boolean isPromoSectionDisplayed = driverTools.isElementDisplayed(PROMO_SECTION);
        boolean isPromoSectionClaimBtnDisplayed = driverTools.isElementDisplayed(PROMO_SECTION_CLAIM_BTN);
        logger.log("Promo Section is displayed: " + isPromoSectionDisplayed);
        logger.log("Promo Section Claim Button is displayed: " + isPromoSectionClaimBtnDisplayed);
        return isPromoSectionDisplayed && isPromoSectionClaimBtnDisplayed;
    }

    @Override
    public String getSelectedLocation() {
        WebElement selectedLocation = driverTools.waitForElementToAppear(SELECTED_LOCATION).orElseThrow();
        return driverTools.getText(selectedLocation);
    }

    @Override
    public String getSelectedTab() {
        WebElement selectedTab = driverTools.waitForElementToPresence(SELECTED_LANDING_PAGE_TAB).orElseThrow();
        return driverTools.getText(selectedTab);
    }

    @Override
    public List<String> getAllTabs() {
        List<WebElement> tabs = driverTools.waitForElementsToAppear(LANDING_PAGE_TAB).orElseThrow();
        List<String> tabNames = new ArrayList<>();
        for (WebElement tab : tabs) {
            tabNames.add(driverTools.getText(tab));
        }
        return tabNames;
    }

    @Override
    public boolean checkDestinationUpdated(){
        driverTools.waitForElementToPresence(DESTINATION_DOT);
        boolean res = driverTools.isElementDisplayed(DESTINATION_DOT);
        if(res) driverTools.clickElementByCoordinate(DESTINATION_DOT);
        return res;
    }

    @Override
    public boolean isCurrentLocationOptionAppears(){
        driverTools.waitForElementToPresence(CURRENT_LOCATION_OPTION);
        return driverTools.isElementDisplayed(CURRENT_LOCATION_OPTION);
    }


    @Override
    public boolean verifyLocationChip() {
        return driverTools.isElementDisplayed(LOCATION_CHIP_SEARCHBAR);
    }

    @Override
    public String checkLocationChip() {
        driverTools.swipeToPresentedElement(LOCATION_CHIP_SEARCHBAR, Swipe.UP, 5);
        return driverTools.getText(LOCATION_CHIP_SEARCHBAR).orElseThrow();
    }

    @Override
    public void clickLocationChip() {
        driverTools.click(LOCATION_CHIP_SEARCHBAR);
    }

    @Override
    public void clickBack() {
        driverTools.click(BACK_BUTTON);
    }

    @Override
    public boolean checkCTA() {
        driverTools.swipeToElement(SEE_ALL_BUTTON_LANDINGPAGE, Swipe.UP, 10).orElseThrow();
        driverTools.click(SEE_ALL_BUTTON_LANDINGPAGE);

        TTDSearchResultPage<T> searchResultPage = TTDSearchResultPage.getInstance(session);
        return searchResultPage.isSearchResultPageShown();
    }

    @Override
    public void openSpecialOffers() {
        driverTools.swipeUp();
        driverTools.swipeToPresentedElement(SELECTED_TAB, Swipe.UP, 5);

        List<String> unselectedTabs = driverTools.getTexts(UNSELECTED_TAB);

        String tab = "";
        for (String tabs: unselectedTabs) {
            if (tabs.contains("Offers") || tabs.contains("Penawaran"))
                tab = tabs;
        }

        Assertion.assertThat("Offer tab exists", tab, CoreMatchers.is(not("")));

        Locator tabLocator = getUnselectedTab(tab);
        WebElement tabButton  = driverTools.element(tabLocator).orElseThrow();
        driverTools.clickElementByCoordinate(tabButton);
    }

    private Locator getReloadChipLocator(String text) {
        if (!text.contains("/"))
            return new Locator("RELOAD_CHIP", AppiumBy.xpath("//*[contains(@resource-id, 'tds_chips_text') and @text='"+ text +"']"));
        else {
            String[] multilangTexts= text.split("/");
            Assertion.assertThat("There is valid texts for both languages", multilangTexts.length, is(2));
            return new Locator("RELOAD_CHIP", AppiumBy.xpath("//*[contains(@resource-id, 'tds_chips_text') and (@text='"+ multilangTexts[0] +"' or @text='"+multilangTexts[1]+"')]"));
        }
    }

    @Override
    public void checkReloadButton(String chipText, String reloadSection) {
        Locator reloadChipLocator = getReloadChipLocator(chipText);

        driverTools.swipeToPresentedElement(reloadChipLocator, Swipe.UP, 10);
        driverTools.click(reloadChipLocator);

        driverTools.swipeUp();
        Locator reloadButton = getReloadLocator(reloadSection);
        Assertion.assertThat("Reload button appears", driverTools.isElementDisplayed(reloadButton), is(true));

        driverTools.click(reloadButton);
        Assertion.assertThat("Reload button reappears", driverTools.waitForElementToPresence(getReloadLocator(reloadSection)).isPresent(), is(true));
    }

    @Override
    public String getUIPrice(String productName, String currency){
        Locator priceLocator = getLocatorForPrice(productName, currency);
        WebElement priceElement = driverTools.swipeToElement(priceLocator, Swipe.UP, 30).orElseThrow();
        String priceText = driverTools.getText(priceElement);
        logger.log("Price Text: " + priceText);
        return priceText;
    }

    private Locator getLocatorForPrice(String productName, String currency) {
        return new Locator("PRODUCT_PRICE", AppiumBy.xpath("(//*[contains(@resource-id,'tv_product_name') and @text='"+productName+"']//following-sibling::*//*[contains(@text,'"+currency+"')])[last()]"));
    }
}