package com.tiket.web.page.accommodation.homes.LandingPage;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomesLandingPageMWeb<T extends WebDriver> extends HomesLandingPageWeb<T> {

    public HomesLandingPageMWeb(Session<T> session) {
        super(session);
    }
    private static final Locator SAVE_BUTTON=new Locator("SAVE_BUTTON", AppiumBy.xpath("//button[@aria-label=\"save\"]"));
    private static final Locator CONTINUE_ON_WEB_POPUP = new Locator("CONTINUE_ON_WEB_POPUP", new By.ByXPath("//*[@data-qa-id='secondaryCtaClose-button']"));
    private static final Locator DONE_SET_ROOM_GUEST = new Locator("DONE_SET_ROOM_GUEST", new By.ByXPath("//button[contains(@class, 'RoomsAndGuests_button_done')]"));
    private static final Locator SAVE_SET_DATE_PICKER = new Locator("SAVE_SET_DATE_PICKER", new By.ByXPath("//button[contains(@class, 'DatePicker_button_save')]"));
    private static final Locator SAVE_NIGHT_BUTTON_MWEB = new Locator("SAVE_NIGHT_BUTTON_MWEB", By.xpath("//button[@aria-label='save']"));
    private static Locator getSearchHistoryLocationLabelByIndex(int index) {
        return new Locator("", new By.ByXPath(String.format("(//*[@data-testid='destination_title'])[%d]", index)));
    }

    private static Locator getSearchHistoryDateLabelByIndex(int index) {
        return new Locator("", new By.ByXPath(String.format("(//div[contains(@class,'RecentListItem_guest_summary')])[%d]/p[1]", index)));
    }

    private static Locator getSearchHistoryGuestLabelByIndex(int index) {
        return new Locator("", new By.ByXPath(String.format("(//div[contains(@class,'RecentListItem_guest_summary')])[%d]/p[2]", index)));
    }

    public String getSearchHistoryLocationLabel(int index) {
        driverTools.waitForElementToAppear(getSearchHistoryLocationLabelByIndex(index)).orElseThrow();
        return driverTools.getText(getSearchHistoryLocationLabelByIndex(index)).orElseThrow();
    }

    public String getSearchHistorySummaryLabel(int index) {
        driverTools.waitForElementToAppear(getSearchHistoryDateLabelByIndex(index)).orElseThrow();
        return driverTools.getText(getSearchHistoryDateLabelByIndex(index)).orElseThrow();
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
