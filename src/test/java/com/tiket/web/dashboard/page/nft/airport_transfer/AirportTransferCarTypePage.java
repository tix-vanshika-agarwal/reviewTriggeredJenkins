package com.tiket.web.dashboard.page.nft.airport_transfer;

import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import com.tiket.web.dashboard.page.member_dashboard.MemberLoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.io.File;
import java.util.List;

public class AirportTransferCarTypePage<T extends WebDriver> extends BasePage<T> {
    private static final Locator USERNAME_GREETING = new Locator("USERNAME_GREETING", new By.ByXPath("//div[contains(@class, 'header_dashboard')]"));
    private static final Locator SEARCH_INPUT_FIELD_CARTYPE = new Locator("SEARCH_INPUT_FIELD_CARTYPE", new By.ByXPath("//input[@placeholder='Cari...']"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", new By.ByXPath("//button[normalize-space()='Search']"));
    private static final Locator RESULT_TABLE = new Locator("RESULT_TABLE", new By.ByXPath("//div[@class='ant-table-wrapper table-v4']"));
    private static final Locator TAMBAH_BUTTON_CARTYPE = new Locator("TAMBAH_BUTTON_CARTYPE", new By.ByXPath("//button[normalize-space()='Add Car Type' or normalize-space()='Tambah Car Type']"));
    private static final Locator IMAGE_PREVIEW = new Locator("IMAGE_PREVIEW", new By.ByXPath("//div[@class='image-preview']"));
    private static final Locator ID_NAME_INPUT = new Locator("ID_NAME_INPUT", new By.ByXPath("//input[@name='nameId']"));
    private static final Locator EN_NAME_INPUT = new Locator("EN_NAME_INPUT", new By.ByXPath("//input[@name='nameEn']"));
    private static final Locator ICON_UPLOAD = new Locator("ICON_UPLOAD", new By.ByXPath("//*[@id='file']"));
    private static final Locator SIMPAN_BUTTON = new Locator("SIMPAN_BUTTON", new By.ByXPath("//button[normalize-space()='Simpan' or normalize-space()='Save']"));
    private static final Locator LABEL_INPUT = new Locator("LABEL_INPUT", new By.ByXPath("//input[@name='label']"));
    private static final Locator CARD_TYPE_DROPDOWN = new Locator("CARD_TYPE_DROPDOWN", new By.ByXPath("//div[@class='tix-dashboard-select']//div[@class='dropdown-select placeholder' and .//small[contains(text(), 'Card Type')]] | //div[@class='tix-dashboard-select']//div[@class='dropdown-select ' and .//p[contains(text(), 'Card Type')]]"));
    private static final Locator MAX_PAX = new Locator("MAX_PAX", new By.ByXPath("//input[@name='maxPax']"));
    private static final Locator MAX_BAGGAGE = new Locator("MAX_BAGGAGE", new By.ByXPath("//input[@name='maxBaggage']"));
    private static final Locator USP_ID = new Locator("USP_ID", new By.ByXPath("//input[@name='uspId']"));
    private static final Locator USP_EN = new Locator("USP_EN", new By.ByXPath("//input[@name='uspEn']"));
    private static final Locator POSSIBLE_MODEL_ID = new Locator("POSSIBLE_MODEL_ID", new By.ByXPath("//input[@name='possibleCarModelId']"));
    private static final Locator POSSIBLE_MODEL_EN = new Locator("POSSIBLE_MODEL_EN", new By.ByXPath("//input[@name='possibleCarModelEn']"));
    private static final Locator LABEL_SELECTION = new Locator("LABEL_SELECTION", new By.ByXPath("//div[@class='tix-dashboard-select']//div[@class='dropdown-select placeholder' and .//small[contains(text(), 'Label Selection')]] | //div[@class='tix-dashboard-select']//div[@class='dropdown-select ' and .//p[contains(text(), 'Label Selection')]]"));
    private static final Locator MODAL = new Locator("MODAL", new By.ByXPath("(//div[@class='ant-notification-notice ant-notification-notice-success ant-notification-notice-closable'])[1]"));
    private static final Locator DELETE_BUTTON = new Locator("DELETE_BUTTON", new By.ByXPath("(//a[@class='action-item'][normalize-space()='Delete'])[1]"));
    private static final Locator ACCEPT_BUTTON = new Locator("ACCEPT_BUTTON", new By.ByXPath("(//button[normalize-space()='Setuju' or normalize-space()='Agree'])[1]"));
    private static final Locator EDIT_BUTTON = new Locator("EDIT_BUTTON", new By.ByXPath("(//a[@class='action-item'][normalize-space()='Edit'])[1]"));
    private static final Locator DELETE_IMAGE_BUTTON_CARTYPE = new Locator("DELETE_IMAGE_BUTTON_CARTYPE", new By.ByXPath("//div[@class='action-button-wrapper']//button[@type='button']"));
    private static final Locator ACTIVE_SWITCH = new Locator("ACTIVE_SWITCH", new By.ByXPath("(//button[@role='switch'])[1]"));
    private static final Locator ACTIVE_STATUS = new Locator("ACTIVE_STATUS", new By.ByXPath("(//td[@class='ant-table-cell'])[9]//div[not(descendant::*)] | (//td[@class='ant-table-cell'])[9]//span[not(descendant::*)]"));

    public AirportTransferCarTypePage(Session<T> session) {
        super(session);
    }

    public void goToATPage(String username, String password) throws InterruptedException {
        driverTools.maximizeWindow();
        new MemberLoginPage<>(session).adminLoginViaCookie(username, password);

        if (Configs.ENVIRONMENT == Environment.GK) {
            driverTools.get("https://gatotkaca.tiket.com/admin/airport-transfer");
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driverTools.get("https://preprod.tiket.com/admin/airport-transfer");
        }
    }


    public boolean isAirportTransferLoggedIn() {
        driverTools.waitForElementToAppear(USERNAME_GREETING, 2);
        return (driverTools.isElementDisplayed(USERNAME_GREETING));
    }

    public boolean verifySuccessSnackbarDisplayed(String expectedText){
        WebElement modal = driverTools.waitForElementToAppear(MODAL).get();
        return modal.getText().contains(expectedText);
    }

    public boolean verifySRPIsEmpty(String emptyMessage){
        WebElement resultTable = driverTools.firstElement(RESULT_TABLE).get();
        String resultText = resultTable.getText();
        return resultText.contains(emptyMessage);
    }

    public boolean verifySRPContainsCorrectName(String name) {
        driverTools.waitForElementToAppear(EDIT_BUTTON);
        List<WebElement> carTypeResults = driverTools.elements(RESULT_TABLE).orElseThrow();
        for (WebElement e : carTypeResults) {
            String carTypeNameValue = driverTools.getText(e);
            logger.log("Car Type name value : " + carTypeNameValue);
            if (!carTypeNameValue.contains(name)) {
                return false;
            }
        }
        return true;
    }

    public void goToCarTypeTab() {
        if (Configs.ENVIRONMENT == Environment.GK) {
            driverTools.get("https://gatotkaca.tiket.com/admin/airport-transfer/master-data/car-type");
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driverTools.get("https://preprod.tiket.com/admin/airport-transfer/master-data/car-type");
        }
    }

    public void searchCarTypeByName(String name){
        driverTools.waitForElementToAppear(SEARCH_INPUT_FIELD_CARTYPE);
        driverTools.sendKeys(SEARCH_INPUT_FIELD_CARTYPE, name);
        driverTools.clickWithJavascriptExecutor(SEARCH_BUTTON);
    }

    public void createNewCarType(String label, String idName, String enName, String maxPax, String maxBaggage, String uspID, String uspEN, String carModelId, String carModelEn, String filepath){
        driverTools.waitForElementToBeClickable(TAMBAH_BUTTON_CARTYPE);
        driverTools.clickWithJavascriptExecutor(TAMBAH_BUTTON_CARTYPE);

        selectOptionByIndex(CARD_TYPE_DROPDOWN, 1);
        driverTools.sendKeys(LABEL_INPUT, label);
        driverTools.sendKeys(ID_NAME_INPUT, idName);
        driverTools.sendKeys(EN_NAME_INPUT, enName);
        driverTools.sendKeys(MAX_PAX, maxPax);
        driverTools.sendKeys(MAX_BAGGAGE, maxBaggage);
        driverTools.sendKeys(USP_ID, uspID);
        driverTools.sendKeys(USP_EN, uspEN);
        driverTools.sendKeys(POSSIBLE_MODEL_ID, carModelId);
        driverTools.sendKeys(POSSIBLE_MODEL_EN, carModelEn);

        driverTools.sendKeys(ICON_UPLOAD, getLocalImagePath(filepath));
        driverTools.waitForElementToAppear(IMAGE_PREVIEW);

        selectOptionByIndex(LABEL_SELECTION, 1);
        driverTools.clickWithJavascriptExecutor(SIMPAN_BUTTON);
    }

    public void editCarType(String idName, String enName, String maxPax, String maxBaggage, String uspID, String uspEN, String carModelId, String carModelEn, String filepath){
        WebElement editButton = driverTools.waitForElementToAppear(EDIT_BUTTON).get();
        driverTools.clickWithJavascriptExecutor(editButton);

        WebElement deleteImageButton = driverTools.waitForElementToBeClickable(DELETE_IMAGE_BUTTON_CARTYPE).get();
        driverTools.clickWithJavascriptExecutor(deleteImageButton);
        driverTools.sendKeys(ID_NAME_INPUT, idName);
        driverTools.sendKeys(EN_NAME_INPUT, enName);
        driverTools.sendKeys(MAX_PAX, maxPax);
        driverTools.sendKeys(MAX_BAGGAGE, maxBaggage);
        driverTools.sendKeys(USP_ID, uspID);
        driverTools.sendKeys(USP_EN, uspEN);
        driverTools.sendKeys(POSSIBLE_MODEL_ID, carModelId);
        driverTools.sendKeys(POSSIBLE_MODEL_EN, carModelEn);

        driverTools.sendKeys(ICON_UPLOAD, getLocalImagePath(filepath));
        driverTools.waitForElementToAppear(IMAGE_PREVIEW);
        selectOptionByIndex(LABEL_SELECTION, 1);

        driverTools.clickWithJavascriptExecutor(SIMPAN_BUTTON);
    }

    public void deleteCarType() {
        driverTools.waitForElementsToAppear(DELETE_BUTTON);
        WebElement deleteButton = driverTools.firstElement(DELETE_BUTTON).get();
        driverTools.clickWithJavascriptExecutor(deleteButton);

        WebElement acceptButton = driverTools.waitForElementToAppear(ACCEPT_BUTTON).get();
        driverTools.clickWithJavascriptExecutor(acceptButton);
    }

    public void scrollTable(){
        driverTools.clickWithJavascriptExecutor(RESULT_TABLE);
        Actions action = new Actions(driver);
        for (int i = 0; i < 30; i++){
            action.sendKeys(Keys.ARROW_RIGHT);
            action.perform();
        }
        driverTools.clickWithJavascriptExecutor(RESULT_TABLE);
    }

    public boolean verifyStatusIsChanged(String expectedStatus){
        java.util.Optional<WebElement> statusOpt = driverTools.waitForElementToAppear(ACTIVE_STATUS);
        if (statusOpt.isPresent()) {
            String statusText = driverTools.getText(statusOpt.get());
            return statusText != null && statusText.contains(expectedStatus);
        }
        return false;
    }

    public void toggleActiveCarType(){
        if(driverTools.isElementDisplayed(MODAL)){
            driverTools.waitForElementToDisappear(MODAL,10);
        }

        WebElement activateSwitch = driverTools.waitForElementToAppear(ACTIVE_SWITCH).get();
        driverTools.clickWithJavascriptExecutor(activateSwitch);
        WebElement acceptButton = driverTools.waitForElementToAppear(ACCEPT_BUTTON).get();
        driverTools.clickWithJavascriptExecutor(acceptButton);
    }

    public String getLocalImagePath(String filepath){
        File image = new File(filepath);
        return image.getAbsolutePath();
    }

    public void selectOptionByIndex(Locator dropdown, int index) {
        int maxRetries = 5;
        selectOptionByIndexWithRetries(dropdown, index, maxRetries);
    }

    private void selectOptionByIndexWithRetries(Locator dropdown, int index, int retriesLeft) {
        if (retriesLeft == 0) {
            throw new RuntimeException("Exceeded maximum retries for selectOptionByIndex.");
        }
        if (!driverTools.isElementDisplayed(new Locator("DROPDOWN_OPTION",new By.ByXPath(String.format("//div[@class='wrapper-checkbox'][%d] | //div[@class='radio'][%d]", index, index))))) {
            driverTools.clickWithJavascriptExecutor(dropdown);
        }
        WebElement option = driverTools.waitForElementToAppear(new Locator("DROPDOWN_OPTION",new By.ByXPath(String.format("//div[@class='wrapper-checkbox'][%d] | //div[@class='radio'][%d]", index, index))), 3).get();
        try {
            driverTools.click(option);
        } catch (org.openqa.selenium.StaleElementReferenceException ex) {
            selectOptionByIndexWithRetries(dropdown, index, retriesLeft - 1);
        }
    }
}