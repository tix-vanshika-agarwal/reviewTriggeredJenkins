package com.tiket.web.dashboard.page.payment;

import com.mongodb.client.FindIterable;
import com.tiket.dbclient.TiketMongoDBManager;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.bson.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CurrencyListDashboard <T extends WebDriver> extends BasePage<T> {

    private static final Locator FILTER_CURRENCY = new Locator("FILTER_CURRENCY",By.xpath("//input[@placeholder='Currency Filter']"));
    private static final Locator FILTER_STATUS = new Locator("FILTER_STATUS",By.xpath("(//input[contains(@id,'filter-single-dropdown')])[2]"));
    private static final Locator SEARCH_BTN = new Locator("SEARCH_BTN",By.xpath("//button[normalize-space()='Search']"));
    private static final Locator FILTER_GBV = new Locator("FILTER_GBV",By.xpath("(//div[@class='css-dvua67-singleValue'])[3]"));
    private static final Locator CURRENCY_MANAGEMENT = new Locator("CURRENCY_MANAGEMENT", By.xpath("//a[contains(text(),'Currency Management')]"));
    private static final Locator CURRENCY_CONFIG = new Locator("CURRENCY_CONFIG", By.xpath("//span[normalize-space()='Currency Configuration']"));
    private static final Locator FX_RATE_V3 = new Locator("FX_RATE_V3", By.xpath("//a[@href='/admin/payment_v2/currency-config/fx-rate']"));
    private static final Locator CURRENCY_LIST_DASHBOARD = new Locator("CURRENCY_LIST_DASHBOARD", By.xpath("//a[contains(text(),'Multicurrency Configuration')]"));
    private static final Locator MESSAGE_BUTTON = new Locator("MESSAGE_BUTTON",By.xpath("//button[contains(text(),'Message')]"));
    private static final Locator MESSAGE_EN = new Locator("MESSAGE_EN",By.xpath("//textarea[contains(@class , \"message-currency-form-control\")][2]"));
    private static final Locator MESSAGE_ID = new Locator("MESSAGE_ID",By.xpath("//textarea[contains(@class , \"message-currency-form-control\")][1]"));
    private static final Locator MESSAGE_ID_DIV = new Locator("MESSAGE_ID_DIV",By.xpath("//div[contains(@class,\"multiple-currency-message\")][last()-1]//a[1]"));
    private static final Locator MESSAGE_EN_DIV = new Locator("MESSAGE_EN_DIV",By.xpath("//div[contains(@class,\"multiple-currency-message\")][last()-1]//a[2]"));
    private static final Locator ADD_MESSAGE = new Locator("ADD_MESSAGE",By.xpath("//button[contains(text(),'+')]"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON",By.xpath("//button[contains(text(),'Save')]"));
    private static final Locator YES_BUTTON = new Locator("YES_BUTTON",By.xpath("//button[contains(text(),'Yes')]"));
    private static final Locator OK_BUTTON = new Locator("OK_BUTTON",By.xpath("//button[contains(text(),'OK')]"));
    private static final Locator VERTICAL_CURRENCY_BUTTON = new Locator("VERTICAL_CURRENCY_BUTTON",By.xpath("(//div[@data-tag='allowRowEvents']//div[contains(@class, 'label_')])[1]"));
    private static final Locator VERTICAL_CURRENCY_BUTTON_TEXT = new Locator("VERTICAL_CURRENCY_BUTTON_TEXT",By.xpath("//div[contains(@class,\"_TableRow\")][1]//button//a"));
    private static final Locator VERTICAL_CURRENCY_SELECTED = new Locator("VERTICAL_CURRENCY_SELECTED",By.xpath("(//div[contains(@class,\"_TableRow\")][1]//div[contains(@class,\"TableCell\")])[1]//div"));
    private static final Locator SELECTED_CURRENCY_CODE = new Locator("SELECTED_CURRENCY_CODE",By.xpath("//h2[contains(text(), 'Change Vertical Currency')]"));
    private static final Locator SEARCH_CURRENCY_INPUT = new Locator("SEARCH_CURRENCY_INPUT",By.xpath("//input[@id='search-currency-code']"));
    private static final Locator VERTICAL_CURRENCY_MODAL_INPUT = new Locator("VERTICAL_CURRENCY_MODAL_INPUT",By.xpath("//input[@id='filter-single-dropdown-undefined-undefined' and contains(@placeholder, 'status')]"));
    private static final Locator STATUS_CHANGE_TEXT = new Locator("statusChangeText",By.xpath("//div[@id='swal2-html-container']"));
    private static final Locator STATUS_CHANGE_TEXT_INACTIVE_TO_ACTIVE = new Locator("STATUS_CHANGE_TEXT_INACTIVE_TO_ACTIVE",By.xpath("//span[contains(text(), 'change inactive status')]"));
    private static final Locator STATUS_CHANGE_TEXT_OTHERS = new Locator("STATUS_CHANGE_TEXT_OTHERS",By.xpath("//span[contains(text(), 'will be disabled')]"));
    private static final Locator STATUS_CHANGE_MORE_THAN_3_CURRENCIES = new Locator("STATUS_CHANGE_MORE_THAN_3_CURRENCIES",By.xpath("//h1[contains(text(), '3 suggested')]"));
    private static final Locator SUGGESTED_CURRENCY_BUTTON = new Locator("SUGGESTED_CURRENCY_BUTTON",By.xpath("//button[contains(text(),'Suggested Currency')]"));
    private static final Locator SUGGESTED_CURRENCY_TEXT_BOX = new Locator("SUGGESTED_CURRENCY_TEXT_BOX",By.xpath("//input[@id='filter-single-dropdown-undefined-undefined' and contains(@placeholder, 'currencies')]"));
    private static final Locator SUGGESTED_CURRENCY= new Locator("SUGGESTED_CURRENCY",By.xpath("(//div[starts-with(@data-testid, 'dropdown-item-')])[1]"));
    private static final Locator ELEMENT_COUNT_ON_PAGE= new Locator("ELEMENT_COUNT_ON_PAGE",By.xpath("//nav[contains(@class,'Pagination')]//span[2]"));
    private static final Locator USER_CURRENCY_ENABLE_DISABLE_BUTTON = new Locator("USER_CURRENCY_ENABLE_DISABLE_BUTTON",By.xpath("(//input[@type='checkbox']/parent::label)[1]"));
    private static final Locator MODAL_DISABLE_BUTTON = new Locator("MODAL_DISABLE_BUTTON",By.xpath("//button[text()='Yes, disable it']"));
    private static final Locator MODAL_ENABLE_BUTTON = new Locator("MODAL_ENABLE_BUTTON",By.xpath("//button[text()='Yes, enable it']"));
    private static final Locator USER_CURRENCY_ENABLE_MODAL_TEXT = new Locator("USER_CURRENCY_ENABLE_MODAL_TEXT",By.xpath("//div[contains(@id,'swal2-html')]"));
    private static final Locator NO_RECORD_TO_DISPLAY = new Locator("NO_RECORD_TO_DISPLAY",By.xpath("//div[contains(@class,'Table')]//div//div"));
    private static final Locator SUGGESTED_CURRENCY_DELETE_ICON = new Locator("SUGGESTED_CURRENCY_DELETE_ICON", By.xpath("//div[contains(@class,'flex') and contains(@class,'space-x-2')]//button//*[local-name()='svg']"));
    private static final Locator SUGGESTED_CURRENCY_NAME = new Locator("SUGGESTED_CURRENCY_NAME",By.xpath("//div[contains(@class,'flex') and contains(@class,'space-x-2')]//button//span[contains(@class,'text-left')]"));
    private static final Locator CONSUMER_SUGGESTED_CURRENCY_NAME = new Locator("CONSUMER_SUGGESTED_CURRENCY_NAME",By.xpath("//input[@name='currency-radio-suggested']/ancestor::label//span[contains(@class,'currency_code')]"));
    private static final Locator CONFIRMATION_BOX = new Locator("CONFIRMATION_BOX",By.xpath("//h2[contains(@class,'swal2-title')]"));
    private static final Locator CURRENCY_STATUS = new Locator("CURRENCY_STATUS", By.xpath("//div[@data-column-id=\"8\"]//input"));
    private static final Locator VERTICAL_CURRENCY_STATUS = new Locator("VERTICAL_CURRENCY_STATUS", By.xpath("//div[@data-column-id=\"7\"]//button"));
    private static final Locator SUCCESS_TOAST = new Locator("SUCCESS_TOAST", By.xpath("//div[contains(@class,'text_content') and contains(text(),'Successfully')]"));

    private static Locator SUGGESTED_CURRENCY_INPUT_CURRENCY(String currency){
        return new Locator("SUGGESTED_CURRENCY_INPUT_CURRENCY", By.xpath(String.format("(//p[contains(text(), '%s')])[last()]", currency)));
    }

    public void waitForSuccessToastToDisappear(){
        driverTools.waitForElementToAppear(SUCCESS_TOAST, 10);
        driverTools.waitForElementToDisappear(SUCCESS_TOAST, 10);
    }

    private Locator FILTER_TEXT(String entry){
        return new Locator("FILTER_TEXT",By.xpath(String.format("//div[contains(@class,'styles_dropdown_option')]//p[normalize-space()='%s']",entry)));
    }

    public CurrencyListDashboard(Session<T> session) {
        super(session);
    }

    public boolean isDashboardLoggedin() {
        driverTools.waitForElementToAppear(CURRENCY_LIST_DASHBOARD, 10);
        return driverTools.isElementDisplayed(CURRENCY_LIST_DASHBOARD);
    }

    public void goToCurrencyList() {
        driverTools.maximizeWindow();
        driverTools.waitForElementToAppear(CURRENCY_CONFIG, 10).ifPresent(driverTools::click);
        driverTools.waitForElementToAppear(CURRENCY_MANAGEMENT, 10).ifPresent(driverTools::click);
    }

    public String getElementText(Locator locator) {
        driverTools.waitForElementToAppear(locator, 10);
        return driverTools.getText(locator).orElseThrow();
    }

    public void clickOnFilterCurrency() {
        driverTools.waitForElementToAppear(FILTER_CURRENCY, 10).ifPresent(driverTools::click);
    }

    public void clickOnFilterStatus() {
        driverTools.waitForElementToAppear(FILTER_STATUS, 10).ifPresent(driverTools::click);
    }

    public void changeFilterStatus(String entry) {
        driverTools.waitForElementToAppear(FILTER_TEXT(entry), 10).ifPresent(driverTools::click);
    }

    public void clickOnFilterStatusInactive(String entry) {
        driverTools.waitForElementToAppear(FILTER_TEXT(entry), 10).ifPresent(driverTools::click);
    }

    public void clickOnFilterStatusAll(String entry) {
        driverTools.waitForElementToAppear(FILTER_TEXT(entry), 10).ifPresent(driverTools::click);
    }

    public void clickOnSearch() {
        driverTools.waitForElementToAppear(SEARCH_BTN, 10).ifPresent(driverTools::click);
    }

    public void clickOnFilterGbv() {
        driverTools.waitForElementToAppear(FILTER_GBV, 10).ifPresent(driverTools::click);
    }

    public void clickOnFilterGbvAll(String entry) {
        driverTools.waitForElementToAppear(FILTER_TEXT(entry), 10).ifPresent(driverTools::click);
    }

    public void clickOnFilterGbvID(String entry) {
        driverTools.waitForElementToAppear(FILTER_TEXT(entry), 10).ifPresent(driverTools::click);
    }

    public void clickOnFilterGbvSG(String entry) {
        driverTools.waitForElementToAppear(FILTER_TEXT(entry), 10).ifPresent(driverTools::click);
    }

    public void clickOnUserCurrency(String entry) {
        driverTools.waitForElementToAppear(FILTER_TEXT(entry), 10).ifPresent(driverTools::click);
    }

    public void clickOnVerticalCurrency(String entry) {
        driverTools.waitForElementToAppear(FILTER_TEXT(entry), 10).ifPresent(driverTools::click);
    }

    public String getFilterText() {
        return getElementText(FILTER_CURRENCY);
    }

    public String getFilterStatusText(String expected) {
        return getElementText(FILTER_STATUS);
    }

    public String getCurrencyStatusInFilter() {
        String currencyStatus = driverTools.element(FILTER_STATUS).get().getAttribute("value");
        logger.log("Currency status: [" + currencyStatus + "]");
        return currencyStatus;
    }

    public String getFilterGBVText(String expected) {
        return getElementText(FILTER_GBV);
    }

    public void openMessage(){
        driverTools.waitForElementToAppear(MESSAGE_BUTTON, 10).ifPresent(driverTools::click);
    }

    public void inputMessage(String IDmessage , String ENmessage){
        WebElement messageID = driverTools.waitForElementToAppear(MESSAGE_ID).orElseThrow();
        driverTools.sendKeys(messageID,IDmessage);
        WebElement messageEN = driverTools.waitForElementToAppear(MESSAGE_EN).orElseThrow();
        driverTools.sendKeys(messageEN,ENmessage);
        driverTools.waitForElementToAppear(ADD_MESSAGE, 10).ifPresent(driverTools::click);
    }

    public void clickMessageModal(){
        driverTools.waitForElementToAppear(SAVE_BUTTON, 10).ifPresent(driverTools::click);
        driverTools.waitForElementToAppear(YES_BUTTON, 10).ifPresent(driverTools::click);
        driverTools.waitForElementToAppear(OK_BUTTON, 10).ifPresent(driverTools::click);
    }

    public String getTextID(){return getElementText(MESSAGE_ID_DIV);}

    public String getTextEN(){return getElementText(MESSAGE_EN_DIV);}

    public String getVerticalCurrencySelected(){
        driverTools.waitForElementToAppear(VERTICAL_CURRENCY_SELECTED);
        return getElementText(VERTICAL_CURRENCY_SELECTED);
    }

    public String getSelectedVerticalCurrencyCode(){
        driverTools.waitForElementToAppear(SELECTED_CURRENCY_CODE);
        String currencySelected = driverTools.element(SELECTED_CURRENCY_CODE).get().getAttribute("textContent").trim();
        String currency = CoreFunctions.getSectionOfString(currencySelected, " ", currencySelected.split(" ").length - 1);
        logger.log("Currency Selected: [" + currency + "]");
        return currency;
    }

    public String getVerticalCurrencyButtonText(){return getElementText(VERTICAL_CURRENCY_BUTTON_TEXT);}

    public void verticalCurrencyButtonInput(){
        driverTools.waitForElementToAppear(VERTICAL_CURRENCY_MODAL_INPUT, 10).ifPresent(driverTools::click);
    }

    public void searchCurrencyInput(String inputText) {
        WebElement currencyInput = driverTools.waitForElementToBeClickable(SEARCH_CURRENCY_INPUT, 10)
                .orElseGet(() -> driverTools.waitForElementToAppear(SEARCH_CURRENCY_INPUT, 10).orElseThrow());
        driverTools.scrollElementToCenter(currencyInput);
        try {
            driverTools.click(currencyInput);
        } catch (org.openqa.selenium.ElementClickInterceptedException e) {
            driverTools.clickWithJavascriptExecutor(currencyInput);
        }
        driverTools.clearInput(currencyInput);
        driverTools.sendKeys(currencyInput, inputText);
        currencyInput.sendKeys(Keys.ENTER);
    }

    public void verticalCurrencyButton(){
        driverTools.waitForElementToAppear(VERTICAL_CURRENCY_BUTTON, 10).ifPresent(driverTools::click);
    }

    public String getSelectedVerticalCurrencyText() {
        driverTools.waitForElementToAppear(VERTICAL_CURRENCY_BUTTON, 5).orElseThrow();
        String selectedCurrencyStatus = driverTools.element(VERTICAL_CURRENCY_BUTTON).get().getAttribute("textContent").trim();
        String trimmedSelectedCurrencyStatus = CoreFunctions.getSectionOfString(selectedCurrencyStatus, " ", 0);
        logger.log("Selected currency status: [" + trimmedSelectedCurrencyStatus + "]");
        return trimmedSelectedCurrencyStatus;
    }

    public void verticalCurrencyButtonInputValue(String entry){
        driverTools.waitForElementToAppear(FILTER_TEXT(entry), 10).ifPresent(driverTools::click);
    }

    public void clickSave(){
        driverTools.waitForElementToAppear(SAVE_BUTTON, 10).ifPresent(driverTools::click);
    }

    public void clickYesandwaitForVerificationMessage() throws InterruptedException{
        driverTools.waitForElementToAppear(YES_BUTTON, 10).ifPresent(driverTools::click);
        Thread.sleep(1000);
    }

    public String getStatusChangeText(){
        return driverTools.getText(STATUS_CHANGE_TEXT).orElseThrow();

    }

    private static final String SUPPORTED_CASE_NAMES =
            "[inactiveToActive, inactiveToReview, reviewToInactive, "
                    + "activeToReview, reviewToActive, suggestedMoreThan3Currencies]";

    public String getStatusChangeText(String caseName) {
        if (caseName == null || caseName.isBlank()) {
            throw new IllegalArgumentException(
                    "caseName must not be null or blank. Supported values: " + SUPPORTED_CASE_NAMES);
        }

        Locator targetLocator = switch (caseName) {
            case "inactiveToActive"   -> STATUS_CHANGE_TEXT_INACTIVE_TO_ACTIVE;
            case "inactiveToReview", "reviewToInactive", "activeToReview", "reviewToActive" -> STATUS_CHANGE_TEXT_OTHERS;
            case "suggestedMoreThan3Currencies" -> STATUS_CHANGE_MORE_THAN_3_CURRENCIES;
            default -> throw new IllegalArgumentException(
                    "Invalid case name: '" + caseName + "'. Supported values: " + SUPPORTED_CASE_NAMES);
        };

        driverTools.waitForElementToAppear(targetLocator).orElseThrow();
        String actualMessage = driverTools.element(targetLocator).get().getAttribute("textContent");
        logger.log(caseName + " message: [" + actualMessage + "]");

        return actualMessage != null ? actualMessage : "";
    }

    public void clickOnOkButton(){
        driverTools.waitForElementToAppear(OK_BUTTON, 10).ifPresent(driverTools::click);
    }
    public void suggestedCurrencyButton(){
        driverTools.waitForElementToAppear(SUGGESTED_CURRENCY_BUTTON, 10).ifPresent(driverTools::click);
    }

    public void suggestedCurrencyTextBox(){
        driverTools.waitForElementToAppear(SUGGESTED_CURRENCY_TEXT_BOX, 10).ifPresent(driverTools::click);
    }

    public void suggestedCurrency(){
        driverTools.waitForElementToAppear(SUGGESTED_CURRENCY,10).ifPresent(driverTools::click);
    }

    public int getNumberOfCurrencyMongoDB(String configType, String dataBase, String collectionName, String queryString) {
        FindIterable<Document> result = TiketMongoDBManager.searchInMongoDB(configType, dataBase, collectionName, queryString);
        int count = 0;
        for (Document document : result) {
            count++;
        }
        return count;
    }

    public boolean getOverrideValue(String configType, String dataBase, String collectionName, String queryString) {
        FindIterable<Document> result = TiketMongoDBManager.searchInMongoDB(configType, dataBase, collectionName, queryString);
        boolean nullValue = false;
        for (Document document : result) {
            if(document.get("overrideValue")==null){
                nullValue = true;
                break;
            }
        }
        return nullValue;
    }

    public double getThresholdOverrideValue(String configType, String dataBase, String collectionName, String queryString) {
        FindIterable<Document> result = TiketMongoDBManager.searchInMongoDB(configType, dataBase, collectionName, queryString);
        double overrideValue = 0.0;
        for (Document document : result) {
            overrideValue = document.getDouble("overrideValue");
        }
        return overrideValue;
    }

    public int getNumberOfCurrencyOnPanelSide() throws InterruptedException{
        driverTools.waitForElementToAppear(ELEMENT_COUNT_ON_PAGE);
        WebElement element = driverTools.element(ELEMENT_COUNT_ON_PAGE).orElseThrow();
        String text = element.getText();
        String[] StringParts = text.split(" ");
        String numberText = StringParts[StringParts.length - 1];
        int number = Integer.parseInt(numberText);
        return number;
    }

    public boolean isNoRecordDisplayed() throws InterruptedException{
        driverTools.waitForElementToAppear(ELEMENT_COUNT_ON_PAGE,10);
        driverTools.waitForElementToAppear(NO_RECORD_TO_DISPLAY);
        String elementText = driverTools.element(NO_RECORD_TO_DISPLAY).get().getText();
        if(elementText.equals("There are no records to display")){

            return true;
        }
        return false;
    }

    public void clickOnUserCurrencyEnableDisableButton(){
        WebElement toggle = driverTools.waitForElementToBeClickable(USER_CURRENCY_ENABLE_DISABLE_BUTTON, 10)
                .orElseGet(() -> driverTools.waitForElementToAppear(USER_CURRENCY_ENABLE_DISABLE_BUTTON, 10).orElseThrow());
        driverTools.scrollElementToCenter(toggle);
        try {
            driverTools.click(toggle);
        } catch (org.openqa.selenium.ElementClickInterceptedException e) {
            driverTools.clickWithJavascriptExecutor(toggle);
        }
    }

    public void clickOnUserCurrencyDisableButton() throws InterruptedException{
        driverTools.waitForElementToAppear(MODAL_DISABLE_BUTTON, 10).ifPresent(driverTools::click);
        driverTools.waitForElementToDisappear(CONFIRMATION_BOX, 10);
    }

    public void clickOnUserCurrencyEnableButton(){
        driverTools.waitForElementToAppear(MODAL_ENABLE_BUTTON, 10).ifPresent(driverTools::click);
        driverTools.waitForElementToDisappear(CONFIRMATION_BOX, 10);
    }

    public String getUserCurrencyEnableModalText(){
        return driverTools.element(USER_CURRENCY_ENABLE_MODAL_TEXT).get().getText();
    }

    public List<String> getSuggestedCurrencyNames() {
        List<WebElement> suggestedCurrencies = driverTools.elements(SUGGESTED_CURRENCY_NAME).orElseThrow();
        List<String> currencyNames = new ArrayList<>();
        for (WebElement currency : suggestedCurrencies) {
            currencyNames.add(driverTools.getText(currency));
        }
        Collections.sort(currencyNames);
        return currencyNames;
    }

    public List<String> getConsumerSuggestedCurrencyNames() {
        driverTools.waitForElementToAppear(CONSUMER_SUGGESTED_CURRENCY_NAME, 10);
        List<WebElement> suggestedCurrencies = driverTools.elements(CONSUMER_SUGGESTED_CURRENCY_NAME).orElseThrow();
        List<String> currencyNames = new ArrayList<>();
        for (WebElement currency : suggestedCurrencies) {
            currencyNames.add(currency.getAttribute("textContent").trim());
        }
        Collections.sort(currencyNames);
        return currencyNames;
    }

    public String getFirstMissingCurrency() {
        List<String> suggestedCurrencies = getSuggestedCurrencyNames();
        String[] SuggestedArray = {"IDR", "USD", "SGD"};
        for (String currency : SuggestedArray) {
            if (!suggestedCurrencies.contains(currency)) {
                return currency;
            }
        }
        return null;
    }

    public void addSuggestedCurrency(){
        String currency = getFirstMissingCurrency();
        driverTools.waitForElementToAppear(SUGGESTED_CURRENCY_INPUT_CURRENCY(currency), 10).ifPresent(driverTools::click);
    }

    public void clickYes(){
        driverTools.waitForElementToAppear(YES_BUTTON, 10).ifPresent(driverTools::click);
    }

    public List<String> getSuggestedCurrencyFromMongoDB(String configType, String dataBase, String collectionName, String queryString) {
        FindIterable<Document> result = TiketMongoDBManager.searchInMongoDB(configType, dataBase, collectionName, queryString);
        List<String> currencyCodes = new ArrayList<>();
        for (Document document : result) {
            String code = document.getString("code");
            if (code != null) {
                currencyCodes.add(code);
            }
        }
        Collections.sort(currencyCodes);
        return currencyCodes;
    }


    public void deleteSuggestedCurrency(){
        List<WebElement> deleteIcons = driverTools.elements(SUGGESTED_CURRENCY_DELETE_ICON).orElseThrow();
        if (!deleteIcons.isEmpty()) {
            WebElement firstIcon = deleteIcons.get(0);
            driverTools.waitForElementToAppear(firstIcon);
            driverTools.clickSvgOrNonHtmlElements(firstIcon);
            clickYes();
            driverTools.waitForElementToDisappear(CONFIRMATION_BOX);
            driverTools.waitForElementToDisappear(firstIcon);
        }
    }

    public void ensureThreeSuggestedCurrencies() {
        List<String> current = getSuggestedCurrencyNames();
        int count = current.size();
        while (count > 0) {
            deleteSuggestedCurrency();
            count--;
        }
        while (count < 3) {
            suggestedCurrencyTextBox();
            addSuggestedCurrency();
            clickYes();
            waitForDisappearingVerificationMessage();
            waitForSuccessToastToDisappear();
            count++;
        }
    }

    public void waitForDisappearingVerificationMessage(){
        driverTools.waitForElementToDisappear(CONFIRMATION_BOX);
    }
    public boolean getCurrencyStatus(){
        WebElement checkbox = driverTools.waitForElementToPresence(CURRENCY_STATUS, 10)
                .orElseGet(() -> driverTools.element(CURRENCY_STATUS).orElseThrow());
        try {
            return checkbox.isSelected();
        } catch (Exception ignored) {
            String ariaChecked = checkbox.getAttribute("aria-checked");
            if ("true".equalsIgnoreCase(ariaChecked)) return true;

            String checkedAttr = checkbox.getAttribute("checked");
            return checkedAttr != null && !checkedAttr.isEmpty();
        }
    }

    public void waitForCurrencyStatus(boolean expected, int seconds) {
        long deadline = System.currentTimeMillis() + (seconds * 1000L);
        boolean last;
        while (System.currentTimeMillis() < deadline) {
            last = getCurrencyStatus();
            if (last == expected) return;
            driverTools.dummyWait(1);
        }
    }

    public boolean getVerticalCurrencyStatus(){
        if(driverTools.getValueAttribute(VERTICAL_CURRENCY_STATUS, "class").contains("inactive") || driverTools.getValueAttribute(VERTICAL_CURRENCY_STATUS, "class").contains("review")){
            return true;
        }
        return false;
    }
}