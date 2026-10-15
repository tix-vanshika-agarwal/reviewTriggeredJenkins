package com.tiket.web.dashboard.page.hotel.extranet;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.NoSuchElementException;

public class ManageUsersPage<T extends WebDriver> extends BasePage<T> {
    public ManageUsersPage(Session<T> session){super(session);}

    private static final Locator MANAGE_USERS_PAGE=new Locator("MANAGE_USERS_PAGE", new By.ByXPath("//div[@class='the-manage-users']"));
    private static final Locator ADD_NEW_USER_BUTTON=new Locator("ADD_NEW_USER_BUTTON", new By.ByXPath("//button[@type='button' and contains(@class, 'add-button')]"));
    private static final Locator SAVE_USER_PAGE=new Locator("SAVE_USER_PAGE",new By.ByXPath("//div[@class= 'the-user-management-form']"));
    private static final Locator EMAIL_INPUT=new Locator("EMAIL_INPUT",new By.ByXPath("//input[@type='text' and @name='email' and @class='input   ']"));
    private static final Locator NAME_INPUT=new Locator("NAME_INPUT",new By.ByXPath("//input[@type='text' and @name='name' and @class='input   ']"));
    private static final Locator PHONENO_INPUT=new Locator("PHONENO_INPUT",new By.ByXPath("//input[@type='tel' and @name='phoneNumber' and @class='input   ']"));
    private static final Locator ADMINISTRATOR_RADIO_BUTTON=new Locator("ADMINISTRATOR_RADIO_BUTTON",new By.ByXPath("(//span[@class= 'radio-style  '])[1]"));
    private static final Locator FINANCE_RADIO_BUTTON=new Locator("FINANCE_RADIO_BUTTON",new By.ByXPath("(//span[@class= 'radio-style  '])[2]"));
    private static final Locator RESERVATION_RADIO_BUTTON=new Locator("RESERVATION_RADIO_BUTTON",new By.ByXPath("(//span[@class= 'radio-style  '])[3]"));
    private static final Locator YES_RADIO_BUTTON=new Locator("YES_RADIO_BUTTON",new By.ByXPath("(//span[@class= 'radio-style  '])[4]"));
    private static final Locator ADD_NEW_USER_BUTTON_AGAIN=new Locator("ADD_NEW_USER_BUTTON_AGAIN", new By.ByXPath("//button[@type='submit' and @name='submit' and contains(@class, 'the-button') and contains(@class, 'yellow')]"));
    private static final Locator ACCOUNT_PIN_POPUP=new Locator("ACCOUNT_PIN_POPUP", new By.ByXPath("//div[@class='dialog-popup']"));
    private static final Locator FIRST_PIN_INPUT=new Locator("FIRST_PIN_INPUT", new By.ByXPath("//input[@data-testid='input-otp-0' and @name='0']"));
    private static final Locator SECOND_PIN_INPUT=new Locator("SECOND_PIN_INPUT", new By.ByXPath("//input[@data-testid='input-otp-1' and @name='1']"));
    private static final Locator THIRD_PIN_INPUT=new Locator("THIRD_PIN_INPUT", new By.ByXPath("//input[@data-testid='input-otp-2' and @name='2']"));
    private static final Locator FOURTH_PIN_INPUT=new Locator("FOURTH_PIN_INPUT", new By.ByXPath("//input[@data-testid='input-otp-3' and @name='3']"));
    private static final Locator FIFTH_PIN_INPUT=new Locator("FIFTH_PIN_INPUT", new By.ByXPath("//input[@data-testid='input-otp-4' and @name='4']"));
    private static final Locator SIXTH_PIN_INPUT=new Locator("SIXTH_PIN_INPUT", new By.ByXPath("//input[@data-testid='input-otp-5' and @name='5']"));
    private static final Locator USER_ADDED_POPUP=new Locator("USER_ADDED_POPUP", new By.ByXPath("//div[@class='manage-user-success-modal-confirmation']"));
    private static final Locator GO_BACK_BUTTON=new Locator("GO_BACK_BUTTON", new By.ByXPath("//button[@class='the-button primary' and @id='button-success']"));
    private static final Locator USER_NAME_FILTER_INPUT=new Locator("USER_NAME_FILTER_INPUT", new By.ByXPath("//input[@name='name']"));
    private static final Locator MANAGE_USERS_SUBMIT_FILTER_BUTTON=new Locator("MANAGE_USERS_SUBMIT_FILTER_BUTTON", new By.ByXPath("//button[@class='the-button primary go-button']"));
    private static final Locator USER_RESULT_FIRST_ROW_NAME_LABEL=new Locator("USER_RESULT_FIRST_ROW_NAME_LABEL", new By.ByXPath("//tr[@id=\"row-0\"]//td[@class=\"table-cell\"][1]"));
    private static final Locator USER_RESULT_FIRST_ROW_EMAIL_LABEL=new Locator("USER_RESULT_FIRST_ROW_EMAIL_LABEL", new By.ByXPath("//tr[@id='row-0']//div[@class='cell-email']"));
    private static final Locator DELETE_USER_BUTTON=new Locator("DELETE_USER_BUTTON", new By.ByXPath("//div[@class='action-button-container']/button[@type='button' and @class='the-icon-button  action-button delete']"));
    private static final Locator DELETE_USER_POPUP=new Locator("DELETE_USER_POPUP",new By.ByXPath("//div[@class='manage-user-delete-modal-confirmation']"));
    private static final Locator POPUP_DELETE_BUTTON=new Locator("POPUP_DELETE_BUTTON", new By.ByXPath("//button[@type='button' and @class='the-button primary delete-user-button']"));
    private static final Locator DELETE_SUCCESS_POPUP=new Locator("DELETE_SUCCESS_POPUP",new By.ByXPath("//div[@class='manage-user-delete-modal-confirmation']"));
    private static final Locator BACK_TO_LIST_BUTTON=new Locator("BACK_TO_LIST_BUTTON",new By.ByXPath("//button[@class='the-button primary' and @id='button-success']"));
    private static final Locator RESET_FILTER_BUTTON=new Locator("RESET_FILTER_BUTTON",new By.ByXPath("(//div[@class='the-row'])[8]/button"));
    private static final Locator USER_RESULT_FOURTH_ROW_ROLE_LABEL=new Locator("USER_RESULT_FOURTH_ROW_ROLE_LABEL",new By.ByXPath("//tr[@id=\"row-0\"]//td[@class=\"table-cell\"][4]"));
    private static final Locator EXPIRED_PIN_CREATE_NEW_PIN_BUTTON=new Locator("EXPIRED_PIN_CREATE_NEW_PIN_BUTTON", new By.ByXPath("//div[@id='expired-pin-wrap']//button[contains(@class,'waves-default') and contains(@class,'small')]"));
    private static final Locator SAVE_PIN_BUTTON=new Locator("SAVE_PIN_BUTTON", new By.ByXPath("//div[@id='modal-pin']//button[contains(@class,'waves-default') and contains(@class,'large')]"));

    public boolean isManageUsersPageLoaded(){
        return driverTools.waitForElementToAppear(MANAGE_USERS_PAGE, 3).isPresent();
    }

    public boolean isSaveUserPageLoaded(){
        return driverTools.waitForElementsToAppear(SAVE_USER_PAGE,3).isPresent();
    }

    public void clickAddNewUserButton(){
        driverTools.waitForElementToAppear(ADD_NEW_USER_BUTTON).orElseThrow().click();
    }

    public void fillUserEmail(String text){
        WebElement input = driverTools.waitForElementToAppear(EMAIL_INPUT).orElseThrow();
        driverTools.sendKeys(input, text);
    }

    public void fillUserName(String text){
        WebElement input = driverTools.waitForElementToAppear(NAME_INPUT).orElseThrow();
        driverTools.sendKeys(input, text);
    }

    public void fillPhoneNo(String text){
        WebElement input = driverTools.waitForElementToAppear(PHONENO_INPUT).orElseThrow();
        driverTools.sendKeys(input, text);
    }

    public void clickAdministratorRadioButton(){
        driverTools.waitForElementToAppear(ADMINISTRATOR_RADIO_BUTTON).orElseThrow().click();
    }

    public void clickFinanceRadioButton(){
        driverTools.waitForElementToAppear(FINANCE_RADIO_BUTTON).orElseThrow().click();
    }

    public void clickReservationRadioButton(){
        driverTools.waitForElementToAppear(RESERVATION_RADIO_BUTTON).orElseThrow().click();
    }

    public void clickYesRadioButton(){
        driverTools.waitForElementToAppear(YES_RADIO_BUTTON).orElseThrow().click();
    }

    public void clickAddNewUserButtonAgain(){
        driverTools.waitForElementToAppear(ADD_NEW_USER_BUTTON_AGAIN).orElseThrow().click();
    }

    public boolean isAccountPinPopupLoaded(){
        return driverTools.waitForElementsToAppear(ACCOUNT_PIN_POPUP,3).isPresent();
    }

    private void fillPinInputFields(String[] pinDigits) {
        Locator[] pinLocators = {
            FIRST_PIN_INPUT, SECOND_PIN_INPUT, THIRD_PIN_INPUT,
            FOURTH_PIN_INPUT, FIFTH_PIN_INPUT, SIXTH_PIN_INPUT
        };
        
        for (int i = 0; i < pinLocators.length; i++) {
            WebElement input = driverTools.waitForElementToAppear(pinLocators[i]).orElseThrow();
            driverTools.sendKeys(input, pinDigits[i]);
        }
    }

    public void fillAccountPin(String text){
        String[] pinDigits = {
            String.valueOf(text.charAt(0)),
            String.valueOf(text.charAt(1)),
            String.valueOf(text.charAt(2)),
            String.valueOf(text.charAt(3)),
            String.valueOf(text.charAt(4)),
            String.valueOf(text.charAt(5))
        };

        // Fill PIN inputs using helper method
        fillPinInputFields(pinDigits);

        // Click Save PIN button after filling PIN
        try {
            logger.log("Clicking Save PIN button");
            WebElement savePinButton = driverTools.waitForElementToBeClickable(SAVE_PIN_BUTTON).orElseThrow();
            driverTools.click(savePinButton);
            logger.log("Save PIN button clicked successfully");
        } catch (Exception e) {
            logger.log("Save PIN button not found or not clickable: " + e.getMessage());
        }

        // Check if user was successfully added first
        boolean isUserAdded = isUserAddedPopupLoaded();
        
        // Only check for expired PIN pop-up if user was NOT successfully added
        if (!isUserAdded) {
            logger.log("User was not added successfully - checking for expired PIN pop-up");
            // Check if expired PIN pop-up appears and click "Create a new PIN" if it does
            try {
                if (driverTools.waitForElementToAppear(EXPIRED_PIN_CREATE_NEW_PIN_BUTTON, 3).isPresent()) {
                logger.log("Expired PIN pop-up detected - clicking Create a new PIN button");
                WebElement createNewPinButton = driverTools.waitForElementToBeClickable(EXPIRED_PIN_CREATE_NEW_PIN_BUTTON).orElseThrow();
                driverTools.click(createNewPinButton);
                logger.log("Create a new PIN button clicked successfully");
                
                // Fill the PIN again after clicking "Create a new PIN" using helper method
                logger.log("Filling PIN again after creating new PIN");
                fillPinInputFields(pinDigits);
                logger.log("PIN filled again successfully");
                
                // Click Save PIN button again after filling PIN in expired PIN scenario
                try {
                    logger.log("Clicking Save PIN button after expired PIN scenario");
                    WebElement savePinButtonAfterPopup = driverTools.waitForElementToBeClickable(SAVE_PIN_BUTTON).orElseThrow();
                    driverTools.click(savePinButtonAfterPopup);
                    logger.log("Save PIN button clicked successfully after expired PIN scenario");
                    
                    // After PIN is saved, click "Add new user" button again since form data is already filled
                    logger.log("Clicking Add new user button again after PIN is set");
                    clickAddNewUserButtonAgain();
                    logger.log("Add new user button clicked successfully after PIN setup");
                } catch (Exception e) {
                    logger.log("Save PIN button or Add new user button not found/clickable after expired PIN scenario: " + e.getMessage());
                }
                } else {
                    logger.log("Expired PIN pop-up did not appear");
                }
            } catch (Exception e) {
                logger.log("Expired PIN pop-up check failed: " + e.getMessage());
            }
        } else {
            logger.log("User was successfully added - no need to check for expired PIN pop-up");
        }
    }
    public boolean isUserAddedPopupLoaded(){
        return driverTools.waitForElementsToAppear(USER_ADDED_POPUP,3).isPresent();
    }

    public void clickGoBackButton(){
        driverTools.waitForElementToAppear(GO_BACK_BUTTON).orElseThrow().click();
    }

    public void enterFilterUsersName(String text) {
        WebElement input = driverTools.waitForElementToAppear(USER_NAME_FILTER_INPUT).orElseThrow();
        driverTools.clearInput(input);
        driverTools.sendKeys(input, text);
    }

    public void submitFilterManageUsers() {
        WebElement button = driverTools.waitForElementToBeClickable(MANAGE_USERS_SUBMIT_FILTER_BUTTON).orElseThrow();
        driverTools.click(button);
    }

    public void clickDeleteUserButton(){
        WebElement button = driverTools.waitForElementToAppear(DELETE_USER_BUTTON).orElseThrow();
        driverTools.click(button);
    }

    public boolean isDeleteUserPopUpLoaded(){
        return driverTools.waitForElementsToAppear(DELETE_USER_POPUP,3).isPresent();
    }

    public void clickPopUpDeleteButton(){
        WebElement button = driverTools.waitForElementToAppear(POPUP_DELETE_BUTTON).orElseThrow();
        driverTools.click(button);
    }

    public boolean isDeleteSuccessPopUpLoaded(){
        return driverTools.waitForElementsToAppear(DELETE_SUCCESS_POPUP,3).isPresent();
    }

    public void clickBackToListButton(){
        WebElement button = driverTools.waitForElementToAppear(BACK_TO_LIST_BUTTON).orElseThrow();
        driverTools.click(button);
    }

    public void clickResetFilterButton(){
        WebElement button = driverTools.waitForElementToAppear(RESET_FILTER_BUTTON).orElseThrow();
        driverTools.click(button);
    }

    public boolean isUserSearchResultFoundByName(String text) {
        try {
            return driverTools.waitForElementToAppear(USER_RESULT_FIRST_ROW_NAME_LABEL).orElseThrow().getText().contains(text);
        } catch (NoSuchElementException e) {
            logger.log("User is not found");
            return false;
        }
    }

    public String getUserRole(){
        WebElement userRole=driverTools.waitForElementToAppear(USER_RESULT_FOURTH_ROW_ROLE_LABEL).orElseThrow();
        return driverTools.getText(userRole);
    }

    public String getDisplayedUserName(){
        WebElement el = driverTools.waitForElementToAppear(USER_RESULT_FIRST_ROW_NAME_LABEL).orElseThrow();
        return driverTools.getText(el).trim();
    }

    public String getDisplayedUserEmail(){
        WebElement el = driverTools.waitForElementToAppear(USER_RESULT_FIRST_ROW_EMAIL_LABEL).orElseThrow();
        return driverTools.getText(el).trim();
    }

}
