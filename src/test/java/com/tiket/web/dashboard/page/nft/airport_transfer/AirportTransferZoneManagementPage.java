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

import java.io.File;

public class AirportTransferZoneManagementPage<T extends WebDriver> extends BasePage<T> {
    private static final Locator USERNAME_GREETING = new Locator("USERNAME_GREETING", new By.ByXPath("//div[contains(@class, 'header_dashboard')]"));
    private static final Locator ICON_UPLOAD = new Locator("ICON_UPLOAD", new By.ByXPath("//*[@id='file']"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", new By.ByXPath("//div[@class='zone-save']"));
    private static final Locator ZONE_ADD_BUTTON_ID = new Locator("ZONE_ADD_BUTTON_ID", new By.ByXPath("//button[normalize-space()='Tambah Zona']"));
    private static final Locator ZONE_ADD_BUTTON_EN = new Locator("ZONE_ADD_BUTTON_EN", new By.ByXPath("//button[normalize-space()='Add Zone']"));
    private static final Locator ZONE_PACKAGE_NAME = new Locator("ZONE_PACKAGE_NAME", new By.ByXPath("//input[@name='zonePackageName']"));
    private static final Locator ZONE_AIRPORT_DROPDOWN = new Locator("ZONE_AIRPORT_DROPDOWN", new By.ByXPath("//div[contains(@class,'css-1s2u09g-control') or contains(@class,'dropdown-select')]") );
    private static final Locator ZONE_DROPDOWN_INPUT = new Locator("ZONE_DROPDOWN_INPUT", new By.ByXPath("//input[contains(@id,'react-select') and contains(@id,'-input')]") );
    private static final Locator ZONE_DROPDOWN_INDICATOR = new Locator("ZONE_DROPDOWN_INDICATOR", new By.ByXPath("//div[contains(@class,'indicator') or contains(@class,'IndicatorsContainer') or contains(@class,'css-tlfecz-indicatorContainer')]") );
    private static final Locator FIRST_OPTION_ZONE = new Locator("FIRST_OPTION_ZONE", new By.ByXPath("(//div[starts-with(@id,'react-select') and contains(@id,'-listbox')]//div[contains(@id,'-option')])[1]"));
    private static final Locator ZONE_SNACKBAR = new Locator("ZONE_SNACKBAR", new By.ByXPath("//div[@role='alert']"));
    private static final Locator ACTIVE_SWITCH = new Locator("ZONE_ACTIVE_SWITCH", new By.ByXPath("(//div[@class='switch  switch-blue'])[1]"));
    private static final Locator ACTIVE_STATUS = new Locator("ZONE_ACTIVE_STATUS", new By.ByXPath("(//div[@class='widgets-content'])[1]"));
    private static final Locator YA_BUTTON = new Locator("YA_BUTTON", new By.ByXPath("//button[normalize-space()='Ya']"));
    private static final Locator YES_BUTTON = new Locator("YES_BUTTON", new By.ByXPath("//button[normalize-space()='Yes']"));
    private static final Locator EDIT_BUTTON = new Locator("EDIT_BUTTON", new By.ByXPath("(//button[@class='tix-button v4 edit-zone-action-btn mobile button waves-effect waves-secondary tertiary'])"));

    public AirportTransferZoneManagementPage(Session<T> session) {
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

    public void goToZoneManagementTab() {
        if (Configs.ENVIRONMENT == Environment.GK) {
            driverTools.get("https://gatotkaca.tiket.com/admin/airport-transfer/zone-management");
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driverTools.get("https://preprod.tiket.com/admin/airport-transfer/zone-management");
        }
    }

    public void addNewZone(String zoneName, String filePath){
        WebElement addZoneButton;
        if (driverTools.isElementDisplayed(ZONE_ADD_BUTTON_ID)){
            addZoneButton = driverTools.waitForElementToAppear(ZONE_ADD_BUTTON_ID).get();
        } else {
            addZoneButton = driverTools.waitForElementToAppear(ZONE_ADD_BUTTON_EN).get();
        }
        driverTools.clickWithJavascriptExecutor(addZoneButton);

        WebElement zoneNameInput = driverTools.waitForElementToAppear(ZONE_PACKAGE_NAME).get();
        driverTools.sendKeys(zoneNameInput, zoneName);

        new AirportTransferFleetPage<>(session).selectFirstDropdownOption(ZONE_AIRPORT_DROPDOWN, FIRST_OPTION_ZONE, ZONE_DROPDOWN_INPUT, ZONE_DROPDOWN_INDICATOR);

        String filepath = getLocalFilePath(filePath);
        driverTools.sendKeys(ICON_UPLOAD, filepath);
        String filename = extractFilename(filepath);
        getImagePreview(filename);
        driverTools.clickWithJavascriptExecutor(SAVE_BUTTON);
    }

    private String extractFilename(String filePath){
        int lastSlashIndex = filePath.lastIndexOf('/');
        if (lastSlashIndex >= 0) {
            return filePath.substring(lastSlashIndex + 1);
        } else {
            return filePath;
        }
    }

    public boolean verifyStatusIsChanged(String expectedStatus){
        WebElement status = driverTools.waitForElementToAppear(ACTIVE_STATUS).get();
        return driverTools.getText(status).contains(String.format(expectedStatus));
    }

    public boolean verifySuccessSnackbarDisplayed(String expectedText){
        WebElement modal = driverTools.waitForElementToAppear(ZONE_SNACKBAR).get();
        return driverTools.getText(modal).contains(expectedText);
    }

    public void toggleActiveZone(){
        if(driverTools.isElementDisplayed(ZONE_SNACKBAR)){
            driverTools.waitForElementToDisappear(ZONE_SNACKBAR,10);
        }

        WebElement activateSwitch = driverTools.waitForElementToAppear(ACTIVE_SWITCH).get();
        driverTools.clickWithJavascriptExecutor(activateSwitch);
        WebElement acceptButton = driverTools.waitForElementToAppear(YA_BUTTON).get();
        if (!driverTools.isElementDisplayed(acceptButton)) {
            acceptButton = driverTools.waitForElementToAppear(YES_BUTTON).get();
        }
        driverTools.clickWithJavascriptExecutor(acceptButton);
    }

    public boolean verifySuccessSnackbarIsDisplayed(){
        return driverTools.isElementDisplayed(ZONE_SNACKBAR);
    }

    public void editZone(String zoneName){
        driverTools.waitForElementToAppear(EDIT_BUTTON);
        WebElement editButton = driverTools.firstElement(EDIT_BUTTON).get();
        driverTools.clickWithJavascriptExecutor(editButton);

        WebElement zoneNameInput = driverTools.waitForElementToAppear(ZONE_PACKAGE_NAME).get();
        driverTools.sendKeys(zoneNameInput, zoneName);

        driverTools.clickWithJavascriptExecutor(SAVE_BUTTON);
    }

    public void addNewZone(){
        WebElement addZoneButton;
        if (driverTools.isElementDisplayed(ZONE_ADD_BUTTON_ID)){
            addZoneButton = driverTools.waitForElementToAppear(ZONE_ADD_BUTTON_ID).get();
        } else {
            addZoneButton = driverTools.waitForElementToAppear(ZONE_ADD_BUTTON_EN).get();
        }
        driverTools.clickWithJavascriptExecutor(addZoneButton);
        driverTools.waitForElementToAppear(SAVE_BUTTON);
        driverTools.clickWithJavascriptExecutor(SAVE_BUTTON);
    }

    private WebElement getImagePreview(String filename){
        return driverTools.waitForElementToAppear(new Locator("FILENAME", new By.ByXPath("//span[normalize-space()='"+ filename + "']"))).get();
    }

    public String getLocalFilePath(String filePath){
        File file = new File(filePath);
        return file.getAbsolutePath();
    }

}