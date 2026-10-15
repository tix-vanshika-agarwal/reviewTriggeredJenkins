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
import com.tiket.pojo.ttd.ResultLoopVariables;
import com.tiket.pojo.ttd.TTDSRPByLocationPOJO;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.Configs;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.NotImplementedException;
import org.hamcrest.CoreMatchers;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.regex.Pattern;

import static com.tiket.core.CoreFunctions.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public class TTDSearchResultPageAndroid<T extends WebDriver> extends TTDSearchResultPage<T> {

    public TTDSearchResultPageAndroid(Session<T> session) {
        super(session);
    }

    public static final Locator NO_DATA_FOUND_BANNER = new Locator("NO_DATA_FOUND_BANNER", AppiumBy.xpath("//*[contains(@resource-id,'tds_main_info_container_view')] | //*[contains(@resource-id,'tds_iv_info_view')]"));
    public static final Locator KEYWORD_SUGGESTION = new Locator("KEYWORD_SUGGESTION", AppiumBy.xpath("//*[contains(@resource-id,'tds_chips_text')] | //*[contains(@resource-id,'suggestion_container') and ./*[contains(@resource-id,'iv_icon')]]/*[contains(@resource-id,'tv_name')] | //*[contains(@resource-id,'suggestion_container')]//following-sibling::*[contains(@resource-id,'tv_item_name')]"));
    public static final Locator RESULT_CONTENT = new Locator("RESULT_CONTENT", AppiumBy.id("suggestion_container"));
    public static final Locator SEE_RESULT_FOR = new Locator("SEE_RESULT_FOR", AppiumBy.id("tv_see_all_result"));
    private static final Locator JOIN_NOW_BUTTON = new Locator("JOIN_NOW_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'tds_btn') and (@text='Join Now' or @text='Gabung Sekarang')]"));
    private static final Locator SEE_ALL_PROMO = new Locator("SEE_ALL_PROMO", AppiumBy.xpath("//*[contains(@resource-id,'v_campaign_filter')]//*[contains(@resource-id,'btn_see_all')]"));
    //    SRP app_bar element
    public static final Locator OPEN_CATEGORIES = new Locator("OPEN_CATEGORIES", AppiumBy.xpath("//*[@class='android.view.ViewGroup' and ./*[@text='Category' or @text='Kategori']]"));
    public static final Locator SRP_CATEGORIES_TITLE = new Locator("SRP_CATEGORIES_TITLE", AppiumBy.xpath("//*[@class='android.view.ViewGroup' and ./*[@text='Category' or @text='Kategori']]"));

    public static final Locator SRP_SUBCATEGORIES_TITLE = new Locator("SRP_SUBCATEGORIES_TITLE", AppiumBy.id("tds_chips_text"));
    private static final Locator SRP_SELECTED_CATEGORY_TITLE = new Locator("SRP_SELECTED_CATEGORY_TITLE", AppiumBy.xpath("//*[contains(@resource-id,'cg_selected_filters')]//*[contains(@resource-id,'tds_chips_text')]"));
    private static final Locator CATEGORY_PAGE_TITLE = new Locator("CATEGORY_PAGE_TITLE", AppiumBy.xpath("//*[contains(@resource-id,'ll_destination_info')]//*[contains(@resource-id,'tv_title')]"));
    private static final Locator SRP_SEARCHBAR = new Locator("SRP_SEARCHBAR", AppiumBy.id("tv_search_keyword"));
    private static final Locator NEWLY_ADDED = new Locator("NEWLY_ADDED", AppiumBy.xpath("//*[@selected='true' and (contains(@text,'Newly Added') or contains(@text,'Baru Ditambahkan'))]"));

    //    SRP element identifier
    public static final Locator SRP_APP_BAR = new Locator("SRP_APP_BAR", AppiumBy.id("app_bar"));
    public static final Locator SRP_RESULT = new Locator("SRP_RESULT", AppiumBy.id("rv_search_result"));
    public static final Locator SRP_FILTER_BAR = new Locator("SRP_FILTER_BAR", AppiumBy.xpath("//*[contains(@resource-id,'bottom_bar_view')] | //*[contains(@resource-id,'iv_icon_filter')]"));
    public static final Locator SRP_RESULT_COUNT = new Locator("SRP_RESULT_COUNT", AppiumBy.id("tv_product_count"));
    public static final Locator SORT_BUTTON = new Locator("SORT_BUTTON", AppiumBy.id("iv_icon_filter"));
    public static final Locator SORT_SELECTOR_PAGE = new Locator("SORT_SELECTOR_PAGE", AppiumBy.id("tds_list_selector_motion_layout"));
    public static final Locator SORT_APPLY_BTN = new Locator("SORT_APPLY_BTN", AppiumBy.xpath("//*[contains(@resource-id,'tds_button_view') and (contains(@text,'Apply') or contains(@text,'Terapkan'))]"));
    public static final Locator RESET_BUTTON = new Locator("RESET_BUTTON", AppiumBy.xpath("//*[@text='Reset']"));

    //    first TTD card, srp element
    public static final Locator FIRST_CARD_TTD = new Locator("FIRST_CARD_TTD ", AppiumBy.xpath("(//*[contains(@resource-id, 'cv_card_container')])[1]"));
    public static final Locator FIRST_TTD_PRODUCT_NAME = new Locator("FIRST_CARD_TTD_PRODUCT_NAME ", AppiumBy.xpath("(//*[contains(@resource-id, 'cv_card_container') and .//*[contains(@resource-id,'tv_name')]])[1]//*[contains(@resource-id,'tv_name')]"));
    public static final Locator PRODUCT_CARD_LOCATION = new Locator("PRODUCT_CARD_LOCATION ", AppiumBy.id("tv_location_info"));
    public static final Locator RESULT_CARD_NAME = new Locator("RESULT_CARD_NAME ", AppiumBy.id("tv_name"));
    public static final Locator SORT_BY_NEAREST = new Locator("SORT_BY_NEAREST", AppiumBy.xpath("(//*[contains(@resource-id, 'tds_list_selector_motion_layout')]//*[contains(@resource-id, 'tds_selection_area_right')])[1]"));
    private static final Locator CLOSE_BUTTON_CHIP = new Locator("CLOSE_BUTTON_CHIP", AppiumBy.id("tds_chips_icon_right_iv"));
    public static final Locator APP_PERMISSION_ALLOW = new Locator("APP_PERMISSION_ALLOW", AppiumBy.id("tds_btn_primary_info_view"));
    public static final Locator PERMISSION_TYPE_ALLOW_ONCE = new Locator("PERMISSION_TYPE_ALLOW_ONCE", AppiumBy.xpath("//*[contains(@resource-id,'permission_allow_one_time_button')]"));
    public static final Locator ACTIVITY_CARD_FOOTER = new Locator("ACTIVITY_CARD_FOOTER", AppiumBy.xpath("(//*[contains(@resource-id, 'll_product_footer_v2')])[1]"));
    public static final Locator NEXT_CARD_TTD = new Locator("NEXT_CARD_TTD", AppiumBy.xpath("(//*[contains(@resource-id, 'cv_card_container')])[2]"));
    public static final Locator FILTER_BUTTON = new Locator("FILTER_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'tds_title_tv') and @text = 'Filter']/parent::*"));
    public static final Locator FILTER_TIKET_CLEAN = new Locator("FILTER_TIKET_CLEAN", AppiumBy.xpath("//*[contains(@resource-id, 'tds_chips_text') and @text='tiket CLEAN']"));
    public static final Locator FILTER_INSTANT_CONFIRMATION = new Locator("FILTER_INSTANT_CONFIRMATION", AppiumBy.xpath("//*[contains(@resource-id, 'tds_chips_text') and @text='Instant Confirmation']"));
    public static final Locator FILTER_INSTANT_PASS = new Locator("FILTER_INSTANT_PASS", AppiumBy.xpath("//*[contains(@resource-id, 'tds_chips_text') and @text='Instant Pass']"));
    public static final Locator FILTER_TIKET_FLEXI = new Locator("FILTER_TIKET_FLEXI", AppiumBy.xpath("//*[contains(@resource-id, 'tds_chips_text') and @text='tiket FLEXI']"));
    public static final Locator APPLY_BUTTON = new Locator("APPLY_BUTTON", AppiumBy.id("btn_apply"));
    private static final Locator CLOSE_GPS = new Locator("CLOSE_GPS", AppiumBy.id("tds_btn_secondary_info_view"));
    private static final Locator ALL_TAB = new Locator("ALL_TAB", AppiumBy.xpath("//*[contains(@resource-id,'tds_title_tab_layout') and contains(@text,'All') or contains(@text,'Semua')]"));
    private static final Locator DATE_FILTER = new Locator("DATE_FILTER", AppiumBy.xpath("//*[contains(@resource-id,'tds_title_tv') and (@text='Tanggal' or @text='Date')]"));
    public static final Locator LOYALTY_BENEFIT_FILTER = new Locator("LOYALTY_BENEFIT_FILTER", AppiumBy.xpath("//*[contains(@resource-id, 'tds_chips_text') and contains(@text, 'Benefit')]"));
    private static final Locator PRICE_FILTER = new Locator("DATE_FILTER", AppiumBy.xpath("//*[contains(@resource-id,'tds_title_tv') and (@text='Harga' or @text='Price')]"));
    private static final Locator CLOSE_BUTTON = new Locator("CLOSE_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'btn_close')]"));
    public static final Locator FILTER_BUTTON_GREEN_BADGE = new Locator("FILTER_BUTTON_BADGE", AppiumBy.id("badge_filter"));
    private static final Locator SRP_LOCATION = new Locator("SRP_LOCATION", AppiumBy.xpath("//*[contains(@resource-id,'tds_search_right_text')] | //*[contains(@resource-id,'container_destination_selector')]/*[contains(@resource-id,'tv_location')]"));
    private static final Locator PRODUCT_NAME = new Locator("PRODUCT_NAME", AppiumBy.xpath("//*[contains(@resource-id,'cv_card_container')]//*[contains(@resource-id,'tv_name')] | //*[contains(@resource-id,'suggestion_container')]/*[contains(@resource-id,'tv_title')]"));
    public static final Locator LOCATION_SEARCHBAR = new Locator("LOCATION_SEARCHBAR", AppiumBy.id("tds_search_right_text"));
    public static final Locator SEARCH_RESULT_TITLE = new Locator("SEARCH_RESULT_TITLE", AppiumBy.id("//*[contains(@resource-id,'tv_item_name')] | //*[contains(@resource-id,'tv_title')]"));
    public static final Locator CHANGE_FILTER_BUTTON = new Locator("CHANGE_FILTER_BUTTON", AppiumBy.id("tds_button_view"));
    public static final Locator  EMPTY_LIST_TABLE = new Locator("EMPTY_LIST_TABLE", AppiumBy.id("tds_iv_info_view"));
    public static final Locator NEXT_MONTH_BUTTON = new Locator("NEXT_MONTH_BUTTON", AppiumBy.id("iv_next_month"));
    public static final Locator BACK_BUTTON_SRP = new Locator("BACK_BUTTON_SRP", AppiumBy.id("iv_back"));
    public static final Locator FIRST_PRODUCT_CARD_CAMPAIGN_LABEL = new Locator("FIRST_PRODUCT_CARD_CAMPAIGN_LABEL", AppiumBy.xpath("(//*[contains(@resource-id,'cv_card_container')])[1]//*[contains(@resource-id,'ll_campaign_container')]"));
    public static final Locator FILTER_RESET_BTN = new Locator("FILTER_RESET_BTN", AppiumBy.id("btn_reset"));

    public static final Locator DATE_FILTER_CHIPS = new Locator("DATE_FILTER_CHIPS", AppiumBy.xpath("//*[contains(@resource-id,'crf_bottom_sheet')]//*[contains(@resource-id,'tds_chips_container')]"));
    public static final Locator SAVE_DATE_FILTER = new Locator("SAVE_DATE_FILTER", AppiumBy.id("tds_btn"));
    private static final Locator LPG_LABEL_CARD = new Locator("LPG_LABEL_CARD", AppiumBy.id("tv_lpg"));
    private static final Locator LPG_HEADER_BANNER = new Locator("LPG_HEADER_BANNER", AppiumBy.xpath("//*[contains(@resource-id,'tds_subtitle_notification_banner')]/*[(contains(@text,'Jaminan Harga Termurah') or contains(@text,'Lowest Price Guarantee'))]"));
    private static final Locator LOCATION_SEARCHBAR_SRP = new Locator("LOCATION_SEARCHBAR_SRP", AppiumBy.xpath("//*[contains(@resource-id,'tds_search_et')]"));
    private static final Locator LOYALTY_LABEL_CARD = new Locator("LOYALTY_LABEL_CARD", AppiumBy.id("price_loyalty_badge"));
    public static final Locator PRODUCT_CARD_PRICE_TEXTS = new Locator("PRODUCT_CARD_PRICE_TEXTS", AppiumBy.xpath("//*[contains(@resource-id,'tv_slash_price') and not(contains(@resource-id,'percentage'))] | //*[contains(@resource-id,'tv_main_price')]"));
    private static final Locator PRICE_FILTER_PARAMETER_TEXTS = new Locator("PRICE_FILTER_PARAMETER_TEXTS", AppiumBy.xpath("//*[contains(@resource-id,'tds_box_input_text')]"));
    private static final Locator LOYALTY_LABEL_BANNER = new Locator("LOYALTY_LABEL_CARD", AppiumBy.xpath("//*[contains(@resource-id,'tds_subtitle_notification_banner')]//*[contains(@text,'Benefit')]"));
    private static final Locator LOYALTY_BOTTOMSHEET = new Locator("LOYALTY_BOTTOMSHEET", AppiumBy.xpath("//*[contains(@resource-id, 'tv_title_benefit') and contains(@text, 'Benefit')]"));
    private static final Locator PRICE_FINAL_TEXTS = new Locator("PRICE_FINAL_TEXTS", AppiumBy.xpath("//*[contains(@resource-id,'tv_price_after_discount')]"));
    public static final Locator DATE_FILTER_CHIP_TODAY = new Locator("DATE_FILTER_CHIP_TODAY", AppiumBy.xpath("(//*[contains(@resource-id,'tds_chips_container')]//*[contains(@resource-id,'tds_chips_text') and (contains(@text, 'Today') or contains(@text, 'Hari Ini'))])[1]"));
    public static final Locator DATE_FILTER_CHIP_TOMORROW = new Locator("DATE_FILTER_CHIP_TOMORROW", AppiumBy.xpath("(//*[contains(@resource-id,'tds_chips_container')]//*[contains(@resource-id,'tds_chips_text') and (contains(@text, 'Tomorrow') or contains(@text, 'Besok'))])[1]"));
    public static final Locator DATE_FILTER_CHIP_OTHER = new Locator("DATE_FILTER_CHIP_OTHER", AppiumBy.xpath("(//*[contains(@resource-id,'tds_chips_container')]//*[contains(@resource-id,'tds_chips_text') and (contains(@text, 'Lainnya') or contains(@text, 'Other'))])[last()]"));
    public static final Locator BOOSTING_LABEL = new Locator("BOOSTING_LABEL", AppiumBy.xpath("//*[contains(@resource-id,'tv_boosting')]"));

    @Override
    public boolean isLpgHeaderBannerDisplayed() {
        return driverTools.waitForElementToAppear(LPG_HEADER_BANNER,10).isPresent();
    }

    @Override
    public void clickLpgHeaderBanner() {
        driverTools.click(LPG_HEADER_BANNER);
    }

    @Override
    public boolean checkLpgLabelCard(int totalCard, String language) {
        driverTools.waitForElementToAppear(LPG_LABEL_CARD);
        for (int i = 0; i < totalCard; i++) {
            String productName = getProductName();
            assertLpgCard(productName);

            if (Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
                TTDPage<T> ttdPage = TTDPage.getInstance(session);
                ttdPage.assertProductLocation(productName, "indonesia", language);
            }

            driverTools.swipeToElement(lpgLabelCardNotName(productName), Swipe.UP, 2);
            if (driverTools.isElementDisplayed(visibleProductName(productName)))
                driverTools.swipeByDistance(SRP_RESULT, Swipe.UP, 0.5);

            int totalProduct = driverTools.elements(PRODUCT_NAME).orElseThrow().size();
            logger.log("totalProduct: " + totalProduct);
            if (productName.equals(getProductName()) && totalProduct > 1) {
                productName = driverTools.elements(PRODUCT_NAME).orElseThrow().get(1).getText();
                assertLpgCard(productName);
                logger.softWarning("End of product list reached");
                break;
            }
        }
        return true;
    }

    private void assertLpgCard(String productName) {
        driverTools.swipeToElement(lpgLabelCardLocator(productName), Swipe.DOWN, 1);
        driverTools.swipeToElement(lpgLabelCardLocator(productName), Swipe.UP, 1);
        Assertion.assertThat("Show the LPG label on the product card: " + productName, driverTools.isElementDisplayed(lpgLabelCardLocator(productName)), is(true));
    }

    private String getProductName() {
        return driverTools.getText(PRODUCT_NAME).orElseThrow();
    }

    private Locator visibleProductName(String productName) {
        return new Locator("VISIBLE_PRODUCT_NAME_" + productName.toUpperCase(), AppiumBy.xpath("//*[contains(@resource-id,'tv_name') and @text='" + productName + "']"));
    }

    private Locator lpgLabelCardLocator(String productName) {
        return new Locator("LPG_LABEL_CARD_" + productName.toUpperCase(), AppiumBy.xpath("//*[contains(@resource-id, 'tv_name') and @text='" + productName + "']//parent::*//parent::*//preceding-sibling::*[contains(@resource-id, 'view_srp_product_imageview')]//*[contains(@resource-id, 'tv_lpg')]"));
    }

    private Locator lpgLabelCardNotName(String productName) {
        return new Locator("LPG_LABEL_CARD_NOT_" + productName.toUpperCase(), AppiumBy.xpath("//*[contains(@resource-id, 'tv_name') and not(@text='" + productName + "')]//parent::*//parent::*//preceding-sibling::*[contains(@resource-id, 'view_srp_product_imageview')]//*[contains(@resource-id, 'tv_lpg')]"));
    }

    @Override
    public void clickProductWithLpg() {
        driverTools.click(LPG_LABEL_CARD);
    }


    @Override
    public boolean isAutoCompleteSuggestionValid(String keyword, String type, String pageType) {
        final Locator NAME_AUTOCOMPLETE_SUGGEST = getNameSuggestLocator(keyword, pageType);
        final Locator TYPE_AUTOCOMPLETE_SUGGEST = getTypeSuggestLocator(keyword, pageType);

        boolean nameAutoCompleteElementDisplayed = driverTools.swipeToElement(NAME_AUTOCOMPLETE_SUGGEST, Swipe.UP, 3).isPresent();
        boolean typeAutoCompleteElementDisplayed = driverTools.swipeToElement(TYPE_AUTOCOMPLETE_SUGGEST, Swipe.UP, 3).isPresent();
        logger.log("nameAutoCompleteElementDisplayed: " + nameAutoCompleteElementDisplayed +
                "\n" + "typeAutoCompleteElementDisplayed: " + typeAutoCompleteElementDisplayed);
        String typeLocationText = driverTools.getText(TYPE_AUTOCOMPLETE_SUGGEST).orElseThrow();
        boolean checkIfTypeSameAsParams = type.toLowerCase().contains(typeLocationText.toLowerCase());

        if (!checkIfTypeSameAsParams) {
            logger.warn("Element TYPE_AUTOCOMPLETE_SUGGEST showing '"+ typeLocationText.toLowerCase() +"' instead of '"+ type +"'");
        }

        return nameAutoCompleteElementDisplayed && typeAutoCompleteElementDisplayed && checkIfTypeSameAsParams;
    }

    @Override
    public boolean isKeywordSuggestionDisplayed(String keyword) {
        Optional<WebElement> resultContentOptional = driverTools.waitForElementToAppear(RESULT_CONTENT, 5);
        Optional<WebElement> keywordSuggestionOptional = driverTools.swipeToElement(KEYWORD_SUGGESTION, Swipe.UP,10);

        if(keywordSuggestionOptional.isPresent()) {
            logger.log("Keyword Suggestion Element is detected");
            return keywordSuggestionOptional.get().isDisplayed();
        } else if(resultContentOptional.isPresent()) {
            logger.log("Result Content Element is detected");
            return resultContentOptional.get().isDisplayed();
        }

        return false;
    }

    @Override
    public void selectLocation(String keyword, String pageType) {
        final Locator NAME_AUTOCOMPLETE_SUGGEST = getNameSuggestLocator(keyword, pageType);
        driverTools.click(NAME_AUTOCOMPLETE_SUGGEST);
    }

    @Override
    public String pickAndClickOneResult() {
        driverTools.waitForElementToAppear(SEE_RESULT_FOR);
        List<WebElement> totalresultDisplayedList = driverTools.elements(SEARCH_RESULT_TITLE).orElseThrow();
        int randomIndex = generateRandomInt(1, totalresultDisplayedList.size());
        List<WebElement> webElementList = driverTools.elements(PRODUCT_NAME).orElseThrow();
        int randomNumber = generateRandomInt(1, webElementList.size());

        final Locator oneOfResultTitleLocator = getRandomResultTitleLocator(randomIndex);
        WebElement oneOfResultTitleElement = driverTools.waitForElementToAppear(oneOfResultTitleLocator).orElseThrow();
        String oneOfResultTitleText = driverTools.getText(oneOfResultTitleElement);

        driverTools.click(oneOfResultTitleElement);

        return oneOfResultTitleText;
    }

    private Locator getNameSuggestLocator(String keyword, String page) {
        if (page.equalsIgnoreCase("explore")) {
            return new Locator("NAME_AUTOCOMPLETE_SUGGEST", AppiumBy.xpath("//*[contains(@resource-id,'tv_suggestion_name') and @text='"+ keyword +"']"));
        }
        return new Locator("NAME_AUTOCOMPLETE_SUGGEST", AppiumBy.xpath("//*[contains(@resource-id,'tv_item_name') and @text='"+ keyword +"'] | //*[contains(@resource-id,'tv_destination') and @text='"+ keyword +"']"));
    }

    private Locator getTypeSuggestLocator(String keyword, String page) {
        if (page.equalsIgnoreCase("explore")) {
            return new Locator("TYPE_AUTOCOMPLETE_SUGGEST", AppiumBy.xpath("//*[contains(@resource-id,'tv_suggestion_name') and @text='"+ keyword +"']/parent::*//*[contains(@resource-id,'tv_suggestion_location')]"));
        }
        return new Locator("TYPE_AUTOCOMPLETE_SUGGEST", AppiumBy.xpath("//*[contains(@resource-id,'tv_item_name') and @text='"+ keyword +"']/following-sibling::*[1]//*[contains(@resource-id, 'tv_location')] | //*[contains(@resource-id,'tv_destination') and @text='"+ keyword +"']/following-sibling::*[contains(@resource-id, 'tv_parent_destination')]"));
    }

    private Locator getRandomResultTitleLocator(int randomIndex) {
        return new Locator("RESULT_TITLE", AppiumBy.xpath("(//*[contains(@resource-id,'tv_item_name')])["+ randomIndex +"] | (//*[contains(@resource-id,'tv_title')])["+ randomIndex +"]"));
    }
    
    @Override
    public boolean isSearchResultPageShown() {
//        use this method to verify the SRP is shown in general
        Optional<WebElement> closeGps = driverTools.waitForElementToAppear(CLOSE_GPS, 5);
        if (closeGps.isPresent()) driverTools.click(closeGps.orElseThrow());

        boolean isAppBarDisplayed = driverTools.waitForElementToAppear(SRP_APP_BAR).isPresent();
        boolean isSrpResultDisplayed = driverTools.waitForElementToAppear(SRP_RESULT).isPresent();
        boolean isSrpFilterBarDisplayed = driverTools.waitForElementToAppear(SRP_FILTER_BAR).isPresent();
        boolean isFirstCardTtdDisplayed = driverTools.waitForElementToAppear(FIRST_CARD_TTD,5).isPresent();
        boolean isNoResultBannerDisplayed = driverTools.waitForElementToAppear(NO_DATA_FOUND_BANNER).isPresent();

        if (!isAppBarDisplayed) {
            logger.warn("There is no upper bar menu");
        }
        if (!isSrpResultDisplayed) {
            logger.warn("There is no result displayed");
        }
        if (!isSrpFilterBarDisplayed) {
            logger.warn("There is no bottom bar menu");
        }
        if (!isFirstCardTtdDisplayed) {
            logger.log("The first card container is not exist");
        }
        if(!isNoResultBannerDisplayed){
            logger.log("No result banner not present");
        }

        return isAppBarDisplayed && isSrpResultDisplayed && isSrpFilterBarDisplayed && (isFirstCardTtdDisplayed || isNoResultBannerDisplayed);
    }

    @Override
    public String getSelectedCategory() {
        Optional<WebElement> closeGps = driverTools.waitForElementToAppear(CLOSE_GPS, 5);
        if (closeGps.isPresent()) driverTools.click(closeGps.orElseThrow());
        clickSortFilter();
        String title = driverTools.getText(SRP_SELECTED_CATEGORY_TITLE).orElseThrow();
        closeFilterBottomsheet();
        return title;
    }

    @Override
    public boolean isCategoriesSubCategoriesValid() {
        boolean testResult = loopCategories();
        return testResult;
    }

    @Override
    public boolean checkResultList(String modifyType, String modifySubType, int totalResult, String[] validationData) {
        ArrayList<String> listOfResultCard = new ArrayList<>();
        boolean loopResultCardDone = false;
        boolean testResult = true;
        String lastCheckedData = "";

        while (!loopResultCardDone) {
            driverTools.waitForElementToAppear(RESULT_CARD_NAME, 30).orElseThrow();
            List<WebElement> displayedResultCards = driverTools.elements(RESULT_CARD_NAME).orElseThrow();

            if (displayedResultCards.isEmpty()) {
                testResult = false;
                break;
            }

            ResultLoopVariables returnParameterAfterLoop = loopDisplayedResultCard(modifySubType, validationData, displayedResultCards, listOfResultCard, totalResult, lastCheckedData);

            if (!returnParameterAfterLoop.listOfResultCard.isEmpty()) {
                listOfResultCard.addAll(returnParameterAfterLoop.listOfResultCard);
            }

            loopResultCardDone = returnParameterAfterLoop.loopResultCardDone;
            lastCheckedData = returnParameterAfterLoop.lastCheckedData;
        }

        return testResult;
    }

    @Override
    public List<String> selectPriceFilter(String priceChanges, int swipePercentage) {
        List<String> selectedPrice = new ArrayList<>();
        selectedPrice.add("");
        return selectedPrice; //FIXME: Pending implementation
    }

    @Override
    public boolean isPriceFilterApplied(int totalActivitiesToCompare, List<String> selectedPrice,String currency) {
        return false; //FIXME: Pending implementation
    }

    @Override
    public void clickFilter() {
        driverTools.waitForElementToAppear(getActivityCardNameLocator(1));
        driverTools.waitForElementToBeClickable(SRP_FILTER_BAR);
        driverTools.clickElementByCoordinate(SRP_FILTER_BAR);
    }

    @Override
    public void removeAllFilter() {
        if (driverTools.waitForElementToAppear(CLOSE_BUTTON_CHIP).isPresent()) {
            List<WebElement> totalCloseChip = driverTools.elements(CLOSE_BUTTON_CHIP).orElseThrow();
            for (int i = 0; i < totalCloseChip.size(); i++) {
                driverTools.click(CLOSE_BUTTON_CHIP);
            }
        }
    }

    @Override
    public List<String> selectGeneralFilter(String filter) {
        List<String> selectedFilter = new ArrayList<>();
        List<Integer> selectedIndex = new ArrayList<>();

        for(String filterName : filter.split("-")) {
            switch (filterName) {
                case "tiket clean" -> {
                    driverTools.swipeToElement(FILTER_TIKET_CLEAN, Swipe.UP, 5);
                    driverTools.click(FILTER_TIKET_CLEAN);
                    selectedFilter.add("tiket CLEAN");
                }
                case "tiket flexi" -> {
                    driverTools.swipeToElement(FILTER_TIKET_FLEXI, Swipe.UP, 5);
                    driverTools.click(FILTER_TIKET_FLEXI);
                    selectedFilter.add("tiket FLEXI");
                }
                case "instant pass" -> {
                    driverTools.swipeToElement(FILTER_INSTANT_PASS, Swipe.UP, 5);
                    driverTools.click(FILTER_INSTANT_PASS);
                    selectedFilter.add("Instant Pass");
                }
                case "Benefit" -> {
                    driverTools.swipeToElement(LOYALTY_BENEFIT_FILTER, Swipe.UP, 5);
                    driverTools.click(LOYALTY_BENEFIT_FILTER);
                    selectedFilter.add("Benefit");
                }
                case "instant confirmation" -> {
                    driverTools.swipeToElement(FILTER_INSTANT_CONFIRMATION, Swipe.UP, 5);
                    driverTools.click(FILTER_INSTANT_CONFIRMATION);
                    selectedFilter.add("Instant Confirmation");
                }
                case "random" -> {
                    for(int i = 0; i < 4; i++) {
                        int randomIndex = generateRandomInt(1,3);
                        Locator randomGeneralFilter = filterByLocator(randomIndex);
                        driverTools.swipeToElement(randomGeneralFilter,Swipe.UP,6);
                        String filterTitle = driverTools.getText(randomGeneralFilter).orElseThrow();
                        if(!selectedIndex.contains(randomIndex) && !selectedFilter.contains(filterTitle)) {
                            driverTools.click(randomGeneralFilter);
                            selectedFilter.add(filterTitle);
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

    private static Locator getPromoFilterLocator(String promoName) {
        String promoNameId;
        String promoNameEn;
        if (promoName.contains("/")) {
            promoNameId = promoName.split("/")[0];
            promoNameEn = promoName.split("/")[1];
        } else {
            promoNameId = promoName;
            promoNameEn = promoName;
        }
        return new Locator("PROMO_FILTER", AppiumBy.xpath("//*[contains(@resource-id,'v_campaign_filter')]//*[contains(@resource-id,'tds_chips_text') and (contains(@text,'"+promoNameId+"') or contains(@text,'"+promoNameEn+"'))]"));
    }

    @Override
    public void selectPromoFilter(String promoName) {
        driverTools.swipeToElement(SEE_ALL_PROMO, Swipe.UP, 5);
        driverTools.click(SEE_ALL_PROMO);
        driverTools.click(getPromoFilterLocator(promoName));
    }

    @Override
    public void clickReset() {
        driverTools.waitForElementToBeClickable(RESET_BUTTON);
        driverTools.click(RESET_BUTTON);
        driverTools.click(APPLY_BUTTON);
    }

    private Locator filterByLocator(int index) {
        return new Locator("GENERAL_FILTER_RANDOM", AppiumBy.xpath("(//*[contains(@resource-id,'v_feature_filter')]//*[contains(@resource-id,'cg_features_filter')]//*[contains(@resource-id,'tds_chips')]//*[contains(@resource-id,'tds_chips_container')]//*[contains(@resource-id,'tds_chips_text')])["+index+"]"));
    }

    @Override
    public void submitFilter() {
        driverTools.click(APPLY_BUTTON);
    }

    @Override
    public boolean isGeneralFilterApplied(String totalActivitiesToCompare, List<String> selectedFilter) {
        boolean result = false;
        if(driverTools.waitForElementToAppear(activityCardLocatorName(1),30).isPresent()) {
            for (int i = 1; i < Integer.parseInt(totalActivitiesToCompare) + 1; i++) {
                Locator productName = activityCardLocatorName(i);
                WebElement productNameElement = driverTools.swipeToElement(productName, Swipe.UP, 3).orElseThrow();
               String productTitle =  driverTools.getText(productNameElement);
                logger.log("Product Name : " + productTitle);

                for (String filter : selectedFilter) {
                    logger.log("Filter Applied Checked : " + filter);
                    if (filter.contains("promo")) {
                        if(checkProductPromo(i)) {
                            result = true;
                        } else {
                            return false;
                        }
                    } else {
                        if (checkProductFeature(productTitle, filter)) {
                            result = true;
                        } else {
                            return false;
                        }
                    }
                }
            }
        } else {
            driverTools.waitForElementToAppear(CHANGE_FILTER_BUTTON).orElseThrow();
            driverTools.waitForElementToAppear(EMPTY_LIST_TABLE).orElseThrow();
            return true;
        }

        return result;
    }

    private boolean checkProductPromo(int i) {
        return driverTools.waitForElementToPresence(getCrossPriceLocatorByProductIndex(i)).isPresent() || !driverTools.getValueAttribute(getCampaignLabelLocatorByProductIndex(i), "name").contains("price");
    }

    private Locator getCampaignLabelLocatorByProductIndex(int index) {
        return new Locator("CAMPAIGN LABEL_PRODUCT_"+index, AppiumBy.xpath("(//*[contains(@resource-id,'tv_campaign_label')])["+index+"]"));
    }

    private Locator getCrossPriceLocatorByProductIndex(int index) {
        return new Locator("CROSS_PRICE_PRODUCT_"+index, AppiumBy.xpath("(//*[contains(@resource-id,'tv_price_before_discount')])["+index+"]"));
    }

    private boolean checkProductFeature(String productName, String featureName) {
        boolean isFeatureDisplayed = driverTools.waitForElementToAppear(getProductFeatureByName(productName,featureName)).isPresent();

        if (!isFeatureDisplayed) {
            driverTools.swipeLeft(getProductFeatureSectionByName(productName), 5);
            isFeatureDisplayed = driverTools.element(getProductFeatureByName(productName,featureName)).isPresent();
        }

        return isFeatureDisplayed;
    }

    private Locator getProductFeatureByName(String productName, String featureName) {
        return new Locator("PRODUCT_FEATURE_LABEL", AppiumBy.xpath("//*[contains(@resource-id,'cv_card_container') and .//*[contains(@resource-id,'tv_name') and contains(@text,'"+productName+"')]]//android.widget.TextView[@text='"+featureName+"']"));
    }

    private Locator getProductFeatureSectionByName(String productName) {
        return new Locator("PRODUCT_FEATURE_SECTION", AppiumBy.xpath("//*[contains(@resource-id,'cv_card_container') and .//*[contains(@resource-id,'tv_name') and contains(@text,'"+productName+"')]]//*[contains(@resource-id,'rv_item_list_benefit')]"));
    }

    private ResultLoopVariables loopDisplayedResultCard(String modifySubType, String[] validationData, List<WebElement> displayedResultCards, ArrayList<String> listOfResultCard, int totalResult, String lastCheckedData) {
        ResultLoopVariables result = new ResultLoopVariables();
        result.listOfResultCard = new ArrayList<>();
        boolean loopResultCardDone = false;
        int totalSameHotelCardName = 0;

        for (WebElement e: displayedResultCards) {
            String resultCardNameText = driverTools.getText(e);

            if(listOfResultCard.contains(resultCardNameText)) {
                totalSameHotelCardName++;
                continue;
            } else {
                result.listOfResultCard.add(resultCardNameText);
                lastCheckedData = handleVerifyCheck(modifySubType, resultCardNameText, validationData, lastCheckedData);
            }

            if (listOfResultCard.size() >= totalResult) {
                loopResultCardDone = true;
                break;
            }
        }

        loopResultCardDone = checkIfShouldContinueLoop(totalSameHotelCardName, displayedResultCards.size() , loopResultCardDone);
        result.loopResultCardDone = loopResultCardDone;
        result.lastCheckedData = lastCheckedData;

        return result;
    }

    private boolean checkIfShouldContinueLoop(int totalSameHotelCardName, int displayedResultSize, boolean loopResultCardDone) {
        if (totalSameHotelCardName > displayedResultSize) {
            return true;
        } else {
            driverTools.swipeUp();
            return loopResultCardDone;
        }
    }

    private String handleVerifyCheck(String modifySubType, String resultCardNameText, String[] validationData, String lastCheckedData) {
        switch (modifySubType) {
            case "rating" :
                return resultCardRatingCheck(resultCardNameText, validationData, lastCheckedData);
            default :
                logger.fail("There is no available option for checking '"+modifySubType+"'");
                return "";
        }
    }

    private String resultCardRatingCheck(String resultCardNameText, String[] validationData, String lastCheckedDataText) {
        Locator resultCardRatingLocator = getResultCardRatingLocator(resultCardNameText);

        driverTools.swipeToElement(resultCardRatingLocator, Swipe.UP,2);
        String resultCardRatingText = driverTools.getText(resultCardRatingLocator).orElseThrow().split("/")[0].replace(",","").replace(".","");
        int resultCardRating = Integer.parseInt(resultCardRatingText);

        if (validationData[0].equalsIgnoreCase("desc") && !lastCheckedDataText.equalsIgnoreCase("")) {
            int lastCheckedData = Integer.parseInt(lastCheckedDataText);
            Assertion.assertThat("Assert if the result card '"+resultCardNameText+"' is already showing the valid rating", resultCardRating <= lastCheckedData, is(true));
        }

        return resultCardRatingText;
    }

    private Locator getResultCardRatingLocator(String resultCardNameText) {
        return new Locator("RESULT_CARD_RATING", AppiumBy.xpath("//*[contains(@resource-id,'tv_name') and contains(@text,\""+resultCardNameText+"\")]/parent::*//*[contains(@resource-id,'tv_rating_value')]"));
    }

    @Override
    public void setAppPermissionAllow() {
        if (driverTools.isElementDisplayed(APP_PERMISSION_ALLOW)) {
            driverTools.click(APP_PERMISSION_ALLOW);
            driverTools.click(PERMISSION_TYPE_ALLOW_ONCE);
        } else {
            logger.log("Element " + APP_PERMISSION_ALLOW + " is not displayed.");
        }
    }

    @Override
    public boolean checkLocationCampaign(String location, String promoName, int totalActivity) {
        location = location.toLowerCase();
        String regexId = "^([\\w\\s]+,\\s+" + Pattern.quote(location) + "\\b[\\w\\s]*)$";
        String regexEn = "^([\\w\\s]+,\\s+(?:\\w+\\s)*" + Pattern.quote(location) + ")$";
        String regexAny = "^[\\w\\s]*" + Pattern.quote(location) + "[\\w\\s]*$";
        String lastActivityName = "";
        boolean productStillSame = false;

        driverTools.waitForElementToAppear(FIRST_TTD_PRODUCT_NAME);
        for (int i = 0; i < totalActivity; i++) {
            driverTools.swipeUp();
            String activityName = driverTools.getText(FIRST_TTD_PRODUCT_NAME).orElseThrow();
            String activityLocation = driverTools.getText(PRODUCT_CARD_LOCATION).orElseThrow().toLowerCase();
            if (activityName.equals(lastActivityName)) {
                logger.log("current and previous activity name is the same");
                driverTools.swipeUp();
                List<WebElement> productNameList = driverTools.elements(PRODUCT_NAME).orElseThrow();
                int totalProduct = productNameList.size();
                logger.log("Total product displayed: "+totalProduct);
                totalProduct--;
                WebElement selectedProductName = productNameList.get(totalProduct);
                String selectedActivityName = driverTools.getText(selectedProductName);
                if (selectedActivityName.equals(lastActivityName)) {
                    logger.log("End of product list reached");
                    break;
                }
                List<WebElement> productLocationList = driverTools.elements(PRODUCT_CARD_LOCATION).orElseThrow();
                WebElement selectedProductLocation = productLocationList.get(totalProduct);
                activityLocation = driverTools.getText(selectedProductLocation).toLowerCase();
                String firstProduct = driverTools.getText(FIRST_TTD_PRODUCT_NAME).orElseThrow();
                if (firstProduct.equals(activityName)) productStillSame = true;
                logger.log("is the 1st product displayed still the same: "+productStillSame);
                activityName = selectedActivityName;
            }
            Assertion.assertThat("Activity: \""+activityName+"\" location should be in or near " + location, activityLocation, is(either(matchesPattern(regexId)).or(matchesPattern(regexEn)).or(matchesPattern(regexAny)).or(containsString("near "+location)).or(containsString("dekat "+location))));
            Assertion.assertThat("Activity: \""+activityName+"\" in the correct campaign " + promoName, driverTools.isElementDisplayed(activityCardWithAnyText(activityName, promoName)), is(true));
            if (productStillSame) {
                logger.log("End of product list reached");
                break;
            }
            lastActivityName = activityName;
        }
        return true;
    }

    @Override
    public boolean isSortByNearestLocationCorrect(String totalActivitiesToCompare) {
        driverTools.waitForElementToAppear(FIRST_CARD_TTD);

        for (int i=1; i<=Integer.parseInt(totalActivitiesToCompare); i++) {

            driverTools.swipeUp();

            String expectedLocation = driverTools.getText(activityCardLocationLocator(1)).orElseThrow().split("·")[1].replace("km", "").replace("m","").trim();

            driverTools.swipeToElement(activityCardLocationLocator(1),Swipe.UP,5);
            String activityName = driverTools.getText(activityCardLocatorName(1)).orElseThrow();

            driverTools.swipeUp();

            String actualLocation = driverTools.getText(activityCardLocationLocator(1)).orElseThrow().split("·")[1].replace("km","").replace("m", "").trim();

            float expectedLocationFloat = Float.parseFloat(expectedLocation.replace(",","."));
            float actualLocationFloat = Float.parseFloat(actualLocation.replace(",","."));

            logger.log("activity card name : " + activityName);
            logger.log("current activity index : " + i);
            Assertion.assertThat("Activity card name: \""+activityName+"\" location should be in or near " + expectedLocation, actualLocationFloat, is(lessThanOrEqualTo(expectedLocationFloat)));
        }

        return true;
    }

    private Locator activityCardLocatorName(int index) {
        return new Locator("ACTIVITY_CARD_NAME", AppiumBy.xpath("(//*[contains(@resource-id, 'cv_card_container')]//*[contains(@resource-id, 'rl_product_info_v2')]//*[contains(@resource-id, 'tv_name')])["+index+"]"));
    }

    private Locator activityCardLocationLocator(int index) {
        return new Locator("ACTIVITY_CARD_LOCATION", AppiumBy.xpath("(//*[contains(@resource-id, 'tv_location_info')])["+index+"]"));
    }

    private Locator activityCardWithAnyText(String activityName, String anyText) {
        return new Locator("ACTIVITY_CARD_ANY_TEXT", AppiumBy.xpath("//*[contains(@resource-id,'tv_name') and @text='"+activityName+"']//parent::*//parent::*//parent::*//parent::*[contains(@resource-id,'cv_card_container')]//*[contains(@text,'"+anyText+"')]"));
    }

    private Locator activityCardPriceLocator(int index) {
        return new Locator("ACTIVITY_CARD_PRICE", AppiumBy.xpath("(//*[contains(@resource-id, 'tv_price_after_discount')])["+index+"]"));
    }

    @Override
    public void shortBy(String sortType) {
        switch (sortType) {
            case "nearest" -> {driverTools.click(SORT_BY_NEAREST);}
            case "highest rating" -> {}
            case "highest price" -> {}
            case "highest star" -> {}
            case "lowest price" -> {}
            default -> {
                logger.fail("wrong sort type argument, do check your testdata.xlsx");
            }
        }
    }

    @Override
    public void clickSortFilter() {
        driverTools.waitForElementToBeClickable(SORT_BUTTON);
        driverTools.click(SORT_BUTTON);
    }

    @Override
    public void clickSortByButton(String sortType) {
        driverTools.waitForElementToAppear(SORT_BUTTON);
        driverTools.click(SORT_BUTTON);

        driverTools.waitForElementToAppear(getSortTypeLocator(sortType));
        driverTools.click(getSortTypeLocator(sortType));

        driverTools.click(SORT_APPLY_BTN);

        driverTools.waitForElementsToAppear(FIRST_CARD_TTD);
    }

    private Locator getSubCategoriesByIndex(int index) {
        return new Locator("SRP_SUBCATEGORIES_TITLE", AppiumBy.xpath("(//*[contains(@resource-id,'tds_chips_text')])["+ index +"]"));
    }

    private boolean loopCategories() {
        ArrayList<String> listOfCategories = new ArrayList<>();
        boolean loopCategoriesDone = false;
        boolean testResult = true;
        driverTools.click(OPEN_CATEGORIES);

        // Looping Categories
        while (!loopCategoriesDone) {
            driverTools.waitForElementToPresence(SRP_CATEGORIES_TITLE, 20).orElseThrow();
            List<WebElement> displayedCategories = driverTools.elements(SRP_CATEGORIES_TITLE).orElseThrow();
            if (displayedCategories.isEmpty()) {
                testResult = false;
                break;
            }
            int totalSameCategories = 0;

            for (WebElement e: displayedCategories) {
                String categoryTitleText = driverTools.getText(e);

                if(listOfCategories.contains(categoryTitleText)) {
                    totalSameCategories++;
                    if (totalSameCategories > displayedCategories.size()) {
                        testResult = false;
                        break;
                    }
                    continue;
                } else {
                    driverTools.click(e);
                    loopSubCategories(categoryTitleText);
                    listOfCategories.add(categoryTitleText);
                }
            }

            if (totalSameCategories == displayedCategories.size()) {
                loopCategoriesDone = true;
            } else {
                driverTools.swipeLeft(displayedCategories.get(displayedCategories.size()-1));
            }
        }

        return testResult;
    }

    private void loopSubCategories(String categoryTitleText) {
        ArrayList<String> listOfSubCategories = new ArrayList<>();
        boolean loopSubCategoriesDone = false;

        while (!loopSubCategoriesDone) {

            int displayedSubCategories = driverTools.elements(SRP_SUBCATEGORIES_TITLE).orElseThrow().size();
            int totalSameSubCategories = 0;

            for (int indexSub = 1; indexSub < displayedSubCategories+1;indexSub++) {
                WebElement subCategoryTitleElement = driverTools.element(getSubCategoriesByIndex(indexSub)).orElseThrow();
                String subCategoryTitleText = driverTools.getText(subCategoryTitleElement);

                if(listOfSubCategories.contains(subCategoryTitleText)) {
                    totalSameSubCategories++;
                    continue;
                } else {
                    listOfSubCategories.add(subCategoryTitleText);
                    driverTools.click(subCategoryTitleElement);

                    if (driverTools.isElementDisplayed(SRP_RESULT_COUNT) || driverTools.isElementDisplayed(FIRST_CARD_TTD)) {
                        Assertion.assertThat("Assert if Category '"+ categoryTitleText +"' Sub-Category '"+ subCategoryTitleText +"' has result",driverTools.isElementDisplayed(SRP_RESULT_COUNT) && driverTools.isElementDisplayed(FIRST_CARD_TTD),is(true));
                    } else {
                        logger.softWarning("Category '"+ categoryTitleText +"' Sub-Category '"+ subCategoryTitleText +"' has no result");
                    }
                }
            }

            if (totalSameSubCategories == displayedSubCategories) {
                loopSubCategoriesDone = true;
            } else {
                driverTools.swipeLeft(getSubCategoriesByIndex(displayedSubCategories));
            }
        }
    }

    private Locator getSortTypeLocator(String sortType) {
        String englishVer = sortType.split("/")[0];
        String bahasaVer = sortType.split("/")[1];
        return new Locator("KEYWORD_SUGGESTION", AppiumBy.xpath("//*[contains(@resource-id,'tds_chips_text') and (contains(@text,'"+englishVer+"') or contains(@text,'"+bahasaVer+"'))]"));
    }

    private Locator getActivityCardNameLocator(int index) {
        return new Locator("RESULT_CARD_NAME ", AppiumBy.xpath("(//*[contains(@resource-id, 'tv_name')])["+index+"]"));
    }

    private Locator getLabelInstantPassLocator(int index) {
        return new Locator("LABEL_INSTANT_PASS", AppiumBy.xpath("(//*[contains(@resource-id, 'cv_card_container')])["+index+"]//*[contains(@resource-id, 'll_instant_pass_container')]"));
    }

    private Locator getLabelTiketCleanLocator(int index) {
        return new Locator("LABEL_TIKET_CLEAN", AppiumBy.xpath("(//*[contains(@resource-id, 'cv_card_container')])["+index+"]//*[contains(@resource-id, 'll_instant_pass_container')]"));
    }

    private Locator getLabelTiketFlexiLocator(int index) {
        return new Locator("LABEL_TIKET_FLEXI", AppiumBy.xpath("(//android.widget.ImageView[@content-desc='tiket FLEXI'])["+index+"]"));
    }

    @Override
    public boolean isFilterApplied(int totalResult, String filterType) {
        boolean isCorrect = true;

        logger.log("looking for tiket FLEXI label..");
        for(int i = 1; i<=totalResult; i++) {
            String activityName = driverTools.getText(getActivityCardNameLocator(1)).orElseThrow();

            if(!driverTools.isElementDisplayed(getLabelTiketFlexiLocator(1))) {
                isCorrect = false;
            }

            logger.log("activity card number : " + i);
            logger.log("activity name : " + activityName);
            logger.log("isCorrect status : " + isCorrect);

            driverTools.swipeUp(NEXT_CARD_TTD);
            driverTools.swipeUp(ACTIVITY_CARD_FOOTER);
        }

        return isCorrect;
    }

    @Override
    public void openPdpDeeplink(String slugProductName) {
        Deeplink pdpDeeplink = new Deeplink("Pdp",
                "tiket://gatotkaca.tiket.com/to-do/"+slugProductName,
                "tiket://preprod.tiket.com/to-do/"+slugProductName,
                "tiket://tiket.com/to-do/"+slugProductName);
        driverTools.deeplink(pdpDeeplink);
        driverTools.switchToNative();
        driverTools.waitForElementToAppear(PRODUCT_NAME,5);
    }

    @Override
    public void clickJoinNow() {
        isLoginBannerDisplayed();
        driverTools.click(JOIN_NOW_BUTTON);
        if (driverTools.isElementDisplayed(JOIN_NOW_BUTTON))
            driverTools.clickElementByCoordinate(JOIN_NOW_BUTTON);
    }

    @Override
    public boolean isLoginBannerDisplayed() {
        return driverTools.isElementDisplayed(JOIN_NOW_BUTTON);
    }

    public void switchCategory(String category){
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
                logger.fail("wrong argument '"+ category.toLowerCase() +"', please check productCategory on datasheet");
            }
        }

        WebElement productCategory = driverTools.waitForElementToPresence(productCategoryLocator(categoryID, categoryENG)).orElseThrow();
        driverTools.click(productCategory);
    }

    private Locator productCategoryLocator(String categoryID, String categoryENG) {
        return new Locator("PRODUCT_CATEGORY ", AppiumBy.xpath("//*[contains(@resource-id,'tab_layout_tab')]//*[@content-desc=\""+ categoryID +"\" or @content-desc=\""+ categoryENG +"\"]"));
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
        driverTools.waitForElementToAppear(LOCATION_SUGGESTION);
        String result = driverTools.getText(LOCATION_SUGGESTION).orElseThrow();
        driverTools.click(LOCATION_SUGGESTION);

        return result;
    }

    private static Locator campaignProductLocator(String productName) {
        return new Locator("CAMPAIGN_PRODUCT_NAME", AppiumBy.xpath("//*[contains(@resource-id,'cv_card_container') and .//*[contains(@resource-id,'tv_name') and @text='"+productName+"']]//*[contains(@resource-id,'dynamic_content_view')]//android.widget.TextView"));
    }

    @Override
    public String findCampaignProduct(String productName, int totalSwipe) {
        driverTools.swipeToElement(campaignProductLocator(productName), Swipe.UP, totalSwipe);
        return driverTools.getText(campaignProductLocator(productName)).orElseThrow();
    }

    @Override
    public void selectLoyaltyProduct() {
        driverTools.waitForElementToAppear(LOYALTY_LABEL_CARD);
        driverTools.clickElementByCoordinate(LOYALTY_LABEL_CARD);
    }

    @Override
    public void selectLoyaltyProduct(String productName) {
        if (productName == null) {
            selectLoyaltyProduct();
        } else {
            driverTools.swipeToElement(loyaltyProductName(productName), Swipe.UP, 5);
            driverTools.clickElementByCoordinate(loyaltyProductName(productName));
        }
    }

    private Locator loyaltyProductName(String productName) {
        return new Locator("LOYALTY_PRODUCT_NAME", AppiumBy.xpath("//*[contains(@resource-id,'price_loyalty_badge')]//preceding::*[contains(@resource-id,'tv_name') and contains(@text,'"+productName+"')]"));
    }

    @Override
    public boolean verifyDisplayedLoyaltyBanner() {
        driverTools.waitForElementToAppear(LPG_HEADER_BANNER);
        driverTools.waitForElementToAppear(LOYALTY_LABEL_BANNER).ifPresent(driverTools::click);
        boolean isLoyaltyBannerPresent = driverTools.waitForElementToAppear(LOYALTY_BOTTOMSHEET).isPresent();
        driverTools.click(CLOSE_BUTTON);
        return isLoyaltyBannerPresent;
    }

    @Override
    public String[] getProductList(int totalActivity) {
        List<String> productList = new ArrayList<>();
        int productCountInt = 0;

        if(driverTools.waitForElementToAppear(SRP_RESULT_COUNT,10).isPresent()) {
            String productCount = driverTools.getText(SRP_RESULT_COUNT).orElseThrow().split(" ")[1];
            productCountInt = Integer.parseInt(productCount);

            totalActivity = Math.min(productCountInt, totalActivity);
        } else if (driverTools.waitForElementToAppear(NO_DATA_FOUND_BANNER,10).isPresent()) {
            return productList.toArray(new String[0]);
        }

        int count = 0;

        driverTools.waitForElementToAppear(FIRST_CARD_TTD);

        logger.log("Total activity to be checked: " + totalActivity);
        WebElement activityName;
        String activityNameText;
        for (int i = 0; i < productCountInt; i++) {
            activityName = driverTools.waitForElementToPresence(activityCardLocatorName(1), 10).orElseThrow();
            activityNameText = driverTools.getText(activityName);

            if(productList.contains(activityNameText.toLowerCase())) {
                activityNameText = driverTools.getText(activityCardLocatorName(2)).orElseThrow();
                productList.add(activityNameText.toLowerCase());
            } else {
                productList.add(activityNameText.toLowerCase());
            }
            count++;

            logger.log("Current Activity Index: " + count);
            logger.log("Current Activity Card Name: " + activityNameText);

            if(count == totalActivity ) {
                break;
            }

            driverTools.swipeUp();
        }

        logger.log("productList -> " + Arrays.toString(productList.toArray()));
        return productList.toArray(new String[0]);
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

    @Override
    public boolean verifyDisplayedCurrencyProductCard(String currency) {
        for(int i = 0; i < 3; i++) {
            driverTools.swipeUp();
            driverTools.swipeToElement(PRODUCT_CARD_PRICE_TEXTS, Swipe.UP, 5);
            List<String> priceTexts = driverTools.getTexts(PRODUCT_CARD_PRICE_TEXTS);

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

        driverTools.swipeDown(10);

        return true;
    }

    @Override
    public boolean verifyDisplayedCurrencyPriceFilter(String currency) {
        driverTools.swipeToElement(PRICE_FILTER_PARAMETER_TEXTS, Swipe.UP, 10);
        List<String> priceFilterTexts = driverTools.getTexts(PRICE_FILTER_PARAMETER_TEXTS);
        logger.log("priceFilterTexts -> " + Arrays.toString(priceFilterTexts.toArray(new String[0])));

        return CoreFunctions.checkCurrencyOnStringList(currency, priceFilterTexts);
    }

    @Override
    public void openPriceFilter() {
        driverTools.swipeDown();
        driverTools.waitForElementToAppear(PRICE_FILTER);
        driverTools.click(PRICE_FILTER);
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

        return new Locator("LOCATION_SUGGESTION", AppiumBy.xpath("//*[contains(@resource-id,'suggestion_container') and ./*[contains(@resource-id,'tv_suggestion_name') and (@text='"+locationEn+"' or @text='"+locationId+"')] and ./*[contains(@resource-id,'tv_suggestion_location') and (contains(@text,'"+ locationTypeEng +"') or contains(@text,'"+ locationTypeInd +"'))]]/*[contains(@resource-id,'tv_suggestion_name')]"));
    }

    @Override
    public boolean isSortByLowestPrice(int totalActivity) {
        boolean isValid = false;
        List<Double> priceList = new ArrayList<>();
        List<String> productNameList = new ArrayList<>();

        driverTools.waitForElementToAppear(FIRST_CARD_TTD);

        for (int i = 1; i <= totalActivity+1; i++) {
            for (int j = 0; j < 3; j++) {
                String productName = driverTools.getText(FIRST_TTD_PRODUCT_NAME).orElseThrow();

                if (!(productNameList.contains(productName))) {
                    productNameList.add(productName);

                    driverTools.swipeToElement(getProductCardFinalPriceLocator(productName), Swipe.UP, 4);
                    String currentActivityPrice = getProductCardPriceText(productName);
                    priceList.add(convertPrice(currentActivityPrice));
                    logger.log("Product card '" + productName + "' with price : '" + currentActivityPrice + "'");
                    driverTools.swipeUp();
                } else {
                    driverTools.swipeUp();
                }
            }
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

    @Override
    public String getProductBelowPrice(double maxPrice, int totalActivity) {
     //FIX ME LATER
        return "";
    }

    @Override
    public void selectProduct(String productName) {
       // FIX ME LATER
    }

    private String getProductCardPriceText(String productName) {
        if (driverTools.waitForElementsToAppear(getProductCardCrossPriceLocator(productName)).isPresent()) {
            return driverTools.getText(getProductCardCrossPriceLocator(productName)).orElseThrow();
        } else {
            return driverTools.getText(getProductCardFinalPriceLocator(productName)).orElseThrow();
        }
    }

    private Locator getProductCardFinalPriceLocator(String productName) {
        return new Locator("PRODUCT_CARD_FINAL_PRICE", AppiumBy.xpath("//*[contains(@resource-id,'cv_card_container') and .//*[contains(@resource-id,'tv_name') and contains(@text,'"+productName+"')]]//*[contains(@resource-id,'tv_price_after_discount')]"));
    }

    private Locator getProductCardCrossPriceLocator(String productName) {
        return new Locator("PRODUCT_CARD_CROSS_PRICE", AppiumBy.xpath("//*[contains(@resource-id,'cv_card_container') and .//*[contains(@resource-id,'tv_name') and contains(@text,'"+productName+"')]]//*[contains(@resource-id,'tv_price_before_discount')]"));
    }

    @Override
    public void selectTheProduct() {
        driverTools.waitForElementToAppear(RESULT_CARD_NAME, 20).orElseThrow();
        driverTools.click(RESULT_CARD_NAME);
    }

    @Override
    public void chooseProduct(String productName, int totalSwipe) {
        if (totalSwipe == 0)
            driverTools.waitForElementToAppear(visibleProductName(productName));
        driverTools.swipeToPresentedElement(visibleProductName(productName), Swipe.UP, totalSwipe);
        driverTools.clickElementByCoordinate(visibleProductName(productName));
    }

    @Override
    public boolean isAllTabShown() {
        return driverTools.isElementDisplayed(ALL_TAB);
    }

    @Override
    public String getSelectedTab() {
        return ""; //FIXME: Pending implementation
    }

    @Override
    public String selectDateFilter(String date) {
        CalendarPage<T> calendarPage = CalendarPage.getInstance(session);

        String targetDate = "";
        if (date.equalsIgnoreCase("random")) {

            targetDate = String.valueOf(generateRandomInt(1, 28)); // Generate a valid random date
        } else {
            targetDate = date;
        }

        if (!targetDate.matches("\\d+")) { // Validate the targetDate
            throw new DriverToolsException("Test data 'date' with value '" + targetDate + "' is not valid, please provide a valid numeric date");
        }

        date = targetDate;

        switch (date.toLowerCase()) {
            case "today":
            case "hari ini":
                driverTools.click(DATE_FILTER_CHIP_TODAY);
                return "0";
            case "tomorrow":
            case "besok":
                driverTools.click(DATE_FILTER_CHIP_TOMORROW);
                return "00";
        }

        driverTools.clickElementByCoordinate(DATE_FILTER_CHIP_OTHER);

        int numberDate = Integer.parseInt(date);
        if (numberDate <= 0 || numberDate > 31) {
            throw new DriverToolsException("Test data 'date' with value '" + date + "' is not valid, please check again");
        }

        LocalDate currentDate = LocalDate.now();
        int currentDay = currentDate.getDayOfMonth();
        int currentMonthInt = currentDate.getMonthValue();
        int currentYearInt = currentDate.getYear();

        Locator targetDateLocator = getDateElement(currentMonthInt, numberDate);

        if (numberDate < currentDay) {
            int nextMonthInt = currentMonthInt + 1 == 13 ? 1 : currentMonthInt + 1;
            int selectedYear = currentMonthInt + 1 == 13 ? currentYearInt + 1 : currentYearInt;

            targetDate += "-" + nextMonthInt + "-" + selectedYear;
            targetDateLocator = getDateElement(nextMonthInt, numberDate);

            driverTools.swipeToElement(targetDateLocator, Swipe.UP, 3);
        } else {
            targetDate += "-" + currentMonthInt + "-" + currentYearInt;
        }

        logger.log("Selected Date : " + targetDate);
        driverTools.waitForElementToAppear(targetDateLocator);
        driverTools.click(targetDateLocator);
        driverTools.click(SAVE_DATE_FILTER);
        driverTools.waitForElementToAppear(FIRST_CARD_TTD, 20);

        return targetDate;
    }

    @Override
    public void openDateFilter() {
        driverTools.waitForElementToAppear(getActivityCardNameLocator(1));
        driverTools.waitForElementToBeClickable(DATE_FILTER);
        driverTools.click(DATE_FILTER);

        if(driverTools.waitForElementToAppear(FILTER_RESET_BTN, 5).isEmpty()) {
            driverTools.click(DATE_FILTER);
        }
    }

    public boolean verifyDateFilterChip() {
        driverTools.swipeToElement(DATE_FILTER_CHIP_TODAY, Swipe.UP, 8);
        List<Locator> dateOptions = Arrays.asList(DATE_FILTER_CHIP_TODAY, DATE_FILTER_CHIP_TODAY, DATE_FILTER_CHIP_OTHER);
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

    private Locator getDateElement(int month, int date) {
        String monthEng = String.valueOf(Month.of(month));
        monthEng = monthEng.substring(0, 1).toUpperCase() + monthEng.substring(1).toLowerCase();
        String monthId = monthEngToId(monthEng);

        return new Locator("DATE_ELEMENT ", AppiumBy.xpath("//android.widget.LinearLayout[./*[contains(@resource-id,'tds_month_name_calendar') and contains(@text,'"+monthEng+"') or contains(@text,'"+monthId+"')]]//*[contains(@resource-id,'ll_day_group')]/*[@text='"+date+"']"));
    }

    @Override
    public Locator getNextMonthElement(String nextMonthId,String nextMonthEng) {
        return new Locator("NEXT_MONTH", AppiumBy.xpath("//*[contains(@resource-id, 'tv_current_month') and (contains(@text, '" + nextMonthId + "') or contains(@text, '" + nextMonthEng + "'))]"));
    }

    @Override
    public boolean isFilterActivated(String filterType) {
        //verify general filter green icon is applied
        driverTools.waitForElementToAppear(SRP_FILTER_BAR).orElseThrow();
        Boolean filter_badge = driverTools.isElementDisplayed(FILTER_BUTTON_GREEN_BADGE);

        //verify selected filter green icon is applied
        logger.log("filter_badge : "+filter_badge);

        //both filter green icon much exist
        return filter_badge;
    }

    @Override
    public String getLocation() {
        driverTools.waitForElementToAppear(RESULT_CARD_NAME);
        String result = driverTools.getText(SRP_LOCATION).orElseThrow();
        result = result.contains("...") ? result.replace("...","") : result;
        return result;
    }

    /**
     * @param filterType name of the filter, can be seen on bottom bar of search result page
     * @return locator of the `green icon` of the filter after the filter is applied
     */

    private Locator getFilterTypeBadgeLocator(String filterType) {
        String english ="";
        String indonesian = "";
        switch (filterType.toLowerCase()) {
            case "date" -> {
                english = "Date";
                indonesian = "Tanggal";
            }
            case "sort" -> {
                english = "Sort";
                indonesian = "Urutkan";
            }
            case "general" -> {
                english = "Filter";
                indonesian = "Filter";
            }
            default -> {
                logger.fail("There is no handle for filterType '" + filterType + "'");
                throw new NoSuchMethodError();
            }
        }
        return new Locator(filterType+"FILTER_BUTTON_BADGE", AppiumBy.xpath("//*[contains(@resource-id,'tds_title_tv') and contains(@text,'"+english+"') or contains(@text,'"+indonesian+"')]/parent::*/*[contains(@resource-id,'tds_badge')]"));
    }

    private Locator productNameLocator(String product) {
        if(product.contains("/")) {
            String enProduct = product.split("/")[0];
            String inProduct = product.split("/")[1];
            return new Locator("PRODUCT_NAME_LOCATOR", AppiumBy.xpath("//*[contains(@resource-id,'tv_name') and @text='"+enProduct+"' or @text='"+inProduct+"' ]"));
        } else {
            return new Locator("PRODUCT_NAME_LOCATOR", AppiumBy.xpath("//*[contains(@resource-id,'tv_name') and @text='"+product+"' ]"));
        }
    }

    @Override
    public void findProductByProductName(String product) {
        driverTools.waitForElementToAppear(FIRST_CARD_TTD,60).orElseThrow();
        Locator productName = productNameLocator(product);
        driverTools.swipeToElement(productName, Swipe.UP, 20);
        driverTools.click(productName);
    }

    @Override
    public void clickLocation() {
        driverTools.click(SRP_LOCATION);
    }

    @Override
    public String selectRandomLocation() {
        driverTools.waitForElementToAppear(KEYWORD_SUGGESTION);
        List<WebElement> locations = driverTools.elements(KEYWORD_SUGGESTION).orElseThrow();
        int numberOfLocations = locations.size();

        logger.log("Location tags->" + (numberOfLocations-2));

        WebElement selectedLocation = locations.get(CoreFunctions.generateRandomInt(2, numberOfLocations-1));
        String location = driverTools.getText(selectedLocation);

        logger.log("selected Location ->" + location);
        driverTools.click(selectedLocation);
        return location;
    }

    @Override
    public boolean compareProductNameWithAPI(TTDSRPByLocationPOJO response, int productCount) {
        int cardsFromApi = response.data == null ? 0 : response.data.length;
        if(cardsFromApi == 0) {
            logger.log("No product found for this location");
            return driverTools.isElementDisplayed(NO_DATA_FOUND_BANNER);
        }

        ArrayList<String> dataFromAPI = filterProductNameFromAPI(response);

        for(int i = 0; i < cardsFromApi && i < productCount; i++) {
            String titleFromUI = driverTools.getText(getActivityCardNameLocator(1)).orElseThrow().replace(" ", "");
            driverTools.swipeUp();
            String normalizedAPI = Normalizer.normalize(dataFromAPI.get(i), Normalizer.Form.NFC);
            String normalizedUI = Normalizer.normalize(titleFromUI, Normalizer.Form.NFC);
            Assertion.assertThat("index "+i+": product name from API should be equal with UI", normalizedAPI, containsStringIgnoringCase(normalizedUI));
        }
        return true;
    }

    private ArrayList<String> filterProductNameFromAPI(TTDSRPByLocationPOJO response) {
        ArrayList<String> result = new ArrayList<>();

        for(int i = 0; i < response.data.length; i++) {
            result.add(response.data[i].translations.get(0).title.replace(" ", ""));
        }

        return result;
    }

    @Override
    public void clickBack() {
        driverTools.waitForElementToPresence(BACK_BUTTON_SRP, 20).ifPresent(driverTools::click);
    }

    @Override
    public boolean isCampaignLabelPresent() {
        WebElement campaignLabelElement = driverTools.waitForElementToPresence(FIRST_PRODUCT_CARD_CAMPAIGN_LABEL).orElseThrow();

        Assertion.assertThat("Assert if campaign label is present on SRP", driverTools.isElementDisplayed(campaignLabelElement), is(true));
        return true;
    }

    @Override
    public boolean isDateFilterValid(String date, int totalProduct) {
        String targetDate = "";

        if(date.equals("0") || date.equals("00")) {
            LocalDate today = date.equals("00") ? LocalDate.now().plusDays(1) : LocalDate.now().plusDays(0);
            targetDate += today.getDayOfMonth() + "-";
            targetDate += today.getMonthValue() + "-";
            targetDate += today.getYear();
        } else {
            targetDate += date;
        }

        int totalProducts = 0;
        boolean resultCheck = false;
        List<String> scannedProductName = new ArrayList<>();

        driverTools.waitForElementToPresence(FIRST_CARD_TTD);
        if(driverTools.waitForElementToPresence(SRP_RESULT_COUNT).isPresent()) {
            int resultCount = Integer.parseInt(driverTools.getValueAttribute(SRP_RESULT_COUNT, "text").split(" ")[1]);

            if(resultCount < totalProduct) {
                totalProducts += resultCount;
            } else {
                totalProducts += totalProduct;
            }
        }

        for(int i = 0; i < 10; i++) {
            List<WebElement> productNameList = driverTools.elements(PRODUCT_NAME).orElseThrow();
            List<String> willBeScannedProductNameList = new ArrayList<>();
            int counter = 0;

            for(WebElement element : productNameList) {
                String productName = driverTools.getText(element);

                if(!scannedProductName.contains(productName)) {
                    willBeScannedProductNameList.add(productName);
                } else {
                    counter++;
                }

                if(counter == productNameList.size()) {
                    driverTools.swipeUp();
                }
            }

            for(String productName : willBeScannedProductNameList) {

                driverTools.swipeToElement(getDateProductCard(productName), Swipe.UP, 3).orElseThrow();
                String[] productFullAvailability = driverTools.getValueAttribute(getDateProductCard(productName), "text").split(" ");
                String[] productLastAvailability = Arrays.asList(productFullAvailability).subList(productFullAvailability.length - 3, productFullAvailability.length).toArray(new String[0]);
                String comparableTextProductLastAvailability = changeDateFormatToInt(productLastAvailability);

                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    Date selectedDateConverted = dateFormat.parse(targetDate);
                    Date displayedDateConverted = dateFormat.parse(comparableTextProductLastAvailability);
                    logger.log("selectedDate : " + selectedDateConverted + "\ndisplayedDate : " + displayedDateConverted);

                    Assertion.assertThat("Product '" + productName + "' is having valid last date", displayedDateConverted.compareTo(selectedDateConverted) >= 0, CoreMatchers.is(true));
                    if(driverTools.isElementDisplayed(getProductAvailabilityText(productName))){
                        String availabilityText = driverTools.getText(getProductAvailabilityText(productName)).orElseThrow();
                        Assertion.assertThat("Product '" + productName + "' is having availability text", isAvailabilityTextValid(date, availabilityText),is(true));
                    } else {
                        logger.softWarning("There is no availability text on product index " + i);
                    }
                    resultCheck = true;
                } catch (ParseException e) {
                    String availabilityText = driverTools.getText(getProductAvailabilityText(productName)).orElseThrow();
                    Assertion.assertThat("Product '" + productName + "' is having availability text", isAvailabilityTextValid(date, availabilityText),is(true));
                }

                scannedProductName.add(productName);
                if(scannedProductName.size() >= totalProducts) {
                    break;
                }
            }

            if(scannedProductName.size() >= totalProducts) {
                break;
            }
        }

        return resultCheck;
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

    private Locator getProductAvailabilityText(String productName) {
        return new Locator("PRODUCT_AVAILABILITY_"+productName, AppiumBy.xpath("//*[contains(@resource-id,'cv_card_container') and .//*[contains(@resource-id,'tv_name') and @text=\""+productName+"\"]]//*[contains(@resource-id,'tv_same_day_booking')]"));
    }

    private Locator getDateProductCard(String productName) {
        return new Locator("PRODUCT_AVAILABILITY_"+productName, AppiumBy.xpath("//*[contains(@resource-id,'cv_card_container') and .//*[contains(@resource-id,'tv_name') and @text=\""+productName+"\"]]//*[contains(@resource-id,'tv_benefit')][1]"));
    }
    private String changeDateFormatToInt(String[] productLastAvailability) {
        String resultDate = "";
        resultDate += productLastAvailability[0].substring(productLastAvailability[0].length()-2) + "-";
        resultDate += CoreFunctions.getMonthNumber(productLastAvailability[1]) + "-";
        resultDate += productLastAvailability[2];

        return resultDate;
    }

    private Locator labelTypeLocator(String labelType) {
        return new Locator(labelType, AppiumBy.xpath("//*[contains(@resource-id,'rl_product_info_v2')]//*[contains(@text,'"+ labelType+"')]"));
    }

    @Override
    public void findProductByLabelName(String labelType) {
        Locator productName = labelTypeLocator(labelType);
        driverTools.swipeToElement(productName, Swipe.UP, 10);
        if (driverTools.isElementDisplayed(productName)) {
            driverTools.click(RESULT_CARD_NAME);
        } else {
            throw new RuntimeException("No label found.");
        }
    }

    @Override
    public Boolean isPriceBelowTenNonIDR(String currency, String productCount) {
        int totalProduct = Integer.parseInt(productCount);
        for(int i = 0; i < totalProduct; i++) {
            driverTools.swipeUp();
            driverTools.swipeToElement(PRODUCT_CARD_PRICE_TEXTS, Swipe.UP, 5);
            List<String> priceTexts = driverTools.getTexts(PRODUCT_CARD_PRICE_TEXTS);
            logger.log("priceTexts -> " + Arrays.toString(priceTexts.toArray(new String[0])));

            for(String priceText : priceTexts) {
                if (priceText.matches(".*\\d.*")) {
                    priceText = priceText.split(" ")[1];
                    priceText = priceText.replace(",", ".");
                    if (Double.parseDouble(priceText) >= 10 && priceText.contains(".")) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    @Override
    public Boolean isPriceAboveTenNonIDR(String currency, String productCount) {
        int totalProduct = Integer.parseInt(productCount);
        for(int i = 0; i < totalProduct; i++) {
            driverTools.swipeUp();
            driverTools.swipeToElement(PRODUCT_CARD_PRICE_TEXTS, Swipe.UP, 5);
            List<String> priceTexts = driverTools.getTexts(PRICE_FINAL_TEXTS);

            for(String priceText : priceTexts) {
                if(priceText.contains(currency)) {
                    String price = priceText.split(" ")[1];
                    price = price.replace(",",".");
                    logger.log(price);
                    if(Double.parseDouble(price) < 10) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    @Override
    public Boolean verifyDisplayedPriceAmountProductCard(int currencyDecimalDigit, int productCount, GETProductListTTDSRPPOJO productDatafromAPI, String currency) {
        throw new NotImplementedException("This method is not implemented in Android");
    }

    @Override
    public Boolean verifyDisplayedPriceAmountProductCard(int currencyDecimalDigit, int productCount, GETProductListTTDSRPPOJO productDatafromAPI) {
        if(driverTools.waitForElementToAppear(SRP_RESULT_COUNT,10).isPresent()) {
            String productCount1 = driverTools.getText(SRP_RESULT_COUNT).orElseThrow().split(" ")[1];
            int productCountInt = Integer.parseInt(productCount1);

            productCount = Math.min(productCountInt, productCount);
        }

        logger.log("checking products...");

        List<String> checkedProducts = new ArrayList<>();

        for(int i = 0; checkedProducts.size() < productCount && i < 20; i++) {
            List<String> scannedProductTitle;

            scannedProductTitle = driverTools.getTexts(PRODUCT_NAME);

            for(String productTitle : scannedProductTitle) {
                if(!checkedProducts.contains(productTitle)) {
                    logger.log("checking -> " + productTitle);

                    GETProductListTTDSRPPOJO.ProductCard productCard = productDatafromAPI.data.stream().filter(e -> e.translations.get(0).title.equals(productTitle)).findFirst().orElseThrow();
                    // the card only renders a slash price when there is a real discount. The API leaves
                    // startingCrossedPrice at 0 for an undiscounted product, and on preprod some products
                    // carry a crossed price below the final price - neither is shown as a strikethrough.
                    boolean needCheckCrossedPrice = productCard.startingCrossedPrice > productCard.startingFinalPrice;

                    logger.log("startingFinalPrice -> " + productCard.startingFinalPrice + ", startingCrossedPrice -> " + productCard.startingCrossedPrice);
                    logger.log("needCheckCrossedPrice -> " + needCheckCrossedPrice);

                    switch (currencyDecimalDigit) {
                        case 0 -> {verifyDisplayedPriceAmountProductCard0Digit(productCard, needCheckCrossedPrice);}
                        case 2 -> {verifyDisplayedPriceAmountProductCard2Digit(productCard, needCheckCrossedPrice);}
                        default -> {
                            logger.fail("Currency decimal digit '" + currencyDecimalDigit + "' is not valid, please check again");
                        }
                    }
                    checkedProducts.add(productTitle);
                    driverTools.swipeUp();
                }
            }
        }

        return true;
    }

    @Override
    public Boolean verifyRedirectionToSearchResultPage(String sectionType) {
        driverTools.waitForElementToAppear(FIRST_CARD_TTD);
        boolean isSelectedCategoryCorrect;
        boolean isSortedActivated;
        boolean isCardDisplayed = driverTools.waitForElementsToAppear(FIRST_CARD_TTD, 10).isPresent();
        String selectedCategory = "";
        boolean isSelectedSort = false;

        clickSortFilter();
        if(sectionType.toLowerCase()=="new activities") {
            isSelectedSort = driverTools.isElementDisplayed(NEWLY_ADDED);
        }else{
            selectedCategory = driverTools.getText(SRP_SELECTED_CATEGORY_TITLE).orElseThrow().toLowerCase();
        }
        driverTools.click(CLOSE_BUTTON);

        switch (sectionType.toLowerCase()) {
            case "new activities":
                boolean isAllLocationDisplayed = getLocation().equals("All locations") || getLocation().equals("Semua lokasi");
                return isSelectedSort && isCardDisplayed && isAllLocationDisplayed;
            case "popular events":
                isSelectedCategoryCorrect = selectedCategory.equals("semua event") || selectedCategory.equals("all events");
                return isSelectedCategoryCorrect && isCardDisplayed;
            case "popular attractions":
                isSelectedCategoryCorrect = selectedCategory.equals("semua atraksi") || selectedCategory.equals("all attractions");
                return isSelectedCategoryCorrect && isCardDisplayed;
            default:
                logger.fail("There is no option for sectionType '"+ sectionType +"'");
        }
        return false;
    }
    
    public void clickSearchBar() {
        driverTools.click(SRP_SEARCHBAR);
    }

    private void verifyDisplayedPriceAmountProductCard0Digit(GETProductListTTDSRPPOJO.ProductCard productCard, boolean needCheckCrossedPrice) {
        String productTitle = productCard.translations.get(0).title;

        double finalPriceAPI1 = productCard.startingFinalPrice;
        double crossedPriceAPI1 = productCard.startingCrossedPrice;

        Locator productFinalPriceText = getFinalPriceByProductNameLocator(productTitle);
        driverTools.swipeToElement(productFinalPriceText, Swipe.UP, 3);
        String finalPriceText = driverTools.getText(productFinalPriceText).orElseThrow();
        double finalPriceUI = convertPrice(finalPriceText.split(" ")[1]);

        if(needCheckCrossedPrice) {
            Locator productCrossedPriceText = getCrossedPriceByProductNameLocator(productTitle);
            String crossedPriceText = driverTools.getText(productCrossedPriceText).orElseThrow(
                    () -> new IllegalStateException("No crossed price is displayed for product '" + productTitle + "' although the API returns a crossed price of " + crossedPriceAPI1));
            double crossedPriceUI = convertPrice(crossedPriceText.split(" ")[1]);

            Assertion.assertThat("Assert if final price product '"+productTitle+"' is correct", finalPriceUI, is(finalPriceAPI1));
            Assertion.assertThat("Assert if crossed price product '"+productTitle+"' is correct", crossedPriceUI, is(crossedPriceAPI1));
        } else {
            Assertion.assertThat("Assert if final price product '"+productTitle+"' is correct", finalPriceUI, is(finalPriceAPI1));
        }
    }

    private void verifyDisplayedPriceAmountProductCard2Digit(GETProductListTTDSRPPOJO.ProductCard productCard, boolean needCheckCrossedPrice) {
        String productTitle = productCard.translations.get(0).title;

        double finalPriceAPI1 = (double) productCard.startingFinalPrice / 100;
        double crossedPriceAPI1 = (double) productCard.startingCrossedPrice / 100;

        double finalPriceAPI2 = finalPriceAPI1 > 10 ? CoreFunctions.roundPrice(finalPriceAPI1) : finalPriceAPI1;
        double crossedPriceAPI2 = crossedPriceAPI1 > 10 ? CoreFunctions.roundPrice(crossedPriceAPI1) : crossedPriceAPI1;

        Locator productFinalPriceText = getFinalPriceByProductNameLocator(productTitle);
        driverTools.swipeToElement(productFinalPriceText, Swipe.UP, 3);
        String finalPriceText = driverTools.getText(productFinalPriceText).orElseThrow();
        double finalPriceUI = convertPrice(finalPriceText.split(" ")[1]);

        if(needCheckCrossedPrice) {
            Locator productCrossedPriceText = getCrossedPriceByProductNameLocator(productTitle);
            String crossedPriceText = driverTools.getText(productCrossedPriceText).orElseThrow(
                    () -> new IllegalStateException("No crossed price is displayed for product '" + productTitle + "' although the API returns a crossed price of " + crossedPriceAPI2));
            double crossedPriceUI = convertPrice(crossedPriceText.split(" ")[1]);

            Assertion.assertThat("Assert if final price product '"+productTitle+"' is correct", finalPriceUI, is(finalPriceAPI2));
            Assertion.assertThat("Assert if crossed price product '"+productTitle+"' is correct", crossedPriceUI, is(crossedPriceAPI2));
        } else {
            Assertion.assertThat("Assert if final price product '"+productTitle+"' is correct", finalPriceUI, is(finalPriceAPI2));
        }
    }

    @Override
    public boolean checkBoostingLabels(String language) {
        logger.log("language -> " + language);

        final String[] indonesianLabels = new String[] { "Baru", "Ekslusif di tiket.com", "Pilihan tiket.com", "Rekomendasi", "Wajib dikunjungi", "Wajib dicoba"};
        final String[] englishLabels = new String[] { "New", "Exclusive on tiket.com", "tiket.com's choice", "Recommended", "Must visit", "Must try"};

        driverTools.swipeToPresentedElement(BOOSTING_LABEL, Swipe.UP, 30);
        List<WebElement> boostingLabels = driverTools.elements(BOOSTING_LABEL).orElseThrow();

        for (WebElement element: boostingLabels) {
            String label = element.getText();

            if (language.equals("id"))
                Assertion.assertThat("Label "+ label +" is in Indonesian and is valid", label, in(indonesianLabels));
            else
                Assertion.assertThat("Label "+ label +" is in english and is valid", label, in(englishLabels));
        }

        return true;
    }

    private Locator getCrossedPriceByProductNameLocator(String title) {
        return new Locator("CROSSED_PRICE", AppiumBy.xpath("//*[contains(@resource-id,'cv_card_container') and .//*[contains(@resource-id,'tv_name') and @text='"+title+"']]//*[contains(@resource-id,'tv_slash_price') and not(contains(@resource-id,'percentage'))]"));
    }

    private Locator getFinalPriceByProductNameLocator(String title) {
        return new Locator("FINAL_PRICE", AppiumBy.xpath("//*[contains(@resource-id,'cv_card_container') and .//*[contains(@resource-id,'tv_name') and @text='"+title+"']]//*[contains(@resource-id,'tv_main_price')]"));
    }
    @Override
    public boolean isCartContainsLoyaltyBadge() {
        return driverTools.waitForElementToPresence(LOYALTY_LABEL_CARD,20).isPresent();
    }

    @Override
    public int getProductCountOnSrp() {
        driverTools.waitForElementToPresence(SRP_RESULT_COUNT);
        String productCount = driverTools.getText(SRP_RESULT_COUNT).orElseThrow().split(" ")[1];
        return Integer.parseInt(productCount);
    }

    @Override
    public boolean verifyLoyaltyIconOrCampaignOnSRPCard(String tier) {
        return driverTools.isElementDisplayed(LOYALTY_LABEL_CARD) || driverTools.isElementDisplayed(FIRST_PRODUCT_CARD_CAMPAIGN_LABEL);
    }
}