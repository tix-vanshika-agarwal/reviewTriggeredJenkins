package com.tiket.web.dashboard.page.platform.eiffel.loyalty;

import com.google.common.collect.Iterables;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class DeletePointPage <T extends WebDriver> extends BasePage<T> {

    private static final Locator DELETE_PAGE = new Locator("DELETE_PAGE", new By.ByXPath("//span[text()='Delete Point']"));
    private static final Locator DROP_DOWN = new Locator("DROP_DOWN", new By.ByXPath("//input[@id='select-search-by']"));
    private static final Locator USER_NAME_POINT_TYPE = new Locator("USER_NAME_POINT_TYPE", new By.ByXPath("//p[text()='Username & Point Type']"));
    private static final Locator USER_NAME_TEXT_BOX = new Locator("USER_NAME_TEXT_BOX", new By.ByXPath("//input[@name='sb-text-input-username']"));
    private static final Locator SELECT_POINT_TYPE = new Locator("SELECT_POINT_TYPE", new By.ByXPath("//input[@id='select-point-type']"));
    private static final Locator REWARD = new Locator("REWARD", new By.ByXPath("//p[text()='REWARD']"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", new By.ByXPath("//button[normalize-space()='Search']"));
    private static final Locator DELETE_ICON = new Locator("DELETE_ICON", new By.ByXPath("//button[normalize-space()='Delete']"));
    private static final Locator PROCESS_TYPE = new Locator("PROCESS_TYPE", new By.ByXPath("//tbody[contains(@class,'Table_table_body')]//td[4]"));
    private static final Locator AMOUNT = new Locator("AMOUNT", new By.ByXPath("//tbody[contains(@class,'Table_table_body')]//td[6]"));
    private static final Locator SUBMIT_ICON = new Locator("SUBMIT_ICON", new By.ByXPath("//button[normalize-space()='Submit']"));
    private static final Locator NOTE_TEXT_AREA = new Locator("NOTE_TEXT_AREA", By.id("add-note"));
    private static final Locator SUCCESS_SNACKBAR = new Locator("SUCCESS_SNACKBAR", new By.ByXPath("//div[contains(text(),'SUCCESSFULLY SUBMIT DELETE')]"));
    private static final Locator TABLE_BODY = new Locator("TABLE_BODY", new By.ByXPath("//tbody[contains(@class,'Table_table_body')]"));

    public DeletePointPage(Session<T> session) {
        super(session);
    }

    public void openDeletePointsPage(){
        WebElement deletePage = driverTools.waitForElementToAppear(DELETE_PAGE).orElseThrow();
        driverTools.click(deletePage);
    }

    public void selectUserName(){
        WebElement dropDown = driverTools.waitForElementToAppear(DROP_DOWN).orElseThrow();
        driverTools.click(dropDown);
        WebElement userNamePointType = driverTools.waitForElementToAppear(USER_NAME_POINT_TYPE).orElseThrow();
        driverTools.click(userNamePointType);
    }

    public void enterUserName(String input) {
        driverTools.sendKeys(USER_NAME_TEXT_BOX, input);
    }

    public void selectPointType(){
        WebElement dropDown = driverTools.waitForElementToAppear(SELECT_POINT_TYPE).orElseThrow();
        driverTools.click(dropDown);
        WebElement userNamePointType = driverTools.waitForElementToAppear(REWARD).orElseThrow();
        driverTools.click(userNamePointType);
    }

    public void clickOnSearch(){
        WebElement searchButton = driverTools.waitForElementToAppear(SEARCH_BUTTON).orElseThrow();
        driverTools.click(searchButton);
        driverTools.waitForElementToAppear(TABLE_BODY, 10).orElseThrow(() -> new AssertionError(TABLE_BODY.name() + " not found after search"));
    }

    public boolean getProcessType(String input){
        WebElement processType = driverTools.waitForElementToAppear(PROCESS_TYPE,3).orElseThrow();
        return driverTools.getText(processType).equals(input);
    }

    public boolean Amount(String input){
        WebElement amount = driverTools.waitForElementToAppear(AMOUNT,3).orElseThrow();
        return driverTools.getText(amount).equals(input);
    }

    public void clickOnDeleteIcon(){
        List<WebElement> deleteIcons = driverTools.elements(DELETE_ICON).orElseThrow()
                .stream()
                .filter(el -> el.getAttribute("disabled") == null).toList();
        if (deleteIcons.isEmpty()) {
            throw new AssertionError("No enabled Delete buttons found — all entries may already be deleted or not yet loaded");
        }
        WebElement deleteIcon = Iterables.getLast(deleteIcons);
        driverTools.scrollElementToCenter(deleteIcon);
        driverTools.click(deleteIcon);
    }

    public void clickOnSubmitIcon(){
        WebElement submitButton = driverTools.waitForElementToAppear(SUBMIT_ICON).orElseThrow();
        driverTools.click(submitButton);
    }

    public void addNote(String note) {
        WebElement noteTextArea = driverTools.waitForElementToAppear(NOTE_TEXT_AREA).orElseThrow();
        driverTools.sendKeys(noteTextArea, note);
    }

    public boolean isSuccessSnackbarDisplayed() {
        return driverTools.waitForElementToAppear(SUCCESS_SNACKBAR, 10).isPresent();
    }

    public boolean isResultTableDisplayed() {
        return driverTools.waitForElementToAppear(TABLE_BODY, 5).isPresent();
    }
}
