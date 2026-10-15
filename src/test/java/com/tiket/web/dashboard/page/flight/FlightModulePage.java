package com.tiket.web.dashboard.page.flight;

import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class FlightModulePage<T extends WebDriver> extends BasePage<T> {

    public FlightModulePage(Session<T> session) {
        super(session);
    }


    private static final Locator USERNAME_GREETING = new Locator("USERNAME_GREETING", new By.ByXPath("//div[@class='account-card']"));
    private static final Locator FILTER_TEXTFIELD = new Locator("FILTER_TEXTFIELD", new By.ById("custom-table__filter--module"));
    private static final Locator APPLY_FILTER_BTN = new Locator("APPLY_FILTER_BTN", new By.ByXPath("//button[contains(@class, 'custom-table__filter--submit')]"));
    private static final Locator MODULE_NAME_INPUT = new Locator("MODULE_NAME_INPUT", new By.ByName("general.moduleName"));
    private static final Locator SUBMIT_BTN = new Locator("SUBMIT_BTN", new By.ByCssSelector("button.general-form__submit"));

    // Dynamic Locator method to find the Edit button specifically for the passed module name
    private Locator getEditButtonLocator(String moduleName) {
        String xpath = String.format("//tr[contains(@class, 'MuiTableRow-root') and .//td[contains(text(), '%s')]]//div[@title='Edit']//button", moduleName);
        return new Locator("EDIT_BUTTON_FOR_" + moduleName, new By.ByXPath(xpath));
    }

    public void updateModuleName(String newName) {
        driverTools.waitForElementToAppear(MODULE_NAME_INPUT, 5);
        driverTools.clearInput(MODULE_NAME_INPUT);
        driverTools.sendKeys(MODULE_NAME_INPUT, newName);
        driverTools.scrollToElement(SUBMIT_BTN);
        driverTools.click(SUBMIT_BTN);
        driverTools.waitForElementToAppear(FILTER_TEXTFIELD, 5).orElseThrow();
    }

    public boolean isModulePresentInTable(String moduleName) {
        Locator moduleRow = getEditButtonLocator(moduleName);
        driverTools.waitForElementToAppear(moduleRow,5);
        return driverTools.isElementDisplayed(moduleRow);
    }


    public void goToFlightPageModule() {
        if (Configs.ENVIRONMENT == Environment.GK) {
            driverTools.get("https://gatotkaca.tiket.com/admin/flight-ms-v2/page-module");
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driverTools.get("https://preprod.tiket.com/admin/flight-ms-v2/page-module");
        } else if (Configs.ENVIRONMENT == Environment.PROD) {
            driverTools.get("https://www.tiket.com/admin/flight-ms-v2/page-module");
        }
    }

    public boolean isFlightLoggedIn() {
        driverTools.waitForElementToAppear(USERNAME_GREETING, 3);
        return driverTools.isElementDisplayed(USERNAME_GREETING);
    }

    public void searchModuleFilter(String moduleName) {
        driverTools.waitForElementToAppear(FILTER_TEXTFIELD, 5);
        driverTools.clearInput(FILTER_TEXTFIELD);
        driverTools.sendKeys(FILTER_TEXTFIELD, moduleName);
        driverTools.click(APPLY_FILTER_BTN);
    }

    public void clickEditButton(String moduleName) {
        Locator dynamicEditButton = getEditButtonLocator(moduleName);
        driverTools.waitForElementToAppear(dynamicEditButton, 5);
        driverTools.click(dynamicEditButton);
    }
}