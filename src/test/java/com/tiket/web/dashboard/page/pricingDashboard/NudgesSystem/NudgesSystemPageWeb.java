package com.tiket.web.dashboard.page.pricingDashboard.NudgesSystem;

import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;

public class NudgesSystemPageWeb<T extends WebDriver> extends WebPage<T> {
    public NudgesSystemPageWeb(Session<T> session) {
        super(session);
    }

    private static final Locator NUDGES_SYSTEM_TITLE = new Locator("NUDGES_SYSTEM_TITLE", By.xpath("//span[text()='Nudges System']"));
    private static final Locator SETTINGS_BUTTON = new Locator("SETTINGS_BUTTON", By.xpath("(//button[.//span[text()='Settings']])[1]"));
    private static final Locator EDIT_BUTTON = new Locator("EDIT_BUTTON", By.xpath("(//div[contains(@data-testid,'Edit')]//span[text()='Edit'])[1]"));
    private static final Locator DEVICE_VALUE = new Locator("DEVICE_VALUE", By.xpath("(//span[normalize-space()='Device']/following-sibling::div//span)[1]"));
    private static final Locator ADD_NUDGE_BUTTON = new Locator("ADD_NUDGE_BUTTON", By.xpath("//button[normalize-space()='Add Nudge']"));
    private static final Locator VERTICAL_INPUT = new Locator("VERTICAL_INPUT", By.xpath("//input[@id='vertical--distribution-channel']"));
    private static final Locator ACCOM_VERTICAL_DROPDOWN = new Locator("ACCOM_VERTICAL_DROPDOWN", By.xpath("//div[@data-testid='autocomplete-option-verticalId-ACCOM']"));
    private static final Locator VERTICAL_NAME= new Locator("VERTICAL_NAME", By.xpath("//input[@id='name']"));
    private static final Locator LAYER_INPUT= new Locator("LAYER_INPUT", By.xpath("//input[@id='layer']"));
    private static final Locator LAYER_UNPUT_VALUE = new Locator("LAYER_UNPUT_VALUE", By.xpath("(//div[contains(@id,'autocomplete-options-layer')])[1]"));
    private static final Locator PRIORITY_INPUT= new Locator("PRIORITY_INPUT", By.xpath("//input[@id='priority']"));
    private static final Locator DEVICE_BTN = new Locator("DEVICE_BTN", By.xpath("//button[contains(@class,'Chip_chip') and .//span[text()='Android']]"));
    private static final Locator FUNNEL_INPUT = new Locator("FUNNEL_INPUT", By.xpath("//input[@id='funnel' and @placeholder='Funnel']"));
    private static final Locator FUNNEL_INPUT_VALUE = new Locator("FUNNEL_INPUT_VALUE", By.xpath("//span[text()='Hotel' and contains(@class, 'Text_text__DSnue')]"));
    private static final Locator TARGET_ACTION_BTN = new Locator("TARGET_ACTION_BTN", By.xpath("//span[@data-testid='progress-step-2' and text()='Target & Action']"));
    private static final Locator PLACEMENT_INPUT = new Locator("PLACEMENT_INPUT", By.xpath("//input[@id='nudge-placement' and @placeholder='Nudge Placement']"));
    private static final Locator PLACEMENT_INPUT_VALUE = new Locator("PLACEMENT_INPUT_VALUE", By.xpath("(//div[contains(@id,'autocomplete-options-placement')])[1]"));
    private static final Locator STYLE_INPUT = new Locator("STYLE_INPUT", By.xpath("//input[@id='style']"));
    private static final Locator STYLE_INPUT_VALUE = new Locator("STYLE_INPUT_VALUE", By.xpath("(//div[contains(@id,'autocomplete-options-style')])[1]"));
    private static final Locator COLOR_INPUT = new Locator("COLOR_INPUT", By.xpath("//input[@id='color']"));
    private static final Locator COLOR_INPUT_VALUE = new Locator("COLOR_INPUT_VALUE", By.xpath("(//div[contains(@id,'autocomplete-options-color')])[1]"));
    private static final Locator SIZE_INPUT = new Locator("SIZE_INPUT", By.xpath("//input[@id='size']"));
    private static final Locator SIZE_INPUT_VALUE = new Locator("SIZE_INPUT_VALUE", By.xpath("(//div[contains(@id,'autocomplete-options-size')])[1]"));
    private static final Locator MSG_INDONESIA_VALUE = new Locator("MSG_INDONESIA_VALUE", By.xpath("//textarea[@data-testid='message-id']"));
    private static final Locator MSG_ENGLISH_VALUE = new Locator("MSG_ENGLISH_VALUE", By.xpath("//textarea[@data-testid='message-en']"));
    private static final Locator CONDITION_BTN = new Locator("CONDITION_BTN", By.xpath("//span[@data-testid='progress-step-3']"));
    private static final Locator STATUS_INPUT = new Locator("STATUS_INPUT", By.xpath("//input[@id='status']"));
    private static final Locator STATUS_INPUT_VALUE = new Locator("STATUS_INPUT_VALUE", By.xpath("//div[@data-name='status' and @data-value='PAUSED']"));
    private static final Locator CONDITION_INPUT = new Locator("CONDITION_INPUT", By.xpath("//input[@id='condition']"));
    private static final Locator CONDITION_INPUT_VALUE = new Locator("CONDITION_INPUT_VALUE", By.xpath("(//div[contains(@id,'autocomplete-options-condition')])[1]"));
    private static final Locator OPERATOR_INPUT = new Locator("OPERATOR_INPUT", By.xpath("//input[@id='operator']"));
    private static final Locator OPERATOR_INPUT_VALUE = new Locator("OPERATOR_INPUT_VALUE", By.xpath("//div[contains(@id,'autocomplete-options-condition.0.operator')][1]"));
    private static final Locator ADJUSTMENT_VALUE = new Locator("ADJUSTMENT_VALUE", By.xpath("//input[@placeholder='Accom Adjustment Value']"));
    private static final Locator SUMMARY_BTN = new Locator("SUMMARY_BTN", By.xpath("//span[@data-testid='progress-step-4']"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", By.xpath("//button[@data-testid='save-rule-button']"));
    private static final Locator SAVE_CONFIRM_BUTTON = new Locator("SAVE_CONFIRM_BUTTON", By.xpath("//button[normalize-space(text())='Yes, save']"));
    private static final Locator SEARCH_INPUT = new Locator("SEARCH_INPUT", By.xpath("//input[@data-testid='nudge-filter-search-name']"));
    private static final Locator SEARCH_BTN = new Locator("SEARCH_BTN", By.xpath("//button[normalize-space()='Search']"));
    private static final Locator RESULT_ROW= new Locator("RESULT_ROW", By.xpath("(//table[contains(@class,'p-datatable-table')]//tbody/tr/td[3])[1]"));
    private static final Locator VERTICAL_SEARCH_DROPDOWN = new Locator("VERTICAL_SEARCH_DROPDOWN", By.xpath("//div[starts-with(@id,'autocomplete-options-verticalIds-')][1]"));
    private static final Locator DELETE_BUTTON = new Locator("DELETE_BUTTON", By.xpath("//div[@data-testid='action-button-option-undefined-Delete']"));
    private static final Locator CONFIRM_DELETE_BUTTON = new Locator("CONFIRM_DELETE_BUTTON", By.xpath("//button[text()='Yes, delete']"));
    private static final Locator NO_DATA_FOUND = new Locator("NO_DATA_FOUND", By.xpath("//td[text()='No data found']"));
    private static final Locator LAST = new Locator("LAST", By.xpath("//button[contains(@class,'p-paginator-page') and @aria-label='Page 1' and contains(@class,'p-highlight')]"));

    public static <T extends WebDriver> NudgesSystemPageWeb<?> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new NudgesSystemPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new NudgesSystemPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    public void goToNudgesSystemPage(){
        if (Configs.ENVIRONMENT == Environment.GK) {
            driver.get("https://gatotkaca.tiket.com/admin/pricing-dashboard/nudges-system");
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driver.get("https://preprod.tiket.com/admin/pricing-dashboard/nudges-system");
        } else if (Configs.ENVIRONMENT == Environment.PROD) {
            driver.get("https://tiket.com/admin/pricing-dashboard/nudges-system");
        }
    }

    public boolean isNudgesSystemPageLoaded() {
        driverTools.waitForElementToAppear(NUDGES_SYSTEM_TITLE, 3);
        return driverTools.isElementDisplayed(NUDGES_SYSTEM_TITLE);
    }

    public void clickSettingsButton() {
        driverTools.waitForElementToAppear(LAST);
        driverTools.scrollToElement(LAST);
        driverTools.waitForElementToAppear(SETTINGS_BUTTON, 10);
        driverTools.click(SETTINGS_BUTTON);
    }

    public void clickEditButton() {
        driverTools.dummyWait(5);
        driverTools.scrollToElement(EDIT_BUTTON);
        driverTools.waitForElementToAppear(EDIT_BUTTON, 10);
        driverTools.click(EDIT_BUTTON);
    }

    public boolean isValuePresent(Locator ELEMENT_LOCATOR) {
        boolean isPresent = driverTools.isElementDisplayed(ELEMENT_LOCATOR);
        if (!isPresent) {
            logger.log(ELEMENT_LOCATOR + " is not present or not pre-filled.");
        }
        return isPresent;
    }

    private static Locator getValueLocator(String labelName) {
        return new Locator(labelName.toUpperCase() + "_VALUE", By.xpath("//span[normalize-space()='" + labelName + "']/following-sibling::span"));
    }

    public boolean verifyGeneralRuleIsPreFilled() {
        return  driverTools.isElementPresent(getValueLocator("Vertical / Distribution Channel")) &&
                driverTools.isElementPresent(getValueLocator("Nudge Name")) &&
                driverTools.isElementPresent(getValueLocator("Layer")) &&
                driverTools.isElementPresent(getValueLocator("Priority")) &&
                driverTools.isElementPresent(DEVICE_VALUE);
    }

    public boolean verifyTargetActionIsPreFilled() {
        return driverTools.isElementPresent(getValueLocator("Funnel")) &&
                driverTools.isElementPresent(getValueLocator("Placement")) &&
                driverTools.isElementPresent(getValueLocator("Icon")) &&
                driverTools.isElementPresent(getValueLocator("Icon Color")) &&
                driverTools.isElementPresent(getValueLocator("Style")) &&
                driverTools.isElementPresent(getValueLocator("Color")) &&
                driverTools.isElementPresent(getValueLocator("Style Variant")) &&
                driverTools.isElementPresent(getValueLocator("Message (EN)")) &&
                driverTools.isElementPresent(getValueLocator("Message (ID)"));
    }

    private static Locator getConditionValueLocator(String fieldLabel) {
        return new Locator(fieldLabel.toUpperCase().replace(" ", "_") + "_VALUE", By.xpath("((//div[starts-with(normalize-space(),'Condition ')])//span[normalize-space()='" + fieldLabel + "']/following-sibling::span)[1]"));
    }

    public boolean verifyConditionIsPreFilled() {
        return driverTools.isElementPresent(getConditionValueLocator("Condition")) &&
                driverTools.isElementPresent(getConditionValueLocator("Operator")) &&
                driverTools.isElementPresent(getConditionValueLocator("Value"));
    }

    public boolean verifyScheduleIsPreFilled() {
        return driverTools.isElementPresent(getValueLocator("Status")) &&
                driverTools.isElementPresent(getValueLocator("Schedule"));
    }

    private static Locator getNudgeCardContentLocator(String cardTitle) {
        return new Locator(
                cardTitle.toUpperCase().replace(" ", "_") + "_CONTENT", By.xpath("//h3[normalize-space()='" + cardTitle + "']/ancestor::div[contains(@class,'CardWithNudge_card_with_nudge__Q_sy_')]//div[contains(@class,'CardWithNudge_content_container__hHsTJ')]"));
    }
    public boolean verifyNudgesPreviewIsPreFilled() {
        return driverTools.isElementPresent(getNudgeCardContentLocator("Nudges SRP - ID")) &&
                driverTools.isElementPresent(getNudgeCardContentLocator("Nudges SRP - EN"));
    }

    public void clickAddNudgeButton() {
        driverTools.waitForElementToAppear(ADD_NUDGE_BUTTON, 10);
        driverTools.click(ADD_NUDGE_BUTTON);
    }

    public void fillGeneralRuleSection(String vertical, String nudgeName, String priority) {
        driverTools.dummyWait(4);
        driverTools.waitForElementToAppear(VERTICAL_INPUT, 10);
        driverTools.click(VERTICAL_INPUT);
        driverTools.sendKeys(VERTICAL_INPUT, vertical);
        driverTools.waitForElementToAppear(ACCOM_VERTICAL_DROPDOWN,5);
        driverTools.click(ACCOM_VERTICAL_DROPDOWN);

        driverTools.clickWithJavascriptExecutor(VERTICAL_NAME);
        driverTools.sendKeys(VERTICAL_NAME, nudgeName);

        driverTools.click(LAYER_INPUT);
        driverTools.waitForElementToAppear(LAYER_UNPUT_VALUE);
        driverTools.click(LAYER_UNPUT_VALUE);

        driverTools.click(PRIORITY_INPUT);
        driverTools.sendKeys(PRIORITY_INPUT, priority);

        driverTools.clickWithJavascriptExecutor(DEVICE_BTN);
        driverTools.click(TARGET_ACTION_BTN);
    }

    public void fillTargetActionSection(String funnel, String message) {
        driverTools.dummyWait(4);
        driverTools.waitForElementToAppear(FUNNEL_INPUT, 10);
        driverTools.click(FUNNEL_INPUT);
        driverTools.sendKeys(FUNNEL_INPUT, funnel);
        driverTools.waitForElementToBeClickable(FUNNEL_INPUT_VALUE, 10);
        driverTools.click(FUNNEL_INPUT_VALUE);

        driverTools.click(PLACEMENT_INPUT);
        driverTools.waitForElementToAppear(PLACEMENT_INPUT_VALUE,5);
        driverTools.click(PLACEMENT_INPUT_VALUE);

        driverTools.waitForElementToAppear(STYLE_INPUT,5);
        driverTools.click(STYLE_INPUT);
        driverTools.waitForElementToBeClickable(STYLE_INPUT_VALUE);
        driverTools.clickWithJavascriptExecutor(STYLE_INPUT_VALUE);

        driverTools.click(COLOR_INPUT);
        driverTools.waitForElementToBeClickable(COLOR_INPUT_VALUE);
        driverTools.click(COLOR_INPUT_VALUE);

        driverTools.clickWithJavascriptExecutor(SIZE_INPUT);
        driverTools.waitForElementToBeClickable(SIZE_INPUT_VALUE);
        driverTools.click(SIZE_INPUT_VALUE);

        driverTools.clickWithJavascriptExecutor(MSG_INDONESIA_VALUE);
        driverTools.sendKeys(MSG_INDONESIA_VALUE, message);

        driverTools.clickWithJavascriptExecutor(MSG_ENGLISH_VALUE);
        driverTools.sendKeys(MSG_ENGLISH_VALUE, message);
    }

    public void fillConditionSection( String adjustValue) {
        driverTools.click(CONDITION_BTN);
        driverTools.dummyWait(4);

        driverTools.click(STATUS_INPUT);
        driverTools.waitForElementToBeClickable(STATUS_INPUT_VALUE,5);
        driverTools.scrollToElement(STATUS_INPUT_VALUE);
        driverTools.click(STATUS_INPUT_VALUE);

        driverTools.click(CONDITION_INPUT);
        driverTools.waitForElementToBeClickable(CONDITION_INPUT_VALUE,5);
        driverTools.scrollToElement(CONDITION_INPUT_VALUE);
        driverTools.click(CONDITION_INPUT_VALUE);

        driverTools.click(OPERATOR_INPUT);
        driverTools.waitForElementToBeClickable(OPERATOR_INPUT_VALUE,5);
        driverTools.scrollToElement(OPERATOR_INPUT_VALUE);
        driverTools.clickWithJavascriptExecutor(OPERATOR_INPUT_VALUE);

        driverTools.click(ADJUSTMENT_VALUE);
        driverTools.sendKeys(ADJUSTMENT_VALUE, adjustValue );
    }

    public void goToSummarySection(){
        driverTools.click(SUMMARY_BTN);
        driverTools.dummyWait(4);
    }

    /**
     * Changes an existing rule's status to Paused (inactive) through the edit form and saves it.
     * Must be called after clickEditButton() has opened the rule in edit mode. Used to deactivate
     * ONGOING rules so they can then be deleted.
     */
    public void updateStatusToPaused() {
        driverTools.click(CONDITION_BTN);
        driverTools.waitForElementToBeClickable(STATUS_INPUT, 10);

        driverTools.click(STATUS_INPUT);
        driverTools.waitForElementToBeClickable(STATUS_INPUT_VALUE, 5);
        driverTools.scrollToElement(STATUS_INPUT_VALUE);
        driverTools.click(STATUS_INPUT_VALUE);

        goToSummarySection();
        clickSaveButton();
    }

    public void clickSaveButton() {
        driverTools.waitForElementToBeClickable(SAVE_BUTTON, 10);
        driverTools.click(SAVE_BUTTON);

        driverTools.waitForElementToBeClickable(SAVE_CONFIRM_BUTTON);
        driverTools.click(SAVE_CONFIRM_BUTTON);
    }

    public void searchNewNudgeRule(String nudgeName, String vertical){
        driverTools.dummyWait(4);

        driverTools.click(VERTICAL_INPUT);
        driverTools.sendKeys(VERTICAL_INPUT, vertical);
        driverTools.dummyWait(4);
        driverTools.waitForElementToBeClickable(VERTICAL_SEARCH_DROPDOWN,5);
        driverTools.clickWithJavascriptExecutor(VERTICAL_SEARCH_DROPDOWN);

        driverTools.click(SEARCH_INPUT);
        driverTools.clear(SEARCH_INPUT);
        driver.findElement(SEARCH_INPUT.by()).sendKeys(Keys.CONTROL + "a");
        driver.findElement(SEARCH_INPUT.by()).sendKeys(Keys.DELETE);
        driver.findElement(SEARCH_INPUT.by()).sendKeys(nudgeName);

        driverTools.dummyWait(2);
        driverTools.click(SEARCH_BTN);
    }

    public boolean verifyNudgeName( String nudgeName) {
        driverTools.dummyWait(4);

        driverTools.waitForElementToAppear(RESULT_ROW);
        String cellText = driver.findElement(RESULT_ROW.by()).getText().trim();
        return cellText.equals(nudgeName);
    }

    public void clickDeleteButton() {
        driverTools.scrollToElement(DELETE_BUTTON);
        driverTools.waitForElementToBeClickable(DELETE_BUTTON, 10);
        driverTools.clickWithJavascriptExecutor(DELETE_BUTTON);

        driverTools.waitForElementToBeClickable(CONFIRM_DELETE_BUTTON);
        driverTools.click(CONFIRM_DELETE_BUTTON);
    }

    public boolean verifyNudgeRuleIsDeleted() {
        driverTools.dummyWait(4);
        return driverTools.isElementPresent(NO_DATA_FOUND);
    }
}