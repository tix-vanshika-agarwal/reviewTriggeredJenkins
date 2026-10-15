package com.tiket.web.page.ttd.TtdSearchResultPage;

import com.tiket.model.*;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import com.tiket.web.page.ttd.TtdLandingPage.TtdLandingPageWeb;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.regex.Pattern;

import static com.tiket.core.CoreFunctions.generateRandomInt;
import static com.tiket.web.page.ttd.TtdLandingPage.TtdLandingPageWeb.CLOSE_POPUP;
import static com.tiket.web.page.ttd.TtdLandingPage.TtdLandingPageWeb.CROSS_POPUP;
import static org.hamcrest.Matchers.*;

public abstract class TtdSearchResultPageWeb<T extends WebDriver> extends WebPage<T> {

    public TtdSearchResultPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> TtdSearchResultPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new TtdSearchResultDweb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new TtdSearchResultMweb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    private static final Locator LOGIN_BANNER = new Locator("LOGIN_BANNER", new By.ByXPath("//*[contains(@class,'ActionBanner_elevation')]//*[contains(@class,'Button')]"));
    private static final Locator PRODUCT_TITLE = new Locator("PRODUCT_TITLE", new By.ByXPath("//*[contains(@class,'ProductCardTTD_product_title')]"));
    private static final Locator PRODUCT_LOCATION = new Locator("PRODUCT_LOCATION", new By.ByXPath("//*[contains(@class,'product-info_info_text')]"));
    private static final Locator PRODUCT_CONTENT = new Locator("PRODUCT_CONTENT", new By.ByXPath("//*[contains(@class,'ProductCardTTD_card_content_variant')]"));
    public static final Locator PRODUCT_CAMPAIGN = new Locator("PRODUCT_CAMPAIGN", new By.ByXPath("//*[contains(@class,'ProductCardTTD_campaign_label')]//*[contains(@class,'ProductCardTTD_text_label')]"));
    private static final Locator FILTER_NOTIF_SIGN = new Locator("FILTER_NOTIF_SIGN", new By.ByXPath("//*[@data-testid='icon-filter']//following-sibling::*[contains(@class,'Notif_placement_top_end')]"));
    public static final Locator FILTER_BUTTON = new Locator("FILTER_BUTTON", new By.ByXPath("//*[contains(@class,'SortFilterSection_filter_button')]"));
    private static final Locator SELECTED_FILTERS = new Locator("SELECTED_FILTERS", new By.ByXPath("(//div[contains(@class, 'SearchResultFilter_filter_chip_container')]//button[@type='button' and contains(@class, 'Chip_chip') and contains(@class, 'Chip_size_default')])[1]"));
    private static final Locator ACTIVE_ATTRACTIONS_CATEGORY = new Locator("ACTIVE_ATTRACTIONS_CATEGORY", new By.ByXPath("//*[@id='categories-ATTRACTION']//parent::*//following-sibling::*[contains(@class,'SearchResultFilter_filter_category_chip_container')]//*[contains(@class,'Chip_active')]"));
    private static final Locator DEFAULT_ATTRACTIONS_CATEGORY = new Locator("DEFAULT_ATTRACTIONS_CATEGORY", new By.ByXPath("//*[@id='categories-ATTRACTION']//parent::*//following-sibling::*[contains(@class,'SearchResultFilter_filter_category_chip_container')]//*[contains(@class,'Chip_size_default')]"));
    private static final Locator REFUND_100P = new Locator("REFUND_100P", new By.ByXPath("//*[contains(@class,'ProductCardTTD_label_list')]//*[contains(text(),'100% Refund')]"));
    private static final Locator FEATURES_BUTTON = new Locator("FEATURES_BUTTON", new By.ByXPath("//p[contains(@class, 'SearchResultFilter_filter_label') and (text()='Features' or text()='Fitur')]"));
    private static final Locator FEATURES_CATEGORY_FILTER = new Locator("FEATURES_CATEGORY_FILTER", new By.ByXPath("//p[contains(@class, 'SearchResultFilter_filter_label') and (text()='Features' or text()='Fitur')]//following-sibling::*//span[contains(@class, 'Chip_text')]"));
    private static final Locator PROMO_BUTTON = new Locator("PROMO_BUTTON", new By.ByXPath("//p[contains(@class, 'SearchResultFilter_filter_label') and text()='Promo']"));
    public static final Locator DATE_BUTTON = new Locator("DATE_BUTTON", new By.ByXPath("//p[contains(@class, 'SearchResultFilter_filter_label') and (text()='Tanggal' or text()='Date')]"));
    public static final Locator APPLY_BUTTON = new Locator("APPLY_BUTTON", new By.ByXPath("//span[contains(text(),'Apply') or contains(text(),'Terapkan')]//parent::button"));
    private static final Locator TIKET_FLEXI = new Locator("TIKET_FLEXI", new By.ByXPath("//span[contains(@class, 'Text_text') and text()='tiket FLEXI']"));
    private static final Locator TIKET_FLEXI_BAHASA = new Locator("TIKET_FLEXI_BAHASA", new By.ByXPath("//button[contains(@class, 'Chip_chip') and .//span[text()='tiket FLEXI']]"));
    private static final Locator INSTANT_CONFIRMATION = new Locator("INSTANT_CONFIRMATION", new By.ByXPath("//button[contains(@class, 'Chip_chip')]//span[normalize-space(text())='Instant Confirmation' or normalize-space(text())='Konfirmasi Instan']"));
    public static final Locator SEARCH_LOCATION = new Locator("SEARCH_LOCATION", new By.ByXPath("//*[contains(@class,'SRPSearchBox_custom_searchbox')]//*[contains(@class,'Text_weight_bold')]"));
    private static final Locator SEARCH_INPUT_PRODUCT = new Locator("SEARCH_INPUT_PRODUCT", new By.ByXPath("//*[contains(@class,'SRPSearchBox_custom_input_wrapper')]//input"));
    private static final Locator SEARCH_INPUT_LOCATION = new Locator("SEARCH_INPUT_LOCATION", new By.ByXPath("//*[contains(@class,'SearchBox_input_box')]//input"));
    private static final Locator SEARCH_SUGGESTION_TITLE = new Locator("SEARCH_SUGGESTION_TITLE", new By.ByXPath("//*[contains(@class,'SearchItem_search_item_title')]"));
    private static final Locator SEARCH_SUGGESTION_TYPE = new Locator("SEARCH_SUGGESTION_TYPE", new By.ByXPath("//*[contains(@class,'SearchItem_search_item_description')]"));
    private static final Locator SEARCH_INPUT_ATTRACTION = new Locator("SEARCH_INPUT_ATTRACTION", new By.ByXPath("//input[@type='text' and (contains(@placeholder, 'Search activities') or contains(@placeholder, 'Cari aktivitas'))]"));
    private static final Locator ALL_LOCATION_CHIP_SRP = new Locator("ALL_LOCATION_CHIP_SRP", new By.ByXPath("//span[contains(@class, 'Text') and contains(text(),'Semua lokasi') or contains(text(),'All locations')]"));
    public static final Locator OTHER_DATE = new Locator("OTHER_DATE", new By.ByXPath("//*[contains(@class,'Chip_icon_left')]/following-sibling::*[text()='Other' or text()='Lainnya']"));
    public static final Locator SAVE_DATE_BUTTON = new Locator("SAVE_DATE_BUTTON", new By.ByXPath("//*[contains(@class,'footer_button')]//button"));
    private static final Locator RESET_FILTER_BUTTON = new Locator("RESET_FILTER_BUTTON", AppiumBy.xpath("//span[contains(text(), 'Reset')]//parent::button"));
    private static final Locator RESET_FILTER_BUTTON_SHEET = new Locator("RESET_FILTER_BUTTON_SHEET", AppiumBy.xpath("//button[contains(text(), 'Reset')]"));
    private static final Locator FIRST_PRODUCT_SUGGESTION = new Locator("FIRST_PRODUCT_SUGGESTION", By.xpath("(//a[contains(@class,'SearchItem_search_item')]//span[contains(@class,'SearchItem_search_item_title')])[1]"));
    private static final Locator TODAY_FILTER_BUTTON = new Locator("TODAY_FILTER_BUTTON", By.xpath("//button[./span[contains(text(), 'Today')]]"));
    private static final Locator FIRST_PRODUCT_CARD = new Locator("FIRST_PRODUCT_CARD", By.xpath("(//a[contains(@class, 'ProductCardSearch_product_card')]//h2)[1]"));
    private static final Locator CONTINUE_ON_WEB_POPUP = new Locator("CONTINUE_ON_WEB_POPUP", new By.ByXPath("//*[@data-qa-id='secondaryCtaClose-button']"));
    private static final Locator RESULT_COUNT_TEXT = new Locator("RESULT_COUNT_TEXT", By.xpath("//div[contains(@class, 'SearchResultContent_search_count')]/div"));

    public void closeContinueOnWebPopup() {
        driverTools.waitForElementToBeClickable(CONTINUE_ON_WEB_POPUP, 15);
        if (driverTools.isElementDisplayed(CONTINUE_ON_WEB_POPUP)) {
            driverTools.click(CONTINUE_ON_WEB_POPUP);
        }
    }

    public void resetFilter() {
        if (driverTools.isElementDisplayed(RESET_FILTER_BUTTON)) {
            driverTools.click(RESET_FILTER_BUTTON);
        } else {
            driverTools.waitForElementsToAppear(FILTER_BUTTON);
            driverTools.click(FILTER_BUTTON);
            driverTools.click(RESET_FILTER_BUTTON_SHEET);
            driverTools.click(APPLY_BUTTON);
        }
    }

    private Locator productWithRefund(String productTitle) {
        return new Locator("PRODUCT_WITH_REFUND", new By.ByXPath("//*[text()='"+productTitle+"' and contains(@class,'ProductCardTTD_product_title')]//parent::*//preceding-sibling::*[contains(@class,'label_wrapper')]//*[contains(text(),'100% Refund')]"));
    }
    private Locator anyProduct(String productTitle) {
        return new Locator("ANY_PRODUCT", By.xpath("//h2[normalize-space()='" + productTitle + "']"));
    }
    private Locator productMinimumPriceLocator(String productName) {
        return new Locator("PRODUCT_MINIMUM_PRICE", new By.ByXPath(
                "//*[contains(@class,'ProductCardTTD_product_title') and normalize-space()='" + productName + "']/ancestor::*[contains(@class,'ProductCardSearch_product_card') or contains(@class,'ProductCardTTD_card')][1]//*[contains(@class,'PriceModuleV2_main_price_content')]//*[contains(@class,'Text_variant_price')]"));    
    }
    private Locator productWithCampaign(String productTitle) {
        return new Locator("PRODUCT_WITH_CAMPAIGN", new By.ByXPath("//*[text()='"+productTitle+"' and contains(@class,'ProductCardTTD_product_title')]//following-sibling::*//*[contains(@class,'ProductCardTTD_campaign_label')]//span"));
    }

    public void openFirstProduct() {
        driverTools.waitForElementToAppear(FIRST_PRODUCT_CARD);
        driverTools.clickWithJavascriptExecutor(FIRST_PRODUCT_CARD);
        driverTools.waitForWebToLoad();
    }

    public void openSrpUrl(String productCategoryCodes)  {
        String url = "https://www.tiket.com/to-do/search?productAllCategoryCodes=";
        if (Configs.ENVIRONMENT == Environment.GK) {
            url = "https://gatotkaca.tiket.com/to-do/search?productAllCategoryCodes=";
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            url = "https://preprod.tiket.com/to-do/search?productAllCategoryCodes=";
        }
        driverTools.get(url + productCategoryCodes.toUpperCase());
    }

    public void clickLoginBanner() {
        WebElement loginBanner = driverTools.waitForElementToPresence(LOGIN_BANNER).orElseThrow();
        driverTools.click(loginBanner);
    }

    public boolean isLoginBannerDisplayed() {
        return driverTools.isElementDisplayed(LOGIN_BANNER);
    }

    public String getProductTitle() {
        driverTools.waitForElementToPresence(PRODUCT_TITLE);
        return driverTools.getText(PRODUCT_TITLE).orElseThrow();
    }

    public void inputSearchKeywordAttractionPage(String keyword) {
        String searchInput = "";
        if(keyword.contains("/")) {
            searchInput += keyword.split("/")[0];
        } else {
            searchInput += keyword;
        }
        driverTools.click(SEARCH_INPUT_ATTRACTION);
        WebElement inputBar = driverTools.waitForElementToAppear(SEARCH_INPUT_ATTRACTION,15).orElseThrow();
        driverTools.sendKeys(inputBar, searchInput);
    }

    public void clickFirstSuggestion() {
        driverTools.click(FIRST_PRODUCT_SUGGESTION);
    }

    private Locator createSearchItemAttractionLocator(String productName) {
        String formattedProductName = productName.trim();

        String xpath = String.format("(//a[contains(@class,'SearchItem_search_item')]//span[contains(@class,'SearchItem_search_item_title')]//span[text()='%s'])[1]", formattedProductName);

        return new Locator("SEARCH_ITEM_ATTRACTION", By.xpath(xpath));
    }


    public void selectRecommendedActivityAttractionPage(String keyword) {
        driverTools.waitForElementToAppear(createSearchItemAttractionLocator(keyword));
        String keyword1;
        String keyword2;
        if (keyword.contains("/")) {
            keyword1 = keyword.split("/")[0];
            keyword2 = keyword.split("/")[1];
        } else {
            keyword1 = keyword;
            keyword2 = keyword1;
        }
        String searchItem = driverTools.getText(createSearchItemAttractionLocator(keyword)).orElseThrow();
        if (searchItem.equalsIgnoreCase(keyword1) || searchItem.equalsIgnoreCase(keyword2)) {
            driverTools.click(createSearchItemAttractionLocator(keyword));
        } else {
            logger.warn("No " + keyword + " on the search item list, just getting " + searchItem);
        }
    }

    public boolean isGreenDotVisible() {
        driverTools.waitForElementsToAppear(FILTER_NOTIF_SIGN);
        return driverTools.waitForElementsToAppear(FILTER_NOTIF_SIGN).isPresent();
    }

    public String getSelectedFilter() {
        Assertion.assertThat("Filter active with the green dots should be visible", driverTools.isElementDisplayed(FILTER_NOTIF_SIGN), is(true));
        driverTools.clickWithJavascriptExecutor(FILTER_BUTTON);
        driverTools.waitForElementToPresence(SELECTED_FILTERS);
        Assertion.assertThat("Active attractions category label is displayed", driverTools.isElementPresent(ACTIVE_ATTRACTIONS_CATEGORY), is(true));
        List<WebElement> defaultCategory = driverTools.elements(DEFAULT_ATTRACTIONS_CATEGORY).orElseThrow();
        List<WebElement> activeCategory = driverTools.elements(ACTIVE_ATTRACTIONS_CATEGORY).orElseThrow();
        Assertion.assertThat("All attractions category should be selected", defaultCategory.size(), is(activeCategory.size()));
        String selectedFilter = driverTools.getText(SELECTED_FILTERS).orElseThrow();
        driverTools.clickWithJavascriptExecutor(APPLY_BUTTON);
        return selectedFilter;
    }

    public List<String> getSelectedFilterInProductCard(String filter) {
        WebElement availableFilters = driverTools.waitForElementToAppear(getAvailableFiltersLocator(filter)).orElseThrow();
        return driverTools.getTexts(availableFilters);
    }

    public String getSelectedFilterOnly() {
        driverTools.clickWithJavascriptExecutor(FILTER_BUTTON);
        driverTools.waitForElementToPresence(SELECTED_FILTERS);
        String selectedFilter = driverTools.getText(SELECTED_FILTERS).orElse("");
        driverTools.clickWithJavascriptExecutor(CROSS_POPUP);
        return selectedFilter;
    }

    public String getSelectedEventFilter() {
        Assertion.assertThat("Filter active with the green dots should be visible", driverTools.isElementDisplayed(FILTER_NOTIF_SIGN), is(true));
        driverTools.click(FILTER_BUTTON);
        driverTools.waitForElementToPresence(SELECTED_FILTERS);
        String selectedFilter = driverTools.getText(SELECTED_FILTERS).orElseThrow();
        driverTools.click(CLOSE_POPUP);
        return selectedFilter;
    }

    public void chooseFilter(String filter, FilterType filterType) {
        driverTools.waitForElementsToAppear(FILTER_BUTTON);
        driverTools.clickWithJavascriptExecutor(FILTER_BUTTON);
        switch (filterType) {
            case FEATURES -> {
                driverTools.waitForElementsToAppear(FEATURES_BUTTON);
                driverTools.scrollIntoWebView(FEATURES_BUTTON);
            }
            case PROMO -> {
                driverTools.waitForElementsToAppear(PROMO_BUTTON);
                driverTools.scrollIntoWebView(PROMO_BUTTON);
            }
            default -> logger.log("Filter not recognized: " + filterType);
        }
        Locator filterElement = createLocatorForFilter(filter);

        if (driverTools.isElementDisplayed(filterElement)) {
            driverTools.scrollIntoWebView(filterElement);
            driverTools.clickWithJavascriptExecutor(filterElement);
            logger.log("Selected filter: " + filter);
        } else {
            logger.log("Filter not found: " + filter);
        }
        driverTools.clickWithJavascriptExecutor(APPLY_BUTTON);
    }

    public List<String> getFilterByCategory(String filterType) {
        driverTools.waitForElementsToAppear(FILTER_BUTTON);
        driverTools.click(FILTER_BUTTON);
        switch (filterType) {
            case "Features" -> {
                driverTools.waitForElementsToAppear(FEATURES_BUTTON);
                driverTools.scrollIntoWebView(FEATURES_BUTTON);
                List<String> availableFilters = driverTools.getTexts(FEATURES_CATEGORY_FILTER);
                driverTools.click(APPLY_BUTTON);
                return availableFilters;
            }
            default -> logger.log("Filter not recognized: " + filterType);
        }

        return List.of();
    }

    public boolean checkLocationCampaign(String location, String campaignName) {
        List<WebElement> productTitleList = driverTools.waitForElementsToAppear(PRODUCT_TITLE).orElseThrow();
        int totalProduct = productTitleList.size();
        logger.log("Total product displayed: " + totalProduct);
        List<WebElement> productLocationList = driverTools.elements(PRODUCT_LOCATION).orElseThrow();
        List<WebElement> productContent = driverTools.elements(PRODUCT_CONTENT).orElseThrow();
        for (int i=0; i<totalProduct; i++) {
            String activityName = driverTools.getText(productTitleList.get(i));
            String activityLocation = driverTools.getText(productLocationList.get(i)).toLowerCase();
            String activityContent = driverTools.getText(productContent.get(i)).toLowerCase();
            location = location.toLowerCase();
            String regexId = "^([\\w\\s]+,\\s+" + Pattern.quote(location) + "\\b[\\w\\s]*)$";
            String regexEn = "^([\\w\\s]+,\\s+(?:\\w+\\s)*" + Pattern.quote(location) + ")$";
            String regexAny = "^[\\w\\s]*" + Pattern.quote(location) + "[\\w\\s]*$";
            Assertion.assertThat("Activity: \""+activityName+"\" location should be in or near " + location, activityLocation, is(either(matchesPattern(regexId)).or(matchesPattern(regexEn)).or(matchesPattern(regexAny)).or(containsString("near "+location)).or(containsString("dekat "+location))));
            Assertion.assertThat("Activity: \""+activityName+"\" in the correct campaign " + campaignName, activityContent, is(containsString(campaignName.toLowerCase())));
        }
        return true;
    }

    public void openSearchPageCustom() {
        Deeplink homeDeeplink = new Deeplink("Home",
                "https://en.gatotkaca.tiket.com/to-do/search",
                "https://en.preprod.tiket.com/to-do/search",
                "https://en.tiket.com/to-do/search");
        driverTools.deeplink(homeDeeplink);
    }

    private Locator createLocatorForFilter(String filterName) {
        String filterNameEn;
        String filterNameId;
        if (filterName.contains("/")) {
            filterNameEn = filterName.split("/")[0];
            filterNameId = filterName.split("/")[1];
        } else {
            filterNameEn = filterName;
            filterNameId = filterName;
        }
        return new Locator(filterNameEn.toUpperCase() + "_FILTER", new By.ByXPath("//span[contains(@class, 'Chip_text') and (contains(text(),'"+filterNameEn+"') or contains(text(),'"+filterNameId+"'))]"));
    }

    private Locator getAvailableFiltersLocator(String filterName) {
        String filterNameEn;
        String filterNameId;
        if (filterName.contains("/")) {
            filterNameEn = filterName.split("/")[0];
            filterNameId = filterName.split("/")[1];
        } else {
            filterNameEn = filterName;
            filterNameId = filterName;
        }
        return new Locator(filterNameEn.toUpperCase() + "_FILTER", new By.ByXPath("//span[contains(@class, 'Text_text') and (contains(text(),'"+filterNameEn+"') or contains(text(),'"+filterNameId+"'))]"));
    }

    public boolean isFilterApplied(String filterName) {
        switch (filterName) {
            case "tiket FLEXI" -> {
                return driverTools.isElementDisplayed(TIKET_FLEXI)|| driverTools.isElementDisplayed(TIKET_FLEXI_BAHASA);
            }
            case "Instant Confirmation", "Konfirmasi Instan"-> {
                return driverTools.isElementDisplayed(INSTANT_CONFIRMATION);
            }
            default -> {
                logger.log("Filter not recognized: " + filterName);
                return false;
            }
        }
    }

    public boolean isRefund100PVisible() {
        driverTools.waitForElementsToAppear(PRODUCT_TITLE);
        return driverTools.waitForElementsToAppear(REFUND_100P).isPresent();
    }

    public String getSearchLocation() {
        driverTools.waitForElementToPresence(PRODUCT_TITLE);
        return driverTools.getText(SEARCH_LOCATION).orElseThrow();
    }

    public String getSearchInputProduct() {
        driverTools.waitForElementToPresence(SEARCH_INPUT_PRODUCT);
        return driverTools.getValueAttribute(SEARCH_INPUT_PRODUCT, "value");
    }

    public void clickLocation() {
        WebElement searchLocation = driverTools.waitForElementToAppear(SEARCH_LOCATION).orElseThrow();
        driverTools.click(searchLocation);
    }

    public void inputLocationSearchBar(String location) {
        if (location.contains("/")) location = location.split("/")[0];
        driverTools.waitForElementToAppear(SEARCH_INPUT_LOCATION);
        driverTools.sendKeys(SEARCH_INPUT_LOCATION, location);
    }

    private List<String> getLocationTypeLanguage(String locationType) {
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
            default:
                logger.log("Location type not recognized: " + locationType);
        }

        return List.of(locationTypeEng, locationTypeInd);
    }

    public String clickLocationSearchResult(String location, String locationType) {
        String locationEn;
        String locationId;
        if (location.contains("/")) {
            locationEn = location.split("/")[0];
            locationId = location.split("/")[1];
        } else {
            locationEn = location;
            locationId = location;
        }
        List<String> locationTypeLanguage = getLocationTypeLanguage(locationType);
        List<WebElement> searchSuggestionTitle = driverTools.waitForElementsToAppear(SEARCH_SUGGESTION_TITLE).orElseThrow();
        int totalSuggestion = searchSuggestionTitle.size();
        logger.log("Total search suggestion list: " + totalSuggestion);
        List<WebElement> searchSuggestionType = driverTools.elements(SEARCH_SUGGESTION_TYPE).orElseThrow();
        for (int i=0; i<totalSuggestion; i++) {
            String suggestionTitle = driverTools.getText(searchSuggestionTitle.get(i));
            String suggestionType = driverTools.getText(searchSuggestionType.get(i));
            if ((suggestionTitle.equalsIgnoreCase(locationEn) || suggestionTitle.equalsIgnoreCase(locationId)) && (suggestionType.contains(locationTypeLanguage.get(0)) || suggestionType.contains(locationTypeLanguage.get(1))) ) {
                driverTools.click(searchSuggestionTitle.get(i));
                return suggestionTitle;
            }
        }
        logger.log("Location not found: " + location);
        return "";
    }

    public String findCampaignProduct(String productName, int totalScroll) {
        boolean isProductFound = false;
        String campaignLabel = "";
        for (int i = 1; i <= totalScroll; i++) {
            if (driverTools.waitForElementsToAppear(productWithCampaign(productName)).isPresent()) {
                campaignLabel = driverTools.getText(productWithCampaign(productName)).orElseThrow();
                isProductFound = true;
            }
            if (isProductFound) break;
            List<WebElement> productList = driverTools.waitForElementsToAppear(PRODUCT_TITLE).orElseThrow();
            int totalProduct = productList.size();
            logger.log("Scroll: " + i);
            driverTools.scrollIntoWebView(productList.get(totalProduct - 1));
            if (i == totalScroll) logger.fail("Max scroll reached, product with campaign not found");
        }
        return campaignLabel;
    }

    public void chooseProductSrp(boolean isRefund, String productTitle, int totalScroll) {
        if (totalScroll == 0) totalScroll = 1;
        if (!productTitle.isEmpty()) {
            logger.log("Find product with title: " + productTitle + " and refund: " + isRefund);
            findProduct(isRefund, productTitle, totalScroll);
        } else if (isRefund) {
            logger.log("Find random product with refund");
            findRandomProductRefund();
        } else {
            logger.log("find random product");
            findRandomProduct();
        }
    }

    private void findProduct(boolean isRefund, String productTitle, int totalScroll) {
        for (int i = 1; i <= totalScroll; i++) {
            boolean isProductFound = selectProduct(isRefund, productTitle);
            if (isProductFound) break;

            List<WebElement> productList = driverTools.waitForElementsToAppear(PRODUCT_TITLE).orElseThrow();
            int totalProduct = productList.size();
            logger.log("Scroll: " + i);
            driverTools.scrollIntoWebView(productList.get(totalProduct - 1));
            if (i == totalScroll) logger.fail("Max scroll reached");
        }
    }

    private boolean selectProduct(boolean isRefund, String productTitle) {
        boolean isProductFound = false;
        if (isRefund) {
            if (driverTools.waitForElementsToAppear(productWithRefund(productTitle)).isPresent()) {
                driverTools.clickWithJavascriptExecutor(productWithRefund(productTitle));
                isProductFound = true;
            }
        } else {
            if (driverTools.waitForElementsToAppear(anyProduct(productTitle)).isPresent()) {
                driverTools.clickWithJavascriptExecutor(anyProduct(productTitle));
                isProductFound = true;
            }
        }
        return isProductFound;
    }

    private void findRandomProductRefund() {
        List<WebElement> productList = driverTools.waitForElementsToAppear(REFUND_100P).orElseThrow();
        int totalProduct = productList.size();
        driverTools.clickWithJavascriptExecutor(productList.get(generateRandomInt(1,totalProduct)));
    }

    private void findRandomProduct() {
        List<WebElement> productList = driverTools.waitForElementsToAppear(PRODUCT_TITLE).orElseThrow();
        int totalProduct = productList.size();
        driverTools.clickWithJavascriptExecutor(productList.get(generateRandomInt(1,totalProduct)));
    }

    public boolean checkAllLocationsBadgeSRP() {
       return driverTools.isElementDisplayed(ALL_LOCATION_CHIP_SRP);
    }

    public abstract void filterDate(int date);

    public Locator getDateLocator(int date){
    return new Locator("DATE", AppiumBy.xpath("//*[contains(@class,'Day_day_number')and not(contains(@class,'disabled')) and text()='"+date+"']"));
    }

    public List<String> getProductTitles(){
        driverTools.waitForElementToPresence(PRODUCT_TITLE);
        return driverTools.getTexts(PRODUCT_TITLE);
    }
    public String getMinimumPriceByProductName(String productName) {
        scrollToProductOnSrp(productName, 3);
        Locator priceLocator = productMinimumPriceLocator(productName);
        WebElement priceElement = driverTools.waitForElementToAppear(priceLocator).orElseThrow();
        String price = driverTools.getText(priceElement);
        logger.log("Minimum price on SRP for '" + productName + "': " + price);
        return price;
    }
    public String getMinimumPriceByProductSlug(String productSlug) {
        scrollToProductOnSrpSlug(productSlug, 3);
        Locator priceLocator = productMinimumPriceLocatorSlug(productSlug);
        WebElement priceElement = driverTools.waitForElementToAppear(priceLocator).orElseThrow();
        String price = driverTools.getText(priceElement);
        logger.log("Minimum price on SRP for slug '" + productSlug + "': " + price);
        return price;
    }
    private Locator productMinimumPriceLocatorSlug(String productSlug) {
        return new Locator("PRODUCT_MINIMUM_PRICE",
                By.xpath("//a[contains(@href,'/to-do/" + productSlug + "')]"
                        + "//div[contains(@class,'PriceModuleV2_main_price_content')]"
                        + "//div[contains(@class,'Text_variant_price')]"));
    }
    private Locator productCardBySlug(String productSlug) {
        return new Locator("PRODUCT_CARD",
                By.xpath("//a[contains(@href,'/to-do/" + productSlug + "')]"));
    }
    private void scrollToProductOnSrp(String productTitle, int totalScroll) {
        for (int i = 1; i <= totalScroll; i++) {

            List<WebElement> productList =
                    driverTools.waitForElementsToAppear(PRODUCT_TITLE).orElseThrow();

            for (WebElement product : productList) {
                logger.log("Visible title: [" + product.getText() + "]");
            }

            logger.log("Expected title: [" + productTitle + "]");
            for (WebElement product : productList) {
                if (product.getText().trim().equals(productTitle)) {
                    driverTools.scrollIntoWebView(product);
                    return;
                }
            }

            logger.log("Scroll to find product on SRP: " + i);
            driverTools.scrollIntoWebView(productList.get(productList.size() - 1));
        }

        logger.fail("Product not found on SRP: " + productTitle);
    }
    private void scrollToProductOnSrpSlug(String productSlug, int totalScroll) {
        Locator productLocator = new Locator(
                "PRODUCT_CARD",
                By.xpath("//a[contains(@href,'/to-do/" + productSlug + "')]"));

        for (int i = 1; i <= totalScroll; i++) {

            if (driverTools.waitForElementToAppear(productLocator).isPresent()) {
                driverTools.scrollIntoWebView(productLocator);
                return;
            }

            List<WebElement> productList =
                    driverTools.waitForElementsToAppear(PRODUCT_TITLE).orElseThrow();

            logger.log("Scroll to find product on SRP: " + i);
            driverTools.scrollIntoWebView(productList.get(productList.size() - 1));
        }

        logger.fail("Product not found on SRP with slug: " + productSlug);
    }
    public void switchToNewTab() {
        driverTools.switchToNewTab();
    }

    public void clickTodayFilterButton() {
        driverTools.waitForElementToAppear(TODAY_FILTER_BUTTON);
        driverTools.clickWithJavascriptExecutor(TODAY_FILTER_BUTTON);
    }

    public boolean checkCountResultExist() {
        return driverTools.isElementDisplayed(RESULT_COUNT_TEXT);
    }
}
