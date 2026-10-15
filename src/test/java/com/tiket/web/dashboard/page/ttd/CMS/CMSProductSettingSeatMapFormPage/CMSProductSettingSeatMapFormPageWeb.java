package com.tiket.web.dashboard.page.ttd.CMS.CMSProductSettingSeatMapFormPage;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CMSProductSettingSeatMapFormPageWeb<T extends WebDriver> extends WebPage<T> {
    public CMSProductSettingSeatMapFormPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> CMSProductSettingSeatMapFormPageWeb<?> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new CMSProductSettingSeatMapFormPageDweb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new CMSProductSettingSeatMapFormPageMweb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    private static final Locator SEAT_MAP_NAME_INPUT = new Locator("SEAT_MAP_NAME_INPUT", By.xpath("//input[@name='code']"));
    private static final Locator ADD_NEW_CATEGORY = new Locator("ADD_NEW_CATEGORY", By.xpath("//div[@class='action-add-component component__add']"));
    private static final Locator CATEGORY_NAME_INPUT = new Locator("CATEGORY_NAME_INPUT", By.xpath("//div[contains(@class,'modal')]//input"));
    private static final Locator ADD_SECTION_BUTTON_POPUP = new Locator("ADD_SECTION_BUTTON_POPUP", By.xpath("//div[@class='modal-footer']/button[contains(@class,'waves-default')]"));
    private static final Locator ROW_HEIGHT_INPUT = new Locator("ROW_HEIGHT_INPUT", By.xpath("//input[@name='height']"));
    private static final Locator COLUMN_WIDTH_INPUT = new Locator("COLUMN_WIDTH_INPUT", By.xpath("//input[@name='width']"));
    private static final Locator REFRESH_GRID_BUTTON = new Locator("REFRESH_GRID_BUTTON", By.xpath("//div[contains(@class,'grid-manual-section')]//button[contains(@class,'waves-secondary') and not(contains(@class,'disabled'))]"));
    private static final Locator SUBMIT_BUTTON = new Locator("SUBMIT_BUTTON", By.xpath("(//div[@class='form-group seatmap-container']/div[last()]//button)[2]"));

    public void inputSeatMapName(String seatMapName) {
        driverTools.waitForElementToAppear(SEAT_MAP_NAME_INPUT);
        driverTools.sendKeys(SEAT_MAP_NAME_INPUT, seatMapName);
    }

    public void clickAddCategory() {
        driverTools.waitForElementToAppear(ADD_NEW_CATEGORY);
        driverTools.click(ADD_NEW_CATEGORY);
    }

    public void inputCategoryName(String categoryName) {
        driverTools.waitForElementToBeVisible(CATEGORY_NAME_INPUT,10);
        driverTools.sendKeys(CATEGORY_NAME_INPUT, categoryName);
    }

    public void clickAddSectionButtonPopup() {
        driverTools.waitForElementToBeVisible(ADD_SECTION_BUTTON_POPUP, 5);
        driverTools.click(ADD_SECTION_BUTTON_POPUP);
    }

    public void inputRowHeight(String rowHeightAmount) {
        driverTools.waitForElementToAppear(ROW_HEIGHT_INPUT);
        driverTools.sendKeys(ROW_HEIGHT_INPUT, rowHeightAmount);
    }

    public void inputColumnWidth(String columnWidthAmount) {
        driverTools.waitForElementToAppear(COLUMN_WIDTH_INPUT);
        driverTools.sendKeys(COLUMN_WIDTH_INPUT, columnWidthAmount);
    }

    public void clickRefreshGrid() {
        driverTools.waitForElementToAppear(REFRESH_GRID_BUTTON);
        driverTools.scrollElementToCenter(REFRESH_GRID_BUTTON);
        driverTools.click(REFRESH_GRID_BUTTON);
    }

    public void clickSubmitButton() {
        driverTools.dummyWait(1);
        driverTools.waitForElementToAppear(SUBMIT_BUTTON);
        driverTools.scrollElementToCenter(SUBMIT_BUTTON);
        driverTools.click(SUBMIT_BUTTON);
    }
}
