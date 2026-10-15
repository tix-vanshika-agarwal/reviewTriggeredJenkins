package com.tiket.web.dashboard.page.refund;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


public class RefundSpecialRejectReasonPage<T extends WebDriver> extends BasePage<T> {
    public RefundSpecialRejectReasonPage(Session<T> session) { super(session); }

    private static final Locator MENU_SPECIAL_REJECT_REASON = new Locator("MENU_SPECIAL_REJECT_REASON", new By.ByXPath("//span[contains(text(),'Reject')]"));
    private static final Locator SPECIAL_REJECT_REASON_HEADING = new Locator("SPECIAL_REJECT_REASON_HEADING", By.xpath("//h1[text()='Special Reject Reason']"));
    private static final Locator BUTTON_CREATE_NEW_REASON = new Locator("BUTTON_CREATE_NEW_REASON", new By.ByXPath("//a[contains(@href, '/special-reject-reason/create')]//button"));
    private static final Locator INPUT_EN_REASON = new Locator("INPUT_EN_REASON", new By.ByXPath("//input[@name='reasonEn']"));
    private static final Locator INPUT_ID_REASON = new Locator("INPUT_ID_REASON", new By.ByXPath("//input[@name='reasonIdn']"));
    private static final Locator DROPDOWN_VERTICAL = new Locator("DROPDOWN_VERTICAL", new By.ByXPath("//div[@type='button']//div[@role='button']"));
    private static final Locator BUTTON_SUBMIT_SPECIAL_REFUND = new Locator("BUTTON_SUBMIT_SPECIAL_REFUND", new By.ByXPath("//button[contains(normalize-space(.), 'Submit Special Refund')]"));
    private static final Locator LOADING_INDICATOR = new Locator("LOADING_INDICATOR",new By.ByXPath("//td[contains(@class, 'text-center')]//div[contains(@class, 'rounded-md')]"));
    private static final Locator LAST_PAGE_NUMBER_BUTTON = new Locator("LAST_PAGE_NUMBER_BUTTON", new By.ByXPath("(//button[normalize-space(text()) and number(text()) = number(text())])[last()]"));
    private static final Locator LAST_TABLE_ROW = new Locator("LAST_TABLE_ROW", new By.ByXPath("(//div[@data-testid='data-table']//tbody/tr)[last()]"));

    private Locator getVerticalOption(String verticalName) {
        return new Locator("VERTICAL_OPTION_" + verticalName.toUpperCase(),
                new By.ByXPath("//div[@role='option'][.//span[normalize-space()='" + verticalName + "'] or normalize-space()='" + verticalName + "']"));
    }

    private Locator getActionButtonByReason(String reasonText) {
        return new Locator("ACTION_BUTTON_" + reasonText.toUpperCase(),
                new By.ByXPath("//tr[.//td[contains(normalize-space(), '" + reasonText + "')]]//button[.//div[text()='Action']]"));
    }

    private static final Locator DELETE_MENU_ITEM = new Locator("DELETE_MENU_ITEM",
            new By.ByXPath("//div[@role='menu']//div[@role='menuitem'][.//p[text()='Delete'] or normalize-space()='Delete']"));

    private static final Locator BUTTON_YES_DELETE = new Locator("BUTTON_YES_DELETE",
            new By.ByXPath("//button[contains(normalize-space(.), 'Yes, Delete')]"));

    public void goToSpecialRejectReasonPage() {
        driverTools.waitForElementToAppear(MENU_SPECIAL_REJECT_REASON).ifPresentOrElse(driverTools::click, () -> {
            throw new NoSuchElementException(MENU_SPECIAL_REJECT_REASON + "element not found");
        });
    }

    public boolean isOnSpecialRejectReasonPage() {
        return driverTools.waitForElementToAppear(SPECIAL_REJECT_REASON_HEADING).isPresent();
    }

    public void clickCreateNewReason() {
        driverTools.waitForElementToAppear(BUTTON_CREATE_NEW_REASON).ifPresentOrElse(driverTools::click, () -> {
            throw new NoSuchElementException(BUTTON_CREATE_NEW_REASON + "element not found");
        });
    }

    public void inputEnglishReason(String reasonEn) {
        WebElement input = driverTools.waitForElementToAppear(INPUT_EN_REASON).orElseThrow();
        driverTools.clear(input);
        driverTools.sendKeys(input, reasonEn);
        logger.log("Entered EN Reason: " + reasonEn);
    }

    public void inputIndonesianReason(String reasonId) {
        WebElement input = driverTools.waitForElementToAppear(INPUT_ID_REASON).orElseThrow();
        driverTools.clear(input);
        driverTools.sendKeys(input, reasonId);
        logger.log("Entered ID Reason: " + reasonId);
    }

    public void selectVertical(String verticalName) {
        WebElement dropdown = driverTools.waitForElementToBeClickable(DROPDOWN_VERTICAL).orElseThrow();
        driverTools.click(dropdown);
        WebElement option = driverTools.waitForElementToBeClickable(getVerticalOption(verticalName)).orElseThrow();
        driverTools.click(option);

        driverTools.pressEscape();
        driverTools.waitForElementToDisappear(getVerticalOption(verticalName));
        logger.log("Selected Vertical: " + verticalName + " and closed dropdown overlay.");
    }

    public void clickSubmitSpecialRefund() {
        WebElement button = driverTools.waitForElementToBeClickable(BUTTON_SUBMIT_SPECIAL_REFUND).orElseThrow();
        driverTools.scrollToElement(button);
        driverTools.click(button);
        driverTools.waitForElementToDisappear(LOADING_INDICATOR);
    }

    public void goToLastPage() {
        if (driverTools.isElementPresent(LAST_PAGE_NUMBER_BUTTON)) {
            WebElement lastPageBtn = driverTools.waitForElementToBeClickable(LAST_PAGE_NUMBER_BUTTON).orElseThrow();
            driverTools.scrollToElement(lastPageBtn);
            driverTools.click(lastPageBtn);
            driverTools.waitForElementToDisappear(LOADING_INDICATOR);
            logger.log("Navigated to the last pagination page.");
        }
    }

    public boolean isReasonInLastRow(String expectedReason) {
        goToLastPage();
        WebElement lastRow = driverTools.waitForElementToAppear(LAST_TABLE_ROW).orElseThrow();
        driverTools.scrollToElement(lastRow);

        Locator lastRowWithReason = new Locator("LAST_ROW_WITH_REASON",
                new By.ByXPath("(//div[@data-testid='data-table']//tbody/tr)[last()][.//td[contains(normalize-space(), '" + expectedReason + "')]]"));

        return driverTools.waitForElementToAppear(lastRowWithReason, 5).isPresent();
    }

    public void deleteReasonByText(String reasonText) {

        WebElement actionBtn = driverTools.waitForElementToBeClickable(getActionButtonByReason(reasonText)).orElseThrow();
        driverTools.scrollToElement(actionBtn);
        driverTools.click(actionBtn);

        WebElement deleteOption = driverTools.waitForElementToBeClickable(DELETE_MENU_ITEM).orElseThrow();
        driverTools.click(deleteOption);

        WebElement confirmDelete = driverTools.waitForElementToBeClickable(BUTTON_YES_DELETE).orElseThrow();
        driverTools.click(confirmDelete);

        driverTools.waitForElementToDisappear(BUTTON_YES_DELETE);
        logger.log("Successfully deleted reason: " + reasonText);
    }


}