package com.tiket.web.dashboard.page.platform.pricing.nudges_system;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class NudgesSystemCreatePageWeb<T extends WebDriver> extends WebPage<T> {
    public NudgesSystemCreatePageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> NudgesSystemCreatePageWeb<?> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new NudgesSystemCreatePageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new NudgesSystemCreatePageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    private static final Locator CONDITION_SCHEDULE_TABS = new Locator("CONDITION_SCHEDULE_TABS", By.xpath("//span[@data-testid='progress-step-3']"));
    private static final Locator SUMMARY_TABS = new Locator("SUMMARY_TABS", By.xpath("//span[@data-testid='progress-step-4']"));

    // General Rule Section
    private static final Locator NUDGE_NAME_INPUT = new Locator("NUDGE_NAME_INPUT", By.xpath("//input[@name='name']"));
    private static final Locator NUDGE_PRIORITY_INPUT = new Locator("NUDGE_PRIORITY_INPUT", By.xpath("//input[@name='priority']"));
    private static final Locator VERTICAL_INPUT = new Locator("VERTICAL_INPUT", By.xpath("//input[@id='vertical--distribution-channel']"));
    private static final Locator VERTICAL_NAME = new Locator("VERTICAL_NAME", By.xpath("//input[@id='name']"));
    private static final Locator LAYER_INPUT = new Locator("LAYER_INPUT", By.xpath("//input[@id='layer']"));
    private static final Locator PRIORITY_INPUT = new Locator("PRIORITY_INPUT", By.xpath("//input[@id='priority']"));
    private static final Locator DEVICE_BTN = new Locator("DEVICE_BTN", By.xpath("//button[contains(@class,'Chip_chip') and .//span[text()='Android']]"));
    private static final Locator TARGET_ACTION_BTN = new Locator("TARGET_ACTION_BTN", By.xpath("//span[@data-testid='progress-step-2' and text()='Target & Action']"));
    
    // Target & Action Section
    private static final Locator FUNNEL_INPUT = new Locator("FUNNEL_INPUT", By.xpath("//input[@id='funnel' and @placeholder='Funnel']"));
    private static final Locator PLACEMENT_INPUT = new Locator("PLACEMENT_INPUT", By.xpath("//input[@id='nudge-placement' and @placeholder='Nudge Placement']"));
    private static final Locator PLACEMENT_INPUT_VALUE = new Locator("PLACEMENT_INPUT_VALUE", By.xpath("(//div[contains(@id,'autocomplete-options-placement')])[1]"));
    private static final Locator STYLE_INPUT = new Locator("STYLE_INPUT", By.xpath("//input[@id='style']"));
    private static final Locator STYLE_INPUT_VALUE = new Locator("STYLE_INPUT_VALUE", By.xpath("(//div[contains(@id,'autocomplete-options-style')])[1]"));
    private static final Locator CAMPAIGN_ID_INPUT = new Locator("CAMPAIGN_ID_INPUT", By.xpath("//input[contains(@id, 'inventory-attribute-key-input-campaign-id')]"));
    private static final Locator COLOR_INPUT = new Locator("COLOR_INPUT", By.xpath("//input[@id='color']"));
    private static final Locator COLOR_INPUT_VALUE = new Locator("COLOR_INPUT_VALUE", By.xpath("(//div[contains(@id,'autocomplete-options-color')])[1]"));
    private static final Locator SIZE_INPUT = new Locator("SIZE_INPUT", By.xpath("//input[@id='size']"));
    private static final Locator SIZE_INPUT_VALUE = new Locator("SIZE_INPUT_VALUE", By.xpath("(//div[contains(@id,'autocomplete-options-size')])[1]"));
    private static final Locator MSG_INDONESIA_VALUE = new Locator("MSG_INDONESIA_VALUE", By.xpath("//textarea[@data-testid='message-id']"));
    private static final Locator MSG_ENGLISH_VALUE = new Locator("MSG_ENGLISH_VALUE", By.xpath("//textarea[@data-testid='message-en']"));
    
    // Condition and Schedule Section
    private static final Locator STATUS_DROPDOWN = new Locator("STATUS_DROPDOWN", By.xpath("//input[@id='status']"));
    private static final Locator STATUS_DROPDOWN_ONGOING_SELECTION = new Locator("STATUS_DROPDOWN_ONGOING_SELECTION", By.xpath("//div[@data-value='ON_GOING']"));
    private static final Locator SNACKBAR_ERROR = new Locator("SNACKBAR_ERROR", By.xpath("//div[contains(@class, 'Snackbar_type_error')]"));
    private static final Locator CONDITION_DROPDOWN = new Locator("CONDITION_DROPDOWN", By.xpath("//input[contains(@id, 'criterion') or @placeholder='Condition']"));
    private static final Locator FIRST_CONDITION_OPTION = new Locator("FIRST_CONDITION_OPTION", By.xpath("//div[@id='autocomplete-options-condition.0.criterion-0']"));
    private static final Locator OPERATOR_DROPDOWN = new Locator("OPERATOR_DROPDOWN", By.xpath("//input[contains(@id, 'operator') or @placeholder='Operator']"));
    private static final Locator FIRST_OPERATOR_OPTION = new Locator("FIRST_OPERATOR_OPTION", By.xpath("//div[@id='autocomplete-options-condition.0.operator-0']"));
    private static final Locator ALL_TIME_RADIO = new Locator("ALL_TIME_RADIO", By.xpath("//input[@name='scheduleType' and contains(@value, 'ALL_TIME')]"));
    private static final Locator SCHEDULED_RADIO = new Locator("SCHEDULED_RADIO", By.xpath("//input[@name='scheduleType' and contains(@value, 'SCHEDULED')]"));
    private static final Locator START_DATE_INPUT = new Locator("START_DATE_INPUT", By.xpath("//input[@id='start-date']"));
    private static final Locator END_DATE_INPUT = new Locator("END_DATE_INPUT", By.xpath("//input[@id='end-date']"));
    private static final Locator CALENDAR_NEXT_BTN = new Locator("CALENDAR_NEXT_BTN", By.xpath("//small[text()='Hari Ini']/following::button[1]"));
    // Summary Section
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", By.xpath("//button[@data-testid='save-rule-button']"));
    private static final Locator SAVE_CONFIRM_MODAL = new Locator("SAVE_CONFIRM_MODAL", By.xpath("//section[.//span[contains(text(), 'Save the nudge?')]]"));
    private static final Locator SAVE_CONFIRM_MODAL_YES_BUTTON = new Locator("SAVE_CONFIRM_MODAL_YES_BUTTON", By.xpath("//button[contains(text(), 'Yes, save')]"));
    private static final Locator SAVE_CONFIRM_MODAL_NO_BUTTON = new Locator("SAVE_CONFIRM_MODAL_NO_BUTTON", By.xpath("//button[contains(text(), 'No, go back')]"));
    
    private static Locator getDynamicVerticalDropdown(String vertical) {
        return new Locator("DYNAMIC_VERTICAL_DROPDOWN_" + vertical.toUpperCase(), 
        By.xpath("//div[@data-testid='autocomplete-option-verticalId-" + vertical + "']"));
    }
    
    private static Locator getDynamicLayerSelection(String layer) {
        return new Locator("DYNAMIC_LAYER_" + layer.toUpperCase().replace(" ", "_"), 
        By.xpath("//div[contains(@data-testid, 'autocomplete-option-layer-" + layer + "')]"));
    }
    
    private static Locator getDynamicFunnelInputValue(String funnel) {
        return new Locator("DYNAMIC_FUNNEL_VALUE_" + funnel.toUpperCase().replace(" ", "_"), 
                By.xpath("//div[contains(@data-testid, 'autocomplete-option-funnel-" + funnel + "')]"));
    }

    private static Locator getDynamicAddAsNewValueOption(String value) {
        return new Locator("DYNAMIC_ADD_NEW_VALUE_" + value, 
                By.xpath("//*[contains(normalize-space(), 'Add \"" + value + "\" as new value')]"));
    }
    
    private static Locator getDynamicStatusSelection(String status) {
        String statusValue = status.toUpperCase().replace(" ", "_");
        if ("ONGOING".equals(statusValue)) statusValue = "ON_GOING";
        return new Locator("DYNAMIC_STATUS_" + statusValue, By.xpath("//div[@data-value='" + statusValue + "']"));
    }

    private static final Locator CONDITION_VALUE_INPUT = new Locator("CONDITION_VALUE_INPUT", 
            By.xpath("//input[contains(@id, 'string-input-account-id')]"));
    

    public void fillDynamicGeneralRule(String vertical, String nudgeName, String layer, String priority) {
        driverTools.dummyWait(4);
        driverTools.waitForElementToAppear(VERTICAL_INPUT, 10);
        driverTools.click(VERTICAL_INPUT);
        driverTools.sendKeys(VERTICAL_INPUT, vertical);

        Locator verticalDropdown = getDynamicVerticalDropdown(vertical);
        driverTools.waitForElementToAppear(verticalDropdown, 5);
        driverTools.clickWithJavascriptExecutor(verticalDropdown);
        driverTools.dummyWait(1); 

        driverTools.clickWithJavascriptExecutor(VERTICAL_NAME);
        driverTools.sendKeys(VERTICAL_NAME, nudgeName);

        driverTools.click(LAYER_INPUT);
        Locator layerDropdown = getDynamicLayerSelection(layer);
        driverTools.waitForElementToAppear(layerDropdown, 5);
        driverTools.dummyWait(1);
        driverTools.clickWithJavascriptExecutor(layerDropdown);
        driverTools.dummyWait(1);

        driverTools.click(PRIORITY_INPUT);
        driverTools.sendKeys(PRIORITY_INPUT, priority);

        driverTools.clickWithJavascriptExecutor(DEVICE_BTN);
        driverTools.click(TARGET_ACTION_BTN);
    }

    public void fillDynamicTargetAction(String funnel, String campaignId, String message) {
        driverTools.dummyWait(4);
        driverTools.waitForElementToAppear(FUNNEL_INPUT, 10);
        driverTools.click(FUNNEL_INPUT);
        driverTools.sendKeys(FUNNEL_INPUT, funnel);

        Locator funnelDropdown = getDynamicFunnelInputValue(funnel);
        driverTools.waitForElementToBeClickable(funnelDropdown, 10);
        driverTools.clickWithJavascriptExecutor(funnelDropdown);
        driverTools.dummyWait(1);

        driverTools.click(PLACEMENT_INPUT);
        driverTools.waitForElementToAppear(PLACEMENT_INPUT_VALUE, 5);
        driverTools.clickWithJavascriptExecutor(PLACEMENT_INPUT_VALUE);

        driverTools.waitForElementToAppear(STYLE_INPUT, 5);
        driverTools.click(STYLE_INPUT);
        driverTools.waitForElementToBeClickable(STYLE_INPUT_VALUE);
        driverTools.clickWithJavascriptExecutor(STYLE_INPUT_VALUE);

        driverTools.click(COLOR_INPUT);
        driverTools.waitForElementToBeClickable(COLOR_INPUT_VALUE);
        driverTools.clickWithJavascriptExecutor(COLOR_INPUT_VALUE);

        driverTools.clickWithJavascriptExecutor(SIZE_INPUT);
        driverTools.waitForElementToBeClickable(SIZE_INPUT_VALUE);
        driverTools.clickWithJavascriptExecutor(SIZE_INPUT_VALUE);

        driverTools.scrollToElement(CAMPAIGN_ID_INPUT);
        driverTools.click(CAMPAIGN_ID_INPUT);
        driverTools.sendKeys(CAMPAIGN_ID_INPUT, campaignId);
        driverTools.dummyWait(1); 
        Locator addValueOption = getDynamicAddAsNewValueOption(campaignId);
        driverTools.waitForElementToAppear(addValueOption, 5);
        driverTools.clickWithJavascriptExecutor(addValueOption);
        driverTools.dummyWait(1); 
        
        driverTools.clickWithJavascriptExecutor(MSG_INDONESIA_VALUE);
        driverTools.sendKeys(MSG_INDONESIA_VALUE, message);

        driverTools.clickWithJavascriptExecutor(MSG_ENGLISH_VALUE);
        driverTools.sendKeys(MSG_ENGLISH_VALUE, message);
    }

    public void fillDynamicConditionAndSchedule(String scheduleType, String status) {
        driverTools.waitForElementToAppear(CONDITION_SCHEDULE_TABS, 5);
        driverTools.clickWithJavascriptExecutor(CONDITION_SCHEDULE_TABS);
        driverTools.dummyWait(4);

        if ("SCHEDULED".equalsIgnoreCase(scheduleType)) {
            driverTools.waitForElementToAppear(SCHEDULED_RADIO, 5);
            driverTools.clickWithJavascriptExecutor(SCHEDULED_RADIO);
            driverTools.dummyWait(1);

            driverTools.click(START_DATE_INPUT);
            driverTools.waitForElementToAppear(CALENDAR_NEXT_BTN, 5);
            driverTools.click(CALENDAR_NEXT_BTN);
            driverTools.dummyWait(1);

            driverTools.click(END_DATE_INPUT);
            driverTools.waitForElementToAppear(CALENDAR_NEXT_BTN, 5);
            driverTools.click(CALENDAR_NEXT_BTN);
            driverTools.dummyWait(1);
        } else {
            driverTools.waitForElementToAppear(ALL_TIME_RADIO, 5);
            driverTools.clickWithJavascriptExecutor(ALL_TIME_RADIO);
            driverTools.dummyWait(1);
            driverTools.scrollElementToCenter(STATUS_DROPDOWN);
            driverTools.click(STATUS_DROPDOWN);
            Locator statusSelection = getDynamicStatusSelection(status); 
            driverTools.scrollElementToCenter(statusSelection);
            driverTools.click(statusSelection);
            driverTools.dummyWait(1);

        }
        // first condition
        String randomAccountId = "acc" + CoreFunctions.randomInt(6);
        driverTools.waitForElementToAppear(CONDITION_DROPDOWN, 10);
        driverTools.clickWithJavascriptExecutor(CONDITION_DROPDOWN);
        driverTools.waitForElementToAppear(FIRST_CONDITION_OPTION, 5);
        driverTools.dummyWait(1); 
        driverTools.clickWithJavascriptExecutor(FIRST_CONDITION_OPTION);
        driverTools.dummyWait(1); 
        driverTools.waitForElementToAppear(OPERATOR_DROPDOWN, 10);
        driverTools.clickWithJavascriptExecutor(OPERATOR_DROPDOWN);
        driverTools.waitForElementToAppear(FIRST_OPERATOR_OPTION, 5);
        driverTools.dummyWait(1);
        driverTools.clickWithJavascriptExecutor(FIRST_OPERATOR_OPTION);
        driverTools.dummyWait(1);
        driverTools.waitForElementToAppear(CONDITION_VALUE_INPUT, 10);
        driverTools.clickWithJavascriptExecutor(CONDITION_VALUE_INPUT);
        driverTools.clear(CONDITION_VALUE_INPUT); 
        driverTools.sendKeys(CONDITION_VALUE_INPUT, randomAccountId);
        driverTools.dummyWait(1);


    }
    public void inputNudgeName(String nudgeName) {
        driverTools.waitForElementsToAppear(NUDGE_NAME_INPUT);
        driverTools.scrollElementToCenter(NUDGE_NAME_INPUT);
        driverTools.click(NUDGE_NAME_INPUT);
        driverTools.clearInput(NUDGE_NAME_INPUT);
        driverTools.sendKeys(NUDGE_NAME_INPUT, nudgeName);
    }

    public void inputNudgePriorityInput(String priority) {
        driverTools.scrollElementToCenter(NUDGE_PRIORITY_INPUT);
        driverTools.click(NUDGE_PRIORITY_INPUT);
        driverTools.clearInput(NUDGE_PRIORITY_INPUT);
        driverTools.sendKeys(NUDGE_PRIORITY_INPUT, priority);
    }
    public void inputNudgeLayer(String layer) {
        driverTools.scrollElementToCenter(LAYER_INPUT);
         driverTools.click(LAYER_INPUT);
        Locator layerDropdown = getDynamicLayerSelection(layer);
        driverTools.waitForElementToAppear(layerDropdown, 5);
        driverTools.dummyWait(1);
        driverTools.clickWithJavascriptExecutor(layerDropdown);
        driverTools.dummyWait(1);
    }

    public void goToConditionScheduleStep() {
        driverTools.click(CONDITION_SCHEDULE_TABS);
        driverTools.dummyWait(1);
    }

    public void changeStatusToOngoing() {
        driverTools.scrollElementToCenter(STATUS_DROPDOWN);
        driverTools.click(STATUS_DROPDOWN);
        driverTools.scrollElementToCenter(STATUS_DROPDOWN_ONGOING_SELECTION);
        driverTools.click(STATUS_DROPDOWN_ONGOING_SELECTION);
    }

    public void goToSummaryStep() {
        driverTools.click(SUMMARY_TABS);
    }

    public void goToTargetActionStep() {
        driverTools.click(TARGET_ACTION_BTN);
    }

    public void clickSave() {
        driverTools.click(SAVE_BUTTON);
        driverTools.waitForElementToAppear(SAVE_CONFIRM_MODAL);
        driverTools.click(SAVE_CONFIRM_MODAL_YES_BUTTON);
    }

    public void closeSaveConfirmationModal() {
        driverTools.click(SAVE_CONFIRM_MODAL_NO_BUTTON);
        driverTools.waitForElementToAbsence(SAVE_CONFIRM_MODAL);
    }

    public boolean verifySnackbarError() {
        WebElement element = driverTools.waitForElementToAppear(SNACKBAR_ERROR).orElse(null);
        return element != null;
    }

    public boolean verifySnackbarErrorLayerPriorityExist() {
        WebElement element = driverTools.waitForElementToAppear(SNACKBAR_ERROR).orElse(null);
        if (element == null) return false;

        return driverTools.getText(element).contains("nudge rule with same layer and priority exist");
    }
    public boolean verifySnackbarErrorDuplicateNudgeName() {
        WebElement element = driverTools.waitForElementToAppear(SNACKBAR_ERROR).orElse(null);
        if (element == null) return false;

        return driverTools.getText(element).contains("duplicate nudge rule with vertical");
    }

}
