package com.tiket.web.dashboard.page.hotel.extranet;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Optional;

public class ChildrenRatePlanPage <T extends WebDriver> extends BasePage<T> {

    public ChildrenRatePlanPage(Session<T> session) {
        super(session);
    }

    private static final Locator ACTIVE_CHILDREN_RATE_PLAN_MENU_LABEL = new Locator("ACTIVE_CHILDREN_RATE_PLAN_MENU_LABEL", new By.ByXPath("//span[contains(@class,'#007BFF') and contains(@class,'font-medium')]//ancestor::a"));
    private static final Locator ADD_CHILDREN_RATE_PLAN_FORM_PAGE = new Locator("ADD_CHILDREN_RATE_PLAN_FORM_PAGE", new By.ByXPath("//form[@autocomplete='off' and contains(@class,'mt-3')]"));
    private static final Locator CHILDREN_RATE_PLAN_COACHMARK_OK_BUTTON = new Locator("CHILDREN_RATE_PLAN_COACHMARK_OK_BUTTON", new By.ByXPath("//button[contains(@class,'tix-coachmark-button-primary')]"));
    private static final Locator CHILDREN_RATE_PLAN_COACHMARK_SKIP_BUTTON = new Locator("CHILDREN_RATE_PLAN_COACHMARK_SKIP_BUTTON", new By.ByXPath("//button[contains(@class,'coachmark-button-secondary')]"));
    private static final Locator ADD_CHILDREN_RATE_PLAN_BUTTON = new Locator("ADD_CHILDREN_RATE_PLAN_BUTTON", new By.ByXPath("//*[contains(@class,'ActionBanner')]//button"));
    private static final Locator ADD_CHILDREN_RATE_PLAN_COACHMARK_SKIP_BUTTON = new Locator("ADD_CHILDREN_RATE_PLAN_COACHMARK_SKIP_BUTTON", new By.ByXPath("//button[contains(@class,'tix-coachmark-button-secondary')]"));
    private static final Locator CHILDREN_RATE_PLAN_PRICE_INPUT = new Locator("CHILDREN_RATE_PLAN_PRICE_INPUT", new By.ByXPath("//input[@id='enter-the-price' or @id='masukkan-harga']"));
    private static final Locator ADD_CHILDREN_RATE_PLAN_SUBMIT_BUTTON = new Locator("ADD_CHILDREN_RATE_PLAN_SUBMIT_BUTTON", new By.ByXPath("(//button[@type='submit'])[1]"));
    private static final Locator SAVE_CHILDREN_RATE_PLAN_CONFIRM_BUTTON = new Locator("SAVE_CHILDREN_RATE_PLAN_CONFIRM_BUTTON", new By.ByXPath("//div[contains(@class,'Dialog_pop_up')]//div[contains(@class,'Figure_footer')]//button[contains(@class,'Button_variant_primary')]"));
    private static final Locator DELETE_CHILDREN_RATE_PLAN_CONFIRM_MODAL = new Locator("DELETE_CHILDREN_RATE_PLAN_CONFIRM_MODAL", new By.ByXPath("delete-rateplan-modal-root"));
    private static final Locator DELETE_CHILDREN_RATE_PLAN_CONFIRM_BUTTON = new Locator("DELETE_CHILDREN_RATE_PLAN_CONFIRM_BUTTON", new By.ByXPath("//span[@data-testid='confirm-delete-crp']"));
    private static final Locator CHILDREN_RATE_PLAN_NAME_INPUT = new Locator("CHILDREN_RATE_PLAN_NAME_INPUT", new By.ByXPath("//input[@name='name']"));
    private static final Locator SUCCESS_SNACKBAR = new Locator("SUCCESS_SNACKBAR", new By.ByXPath("//div[contains(@class,'Snackbar_text_content')]"));
    private static final Locator SELECT_RATE_PLAN_TRIGGER = new Locator("SELECT_RATE_PLAN_TRIGGER", new By.ByXPath("(//div[contains(@class,'styles_input_inner_wrapper')])[2]"));
    private static final Locator SELECT_RATE_PLAN_SAVE_BUTTON = new Locator("SELECT_RATE_PLAN_SAVE_BUTTON", new By.ByXPath("//ul[contains(@class,'rounded-xl')]//button[contains(@class,'Button_variant_primary')]"));
    private static final Locator RATE_PLAN_CHIP = new Locator("RATE_PLAN_CHIP", new By.ByXPath("(//button[contains(@class,'Chip_chip') and contains(@class,'Chip_active')])[1]"));

    private static Locator getRatePlanLabelByName(String text) {
        return new Locator("RATE_PLAN_LABEL", new By.ByXPath(String.format("//ul[contains(@class,'rounded')]//label[.//span[normalize-space()='%s']]", text)));
    }

    private static Locator getChildrenRatePlanByName(String text) {
        return new Locator("RATE_PLAN_NAME", new By.ByXPath(String.format("//td//span[text()='%s']", text)));
    }

    private static Locator getSettingButtonByName(String text) {
        return new Locator("SETTING_BUTTON", new By.ByXPath(String.format("//*[name()='svg' and @data-testid='crp-icon-more']", text)));
    }

    private static Locator getEditButtonByName(String text) {
        return new Locator("EDIT_BUTTON", new By.ByXPath(String.format("(//td[text()='%s']/following-sibling::td[last()]//div[contains(@class,'fixed')]/div[contains(@class,'flex')])[1]", text)));
    }

    private static Locator getDeleteButtonByName(String text) {
        return new Locator("DELETE_BUTTON",new By.ByXPath("(//div[.//*[local-name()='svg'] and count(.//*[local-name()='svg'])=1])[last()]"));
    }

    private static Locator getToggleButtonByName(String text) {
        return new Locator("TOGGLE_BUTTON", new By.ByXPath(String.format("//td[text()='%s']/following-sibling::td[last()-1]//div[contains(@class,'Toggle_container')]", text)));
    }

    private static Locator getToggleStateByName(String text) {
        return new Locator("TOGGLE_STATE", new By.ByXPath(String.format("//td[text()='%s']/following-sibling::td[last()-1]//*[@data-testid=\"toggle-crp-status\"]", text)));
    }

    private static Locator getChildrenRatePlanBySettingName(String text) {
        return new Locator("RATE_PLAN_NAME_EDIT", new By.ByXPath(String.format("//tr/td[1][normalize-space(.)='%s']", text)));
    }

    public boolean isChildrenRatePlanPageLoaded(){
        WebElement menu = driverTools.waitForElementToAppear(ACTIVE_CHILDREN_RATE_PLAN_MENU_LABEL).orElseThrow();
        String attr = driverTools.getValueAttribute(menu, "href");
        return attr.contains("children-rate-plans");
    }

    public boolean isAddChildrenRatePlanFormPageLoaded(){
        driverTools.waitForElementToAppear(ADD_CHILDREN_RATE_PLAN_FORM_PAGE);
        return driverTools.isElementDisplayed(ADD_CHILDREN_RATE_PLAN_FORM_PAGE);
    }

    public boolean isSuccessSnackbarAppeared(){
        return driverTools.waitForElementToAppear(SUCCESS_SNACKBAR).isPresent();
    }

    public boolean isSuccessSnackbarDisappeared(){
        return driverTools.waitForElementToDisappear(SUCCESS_SNACKBAR);
    }

    public void dismissChildrenRatePlanLandingCoachmark() {
        try {
            WebElement coachmark = driverTools.waitForElementToBeClickable(CHILDREN_RATE_PLAN_COACHMARK_OK_BUTTON).orElseThrow();
            driverTools.click(coachmark);
        } catch (Exception ignored) {

        }
    }

    public void skipChildrenRatePlanLandingCoachmark() {
        try {
            WebElement coachmark = driverTools.waitForElementToBeClickable(CHILDREN_RATE_PLAN_COACHMARK_SKIP_BUTTON).orElseThrow();
            driverTools.click(coachmark);
        } catch (Exception ignored) {

        }
    }

    public void clickAddChildrenRatePlanButton(){
        driverTools.waitForElementToAppear(ADD_CHILDREN_RATE_PLAN_BUTTON).orElseThrow().click();
    }

    public void skipAddChildrenRatePlanLandingCoachmark() {
        try {
            WebElement coachmark = driverTools.waitForElementToBeClickable(ADD_CHILDREN_RATE_PLAN_COACHMARK_SKIP_BUTTON).orElseThrow();
            driverTools.click(coachmark);
        } catch (Exception ignored) {

        }
    }

    public void clickAddChildrenRatePlanSubmitButton(){
        driverTools.waitForElementToAppear(ADD_CHILDREN_RATE_PLAN_SUBMIT_BUTTON);
        driverTools.click(ADD_CHILDREN_RATE_PLAN_SUBMIT_BUTTON);
    }

    public void clickSaveChildrenRatePlanConfirmButton(){
        WebElement confirmBtn = driverTools.waitForElementToBeClickable(SAVE_CHILDREN_RATE_PLAN_CONFIRM_BUTTON, 10).orElseThrow();
        driverTools.click(confirmBtn);
    }

    public void fillOutAllChildrenRatePlanNameInput(String name){
        WebElement input = driverTools.waitForElementToAppear(CHILDREN_RATE_PLAN_NAME_INPUT).orElseThrow();
        driverTools.clearInput(input);
        driverTools.sendKeys(input, name);
    }

    public void selectRatePlan(String name) {
        WebElement selectTrigger = driverTools.waitForElementToPresence(SELECT_RATE_PLAN_TRIGGER).orElseThrow();
        driverTools.clickElementByCoordinateDWeb(selectTrigger);
        WebElement label = driverTools.waitForElementToPresence(getRatePlanLabelByName(name), 10).orElseThrow();
        driverTools.clickWithJavascriptExecutor(label);
        WebElement saveButton = driverTools.waitForElementToPresence(SELECT_RATE_PLAN_SAVE_BUTTON, 10).orElseThrow();
        driverTools.clickWithJavascriptExecutor(saveButton);
    }

    public boolean isRatePlanChipTextMatching(String expectedRatePlanName) {
        return driverTools.waitForElementToAppear(RATE_PLAN_CHIP, 5)
                .flatMap(el -> Optional.ofNullable(driverTools.getText(el)).map(String::trim))
                .map(text -> text.contains(expectedRatePlanName.trim()))
                .orElse(false);
    }

    public void enterRate(String text) {
        WebElement input = driverTools.waitForElementToPresence(CHILDREN_RATE_PLAN_PRICE_INPUT, 30).orElseThrow();
        driverTools.clickWithJavascriptExecutor(input);
        driverTools.clearWithJsExecutor(input);
        driverTools.sendKeysWithJsExecutorForceInputValueAndTriggerEvents(CHILDREN_RATE_PLAN_PRICE_INPUT, text);
    }

    public void clickSettingByChildrenRatePlanName(String name){
        WebElement settingButton = driverTools.waitForElementToBeClickable(getSettingButtonByName(name)).orElseThrow();
        driverTools.click(settingButton);
    }

    public void clickEditChildrenRatePlan(String text){
        WebElement editButton = driverTools.waitForElementToBeClickable(getEditButtonByName(text)).orElseThrow();
        driverTools.click(editButton);
    }

    public boolean isChildrenRatePlanActiveByName(String text) {
        return driverTools.isElementAttributePresent(getToggleStateByName(text), "checked");
    }

    public void clickChildrenRatePlanToggleByName(String text, boolean expectedStatus) {
        WebElement toggle = driverTools.waitForElementToBeClickable(getToggleButtonByName(text)).orElseThrow();
        if (((!isChildrenRatePlanActiveByName(text)) && expectedStatus) || (isChildrenRatePlanActiveByName(text)) && (!expectedStatus)){
            driverTools.click(toggle);
        }
    }

    public boolean isDeleteChildrenRatePlanConfirmModalDisplayed() {
        return driverTools.waitForElementToAppear(DELETE_CHILDREN_RATE_PLAN_CONFIRM_MODAL).isPresent();
    }

    public void deleteChildrenRatePlan(String text) {
        WebElement delete = driverTools.waitForElementToBeClickable(getDeleteButtonByName(text)).orElseThrow();
        driverTools.click(delete);
        WebElement button = driverTools.waitForElementToBeClickable(DELETE_CHILDREN_RATE_PLAN_CONFIRM_BUTTON).orElseThrow();
        driverTools.click(button);
    }

    public boolean isChildrenRatePlanDisplayed(String name) {
        return driverTools.waitForElementToAppear(getChildrenRatePlanByName(name), 3).isPresent();
    }

    public boolean isChildrenRatePlanDisplayedBySettingName(String name) {
        return driverTools.waitForElementToAppear(getChildrenRatePlanBySettingName(name), 3).isPresent();
    }
}
