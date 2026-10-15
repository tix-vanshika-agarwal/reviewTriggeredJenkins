package com.tiket.web.dashboard.page.nft.airport_transfer;

import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import com.tiket.web.dashboard.page.member_dashboard.MemberLoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;

public class AirportTransferFacilityPage<T extends WebDriver> extends BasePage<T> {
    private static final Locator USERNAME_GREETING = new Locator("USERNAME_GREETING", new By.ByXPath("//div[contains(@class, 'header_dashboard')]"));
    private static final Locator SEARCH_INPUT_FIELD = new Locator("SEARCH_INPUT_FIELD", new By.ByXPath("//input[@placeholder='Cari' or @placeholder='Search']"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", new By.ByXPath("//button[normalize-space()='Search']"));
    private static final Locator RESULT_TABLE = new Locator("RESULT_TABLE", new By.ByXPath("//div[@class='ant-table-wrapper table-v4']"));
    private static final Locator TAMBAH_BUTTON = new Locator("TAMBAH_BUTTON", new By.ByXPath("//button[normalize-space() = 'Tambah Facility' or normalize-space() = 'Add Facility']"));
    private static final Locator IMAGE_PREVIEW = new Locator("IMAGE_PREVIEW", new By.ByXPath("//div[@class='image-preview']"));
    private static final Locator ID_NAME_INPUT = new Locator("ID_NAME_INPUT", new By.ByXPath("//input[@name='nameId']"));
    private static final Locator EN_NAME_INPUT = new Locator("EN_NAME_INPUT", new By.ByXPath("//input[@name='nameEn']"));
    private static final Locator ICON_UPLOAD = new Locator("ICON_UPLOAD", new By.ByXPath("//*[@id='file']"));
    private static final Locator SIMPAN_BUTTON = new Locator("SIMPAN_BUTTON", new By.ByXPath("//button[normalize-space()='Simpan' or normalize-space()='Save']"));
    private static final Locator LABEL_INPUT = new Locator("LABEL_INPUT", new By.ByXPath("//input[@name='label']"));
    private static final Locator MODAL = new Locator("MODAL", new By.ByXPath("(//div[@class='ant-notification-notice ant-notification-notice-success ant-notification-notice-closable'])[1]"));
    private static final Locator DELETE_BUTTON = new Locator("DELETE_BUTTON", new By.ByXPath("(//a[@class='action-item'][normalize-space()='Delete'])[1]"));
    private static final Locator ACCEPT_BUTTON = new Locator("ACCEPT_BUTTON", new By.ByXPath("(//button[normalize-space() = 'Setuju' or normalize-space() = 'Agree'])[1]"));
    private static final Locator EDIT_BUTTON = new Locator("EDIT_BUTTON", new By.ByXPath("(//a[@class='action-item'][normalize-space()='Edit'])[1]"));
    private static final Locator SELECT_ICON_BUTTON = new Locator("SELECT_ICON_BUTTON", new By.ByXPath("//span[@class='b2 mobile high-emphasis bold hover invert tix-font v4']"));
    private static final Locator PRIORITY_SCORE = new Locator("PRIORITY_SCORE", new By.ByXPath("//input[@name='priorityScore']"));
    private static final Locator LIGHTROOM_LINK = new Locator("LIGHTROOM_LINK", new By.ByXPath("//input[@name='imageUrl']"));

    public AirportTransferFacilityPage(Session<T> session) {
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

    public void goToFacilityTab() {
        if (Configs.ENVIRONMENT == Environment.GK) {
            driverTools.get("https://gatotkaca.tiket.com/admin/airport-transfer/master-data/facility");
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driverTools.get("https://preprod.tiket.com/admin/airport-transfer/master-data/facility");
        }
    }

    public void searchFacilityByName(String name){
        driverTools.waitForElementToAppear(SEARCH_INPUT_FIELD);
        driverTools.sendKeys(SEARCH_INPUT_FIELD, name);
        driverTools.clickWithJavascriptExecutor(SEARCH_BUTTON);
    }

    public void addNewFacility(String label, String idName, String enName, String imageLink, String priorityScore){
        driverTools.waitForElementToBeClickable(TAMBAH_BUTTON);
        driverTools.clickWithJavascriptExecutor(TAMBAH_BUTTON);

        driverTools.sendKeys(LABEL_INPUT, label);
        driverTools.sendKeys(ID_NAME_INPUT, idName);
        driverTools.sendKeys(EN_NAME_INPUT, enName);
        driverTools.sendKeys(PRIORITY_SCORE, priorityScore);

        imageLink = "https://" + imageLink;
        driverTools.sendKeys(LIGHTROOM_LINK, imageLink);

        driverTools.clickWithJavascriptExecutor(SIMPAN_BUTTON);
    }

    public boolean verifySuccessSnackbarDisplayed(String expectedText){
        WebElement modal = driverTools.waitForElementToAppear(MODAL).get();
        return modal.getText().contains(expectedText);
    }

    public void deleteFacility() {
        driverTools.waitForElementsToAppear(DELETE_BUTTON);
        WebElement deleteButton = driverTools.firstElement(DELETE_BUTTON).get();
        driverTools.clickWithJavascriptExecutor(deleteButton);

        WebElement acceptButton = driverTools.waitForElementToAppear(ACCEPT_BUTTON).get();
        driverTools.clickWithJavascriptExecutor(acceptButton);
    }

    public boolean verifySRPIsEmpty(String emptyMessage){
        WebElement resultTable = driverTools.firstElement(RESULT_TABLE).get();
        String resultText = resultTable.getText();
        return resultText.contains(emptyMessage);
    }

    public void editFacility(String newIDName, String newENName, String imageLink, String priorityScore) {
        driverTools.waitForElementToAppear(EDIT_BUTTON);
        driverTools.clickWithJavascriptExecutor(EDIT_BUTTON);

        driverTools.clickWithJavascriptExecutor(ID_NAME_INPUT);
        driverTools.sendKeys(ID_NAME_INPUT, newIDName);
        driverTools.clickWithJavascriptExecutor(EN_NAME_INPUT);
        driverTools.sendKeys(EN_NAME_INPUT, newENName);
        driverTools.sendKeys(PRIORITY_SCORE, priorityScore);

        imageLink = "https://" + imageLink;
        driverTools.sendKeys(LIGHTROOM_LINK, imageLink);

        driverTools.clickWithJavascriptExecutor(SIMPAN_BUTTON);
    }

    public String getLocalImagePath(String filepath){
        File image = new File(filepath);
        return image.getAbsolutePath();
    }
}
