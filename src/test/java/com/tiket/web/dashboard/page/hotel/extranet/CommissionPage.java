package com.tiket.web.dashboard.page.hotel.extranet;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
public class CommissionPage<T extends WebDriver> extends BasePage<T> {

    public CommissionPage(Session<T> session) {
        super(session);
    }

    private static final Locator COMMISSION_FORM_PAGE = new Locator("COMMISSION_FORM_PAGE", new By.ByXPath("//div[@class='commission-container']"));
    private static final Locator ADDONS_COMMISSION_FORM_PAGE = new Locator("ADDONS_COMMISSION_FORM_PAGE", new By.ByXPath("//div[@class='the-card']"));
    private static final Locator REQUEST_CHANGE_BUTTON = new Locator("REQUEST_CHANGE_BUTTON", new By.ByXPath("//button[@class='the-button yellow button btn-footer v4']"));
    private static final Locator REQUEST_CHANGE_BUTTON_AGAIN = new Locator("REQUEST_CHANGE_BUTTON_AGAIN", new By.ByXPath("//button[@class='the-button yellow btn-footer v4']"));
    private static final Locator SEND_REQUEST_BUTTON = new Locator("SEND_REQUEST_BUTTON", new By.ByXPath("//button[@class='the-button yellow btn-footer']"));
    private static final Locator ADDONS_REQUEST_CHANGE_BUTTON = new Locator("ADDONS_REQUEST_CHANGE_BUTTON", new By.ByXPath("//button[@class='the-button yellow button btn-footer']"));
    private static final Locator COMMISSION_PERCENTAGE_INPUT = new Locator("COMMISSION_PERCENTAGE_INPUT", new By.ByXPath("//input[@name='percentage']"));
    private static final Locator MEALS_COMMISSION_PERCENTAGE_INPUT = new Locator("MEALS_COMMISSION_PERCENTAGE_INPUT", new By.ByXPath("//input[@name='percentage-0' and @class='input   ']"));
    private static final Locator TRANSPORTS_COMMISSION_PERCENTAGE_INPUT = new Locator("TRANSPORTS_COMMISSION_PERCENTAGE_INPUT", new By.ByXPath("//input[@name='percentage-2' and @class='input   ']"));
    private static final Locator ACTIVITIES_COMMISSION_PERCENTAGE_INPUT = new Locator("ACTIVITIES_COMMISSION_PERCENTAGE_INPUT", new By.ByXPath("//input[@name='percentage-3' and @class='input   ']"));
    private static final Locator ROOM_COMMISSION_PERCENTAGE_INPUT = new Locator("ROOM_COMMISSION_PERCENTAGE_INPUT", new By.ByXPath("//input[@name='percentage-1' and @class='input   ']"));
    private static final Locator SUCCESS_TOAST = new Locator("SUCCESS_TOAST", new By.ByXPath("//div[@class='the-toast success opened']"));
    private static final Locator ACTIVE_COMMISSION_MENU_LABEL = new Locator("ACTIVE_COMMISSION_MENU_LABEL", new By.ByXPath("//a[@class='nav-sub-list active' and @href='/v3/finance/commission']"));
    private static final Locator SEE_REQUEST_BUTTON = new Locator("SEE_REQUEST_BUTTON", new By.ByXPath("//button[@type='button' and contains(@class, 'commission-btn')]"));
    private static final Locator SEE_CHANGE_BUTTON = new Locator("SEE_CHANGE_BUTTON", new By.ByXPath("//button[@type='button' and contains(@class, ' the-flat-button button   btn-lg btn-primary')]"));
    private static final Locator COMMISSION_CHANGES_POPUP = new Locator("COMMISSION_CHANGES_POPUP", new By.ByXPath("//div[contains(@class,'modal-request-change')]"));
    private static final Locator PROPERTY_COMMISSION_CHANGES_APPROVE_BUTTON = new Locator("PROPERTY_COMMISSION_CHANGES_APPROVE_BUTTON", new By.ByXPath("//div[contains(@class,'modal-request-change')]//div[contains(@class,'button-row')]//button[contains(@class,'primary') and contains(@class,'commission-btn')]"));
    private static final Locator ADDONS_COMMISSION_CHANGES_APPROVE_BUTTON = new Locator("ADDONS_COMMISSION_CHANGES_APPROVE_BUTTON", new By.ByXPath("//div[contains(@class,'modal-request-change')]//button[contains(@class,'primary') and contains(@class,'btn-footer')]"));
    private static final Locator PROPERTY_AGREEMENT_FOLLOWUP_SAVE_WITHOUT_BUTTON = new Locator("PROPERTY_AGREEMENT_FOLLOWUP_SAVE_WITHOUT_BUTTON", new By.ByXPath("//div[@data-testid='the-modal' and contains(@class,'show')]//div[contains(@class,'modal-agreement')]//button[contains(@class,'primary') and contains(@class,'btn-footer')]"));
    private static final Locator CURRENT_PROPERTY_COMMISSION_IN_POPUP = new Locator("CURRENT_PROPERTY_COMMISSION_IN_POPUP", new By.ByXPath("//div[@class='before-section']//div[@class='subtitle']"));
    private static final Locator CURRENT_ADDONS_COMMISSION_IN_POPUP = new Locator("CURRENT_ADDONS_COMMISSION_IN_POPUP", new By.ByXPath("//div[contains(@class,'modal-request-change')]/div[2]"));
    private static final Locator PROPOSED_NEW_ADDONS_COMMISSION = new Locator("PROPOSED_NEW_ADDONS_COMMISSION", new By.ByXPath("//div[contains(@class,'modal-request-change')]/div[4]"));
    private static final Locator ACTIVE_ADDONS_COMMISSION_TAB = new Locator("ACTIVE_ADDONS_COMMISSION_TAB", new By.ByXPath("//div[@class='commission-container']//div[@class='the-tab-edit'][2]"));
    private static final Locator PROPOSED_NEW_MEAL_COMMISSION = new Locator("PROPOSED_NEW_MEAL_COMMISSION", new By.ByXPath("//div[contains(@class,'modal-request-change')]//div[4]//div[@class='the-row'][1]//div[2]"));
    private static final Locator PROPOSED_NEW_ROOM_COMMISSION = new Locator("PROPOSED_NEW_ROOM_COMMISSION", new By.ByXPath("//div[contains(@class,'modal-request-change')]//div[4]//div[@class='the-row'][2]//div[2]"));
    private static final Locator PROPOSED_NEW_TRANSPORT_COMMISSION = new Locator("PROPOSED_NEW_TRANSPORT_COMMISSION", new By.ByXPath("//div[contains(@class,'modal-request-change')]//div[4]//div[@class='the-row'][3]//div[2]"));
    private static final Locator PROPOSED_NEW_ACTIVITIES_COMMISSION = new Locator("PROPOSED_NEW_ACTIVITIES_COMMISSION", new By.ByXPath("//div[contains(@class,'modal-request-change')]//div[4]//div[@class='the-row'][4]//div[2]"));

    private static Locator getProposedNewPropertyCommission(String commissionValue) {
        return new Locator("PROPOSED_NEW_PROPERTY_COMMISSION", new By.ByXPath(String.format("//div[contains(@class,'modal-request-change')]//div[4]//div[contains(@class,'the-row')]//div[contains(normalize-space(), '%s')]", commissionValue)));
    }

    public void reloadBrowserPage() {
        driverTools.refreshWebpage();
    }

    public boolean isCommissionPageLoaded() {
        WebElement menu = driverTools.waitForElementToAppear(ACTIVE_COMMISSION_MENU_LABEL).orElseThrow();
        String attr = driverTools.getValueAttribute(menu, "href");
        return attr.contains("commission");
    }

    public boolean isAddOnsCommissionPageLoaded() {
        return driverTools.waitForElementToAppear(ADDONS_REQUEST_CHANGE_BUTTON, 5).isPresent();
    }

    public void clickAddOnsCommissionTab() {
        driverTools.waitForElementToAppear(ACTIVE_ADDONS_COMMISSION_TAB).orElseThrow();
        driverTools.scrollIntoWebView(ACTIVE_ADDONS_COMMISSION_TAB);
        try {
            driverTools.click(ACTIVE_ADDONS_COMMISSION_TAB);
        } catch (Exception e) {
            WebElement tab = driverTools.waitForElementToAppear(ACTIVE_ADDONS_COMMISSION_TAB).orElseThrow();
            driverTools.clickSvgOrNonHtmlElements(tab);
        }
    }

    public void clickRequestChangeButton() {
        driverTools.waitForElementToAppear(REQUEST_CHANGE_BUTTON);
        driverTools.clickWithJavascriptExecutor(REQUEST_CHANGE_BUTTON);
    }

    public void clickAddOnsRequestChangeButton() {
        driverTools.waitForElementToAppear(ADDONS_REQUEST_CHANGE_BUTTON);
        driverTools.clickWithJavascriptExecutor(ADDONS_REQUEST_CHANGE_BUTTON);
    }

    public void clickRequestChangeButtonAgain() {
        driverTools.waitForElementToAppear(REQUEST_CHANGE_BUTTON_AGAIN);
        driverTools.clickWithJavascriptExecutor(REQUEST_CHANGE_BUTTON_AGAIN);
    }

    public void clickSendRequestButton() {
        driverTools.waitForElementToAppear(SEND_REQUEST_BUTTON);
        driverTools.clickWithJavascriptExecutor(SEND_REQUEST_BUTTON);
    }

    public boolean isPropertyCommissionFormPageLoaded() {
        driverTools.waitForElementToAppear(COMMISSION_FORM_PAGE);
        return driverTools.isElementDisplayed(COMMISSION_FORM_PAGE);
    }

    public boolean isAddOnsCommissionFormPageLoaded() {
        driverTools.waitForElementToAppear(ADDONS_COMMISSION_FORM_PAGE);
        return driverTools.isElementDisplayed(ADDONS_COMMISSION_FORM_PAGE);
    }

    private String readTrimmedInputValue(Locator percentageInputLocator) {
        WebElement inputField = driverTools.waitForElementToAppear(percentageInputLocator).orElseThrow();
        String attributeValue = driverTools.getValueAttribute(inputField, "value");
        return attributeValue == null ? "" : attributeValue.trim();
    }

    public String getPropertyCommissionFromFormInput() {
        return readTrimmedInputValue(COMMISSION_PERCENTAGE_INPUT);
    }

    public String getMealsCommissionFromFormInput() {
        return readTrimmedInputValue(MEALS_COMMISSION_PERCENTAGE_INPUT);
    }

    public String getRoomComplementsCommissionFromFormInput() {
        return readTrimmedInputValue(ROOM_COMMISSION_PERCENTAGE_INPUT);
    }

    public String getTransportsCommissionFromFormInput() {
        return readTrimmedInputValue(TRANSPORTS_COMMISSION_PERCENTAGE_INPUT);
    }

    public String getActivitiesCommissionFromFormInput() {
        return readTrimmedInputValue(ACTIVITIES_COMMISSION_PERCENTAGE_INPUT);
    }

    private void enterPercentIntoInput(Locator inputLocator, String percentValue) {
        WebElement input = driverTools.waitForElementToAppear(inputLocator).orElseThrow();
        driverTools.clearInput(input);
        driverTools.sendKeys(input, percentValue);
    }

    public void enterCommission(String percentValue) {
        enterPercentIntoInput(COMMISSION_PERCENTAGE_INPUT, percentValue);
    }

    public void enterMealsCommission(String percentValue) {
        enterPercentIntoInput(MEALS_COMMISSION_PERCENTAGE_INPUT, percentValue);
    }

    public void enterRoomComplementsCommission(String percentValue) {
        enterPercentIntoInput(ROOM_COMMISSION_PERCENTAGE_INPUT, percentValue);
    }

    public void enterTransportsCommission(String percentValue) {
        enterPercentIntoInput(TRANSPORTS_COMMISSION_PERCENTAGE_INPUT, percentValue);
    }

    public void enterActivitiesCommission(String percentValue) {
        enterPercentIntoInput(ACTIVITIES_COMMISSION_PERCENTAGE_INPUT, percentValue);
    }

    public boolean isSuccessfullyRequestHotelCommissionChangeToastLoaded() {
        driverTools.waitForElementToAppear(SUCCESS_TOAST);
        return driverTools.isElementDisplayed(SUCCESS_TOAST);
    }

    public void clickSeeRequestButton() {
        driverTools.waitForElementToAppear(SEE_REQUEST_BUTTON).orElseThrow().click();
    }

    public void clickSeeChangeButton() {
        driverTools.waitForElementToAppear(SEE_CHANGE_BUTTON).orElseThrow().click();
    }

    /**
     * Property commission request modal: Approve, then follow-up agreement modal — primary "Save without agreement".
     */
    public void approvePropertyCommissionChangeRequestAndConfirmSaveWithoutAgreement() {
        driverTools.waitForElementToAppear(PROPERTY_COMMISSION_CHANGES_APPROVE_BUTTON);
        driverTools.clickWithJavascriptExecutor(PROPERTY_COMMISSION_CHANGES_APPROVE_BUTTON);
        driverTools.waitForElementToAppear(PROPERTY_AGREEMENT_FOLLOWUP_SAVE_WITHOUT_BUTTON);
        driverTools.clickWithJavascriptExecutor(PROPERTY_AGREEMENT_FOLLOWUP_SAVE_WITHOUT_BUTTON);
    }

    /**
     * Add-ons commission request modal (e.g. "Changes in Add Ons Commission"): Approve only — no agreement / save-without follow-up (property commission only).
     */
    public void approveAddOnsCommissionChangeRequest() {
        driverTools.waitForElementToAppear(ADDONS_COMMISSION_CHANGES_APPROVE_BUTTON);
        driverTools.clickWithJavascriptExecutor(ADDONS_COMMISSION_CHANGES_APPROVE_BUTTON);
    }

    public boolean isCommissionChangesRequestPopupLoaded() {
        driverTools.waitForElementToAppear(COMMISSION_CHANGES_POPUP);
        return driverTools.isElementDisplayed(COMMISSION_CHANGES_POPUP);
    }

    public boolean isCurrentCommissionLoaded() {
        driverTools.waitForElementToAppear(CURRENT_PROPERTY_COMMISSION_IN_POPUP);
        return driverTools.isElementDisplayed(CURRENT_PROPERTY_COMMISSION_IN_POPUP);
    }

    public boolean isCurrentAddOnsCommissionLoaded() {
        driverTools.waitForElementToAppear(CURRENT_ADDONS_COMMISSION_IN_POPUP);
        return driverTools.isElementDisplayed(CURRENT_ADDONS_COMMISSION_IN_POPUP);
    }

    public boolean isProposedNewCommissionLoaded(String expectedProposedPercentSnippet) {
        WebElement proposedRow = driverTools.waitForElementToAppear(getProposedNewPropertyCommission(expectedProposedPercentSnippet)).orElseThrow();
        String proposedText = driverTools.getText(proposedRow);
        return proposedText.contains(expectedProposedPercentSnippet);
    }

    /** Visible row text for property proposed commission (for test reports when matching snippet). */
    public String getProposedNewPropertyCommissionRowTextForMessage(String commissionValueSnippet) {
        return driverTools.waitForElementToAppear(getProposedNewPropertyCommission(commissionValueSnippet), 5)
                .map(el -> driverTools.getText(el).trim())
                .orElse("(no proposed row found for snippet: " + commissionValueSnippet + ")");
    }

    public boolean isProposedNewAddOnsCommissionLoaded() {
        driverTools.waitForElementToAppear(PROPOSED_NEW_ADDONS_COMMISSION);
        return driverTools.isElementDisplayed(PROPOSED_NEW_ADDONS_COMMISSION);
    }

    private String readVisibleText(Locator locator) {
        WebElement element = driverTools.waitForElementToAppear(locator).orElseThrow();
        return driverTools.getText(element);
    }

    public String getProposedMealsCommissionTextFromRequestPopup() {
        return readVisibleText(PROPOSED_NEW_MEAL_COMMISSION);
    }

    public String getProposedRoomComplementsCommissionTextFromRequestPopup() {
        return readVisibleText(PROPOSED_NEW_ROOM_COMMISSION);
    }

    public String getProposedTransportsCommissionTextFromRequestPopup() {
        return readVisibleText(PROPOSED_NEW_TRANSPORT_COMMISSION);
    }

    public String getProposedActivitiesCommissionTextFromRequestPopup() {
        return readVisibleText(PROPOSED_NEW_ACTIVITIES_COMMISSION);
    }

}
