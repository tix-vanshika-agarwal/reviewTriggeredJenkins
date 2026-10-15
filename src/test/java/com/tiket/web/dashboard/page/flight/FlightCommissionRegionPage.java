package com.tiket.web.dashboard.page.flight;

import com.tiket.core.CoreFunctions;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlightCommissionRegionPage<T extends WebDriver> extends BasePage<T> {
    public FlightCommissionRegionPage(Session<T> session) {
        super(session);
    }

    private static final Locator USERNAME_GREETING = new Locator("USERNAME_GREETING", new By.ByXPath("//span[contains(@class,'header__username')]"));
    private static final Locator PAGE_TITLE = new Locator("PAGE_TITLE", new By.ByXPath("//span[contains(@class,'custom-table__page-title')]"));
    private static final Locator ADD_NEW_BTN = new Locator("ADD_NEW_BTN", new By.ByXPath("//a[contains(@href,'commission-region/add')]//button"));
    private static final Locator NAME_FILTER_FIELDS = new Locator("NAME_FILTER_FIELDS", new By.ByXPath("//input[@id='custom-table__filter--name']"));
    private static final Locator APPLY_FILTER_BTN = new Locator("APPLY_FILTER_BTN", new By.ByXPath("//button[contains(@class,'custom-table__filter--submit')]"));
    private static final Locator RESET_FILTER_BTN = new Locator("RESET_FILTER_BTN", new By.ByXPath("//button[contains(@class,'custom-table__filter--reset')]"));
    /** An empty grid still renders a row, but as a single cell spanning all four columns. */
    private static final Locator DATA_ROWS = new Locator("DATA_ROWS", new By.ByXPath("//tbody//tr[count(./td)=4]"));
    private static final Locator DATA_NOT_EXIST_TEXT = new Locator("DATA_NOT_EXIST_TEXT", new By.ByXPath("//tbody//td[normalize-space()='Data Not Exist']"));
    private static final Locator OK_PRIMARY_BUTTON = new Locator("OK_PRIMARY_BUTTON", new By.ByXPath("//div[@class='button']/button[contains(@class,'btn-primary')]"));
    private static final Locator LOADING_OVERLAY = new Locator("LOADING_OVERLAY", new By.ByXPath("//div[contains(@class,'loading-overlay')]"));

    public boolean isLoggedIn() {
        driverTools.waitForElementToAppear(USERNAME_GREETING, 3);
        return driverTools.isElementDisplayed(USERNAME_GREETING);
    }

    public void goToCommissionRegionDashboard() {
        if (Configs.ENVIRONMENT == Environment.GK) {
            driverTools.get("https://gatotkaca.tiket.com/admin/flight-ms-v2/commission-region");
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driverTools.get("https://preprod.tiket.com/admin/flight-ms-v2/commission-region");
        } else if (Configs.ENVIRONMENT == Environment.PROD) {
            driverTools.get("https://www.tiket.com/admin/flight-ms-v2/commission-region");
        }
    }

    public String getPageTitle() {
        WebElement pageTitle = driverTools.waitForElementToAppear(PAGE_TITLE, 5).orElseThrow();
        return driverTools.getText(pageTitle);
    }

    /**
     * The filter row sits in the same table head and repeats the column names as
     * 'Name Filter' and 'Status Filter', so the column name has to match exactly.
     */
    public boolean isColumnDisplayed(String column) {
        Locator columnHeader = new Locator("COLUMN_HEADER_" + column.toUpperCase(),
                new By.ByXPath("//thead//th[normalize-space()='" + column + "']"));
        driverTools.waitForElementToAppear(columnHeader, 5);
        return driverTools.isElementDisplayed(columnHeader);
    }

    public boolean isAddNewBtnDisplayed() {
        driverTools.waitForElementToAppear(ADD_NEW_BTN, 5);
        return driverTools.isElementDisplayed(ADD_NEW_BTN);
    }

    public boolean isApplyFilterBtnDisplayed() {
        driverTools.waitForElementToAppear(APPLY_FILTER_BTN, 5);
        return driverTools.isElementDisplayed(APPLY_FILTER_BTN);
    }

    public boolean isResetFilterBtnDisplayed() {
        driverTools.waitForElementToAppear(RESET_FILTER_BTN, 5);
        return driverTools.isElementDisplayed(RESET_FILTER_BTN);
    }

    /** Counts the regions the grid currently lists, so 0 for an empty grid. */
    public int getRowCount() {
        driverTools.waitForElementToDisappear(LOADING_OVERLAY, 10);
        if (driverTools.waitForElementToAppear(DATA_NOT_EXIST_TEXT, 5).isPresent()) {
            return 0;
        }
        return driverTools.elements(DATA_ROWS).orElse(List.of()).size();
    }

    public void clickAddNewBtn() {
        driverTools.waitForElementToAppear(ADD_NEW_BTN, 5);
        driverTools.click(ADD_NEW_BTN);
    }

    /**
     * A newly created region can land on any page of the grid, so every lookup goes
     * through the name filter first and then reads row 1.
     */
    public void searchByName(String name) {
        driverTools.waitForElementToDisappear(LOADING_OVERLAY, 10);
        driverTools.waitForElementToAppear(NAME_FILTER_FIELDS, 5).orElseThrow();
        driverTools.clear(NAME_FILTER_FIELDS);
        driverTools.sendKeys(NAME_FILTER_FIELDS, name);
        driverTools.click(APPLY_FILTER_BTN);
        driverTools.waitForElementToDisappear(LOADING_OVERLAY, 10);
    }

    public void resetFilter() {
        driverTools.waitForElementToAppear(RESET_FILTER_BTN, 5);
        driverTools.click(RESET_FILTER_BTN);
        driverTools.waitForElementToDisappear(LOADING_OVERLAY, 10);
    }

    public Map<String, String> getDisplayedData(int targetIndex) {
        Map<String, String> displayedCommissionRegion = new HashMap<>();

        driverTools.waitForElementToDisappear(LOADING_OVERLAY, 10);

        String name = getName(targetIndex);
        displayedCommissionRegion.put("name", name);

        String countries = getCountries(targetIndex);
        displayedCommissionRegion.put("countries", countries);

        String status = getStatus(targetIndex);
        displayedCommissionRegion.put("status", status);

        return displayedCommissionRegion;
    }

    private String getName(int index) {
        driverTools.waitForElementToAppear(getNameTextLocatorByIndex(index));
        WebElement targetElement = driverTools.element(getNameTextLocatorByIndex(index)).orElseThrow();
        return driverTools.getText(targetElement);
    }

    private Locator getNameTextLocatorByIndex(int index) {
        return new Locator("NAME_TEXT_" + index, new By.ByXPath("//tbody//tr["+ index +"]/td[1]"));
    }

    private String getCountries(int index) {
        driverTools.waitForElementToAppear(getCountriesTextLocatorByIndex(index));
        WebElement targetElement = driverTools.element(getCountriesTextLocatorByIndex(index)).orElseThrow();
        return driverTools.getText(targetElement);
    }

    private Locator getCountriesTextLocatorByIndex(int index) {
        return new Locator("COUNTRIES_TEXT_" + index, new By.ByXPath("//tbody//tr["+ index +"]/td[2]"));
    }

    private String getStatus(int index) {
        driverTools.waitForElementToAppear(getStatusTextLocatorByIndex(index));
        WebElement targetElement = driverTools.element(getStatusTextLocatorByIndex(index)).orElseThrow();
        return driverTools.getText(targetElement);
    }

    private Locator getStatusTextLocatorByIndex(int index) {
        return new Locator("STATUS_TEXT_" + index, new By.ByXPath("//tbody//tr["+ index +"]/td[3]"));
    }

    /**
     * The action cell is far wider than the icon buttons it holds, and the grid
     * re-lays it out as rows are filtered, so a coordinate click on either the edit
     * or the delete button is intermittently intercepted by the cell itself. The JS
     * fallback clicks the resolved button without relying on its position.
     */
    public void clickEditButton(int index) {
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(getEditButtonLocatorByIndex(index));
    }

    private Locator getEditButtonLocatorByIndex(int index) {
        return new Locator("EDIT_BUTTON_" + index, new By.ByXPath("//tbody//tr["+ index +"]/td[4]//div[@title='Edit']//button"));
    }

    public boolean isEditButtonDisplayed(int index) {
        driverTools.waitForElementToAppear(getEditButtonLocatorByIndex(index), 5);
        return driverTools.isElementDisplayed(getEditButtonLocatorByIndex(index));
    }

    public boolean isDeleteButtonDisplayed(int index) {
        driverTools.waitForElementToAppear(getDeleteButtonLocatorByIndex(index), 5);
        return driverTools.isElementDisplayed(getDeleteButtonLocatorByIndex(index));
    }

    public boolean isStatusToggleButtonDisplayed(int index) {
        driverTools.waitForElementToAppear(getStatusToggleButtonLocatorByIndex(index), 5);
        return driverTools.isElementDisplayed(getStatusToggleButtonLocatorByIndex(index));
    }

    /** One button toggles the status, labelled for the transition the row can make. */
    private Locator getStatusToggleButtonLocatorByIndex(int index) {
        return new Locator("STATUS_TOGGLE_BUTTON_" + index,
                new By.ByXPath("//tbody//tr["+ index +"]/td[4]//div[@title='Activate' or @title='Inactivate']//button"));
    }

    public void deleteCommissionRegionIfExists(String name) {
        searchByName(name);
        if (driverTools.waitForElementToAppear(getCommissionRegionLocator(name), 3).isPresent()) {
            deleteCommissionRegion(name);
        }
    }

    public void deleteCommissionRegion(String name) {
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(getDeleteButtonLocatorByName(name));
        clickOKButton();
        driverTools.waitForElementToDisappear(LOADING_OVERLAY, 10);
    }

    private Locator getDeleteButtonLocatorByName(String name) {
        return new Locator("DELETE_BUTTON_" + name,
                new By.ByXPath(getCommissionRegionRowXPath(name) + "/td[4]//div[@title='Delete']//button"));
    }

    public void deleteCommissionRegion(int index) {
        clickDeleteButton(index);
        clickOKButton();
        driverTools.waitForElementToDisappear(LOADING_OVERLAY, 10);
    }

    private void clickDeleteButton(int index) {
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(getDeleteButtonLocatorByIndex(index));
    }

    private Locator getDeleteButtonLocatorByIndex(int index) {
        return new Locator("DELETE_BUTTON_" + index, new By.ByXPath("//tbody//tr["+ index +"]/td[4]//div[@title='Delete']//button"));
    }

    private void clickOKButton() {
        driverTools.waitForElementToAppear(OK_PRIMARY_BUTTON).orElseThrow();
        driverTools.click(OK_PRIMARY_BUTTON);
    }

    public boolean isDataNotExist() {
        driverTools.waitForElementToAppear(DATA_NOT_EXIST_TEXT, 5);
        return driverTools.isElementDisplayed(DATA_NOT_EXIST_TEXT);
    }

    public boolean isExist(Map<String, String> commissionRegion, boolean expectedOutcome) {
        searchByName(commissionRegion.get("name"));

        Locator targetCommissionRegion = getCommissionRegionLocator(commissionRegion);
        if (expectedOutcome) {
            driverTools.waitForElementToAppear(targetCommissionRegion, 3);
        } else {
            driverTools.waitForElementToDisappear(targetCommissionRegion, 3);
        }

        return driverTools.isElementDisplayed(targetCommissionRegion);
    }

    /**
     * Matched on name alone: the Country column renders each country's name as
     * stored in the country master, which the dashboard tests in this suite also
     * edit, so it is not a dependable part of a row's identity.
     */
    private Locator getCommissionRegionLocator(Map<String, String> commissionRegion) {
        return getCommissionRegionLocator(commissionRegion.get("name"));
    }

    private Locator getCommissionRegionLocator(String name) {
        return new Locator("COMMISSION_REGION", new By.ByXPath(getCommissionRegionRowXPath(name)));
    }

    private static String getCommissionRegionRowXPath(String name) {
        return "//tbody/tr[./td[normalize-space()=" + toXPathLiteral(name) + "]]";
    }
    
    private static String toXPathLiteral(String value) {
        if (value.contains("'") && value.contains("\"")) {
            String[] parts = value.split("'");
            return "concat('" + String.join("',\"'\",'", parts) + "')";
        }
        return value.contains("'") ? "\"" + value + "\"" : "'" + value + "'";
    }

    public String generateRandomName(String name) {
        if (name.equalsIgnoreCase("random")) {
            name = "Automation Region " + CoreFunctions.generateRandomNumeric(8);
        }
        return name;
    }
}
