package com.tiket.page.accommodation.homes.homeslandingpage;

import com.tiket.model.Session;
import com.tiket.pojo.accommodation.homes.HomesData;
import com.tiket.pojo.accommodation.homes.HomesLandingPageModuleData;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.Map;

import static org.hamcrest.core.Is.is;

public abstract class HomesLandingPage<T extends WebDriver> extends BasePage<T> {

    public HomesLandingPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> HomesLandingPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case IOS -> {return new HomesLandingPageIOS<>(session);}
            case ANDROID -> {return new HomesLandingPageAndroid<>(session);}
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public abstract void clickSubmit();
    public abstract void searchStayLocation(String location);
    public abstract void openTotalGuest();
    public abstract String getTotalGuest();
    public abstract void openSearchLocation();
    public abstract String chooseDestination(String destination);
    public abstract void chooseDestinationLoop(int totalLoop, String notContainDestination);
    public abstract String chooseRecentSearch();
    public abstract boolean findRecentSearch(String destination, int totalSwipe);
    public abstract void clickRecentSearchDeleteButton();
    public abstract boolean isRecentSearchVisible();
    public abstract String getSearchDestination();
    public abstract void clickSearchFieldText();
    public abstract boolean isSearchFormExist();
    public abstract boolean isSearchLocationExist();
    public abstract boolean isGuestFormExist();
    public abstract boolean isCalendarExist();
    public abstract boolean clickViewMoreButton();
    public abstract boolean clickBackToTopButton();
    public abstract String getHeroBannerTitle();
    public abstract boolean compareModuleTitleCaption(ArrayList<String> modules);
    public abstract String getLandscapeBannerTitle();
    public abstract void goToTimedPromoSection();
    public abstract String getTimedPromoTitle();
    public abstract String selectTimedPromoLocation(int index);
    public abstract boolean compareTimedPromoHomesName(HomesLandingPageModuleData homesLandingPageModuleData);
    public abstract String clickSeeAllButton();
    public abstract String getLandscapeBannerSubtitle();
    public abstract boolean compareTotalLandscapeBanner(int dataLength);
    public abstract String getThreePointsBannerAttribute(String attributeName);
    public abstract ArrayList<HomesData.ListOfTitleAndSubTitle> getThreePointsBannerContentList();
    public abstract void clickThreePointBannersButton();
    public abstract void swipeToThreePointsBanner();
    public abstract void swipeToTitleMultipleHotelId(String title);
    public abstract boolean isTitleAndSubTitleDisplayedMultipleHotelId(String title,String subTitle);
    public abstract boolean verifyLocationsMultipleHotelId(ArrayList<HomesData.ContentData> contentLists);
    public abstract void clickLocationChipBtnMultipleHotelId(String location);
    public abstract boolean verifyButtonTextMultipleHotelId(String buttonText);
    public abstract String getMiniCardTitle();
    public abstract boolean compareMiniCardTitleList(HomesData data);
    public abstract void clickMiniCard(int index, String title);
    public abstract String getFabText();
    public abstract boolean verifySeeMoreAndBackToTopFunctionality(int times);
    public abstract void swipeToSatisfyWanderlust();
    public abstract void clickWanderlustCategory(String category);

    public boolean compareThreeBannersContentList(ArrayList<HomesData.ListOfTitleAndSubTitle> threePointBannerContentListApi,ArrayList<HomesData.ListOfTitleAndSubTitle> threePointBannerContentListApp) {
        logger.log("threePointBannerContentListApp size -> " + threePointBannerContentListApp.size());
        logger.log("threePointBannerContentListApi size -> " + threePointBannerContentListApi.size());

        if(threePointBannerContentListApp.size() != threePointBannerContentListApi.size()) {
            return false;
        }

        for(int i = 0; i < threePointBannerContentListApi.size(); i++) {
            String titleFromApi = threePointBannerContentListApi.get(i).title;
            String subTitleFromApi = threePointBannerContentListApi.get(i).subTitle;

            String titleFromApp = threePointBannerContentListApp.get(i).title;
            String subTitleFromApp = threePointBannerContentListApp.get(i).subTitle;

            Assertion.assertThat("Three Points Banner Content List Title "+(i+1)+" on the App should be equal with API", titleFromApp, is(titleFromApi));
            Assertion.assertThat("Three Points Banner Content List Sub Title "+(i+1)+" on the App should be equal with API", subTitleFromApp, is(subTitleFromApi));
        }

        return true;
    }
}