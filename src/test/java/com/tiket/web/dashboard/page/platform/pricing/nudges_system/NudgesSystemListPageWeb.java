package com.tiket.web.dashboard.page.platform.pricing.nudges_system;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.NoSuchElementException;

public class NudgesSystemListPageWeb<T extends WebDriver> extends WebPage<T> {
    public NudgesSystemListPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> NudgesSystemListPageWeb<?> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new NudgesSystemListPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new NudgesSystemListPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    private static final Locator NUDGE_NAME_SEARCH_INPUT = new Locator("NUDGE_NAME_SEARCH_INPUT", By.xpath("//input[@id='search-by-nudge-name']"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", By.xpath("//button[text()='Search']"));
    private static final Locator DETAILS_OPTION = new Locator("DETAILS_OPTION", By.xpath("//span[text()='Detail']"));
    private static final Locator NUDGE_DETAILS_MODAL = new Locator("NUDGE_DETAILS_MODAL", By.xpath("//section[contains(@class,'modal')] | //div[contains(@class,'modal')] | //div[@role='dialog']"));
    private static final Locator MODAL_TITLE = new Locator("MODAL_TITLE", By.xpath("//h1[contains(text(),'General Rule')] | //h2[contains(text(),'General Rule')] | //span[contains(text(),'General Rule')]"));
    private static final Locator MODAL_EDIT_BUTTON = new Locator("MODAL_EDIT_BUTTON", By.xpath("//button[contains(text(),'Edit')] | //button[.//span[contains(text(),'Edit')]]"));
    private static final Locator MODAL_NUDGE_NAME = new Locator("MODAL_NUDGE_NAME", By.xpath("//div[contains(text(),'Nudge Name')] | //span[contains(text(),'Name')] | //label[contains(text(),'Name')]"));
    private static final Locator MODAL_PRIORITY = new Locator("MODAL_PRIORITY", By.xpath("//div[contains(text(),'Priority')] | //span[contains(text(),'Priority')] | //label[contains(text(),'Priority')]"));
    private static final Locator MODAL_STATUS = new Locator("MODAL_STATUS", By.xpath("//div[contains(text(),'Status')] | //span[contains(text(),'Status')] | //label[contains(text(),'Status')]"));
    private static final Locator MODAL_CLOSE_BUTTON = new Locator("MODAL_CLOSE_BUTTON", By.xpath("//button[@data-testid='modal-close-button']"));
    private static final Locator FILTER_STATUS_DROPDOWN = new Locator("FILTER_STATUS_DROPDOWN", By.xpath("//*[@id='status']"));
    private static final Locator DELETION_RESTRICTION_ERROR = new Locator("DELETION_RESTRICTION_ERROR", By.xpath("//span[contains(text(), \"Can't delete nudge\")]"));
    private static final Locator I_UNDERSTAND_BUTTON = new Locator("I_UNDERSTAND_BUTTON", By.xpath("//button[text()='I Understand']"));
    private static final Locator RESET_FILTER_BUTTON = new Locator("RESET_FILTER_BUTTON", By.xpath("//button[contains(text(),'Reset filter')]"));
    private static final Locator DELETE_BUTTON = new Locator("DELETE_BUTTON", By.xpath("//div[@data-testid='action-button-option-undefined-Delete']"));
    private static final Locator CONFIRM_DELETE_BUTTON = new Locator("CONFIRM_DELETE_BUTTON", By.xpath("//button[text()='Yes, delete']"));
    public void inputNudgeNameSearch(String nudgeName) {
        driverTools.waitForElementToAppear(NUDGE_NAME_SEARCH_INPUT);
        driverTools.clearInput(NUDGE_NAME_SEARCH_INPUT);
        driverTools.sendKeys(NUDGE_NAME_SEARCH_INPUT, nudgeName);
    }

    public void clickSearch() {
        driverTools.click(SEARCH_BUTTON);
        driverTools.dummyWait(2);
    }

    public void clickSettingsButtonByNudgeName(String nudgeName) {
        int nudgeNameIndex = getIndexOfColumnTitle("Nudge Name");
        int actionIndex = getIndexOfColumnTitle("Action");

        WebElement element = driverTools.waitForElementToAppear(new Locator("ROW_" + nudgeName, By.xpath("//tr[./td[" + nudgeNameIndex + "][text()='" + nudgeName +  "']]/td[" + actionIndex + "]//button"))).orElse(null);

        if (element == null) throw new NoSuchElementException("There are no data for " + nudgeName);
        driverTools.scrollElementToCenter(element);
        driverTools.click(element);
        driverTools.dummyWait(1);
    }

    public void clickDuplicateButtonByNudgeName(String nudgeName) {
        int nudgeNameIndex = getIndexOfColumnTitle("Nudge Name");

        List<WebElement> rows = driverTools.getDriver().findElements(By.xpath("//tr[./td[" + nudgeNameIndex + "][text()='" + nudgeName + "']]/preceding-sibling::tr"));

        driverTools.clickWithJavascriptExecutor(new Locator("DUPLICATE_BUTTON", By.xpath("(//span[text()='Duplicate'])[" + (rows.size() + 1) + "]")));
    }

    public String getPriorityValueByNudgeName(String nudgeName) {
        int nudgeNameIndex = getIndexOfColumnTitle("Nudge Name");
        int priorityIndex = getIndexOfColumnTitle("Priority");

        List<WebElement> rows = driverTools.getDriver().findElements(By.xpath("//tr[./td[" + nudgeNameIndex + "][text()='" + nudgeName + "']]/preceding-sibling::tr"));

        return driverTools.getText(new Locator("PRIORITY_" + nudgeName, By.xpath("//tbody/tr[" + (rows.size() + 1) + "]/td[" + priorityIndex + "]"))).orElseThrow();
    }

    private int getIndexOfColumnTitle(String columnTitle) {
        List<WebElement> cols = driverTools.getDriver().findElements(By.xpath("//th[.//text()='" + columnTitle + "']/preceding-sibling::th"));
        return cols.size() + 1;
    }

    public void clickDetailsOptionByNudgeName(String nudgeName) {
        clickSettingsButtonByNudgeName(nudgeName);
        driverTools.waitForElementToAppear(DETAILS_OPTION);
        driverTools.click(DETAILS_OPTION);
    }

    public boolean verifyNudgeDetailsModalDisplayed() {
        return driverTools.waitForElementToAppear(NUDGE_DETAILS_MODAL, 10).isPresent();
    }

    public boolean verifyModalTitleDisplayed() {
        return driverTools.waitForElementToAppear(MODAL_TITLE, 5).isPresent();
    }

    public boolean verifyModalEditButtonDisplayed() {
        return driverTools.isElementDisplayed(MODAL_EDIT_BUTTON);
    }

    public boolean verifyModalContainsRuleDetails() {
        boolean hasNudgeName = driverTools.waitForElementToAppear(MODAL_NUDGE_NAME, 5).isPresent();
        boolean hasPriority = driverTools.waitForElementToAppear(MODAL_PRIORITY, 5).isPresent();
        boolean hasStatus = driverTools.waitForElementToAppear(MODAL_STATUS, 5).isPresent();

        return hasNudgeName || hasPriority || hasStatus; // At least one detail should be present
    }

    public void clickEditButtonInModal() {
        driverTools.waitForElementToAppear(MODAL_EDIT_BUTTON);
        driverTools.click(MODAL_EDIT_BUTTON);
    }

    public boolean verifyRedirectToEditPage() {
        driverTools.dummyWait(2);
        String currentUrl = driverTools.getCurrentURL();
        return currentUrl.contains("edit") || currentUrl.contains("update");
    }

    public boolean verifyComprehensiveNudgeDetailsModal() {
        boolean modalDisplayed = verifyNudgeDetailsModalDisplayed();
        boolean titleDisplayed = verifyModalTitleDisplayed();
        boolean hasRuleDetails = verifyModalContainsRuleDetails();
        boolean hasEditButton = verifyModalEditButtonDisplayed();

        return modalDisplayed && titleDisplayed && hasRuleDetails && hasEditButton;
    }
    
    public boolean isDeletionRestrictionErrorDisplayed() {
        return driverTools.waitForElementToAppear(DELETION_RESTRICTION_ERROR, 10).isPresent();
    }

    public void clickIUnderstandButton() {
        driverTools.waitForElementToBeClickable(I_UNDERSTAND_BUTTON, 5);
        driverTools.click(I_UNDERSTAND_BUTTON);
        driverTools.dummyWait(1);
    }
    
    public void filterByStatusDropdown(String statusToFilter) {
        if (driverTools.isElementDisplayed(RESET_FILTER_BUTTON)) {
            driverTools.clickWithJavascriptExecutor(RESET_FILTER_BUTTON);
            driverTools.dummyWait(1);
        }
        driverTools.waitForElementToAppear(FILTER_STATUS_DROPDOWN, 5);
        driverTools.waitForElementToBeClickable(FILTER_STATUS_DROPDOWN, 5);
        driverTools.clickWithJavascriptExecutor(FILTER_STATUS_DROPDOWN);
        driverTools.dummyWait(1);
        Locator allCheckbox = new Locator("STATUS_CHECKBOX_ALL", By.xpath("//input[@data-testid='check-ON_GOING']/preceding::*[text()='All'][1]"));
        driverTools.clickWithJavascriptExecutor(allCheckbox);
        driverTools.dummyWait(1);
        Locator statusCheckbox = new Locator("STATUS_CHECKBOX_" + statusToFilter, By.xpath("//input[@value='" + statusToFilter.toUpperCase() + "' or @data-testid='check-" + statusToFilter.toUpperCase() + "']"));
        driverTools.clickWithJavascriptExecutor(statusCheckbox);
        driverTools.dummyWait(1);
        driverTools.clickWithJavascriptExecutor(FILTER_STATUS_DROPDOWN); 
        clickSearch(); 
    }

    public String getTopRowNudgeName() {
        int nudgeNameIndex = getIndexOfColumnTitle("Nudge Name"); 
        return driverTools.getText(new Locator("TOP_ROW_NAME", By.xpath("//tbody/tr[1]/td[" + nudgeNameIndex + "]"))).orElseThrow();
    }

    public void clickDeleteButton() {
        driverTools.scrollToElement(DELETE_BUTTON);
        driverTools.waitForElementToBeClickable(DELETE_BUTTON, 10);
        driverTools.clickWithJavascriptExecutor(DELETE_BUTTON);
    }

    public void clickConfirmDeleteButton() {
        driverTools.waitForElementToBeClickable(CONFIRM_DELETE_BUTTON, 10);
        driverTools.click(CONFIRM_DELETE_BUTTON);
        driverTools.dummyWait(3);
    }
}
