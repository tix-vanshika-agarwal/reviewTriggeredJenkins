package com.tiket.page.ttd.landingpage;

import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.PriceType;
import com.tiket.model.Session;
import com.tiket.page.home.HomePageIOS;
import com.tiket.page.ttd.destinationpage.TTDDestinationPageAndroid;
import com.tiket.page.ttd.searchresultpage.TTDSearchResultPage;
import com.tiket.pojo.ttd.TTDLandingPagePOJO;
import com.tiket.testbase.Assertion;
import com.tiket.web.page.ttd.TtdProductDetailPage.TtdProductDetailPageWeb;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.*;

import static org.hamcrest.CoreMatchers.*;

import static com.tiket.core.CoreFunctions.generateRandomInt;
import static com.tiket.core.CoreFunctions.getPrice;
import static com.tiket.page.ttd.explorepage.TTDExplorePageIOS.*;
import static com.tiket.page.ttd.searchresultpage.TTDSearchResultPageIOS.SEARCH_RESULT_PAGE_CONTAINER;
import static org.hamcrest.core.Is.is;

public class TTDLandingPageIOS<T extends WebDriver> extends TTDLandingPage<T> {

    private static final Locator SCREEN = new Locator("SCREEN", AppiumBy.xpath("(//XCUIElementTypeScrollView)[1]"));
    public static final Locator SEARCH_BAR_LANDINGPAGE = new Locator("SEARCH_BAR_LANDINGPAGE", AppiumBy.name("searchBoxView"));
    public static final Locator EXPLORE_ARROW = new Locator("EXPLORE_ARROW", AppiumBy.xpath("//*[@label='Explore Destinations' or @label='Jelajahi Destinasi']//following-sibling::*//*[@name='tds_ic_oval_chevron_right']"));
    public static final Locator BACK_BUTTON = new Locator("BACK_BUTTON", AppiumBy.xpath(
            "//*[(@name='tds_ic_back' or @name='tds ic back') and @visible='true']"));
    public static final Locator EXPLORE_TEXT_LANDINGPAGE = new Locator("EXPLORE_TEXT_LANDINGPAGE", AppiumBy.xpath("//XCUIElementTypeStaticText[@name='ttd_destination_name']"));
    public static final Locator THINGS_TO_DO_IN_TEXT = new Locator("THINGS_TO_DO_IN_TEXT", AppiumBy.accessibilityId("ttd_things_to_do_in"));
    public static final Locator ENABLE_LOCATION_BUTTON_LANDINGPAGE = new Locator("ENABLE_LOCATION_BUTTON_LANDINGPAGE", AppiumBy.id("enable_location_btn"));
    public static final Locator ALLOW_ONCE_PERMISSION_BUTTON = new Locator("ALLOW_ONCE_PERMISSION_BUTTON", AppiumBy.accessibilityId("Allow Once"));
    public static final Locator ALLOW_ACCESS_BUTTON = new Locator("ALLOW_ACCESS_BUTTON", AppiumBy.accessibilityId("buttonPrimary"));
    public static final Locator SEE_ALL_BUTTON_LANDINGPAGE = new Locator("SEE_ALL_BUTTON_LANDINGPAGE", AppiumBy.xpath("//*[@name='page_module_see_all_button' or @name='flash_sale_see_all']"));
    private static final Locator PRODUCT_CATEGORY = new Locator("PRODUCT_CATEGORY", AppiumBy.id("product_category_name"));
    private static final Locator VISIBLE_PRODUCT_CATEGORY = new Locator("VISIBLE_PRODUCT_CATEGORY", AppiumBy.xpath("//*[@name='product_category_name' and @visible='true']"));
    public static final Locator FIRST_COUNTRY_DESTINATION_NAME = new Locator("FIRST_COUNTRY_DESTINATION_NAME", AppiumBy.id("(//XCUIElementTypeStaticText[@name='ttd_country_name'])[1]"));
    public static final Locator SCROLLABLE_PRODUCT_CATEGORY_CONTAINER = new Locator("SCROLLABLE_PRODUCT_CATEGORY_CONTAINER", AppiumBy.xpath("//XCUIElementTypeTable//XCUIElementTypeCell[XCUIElementTypeStaticText[contains(@name,'product_category')]]/XCUIElementTypeOther[1]/*"));
    private static final Locator FIRST_PRODUCT_NAME_ON_SRP = new Locator("FIRST_PRODUCT_NAME_ON_SRP", AppiumBy.xpath("(//XCUIElementTypeStaticText[@name='product_title'])[1]"));
    public static final Locator CTA_SECTION_NON_CAMPAIGN_PRODUCT_CARD = new Locator("ADVENTURE_PRODUCT_CARD", AppiumBy.xpath("//XCUIElementTypeTable//XCUIElementTypeCell[not(XCUIElementTypeStaticText[@label=':']) and (XCUIElementTypeButton[@label='See All' or @label='See all' or @label='Lihat Semua' or @label='Lihat semua'])]//XCUIElementTypeImage[@name='bn_chips']"));
    private static final Locator LOCATION_TAGS_CTA_SECTION_NON_CAMPAIGN = new Locator("LOCATION_TAGS", AppiumBy.xpath("//XCUIElementTypeTable//XCUIElementTypeCell[not(XCUIElementTypeStaticText[@label=':']) and (XCUIElementTypeButton[@label='See All' or @label='See all' or @label='Lihat Semua' or @label='Lihat semua'])]//*[@name='bn_chips']//following-sibling::*[contains(@name,'(selected)')]"));
    public static final Locator SEE_ALL_BUTTON_CTA_NON_CAMPAIGN = new Locator("SEE_ALL_BUTTON_CTA", AppiumBy.xpath("//XCUIElementTypeTable//XCUIElementTypeCell[not(XCUIElementTypeStaticText[@label=':']) and (XCUIElementTypeButton[@label='See All' or @label='See all' or @label='Lihat Semua' or @label='Lihat semua'])]/XCUIElementTypeButton[@label='See All' or @label='See all' or @label='Lihat Semua' or @label='Lihat semua']"));
    public static final Locator CTA_SECTION_NON_CAMPAIGN = new Locator("CTA_SECTION", AppiumBy.xpath("//XCUIElementTypeTable//XCUIElementTypeCell[not(XCUIElementTypeStaticText[@label=':']) and (XCUIElementTypeButton[@label='See All' or @label='See all' or @label='Lihat Semua' or @label='Lihat semua'])]"));
    public static final Locator FIRST_SEARCH_RESULT = new Locator("FIRST_SEARCH_RESULT", AppiumBy.id("ttd_search_page_search_result_title"));
    private static final Locator RECENTLY_VIEWED_SECTION = new Locator("RECENTLY_VIEWED_SECTION", AppiumBy.xpath("//*[@label='Terakhir Dilihat' or @label='Recently Viewed']"));
    private static final Locator SEE_ALL_RECENTLY_VIEWED = new Locator("SEE_ALL_RECENTLY_VIEWED", AppiumBy.xpath("//*[@label='Terakhir Dilihat' or @label='Recently Viewed']//following-sibling::*[(@label='See all' or @label='Lihat semua') and @visible='true']"));
    private static final Locator RECENTLY_VIEWED_POPUP_ITEM = new Locator("RECENTLY_VIEWED_POPUP_ITEM", AppiumBy.xpath("//*[@label='Terakhir Dilihat' or @label='Recently Viewed']//parent::XCUIElementTypeOther//following::XCUIElementTypeOther//XCUIElementTypeTable//XCUIElementTypeCell//XCUIElementTypeStaticText[@visible='true' and not(contains(@label,' • '))]"));
    private static final Locator RECENTLY_VIEWED_POPUP_DELETE_BUTTON = new Locator("RECENTLY_VIEWED_POPUP_DELETE_BUTTON", AppiumBy.xpath("//*[@label='Terakhir Dilihat' or @label='Recently Viewed']//parent::XCUIElementTypeOther//following::XCUIElementTypeOther//XCUIElementTypeTable//XCUIElementTypeCell//XCUIElementTypeStaticText[@visible='true' and not(contains(@label,' • '))]//preceding-sibling::XCUIElementTypeButton"));
    private static final Locator RECENTLY_VIEWED_POPUP_CONTAINER = new Locator("RECENTLY_VIEWED_POPUP_CONTAINER", AppiumBy.xpath("//*[@label='Terakhir Dilihat' or @label='Recently Viewed']//parent::XCUIElementTypeOther//following-sibling::XCUIElementTypeOther//XCUIElementTypeTable"));
    public static final Locator PRODUCT_CARD_PRICE_TEXT = new Locator("PRODUCT_CARD_PRICE_TEXT", AppiumBy.name("mainPrice"));
    public static final Locator LOCATION_TEXT = new Locator("LOCATION_TEXT", AppiumBy.xpath("//XCUIElementTypeStaticText[@name='All locations' or @name='Semua lokasi' or @label='All locations' or @label='Semua lokasi'] | (//XCUIElementTypeButton/following-sibling::XCUIElementTypeOther/XCUIElementTypeStaticText)[1]"));
    private static final Locator CURRENT_LOCATION_OPTION = new Locator("CURRENT_LOCATION_OPTION", AppiumBy.xpath("//*[@type='XCUIElementTypeOther' and ./*[@name='See all activities in'] or ./*[@name='Lihat semua aktivitas di']]"));
    public static final Locator TOP_PRODUCT_CATEGORY = new Locator("TOP_PRODUCT_CATEGORY", AppiumBy.xpath("(//*[@name='product_category'])[1]"));
    private static final Locator FLOATING_UP_BUTTON = new Locator("FLOATING_UP_BUTTON", AppiumBy.id("tds_ic_chevron_up"));
    public static final Locator CROSSED_PRICE = new Locator("CROSSED_PRICE", AppiumBy.xpath("//*[@name='bn_chips_slash_price' and @visible='true']"));
    public static final Locator FIRST_RECENTLY_VIEWED_PRODUCT_NAME =new Locator("FIRST_RECENTLY_VIEWED_PRODUCT_NAME", AppiumBy.xpath("//XCUIElementTypeOther[@name=\"Recently Viewed\" or @name=\"Terakhir Dilihat\"]/following-sibling::XCUIElementTypeCell[1]/XCUIElementTypeStaticText[1]"));
    public static final Locator FIRST_RECENTLY_VIEWED_CROSS_BUTTON =new Locator("FIRST_RECENTLY_VIEWED_CROSS_BUTTON", AppiumBy.xpath("//XCUIElementTypeOther[@name=\"Recently Viewed\" or @name=\"Terakhir Dilihat\"]/following-sibling::XCUIElementTypeCell[1]/XCUIElementTypeButton[last()]"));
    public static final Locator LOCATION_EXPLORE_BUTTON = new Locator("LOCATION_EXPLORE_BUTTON", AppiumBy.id("enable_location_btn"));
    private static final Locator CONTINUE = new Locator("CONTINUE_LOCATION", AppiumBy.xpath("//XCUIElementTypeStaticText[@name='Continue' or @name='Lanjutkan']"));
    public static final Locator ALLOW_PERMISSION_BUTTON = new Locator("ALLOW_PERMISSION_BUTTON", AppiumBy.xpath("//XCUIElementTypeButton[@name='Allow Once']"));
    private static final Locator CONTINUE_LOCATION = new Locator("CONTINUE_LOCATION", AppiumBy.id("buttonPrimary"));
    private static final Locator FLOATING_BUTTON_ICON = new Locator("FLOATING_BUTTON_ICON", AppiumBy.xpath("//*[contains(@name,'tds_ic_todo')]/parent::*"));
    private static final Locator FIRST_GROUP_MENU_CHIP = new Locator("FIRST_GROUP_MENU_CHIP", AppiumBy.xpath("(//*[@name='product_category_name'])[10]"));
    private static final Locator BOTTOMSHEET_CROSS = new Locator("BOTTOMSHEET_CROSS", AppiumBy.id("ic cancel grey"));
    private static final Locator FIRST_BOTTOMSHEET_SUB_CATEGORY_CHIP = new Locator("FIRST_BOTTOMSHEET_SUB_CATEGORY_CHIP", AppiumBy.xpath("//XCUIElementTypeCollectionView/XCUIElementTypeCell[1]"));
    private static final Locator BOTTOMSHEET_TITLE_TEXT = new Locator("BOTTOMSHEET_TITLE_TEXT", AppiumBy.xpath("//*[contains(@name,'ic cancel grey')]/parent::*/parent::*/following-sibling::*[2]/XCUIElementTypeStaticText"));
    private static final Locator BOTTOMSHEET_CATEGORY_TITLE = new Locator("BOTTOMSHEET_CATEGORY_TITLE", AppiumBy.id("//XCUIElementTypeCollectionView/XCUIElementTypeOther//XCUIElementTypeStaticText"));
    private static final Locator BOTTOMSHEET_CATEGORY_CHEVRON = new Locator("BOTTOMSHEET_CATEGORY_CHEVRON", AppiumBy.id("//XCUIElementTypeCollectionView/XCUIElementTypeOther//*[@name='tds_ic_chevron_right']"));
    private static final Locator PRODUCT_CATEGORY_ICON = new Locator("PRODUCT_CATEGORY_ICON", AppiumBy.xpath("//XCUIElementTypeImage[contains(@name,'product_category')]"));
    private static final Locator SEE_ALL_MENU = new Locator("SEE_ALL_MENU", AppiumBy.xpath("//XCUIElementTypeStaticText[@name='product_category_name' and (contains(@label,'All') or contains(@label,'Semua'))]"));
    private static final Locator PROMO_SECTION_CLAIM_BTN = new Locator("PROMO_SECTION_CLAIM_BTN", AppiumBy.xpath("//XCUIElementTypeButton[@label='Claim' or @label='Klaim']"));
    private static final Locator PROMO_SECTION = new Locator("PROMO_SECTION", AppiumBy.xpath("//XCUIElementTypeStaticText[@label='Claim your log in benefits' or @label='Klaim benefit khusus log in']"));
    private static final Locator CLOSE_BTN = new Locator("CLOSE_BTN", AppiumBy.xpath("//*[@name='ic cancel grey']"));
    private static final Locator CLOSE_BTN_SRP = new Locator("CLOSE_BTN_SRP", AppiumBy.xpath("//*[@name='ttd_back_btn']"));
    private static final Locator DESTINATION_CONTAINER = new Locator("DESTINATION_CONTAINER", AppiumBy.xpath("//*[@type='XCUIElementTypeCollectionView' and ./*[@name='Vertical scroll bar, 3 pages']]"));
    private static final Locator DESTINATION_DOT = new Locator("DESTINATION_DOT", AppiumBy.xpath("//*[@name='tds_ic_oval_check']"));
    private static final Locator SPECIAL_OFFER = new Locator("SPECIAL_OFFER", AppiumBy.xpath("//*[contains(@label,'Special Offers') or contains(@label,'Penawaran Spesial')]"));
    private static final Locator SELECTED_LOCATION = new Locator("SELECTED_LOCATION", AppiumBy.xpath("//*[@name='tds_ic_oval_check']//preceding::XCUIElementTypeStaticText[1]"));
    private static final Locator LANDING_PAGE_TAB = new Locator("LANDING_PAGE_TAB", AppiumBy.iOSClassChain("**/*[`name CONTAINS 'textLabel'`]"));
    private static final Locator SELECTED_LANDING_PAGE_TAB = new Locator("SELECTED_LANDING_PAGE_TAB", AppiumBy.iOSClassChain("**/*[`name == 'textLabel (selected)'`]"));
    public static final Locator SELECTED_TAB = new Locator("SELECTED_TAB", AppiumBy.xpath("//*[@name='textLabel (selected)']"));
    public static final Locator UNSELECTED_TAB = new Locator("UNSELECTED_TAB", AppiumBy.xpath("//*[@name='textLabel']"));
    private static final Locator LOCATION_CHIP_SEARCHBAR = new Locator("LOCATION_CHIP_SEARCHBAR", AppiumBy.xpath("//*[@name='All locations' or @name='Semua lokasi']"));
    private static final Locator POPULAR_DESTINATION_BUTTON = new Locator("POPULAR_DESTINATION_BUTTON", AppiumBy.xpath("//XCUIElementTypeTable/*[translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')='popular destinations' or translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')='destinasi populer']"));
    private static final Locator TOTAL_ACTIVITES_DESTINATIONS = new Locator("TOTAL_ACTIVITES_DESTINATIONS", AppiumBy.name("ttd_popular_destination_card_activities_count"));

    public TTDLandingPageIOS(Session<T> session) {
        super(session);
    }

    private static Locator productCardPrice(String currency) {
        return new Locator("PRODUCT_CARD_PRICE_" + currency.toUpperCase(), AppiumBy.iOSClassChain("**/XCUIElementTypeStaticText[`label CONTAINS '"+currency+"'`]"));
    }

    private static Locator getUnselectedTab(String tabName) {
        return new Locator("SPECIFIC_UNSELECTED_TAB", AppiumBy.xpath("//*[@name='textLabel' and @value='"+tabName+"']"));
    };

    @Override
    public void checkTabbing() {
        driverTools.swipeUp();
        driverTools.swipeToPresentedElement(SELECTED_TAB, Swipe.UP, 5);

        String selectedTab = driverTools.getText(SELECTED_TAB).orElseThrow();
        Assertion.assertThat("Default elected tab is 'For You' or 'Untukmu'", selectedTab, anyOf(is("For You"), is("Untukmu")));

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
    public void goToSection(String cardType) {
        HomePageIOS<T> homePage = new HomePageIOS<>(session);
        driverTools.swipeByDistanceToElement(homePage.getCardLocator(cardType), SCREEN,Swipe.UP,0.6,40).orElseThrow();
        driverTools.swipeByDistance(SCREEN,Swipe.UP,0.05);
    }

    private Locator getCampaignLabelProduct(String productName) {
        if (productName.contains("/")) {
            String productEn = productName.split("/")[0];
            String productId = productName.split("/")[1];
            return new Locator("CAMPAIGN_LABEL_PRODUCT", AppiumBy.xpath("//*[(contains(@label,'"+productEn+"') or contains(@label,'"+productId+"')) and @name='product_name']//preceding-sibling::*[@name='page_module_product_card_campaign_label']"));
        } else {
            return new Locator("CAMPAIGN_LABEL_PRODUCT", AppiumBy.xpath("//*[contains(@label,'"+productName+"') and @name='product_name']//preceding-sibling::*[@name='page_module_product_card_campaign_label']"));
        }
    }

    @Override
    public String getCampaignProduct(String productName) {
        Locator campaignLabel = getCampaignLabelProduct(productName);
        driverTools.swipeToElement(campaignLabel, Swipe.UP, 3);
        return driverTools.getText(campaignLabel).orElseThrow();
    }

    @Override
    public String findPrice(PriceType priceType, String currency) {
        String price = "";
        int loop = 0;
        while (price.isEmpty() && loop < 10) {
            driverTools.swipeUp(2);
            if (driverTools.waitForElementToPresence(productCardPrice(currency)).isPresent()) {
                for (WebElement priceItem : driverTools.elements(productCardPrice(currency)).orElseThrow()) {
                    String priceNumberText = driverTools.getText(priceItem);
                    if(!(priceNumberText.equalsIgnoreCase(""))) {
                        String priceNumber = priceNumberText.split(" ")[1];
                        price = getPrice(priceNumber, priceType);
                        if (!price.isEmpty()) break;
                    }
                }
            }
            loop++;
        }

        return price;
    }

    @Override
    public void clickSeeAllRecentlyViewed() {
        driverTools.clickElementByCoordinate(SEE_ALL_RECENTLY_VIEWED);
    }

    @Override
    public int getTotalRecentlyViewedItems() {
        driverTools.waitForElementToPresence(RECENTLY_VIEWED_POPUP_ITEM);
        List<WebElement> itemList = driverTools.elements(RECENTLY_VIEWED_POPUP_ITEM).orElseThrow();
        int totalItems = itemList.size();
        logger.log("total items -> " + totalItems);

        String lastItemName = driverTools.getText(itemList.get(totalItems-1));
        driverTools.swipeByDistance(RECENTLY_VIEWED_POPUP_CONTAINER, Swipe.UP, 0.9);
        driverTools.waitForElementToPresence(nextRecentlyViewedPopupItem(lastItemName));

        List<WebElement> nextItemList = driverTools.elements(nextRecentlyViewedPopupItem(lastItemName)).orElseThrow();
        int nextTotalItems = nextItemList.size();
        logger.log("next total items -> " + nextTotalItems);

        return totalItems + nextTotalItems;
    }

    private Locator nextRecentlyViewedPopupItem(String firstItemName) {
        return new Locator("NEXT_RECENTLY_VIEWED_POPUP_ITEM_" + firstItemName.toUpperCase(), AppiumBy.xpath("//*[@label='Terakhir Dilihat' or @label='Recently Viewed']//parent::XCUIElementTypeOther//following-sibling::XCUIElementTypeOther//XCUIElementTypeTable//XCUIElementTypeCell//XCUIElementTypeStaticText[@visible='true' and @label='" + firstItemName + "']//following::XCUIElementTypeCell[@visible='true']"));
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
        driverTools.clickElementByCoordinate(itemName.get(index));
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
        driverTools.waitForElementToPresence(RECENTLY_VIEWED_POPUP_ITEM);
        return driverTools.getText(RECENTLY_VIEWED_POPUP_ITEM).orElseThrow();
    }

    @Override
    public boolean isRecentlyViewedDisplayed() {
        return driverTools.waitForElementToAppear(RECENTLY_VIEWED_SECTION).isPresent();
    }

    @Override
    public boolean isLandingPageValid() {
        return driverTools.isElementDisplayed(SEARCH_BAR_LANDINGPAGE);
    }

    @Override
    public void clickSearchBar() {
        WebElement searchBar = driverTools.waitForElementToPresence(SEARCH_BAR_LANDINGPAGE).orElseThrow();
        try {
            driverTools.click(searchBar);
        } catch (Exception e) {
            logger.log("Failed to click search bar normally, trying by coordinate");
            driverTools.clickElementByCoordinate(SEARCH_BAR_LANDINGPAGE);
        }
    }

    @Override
    public void clickSearchBarSRP() {
        /// implementation pending
    }

    @Override
    public void clickExploreArrow() {
        WebElement explore = driverTools.waitForElementToPresence(EXPLORE_ARROW, 20).orElseThrow();
        driverTools.clickElementByCoordinate(explore);

        if (driverTools.isElementDisplayed(FIRST_COUNTRY_DESTINATION_NAME)) {
            driverTools.clickElementByCoordinate(explore);
        }
    }

    @Override
    public void backToHomePage() {
        driverTools.switchToNative();
        driverTools.waitForElementToPresence(BACK_BUTTON, 10)
                .ifPresentOrElse(
                        back -> driverTools.clickElementByCoordinate(back),
                        () -> driverTools.back());
        driverTools.waitForElementToDisappear(BACK_BUTTON, 5);
    }

    private Locator productCategoryElement(String productCategory, String pageType) {
        productCategory = getProductCategory(productCategory);

        String productCategoryID = productCategory.split("/")[1];
        String productCategoryENG = productCategory.split("/")[0];

        if(pageType.equalsIgnoreCase("bottomsheet")){
            return new Locator("PRODUCT_CATEGORY : " + productCategory, AppiumBy.xpath("//XCUIElementTypeImage[@name='tds_ic_chevron_right']/parent::*/parent::*/XCUIElementTypeStaticText[contains(@label,'" + productCategoryID + "') or contains(@label,'" + productCategoryENG + "')]"));
        } else if(pageType.equalsIgnoreCase("bottomsheet-container")){
            return new Locator("PRODUCT_CATEGORY_CONTAINER : " + productCategory, AppiumBy.xpath("//XCUIElementTypeImage[@name='tds_ic_chevron_right']/parent::*/parent::*[./XCUIElementTypeStaticText[contains(@label,'" + productCategoryID + "') or contains(@label,'" + productCategoryENG + "')]]/parent::*"));
        } else {
            return new Locator("PRODUCT_CATEGORY : " + productCategory, AppiumBy.iOSClassChain("**/XCUIElementTypeStaticText[`name == 'product_category_name' AND label =='" + productCategoryID + "' OR label =='" + productCategoryENG + "'`]"));
        }
    }

    @Override
    public String openProductByCategory(String productCategory) {
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
            Locator productCategoryTitleContainerLocator = productCategoryElement(productCategory, "bottomsheet-container");

            if(driverTools.waitForElementToAppear(productCategoryLocator).isEmpty()) {
                clickSeeAllMenus();

                driverTools.swipeToElement(productCategoryTitleContainerLocator, Swipe.UP, 10).orElseThrow();
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

    @Override
    public String getExploreText() {
        driverTools.waitForElementToPresence(EXPLORE_TEXT_LANDINGPAGE);
        return driverTools.getValueAttribute(EXPLORE_TEXT_LANDINGPAGE, "label");
    }

    @Override
    public void choosePopularDestination() {
        driverTools.waitForElementToPresence(POPULAR_DESTINATION_BUTTON);
        driverTools.click(POPULAR_DESTINATION_BUTTON);

        logger.log("choose popular destination with the lowest activities amount");
        List<WebElement> totalActivitiesDestinations = driverTools.waitForPresenceElementsList(TOTAL_ACTIVITES_DESTINATIONS).orElseThrow();
        int lowestActivitiesAmount = Integer.MAX_VALUE;
        int index = 0;
        for (int i = 0; i < totalActivitiesDestinations.size(); i++) {
            WebElement totalActivitiesDestination = totalActivitiesDestinations.get(i);
            String text = driverTools.getText(totalActivitiesDestination);
            if (text.contains("Activities") || text.contains("Aktivitas")) {
                int activitiesAmount = Integer.parseInt(text.replaceAll("[^0-9]", ""));
                if (activitiesAmount < lowestActivitiesAmount) {
                    lowestActivitiesAmount = activitiesAmount;
                    index = i;
                }
            }
        }
        logger.log("Selected destination has " + lowestActivitiesAmount + " activities at index: " + index);
        driverTools.click(totalActivitiesDestinations.get(index));
    }

    @Override
    public void clickEnableLocation() { //Adjust after final New UI
        String locationText = getLocationText();
        if(locationText.contains("nearby") || locationText.contains("terdekat")) {
            driverTools.clickElementByCoordinate(LOCATION_TEXT);
            if(driverTools.waitForElementToPresence(ALLOW_ACCESS_BUTTON).isPresent()){
                driverTools.clickElementByCoordinate(ALLOW_ACCESS_BUTTON);
                driverTools.waitForElementToPresence(ALLOW_ONCE_PERMISSION_BUTTON).orElseThrow();
                driverTools.clickElementByCoordinate(ALLOW_ONCE_PERMISSION_BUTTON);
            }
            else{
                driverTools.clickElementByCoordinate(CONTINUE_LOCATION);
                driverTools.clickElementByCoordinate(ALLOW_ONCE_PERMISSION_BUTTON);
            }
        }
    }

    @Override
    public void clickSeeAllButton() {
        WebElement seeAllLandingPageButton = driverTools.swipeToElement(SEE_ALL_BUTTON_LANDINGPAGE, Swipe.UP, 3).orElseThrow();
        driverTools.click(seeAllLandingPageButton);
    }

    @Override
    public boolean isLocationOff() {
        String locationText = getLocationText();
        return locationText.contains("nearby") || locationText.contains("terdekat");
    }

    @Override
    public String getLocationText() {
        driverTools.swipeToElement(LOCATION_TEXT, Swipe.UP, 5);
        driverTools.waitForElementToPresence(LOCATION_TEXT);
        return driverTools.getText(LOCATION_TEXT).orElseThrow();
    }

    @Override
    public void clickLocationText() {
        driverTools.clickElementByCoordinate(LOCATION_TEXT);
    }

    @Override
    public String getRandomLocationTag() {
        WebElement locationTags = driverTools.swipeToElement(LOCATION_TAGS_CTA_SECTION_NON_CAMPAIGN, Swipe.UP, 6).orElseThrow();
        return driverTools.getText(locationTags);
    }

    @Override
    public void clickSeeAllButtonLocationTags() {
        WebElement locationTagsSeeAll = driverTools.swipeToElement(SEE_ALL_BUTTON_CTA_NON_CAMPAIGN, Swipe.UP, 3).orElseThrow();
        driverTools.click(locationTagsSeeAll);
    }

    @Override
    public Boolean isProductCardVisible() {
        driverTools.swipeToElement(CTA_SECTION_NON_CAMPAIGN, Swipe.UP, 5);
        return driverTools.isElementDisplayed(CTA_SECTION_NON_CAMPAIGN_PRODUCT_CARD);
    }

    private Locator getHeadingAndChipsLocator(String sectionName) {
        return new Locator("HEADING/CHIPS_LOCATOR", AppiumBy.xpath("//XCUIElementTypeStaticText[@label='" + sectionName + "' and @visible='true']"));
    }

    private Locator getHeadingAndChipsLocator(String sectionName, String productName) {
        return new Locator("HEADING/CHIPS_LOCATOR", AppiumBy.xpath("//XCUIElementTypeCell[./XCUIElementTypeStaticText[@name='page_module_title' and @label='" + sectionName + "']]//XCUIElementTypeStaticText[@name='product_name' and @label='" + productName + "']"));
    }

    @Override
    public void findSpecificSection(String sectionName) {
        driverTools.swipeToElement(SPECIAL_OFFER, Swipe.UP, 2);
        driverTools.clickElementByCoordinate(SPECIAL_OFFER);
        driverTools.swipeToElement(getHeadingAndChipsLocator(sectionName), Swipe.UP, 30);
        logger.log("Found section '" + sectionName + "' "+ driverTools.isElementDisplayed(getHeadingAndChipsLocator(sectionName)));
    }

    @Override
    public void clickChips(String chipsName) {
        if (chipsName != null) {
            driverTools.swipeToElement(getHeadingAndChipsLocator(chipsName), Swipe.UP, 3);
            driverTools.clickElementByCoordinate(getHeadingAndChipsLocator(chipsName));
        }
    }

    private Locator getReloadLocator(String sectionName) {
        if (!sectionName.contains("/"))
            return new Locator("RELOAD_SECTION", AppiumBy.xpath("//*[contains(@label, '" + sectionName + "')]/parent::*//XCUIElementTypeStaticText[@name='Reload']"));
        else {
            String[] multilangTexts= sectionName.split("/");
            Assertion.assertThat("There is valid texts for both languages", multilangTexts.length, is(2));
            return new Locator("RELOAD_SECTION", AppiumBy.xpath("//*[contains(@label, '" + multilangTexts[0] + "') or contains(@label, '"+ multilangTexts[1] +"')]/parent::*//XCUIElementTypeStaticText[@name='Reload']"));
        }
    }

    @Override
    public boolean isReloadSectionVisible(String sectionName) {
        driverTools.swipeToElement(getReloadLocator(sectionName), Swipe.UP, 3);
        driverTools.swipeUp();
        driverTools.clickElementByCoordinate(getReloadLocator(sectionName));
        return driverTools.isElementDisplayed(getReloadLocator(sectionName));
    }

    @Override
    public boolean getSectionName(String sectionName , String productName) {
        driverTools.swipeToElement(SPECIAL_OFFER, Swipe.UP, 2);
        driverTools.clickElementByCoordinate(SPECIAL_OFFER);
        Optional<WebElement> sectionTitleElement = driverTools.swipeToElement(getHeadingAndChipsLocator(sectionName),Swipe.UP, 15);
        Optional<WebElement> productCardElement  =driverTools.swipeToElement(getHeadingAndChipsLocator(sectionName, productName), Swipe.UP, 15);
        return productCardElement.isPresent() && sectionTitleElement.isPresent();

    }

    @Override
    public boolean verifyRedirectionCTA(String productName) {
        driverTools.clickElementByCoordinate(getHeadingAndChipsLocator(productName));
        boolean productDisappeared = driverTools.waitForElementToDisappear(getHeadingAndChipsLocator(productName), 5);
        if(productDisappeared){
            Optional<WebElement> firstCardElement = driverTools.waitForElementToPresence(FIRST_SEARCH_RESULT);
            Optional<WebElement> destinationNameElement = firstCardElement.isPresent() ? Optional.empty() : driverTools.waitForElementToPresence(TTDDestinationPageAndroid.NAME_DESTINATIONPAGE);
            driverTools.switchToWebView();
            Optional<WebElement> productNameElement = firstCardElement.isPresent() || destinationNameElement.isPresent() ? Optional.empty() : driverTools.waitForElementToPresence(TtdProductDetailPageWeb.PRODUCT_NAME_WEBVIEW);
            driverTools.switchToNative();
            return firstCardElement.isPresent() || productNameElement.isPresent() || destinationNameElement.isPresent();
        }
        return false;
    }

    @Override
    public boolean verifyDisplayedCurrency(String currency) {
        for (int i = 0; i < 3; i++) {
            driverTools.swipeUp();

            if (driverTools.swipeToElement(PRODUCT_CARD_PRICE_TEXT, Swipe.UP, 2).isPresent()) {
                break;
            }
        }
        List<WebElement> priceTextElements = driverTools.elements(PRODUCT_CARD_PRICE_TEXT).orElseThrow();
        List<String> priceTexts = new ArrayList<>();

        for(WebElement e : priceTextElements) {
            String priceText = driverTools.getValueAttribute(e,"label");
            if(priceText != null) {
                priceTexts.add(priceText);
            }
        }
        logger.log("PRODUCT_CARD_PRICE_TEXT -> " + Arrays.toString(priceTexts.toArray(new String[0])));
        driverTools.swipeDown();
        return CoreFunctions.checkCurrencyOnStringList(currency, priceTexts);
    }

    @Override
    public void swipeToTop() {
        driverTools.swipeToElement(FLOATING_UP_BUTTON, Swipe.UP, 2);
        if (driverTools.isElementDisplayed(FLOATING_UP_BUTTON)) driverTools.clickElementByCoordinate(FLOATING_UP_BUTTON);
        driverTools.swipeUntilElementVisible(VISIBLE_PRODUCT_CATEGORY, SCREEN, Swipe.DOWN, 0.7, 10).orElseThrow();
    }

    @Override
    public void goToProductCard(TTDLandingPagePOJO.Card productCard) {
        findSpecificSection(productCard.pageModuleTitle);

        if(productCard.chipsName != null) {
            clickPageModuleChip(productCard.pageModuleTitle, productCard.chipsName);
        }
    }

    private void clickPageModuleChip(String pageModuleTitle, String chipsName) {
        driverTools.swipeToElement(getChipsLocatorByPageModuleTitle(pageModuleTitle, chipsName), Swipe.UP, 5);
        driverTools.clickElementByCoordinate(getChipsLocatorByPageModuleTitle(pageModuleTitle, chipsName));
    }

    private Locator getChipsLocatorByPageModuleTitle(String pageModuleTitle, String chipsName) {
        return new Locator("CHIPS", AppiumBy.xpath("//XCUIElementTypeStaticText[@label='" + pageModuleTitle + "']//following-sibling::*//*[contains(@name,'page_module_chip_text')]//*[@label='" + chipsName + "']"));
    }

    public boolean isProductCardPriceVisible(TTDLandingPagePOJO.Card productCard, String price) {
        return driverTools.swipeToElement(getProductCardPriceTextLocator(productCard.pageModuleTitle, productCard.productName, price), Swipe.UP, 5).isPresent();
    }

    private Locator getProductCardPriceTextLocator(String pageModuleTitle, String productName, String price) {
        return new Locator("PRODUCT_CARD_PRICE", AppiumBy.xpath("//XCUIElementTypeStaticText[@label='" + pageModuleTitle + "']//parent::XCUIElementTypeCell//*[@name='product_name' and @label='" + productName + "']//following-sibling::*[contains(@label,'"+ price +"')]"));
    }

    @Override
    public String getProductCardPriceText(TTDLandingPagePOJO.Card productCard) {
        WebElement element = driverTools.swipeToElement(getProductCardPriceTextLocator(productCard.pageModuleTitle, productCard.productName), Swipe.UP, 2).orElseThrow();
        return driverTools.getText(element);
    }

    private Locator getProductCardPriceTextLocator(String pageModuleTitle, String productName) {
        return new Locator("PRODUCT_CARD_PRICE", AppiumBy.xpath("//XCUIElementTypeStaticText[@label='" + pageModuleTitle + "']//parent::XCUIElementTypeCell//*[@name='product_name' and @label='" + productName + "']//following-sibling::*[@name='bn_chips_price']"));
    }

    @Override
    public String getFirstProductNameRecentlyViewed() {
        driverTools.waitForElementToPresence(FIRST_RECENTLY_VIEWED_PRODUCT_NAME);
        return driverTools.getValueAttribute(FIRST_RECENTLY_VIEWED_PRODUCT_NAME, "label");
    }

    @Override
    public void removeFirstRecentlyViewedProduct() {
        driverTools.waitForElementToPresence(FIRST_RECENTLY_VIEWED_CROSS_BUTTON);
        driverTools.clickElementByCoordinate(FIRST_RECENTLY_VIEWED_CROSS_BUTTON);
    }

    @Override
    public void scrollUntilSRP() {
        driverTools.swipeToElement(SEARCH_RESULT_PAGE_CONTAINER, Swipe.UP_LONG, 15);
    }

    @Override
    public Boolean scrollUpUntilFloatingButtonShown() {
        driverTools.swipeToElement(FLOATING_BUTTON_ICON, Swipe.UP, 5);
        return driverTools.waitForElementToPresence(FLOATING_BUTTON_ICON).isPresent();
    }

    @Override
    public void clickFloatingButton() {
        driverTools.clickElementByCoordinate(FLOATING_BUTTON_ICON);
    }

    @Override
    public String getFirstGroupMenuChipText() {
        driverTools.waitForElementsToAppear(FIRST_GROUP_MENU_CHIP);
        return driverTools.getText(FIRST_GROUP_MENU_CHIP).orElseThrow();
    }

    @Override
    public void openGroupMenus(String groupMenuTitle) {
        Locator groupMenuChipLocator = getGroupMenuChipLocatorByText(groupMenuTitle);
        driverTools.waitForElementToPresence(groupMenuChipLocator);
        driverTools.clickElementByCoordinate(groupMenuChipLocator);
        if (driverTools.isElementDisplayed(CLOSE_BTN)) driverTools.clickElementByCoordinate(CLOSE_BTN);
        if (driverTools.isElementDisplayed(CLOSE_BTN_SRP)) driverTools.clickElementByCoordinate(CLOSE_BTN_SRP);
        driverTools.back();
    }

    private Locator getGroupMenuChipLocatorByText(String groupMenuTitle) {
        switch (groupMenuTitle.toLowerCase()) {
            case "activities":
                return new Locator("GROUP_MENU_CHIP_ACTIVITIES", AppiumBy.xpath("(//*[contains(@label, 'activities') or contains(@label, 'aktivitas')])[1]"));
            case "destination":
                return new Locator("GROUP_MENU_CHIP_DESTINATION", AppiumBy.xpath("//*[@type='XCUIElementTypeStaticText' and contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'destinations') or contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'destinasi')]"));
            default:
                logger.fail("Group menu '"+ groupMenuTitle +"' is not yet supported");
        }

        return null;
    }

    @Override
    public void clickMenuByIndex(int index) {
        Locator menuIconLocator = getMenuIconLocatorByIndex(index);
        driverTools.click(menuIconLocator);
    }

    private Locator getMenuIconLocatorByIndex(int index) {
        return new Locator("MENU_ICON_" + index, AppiumBy.xpath("(//*[@name='product_category_name'])["+index+"]"));
    }

    @Override
    public void clickBottomsheetCrossButton() {
        driverTools.waitForElementsToAppear(BOTTOMSHEET_CROSS);
        driverTools.clickElementByCoordinate(BOTTOMSHEET_CROSS);
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

    @Override
    public void clickDestinationMenuByName(String name) {
        Locator menuTextLocator = getDestinationMenuIconLocatorByName(name);
        driverTools.swipeLeftUntilFound(DESTINATION_CONTAINER, menuTextLocator, 10);
        driverTools.clickElementByCoordinate(menuTextLocator);
    }


    @Override
    public boolean checkDestinationUpdated(){
        driverTools.waitForElementToPresence(DESTINATION_DOT);
        boolean res = driverTools.isElementDisplayed(DESTINATION_DOT);
        if(res) driverTools.clickElementByCoordinate(DESTINATION_DOT);
        return res;
    }

    private Locator getDestinationMenuIconLocatorByName(String name) {
        return new Locator("DESTINATION_MENU_ICON_" + name.toUpperCase(), AppiumBy.xpath("(//*[@type='XCUIElementTypeStaticText' and @value='" + name + "'])[1]"));
    }

    private Locator getMenuIconLocatorByName(String name) {
        return new Locator("MENU_ICON_" + name.toUpperCase(), AppiumBy.xpath("//*[@name='product_category_name' and @label='" + name + "']"));
    }

    private Boolean verifyAllDestinationGroupMenu() {
        driverTools.waitForElementsToAppear(ALL_LOCATION_CHIPS_BUTTON);
        boolean isLocationChipsDisplayed = driverTools.isElementDisplayed(ALL_LOCATION_CHIPS_BUTTON);
        boolean isCloseBackButtonDisplayed = driverTools.isElementDisplayed(BOTTOMSHEET_CROSS) || driverTools.isElementDisplayed(BACK_BUTTON);
        boolean isSearchBarDisplayed = driverTools.isElementDisplayed(SEARCH_BAR);
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
        return isPromoSectionDisplayed && isPromoSectionClaimBtnDisplayed;    }

    @Override
    public String getSelectedLocation() {
        WebElement selectedLocation = driverTools.waitForElementToPresence(SELECTED_LOCATION).orElseThrow();
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
    public boolean isCurrentLocationOptionAppears(){
        driverTools.waitForElementToPresence(CURRENT_LOCATION_OPTION);
        return driverTools.isElementDisplayed(CURRENT_LOCATION_OPTION);
    }

    @Override
    public boolean verifyLocationChip() {
        return driverTools.isElementPresent(LOCATION_CHIP_SEARCHBAR);
    }

    @Override
    public String checkLocationChip() {
        driverTools.swipeToPresentedElement(LOCATION_CHIP_SEARCHBAR, Swipe.UP, 5);
        return driverTools.getText(LOCATION_CHIP_SEARCHBAR).orElseThrow();
    }

    @Override
    public void clickLocationChip() {
        driverTools.clickElementByCoordinate(LOCATION_CHIP_SEARCHBAR);
    }

    @Override
    public void clickBack() {
        driverTools.click(BACK_BUTTON);
    }

    @Override
    public boolean checkCTA() {
        driverTools.swipeToElement(SEE_ALL_BUTTON_LANDINGPAGE, Swipe.UP, 10).orElseThrow();
        driverTools.clickElementByCoordinate(SEE_ALL_BUTTON_LANDINGPAGE);

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

        Assertion.assertThat("Offer tab exists", tab, is(not("")));

        Locator tabLocator = getUnselectedTab(tab);
        WebElement tabButton  = driverTools.element(tabLocator).orElseThrow();
        driverTools.clickElementByCoordinate(tabButton);
    }

    private Locator getReloadChipLocator(String text) {
        if (!text.contains("/"))
            return new Locator("RELOAD_CHIP", AppiumBy.xpath("//*[contains(@name, 'page_module_chip_text')]//*[@label='"+ text +"']"));
        else {
            String[] multilangTexts= text.split("/");
            Assertion.assertThat("There is valid texts for both languages", multilangTexts.length, is(2));
            return new Locator("RELOAD_CHIP", AppiumBy.xpath("//*[contains(@name, 'page_module_chip_text')]//*[@label='"+ multilangTexts[0] +"' or @label='"+multilangTexts[1]+"']"));
        }
    }

    @Override
    public void checkReloadButton(String chipText, String reloadSection) {
        Locator reloadChipLocator = getReloadChipLocator(chipText);

        driverTools.swipeToElement(reloadChipLocator, Swipe.UP, 10);
        driverTools.clickElementByCoordinate(reloadChipLocator);

        Locator reloadButton = getReloadLocator(reloadSection);
        driverTools.swipeToPresentedElement(reloadButton, Swipe.UP, 1);
        Assertion.assertThat("Reload button appears", driverTools.waitForElementsToAppear(reloadButton).isPresent(), is(true));

        driverTools.clickElementByCoordinate(reloadButton);
        Assertion.assertThat("Reload button reappears", driverTools.isElementDisplayed(reloadButton), is(true));
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
        return new Locator("PRODUCT_PRICE", AppiumBy.xpath("(//*[@label='"+productName+"']//following-sibling::*[@type='XCUIElementTypeOther']//*[contains(@label,'"+currency+"')])[last()]"));
    }
}