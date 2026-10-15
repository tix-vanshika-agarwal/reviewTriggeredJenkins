package com.tiket.web.page.platform.eiffel.membership;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;
import com.tiket.pojo.member.MainProfileData;
import com.tiket.testbase.Assertion;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;
import com.tiket.model.Locator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.SkipException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AccountPageMWeb<T extends WebDriver> extends AccountPageWeb<T> {

    public AccountPageMWeb(Session<T> session) {
        super(session);
    }

    private static final Locator SAVE_PASSENGER_DATA = new Locator("SAVE_PASSENGER_DATA", new By.ByXPath("//img[@alt='Save Passenger Data' or @alt='Simpan Data Penumpang']"));
    private static final Locator ADD_PASSENGER_DATA = new Locator("ADD_PASSENGER_DATA", new By.ByXPath("//a[text() = 'Add Passenger Data' or text()='Tambah Data Penumpang']"));
    private static final Locator CURRENCY_CODE_ELEMENTS = new Locator("CURRENCY_CODE_ELEMENTS", new By.ByXPath("//div[contains(@class,'CurrencyListSelection_left')]/span"));
    private static final Locator CURRENCY_CLOSE_BUTTON = new Locator("CURRENCY_CLOSE_BUTTON", new By.ByXPath("//button[contains(@class,'BottomSheet_close_button')]"));
    private static final Locator CURRENCY_MENU_MWEB = new Locator("CURRENCY_MENU_MWEB",
            new By.ByXPath("//p[contains(@class,'List_title') and (contains(text(),'Currency') or contains(text(),'Mata Uang'))]/ancestor::div[contains(@class,'settings_menu_item')]"));
    private static final Locator SETTINGS_MENU_ITEMS = new Locator("SETTINGS_MENU_ITEMS", new By.ByXPath("//div[contains(@class,'settings_menu_item')]"));
    private static final Locator CURRENT_CURRENCY_TEXT = new Locator("CURRENT_CURRENCY_TEXT", new By.ByXPath(".//span[contains(@class,'settings_general_menu')]"));
    private static final Locator LANGUAGE_TEXT = new Locator("LANGUAGE_TEXT", new By.ByXPath("(//span[contains(@class,'settings_general_menu')])[1]"));
    private static final Locator BOTTOM_SHEET_BODY = new Locator("BOTTOM_SHEET_BODY", new By.ByXPath("//div[@data-testid='bottom-sheet-body']"));
    private static final Locator LOADING = new Locator("LOADING", new By.ByXPath("//div[@data-testid='loading']"));

    private Locator getLanguageLocator(String language) {
        return new Locator("LANGUAGE_" + language.toUpperCase(), new By.ByXPath(String.format(".//input[@value='\"%s\"']/parent::span", language)));
    }

    @Override
    public void goToSavePassengerData() {
        WebElement save_passenger = driverTools.waitForElementToAppear(SAVE_PASSENGER_DATA).orElseThrow();
        driverTools.scrollIntoWebView(save_passenger);
        driverTools.clickWithJavascriptExecutor(save_passenger);
    }

    @Override
    public void clickAddPassengerData() {
        driverTools.click(ADD_PASSENGER_DATA);
    }

    @Override
    public String getRandomCurrency(String currentCurrency) {
        driverTools.click(CURRENCY_MENU_MWEB);
        driverTools.waitForElementToAppear(CURRENCY_CODE_ELEMENTS).orElseThrow(() -> new SkipException("Seems like currency list is down. Please contact payment team!"));
        List<String> currencies = new ArrayList<>();
        List<WebElement> currencyElements = driverTools.elements(CURRENCY_CODE_ELEMENTS).orElseThrow();
        for (WebElement currencyElement : currencyElements) {
            String currency = driverTools.getText(currencyElement);
            if (!currency.equals(currentCurrency)) {
                currencies.add(currency);
            }
        }
        currencies = currencies.stream().distinct().collect(Collectors.toList());
        currencies.removeIf(currency -> currency.equals(currentCurrency));
        String randomCurrency = "";
        if (!currencies.isEmpty()) {
            randomCurrency = currencies.get(CoreFunctions.generateRandomInt(0, currencies.size() - 1));
        }
        driverTools.click(CURRENCY_CLOSE_BUTTON);
        driverTools.waitForElementToDisappear(currencyElements.get(0));
        return randomCurrency;
    }

    @Override
    public String getCurrentCurrency() {
        if (!isUserInMyAccount()) {
            goToAccountPage();
        }

        driverTools.waitForElementToAppear(SETTINGS_MENU_ITEMS);
        driverTools.scrollElementToCenter(CURRENCY_MENU_MWEB);
        WebElement currencyText = driverTools.element(CURRENT_CURRENCY_TEXT, driverTools.element(CURRENCY_MENU_MWEB).orElseThrow()).orElseThrow();
        return driverTools.getText(currencyText);
    }

    @Override
    public void changeCurrency(String currency) {
        if (!isUserInMyAccount()) {
            goToAccountPage();
        }
        driverTools.scrollElementToCenter(CURRENCY_MENU_MWEB);
        driverTools.click(CURRENCY_MENU_MWEB);
        if (driverTools.waitForElementToAppear(getCurrencyLocator(currency)).isPresent()) {
            driverTools.click(getCurrencyLocator(currency));
        } else {
            Assertion.fail("Currency " + currency + " not found in the list");
        }
        driver.navigate().refresh();
        driverTools.waitForWebToLoad();
    }

    @Override
    public String getCurrentLanguage() {
        if (!isUserInMyAccount()) {
            goToAccountPage();
        }

        driverTools.waitForElementToAppear(LANGUAGE_TEXT);
        driverTools.scrollElementToCenter(LANGUAGE_TEXT);
        final String lang = driverTools.getText(LANGUAGE_TEXT).orElse("");
        if (lang.isEmpty()) {
            Assertion.fail("Language text is empty, please check the locator");
        }
        if (lang.equalsIgnoreCase("English")) {
            return "en";
        } else
            return "id";
    }

    @Override
    public void changeLanguage(String language) {
        if (!isUserInMyAccount()) {
            goToAccountPage();
        }

        driverTools.scrollElementToCenter(LANGUAGE_TEXT);
        driverTools.click(LANGUAGE_TEXT);
        driverTools.waitForElementToAppear(BOTTOM_SHEET_BODY);
        WebElement languageElement = driverTools.element(getLanguageLocator(language), driverTools.element(BOTTOM_SHEET_BODY).orElseThrow()).orElseThrow();
        driverTools.click(languageElement);
        driverTools.waitForElementToAppear(LOADING, 2).ifPresent(el -> driverTools.waitForElementToDisappear(el));
        driverTools.refreshWebpage();
        driverTools.waitForWebToLoad();
    }

    @Override
    public void addSmartProfile(String firstName, String lastName, String ninCode, String email, String pn) throws InterruptedException {
        clickAddPassengerData();
        driverTools.waitForElementToAppear(ACCOUNT_SALUTATION);
        driverTools.clickWithJavascriptExecutor(ACCOUNT_SALUTATION);
        selectSex(SEX_MATCHING_DEFAULT_SALUTATION);
        driverTools.sendKeys(ACCOUNT_FIRST_NAME, firstName);
        driverTools.sendKeys(ACCOUNT_LAST_NAME, lastName);
        driverTools.clickWithJavascriptExecutor(DATE);
        driverTools.clickWithJavascriptExecutor(SAVE_DATE);
        driverTools.clickWithJavascriptExecutor(ACCOUNT_NATIONALITY_CODE);
        driverTools.clickWithJavascriptExecutor(SELECT_NATIONALITY);
        driverTools.clickWithJavascriptExecutor(NIN_CODE);
        driverTools.sendKeys(NIN_CODE, ninCode);
        driverTools.sendKeys(ACCOUNT_EMAIL, "abc" + email);
        driverTools.click(ACCOUNT_PHONE);
        driverTools.sendKeys(ACCOUNT_PHONE, pn);
        driverTools.scrollIntoWebView(SAVE_PASSENGER);
        driverTools.clickWithJavascriptExecutor(SAVE_PASSENGER);
    }

    @Override
    public void openAccountOwnerProfile() {
        throw new NotImplementedException("Edit main profile is implemented for DWEB only");
    }

    @Override
    public void editMainProfileSection(MainProfileData data) {
        throw new NotImplementedException("Edit main profile is implemented for DWEB only");
    }

    @Override
    public void editPassportSection(MainProfileData data) {
        throw new NotImplementedException("Edit main profile is implemented for DWEB only");
    }

    @Override
    public void editDriverLicenseSection(MainProfileData data) {
        throw new NotImplementedException("Edit main profile is implemented for DWEB only");
    }

    @Override
    public void clearFrequentFlyerSection() {
        throw new NotImplementedException("Edit main profile is implemented for DWEB only");
    }

    @Override
    public void addFrequentFlyerSection(MainProfileData data) {
        throw new NotImplementedException("Edit main profile is implemented for DWEB only");
    }

    @Override
    public void saveAccountOwnerProfile() {
        throw new NotImplementedException("Edit main profile is implemented for DWEB only");
    }

    @Override
    public String getSuccessSnackbarText() {
        throw new NotImplementedException("Edit main profile is implemented for DWEB only");
    }

    @Override
    public MainProfileData getAccountOwnerProfileData() {
        throw new NotImplementedException("Edit main profile is implemented for DWEB only");
    }

}
