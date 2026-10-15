package com.tiket.web.dashboard.page.nft.ferry;

import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.bson.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FerryFacilityPage<T extends WebDriver> extends BasePage<T> {
    private static final Locator USERNAME_GREETING = new Locator("USERNAME_GREETING", new By.ByXPath("//div[contains(@class, 'header_dashboard')]"));
    private static final Locator SEARCH_FIELD = new Locator("SEARCH_FIELD", new By.ByXPath("//input[@id='sb-text-input']"));
    private static final Locator MOVE_TO_ADD_FACILITY_PAGE_BUTTON = new Locator("MOVE_TO_ADD_FACILITY_PAGE_BUTTON", new By.ByXPath("//button[contains(@href,'/admin/ferry/facilities/add')]"));
    private static final Locator LABEL_INPUT = new Locator("LABEL_INPUT", new By.ByXPath("//input[@name='tag']"));
    private static final Locator PRIORITY_INPUT = new Locator("PRIORITY_INPUT", new By.ByXPath("//input[@name='priorityScore']"));
    private static final Locator ID_NAME_INPUT = new Locator("ID_NAME_INPUT", new By.ByXPath("//input[@name='nameId']"));
    private static final Locator EN_NAME_INPUT = new Locator("EN_NAME_INPUT", new By.ByXPath("//input[@name='nameEn']"));
    private static final Locator IMAGE_UPLOAD_AVAILABLE = new Locator("IMAGE_UPLOAD_AVAILABLE", new By.ByXPath("//label[span[contains(text(), 'Upload Facility Icon Available')]]/input[@name='file']"));
    private static final Locator IMAGE_UPLOAD_UNAVAILABLE = new Locator("IMAGE_UPLOAD_UNAVAILABLE", new By.ByXPath("//label[span[contains(text(), 'Upload Facility Icon Unavailable')]]/input[@name='file']"));
    private static final Locator IMAGE_UPLOAD_DISABLED = new Locator("IMAGE_UPLOAD_DISABLED", new By.ByXPath("//label[span[contains(text(), 'Upload Facility Icon Disabled')]]/input[@name='file']"));
    private static final Locator SUCCESS_SNACKBAR = new Locator("SUCCESS_SNACKBAR", new By.ByXPath("//div[@class='_2m8F2q_notification_banner _2m8F2q_variant_success']"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", new By.ByXPath("//button[contains(text(), 'Save')]"));
    private static final Locator FERRY_FACILITY_LABEL_CELL = new Locator("FERRY_FACILITY_LABEL_CELL", new By.ByXPath("//td[@class='rc-table-cell'][2]"));
    private static final Locator FERRY_FACILITY_ID_NAME_CELL = new Locator("FERRY_FACILITY_ID_NAME_CELL", new By.ByXPath("//td[@class='rc-table-cell'][3]"));
    private static final Locator FERRY_FACILITY_EN_NAME_CELL = new Locator("FERRY_FACILITY_EN_NAME_CELL", new By.ByXPath("//td[@class='rc-table-cell'][4]"));
    private static final Locator EDIT_BUTTON = new Locator("EDIT_BUTTON", new By.ByCssSelector("a[href*='facilities/']"));
    private static final Locator ACTIVE_SWITCH = new Locator("ACTIVE_SWITCH", new By.ByXPath("(//div[@class='n8SXEW_toggle n8SXEW_toggle_small'])[1]"));
    private static final Locator ACCEPT_BUTTON = new Locator("ACCEPT_BUTTON", new By.ByXPath("//button[@class='KV-nSG_button KV-nSG_full_width KV-nSG_horizontal_padding KV-nSG_variant_primary']"));
    private static final Locator DELETE_BUTTON = new Locator("DELETE_BUTTON", new By.ByXPath("(//span[@class='HcPVsG_text HcPVsG_variant_activity'][normalize-space()='Delete'])[1]"));
    private static final Locator CLOSE_BUTTON = new Locator("CLOSE_BUTTON", new By.ByXPath("//button[@class='_2m8F2q_close_button']"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", new By.ByXPath("//button[@class='KV-nSG_button KV-nSG_horizontal_padding KV-nSG_variant_secondary' and contains(text(), 'Search')]"));

    public FerryFacilityPage(Session<T> session) {
        super(session);
    }

    public void goToFerryPage(String username, String password) {
        driverTools.maximizeWindow();

        if (Configs.ENVIRONMENT == Environment.GK) {
            driverTools.get("https://gatotkaca.tiket.com/admin/member");
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driverTools.get("https://preprod.tiket.com/admin/member");
        }

        if (!isFerryLoggedIn()) {
            FerryLoginPage<?> ferryLoginPage = new FerryLoginPage<>(session);
            ferryLoginPage.loginToFerryPage(username, password);
            driverTools.waitForElementToAppear(USERNAME_GREETING, 2);

            if (Configs.ENVIRONMENT == Environment.GK) {
                driverTools.get("https://gatotkaca.tiket.com/admin/ferry");
            } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
                driverTools.get("https://preprod.tiket.com/admin/ferry");
            }
        }
    }

    public boolean isFerryLoggedIn() {
        driverTools.waitForElementToAppear(USERNAME_GREETING, 2);
        return driverTools.isElementDisplayed(USERNAME_GREETING);
    }

    public void goToFacilityTab() {
        if (Configs.ENVIRONMENT == Environment.GK) {
            driverTools.get("https://gatotkaca.tiket.com/admin/ferry/facilities");
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driverTools.get("https://preprod.tiket.com/admin/ferry/facilities");
        }
    }

    public void goToAddFacilityPage() {
        driverTools.waitForElementToAppear(MOVE_TO_ADD_FACILITY_PAGE_BUTTON);
        driverTools.clickWithJavascriptExecutor(MOVE_TO_ADD_FACILITY_PAGE_BUTTON);
    }

    public void addImage(Locator imageInputLocator, String filepath) {
        driverTools.sendKeys(imageInputLocator, getLocalImagePath(filepath));
    }

    public void addFacilityImages(String filepath) {
        addImage(IMAGE_UPLOAD_AVAILABLE, filepath);
        addImage(IMAGE_UPLOAD_DISABLED, filepath);
        addImage(IMAGE_UPLOAD_UNAVAILABLE, filepath);
    }

    public void addNewFacility(String label, String priorityScore, String idName, String enName){
        driverTools.waitForElementToAppear(LABEL_INPUT);
        driverTools.sendKeys(LABEL_INPUT, label);
        driverTools.sendKeys(PRIORITY_INPUT, priorityScore);
        driverTools.sendKeys(ID_NAME_INPUT, idName);
        driverTools.sendKeys(EN_NAME_INPUT, enName);
    }

    public void waitForSnackbarToDisappear() {
        if (driverTools.isElementDisplayed(SUCCESS_SNACKBAR)) {
            driverTools.click(CLOSE_BUTTON);
        }
    }

    public void saveForm() {
        driverTools.clickWithJavascriptExecutor(SAVE_BUTTON);
    }

    public void clearSearchField() {
        WebElement searchField = driverTools.element(SEARCH_FIELD).get();
        driverTools.clearWithJsExecutor(searchField);
    }

    public void searchFerryFacility(String label) {
        int maxRetries = 5;
        searchFerryFacilityWithRetries(label, maxRetries);
    }

    private void searchFerryFacilityWithRetries(String label, int retriesLeft) {
        if (retriesLeft == 0) {
            throw new RuntimeException("Exceeded maximum retries for searchFerryFacility.");
        }

        clearSearchField();
        WebElement searchInput = driverTools.waitForElementToAppear(SEARCH_FIELD).get();
        try {
            waitForSnackbarToDisappear();
            driverTools.sendKeys(searchInput, label);
            driverTools.click(SEARCH_BUTTON);
        } catch (org.openqa.selenium.StaleElementReferenceException ex) {
            searchFerryFacilityWithRetries(label, retriesLeft - 1);
        }
    }

    public boolean verifySRPContainsFacilityLabel(String label) {
        return verifySRPContainsText(FERRY_FACILITY_LABEL_CELL, label);
    }

    public boolean verifySRPContainsFacilityIdName(String idName) {
        return verifySRPContainsText(FERRY_FACILITY_ID_NAME_CELL, idName);
    }

    public boolean verifySRPContainsFacilityEnName(String enName) {
        return verifySRPContainsText(FERRY_FACILITY_EN_NAME_CELL, enName);
    }

    private boolean verifySRPContainsText(Locator cellLocator, String text) {
        List<WebElement> elements = driverTools.elements(cellLocator).get();
        return elements.stream().allMatch(element -> driverTools.getText(element).contains(text));
    }


    public String getLocalImagePath(String filepath){
        File image = new File(filepath);
        return image.getAbsolutePath();
    }

    public boolean isSuccessSnackbarDisplayed() {
        return driverTools.waitForElementToAppear(SUCCESS_SNACKBAR, 10).isPresent();
    }

    public String getFacilityId(){
        WebElement element = driverTools.element(EDIT_BUTTON).get();
        String href = element.getAttribute("href");
        return href.substring(href.lastIndexOf('/') + 1);
    }

    public boolean verifyTheSRPIsNotEmpty() {
        List<WebElement> classLabels = driverTools.waitForElementsToAppear(FERRY_FACILITY_LABEL_CELL, 3).orElse(null);
        return classLabels != null && !classLabels.isEmpty();
    }

    public void goToEditFacilityPage() {
        driverTools.waitForElementToAppear(EDIT_BUTTON);
        driverTools.clickWithJavascriptExecutor(EDIT_BUTTON);
    }

    public void editFerryFacility(String idName, String enName) {
        driverTools.waitForElementToAppear(ID_NAME_INPUT);
        driverTools.sendKeys(ID_NAME_INPUT, Keys.COMMAND + "a");
        driverTools.sendKeys(ID_NAME_INPUT, idName);
        driverTools.sendKeys(EN_NAME_INPUT, Keys.COMMAND + "a");
        driverTools.sendKeys(EN_NAME_INPUT, enName);
        driverTools.click(SAVE_BUTTON);
    }

    public void toggleActiveFacility() {
        toggleActivateWithRetry(5);
    }

    private void toggleActivateWithRetry(int retriesLeft) {
        if (retriesLeft == 0) {
            throw new RuntimeException("Exceeded maximum retries for toggleActivate.");
        }
        waitForSnackbarToDisappear();
        WebElement activateSwitch = driverTools.waitForElementToAppear(ACTIVE_SWITCH).get();
        driverTools.click(activateSwitch);
        WebElement acceptButton = driverTools.waitForElementToAppear(ACCEPT_BUTTON,5).orElse(null);
        if (acceptButton == null) {
            toggleActivateWithRetry(retriesLeft - 1);
        }
        driverTools.click(acceptButton);
    }

    public void deleteFacility() {
        driverTools.waitForElementToAppear(DELETE_BUTTON);
        driverTools.click(DELETE_BUTTON);
        driverTools.waitForElementToAppear(ACCEPT_BUTTON);
        driverTools.click(ACCEPT_BUTTON);
    }
}