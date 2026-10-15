package com.tiket.page.accommodation.homes.homessearchresultpage;

import com.tiket.appium.Swipe;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static com.tiket.core.CoreFunctions.splitLanguageData;
import static com.tiket.page.accommodation.hotel.searchresultpage.SearchResultPageAndroid.SRP_FILTER_BAR;

public class HomesSearchResultPageAndroid<T extends WebDriver> extends HomesSearchResultPage<T> {

    private static final Locator BEDROOM_FILTER = new Locator("BEDROOM_FILTER", AppiumBy.xpath("//*[contains(@resource-id,'tds_chips_text') and (contains(@text,'Jumlah Kamar') or contains(@text,'No. of Bedroom'))]"));
    public static final Locator BEDROOM_FILTER_SELECTED = new Locator("BEDROOM_FILTER_SELECTED", AppiumBy.xpath("//*[contains(@resource-id,'tds_chips_text') and ((contains(@text,' Kamar') or contains(@text,' Bedrooms')) and not(contains(@text,'Jumlah')))]"));
    private static final Locator LOYALTY_BADGE = new Locator("LOYALTY_BADGE", AppiumBy.id("iv_tier"));
    private static final Locator BEDROOM_FILTER_COUNT = new Locator("BEDROOM_FILTER_COUNT", AppiumBy.xpath("//*[contains(@resource-id,'tds_selection_control_list')]//*[contains(@resource-id,'tv_count')]"));
    private static final Locator BEDROOM_FILTER_MIN_BUTTON = new Locator("BEDROOM_FILTER_MIN_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'tds_selection_control_list')]//*[contains(@resource-id,'btn_decrement')]"));
    private static final Locator BEDROOM_FILTER_ADD_BUTTON = new Locator("BEDROOM_FILTER_ADD_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'tds_selection_control_list')]//*[contains(@resource-id,'btn_increment')]"));
    private static final Locator FLEXIBLE_STAY_DATES_TITLE = new Locator("FLEXIBLE_STAY_DATES_TITLE", AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and (@text='Do you have flexible stay dates?' or @text='Tanggal nginepmu fleksibel?')]"));
    private static final Locator FIRST_FLEXIBLE_STAY_HOTEL_NAME = new Locator("FIRST_FLEXIBLE_STAY_HOTEL_NAME", AppiumBy.xpath("(//*[contains(@resource-id,'tv_title') and (@text='Do you have flexible stay dates?' or @text='Tanggal nginepmu fleksibel?')]//following-sibling::*[contains(@resource-id,'rv_search_result')]//*[contains(@resource-id,'tv_hotel_name')])[1]"));
    private static final Locator FIRST_FLEXIBLE_STAY_LOCATION = new Locator("FIRST_FLEXIBLE_STAY_LOCATION", AppiumBy.xpath("(//*[contains(@resource-id,'tv_title') and (@text='Do you have flexible stay dates?' or @text='Tanggal nginepmu fleksibel?')]//following-sibling::*[contains(@resource-id,'rv_search_result')]//*[contains(@resource-id,'tv_location_info')])[1]"));

    public HomesSearchResultPageAndroid(Session<T> session) {
        super(session);
    }

    @Override
    public boolean isFlexibleStayDatesSectionShown() {
        return driverTools.swipeToElement(FLEXIBLE_STAY_DATES_TITLE, Swipe.UP, 25).isPresent();
    }

    @Override
    public void swipeToFlexibleStayDates() {
        driverTools.swipeToElement(FLEXIBLE_STAY_DATES_TITLE, Swipe.UP, 25).orElseThrow();
    }

    @Override
    public String getFirstFlexibleStayHotelName() {
        WebElement hotelName = driverTools.swipeToElement(FIRST_FLEXIBLE_STAY_HOTEL_NAME, Swipe.UP, 10).orElseThrow();
        String name = driverTools.getText(hotelName);
        logger.log("first flexible stay dates hotel name -> " + name);
        return name;
    }

    @Override
    public String getFirstFlexibleStayLocation() {
        WebElement location = driverTools.swipeToElement(FIRST_FLEXIBLE_STAY_LOCATION, Swipe.UP, 10).orElseThrow();
        String locationText = driverTools.getText(location);
        logger.log("first flexible stay dates location -> " + locationText);
        return locationText;
    }

    @Override
    public void selectFirstFlexibleStayCard() {
        WebElement card = driverTools.swipeToElement(FIRST_FLEXIBLE_STAY_HOTEL_NAME, Swipe.UP, 10).orElseThrow();
        driverTools.click(card);
    }

    @Override
    public boolean verifySrpElements() {
        return driverTools.swipeToElement(BEDROOM_FILTER, SRP_FILTER_BAR, Swipe.LEFT, 10).isPresent();
    }

    @Override
    public void clickBedroomsFilterButton() {
        // FIXME: Android implementation pending
    }

    @Override
    public void addTotalBedroom(String totalBedroom) {
        driverTools.swipeToElement(BEDROOM_FILTER_ADD_BUTTON, Swipe.UP, 2).orElseThrow();
        logger.log("adding "+totalBedroom+" for number bedroom");

        int currentCount = Integer.parseInt(driverTools.getText(BEDROOM_FILTER_COUNT).orElseThrow());
        int difference = Integer.parseInt(totalBedroom) - currentCount;

        if (difference < 0) {
            for (int i = 0; i > difference; i--) {
                driverTools.click(BEDROOM_FILTER_MIN_BUTTON);
            }
        } else {
            for (int i = 0; i < difference; i++) {
                driverTools.click(BEDROOM_FILTER_ADD_BUTTON);
            }
        }
    }

    @Override
    public void selectAccomodationTypeFilter(String accomadationType) {
        WebElement accomodationOption = driverTools.swipeToElement(homesAccomodationTypeLocator(accomadationType),Swipe.UP,2).orElseThrow();
        driverTools.click(accomodationOption);
    }

    private Locator homesAccomodationTypeLocator(String type) {
        String accomadationTypeIN = splitLanguageData(type, "/")[0];
        String accomadationTypeENG = splitLanguageData(type, "/")[1];
        return new Locator("ACCOMODATION_TYPE", AppiumBy.xpath("//*[contains(@resource-id,'tds_subtitle_list') and (@text='"+accomadationTypeENG+"' or @text='"+accomadationTypeIN+"')]"));

    }

    @Override
    public void setBedroomsFilterHomes(String totalBedRoom) {

    }

    @Override
    public void selectHomesWithLoyaltyBenefit() {
        driverTools.swipeToElement(LOYALTY_BADGE, Swipe.UP, 5);
        driverTools.click(LOYALTY_BADGE);
    }
}