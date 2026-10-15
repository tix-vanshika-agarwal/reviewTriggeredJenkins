package com.tiket.page.ttd.searchresultpage;

import com.tiket.model.Deeplink;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.page.common.webview.WebViewPage;
import com.tiket.pojo.ttd.GETProductListTTDSRPPOJO;
import com.tiket.pojo.ttd.TTDSRPByLocationPOJO;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import com.tiket.web.page.ttd.TtdProductDetailPage.TtdProductDetailPageWeb;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

import java.util.List;

public abstract class TTDSearchResultPage<T extends WebDriver> extends BasePage<T> {

    public TTDSearchResultPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> TTDSearchResultPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {return new TTDSearchResultPageAndroid<>(session);}
            case IOS -> {return new TTDSearchResultPageIOS<>(session);}
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public abstract boolean isLpgHeaderBannerDisplayed();
    public abstract void clickLpgHeaderBanner();
    public abstract boolean checkLpgLabelCard(int totalCard, String language);
    public abstract void clickProductWithLpg();
    public abstract boolean isAutoCompleteSuggestionValid(String keyword, String type, String pageType);
    public abstract boolean isKeywordSuggestionDisplayed(String keyword);
    public abstract boolean isSearchResultPageShown();
    public abstract String getSelectedCategory();
    public abstract boolean isCategoriesSubCategoriesValid();
    public abstract boolean checkResultList(String modifyType, String modifySubType, int totalResult, String[] validationData);
    public abstract void selectLocation(String keyword, String pageType);
    public abstract String pickAndClickOneResult();
    public abstract void setAppPermissionAllow();
    public abstract boolean checkLocationCampaign(String location, String promoName, int totalActivity);
    public abstract boolean isSortByNearestLocationCorrect(String totalActivitiesToCompare);
    public abstract void shortBy(String sortType);
    public abstract void clickSortFilter();
    public abstract void clickSortByButton(String sortType);
    public abstract List<String> selectPriceFilter(String priceChanges, int swipePercentage);
    public abstract boolean isPriceFilterApplied(int totalActivitiesToCompare, List<String> selectedPrice,String currency);
    public abstract void clickFilter();
    public abstract void clickReset();
    public abstract void removeAllFilter();
    public abstract List<String> selectGeneralFilter(String filter);
    public abstract void selectPromoFilter(String promoName);
    public abstract void submitFilter();
    public abstract boolean isGeneralFilterApplied(String totalActivitiesToCompare, List<String> selectedFilter);
    public abstract boolean isFilterApplied(int totalResult, String filterType);
    public abstract void openPdpDeeplink(String slugProductName);

    public void openSomePdpDeeplink(String[] slugProductNames) {
        WebViewPage<T> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<T> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        for (String slug : slugProductNames) {
            openPdpDeeplink(slug);
            webViewPage.switchToWebViewUrl("to-do");
            ttdProductDetailPageWeb.getProductName();
            webViewPage.switchToNative();
        }
    }

    public void openSrpDeeplinkUsingPublicId(String areaId){
        Deeplink srpDeeplink = new Deeplink("SRP_DEEPLINK",
                "tiket://m.gatotkaca.tiket.com/to-do/search?publicIds=" + areaId,
                "tiket://m.preprod.tiket.com/to-do/search?publicIds=" + areaId,
                "tiket://m.tiket.com/to-do/search?publicIds=" + areaId);
        driverTools.deeplink(srpDeeplink);
        driverTools.switchToNative();
    }

    public void openSrpDeeplink(String productCategoryCodes) {
        Deeplink srpDeeplink = new Deeplink("SRP_DEEPLINK",
                "tiket://m.gatotkaca.tiket.com/to-do/search?productCategoryCodes=" + productCategoryCodes.toUpperCase(),
                "tiket://m.preprod.tiket.com/to-do/search?productCategoryCodes=" + productCategoryCodes.toUpperCase(),
                "tiket://m.tiket.com/to-do/search?productCategoryCodes=" + productCategoryCodes.toUpperCase());
        driverTools.deeplink(srpDeeplink);
        driverTools.switchToNative();
    }

    public abstract void clickJoinNow();
    public abstract boolean isLoginBannerDisplayed();
    public abstract void switchCategory(String category);
    public abstract boolean isSortByLowestPrice(int totalActivity);
    public abstract String getProductBelowPrice(double maxPrice, int totalActivity);
    public abstract void selectProduct(String productName);
    public abstract void selectTheProduct();
    public abstract void chooseProduct(String productName, int totalSwipe);
    public abstract boolean isAllTabShown();
    public abstract String getSelectedTab();
    public abstract String selectDateFilter(String date);
    public abstract void openDateFilter();
    public abstract boolean verifyDateFilterChip();
    public abstract boolean isFilterActivated(String filterType);
    public abstract String getLocation();
    public abstract void findProductByProductName(String product);
    public abstract void findProductByLabelName(String labelType);
    public abstract void clickLocation();
    public abstract String selectRandomLocation();
    public abstract boolean compareProductNameWithAPI(TTDSRPByLocationPOJO response, int productCount);
    public abstract void clickBack();
    public abstract boolean isCampaignLabelPresent();
    public abstract boolean isDateFilterValid(String date, int totalProduct);
    public abstract void inputLocationSearchBar(String location);
    public abstract String clickLocationSearchResult(String location, String locationType);
    public abstract String findCampaignProduct(String productName, int totalSwipe);
    public abstract boolean verifyDisplayedCurrency(String currency, String sectionType);
    public abstract boolean verifyDisplayedCurrencyProductCard(String currency);
    public abstract boolean verifyDisplayedCurrencyPriceFilter(String currency);
    public abstract void openPriceFilter();
    public abstract void closeFilterBottomsheet();
    public abstract void selectLoyaltyProduct();
    public abstract void selectLoyaltyProduct(String productName);
    public abstract boolean verifyDisplayedLoyaltyBanner();
    public abstract String[] getProductList(int totalActivity);
    public abstract Boolean isPriceBelowTenNonIDR(String currency, String productCount);
    public abstract Boolean isPriceAboveTenNonIDR(String currency, String productCount);
    public abstract Boolean verifyDisplayedPriceAmountProductCard(int currencyDecimalDigit, int productCount, GETProductListTTDSRPPOJO productDatafromAPI);
    public abstract Boolean verifyDisplayedPriceAmountProductCard(int currencyDecimalDigit, int productCount, GETProductListTTDSRPPOJO productDatafromAPI, String currency);
    public abstract Boolean verifyRedirectionToSearchResultPage(String sectionType);
    public abstract void clickSearchBar();
    public abstract boolean checkBoostingLabels(String language);
    public abstract boolean isCartContainsLoyaltyBadge();
    public abstract int getProductCountOnSrp();
    public abstract boolean verifyLoyaltyIconOrCampaignOnSRPCard(String tier);
    public abstract Locator getNextMonthElement(String nextMonthId, String nextMonthEng);
}