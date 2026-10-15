package com.tiket.page.member.smart_profile;

import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class SmartProfilePageIOS<T extends WebDriver> extends SmartProfilePage<T> {
    public SmartProfilePageIOS(Session<T> session) {
        super(session);
    }
    private static final Locator ADD_PASSENGER_DATA = new Locator("ADD_PASSENGER_DATA", AppiumBy.name("add_new_passenger_btn"));
    private static final Locator SALUTATION = new Locator("SALUTATION", By.id("mr_button"));
    private static final Locator FIRST_NAME = new Locator("FIRST_NAME", By.id("accountFirstName"));
    private static final Locator LAST_NAME = new Locator("LAST_NAME", By.id("accountLastName"));
    private static final Locator DATE_OF_BIRTH = new Locator("DATE_OF_BIRTH", By.id("accountBirthdate"));
    private static final Locator DATE_OF_BIRTH_DONE = new Locator("DATE_OF_BIRTH_DONE", By.id("save_passenger_birthName_btn"));
    private static final Locator PASSENGER_EMAIL = new Locator("PASSENGER_EMAIL", By.id("accountEmail"));
    private static final Locator PASSENGER_PHONE = new Locator("PASSENGER_PHONE", By.id("accountPhone"));
    private static final Locator PASSENGER_NATIONALITY_CODE = new Locator("PASSENGER_NATIONALITY_CODE", AppiumBy.name("accountNationalityCode"));
    private static final Locator PASSENGER_NATIONALITY = new Locator("PASSENGER_NATIONALITY", By.xpath("//XCUIElementTypeStaticText[@name='Indonesia']"));
    private static final Locator PASSENGER_IDENTITY_NUMBER = new Locator("PASSENGER_IDENTITY_NUMBER", By.id("identityNumber"));
    private static final Locator PASSENGER_DATA_SAVE = new Locator("PASSENGER_DATA_SAVE", By.id("save_btn"));
    private static final Locator SKIP_PASSENGER_DOCUMENTS = new Locator("SKIP_PASSENGER_DOCUMENTS", By.id("skip_document_btn"));
    private static final Locator SUCCESS_PASSENGER_ADD_POPUP = new Locator("SUCCESS_PASSENGER_ADD_POPUP",AppiumBy.xpath("//*[@label='A new profile successfully added.' or @label='Profil baru berhasil ditambahkan.']"));
    private static final Locator SUCCESS_PASSENGER_DELETE_POPUP = new Locator("SUCCESS_PASSENGER_DELETE_POPUP",AppiumBy.xpath("//*[@label='The profile successfully removed.' or @label='Profil berhasil dihapus.']"));
    private static final Locator DELETE_SMART_PROFILE = new Locator("DELETE_SMART_PROFILE",AppiumBy.accessibilityId("delete_passenger_button"));
    private static final Locator CONFIRM_DELETE_SMART_PROFILE = new Locator("DELETE_SMART_PROFILE",AppiumBy.id("confirm_delete_button"));
    private static final Locator SUCCESS_PASSENGER_EDIT_POPUP = new Locator("SUCCESS_PASSENGER_EDIT_POPUP",AppiumBy.xpath("//*[@label='Yay! You've updated your profile.' or @label='Hore! Profil kamu berhasil diperbarui.']"));
    private static final Locator SNACKBAR_POPUP = new Locator("SNACKBAR_POPUP",AppiumBy.name("smart_profile_snackbar"));
    private static final Locator SEARCH_FIELD = new Locator("SEARCH_FIELD",AppiumBy.id("search_field"));
    private static final Locator OWNER_NAME = new Locator("OWNER_NAME",AppiumBy.xpath("//*[@name='Account Owner' or @name='Pemilik Akun']/following-sibling::XCUIElementTypeStaticText"));
    private static final Locator PASSPORT = new Locator("PASSPORT",AppiumBy.xpath("//*[@name='Passport' or @name='Paspor']"));
    private static final Locator PASSPORT_NUMBER = new Locator("PASSPORT_NUMBER",AppiumBy.name("passportNumber"));
    private static final Locator PASSPORT_ISSUE_DATE = new Locator("PASSPORT_ISSUE_DATE",By.name("passportIssuedDate"));
    private static final Locator PASSPORT_EXPIRY_DATE = new Locator("PASSPORT_EXPIRY_DATE",By.name("passportExpiryDate"));
    private static final Locator PASSPORT_COUNTRY = new Locator("PASSPORT_COUNTRY",By.name("passportIssuingCountry"));
    private static final Locator PASSPORT_CONTINUE = new Locator("PASSPORT_CONTINUE",By.xpath("//*[(@name='Continue' or @name='Lanjut') and @visible='true']"));
    private static final Locator PASSPORT_SAVE = new Locator("PASSPORT_SAVE",By.xpath("//*[(@name='Save' or @name='Simpan') and @visible='true']"));
    private static final Locator PASSPORT_DAY_TABLE = new Locator("PASSPORT_DAY_TABLE",By.xpath("(//*[@type='XCUIElementTypeTable'])[1]"));
    private static final Locator DONE = new Locator("DONE",By.xpath("//XCUIElementTypeButton[(@name='Selesai' or @name='Done') and @visible='true']"));
    private static final Locator COUNTRIES = new Locator("COUNTRIES",AppiumBy.xpath("//*[@type='XCUIElementTypeTable']//XCUIElementTypeStaticText[@visible='true']"));

    @Override
    public String addPassengerData() {
        driverTools.click(ADD_PASSENGER_DATA);
        driverTools.clickElementByCoordinate(SALUTATION);
        driverTools.click(FIRST_NAME);
        String firstName = CoreFunctions.randomName();
        driverTools.sendKeys(FIRST_NAME, firstName);
        driverTools.hideKeyboard(1);
        driverTools.click(LAST_NAME);
        driverTools.sendKeys(LAST_NAME, firstName);
        driverTools.hideKeyboard(1);
        driverTools.clickElementByCoordinate(DATE_OF_BIRTH);
        driverTools.click(DATE_OF_BIRTH_DONE);
        driverTools.click(PASSENGER_EMAIL);
        driverTools.sendKeys(PASSENGER_EMAIL, CoreFunctions.randomEmail());
        driverTools.hideKeyboard(2);
        driverTools.click(PASSENGER_PHONE);
        driverTools.sendKeys(PASSENGER_PHONE, "08"+ CoreFunctions.randomInt(10));
        driverTools.hideKeyboard(2);
        driverTools.clickElementByCoordinate(PASSENGER_NATIONALITY_CODE);
        driverTools.click(PASSENGER_NATIONALITY);
        driverTools.click(PASSENGER_IDENTITY_NUMBER);
        driverTools.sendKeys(PASSENGER_IDENTITY_NUMBER, "31730422099" + CoreFunctions.randomInt(5) + Keys.ENTER);
        driverTools.hideKeyboard(2);
        driverTools.swipeToElement(PASSENGER_DATA_SAVE,Swipe.UP,5);
        driverTools.click(PASSENGER_DATA_SAVE);
        return firstName+" "+firstName;
    }

    @Override
    public void editSmartProfile(String newName) {
        logger.log("Changing first name to: " + newName);
        driverTools.clear(FIRST_NAME);
        driverTools.sendKeys(FIRST_NAME, newName);
        driverTools.hideKeyboard(2);
        driverTools.swipeToElement(PASSENGER_DATA_SAVE, Swipe.UP, 5);
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
        driverTools.hideKeyboard(1);
    }

    @Override
    public void openSmartProfile(int profileIndex) {
        logger.log("Opening smart profile index: "+profileIndex);
        WebElement profile = driverTools.waitForElementToAppear(getSmartProfileNameLocator(profileIndex)).orElseThrow();
        driverTools.click(profile);
        driverTools.hideKeyboard(1);
    }

    @Override
    public void deleteSmartProfile() {
        driverTools.hideKeyboard();
        driverTools.swipeToElement(DELETE_SMART_PROFILE, Swipe.UP, 5);
        driverTools.click(DELETE_SMART_PROFILE);
        driverTools.click(CONFIRM_DELETE_SMART_PROFILE);
    }

    @Override
    public boolean findProfile(String name) {
        logger.log("Finding smart profile: " + name);
        WebElement searchField = driverTools.waitForElementToAppear(SEARCH_FIELD).orElseThrow();
        driverTools.sendKeys(searchField,name);
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
        driverTools.swipeToElement(PASSPORT, Swipe.UP, 10);
        WebElement passport = driverTools.waitForElementToAppear(PASSPORT).orElseThrow();
        driverTools.click(passport);
        driverTools.waitForElementToAppear(PASSPORT_NUMBER).orElseThrow();
        String passportNumber = driverTools.getText(PASSPORT_NUMBER).orElseThrow();
        String passportIssueDate = driverTools.getText(PASSPORT_ISSUE_DATE).orElseThrow();
        String passportExpiryDate = driverTools.getText(PASSPORT_EXPIRY_DATE).orElseThrow();
        String passportCountry = driverTools.getText(PASSPORT_COUNTRY).orElseThrow();
        PassportInfo passportInfo = new PassportInfo(passportNumber, passportIssueDate, passportExpiryDate, passportCountry);
        logger.log("Passport Info: " + passportInfo);
        driverTools.swipeToElement(PASSPORT_CONTINUE, Swipe.UP, 10);
        driverTools.click(PASSPORT_CONTINUE);
        driverTools.swipeToElement(PASSPORT_SAVE, Swipe.UP, 10);
        driverTools.click(PASSPORT_SAVE);
        return passportInfo;
    }

    @Override
    public PassportInfo changeProfilePassportInfo(int profileIndex) {
        PassportInfo newPassportInfo = new PassportInfo(String.valueOf(CoreFunctions.randomInt(16)), "", "", "");
        openSmartProfile(profileIndex);
        driverTools.swipeToElement(PASSPORT, Swipe.UP, 10);
        WebElement passport = driverTools.waitForElementToAppear(PASSPORT).orElseThrow();
        driverTools.click(passport);
        driverTools.waitForElementToAppear(PASSPORT_NUMBER).orElseThrow();
        driverTools.click(PASSPORT_NUMBER);
        String newPassportNumber = newPassportInfo.passportNumber();
        driverTools.clear(PASSPORT_NUMBER);
        driverTools.sendKeys(PASSPORT_NUMBER, newPassportNumber);
        driverTools.hideKeyboard(1);
        WebElement passportIssuedDateElement = driverTools.waitForElementToPresence(PASSPORT_ISSUE_DATE).orElseThrow();
        driverTools.click(passportIssuedDateElement);
        driverTools.scrollPickerWheel(PASSPORT_DAY_TABLE, 1, "next");
        driverTools.click(DONE);
        WebElement passportExpiryDateElement = driverTools.waitForElementToPresence(PASSPORT_EXPIRY_DATE).orElseThrow();
        driverTools.click(passportExpiryDateElement);
        driverTools.scrollPickerWheel(PASSPORT_DAY_TABLE, 1, "next");
        driverTools.click(DONE);
        WebElement countryElement = driverTools.waitForElementToPresence(PASSPORT_COUNTRY).orElseThrow();
        driverTools.click(countryElement);
        selectRandomCountry();
        String passportNumber = driverTools.getText(PASSPORT_NUMBER).orElseThrow();
        String passportIssueDate = driverTools.getText(PASSPORT_ISSUE_DATE).orElseThrow();
        String passportExpiryDate = driverTools.getText(PASSPORT_EXPIRY_DATE).orElseThrow();
        String passportCountry = driverTools.getText(PASSPORT_COUNTRY).orElseThrow();
        newPassportInfo = new PassportInfo(passportNumber, passportIssueDate, passportExpiryDate, passportCountry);
        driverTools.swipeToElement(PASSPORT_CONTINUE, Swipe.UP, 10);
        driverTools.clickElementByCoordinate(PASSPORT_CONTINUE);
        driverTools.swipeToElement(PASSPORT_SAVE, Swipe.UP, 10);
        driverTools.click(PASSPORT_SAVE);
        return newPassportInfo;
    }

    @Override
    public void selectRandomCountry() {
        driverTools.waitForElementsToAppear(COUNTRIES).ifPresent(countries -> {
            int randomIndex = CoreFunctions.generateRandomInt(0, countries.size() - 1);
            WebElement randomCountry = countries.get(randomIndex);
            driverTools.click(randomCountry);
        });
    }

    private Locator getSmartProfileNameLocator(String name){
        return new Locator("Name",AppiumBy.xpath("//XCUIElementTypeCell/XCUIElementTypeStaticText[contains(@name,'"+name+"')]"));
    }

    private Locator getSmartProfileNameLocator(int profileIndex){
        return new Locator("Name",AppiumBy.xpath("(//*[contains(@name,'Duplicate Profile')])["+profileIndex+"]"));
    }
}