package com.tiket.web.dashboard.page.hotel.extranet;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class RatePlanPage <T extends WebDriver> extends BasePage<T> {

    public RatePlanPage(Session<T> session) {
        super(session);
    }

    private static final Locator RATE_PLAN_PAGE_TITLE = new Locator("RATE_PLAN_PAGE_TITLE", new By.ByXPath("//main//span[contains(@class,'Text_weight_bold') and contains(@class,'Text_variant_highEmphasis')]"));
    private static final Locator SEARCH_RATE_PLAN_INPUT = new Locator("SEARCH_RATE_PLAN_INPUT", new By.ByXPath("//input[@name='ratePlanName']"));
    private static final Locator SEARCH_RATE_PLAN_BUTTON = new Locator("SEARCH_RATE_PLAN_BUTTON", new By.ByXPath("//input[@name='ratePlanName']/ancestor::div[following-sibling::button][1]/following-sibling::button[1]"));
    private static final Locator ADD_NEW_RATE_PLAN_BUTTON = new Locator("ADD_NEW_RATE_PLAN_BUTTON", new By.ByXPath("//input[@name='ratePlanName']/ancestor::main[1]/div[1]/button"));
    private static final Locator EDIT_RATE_PLAN_BUTTON = new Locator("EDIT_RATE_PLAN_BUTTON", new By.ByXPath("//button[contains(@class, 'the-button primary button-v4 primary')]"));
    private static final Locator DELETE_RATE_PLAN_CONFIRM_BUTTON = new Locator("DELETE_RATE_PLAN_CONFIRM_BUTTON", new By.ByXPath("//footer//button[preceding-sibling::button and not(following-sibling::button)]"));
    private static final Locator DELETE_RATE_PLAN_SUCCESS_BACK_BUTTON = new Locator("DELETE_RATE_PLAN_SUCCESS_BACK_BUTTON", new By.ByXPath("//div[contains(@class,'Snackbar_type_success')]"));
    private static final Locator CUSTOM_RATE_PLAN_BUTTON = new Locator("CUSTOM_RATE_PLAN_BUTTON", new By.ByXPath("//div[contains(@class, 'options')]/div[1]"));
    private static final Locator WEEKLY_RATE_PLAN_BUTTON = new Locator("WEEKLY_RATE_PLAN_BUTTON", new By.ByXPath("//div[contains(@class, 'options')]/div[2]"));
    private static final Locator MONTHLY_RATE_PLAN_BUTTON = new Locator("MONTHLY_RATE_PLAN_BUTTON", new By.ByXPath("//div[contains(@class, 'options')]/div[3]"));
    private static final Locator CREATE_RATE_PLAN_PAGE = new Locator("CREATE_RATE_PLAN_PAGE", new By.ByXPath("//div[contains(@class,'the-create-rate-plans')]"));
    private static final Locator RATE_PLAN_NAME_INPUT = new Locator("RATE_PLAN_NAME_INPUT", new By.ByXPath("//input[@name='rate_plan_name']"));
    private static final Locator CLEAR_RATE_PLAN_NAME_BUTTON = new Locator("CLEAR_RATE_PLAN_NAME_BUTTON", new By.ByXPath("//input[@name=\"rate_plan_name\"]/following-sibling::div[@class=\"btn-area\"]"));
    private static final Locator NO_MEALS_RADIO_BUTTON = new Locator("NO_MEALS_RADIO_BUTTON", new By.ByXPath("(//div[contains(@class, 'meal-type-option')])[1]"));
    private static final Locator NON_REFUNDABLE_RADIO_BUTTON = new Locator("NON_REFUNDABLE_RADIO_BUTTON", new By.ByXPath("(//input[@name='cancellationPolicy']/parent::div)[1]"));
    private static final Locator ADD_NEW_RATE_PLAN_SUBMIT_BUTTON = new Locator("ADD_NEW_RATE_PLAN_SUBMIT_BUTTON", new By.ByXPath("//button[contains(@class, 'the-button primary button-v4 primary') and @type='submit']"));
    private static final Locator SUCCESS_TOAST = new Locator("SUCCESS_TOAST", new By.ByXPath("//div[@class='the-toast success opened']"));
    private static final Locator RATE_PLAN_TYPE_DROPDOWN = new Locator("RATE_PLAN_TYPE_DROPDOWN", new By.ByXPath("//input[@name='parentRatePlan']/parent::div"));
    private static final Locator CUSTOM_RP_PARENT = new Locator("CUSTOM_RP_PARENT", new By.ByXPath("(//span[contains(@class, 'popoverlist-list')])[4]"));

    private static Locator getDeleteRatePlanButtonByName(String text) {
        return new Locator("DELETE_RATE_PLAN_BUTTON", new By.ByXPath(String.format("//td[@class=\"table-cell\"][contains(text(),'%s')]/parent::tr//button[contains(@class,'action-button delete')]", text)));
    }
    
    public boolean isRatePlanPageLoaded(){
        driverTools.waitForElementToAppear(RATE_PLAN_PAGE_TITLE);
        return driverTools.isElementDisplayed(RATE_PLAN_PAGE_TITLE);
    }

    public boolean isCreateRatePlanPageLoaded(){
        driverTools.waitForElementToAppear(CREATE_RATE_PLAN_PAGE);
        return driverTools.isElementDisplayed(CREATE_RATE_PLAN_PAGE);
    }

    public Locator getLinkedRoomByNum(int num){
        return new Locator("LINKED_ROOM", new By.ByXPath(String.format("//div[@class='linked-rooms']/div[%d]/div", num)));
    }

    public void clickLinkedRoomByNumber(int num){
        Locator LINKED_ROOM = getLinkedRoomByNum(num);
        driverTools.waitForElementToAppear(LINKED_ROOM);
        driverTools.click(LINKED_ROOM);
    }

    public void searchRatePlan(String text) {
        WebElement input = driverTools.waitForElementToBeClickable(SEARCH_RATE_PLAN_INPUT).orElseThrow();
        driverTools.clearInput(input);
        driverTools.sendKeys(input, text);
        WebElement button = driverTools.waitForElementToBeClickable(SEARCH_RATE_PLAN_BUTTON).orElseThrow();
        driverTools.click(button);
    }

    public void clickAddNewRatePlanButton(){
        driverTools.waitForElementToAppear(ADD_NEW_RATE_PLAN_BUTTON);
        driverTools.click(ADD_NEW_RATE_PLAN_BUTTON);
    }
    
    public void clickEditRatePlanButton(){
        driverTools.waitForElementToAppear(EDIT_RATE_PLAN_BUTTON);
        driverTools.click(EDIT_RATE_PLAN_BUTTON);
    }

    public void deleteRatePlanByName(String text) {
        WebElement button = driverTools.waitForElementToBeClickable(getDeleteRatePlanButtonByName(text)).orElseThrow();
        driverTools.click(button);
        WebElement confirm = driverTools.waitForElementToBeClickable(DELETE_RATE_PLAN_CONFIRM_BUTTON).orElseThrow();
        driverTools.click(confirm);
        WebElement success = driverTools.waitForElementToBeClickable(DELETE_RATE_PLAN_SUCCESS_BACK_BUTTON).orElseThrow();
        driverTools.click(success);
    }
    
    public void selectCustomRatePlanType(){
        driverTools.waitForElementToAppear(CUSTOM_RATE_PLAN_BUTTON);
        driverTools.click(CUSTOM_RATE_PLAN_BUTTON);
    }

    public void selectWeeklyRatePlanType(){
        driverTools.waitForElementToAppear(WEEKLY_RATE_PLAN_BUTTON);
        driverTools.click(WEEKLY_RATE_PLAN_BUTTON);
    }

    public void selectMonthlyRatePlanType(){
        driverTools.waitForElementToAppear(MONTHLY_RATE_PLAN_BUTTON);
        driverTools.click(MONTHLY_RATE_PLAN_BUTTON);
    }
    
    public void fillInRatePlanNameInput(String text) {
        WebElement input = driverTools.waitForElementToAppear(RATE_PLAN_NAME_INPUT).orElseThrow();
        driverTools.clearInput(input);
        driverTools.sendKeys(input, text);
    }

    public void clearRatePlanNameInput() {
        WebElement button = driverTools.waitForElementToBeClickable(CLEAR_RATE_PLAN_NAME_BUTTON).orElseThrow();
        driverTools.click(button);
    }

    public void clickNoMealsOption(){
        driverTools.waitForElementToAppear(NO_MEALS_RADIO_BUTTON);
        driverTools.click(NO_MEALS_RADIO_BUTTON);
    }

    public void clickNonRefundableOption(){
        driverTools.waitForElementToAppear(NON_REFUNDABLE_RADIO_BUTTON);
        driverTools.click(NON_REFUNDABLE_RADIO_BUTTON);
    }

    public void clickAddNewRatePlanSubmitButton(){
        driverTools.waitForElementToAppear(ADD_NEW_RATE_PLAN_SUBMIT_BUTTON);
        driverTools.click(ADD_NEW_RATE_PLAN_SUBMIT_BUTTON);
    }

    public boolean isSuccessToastAppeared(){
        driverTools.waitForElementToAppear(SUCCESS_TOAST);
        return driverTools.isElementDisplayed(SUCCESS_TOAST);
    }

    public Locator getRatePlanByName(String name){
        return new Locator("RATE_PLAN", new By.ByXPath(String.format("//td[normalize-space()='%s']", name)));
    }

    public boolean isRatePlanAppeared(String ratePlanName){
        return driverTools.waitForElementToAppear(getRatePlanByName(ratePlanName), 3).isPresent();
    }

    public void selectCustomRatePlanParent(){
        driverTools.waitForElementToAppear(RATE_PLAN_TYPE_DROPDOWN);
        driverTools.click(RATE_PLAN_TYPE_DROPDOWN);

        driverTools.waitForElementToAppear(CUSTOM_RP_PARENT);
        driverTools.click(CUSTOM_RP_PARENT);
    }

    public Locator getEditButtonByRoomName(String roomName){
        return new Locator("EDIT_BUTTON", By.xpath(String.format("(//td[contains(text(),'%s')]/parent::tr//div[contains(@class, 'action-button-container')])[2]", roomName)));
    }

    public void clickEditButtonByRatePlanName(String roomName){
        Locator EDIT_BUTTON = getEditButtonByRoomName(roomName);
        driverTools.waitForElementToAppear(EDIT_BUTTON);
        driverTools.click(EDIT_BUTTON);
    }

    // Visible, clickable toggle element (new UI: div.Toggle_container with cursor-pointer; old UI: span.the-toggle-btn)
    public Locator getToggleButtonByRatePlanName(String ratePlanName){
        return new Locator("TOGGLE_BUTTON", By.xpath(String.format("//td[contains(normalize-space(.),'%s')]/ancestor::tr//div[contains(@class,'Toggle_container')] | //td[contains(normalize-space(.),'%s')]/ancestor::tr//span[contains(@class,'the-toggle-btn')]", ratePlanName, ratePlanName)));
    }

    // Underlying checkbox input for state check (styled/hidden, read by presence)
    public Locator getToggleButtonInputByRatePlanName(String ratePlanName){
        return new Locator("TOGGLE_BUTTON_INPUT", By.xpath(String.format("//td[contains(normalize-space(.),'%s')]/ancestor::tr//input[@type='checkbox' or contains(@class,'the-toggle')]", ratePlanName)));
    }

    // The row re-renders after each toggle, so the input can go stale between find and isSelected().
    // waitUntil ignores stale and re-finds; state is returned as a String because a Boolean false would keep it polling.
    public boolean isToggleCheckedByRatePlanName(String ratePlanName){
        driverTools.waitForElementToAppear(getToggleButtonByRatePlanName(ratePlanName));
        Locator toggleInput = getToggleButtonInputByRatePlanName(ratePlanName);
        return driverTools.waitUntil(d -> String.valueOf(d.findElement(toggleInput.by()).isSelected()), 10)
                .map(Boolean::parseBoolean)
                .orElse(false);
    }

    public boolean isRatePlanActiveByName(String ratePlanName) {
        return isToggleCheckedByRatePlanName(ratePlanName);
    }

    public boolean clickAndVerifyToggleButtonByRatePlanName(String ratePlanName, boolean isToggleOn) throws InterruptedException  {
        boolean initialState = isToggleCheckedByRatePlanName(ratePlanName);

        if ((initialState && isToggleOn) || (!initialState && !isToggleOn)) {
            return true;
        }

        // Re-resolve the toggle at click time; the row re-renders after each toggle, which staled a cached WebElement.
        Locator toggle = getToggleButtonByRatePlanName(ratePlanName);
        driverTools.waitForElementToAppear(toggle);
        driverTools.click(toggle);

        // Row re-renders after the toggle API responds; waitUntil ignores stale and re-finds the input until it reflects the new state.
        Locator toggleInput = getToggleButtonInputByRatePlanName(ratePlanName);
        return driverTools.waitUntil(d -> d.findElement(toggleInput.by()).isSelected() == isToggleOn, 10).isPresent();
    }

    public void refreshPage() {
        driverTools.refreshWebpage();
    }
}
