package com.tiket.web.dashboard.page.hotel.extranet;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class TaxPage<T extends WebDriver> extends BasePage<T> {
    public TaxPage(Session<T> session){super(session);}

    private static final Locator ACTIVE_TAX_MENU_LABEL=new Locator("ACTIVE_TAX_MENU_LABEL",new By.ByXPath("//a[@class='nav-sub-list active' and @href='/v3/finance/tax']"));
    private static final Locator TAX_PAGE=new Locator("TAX_PAGE",new By.ByXPath("//div[@class='the-tax']"));
    private static final Locator EDIT_BUTTON=new Locator("EDIT_BUTTON",new By.ByXPath("//button[@class='the-button ghost edit-button']"));
    private static final Locator SERVICE_CHARGE_PERCENTAGE_INPUT=new Locator("SERVICE_CHARGE_PERCENTAGE_INPUT",new By.ByXPath("//input[@name='service_charge' and @class='input   ']"));
    private static final Locator SAVE_BUTTON=new Locator("SAVE_BUTTON",new By.ByXPath("//button[@class='the-button yellow button-submit']"));
    private static final Locator SUCCESS_TOAST=new Locator("SUCCESS_TOAST",new By.ByXPath("//div[@class='the-toast success opened']"));
    private static final Locator PROPOSED_NEW_SERVICE_CHARGE=new Locator("PROPOSED_NEW_SERVICE_CHARGE",new By.ByXPath("//div[@class='the-tax-detail-body']/div[5]/div[2]"));
    private static final Locator PROPOSED_NEW_OTHER_SERVICE_NAME=new Locator("PROPOSED_NEW_OTHER_SERVICE_NAME",new By.ByXPath("//div[@class='the-tax-detail-body']/div[6]/div[2]"));
    private static final Locator PROPOSED_NEW_OTHER_SERVICE_VALUE=new Locator("PROPOSED_NEW_OTHER_SERVICE_VALUE",new By.ByXPath("//div[@class='the-tax-detail-body']/div[7]/div[2]"));
    private static final Locator OTHER_SERVICE_NAME=new Locator("OTHER_SERVICE_NAME",new By.ByXPath("//div[@class='the-row'][7]"));
    private static final Locator OTHER_SERVICE_VALUE=new Locator("OTHER_SERVICE_VALUE",new By.ByXPath("//div[@class='the-row'][8]"));
    private static final Locator OTHER_SERVICE_NAME_INPUT=new Locator("OTHER_SERVICE_NAME_INPUT",new By.ByXPath("//input[@name='other_service_name']"));
    private static final Locator OTHER_SERVICE_VALUE_INPUT=new Locator("OTHER_SERVICE_VALUE_INPUT",new By.ByXPath("//input[@name='other_service_value']"));

    public String isTaxPageLoaded(){
        WebElement menu = driverTools.waitForElementToAppear(ACTIVE_TAX_MENU_LABEL).orElseThrow();
        driverTools.waitForElementToAppear(TAX_PAGE).orElseThrow();
        return driverTools.getValueAttribute(menu, "href");
    }

    public void clickEditButton(){
        driverTools.waitForElementToAppear(EDIT_BUTTON);
        driverTools.clickWithJavascriptExecutor(EDIT_BUTTON);
    }

    public boolean isTaxFormPageLoaded(){
        driverTools.waitForElementToAppear(TAX_PAGE);
        return driverTools.isElementDisplayed(TAX_PAGE);
    }

    public boolean isOtherServiceOptionsLoaded() {
        driverTools.waitForElementToAppear(OTHER_SERVICE_NAME);
        driverTools.waitForElementToAppear(OTHER_SERVICE_VALUE);
        return driverTools.isElementDisplayed(OTHER_SERVICE_NAME) && driverTools.isElementDisplayed(OTHER_SERVICE_VALUE);
    }

    public void enterServiceCharge(String text) {
        WebElement input = driverTools.waitForElementToAppear(SERVICE_CHARGE_PERCENTAGE_INPUT).orElseThrow();
        driverTools.clearInput(input);
        driverTools.sendKeys(input,text);
    }

    public void enterOtherServiceName(String text) {
        WebElement input = driverTools.waitForElementToAppear(OTHER_SERVICE_NAME_INPUT).orElseThrow();
        driverTools.clearInput(input);
        driverTools.sendKeys(input,text);
    }

    public void enterOtherServiceValue(String text) {
        WebElement input = driverTools.waitForElementToAppear(OTHER_SERVICE_VALUE_INPUT).orElseThrow();
        driverTools.clearInput(input);
        driverTools.sendKeys(input,String.valueOf(text));
    }

    public void clickSaveButton(){
        driverTools.waitForElementToAppear(SAVE_BUTTON);
        driverTools.clickWithJavascriptExecutor(SAVE_BUTTON);
    }

    public boolean isTaxUpdatedSuccessfullyToastLoaded(){
        driverTools.waitForElementToAppear(SUCCESS_TOAST);
        return driverTools.isElementDisplayed(SUCCESS_TOAST);
    }

    public String getProposedNewServiceCharge(){
        WebElement newCommission = driverTools.waitForElementToAppear(PROPOSED_NEW_SERVICE_CHARGE).orElseThrow();
        return driverTools.getText(newCommission);
    }

    public String getProposedNewOtherServiceName(){
        WebElement newCommission = driverTools.waitForElementToAppear(PROPOSED_NEW_OTHER_SERVICE_NAME).orElseThrow();
        return driverTools.getText(newCommission);
    }

    public String getProposedNewOtherServiceValue(){
        WebElement newCommission = driverTools.waitForElementToAppear(PROPOSED_NEW_OTHER_SERVICE_VALUE).orElseThrow();
        return driverTools.getText(newCommission);
    }

}
