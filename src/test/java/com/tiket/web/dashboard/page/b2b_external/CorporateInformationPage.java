package com.tiket.web.dashboard.page.b2b_external;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CorporateInformationPage <T extends WebDriver> extends BasePage<T> {

    private static final Locator CORPORATE_INFORMATION_HEADING =  new Locator("CORPORATE_INFORMATION_HEADING", new By.ByXPath("//div[contains(@class,'w-full pl-7')]//span[text()='Corporate Information']"));
    private static final Locator ADD_CODE =  new Locator("ADD_CODE", new By.ByXPath("//button[normalize-space()='Add a new code'] | //button[contains(text(), 'Add a New Code')]"));
    private static final Locator SELECT_AIRLINE_DROPDOWN =  new Locator("SELECT_AIRLINE_DROPDOWN", new By.ByXPath("//div[@class='relative bg-white']"));
    private static final Locator SELECT_AIRLINE_PELITA =  new Locator("SELECT_AIRLINE_PELITA", new By.ByXPath("//li[normalize-space()='Pelita Air']"));
    private static final Locator ENTER_CODE =  new Locator("ENTER_CODE", new By.ByXPath("//input[@id='enter-the-code']"));
    private static final Locator PELITA_CODE_BUTTON =  new Locator("PELITA_CODE_BUTTON", new By.ByXPath("//div[text()='Pelita Air']"));
    private static final Locator DELETE_CODE =  new Locator("DELETE_CODE", new By.ByXPath("//p[text()='Delete the code']"));
    private static final Locator DELETE_CONFIRMATION_POPUP_BUTTON =  new Locator("DELETE_CONFIRMATION_POPUP_BUTTON", new By.ByXPath("//button[text()='Delete']"));
    private static final Locator SNACKBAR_TEXT =  new Locator("SNACKBAR_TEXT", new By.ByXPath("//div[contains(@class,'Snackbar_text_content')]"));
    private static final Locator SAVE =  new Locator("SAVE", new By.ByXPath("//button[normalize-space()='Save']"));


    public CorporateInformationPage(Session<T> session) {super(session);}

    public boolean isCorporateInformationPageOpen() {
        driverTools.waitForElementToAppear(CORPORATE_INFORMATION_HEADING, 5);
        return driverTools.isElementDisplayed(CORPORATE_INFORMATION_HEADING);
    }

    public void addCorporateCodePelita() {
        driverTools.waitForElementToDisappear(SNACKBAR_TEXT,3);
        driverTools.clickWithJavascriptExecutor(ADD_CODE);
        driverTools.click(SELECT_AIRLINE_DROPDOWN);
        driverTools.click(SELECT_AIRLINE_PELITA);
        driverTools.click(ENTER_CODE);
        driverTools.sendKeys(ENTER_CODE, CoreFunctions.randomName(4));
        driverTools.click(SAVE);
    }

    public String getSnackBarText() {
        driverTools.waitForElementToAppear(SNACKBAR_TEXT, 5);
       return driverTools.getText(SNACKBAR_TEXT).orElse("");
    }

    public void deleteCorporateCode(){
        driverTools.click(PELITA_CODE_BUTTON);

        driverTools.waitForElementToAppear(DELETE_CODE,2);
        driverTools.click(DELETE_CODE);

        driverTools.waitForElementToAppear(DELETE_CONFIRMATION_POPUP_BUTTON,2);
        driverTools.click(DELETE_CONFIRMATION_POPUP_BUTTON);
    }

    public boolean isPelitaCodeAdded(){
        driverTools.waitForElementToAppear(PELITA_CODE_BUTTON,2);
        return driverTools.isElementDisplayed(PELITA_CODE_BUTTON);
    }
}
