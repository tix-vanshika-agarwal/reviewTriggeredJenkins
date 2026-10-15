package com.tiket.page.ttd.searchpage;

import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.Map;

public abstract class TTDSearchPage<T extends WebDriver> extends BasePage<T> {

    public TTDSearchPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> TTDSearchPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {return new TTDSearchPageAndroid<>(session);}
            case IOS -> {return new TTDSearchPageIOS<>(session);}
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public abstract void selectSection(String section);
    public abstract boolean checkToDoRank(int expectedTotalRank);
    public abstract int getTotalItems();
    public abstract String clickItemOnList(String list);
    public abstract void clickSeeAllButton();
    public abstract boolean isSearchPageValid();
    public abstract boolean isPopularDestinationDisplayed();
    public abstract boolean isTopKeywordDisplayed();
    public abstract boolean isPopularActivitiesDisplayed();
    public abstract boolean isPopularPartnersDisplayed();
    public abstract boolean isRecentlySearchedValid(String clickedResult);
    public abstract void inputSearchKeyword(String keyword);
    public abstract void clickNearbyActivity();
    public abstract void clickClearSearch();
    public abstract void clickAndCheckAllTopKeyword();
    public abstract void clickSeeAllActivities();
    public abstract String pickAndClickPopularDestination();
    public abstract String pickAndClickPopularPartner();
    public abstract Map<String, String> pickAndClickPopularActivity();
    public abstract void selectRecommendedActivity(String keyword);
    public abstract void selectRecommendedActivitySRP(String keyword);
    public abstract void clickClearAll();
    public abstract boolean isSearchCleared();
    public abstract void clickPopularActivitySeeAll();
    public abstract String getSearchFieldText();
    public abstract void clickPopularDestinationSeeAll();
    public abstract void clickRefreshTopKeyword();
    public abstract List<String> getTopKeywordList();
    public abstract String clickRandomKeyword();
    public abstract void clickBack();
    public abstract void findAndClickPartnerPage(String keyword);
    public abstract String selectRecommendedDestination(String destination);
    public abstract List<String> getProductTitleOrder();
    public abstract void clickRecentlySearchedItem(String keyword);
    public abstract void clickLocationButton();
    public abstract int verifyLocationChips();
    public abstract String getCityLocationChipText();
    public abstract void clickCityLocationChip();
    public abstract String getLocation();
}