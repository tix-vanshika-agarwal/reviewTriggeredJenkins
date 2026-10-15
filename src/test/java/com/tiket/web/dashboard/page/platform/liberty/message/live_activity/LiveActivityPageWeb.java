package com.tiket.web.dashboard.page.platform.liberty.message.live_activity;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LiveActivityPageWeb<T extends WebDriver> extends WebPage<T> {

    public LiveActivityPageWeb(Session<T> session) {
        super(session);
    }

    // List Page Locators
    private static final Locator WAITING_FOR_APPROVAL_ROW = new Locator("WAITING_FOR_APPROVAL_ROW",
            By.xpath("(//tr[.//div[contains(text(), 'Waiting for approval')]])[1]"));
    private static final Locator WAITING_FOR_APPROVAL_ACTION_BTN = new Locator("WAITING_FOR_APPROVAL_ACTION_BTN",
            By.xpath("(//tr[.//div[contains(text(), 'Waiting for approval')]]//div[@data-testid='live-activity-action-trigger'])[1]"));
    private static final Locator DUPLICATE_MENU_ITEM = new Locator("DUPLICATE_MENU_ITEM",
            By.xpath("//div[contains(@class, 'action_row') and contains(text(), 'Duplicate')]"));
    private static final Locator DELETE_MENU_ITEM = new Locator("DELETE_MENU_ITEM",
            By.xpath("//div[contains(@class, 'action_row') and contains(text(), 'Delete')]"));

    // Fallback confirmation button locator if a standard popup is triggered on Delete
    private static final Locator CONFIRM_DELETE_BTN = new Locator("CONFIRM_DELETE_BTN",
            By.xpath("//button[contains(@class, 'primary') and .//span[contains(text(), 'Delete') or contains(text(), 'Yes')]]"));

    // Step 1: Basic Classification Locators
    private static final Locator TEMPLATE_NAME_INPUT = new Locator("TEMPLATE_NAME_INPUT",
            By.xpath("//input[@id='template-name']"));
    private static final Locator TRIP_RADIO = new Locator("TRIP_RADIO",
            By.xpath("//input[@name='templateType' and @value='\"TRIP\"']"));
    private static final Locator NON_TRIP_RADIO = new Locator("NON_TRIP_RADIO",
            By.xpath("//input[@name='templateType' and @value='\"NON_TRIP\"']"));

    // Reusable Next / Submit / Save Buttons
    private static final Locator NEXT_OR_SUBMIT_BUTTON = new Locator("NEXT_OR_SUBMIT_BUTTON",
            By.xpath("//button[.//span[text()='Next' or text()='Submit']]"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON",
            By.xpath("//button[.//span[text()='Save']]"));

    // Step 2: Metadata & Content Locators
    private static final Locator DYNAMIC_LINK_RADIO = new Locator("DYNAMIC_LINK_RADIO",
            By.xpath("//input[@name='content-type-step-0-click-url-content-type' and @value='\"DYNAMIC\"']"));
    private static final Locator LINK_INPUT = new Locator("LINK_INPUT",
            By.xpath("//input[@id='link']"));

    private static final Locator DYNAMIC_SUBTEXT_RADIO = new Locator("DYNAMIC_SUBTEXT_RADIO",
            By.xpath("//input[@name='content-type-step-0-subtitle-1-content-type' and @value='\"DYNAMIC\"']"));
    private static final Locator SUBTEXT_ID_TEXTAREA = new Locator("SUBTEXT_ID_TEXTAREA",
            By.xpath("(//textarea[@id='id'])[1]"));
    private static final Locator SUBTEXT_EN_TEXTAREA = new Locator("SUBTEXT_EN_TEXTAREA",
            By.xpath("(//textarea[@id='en'])[1]"));
    private static final Locator SAME_AS_ABOVE_BTN = new Locator("SAME_AS_ABOVE_BTN",
            By.xpath("//button[.//span[text()='Same as above']]"));

    // Step 3: JSON and Visual Preview Locators
    private static final Locator JSON_PREVIEW_BLOCK = new Locator("JSON_PREVIEW_BLOCK",
            By.xpath("(//pre[contains(@class, 'code_block')])[1]"));

    private Locator getPreviewHighEmphasisTextLoc(int index) {
        return new Locator("PREVIEW_HIGH_EMP", By.xpath("(//div[contains(@class, 'lang_block')])[" + index + "]//span[contains(@class, 'text_high_emphasis')]"));
    }

    private Locator getPreviewLowEmphasisTextLoc(int index) {
        return new Locator("PREVIEW_LOW_EMP", By.xpath("(//div[contains(@class, 'lang_block')])[" + index + "]//span[contains(@class, 'text_low_emphasis')]"));
    }

    private Locator getTemplateRowByName(String templateName) {
        return new Locator("TEMPLATE_ROW", By.xpath("//tr[.//td[text()='" + templateName + "']]"));
    }

    private Locator getTemplateActionBtnByName(String templateName) {
        return new Locator("TEMPLATE_ACTION_BTN", By.xpath("//tr[.//td[text()='" + templateName + "']]//div[@data-testid='live-activity-action-trigger']"));
    }

    public void goToPage() {
        driverTools.dummyWait(3);
        switch (Configs.ENVIRONMENT) {
            case GK -> driverTools.get("https://gatotkaca.tiket.com/admin/message/live-activity/list");
            case PREPROD -> driverTools.get("https://preprod.tiket.com/admin/message/live-activity/list");
            case PROD -> driverTools.get("https://www.tiket.com/admin/message/live-activity/list");
        }
        driverTools.waitForWebToLoad();
    }

    public String getFirstWaitingForApprovalTemplateName() {
        driverTools.waitForElementToAppear(WAITING_FOR_APPROVAL_ROW);
        // Assumes the template name is in the 2nd column of the table row based on provided HTML
        return driverTools.getText(new Locator("TEMPLATE_NAME", By.xpath("(//tr[.//div[contains(text(), 'Waiting for approval')]])[1]/td[2]"))).orElse("");
    }

    public void clickActionOnWaitingForApprovalTemplate() {
        driverTools.waitForElementToAppear(WAITING_FOR_APPROVAL_ACTION_BTN);
        driverTools.clickWithJavascriptExecutor(WAITING_FOR_APPROVAL_ACTION_BTN);
    }

    public void clickDuplicate() {
        driverTools.waitForElementToAppear(DUPLICATE_MENU_ITEM);
        driverTools.clickWithJavascriptExecutor(DUPLICATE_MENU_ITEM);
        driverTools.waitForWebToLoad();
    }

    public void updateTemplateName(String fullTemplateName) {
        driverTools.waitForElementToAppear(TEMPLATE_NAME_INPUT);
        driverTools.clearInput(TEMPLATE_NAME_INPUT);

        // The UI has a static "Order_" prefix label.
        // If the fullTemplateName from param contains "order_", strip it before typing.
        String textToType = fullTemplateName;
        if (textToType.toLowerCase().startsWith("order_")) {
            textToType = textToType.substring(6); // Removes the first 6 characters ("order_")
        }
        driverTools.clearInput(TEMPLATE_NAME_INPUT);
        driverTools.sendKeys(TEMPLATE_NAME_INPUT, textToType);
    }

    public void selectTemplateType(String type) {
        if (type.equalsIgnoreCase("Non Trip")) {
            driverTools.waitForElementToAppear(TRIP_RADIO);
            driverTools.clickWithJavascriptExecutor(NON_TRIP_RADIO);
        } else {
            driverTools.waitForElementToAppear(TRIP_RADIO);
            driverTools.clickWithJavascriptExecutor(TRIP_RADIO);
        }
    }

    public void clickNextOrSubmitButton() {
        driverTools.waitForElementToAppear(NEXT_OR_SUBMIT_BUTTON);
        driverTools.scrollElementToCenter(NEXT_OR_SUBMIT_BUTTON);
        driverTools.clickWithJavascriptExecutor(NEXT_OR_SUBMIT_BUTTON);
        driverTools.dummyWait(2);
    }

    public void fillLink(String link) {
        driverTools.waitForElementToAppear(DYNAMIC_LINK_RADIO);
        driverTools.clickWithJavascriptExecutor(DYNAMIC_LINK_RADIO);
        driverTools.clickWithJavascriptExecutor(LINK_INPUT);
        driverTools.clearInput(LINK_INPUT);
        driverTools.sendKeys(LINK_INPUT, link);
    }

    public void fillSubtext(String idText, String enText) {
        driverTools.waitForElementToAppear(DYNAMIC_SUBTEXT_RADIO);
        driverTools.clickWithJavascriptExecutor(DYNAMIC_SUBTEXT_RADIO);

        driverTools.clickWithJavascriptExecutor(SUBTEXT_ID_TEXTAREA);
        driverTools.clearInput(SUBTEXT_ID_TEXTAREA);
        driverTools.sendKeys(SUBTEXT_ID_TEXTAREA, idText);

        driverTools.clickWithJavascriptExecutor(SUBTEXT_EN_TEXTAREA);
        driverTools.clearInput(SUBTEXT_EN_TEXTAREA);
        driverTools.sendKeys(SUBTEXT_EN_TEXTAREA, enText);
    }

    public void clickSameAsAbove() {
        driverTools.clickWithJavascriptExecutor(SAME_AS_ABOVE_BTN);
    }

    public String getPreviewHighEmphasisText(int index) {
        return driverTools.getText(getPreviewHighEmphasisTextLoc(index)).orElse("");
    }

    public String getPreviewLowEmphasisText(int index) {
        return driverTools.getText(getPreviewLowEmphasisTextLoc(index)).orElse("");
    }

    public String getJsonPreviewText() {
        driverTools.waitForElementToAppear(JSON_PREVIEW_BLOCK);
        return driverTools.getText(JSON_PREVIEW_BLOCK).orElse("");
    }

    public void clickSaveButton() {
        driverTools.clickWithJavascriptExecutor(SAVE_BUTTON);
        driverTools.dummyWait(3);
    }

    public boolean isTemplateExist(String templateName) {
        return driverTools.isElementPresent(getTemplateRowByName(templateName));
    }

    public void deleteTemplate(String templateName) {
        Locator actionBtn = getTemplateActionBtnByName(templateName);
        driverTools.waitForElementToAppear(actionBtn);
        driverTools.clickWithJavascriptExecutor(actionBtn);

        driverTools.waitForElementToAppear(DELETE_MENU_ITEM);
        driverTools.clickWithJavascriptExecutor(DELETE_MENU_ITEM);

        if (driverTools.isElementPresent(CONFIRM_DELETE_BTN)) {
            driverTools.clickWithJavascriptExecutor(CONFIRM_DELETE_BTN);
        }
        driverTools.dummyWait(3);
    }

    /**
     * Safely attempts to delete a template during the test cleanup phase.
     * Catches any exceptions to prevent failing the overall test execution
     * and uses the framework logger instead of standard output.
     */
    public void safeDeleteTemplate(String templateName) {
        try {
            goToPage();

            if (isTemplateExist(templateName)) {
                deleteTemplate(templateName);

                if (isTemplateExist(templateName)) {
                    logger.warn("Warning: Template '" + templateName + "' was not deleted successfully during cleanup.");
                } else {
                    logger.log("Template '" + templateName + "' deleted successfully during cleanup.");
                }
            } else {
                logger.log("Cleanup skipped: Template '" + templateName + "' does not exist.");
            }
        } catch (Throwable e) {
            logger.warn("Exception occurred during cleanup of template '" + templateName + "': " + e.getMessage());
        }
    }
}