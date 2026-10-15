package com.tiket.page.carrental.searchpage;

import com.tiket.appium.Swipe;
import com.tiket.model.Deeplink;
import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.page.common.calendar.CalendarPage;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.NoSuchElementException;

public class SearchPageCarRentalIOS  <T extends WebDriver> extends SearchPageCarRental<T>{

    private static final Locator BACK_BUTTON = new Locator("BACK_BUTTON",AppiumBy.xpath("//*[@aria-label='back to homepage' or @aria-label='kembali ke beranda']"));
    private static final Locator SKIP_INFORMATION_BTN = new Locator("SKIP_INFORMATION_BTN", By.xpath("//*[contains(@class,'Coachmark_coachmark_skip_btn')]"));
    private static final Locator TAB_WITH_DRIVER = new Locator("TAB_WITH_DRIVER", AppiumBy.name("with_driver_tab"));
    private static final Locator TAB_SELF_DRIVE = new Locator("TAB_SELF_DRIVER", AppiumBy.name("without_driver_tab"));
    private static final Locator SELECT_LOCATION_WITHOUT_DRIVER = new Locator("SELECT_LOCATION_WITHOUT_DRIVER", AppiumBy.name("cr_location_rental_without_driver"));
    private static final Locator ENTER_PICKUP_CITY = new Locator("ENTER_PICKUP_CITY", AppiumBy.name("search_city"));
    private static final Locator BTN_SEARCH_CAR = new Locator("BTN_SEARCH_CAR", AppiumBy.name("search_button"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", AppiumBy.name("save_button"));

    private static final Locator RENTAL_DURATION_WHEEL_PICKER = new Locator("RENTAL_DURATION_WHEEL_PICKER", AppiumBy.className("XCUIElementTypePickerWheel"));
    private static final Locator CAR_RENTAL_EDIT_DURATION = new Locator("CAR_RENTAL_EDIT_DURATION", AppiumBy.name("cr_rental_duration"));
    private static final Locator SELECT_LOCATION_WITH_DRIVER = new Locator("SELECT_LOCATION_WITH_DRIVER", AppiumBy.accessibilityId("cr_location_rental_with_driver"));
    private static final Locator RENTAL_CALENDAR = new Locator("DEPART_CALENDAR", AppiumBy.name("cr_rental_date"));
    private static final Locator SAVE_BUTTON_CALENDAR = new Locator("SAVE_BUTTON_CALENDAR", AppiumBy.xpath("//XCUIElementTypeButton[@label='Simpan' or @label='Save']"));
    private static final Locator CONTINUE_LOCATION = new Locator("CONTINUE_LOCATION", AppiumBy.accessibilityId("buttonPrimary"));
    private static final Locator CLOSE_GPS = new Locator("CLOSE_GPS", AppiumBy.xpath("//XCUIElementTypeButton[contains(@label,'Nanti aja') or contains(@label,'Maybe later')]"));
    private static final Locator WITHOUT_DRIVER_MARKER = new Locator("WITHOUT_DRIVER_MARKER", AppiumBy.name("without_driver_tab (selected) (selected)"));
    private static final Locator INFO_HOW_TO_RENT_CAR = new Locator("INFO_HOW_TO_RENT_CAR", AppiumBy.name("how_to_rent"));
    private static final Locator BACK_BUTTON_HOW_TO_RENT_CAR = new Locator("BACK_BUTTON_HOW_TO_RENT_CAR", AppiumBy.name("back"));
    private static final Locator HEADER_HOW_TO_RENT_CAR = new Locator("HEADER_HOW_TO_RENT_CAR", AppiumBy.xpath("//XCUIElementTypeStaticText[@label='Sewa Mobil | tiket.com' or @label='Car Rentals | tiket.com']"));
    private static final Locator CONTENT_HOW_TO_RENT_CAR = new Locator("CONTENT_HOW_TO_RENT_CAR", AppiumBy.xpath("//XCUIElementTypeOther[@label='Sewa Mobil | tiket.com' or @label='Car Rentals | tiket.com']"));
    private static final Locator ARROW_UP_BUTTON_HOW_TO_RENT_CAR = new Locator("ARROW_UP_BUTTON_HOW_TO_RENT_CAR", AppiumBy.accessibilityId("arrow up"));
    private static final Locator VIDEO_HOW_TO_RENT_CAR = new Locator("VIDEO_HOW_TO_RENT_CAR", AppiumBy.accessibilityId("YouTube Video Player"));
    private static final Locator PLAY_VIDEO_HOW_TO_RENT_CAR = new Locator("PLAY_VIDEO_HOW_TO_RENT_CAR", AppiumBy.accessibilityId("Play"));
    private static final Locator PAUSE_VIDEO_HOW_TO_RENT_CAR = new Locator("PAUSE_VIDEO_HOW_TO_RENT_CAR", AppiumBy.accessibilityId("Pause"));
    private static final Locator SEEKSLIDER_VIDEO_HOW_TO_RENT_CAR = new Locator("SEEKSLIDER_VIDEO_HOW_TO_RENT_CAR", AppiumBy.accessibilityId("Seek slider"));
    private static final Locator CELL_AFTER_VIDEO_HOW_TO_RENT_CAR = new Locator("CELL_AFTER_VIDEO_HOW_TO_RENT_CAR", AppiumBy.xpath("(//XCUIElementTypeOther[@name='YouTube Video Player']/ancestor::XCUIElementTypeCell/following-sibling::XCUIElementTypeCell)[1]"));
    public static final Locator ALLOW_ONCE_PERMISSION_BUTTON = new Locator("ALLOW_ONCE_PERMISSION_BUTTON", AppiumBy.xpath("//XCUIElementTypeButton[@name='Allow Once']"));
    public static final Locator ALLOW_CURRENT_LOCATION_POPUP = new Locator("ALLOW_CURRENT_LOCATION_POPUP", AppiumBy.xpath("//XCUIElementTypeButton[@name='Allow']"));
    public static final Locator RENTAL_TIME = new Locator("RENTAL_TIME", AppiumBy.accessibilityId("cr_rental_time"));
    public static final Locator HOUR_SECTION = new Locator("HOUR_SECTION", AppiumBy.accessibilityId("cr_tpw_hours"));
    public static final Locator SAVE_BUTTON_RENTAL_TIME = new Locator("SAVE_BUTTON_RENTAL_TIME", AppiumBy.xpath("//XCUIElementTypeOther[@name='cr_tpw_hours']/parent::*//following-sibling::*//XCUIElementTypeButton"));


    public SearchPageCarRentalIOS(Session<T> session) {
        super(session);
    }

    private boolean isSelectingWithoutDriver(){
        return driverTools.isElementDisplayed(WITHOUT_DRIVER_MARKER);
    }

   @Override
    public void backToHomePage(){
        driverTools.waitForWebview();
        driverTools.switchToWebView();
        driverTools.waitForElementToAppear(BACK_BUTTON).ifPresentOrElse(driverTools::click, ()-> {throw new NoSuchElementException();});
        driverTools.switchToNative();
    }

    private Locator getLocationLocator(String location) {
        return new Locator("CHOOSE_LOCATION", AppiumBy.xpath("//*[@label='"+location+"']"));
    }

    private boolean isCarRentalDurationCorrect(String daysAndEndDate){
        WebElement wheelPicker = driverTools.element(RENTAL_DURATION_WHEEL_PICKER).orElseThrow();
        String wheelPickerValue = wheelPicker.getAttribute("value");
        String daysValue = wheelPickerValue.split(" ")[0];
        logger.log("Wheel picker value -> "+wheelPickerValue);
        logger.log("is car rental duration correct -> "+ daysValue.equals(daysAndEndDate));
        return daysValue.equals(daysAndEndDate);
    }

    private void adjustTime(){
        driverTools.click(driverTools.onScreenElement(RENTAL_TIME));
        driverTools.swipeByDistance(HOUR_SECTION,Swipe.UP,0.6,3);
        driverTools.click(SAVE_BUTTON_RENTAL_TIME);
    }

    @Override
    public void swipeToInfoSection() {
        driverTools.swipeToElement(INFO_HOW_TO_RENT_CAR, Swipe.UP, 3);
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
            boolean isHeaderDisplayed = driverTools.isElementDisplayed(HEADER_HOW_TO_RENT_CAR);
            boolean isContentPresent = driverTools.waitForElementToPresence(CONTENT_HOW_TO_RENT_CAR).isPresent();
            boolean isArrowUpDisplayed = driverTools.isElementDisplayed(ARROW_UP_BUTTON_HOW_TO_RENT_CAR);

            return isHeaderDisplayed && isContentPresent && isArrowUpDisplayed;
        }

        return driverTools.isElementDisplayed(BACK_BUTTON_HOW_TO_RENT_CAR);
    }

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
        boolean isPlayButtonDisappear = driverTools.waitForElementToDisappear(PLAY_VIDEO_HOW_TO_RENT_CAR);
        boolean isPauseButtonAppear = driverTools.waitForElementToAppear(PAUSE_VIDEO_HOW_TO_RENT_CAR).isPresent();
        boolean isSeekSliderDisplayed = driverTools.isElementDisplayed(SEEKSLIDER_VIDEO_HOW_TO_RENT_CAR);

        return isPlayButtonDisappear && isPauseButtonAppear && isSeekSliderDisplayed;
    }

    @Override
    public void openViaDeeplink(){
        Deeplink deepLink = new Deeplink("CAR_SEARCH_PAGE",
                "tiket://m.gatotkaca.tiket.com/sewa-mobil",
                "tiket://m.preprod.tiket.com/sewa-mobil",
                "tiket://m.tiket.com/sewa-mobil");
        driverTools.deeplink(deepLink);
    }

    @Override
    public void allowLocationAccess() {
        driverTools.switchToNative();
        driverTools.waitForElementToAppear(CONTINUE_LOCATION).ifPresent(driverTools::click);
        driverTools.waitForElementToPresence(ALLOW_ONCE_PERMISSION_BUTTON, 15).ifPresent(driverTools::click);
        driverTools.waitForElementToPresence(ALLOW_CURRENT_LOCATION_POPUP).ifPresent(driverTools::click);
    }

    @Override
    public Boolean isLandingPageValid() {
        boolean isSearchCarButtonDisplayed = driverTools.waitForElementToAppear(BTN_SEARCH_CAR, 5).isPresent();
        boolean isSelectDriverDisplayed = driverTools.waitForElementToAppear(TAB_WITH_DRIVER, 5).isPresent();

        return isSearchCarButtonDisplayed && isSelectDriverDisplayed;
    }

}
