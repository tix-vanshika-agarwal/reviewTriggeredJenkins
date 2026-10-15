package com.tiket.web.dashboard.page.affiliate;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AffiliateHotelPartnerEdit<T extends WebDriver> extends BasePage<T> {
    public AffiliateHotelPartnerEdit(Session<T> session) {
        super(session);
    }

    private static final Locator SETTINGS_DROPDOWN = new Locator("SETTINGS_DROPDOWN", By.xpath("//input[@id='settings']"));
    private static final Locator EDIT_FINANCIAL_DETAILS = new Locator("EDIT_FINANCIAL_DETAILS", By.xpath("//div[span[text()='Edit Financial Details']]"));
    private static final Locator EDIT_CONTENT_DETAILS = new Locator("EDIT_CONTENT_DETAILS", By.xpath("//span[normalize-space()='Edit Content Details']"));
    private static final Locator ENTITY_RADIOBUTTON = new Locator("AGODA_PARTNER", By.xpath("//span[normalize-space()='Agoda']"));
    private static final Locator INVENTORY_CHECKBOX = new Locator("INVENTORY_CHECKBOX", By.xpath("//span[contains(@class, 'Check_children')]"));
    private static final Locator SAVE_BTN = new Locator("SAVE_BTN", By.xpath("//button[text()='Save']"));
    private static final Locator CONFIRM_SAVE_BTN = new Locator("CONFIRM_SAVE_BTN", By.xpath("(//button[text()='Save'])[2]"));
    private static final Locator SETUP_COMMISSION_BTN = new Locator("SETUP_COMMISSION_BTN", By.xpath("//div[contains(@class, 'cursor-pointer') and .//span[text()='Setup Commission']]"));
    private static final Locator ADD_NEW_COMMISSION_BTN = new Locator("ADD_NEW_COMMISSION_BTN", By.xpath("//button[normalize-space(text())='Add New Comission']"));
    private static final Locator DROPDOWN_ICON_BUTTON = new Locator("DROPDOWN_ICON_BUTTON", By.xpath("(//button[contains(@class, 'clickable_icon')])[2]"));
    private static final Locator PERCENTAGE_LIST_ITEM = new Locator("PERCENTAGE_LIST_ITEM", By.xpath("//li[contains(@class, 'cursor-pointer') and text()='Percentage']"));
    private static final Locator COMMISSION_VALUE_INPUT = new Locator("COMMISSION_VALUE_INPUT", By.id("comission-value"));
    private static final Locator COMMISSION_DESCRIPTION = new Locator("COMMISSION_DESCRIPTION", By.id("commission-description"));

    public void clickSettings(){
        driverTools.waitForElementToAppear(SETTINGS_DROPDOWN, 5);
        driverTools.click(SETTINGS_DROPDOWN);
    }

    public void clickEditFinancialDetails(){
        driverTools.waitForElementToBeClickable(EDIT_FINANCIAL_DETAILS,5);
        driverTools.click(EDIT_FINANCIAL_DETAILS);
    }

    public void clickEditContentDetails(){
        driverTools.scrollToElement(EDIT_CONTENT_DETAILS);
        driverTools.waitForElementToBeClickable(EDIT_CONTENT_DETAILS, 5);
        driverTools.click(EDIT_CONTENT_DETAILS);
    }

    public void editPartnerEntity(){
        driverTools.waitForElementToAppear(ENTITY_RADIOBUTTON,5);
        driverTools.click(ENTITY_RADIOBUTTON);
    }

    public void editInventory(){
        driverTools.waitForElementToAppear(INVENTORY_CHECKBOX,5);
        driverTools.click(INVENTORY_CHECKBOX);
    }

    public void saveUpdateHotelDetails(){
        driverTools.scrollToElement(SAVE_BTN);
        driverTools.waitForElementToAppear(SAVE_BTN, 5);
        driverTools.click(SAVE_BTN);

        driverTools.waitForElementToAppear(CONFIRM_SAVE_BTN, 5);
        driverTools.click(CONFIRM_SAVE_BTN);
    }

    public void addNewCommission(String commissionValue, String commissionDescription){

        driverTools.scrollToElement(SETUP_COMMISSION_BTN);
        driverTools.click(SETUP_COMMISSION_BTN);
        driverTools.click(ADD_NEW_COMMISSION_BTN);
        driverTools.waitForElementToAppear(DROPDOWN_ICON_BUTTON, 5);
        driverTools.click(DROPDOWN_ICON_BUTTON);
        driverTools.waitForElementToAppear(PERCENTAGE_LIST_ITEM, 5);
        driverTools.click(PERCENTAGE_LIST_ITEM);
        driverTools.waitForElementToPresence(COMMISSION_VALUE_INPUT,5);
        driverTools.sendKeys(COMMISSION_VALUE_INPUT, commissionValue);
        driverTools.sendKeys(COMMISSION_DESCRIPTION, commissionDescription);

        driverTools.waitForElementToAppear(SAVE_BTN, 5);
        driverTools.click(SAVE_BTN);
    }
}