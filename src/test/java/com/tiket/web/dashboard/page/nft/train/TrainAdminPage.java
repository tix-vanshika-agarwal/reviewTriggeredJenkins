package com.tiket.web.dashboard.page.nft.train;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class TrainAdminPage<T extends WebDriver> extends BasePage<T> {
    private static final Locator TRAIN_ADMIN_MENU = new Locator("TRAIN_ADMIN_MENU", By.xpath("//div[@class='menu-list' and contains(text(),'Train Admin')]"));
    private static final Locator START_TIME_PICKER = new Locator("START_TIME_PICKER", By.xpath("(//div[@class='input-datepicker-container'])[1]"));
    private static final Locator END_TIME_PICKER = new Locator("END_TIME_PICKER", By.xpath("(//div[@class='input-datepicker-container'])[2]"));
    private static final Locator CALENDAR_NEXT_BUTTON = new Locator("CALENDAR_NEXT_BUTTON", By.xpath("//i[contains(@class, 'tix-chevron-right')]"));
    private static final Locator HOURS_FIELD = new Locator("HOURS_FIELD", By.xpath("//input[@name='hour' and @tabindex='1']"));
    private static final Locator MINUTES_FIELD = new Locator("MINUTES_FIELD", By.xpath("//input[@name='minute' and @tabindex='1']"));
    private static final Locator SECONDS_FIELD = new Locator("SECONDS_FIELD", By.xpath("//input[@name='second' and @tabindex='1']"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", By.xpath("//img[@class='icon-save']"));
    private static final Locator SUCCESS_BANNER = new Locator("SUCCESS_BANNER", By.xpath("//p[contains(text(), 'Maintenance time saved')]"));
    private static final Locator EDIT_ICON = new Locator("EDIT_ICON", By.xpath("//img[@class='icon-edit']"));
    private static final Locator DONE_BUTTON = new Locator("DONE_BUTTON", By.xpath("//button[@class='done-button']"));

    public TrainAdminPage(Session<T> session) {
        super(session);
    }
    public void goToTrainAdminMenu() {
        driverTools.waitForElementToAppear(TRAIN_ADMIN_MENU);
        driverTools.click(TRAIN_ADMIN_MENU);
    }

    public void goToSubMenu(String subMenuName) {
        Locator subMenuLocator = new Locator("SUB_MENU", By.xpath(String.format("//div[@class='submenu-list' and contains(text(),'%s')]", subMenuName)));
        WebElement subMenu = driverTools.waitForElementToAppear(subMenuLocator).get();
        driverTools.click(subMenu);
    }

    public void setTrainMaintenance(String hours, String startMinutes, String endMinutes, String seconds){
        driverTools.click(EDIT_ICON);
        setTimePicker(START_TIME_PICKER, hours, startMinutes, seconds);
        setTimePicker(END_TIME_PICKER, hours, endMinutes, seconds);
        driverTools.click(SAVE_BUTTON);
    }

    private void setTimePicker(Locator timePicker, String hours, String minutes, String seconds){
        setTimeField(HOURS_FIELD, hours, timePicker);
        setTimeField(HOURS_FIELD, hours, timePicker);
        setTimeField(MINUTES_FIELD, minutes, timePicker);
        setTimeField(SECONDS_FIELD, seconds, timePicker);
    }
    private void setTimeField(Locator field, String value, Locator timePicker){
        driverTools.click(timePicker);
        driverTools.click(field);
        driverTools.sendKeys(field, Keys.COMMAND + "a");
        driverTools.sendKeys(field, value);
        driverTools.click(DONE_BUTTON);
    }

    public boolean verifyMaintenanceSet() {
        return driverTools.waitForElementToAppear(SUCCESS_BANNER).isPresent();
    }
}