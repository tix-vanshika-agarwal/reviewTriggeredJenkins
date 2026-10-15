package com.tiket.web.dashboard.page.ttd.CMS.CMSProductManagementSchedulePackagePage;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.time.LocalDate;

public class CMSProductManagementSchedulePackagePageWeb<T extends WebDriver> extends WebPage<T> {
    public CMSProductManagementSchedulePackagePageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> CMSProductManagementSchedulePackagePageWeb<?> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new CMSProductManagementSchedulePackagePageDweb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new CMSProductManagementSchedulePackagePageMweb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }
    private static final Locator UPDATE_SCHEDULE_BUTTON = new Locator("UPDATE_SCHEDULE_BUTTON", By.xpath("//div[@class='action-menu-schedule']/child::div[2]"));
    private static final Locator ADD_SCHEDULE_BUTTON = new Locator("ADD_SCHEDULE_BUTTON", By.xpath("//div[@class='menu-update-schedule']/a"));
    private static final Locator DATE_RANGE_SELECTION = new Locator("DATE_RANGE_SELECTION", By.xpath("//div[@role='radiogroup']/label[1]"));
    private static final Locator INDIVIDUAL_DATE_SELECTION = new Locator("INDIVIDUAL_DATE_SELECTION", By.xpath("//div[@role='radiogroup']/label[2]"));
    private static final Locator SUBMIT_BUTTON = new Locator("SUBMIT_BUTTON", By.xpath("//button[contains(@class,'waves-default')]"));
    private static final Locator PUBLISH_SCHEDULE = new Locator("PUBLISH_SCHEDULE", By.xpath("//div[@id='modal-root']//button[contains(@class,'waves-default')]"));
    private static final Locator SUCCESS_SNACKBAR = new Locator("SUCCESS_SNACKBAR", By.xpath("//div[@class='nav-bar-alert alert alert-success fade show']"));
    private static final Locator NO_SEATMAP_SCHEDULE = new Locator("NO_SEATMAP_SCHEDULE", By.xpath("//div[@id='dialog-validation-no-schedule']//div[@class='dialog-popup']"));
    private static final Locator SUBMIT_DELETE = new Locator("SUBMIT_DELETE", By.xpath("//div[@class='modal-footer']//button[@type='error']"));
    private static final Locator CANCEL_BUTTON = new Locator("CANCEL_BUTTON", By.xpath("//button[contains(@class,'waves-secondary')]"));
    private static final Locator NO_SCHEDULE_OKAY_BUTTON = new Locator("NO_SCHEDULE_OKAY_BUTTON", By.xpath("//div[@id='dialog-validation-no-schedule']//button"));

    public void clickSeatMapByRowIndex(int index) {
        Locator locator = getSeatMapButtonByIndexRow(index);
        driverTools.waitForElementToAppear(locator, 3);
        driverTools.click(locator);
    }

    private Locator getSeatMapButtonByIndexRow(int index) {
        return new Locator("SEAT_MAP_BUTTON_"+index, By.xpath("//tbody/tr["+index+"]//div[@class='action-icon']/child::div[1]"));
    }

    public void changeWindowPointing() {
        driverTools.closeCurrentTab();
        driverTools.switchToWindowContainingUrl("product-management/schedule-manange");
    }

    public void clickSeatMapByScheduleDate(String date) {
        Locator locator = getSeatMapButtonByScheduleDate(date);
        driverTools.waitForElementToAppear(locator, 3);
        driverTools.click(locator);
    }

    private Locator getSeatMapButtonByScheduleDate(String date) {
        return new Locator("SEAT_MAP_BUTTON_"+date, By.xpath("(//tbody/tr[.//div[contains(text(),'"+date+"')]]//div[@class='action-icon']/child::div[1])[1]"));
    }

    public void clickUpdateSchedule() {
        driverTools.waitForElementToAppear(UPDATE_SCHEDULE_BUTTON);
        driverTools.click(UPDATE_SCHEDULE_BUTTON);
    }

    public void clickAddSchedule() {
        driverTools.waitForElementToAppear(ADD_SCHEDULE_BUTTON);
        driverTools.click(ADD_SCHEDULE_BUTTON);
    }

    public void changeDateInputTypeNewSchedule(String dateSelectionMode) {
        switch (dateSelectionMode.toLowerCase()) {
            case "daterange" -> {
                driverTools.click(DATE_RANGE_SELECTION);
            }
            case "individual" -> {
                driverTools.click(INDIVIDUAL_DATE_SELECTION);
            }
        }
    }

    public void selectIndividualDateNewSchedule(String date) {
        Locator locator = getDateLocatorBasedOnDateString(date);
        driverTools.scrollToElement(locator);
        driverTools.click(locator);
    }

    private Locator getDateLocatorBasedOnDateString(String date) {
        LocalDate parsedDate = CoreFunctions.parseDateStringToLocalDate(date);
        int monthDiffFromCurrentMonth = CoreFunctions.getMonthDifference(LocalDate.now(), parsedDate);
        String[] dateSplit = date.split("-");
        String newDateNumber = String.valueOf(Integer.parseInt(dateSplit[2]));
        return new Locator("INDIVIDUAL_DATE_"+date, By.xpath("//div[@class='wrapper-calender row']/div["+(monthDiffFromCurrentMonth+1)+"]//div[@class='cell-days' and text()='"+newDateNumber+"']"));
    }

    public void clickSubmitButtonNewSchedule() {
        driverTools.scrollToElement(SUBMIT_BUTTON);
        driverTools.click(SUBMIT_BUTTON);
        driverTools.click(PUBLISH_SCHEDULE);
    }

    public boolean isSuccessNotifBarVisible() {
        return driverTools.waitForElementToAppear(SUCCESS_SNACKBAR).isPresent();
    }

    public boolean isScheduleWithSeatMapCreated(String date) {
        String newDate = convertDateFormat(date);

        Locator locator = getScheduleSeatOptionLocatorByDate(newDate);
        driverTools.click(locator);

        boolean result = driverTools.waitForElementToAppear(NO_SEATMAP_SCHEDULE).isEmpty();
        if (result) {
            driverTools.click(CANCEL_BUTTON);
        } else {
            driverTools.click(NO_SCHEDULE_OKAY_BUTTON);
        }

        return result;
    }

    private String convertDateFormat(String date) {
        String[] dateSplit = date.split("-");
        String dateNumber = dateSplit[2];
        String month = CoreFunctions.monthLongToShortEn(CoreFunctions.getMonthName(Integer.parseInt(dateSplit[1]), "en"));
        String year = String.valueOf(Integer.parseInt(dateSplit[0]) % 1000);
        return dateNumber + " " + month + " " + year;
    }

    private Locator getScheduleSeatOptionLocatorByDate(String date) {
        return new Locator("SCHEDULE_SEAT_"+date, By.xpath("//tbody/tr[./td[1]/div[text()='"+date+"']]/td[last()]//div[@class='icon' and ./span[text()='Seat Map']]"));
    }

    public void deleteSchedule(String date) {
        String newDate = convertDateFormat(date);

        Locator locator = getScheduleDeleteOptionLocatorByDate(newDate);
        driverTools.click(locator);

        driverTools.click(SUBMIT_DELETE);
    }

    private Locator getScheduleDeleteOptionLocatorByDate(String date) {
        return new Locator("SCHEDULE_DELETE_"+date, By.xpath("//tbody/tr[./td[1]/div[text()='"+date+"']]/td[last()]//div[@class='icon' and ./span[text()='Delete']]"));
    }
}
