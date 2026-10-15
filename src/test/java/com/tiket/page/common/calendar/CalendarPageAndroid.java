package com.tiket.page.common.calendar;

import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.pojo.utilities.HolidayResponse;
import com.tiket.testbase.Assertion;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;

public class CalendarPageAndroid <T extends WebDriver> extends CalendarPage<T> {

    private static final Locator MAIN_CALENDAR_VIEW = new Locator("MAIN_CALENDAR_VIEW", AppiumBy.id("tds_calendar_month_rv"));
    private static final Locator NEXT_MONTH_BTN = new Locator("NEXT_MONTH_BTN", AppiumBy.id("ll_next_month"));
    private static final Locator MONTH_NAME = new Locator("MONTH_NAME", AppiumBy.id("tds_calendar_sticky_name"));
    private static final Locator HOLIDAY_DATE = new Locator("HOLIDAY_DATE", AppiumBy.id("ll_holidays"));
    private static final Locator HOLIDAY_DATE_CHILD = new Locator("HOLIDAY_DATE_CHILD", AppiumBy.xpath("//*[contains(@resource-id,'ll_holidays')]/*"));

    public CalendarPageAndroid(Session<T> session) {
        super(session);
    }

    @Override
    public void swipe(Swipe direction) {
        WebElement element = driverTools.element(MAIN_CALENDAR_VIEW).orElseThrow();
        switch (direction) {
            case UP -> driverTools.swipeUp();
            case DOWN -> driverTools.swipeDown(element);
            case RIGHT -> driverTools.swipeRight(element);
            case LEFT -> driverTools.swipeLeft(element);
        }
    }

    @Override
    public boolean selectDate(String date) {
        return true;
    }//FIXME: Implementation Pending

    @Override
    public void clickNextMonthButton() {
        driverTools.click(NEXT_MONTH_BTN);
    }

    @Override
    public boolean isHolidayValid(HolidayResponse holidayResponse, String language) {
        ArrayList<HolidayResponse.Content> listContent = holidayResponse.data.content;
        ArrayList<HolidayResponse.Content> listContentCurrentMonth = new ArrayList<>();
        driverTools.waitForElementToAppear(MONTH_NAME);
        String[] currentMonthText = driverTools.getText(MONTH_NAME).orElseThrow().split(" ");
        int monthNumber = CoreFunctions.getMonthNumber(currentMonthText[0]);
        String currentYear = currentMonthText[1];
        String monthNumberString = monthNumber < 10 ? "0" + monthNumber : "" + monthNumber;

        for(HolidayResponse.Content content : listContent) {
            logger.log("Date: "+ content.date);
            logger.log("Find: "+ currentYear + "-" + monthNumberString);
            if(content.date.contains(currentYear + "-" + monthNumberString)) {
                listContentCurrentMonth.add(content);
            }
        }

        logger.log("listContentCurrentMonth size : " + listContentCurrentMonth.size());
        if(listContentCurrentMonth.size() < 1) {
            return !driverTools.isElementDisplayed(HOLIDAY_DATE);
        } else {
            if (driverTools.isElementDisplayed(HOLIDAY_DATE)) {
                List<WebElement> holidaysElement = driverTools.elements(HOLIDAY_DATE_CHILD).orElseThrow();
                int holidayCount = holidaysElement.size();

                if(listContentCurrentMonth.size() == holidayCount) {
                    for(HolidayResponse.Content content : listContentCurrentMonth) {
                        String holidayDescriptionAPI = "";
                        String holidayDateAPI = content.date;
                        for(HolidayResponse.Description description : content.descriptions) {
                            if(description.lang.equalsIgnoreCase(language)) {
                                holidayDescriptionAPI += description.content;
                            }
                        }
                        boolean isHolidayDateDisplayed = driverTools.isElementDisplayed(getHolidayDateLocatorByTextValue(holidayDateAPI, language));
                        boolean isHolidayDescDisplayed = driverTools.isElementDisplayed(getHolidayDescLocatorByTextValue(holidayDescriptionAPI));

                        Assertion.assertThat("[ID] Holiday Date '"+holidayDateAPI+"' Holiday Text '"+holidayDescriptionAPI+"'", isHolidayDateDisplayed && isHolidayDescDisplayed, is(true));
                    }
                } else {
                    return false;
                }
                return true;
            } else {
                return false;
            }
        }
    }

    private Locator getHolidayDateLocatorByTextValue(String textValue, String language) {
        String[] textValueSplit = textValue.split("-");
        textValue = textValueSplit[2] + " " + CoreFunctions.getMonthName(Integer.parseInt(textValueSplit[1]), language.toLowerCase());
        return new Locator("HOLIDAY_DATE_CHILD", AppiumBy.xpath("//*[contains(@resource-id,'ll_holidays')]//*[contains(@resource-id,'tv_date') and contains(@text,'"+textValue+"')]"));
    }

    private Locator getHolidayDescLocatorByTextValue(String textValue) {
        return new Locator("HOLIDAY_DATE_CHILD", AppiumBy.xpath("//*[contains(@resource-id,'ll_holidays')]//*[contains(@resource-id,'tv_name') and contains(@text,'"+textValue+"')]"));
    }

}
