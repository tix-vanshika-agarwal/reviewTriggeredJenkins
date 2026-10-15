package com.tiket.SmartUI.page.hotel;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;

public class HotelSmartPageAndroid<T extends WebDriver> extends HotelSmartPage<T> {
    public static final Locator BACK_BUTTON = new Locator("BACK_BUTTON",AppiumBy.xpath("//*[contains(@resource-id, 'tds_icon_navigation_toolbar')]"));
    private static final Locator GRADIENT_BANNER = new Locator("GRADIENT_BANNER", AppiumBy.id("tds_content_notification_banner"));
    public static final Locator DATE_FIELD_BUTTON = new Locator("DATE_FIELD_BUTTON", AppiumBy.xpath("//*[contains(@resource-id, 'form_date_guest_room')]//*[contains(@resource-id, 'tds_search_form_title_group')]"));
    public static final Locator TODAY_DATE_CALENDAR = new Locator("TODAY_DATE_CALENDAR", AppiumBy.xpath("//android.widget.TextView[@text='Today']/parent::*[@resource-id='com.tiket.gits:id/ll_day_group']"));
    public static final Locator DATE_25 = new Locator("DATE_25", AppiumBy.xpath("//android.widget.TextView[@text='25']/parent::*[@resource-id='com.tiket.gits:id/ll_day_group']"));
    private static final Locator BOTTOMSHEET_CLOSE_BUTTON = new Locator("BOTTOMSHEET_CLOSE_BUTTON", AppiumBy.id("tds_calendar_close"));
    private static final Locator SRP_CHANGE_DATE_BUTTON = new Locator("SRP_CHANGE_DATE_BUTTON", AppiumBy.id("tds_appbar_item_button"));
    private static final Locator CLOSE_SEARCHFORM_BUTTON = new Locator("CLOSE_SEARCHFORM_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'iv_close') and @clickable='true' and @enabled='true']"));
    private static final Locator SEARCHFORM_SRP_TITLE = new Locator("SEARCHFORM_SRP_TITLE", AppiumBy.id("tv_label"));



    public HotelSmartPageAndroid(Session<T> session) {
        super(session);
    }

    @Override
    public void clickGradientBanner() {
        driverTools.waitForElementToAppear(GRADIENT_BANNER);
        driverTools.click(GRADIENT_BANNER);
    }

    @Override
    public void setDate(String type, String page) {

        driverTools.click(DATE_FIELD_BUTTON);

        switch (type) {
            case "ONE_DAY":
                driverTools.click(TODAY_DATE_CALENDAR);
                driverTools.click(TODAY_DATE_CALENDAR);
                break;
            case "MORE_THAN_30_DAYS":
                driverTools.click(TODAY_DATE_CALENDAR);
                driverTools.swipeUp(3);
                driverTools.click(DATE_25);
                break;
        }
    }

    @Override
    public void closeCalendar() {
        driverTools.click(BOTTOMSHEET_CLOSE_BUTTON);
    }

    @Override
    public void openSRPSearchForm() {
        driverTools.click(SRP_CHANGE_DATE_BUTTON);
    }

    @Override
    public void closeSearchBottomsheet() {
        driverTools.waitForElementToAppear(SEARCHFORM_SRP_TITLE);
        driverTools.clickElementByCoordinate(CLOSE_SEARCHFORM_BUTTON);
    }


}