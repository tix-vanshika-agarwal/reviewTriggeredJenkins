package com.tiket.web.dashboard.page.platform.pricing.criterion_group;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class CriterionGroupListPageWeb<T extends WebDriver> extends WebPage<T> {
    public CriterionGroupListPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> CriterionGroupListPageWeb<?> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new CriterionGroupListPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new CriterionGroupListPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    private static final Locator ADD_CRITERION_BUTTON = new Locator("ADD_CRITERION_BUTTON", By.xpath("//button[text()='Add Criterion Group']"));
    private static final Locator SEARCH_BY_GROUP_NAME_INPUT = new Locator("SEARCH_BY_GROUP_NAME_INPUT", By.xpath("//input[@id='search-by-group-name']"));
    private static final Locator SEARCH_BY_VERTICAL_DROPDOWN = new Locator("SEARCH_BY_VERTICAL_DROPDOWN", By.xpath("//input[@id='vertical--distribution-channel']"));
    private static final Locator SEARCH_BY_VERTICAL_DROPDOWN_SELECTIONS = new Locator("SEARCH_BY_VERTICAL_DROPDOWN_SELECTIONS", By.xpath("//div[contains(@id,'autocomplete-options-verticalIds')]"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", By.xpath("//button[text()='Search']"));
    private static final Locator DELETE_MODAL = new Locator("DELETE_MODAL", By.xpath("//section[.//span[text()='Delete this criterion group?']]"));
    private static final Locator DELETE_MODAL_CONFIRM_BUTTON = new Locator("DELETE_MODAL_CONFIRM_BUTTON", By.xpath("//button[text()='Yes, Delete']"));
    private static final Locator CRITERION_GROUP_PAGE_HEADER = new Locator("CRITERION_GROUP_PAGE_HEADER", By.xpath("//h1[text()='Criterion Group'] | //h2[text()='Criterion Group'] | //span[text()='Criterion Group']"));
    private static final Locator CRITERION_GROUP_TABLE = new Locator("CRITERION_GROUP_TABLE", By.xpath("//table | //div[contains(@class,'table')] | //tbody"));
    private static final Locator CRITERION_GROUP_ROWS = new Locator("CRITERION_GROUP_ROWS", By.xpath("//tbody//tr | //div[contains(@class,'row')]"));
    private static final Locator CRITERION_GROUP_SIDEBAR_BUTTON = new Locator("CRITERION_GROUP_SIDEBAR_BUTTON", new By.ByXPath("//span[@title='Criterion Group']"));
    private static final Locator CRITERION_GROUP_ACCESS_DENIED = new Locator("CRITERION_GROUP_ACCESS_DENIED", new By.ByXPath("//h2[normalize-space()='You do not have access to the dashboard']"));
    private static final Locator DOWNLOAD_CSV_TEMPLATE_BUTTON = new Locator("DOWNLOAD_CSV_TEMPLATE_BUTTON", By.xpath("//div[@id='popover-show']//span[normalize-space()='Download']"));
    private static final Locator SETTINGS_POPOVER = new Locator("SETTINGS_POPOVER", By.xpath("//div[@id='popover-show']"));
    private static final Locator SEARCH_BY_KEY_INPUT = new Locator("SEARCH_BY_KEY_INPUT", By.xpath("//input[@id='search-by-key'] | //input[contains(@placeholder,'Key') or contains(@placeholder,'key')]"));
    private static final Locator RESET_FILTER_BUTTON = new Locator("RESET_FILTER_BUTTON", By.xpath("//button[contains(normalize-space(),'Reset Filter') or contains(normalize-space(),'Reset filter')]"));
    private static final Locator NO_DATA_TEXT = new Locator("NO_DATA_TEXT", By.xpath("//*[contains(normalize-space(),'No data') or contains(normalize-space(),'No results') or contains(normalize-space(),'Data not found')]"));
    private static final Locator SUCCESS_NOTIFICATION = new Locator("SUCCESS_NOTIFICATION", By.xpath("//div[contains(text(), 'Criterion group saved')]"));
    private static final Locator DELETE_IN_USE_ERROR = new Locator("DELETE_IN_USE_ERROR", By.xpath("//div[contains(text(), 'cannot delete criterion group since it is being used')]"));
    private static final Locator SUCCESS_ALERT = new Locator("SUCCESS_ALERT", By.xpath("//div[contains(text(), 'successfully added')]"));

    public void clickAddCriterionButton() {
        driverTools.click(ADD_CRITERION_BUTTON);
        driverTools.dummyWait(3);
    }

    public void clickSettingsGroupName(String groupName) {
        int groupNameIndex = getColumnIndex("Group Name");
        int actionIndex = getColumnIndex("Action");

        Locator settingsButtonLoc = new Locator("SETTINGS_BUTTON_" + groupName, By.xpath("//tr[./td[" + groupNameIndex + "][text()='" + groupName + "']]/td[" + actionIndex + "]//button"));

        driverTools.clickWithJavascriptExecutor(settingsButtonLoc);
    }

    public void clickDeleteInSettingsGroupName(String groupName) {
        int groupNameIndex = getColumnIndex("Group Name");
        int rowIndex = driverTools
                .waitForElementsToAppear(new Locator("GROUP_NAME_ROW", By.xpath("//tr[./td[" + groupNameIndex +"][text()='" + groupName + "']]")))
                .orElseThrow().size();

        driverTools.clickWithJavascriptExecutor(new Locator("DELETE_BUTTON_" + groupName, By.xpath("(//span[text()='Delete'])[" + rowIndex + "]")));
    }

    public void confirmCriterionGroupDeletion() {
        driverTools.waitForElementToAppear(DELETE_MODAL);
        driverTools.clickWithJavascriptExecutor(DELETE_MODAL_CONFIRM_BUTTON);
        driverTools.waitForElementToAbsence(DELETE_MODAL);
        driverTools.dummyWait(2);
    }

    public void inputGroupNameSearch(String groupName) {
        driverTools.click(SEARCH_BY_GROUP_NAME_INPUT);
        driverTools.clearInput(SEARCH_BY_GROUP_NAME_INPUT);
        driverTools.sendKeys(SEARCH_BY_GROUP_NAME_INPUT, groupName);
    }

    public void inputVerticalSearch(String vertical) {
        driverTools.scrollElementToCenter(SEARCH_BY_VERTICAL_DROPDOWN);
        driverTools.click(SEARCH_BY_VERTICAL_DROPDOWN);
        driverTools.clearInput(SEARCH_BY_VERTICAL_DROPDOWN);
        driverTools.sendKeys(SEARCH_BY_VERTICAL_DROPDOWN, vertical);
        driverTools.dummyWait(4);

        List<WebElement> selections = driverTools.waitForElementsToAppear(SEARCH_BY_VERTICAL_DROPDOWN_SELECTIONS).orElseThrow();
        for (WebElement selection : selections) {
            if(driverTools.getText(selection).equalsIgnoreCase(vertical)) {
                driverTools.clickWithJavascriptExecutor(selection);
                break;
            }
        }
    }

    public void clickSearch() {
        driverTools.click(SEARCH_BUTTON);
        driverTools.dummyWait(3);
    }

    public boolean verifyCriterionGroupNotExist(String groupName) {
        int groupNameIndex = getColumnIndex("Group Name");
        Locator loc = new Locator("ROW_" + groupName, By.xpath("//tr[./td[" + groupNameIndex + "][text()='" + groupName + "']]"));
        return driverTools.waitForElementToAppear(loc, 5).orElse(null) == null;
    }

    private int getColumnIndex(String colName) {
        List<WebElement> cols = driverTools.getDriver().findElements(By.xpath("//th[text()='" + colName + "']/preceding-sibling::th"));
        return cols.size() + 1;
    }

    public boolean verifyCriterionGroupPageRedirect() {
        driverTools.dummyWait(1);
        String currentUrl = driverTools.getCurrentURL();
        return currentUrl.contains("criterion-group");
    }

    public boolean verifyCriterionGroupPageHeaderDisplayed() {
        return driverTools.waitForElementToAppear(CRITERION_GROUP_PAGE_HEADER, 10).isPresent();
    }

    public boolean verifyCriterionGroupDataDisplayed() {
        driverTools.waitForElementToAppear(CRITERION_GROUP_TABLE, 10);
        boolean tableDisplayed = driverTools.isElementDisplayed(CRITERION_GROUP_TABLE);
        boolean addButtonDisplayed = driverTools.isElementDisplayed(ADD_CRITERION_BUTTON);
        return tableDisplayed && addButtonDisplayed;
    }

    public boolean verifyUserHasAccessToCriterionGroup() {
        boolean pageRedirected = verifyCriterionGroupPageRedirect();
        boolean headerDisplayed = verifyCriterionGroupPageHeaderDisplayed();
        boolean dataDisplayed = verifyCriterionGroupDataDisplayed();

        return pageRedirected && headerDisplayed && dataDisplayed;
    }

    public void clickCriterionGroupSideBarButton() {
        driverTools.waitForElementToAppear(CRITERION_GROUP_SIDEBAR_BUTTON);
        driverTools.click(CRITERION_GROUP_SIDEBAR_BUTTON);
    }

    public boolean verifyCreateSuccessMessage() {
        return driverTools.waitForElementToAppear(SUCCESS_NOTIFICATION, 5).isPresent();
    }

    public void clickEditInSettingsGroupName(String groupName) {
        int groupNameIndex = getColumnIndex("Group Name");
        int rowIndex = driverTools
                .waitForElementsToAppear(new Locator("GROUP_NAME_ROW", By.xpath("//tr[./td[" + groupNameIndex + "][text()='" + groupName + "']]")))
                .orElseThrow().size();
        driverTools.click(new Locator("EDIT_BUTTON_" + groupName, By.xpath("(//span[text()='Edit'])[" + rowIndex + "]")));
    }

    public boolean verifyOnEditPage() {
        driverTools.dummyWait(1);
        String currentUrl = driverTools.getCurrentURL();
        return currentUrl.contains("criterion-group") && (currentUrl.contains("edit") || currentUrl.contains("update"));
    }

    public boolean verifyEditSuccessMessage() {
        return driverTools.waitForElementToAppear(SUCCESS_ALERT, 5).isPresent();
    }

    public boolean verifyDeleteWithDependencyError() {
        return driverTools.waitForElementToAppear(DELETE_IN_USE_ERROR, 5).isPresent();
    }

    public void inputKeySearch(String key) {
        driverTools.waitForElementToAppear(SEARCH_BY_KEY_INPUT, 5);
        driverTools.click(SEARCH_BY_KEY_INPUT);
        driverTools.clearInput(SEARCH_BY_KEY_INPUT);
        driverTools.sendKeys(SEARCH_BY_KEY_INPUT, key);
    }

    public void clickResetFilterButton() {
        driverTools.waitForElementToAppear(RESET_FILTER_BUTTON, 5);
        driverTools.click(RESET_FILTER_BUTTON);
    }

    public boolean verifyFilterResultsContain(String groupName) {
        int groupNameIndex = getColumnIndex("Group Name");
        Locator loc = new Locator("FILTER_ROW_" + groupName, By.xpath("//tr[./td[" + groupNameIndex + "][contains(text(),'" + groupName + "')]]"));
        return driverTools.waitForElementToAppear(loc, 5).isPresent();
    }

    public boolean verifyNoDataFound() {
        return driverTools.waitForElementToAppear(NO_DATA_TEXT, 5).isPresent();
    }

    public boolean verifyFilterIsReset() {
        driverTools.waitForElementToAppear(CRITERION_GROUP_TABLE, 5);
        return driverTools.isElementDisplayed(CRITERION_GROUP_TABLE);
    }

    public void clickDownloadInSettingsGroupName(String groupName) {
        int groupNameIndex = getColumnIndex("Group Name");
        int rowIndex = driverTools
                .waitForElementsToAppear(new Locator("GROUP_NAME_ROW", By.xpath("//tr[./td[" + groupNameIndex + "][text()='" + groupName + "']]")))
                .orElseThrow().size();
        driverTools.click(new Locator("DOWNLOAD_BUTTON_" + groupName, By.xpath("(//span[text()='Download'])[" + rowIndex + "]")));
        driverTools.waitForElementToDisappear(SETTINGS_POPOVER, 5);
    }

    public boolean verifyDownloadInitiated() {
        return driverTools.waitForElementToAppear(CRITERION_GROUP_TABLE, 5).isPresent();
    }

    public boolean verifyPageIsNotAccessible(String text) {
        WebElement textElement = driverTools.waitForElementToAppear(CRITERION_GROUP_ACCESS_DENIED, 10).orElseThrow();
        String str = driverTools.getText(textElement);
        return str.contains(text);
    }
}
