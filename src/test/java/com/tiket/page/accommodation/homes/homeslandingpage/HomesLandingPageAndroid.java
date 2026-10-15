package com.tiket.page.accommodation.homes.homeslandingpage;

import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.page.accommodation.hotel.landingpage.LandingPageAndroid;
import com.tiket.pojo.accommodation.homes.HomesData;
import com.tiket.pojo.accommodation.homes.HomesLandingPageModuleData;
import com.tiket.testbase.Assertion;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.tiket.core.CoreFunctions.generateRandomInt;
import static com.tiket.core.CoreFunctions.splitLanguageData;
import static com.tiket.page.accommodation.hotel.landingpage.LandingPageAndroid.*;
import static org.hamcrest.Matchers.is;

public class HomesLandingPageAndroid<T extends WebDriver> extends HomesLandingPage<T> {

    public HomesLandingPageAndroid(Session<T> session) {
        super(session);
    }

    public static final Locator SEARCH_FIELD_TEXT_HOMES = new Locator("SEARCH_FIELD_TEXT_HOMES", AppiumBy.id("tds_text_search_box"));
    private static final Locator SEARCH_FIELD_BUTTON = new Locator("SEARCH_FIELD_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'form_destination')]//*[contains(@resource-id,'tds_search_form_title_group') and not(contains(@resource-id,'image'))]"));
    private static final Locator SEARCH_FIELD_POPUP_TEXT = new Locator("SEARCH_FIELD_POPUP_TEXT", AppiumBy.xpath("//*[contains(@resource-id,'form_destination')]//*[contains(@resource-id,'tds_search_form_title_group')]//*[contains(@resource-id,'tds_search_form_title') and not(contains(@resource-id,'image'))]"));
    private static final Locator POPULAR_DESTINATION_BUTTON = new Locator("POPULAR_DESTINATION_BUTTON", AppiumBy.id("tds_chips_text"));
    private static final Locator TOTAL_GUEST_TEXT = new Locator("TOTAL_GUEST_TEXT", AppiumBy.xpath("(//*[contains(@resource-id,'form_guest_room')]//*[contains(@resource-id,'tds_search_form_title')])[last()]"));
    public static final Locator GUEST_FORM = new Locator("GUEST_FORM", AppiumBy.id("motion_layout"));
    public static final Locator CLOSE_CALENDAR_BUTTON = new Locator("CLOSE_CALENDAR_BUTTON", AppiumBy.id("tds_calendar_close"));
    public static final Locator VIEW_MORE_BUTTON = new Locator("VIEW_MORE_BUTTON", AppiumBy.id("fab_back_to_top"));
    public static final Locator HOMES_CARD = new Locator("HOMES_CARD", AppiumBy.id("card_view"));
    public static final Locator BACK_TO_TOP_BUTTON = new Locator("BACK_TO_TOP_BUTTON", AppiumBy.id("fab_back_to_top"));
    public static final Locator HERO_BANNER_TITLE = new Locator("HERO_BANNER_TITLE", AppiumBy.id("text_title"));
    public static final Locator LANDSCAPE_BANNER_TITLE = new Locator("LANDSCAPE_BANNER_TITLE", AppiumBy.xpath("//*[contains(@resource-id, 'rv_landscape_banner')]/preceding-sibling::*/*[contains(@resource-id, 'tv_title')]"));
    public static final Locator LANDSCAPE_BANNER_SUB_TITLE = new Locator("LANDSCAPE_BANNER_SUB_TITLE", AppiumBy.xpath("//*[contains(@resource-id, 'rv_landscape_banner')]/preceding-sibling::*/*[contains(@resource-id, 'tv_sub_title')]"));
    public static final Locator HERO_BANNER_CAPTION = new Locator("HERO_BANNER_CAPTION", AppiumBy.id("text_caption"));
    private static final Locator TIMED_PROMO_SEE_ALL_BUTTON = new Locator("TIMED_PROMO_SEE_ALL_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'tv_promo_count_down')]//following-sibling::*[contains(@resource-id,'v_container')]//*[contains(@resource-id,'btn_see_all')]//*[contains(@resource-id,'tds_btn')]"));
    private static final Locator TIMED_PROMO_TITLE = new Locator("TIMED_PROMO_TITLE", AppiumBy.xpath("//*[contains(@resource-id,'tv_promo_count_down')]//preceding-sibling::*[contains(@resource-id,'view_title')]//*[contains(@resource-id,'tv_title')]"));
    private static final Locator TIMED_PROMO_EMPTY_HOMES = new Locator("TIMED_PROMO_EMPTY_HOMES", AppiumBy.xpath("//*[contains(@resource-id,'chip_group_promo')]//following-sibling::*[contains(@resource-id,'vg_content')]//*[contains(@resource-id,'ll_error_container')]"));
    private static final Locator TIMED_PROMO_LOCATION_CONTAINER = new Locator("TIMED_PROMO_LOCATION_CONTAINER", AppiumBy.xpath("//*[contains(@resource-id,'tv_promo_count_down')]//following-sibling::*[contains(@resource-id,'v_container')]//*[contains(@resource-id,'chip_group_promo')]"));
    public static final Locator THREE_POINTS_BANNER_TITLE = new Locator("THREE_POINTS_BANNER_TITLE", AppiumBy.xpath("//*[contains(@resource-id,'iv_background')]/following-sibling::*[contains(@resource-id,'tv_title')]"));
    public static final Locator THREE_POINTS_BANNER_SUB_TITLE = new Locator("THREE_POINTS_BANNER_SUB_TITLE", AppiumBy.xpath("//*[contains(@resource-id,'iv_background')]/following-sibling::*[contains(@resource-id,'tv_sub_title')]"));
    public static final Locator THREE_POINTS_BANNER_BUTTON = new Locator("THREE_POINTS_BANNER_BUTTON_TEXT", AppiumBy.xpath("//*[contains(@resource-id,'iv_background')]/following-sibling::*[contains(@resource-id,'btn_join')]"));
    public static final Locator THREE_POINTS_BANNER_BUTTON_TEXT = new Locator("THREE_POINTS_BANNER_BUTTON_TEXT", AppiumBy.xpath("//*[contains(@resource-id,'iv_background')]/following-sibling::*[contains(@resource-id,'btn_join')]/*[contains(@resource-id,'tds_btn')]"));
    public static final Locator THREE_POINTS_BANNER_CONTENT_LIST = new Locator("THREE_POINTS_BANNER_CONTENT_LIST", AppiumBy.xpath("//*[contains(@resource-id,'rv_point')]/child::*"));
    public static final Locator TITLE_MULTIPLE_HOTEL_ID = new Locator("TITLE_MULTIPLE_HOTEL_ID", AppiumBy.id("tv_title"));
    public static final Locator SUB_TITLE_MULTIPLE_HOTEL_ID = new Locator("SUB_TITLE_MULTIPLE_HOTEL_ID", AppiumBy.id("tv_sub_title"));
    public static final Locator LOCATIONS_MULTIPLE_HOTEL_ID = new Locator("LOCATIONS_MULTIPLE_HOTEL_ID", AppiumBy.xpath("//*[contains(@resource-id,'chip_group_promo')]//*[contains(@resource-id,'tds_chips_text')]"));
    private static final Locator MINI_CARD_TITLE = new Locator("MINI_CARD_TITLE", AppiumBy.xpath("//*[contains(@resource-id,'rv_mini_card')]//preceding-sibling::*[contains(@resource-id,'tv_title')]//*[contains(@resource-id,'tv_title')]"));
    public static final Locator TITLE_ON_MINI_CARD = new Locator("TITLE_ON_MINI_CARD", AppiumBy.id("tv_item_mini_card"));
    private static final Locator HOMES_FLOWER_CONTAINER = new Locator("HOMES_FLOWER_CONTAINER", AppiumBy.id("homes_star_container"));
    private static final Locator HOMES_BANNER_ITEM = new Locator("HOMES_BANNER_ITEM", AppiumBy.id("iv_banner"));
    private static final Locator FAB_TEXT = new Locator("FAB_TEXT", AppiumBy.id("tds_extended_fab_text"));
    private static final Locator SEE_MORE_FAB = new Locator("SEE_MORE_FAB", AppiumBy.xpath("//*[contains(@resource-id,'tds_extended_fab_text') and (@text='See More' or @text='Selengkapnya')]"));
    private static final Locator BACK_TO_TOP_FAB = new Locator("BACK_TO_TOP_FAB", AppiumBy.xpath("//*[contains(@resource-id,'tds_extended_fab_text') and (@text='Back to Top' or @text='Kembali ke Atas')]"));
    private static final Locator SATISFY_WANDERLUST_TITLE = new Locator("SATISFY_WANDERLUST_TITLE", AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and (@text='Satisfy your wanderlust' or @text='Asiknya rebahan di tempat idaman')]"));

    private Locator getPopularDestinationButton(String destinationName) {
        return new Locator("POPULAR_DESTINATION_BUTTON_" + destinationName.toUpperCase(), AppiumBy.xpath("//*[contains(@resource-id,'tds_chips_text') and @text='" + destinationName + "']"));
    }

    private Locator getTimedPromoLocationButton(int index) {
        return new Locator("TIMED_PROMO_LOCATION_BUTTON", AppiumBy.xpath("(//*[contains(@resource-id,'chip_group_promo')]//*[contains(@resource-id,'tds_chips_text')])[" + index + "]"));
    }

    @Override
    public void searchStayLocation(String location) {
        String separator = "/";
        String[] splitData = splitLanguageData(location, separator);
        String locationId = splitData[0];
        String locationEn = splitData[1];

        driverTools.click(SEARCH_FIELD_TEXT_HOMES);
        driverTools.click(SEARCH_FIELD_BUTTON);
        driverTools.sendKeys(SEARCH_FIELD_TEXT, locationId);
        Locator searchList = searchList(locationId, locationEn);
        driverTools.click(driverTools.waitForElementToAppear(searchList,12).orElseThrow());
    }

    @Override
    public void clickSubmit() {
        driverTools.click(SUBMIT_BUTTON);
    }

    @Override
    public void openTotalGuest() {
        WebElement totalGuestButton = driverTools.waitForElementToAppear(ROOM_AND_GUEST_BTN).orElseThrow();
        driverTools.click(totalGuestButton);
    }

    @Override
    public String getTotalGuest() {
        WebElement totalGuestText = driverTools.waitForElementToAppear(TOTAL_GUEST_TEXT).orElseThrow();
        String[] numbers = driverTools.getText(totalGuestText).split(", ");
        int totalGuest =  Integer.parseInt(numbers[0].split(" ")[0]) + Integer.parseInt(numbers[1].split(" ")[0]);
        logger.log("Total guest: " + totalGuest);
        return String.valueOf(totalGuest);
    }

    @Override
    public void openSearchLocation() {
        driverTools.click(SEARCH_FIELD_TEXT_HOMES);
        driverTools.click(SEARCH_FIELD_BUTTON);
    }

    @Override
    public void chooseDestinationLoop(int totalLoop, String notContainDestination) {
        //FIXME: Pending implementation
    }

    @Override
    public String chooseRecentSearch() {
        return ""; //FIXME: Pending implementation
    }

    @Override
    public boolean findRecentSearch(String destination, int totalSwipe) {
        return true; //FIXME: Pending implementation
    }

    @Override
    public void clickRecentSearchDeleteButton() {
        //FIXME: Pending implementation
    }

    @Override
    public boolean isRecentSearchVisible() {
        return true; //FIXME: Pending implementation
    }

    @Override
    public String chooseDestination(String destination) {
        if (destination.equals("random")) {
            driverTools.swipeToElement(POPULAR_DESTINATION_BUTTON, Swipe.UP, 2);
            List<WebElement> popularDestination = driverTools.elements(POPULAR_DESTINATION_BUTTON).orElseThrow();
            int totalDestination = popularDestination.size();
            int randomIndex = generateRandomInt(0, totalDestination-1);
            WebElement selectedDestination = popularDestination.get(randomIndex);
            destination = driverTools.getText(selectedDestination);
            driverTools.click(popularDestination.get(randomIndex));
        } else {
            driverTools.swipeToElement(getPopularDestinationButton(destination), Swipe.UP, 2).ifPresent(WebElement::click);
        }
        return destination;
    }

    @Override
    public String getSearchDestination() {
        WebElement destination = driverTools.waitForElementToPresence(SEARCH_FIELD_POPUP_TEXT).orElseThrow();
        return driverTools.getText(destination);
    }

    @Override
    public void clickSearchFieldText() {
        WebElement searchField = driverTools.waitForElementToAppear(SEARCH_FIELD_TEXT_HOMES).orElseThrow();
        driverTools.click(searchField);
    }

    @Override
    public boolean isSearchFormExist() {
        driverTools.click(SEARCH_FIELD_TEXT_HOMES);
        boolean searchForm = driverTools.waitForElementToAppear(SEARCH_FIELD_BUTTON).isPresent();
        logger.log("is search form exist: " + searchForm);
        return searchForm;
    }

    @Override
    public boolean isSearchLocationExist() {
        driverTools.click(SEARCH_FIELD_BUTTON);
        boolean searchLocation = driverTools.waitForElementToAppear(SEARCH_FIELD_TEXT).isPresent();
        logger.log("is search location exist: " + searchLocation);
        driverTools.click(CLOSE_SEARCH);
        return searchLocation;
    }

    @Override
    public boolean isGuestFormExist() {
        openTotalGuest();
        boolean guestForm = driverTools.waitForElementToAppear(GUEST_FORM).isPresent();
        logger.log("is guest form exist: " + guestForm);
        driverTools.click(CLOSE_SEARCH);
        return guestForm;
    }

    @Override
    public boolean isCalendarExist() {
        driverTools.click(DATE_FIELD_BUTTON);
        boolean calendar = driverTools.waitForElementToAppear(CALENDAR).isPresent();
        logger.log("is calendar exist: " + calendar);
        driverTools.click(CLOSE_CALENDAR_BUTTON);
        driverTools.click(CLOSE_SEARCH);
        return calendar;
    }

    @Override
    public boolean clickViewMoreButton() {
        WebElement viewMoreButton = driverTools.waitForElementToAppear(VIEW_MORE_BUTTON).orElseThrow();
        driverTools.click(viewMoreButton);
        boolean homesCard = driverTools.waitForElementToAppear(HOMES_CARD).isPresent();
        boolean homesFlowerContainer = driverTools.waitForElementsToAppear(HOMES_FLOWER_CONTAINER).isPresent();
        boolean homesBannerItem = driverTools.waitForElementsToAppear(HOMES_BANNER_ITEM).isPresent();
        logger.log("is homes card exist : " + homesCard);
        logger.log("is homes flower exist : " + homesFlowerContainer);
        logger.log("is homes banner exist : " + homesBannerItem);
        Assertion.assertThat("Homes card or Homes flower should be displayed", homesCard || homesFlowerContainer || homesBannerItem, is(true));
        return true;
    }

    @Override
    public boolean clickBackToTopButton() {
        driverTools.swipeUp();
        driverTools.swipeUp();
        driverTools.swipeDown();
        WebElement backToTopBtn = driverTools.waitForElementToAppear(BACK_TO_TOP_BUTTON).orElseThrow();
        driverTools.click(backToTopBtn);
        boolean searchField = driverTools.waitForElementToAppear(SEARCH_FIELD_TEXT).isPresent();
        logger.log("is search field exist : " + searchField);
        return searchField;
    }

    @Override
    public String getHeroBannerTitle() {
        WebElement heroBannerTitle = driverTools.waitForElementToAppear(HERO_BANNER_TITLE).orElseThrow();
        return driverTools.getText(heroBannerTitle);
    }

    @Override
    public String getLandscapeBannerTitle() {
        driverTools.swipeUp(5);
        WebElement landscapeBannerTitle = driverTools.swipeToElement(LANDSCAPE_BANNER_TITLE, Swipe.UP, 2).orElseThrow(); //only for back up, swipeUp(5) does the job
        return landscapeBannerTitle.getText();
    }

    @Override
    public String getLandscapeBannerSubtitle() {
        //swiping to landscape banner is already handled by getLandscapeBannerTitle()
        WebElement subtitle = driverTools.waitForElementToAppear(LANDSCAPE_BANNER_SUB_TITLE).orElseThrow();
        return driverTools.getText(subtitle);
    }

    @Override
    public boolean compareModuleTitleCaption(ArrayList<String> arrayList) {
        WebElement heroBannerCaption = driverTools.waitForElementToPresence(HERO_BANNER_CAPTION).orElseThrow();

        logger.log("arrayList after converted from jsonArray-> " + arrayList);
        logger.log("data.size() ->" + arrayList.size());

        int loop = 0;
        boolean emptyList = false;
        while (!arrayList.isEmpty()) {
            String caption = driverTools.getText(heroBannerCaption);
            logger.log("caption-> " + caption);

            if (!caption.equals("")) arrayList.removeIf(title -> title.equals(caption));
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

    private Locator getLandscapeBannerLocator (int index){
        return new Locator("LANDSCAPE BANNER NO " + index, AppiumBy.xpath("//*[@content-desc='landscape_banner_"+index+"']"));
    }

    @Override
    public boolean compareTotalLandscapeBanner(int totalLandscapeBanner) {
        driverTools.waitForElementToAppear(LANDSCAPE_BANNER_TITLE);
        boolean isCorrect = false;

        //checking each landscape banner
        for(int index = 0; index < totalLandscapeBanner; index++){
            //looking for landscape banner
            logger.log("loop index : " + index);
            WebElement landscapeBannerLocator = driverTools.waitForElementToAppear(getLandscapeBannerLocator(index)).orElseThrow();
            isCorrect = driverTools.isElementDisplayed(landscapeBannerLocator);
            if(!isCorrect){
                logger.log("total landscape banner app isn't equal with landscape banner on api");
                return false;
            }
            driverTools.swipeLeft(landscapeBannerLocator);
            driverTools.swipeLeft(landscapeBannerLocator);
        }

        return isCorrect;
    }

    @Override
    public String getThreePointsBannerAttribute(String attributeName) {
        switch(attributeName) {
            case "content.title" -> {return driverTools.getText(THREE_POINTS_BANNER_TITLE).orElseThrow();}
            case "content.subTitle" -> {return driverTools.getText(THREE_POINTS_BANNER_SUB_TITLE).orElseThrow();}
            case "content.showButton" -> {return Boolean.toString(driverTools.isElementDisplayed(THREE_POINTS_BANNER_BUTTON));}
            case "content.buttonText" -> {return driverTools.getText(THREE_POINTS_BANNER_BUTTON_TEXT).orElseThrow();}
            default -> {throw new NoSuchMethodError("There is no '"+attributeName+"' option");}
        }
    }

    @Override
    public ArrayList<HomesData.ListOfTitleAndSubTitle> getThreePointsBannerContentList() {
        driverTools.swipeToElement(THREE_POINTS_BANNER_CONTENT_LIST, Swipe.UP, 2);
        driverTools.swipeUp();

        ArrayList<HomesData.ListOfTitleAndSubTitle> result = new ArrayList<>();
        List<WebElement> threePointBannerElement = driverTools.elements(THREE_POINTS_BANNER_CONTENT_LIST).orElseThrow();
        int count = threePointBannerElement.size();

        for(int i = 0; i < count; i++) {
            HomesData.ListOfTitleAndSubTitle newList = new HomesData.ListOfTitleAndSubTitle();
            newList.title = driverTools.getText(getThreePointsBannerContentTitleLocatorByIndex(i+1)).orElseThrow();
            newList.subTitle = driverTools.getText(getThreePointsBannerContentSubTitleLocatorByIndex(i+1)).orElseThrow();
            logger.log("title app -> "+ newList.title);
            logger.log("sub-title app -> "+ newList.subTitle);
            result.add(newList);
        }

        return result;
    }

    @Override
    public void clickThreePointBannersButton() {
        driverTools.click(THREE_POINTS_BANNER_BUTTON_TEXT);
    }

    @Override
    public void swipeToThreePointsBanner() {
        driverTools.swipeToElement(THREE_POINTS_BANNER_BUTTON_TEXT, Swipe.UP, 10);
    }

    private Locator getThreePointsBannerContentTitleLocatorByIndex(int i) {
        return new Locator("THREE_POINTS_BANNER_CONTENT_LIST_TITLE_"+i, AppiumBy.xpath("//*[contains(@resource-id,'rv_point')]/child::*["+i+"]/*[contains(@resource-id,'tv_title')]"));
    }

    private Locator getThreePointsBannerContentSubTitleLocatorByIndex(int i) {
        return new Locator("THREE_POINTS_BANNER_CONTENT_LIST_SUB_TITLE_"+i, AppiumBy.xpath("//*[contains(@resource-id,'rv_point')]/child::*["+i+"]/*[contains(@resource-id,'tv_sub_title')]"));
    }
    
    public void goToTimedPromoSection() {
        clickViewMoreButton();
        driverTools.swipeToElement(TIMED_PROMO_SEE_ALL_BUTTON, Swipe.UP, 5).orElseThrow();
    }

    @Override
    public String getTimedPromoTitle() {
        WebElement title = driverTools.waitForElementToAppear(TIMED_PROMO_TITLE).orElseThrow();
        return driverTools.getText(title);
    }

    @Override
    public String selectTimedPromoLocation(int index) {
        WebElement selectedLocation = driverTools.swipeToElement(getTimedPromoLocationButton(index), TIMED_PROMO_LOCATION_CONTAINER, Swipe.LEFT, 5).orElseThrow();
        String locationTitle = driverTools.getText(selectedLocation);
        logger.log(index + " : locationTitle-> " + locationTitle);
        driverTools.click(selectedLocation);
        return locationTitle;
    }

    @Override
    public boolean compareTimedPromoHomesName(HomesLandingPageModuleData homesLandingPageModuleData) {
        ArrayList<String> homesName = homesLandingPageModuleData.homesList;
        boolean result;
        int totalHomesParam = homesName.size();
        logger.log("totalHomesParam-> " + totalHomesParam);

        if (totalHomesParam == 0) {
            logger.softWarning("There is no homes list on the API");
            result = driverTools.element(TIMED_PROMO_EMPTY_HOMES).isPresent();
        } else {
            LandingPageAndroid landingPageAndroid = new LandingPageAndroid(session);
            result = landingPageAndroid.compareTimedPromoHotelName(homesName);
        }
        return result;
    }

    @Override
    public String clickSeeAllButton() {
        WebElement button = driverTools.waitForElementToAppear(TIMED_PROMO_SEE_ALL_BUTTON).orElseThrow();
        String buttonText = driverTools.getText(button);
        driverTools.click(button);
        return buttonText;
    }

    private Locator getMultiHotelIdLocator(String title){
        return new Locator("SWIPE_TO_" + title,AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and contains(@text,'"+title+"')]"));
    }

    @Override
    public void swipeToTitleMultipleHotelId(String title){
        driverTools.swipeToElement(getMultiHotelIdLocator(title),Swipe.UP,10);
    }

    @Override
    public boolean isTitleAndSubTitleDisplayedMultipleHotelId(String title,String subTitle){
        WebElement actualTitleElement = driverTools.element(TITLE_MULTIPLE_HOTEL_ID).orElseThrow();
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
        return new Locator("Location Chip For " + location, AppiumBy.xpath("//*[contains(@resource-id,'chip_group_promo')]//*[contains(@resource-id,'tds_chips_text') and contains(@text,'"+location+"')]"));
    }

    @Override
    public void clickLocationChipBtnMultipleHotelId(String location){
        driverTools.click(getLocationChipBtnMultipleHotelId(location));
        driverTools.swipeUp();
    }

    private Locator getBtnLocatorMultipleHotelId(String text){
        return new Locator("Button " + text, AppiumBy.xpath("//*[contains(@resource-id,'v_container')]//*[contains(@resource-id,'tds_btn') and contains(@text,'" + text + "')]"));
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
        driverTools.swipeToElement(MINI_CARD_TITLE, Swipe.UP, 4);
        return driverTools.getText(MINI_CARD_TITLE).orElseThrow();
    }

    private Locator getMiniCardTitleLocator(int index) {
        return new Locator("MINI_CARD_TITLE"+index+"", AppiumBy.xpath("(//*[contains(@resource-id,'tv_item_mini_card')])["+index+"]"));
    }

    @Override
    public boolean compareMiniCardTitleList(HomesData data) {
        ArrayList<String> miniCardTitleAPI = data.contentListTitle;
        List<WebElement> miniCardElement = driverTools.elements(TITLE_ON_MINI_CARD).orElseThrow();

        for(int i = 0; i < miniCardElement.size() && i < miniCardTitleAPI.size(); i++) {
            String miniCardTitleApi = miniCardTitleAPI.get(i);
            String miniCardTitleApp = driverTools.getText(getMiniCardTitleLocator(i+1)).orElseThrow();

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
        driverTools.click(getMiniCardTitleLocator(index));
    }

    @Override
    public String getFabText() {
        WebElement fabText = driverTools.waitForElementToAppear(FAB_TEXT).orElseThrow();
        return driverTools.getText(fabText);
    }

    @Override
    public boolean verifySeeMoreAndBackToTopFunctionality(int times) {
        boolean allCorrect = true;
        for (int i = 1; i <= times; i++) {
            logger.log("See More / Back to Top iteration -> " + i);

            String seeMoreText = driverTools.getText(driverTools.waitForElementToAppear(FAB_TEXT).orElseThrow());
            logger.log("FAB text (expecting See More) -> " + seeMoreText);
            boolean seeMoreCorrect = seeMoreText.toLowerCase().contains("more") || seeMoreText.toLowerCase().contains("selengkap");
            driverTools.click(SEE_MORE_FAB);

            // scroll down the page to switch the FAB into "Back to Top" state
            driverTools.swipeUp(2);
            driverTools.swipeDown(0.2);
            String backToTopText = driverTools.getText(driverTools.waitForElementToAppear(FAB_TEXT).orElseThrow());
            logger.log("FAB text (expecting Back to Top) -> " + backToTopText);
            boolean backToTopCorrect = backToTopText.toLowerCase().contains("top") || backToTopText.toLowerCase().contains("atas");
            driverTools.click(BACK_TO_TOP_FAB);
            driverTools.waitForElementToAppear(FAB_TEXT);

            allCorrect = allCorrect && seeMoreCorrect && backToTopCorrect;
        }
        return allCorrect;
    }

    @Override
    public void swipeToSatisfyWanderlust() {
        driverTools.swipeToElement(SATISFY_WANDERLUST_TITLE, Swipe.UP, 15).orElseThrow();
        driverTools.swipeElementToMidScreen(SATISFY_WANDERLUST_TITLE);
    }

    private Locator getWanderlustCategory(String category) {
        return new Locator("WANDERLUST_CATEGORY_" + category.toUpperCase(), AppiumBy.xpath("//*[contains(@resource-id,'view_gradient')]//following-sibling::*[contains(@resource-id,'tv_title') and @text='" + category + "']"));
    }

    @Override
    public void clickWanderlustCategory(String category) {
        WebElement categoryTile = driverTools.swipeToElement(getWanderlustCategory(category), Swipe.UP, 5).orElseThrow();
        driverTools.click(categoryTile);
    }

    private Locator getDiscountedPricLocator(String title){
        return new Locator("CROSSED_PRICE" + title,AppiumBy.xpath("//*[contains(@resource-id,'price_module_slash_price') and contains(@text,'"+title+"')]"));
    }

    private Locator getBasePriceLocator(String title){
        return new Locator("BASE_PRICE" + title,AppiumBy.xpath("//*[contains(@resource-id,'price_module_final_price') and contains(@text,'"+title+"')]"));
    }
}