package com.tiket.page.carrental.searchpage;

import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Deeplink;
import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.NoSuchElementException;


public class SearchPageCarRentalAndroid  <T extends WebDriver> extends SearchPageCarRental<T>{
    
    private static final Locator SKIP_INFORMATION_BTN = new Locator("SKIP_INFORMATION_BTN", AppiumBy.xpath("//*[@text='Lewati' or @text='Skip']"));
    private static final Locator TAB_WITH_DRIVER = new Locator("TAB_WITH_DRIVER", AppiumBy.xpath("//*[@text='With Driver' or @text='Dengan Sopir']"));
    private static final Locator TAB_SELF_DRIVE = new Locator("TAB_SELF_DRIVE", AppiumBy.xpath("//*[@text='Self Drive' or @text='Lepas Kunci']"));
    private static final Locator SELECT_LOCATION = new Locator("SELECT_LOCATION", AppiumBy.id("sf_pickup_location"));
    private static final Locator INPUT_PICKUP_LOCATION = new Locator("ENTER_PICKUP_CITY", AppiumBy.id("tds_search_et"));
    private static final Locator SELECT_PICKUP_LOCATION = new Locator("SELECT_PICKUP_LOCATION", AppiumBy.id("tds_button_view"));
    private static final Locator BTN_SEARCH_CAR = new Locator("BTN_SEARCH_CAR", AppiumBy.id("btn_search"));
    private static final Locator CURRENT_SELECTED_CAR_RENT_DURATION = new Locator("CURRENT_SELECTED_CAR_RENT_DURATION", AppiumBy.xpath("//*[contains(@resource-id,'sf_duration')]//android.widget.TextView[contains(@resource-id,'tds_search_form_title')]"));
    private static final Locator SELECT_CAR_DURATION = new Locator("SELECT_CAR_DURATION", AppiumBy.xpath("//*[contains(@resource-id,'sf_duration')]//following-sibling::*[contains(@resource-id,'tds_search_form_title_group')]"));
    private static final Locator CAR_RENTAL_DURATION_WHEEL_PICKER = new Locator("CAR_RENTAL_DURATION_WHEEL_PICKER", AppiumBy.id("rv_content"));
    private static final Locator CAR_RENTAL_DURATION_CURRENT_VALUE = new Locator("CAR_RENTAL_DURATION_CURRENT_VALUE", AppiumBy.xpath("(//*[contains(@resource-id,'tv_duration')])[2]"));
    private static final Locator CAR_RENTAL_DURATION_SAVE_BTN = new Locator("CAR_RENTAL_DURATION_SAVE_BTN", AppiumBy.id("tds_button_view"));
    private static final Locator RENTAL_CALENDAR = new Locator("DEPART_CALENDAR", AppiumBy.id("sf_pickup_date"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", AppiumBy.id("tds_btn"));
    private static final Locator CLOSE_GPS = new Locator("CLOSE_GPS", AppiumBy.id("tds_btn_secondary_info_view"));
    private static final Locator INFO_SECTION = new Locator("INFO_SECTION", AppiumBy.xpath("//*[contains(@resource-id,'tv_section_title') and (@text='Info Sewa dengan Sopir' or @text='Info Sewa Lepas Kunci' or @text='Info about Rent With Driver' or @text='Info about Rent Self Drive')]"));
    private static final Locator INFO_HOW_TO_RENT_CAR = new Locator("INFO_HOW_TO_RENT_CAR", AppiumBy.xpath("//*[contains(@resource-id,'vg_list_item')]//*[contains(@resource-id,'tds_title_list') and (@text='Cara Menyewa Mobil' or @text='How to Rent a Car')]"));
    private static final Locator BACK_BUTTON_HOW_TO_RENT_CAR = new Locator("BACK_BUTTON_HOW_TO_RENT_CAR", AppiumBy.xpath("//*[contains(@resource-id,'toolbar')]//android.widget.ImageButton"));
    private static final Locator HEADER_HOW_TO_RENT_CAR = new Locator("HEADER_HOW_TO_RENT_CAR", AppiumBy.xpath("//*[contains(@resource-id,'toolbar')]//android.widget.TextView"));
    private static final Locator CONTENT_HOW_TO_RENT_CAR = new Locator("CONTENT_HOW_TO_RENT_CAR", AppiumBy.xpath("//*[contains(@resource-id,'__next')]"));
    private static final Locator ARROW_UP_BUTTON_HOW_TO_RENT_CAR = new Locator("ARROW_UP_BUTTON_HOW_TO_RENT_CAR", AppiumBy.xpath("//*[contains(@resource-id,'__next')]//android.widget.Button[@text='arrow up']"));
    private static final Locator VIDEO_HOW_TO_RENT_CAR = new Locator("VIDEO_HOW_TO_RENT_CAR", AppiumBy.xpath("//*[contains(@resource-id,'v_overlay')]"));
    private static final Locator PLAY_VIDEO_HOW_TO_RENT_CAR = new Locator("PLAY_VIDEO_HOW_TO_RENT_CAR", AppiumBy.xpath("//*[contains(@resource-id,'iv_play')]"));
    private static final Locator CELL_AFTER_VIDEO_HOW_TO_RENT_CAR = new Locator("CELL_AFTER_VIDEO_HOW_TO_RENT_CAR", AppiumBy.xpath("//*[contains(@resource-id,'div_bottom')]"));
    private static final Locator PAUSE_VIDEO_HOW_TO_RENT_CAR = new Locator("PAUSE_VIDEO_HOW_TO_RENT_CAR", AppiumBy.id("play_pause_button"));
    private static final Locator SEEKSLIDER_VIDEO_HOW_TO_RENT_CAR = new Locator("SEEKSLIDER_VIDEO_HOW_TO_RENT_CAR", AppiumBy.id("youtube_player_seekbar"));

    public SearchPageCarRentalAndroid(Session<T> session) {
        super(session);
    }

    private Locator getLocationLocator(String location) {
        return new Locator("CHOOSE_LOCATION", AppiumBy.xpath("//*[contains(@resource-id,'tds_title_list') and (@text='"+location+"')]"));
    }

    private boolean isSelectedRentalDurationCorrect(String duration){
        String selectedCarRentalDurationValue = driverTools.getText(CAR_RENTAL_DURATION_CURRENT_VALUE).orElseThrow().split(" ")[0].trim();
        logger.log("selected duration in rental duration wheel picker -> " + selectedCarRentalDurationValue);
        logger.log("duration -> " + duration);
        logger.log(String.valueOf(selectedCarRentalDurationValue.equals(duration)));
        return  selectedCarRentalDurationValue.equals(duration);
    }

    @Override
    public void closeInformationPopUp(){
        boolean skipPopup = false;
        if (driverTools.waitForElementToAppear(SKIP_INFORMATION_BTN,5).isPresent()){
            driverTools.click(SKIP_INFORMATION_BTN);
            skipPopup = true;
        }
        driverTools.waitForElementToAppear(CLOSE_GPS,5).ifPresent(driverTools::click);

        if (!skipPopup){
            driverTools.waitForElementToAppear(SKIP_INFORMATION_BTN,5).ifPresent(driverTools::click);
        }

        driverTools.waitForElementToAppear(BTN_SEARCH_CAR);
    }
    @Override
    public void backToHomePage(){}

    @Override
    public void swipeToInfoSection() {
        driverTools.swipeToElement(INFO_SECTION, Swipe.UP, 3);
    }

    @Override
    public void clickInfoSection(String infoSubSectionType) {
        switch (infoSubSectionType.toLowerCase()) {
            case("howtorent") -> {driverTools.click(INFO_HOW_TO_RENT_CAR);}
            default -> {logger.fail("There is no option for '"+infoSubSectionType+"'");}
        }
    }

    @Override
    public boolean verifyHowToRentPage(Environment environment) {
        driverTools.waitForElementToAppear(BACK_BUTTON_HOW_TO_RENT_CAR);

        if(environment == Environment.PROD){
            boolean isHeaderDisplayed = driverTools.waitForElementToAppear(HEADER_HOW_TO_RENT_CAR).isPresent();
            boolean isContentPresent = driverTools.waitForElementToAppear(CONTENT_HOW_TO_RENT_CAR).isPresent();
            boolean isArrowUpDisplayed = driverTools.waitForElementToAppear(ARROW_UP_BUTTON_HOW_TO_RENT_CAR).isPresent();

            return isHeaderDisplayed && isContentPresent && isArrowUpDisplayed;
        }

        return driverTools.isElementDisplayed(BACK_BUTTON_HOW_TO_RENT_CAR);    }

    @Override
    public void clickBackFromHowToRent() {
        driverTools.click(BACK_BUTTON_HOW_TO_RENT_CAR);
    }

    @Override
    public void swipeToHowToRentVideo() {
        driverTools.swipeToElement(VIDEO_HOW_TO_RENT_CAR, Swipe.UP, 4);
    }

    @Override
    public void clickToHowToRentVideo() {
        driverTools.swipeToElement(CELL_AFTER_VIDEO_HOW_TO_RENT_CAR, Swipe.UP, 2);
        driverTools.click(PLAY_VIDEO_HOW_TO_RENT_CAR);
    }

    @Override
    public boolean verifyHowToRentVideoPlayed() {
        boolean isPauseButtonAppear = driverTools.element(PAUSE_VIDEO_HOW_TO_RENT_CAR).isPresent();
        boolean isSeekSliderDisplayed = driverTools.element(SEEKSLIDER_VIDEO_HOW_TO_RENT_CAR).isPresent();
        boolean isPlayButtonDisappear = driverTools.element(PLAY_VIDEO_HOW_TO_RENT_CAR).isEmpty();

        return isPlayButtonDisappear && isPauseButtonAppear && isSeekSliderDisplayed;
    }

    private Locator getDateLocator(String monthId, String monthEng, String day){
        return new Locator("Date Locator", AppiumBy.xpath("//*[contains(@resource-id,'month_name') and (contains(@text,'"+monthId+"') or contains(@text,'"+monthEng+"'))]//following-sibling::*//*[contains(@resource-id,'day')]//*[@text='"+day+"']"));
    }

    @Override
    public void openViaDeeplink(){
        Deeplink deepLink = new Deeplink("CAR_SEARCH_PAGE",
                "https://m.gatotkaca.tiket.com/sewa-mobil",
                "https://m.preprod.tiket.com/sewa-mobil",
                "https://m.tiket.com/sewa-mobil");
        driverTools.deeplink(deepLink);
    }

    @Override
    public void allowLocationAccess() {

    }

    @Override
    public Boolean isLandingPageValid() {
        boolean isSearchCarButtonDisplayed = driverTools.waitForElementToAppear(BTN_SEARCH_CAR, 5).isPresent();
        boolean isSelectLocationDisplayed = driverTools.waitForElementToAppear(SELECT_LOCATION, 5).isPresent();
        boolean isSelectDriverDisplayed = driverTools.waitForElementToAppear(TAB_WITH_DRIVER, 5).isPresent();

        return isSearchCarButtonDisplayed && isSelectLocationDisplayed && isSelectDriverDisplayed;
    }
}
