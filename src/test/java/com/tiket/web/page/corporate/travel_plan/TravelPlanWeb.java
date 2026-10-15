package com.tiket.web.page.corporate.travel_plan;

import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.pojo.b2b.corporate.TravelPlanPojo;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.NotImplementedException;
import org.hamcrest.Matchers;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalToIgnoringCase;

public class TravelPlanWeb<T extends WebDriver> extends WebPage<T> {

    public TravelPlanWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> TravelPlanWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new TravelPlanDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new TravelPlanMWeb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    private static final Locator TRAVEL_PLAN_HEADER = new Locator("TRAVEL_PLAN_HEADER", new By.ByXPath("//div[contains(@class, 'select_travel_plan_btn')]"));
    private static final Locator SELECT_TRAVEL_PLAN = new Locator("SELECT_TRAVEL_PLAN", AppiumBy.xpath("//*[@id='select-travel-plan' or @id='pilih-travel-plan']"));
    private static final Locator ADDITIONAL_NOTE = new Locator("ADDITIONAL_NOTE", AppiumBy.xpath("//*[contains(@class,'container')]//textarea"));
    private static final Locator CONTINUE_BUTTON_TRAVEL_PLAN = new Locator("CONTINUE_BUTTON_TRAVEL_PLAN", new By.ByXPath("//*[contains(@class,'TravelPlanInterimPage') or contains(@class,'footer_btn')]//button[contains(@class,'Button-module') or contains(@class,'Button_variant_primary')]"));
    private static final Locator ADDITIONAL_CUSTOM_FIELD = new Locator("ADDITIONAL_NOTE", AppiumBy.xpath("//input[@id='additional-custom-field']"));
    private static final Locator AUTOMATION_LOC = new Locator("ADDITIONAL_NOTE", AppiumBy.xpath("//div[@data-testid='option-multiple-1']//input[@type='checkbox']"));
    private static final Locator SAVE_LOC = new Locator("ADDITIONAL_NOTE", AppiumBy.xpath("//button[@data-testid='dropdown-apply-button']"));
    private static final Locator TRAVEL_PLAN = new Locator("TRAVEL_PLAN", AppiumBy.xpath("//*[contains(@class,'SelectTravelPlan-module__travel_plan_content')]"));
    private static final Locator FILE_CUSTOM_FIELD = new Locator("FILE_CUSTOM_FIELD", AppiumBy.xpath("//span[@title='image.png']"));
    private static final Locator MULTI_SELECT_CUSTOM_FIELD = new Locator("MULTI_SELECT_CUSTOM_FIELD", AppiumBy.xpath("//input[@id='automation-multi-selection']"));
    private static final Locator SINGLE_SELECT_CUSTOM_FIELD = new Locator("SINGLE_SELECT_CUSTOM_FIELD", AppiumBy.xpath("//input[@id='automation-single-selection']"));
    private static final Locator INPUT_SHORT_TEXT_CUSTOM_FIELD = new Locator("INPUT_SHORT_TEXT_CUSTOM_FIELD", AppiumBy.xpath("//input[@id='automation-short-answer']"));
    private static final Locator INPUT_LONG_TEXT_CUSTOM_FIELD = new Locator("INPUT_LONG_TEXT_CUSTOM_FIELD", AppiumBy.xpath("//textarea[@id='automation-long-text']"));
    private static final Locator CONNECTING_FLIGHTS_CARD = new Locator("CONNECTING_FLIGHTS_CARD", AppiumBy.xpath("//*[contains(@class,'card_container')]//*[contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'connecting flights')]"));

    public boolean isConnectingFlightsCardShown() {
        return driverTools.waitForElementToPresence(CONNECTING_FLIGHTS_CARD).isPresent();
    }
    
    public void skipTravelPlan(){
        driverTools.waitForElementToPresence(CONTINUE_BUTTON_TRAVEL_PLAN);
        driverTools.clickWithJavascriptExecutor(CONTINUE_BUTTON_TRAVEL_PLAN);
    }

    public void skipTravelPlanIfPresent(){
        driverTools.waitForElementToPresence(CONTINUE_BUTTON_TRAVEL_PLAN).ifPresent(driverTools::clickWithJavascriptExecutor);
    }

    public boolean isTravelPlanInterimPageShown() {
        driverTools.waitForElementToPresence(TRAVEL_PLAN_HEADER);
        Assertion.assertThat("Travel Plan Interim Page header is shown", driverTools.isElementDisplayed(TRAVEL_PLAN_HEADER), is(true));
        driverTools.waitForElementToPresence(SELECT_TRAVEL_PLAN);
        Assertion.assertThat("Select Travel Plan dropdown is shown", driverTools.isElementDisplayed(SELECT_TRAVEL_PLAN), is(true));
        driverTools.waitForElementToPresence(ADDITIONAL_NOTE);
        Assertion.assertThat("Additional Note textarea is shown", driverTools.isElementDisplayed(ADDITIONAL_NOTE), is(true));
        return true;
    }

    public void enterCustomField(){
        driverTools.waitForElementToAppear(ADDITIONAL_CUSTOM_FIELD);
        driverTools.clickWithJavascriptExecutor(ADDITIONAL_CUSTOM_FIELD);
        driverTools.waitForElementToAppear(AUTOMATION_LOC);
        driverTools.clickWithJavascriptExecutor(AUTOMATION_LOC);
        driverTools.waitForElementToAppear(SAVE_LOC);
        driverTools.clickWithJavascriptExecutor(SAVE_LOC);
    }


    public boolean isButtonEnabled(){
       return driverTools.isElementEnabled(CONTINUE_BUTTON_TRAVEL_PLAN);
    }


    public void setSelectTravelPlan(){
        driverTools.waitForElementToAppear(SELECT_TRAVEL_PLAN);
        driverTools.click(SELECT_TRAVEL_PLAN);

        driverTools.click(TRAVEL_PLAN);
    }


    public boolean checkTravelPlanCustomFields(TravelPlanPojo travelPlanPojo) {
        int totalCustomFields = travelPlanPojo.customFields.size();
        for (int i = 0; i < totalCustomFields; i++) {
            if (travelPlanPojo.customFields.get(i).fieldType.equalsIgnoreCase("file")){
                String fileName = travelPlanPojo.customFields.get(i).file.name;
                driverTools.swipeToElement(FILE_CUSTOM_FIELD, Swipe.UP, 3);
                Assertion.assertThat("custom field : " + fileName + " should be filled automatically", driverTools.getText(FILE_CUSTOM_FIELD).orElseThrow(), equalToIgnoringCase(fileName));
            } else {
                String fieldName = travelPlanPojo.customFields.get(i).fieldName;
                String fieldValue = String.join(", ", travelPlanPojo.customFields.get(i).value);
                WebElement customField;
                switch (fieldName.toLowerCase()) {
                    case "automation multi selection" -> customField = driverTools.waitForElementToAppear(MULTI_SELECT_CUSTOM_FIELD).orElseThrow();
                    case "automation single selection" -> customField = driverTools.waitForElementToAppear(SINGLE_SELECT_CUSTOM_FIELD).orElseThrow();
                    case "automation short answer" -> customField = driverTools.waitForElementToAppear(INPUT_SHORT_TEXT_CUSTOM_FIELD).orElseThrow();
                    case "automation long text" -> customField = driverTools.waitForElementToPresence(INPUT_LONG_TEXT_CUSTOM_FIELD).orElseThrow();
                    default -> throw new IllegalStateException("Unexpected fieldName: " + fieldName);
                }

                String actualValue = driverTools.getValueAttribute(customField, "value");
                if (actualValue == null) {
                    actualValue = driverTools.getText(customField);
                }

                Assertion.assertThat(
                        "custom field: " + fieldName + " with value: " + fieldValue + ", should be filled automatically", actualValue,
                        equalToIgnoringCase(fieldValue)
                );

            }
        }
        Assertion.assertThat("Continue to pay button should be enabled", driverTools.isElementEnabled(CONTINUE_BUTTON_TRAVEL_PLAN), Matchers.is(true));
        return driverTools.isElementEnabled(CONTINUE_BUTTON_TRAVEL_PLAN);
    }

    public boolean isInterimPageShown() {
        driverTools.waitForElementToPresence(TRAVEL_PLAN_HEADER);
        Assertion.assertThat("Travel Plan Interim Page header is shown", driverTools.isElementDisplayed(TRAVEL_PLAN_HEADER), is(true));
        return true;
    }

    public boolean noTravelPlanAvailable(){
        driverTools.waitForElementToAppear(SELECT_TRAVEL_PLAN);
        return driverTools.isElementClickable(SELECT_TRAVEL_PLAN,2);
    }

    private Locator travelPlanOption(String planName) {
        return new Locator("TRAVEL_PLAN_OPTION", new By.ByXPath(String.format(
                "//*[contains(normalize-space(.),%s)]/parent::*[contains(@class,'travel_plan_text')]",
                CoreFunctions.toXPathLiteral(planName.trim()))));
    }

    private Locator selectedTravelPlan(String planName) {
        return new Locator("SELECTED_TRAVEL_PLAN", new By.ByXPath(String.format(
                "//*[(@id='select-travel-plan' or @id='pilih-travel-plan') and (contains(@value,%1$s) or contains(normalize-space(.),%1$s))] | //*[(@id='select-travel-plan' or @id='pilih-travel-plan')]/*[contains(normalize-space(.),%1$s)]",
                CoreFunctions.toXPathLiteral(planName.trim()))));
    }

    public boolean checkTravelPlanIsInactiveOrPlanIsThereOrNot(String planName) {
        driverTools.waitForElementToAppear(SELECT_TRAVEL_PLAN);
        boolean isOpenBtnDisplayed = driverTools.isElementEnabled(SELECT_TRAVEL_PLAN);
        logger.log("isOpenBtnDisplayed: " + isOpenBtnDisplayed);

        driverTools.clickWithJavascriptExecutor(SELECT_TRAVEL_PLAN);
        boolean planPresent = driverTools.waitForElementToPresence(travelPlanOption(planName), 10).isPresent();
        logger.log("travel plan present in list: " + planName + " -> " + planPresent);
        return planPresent;
    }

    public void selectTravelPlan(String planName) {
        driverTools.waitForElementToAppear(SELECT_TRAVEL_PLAN).orElseThrow();
        driverTools.clickWithJavascriptExecutor(SELECT_TRAVEL_PLAN);
        Locator planOption = travelPlanOption(planName);
        driverTools.waitForElementToPresence(planOption);
        driverTools.clickWithJavascriptExecutor(planOption);
    }

    public boolean isCorrectTravelPlanSelected(String planName) {
        boolean selected = driverTools.waitForElementToPresence(selectedTravelPlan(planName), 10).isPresent();
        logger.log("Selected travel plan: " + planName + " is displayed: " + selected);
        return selected;
    }

    public String addApprovalNotes() {
        String approvalNotes = CoreFunctions.generateRandomString(5) + " automation approval notes";
        driverTools.sendKeys(ADDITIONAL_NOTE, approvalNotes);
        return approvalNotes;
    }

    public void selectContinueToPay() {
        driverTools.waitForElementToPresence(CONTINUE_BUTTON_TRAVEL_PLAN);
        driverTools.clickWithJavascriptExecutor(CONTINUE_BUTTON_TRAVEL_PLAN);
    }

}
