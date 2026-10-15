package com.tiket.page.accommodation.homes.homeslandingpage;

import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.page.accommodation.hotel.landingpage.LandingPage;
import com.tiket.page.accommodation.hotel.searchresultpage.SearchResultPage;
import com.tiket.pojo.accommodation.homes.HomesData;
import com.tiket.pojo.accommodation.homes.HomesLandingPageModuleData;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.tiket.core.CoreFunctions.generateRandomInt;
import static com.tiket.core.CoreFunctions.splitLanguageData;
import static com.tiket.page.accommodation.hotel.landingpage.LandingPageIOS.*;

public class HomesLandingPageIOS<T extends WebDriver> extends HomesLandingPage<T> {

    public HomesLandingPageIOS(Session<T> session) {
        super(session);
    }

    private static final Locator RECENT_SEARCH_CONTAINER = new Locator("RECENT_SEARCH_CONTAINER", AppiumBy.id("TNMRecentSearchTableViewCell"));
    private static final Locator RECENT_SEARCH_LOCATION = new Locator("RECENT_SEARCH_LOCATION", AppiumBy.name("recent_hotel_name"));
    private static final Locator RECENT_SEARCH_DELETE_BUTTON = new Locator("RECENT_SEARCH_DELETE_BUTTON", AppiumBy.iOSClassChain("**/*[`name == 'TNMRecentSearchTableViewCell'`]/XCUIElementTypeButton"));
    private static final Locator SEARCH_FIELD_LANDING_PAGE = new Locator("SEARCH_FIELD_LANDING_PAGE", AppiumBy.name("search_button_landing_page"));
    private static final Locator HERO_BANNER_TITLE = new Locator("HERO_BANNER_TITLE", AppiumBy.id("hero-banner-static"));
    private static final Locator HERO_BANNER_CAPTION = new Locator("HERO_BANNER_CAPTION", AppiumBy.id("hero-banner-typing"));
    public static final Locator SEARCH_FIELD_POPUP = new Locator("SEARCH_FIELD_POPUP", AppiumBy.name("search_field"));
    private static final Locator TOTAL_GUEST_BUTTON = new Locator("TOTAL_GUEST_BUTTON", AppiumBy.name("total_room_guest"));
    public static final Locator CLOSE_SEARCH_PAGE_TWO = new Locator("CLOSE_SEARCH_PAGE_TWO", AppiumBy.xpath("(//XCUIElementTypeButton[@name='ic cancel grey'])[last()]"));
    public static final Locator HOMES_CARD = new Locator("HOMES_CARD", AppiumBy.xpath("//XCUIElementTypeCell[(contains(@name, 'TVCell') or contains(@name,'UITableViewCell')) and not(contains(@name, 'TopSection'))]"));
    public static final Locator GUEST_FORM = new Locator("GUEST_FORM", AppiumBy.xpath("//XCUIElementTypeScrollView"));
    public static final Locator DATE_FIELD_BUTTON = new Locator("DATE_FIELD_BUTTON", AppiumBy.xpath("//XCUIElementTypeCell[@name='TNMSearchFormTableViewCell']//XCUIElementTypeStaticText[contains(@label,'-')]"));
    public static final Locator VIEW_MORE_BUTTON = new Locator("VIEW_MORE_BUTTON", AppiumBy.id("view_more_button"));
    public static final Locator LANDING_BANNER = new Locator("LANDING_BANNER", AppiumBy.xpath("//*[@name='TVCell<TNMLandingBannerView>' and @visible='true']"));
    private static final Locator HOMES_LOCATION_CHIPS = new Locator("HOMES_LOCATION_CHIPS", AppiumBy.id("page_module_chip_text"));
    public static final Locator LANDING_PAGE_FOOTER = new Locator("LANDING_PAGE_FOOTER", AppiumBy.xpath("//*[@name='TNMFooterTableViewCell' and @visible='true']"));
    public static final Locator SCREEN = new Locator("SCREEN", AppiumBy.className("XCUIElementTypeTable"));
    public static final Locator BACK_TO_TOP_BUTTON = new Locator("BACK_TO_TOP_BUTTON", AppiumBy.id("tds_ic_chevron_up"));
    private static final Locator LANDSCAPE_BANNER_SUB_TITLE = new Locator("LANDSCAPE_BANNER_SUB_TITLE", AppiumBy.iOSClassChain("**/*[`name == 'landscape-banner'`][1]/XCUIElementTypeStaticText[`name != 'Label'`][1]"));
    private static final Locator LANDSCAPE_BANNER_TITLE = new Locator("LANDSCAPE_BANNER_TITLE", AppiumBy.iOSClassChain("**/*[`name == 'landscape-banner'`][1]/XCUIElementTypeStaticText[`name != 'Label'`][2]"));
    private static final Locator TIMED_PROMO_SEE_ALL_BUTTON = new Locator("TIMED_PROMO_SEE_ALL_BUTTON", AppiumBy.xpath("//*[contains(@name,'TimedPromoView')]//XCUIElementTypeButton//*[@name]"));
    private static final Locator TIMED_PROMO_TITLE = new Locator("TIMED_PROMO_TITLE", AppiumBy.xpath("//*[contains(@name,'TimedPromoView')]//*[contains(@label,'Berakhir dalam') or contains(@label,'Promo ends in')]//preceding-sibling::XCUIElementTypeStaticText[not(contains(@name,'THMRatingView'))]"));
    private static final Locator TIMED_PROMO_EMPTY_HOMES = new Locator("TIMED_PROMO_EMPTY_HOMES", AppiumBy.iOSClassChain("**/*[`name contains 'TimedPromoView'`]/**/XCUIElementTypeButton[-1]"));
    public static final Locator THREE_POINTS_BANNER_TITLE = new Locator("THREE_POINTS_BANNER_TITLE", AppiumBy.id("three-points-title"));
    public static final Locator THREE_POINTS_BANNER_SUB_TITLE = new Locator("THREE_POINTS_BANNER_SUB_TITLE", AppiumBy.id("three-points-description"));
    public static final Locator THREE_POINTS_BANNER_BUTTON = new Locator("THREE_POINTS_BANNER_BUTTON_TEXT", AppiumBy.xpath("//*[@name='three-points-description']/following-sibling::XCUIElementTypeButton[1]"));
    public static final Locator THREE_POINTS_BANNER_CONTENT_LIST = new Locator("THREE_POINTS_BANNER_CONTENT_LIST", AppiumBy.xpath("//XCUIElementTypeCell[contains(@name,'TNMLandingJoinHomesItemView')]"));
    public static final Locator LANDSCAPE_BANNER_DETAIL_PAGE_TITLE_CLOSE_BTN_NATIVE = new Locator("LANDSCAPE_BANNER_DETAIL_PAGE_TITLE_CLOSE_BTN_NATIVE", AppiumBy.iOSClassChain("**/XCUIElementTypeButton"));
    private static final Locator MINI_CARD_TITLE = new Locator("MINI_CARD_TITLE", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@name,'mini card')]"));
    public static final Locator TITLE_ON_MINI_CARD = new Locator("TITLE_ON_MINI_CARD", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@name,'villa with')]"));
    public static final Locator SUB_TITLE_MULTIPLE_HOTEL_ID = new Locator("SUB_TITLE_MULTIPLE_HOTEL_ID", AppiumBy.iOSClassChain("**/*[`name contains 'TVAtomicCell'`][1]/**/XCUIElementTypeStaticText"));
    public static final Locator LOCATIONS_MULTIPLE_HOTEL_ID = new Locator("LOCATIONS_MULTIPLE_HOTEL_ID", AppiumBy.iOSClassChain("**/*[`name == 'TVCell<TDSChipsViewV2>+MultipleHotelId-Chips'`][1]/**/XCUIElementTypeStaticText"));
    private static final Locator SUBMIT_BUTTON = new Locator("SUBMIT_BUTTON", AppiumBy.xpath("(//XCUIElementTypeButton[contains(@label,'Search') or contains(@label,'Ayo Cari') or contains(@label,'Simpan') or contains(@label,'Save') or contains(@label,'Selesai') or contains(@label,'Done')])[last()]"));
    public static final Locator HOMES_POPULAR_DESTINATION = new Locator("HOMES_POPULAR_DESTINATION", AppiumBy.xpath("//*[@name='popular_destination_list']//XCUIElementTypeStaticText"));
    public static final Locator HOME_TEXT_INPUT = new Locator("HOME_TEXT_INPUT",AppiumBy.xpath("//*[@name=\"search_field\" and @type=\"XCUIElementTypeTextField\"]"));
    private Locator getTimedPromoLocationButton(int index) {
        return new Locator("TIMED_PROMO_LOCATION_BUTTON", AppiumBy.xpath("(//*[contains(@name,'TimedPromoView')]//XCUIElementTypeOther//XCUIElementTypeStaticText)[" + index + "]"));
    }

    private static Locator getTimedPromoHomesName(String homesName) {
        return new Locator("TIMED_PROMO_HOMES_NAME", AppiumBy.iOSClassChain("**/*[`name contains 'TimedPromoView'`]/*[`label contains '" + homesName + "'`]"));
    }

    @Override
    public void clickSubmit() {
        driverTools.waitForElementToPresence(SUBMIT_BUTTON, 20).orElseThrow();
        driverTools.clickElementByCoordinate(SUBMIT_BUTTON);
    }

    @Override
    public void searchStayLocation(String location) {
        String separator = "/";
        String[] splitData = splitLanguageData(location, separator);
        String locationId = splitData[0];
        String locationEn = splitData[1];

        driverTools.waitForElementToAppear(HOMES_CARD, 20).orElseThrow();
        driverTools.waitForElementToPresence(SEARCH_FIELD_LANDING_PAGE).orElseThrow();
        driverTools.clickElementByCoordinate(SEARCH_FIELD_LANDING_PAGE);
        logger.log("click search field landing page");
        if(driverTools.waitForElementToPresence(SEARCH_FIELD_POPUP).isPresent()){
            driverTools.clickElementByCoordinate(SEARCH_FIELD_POPUP);
        }
        driverTools.waitForElementToPresence(SEARCH_FIELD_TEXT);
        try {
            driverTools.clickElementByCoordinate(SEARCH_FIELD_TEXT);
        } catch (Exception e) {
            logger.log("failed to click the search field by coordinate: " + e.getMessage());
            driverTools.click(SEARCH_FIELD_TEXT);
        }
        WebElement searchField  = driverTools.waitForElementToPresence(SEARCH_FIELD_TEXT).orElseThrow();
        driverTools.sendKeysKeyboardNoClick(searchField, locationId);
        driverTools.hideKeyboard(1);
        WebElement searchListLocation = driverTools.waitForElementToPresence(searchList(locationId, locationEn), 30).orElseThrow();
        driverTools.clickElementByCoordinate(searchListLocation);
    }

    @Override
    public void openTotalGuest() {
        driverTools.waitForElementToPresence(TOTAL_GUEST_BUTTON).orElseThrow();
        driverTools.click(TOTAL_GUEST_BUTTON);
    }

    @Override
    public String getTotalGuest() {
        WebElement totalGuestText = driverTools.waitForElementToPresence(TOTAL_GUEST_BUTTON).orElseThrow();
        String[] numbers = driverTools.getText(totalGuestText).split(", ");
        int totalGuest =  Integer.parseInt(numbers[0].split(" ")[0]) + Integer.parseInt(numbers[1].split(" ")[0]);
        logger.log("Total guest: " + totalGuest);
        return String.valueOf(totalGuest);
    }

    @Override
    public void openSearchLocation() {
        driverTools.waitForElementToAppear(HOMES_CARD, 20).orElseThrow();
        WebElement searchFieldLandingPage = driverTools.waitForElementToPresence(SEARCH_FIELD_LANDING_PAGE).orElseThrow();
        driverTools.click(searchFieldLandingPage);
        WebElement searchFieldPopup = driverTools.waitForElementToPresence(SEARCH_FIELD_POPUP).orElseThrow();
        driverTools.click(searchFieldPopup);
    }

    @Override
    public void chooseDestinationLoop(int totalLoop, String notContainDestination) {
        LandingPage<T> hotelLandingPage = LandingPage.getInstance(session);
        SearchResultPage<T> searchResultPage = SearchResultPage.getInstance(session);

        for (int i = 1; i <= totalLoop; i++) {
            logger.log("Destination loop -> " + i);
            driverTools.waitForElementToPresence(getPopularDestinationButtonWithout(notContainDestination)).orElseThrow();
            List<WebElement> popularDestination = driverTools.elements(getPopularDestinationButtonWithout(notContainDestination)).orElseThrow();
            driverTools.click(popularDestination.get(i));

            hotelLandingPage.clickSubmit();
            searchResultPage.clickBackButton();
            openSearchLocation();
        }

        driverTools.click(CLOSE_SEARCH_PAGE_TWO);
    }

    private Locator getPopularDestinationButtonWithout(String destinationName) {
        return new Locator("POPULAR_DESTINATION_BUTTON", AppiumBy.xpath("//*[@label='Popular Destination' or @label='Destinasi Populer']//parent::*//following-sibling::XCUIElementTypeCell//*[not(@label='" + destinationName + "') and @name]"));
    }

    @Override
    public String chooseRecentSearch() {
        String destination = driverTools.getText(RECENT_SEARCH_LOCATION).orElseThrow();
        driverTools.click(RECENT_SEARCH_LOCATION);
        return destination;
    }

    @Override
    public boolean findRecentSearch(String destination, int totalSwipe) {
        boolean status = false;
        for (int i = 0; i <= totalSwipe; i++) {
            String actualRecentSearch = driverTools.getText(RECENT_SEARCH_CONTAINER).orElseThrow();
            String expectedRecentSearch = ", " + destination;
            logger.log("actualRecentSearch -> " + actualRecentSearch);
            logger.log("expectedRecentSearch -> " + expectedRecentSearch);

            status = actualRecentSearch.contains(expectedRecentSearch);
            logger.log("status comparing the destination -> " + status);
            if (status) break;

            logger.log("recent search swipe count -> " + i);
            driverTools.swipeByDistance(RECENT_SEARCH_CONTAINER, Swipe.LEFT, 0.8);
        }
        return status;
    }

    @Override
    public void clickRecentSearchDeleteButton() {
        driverTools.click(RECENT_SEARCH_DELETE_BUTTON);
    }

    @Override
    public boolean isRecentSearchVisible() {
        return driverTools.waitForElementToPresence(RECENT_SEARCH_CONTAINER).isPresent();
    }

    @Override
    public String chooseDestination(String destination) {
        driverTools.waitForElementToPresence(HOMES_POPULAR_DESTINATION).orElseThrow();
        if (destination.equals("random")) {
            List<WebElement> popularDestination = driverTools.elements(HOMES_POPULAR_DESTINATION, 20).orElseThrow();
            int totalDestination = popularDestination.size();
            logger.log("totalDestination -> " + totalDestination);
            int randomIndex = generateRandomInt(1, totalDestination-1);
            WebElement selectedDestination = popularDestination.get(randomIndex);
            destination = driverTools.getValueAttribute(selectedDestination, "value");
            logger.log("destination -> " + destination);
        }
        Locator destinationLocator = getPopularDestinationButton(destination);
        WebElement destinationElement = driverTools.swipeToElement(destinationLocator,Swipe.UP,3).orElseThrow();
        driverTools.click(destinationElement);

        return destination;
    }

    private Locator getPopularDestinationButton(String destinationName) {
        return new Locator("POPULAR_DESTINATION_BUTTON_" + destinationName.toUpperCase(), AppiumBy.xpath("//*[contains(@name,'popular_destination_list')]//*[contains(@label,'" + destinationName + "') and @name]"));
    }
    
    @Override
    public String getSearchDestination() {
        WebElement destination = driverTools.waitForElementToPresence(SEARCH_FIELD_POPUP).orElseThrow();
        return driverTools.getText(destination);
    }

    @Override
    public void clickSearchFieldText() {
        WebElement searchField = driverTools.waitForElementToAppear(SEARCH_FIELD_LANDING_PAGE).orElseThrow();
        driverTools.click(searchField);
    }

    @Override
    public boolean isSearchFormExist() {
        driverTools.waitForElementToAppear(HOMES_CARD, 20).orElseThrow();
        driverTools.click(SEARCH_FIELD_LANDING_PAGE);
        boolean searchForm = driverTools.waitForElementToAppear(SEARCH_FIELD_POPUP).isPresent();
        logger.log("is search form exist: " + searchForm);
        return searchForm;
    }

    @Override
    public boolean isSearchLocationExist() {
        driverTools.click(SEARCH_FIELD_POPUP);
        boolean searchLocation = driverTools.waitForElementToAppear(SEARCH_FIELD_TEXT).isPresent();
        logger.log("is search location exist: " + searchLocation);
        // there are duplicate close button elements found as multiple popup and forms are being opened
        List<WebElement> closeSearchBtnList = driverTools.elements(CLOSE_SEARCH).orElseThrow();
        driverTools.click(closeSearchBtnList.get(1));
        return searchLocation;
    }

    @Override
    public boolean isGuestFormExist() {
        driverTools.click(TOTAL_GUEST_BUTTON);
        boolean guestForm = driverTools.waitForElementToAppear(GUEST_FORM).isPresent();
        logger.log("is guest form exist: " + guestForm);
        List<WebElement> closeGuestBtnList = driverTools.elements(CLOSE_SEARCH).orElseThrow();
        driverTools.click(closeGuestBtnList.get(1));
        return guestForm;
    }

    @Override
    public boolean isCalendarExist() {
        driverTools.click(DATE_FIELD_BUTTON);
        boolean calendar = driverTools.waitForElementToPresence(SUBMIT_BUTTON).isPresent();
        logger.log("is calendar popup exist: " + calendar);
        driverTools.click(CLOSE_SEARCH);
        // close search form
        driverTools.click(CLOSE_SEARCH);
        return calendar;
    }

    @Override
    public boolean clickViewMoreButton() {
        boolean isLandingBannerPresent = driverTools.waitForElementToAppear(LANDING_BANNER).isPresent();
        driverTools.waitForElementToPresence(HOMES_LOCATION_CHIPS,20);
        driverTools.click(VIEW_MORE_BUTTON);
        boolean isLandingBannerPresentAfterClick = driverTools.waitForElementToAppear(LANDING_BANNER).isPresent();
        logger.log("is redirect from landing page to page module : " + (!isLandingBannerPresentAfterClick && isLandingBannerPresent));
        return !isLandingBannerPresentAfterClick && isLandingBannerPresent;
    }

    @Override
    public boolean clickBackToTopButton() {
        driverTools.swipeToElement(VIEW_MORE_BUTTON, SCREEN, Swipe.UP,30).orElseThrow();
        driverTools.swipeUp(); // one more time just to be sure
        WebElement backToTopBtn = driverTools.waitForElementToPresence(VIEW_MORE_BUTTON).orElseThrow();
        driverTools.clickElementByCoordinate(backToTopBtn);
        boolean landingBanner = driverTools.waitForElementToAppear(LANDING_BANNER).isPresent();
        logger.log("is landing banner exist : " + landingBanner);
        return landingBanner;
    }

    @Override
    public String getHeroBannerTitle() {
        driverTools.waitForElementToPresence(HERO_BANNER_TITLE);
        return driverTools.getText(HERO_BANNER_TITLE).orElseThrow();
    }

    @Override
    public boolean compareModuleTitleCaption(ArrayList<String> arrayList) {
        logger.log("arrayList after converted from jsonArray-> " + arrayList);
        logger.log("data.size() ->" + arrayList.size());

        int loop = 0;
        boolean emptyList = false;
        while (!arrayList.isEmpty()) {
            driverTools.waitForElementToPresence(HERO_BANNER_CAPTION).orElseThrow();
            String caption = driverTools.getText(HERO_BANNER_CAPTION).orElseThrow();
            logger.log("caption-> " + caption);

            if (!caption.isEmpty()) arrayList.removeIf(title -> title.equals(caption));
            logger.log("arrayList after removing value-> " + arrayList);
            if (arrayList.isEmpty()) emptyList = true;

            loop++;
            logger.log("loop-> " + loop);
            if (loop == 60) break;
        }

        if (emptyList) {
            logger.softWarning("PASSED: All the module title name on the API are equal with caption text on the App");
            return true;
        } else {
            logger.fail("FAILED: This caption text is not visible on the App ->" + arrayList +
                    ",\n or compare method reach the max loop limit: " + loop);
            return false;
        }
    }

    @Override
    public void swipeToThreePointsBanner() {
        driverTools.swipeToElement(THREE_POINTS_BANNER_BUTTON, Swipe.UP, 30);
    }

    @Override
    public String getThreePointsBannerAttribute(String attributeName) {
        switch(attributeName) {
            case "content.title" -> {return driverTools.getValueAttribute(THREE_POINTS_BANNER_TITLE, "label");}
            case "content.subTitle" -> {return driverTools.getValueAttribute(THREE_POINTS_BANNER_SUB_TITLE, "label");}
            case "content.showButton" -> {return Boolean.toString(driverTools.isElementDisplayed(THREE_POINTS_BANNER_BUTTON));}
            case "content.buttonText" -> {return driverTools.getValueAttribute(THREE_POINTS_BANNER_BUTTON, "label");}
            default -> {throw new NoSuchMethodError("There is no '"+attributeName+"' option");}
        }
    }

    @Override
    public ArrayList<HomesData.ListOfTitleAndSubTitle> getThreePointsBannerContentList() {
        driverTools.swipeToElement(THREE_POINTS_BANNER_CONTENT_LIST, Swipe.UP, 3);
        driverTools.swipeUp();

        ArrayList<HomesData.ListOfTitleAndSubTitle> result = new ArrayList<>();
        List<WebElement> threePointBannerElement = driverTools.elements(THREE_POINTS_BANNER_CONTENT_LIST).orElseThrow();
        int count = threePointBannerElement.size();
        logger.log("count-> "+ count);
        for(int i = 0; i < count; i++) {
            HomesData.ListOfTitleAndSubTitle newList = new HomesData.ListOfTitleAndSubTitle();
            newList.title = driverTools.getValueAttribute(getThreePointsBannerContentTitleLocatorByIndex(i+1), "label");
            newList.subTitle = driverTools.getValueAttribute(getThreePointsBannerContentSubTitleLocatorByIndex(i+1), "label");
            logger.log("title app -> "+ newList.title);
            logger.log("sub-title app -> "+ newList.subTitle);
            result.add(newList);
        }

        return result;
    }

    private Locator getThreePointsBannerContentTitleLocatorByIndex(int i) {
        return new Locator("THREE_POINTS_BANNER_CONTENT_LIST_TITLE_"+i, AppiumBy.xpath("(//XCUIElementTypeCell[contains(@name,'TNMLandingJoinHomesItemView')])["+i+"]/child::XCUIElementTypeStaticText[2]"));
    }

    private Locator getThreePointsBannerContentSubTitleLocatorByIndex(int i) {
        return new Locator("THREE_POINTS_BANNER_CONTENT_LIST_SUB_TITLE_"+i, AppiumBy.xpath("(//XCUIElementTypeCell[contains(@name,'TNMLandingJoinHomesItemView')])["+i+"]/child::XCUIElementTypeStaticText[1]"));
    }

    @Override
    public void clickThreePointBannersButton() {
        driverTools.clickElementByCoordinate(THREE_POINTS_BANNER_BUTTON);
    }

    public String getLandscapeBannerTitle() {
        WebElement landscapeBannerTitle = driverTools.swipeToElement(LANDSCAPE_BANNER_TITLE, Swipe.UP,20).orElseThrow();
        return landscapeBannerTitle.getText();
    }

    @Override
    public void goToTimedPromoSection() {
        driverTools.swipeToElement(TIMED_PROMO_SEE_ALL_BUTTON, Swipe.UP, 10).orElseThrow();
    }

    @Override
    public String getTimedPromoTitle() {
        WebElement title = driverTools.waitForElementToPresence(TIMED_PROMO_TITLE).orElseThrow();
        return driverTools.getValueAttribute(title, "label");
    }

    @Override
    public String selectTimedPromoLocation(int index) {
        WebElement selectedLocation = driverTools.swipeToElement(getTimedPromoLocationButton(index), Swipe.LEFT, 5).orElseThrow();
        String locationTitle = driverTools.getValueAttribute(selectedLocation, "label");
        logger.log(index + " : locationTitle-> " + locationTitle);
        driverTools.click(selectedLocation);
        return locationTitle;
    }

    @Override
    public boolean compareTimedPromoHomesName(HomesLandingPageModuleData homesLandingPageModuleData) {
        ArrayList<String> homesName = homesLandingPageModuleData.homesList;

        boolean result = false;
        int totalHomesParam = homesName.size();
        logger.log("totalHomesParam-> " + totalHomesParam);

        if (totalHomesParam == 0) {
            logger.softWarning("There is no homes list on the API");
            result = driverTools.element(TIMED_PROMO_EMPTY_HOMES).isPresent();

        } else {
            if (totalHomesParam > 3) totalHomesParam = 3;
            for (int i = 0; i < totalHomesParam; i++) {
                String homesNameApi = homesName.get(i);
                logger.log(i + " index of homesNameApi-> " + homesNameApi);

                boolean isHomesNamePresent = driverTools.waitForElementToPresence(getTimedPromoHomesName(homesNameApi)).isPresent();
                logger.log("isHomesNamePresent on the App -> " + isHomesNamePresent);
                if (!isHomesNamePresent) {
                    logger.fail(i + " index = Homes name from the API is \"" + homesNameApi + "\", but not found on the App");
                    result = false;
                    break;
                } else {
                    result = true;
                }
            }
        }

        return result;
    }

    @Override
    public String clickSeeAllButton() {
        WebElement button = driverTools.waitForElementToPresence(TIMED_PROMO_SEE_ALL_BUTTON).orElseThrow();
        String buttonText = driverTools.getValueAttribute(button, "label");
        driverTools.click(button);
        return buttonText;
    }

    @Override
    public String getLandscapeBannerSubtitle() {
        WebElement subtitle = driverTools.swipeToElement(LANDSCAPE_BANNER_SUB_TITLE, Swipe.UP, 3).orElseThrow();
        return driverTools.getText(subtitle);
    }

    /*
    assertion method, compare total landscape banner from API and APP,argument of dataLength should be total of landscape banner from API
    Notes: due to issue that is delivered on https://borobudur.atlassian.net/browse/QAAUT-2613?focusedCommentId=505062, dataLength will be change into 2
    */
    @Override
    public boolean compareTotalLandscapeBanner(int dataLength) {
        if (dataLength > 2) {
            dataLength = 2;
        }
        boolean isCorrect = true;

        driverTools.waitForElementToAppear(LANDSCAPE_BANNER_TITLE);
        for(int index = 1; index<= dataLength; index++){

            boolean currentBannerIsExist = driverTools.waitForElementToAppear(landscapeBannerLocator(index)).isPresent();
            if(!currentBannerIsExist){
                logger.fail("banner does not exist on the app");
                isCorrect = false;
                break;
            }
        }

        return isCorrect;
    }

    /*method only belong to getLandscapeBannerTitle*/
    private Locator landscapeBannerLocator(int index) {
        return new Locator("LANDSCAPE_BANNER_" + index, AppiumBy.iOSClassChain("**/XCUIElementTypeImage[`name == 'bn_carrousel'`]["+index+"]"));
    }

    private Locator getMultiHotelIdLocator(String title){
        return new Locator("LOCATOR_" + title,AppiumBy.xpath("//XCUIElementTypeStaticText[@label='"+title+"']"));
    }

    @Override
    public void swipeToTitleMultipleHotelId(String title){
        driverTools.swipeToElement(getMultiHotelIdLocator(title),Swipe.UP,10);
    }

    @Override
    public boolean isTitleAndSubTitleDisplayedMultipleHotelId(String title,String subTitle){
        WebElement actualTitleElement = driverTools.element(getMultiHotelIdLocator(title)).orElseThrow();
        String actualTitle = driverTools.getText(actualTitleElement);
        logger.log("actual title got from the app: " + actualTitle);

        WebElement actualSubTitleElement = driverTools.element(SUB_TITLE_MULTIPLE_HOTEL_ID).orElseThrow();
        String actualSubTitle = driverTools.getText(actualSubTitleElement);
        logger.log("actual subtitle got from the app: " + actualSubTitle);

        boolean isBothCorrect = actualTitle.equalsIgnoreCase(title) && actualSubTitle.equalsIgnoreCase(subTitle);
        logger.log("is both title and subtitle correct : " + isBothCorrect);
        return isBothCorrect;
    }

    @Override
    public boolean verifyLocationsMultipleHotelId(ArrayList<HomesData.ContentData> contentLists){
        List<WebElement> locationsElement = driverTools.elements(LOCATIONS_MULTIPLE_HOTEL_ID).orElseThrow();
        logger.log("contentLists -> " + contentLists);
        if(locationsElement.size()!=contentLists.size())
            return false;

        ArrayList<String> actualLocations = new ArrayList<>();
        for (WebElement location : locationsElement){
            String locationText = driverTools.getText(location);
            logger.log("Location -> " + locationText);
            actualLocations.add(locationText);
        }

        ArrayList<String> expectedLocation = new ArrayList<>();
        for (HomesData.ContentData contentList : contentLists){
            expectedLocation.add(contentList.location);
        }

        boolean isLocationCorrect = true;
        for (int i =0; i<actualLocations.size();i++ ){
            isLocationCorrect = isLocationCorrect & actualLocations.get(i).equalsIgnoreCase(expectedLocation.get(i));
        }
        logger.log("is Locations in multiple hotel id correct : " + isLocationCorrect);
        return isLocationCorrect;
    }

    private Locator getLocationChipBtnMultipleHotelId(String location){
        return new Locator("Location Chip For " + location, AppiumBy.xpath("//XCUIElementTypeCell[@name='TVCell<TDSChipsViewV2>+MultipleHotelId-Chips']//XCUIElementTypeStaticText[contains(@label,'"+location+"')]"));
    }

    @Override
    public void clickLocationChipBtnMultipleHotelId(String location){
        WebElement locationChip = driverTools.swipeToElement(getLocationChipBtnMultipleHotelId(location), Swipe.UP, 3).orElseThrow();
        driverTools.click(locationChip);
    }

    private Locator getBtnLocatorMultipleHotelId(String text){
        return new Locator("Button " + text, AppiumBy.xpath("//XCUIElementTypeCell[@name='TVCell<TDSChipsViewV2>+MultipleHotelId-Chips']//following-sibling::*//XCUIElementTypeButton[contains(@label,'"+text+"')]"));
    }

    @Override
    public boolean verifyButtonTextMultipleHotelId(String buttonText){
        WebElement btnElement = driverTools.swipeToElement(getBtnLocatorMultipleHotelId(buttonText),Swipe.UP,6).orElseThrow();
        boolean isButtonTextMatch = driverTools.isElementDisplayed(btnElement);
        logger.log("is button text from api match with app: " + isButtonTextMatch);
        driverTools.click(btnElement);
        return isButtonTextMatch;
    }

    @Override
    public String getMiniCardTitle() {
        driverTools.swipeToElement(MINI_CARD_TITLE, Swipe.UP, 10);
        return driverTools.getText(MINI_CARD_TITLE).orElseThrow();
    }

    private Locator getMiniCardTitleLocator(String title) {
        return new Locator("MINI_CARD_TITLE"+title+"", AppiumBy.xpath("//XCUIElementTypeStaticText[@name='"+title+"']"));
    }

    @Override
    public boolean compareMiniCardTitleList(HomesData data) {
        ArrayList<String> miniCardTitleAPI = data.contentListTitle;
        driverTools.swipeToElement(TITLE_ON_MINI_CARD, Swipe.UP, 3).orElseThrow();
        List<WebElement> miniCardElement = driverTools.elements(TITLE_ON_MINI_CARD).orElseThrow();

        for(int i = 0; i < miniCardElement.size() && i < miniCardTitleAPI.size(); i++) {
            String miniCardTitleApi = miniCardTitleAPI.get(i);
            String miniCardTitleApp = driverTools.getText(getMiniCardTitleLocator(miniCardTitleApi)).orElseThrow();

            if(!miniCardTitleApi.equals(miniCardTitleApp)) {
                logger.log("mini card text APP->" + miniCardTitleApp);
                logger.log("mini card text API->" + miniCardTitleApi);
                return false;
            }
        }
        return true;
    }

    @Override
    public void clickMiniCard(int index, String title) {
        driverTools.click(getMiniCardTitleLocator(title));
    }

    @Override
    public String getFabText() {
        throw new org.apache.commons.lang3.NotImplementedException("Not implemented");
    }

    @Override
    public boolean verifySeeMoreAndBackToTopFunctionality(int times) {
        throw new org.apache.commons.lang3.NotImplementedException("Not implemented");
    }

    @Override
    public void swipeToSatisfyWanderlust() {
        throw new org.apache.commons.lang3.NotImplementedException("Not implemented");
    }

    @Override
    public void clickWanderlustCategory(String category) {
        throw new org.apache.commons.lang3.NotImplementedException("Not implemented");
    }

    private Locator getPriceLocator(String hotelName, String price) {
        return new Locator("PRICE_WITHOUT_TAX_ABT", AppiumBy.xpath("//XCUIElementTypeStaticText[@name='product_name' and @label='" + hotelName + "']//following-sibling::XCUIElementTypeStaticText[contains(@name, 'IDR " + price + "')]"));
    }
}
