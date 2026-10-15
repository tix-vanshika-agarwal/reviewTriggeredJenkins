package com.tiket.page.member.smart_profile;
import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class SmartProfilePageAndroid<T extends WebDriver> extends SmartProfilePage<T> {

    private static final Locator ADD_PASSENGER_DATA = new Locator("ADD_PASSENGER_DATA", AppiumBy.xpath("//*[contains(@resource-id,'tds_button_view') and (contains(@text,'Data'))]"));
    private static final Locator SALUTATION = new Locator("SALUTATION", By.id("radio_button"));
    private static final Locator FIRST_NAME = new Locator("FIRST_NAME", By.xpath("//*[contains(@resource-id,'tf_first_middle_name')]//android.widget.EditText"));
    private static final Locator LAST_NAME = new Locator("LAST_NAME", By.xpath("//*[contains(@resource-id,'tf_last_name')]//android.widget.EditText"));
    private static final Locator DATE_OF_BIRTH = new Locator("DATE_OF_BIRTH", By.xpath("//*[contains(@resource-id,'tf_date_of_birth')]//android.widget.EditText"));
    private static final Locator DATE_OF_BIRTH_DONE = new Locator("DATE_OF_BIRTH_DONE", By.id("tds_btn_submit_picker"));
    private static final Locator PASSENGER_EMAIL = new Locator("PASSENGER_EMAIL", By.xpath("//*[contains(@resource-id,'tf_email')]//android.widget.EditText"));
    private static final Locator PASSENGER_PHONE = new Locator("PASSENGER_PHONE", By.xpath("//*[contains(@resource-id,'tf_phone_number')]//android.widget.EditText"));
    private static final Locator PASSENGER_NATIONALITY_CODE = new Locator("PASSENGER_NATIONALITY_CODE", By.xpath("//*[contains(@resource-id,'tf_nationality')]//android.widget.EditText"));
    private static final Locator PASSENGER_NATIONALITY = new Locator("PASSENGER_NATIONALITY", By.id("tds_country_code_title_text"));
    private static final Locator PASSENGER_IDENTITY_NUMBER = new Locator("PASSENGER_IDENTITY_NUMBER", By.xpath("//*[contains(@resource-id,'tf_identity_number')]//android.widget.EditText"));
    private static final Locator PASSENGER_DATA_SAVE = new Locator("PASSENGER_DATA_SAVE", By.id("btn_save"));
    private static final Locator SKIP_PASSENGER_DOCUMENTS = new Locator("SKIP_PASSENGER_DOCUMENTS", By.id("tds_button_view"));
    private static final Locator SUCCESS_PASSENGER_ADD_POPUP = new Locator("SUCCESS_PASSENGER_ADD_POPUP",AppiumBy.xpath("//*[contains(@resource-id,'tds_snackbar_text') and (contains(@text,'Profil baru berhasil ditambahkan')) or (contains(@text,'A new profile successfully added'))]"));
    private static final Locator DELETE_SMART_PROFILE = new Locator("DELETE_SMART_PROFILE",AppiumBy.id("btn_delete"));
    private static final Locator CONFIRM_DELETE_SMART_PROFILE = new Locator("CONFIRM_DELETE_SMART_PROFILE",AppiumBy.id("tds_btn_primary_info_view"));
    private static final Locator SUCCESS_PASSENGER_DELETE_POPUP = new Locator("SUCCESS_PASSENGER_DELETE_POPUP",AppiumBy.xpath("//*[contains(@resource-id,'tds_snackbar_text') and (contains(@text,'The profile successfully removed.')) or (contains(@text,'Profil berhasil dihapus.'))]"));
    private static final Locator SUCCESS_PASSENGER_EDIT_POPUP = new Locator("SUCCESS_PASSENGER_EDIT_POPUP",AppiumBy.xpath("//*[contains(@resource-id,'tds_snackbar_text') and (contains(@text,'The profile successfully updated.')) or (contains(@text,'Profil berhasil diperbarui.'))]"));
    private static final Locator SEARCH_FIELD = new Locator("SEARCH_FIELD",AppiumBy.id("tds_search_et"));
    private static final Locator SNACKBAR_POPUP = new Locator("SEARCH_FIELD",AppiumBy.id("tds_snackbar_text"));
    private static final Locator OWNER_NAME = new Locator("OWNER_NAME",AppiumBy.xpath("//*[contains(@resource-id,'tv_owner_text')]/preceding-sibling::*[contains(@resource-id,'tv_full_name')]"));
    private static final Locator PASSPORT = new Locator("PASSPORT",AppiumBy.xpath("//*[contains(@resource-id,'tv_docs_title') and (@text='Passport' or @text='Paspor')]"));
    private static final Locator PASSPORT_NUMBER = new Locator("PASSPORT_NUMBER",AppiumBy.xpath("(//*[contains(@resource-id,'tds_box_input_text')])[1]"));
    private static final Locator PASSPORT_ISSUE_DATE = new Locator("PASSPORT_ISSUE_DATE",By.xpath("(//*[contains(@resource-id,'tds_box_input_text')])[2]"));
    private static final Locator PASSPORT_EXPIRY_DATE = new Locator("PASSPORT_EXPIRY_DATE",By.xpath("(//*[contains(@resource-id,'tds_box_input_text')])[3]"));
    private static final Locator PASSPORT_COUNTRY = new Locator("PASSPORT_COUNTRY",By.xpath("(//*[contains(@resource-id,'tds_box_input_text')])[4]"));
    private static final Locator PASSPORT_CONTINUE = new Locator("PASSPORT_CONTINUE",By.xpath("//*[contains(@resource-id,'tds_button_view')]"));
    private static final Locator PASSPORT_SAVE = new Locator("PASSPORT_SAVE",By.xpath("//*[contains(@resource-id,'tds_button_view') and (@text='Save' or @text='Simpan')]"));
    private static final Locator PASSPORT_DAY_TABLE = new Locator("PASSPORT_DAY_TABLE",By.id("tds_rv_left_picker"));
    private static final Locator DONE = new Locator("DONE",By.xpath("//android.widget.Button[contains(@resource-id,'tds_btn') and @displayed='true']"));
    private static final Locator COUNTRIES = new Locator("COUNTRIES",AppiumBy.id("tds_country_code_title_text"));

    public SmartProfilePageAndroid(Session<T> session) {
        super(session);
    }

    @Override
    public String addPassengerData() {
        driverTools.click(ADD_PASSENGER_DATA);
        driverTools.click(SALUTATION);
        driverTools.click(FIRST_NAME);
        String firstName = CoreFunctions.randomName();
        driverTools.sendKeys(FIRST_NAME, firstName);
        driverTools.click(LAST_NAME);
        driverTools.sendKeys(LAST_NAME, firstName);
        driverTools.click(DATE_OF_BIRTH);
        driverTools.click(DATE_OF_BIRTH_DONE);
        driverTools.swipeUp();
        driverTools.click(PASSENGER_EMAIL);
        driverTools.sendKeys(PASSENGER_EMAIL, CoreFunctions.randomEmail());
        driverTools.pressKey(PASSENGER_PHONE, "08"+ CoreFunctions.randomInt(10));
        driverTools.click(PASSENGER_NATIONALITY_CODE);
        driverTools.click(PASSENGER_NATIONALITY);
        driverTools.click(PASSENGER_IDENTITY_NUMBER);
        driverTools.sendKeys(PASSENGER_IDENTITY_NUMBER, "31730422099" + CoreFunctions.randomInt(5));
        driverTools.hideKeyboard(1);
        driverTools.swipeUp();
        driverTools.click(PASSENGER_DATA_SAVE);
        driverTools.click(SKIP_PASSENGER_DOCUMENTS);
        return firstName+" "+firstName;
    }

    @Override
    public void editSmartProfile(String newName) {
        driverTools.swipeToElement(FIRST_NAME,Swipe.DOWN,5);
        driverTools.clear(FIRST_NAME);
        driverTools.sendKeys(FIRST_NAME, newName);
        driverTools.swipeToElement(PASSENGER_DATA_SAVE, Swipe.UP, 3);
        driverTools.click(PASSENGER_DATA_SAVE);
    }

    @Override
    public String getSnackbarText() {
        WebElement snackbar = driverTools.waitForElementToAppear(SNACKBAR_POPUP).orElseThrow();
        String snackbarText = driverTools.getText(snackbar);
        logger.log("Snackbar text: " + snackbarText);
        return snackbarText;
    }

    @Override
    public Boolean isSnackbarShown(ActionType action) {
        boolean isSnackbarDisplayed = false;
        switch (action) {
            case ADD -> {
                isSnackbarDisplayed = driverTools.isElementDisplayed(SUCCESS_PASSENGER_ADD_POPUP);
                break;
            }
            case DELETE -> {
                isSnackbarDisplayed = driverTools.isElementDisplayed(SUCCESS_PASSENGER_DELETE_POPUP);
                break;
            }
            case EDIT -> {
                isSnackbarDisplayed = driverTools.isElementDisplayed(SUCCESS_PASSENGER_EDIT_POPUP);
                break;
            }
            default -> {
                throw new NotImplementedException("Not implemented");
            }
        }
        return isSnackbarDisplayed;
    }

    @Override
    public void openSmartProfile(String name) {
        logger.log("Opening smart profile: "+name);
        WebElement profile = driverTools.waitForElementToAppear(getSmartProfileNameLocator(name)).orElseThrow();
        driverTools.click(profile);
    }

    @Override
    public void openSmartProfile(int profileIndex) {
        logger.log("Opening smart profile index: "+profileIndex);
        WebElement profile = driverTools.waitForElementToAppear(getSmartProfileNameLocator(profileIndex)).orElseThrow();
        driverTools.click(profile);
    }

    @Override
    public void deleteSmartProfile() {
        driverTools.swipeToElement(DELETE_SMART_PROFILE, Swipe.UP, 5);
        driverTools.click(DELETE_SMART_PROFILE);
        driverTools.click(CONFIRM_DELETE_SMART_PROFILE);
    }

    @Override
    public boolean findProfile(String name) {
        logger.log("Finding smart profile: " + name);
        WebElement searchField = driverTools.waitForElementToAppear(SEARCH_FIELD).orElseThrow();
        driverTools.click(searchField);
        driverTools.sendKeys(SEARCH_FIELD,name);
        driverTools.hideKeyboard(1);
        boolean isProfileExist = driverTools.waitForElementToAppear(getSmartProfileNameLocator(name)).isPresent();
        logger.log("Smart profile found: " + isProfileExist);
        return isProfileExist;
    }

    @Override
    public String getOwnerName() {
        WebElement ownerName = driverTools.waitForElementToAppear(OWNER_NAME).orElseThrow();
        return driverTools.getText(ownerName);
    }

    @Override
    public String getProfileName(int profileIndex) {
        WebElement profile = driverTools.waitForElementToAppear(getSmartProfileNameLocator(profileIndex)).orElseThrow();
        return driverTools.getText(profile);
    }

    @Override
    public PassportInfo getProfilePassportInfo(int profileIndex) {
        openSmartProfile(profileIndex);
        WebElement passport = driverTools.waitForElementToAppear(PASSPORT).orElseThrow();
        driverTools.click(passport);
        driverTools.waitForElementToAppear(PASSPORT_NUMBER).orElseThrow();
        String passportNumber = driverTools.getText(PASSPORT_NUMBER).orElseThrow();
        String passportIssueDate = driverTools.getText(PASSPORT_ISSUE_DATE).orElseThrow();
        String passportExpiryDate = driverTools.getText(PASSPORT_EXPIRY_DATE).orElseThrow();
        String passportCountry = driverTools.getText(PASSPORT_COUNTRY).orElseThrow();
        PassportInfo passportInfo = new PassportInfo(passportNumber, passportIssueDate, passportExpiryDate, passportCountry);
        logger.log("Passport Info: " + passportInfo);
        driverTools.click(PASSPORT_CONTINUE);
        driverTools.click(PASSPORT_SAVE);
        return passportInfo;
    }

    @Override
    public PassportInfo changeProfilePassportInfo(int profileIndex) {
        PassportInfo newPassportInfo = new PassportInfo(String.valueOf(CoreFunctions.randomInt(16)), "01/01/2020", "01/01/2030", "Indonesia");
        openSmartProfile(profileIndex);
        WebElement passport = driverTools.waitForElementToAppear(PASSPORT).orElseThrow();
        driverTools.click(passport);
        driverTools.waitForElementToAppear(PASSPORT_NUMBER).orElseThrow();
        driverTools.click(PASSPORT_NUMBER);
        String newPassportNumber = newPassportInfo.passportNumber();
        driverTools.clear(PASSPORT_NUMBER);
        driverTools.sendKeys(PASSPORT_NUMBER, newPassportNumber);
        driverTools.click(PASSPORT_ISSUE_DATE);
        driverTools.scrollPickerWheel(PASSPORT_DAY_TABLE, 1, "next");
        driverTools.click(DONE);
        driverTools.click(PASSPORT_EXPIRY_DATE);
        driverTools.scrollPickerWheel(PASSPORT_DAY_TABLE, 1, "next");
        driverTools.click(DONE);
        driverTools.click(PASSPORT_COUNTRY);
        selectRandomCountry();
        String passportNumber = driverTools.getText(PASSPORT_NUMBER).orElseThrow();
        String passportIssueDate = driverTools.getText(PASSPORT_ISSUE_DATE).orElseThrow();
        String passportExpiryDate = driverTools.getText(PASSPORT_EXPIRY_DATE).orElseThrow();
        String passportCountry = driverTools.getText(PASSPORT_COUNTRY).orElseThrow();
        newPassportInfo = new PassportInfo(passportNumber, passportIssueDate, passportExpiryDate, passportCountry);
        driverTools.click(PASSPORT_CONTINUE);
        driverTools.click(PASSPORT_SAVE);
        return newPassportInfo;
    }

    @Override
    public void selectRandomCountry() {
        driverTools.waitForElementsToAppear(COUNTRIES).orElseThrow();
        driverTools.elements(COUNTRIES).ifPresent(countries -> {
            int randomIndex = CoreFunctions.generateRandomInt(0, countries.size() - 1);
            WebElement randomCountry = countries.get(randomIndex);
            driverTools.click(randomCountry);
        });
    }

    private Locator getSmartProfileNameLocator(String name){
        return new Locator("Name",AppiumBy.xpath("//*[contains(@resource-id,'tv_full_name') and contains(@text,'"+name+"')]"));
    }

    private Locator getSmartProfileNameLocator(int profileIndex){
        return new Locator("Name",AppiumBy.xpath("(//*[contains(@resource-id,'tv_full_name')])["+profileIndex+"]"));
    }
}