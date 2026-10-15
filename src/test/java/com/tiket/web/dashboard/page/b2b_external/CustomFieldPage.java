package com.tiket.web.dashboard.page.b2b_external;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

public class CustomFieldPage <T extends WebDriver> extends BasePage<T> {

    private static final Locator CUSTOM_FIELD_PAGE_HEADING =  new Locator("CUSTOM_FIELD_PAGE_HEADING", new By.ByXPath("//span[contains(text(), 'Traveler Custom Fields')]"));
    private static final Locator ADD_CUSTOM_FEILD_BUTTON =  new Locator("ADD_CUSTOM_FEILD_BUTTON", new By.ByXPath("//span[contains(text(), 'Add a Custom Field')]"));
    private static final Locator CUSTOM_FIELD_FORM_HEADING =  new Locator("CUSTOM_FEILD_FORM_HEADING", new By.ByXPath("//span[contains(text(), 'Where do you want to show this custom field?')]"));
    private static final Locator FIELD_NAME =  new Locator("FIELD_NAME", new By.ByXPath("//input[@id='field-name']"));
    private static final Locator FIELD_TYPE =  new Locator("FIELD_TYPE", new By.ByXPath("//input[@id='field-type']"));
    private static final Locator SHORT_DESCRIPTION =  new Locator("SHORT_DESCRIPTION", new By.ByXPath("//input[@id='short-description-optional']"));
    private static final Locator SAVE_BUTTON  =  new Locator("SAVE_BUTTON", new By.ByXPath("//button[contains(@class, 'Button_variant_primary')]"));
    private static final Locator RADIO_INTERIM_PAGE  =  new Locator("RADIO_INTERIM_PAGE", new By.ByXPath("//span[contains(text(), 'Corporate Booking Form')]"));
    private static final Locator ADD_MORE_OPTION  =  new Locator("ADD_MORE_OPTION", new By.ByXPath("//button[contains(@class, 'items-center') and contains(@class, 'gap-2') and  contains(@class, 'Button_variant_tertiary')]"));
    private static final Locator SNACKBAR_TEXT  =  new Locator("SNACKBAR_TEXT", new By.ByXPath("//div[contains(text(), 'added')]"));
    private static final Locator ACTION_BUTTON  =  new Locator("ACTION_BUTTON", new By.ByXPath("(//tr//*[local-name()='svg' and contains(@class, 'cursor-pointer')])[3]"));
    private static final Locator DELETE_OPTION  =  new Locator("DELETE_OPTION", new By.ByXPath("//span[contains(text(), 'Delete')]"));
    private static final Locator DELETE_BUTTON  =  new Locator("DELETE_BUTTON", new By.ByXPath("//button//span[contains(text(), 'Yes, delete')]"));
    private static final Locator DELETE_SNACKBAR  =  new Locator("DELETE_SNACKBAR", new By.ByXPath("//div[contains(@class, 'Snackbar_text')]"));
    private static final Locator DEACTIVATE_OPTION  =  new Locator("DEACTIVATE_OPTION", new By.ByXPath("(//span[contains(text(), 'Deactivate')])[3]"));
    private static final Locator DEACTIVATE_BUTTON  =  new Locator("DEACTIVATE_BUTTON", new By.ByXPath("//button[contains(text(), 'deactivate')]"));
    private static final Locator EDIT_BUTTON  =  new Locator("EDIT_BUTTON", new By.ByXPath("//span[contains(text(),'Edit')]"));
    private static final Locator FIELD_NAME_HEADER  =  new Locator("FIELD_NAME_HEADER", new By.ByXPath("//table//th[contains(text(),'Field Name')]"));
    private static final Locator FIELD_TYPE_HEADER  =  new Locator("FIELD_TYPE_HEADER", new By.ByXPath("//table//th[contains(text(),'Field Type')]"));
    private static final Locator STATUS_HEADER  =  new Locator("STATUS_HEADER", new By.ByXPath("//table//th[contains(text(),'Status')]"));
    private static final Locator INPUT_REQUIREMENT_HEADER  =  new Locator("FIELD_TYPE_HEADER", new By.ByXPath("//table//th[contains(text(),'Input Requirement')]"));
    private static final Locator FIELD_NAME_ROW_DATA  =  new Locator("FIELD_NAME_ROW_DATA", new By.ByXPath("//tr[contains(@class,'bg-white')]//td[3]"));
    private static final Locator FIELD_TYPE_ROW_DATA  =  new Locator("FIELD_TYPE_ROW_DATA", new By.ByXPath("//tr[contains(@class,'bg-white')]//td[4]"));
    private static final Locator FIELD_STATUS_ROW_DATA  =  new Locator("FIELD_STATUS_ROW_DATA", new By.ByXPath("//tr[contains(@class,'bg-white')]//td[5]"));
    private static final Locator TRAVEL_PLAN_PAGE  =  new Locator("FEILD_STATUS_ROW_DATA", new By.ByXPath("//span[contains(text(),'Travel Plan Page')]"));
    private static final Locator INTERIM_PAGE  =  new Locator("FEILD_STATUS_ROW_DATA", new By.ByXPath("  //span[contains(text(),'Corporate Booking Form Page')]"));
    private static final Locator ACTIVATE_OPTION  =  new Locator("ACTIVATE_OPTION", new By.ByXPath("//span[contains(text(), 'Activate')]"));
    private static final Locator ACTIVATE_BUTTON  =  new Locator("ACTIVATE_BUTTON", new By.ByXPath("//button[contains(text(), 'activate')]"));
    public CustomFieldPage(Session<T> session) {
        super(session);
    }

    public boolean isCustomFieldHeadingVisible(){
        driverTools.waitForElementToAppear(CUSTOM_FIELD_PAGE_HEADING);
        return driverTools.isElementDisplayed(CUSTOM_FIELD_PAGE_HEADING);
    }

    public boolean isAddCustomFieldButton(){
        driverTools.waitForElementToAppear(ADD_CUSTOM_FEILD_BUTTON);
        return driverTools.isElementDisplayed(ADD_CUSTOM_FEILD_BUTTON);
    }

    public void clickAddCustomFieldButton(){
        driverTools.waitForElementToAppear(ADD_CUSTOM_FEILD_BUTTON);
        driverTools.clickWithJavascriptExecutor(ADD_CUSTOM_FEILD_BUTTON);
    }

    public boolean isFormHeadingPresent(){
        driverTools.waitForElementToAppear(CUSTOM_FIELD_FORM_HEADING);
        return driverTools.isElementDisplayed(CUSTOM_FIELD_FORM_HEADING);
    }

    public boolean isFieldNameInputPresent(){
        driverTools.waitForElementToAppear(FIELD_NAME);
        return driverTools.isElementDisplayed(FIELD_NAME);
    }

    public boolean isFieldTypeInputPresent(){
        driverTools.waitForElementToAppear(FIELD_TYPE);
        return driverTools.isElementDisplayed(FIELD_TYPE);
    }

    public boolean isShortDescriptionInputPresent(){
        driverTools.waitForElementToAppear(SHORT_DESCRIPTION);
        return driverTools.isElementDisplayed(SHORT_DESCRIPTION);
    }

    public boolean isSaveFormButtonPresent(){
        driverTools.waitForElementToAppear(SAVE_BUTTON);
        return driverTools.isElementDisplayed(SAVE_BUTTON);
    }

    public void checkInterimPage(){
        driverTools.waitForElementToAppear(RADIO_INTERIM_PAGE);
        driverTools.click(RADIO_INTERIM_PAGE);
    }

    public void enterFieldName(String fieldName){
        driverTools.waitForElementToAppear(FIELD_NAME);
        driverTools.sendKeys(FIELD_NAME,fieldName);
    }

    private Locator getFiedlTypeLocator(String FieldType) {
        return new Locator("SEARCH_FORM_RESULT", new By.ByXPath("//span[contains(text(), '"+FieldType+"')]"));
    }

    private Locator getOptionLocator(int index) {
        return new Locator("OPTION_" + index, By.xpath("//input[@id='option-" + index + "']"));
    }

    private void enterOption(int index, String value) {
        Locator optionLocator = getOptionLocator(index);
        driverTools.waitForElementToAppear(optionLocator);
        driverTools.clickWithJavascriptExecutor(optionLocator);
        driverTools.sendKeys(optionLocator, value);
    }

    public void fillCustomFieldForm(String fieldType, String... options) {
        driverTools.click(FIELD_TYPE);
        Locator fieldTypeLocator = getFiedlTypeLocator(fieldType);
        driverTools.waitForElementToAppear(fieldTypeLocator);
        driverTools.click(fieldTypeLocator);

        int optionCount = fieldType.equals("Single Selection") ? 2 : fieldType.equals("Multiple Selection") ? 4 : 0;

        for (int i = 0; i < optionCount; i++) {
            if (i >= options.length) break;

            if (i >= 2 && fieldType.equals("Multiple Selection")) {
                driverTools.click(ADD_MORE_OPTION);
            }
            enterOption(i + 1, options[i]);
        }

        driverTools.clickWithJavascriptExecutor(SAVE_BUTTON);
    }

    public String getSnackbarText(){
        driverTools.waitForElementToAppear(SNACKBAR_TEXT,5);
        Assertion.assertThat("Custom Field is Added successfully",driverTools.isElementDisplayed(SNACKBAR_TEXT),is(true));
        return driverTools.getText(SNACKBAR_TEXT).orElseThrow();
    }

    public void deleteCustomField(){
        driverTools.waitForElementToDisappear(SNACKBAR_TEXT);
        driverTools.waitForElementToAppear(ACTION_BUTTON);
        driverTools.click(ACTION_BUTTON);

        driverTools.waitForElementToAppear(DELETE_OPTION);
        driverTools.clickWithJavascriptExecutor(DELETE_OPTION);

        driverTools.waitForElementToAppear(DELETE_BUTTON);
        driverTools.click(DELETE_BUTTON);

    }

    public void verifyDeleteSnackbar(){
        driverTools.waitForElementToAppear(DELETE_SNACKBAR);
        String text=driverTools.getText(DELETE_SNACKBAR).orElseThrow();

        Assertion.assertThat("Custom field is deleted successfully",text,containsString("deleted"));

    }

    public String deactivateCustomField(){
        driverTools.click(ACTION_BUTTON);
        driverTools.click(DEACTIVATE_OPTION);
        driverTools.click(DEACTIVATE_BUTTON);

        driverTools.waitForElementToAppear(DELETE_SNACKBAR);
        return driverTools.getText(DELETE_SNACKBAR).orElseThrow();
    }

    public String editCustomField(){
        driverTools.click(ACTION_BUTTON);
        driverTools.click(EDIT_BUTTON);

        driverTools.click(FIELD_NAME);
        driverTools.clearInput(FIELD_NAME);
        driverTools.sendKeys(FIELD_NAME, CoreFunctions.randomName()+"Edited");
        driverTools.clickWithJavascriptExecutor(SAVE_BUTTON);

        driverTools.waitForElementToAppear(DELETE_SNACKBAR);
        return driverTools.getText(DELETE_SNACKBAR).orElseThrow();
    }

    public boolean isFieldNameHeaderPresent(){
        driverTools.waitForElementToAppear(FIELD_NAME_HEADER);
        return driverTools.isElementPresent(FIELD_NAME_HEADER);
    }

    public boolean isFieldTypeHeaderPresent(){
        driverTools.waitForElementToAppear(FIELD_TYPE_HEADER);
        return driverTools.isElementPresent(FIELD_TYPE_HEADER);
    }

    public boolean isFieldStatusHeaderPresent(){
        driverTools.waitForElementToAppear(STATUS_HEADER);
        return driverTools.isElementPresent(STATUS_HEADER);
    }

    public boolean isFieldRequirementHeaderPresent(){
        driverTools.waitForElementToAppear(INPUT_REQUIREMENT_HEADER);
        return driverTools.isElementPresent(INPUT_REQUIREMENT_HEADER);
    }

    public boolean isFieldNameRowDataPresent(){
        driverTools.waitForElementToAppear(FIELD_NAME_ROW_DATA);
        return driverTools.isElementPresent(FIELD_TYPE_ROW_DATA);
    }

    public boolean isFieldTypeRowDataPresent(){
        driverTools.waitForElementToAppear(FIELD_TYPE_ROW_DATA);
        return driverTools.isElementPresent(FIELD_TYPE_ROW_DATA);
    }

    public boolean isFieldStatusRowDataPresent(){
        driverTools.waitForElementToAppear(FIELD_STATUS_ROW_DATA);
        return driverTools.isElementPresent(FIELD_STATUS_ROW_DATA);
    }

    public void openTravelPlanTab(){
        driverTools.waitForElementToAppear(TRAVEL_PLAN_PAGE);
        driverTools.click(TRAVEL_PLAN_PAGE);
    }

    public void openCorporateBookingFormTab(){
        driverTools.waitForElementToAppear(INTERIM_PAGE);
        driverTools.click(INTERIM_PAGE);
    }

    public void activateCustomField(){
        driverTools.click(ACTION_BUTTON);
        driverTools.click(ACTIVATE_OPTION);
        driverTools.click(ACTIVATE_BUTTON);
    }
}
