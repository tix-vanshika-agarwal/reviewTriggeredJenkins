package com.tiket.web.dashboard.page.nft.airport_transfer;

import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import com.tiket.web.dashboard.page.member_dashboard.MemberLoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import com.tiket.testbase.Assertion;

import java.io.File;
import java.time.Duration;

public class AirportTransferAirportPage<T extends WebDriver> extends BasePage<T> {
    private static final Locator USERNAME_GREETING = new Locator("USERNAME_GREETING", new By.ByXPath("//div[contains(@class, 'header_dashboard')]"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", new By.ByXPath("//button[normalize-space()='Search']"));
    private static final Locator RESULT_TABLE = new Locator("RESULT_TABLE", new By.ByXPath("//div[@class='ant-table-wrapper table-v4']"));
    private static final Locator IMAGE_PREVIEW = new Locator("IMAGE_PREVIEW", new By.ByXPath("//div[@class='image-preview']"));
    private static final Locator ICON_UPLOAD = new Locator("ICON_UPLOAD", new By.ByXPath("//*[@id='file']"));
    private static final Locator SIMPAN_BUTTON = new Locator("SIMPAN_BUTTON", new By.ByXPath("//button[normalize-space()='Simpan' or normalize-space()='Save']"));
    private static final Locator MODAL = new Locator("MODAL", new By.ByXPath("(//div[@class='ant-notification-notice ant-notification-notice-success ant-notification-notice-closable'])[1]"));
    private static final Locator ACCEPT_BUTTON = new Locator("ACCEPT_BUTTON", new By.ByXPath("(//button[normalize-space()='Setuju' or normalize-space()='Agree'])[1]"));
    private static final Locator DELETE_IMAGE_BUTTON = new Locator("DELETE_IMAGE_BUTTON", new By.ByXPath("//div[@class='image-preview']//button[@type='button']"));
    private static final Locator ACTIVE_SWITCH = new Locator("ACTIVE_SWITCH", new By.ByXPath("(//button[@role='switch'])[1]"));
    private static final Locator EDIT_BUTTON_ID = new Locator("EDIT_BUTTON_ID", new By.ByXPath("(//a[@class='action-item'][normalize-space()='edit' or normalize-space()='Edit'])[1]"));
    private static final Locator DELETE_BUTTON_ID = new Locator("DELETE_BUTTON_ID", new By.ByXPath("(//a[@class='action-item'][normalize-space()='Hapus' or normalize-space()='Delete'])[1]"));
    private static final Locator ADD_AIRPORT_BUTTON = new Locator("ADD_AIRPORT_BUTTON", new By.ByXPath("//button[normalize-space()='Tambah Airport' or normalize-space()='Add Airport']"));
    private static final Locator AIRPORT_NAME = new Locator("AIRPORT_NAME", new By.ByXPath("//input[@name='airportName']"));
    private static final Locator CITY_NAME = new Locator("CITY_NAME", new By.ByXPath("//input[@name='cityName']"));
    private static final Locator STATION_CODE = new Locator("STATION_CODE", new By.ByXPath("//input[@name='stationCode']"));
    private static final Locator LATITUDE = new Locator("LATITUDE", new By.ByXPath("//input[@name='latitude']"));
    private static final Locator LONGITUDE = new Locator("LONGITUDE", new By.ByXPath("//input[@name='longtitude']"));
    private static final Locator AIRPORT_DESCRIPTION_FIELD = new Locator("AIRPORT_DESCRIPTION_FIELD", new By.ByXPath("//div[@class='public-DraftStyleDefault-block public-DraftStyleDefault-ltr']"));
    private static final Locator AIRPORT_EN_DESCRIPTION_SWITCH = new Locator("AIRPORT_EN_DESCRIPTION_SWITCH", new By.ByXPath("//div[@class='title unactive-tab-color']"));
    private static final Locator AIRPORT_SEARCH_INPUT_FIELD = new Locator("AIRPORT_SEARCH_INPUT_FIELD", new By.ByXPath("//input[@placeholder='Cari...' or @placeholder='Search...' or @placeholder='Search']"));
    private static final Locator ACTIVE_STATUS = new Locator("ACTIVE_STATUS_AIRPORT", new By.ByXPath("(//td[@class='ant-table-cell'])[9]"));
    private static final Locator EDIT_BOOTH = new Locator("EDIT_BOOTH", new By.ByXPath("(//a[@class='action-item'][normalize-space()='Edit Booth' or normalize-space()='Edit Stan'])[1]"));
    private static final Locator BOOTH_NAME_FIELD = new Locator("BOOTH_NAME_FIELD", new By.ByXPath("//input[@name='stan-0']"));
    private static final Locator YA_BUTTON = new Locator("YA_BUTTON", new By.ByXPath("(//button[normalize-space()='Ya'])"));
    private static final Locator COUNTRY_NAME_AND_CODE = new Locator("COUNTRY_NAME_AND_CODE", new By.ByXPath("(//div[@class='dropdown-select placeholder'])[1]"));
    private static final Locator COUNTRY_NAME_AND_CODE_EDIT = new Locator("COUNTRY_NAME_AND_CODE_EDIT", new By.ByXPath("//div[contains(@class,'dropdown-select') and .//p[normalize-space()='Country Name & Code']]"));
    private static final Locator FIRST_OPTION_DROPDOWN = new Locator("FIRST_OPTION_DROPDOWN", new By.ByXPath("//div[contains(@class,'tix-dashboard-select__menu-list')]//div[contains(@class,'tix-dashboard-select__option')][1]"));
    private static final Locator MIN_BOOKING_TIME_TRANSFER_OUT = new Locator("MIN_BOOKING_TIME_TRANSFER_OUT", new By.ByXPath("//input[@name='minBookingTransferOut']"));
    private static final Locator MAX_BOOKING_TIME_TRANSFER_IN = new Locator("MAX_BOOKING_TIME_TRANSFER_OUT", new By.ByXPath("//input[@name='minBookingTransferIn']"));
    private static final Locator AUTOCOMPLETE_RADIUS = new Locator("AUTOCOMPLETE_RADIUS", new By.ByXPath("//input[@name='autoCompleteRadius']"));

    // New locators for new dashbaord
    private static final Locator DASHBOARD_AIRPORT_TRANSFER_CARD = new Locator("DASHBOARD_AIRPORT_TRANSFER_CARD", new By.ByXPath("//div[contains(@class, 'Homepage_card')]//span[text()='Airport Transfer']"));
    private static final Locator SIDEBAR_AIRPORT_STATION_MENU = new Locator("SIDEBAR_AIRPORT_STATION_MENU", new By.ByXPath("//button[@data-level='1' and .//span[contains(normalize-space(string(.)), 'Airport') and contains(normalize-space(string(.)), 'Station')]]"));
    private static final Locator AIRPORT_SEARCH_INPUT = new Locator("AIRPORT_SEARCH_INPUT", new By.ByXPath("//input[@id='search-country-city-airport-or-code']"));
    private static final Locator EDIT_ACTION_BUTTON = new Locator("EDIT_ACTION_BUTTON", new By.ByXPath("//button[contains(@class, 'AirportActionsCell_action')]//span[text()='Edit']"));
    private static final Locator EDIT_MIN_TRANSFER_OUT_INPUT = new Locator("EDIT_MIN_TRANSFER_OUT_INPUT", new By.ByXPath("//input[@id='minbookingtimetransferout']"));
    private static final Locator EDIT_MIN_TRANSFER_IN_INPUT = new Locator("EDIT_MIN_TRANSFER_IN_INPUT", new By.ByXPath("//input[@id='minbookingtimetransferin']"));
    private static final Locator EDIT_LATITUDE_INPUT = new Locator("EDIT_LATITUDE_INPUT", new By.ByXPath("//input[@id='latitude']"));
    private static final Locator EDIT_LONGITUDE_INPUT = new Locator("EDIT_LONGITUDE_INPUT", new By.ByXPath("//input[@id='longitude']"));
    private static final Locator EDIT_RADIUS_INPUT = new Locator("EDIT_RADIUS_INPUT", new By.ByXPath("//input[@id='radius']"));
    private static final Locator EDIT_DESCRIPTION_CONTENT_AREA_IDN = new Locator("EDIT_DESCRIPTION_CONTENT_AREA_IDN", new By.ByXPath("(//div[@contenteditable='true' and contains(@class, 'TextBox_editor_content')])[1]"));
    private static final Locator EDIT_DESCRIPTION_CONTENT_AREA_ENG = new Locator("EDIT_DESCRIPTION_CONTENT_AREA_ENG", new By.ByXPath("(//div[@contenteditable='true' and contains(@class, 'TextBox_editor_content')])[last()]"));
    private static final Locator EDIT_SAVE_BUTTON = new Locator("EDIT_SAVE_BUTTON", new By.ByXPath("//button[@type='submit' and .//span[normalize-space()='Save']]"));
    private static final Locator EDIT_ENG_TAB_SWITCH = new Locator("EDIT_ENG_TAB_SWITCH", new By.ByXPath("//button[@role='tab' and normalize-space()='ENG']"));
    private static final Locator NEW_SUCCESS_SNACKBAR = new Locator("NEW_SUCCESS_SNACKBAR", new By.ByXPath("//div[contains(@class, 'SnackbarsPositioner')]"));
    private static final Locator EDIT_BOOTH_ACTION_BUTTON = new Locator("EDIT_BOOTH_ACTION_BUTTON", new By.ByXPath("//button[.//span[normalize-space()='Edit Booth']]"));
    private static final Locator BOOTH_NAME_INPUT = new Locator("BOOTH_NAME_INPUT", new By.ByXPath("//input[@id='booth-name']"));
    private static final Locator BOOTH_TOGGLE_BUTTON = new Locator("BOOTH_TOGGLE_BUTTON", new By.ByXPath("//div[contains(@class, 'Toggle_toggle')]"));
    private static final Locator BOOTH_TOGGLE_CHECKBOX = new Locator("BOOTH_TOGGLE_CHECKBOX", new By.ByXPath("//input[contains(@class, 'Toggle_hidden_checkbox')]"));
    private static final Locator BOOTH_SAVE_BUTTON = new Locator("BOOTH_SAVE_BUTTON", new By.ByXPath("//button[@type='button' and .//span[normalize-space()='Save']]"));
    private static final Locator AIRPORT_STATUS_TOGGLE = new Locator("AIRPORT_STATUS_TOGGLE", new By.ByXPath("(//span[contains(@class, 'StatusToggleCell_root')]//div[contains(@class, 'Toggle_toggle')])[1]"));
    private static final Locator AIRPORT_STATUS_TOGGLE_CHECKBOX = new Locator("AIRPORT_STATUS_TOGGLE_CHECKBOX", new By.ByXPath("(//span[contains(@class, 'StatusToggleCell_root')]//input[contains(@class, 'Toggle_hidden_checkbox')])[1]"));
    private static final Locator TOGGLE_CONFIRM_YES_BUTTON = new Locator("TOGGLE_CONFIRM_YES_BUTTON", new By.ByXPath("//button[.//span[normalize-space()='Yes']]"));


    public AirportTransferAirportPage(Session<T> session) {
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

    public void goToAirportTab(){
        if (Configs.ENVIRONMENT == Environment.GK) {
            driverTools.get("https://gatotkaca.tiket.com/admin/airport-transfer/master-data/airport");
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driverTools.get("https://preprod.tiket.com/admin/airport-transfer/master-data/airport");
        }
    }

    public boolean verifySuccessSnackbarDisplayed(String expectedText){
        WebElement modal = driverTools.waitForElementToAppear(MODAL).get();
        return driverTools.getText(modal).contains(expectedText);
    }

    public boolean verifySRPIsEmpty(String emptyMessage){
        WebElement resultTable = driverTools.firstElement(RESULT_TABLE).get();
        String resultText = driverTools.getText(resultTable);
        return resultText.contains(emptyMessage);
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
        return driverTools.getText(status).contains(String.format(expectedStatus));
    }

    public void addNewAirport(String airportName, String cityName, String stationCode, String latitude, String longitude, String description, int minTransferOut, int minTransferIn, int autoCompleteRadius){
        WebElement addButton = driverTools.waitForElementToAppear(ADD_AIRPORT_BUTTON).get();
        driverTools.clickWithJavascriptExecutor(addButton);

        driverTools.waitForElementToAppear(COUNTRY_NAME_AND_CODE);
        driverTools.clickWithJavascriptExecutor(COUNTRY_NAME_AND_CODE);
        driverTools.click(FIRST_OPTION_DROPDOWN);

        driverTools.sendKeys(AIRPORT_NAME, airportName);
        driverTools.sendKeys(CITY_NAME, cityName);
        driverTools.sendKeys(STATION_CODE, stationCode);
        driverTools.sendKeys(LONGITUDE, longitude);
        driverTools.sendKeys(LATITUDE, latitude);

        driverTools.click(COUNTRY_NAME_AND_CODE);
        driverTools.click(FIRST_OPTION_DROPDOWN);

        WebElement descriptionField = driverTools.waitForElementToAppear(AIRPORT_DESCRIPTION_FIELD).get();
        driverTools.clickWithJavascriptExecutor(descriptionField);
        driverTools.sendKeys(descriptionField, description);
        
        WebElement minBookingTimeTransferOut = driverTools.waitForElementToAppear(MIN_BOOKING_TIME_TRANSFER_OUT).get();
        driverTools.clickWithJavascriptExecutor(minBookingTimeTransferOut);
        driverTools.sendKeys(minBookingTimeTransferOut, String.valueOf(minTransferOut));

        WebElement maxBookingTimeTransferIn = driverTools.waitForElementToAppear(MAX_BOOKING_TIME_TRANSFER_IN).get();
        driverTools.clickWithJavascriptExecutor(maxBookingTimeTransferIn);
        driverTools.sendKeys(maxBookingTimeTransferIn, String.valueOf(minTransferIn));

        WebElement autoCompleteRadiusField = driverTools.waitForElementToAppear(AUTOCOMPLETE_RADIUS).get();
        driverTools.clickWithJavascriptExecutor(autoCompleteRadiusField);
        driverTools.sendKeys(autoCompleteRadiusField, String.valueOf(autoCompleteRadius));

        driverTools.clickWithJavascriptExecutor(AIRPORT_EN_DESCRIPTION_SWITCH);
        WebElement descriptionFieldEN = driverTools.waitForElementToAppear(AIRPORT_DESCRIPTION_FIELD).get();
        driverTools.clickWithJavascriptExecutor(descriptionFieldEN);
        driverTools.sendKeys(descriptionFieldEN, description);

        driverTools.sendKeys(ICON_UPLOAD, getLocalImagePath());
        driverTools.waitForElementToAppear(IMAGE_PREVIEW);
        driverTools.clickWithJavascriptExecutor(SIMPAN_BUTTON);
    }

    public void searchAirportByName(String name){
        driverTools.waitForElementToAppear(AIRPORT_SEARCH_INPUT_FIELD);

        driverTools.clearWithJsExecutor(AIRPORT_SEARCH_INPUT_FIELD);

        driverTools.sendKeys(AIRPORT_SEARCH_INPUT_FIELD, name);
        driverTools.clickWithJavascriptExecutor(SEARCH_BUTTON);
    }

    public void editAirport(String longitude, String latitude, String description, int minTransferOut, int minTransferIn, int autoCompleteRadius){
        driverTools.waitForElementToAppear(EDIT_BUTTON_ID);
        driverTools.clickWithJavascriptExecutor(EDIT_BUTTON_ID);

        driverTools.waitForElementToAppear(COUNTRY_NAME_AND_CODE_EDIT);
        driverTools.clickWithJavascriptExecutor(COUNTRY_NAME_AND_CODE_EDIT);
        driverTools.click(FIRST_OPTION_DROPDOWN);

        WebElement deleteImageButton = driverTools.waitForElementToBeClickable(DELETE_IMAGE_BUTTON).get();
        driverTools.clickWithJavascriptExecutor(deleteImageButton);

        WebElement longitudeField = driverTools.waitForElementToAppear(LONGITUDE).get();
        driverTools.clickWithJavascriptExecutor(longitudeField);
        driverTools.clearInput(longitudeField);
        driverTools.sendKeys(LONGITUDE, longitude);

        WebElement latitudeField = driverTools.waitForElementToAppear(LATITUDE).get();
        driverTools.clickWithJavascriptExecutor(latitudeField);
        driverTools.clearInput(latitudeField);
        driverTools.sendKeys(LATITUDE, latitude);

        WebElement descriptionField = driverTools.waitForElementToAppear(AIRPORT_DESCRIPTION_FIELD).get();
        driverTools.clickWithJavascriptExecutor(descriptionField);
        driverTools.sendKeys(descriptionField, description);

        WebElement minBookingTimeTransferOut = driverTools.waitForElementToAppear(MIN_BOOKING_TIME_TRANSFER_OUT).get();
        driverTools.clickWithJavascriptExecutor(minBookingTimeTransferOut);
        driverTools.clearInput(minBookingTimeTransferOut);
        driverTools.sendKeys(minBookingTimeTransferOut, String.valueOf(minTransferOut));

        WebElement maxBookingTimeTransferIn = driverTools.waitForElementToAppear(MAX_BOOKING_TIME_TRANSFER_IN).get();
        driverTools.clickWithJavascriptExecutor(maxBookingTimeTransferIn);
        driverTools.clearInput(maxBookingTimeTransferIn);
        driverTools.sendKeys(maxBookingTimeTransferIn, String.valueOf(minTransferIn));

        WebElement autoCompleteRadiusField = driverTools.waitForElementToAppear(AUTOCOMPLETE_RADIUS).get();
        driverTools.clickWithJavascriptExecutor(autoCompleteRadiusField);
        driverTools.clearInput(autoCompleteRadiusField);
        driverTools.sendKeys(autoCompleteRadiusField, String.valueOf(autoCompleteRadius));

        driverTools.clickWithJavascriptExecutor(AIRPORT_EN_DESCRIPTION_SWITCH);
        WebElement descriptionFieldEN = driverTools.waitForElementToAppear(AIRPORT_DESCRIPTION_FIELD).get();
        driverTools.clickWithJavascriptExecutor(descriptionFieldEN);
        driverTools.sendKeys(descriptionFieldEN, description);

        driverTools.sendKeys(ICON_UPLOAD, getLocalImagePath());
        driverTools.waitForElementToAppear(IMAGE_PREVIEW);
        driverTools.clickWithJavascriptExecutor(SIMPAN_BUTTON);
    }

    public void editBooth(String boothName){
        if (driverTools.isElementDisplayed(MODAL)) {
            driverTools.waitForElementToDisappear(MODAL);
        }

        driverTools.clickWithJavascriptExecutor(EDIT_BOOTH);
        WebElement nameField = driverTools.waitForElementToAppear(BOOTH_NAME_FIELD).get();
        driverTools.sendKeys(nameField, boothName);
        driverTools.clickWithJavascriptExecutor(ACTIVE_SWITCH);
        driverTools.waitForElementToAppear(YA_BUTTON);
        driverTools.clickWithJavascriptExecutor(YA_BUTTON);

        driverTools.clickWithJavascriptExecutor(SIMPAN_BUTTON);
    }

    public void toggleActiveAirport(){
        if(driverTools.isElementDisplayed(MODAL)){
            driverTools.waitForElementToDisappear(MODAL,10);
        }

        WebElement activateSwitch = driverTools.waitForElementToAppear(ACTIVE_SWITCH).get();
        driverTools.clickWithJavascriptExecutor(activateSwitch);
        WebElement acceptButton = driverTools.waitForElementToAppear(ACCEPT_BUTTON).get();
        driverTools.clickWithJavascriptExecutor(acceptButton);
    }

    public void deleteAirport(){
        WebElement deleteButton = driverTools.waitForElementToAppear(DELETE_BUTTON_ID).get();
        driverTools.clickWithJavascriptExecutor(deleteButton);

        WebElement acceptButton = driverTools.waitForElementToAppear(ACCEPT_BUTTON).get();
        driverTools.clickWithJavascriptExecutor(acceptButton);
    }

    public String getLocalImagePath(){
        File image = new File ("src/test/resources/nft/image.png");
        return image.getAbsolutePath();
    }

    public void goToNFTDashboard(String username, String password) throws InterruptedException {
        driverTools.maximizeWindow();
        new MemberLoginPage<>(session).adminLoginViaCookie(username, password);

        if (Configs.ENVIRONMENT == Environment.GK) {
            driverTools.get("https://gatotkaca.tiket.com/admin/nft");
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driverTools.get("https://preprod.tiket.com/admin/nft");
        }
    }

    public void navigateToAirportAndStationMenu() {
        driverTools.waitForElementToBeClickable(DASHBOARD_AIRPORT_TRANSFER_CARD);
        driverTools.clickWithJavascriptExecutor(DASHBOARD_AIRPORT_TRANSFER_CARD);
        driverTools.switchToNewTab();

        driverTools.waitForElementToBeClickable(SIDEBAR_AIRPORT_STATION_MENU);
        driverTools.clickWithJavascriptExecutor(SIDEBAR_AIRPORT_STATION_MENU);
    }

    public void searchAirportName(String airportName){
        driverTools.waitForElementToAppear(AIRPORT_SEARCH_INPUT);

        driverTools.clearWithJsExecutor(AIRPORT_SEARCH_INPUT);
        driverTools.dummyWait(1);

        driverTools.sendKeys(AIRPORT_SEARCH_INPUT, airportName);
        driverTools.dummyWait(1);

        driverTools.clickWithJavascriptExecutor(SEARCH_BUTTON);

        driverTools.dummyWait(3);
    }

    private void robustClearAndType(Locator locator, String value, boolean isContentEditable) {
        if (value == null || value.trim().isEmpty() || value.equalsIgnoreCase("null")) {
            return;
        }
        WebElement element = driverTools.waitForElementToAppear(locator).get();
        driverTools.click(locator);
        driverTools.dummyWait(1);

        for (int attempt = 0; attempt < 3; attempt++) {
            element.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
            String current = isContentEditable ? element.getText() : element.getAttribute("value");
            if (current == null || current.isEmpty()) {
                break;
            }
        }

        String remaining = isContentEditable ? element.getText() : element.getAttribute("value");
        if (remaining != null && !remaining.isEmpty()) {
            String clearScript = isContentEditable
                    ? "arguments[0].innerText=''; arguments[0].dispatchEvent(new Event('input', {bubbles: true}));"
                    : "arguments[0].value=''; arguments[0].dispatchEvent(new Event('input', {bubbles: true}));";
            ((JavascriptExecutor) driverTools.getDriver()).executeScript(clearScript, element);
        }

        driverTools.sendKeys(locator, value);
        driverTools.dummyWait(1);
    }

    public void editAirportDetails(String longitude, String latitude, String descriptionIdn, String descriptionEng, String minTransferOut, String minTransferIn, String autoCompleteRadius) {

        driverTools.waitForElementToAppear(EDIT_ACTION_BUTTON);
        driverTools.clickWithJavascriptExecutor(EDIT_ACTION_BUTTON);
        driverTools.dummyWait(3);

        robustClearAndType(EDIT_LATITUDE_INPUT, latitude, false);
        robustClearAndType(EDIT_LONGITUDE_INPUT, longitude, false);
        robustClearAndType(EDIT_MIN_TRANSFER_OUT_INPUT, minTransferOut, false);
        robustClearAndType(EDIT_MIN_TRANSFER_IN_INPUT, minTransferIn, false);
        robustClearAndType(EDIT_RADIUS_INPUT, autoCompleteRadius, false);

        robustClearAndType(EDIT_DESCRIPTION_CONTENT_AREA_IDN, descriptionIdn, true);

        if (descriptionEng != null && !descriptionEng.trim().isEmpty() && !descriptionEng.equalsIgnoreCase("null")) {
            driverTools.waitForElementToBeClickable(EDIT_ENG_TAB_SWITCH);
            driverTools.click(EDIT_ENG_TAB_SWITCH);
            driverTools.dummyWait(1);

            robustClearAndType(EDIT_DESCRIPTION_CONTENT_AREA_ENG, descriptionEng, true);
        }

        driverTools.waitForElementToBeClickable(EDIT_SAVE_BUTTON);
        driverTools.clickWithJavascriptExecutor(EDIT_SAVE_BUTTON);
    }

    public boolean verifyNewSuccessSnackbarDisplayed(String expectedMessage) {
        try {
            WebElement snackbar = driverTools.waitForElementToAppear(NEW_SUCCESS_SNACKBAR, 15).get();
            driverTools.dummyWait(2);
            String actualMessage = snackbar.getText();

            System.out.println("Actual New Snackbar Message: " + actualMessage);
            System.out.println("Expected Message: " + expectedMessage);

            if (actualMessage == null || actualMessage.trim().isEmpty()) {
                actualMessage = snackbar.getAttribute("innerText");
            }

            return actualMessage.contains(expectedMessage);
        } catch (Exception e) {
            System.out.println("New Snackbar did not appear within timeout.");
            return false;
        }
    }

    private boolean boothToggleCheckedBeforeEdit;

    public void editBoothDetails(String boothName) {

        driverTools.waitForElementToAppear(EDIT_BOOTH_ACTION_BUTTON);
        driverTools.clickWithJavascriptExecutor(EDIT_BOOTH_ACTION_BUTTON);
        driverTools.dummyWait(3);

        robustClearAndType(BOOTH_NAME_INPUT, boothName, false);

        WebElement toggleCheckbox = findPresentElement(BOOTH_TOGGLE_CHECKBOX);
        boothToggleCheckedBeforeEdit = toggleCheckbox.isSelected();
        driverTools.waitForElementToBeClickable(BOOTH_TOGGLE_BUTTON);
        driverTools.click(BOOTH_TOGGLE_BUTTON);
        driverTools.acceptBrowserAlert();
        driverTools.dummyWait(1);

        driverTools.waitForElementToBeClickable(BOOTH_SAVE_BUTTON);
        driverTools.clickWithJavascriptExecutor(BOOTH_SAVE_BUTTON);
    }

    public boolean verifyBoothUpdatedAfterSave(String expectedBoothName) {
        driverTools.dummyWait(2);
        driverTools.waitForElementToAppear(EDIT_BOOTH_ACTION_BUTTON);
        driverTools.clickWithJavascriptExecutor(EDIT_BOOTH_ACTION_BUTTON);
        driverTools.dummyWait(3);

        WebElement nameInput = driverTools.waitForElementToAppear(BOOTH_NAME_INPUT).get();
        String actualBoothName = nameInput.getAttribute("value");

        WebElement toggleCheckbox = findPresentElement(BOOTH_TOGGLE_CHECKBOX);
        boolean boothToggleCheckedAfterSave = toggleCheckbox.isSelected();

        System.out.println("Expected Booth Name: " + expectedBoothName + " | Actual Booth Name: " + actualBoothName);
        System.out.println("Toggle checked before edit: " + boothToggleCheckedBeforeEdit + " | Toggle checked after save: " + boothToggleCheckedAfterSave);

        boolean isNameUpdated = actualBoothName != null && actualBoothName.trim().equalsIgnoreCase(expectedBoothName.trim());
        boolean isToggleStatusPersisted = boothToggleCheckedAfterSave != boothToggleCheckedBeforeEdit;

        return isNameUpdated && isToggleStatusPersisted;
    }

    private WebElement findPresentElement(Locator locator) {
        WebDriverWait wait = new WebDriverWait(driverTools.getDriver(), Duration.ofSeconds(10));
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator.by()));
    }

    public boolean isAirportStatusActive() {
        WebDriverWait wait = new WebDriverWait(driverTools.getDriver(), Duration.ofSeconds(10));
        WebElement checkbox = wait.until(ExpectedConditions.presenceOfElementLocated(AIRPORT_STATUS_TOGGLE_CHECKBOX.by()));
        return checkbox.isSelected();
    }

    public void toggleAirportStatus() {
        driverTools.waitForElementToAppear(AIRPORT_STATUS_TOGGLE);
        driverTools.waitForElementToBeClickable(AIRPORT_STATUS_TOGGLE);
        driverTools.clickWithJavascriptExecutor(AIRPORT_STATUS_TOGGLE);

        driverTools.dummyWait(1);

        driverTools.waitForElementToAppear(TOGGLE_CONFIRM_YES_BUTTON);
        driverTools.waitForElementToBeClickable(TOGGLE_CONFIRM_YES_BUTTON);
        driverTools.clickWithJavascriptExecutor(TOGGLE_CONFIRM_YES_BUTTON);

        driverTools.dummyWait(2);
    }

    public void verifyUpdatedAirportDetails(String expectedLongitude, String expectedLatitude, String expectedDescriptionIdn, String expectedDescriptionEng, String expectedMinTransferOut, String expectedMinTransferIn, String expectedRadius) {
        driverTools.waitForElementToAppear(EDIT_ACTION_BUTTON);
        driverTools.clickWithJavascriptExecutor(EDIT_ACTION_BUTTON);
        driverTools.dummyWait(3);

        if (expectedLatitude != null && !expectedLatitude.equalsIgnoreCase("null")) {
            String actualLat = driverTools.waitForElementToAppear(EDIT_LATITUDE_INPUT).get().getAttribute("value");
            Assertion.assertThat("Verify Latitude", actualLat, containsString(expectedLatitude));
        }

        if (expectedLongitude != null && !expectedLongitude.equalsIgnoreCase("null")) {
            String actualLong = driverTools.waitForElementToAppear(EDIT_LONGITUDE_INPUT).get().getAttribute("value");
            Assertion.assertThat("Verify Longitude", actualLong, containsString(expectedLongitude));
        }

        if (expectedMinTransferOut != null && !expectedMinTransferOut.equalsIgnoreCase("null")) {
            String actualMinOut = driverTools.waitForElementToAppear(EDIT_MIN_TRANSFER_OUT_INPUT).get().getAttribute("value");
            Assertion.assertThat("Verify Min Transfer Out", actualMinOut, containsString(expectedMinTransferOut));
        }

        if (expectedMinTransferIn != null && !expectedMinTransferIn.equalsIgnoreCase("null")) {
            String actualMinIn = driverTools.waitForElementToAppear(EDIT_MIN_TRANSFER_IN_INPUT).get().getAttribute("value");
            Assertion.assertThat("Verify Min Transfer In", actualMinIn, containsString(expectedMinTransferIn));
        }

        if (expectedRadius != null && !expectedRadius.equalsIgnoreCase("null")) {
            String actualRadius = driverTools.waitForElementToAppear(EDIT_RADIUS_INPUT).get().getAttribute("value");
            Assertion.assertThat("Verify Radius", actualRadius, containsString(expectedRadius));
        }

        if (expectedDescriptionIdn != null && !expectedDescriptionIdn.equalsIgnoreCase("null")) {
            String actualIdn = driverTools.waitForElementToAppear(EDIT_DESCRIPTION_CONTENT_AREA_IDN).get().getText();
            Assertion.assertThat("Verify IDN Description", actualIdn.trim(), equalTo(expectedDescriptionIdn.trim()));
        }

        if (expectedDescriptionEng != null && !expectedDescriptionEng.trim().isEmpty() && !expectedDescriptionEng.equalsIgnoreCase("null")) {
            driverTools.waitForElementToBeClickable(EDIT_ENG_TAB_SWITCH);
            driverTools.click(EDIT_ENG_TAB_SWITCH);
            driverTools.dummyWait(1);
            String actualEng = driverTools.waitForElementToAppear(EDIT_DESCRIPTION_CONTENT_AREA_ENG).get().getText();
            Assertion.assertThat("Verify ENG Description", actualEng.trim(), equalTo(expectedDescriptionEng.trim()));
        }

        driverTools.waitForElementToBeClickable(EDIT_SAVE_BUTTON);
        driverTools.clickWithJavascriptExecutor(EDIT_SAVE_BUTTON);
    }
}