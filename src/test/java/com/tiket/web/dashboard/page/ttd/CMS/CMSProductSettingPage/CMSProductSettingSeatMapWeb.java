package com.tiket.web.dashboard.page.ttd.CMS.CMSProductSettingPage;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CMSProductSettingSeatMapWeb<T extends WebDriver> extends WebPage<T> {
    public CMSProductSettingSeatMapWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> CMSProductSettingSeatMapWeb<?> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new CMSProductSettingSeatMapDweb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new CMSProductSettingSeatMapMweb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    private static final Locator ACTION_BUTTON = new Locator("ACTION_BUTTON", By.xpath("(//button[contains(@class, 'btn-action-table')])[1]"));
    private static final Locator EDIT_BUTTON = new Locator("EDIT_BUTTON", By.xpath("//li[contains(@class,'MuiListItem-button')]//span[text()='Edit']"));
    private static final Locator EXPORT_TO_XLSX_BUTTON = new Locator("EXPORT_TO_XLSX_BUTTON", By.xpath("//button[contains(@class,'tix-button') and contains(., 'Export to XLSX')]"));
    private static final Locator SUBMIT_BUTTON = new Locator("SUBMIT_BUTTON", By.xpath("//button[normalize-space(text())='Submit']"));
    private static final Locator SEARCH_INPUT = new Locator("SEARCH_INPUT", By.xpath("(//input[@type='text' and @maxlength='500'])[1]"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", By.xpath("(//button[@data-testid='button-submit' and normalize-space()='Search'])[1]"));
    private static final Locator UPGRADE_GRID_BTN = new Locator("UPGRADE_GRID_BTN", By.xpath("(//button[contains(., 'Upgrade Grid')])[1]"));
    private static final Locator YES_UPGRADE_BUTTON = new Locator("YES_UPGRADE_BUTTON", By.xpath("//button[normalize-space()='Yes, Upgrade']"));
    private static final Locator HEIGHT_ROW_INPUT = new Locator("HEIGHT_ROW_INPUT", By.xpath("(//div[@class='input-name' and normalize-space()='Height (Row)']/following::input[@type='number'][1])[1]"));
    private static final Locator WIDTH_COLUMN_INPUT = new Locator("WIDTH_COLUMN_INPUT", By.xpath("(//div[@class='input-name' and normalize-space(text())='Width (Column)']/following-sibling::div[contains(@class,'input-wrapper')]/input[@name='width'])[1]"));
    private static final Locator TOTAL_GRID = new Locator("TOTAL_GRID", By.xpath("(//div[@class='section-value-summary col-2'][1])[1]"));
    private static final Locator SEATMAP_UPDATE_ALERT = new Locator("SEATMAP_UPDATE_ALERT", By.xpath("//div[@class='nav-bar-alert alert alert-success fade show' and contains(normalize-space(), 'Success update new seat map')]"));

    public void goToSeatMapPage() {
        switch (Configs.ENVIRONMENT) {
            case GK -> {driver.get("https://gatotkaca.tiket.com/admin/event-ms/to-do/product-configuration/seat-map");}
            case PREPROD -> {driver.get("https://preprod.tiket.com/admin/event-ms/to-do/product-configuration/seat-map");}
            case PROD -> {driver.get("https://www.tiket.com/admin/event-ms/to-do/product-configuration/seat-map");}
        }
        driverTools.waitForWebToLoad();
    }

    public void clickActionButton() {
        driverTools.refreshWebpage();
        driverTools.waitForElementToAppear(ACTION_BUTTON);
        driverTools.clickWithJavascriptExecutor(ACTION_BUTTON);
    }

    public void clickEditButton() {
        driverTools.waitForElementToAppear(EDIT_BUTTON);
        driverTools.clickWithJavascriptExecutor(EDIT_BUTTON);
    }

    public void searchSeatmapInput(String seatMapName) {
        driverTools.waitForElementToAppear(SEARCH_INPUT);
        driverTools.clickWithJavascriptExecutor(SEARCH_INPUT);
        driverTools.sendKeys(SEARCH_INPUT, seatMapName);
    }

    public void clickSearchButton() {
        driverTools.waitForElementToAppear(SEARCH_BUTTON);
        driverTools.clickWithJavascriptExecutor(SEARCH_BUTTON);
    }

    public void clickOnExportButton(){
        driverTools.waitForElementToAppear(EXPORT_TO_XLSX_BUTTON);
        driverTools.scrollToElement(EXPORT_TO_XLSX_BUTTON);
        driverTools.clickWithJavascriptExecutor(EXPORT_TO_XLSX_BUTTON);
    }

    public void clickOnSubmitButton(){
        driverTools.waitForElementToAppear(SUBMIT_BUTTON);
        driverTools.clickWithJavascriptExecutor(SUBMIT_BUTTON);
    }

    public void updateColumnSize(){
        driverTools.waitForElementToAppear(WIDTH_COLUMN_INPUT);
        driverTools.scrollToElement(WIDTH_COLUMN_INPUT);
        driverTools.clickWithJavascriptExecutor(WIDTH_COLUMN_INPUT);
        int randomSize = CoreFunctions.generateRandomInt(1,9);
        driverTools.clearInput(WIDTH_COLUMN_INPUT);
        driverTools.sendKeys(WIDTH_COLUMN_INPUT, String.valueOf(randomSize));
    }

    public void clickOnUpgradeGridButton(){
        driverTools.waitForElementToAppear(UPGRADE_GRID_BTN);
        driverTools.clickWithJavascriptExecutor(UPGRADE_GRID_BTN);
        driverTools.waitForElementToAppear(YES_UPGRADE_BUTTON);
        driverTools.clickWithJavascriptExecutor(YES_UPGRADE_BUTTON);
    }

    public boolean isSuccessAlertDisplayed(){
        driverTools.waitForElementToAppear(SEATMAP_UPDATE_ALERT);
        return driverTools.isElementDisplayed(SEATMAP_UPDATE_ALERT);
    }

    public boolean checkTotalGridValue() {
        driverTools.waitForElementToAppear(WIDTH_COLUMN_INPUT);
        driverTools.scrollToElement(WIDTH_COLUMN_INPUT);

        String rowSizeStr    = driverTools.getAttribute(HEIGHT_ROW_INPUT.by(), "value").trim();
        String columnSizeStr = driverTools.getAttribute(WIDTH_COLUMN_INPUT.by(), "value").trim();


        logger.log("Row Size: " + rowSizeStr);
        logger.log("Column Size: " + columnSizeStr);

        int expectedTotal = Integer.parseInt(rowSizeStr) * Integer.parseInt(columnSizeStr);

        driverTools.waitForElementToAppear(TOTAL_GRID);
        driverTools.scrollToElement(TOTAL_GRID);

        String totalStr = driverTools.getText(TOTAL_GRID).orElse("").trim().replaceAll("\\D", "");
        int totalGridValue;
        try {
            totalGridValue = Integer.parseInt(totalStr);
        } catch (NumberFormatException e) {
            logger.log("Failed to parse total grid value from string: '" + totalStr + "'. Exception: " + e.getMessage());
            return false;
        }
        return expectedTotal == totalGridValue;
    }

}
