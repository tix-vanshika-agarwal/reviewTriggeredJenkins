package com.tiket.web.page.platform.eiffel.membership;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;
import com.tiket.pojo.member.MainProfileData;
import com.tiket.web.page.common.home.HomePageWeb;
import org.openqa.selenium.WebDriver;
import com.tiket.model.Locator;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;

import java.util.List;


public class AccountPageDWeb <T extends WebDriver> extends AccountPageWeb<T> {

    public AccountPageDWeb(Session<T> session) {
        super(session);
    }

    HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
    /** Passport date picks: the first one plus a single retry when the field does not reflect the picked day. */
    private static final int PASSPORT_DATE_PICK_ATTEMPTS = 2;
    private static final Locator ADD_PASSENGER_DATA = new Locator("ADD_PASSENGER_DATA", new By.ByXPath("//span[text() = '+ Add Profile' or text()='+ Tambah Profil']"));
    private static final Locator SAVE_PASSENGERS_DATA = new Locator("SAVE_PASSENGERS_DATA", new By.ByXPath("//span[text() = 'Save Passengers Data' or text()='Simpan Data Penumpang']"));
    private static final Locator DONE_BUTTON =  new Locator("DONE_BUTTON" , new By.ByXPath("//button[text() = 'Done' or text()='Selesai']")) ;
    private static final Locator ADD_SMART_PROFILE = new Locator("ADD_SMART_PROFILE",
            new By.ByXPath("//span[contains(@class,'Header_profile_add')]"));

    private static final Locator ACCOUNT_OWNER_PROFILE = new Locator("ACCOUNT_OWNER_PROFILE",
            new By.ByXPath("//*[contains(text(),'Account Owner') or contains(text(),'Pemilik Akun')]"));
    private static final Locator NATIONALITY_COUNTRY_TRIGGER = new Locator("NATIONALITY_COUNTRY_TRIGGER",
            new By.ByXPath("//div[@data-testid='click-show-country-list' and .//input[@id='account-nationality-code']]"));
    private static final Locator COUNTRY_LIST_ITEM = new Locator("COUNTRY_LIST_ITEM",
            new By.ByXPath("//*[contains(@class,'CountryListSelection_list_item')]"));
    private static final Locator CONTINUE_BUTTON = new Locator("CONTINUE_BUTTON",
            new By.ByXPath("//button[normalize-space()='Continue' or normalize-space()='Lanjut' or .//span[normalize-space()='Continue' or normalize-space()='Lanjut']]"));
    private static final Locator SAVE_PROFILE_BUTTON = new Locator("SAVE_PROFILE_BUTTON",
            new By.ByXPath("//div[contains(@class,'styles_save_passenger')]//button[contains(@class,'Button_variant_primary')]"));
    private static final Locator SUCCESS_SNACKBAR = new Locator("SUCCESS_SNACKBAR",
            new By.ByXPath("//*[@role='alert' or contains(@class,'Snackbar') or contains(@class,'snackbar') or contains(@class,'Toast')][contains(.,'updated') or contains(.,'diperbarui') or contains(.,'berhasil')]"));

    private static final Locator PASSPORT_ROW = new Locator("PASSPORT_ROW",
            new By.ByXPath("(//div[@id='passport-number']//div[contains(@class,'styles_box_detail')])[2]"));
    private static final Locator PASSPORT_NUMBER = new Locator("PASSPORT_NUMBER",
            new By.ByXPath("//input[@id='passport-number' or @name='passportNumber']"));
    private static final Locator PASSPORT_ISSUE_DATE = new Locator("PASSPORT_ISSUE_DATE", By.id("date-issued"));
    private static final Locator PASSPORT_EXPIRY_DATE = new Locator("PASSPORT_EXPIRY_DATE", By.id("date-expired"));
    private static final Locator PASSPORT_ISSUING_COUNTRY = new Locator("PASSPORT_ISSUING_COUNTRY", By.id("passport-issuing-country"));
    private static final Locator PASSPORT_ISSUING_COUNTRY_TRIGGER = new Locator("PASSPORT_ISSUING_COUNTRY_TRIGGER",
            new By.ByXPath("//div[@data-testid='click-show-country-list' and .//input[@id='passport-issuing-country']]"));
    private static final Locator PASSPORT_DATE_CONFIRM = new Locator("PASSPORT_DATE_CONFIRM",
            new By.ByXPath("//footer[contains(@class,'Date')]//button"));

    private static final Locator DRIVER_LICENSE_ROW = new Locator("DRIVER_LICENSE_ROW",
            new By.ByXPath("//div[contains(@class,'styles_box_detail')][.//span[contains(normalize-space(),'Driver') or contains(normalize-space(),'(SIM)')]]"));
    private static final Locator DRIVER_LICENSE_NUMBER = new Locator("DRIVER_LICENSE_NUMBER",
            new By.ByXPath("//input[@id='driver-license-number' or @id='driving-license-number' or @name='drivingLicense']"));
    private static final Locator DRIVER_LICENSE_NUMBER_DISPLAY = new Locator("DRIVER_LICENSE_NUMBER_DISPLAY",
            new By.ByXPath("//div[contains(@class,'styles_box_detail')][.//span[contains(normalize-space(),'Driver') or contains(normalize-space(),'(SIM)')]]//div[contains(@class,'styles_detail')]//span"));

    private static final Locator FREQUENT_FLYER_SECTION = new Locator("FREQUENT_FLYER_SECTION", By.id("frequent-flyer"));
    private static final Locator FREQUENT_FLYER_ADD_NEW = new Locator("FREQUENT_FLYER_ADD_NEW",
            new By.ByXPath("//div[@id='frequent-flyer']//button[.//span[normalize-space()='Add new' or normalize-space()='Tambah baru']]"));
    private static final Locator FREQUENT_FLYER_AIRLINE = new Locator("FREQUENT_FLYER_AIRLINE",
            new By.ByXPath("//input[@id='frequent-flyer-airline' or @id='airlines' or @id='maskapai']"));
    private static final Locator FREQUENT_FLYER_ENTRIES = new Locator("FREQUENT_FLYER_ENTRIES",
            new By.ByXPath("//div[@id='frequent-flyer']//div[@data-testid='click-edit-frequent-flyer']"));
    private static final Locator FREQUENT_FLYER_EDIT_FORM = new Locator("FREQUENT_FLYER_EDIT_FORM",
            new By.ByXPath("//div[contains(@class,'styles_edit_frequent_flyer')]"));
    private static final Locator FREQUENT_FLYER_DELETE = new Locator("FREQUENT_FLYER_DELETE",
            new By.ByXPath("//div[contains(@class,'styles_edit_frequent_flyer')]//button[contains(@class,'Button_variant_danger')]"));
    private static final Locator FREQUENT_FLYER_DELETE_CONFIRM = new Locator("FREQUENT_FLYER_DELETE_CONFIRM",
            new By.ByXPath("//button[contains(@class,'Button_variant_secondary') and .//span[normalize-space()='Yes, delete' or normalize-space()='Ya, hapus']]"));
    private static final Locator FREQUENT_FLYER_SAVE = new Locator("FREQUENT_FLYER_SAVE",
            new By.ByXPath("//div[contains(@class,'styles_edit_frequent_flyer')]//button[contains(@class,'Button_variant_primary') and .//span[normalize-space()='Save' or normalize-space()='Simpan']]"));
    private static final Locator FREQUENT_FLYER_AIRLINE_FETCH = new Locator("FREQUENT_FLYER_AIRLINE_FETCH",
            new By.ByXPath("(//div[@id='frequent-flyer']//div[@data-testid='click-edit-frequent-flyer'])[1]//span[contains(@class,'Text_weight_bold')]"));
    private static final Locator FREQUENT_FLYER_AIRLINE_LIST = new Locator("FREQUENT_FLYER_AIRLINE_LIST",
            new By.ByXPath("//*[contains(@class,'AirlineListSelection_list_item') or contains(@class,'styles_option')]//*[contains(@class,'select_airline')]"));
    private static final Locator FREQUENT_FLYER_NUMBER = new Locator("FREQUENT_FLYER_NUMBER",
            new By.ByXPath("//input[@id='frequent-flyer-number' or @name='frequentFlyerNumber']"));
    private static final Locator FREQUENT_FLYER_NUMBER_FETCH = new Locator("FREQUENT_FLYER_NUMBER_FETCH",
            new By.ByXPath("(//div[@id='frequent-flyer']//div[@data-testid='click-edit-frequent-flyer'])[1]//div[contains(@class,'styles_detail')]//span[contains(@class,'Text_variant_lowEmphasis')]"));

    @Override
    public void goToSavePassengerData(){
        driverTools.waitForElementToAppear(DONE_BUTTON, 10).ifPresent(driverTools::click);
        driverTools.click(SAVE_PASSENGERS_DATA);
    }

    @Override 
    public void clickAddPassengerData() {
        driverTools.click(ADD_PASSENGER_DATA) ;
    }

    @Override
    public String getRandomCurrency(String currentCurrency) {
        return homePage.getRandomCurrency(currentCurrency);
    }

    @Override
    public String getCurrentCurrency() {
        return homePage.getCurrentCurrency();
    }

    @Override
    public void changeCurrency(String currency) {
        homePage.selectCurrency(currency);
    }

    @Override
    public String getCurrentLanguage() { return homePage.getLanguage(); }

    @Override
    public void changeLanguage(String language) {
        if(language.equals("en")){
            homePage.selectEnglishLanguage();
        } else if(language.equals("id")){
            homePage.selectBahasaLanguage();
        } else {
            throw new IllegalArgumentException("Unsupported language: " + language);
        }
    }

    @Override
    public void addSmartProfile(String firstName, String lastName, String ninCode, String email, String pn)
            throws InterruptedException {
                
        driverTools.waitForElementToPresence(ADD_SMART_PROFILE, 2).ifPresent(driverTools::clickWithJavascriptExecutor);

        driverTools.waitForElementToPresence(ACCOUNT_SALUTATION);
        Thread.sleep(1000);
        driverTools.clickWithJavascriptExecutor(ACCOUNT_SALUTATION);
        selectSex(SEX_MATCHING_DEFAULT_SALUTATION);
        driverTools.sendKeys(ACCOUNT_FIRST_NAME, firstName);
        Thread.sleep(1000);
        driverTools.sendKeys(ACCOUNT_LAST_NAME, lastName);
        driverTools.clickWithJavascriptExecutor(DATE);
        Thread.sleep(1000);
        driverTools.clickWithJavascriptExecutor(SAVE_DATE);
        driverTools.clickWithJavascriptExecutor(ACCOUNT_NATIONALITY_CODE);
        Thread.sleep(1000);
        driverTools.clickWithJavascriptExecutor(SELECT_NATIONALITY);
        Thread.sleep(1000);
        driverTools.clickWithJavascriptExecutor(NIN_CODE);
        driverTools.sendKeys(NIN_CODE, ninCode);
        Thread.sleep(1000);
        driverTools.sendKeys(ACCOUNT_EMAIL, "abc" + email);
        Thread.sleep(1000);
        driverTools.click(ACCOUNT_PHONE);
        driverTools.sendKeys(ACCOUNT_PHONE, pn);
        Thread.sleep(1000);
        driverTools.scrollIntoWebView(SAVE_PASSENGER);
        driverTools.clickWithJavascriptExecutor(SAVE_PASSENGER);
    }

    @Override
    public void openAccountOwnerProfile() {
        if (driverTools.waitForElementToPresence(ACCOUNT_NATIONALITY_CODE, 3).isEmpty()) {
            driverTools.waitForElementToAppear(ACCOUNT_OWNER_PROFILE, 10).orElseThrow();
            driverTools.clickWithJavascriptExecutor(ACCOUNT_OWNER_PROFILE);
        }
        driverTools.waitForElementToPresence(ACCOUNT_NATIONALITY_CODE, 10);
    }

    @Override
    public void editMainProfileSection(MainProfileData data) {
        data.nationality = selectCountryFromList(NATIONALITY_COUNTRY_TRIGGER, ACCOUNT_NATIONALITY_CODE);
        data.identityNumber = "1234567890123456";
        clearAndTypeReactInput(NIN_CODE, data.identityNumber);

        data.emergencyContactName = CoreFunctions.randomName();
        clearAndTypeReactInput(EMERGENCY_CONTACT, data.emergencyContactName);

        data.emergencyPhoneNumber = "9"+CoreFunctions.generateRandomNumeric(CoreFunctions.generateRandomInt(6, 8));
        clearAndTypeReactInput(EMERGENCY_NUMBER, data.emergencyPhoneNumber);
    }

    @Override
    public void editPassportSection(MainProfileData data) {
        driverTools.scrollIntoWebView(PASSPORT_ROW);
        driverTools.clickWithJavascriptExecutor(PASSPORT_ROW);
        driverTools.waitForElementToPresence(PASSPORT_NUMBER, 10);

        data.passportNumber = CoreFunctions.generateRandomAlphanumeric(CoreFunctions.generateRandomInt(6, 10));
        clearAndTypeReactInput(PASSPORT_NUMBER, data.passportNumber);

        int issueDay = CoreFunctions.generateRandomInt(1, 10);
        int expiryDay = CoreFunctions.generateRandomInt(15, 25);
        pickPassportDate(PASSPORT_ISSUE_DATE, issueDay);
        pickPassportDate(PASSPORT_EXPIRY_DATE, expiryDay);
        data.passportIssueDate = driverTools.getValue(PASSPORT_ISSUE_DATE);
        data.passportExpiryDate = driverTools.getValue(PASSPORT_EXPIRY_DATE);

        data.issuingCountry = selectCountryFromList(PASSPORT_ISSUING_COUNTRY_TRIGGER, PASSPORT_ISSUING_COUNTRY);

        driverTools.waitForElementToAppear(CONTINUE_BUTTON, 10).orElseThrow();
        driverTools.clickWithJavascriptExecutor(CONTINUE_BUTTON);
        driverTools.waitForElementToDisappear(PASSPORT_NUMBER, 5);
    }

    /**
     * Opens the date field and picks the day in the calendar's currently displayed month/year.
     * Previously opened calendars stay in the DOM (hidden), so the day cell and confirm button are
     * resolved among visible elements only; retries once if the field does not reflect the picked day.
     * Nothing is returned: a later pick can still change an earlier field, so callers read every date
     * back only after all picks are done.
     *
     * @throws AssertionError when the field still does not show the picked day after every attempt
     */
    private void pickPassportDate(Locator dateField, int dayOfMonth) {
        String expectedDay = String.format("%02d", dayOfMonth);
        String value = "";
        for (int attempt = 1; attempt <= PASSPORT_DATE_PICK_ATTEMPTS; attempt++) {
            driverTools.waitForElementToPresence(dateField, 10);
            driverTools.scrollIntoWebView(dateField);
            driverTools.clickWithJavascriptExecutor(dateField);
            selectCalendarDay(dayOfMonth);
            driverTools.clickWithJavascriptExecutor(lastVisible(PASSPORT_DATE_CONFIRM));
            value = driverTools.getValue(dateField);
            if (value.startsWith(expectedDay)) {
                return;
            }
        }
        throw new AssertionError("Passport date " + dateField + " should start with day " + expectedDay
                + " after picking it, but shows '" + value + "'");
    }

    private void selectCalendarDay(int dayOfMonth) {
        Locator dayCell = new Locator("CALENDAR_DAY_" + dayOfMonth,
                new By.ByXPath("//*[self::button or self::td or self::div or self::span]"
                        + "[normalize-space()='" + dayOfMonth + "' "
                        + "and not(contains(@class,'outside')) and not(contains(@class,'disabled')) and not(@aria-disabled='true')]"));
        driverTools.clickWithJavascriptExecutor(lastVisible(dayCell));
    }

    /** Last displayed match: the most recently opened calendar is rendered after the hidden ones. */
    private WebElement lastVisible(Locator locator) {
        return driverTools.waitUntil(driver -> driverTools.waitForElementsToAppear(locator, 1).orElse(List.of()).stream()
                        .filter(this::isDisplayedSafely)
                        .reduce((first, second) -> second)
                        .orElse(null), 10)
                .orElseThrow(() -> new IllegalStateException("No visible element for " + locator));
    }

    private boolean isDisplayedSafely(WebElement element) {
        try {
            return driverTools.isElementDisplayed(element);
        } catch (StaleElementReferenceException e) {
            return false;
        }
    }

    @Override
    public void editDriverLicenseSection(MainProfileData data) {
        driverTools.scrollIntoWebView(DRIVER_LICENSE_ROW);
        driverTools.clickWithJavascriptExecutor(DRIVER_LICENSE_ROW);
        driverTools.waitForElementToPresence(DRIVER_LICENSE_NUMBER, 10);

        data.driverLicenseNumber = CoreFunctions.generateRandomNumeric(CoreFunctions.generateRandomInt(14, 16));
        clearAndTypeReactInput(DRIVER_LICENSE_NUMBER, data.driverLicenseNumber);

        driverTools.waitForElementToAppear(CONTINUE_BUTTON, 10).orElseThrow();
        driverTools.clickWithJavascriptExecutor(CONTINUE_BUTTON);
        driverTools.waitForElementToDisappear(DRIVER_LICENSE_NUMBER, 5);
    }

    @Override
    public void clearFrequentFlyerSection() {
        driverTools.scrollIntoWebView(FREQUENT_FLYER_SECTION);
        removeAllFrequentFlyerEntries();
    }

    @Override
    public void addFrequentFlyerSection(MainProfileData data) {
        driverTools.scrollIntoWebView(FREQUENT_FLYER_SECTION);
        driverTools.waitForElementToPresence(FREQUENT_FLYER_ADD_NEW, 10);
        driverTools.clickWithJavascriptExecutor(FREQUENT_FLYER_ADD_NEW);

        driverTools.waitForElementToPresence(FREQUENT_FLYER_AIRLINE, 10);
        driverTools.clickWithJavascriptExecutor(FREQUENT_FLYER_AIRLINE);
        data.airline = clickRandomFromListAndGetText(FREQUENT_FLYER_AIRLINE_LIST);

        data.frequentFlyerNumber = CoreFunctions.generateRandomNumeric(5);
        clearAndTypeReactInput(FREQUENT_FLYER_NUMBER, data.frequentFlyerNumber);

        submitFrequentFlyerForm();
    }

    @Override
    public void saveAccountOwnerProfile() {
        driverTools.scrollIntoWebView(SAVE_PROFILE_BUTTON);
        driverTools.clickWithJavascriptExecutor(SAVE_PROFILE_BUTTON);
        driverTools.waitForElementToAppear(SUCCESS_SNACKBAR, 15);
        driverTools.waitForWebToLoad();
    }

    @Override
    public String getSuccessSnackbarText() {
        return driverTools.waitForElementToAppear(SUCCESS_SNACKBAR, 15)
                .map(driverTools::getText)
                .orElse("");
    }

    @Override
    public MainProfileData getAccountOwnerProfileData() {
        driverTools.refreshWebpage();
        driverTools.waitForWebToLoad();
        openAccountOwnerProfile();

        MainProfileData data = new MainProfileData();
        data.nationality = driverTools.getValue(ACCOUNT_NATIONALITY_CODE);
        data.identityNumber = driverTools.getValue(NIN_CODE);
        data.emergencyContactName = driverTools.getValue(EMERGENCY_CONTACT);
        data.emergencyPhoneNumber = driverTools.getValue(EMERGENCY_NUMBER);

        driverTools.scrollIntoWebView(PASSPORT_ROW);
        driverTools.clickWithJavascriptExecutor(PASSPORT_ROW);
        driverTools.waitForElementToPresence(PASSPORT_NUMBER, 10);
        data.passportNumber = driverTools.getValue(PASSPORT_NUMBER);
        data.passportIssueDate = driverTools.getValue(PASSPORT_ISSUE_DATE);
        data.passportExpiryDate = driverTools.getValue(PASSPORT_EXPIRY_DATE);
        data.issuingCountry = driverTools.getValue(PASSPORT_ISSUING_COUNTRY);

        data.driverLicenseNumber = driverTools.getText(DRIVER_LICENSE_NUMBER_DISPLAY).orElse("");
        if (data.driverLicenseNumber.isBlank()) {
            driverTools.clickWithJavascriptExecutor(DRIVER_LICENSE_ROW);
            driverTools.waitForElementToPresence(DRIVER_LICENSE_NUMBER, 10);
            data.driverLicenseNumber = driverTools.getValue(DRIVER_LICENSE_NUMBER);
        }

        driverTools.scrollIntoWebView(FREQUENT_FLYER_SECTION);
        data.airline = driverTools.getText(FREQUENT_FLYER_AIRLINE_FETCH).orElse("");
        data.frequentFlyerNumber = driverTools.getText(FREQUENT_FLYER_NUMBER_FETCH).orElse("");

        return data;
    }

    private void removeAllFrequentFlyerEntries() {
        for (int attempt = 0; attempt < 10; attempt++) {
            List<WebElement> entries = driverTools.waitForElementsToAppear(FREQUENT_FLYER_ENTRIES, 3).orElse(List.of());
            if (entries.isEmpty()) {
                return;
            }

            driverTools.scrollIntoWebView(entries.get(0));
            driverTools.clickWithJavascriptExecutor(entries.get(0));
            driverTools.waitForElementToAppear(FREQUENT_FLYER_EDIT_FORM, 10).orElseThrow();

            driverTools.scrollIntoWebView(FREQUENT_FLYER_DELETE);
            driverTools.clickWithJavascriptExecutor(FREQUENT_FLYER_DELETE);

            driverTools.waitForElementToAppear(FREQUENT_FLYER_DELETE_CONFIRM, 10).orElseThrow();
            driverTools.clickWithJavascriptExecutor(FREQUENT_FLYER_DELETE_CONFIRM);

            driverTools.waitForElementToDisappear(FREQUENT_FLYER_EDIT_FORM, 10);
        }
    }

    private void submitFrequentFlyerForm() {
        if (driverTools.waitForElementToAppear(FREQUENT_FLYER_SAVE, 3).isPresent()) {
            driverTools.clickWithJavascriptExecutor(FREQUENT_FLYER_SAVE);
        } else {
            driverTools.waitForElementToAppear(CONTINUE_BUTTON, 10).orElseThrow();
            driverTools.clickWithJavascriptExecutor(CONTINUE_BUTTON);
        }
        driverTools.waitForElementToDisappear(FREQUENT_FLYER_NUMBER, 5);
    }

    private String selectCountryFromList(Locator countryTrigger, Locator countryInput) {
        driverTools.scrollIntoWebView(countryTrigger);
        driverTools.clickWithJavascriptExecutor(countryTrigger);
        clickRandomFromList(COUNTRY_LIST_ITEM);
        return driverTools.getValue(countryInput);
    }

    private void clickRandomFromList(Locator listItems) {
        List<WebElement> items = driverTools.waitForElementsToAppear(listItems, 10).orElseThrow();
        int max = Math.min(items.size(), 3);
        WebElement item = items.get(CoreFunctions.generateRandomInt(0, max - 1));
        driverTools.clickWithJavascriptExecutor(item);
    }

    private String clickRandomFromListAndGetText(Locator listItems) {
        List<WebElement> items = driverTools.waitForElementsToAppear(listItems, 10).orElseThrow();
        int max = Math.min(items.size(), 3);
        WebElement item = items.get(CoreFunctions.generateRandomInt(0, max - 1));
        String text = driverTools.getText(item);
        driverTools.clickWithJavascriptExecutor(item);
        return text;
    }
}
