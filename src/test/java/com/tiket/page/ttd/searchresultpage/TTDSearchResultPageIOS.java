package com.tiket.page.ttd.searchresultpage;

import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Deeplink;
import com.tiket.model.Environment;
import com.tiket.model.Exception.DriverToolsException;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.page.common.calendar.CalendarPage;
import com.tiket.page.ttd.TTDPage;
import com.tiket.pojo.ttd.GETProductListTTDSRPPOJO;
import com.tiket.pojo.ttd.TTDSRPByLocationPOJO;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.Configs;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.NotImplementedException;
import org.hamcrest.core.Is;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.tiket.core.CoreFunctions.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.in;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.number.OrderingComparison.greaterThanOrEqualTo;
import static org.hamcrest.number.OrderingComparison.lessThanOrEqualTo;

public class TTDSearchResultPageIOS<T extends WebDriver> extends TTDSearchResultPage<T> {
    public static final Locator RESULT_KEYWORD_SECTION = new Locator("RESULT_KEYWORD_SECTION", AppiumBy.id("ttd_search_page_search_result_title"));
    public static final Locator FIRST_SEARCH_RESULT = new Locator("FIRST_SEARCH_RESULT", AppiumBy.id("ttd_search_page_search_result_title"));
    public static final Locator SEARCH_RESULT_PAGE_CONTAINER = new Locator("SEARCH_RESULT_PAGE_CONTAINER", AppiumBy.accessibilityId("TTDSRP"));
    private static final Locator SRP_SELECTED_CATEGORY_TITLE = new Locator("SRP_SELECTED_CATEGORY_TITLE", AppiumBy.xpath("//*[@label=\"tds ic oval cross\"]//parent::*//XCUIElementTypeStaticText"));
    public static final Locator RESULT_COUNT = new Locator("RESULT_COUNT", AppiumBy.name("result_count"));
    public static final Locator FIRST_RESULT_CONTENT = new Locator("FIRST_RESULT_CONTENT", AppiumBy.iOSClassChain("**/XCUIElementTypeCell[`name == 'product_container'`][1]"));
    public static final Locator SEARCH_RESULT_COUNT = new Locator("SEARCH_RESULT_COUNT", AppiumBy.xpath("(//XCUIElementTypeStaticText[@name=\"result_count\"])[1]"));
    public static final Locator SUBCATEGORIES_SCROLLVIEW = new Locator("SUBCATEGORIES_SCROLLVIEW", AppiumBy.iOSClassChain("**/XCUIElementTypeOther[`name == 'TTDSRP'`]/XCUIElementTypeOther[1]/XCUIElementTypeOther/XCUIElementTypeOther[2]/XCUIElementTypeOther[2]/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeScrollView")); // FIXME: Need Unique Locator TTD - iOS - Akbar
    public static final Locator CATEGORIES_SCROLLVIEW = new Locator("CATEGORIES_SCROLLVIEW", AppiumBy.xpath("//XCUIElementTypeScrollView[.//XCUIElementTypeStaticText[contains(@name,\"product_category_\")]]"));
    public static final Locator SUBCATEGORIES = new Locator("SUBCATEGORIES_SCROLLVIEW", AppiumBy.iOSClassChain("**/XCUIElementTypeOther[`name == 'TTDSRP'`]/XCUIElementTypeOther[1]/XCUIElementTypeOther/XCUIElementTypeOther[2]/XCUIElementTypeOther[2]/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeScrollView/XCUIElementTypeOther/XCUIElementTypeOther")); // FIXME: Need Unique Locator TTD - iOS - Akbar
    public static final Locator FILTER_BUTTON = new Locator("FILTER_BUTTON", AppiumBy.xpath("//*[@name='tds_ic_filter']/preceding-sibling::XCUIElementTypeButton | //*[@name='tds_ic_filter']/following-sibling::XCUIElementTypeButton"));
    public static final Locator FILTER_BUTTON_GREEN_BADGE = new Locator("FILTER_BUTTON_GREEN_BADGE", AppiumBy.xpath("//XCUIElementTypeImage[@name='tds_ic_filter']/following-sibling::XCUIElementTypeOther"));
    public static final Locator FILTER_CHECKBOX = new Locator("FILTER_CHECKBOX", AppiumBy.iOSClassChain("**/XCUIElementTypeOther[`name CONTAINS '_filter_checkbox'`]"));
    public static final Locator APPLY_BUTTON = new Locator("APPLY_BUTTON", AppiumBy.name("apply_btn"));
    private static final Locator JOIN_NOW_BUTTON = new Locator("JOIN_NOW_BUTTON", AppiumBy.name("join_now_btn"));
    private static final Locator PRODUCT_ALL_CATEGORY_SELECTED_TAB = new Locator("PRODUCT_ALL_CATEGORY_SELECTED_TAB", AppiumBy.accessibilityId("product_category_ (selected)"));
    public static final Locator BACK_BUTTON = new Locator("BACK_BUTTON", AppiumBy.iOSClassChain("**/XCUIElementTypeButton[`name == \"tds ic back\"`]"));
    private static final Locator FIRST_PRODUCT_CARD = new Locator("FIRST_PRODUCT_CARD", AppiumBy.xpath("(//*[@name='product_container'])[1]"));
    public static final Locator FIRST_PRODUCT_CARD_CAMPAIGN_LABEL = new Locator("FIRST_PRODUCT_CARD_CAMPAIGN_LABEL", AppiumBy.xpath("(//XCUIElementTypeCell[@name='product_container'])[1]//*[@name='campaign_label']"));
    private static final Locator PRODUCT_NAME = new Locator("PRODUCT_NAME", AppiumBy.xpath("//XCUIElementTypeStaticText[@name='product_title' and @visible='true']"));
    private static final Locator VISIBLE_PRODUCT_NAME = new Locator("VISIBLE_PRODUCT_NAME", AppiumBy.xpath("//*[@name='product_title' and @visible='true']"));
    public static final Locator ALLOW_ACCESS_BUTTON = new Locator("ALLOW_ACCESS_BUTTON", AppiumBy.xpath("//XCUIElementTypeButton[@name='buttonPrimary' or (@label='Beri izin akses' or @label='Allow access')]"));
    public static final Locator ALLOW_ONCE_PERMISSION_BUTTON = new Locator("ALLOW_ONCE_PERMISSION_BUTTON", AppiumBy.xpath("//XCUIElementTypeButton[contains(@label,'Allow Once')]"));
    public static final Locator LOCATION_SEARCHBAR_SRP = new Locator("LOCATION_SEARCHBAR_SRP", AppiumBy.accessibilityId("ttd_search_input"));
    public static final Locator SRP_SEARCHBAR = new Locator("SRP_SEARCHBAR", AppiumBy.xpath("(//*[contains(@name, 'Search activities') or contains(@name, 'Cari aktivitas') and @type='XCUIElementTypeButton'])[1]"));
    private static final Locator PRICE_MIN_BUTTON_POINT = new Locator("PRICE_MIN_BUTTON_POINT", AppiumBy.name("ttd_srp_price_filter_min"));
    private static final Locator PRICE_MAX_BUTTON_POINT = new Locator("PRICE_MAX_BUTTON_POINT", AppiumBy.name("ttd_srp_price_filter_max"));
    private static final Locator PRICE_MIN_TEXT = new Locator("PRICE_MIN_TEXT", AppiumBy.xpath("(//XCUIElementTypeCell//XCUIElementTypeTextField)[last()]"));
    private static final Locator PRICE_MAX_TEXT = new Locator("PRICE_MAX_TEXT", AppiumBy.xpath("(//XCUIElementTypeCell//XCUIElementTypeTextField)[1]"));
    private static final Locator SEARCH_ITEM_CONTAINER = new Locator("SEARCH_ITEM_CONTAINER", AppiumBy.xpath("//*[@name='ttd_search_page_search_result_title']//parent::XCUIElementTypeCell"));
    private static final Locator SEE_ALL_PROMO = new Locator("SEE_ALL_PROMO", AppiumBy.xpath("//*[(@label='See all promos' or @label='Lihat semua promo') and @visible='true']"));
    private static final Locator FILTER_CONTAINER = new Locator("FILTER_CONTAINER", AppiumBy.xpath("//*[@label='Sort & Filter' or @label='Urutkan & Filter']//following::XCUIElementTypeTable"));
    private static final Locator ALL_LOCATIONS = new Locator("ALL_LOCATIONS", AppiumBy.xpath("//*[@label='All locations' or @label='Semua lokasi']"));

    //SRP Content
    public static final Locator FIRST_RESULT_TITLE = new Locator("FIRST_RESULT_TITLE", AppiumBy.xpath("(//XCUIElementTypeStaticText[@name='product_title'])[1]"));
    public static final Locator LOCATION_TEXT = new Locator("LOCATION_TEXT", AppiumBy.xpath("//XCUIElementTypeImage[@name='tds_ic_oval_chevron_right']/preceding-sibling::XCUIElementTypeStaticText[1]"));
    public static final Locator  LOCATION_TAGS = new Locator("LOCATION_TAGS", AppiumBy.xpath("//XCUIElementTypeOther[@name=\"populer_destination_chip\"]//XCUIElementTypeStaticText"));
    public static final Locator  CHANGE_FILTER_BUTTON = new Locator("CHANGE_FILTER_BUTTON", AppiumBy.accessibilityId("ttd_srp_reset_filter_btn"));
    public static final Locator PRODUCT_CARD_PRICE_TEXTS = new Locator("PRODUCT_CARD_PRICE_TEXTS", AppiumBy.xpath("//*[@name='product_cross_price'] | //*[@name='product_final_price']"));
    public static final Locator OPEN_CATEGORIES = new Locator("OPEN_CATEGORIES", AppiumBy.xpath("//*[@class='android.view.ViewGroup' and (./*[@text='Kategori'] or ./*[@text='Category'])]"));
    private static final Locator SEARCH_ACTIVITIES_BUTTON = new Locator(
            "SEARCH_ACTIVITIES_BUTTON",
            AppiumBy.xpath("(//XCUIElementTypeButton[contains(@name,'Search activities') or contains(@name,'Cari aktivitas')])[1]")
    );
    //sort by
    public static final Locator FILTER_BTN = new Locator("FILTER_BTN", AppiumBy.xpath("//*[@type=\"XCUIElementTypeButton\" and ./following-sibling::*[@name=\"tds_ic_filter\"]]"));
    public static final Locator SORT_BY_NEAREST = new Locator("SORT_BY_NEAREST", AppiumBy.name("nearest"));
    public static final Locator SORT_BY_LOWEST = new Locator("SORT_BY_LOWEST", AppiumBy.name("lowestPrice"));
    public static final Locator SORT_BY_HIGHEST = new Locator("SORT_BY_HIGHEST", AppiumBy.name("highestPrice"));
    public static final Locator SORT_BY_HIGHEST_RATING = new Locator("SORT_BY_HIGHEST_RATING", AppiumBy.name("highestRating"));
    public static final Locator APPLY_FILTER_BTN = new Locator("APPLY_FILTER_BTN", AppiumBy.xpath("//XCUIElementTypeButton[contains(@name,'Apply') or contains(@name,'Terapkan')]"));
    private static final Locator NEWLY_ADDED = new Locator("NEWLY_ADDED", AppiumBy.xpath("//*[@name='newlyAdded selected']"));

    //quick filter
    public static final Locator DATE_FILTER = new Locator("DATE_FILTER", AppiumBy.accessibilityId("date_filter_btn"));
    private static final Locator PRICE_FILTER = new Locator("PRICE_FILTER", AppiumBy.accessibilityId("price_filter_btn"));
    private static final Locator CLOSE_BUTTON = new Locator("CLOSE_BUTTON", AppiumBy.accessibilityId("ic cancel grey"));
    public static final Locator RESET_BUTTON = new Locator("RESET_BUTTON", AppiumBy.xpath("//*[@value='Reset']"));


    //language recognition
    public static final Locator IS_IN_ENG_SESSION = new Locator("IS_IN_ENG_SESSION", AppiumBy.id("All"));
    private static final Locator LPG_HEADER_BANNER = new Locator("LPG_HEADER_BANNER", AppiumBy.xpath("//*[contains(@label,'Jaminan Harga Termurah') or contains(@label,'Lowest Price Guarantee')]"));
    private static final Locator LOYALTY_LABEL_BANNER = new Locator("LOYALTY_LABEL_BANNER", AppiumBy.xpath("//XCUIElementTypeCell//XCUIElementTypeStaticText[contains(@label,'Benefit')]"));
    private static final Locator LOYALTY_BOTTOMSHEET = new Locator("LOYALTY_BOTTOMSHEET", AppiumBy.xpath("//XCUIElementTypeScrollView//XCUIElementTypeStaticText[contains(@label,'benefit')]"));
    private static final Locator LPG_LABEL_CARD = new Locator("LPG_LABEL_CARD", AppiumBy.xpath("//*[contains(@name, 'Jaminan Harga Termurah') or contains(@name, 'Lowest Price Guarantee') and ./preceding-sibling::*[@name='result_count']]"));
    private static final Locator CARD_WITH_LPG = new Locator("CARD_WITH_LPG", AppiumBy.xpath("//*[(contains(@label,'Jaminan Harga Termurah') or contains(@label,'Lowest Price Guarantee')) and @visible='true']//parent::*//parent::*//parent::*//parent::*//parent::*[@name='product_feature']//following-sibling::*[@name='product_title']"));
    private static final Locator PRODUCT_CONTAINER = new Locator("PRODUCT_CONTAINER", AppiumBy.name("product_container"));
    public static final Locator BANNER_SCROLL = new Locator("BANNER_SCROLL", AppiumBy.xpath("//XCUIElementTypeOther[@name=\"Vertical scroll bar, 6 pages\"]"));

    public static final Locator TODAY_BUTTON = new Locator("TODAY_BUTTON", AppiumBy.xpath("(//XCUIElementTypeStaticText[@label='Hari Ini' or @label='Today'])[2]"));
    public static final Locator TOMORROW_BUTTON = new Locator("TOMORROW_BUTTON", AppiumBy.xpath("//XCUIElementTypeStaticText[@label='Besok' or @label='Tomorrow']"));
    public static final Locator OTHER_BUTTON = new Locator("OTHER_BUTTON", AppiumBy.xpath("//XCUIElementTypeStaticText[@label='Lainnya' or @label='Other']"));
    public static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", AppiumBy.xpath("//XCUIElementTypeButton[@label='Save']"));

    public static final Locator INSTANT_PASS = new Locator("INSTANT_PASS", AppiumBy.iOSClassChain("**/XCUIElementTypeOther[`name == 'Instant Pass'`]/XCUIElementTypeOther"));
    public static final Locator TIKET_FLEXI = new Locator("INSTANT_PASS", AppiumBy.iOSClassChain("**/XCUIElementTypeOther[`name == 'tiket FLEXI'`]"));
    public static final Locator LOYALTY_BENEFIT_IN_CARD = new Locator("LOYALTY_BENEFIT_IN_CARD", AppiumBy.xpath("//*[contains(@name,'loyalty')]"));

    private Locator getLoyaltyIconInCardTier(String tier) {
        return new Locator("LOYALTY_BENEFIT_IN_CARD", AppiumBy.xpath("//*[@name='loyalty_"+tier+"']"));
    }

    public TTDSearchResultPageIOS(Session<T> session) {
        super(session);
    }

    @Override
    public boolean isLpgHeaderBannerDisplayed() {
        return driverTools.waitForElementToAppear(LPG_HEADER_BANNER).isPresent();
    }

    @Override
    public void clickLpgHeaderBanner() {
        driverTools.click(LPG_HEADER_BANNER);
    }

    @Override
    public boolean checkLpgLabelCard(int totalCard, String language) {
        driverTools.waitForElementToAppear(LPG_LABEL_CARD, 20);
        for (int i = 0; i < totalCard; i++) {
            String productName = getProductName();
            logger.log(productName);
            assertionLpgCard(productName);

            if (Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
                TTDPage<T> ttdPage = TTDPage.getInstance(session);
                ttdPage.assertProductLocation(productName, "indonesia", language);
            }

            driverTools.swipeToElement(lpgLabelCardNotName(productName), Swipe.UP, 2);
            if (driverTools.isElementDisplayed(visibleProductName(productName)))
                driverTools.swipeByDistance(PRODUCT_CONTAINER, Swipe.UP, 0.6);

            if (productName.equals(getProductName())) {
                try {
                    productName = driverTools.elements(VISIBLE_PRODUCT_NAME).orElseThrow().get(1).getText();
                    assertionLpgCard(productName);
                    logger.softWarning("End of product list reached");
                    break;
                } catch(IndexOutOfBoundsException e) {
                    logger.softWarning("End of product list reached");
                    break;
                }
            }
        }
        return true;
    }

    private void assertionLpgCard(String productName) {
        Assertion.assertThat("Show the LPG label on the product card: " + productName, driverTools.waitForElementToPresence(lpgLabelCard(productName)).isPresent(), is(true));
    }

    private String getProductName() {
        return driverTools.getText(VISIBLE_PRODUCT_NAME).orElseThrow();
    }

    private Locator visibleProductName(String productName) {
        return new Locator("VISIBLE_PRODUCT_NAME_" + productName.toUpperCase(), AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@name, 'product_title') and @value='" + productName + "']"));
    }

    private Locator lpgLabelCard(String productName) {
        return new Locator("LPG_LABEL_CARD_" + productName.toUpperCase(), AppiumBy.xpath("//*[@name='product_title' and @label='"+productName+"']//preceding-sibling::*[@name='product_feature']//*[(contains(@label,'Jaminan Harga Termurah') or contains(@label,'Lowest Price Guarantee'))]"));
    }

    private Locator lpgLabelCardNotName(String productName) {
        return new Locator("LPG_LABEL_CARD_NOT_" + productName.toUpperCase(), AppiumBy.xpath("//*[@name='product_title' and not(@label='" + productName + "')]//preceding-sibling*[@name='product_feature']//*[(contains(@label,'Jaminan Harga Termurah') or contains(@label,'Lowest Price Guarantee'))]"));
    }

    @Override
    public void clickProductWithLpg() {
        driverTools.waitForElementToPresence(CARD_WITH_LPG);
        driverTools.clickElementByCoordinate(CARD_WITH_LPG);
    }

    @Override
    public boolean isAutoCompleteSuggestionValid(String keyword, String type, String pageType) {
        driverTools.waitForElementToAppear(SEARCH_ITEM_CONTAINER);
        driverTools.swipeByDistance(SEARCH_ITEM_CONTAINER, Swipe.DOWN, 0.9);

        String[] typeLanguages = type.split(" / ");

        WebElement nameAutoCompleteElement = driverTools.swipeToElement(getNameAutoCompleteSuggestion(keyword), Swipe.UP, 3).orElseThrow();
        WebElement typeAutoCompleteElement = driverTools.swipeToElement(getTypeAutoCompleteSuggestion(typeLanguages[0], typeLanguages[1]), Swipe.UP, 3).orElseThrow();

        boolean isNameExist = driverTools.isElementDisplayed(nameAutoCompleteElement);
        boolean isTypeExist = driverTools.isElementDisplayed(typeAutoCompleteElement);
        logger.log("isNameExist: " + isNameExist +
                "\n" + "isTypeExist: " + isTypeExist);

        String nameResult = driverTools.getText(nameAutoCompleteElement);
        String typeResult = driverTools.getText(typeAutoCompleteElement);

        logger.log("Auto Complete Suggestion Found : " + nameResult + " " + typeResult);
        return isNameExist && isTypeExist;
    }

    private Locator getNameAutoCompleteSuggestion(String keyword) {
        return new Locator("NAME_AUTOCOMPLETE_SUGGESTION_FOR_" + keyword.toUpperCase(), AppiumBy.xpath("//XCUIElementTypeCell/XCUIElementTypeStaticText[@label='" + keyword + "'] | //*[@name='ttd_search_page_search_result_title' and @label='"+keyword+"']"));
    }

    private Locator getTypeAutoCompleteSuggestion(String typeId, String typeEng) {
        return new Locator("NAME_AUTOCOMPLETE_SUGGESTION_FOR_" + typeId.toUpperCase(), AppiumBy.xpath("//XCUIElementTypeCell/XCUIElementTypeStaticText[contains(@label, '" + typeId + "') or contains(@label, '" + typeEng + "')] | //*[@name='ttd_search_page_search_result_title']//preceding-sibling::XCUIElementTypeStaticText[contains(@label, '" + typeId + "') or contains(@label, '" + typeEng + "')]"));
    }

    @Override
    public void selectLocation(String keyword, String pageType) {
        final Locator NAME_AUTOCOMPLETE_SUGGEST = new Locator("NAME_AUTOCOMPLETE_SUGGEST", AppiumBy.xpath("//XCUIElementTypeCell/XCUIElementTypeStaticText[@label='" + keyword + "']"));
        WebElement nameAutocomplete = driverTools.waitForElementToPresence(NAME_AUTOCOMPLETE_SUGGEST).orElseThrow();
        driverTools.click(nameAutocomplete);
    }

    @Override
    public String pickAndClickOneResult() {
        driverTools.waitForElementToAppear(RESULT_KEYWORD_SECTION, 5);
        WebElement firstResult = driverTools.waitForElementToPresence(FIRST_SEARCH_RESULT).orElseThrow();
        String clickedResultText = driverTools.getText(firstResult);
        driverTools.click(firstResult);
        return clickedResultText;
    }

    @Override
    public boolean isKeywordSuggestionDisplayed(String keyword) {
        driverTools.waitForElementToPresence(RESULT_KEYWORD_SECTION);
        List<WebElement> searchResult = driverTools.elements(RESULT_KEYWORD_SECTION).orElseThrow();

        if(searchResult.size() > 0) {
            for(WebElement element : searchResult) {
                String elementText = driverTools.getValueAttribute(element, "label").toLowerCase();

                if(elementText.contains(keyword.toLowerCase())) {
                    return true;
                }
            }
        }

        return driverTools.waitForElementToPresence(RESULT_KEYWORD_SECTION).isPresent();
    }

    @Override
    public boolean isSearchResultPageShown() {
//        use this method to verify the SRP is shown in general
        return driverTools.waitForElementToAppear(SEARCH_RESULT_PAGE_CONTAINER, 10).isPresent();
    }

    @Override
    public String getSelectedCategory() {
        clickSortFilter();
        String title = driverTools.getText(SRP_SELECTED_CATEGORY_TITLE).orElseThrow();
        closeFilterBottomsheet();
        return title;
    }

    @Override
    public boolean isCategoriesSubCategoriesValid() {
        ArrayList<String> categoriesWithSubCategory = new ArrayList<>();
        boolean isValid = false;
        driverTools.click(OPEN_CATEGORIES);

        // Looping Categories
        for (int i = 1; i < 5; i++) {
            //get current category tittle
            WebElement currentCategoryElement = driverTools.waitForElementToAppear(getCategoryTitleElement(i)).orElseThrow();
            boolean isCurrentCategoryExist = driverTools.isElementDisplayed(currentCategoryElement);
            String currentCategoryTittle = driverTools.getText(currentCategoryElement);

            //log and assertion
            logger.log("Found category --> " + currentCategoryTittle);
            if (!isCurrentCategoryExist) {
                break;
            }

            //open category
            driverTools.click(currentCategoryElement);

            //iteration
            try {
                //check sub categories scroll view
                WebElement subCategoriesScrollViewElement = driverTools.waitForElementToAppear(SUBCATEGORIES_SCROLLVIEW, 5).orElseThrow();
                logger.log("sub category exist for category --> " + currentCategoryTittle);

                //add category to the list
                categoriesWithSubCategory.add(currentCategoryTittle);

                //start looping sub category
                if (driverTools.isElementDisplayed(subCategoriesScrollViewElement)) {
                    loopSubCategories(currentCategoryTittle);
                }
            } catch (NoSuchElementException e) {
                logger.log("sub category does not exist for category --> " + currentCategoryTittle);
            }

            if (i == 3) {
                driverTools.swipeLeft(currentCategoryElement);
            }

        }

        //0 chances for all category doesn't have any sub categories
        if (!(categoriesWithSubCategory.size() == 0)) {
            isValid = true;
        }

        return isValid;
    }

    @Override
    public boolean checkResultList(String modifyType, String modifySubType, int totalResult, String[] validationData) {
        driverTools.waitForElementToAppear(FIRST_RESULT_CONTENT, 20);
        loopDetectedResultCard(totalResult, modifySubType, validationData);
        return true;
    }

    private void loopDetectedResultCard(int totalResult, String modifySubType, String[] validationData) {
        String lastCheckedData = "";
        for (int index = 0; index < totalResult; index++) {
            //handle join now banner
            if (index == 3) {
                driverTools.swipeUp();
                continue;
            }

            lastCheckedData = handleVerifyCheck(modifySubType, validationData, lastCheckedData, index + 1);
        }
    }

    private String handleVerifyCheck(String modifySubType, String[] validationData, String lastCheckedData, int index) {
        switch (modifySubType) {
            case "rating":
                return resultCardRatingCheck(validationData, lastCheckedData, index);
            default:
                Assertion.fail("There is no available option for checking '" + modifySubType + "'");
        }
        return "";
    }

    private String resultCardRatingCheck(String[] validationData, String lastCheckedData, int index) {
        driverTools.swipeToElement(getResultCardRatingLocator(index), Swipe.UP, 2);

        WebElement ratingCard = driverTools.swipeToElement(getResultCardRatingLocator(index), Swipe.UP,2).orElseThrow();
        String resultCardRatingText = driverTools.getValueAttribute(ratingCard, "label");
        logger.log("value of label : " + resultCardRatingText);

        resultCardRatingText = resultCardRatingText.split(" ")[0];
        logger.log("label prepocess 1 : " + resultCardRatingText);

        resultCardRatingText = resultCardRatingText.split("/")[0].replace(",", "").replace(".", "");
        logger.log("label prepocess 2 : " + resultCardRatingText);

        int resultCardRating = Integer.parseInt(resultCardRatingText);

        if (validationData[0].equalsIgnoreCase("desc") && !lastCheckedData.equalsIgnoreCase("")) {
            int lastCheckedDataValue = Integer.parseInt(lastCheckedData);
            Assertion.assertThat("Assert if the result card '" + index + "' is already showing the valid rating", resultCardRating <= lastCheckedDataValue, Is.is(true));
        }

        driverTools.swipeUp();
        return resultCardRatingText;
    }

    private Locator getResultCardRatingLocator(int index) {
        return new Locator("RESULT_CARD_RATING", AppiumBy.xpath("//XCUIElementTypeOther[@name='TTDSRP']//XCUIElementTypeTable//XCUIElementTypeCell[@name='product_container']["+index+"]//XCUIElementTypeStaticText[@name='product_location']"));
    }

    private Locator getProductNameByIndexLocator(int index) {
        return new Locator("PRODUCT_CARD", AppiumBy.xpath("(//XCUIElementTypeStaticText[@name='product_title'])[" + index + "]"));
    }

    private void loopSubCategories(String categoryTitleText) {
        ArrayList<String> listOfSubCategories = new ArrayList<>();

        //get existing sub category
        List<WebElement> subCategoriesElement = driverTools.elements(SUBCATEGORIES).orElseThrow();
        int totalSubCategories = subCategoriesElement.size();

        // Looping Sub categories
        for (int i = 1; i <= totalSubCategories; i++) {
            //get current sub categories tittle
            WebElement currentSubCategoryElement = driverTools.waitForElementToAppear(getSubCategoryTitleElement(i)).orElseThrow();
            String currentSubCategoryTittle = currentSubCategoryElement.getAttribute("name");
            listOfSubCategories.add(currentSubCategoryTittle);

            //open current sub categories
            driverTools.click(getSubCategoryTitleElement(i));
            if (i / 3 == 0) {
                driverTools.swipeLeft(getSubCategoryTitleElement(i));
            }
        }

        //log
        logger.log("Category --> " + categoryTitleText);
        logger.log("Sub Category --> " + listOfSubCategories);

    }

    private Locator getCategoryTitleElement(int index) {
        return new Locator("CATEGORY_TITLE_TEXT", AppiumBy.xpath("(//XCUIElementTypeStaticText[contains(@name,'product_category_')])["+ index +"]"));
    }

    private Locator getSubCategoryTitleElement(int index) {
        return new Locator("SUBCATEGORIES_SCROLLVIEW", AppiumBy.xpath("(//XCUIElementTypeStaticText[contains(@name,'product_sub_category_')])[" + index + "]")); // FIXME: Need Unique Locator TTD - iOS - Akbar
    }

    @Override
    public void setAppPermissionAllow() {
        driverTools.waitForElementToAppear(ALLOW_ACCESS_BUTTON, 10).ifPresent(driverTools::click);
        driverTools.waitForElementToBeClickable(ALLOW_ONCE_PERMISSION_BUTTON).ifPresent(driverTools::click);
    }

    private Locator activityCardWithAnyText(String activityName, String anyText) {
        return new Locator("ACTIVITY_CARD_WITH_ANY_TEXT", AppiumBy.xpath("//*[contains(@label,'"+anyText+"')]//parent::*[@name='product_container']//*[@name='product_title' and @label='"+activityName+"']"));
    }

    @Override
    public boolean checkLocationCampaign(String location, String promoName, int totalActivity) {
        location = location.toLowerCase();
        String regexId = "^([\\w\\s]+,\\s+" + Pattern.quote(location) + "\\b[\\w\\s]*)$";
        String regexEn = "^([\\w\\s]+,\\s+(?:\\w+\\s)*" + Pattern.quote(location) + ")$";
        String regexExact = "[\\w\\s,./\\(\\)]+" + Pattern.quote(location);

        driverTools.waitForElementToAppear(getProductNameByIndexLocator(1));
        List<WebElement> totalProduct = driverTools.elements(PRODUCT_NAME).orElseThrow();
        if (totalActivity > totalProduct.size()) totalActivity = totalProduct.size();
        logger.log("Total activity: " + totalActivity);
        for (int i = 0; i < totalActivity; i++) {
            String activityName = driverTools.getText(getProductNameByIndexLocator(i+1)).orElseThrow();
            driverTools.swipeToElement(getResultCardRatingLocator(i+1), Swipe.UP, 3);
            String activityLocation = driverTools.getText(getResultCardRatingLocator(i+1)).orElseThrow().toLowerCase();
            Assertion.assertThat("Activity: \""+activityName+"\" location should be in or near " + location, activityLocation, is(either(is(location)).or(matchesPattern(regexId)).or(matchesPattern(regexEn)).or(matchesPattern(regexExact)).or(containsString("near "+location)).or(containsString("dekat "+location))));
            Assertion.assertThat("Activity: \""+activityName+"\" in the correct campaign " + promoName, driverTools.isElementDisplayed(activityCardWithAnyText(activityName, promoName)), is(true));
        }
        return true;
    }

    /**
     * Validates if activities are sorted by nearest location in ascending order.
     * The method compares the distance from the first activity card (base location)
     * with subsequent activity cards and verifies that the sorting is correct.
     *
     * @param totalActivitiesToCompare the total number of activities to validate
     * @return true if the sorting is correct, false otherwise
     */
    @Override
    public boolean isSortByNearestLocationCorrect(String totalActivitiesToCompare) {

        driverTools.waitForElementToAppear(FIRST_RESULT_CONTENT);
        boolean isSortingCorrect = true;

        // Get the base location distance from the first activity card
        WebElement baseCard = driverTools.waitForElementToAppear(activityCardLocationLocator(1), 60).orElseThrow();
        String baseCardText = driverTools.getText(baseCard);
        logger.log("Base card raw text: " + baseCardText);

        String[] baseCardParts = baseCardText.split("·");
        String baseDistanceString = baseCardParts[baseCardParts.length - 1].trim();
        logger.log("Base distance preprocessed: " + baseDistanceString);

        double baseDistance = Double.parseDouble(baseDistanceString.replace("km", "").trim().replace(",", "."));

        for (int activityIndex = 1; activityIndex <= Integer.parseInt(totalActivitiesToCompare); activityIndex++) {

            // Swipe to the current activity card and retrieve its text
            driverTools.swipeToElement(activityCardLocationLocator(activityIndex), Swipe.UP, 5);
            String currentCardText = driverTools.getText(activityCardLocationLocator(activityIndex)).orElseThrow();
            logger.log("Current activity " + activityIndex + " raw text: " + currentCardText);

            String[] currentCardParts = currentCardText.split("·");
            String currentDistanceString = currentCardParts[currentCardParts.length - 1].trim();
            logger.log("Current distance preprocessed: " + currentDistanceString);

            double currentDistance = Double.parseDouble(currentDistanceString.replace("km", "").trim().replace(",", "."));

            // Compare the current distance with the base distance
            if (currentDistance < baseDistance) {
                isSortingCorrect = false;
            }

            logger.log("Base distance (km): " + baseDistance);
            logger.log("Current activity distance (km): " + currentDistance);
            logger.log("Is sorting correct: " + isSortingCorrect);

            if (!isSortingCorrect) {
                logger.warn("Exiting loop as sorting is incorrect");
                break;
            }
        }

        return isSortingCorrect;
    }
    @Override
    public void shortBy(String sortType) {
        switch (sortType) {
            case "nearest" -> {
                driverTools.click(SORT_BY_NEAREST);
            }
            case "highest rating" -> {
            }
            case "highest price" -> {
            }
            case "highest star" -> {
            }
            case "lowest price" -> {
            }
        }
    }

    @Override
    public void clickSortFilter() {
        driverTools.waitForElementToBeClickable(FILTER_BTN);
        driverTools.click(FILTER_BTN);
    }

    /**
     * Method Review : this method will be return General location. for example "North Jakarta" or
     * "Jakarta Utara", it will return "Jakarta" only. please update this method when "activity card
     * location" locator unique-id is added. estimated after Jan 2023.
     */
    private String getActivityCardLocationGeneral(int index) {

        String activityCardLocation = driverTools.getText(activityCardLocationLocator(index)).orElseThrow();
        int textSeparatorCount = activityCardLocation.length() - activityCardLocation.replaceAll("•", "").length();

        if (textSeparatorCount == 2) {
            activityCardLocation = activityCardLocation.split(" • ")[1];
        } else {
            activityCardLocation = activityCardLocation.split(" • ")[0];
        }

        if (driverTools.isElementDisplayed(IS_IN_ENG_SESSION)) {
            activityCardLocation = activityCardLocation.substring(activityCardLocation.lastIndexOf(' ') + 1);
        } else {
            activityCardLocation = activityCardLocation.split(" ")[0];
        }

        return activityCardLocation;
    }

    private Locator activityCardLocationLocator(int index) {
        return new Locator("ACTIVITY_CARD_LOCATION", AppiumBy.iOSClassChain("**/XCUIElementTypeOther[`name == 'TTDSRP'`]/XCUIElementTypeTable/XCUIElementTypeCell[`name = 'product_container'`][" + index + "]/XCUIElementTypeStaticText[`name == 'product_location'`]"));
    }

    private Locator activityCardLocator(int index) {
        return new Locator("ACTIVITY_CARD", AppiumBy.iOSClassChain("**/XCUIElementTypeCell[`name == 'product_container'`][" + index + "]"));
    }

    public void clickSortByButton(String sortType) {
        driverTools.waitForElementToPresence(FIRST_RESULT_TITLE);

        WebElement sortButtonLocator = driverTools.waitForElementToPresence(FILTER_BTN, 30).orElseThrow();
        driverTools.clickElementByCoordinate(sortButtonLocator);

        switch (sortType) {
            case "Nearest/Terdekat" -> {
                WebElement nearestLocator = driverTools.waitForElementToBeClickable(SORT_BY_NEAREST).orElseThrow();
                driverTools.clickElementByCoordinate(nearestLocator);
            }
            case "Lowest Price/Harga Terendah" -> {
                WebElement sortTypeLocator = driverTools.waitForElementToBeClickable(SORT_BY_LOWEST).orElseThrow();
                driverTools.clickElementByCoordinate(sortTypeLocator);
            }
            case "Highest Rating/Rating Tertinggi" -> {
                WebElement sortTypeLocator = driverTools.waitForElementToBeClickable(SORT_BY_HIGHEST_RATING).orElseThrow();
                driverTools.clickElementByCoordinate(sortTypeLocator);
            }
            default -> {
                WebElement sortTypeLocator = driverTools.waitForElementToBeClickable(SORT_BY_HIGHEST).orElseThrow();
                driverTools.clickElementByCoordinate(sortTypeLocator);
            }
        }

        driverTools.click(APPLY_FILTER_BTN);
        driverTools.waitForElementToPresence(FIRST_RESULT_TITLE);
    }

    @Override
    public List<String> selectPriceFilter(String priceChanges, int swipePercentage) {
        double distance = swipePercentage / 100.0;
        driverTools.swipeToElement(PRICE_MAX_BUTTON_POINT, Swipe.UP, 10);
        if (priceChanges.equalsIgnoreCase("min_price")) {
            driverTools.moveElementHorizontal(PRICE_MIN_BUTTON_POINT, PRICE_MAX_BUTTON_POINT, distance); //swipe min button to max button by swipePercentage distance
        } else if (priceChanges.equalsIgnoreCase("max_price")) {
            driverTools.moveElementHorizontal(PRICE_MAX_BUTTON_POINT, PRICE_MIN_BUTTON_POINT, distance); //swipe max button to min button by swipePercentage distance
        } else if(priceChanges.equalsIgnoreCase("both")){
            driverTools.moveElementHorizontal(PRICE_MIN_BUTTON_POINT, PRICE_MAX_BUTTON_POINT, distance); //swipe min button to max button by swipePercentage distance
            driverTools.moveElementHorizontal(PRICE_MAX_BUTTON_POINT, PRICE_MIN_BUTTON_POINT, distance); //swipe max button to min button by swipePercentage distance
        }else {
            logger.fail("There is no '" + priceChanges + "' option for selecting price filter");
            throw new NotImplementedException();
        }

        driverTools.swipeToElement(PRICE_MIN_TEXT, Swipe.UP, 10);
        String priceMin = driverTools.getText(PRICE_MIN_TEXT).orElseThrow();
        driverTools.swipeToElement(PRICE_MAX_TEXT, Swipe.UP, 10);
        String priceMax = driverTools.getText(PRICE_MAX_TEXT).orElseThrow();

        List<String> selectedPrice = new ArrayList<>();
        selectedPrice.add(priceMin);
        selectedPrice.add(priceMax);
        return selectedPrice;
    }

    @Override
    public boolean isPriceFilterApplied(int totalActivitiesToCompare, List<String> selectedPrice,String currency) {
        int minPrice = Integer.parseInt(selectedPrice.get(0).replaceAll("[^0-9]", ""));
        int maxPrice = Integer.parseInt(selectedPrice.get(1).replaceAll("[^0-9]", ""));

        List<String> priceTextElements;
        if (driverTools.waitForElementToAppear(PRODUCT_CARD_PRICE_TEXTS).isPresent()) {
            priceTextElements = driverTools.getTexts(PRODUCT_CARD_PRICE_TEXTS);
        } else {
            priceTextElements = driverTools.getTexts(getPriceLocatorByCurrency(currency));
        }
        for (int i=0; i<totalActivitiesToCompare; i++) {
            String priceText = priceTextElements.get(i);
            int actualPrice = Integer.parseInt(priceText.replaceAll("[^0-9]", ""));

            Assertion.assertThat("index: " + i + " Price should be shown as filter value", actualPrice,
                    allOf(greaterThanOrEqualTo(minPrice), lessThanOrEqualTo(maxPrice)));
        }
        return true;
    }

    private Locator getPriceLocatorByCurrency(String currency){
        return new Locator("PRICE_LOCATOR_BY_CURRENCY", AppiumBy.xpath("//*[@name='product_location']//following-sibling::XCUIElementTypeStaticText[contains(@label,'"+currency+"')][last()]"));
    }

    @Override
    public void clickFilter() {
        driverTools.waitForElementToPresence(getProductNameByIndexLocator(1));
        driverTools.waitForElementToPresence(FILTER_BUTTON);
        driverTools.click(FILTER_BUTTON);
    }

    @Override
    public void removeAllFilter() {
        //Android only, but possible to implement in iOS
    }

    @Override
    public List<String> selectGeneralFilter(String filter) {
        List<String> selectedFilter = new ArrayList<>();
        List<Integer> selectedIndex = new ArrayList<>();
        WebElement filterContainerElement;
        WebElement selectedFilterElement;

        for(String filterName : filter.split("-")) {
            switch (filterName) {
                case "tiket clean" -> {
                    driverTools.swipeToElement(filterByLocator("tiket CLEAN"), Swipe.UP, 5);
                    driverTools.waitForElementToBeClickable(filterByLocator("tiket CLEAN"), 10);
                    driverTools.clickElementByCoordinate(filterByLocator("tiket CLEAN"));
                    selectedFilter.add("tiket CLEAN");
                }
                case "tiket flexi" -> {
                    driverTools.swipeToElement(TIKET_FLEXI, Swipe.UP, 5);
                    driverTools.waitForElementToBeClickable(TIKET_FLEXI, 10);
                    driverTools.clickElementByCoordinate(TIKET_FLEXI);
                    selectedFilter.add("tiket FLEXI");
                }
                case "instant pass" -> {
                    driverTools.swipeToElement(INSTANT_PASS, Swipe.UP, 5);
                    driverTools.waitForElementToBeClickable(INSTANT_PASS, 10);
                    driverTools.click(INSTANT_PASS);
                    selectedFilter.add("Instant Pass");
                }
                case "instant confirmation" -> {
                    filterContainerElement = driverTools.waitForElementToPresence(FILTER_CONTAINER, 20).orElseThrow();
                    driverTools.swipeReleaseOnElement(filterContainerElement, Swipe.UP, 2);
                    selectedFilterElement = driverTools.waitForElementToBeClickable(filterByLocator("Instant Confirmation"), 20).orElseThrow();
                    driverTools.clickElementByCoordinate(selectedFilterElement);
                    selectedFilter.add("Instant Confirmation");
                }
                case "Benefit" -> {
                    filterContainerElement = driverTools.waitForElementToPresence(FILTER_CONTAINER, 20).orElseThrow();
                    driverTools.swipeReleaseOnElement(filterContainerElement, Swipe.UP, 2);
                    selectedFilterElement = driverTools.waitForElementToBeClickable(filterByLocatorLoyaltyBenefit(), 10).orElseThrow();
                    driverTools.clickElementByCoordinate(selectedFilterElement);
                    selectedFilter.add("Benefit");
                }

                case "random" -> {
                    int checkboxCount = driverTools.elements(FILTER_CHECKBOX).orElseThrow().size();

                    for(int i = 0; i < checkboxCount; i++) {
                        int randomIndex = generateRandomInt(1,checkboxCount);
                        Locator randomGeneralFilter = filterByLocator(randomIndex);
                        String filterTitle = driverTools.getValueAttribute(randomGeneralFilter,"label");
                        if(!selectedIndex.contains(randomIndex) && !selectedFilter.contains(filterTitle)) {
                            if (driverTools.getValueAttribute(randomGeneralFilter,"name").contains("promo")) {
                                selectedFilter.add("promo||"+filterTitle);
                            } else {
                                selectedFilter.add(filterTitle);
                            }
                            driverTools.swipeToElement(randomGeneralFilter, Swipe.UP,5);
                            driverTools.click(randomGeneralFilter);
                            break;
                        }
                    }
                }
                default -> {
                    logger.fail("There is no '"+filter+"' option for selecting filter");
                    throw new NotImplementedException();
                }
            }
        }

        return selectedFilter;
    }

    private Locator getPromoFilterLocator(String promoName) {
        String promoNameId;
        String promoNameEn;
        if (promoName.contains("/")) {
            promoNameId = promoName.split("/")[0];
            promoNameEn = promoName.split("/")[1];
        } else {
            promoNameId = promoName;
            promoNameEn = promoName;
        }
        return new Locator("PROMO_FILTER", AppiumBy.xpath("//*[@name='Promo']//following-sibling::*//*[(contains(@label,'"+promoNameId+"') or contains(@label,'"+promoNameEn+"')) and @visible='true']"));

    }

    @Override
    public void selectPromoFilter(String promoName) {
        for (int i = 0; i < 5; i++) {
            if (driverTools.waitForElementToPresence(SEE_ALL_PROMO).isEmpty()) {
                driverTools.swipeByDistance(FILTER_CONTAINER, Swipe.UP, 0.6);
            }
        }
        driverTools.click(SEE_ALL_PROMO);
        driverTools.swipeToElement(getPromoFilterLocator(promoName), Swipe.UP, 2);
        driverTools.click(getPromoFilterLocator(promoName));
    }

    @Override
    public void clickReset() {
        driverTools.waitForElementToBeClickable(RESET_BUTTON);
        driverTools.click(RESET_BUTTON);
        driverTools.click(APPLY_BUTTON);
    }

    @Override
    public void clickJoinNow() {
        isLoginBannerDisplayed();
        driverTools.click(JOIN_NOW_BUTTON);
        if (driverTools.isElementDisplayed(JOIN_NOW_BUTTON)) driverTools.clickElementByCoordinate(JOIN_NOW_BUTTON);
    }

    @Override
    public boolean isLoginBannerDisplayed() {
        driverTools.waitForElementToPresence(PRODUCT_NAME, 30);
        return driverTools.isElementDisplayed(JOIN_NOW_BUTTON);
    }

    @Override
    public void switchCategory(String category) {
        String categoryID = null;
        String categoryENG = null;

        switch (category.toLowerCase()) {
            case "all" -> {categoryID = "All";categoryENG = "Semua";}
            case "attractions" -> {categoryID = "Atraksi";categoryENG = "Attractions";}
            case "playgrounds" -> {categoryID = "Tempat Bermain";categoryENG = "Playgrounds";}
            case "spa and beauty" -> {categoryID = "Spa & Kecantikan";categoryENG = "Spa & Beauty";}
            case "tours" -> {categoryID = "Tur";categoryENG = "Tours";}
            case "entertainments" -> {categoryID = "Entertainment";categoryENG = "Entertainments";}
            case "sports and outdoors" -> {categoryID = "Olahraga & Outdooor";categoryENG = "Sports & Outdoors";}
            case "events" -> {categoryID = "Event";categoryENG = "Event";}
            case "jr pass" -> {categoryID = "JR Pass";categoryENG = "JR Pass";}
            default -> {
                logger.fail("wrong argument, please check productCategory on datasheet");
            }
        }
        final Locator PRODUCT_CATEGORY_TARGET = productCategoryLocator(categoryID, categoryENG);
        final Locator PRODUCT_CATEGORY_ALL = productCategoryLocator("Semua", "All");

        if(driverTools.waitForElementToAppear(PRODUCT_CATEGORY_TARGET).isEmpty()) {
            driverTools.swipeToElement(PRODUCT_CATEGORY_ALL, CATEGORIES_SCROLLVIEW, Swipe.RIGHT, 15);
        }

        WebElement productCategory = driverTools.swipeToElement(PRODUCT_CATEGORY_TARGET, CATEGORIES_SCROLLVIEW, Swipe.LEFT, 15).orElseThrow();
        driverTools.click(productCategory);
    }

    private Locator productCategoryLocator(String categoryID, String categoryENG) {
        return new Locator("PRODUCT_CATEGORY", AppiumBy.iOSClassChain("**/XCUIElementTypeStaticText[`label == '" + categoryID + "' OR label == '" + categoryENG + "'`]"));
    }

    @Override
    public void submitFilter() {
        driverTools.clickElementByCoordinate(APPLY_BUTTON);
    }

    private Locator getProductFeatureByLabel(int productContainerIndex, String labelName) {
        return new Locator("PRODUCT_CATEGORY", AppiumBy.iOSClassChain("**/XCUIElementTypeStaticText[`label == '"+labelName+"'`]"));
    }

    private List<String> getProductFeatureByIndex(int productContainerIndex){
        List<String> displayedFeatures = new ArrayList<>();

        final Locator PRODUCT_FEATURES = new Locator("PRODUCT_FEATURES", AppiumBy.iOSClassChain("**/XCUIElementTypeCell[`name == 'product_container'`]["+productContainerIndex+"]/XCUIElementTypeCell[`name == 'product_feature'`]/**/XCUIElementTypeStaticText"));
        for(WebElement element : driverTools.elements(PRODUCT_FEATURES).orElseThrow()) {
            displayedFeatures.add(driverTools.getValueAttribute(element, "label"));
        }

        return displayedFeatures;
    }

    @Override
    public boolean isGeneralFilterApplied(String totalActivitiesToCompare, List<String> selectedFilter) {
        boolean result = false;
        driverTools.waitForElementToAppear(getProductNameByIndexLocator(1),30);
        driverTools.waitForElementToBeClickable(getProductNameByIndexLocator(1),30);
        if(driverTools.waitForElementToAppear(getProductNameByIndexLocator(1),30).isPresent()) {
            String resultCount = driverTools.getValueAttribute(RESULT_COUNT,"label").split(" ")[1];
            if(Integer.parseInt(resultCount) < Integer.parseInt(totalActivitiesToCompare)) {
                totalActivitiesToCompare = resultCount;
            }

            for (int i = 1; i < Integer.parseInt(totalActivitiesToCompare) + 1; i++) {
                Locator productName = getProductNameByIndexLocator(i);
                WebElement productNameElement = driverTools.swipeToElement(productName, Swipe.UP, 3).orElseThrow();
                logger.log("Product Name : " + driverTools.getValueAttribute(productNameElement, "label"));

                for (String filter : selectedFilter) {
                    logger.log("Filter Applied Checked : " + filter);
                    if (filter.contains("promo")) {
                        if(checkProductPromo(i)) {
                            result = true;
                        } else {
                            return false;
                        }
                    } else {
                        if (checkProductFeature(i, filter)) {
                            result = true;
                        } else {
                            return false;
                        }
                    }
                }
            }
        } else {
            driverTools.waitForElementToDisappear(RESULT_COUNT);
            driverTools.waitForElementToAppear(CHANGE_FILTER_BUTTON).orElseThrow();
            return true;
        }

        return result;
    }

    private boolean checkProductPromo(int i) {
        return driverTools.waitForElementToPresence(getCrossPriceLocatorByProductIndex(i)).isPresent() || !driverTools.getValueAttribute(getCampaignLabelLocatorByProductIndex(i), "name").contains("price");
    }

    private Locator getCampaignLabelLocatorByProductIndex(int index) {
        return new Locator("CAMPAIGN LABEL_PRODUCT_"+index, AppiumBy.xpath("((//XCUIElementTypeCell[@name = 'product_container'])["+index+"]/XCUIElementTypeStaticText[@name = 'product_location']/following-sibling::*)[1]"));
    }

    private Locator getCrossPriceLocatorByProductIndex(int index) {
        return new Locator("CROSS_PRICE_PRODUCT_"+index, AppiumBy.xpath("(//XCUIElementTypeCell[@name = 'product_container'])["+index+"]/XCUIElementTypeStaticText[@name = 'product_cross_price']"));
    }

    private boolean checkProductFeature(int productContainerIndex, String featureName){
        boolean isFeaturePresent = driverTools.waitForElementToPresence(getProductFeatureByLabel(productContainerIndex,featureName)).isPresent();

        if (!isFeaturePresent) {
            driverTools.swipeLeft(getFeatureByIndex(productContainerIndex,2));
            isFeaturePresent = driverTools.waitForElementToPresence(getProductFeatureByLabel(productContainerIndex,featureName)).isPresent();
        }

        return isFeaturePresent;
    }

    private Locator getFeatureByIndex(int productContainerIndex, int featureIndex) {
        return new Locator("PRODUCT_FEATURES", AppiumBy.iOSClassChain("**/XCUIElementTypeCell[`name == 'product_container'`]["+productContainerIndex+"]/XCUIElementTypeCell[`name == 'product_feature'`]/**/XCUIElementTypeStaticText["+featureIndex+"]"));
    }

    private Locator activityLabelLocatorDualLanguage(String labelId, String labelEng, int index) {
        return new Locator("LABEL", AppiumBy.iOSClassChain("**/XCUIElementTypeOther[`name == 'TTDSRP'`]/XCUIElementTypeTable/XCUIElementTypeCell[" + index + "]/XCUIElementTypeStaticText[`label == '" + labelId + "' or label == '" + labelEng + "'`]"));
    }

    private Locator activityLabelLocator(String labelName, int index) {
        return new Locator("LABEL", AppiumBy.iOSClassChain("**/XCUIElementTypeOther[`name == 'TTDSRP'`]/XCUIElementTypeTable/XCUIElementTypeCell[" + index + "]/XCUIElementTypeStaticText[`label == '" + labelName + "'`]"));
    }

    private Locator bannerCardLocator(int index) {
        return new Locator("BANNER_CARD", AppiumBy.xpath("//XCUIElementTypeOther[@name='TTDSRP']/XCUIElementTypeTable/XCUIElementTypeCell[" + index + "]//XCUIElementTypeStaticText[contains(@name, 'Learn more') or contains(@name, 'Pelajari lebih lanjut')]"));
    }

    private Locator getSortTypeLocator(String sortType) {
        String englishVer = sortType.split("/")[0];
        String bahasaVer = sortType.split("/")[1];
        return new Locator("SORT_BY_TYPE", AppiumBy.xpath("//XCUIElementTypeStaticText[@label='" + englishVer + "' or @label='" + bahasaVer + "']")); // FIXME: Need Unique Locator TTD - iOS - Akbar
    }

    @Override
    public boolean isFilterApplied(int totalActivity, String FilterType) {
        boolean isCorrect = true;
        for (int i = 1; i < totalActivity; i++) {
            if (i == 2) {
                logger.log("Swiping up banner card");
                driverTools.swipeUp();
                continue;
            }
            driverTools.waitForElementToAppear(activityCardLocator(i), 30);

            WebElement tiketFlexi = driverTools.waitForElementToAppear(activityLabelLocator("tiket FLEXI", i)).orElseThrow();
            if (!driverTools.isElementDisplayed(tiketFlexi)) {
                isCorrect = false;
            }
            logger.log("activity card no = " + i);
            logger.log("is correct status =  " + isCorrect);

            driverTools.swipeUp();
        }
        return isCorrect;
    }

    private Locator filterByLocator(String filter) {
        if(filter.contains("/")) {
            String[] filterSplit = filter.split("/");
            return new Locator("GENERAL_FILTER_"+filter.toUpperCase(), AppiumBy.iOSClassChain("**/*[`label == '"+filterSplit[0]+"' OR label =='"+filterSplit[1]+"' AND visible==true`]"));
        }

        return new Locator("GENERAL_FILTER_"+filter.toUpperCase(), AppiumBy.iOSClassChain("**/*[`label == '"+filter+"' AND visible==true`]"));
    }

    private Locator filterByLocator(int index) {
        return new Locator("GENERAL_FILTER_RANDOM", AppiumBy.xpath("(//*[@label='Fitur' or contains(@label,'Feature')]//following-sibling::XCUIElementTypeOther//XCUIElementTypeStaticText[@visible='true'])["+ index +"]"));
    }
    private Locator filterByLocatorLoyaltyBenefit() {
        return new Locator("GENERAL_FILTER_RANDOM", AppiumBy.xpath("//*[@label='Fitur' or contains(@label,'Feature')]//following-sibling::XCUIElementTypeOther//*[contains(@label,'Benefit') and @visible='true']"));
    }

    @Override
    public void openPdpDeeplink(String slugProductName) {
        Deeplink pdpDeeplink = new Deeplink("pdpDeeplink",
                "tiket://m.gatotkaca.tiket.com/to-do/" + slugProductName,
                "tiket://m.preprod.tiket.com/to-do/" + slugProductName,
                "tiket://m.tiket.com/to-do/" + slugProductName);
        driverTools.deeplink(pdpDeeplink);
        driverTools.switchToNative();
        driverTools.waitForElementToPresence(PRODUCT_NAME);
    }

    private Locator getProductCardFinalPriceLocator(int index) {
        return new Locator("PRODUCT_CARD_FINAL_PRICE_" + index, AppiumBy.xpath("(//XCUIElementTypeStaticText[@name='mainPrice'])["+index+"]"));
    }

    private Locator getProductCardFinalPrice(String currency, int index) {
        return new Locator("PRODUCT_CARD_FINAL_PRICE_" + index, AppiumBy.xpath("(//*[@type='XCUIElementTypeStaticText' and contains(@label,'" + currency + "')])[" + index + "]"));
    }

    private Locator getProductCardCrossPriceLocator(int index) {
        return new Locator("PRODUCT_CARD_CROSS_PRICE_" + index, AppiumBy.xpath("(//XCUIElementTypeCell[@name='product_container'])["+index+"]/XCUIElementTypeStaticText[@name='product_cross_price']"));
    }

    private Locator joinNowByIndexLocator(int index) {
        return new Locator("JOIN_NOW_BANNER", AppiumBy.iOSClassChain("**/XCUIElementTypeOther[`name == 'TTDSRP'`]/XCUIElementTypeTable/XCUIElementTypeCell[" + index + "]/XCUIElementTypeButton[`label == 'Gabung Sekarang' or label == 'Join Now'`]"));
    }


    @Override
    public boolean isSortByLowestPrice(int totalActivity) {
        boolean isValid = false;
        List<Double> priceList = new ArrayList<>();

        for (int i = 1; i < totalActivity+1; i++) {
            driverTools.swipeToElement(getProductCardFinalPriceLocator(i), Swipe.UP, 4);
            String currentActivityPrice = getProductCardPriceText(i);
            priceList.add(convertPrice(currentActivityPrice));

            logger.log("Product card index '" + i + "' price : '" + currentActivityPrice + "'");
        }

        double currentPrice = 0;
        for (double element : priceList) {
            if(element >= currentPrice) {
                currentPrice = element;
                isValid = true;
            } else {
                isValid = false;
                break;
            }
        }

        return isValid;
    }

    private String getProductCardPriceText(int i) {
        if (driverTools.waitForElementsToAppear(getProductCardCrossPriceLocator(i)).isPresent()) {
            return driverTools.getValueAttribute(getProductCardCrossPriceLocator(i),"label");
        } else {
            return driverTools.getValueAttribute(getProductCardFinalPriceLocator(i),"label");
        }
    }

    /**
     * handle price text "FREE" and "GRATIS", turn it into IDR 0
     */
    private String handlePriceText(String priceText) {
        if (priceText.equals("GRATIS") || priceText.equals("FREE")) {
            priceText = "IDR 0";
        }

        return priceText;
    }

    @Override
    public void selectTheProduct() {
        //FIXME: Pending implementation
    }

    @Override
    public void chooseProduct(String productName, int totalSwipe) {
        boolean elementPresent;
        elementPresent = driverTools.swipeToElementTillPresented(visibleProductName(productName),Swipe.UP,1).isPresent();
        for (int i = 0; i < totalSwipe; i++) {
            if (elementPresent) break;
            elementPresent = driverTools.swipeToElementTillPresented(visibleProductName(productName),Swipe.UP,1).isPresent();
        }
        driverTools.clickElementByCoordinate(visibleProductName(productName));
    }

    @Override
    public boolean isAllTabShown() {
        return driverTools.waitForElementToAppear(PRODUCT_ALL_CATEGORY_SELECTED_TAB).isPresent();
    }

    @Override
    public String getSelectedTab() {
        driverTools.waitForElementToPresence(PRODUCT_ALL_CATEGORY_SELECTED_TAB);
        return driverTools.getText(PRODUCT_ALL_CATEGORY_SELECTED_TAB).orElseThrow();
    }

    @Override
    public String selectDateFilter(String date) {
        CalendarPage<T> calendarPage = CalendarPage.getInstance(session);

        String targetDate = "";
        if(date.equalsIgnoreCase("random")) {
            int randomNumber = generateRandomInt(0,3);
            if(randomNumber != 2) {
                targetDate += randomNumber % 2 == 0 ? "today" : "tomorrow";
            } else {
                targetDate += String.valueOf(generateRandomInt(1,28));
            }
        } else {
            targetDate += date;
        }
        date = targetDate;

        switch(date.toLowerCase()) {
            case "today":
            case "hari ini":
                driverTools.click(TODAY_BUTTON);
                return "0";
            case "tomorrow":
            case "besok":
                driverTools.click(TOMORROW_BUTTON);
                return "00";
        }

        int numberDate = Integer.parseInt(date);
        String resultDate = "" + numberDate;

        if (numberDate <= 0 || numberDate > 31) {
            throw new DriverToolsException("Test data 'date' with value '" + date + "' is not valid, please check again");
        }

        final Locator SELECTED_DATE = getDateElement(numberDate);

        driverTools.waitForElementToAppear(FIRST_RESULT_TITLE);

        LocalDate currentDate = LocalDate.now();
        int currentDay = currentDate.getDayOfMonth();
        int currentMonthInt = currentDate.getMonthValue();
        int currentYearInt = currentDate.getYear();

        if (numberDate < currentDay) {

            int nextMonthInt = currentMonthInt+1 == 13 ? 1 : currentMonthInt+1;
            int selectedYear = currentMonthInt+1 == 13 ? currentYearInt+1 : currentYearInt;

            String nextMonthEng = String.valueOf(Month.of(nextMonthInt));
            nextMonthEng = nextMonthEng.substring(0, 1).toUpperCase() + nextMonthEng.substring(1).toLowerCase();
            String nextMonthId = monthEngToId(nextMonthEng);

            resultDate += "-" + nextMonthInt + "-" + selectedYear;

            driverTools.swipeUp();
        } else {
            resultDate += "-" + currentMonthInt + "-" + currentYearInt;
        }

        driverTools.click(OTHER_BUTTON);
        logger.log("Selected Date : " + resultDate);
        driverTools.waitForElementToAppear(SELECTED_DATE);
        driverTools.click(SELECTED_DATE);
        driverTools.click(SAVE_BUTTON);
        driverTools.waitForElementToAppear(FIRST_RESULT_TITLE,20);

        return resultDate;
    }

    @Override
    public void openDateFilter() {
        driverTools.waitForElementToAppear(DATE_FILTER, 5);
        driverTools.click(DATE_FILTER);
    }

    @Override
    public boolean verifyDateFilterChip() {
        driverTools.swipeToElement(TODAY_BUTTON, Swipe.UP, 8);

        List<Locator> dateOptions = Arrays.asList(TODAY_BUTTON, TOMORROW_BUTTON, OTHER_BUTTON);

        for (Locator dateOption : dateOptions) {
            Optional<WebElement> element = driverTools.waitForElementToAppear(dateOption, 3);

            if (element.isEmpty() || !element.get().isDisplayed()) {
                logger.log("One of the date filter buttons is not visible: " + dateOption);
                return false;
            }
        }
        logger.log("All date filter buttons are visible.");
        return true;
    }

    @Override
    public Locator getNextMonthElement(String nextMonthId, String nextMonthEng) {
        return new Locator("NEXT_MONTH", By.xpath("//XCUIElementTypeOther[@name='month_"+nextMonthId+"' or @name='month_"+nextMonthEng+"']"));
    }

    private Locator getDateElement(int numberDate) {
        return new Locator("SELECTED_DATE", AppiumBy.accessibilityId(""+numberDate));
    }

    @Override
    public boolean isFilterActivated(String filterType) {
        driverTools.waitForElementToPresence(FILTER_BUTTON).orElseThrow();
        Boolean filter_badge = driverTools.waitForElementToPresence(FILTER_BUTTON_GREEN_BADGE).isPresent();

        //verify selected filter green icon is applied
        logger.log("filter_badge : "+filter_badge);

        //both filter green icon much exist
        return filter_badge;
    }

    private Locator getBottomNavbarIMGLocator(String nameAttributePrefix) {
        return new Locator(nameAttributePrefix.toUpperCase() + "_BOTTOMFILTER", AppiumBy.xpath("//*[@name='"+nameAttributePrefix+"_btn']//XCUIElementTypeOther[2]"));
    }

    @Override
    public String getLocation() {
        driverTools.waitForElementToAppear(FIRST_RESULT_TITLE,20);
        WebElement element = driverTools.waitForElementToPresence(LOCATION_TEXT, 20).orElseThrow();
        String location = driverTools.getText(element);
        location = location.contains("...") ? location.replace("...","") : location;
        return location.stripTrailing();
    }

    private Locator productNameLocator(String product) {
        String enProduct = product.split("/")[0];
        String idProduct = product.split("/")[1];
        return new Locator(product.toUpperCase(), AppiumBy.iOSClassChain("**/XCUIElementTypeStaticText[`name == 'product_title' AND label =='" + enProduct + "' OR label =='" + idProduct + "'`]"));
    }

    @Override
    public void findProductByProductName(String product) {
        Locator productName = productNameLocator(product);
        driverTools.swipeToElement(productName, Swipe.UP, 15);
        driverTools.click(productName);
    }

    @Override
    public void clickLocation() {
        driverTools.waitForElementToPresence(LOCATION_TEXT);
        driverTools.clickElementByCoordinate(LOCATION_TEXT);
    }

    @Override
    public String selectRandomLocation() {
        driverTools.waitForElementToAppear(LOCATION_TAGS,20);
        List<WebElement> locations = driverTools.elements(LOCATION_TAGS).orElseThrow();
        int numberOfLocations = locations.size();

        logger.log("Location tags->" + (numberOfLocations-2));

        WebElement selectedLocation = locations.get(generateRandomInt(2, numberOfLocations-1));
        String location = driverTools.getText(selectedLocation);

        logger.log("selected Location ->" + location);
        driverTools.click(selectedLocation);
        return location;
    }

    @Override
    public boolean compareProductNameWithAPI(TTDSRPByLocationPOJO response, int productCount) {
        if(response.data == null || response.data.length == 0) {
            logger.log("No product found for this location");
            return driverTools.waitForElementToPresence(CHANGE_FILTER_BUTTON).isPresent();
        } else {
            driverTools.waitForElementToPresence(FIRST_RESULT_TITLE);
            for(int i = 0; i < response.data.length && i < productCount; i++) {
                String titleFromApi = response.data[i].translations.get(0).title.replace(" ", "");
                String titleFromUI = driverTools.getText(getProductNameByIndexLocator(i+1)).orElseThrow().replace(" ", "");
                driverTools.swipeUp();
                titleFromUI = titleFromUI.toLowerCase().replaceAll("[^A-Za-z0-9]","");
                titleFromApi = titleFromApi.toLowerCase().replaceAll("[^A-Za-z0-9]","");
                logger.log("Title From API--->" + titleFromApi);
                logger.log("Title From UI---->" + titleFromUI);
                if(!titleFromApi.equals(titleFromUI)) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public void clickBack() {
        WebElement backButtonElement = driverTools.waitForElementToPresence(BACK_BUTTON).orElseThrow();
        driverTools.clickElementByCoordinate(backButtonElement);
    }

    @Override
    public boolean isCampaignLabelPresent() {
        WebElement campaignLabelElement = driverTools.waitForElementToPresence(FIRST_PRODUCT_CARD_CAMPAIGN_LABEL).orElseThrow();

        Assertion.assertThat("Assert if campaign label is present on SRP", driverTools.getValueAttribute(campaignLabelElement, "name"), not("product_location"));
        return true;
    }

    @Override
    public boolean isDateFilterValid(String selectedDate, int totalProduct) {
        String targetDate = "";

        if (selectedDate.equals("0") || selectedDate.equals("00")) {
            LocalDate today = selectedDate.equals("00") ? LocalDate.now().plusDays(1) : LocalDate.now();
            targetDate = today.format(DateTimeFormatter.ofPattern("d MMM yyyy"));
        } else {
            targetDate = selectedDate;
        }

        int totalProducts = 0;
        boolean resultCheck = false;

        driverTools.waitForElementToPresence(FIRST_SEARCH_RESULT);
        if (driverTools.waitForElementToPresence(RESULT_COUNT).isPresent()) {
            int resultCount = Integer.parseInt(driverTools.getValueAttribute(RESULT_COUNT, "label").split(" ")[1]);

            totalProducts = Math.min(resultCount, totalProduct);
        }
        String labelText = "";
        for (int i = 1; i <= totalProducts; i++) {
            driverTools.swipeToElement(getDateProductCard(i), Swipe.UP, 3).orElseThrow();
          if(driverTools.isElementDisplayed(getDateProductCard(i))) {
             labelText = driverTools.getValueAttribute(getDateProductCard(i), "label");
          }

            // Extract date using regex
            String datePattern = "\\d{1,2} \\w+ \\d{4}";
            Pattern pattern = Pattern.compile(datePattern);
            Matcher matcher = pattern.matcher(labelText);

            if (matcher.find()) {
                String extractedDate = matcher.group(); // Extracted date (e.g., "20 Dec 2024")

                try {
                    // Compare dates
                    SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM yyyy", Locale.ENGLISH);
                    Date selectedDateConverted = dateFormat.parse(targetDate);
                    Date displayedDateConverted = dateFormat.parse(extractedDate);

                    logger.log("Selected Date: " + selectedDateConverted + "\nDisplayed Date: " + displayedDateConverted);

                    // Validate availability text
                    if (driverTools.isElementDisplayed(getProductAvailabilityText(i))) {
                        String availabilityText = driverTools.getText(getProductAvailabilityText(i)).orElseThrow();
                        Assertion.assertThat("Product '" + i + "' is having availability text",
                                isAvailabilityTextValid(targetDate, availabilityText), Is.is(true));
                    }
                    resultCheck = true;

                } catch (ParseException e) {
                    logger.log("Failed to parse or convert date: " + extractedDate);
                }
            } else {
                logger.log("No valid date found in label: " + labelText);
            }
        }
        return resultCheck;
    }

    @Override
    public void inputLocationSearchBar(String location) {
        if (location.contains("/")) location = location.split("/")[0];
        driverTools.waitForElementToAppear(LOCATION_SEARCHBAR_SRP);
        driverTools.sendKeys(LOCATION_SEARCHBAR_SRP, location);
    }

    @Override
    public String clickLocationSearchResult(String location, String locationType) {
        final Locator LOCATION_SUGGESTION = getLocationSuggestionLocator(location, locationType);
        driverTools.waitForElementToPresence(LOCATION_SUGGESTION, 30);
        String result = driverTools.getText(LOCATION_SUGGESTION).orElseThrow();
        driverTools.click(LOCATION_SUGGESTION);

        return result;
    }

    private static Locator campaignProductLocator(String productName) {
        return new Locator("CAMPAIGN_PRODUCT_NAME", AppiumBy.xpath("//*[@label='"+productName+"' and @name='product_title']//following-sibling::XCUIElementTypeStaticText[last()]"));
    }

    @Override
    public String findCampaignProduct(String productName, int totalSwipe) {
        driverTools.swipeToElement(campaignProductLocator(productName), Swipe.UP, totalSwipe);
        return driverTools.getText(campaignProductLocator(productName)).orElseThrow();
    }

    @Override
    public void selectLoyaltyProduct() {
        clickProductWithLpg();
     //todo for iOS locator not available
    }

    @Override
    public void selectLoyaltyProduct(String productName) {
        if (productName ==  null) {
            selectLoyaltyProduct();
        } else {
            driverTools.swipeToElement(loyaltyProductName(productName), Swipe.UP, 5);
            driverTools.clickElementByCoordinate(loyaltyProductName(productName));
        }
    }

    private Locator loyaltyProductName(String productName) {
        return new Locator("LOYALTY_PRODUCT_NAME", AppiumBy.xpath("//*[@name='product_title' and contains(@label,'"+productName+"') and @visible='true']"));
    }

    @Override
    public boolean verifyDisplayedLoyaltyBanner() {
        driverTools.waitForElementToAppear(LPG_HEADER_BANNER);
        driverTools.swipeRight(LPG_HEADER_BANNER);
        driverTools.clickElementByCoordinate(LOYALTY_LABEL_BANNER);
        boolean isLoyaltyBannerPresent = driverTools.waitForElementToAppear(LOYALTY_BOTTOMSHEET).isPresent();
        driverTools.click(CLOSE_BUTTON);
        return isLoyaltyBannerPresent;
    }

    @Override
    public String[] getProductList(int totalActivity) {
        driverTools.waitForElementToAppear(getProductNameByIndexLocator(1), 10);
        String[] productNamesOrderList = new String[totalActivity];

        for(int i = 0; i < totalActivity; i++) {
            Locator productNameLocator = getProductNameByIndexLocator(i+1);
            driverTools.swipeToElement(productNameLocator, Swipe.UP, 2);
            productNamesOrderList[i] = driverTools.getText(productNameLocator).orElseThrow().toLowerCase();
        }

        return productNamesOrderList;
    }

    @Override
    public boolean verifyDisplayedCurrency(String currency, String sectionType) {
        switch(sectionType.toLowerCase()) {
            case "product card":
                return verifyDisplayedCurrencyProductCard(currency);
            case "price filter":
                return verifyDisplayedCurrencyPriceFilter(currency);
            default:
                logger.fail("There is no option for sectionType '"+ sectionType +"'");
        }
        return false;
    }

    private Locator getPriceLocator(String currency){
        return new Locator("PRICE", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@label,'"+currency+"')]"));
    }

    @Override
    public boolean verifyDisplayedCurrencyProductCard(String currency) {
        List<WebElement> priceTextElements = Collections.singletonList(driverTools.waitForElementToPresence(getPriceLocator(currency), 20).orElseThrow());
        List<String> priceTexts = new ArrayList<>();

        for(WebElement e : priceTextElements) {
            priceTexts.add(driverTools.getText(e));
        }
        logger.log("PRODUCT_CARD_PRICE_TEXTS -> " + Arrays.toString(priceTexts.toArray(new String[0])));

        return CoreFunctions.checkCurrencyOnStringList(currency, priceTexts);
    }

    private Locator priceFilterParameterTexts(String currency) {
        return new Locator("PRICE_FILTER_PARAMETER_TEXTS", AppiumBy.xpath("//*[contains(@name,'ttd_srp_price_filter')]//following-sibling::XCUIElementTypeStaticText[@label='"+currency.toUpperCase()+"']"));
    }

    @Override
    public boolean verifyDisplayedCurrencyPriceFilter(String currency) {
        List<WebElement> priceFilterTextElements = driverTools.waitForPresenceElementsList(priceFilterParameterTexts(currency), 20).orElseThrow();
        List<String> priceFilterTexts = new ArrayList<>();

        for(WebElement e : priceFilterTextElements) {
            priceFilterTexts.add(driverTools.getValue(e));
        }

        logger.log("priceFilterTexts -> " + Arrays.toString(priceFilterTexts.toArray(new String[0])));

        return CoreFunctions.checkCurrencyOnStringList(currency, priceFilterTexts);
    }

    @Override
    public void openPriceFilter() {
        driverTools.waitForElementToAppear(PRICE_FILTER);
        driverTools.clickElementByCoordinate(PRICE_FILTER);
    }

    @Override
    public void closeFilterBottomsheet() {
        driverTools.waitForElementToAppear(CLOSE_BUTTON);
        driverTools.click(CLOSE_BUTTON);
    }

    private Locator getLocationSuggestionLocator(String location, String locationType) {
        String locationEn;
        String locationId;
        if (location.contains("/")) {
            locationEn = location.split("/")[0];
            locationId = location.split("/")[1];
        } else {
            locationEn = location;
            locationId = location;
        }

        String locationTypeEng = "";
        String locationTypeInd = "";

        switch (locationType.toLowerCase()) {
            case "country":
            case "negara":
                locationTypeEng += "Country";
                locationTypeInd += "Negara";
                break;
            case "region":
            case "wilayah":
                locationTypeEng += "Region";
                locationTypeInd += "Wilayah";
                break;
            case "city":
            case "kota":
                locationTypeEng += "City";
                locationTypeInd += "Kota";
                break;
            case "area":
                locationTypeEng += "Area";
                locationTypeInd += "Area";
                break;
        }

        return new Locator("LOCATION_SUGGESTION", AppiumBy.xpath("//XCUIElementTypeCell[./*[contains(@name,'ttd_destination_search_result_title') and (@label='"+locationEn+"' or @label='"+locationId+"')] and ./*[contains(@name,'ttd_destination_search_result_subtitle') and (contains(@label,'"+ locationTypeEng +"') or contains(@label,'"+ locationTypeInd +"'))]]/*[contains(@name,'ttd_destination_search_result_title')]"));
    }

    private Locator getProductAvailabilityText(int i) {
        return new Locator("PRODUCT_AVAILABILITY_"+i, AppiumBy.xpath("(//XCUIElementTypeStaticText[@name='product_availability'])["+i+"]"));
    }

    private Boolean isAvailabilityTextValid(String date, String availabilityText) {
        logger.log("date -> " + date);
        logger.log("availabilityText -> " + availabilityText);
        boolean todayAvailability = availabilityText.toLowerCase().contains("hari ini") || availabilityText.toLowerCase().contains("today");
        boolean tomorrowAvailability = availabilityText.toLowerCase().contains("hari ini") || availabilityText.toLowerCase().contains("today") ||
                availabilityText.toLowerCase().contains("besok") || availabilityText.toLowerCase().contains("tomorrow") ||
                availabilityText.toLowerCase().contains("sekarang") || availabilityText.toLowerCase().contains("now");

        if (date.equals("0")) {
            return todayAvailability;
        } else if (date.equals("00")) {
            return tomorrowAvailability;
        } else {
            if(tomorrowAvailability) {
                return true;
            }

            String[] productFullAvailability = availabilityText.split(" ");
            String[] productLastAvailability = Arrays.asList(productFullAvailability).subList(productFullAvailability.length - 3, productFullAvailability.length).toArray(new String[0]);

            String comparableTextProductLastAvailability = changeDateFormatToInt(productLastAvailability);

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            try {
                Date targetDateConverted = dateFormat.parse(date);
                Date displayedDateConverted = dateFormat.parse(comparableTextProductLastAvailability);
                return displayedDateConverted.compareTo(targetDateConverted) >= 0;
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private String changeDateFormatToInt(String[] productLastAvailability) {
        String resultDate = "";
        resultDate += productLastAvailability[0].substring(productLastAvailability[0].length()-2) + "-";
        resultDate += CoreFunctions.getMonthNumber(productLastAvailability[1]) + "-";
        if(productLastAvailability.length > 2 && productLastAvailability[2].length() == 4) {
            resultDate += productLastAvailability[2];
        } else {
            resultDate += LocalDate.now().getYear();
        }

        return resultDate;
    }

    private Locator getDateProductCard(int index) {
        return new Locator("PRODUCT_AVAILABILITY_"+index, AppiumBy.xpath("(//XCUIElementTypeStaticText[@name=\"product_availability\"])["+index+"]"));
    }

    @Override
    public void findProductByLabelName(String labelType) {
    }

    @Override
    public Boolean isPriceBelowTenNonIDR (String currency, String productCount) {
        int totalProduct = Integer.parseInt(productCount);
        List<String> priceTexts = new ArrayList<>();
        for (int i = 1; i < totalProduct; i++) {
            String currentActivityPrice = driverTools.getText(getPriceLocator(currency,i)).orElseThrow();

            driverTools.swipeUp();
            WebElement nextActivityPriceLocator = driverTools.element(getPriceLocator(currency,i+1)).orElseThrow();
            String nextActivityPrice = driverTools.getText(nextActivityPriceLocator);

            priceTexts.add(currentActivityPrice);
            priceTexts.add(nextActivityPrice);

            if (currentActivityPrice.matches(".*\\d.*")) {
                currentActivityPrice = currentActivityPrice.split(" ")[1];
                currentActivityPrice = currentActivityPrice.replace(",", ".");
                if (Double.parseDouble(currentActivityPrice) >= 10 && currentActivityPrice.contains(".")) {
                    return false;
                }
            }
            driverTools.swipeUp(nextActivityPriceLocator);
        }
        return true;
    }

    private Locator getPriceLocator(String currency, int index){
        return new Locator("PRICE", AppiumBy.xpath("(//XCUIElementTypeStaticText[contains(@label,'"+currency+"')])["+index+"]"));
    }

    @Override
    public Boolean isPriceAboveTenNonIDR(String currency, String productCount) {
        int totalProduct = Integer.parseInt(productCount);

        for (int i = 1; i < totalProduct; i++) {
            WebElement currentActivityPriceLocator = driverTools.element(getProductCardFinalPrice(currency, i), 20).orElseThrow();
            String currentActivityPrice = String.valueOf(driverTools.getText(getProductCardFinalPrice(currency, i)));

            driverTools.swipeUp();
            WebElement nextActivityPriceLocator = driverTools.element(getProductCardFinalPrice(currency, i + 1)).orElseThrow();
            String nextActivityPrice = String.valueOf(driverTools.getText(getProductCardFinalPrice(currency, i + 1)));

            if (currentActivityPrice.matches(".*\\d.*")) {
                // Extract numeric portion safely
                String[] parts = currentActivityPrice.split(" ");
                String priceValue = parts.length > 1 ? parts[1] : parts[0];

                // Remove thousand separators and non-numeric symbols
                priceValue = priceValue.replace(",", "").replaceAll("[^0-9.]", "");

                if (Double.parseDouble(priceValue) < 10) {
                    return false;
                }
            }
            driverTools.swipeUp(nextActivityPriceLocator);
        }
        return true;
    }

    @Override
    public Boolean verifyDisplayedPriceAmountProductCard(int currencyDecimalDigit, int productCount, GETProductListTTDSRPPOJO productDatafromAPI) {
        throw new NotImplementedException("This method is not implemented in iOS");
    }

    @Override
    public Boolean verifyDisplayedPriceAmountProductCard(int currencyDecimalDigit, int productCount, GETProductListTTDSRPPOJO productDatafromAPI, String currency) {
        if(driverTools.waitForElementToAppear(SEARCH_RESULT_COUNT,10).isPresent()) {
            String productCount1 = driverTools.getText(SEARCH_RESULT_COUNT).orElseThrow().split(" ")[1];
            int productCountInt = Integer.parseInt(productCount1);

            productCount = Math.min(productCountInt, productCount);
        }

        for(int i = 0; i < productCount; i++) {
            GETProductListTTDSRPPOJO.ProductCard productCard = productDatafromAPI.data.get(i);
            boolean needCheckCrossedPrice = productCard.startingCrossedPrice != productCard.startingFinalPrice;
            String productTitle = productCard.translations.get(0).title;

            double productFinalPriceAPI = productCard.startingFinalPrice / Math.pow(10, currencyDecimalDigit);
            double productCrossedPriceAPI = productCard.startingCrossedPrice / Math.pow(10, currencyDecimalDigit);

            if(currencyDecimalDigit == 2) {
                productFinalPriceAPI = productFinalPriceAPI % 1 < 0.5 ? Math.floor(productFinalPriceAPI) : Math.ceil(productFinalPriceAPI);
                productCrossedPriceAPI = productCrossedPriceAPI % 1 < 0.5 ? Math.floor(productCrossedPriceAPI) : Math.ceil(productCrossedPriceAPI);
            }

            WebElement productFinalPriceElement = driverTools.swipeToElement(getProductFinalPriceByTitle(productTitle, currency), Swipe.UP, 3).orElseThrow();
            String price = driverTools.getValueAttribute(productFinalPriceElement, "label");
            if (price.equalsIgnoreCase("free") || price.equalsIgnoreCase("gratis"))
                price = currency + " 0";
            double productFinalPriceUI = convertPrice(price);
            if(currencyDecimalDigit == 2 && productFinalPriceUI < 10) {
                productFinalPriceUI = productFinalPriceUI % 1 < 0.5 ? Math.floor(productFinalPriceUI) : Math.ceil(productFinalPriceUI);
            }

            if(needCheckCrossedPrice) {
                WebElement productCrossedPriceElement = driverTools.element(getProductCrossedPriceByTitle(productTitle, currency), 3).orElseThrow();
                double productCrossedPriceUI = convertPrice(driverTools.getValueAttribute(productCrossedPriceElement, "label"));
                if(currencyDecimalDigit == 2 && productCrossedPriceUI < 10) {
                    productCrossedPriceUI = productCrossedPriceUI % 1 < 0.5 ? Math.floor(productCrossedPriceUI) : Math.ceil(productCrossedPriceUI);
                }

                Assertion.assertThat("Assert if final price product '"+productTitle+"' is correct", productFinalPriceUI, Is.is(productFinalPriceAPI));
                Assertion.assertThat("Assert if crossed price product '"+productTitle+"' is correct", productCrossedPriceUI, Is.is(productCrossedPriceAPI));
            } else {
                Assertion.assertThat("Assert if final price product '"+productTitle+"' is correct", productFinalPriceUI, Is.is(productFinalPriceAPI));
            }
        }

        return true;
    }

    @Override
    public Boolean verifyRedirectionToSearchResultPage(String sectionType) {

        boolean isSearchBarDisplayed = driverTools.waitForElementToPresence(SEARCH_ACTIVITIES_BUTTON, 30).isPresent();
        boolean isSelectedCategoryCorrect;
        String selectedCategory = "";

        boolean isSelectedSort = false;

        clickSortFilter();
        if (sectionType.equalsIgnoreCase("new activities")) {
            isSelectedSort = driverTools.isElementDisplayed(NEWLY_ADDED);
        }else{
            driverTools.waitForElementToPresence(CLOSE_BUTTON);
            WebElement srpSelectedCategory_title = driverTools.waitForElementToPresence(SRP_SELECTED_CATEGORY_TITLE,15).orElseThrow();
            selectedCategory = driverTools.getText(srpSelectedCategory_title).toLowerCase();
        }
        WebElement closeButton = driverTools.waitForElementToPresence(CLOSE_BUTTON).orElseThrow();
        driverTools.clickElementByCoordinate(closeButton);

        switch (sectionType.toLowerCase()) {
            case "new activities":
                boolean isAllLocationDisplayed = getLocation().equals("All locations") || getLocation().equals("Semua lokasi");
                return isSelectedSort && isSearchBarDisplayed && isAllLocationDisplayed;
            case "popular events":
                isSelectedCategoryCorrect = selectedCategory.equals("semua event") || selectedCategory.equals("all events");
                return isSelectedCategoryCorrect && isSearchBarDisplayed;
            case "popular attractions":
                isSelectedCategoryCorrect = selectedCategory.equals("semua atraksi") || selectedCategory.equals("all attractions");
                return isSelectedCategoryCorrect && isSearchBarDisplayed;
            default:
                logger.fail("There is no option for sectionType '"+ sectionType +"'");
        }

        return false;
    }
    @Override
    public boolean checkBoostingLabels(String language) {
        logger.log("language -> " + language);

        final String[] indonesianLabels = new String[] { "Baru", "Ekslusif di tiket.com", "Pilihan tiket.com", "Rekomendasi", "Wajib dikunjungi", "Wajib dicoba"};
        final String[] englishLabels = new String[] { "New", "Exclusive on tiket.com", "tiket.com's choice", "Recommended", "Must visit", "Must try"};

        Locator boostingLabelLocator = getBoostingLabel(indonesianLabels, englishLabels);

        driverTools.swipeToPresentedElement(boostingLabelLocator, Swipe.UP, 30).orElseThrow();
        List<WebElement> boostingLabels = driverTools.elementsUntilExist(boostingLabelLocator).orElseThrow();

        for (WebElement element : boostingLabels) {
            String label = element.getAttribute("label");
            if (label == null || label.trim().isEmpty()) {
                label = driverTools.getText(element);
            }
            logger.log("Found boosting label: " + label);
            if (language.equals("id")) {
                Assertion.assertThat("Label " + label + " is in Indonesian and is valid", label, in(indonesianLabels));
            } else {
                Assertion.assertThat("Label " + label + " is in English and is valid", label, in(englishLabels));
            }
        }
        return true;
    }
    private Locator getBoostingLabel(String[] indonesianLabels, String[] englishLabels) {
        StringBuilder locatorString = new StringBuilder("//XCUIElementTypeStaticText[");

        for (String label : indonesianLabels) {
            locatorString.append("@label=\"").append(label).append("\" or ");
        }
        for (String label : englishLabels) {
            locatorString.append("@label=\"").append(label).append("\" or ");
        }

        String finalXpath = removeSuffixIfExists(locatorString.toString(), " or ") + "]";

        return new Locator("BOOSTING_LABEL", AppiumBy.xpath(finalXpath));
    }
    public void clickSearchBar() {
        driverTools.clickElementByCoordinate(SRP_SEARCHBAR);
    }

    private Locator getProductCrossedPriceByTitle(String productTitle, String currency) {
        return new Locator("CROSSED_PRICE", AppiumBy.xpath("(//*[@name='product_title' and @label='"+productTitle+"']/following-sibling::*[contains(@label, '"+currency+"')])[1]"));
    }

    private Locator getProductFinalPriceByTitle(String productTitle, String currency) {
        return new Locator("FINAL_PRICE", AppiumBy.xpath("(//*[@name='product_title' and @label='"+productTitle+"']/following-sibling::*[contains(@label, '"+currency+"') or contains(@label,'FREE') or contains(@label,'GRATIS')])[last()]"));
    }

    @Override
    public boolean isCartContainsLoyaltyBadge() {
        return driverTools.waitForElementToPresence(LOYALTY_BENEFIT_IN_CARD,10).isPresent();
    }

    @Override
    public int getProductCountOnSrp() {
        String productCount = driverTools.getText(SEARCH_RESULT_COUNT).orElseThrow().split(" ")[1];
        return Integer.parseInt(productCount);
    }

    @Override
    public boolean verifyLoyaltyIconOrCampaignOnSRPCard(String tier) {
        return driverTools.isElementDisplayed(getLoyaltyIconInCardTier(tier)) || driverTools.isElementDisplayed(FIRST_PRODUCT_CARD_CAMPAIGN_LABEL);
    }

    @Override
    public String getProductBelowPrice(double maxPrice, int totalActivity) {
        driverTools.waitForElementToAppear(getProductNameByIndexLocator(1), 10);
        int belowPriceIndex = -1;
        for (int i = 1; i <= totalActivity; i++) {
            driverTools.swipeToElement(getProductCardFinalPriceLocator(i), Swipe.UP, 4);
            if (driverTools.waitForElementsToAppear(getProductCardFinalPriceLocator(i)).isEmpty()) {
                continue;
            }

            String priceText = handlePriceText(driverTools.getValueAttribute(getProductCardFinalPriceLocator(i), "label"));
            logger.log("Product card index '" + i + "' final price text -> '" + priceText + "'");
            double price = priceText.matches(".*\\d.*") ? convertPriceSafely(priceText) : 0.0;
            if(price<1){
                belowPriceIndex = i;
                break;
            }
        }

        if (belowPriceIndex == -1) {
            logger.log("No product found below " + maxPrice + " within " + totalActivity + " scanned products");
            driverTools.swipeDown(10);
            return "";
        }

        String productName = driverTools.getText(getProductNameByIndexLocator(belowPriceIndex)).orElseThrow();
        logger.log("Found product below " + maxPrice + " -> '" + productName + "'");
        driverTools.swipeToElement(getProductNameByIndexLocator(belowPriceIndex), Swipe.UP, 4);
        return productName;
    }

    @Override
    public void selectProduct(String productName) {
        Locator product = getProductCardByName(productName);
        WebElement productElement = driverTools.swipeToElement(product, Swipe.UP, 10).orElseThrow();
        driverTools.click(productElement);
    }

    private Locator getProductCardByName(String productName) {
        return new Locator("PRODUCT_CARD_" + productName.toUpperCase(),
                AppiumBy.xpath("//XCUIElementTypeStaticText[@name='product_title' and contains(@label,'" + productName + "')]"));
    }
}
