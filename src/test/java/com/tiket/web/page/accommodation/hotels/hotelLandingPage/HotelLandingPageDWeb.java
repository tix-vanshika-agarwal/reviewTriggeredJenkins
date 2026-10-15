package com.tiket.web.page.accommodation.hotels.hotelLandingPage;

import com.tiket.model.Exception.DriverToolsException;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;

import java.util.List;

public class HotelLandingPageDWeb<T extends WebDriver> extends HotelLandingPageWeb<T> {

    public HotelLandingPageDWeb(Session<T> session) {
        super(session);
    }

    private static final Locator DONE_SET_ROOM_GUEST = new Locator("DONE_SET_ROOM_GUEST", AppiumBy.xpath("//*[contains(@class,'footer_button')]//*[contains(@class,'Button_variant_primary')]"));
    private static final Locator NEAR_ME = new Locator("NEAR_ME", AppiumBy.xpath("//*[@data-testid='nearby-destination']"));
    private static final Locator SEARCH_FORM = new Locator("SEARCH_FORM", AppiumBy.xpath("//*[@data-testid='destination-input-text']"));

    @Override
    public void closeContinueOnWebPopup() {
        // Not required yet for DWEB
    }
    public void saveDate(){
        //not needed for dweb
        return;
    }

    @Override
    public void clickDoneSetRoomGuest() {
        driverTools.clickWithJavascriptExecutor(DONE_SET_ROOM_GUEST);
    }

    @Override
    public void clickDoneSetStayDate() {
        // Not required yet for DWEB
    }

    @Override
    public void setStayDate(String type, String date) {
        List<Locator> startAndEndDateLocators = getStartAndEndDateLocators(type, date);
        driverTools.click(DATE_FIELD_BUTTON);
        driverTools.waitForElementToAppear(startAndEndDateLocators.get(0));
        driverTools.clickWithJavascriptExecutor(startAndEndDateLocators.get(0));
        driverTools.clickWithJavascriptExecutor(startAndEndDateLocators.get(1));
    }

    @Override
    public void clickOnCalendarWidgetToOpen() {
        driverTools.click(DATE_FIELD_BUTTON);
    }

    @Override
    public void clickNearMeLocation(){
        driverTools.click(NEAR_ME);
        driverTools.waitForElementToAppear(SEARCH_FORM);
    }

    @Override
    public boolean isSearchHistoryDestinationLabelDisplayed(String expected, int index) {
        return false;
    }

    @Override
    public boolean isSearchHistoryDateLabelDisplayed(String expected, int index) {
        return false;
    }

    @Override
    public boolean isSearchHistoryGuestLabelDisplayed(String expected, int index) {
        return false;
    }
}
