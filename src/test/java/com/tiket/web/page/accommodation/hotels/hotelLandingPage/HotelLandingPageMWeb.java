package com.tiket.web.page.accommodation.hotels.hotelLandingPage;

import com.tiket.model.Locator;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.page.home.HomePage;
import com.tiket.testbase.Configs;
import io.appium.java_client.AppiumBy;
import lombok.SneakyThrows;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;

public class HotelLandingPageMWeb<T extends WebDriver> extends HotelLandingPageWeb<T> {

    public HotelLandingPageMWeb(Session<T> session) {
        super(session);
    }
    private static final Locator SAVE_BUTTON=new Locator("SAVE_BUTTON", AppiumBy.xpath("//button[@aria-label=\"save\"]"));
    private static final Locator CONTINUE_ON_WEB_POPUP = new Locator("CONTINUE_ON_WEB_POPUP", new By.ByXPath("//*[@data-qa-id='secondaryCtaClose-button']"));
    private static final Locator DONE_SET_ROOM_GUEST = new Locator("DONE_SET_ROOM_GUEST", new By.ByXPath("//button[contains(@class, 'RoomsAndGuests_button_done')]"));
    private static final Locator SAVE_SET_DATE_PICKER = new Locator("SAVE_SET_DATE_PICKER", new By.ByXPath("//button[contains(@class, 'DatePicker_button_save')]"));
    private static final Locator SAVE_NIGHT_BUTTON_MWEB = new Locator("SAVE_NIGHT_BUTTON_MWEB", By.xpath("//button[@aria-label='save']"));
    private static final Locator ANDROID_ACCEPT_ALERT = new Locator("ANDROID_ACCEPT_ALERT", AppiumBy.xpath("//*[@resource-id=\"com.android.chrome:id/positive_button\"]"));
    private static final Locator ANDROID_NEAR_ME = new Locator("NEAR_ME", AppiumBy.xpath("//div[@data-testid='nearby-destination']"));
    public static final Locator ANDROID_ALLOW_THE_SITE = new Locator("ALLOW_THE_SITE", AppiumBy.accessibilityId("Allow while visiting the site"));
    private static final Locator ANDROID_SEARCH_FORM_CHECK = new Locator("SEARCH_FORM_CHECK",  AppiumBy.xpath("//*[@text=\"Near you\" and @class=\"android.widget.TextView\"]"));
    public static final Locator IOS_ALLOW = new Locator("IOS_ALLOW", AppiumBy.accessibilityId("Allow"));
    private static final Locator SEARCH_FORM = new Locator("SEARCH_FORM", new By.ByXPath("//*[@data-testid='destination-input-text']"));

    private static final Locator NEAR_ME = new Locator("NEAR_ME", new By.ByXPath("//*[@data-testid='nearby-destination']"));

    private static Locator getSearchHistoryLocationLabelByIndex(int index) {
        return new Locator("", new By.ByXPath(String.format("(//*[@data-testid='destination_title'])[%d]", index)));
    }

    private static Locator getSearchHistoryDateLabelByIndex(int index) {
        return new Locator("", new By.ByXPath(String.format("(//div[contains(@class,'RecentListItem_guest_summary')])[%d]/p[1]", index)));
    }

    private static Locator getSearchHistoryGuestLabelByIndex(int index) {
        return new Locator("", new By.ByXPath(String.format("(//div[contains(@class,'RecentListItem_guest_summary')])[%d]/p[2]", index)));
    }

    @Override
    public void closeContinueOnWebPopup() {
        driverTools.waitForElementToBeClickable(CONTINUE_ON_WEB_POPUP, 5);
        if (driverTools.isElementDisplayed(CONTINUE_ON_WEB_POPUP)) {
            driverTools.click(CONTINUE_ON_WEB_POPUP);
        }
    }

    public void saveDate(){
        driverTools.clickWithJavascriptExecutor(SAVE_BUTTON);
    }

    @Override
    public void clickDoneSetRoomGuest() {
        driverTools.clickWithJavascriptExecutor(DONE_SET_ROOM_GUEST);
    }

    @Override
    public void clickDoneSetStayDate() {
        driverTools.click(SAVE_SET_DATE_PICKER);
    }

    @Override
    public void setStayDate(String type, String date) {
        List<Locator> startAndEndDateLocators = getStartAndEndDateLocators(type, date);
        driverTools.click(DATE_FIELD_BUTTON);
        driverTools.waitForElementToAppear(startAndEndDateLocators.get(0));
        driverTools.clickWithJavascriptExecutor(startAndEndDateLocators.get(0));
        driverTools.clickWithJavascriptExecutor(startAndEndDateLocators.get(1));

        driverTools.click(SAVE_NIGHT_BUTTON_MWEB);
        driverTools.waitForElementToDisappear(SAVE_NIGHT_BUTTON_MWEB);
    }

    @Override
    public void clickOnCalendarWidgetToOpen() {
        driverTools.click(DATE_FIELD_BUTTON);
    }

    @Override
    public void clickNearMeLocation(){
        if(Configs.PLATFORM == Platform.AMWEB) {
            driverTools.waitForElementToAppear(ANDROID_NEAR_ME,15);
            driverTools.clickWithJavascriptExecutor(ANDROID_NEAR_ME);
            driverTools.switchToNative();
            driverTools.waitForElementToAppear(ANDROID_ALLOW_THE_SITE, 5).ifPresent(driverTools::click);
            driver.findElement(AppiumBy.androidUIAutomator("new UiSelector().textContains(\"While using the app\")")).click();
            driverTools.waitForWebview();
            driverTools.switchToWebView();
        }
        else if (Configs.PLATFORM == Platform.IMWEB) {
            driverTools.waitForElementToAppear(NEAR_ME);
            driverTools.clickWithJavascriptExecutor(NEAR_ME);
            driverTools.switchToNative();
            driverTools.waitForElementToPresence(IOS_ALLOW, 5).ifPresent(driverTools::click);
            driverTools.switchToWebView();
        }

    }

    @Override
    public boolean isSearchHistoryDestinationLabelDisplayed(String expected, int index) {
        driverTools.waitForElementToAppear(getSearchHistoryLocationLabelByIndex(index)).orElseThrow();
        return driverTools.getText(getSearchHistoryLocationLabelByIndex(index)).orElseThrow().contains(expected);
    }

    @Override
    public boolean isSearchHistoryDateLabelDisplayed(String expected, int index) {
        driverTools.waitForElementToAppear(getSearchHistoryDateLabelByIndex(index)).orElseThrow();
        return driverTools.getText(getSearchHistoryDateLabelByIndex(index)).orElseThrow().contains(expected);
    }

    @Override
    public boolean isSearchHistoryGuestLabelDisplayed(String expected, int index) {
        driverTools.waitForElementToAppear(getSearchHistoryGuestLabelByIndex(index)).orElseThrow();
        return driverTools.getText(getSearchHistoryGuestLabelByIndex(index)).orElseThrow().contains(expected);
    }
}
