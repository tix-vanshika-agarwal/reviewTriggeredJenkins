package com.tiket.web.dashboard.page.affiliate;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AffiliateFlightCommissionPage<T extends WebDriver> extends BasePage<T> {
    public AffiliateFlightCommissionPage(Session<T> session) {
        super(session);
    }

    private static final Locator ADD_NEW_COMMISSION_BUTTON = new Locator("ADD_NEW_COMMISSION_BUTTON", By.xpath("//button[normalize-space()='Add New Comission']"));
    private static final Locator AIRLINES_INPUT_BOX = new Locator("AIRLINES_INPUT_BOX", By.xpath("//input[@id='airlines']"));
    private static final Locator AIRLINE_JETSTAR_ASIA = new Locator("AIRLINE_JETSTAR_ASIA", By.xpath("//li[text()='Jetstar Asia']"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", By.xpath("//button[normalize-space()='Save']"));
    private static final Locator PREFERENCE_DROPDOWN = new Locator("PREFERENCE_DROPDOWN", By.xpath("//input[@placeholder='Preference']/ancestor::div[contains(@class,'styles_input_inner_wrapper')]//button"));
    private static final Locator PREFERENCE_OPTION_PERCENTAGE = new Locator("PREFERENCE_OPTION_PERCENTAGE", By.xpath("//li[normalize-space(text())='Percentage']"));
    private static final Locator COMMISSION_VALUE_INPUT = new Locator("COMMISSION_VALUE_INPUT", By.xpath("//input[@id='comission-value']"));
    private static final Locator DESCRIPTION_TEXTAREA = new Locator("DESCRIPTION_TEXTAREA", By.xpath("//textarea[@id='commission-description']"));
    private static final Locator COMMISSION_ADDED = new Locator("COMMISSION_ADDED", By.xpath("//div[contains(@class, 'Snackbar_text_content')]"));
    private static final Locator COMMISSION_VALUE = new Locator("COMMISSION_VALUE", By.xpath("//table//tbody/tr[3]/td[5]"));


    public void clickAddNewCommissionButton(){
        driverTools.waitForElementToAppear(ADD_NEW_COMMISSION_BUTTON, 4).orElseThrow();
        driverTools.click(ADD_NEW_COMMISSION_BUTTON);
    }

    public void fillCommissionConfig(String commissionValue, String descriptionInput) {
        driverTools.click(AIRLINES_INPUT_BOX);
        driverTools.waitForElementToPresence(AIRLINES_INPUT_BOX, 4).orElseThrow();
        driverTools.click(AIRLINES_INPUT_BOX);
        driverTools.click(AIRLINE_JETSTAR_ASIA);
        driverTools.click(PREFERENCE_DROPDOWN);
        driverTools.waitForElementToAppear(PREFERENCE_OPTION_PERCENTAGE, 4).orElseThrow();
        driverTools.click(PREFERENCE_OPTION_PERCENTAGE);
        driverTools.sendKeys(COMMISSION_VALUE_INPUT, commissionValue);
        driverTools.click(DESCRIPTION_TEXTAREA);
        driverTools.waitForElementToAppear(DESCRIPTION_TEXTAREA, 4).orElseThrow();
        driverTools.sendKeys(DESCRIPTION_TEXTAREA, descriptionInput);
        driverTools.click(SAVE_BUTTON);
    }

    public boolean isCommissionAdded(String expectedSnackbarText){
        driverTools.waitForElementToAppear(COMMISSION_ADDED, 2).orElseThrow();
        String snackbarText = driverTools.getText(COMMISSION_ADDED).orElseThrow();
        return driverTools.isElementPresent(COMMISSION_ADDED) && snackbarText.contains(expectedSnackbarText);
    }

    public String getCommissionValue(){
        driverTools.waitForElementToAppear(COMMISSION_VALUE, 4).orElseThrow();
        return driverTools.getText(COMMISSION_VALUE).orElseThrow();
    }
}