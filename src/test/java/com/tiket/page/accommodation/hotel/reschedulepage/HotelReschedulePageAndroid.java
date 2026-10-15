package com.tiket.page.accommodation.hotel.reschedulepage;

import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static com.tiket.core.CoreFunctions.generateRandomInt;

public class HotelReschedulePageAndroid<T extends WebDriver> extends HotelReschedulePage<T> {

    public HotelReschedulePageAndroid(Session<T> session) {
        super(session);
    }

    public static final Locator CHECK_IN_HEADER = new Locator("CHECK_IN_HEADER", By.xpath("//*[contains(@resource-id,'tv_check_in_header')]/following-sibling::*[1]"));
    public static final Locator TODAYS_DATE = new Locator("TODAYS_DATE", By.xpath("(//android.widget.TextView[@text='Hari Ini' or @text='Today']//following-sibling::*)[last()]"));
    public static final Locator LAST_DATE = new Locator("LAST_DATE", By.xpath("((//*[contains(@resource-id,'tds_days_rv_calendar')])[1]//*[contains(@resource-id,'ll_day_group')]//android.widget.TextView)[last()]"));
    public static final Locator ISSUED_START_DATE = new Locator("ISSUED_START_DATE", By.id("tds_calendar_start_date"));
    public static final Locator NEXT_MONTH_BUTTON = new Locator("NEXT_MONTH_BUTTON", By.xpath("//*[contains(@resource-id,'ll_next_month')]"));
    public static final Locator CHECK_AVAILABILITY_BUTTON = new Locator("CHECK_AVAILABILITY_BUTTON", By.xpath("//*[@text='Check availability' or @text='Cek ketersediaan']"));
    public static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", By.id("tds_btn"));
    private static final Locator CALENDAR_FIRST_DATE = new Locator("CALENDAR_FIRST_DATE", By.xpath("(//*[contains(@resource-id,'ll_day_group')]//android.widget.TextView)[2]"));
    private static final Locator CALENDAR_LAST_DATE = new Locator("CALENDAR_LAST_DATE", By.xpath("((//*[contains(@resource-id,'ll_day_group')][last()])//android.widget.TextView[last()])[1]"));
    public static final Locator CALENDAR_SCROLLABLE = new Locator("CALENDAR_SCROLLABLE",AppiumBy.id("tds_calendar_month_rv"));
    private static final Locator CHECK_IN_VIEW_VALUE = new Locator("CHECK_IN_VIEW_VALUE", AppiumBy.xpath("//android.widget.TextView[@text='Select New Dates' or @text='Pilih Tanggal Baru']/following::android.view.View[1]/android.widget.TextView"));
    private static final Locator BACK_BUTTON = new Locator("BACK_BUTTON", AppiumBy.id("iv_toolbar_left_button"));
    private static final Locator RESCHEDULE_NOT_ALLOWED = new Locator("RESCHEDULE_NOT_ALLOWED",AppiumBy.id("iv_reschedule_not_allowed"));
    private static final Locator TERM_CONDITIONS_CHECKBOX = new Locator("TERM_CONDITIONS_CHECKBOX", AppiumBy.xpath("//*[@class='android.widget.CheckBox']"));

    @Override
    public String chooseNewDate(boolean changeDate) {
        int newStartDate;
        if (changeDate) {
            clickCheckIn();
            driverTools.swipeToElement(TODAYS_DATE, Swipe.DOWN, 4);
            driverTools.waitForElementToAppear(LAST_DATE,15);

            int dateNow = Integer.parseInt(driverTools.getText(TODAYS_DATE).orElseThrow());
            int dateLast = Integer.parseInt(driverTools.getText(LAST_DATE).orElseThrow());

            int issuedStartDate = Integer.parseInt(driverTools.getText(ISSUED_START_DATE).orElseThrow().split(" ")[0]);

            boolean isNextMonth = false;
            if (dateLast - dateNow < 3) {
                driverTools.swipeToElement(getDateLocator(issuedStartDate), Swipe.UP, 4);
                isNextMonth = true;
            }

            dateLast = Integer.parseInt(driverTools.getText(LAST_DATE).orElseThrow());
            newStartDate = issuedStartDate;
            int count = 0;

            while (newStartDate == issuedStartDate && count < 5) {
                newStartDate = !isNextMonth ? generateRandomInt(dateNow, dateLast - 1) : generateRandomInt(1, dateLast);
                count++;
            }

            clickDate(newStartDate);

            int newUntilDate = generateRandomInt(newStartDate + 2, dateLast);
            clickDate(newUntilDate);

            driverTools.click(SAVE_BUTTON);
        } else {
            driverTools.swipeToPresentedElement(CHECK_IN_VIEW_VALUE, Swipe.UP, 5);
            driverTools.waitForElementToAppear(CHECK_IN_VIEW_VALUE);
            String dateRange = driverTools.getText(CHECK_IN_VIEW_VALUE).orElseThrow();
            newStartDate = CoreFunctions.getIntFromString(dateRange.split("-")[0]).orElseThrow();
        }
        clickSubmitSearch();
        logger.log("newStartDate: " + newStartDate);
        return Integer.toString(newStartDate);
    }

    @Override
    public void clickSubmitSearch() {
        if (!driverTools.isElementDisplayed(CHECK_AVAILABILITY_BUTTON)) {
            driverTools.swipeToPresentedElement(CHECK_AVAILABILITY_BUTTON, Swipe.UP, 3);
        }
        driverTools.click(CHECK_AVAILABILITY_BUTTON);
    }

    private void goToDate(int date, Swipe swipe) {
        driverTools.swipeToElement(getDateLocator(date), swipe, 3).orElseThrow();
    }

    private void clickDate(int newStartDate) {
        WebElement newStartDateElement = driverTools.swipeToElement(getDateLocator(newStartDate), Swipe.UP, 3).orElseThrow();
        driverTools.click(newStartDateElement);
    }

    private Locator getDateLocator(int newStartDate) {
        return new Locator("DATE_" + newStartDate, By.xpath("//*[contains(@resource-id,'ll_day_group') and @clickable='true']//android.widget.TextView[@text='" + newStartDate + "']"));
    }

    private void clickNextMonth() {
        driverTools.click(NEXT_MONTH_BUTTON);
    }

    private void clickCheckIn() {
        driverTools.waitForElementToAppear(CHECK_IN_HEADER);
        driverTools.click(CHECK_IN_HEADER);
        if(driverTools.isElementDisplayed(CHECK_IN_HEADER)){
            driverTools.click(CHECK_IN_HEADER);
        }
    }

    public String changeNightDuration(String nightDuration) {
        clickCheckIn();
        driverTools.waitForElementToAppear(LAST_DATE);

        int issuedStartDate = Integer.parseInt(driverTools.getText(ISSUED_START_DATE).orElseThrow().split(" ")[0]);
        int newStartDate = issuedStartDate - 1;
        if (newStartDate < 1)
            newStartDate = Integer.parseInt(driverTools.getText(CALENDAR_FIRST_DATE).orElseThrow());
        goToDate(newStartDate, Swipe.DOWN);
        clickDate(newStartDate);

        int newUntilDate = newStartDate + Integer.parseInt(nightDuration);
        int calendarLastDate = Integer.parseInt(driverTools.getText(CALENDAR_LAST_DATE).orElseThrow());
        if (calendarLastDate < newUntilDate){
            driverTools.swipeUp();
            newUntilDate = 1;
        }
        logger.log("new until date: " + newUntilDate);
        goToDate(newUntilDate, Swipe.UP);
        clickDate(newUntilDate);

        driverTools.click(SAVE_BUTTON);
        clickSubmitSearch();

        String newStartDateString = Integer.toString(newStartDate);
        if (newStartDateString.length() == 1)
            newStartDateString = "0" + newStartDateString;
        logger.log("newStartDateString: " + newStartDateString);
        return newStartDateString;
    }

    @Override
    public String changeDate(String stayDate,int night) {
        if(night==0){
            clickSubmitSearch();
            return null;
        }
        clickCheckIn();
        logger.log("Stay date starting on: "+ stayDate);
        clickDate(stayDate);
        String checkoutDate = CoreFunctions.addToLongDate(stayDate,night);
        logger.log("Checkout date on: "+ checkoutDate);
        clickDate(checkoutDate);
        driverTools.click(SAVE_BUTTON);
        clickSubmitSearch();
        return stayDate;
    }

    private void clickDate(String date){
        Swipe direction = getDirection(date);
        logger.log("direction : " + direction);

        String year = CoreFunctions.getSectionOfString(date," ", 2);
        String monthEng = CoreFunctions.getSectionOfString(date," ", 1);
        String monthId = CoreFunctions.monthEngToId(monthEng);
        String day= CoreFunctions.getSectionOfString(date," ", 0);
        Locator dateLocator = getDateLocator(monthId,monthEng,day);

        for(int i = 10; i > 0; i--){
            if(driverTools.waitForElementToAppear(dateLocator,1).isPresent()){
                driverTools.click(dateLocator);
                break;
            }
            if(direction == Swipe.UP)
                driverTools.swipeUp(CALENDAR_SCROLLABLE);
            else
                driverTools.swipeDown(CALENDAR_SCROLLABLE);
        }
    }

    private Swipe getDirection(String dateToSelect){
        WebElement selectedDateElement = driverTools.waitForElementToAppear(ISSUED_START_DATE).orElseThrow();
        String selectedDate = selectedDateElement.getText();

        logger.log("date to select : " + dateToSelect);
        logger.log("selected date : " + selectedDate);

        String[] dateToSelectArr = dateToSelect.split(" ");
        int dateToSelectDay = Integer.parseInt(dateToSelectArr[0]);
        String dateToSelectMonth = dateToSelectArr[1];
        int dateToSelectYear = Integer.parseInt(dateToSelectArr[2]);

        String[] selectedDateArr = selectedDate.split(" ");
        int selectedDateDay = Integer.parseInt(selectedDateArr[0]);
        String selectedDateMonth = selectedDateArr[1];
        int SelectedDateYear = Integer.parseInt(selectedDateArr[2]);

        int selectedMonthNum = CoreFunctions.monthToMonthNumber(selectedDateMonth);
        logger.log("selected month number: " + selectedMonthNum);
        int dateToSelectMonthNum = CoreFunctions.monthToMonthNumber(dateToSelectMonth);
        logger.log("date to select month number: " + dateToSelectMonthNum);

        int yearDif = dateToSelectYear - SelectedDateYear;
        if(yearDif == 0){
            int monthDif = dateToSelectMonthNum - selectedMonthNum;
            if(monthDif == 0){
                return dateToSelectDay - selectedDateDay < 0 ? Swipe.DOWN: Swipe.UP;
            }
            return monthDif < 0 ? Swipe.DOWN : Swipe.UP;
        }
        return yearDif < 0 ? Swipe.DOWN : Swipe.UP;
    }

    private Locator getDateLocator(String monthId, String monthEng, String day){
        return new Locator("Date Locator", AppiumBy.xpath("//*[contains(@resource-id,'month_name') and (contains(@text,'"+monthId+"') or contains(@text,'"+monthEng+"'))]//following-sibling::*//*[contains(@resource-id,'day')]//*[@text='"+day+"']"));
    }

    @Override
    public boolean verifyRescheduleNotAllowedPresent() {
       return driverTools.waitForElementToAppear(RESCHEDULE_NOT_ALLOWED,15).isPresent();
    }

    @Override
    public void clickTNCCheckboxNative() {
        driverTools.waitForElementToAppear(TERM_CONDITIONS_CHECKBOX);
        driverTools.click(TERM_CONDITIONS_CHECKBOX);
    }
}
