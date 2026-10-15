package com.tiket.web.dashboard.page.hotel.extranet;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Optional;

import static org.hamcrest.Matchers.*;

public class YourPromotionsPage <T extends WebDriver> extends BasePage<T> {
    public YourPromotionsPage(Session<T> session) {
        super(session);
    }

    private static final Locator YOUR_PROMOTIONS_PAGE = new Locator("YOUR_PROMOTIONS_PAGE", new By.ByXPath("//section//main"));
    private static final Locator EDIT_BUTTON = new Locator("EDIT_BUTTON", new By.ByXPath("(//div[@data-focus-lock-disabled=\"false\"]//span)[1]"));
    private static final Locator DEACTIVATE_CONFIRMATION_BUTTON = new Locator("DEACTIVATE_CONFIRMATION_BUTTON", new By.ByXPath("//button[.//span[normalize-space()='Deactivate' or normalize-space()='Nonaktifkan']]"));
    private static final Locator DELETE_PROMO_BUTTON = new Locator("DELETE_PROMO_BUTTON", new By.ByXPath("//span[contains(@class,'alert')]/parent::div"));
    private static final Locator DELETE_PROMO_CONFIRM_BUTTON = new Locator("DELETE_PROMO_CONFIRM_BUTTON", new By.ByXPath("//div[contains(@class,'pop_up')]//button[contains(@class,'primary')]"));
    private static final Locator SUCCESS_SNACKBAR = new Locator("SUCCESS_SNACKBAR", new By.ByXPath("(//div[contains(@class,'Snackbar_text_content')])[last()]"));
    private static final Locator ADD_NEW_PROMO_BUTTON = new Locator("ADD_NEW_PROMO_BUTTON", new By.ByXPath("//a[contains(@href,'/promotions-and-campaigns/add')]//button"));
    private static final Locator SEARCH_PROMO_NAME_INPUT = new Locator("SEARCH_PROMO_NAME_INPUT", new By.ByXPath("//input[@name='promoName']"));
    private static final Locator DEACTIVATE_CONFIRMATION_CANCEL_BUTTON = new Locator("DEACTIVATE_CONFIRMATION_CANCEL_BUTTON", new By.ByXPath("//button[.//span[normalize-space()='Cancel' or normalize-space()='Batalkan']]"));
    private static final Locator EDIT_MENU_ITEM = new Locator("EDIT_MENU_ITEM", new By.ByXPath("(//div[@data-testid='dropdown-item'])[1]"));
    private static final Locator DELETE_MENU_ITEM = new Locator("DELETE_MENU_ITEM", new By.ByXPath("(//div[@data-testid='dropdown-item'])[3]"));
    private static final Locator DELETE_CONFIRMATION_BUTTON = new Locator("DELETE_CONFIRMATION_BUTTON", new By.ByXPath("//button[.//span[normalize-space()='Delete' or normalize-space()='Hapus']]"));
    private static final Locator NO_SEARCH_RESULTS_TITLE = new Locator("NO_SEARCH_RESULTS_TITLE", new By.ByXPath("//img[@alt='no-filter-result']/ancestor::section[1]//h2"));


    public static Locator getActionButtonByPromoName(String text) {
        return new Locator("ACTION_BUTTON", new By.ByXPath("(//div[@class='cursor-pointer w-fit'])[3]"));
    }

    public static Locator getCheckboxStateByPromoName(String text) {
        return new Locator("PROMO_CHECKBOX", new By.ByXPath(String.format("//td[text()='%s']/parent::tr//input[contains(@type,'checkbox')]", text)));
    }

    public static Locator getToggleButtonByPromoName(String text) {
        return new Locator("PROMO_TOGGLE", new By.ByXPath(String.format("//td[text()='%s']/parent::tr//label[.//input[@class='Toggle_hidden_checkbox__croZa']]", text)));
    }

    public static Locator getPromoRowByName(String text) {
        return new Locator("PROMO_ROW", new By.ByXPath(String.format("//td[text()='%s']", text)));
    }

    public static Locator getRowActionMenuButtonByPromoName(String text) {
        return new Locator("ROW_ACTION_MENU_BUTTON", new By.ByXPath(String.format("//td[text()='%s']/parent::tr//div[contains(@class,'cursor-pointer') and contains(@class,'w-fit')]", text)));
    }

    public boolean isYourPromotionsPageLoaded(){
        driverTools.waitForElementToAppear(YOUR_PROMOTIONS_PAGE);
        return driverTools.isElementDisplayed(YOUR_PROMOTIONS_PAGE);
    }

    public void clickEditButton(){
        WebElement button = driverTools.waitForElementToBeClickable(EDIT_BUTTON).orElseThrow();
        try {
            driverTools.clickWithJavascriptExecutor(button);
        } catch (ElementClickInterceptedException e) {
            driverTools.scrollIntoWebView(DELETE_PROMO_BUTTON);
            driverTools.clickWithJavascriptExecutor(button);
        }
    }

    public boolean getPromoToggleState(String name) {
        WebElement checkbox = driverTools.waitForElementToPresence(getCheckboxStateByPromoName(name)).orElseThrow();
        return driverTools.isElementSelected(checkbox);
    }

    public void clickPromoToggle(String name, boolean expectedStatus) {
        boolean initialState = getPromoToggleState(name);

        WebElement toggle = driverTools.waitForElementToBeClickable(getToggleButtonByPromoName(name)).orElseThrow();

        if (initialState && !expectedStatus) {
            driverTools.clickWithJavascriptExecutor(toggle);
            WebElement confirm = driverTools.waitForElementToBeClickable(DEACTIVATE_CONFIRMATION_BUTTON).orElseThrow();
            driverTools.click(confirm);
        } else if (!initialState && expectedStatus) {
            driverTools.clickWithJavascriptExecutor(toggle);
        }
    }

    public void clickActionByPromoName(String text) {
        WebElement button = driverTools.waitForElementToBeClickable(getActionButtonByPromoName(text)).orElseThrow();
        driverTools.clickWithJavascriptExecutor(button);
    }

    public void deletePromo(){
        WebElement button = driverTools.waitForElementToBeClickable(DELETE_PROMO_BUTTON).orElseThrow();
        driverTools.click(button);
        WebElement confirm = driverTools.waitForElementToBeClickable(DELETE_PROMO_CONFIRM_BUTTON).orElseThrow();
        driverTools.click(confirm);
    }

    public boolean isSuccessSnackbarAppeared() {
        return driverTools.waitForElementToAppear(SUCCESS_SNACKBAR).isPresent();
    }

    public void refreshPage() {
        driverTools.refreshWebpage();
    }

    public void clickAddNewPromoButton() {
        WebElement button = driverTools.waitForElementToBeClickable(ADD_NEW_PROMO_BUTTON).orElseThrow();
        driverTools.scrollElementToCenter(button);
        driverTools.click(button);
    }

    public void clickSearchPromoNameField() {
        WebElement input = driverTools.waitForElementToAppear(SEARCH_PROMO_NAME_INPUT).orElseThrow();
        driverTools.click(input);
    }

    public void fillOutSearchPromoNameAndSearch(String name) {
        WebElement input = driverTools.waitForElementToAppear(SEARCH_PROMO_NAME_INPUT).orElseThrow();
        driverTools.scrollElementToCenter(input);
        driverTools.clearInput(input);
        driverTools.sendKeys(input, name);
        input.sendKeys(Keys.ENTER);
    }

    public boolean isPromoPresentInList(String name) {
        Optional<WebElement> row = driverTools.waitForElementToAppear(getPromoRowByName(name));
        row.ifPresent(driverTools::scrollElementToCenter);
        return row.isPresent();
    }

    public void clickStatusToggle(String name) {
        WebElement toggle = driverTools.waitForElementToBeClickable(getToggleButtonByPromoName(name)).orElseThrow();
        driverTools.clickWithJavascriptExecutor(toggle);
    }

    public boolean isDeactivateConfirmationPopupDisplayed() {
        boolean deactivateButtonAppeared = driverTools.waitForElementToAppear(DEACTIVATE_CONFIRMATION_BUTTON, 5).isPresent();
        boolean cancelButtonAppeared = driverTools.waitForElementToAppear(DEACTIVATE_CONFIRMATION_CANCEL_BUTTON, 5).isPresent();
        return deactivateButtonAppeared && cancelButtonAppeared;
    }

    public void clickCancelInDeactivateConfirmation() {
        WebElement button = driverTools.waitForElementToBeClickable(DEACTIVATE_CONFIRMATION_CANCEL_BUTTON).orElseThrow();
        driverTools.click(button);
    }

    public void clickDeactivateInDeactivateConfirmation() {
        WebElement button = driverTools.waitForElementToBeClickable(DEACTIVATE_CONFIRMATION_BUTTON).orElseThrow();
        driverTools.click(button);
    }

    public void verifyStatusChangeSnackbarMessage(String expectedEn, String expectedId) {
        WebElement snackbar = driverTools.waitForElementToAppear(SUCCESS_SNACKBAR, 5).orElseThrow(() -> new NoSuchElementException("Status change snackbar did not appear"));
        String actualMessage = snackbar.getText().trim();
        Assertion.assertThat("Verify status change snackbar message. Expected En: " + expectedEn + " | Expected Id: " + expectedId + " | Actual: " + actualMessage, actualMessage, anyOf(is(expectedEn), is(expectedId)));
    }

    public void waitForSnackbarToDismiss() {
        driverTools.waitForElementToDisappear(SUCCESS_SNACKBAR, 10);
    }

    public void clickRowActionMenu(String name) {
        Locator locator = getRowActionMenuButtonByPromoName(name);
        driverTools.waitForElementToBeClickable(locator).orElseThrow();
        driverTools.scrollElementToCenter(locator);
        WebElement button = driverTools.waitForElementToBeClickable(locator).orElseThrow();
        driverTools.clickWithJavascriptExecutor(button);
    }

    public void clickEditMenuItem() {
        WebElement item = driverTools.waitForElementToBeClickable(EDIT_MENU_ITEM).orElseThrow();
        driverTools.click(item);
    }

    public void clickDeleteMenuItem() {
        WebElement item = driverTools.waitForElementToBeClickable(DELETE_MENU_ITEM).orElseThrow();
        driverTools.click(item);
    }

    public boolean isDeleteConfirmationPopupDisplayed() {
        boolean deleteButtonAppeared = driverTools.waitForElementToAppear(DELETE_CONFIRMATION_BUTTON, 5).isPresent();
        boolean cancelButtonAppeared = driverTools.waitForElementToAppear(DEACTIVATE_CONFIRMATION_CANCEL_BUTTON, 5).isPresent();
        return deleteButtonAppeared && cancelButtonAppeared;
    }

    public void clickCancelInDeleteConfirmation() {
        WebElement button = driverTools.waitForElementToBeClickable(DEACTIVATE_CONFIRMATION_CANCEL_BUTTON).orElseThrow();
        driverTools.click(button);
    }

    public void clickDeleteInDeleteConfirmation() {
        WebElement button = driverTools.waitForElementToBeClickable(DELETE_CONFIRMATION_BUTTON).orElseThrow();
        driverTools.click(button);
    }

    public void deletePromoIfPresent(String name, String noResultsExpectedEn, String noResultsExpectedId) {
        fillOutSearchPromoNameAndSearch(name);
        if (isPromoPresentInList(name)) {
            clickRowActionMenu(name);
            clickDeleteMenuItem();
            clickDeleteInDeleteConfirmation();
            waitForSnackbarToDismiss();
            fillOutSearchPromoNameAndSearch(name);
        }
        verifyNoSearchResultsMessage(noResultsExpectedEn, noResultsExpectedId);
    }

    public void verifyNoSearchResultsMessage(String expectedEn, String expectedId) {
        driverTools.waitForElementToAppear(NO_SEARCH_RESULTS_TITLE).orElseThrow(() -> new NoSuchElementException("No search results message not found"));
        driverTools.scrollElementToCenter(NO_SEARCH_RESULTS_TITLE);
        WebElement titleElement = driverTools.waitForElementToAppear(NO_SEARCH_RESULTS_TITLE).orElseThrow(() -> new NoSuchElementException("No search results message not found"));
        String actualTitle = titleElement.getText().trim();
        Assertion.assertThat("Verify no search results message is displayed. Expected En: " + expectedEn + " | Expected Id: " + expectedId + " | Actual: " + actualTitle, actualTitle, anyOf(is(expectedEn), is(expectedId)));
    }
}
