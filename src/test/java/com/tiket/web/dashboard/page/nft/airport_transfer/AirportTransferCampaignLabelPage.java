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

public class AirportTransferCampaignLabelPage<T extends WebDriver> extends BasePage<T> {
    private static final Locator USERNAME_GREETING = new Locator("USERNAME_GREETING", new By.ByXPath("//div[contains(@class, 'header_dashboard')]"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", new By.ByXPath("//button[normalize-space()='Search']"));
    private static final Locator RESULT_TABLE = new Locator("RESULT_TABLE", new By.ByXPath("//div[@class='ant-table-wrapper table-v4']"));
    private static final Locator IMAGE_PREVIEW = new Locator("IMAGE_PREVIEW", new By.ByXPath("//div[@class='image-preview']"));
    private static final Locator ICON_UPLOAD = new Locator("ICON_UPLOAD", new By.ByXPath("//*[@id='file']"));
    private static final Locator SIMPAN_BUTTON = new Locator("SIMPAN_BUTTON", new By.ByXPath("//button[normalize-space()='Simpan' or normalize-space()='Save']"));
    private static final Locator MODAL = new Locator("MODAL", new By.ByXPath("(//div[@class='ant-notification-notice ant-notification-notice-success ant-notification-notice-closable'])[1]"));
    private static final Locator ACCEPT_BUTTON = new Locator("ACCEPT_BUTTON", new By.ByXPath("(//button[normalize-space()='Setuju' or normalize-space()='Agree'])[1]"));
    private static final Locator ACTIVE_SWITCH = new Locator("ACTIVE_SWITCH", new By.ByXPath("(//button[@role='switch'])[1]"));
    private static final Locator ADD_BUTTON_CAMPAIGN_LABEL = new Locator("ADD_BUTTON_CAMPAIGN_LABEL", new By.ByXPath("//button[normalize-space()='Add Campaign Label' or normalize-space()='Tambah Campaign Label']"));
    private static final Locator CAMPAIGN_LABEL_TITLE = new Locator("CAMPAIGN_LABEL_TITLE", new By.ByXPath("//input[@name='campaign-label-title']"));
    private static final Locator CAMPAIGN_LABEL_ID = new Locator("CAMPAIGN_LABEL_ID", new By.ByXPath("//input[@name='campaign-label-id']"));
    private static final Locator CAMPAIGN_LABEL_EN = new Locator("CAMPAIGN_LABEL_EN", new By.ByXPath("//input[@name='campaign-label-en']"));
    private static final Locator CAMPAIGN_LABEL_BG_COLOR = new Locator("CAMPAIGN_LABEL_BG_COLOR", new By.ByXPath("//div[@class='dropdown-select placeholder']"));
    private static final Locator CAMPAIGN_LABEL_BG_COLOR_OPTION =  new Locator("CAMPAIGN_LABEL_BG_COLOR_OPTION", new By.ByXPath("(//div[@class='radio'])[2]"));
    private static final Locator CAMPAIGN_LABEL_SEARCH_INPUT_FIELD = new Locator("CAMPAIGN_LABEL_SEARCH_INPUT_FIELD", new By.ByXPath("//input[@placeholder='Cari...']"));
    private static final Locator EDIT_BUTTON_ID = new Locator("EDIT_BUTTON_ID", new By.ByXPath("(//a[@class='action-item'][normalize-space()='Ubah' or normalize-space()='Edit' or normalize-space()='edit'])[1]"));
    private static final Locator CAMPAIGN_LABEL_DELETE_IMAGE_BUTTON = new Locator("CAMPAIGN_LABEL_DELETE_IMAGE_BUTTON", new By.ByXPath("//div[@class='image-preview']//button[@type='button']"));
    private static final Locator CAMPAIGN_LABEL_BG_COLOR_EDIT = new Locator("CAMPAIGN_LABEL_BG_COLOR_EDIT", new By.ByXPath("(//div[@class='dropdown-select '])[1]"));
    private static final Locator CAMPAIGN_LABEL_BG_COLOR_OPTION_ALT = new Locator("CAMPAIGN_LABEL_BG_COLOR_OPTION_ALT", new By.ByXPath("(//div[@class='radio'])[3]"));
    private static final Locator DELETE_BUTTON_ID = new Locator("DELETE_BUTTON_ID", new By.ByXPath("(//a[@class='action-item'][normalize-space()='Hapus' or normalize-space()='Delete'])[1]"));
    private static final Locator ACTIVE_STATUS = new Locator("ACTIVE_STATUS_CAMPAIGN_LABEL", new By.ByXPath("(//td[@class='ant-table-cell'])[6]"));

    public AirportTransferCampaignLabelPage(Session<T> session) {
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
        driverTools.waitForElementToAppear(EDIT_BUTTON_ID);
        List<WebElement> campaignLabelResults = driverTools.elements(RESULT_TABLE).orElseThrow();
        for (WebElement e : campaignLabelResults) {
            String campaignLabelNameValue = driverTools.getText(e);
            logger.log("Campaign Label name value : " + campaignLabelNameValue);
            if (!campaignLabelNameValue.contains(name)) {
                return false;
            }
        }
        return true;
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
        WebElement status = driverTools.waitForElementToAppear(ACTIVE_STATUS).get();
        return status.getText().contains(String.format(expectedStatus));
    }

    public void goToCampaignLabelTab() {
        if (Configs.ENVIRONMENT == Environment.GK) {
            driverTools.get("https://gatotkaca.tiket.com/admin/airport-transfer/master-data/campaign-label");
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driverTools.get("https://preprod.tiket.com/admin/airport-transfer/master-data/campaign-label");
        }
    }

    public void createNewCampaignLabel(String title, String labelId, String labelEn, String filepath){
        WebElement addButton = driverTools.waitForElementToAppear(ADD_BUTTON_CAMPAIGN_LABEL).get();
        driverTools.clickWithJavascriptExecutor(addButton);

        WebElement titleField = driverTools.waitForElementToAppear(CAMPAIGN_LABEL_TITLE).get();
        driverTools.sendKeys(titleField, title);

        WebElement labelIdField = driverTools.waitForElementToAppear(CAMPAIGN_LABEL_ID).get();
        driverTools.sendKeys(labelIdField, labelId);

        WebElement labelEnField = driverTools.waitForElementToAppear(CAMPAIGN_LABEL_EN).get();
        driverTools.sendKeys(labelEnField, labelEn);

        driverTools.click(CAMPAIGN_LABEL_BG_COLOR);
        WebElement bgColorOption = driverTools.waitForElementToAppear(CAMPAIGN_LABEL_BG_COLOR_OPTION).get();
        driverTools.clickWithJavascriptExecutor(bgColorOption);

        driverTools.sendKeys(ICON_UPLOAD, getLocalImagePath(filepath));
        driverTools.waitForElementToAppear(IMAGE_PREVIEW);

        driverTools.clickWithJavascriptExecutor(SIMPAN_BUTTON);
    }

    public void searchCampaignLabelByName(String name){
        driverTools.waitForElementToAppear(CAMPAIGN_LABEL_SEARCH_INPUT_FIELD);
        driverTools.sendKeys(CAMPAIGN_LABEL_SEARCH_INPUT_FIELD, name);
        driverTools.clickWithJavascriptExecutor(SEARCH_BUTTON);
    }

    public void editCampaignLabel(String labelId, String labelEn, String filepath){
        driverTools.clickWithJavascriptExecutor(EDIT_BUTTON_ID);

        driverTools.waitForElementToAppear(CAMPAIGN_LABEL_BG_COLOR_EDIT);
        driverTools.clickWithJavascriptExecutor(CAMPAIGN_LABEL_BG_COLOR_EDIT);
        WebElement bgColorOption = driverTools.waitForElementToAppear(CAMPAIGN_LABEL_BG_COLOR_OPTION_ALT, 10).get();
        driverTools.clickWithJavascriptExecutor(bgColorOption);

        WebElement deleteImageButton = driverTools.waitForElementToBeClickable(CAMPAIGN_LABEL_DELETE_IMAGE_BUTTON).get();
        driverTools.clickWithJavascriptExecutor(deleteImageButton);

        WebElement labelIdField = driverTools.waitForElementToAppear(CAMPAIGN_LABEL_ID).get();
        driverTools.sendKeys(labelIdField, labelId);
        WebElement labelEnField = driverTools.waitForElementToAppear(CAMPAIGN_LABEL_EN).get();
        driverTools.sendKeys(labelEnField, labelEn);

        driverTools.sendKeys(ICON_UPLOAD, getLocalImagePath(filepath));
        driverTools.waitForElementToAppear(IMAGE_PREVIEW);
        driverTools.clickWithJavascriptExecutor(SIMPAN_BUTTON);
    }

    public void deleteCampaignLabel(){
        WebElement deleteButton = driverTools.waitForElementToAppear(DELETE_BUTTON_ID).get();
        driverTools.clickWithJavascriptExecutor(deleteButton);

        WebElement acceptButton = driverTools.waitForElementToAppear(ACCEPT_BUTTON).get();
        driverTools.clickWithJavascriptExecutor(acceptButton);
    }

    public void toggleActiveCampaignLabel(){
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
}