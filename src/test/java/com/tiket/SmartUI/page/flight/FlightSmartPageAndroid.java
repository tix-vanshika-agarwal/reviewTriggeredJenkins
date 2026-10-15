package com.tiket.SmartUI.page.flight;

import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class FlightSmartPageAndroid <T extends WebDriver> extends FlightSmartPage<T> {
    public static final Locator CHECK_OTHER_FLIGHTS = new Locator("CHECK_OTHER_FLIGHTS", AppiumBy.xpath("//*[contains(@resource-id,'tds_button_view') and (contains(@text,'Check Other Flights') or contains(@text,'Lihat Penerbangan Lain'))]"));
    public static final Locator CHANGE_DATE = new Locator("CHANGE_DATE", AppiumBy.xpath("//*[contains(@resource-id,'tds_button_view') and (contains(@text,'Change') or contains(@text,'Ubah'))]"));
    public static final Locator BACK_BUTTON_BOOKING_FORM = new Locator("BACK_BUTTON_BOOKING_FORM", AppiumBy.id("tds_icon_navigation_toolbar"));
    public static final Locator CLOSE_BOTTOMSHEET = new Locator("CLOSE_BOTTOMSHEET", AppiumBy.id("iv_close"));
    public static final Locator BACK_BUTTON_SRP = new Locator("BACK_BUTTON_SRP", AppiumBy.xpath("(//*[contains(@resource-id,'tds_icon_navigation_toolbar')])[2]"));
    public static final Locator ADD_CARD_COMPOUND = new Locator("ADD_CARD_COMPOUND", AppiumBy.xpath("(//*[@text=\"Add\" or @text=\"Tambahkan\"])[1]"));
    public static final Locator CANCEL_CARD_COMPOUND = new Locator("CANCEL_CARD_COMPOUND", AppiumBy.xpath("(//*[@text=\"Cancel\" or @text=\"Batalkan\"])[1]"));
    public static final Locator DEPART_CALENDAR = new Locator("DEPART_CALENDAR",AppiumBy.id("tds_flight_depart_date"));
    public static final Locator CALENDAR_SCROLLABLE = new Locator("CALENDAR_SCROLLABLE",AppiumBy.id("tds_calendar_month_rv"));
    public static final Locator SELECTED_DATE = new Locator("SELECTED_DATE",AppiumBy.id("tds_calendar_start_date"));
    public static final Locator CLOSE_CALENDAR = new Locator("CLOSE_CALENDAR",AppiumBy.id("tds_calendar_close"));

    public FlightSmartPageAndroid(Session<T> session) {
        super(session);
    }
    @Override
    public void pressBackButtonBookingForm() {
        driverTools.click(BACK_BUTTON_BOOKING_FORM);
    }

    @Override
    public void pressBackButtonSRP() {
        driverTools.waitForElementToAppear(BACK_BUTTON_SRP);
        driverTools.click(BACK_BUTTON_SRP);
    }

    @Override
    public void checkOtherFlights() {
        driverTools.click(CHECK_OTHER_FLIGHTS);
    }

    @Override
    public void pressChangeDateButton() {
        driverTools.click(CHANGE_DATE);
    }

    @Override
    public void closeBottomSheet() {
        driverTools.click(CLOSE_BOTTOMSHEET);
    }

    @Override
    public void addCardCompound() {
        driverTools.swipeToElement(ADD_CARD_COMPOUND, Swipe.UP, 10);
        driverTools.click(ADD_CARD_COMPOUND);
    }

    @Override
    public void cancelCardCompound() {
        driverTools.click(CANCEL_CARD_COMPOUND);
    }

    @Override
    public void openAndSetCalendar(String departDate, String returnDate) {
        driverTools.click(DEPART_CALENDAR);
        clickDate(departDate);
        if(returnDate != null){
            clickDate(returnDate);
        }
    }

    @Override
    public void closeCalendar() {
        driverTools.click(CLOSE_CALENDAR);
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
        WebElement selectedDateElement = driverTools.waitForElementToAppear(SELECTED_DATE).orElseThrow();
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
}