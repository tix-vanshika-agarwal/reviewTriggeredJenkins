package com.tiket.web.dashboard.page.nft.airport_transfer;

import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import com.tiket.web.dashboard.page.member_dashboard.MemberLoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.List;
import java.util.Collections;

public class AirportTransferFleetPage<T extends WebDriver> extends BasePage<T> {
    private static final Locator USERNAME_GREETING = new Locator("USERNAME_GREETING", new By.ByXPath("//div[contains(@class, 'header_dashboard')]"));
    private static final Locator ADD_FLEET_BUTTON = new Locator("ADD_FLEET_BUTTON", new By.ByXPath("(//button[@type='button'])[2]"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", new By.ByXPath("(//button[@type='button'])"));
    private static final Locator SNACKBAR = new Locator("SNACKBAR", new By.ByXPath("(//div[@class='ant-notification-notice ant-notification-notice-success ant-notification-notice-closable'])[1]"));
    private static final Locator DROPDOWN = new Locator("DROPDOWN", new By.ByXPath("(//div[@class='dropdown-select placeholder'])[1]"));
    private static final Locator DROPDOWN_CAR_MODEL_EDIT = new Locator("DROPDOWN", new By.ByXPath("(//div[@class='dropdown-select '])[1]"));
    private static final Locator FIRST_DROPDOWN_OPTION = new Locator("FIRST_DROPDOWN_OPTION", new By.ByXPath("(//div[contains(@class,'tix-dashboard-select__option')])[1]"));
    private static final Locator FIRST_DROPDOWN_PACKAGE_OPTION = new Locator("FIRST_DROPDOWN_PACKAGE_OPTION", new By.ByXPath("(//div[contains(@class,'react-select-multi-promo__menu')]//div[contains(@class,'react-select-multi-promo__option')])[1]"));
    private static final Locator DROPDOWN_OPTIONS = new Locator("DROPDOWN_OPTIONS", new By.ByXPath("//div[contains(@class,'tix-dashboard-select__option')]") );
    private static final Locator DROPDOWN_INPUT = new Locator("DROPDOWN_INPUT", new By.ByXPath("//input[contains(@class,'tix-dashboard-select__input')]"));
    private static final Locator DROPDOWN_INDICATOR = new Locator("DROPDOWN_INDICATOR", new By.ByXPath("//div[contains(@class,'tix-dashboard-select__indicators')]"));
    private static final Locator CAR_MODEL_INPUT = new Locator("CAR_MODEL_INPUT", new By.ByXPath("//input[@name='car-model']"));
    private static final Locator RESULT_TABLE = new Locator("RESULT_TABLE", new By.ByXPath("//div[@class='ant-table-wrapper table-v4']"));
    private static final Locator SEARCH_FIELD = new Locator("SEARCH_FIELD", new By.ByXPath("//input[@name='manageFleet']"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", new By.ByXPath("//button[normalize-space()='Search']"));
    private static final Locator EDIT_BUTTON = new Locator("EDIT_BUTTON", new By.ByXPath("//a[contains(@class,'action-item') and (normalize-space(text())='Edit' or normalize-space(text())='Ubah Armada')]"));
    private static final Locator ACTIVE_SWITCH = new Locator("ACTIVE_SWITCH", new By.ByXPath("(//button[@role='switch'])[1]"));
    private static final Locator ACCEPT_BUTTON = new Locator("ACCEPT_BUTTON", new By.ByXPath("(//button[normalize-space()='Setuju' or normalize-space()='Agree'])[1]"));
    private static final Locator ACTIVE_STATUS = new Locator("ACTIVE_STATUS", new By.ByXPath("(//td[@class='ant-table-cell'])[6]"));
    private static final Locator PACKAGE_DROPDOWN = new Locator("PACKAGE_DROPDOWN", new By.ByXPath("//div[contains(@class,'react-select-price') and .//div[normalize-space(.)='Select Package']]"));
    private static final Locator PRICE_INPUT = new Locator("PRICE_INPUT", new By.ByXPath("//input[@class='input-forms v4 price-text-input']"));
    private static final Locator EDIT_SAVE_BUTTON = new Locator("EDIT_SAVE_BUTTON", new By.ByXPath("(//button[@type='button'])[5]"));
    private static final Locator EDIT_CONFIRM_BUTTON = new Locator("EDIT_CONFIRM_BUTTON", new By.ByXPath("(//button[@type='button'])[7]"));
    private static final Locator EDIT_PRICE_BUTTON = new Locator("EDIT_PRICE_BUTTON", new By.ByXPath("//a[contains(@class,'action-item') and (normalize-space(text())='Edit Price' or normalize-space(text())='Ubah Harga')]"));
    private static final Locator DELETE_BUTTON = new Locator("DELETE_BUTTON", new By.ByXPath("//a[contains(@class,'action-item') and normalize-space(text())='Delete']"));
    private static final Locator SPINNER = new Locator("SPINNER", new By.ByXPath("//div[@class='ant-spin ant-spin-spinning']"));

    public AirportTransferFleetPage(Session<T> session) {
        super(session);
    }

    public void goToATPage(String username, String password) throws InterruptedException {
        driverTools.maximizeWindow();
        new MemberLoginPage<>(session).adminLoginViaCookie(username, password);

        if (Configs.ENVIRONMENT == Environment.GK) {
            driverTools.get("https://gatotkaca.tiket.com/admin/airport-transfer");
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driverTools.get("https://preprod.tiket.com/admin/airport-transfer");
        }
    }


    public boolean isAirportTransferLoggedIn() {
        driverTools.waitForElementToAppear(USERNAME_GREETING, 2);
        return (driverTools.isElementDisplayed(USERNAME_GREETING));
    }


    public void goToFleetTab() {
        if (Configs.ENVIRONMENT == Environment.GK) {
            driverTools.get("https://gatotkaca.tiket.com/admin/airport-transfer/manage-fleet");
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driverTools.get("https://preprod.tiket.com/admin/airport-transfer/manage-fleet");
        }
    }


    public void addFleet(){
        driverTools.clickWithJavascriptExecutor(ADD_FLEET_BUTTON);
        driverTools.waitForElementToBeClickable(SAVE_BUTTON);
        driverTools.clickWithJavascriptExecutor(SAVE_BUTTON);
    }

    public void addFleet(String business, String model){
        driverTools.clickWithJavascriptExecutor(ADD_FLEET_BUTTON);
        driverTools.sendKeys(CAR_MODEL_INPUT, model);
        // had to use same dropdown since after filling 1, the locator will automatically point to the next dropdown
        selectDropdownOptionByText(DROPDOWN, business);

        selectFirstDropdownOption(DROPDOWN, FIRST_DROPDOWN_OPTION, DROPDOWN_INPUT, DROPDOWN_INDICATOR);

        selectFirstDropdownOption(DROPDOWN, FIRST_DROPDOWN_OPTION, DROPDOWN_INPUT, DROPDOWN_INDICATOR);

        driverTools.clickWithJavascriptExecutor(SAVE_BUTTON);
    }

    public boolean verifySuccessSnackbarIsDisplayed(){
        return driverTools.isElementDisplayed(SNACKBAR);
    }

    public boolean verifySuccessSnackbarContainsText(String expectedText){
        WebElement modal = driverTools.waitForElementToAppear(SNACKBAR).get();
        return modal.getText().contains(expectedText);
    }

    public boolean verifySRPContainsFleet(String name) {
        driverTools.waitForElementToAppear(EDIT_BUTTON);
        List<WebElement> fleetResults = driverTools.elements(RESULT_TABLE).orElseThrow();
        for (WebElement e : fleetResults) {
            String fleetNameValue = driverTools.getText(e);
            logger.log("Fleet name value : " + fleetNameValue);
            if (!fleetNameValue.contains(name)) {
                return false;
            }
        }
        return true;
    }

    public void scrollTable(){
        if (driverTools.isElementDisplayed(SPINNER)){
            driverTools.waitForElementToDisappear(SPINNER);
        }
        driverTools.clickWithJavascriptExecutor(RESULT_TABLE);
        Actions action = new Actions(driver);
        for (int i = 0; i < 30; i++){
            action.sendKeys(Keys.ARROW_RIGHT);
            action.perform();
        }
        driverTools.clickWithJavascriptExecutor(RESULT_TABLE);
    }

    public void toggleActiveFleet(){
        if(driverTools.isElementDisplayed(SNACKBAR)){
            driverTools.waitForElementToDisappear(SNACKBAR,10);
        }

        WebElement activateSwitch = driverTools.waitForElementToAppear(ACTIVE_SWITCH).get();
        driverTools.clickWithJavascriptExecutor(activateSwitch);
        WebElement acceptButton = driverTools.waitForElementToAppear(ACCEPT_BUTTON).get();
        driverTools.clickWithJavascriptExecutor(acceptButton);
    }

    public boolean verifyStatusIsChanged(String expectedStatus){
        if(driverTools.isElementDisplayed(SNACKBAR)){
            driverTools.waitForElementToDisappear(SNACKBAR,10);
        }
        WebElement status = driverTools.waitForElementToAppear(ACTIVE_STATUS).get();
        return status.getText().contains(String.format(expectedStatus));
    }

    public boolean verifySuccessSnackbarDisplayed(String expectedText){
        WebElement modal = driverTools.waitForElementToAppear(SNACKBAR).get();
        return modal.getText().contains(expectedText);
    }

    public void searchFleetByBusinessName(String name){
        driverTools.waitForElementToAppear(SEARCH_FIELD);
        driverTools.sendKeys(SEARCH_FIELD, name);
        driverTools.clickWithJavascriptExecutor(SEARCH_BUTTON);
    }

    public void editFleet(String model){
        driverTools.waitForElementToAppear(EDIT_BUTTON).ifPresent(driverTools::clickWithJavascriptExecutor);
        driverTools.clickWithJavascriptExecutor(CAR_MODEL_INPUT);
        driverTools.sendKeys(CAR_MODEL_INPUT, model);

        selectFirstDropdownOption(DROPDOWN_CAR_MODEL_EDIT, FIRST_DROPDOWN_OPTION, DROPDOWN_INPUT, DROPDOWN_INDICATOR);

        driverTools.clickWithJavascriptExecutor(SAVE_BUTTON);
    }

    public void editPrice(String price){
        driverTools.waitForElementsToAppear(EDIT_PRICE_BUTTON);
        driverTools.firstElement(EDIT_PRICE_BUTTON);
        driverTools.clickWithJavascriptExecutor(EDIT_PRICE_BUTTON);

        driverTools.waitForElementToAppear(PACKAGE_DROPDOWN);
        driverTools.click(PACKAGE_DROPDOWN);
        driverTools.click(FIRST_DROPDOWN_PACKAGE_OPTION);

        List<WebElement> priceInputs = driverTools.elements(PRICE_INPUT).get();
        for (WebElement input: priceInputs){
            driverTools.clickWithJavascriptExecutor(input);
            driver.switchTo().activeElement().sendKeys(price);
        }

        driverTools.clickWithJavascriptExecutor(EDIT_SAVE_BUTTON);
        driverTools.waitForElementToAppear(EDIT_CONFIRM_BUTTON);
        driverTools.clickWithJavascriptExecutor(EDIT_CONFIRM_BUTTON);
    }

    public void selectFirstDropdownOption(Locator dropdown, Locator firstOption, Locator inputLocator, Locator indicatorLocator) {
        try {
            driverTools.clickWithJavascriptExecutor(dropdown);
            if (driverTools.waitForElementToAppear(firstOption, 2).isPresent()) {
                WebElement first = driverTools.waitForElementToAppear(firstOption).get();
                driverTools.click(first);
                return;
            }
        } catch (Exception e) {
            logger.log("click dropdown container failed: " + e.getMessage());
        }

        try {
            driverTools.clickWithJavascriptExecutor(indicatorLocator);
            if (driverTools.waitForElementToAppear(firstOption, 2).isPresent()) {
                WebElement first = driverTools.waitForElementToAppear(firstOption).get();
                driverTools.click(first);
                return;
            }
        } catch (Exception e) {
            logger.log("click dropdown indicator failed: " + e.getMessage());
        }

        try {
            WebElement input = driverTools.waitForElementToAppear(inputLocator).get();
            driverTools.clickWithJavascriptExecutor(input);
            driverTools.sendKeys(input, Keys.ARROW_DOWN.toString());
            driverTools.sendKeys(input, Keys.RETURN.toString());
        } catch (Exception e) {
            logger.log("keyboard select failed: " + e.getMessage());
        }
    }

    public void selectDropdownOptionByText(Locator dropdown, String text) {
        driverTools.clickWithJavascriptExecutor(dropdown);
        WebElement input = driverTools.waitForElementToAppear(DROPDOWN_INPUT).get();
        driverTools.sendKeys(input, text);
        long endTime = System.currentTimeMillis() + 5000;
        while (System.currentTimeMillis() < endTime) {
            try {
                List<WebElement> options = driverTools.elements(DROPDOWN_OPTIONS).orElse(Collections.emptyList());
                for (WebElement opt : options) {
                    if (!opt.isDisplayed()) continue;
                    String optText = driverTools.getText(opt);
                    if (optText != null && optText.toLowerCase().contains(text.toLowerCase())) {
                        driverTools.clickWithJavascriptExecutor(opt);
                        return;
                    }
                }
            } catch (Exception ignored) {
            }
        }
        driverTools.sendKeys(input, Keys.RETURN.toString());
    }

    public void deleteFleet() {
        driverTools.waitForElementsToAppear(DELETE_BUTTON);
        WebElement deleteButton = driverTools.firstElement(DELETE_BUTTON).get();
        driverTools.clickWithJavascriptExecutor(deleteButton);

        WebElement acceptButton = driverTools.waitForElementToAppear(ACCEPT_BUTTON).get();
        driverTools.clickWithJavascriptExecutor(acceptButton);
    }
}